package com.mdmd.service.impl;

import com.mdmd.Manager.RedisCacheManager;
import com.mdmd.controller.GameAction;
import com.mdmd.dao.*;
import com.mdmd.entity.*;
import com.mdmd.entity.JO.*;
import com.mdmd.service.DataService;
import com.mdmd.util.WeiXinMenuUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.mdmd.constant.SystemConstant.DATEFORMAT__yyyyMMddHHmmss;
import static com.mdmd.constant.SystemConstant.ENTITYPATH;


@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private QrcodeDao qrcodeDao;

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserCommissionDao userCommissionDao;
    @Autowired
    private CommissionDao commissionDao;
    @Autowired
    private CommonDao commonDao;
    @Autowired
    private TakeoutDao takeoutDao;

    @Autowired
    private RedisCacheManager redisCacheManager;
    private static final Logger LOGGER = LogManager.getLogger(DataServiceImpl.class);


    public  void initCache() {
        try {
            redisCacheManager.clear();
            LOGGER.info("初始化缓存");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("初始化缓存失败,终止服务!");
            LOGGER.error(e.getMessage());
            System.exit(0);
        }
    }

    @Scheduled(cron = "30 59 23 ? * *")
    public void calcYesterdayCommissionList_Schedule() {
       //todo 每日自动计算昨天排行榜放入缓存中
       LOGGER.info("执行缓存" + new SimpleDateFormat(DATEFORMAT__yyyyMMddHHmmss).format(new Date()));
        List<RankingListJO> rankingListJOS = this.calcYesterdayCommissionRankingList();
        //设置消亡时间为1天10分钟
        redisCacheManager.lSet("commYes",rankingListJOS,87000);
    }

    public List<RankingListJO> calcYesterdayCommissionRankingList() {
        List<Object[]> commissionEntities = commissionDao.listTopCommissionFromCommission_limit(50);
        List<RankingListJO> rankingListJOS = new ArrayList<>();
        for (int i = 0; i < commissionEntities.size(); i++) {
            Object[] objects = commissionEntities.get(i);
            RankingListJO rankingListJO = new RankingListJO();
            rankingListJO.setRanking(i+1);
            rankingListJO.setUserId((Integer) objects[0]);
            rankingListJO.setCommission((Double) objects[1]);
            rankingListJOS.add(rankingListJO);
        }
        return rankingListJOS;
    }

    public List<RankingListJO> getYesterdayCommissionRankingList() {
        Object commYes = redisCacheManager.lGet("commYes",0,-1);
        if(commYes == null || commYes.equals("null")) {
            List<Object[]> userCommissionEntities = userCommissionDao.listTopCommissionFromUserCommission_limit(50);
            List<RankingListJO> rankingListJOS = new ArrayList<>();
            for (int i = 0; i < userCommissionEntities.size(); i++) {
                Object[] objects = userCommissionEntities.get(i);
                RankingListJO rankingListJO = new RankingListJO();
                rankingListJO.setRanking(i+1);
                rankingListJO.setUserId((Integer) objects[0]);
                rankingListJO.setCommission((Double) objects[1]);
                rankingListJOS.add(rankingListJO);
            }
            //计算剩余时间(给一分钟缓冲时间，确保能到第二天。理论上在这天的23.59.30会被定时任务修改)
            Calendar calendar = Calendar.getInstance();
            int sur = 86400 - 60 + calendar.get(Calendar.HOUR_OF_DAY) * 3600 + calendar.get(Calendar.MINUTE) * 60 + calendar.get(Calendar.SECOND);

            redisCacheManager.lSet("commYes",rankingListJOS,sur);
            return rankingListJOS;
        }
        return (List<RankingListJO>) commYes;
    }


    public List listDatas(int type,int userId) throws ClassNotFoundException {
        String classPath = ENTITYPATH;
        String className = "";
        if(type == 1)
        {
            className = "GameRecordEntity";
        }
        else if(type == 2)
        {
            className = "UserTopupEntity";
        }
        else if(type == 3)
        {
            className = "UserTakeoutEntity";
        }
        else if(type == 4)
        {
            className = "UserCommissionEntity";
        }
        classPath += className;
        String keyName = className.replace("Entity","JO") + userId;
        List<Object> list;
        if( redisCacheManager.hasKey(keyName))
        {
            list = redisCacheManager.lGet(keyName,0,-1);
        }
        else
        {
            //todo 防止多次缓存对数据库的冲击 应在list为null时对缓存进行标识set，在取到为标识时应让线程等待
            list = new LinkedList<>();

            if(type == 1)
            {
                List listDatas = userDao.listForeignWithUserEntity_desc_limit(Class.forName(classPath), 12, userId);
                if(listDatas.size() == 0)
                    return list;
                for (int i = 0; i < listDatas.size(); i++) {
                    GameRecordEntity o = (GameRecordEntity) listDatas.get(i);
                    list.add(new GameRecordJO(o));
                }
            }
            else if(type == 2)
            {
                List listDatas = userDao.listForeignWithUserEntity_desc_limit(Class.forName(classPath), 12, userId);
                if(listDatas.size() == 0)
                    return list;
                for (int i = 0; i < listDatas.size(); i++) {
                    UserTopupEntity o = (UserTopupEntity) listDatas.get(i);
                    list.add(new UserTopupJO(o));
                }
            }
            else if(type == 3)
            {
                List<UserTakeoutEntity> listDatas = takeoutDao.listUserTakeoutDatas( userId,12);
                if(listDatas.size() == 0)
                    return list;
                for (int i = 0; i < listDatas.size(); i++) {
                    UserTakeoutEntity o =  listDatas.get(i);
                    list.add(new UserTakeoutJO(o));
                }
            }
            else if(type == 4)
            {
                List listDatas = userDao.listForeignWithUserEntity_desc_limit(Class.forName(classPath), 12, userId);
                if(listDatas.size() == 0)
                    return list;
                for (int i = 0; i < listDatas.size(); i++) {
                    UserCommissionEntity o = (UserCommissionEntity) listDatas.get(i);
                    list.add(new UserCommissionJO(o));
                }
            }
            //个人数据缓存10分钟
            redisCacheManager.lSet(keyName,list,1000);
        }

        return list;
    }



    public List getAllChilds(int userId) {
        return null;
    }


    public String getQRCodeTicketWithUserId(int userId) throws Exception{
        QrcodeEntity qrcodeEntity = qrcodeDao.getQrcodeEntityFromUserId(userId);
        String ticket;
        int sceneId = userId+90000;
        //如果为空就重新计算ticket
        if(qrcodeEntity == null)
        {
            //获取永久二维码 “2”
            ticket = WeiXinMenuUtil.getWXPublicQRCodeTicket("2", sceneId);
            QrcodeEntity newQrcode = new QrcodeEntity();
            newQrcode.setTicket(ticket);
            newQrcode.setUserId(userId);
            newQrcode.setSceneId(sceneId);
            qrcodeDao.addQrcodeEntity(newQrcode);
        }
        else
        {
            ticket = qrcodeEntity.getTicket();
            if(ticket == null)
            {
                ticket = WeiXinMenuUtil.getWXPublicQRCodeTicket("2", sceneId);
                qrcodeEntity.setTicket(ticket);
                qrcodeEntity.setSceneId(sceneId);
                qrcodeDao.updateQrcodeEntity(qrcodeEntity);
            }
        }
        return ticket;
    }

    public UserEntity getUserWithQrcodeSceneId(int sceneId) throws Exception{
        if(sceneId < 0 || sceneId > 100000){
            return null;
        }
        QrcodeEntity qrcodeEntity = qrcodeDao.getQrcodeEntityFromSceneId(sceneId);
        if(qrcodeEntity == null)
            return null;
        UserEntity userEntity = userDao.getUserFromUserId_only(qrcodeEntity.getUserId());
        return userEntity;
    }
}
