package com.mdmd.service.impl;

import com.mdmd.Manager.RedisCacheManager;
import com.mdmd.controller.GameAction;
import com.mdmd.dao.QrcodeDao;
import com.mdmd.dao.UserDao;
import com.mdmd.entity.*;
import com.mdmd.entity.JO.GameRecordJO;
import com.mdmd.entity.JO.UserCommissionJO;
import com.mdmd.entity.JO.UserTakeoutJO;
import com.mdmd.entity.JO.UserTopupJO;
import com.mdmd.service.DataService;
import com.mdmd.util.WeiXinMenuUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

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
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("初始化缓存失败,终止服务!");
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
        Object redisVal = redisCacheManager.get("super" + userId);
        List<UserEntity> superUserEntity;
        if(redisVal == null || redisVal.equals("null"))
        {
            superUserEntity = getSuperUserEntity(userId, new ArrayList<UserEntity>());
            int size = superUserEntity.size();
            String val = "";
            if(size > 0)
            {
                for (int i = 0; i < superUserEntity.size(); i++) {
                    UserEntity userEntity = superUserEntity.get(i);
                    val += userEntity.getUserid() + ",";
                }
            }
            else
            {
                val = "0";
            }
            redisCacheManager.set("super"+userId, val, 300);
        }
        else
        {
            superUserEntity = new ArrayList<>();
            String v = redisVal.toString();
            if(v.equals("0"))
                return superUserEntity;
            else
            {
                String substring = v.substring(0, v.length() - 1);
                if(substring.contains(","))
                {
                    String[] split = substring.split(",");
                    for (int i = 0; i < split.length; i++) {
                        UserEntity entity = (UserEntity) userDao.getEntity(UserEntity.class, Integer.valueOf(split[i]));
                        superUserEntity.add(entity);
                    }
                }
                else
                {
                    UserEntity entity = (UserEntity) userDao.getEntity(UserEntity.class, Integer.valueOf(substring));
                    superUserEntity.add(entity);
                }

            }
        }
        return superUserEntity;
    }

    /**
     * 递归寻找所有父类
     * @param userId
     * @param userEntityList
     * @return
     * @throws RuntimeException
     */
    private List<UserEntity> getSuperUserEntity(int userId,List<UserEntity> userEntityList) throws RuntimeException{

        UserEntity user = (UserEntity) userDao.getEntity(UserEntity.class,userId);
        if(user == null)
        {
            LOGGER.error("不存在的用户id："+userId);
            return userEntityList;
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

        UserEntity superUser = (UserEntity) userDao.getEntity(UserEntity.class,superUserId);
        if(superUser == null)
        {
            LOGGER.error("用户"+userId+"不存在的父类用户id："+superUserId);
            return userEntityList;
        }
        userEntityList.add(superUser);
        if(userEntityList.size() == 5)
            return getSuperUserEntity(superUserId,userEntityList);
        else
            return userEntityList;

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
            List listDatas = userDao.listForeignWithUserEntity_desc_limit(Class.forName(classPath), 12, userId);
            list = new LinkedList<>();
            if(listDatas.size() == 0)
                return list;
            if(type == 1)
            {
                for (int i = 0; i < listDatas.size(); i++) {
                    GameRecordEntity o = (GameRecordEntity) listDatas.get(i);
                    list.add(new GameRecordJO(o));
                }
            }
            else if(type == 2)
            {

                for (int i = 0; i < listDatas.size(); i++) {
                    UserTopupEntity o = (UserTopupEntity) listDatas.get(i);
                    list.add(new UserTopupJO(o));
                }
            }
            else if(type == 3)
            {

                for (int i = 0; i < listDatas.size(); i++) {
                    UserTakeoutEntity o = (UserTakeoutEntity) listDatas.get(i);
                    list.add(new UserTakeoutJO(o));
                }
            }
            else if(type == 4)
            {

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

    public boolean addRecordListForRedis(Object addRedisObj,int userId) {
        String keyName = addRedisObj.getClass().getSimpleName()+userId;
        //如果有表示被查询 才加入
        if(redisCacheManager.hasKey(keyName))
        {
            long size = redisCacheManager.lGetListSize(keyName);
            if(size < 12)
            {
                redisCacheManager.lSet(keyName,addRedisObj);
            }
            else
            {
                redisCacheManager.lRightPopAndLeftPush_IfSize(keyName,addRedisObj,12);
            }
            return true;
        }
        return false;
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
