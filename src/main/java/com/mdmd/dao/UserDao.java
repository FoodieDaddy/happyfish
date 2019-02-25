package com.mdmd.dao;

import com.mdmd.entity.*;

import java.util.List;
import java.util.Map;

public  interface UserDao extends BaseRepository{
    List listAllEntity(Class clazz);
    void addEntity(Object obj);

    void updateEntity(Object obj);

    Object getEntity(Class clazz,int id);
    /**
     * 通过openId找到用户
     * @param openId
     * @return
     */
    UserEntity getUserFromOpenId(String openId);

    /**
     * 查询某个对象的最近number条记录（只可查询主键column name 为id的关联的实体类）
     * @param clazz
     * @param number
     * @return
     */
    List listForeignWithUserEntity_desc_limit(Class clazz,int number,int userId);
    /**
     * 添加用户其他信息
     * @return
     * @throws RuntimeException
     */
    UserDetailEntity addUserDetail(UserEntity user) throws RuntimeException;

    /**
     * 通过userId获取用户信息(仅用户信息，不包含关联)
     * @return
     */
    UserEntity getUserFromUserId_only(int userId);



    /**
     * 获取用户某个时间段的游戏记录 并返回游戏花费（利润 - 本金）
     * @param userId
     * @param beginTime
     * @param endTime
     * @return
     */
    double getGameRecord_costs_betweenTime(int userId, String beginTime, String endTime);

    /**
     * 获取用户某个时间段的充值总数
     * @param userId
     * @param beginTime
     * @param endTime
     * @return
     */
    double getTopup_sum_betweenTime(int userId, String beginTime, String endTime);

    /**
     * 获取用户某个时间段的提现总数
     * @param userId
     * @param beginTime
     * @param endTime
     * @return
     */
    double getTakeout_sum_betweenTime(int userId, String beginTime, String endTime);




}
