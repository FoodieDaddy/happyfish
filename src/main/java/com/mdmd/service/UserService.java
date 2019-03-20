package com.mdmd.service;

import com.google.zxing.WriterException;
import com.mdmd.entity.JO.PageJO;
import com.mdmd.entity.JO.UserChildsDataJO;
import com.mdmd.entity.JO.UserResultJO;
import com.mdmd.entity.UserEntity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UserService {

    /**
     * 通过openId获取用户信息（没有则创建）
     * @param openId
     * @return
     */
    UserEntity getUserWithOpenId(String openId) throws RuntimeException;

    /**
     * 只获取user对象，或获取所有关联对象
     * @param userId
     * @param onlySelf
     * @return
     * @throws RuntimeException
     */
    UserEntity getUserWithUserId_self_or_cascade(int userId,boolean onlySelf) throws RuntimeException;


    List<UserChildsDataJO> listAllLevelChildsNumberAndMoneySum(int userId);

    UserEntity addUserInfo(String openId) throws Exception;

    UserEntity addUserInfo(String openId,int superUserId) throws Exception;

    String handlerTopup(int userId,int quantity,String openid) throws Exception;

    List<UserResultJO> listUserEntity_page(PageJO pageJO, Map<String, Object> filter, Map<String,Object> sort) throws Exception;

    void updateUserEntityFromUserResultJO(List<UserResultJO> userResultJOs) throws Exception;
}
