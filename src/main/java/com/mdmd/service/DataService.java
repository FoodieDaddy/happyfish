package com.mdmd.service;

import com.mdmd.entity.GameRecordEntity;
import com.mdmd.entity.UserEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface DataService {

    /**
     * tomcat启动时加载数据到缓存
     */
    void initCache();

    /**
     * 定时
     */
    void calcYesterdayCommissionList_Schedule();

    /**
     * 获取一个用户的4个父级
     * @param userId
     * @return
     * @throws RuntimeException
     */
    List<UserEntity> get4_SuperUserEntity(int userId) throws RuntimeException;

    /**
     * 查询一些数据 根据自定义的type返回数据 返回12条
     * @param type
     * @param userId
     * @return
     */
    List listDatas(int type,int userId) throws ClassNotFoundException;
    /**
     * 通过userId获取二维码ticket
     * @param userId
     * @return
     */

    /**
     * 添加以user外关联的数据在list中进行添加数据
     * @param addRedisObj
     * @param userId
     * @return
     */
    boolean addRecordListForRedis(Object addRedisObj,int userId);

    String getQRCodeTicketWithUserId(int userId) throws Exception;

    UserEntity getUserWithQrcodeSceneId(int sceneId)throws Exception;

}
