package com.mdmd.service.impl;

import com.google.zxing.WriterException;
import com.mdmd.custom.RedisCustom;
import com.mdmd.dao.CommonDao;
import com.mdmd.dao.UserDao;
import com.mdmd.entity.*;
import com.mdmd.entity.JO.PageJO;
import com.mdmd.entity.JO.UserChildsDataJO;
import com.mdmd.entity.JO.UserResultJO;
import com.mdmd.entity.RO.SuperCommRO;
import com.mdmd.entity.bean.PageBean;
import com.mdmd.enums.RedisChannelEnum;
import com.mdmd.service.UserService;
import com.mdmd.util.CommonUtil;
import com.mdmd.util.QrcodeUtil;
import com.mdmd.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.mdmd.constant.SystemConstant.QRCODE_PREFIX;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisCustom redisCustom;
    @Autowired
    private CommonDao commonDao;


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
            return  commonDao.getEntity(UserEntity.class, userId);
        }
    }


    public List<UserChildsDataJO> listAllLevelChildsNumberAndMoneySum(int userId) {
        List<UserChildsDataJO> userChildsDataJOS = new LinkedList<>();
        for (int i = 1; i < 6; i++) {
            double comm = userDao.getChildsLevelCommissionFormUser(userId, i);
            int num = userDao.getChildsLevelNumberFromUser(userId, i);
            userChildsDataJOS.add(new UserChildsDataJO(i,comm,num));
        }
        return userChildsDataJOS;
    }

    public UserEntity addUserInfo(String openId) throws Exception{
        UserEntity userEntity = new UserEntity(openId);
        HashSet<GoldEntity> goldEntities = new HashSet<>();
        goldEntities.add( new GoldEntity());
        userEntity.setGoldEntitySet(goldEntities);

        HashSet<CommissionEntity> commissionEntityHashSet = new HashSet<>();
        commissionEntityHashSet.add(new CommissionEntity());
        userEntity.setCommissionEntitySet(commissionEntityHashSet);
        commonDao.addEntity(userEntity);
        //创建用户明细信息
        userDao.addUserDetail(userEntity);

//        QrcodeUtil.mergeImageAndDrawQrcode(QRCODE_PREFIX + userId, userId+"");
        return userEntity;
    }

    public UserEntity addUserInfo(String openId,int superUserId) throws Exception {
        UserEntity superUser = userDao.getUserFromUserId_only(superUserId);
        UserEntity userEntity;
        if(superUser == null)
        {
            userEntity = new UserEntity(openId);
        }
        else
        {
            userEntity = new UserEntity(openId,superUser);
        }
        HashSet<GoldEntity> goldEntities = new HashSet<>();
        goldEntities.add( new GoldEntity());
        userEntity.setGoldEntitySet(goldEntities);

        HashSet<CommissionEntity> commissionEntityHashSet = new HashSet<>();
        commissionEntityHashSet.add(new CommissionEntity());
        userEntity.setCommissionEntitySet(commissionEntityHashSet);
        commonDao.addEntity(userEntity);
        //创建用户明细信息
        userDao.addUserDetail(userEntity);
        int userId = userEntity.getUserid();
        QrcodeUtil.mergeImageAndDrawQrcode(QRCODE_PREFIX + userId, userId+"");
        return userEntity;
    }

    public String handlerTopup(int userId, int quantity ,String openid) throws Exception{
        UserEntity userEntity = null;
        try {
            userEntity =commonDao.getEntity(UserEntity.class, userId);
            userEntity.setGold(CommonUtil.formatDouble_two(userEntity.getGold() + quantity));
            commonDao.updateEntity(userEntity);
            UserTopupEntity userTopupEntity = new UserTopupEntity();
            userTopupEntity.setTopupType("人工充值");
            userTopupEntity.setTopupQuantity(quantity);
            userTopupEntity.setTopupResult(1);
            userTopupEntity.setUserEntity(userEntity);
            userTopupEntity.setForeignOrderNumber("0");
            userTopupEntity.setCollector(openid);
            userTopupEntity.setRealQuantity(quantity);
            userTopupEntity.setOrderNumber("0");
            commonDao.addEntity(userTopupEntity);
            //加入缓存
            redisCustom.addRecordListForRedis(userTopupEntity,userId);
            return "充值成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "充值失败";
        }
    }

    public List<UserResultJO> listUserEntity_page(PageJO pageJO, Map<String, Object> filter,Map<String,Object> sort) throws Exception {
        int count = commonDao.getCount(UserEntity.class);
        pageJO.setAllCount(count);
        PageBean pageData = CommonUtil.getPageData(pageJO);

        List<UserResultJO> userResultJOS = userDao.listUserEntity_limit(pageData, filter, sort);
        return userResultJOS;
    }

    public void updateUserEntityFromUserResultJO(List<UserResultJO> userResultJOs) throws Exception {
        for (int i = 0; i < userResultJOs.size(); i++) {
            UserResultJO userResultJO = userResultJOs.get(i);
            UserEntity user = commonDao.getEntity(UserEntity.class, userResultJO.userId);
            user.setGold(userResultJO.gold);
            user.setCommission(userResultJO.commission);
            byte a = 0;
            if(userResultJO.ban)
               a = 1;
            user.setLoginBan(a);
            user.setTakeoutBan(a);
            user.setTopupBan(a);
            user.setCommissionGetBan(a);
            user.setCommissionGiveBan(a);
            commonDao.updateEntity(user);
        }
    }


}
