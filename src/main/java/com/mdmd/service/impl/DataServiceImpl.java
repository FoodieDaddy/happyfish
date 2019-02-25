package com.mdmd.service.impl;

import com.mdmd.Manager.RedisCacheManager;
import com.mdmd.controller.GameAction;
import com.mdmd.dao.QrcodeDao;
import com.mdmd.dao.UserDao;
import com.mdmd.entity.GameRecordEntity;
import com.mdmd.entity.QrcodeEntity;
import com.mdmd.entity.UserEntity;
import com.mdmd.service.DataService;
import com.mdmd.util.WeiXinMenuUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.mdmd.constant.SystemConstant.DATEFORMAT__yyyyMMddHHmmss;
import static com.mdmd.constant.SystemConstant.ENTITYPATH;


@Component
public class DataServiceImpl implements DataService {

    @Autowired
    private QrcodeDao qrcodeDao;

    @Autowired
    private UserDao userDao;


    @Autowired
    private RedisCacheManager redisCacheManager;
    private static final Logger LOGGER = LogManager.getLogger(DataServiceImpl.class);


    public  void initCache() {
        try {
            redisCacheManager.clear();
            LOGGER.info("初始化缓存");
            //存储所有用户的userId和super的userId到redis中 缓存无限时间
            List<UserEntity> userEntities = userDao.listAllEntity(UserEntity.class);
            for (int i = 0; i < userEntities.size(); i++) {
                UserEntity userEntity = userEntities.get(i);
                Integer superUserId = userEntity.getSuperUserId();
                redisCacheManager.set("super"+userEntity.getUserid(),superUserId+"");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("读取系统配置文件失败,终止服务!");
            LOGGER.error(e.getMessage());
            System.exit(0);
        }
    }

    @Scheduled(cron = "0 59 23 ? * *")
    public void calcYesterdayCommissionList_Schedule() {
       //todo 每日自动计算昨天排行榜放入缓存中
       LOGGER.info("执行缓存" + new SimpleDateFormat(DATEFORMAT__yyyyMMddHHmmss).format(new Date()));
    }

    public List<UserEntity> get4_SuperUserEntity(int userId) throws RuntimeException{
        List<UserEntity> superUserEntity = getSuperUserEntity(userId, new ArrayList<UserEntity>());
        return superUserEntity;
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
        List list = redisCacheManager.lGet(className + userId,0,-1);
        if(list == null)
        {
            //todo 防止多次缓存对数据库的冲击 应在list为null时对缓存进行标识set，在取到为标识时应让线程等待
            list = userDao.listForeignWithUserEntity_desc_limit(Class.forName(classPath), 12, userId);
            //个人数据缓存10分钟
            redisCacheManager.lSet(className + userId,list,600);
        }

        return list;
    }


    private List<UserEntity> getSuperUserEntity(int userId,List<UserEntity> userEntityList) throws RuntimeException{
        Object o =  redisCacheManager.get("super" + userId);

        if(o == null || o.equals("null"))
        {
            UserEntity user = (UserEntity) userDao.getEntity(UserEntity.class,userId);
            if(user == null)
            {
                LOGGER.error("不存在的用户id："+userId);
                throw new RuntimeException();
            }
            Integer superUserId = user.getSuperUserId();
            if(superUserId == null)
            {
                return userEntityList;
            }
            if(superUserId == 0)
            {
                return userEntityList;
            }
                redisCacheManager.set("super"+userId,superUserId);

            UserEntity superUser = (UserEntity) userDao.getEntity(UserEntity.class,superUserId);
            if(superUser == null)
            {
                LOGGER.error("用户"+userId+"不存在的父类用户id："+superUserId);
                return userEntityList;
            }
            userEntityList.add(superUser);
            if(userEntityList.size() < 4)
                return getSuperUserEntity(superUserId,userEntityList);
            else
                return userEntityList;
        }
        int superUserId = Integer.valueOf(o.toString());
        if(superUserId == 0)
        {
            return userEntityList;
        }
        UserEntity superUser = (UserEntity) userDao.getEntity(UserEntity.class,superUserId);
        if(superUser == null)
        {
            LOGGER.error("用户"+userId+"不存在的父类用户id："+superUserId);
            return userEntityList;
        }
        userEntityList.add(superUser);
        if(userEntityList.size() < 4)
            return getSuperUserEntity(superUserId,userEntityList);
        else
            return userEntityList;

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
