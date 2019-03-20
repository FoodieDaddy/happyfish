package com.mdmd.service.impl;

import com.mdmd.Manager.RedisCacheManager;
import com.mdmd.Manager.SysPropManager;
import com.mdmd.custom.RedisCustom;
import com.mdmd.custom.UserCustom;
import com.mdmd.dao.CommonDao;
import com.mdmd.dao.TopupDao;
import com.mdmd.dao.UserDao;
import com.mdmd.entity.JO.UserTopupJO;
import com.mdmd.entity.UserEntity;
import com.mdmd.entity.UserOrderTopupEntity;
import com.mdmd.entity.UserTopupEntity;
import com.mdmd.service.TopupService;
import com.mdmd.util.DateFormatUtil;
import com.mdmd.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.mdmd.constant.SystemConstant.*;

@Service
public class TopupServiceImpl implements TopupService {

    @Autowired
    private TopupDao topupDao;
    @Autowired
    private CommonDao commonDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisCacheManager redisCacheManager;
    @Autowired
    private UserCustom userCustom;
    @Autowired
    private RedisCustom  redisCustom;

    public boolean updateUserTopupEntityData(String orderNum,String foreignOrderNum,String collector,double money, int userId,boolean success) throws Exception{
        UserOrderTopupEntity userOrderTopupEntity = topupDao.getUserOrderTopupEntityFromOrderNum(orderNum, userId);
        if(userOrderTopupEntity == null)
        {
            //todo 记录
            return false;
        }
        UserTopupEntity userTopup = new UserTopupEntity();
        userTopup.setCollector(collector);
        userTopup.setRealQuantity(money);
        userTopup.setTopupType(userOrderTopupEntity.getTopupType());
        userTopup.setTopupQuantity(userOrderTopupEntity.getTopupQuantity());
        userTopup.setForeignOrderNumber(foreignOrderNum);
        userTopup.setOrderNumber(orderNum);
        UserEntity userEntity = userOrderTopupEntity.getUserEntity();
        userTopup.setUserEntity(userEntity);
        if(success)
        {
            userTopup.setTopupResult(1);
            //如果成功了还要更新订单为已核实
            userOrderTopupEntity.setDoCheck(1);
            userEntity.setGold(userEntity.getGold() + money);
            userCustom.calcuGoldForGoldEntity(0,money,userEntity.singleGoldEntity());
            commonDao.updateEntity(userEntity);
        }
        else
        {
            userTopup.setTopupResult(0);
        }
        commonDao.addEntity(userTopup);
        commonDao.updateEntity(userOrderTopupEntity);
        //插入缓存
        redisCustom.addRecordListForRedis(new UserTopupJO(userTopup),userId);
        return true;
    }

    public SortedMap<String,Object> getParametersForTopup(int userId,int money,int type) throws Exception {
        SortedMap<String, Object> parameters = new TreeMap<>();
        parameters.put("goodsname","金币充值");
        parameters.put("notify_url", SysPropManager.getSystemWebUrl() + "/topup/record.do");
        String orderid = orderNum(userId);
        parameters.put("orderid",orderid);
        parameters.put("orderuid",userId + "");
        parameters.put("identification",TOPUP_identification);
        parameters.put("return_url", SysPropManager.getSystemWebUrl() + "/stc/index.html");
        parameters.put("type",type);
        parameters.put("price",money * 100);
        parameters.put("token",TOPUP_token);
        Iterator<Map.Entry<String, Object>> iterator = parameters.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()){
            Map.Entry<String, Object> next = iterator.next();
             sb.append(next.getValue());
        }
        String key = MD5Util.MD5Encode(sb.toString(),"utf-8").toLowerCase();
        parameters.remove("token");
        parameters.put("key",key);
        UserEntity userEntity =  commonDao.getEntity(UserEntity.class, userId);
        //创建一个支付订单
        UserOrderTopupEntity userOrderTopupEntity = new UserOrderTopupEntity(orderid, type == 1 ? "微信支付" : "支付宝支付", money, userEntity);
        commonDao.addEntity(userOrderTopupEntity);
        return parameters;
    }

    private String orderNum(int userId){
        StringBuilder orderNum = new StringBuilder(DateFormatUtil.now_orderTime());
        String u = userId + "";
        int time = 6 - u.length();
        for (int i = 0; i < time; i++) {
            orderNum.append("0");
        }
        Random random = new Random();
        orderNum.append(userId).append(random.nextInt(10))
                .append(random.nextInt(10)).append(random.nextInt(10));
        return  orderNum.toString();
    }
}

















