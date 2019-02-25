package com.mdmd.service.impl;

import com.google.zxing.WriterException;
import com.mdmd.dao.UserDao;
import com.mdmd.entity.*;
import com.mdmd.service.UserService;
import com.mdmd.util.CommonUtil;
import com.mdmd.util.QrcodeUtil;
import com.mdmd.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import static com.mdmd.constant.SystemConstant.DATEFORMEN__yyyyMMddHHmmss;
import static com.mdmd.constant.SystemConstant.QRCODE_PREFIX;

@Component

public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public UserEntity getUserWithOpenId(String openId) throws RuntimeException{
        //todo 判断openId长度28

        UserEntity userFromOpenId = userDao.getUserFromOpenId(openId);
        if(userFromOpenId == null)
        {
            return null;

        }
        return userFromOpenId;
    }

    public UserEntity getUserWithUserId_self_or_cascade(int userId, boolean onlySelf) throws RuntimeException {
        if(onlySelf)
        {
           return userDao.getUserFromUserId_only(userId);
        }
        else
        {
            UserEntity userEntity = (UserEntity) userDao.getEntity(UserEntity.class,userId);

            return userEntity;
        }
    }

    public UserEntity addUserInfo(String openId) throws Exception{
        UserEntity userEntity = new UserEntity(openId);
        HashSet<GoldEntity> goldEntities = new HashSet<>();
        goldEntities.add( new GoldEntity());
        userEntity.setGoldEntitySet(goldEntities);

        HashSet<CommissionEntity> commissionEntityHashSet = new HashSet<>();
        commissionEntityHashSet.add(new CommissionEntity());
        userEntity.setCommissionEntitySet(commissionEntityHashSet);
        userDao.addEntity(userEntity);
        //创建用户明细信息
        userDao.addUserDetail(userEntity);
        int userId = userEntity.getUserid();
        //创建二维码
        QrcodeUtil.mergeImageAndDrawQrcode(QRCODE_PREFIX + userId, userId+"");
        return userEntity;
    }

    public UserEntity addUserInfo(String openId,int superUserId) throws Exception {
        UserEntity userEntity = new UserEntity(openId,superUserId);
        HashSet<GoldEntity> goldEntities = new HashSet<>();
        goldEntities.add( new GoldEntity());
        userEntity.setGoldEntitySet(goldEntities);

        HashSet<CommissionEntity> commissionEntityHashSet = new HashSet<>();
        commissionEntityHashSet.add(new CommissionEntity());
        userEntity.setCommissionEntitySet(commissionEntityHashSet);
        userDao.addEntity(userEntity);
        //创建用户明细信息
        userDao.addUserDetail(userEntity);
        int userId = userEntity.getUserid();
        //创建二维码
        QrcodeUtil.mergeImageAndDrawQrcode(QRCODE_PREFIX + userId, userId+"");
        return userEntity;
    }

    public UserEntity handlerTopup(int userId, int quantity) throws RuntimeException{
        UserEntity userEntity = null;
        try {
            userEntity = (UserEntity) userDao.getEntity(UserEntity.class, userId);
            userEntity.setGold(CommonUtil.formatDouble_two(userEntity.getGold() + quantity));
            userDao.updateEntity(userEntity);
            UserTopupEntity userTopupEntity = new UserTopupEntity();
            userTopupEntity.setTopupType("人工");
            userTopupEntity.setTopupQuantity(quantity);
            userTopupEntity.setTopupResult("成功");
            userTopupEntity.setUserEntity(userEntity);
            userTopupEntity.setOrderNumber("none");
            userDao.addEntity(userTopupEntity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("充值时发生意外user"+userId+",数量"+quantity+"。");
        }
        return userEntity;
    }


}
