package com.mdmd.service;

import com.google.zxing.WriterException;
import com.mdmd.entity.UserEntity;

import java.io.IOException;

public interface UserService {

    /**
     * 通过openId获取用户信息（没有则创建）
     * @param openId
     * @return
     */
    UserEntity getUserWithOpenId(String openId) throws RuntimeException;

    UserEntity getUserWithUserId_self_or_cascade(int userId,boolean onlySelf) throws RuntimeException;

    UserEntity addUserInfo(String openId) throws Exception;

    UserEntity addUserInfo(String openId,int superUserId) throws Exception;

    UserEntity handlerTopup(int userId,int quantity) throws RuntimeException;
}
