package com.mdmd.dao;

import com.mdmd.entity.*;
import com.mdmd.entity.JO.PageJO;
import com.mdmd.entity.JO.UserResultJO;
import com.mdmd.entity.bean.PageBean;

import java.util.List;
import java.util.Map;

public  interface UserDao extends BaseRepository{

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
     * 获取某个级别子类的数量，最多为5级，比如一个user的父类的父类的父类（既第三级父类）level为3，
     * @param userId
     * @param level
     * @return
     */
    int getChildsLevelNumberFromUser(int userId, int level);

    /**
     * 获取某个级别子类的所有提供佣金
     * @param userId
     * @param level
     * @return
     */
    double getChildsLevelCommissionFormUser(int userId, int level);

    List<UserResultJO> listUserEntity_limit(PageBean pageBean, Map<String, Object> filter, Map<String,Object> sort) throws Exception;
}
