package com.mdmd.service.impl;

import com.mdmd.custom.RedisCustom;
import com.mdmd.dao.CommonDao;
import com.mdmd.dao.TakeoutDao;
import com.mdmd.dao.UserDao;
import com.mdmd.entity.CommissionEntity;
import com.mdmd.entity.GoldEntity;
import com.mdmd.entity.UserEntity;
import com.mdmd.entity.UserTakeoutEntity;
import com.mdmd.service.TakeoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.mdmd.constant.ActionConstant.MSG;
import static com.mdmd.constant.GameConstant.TAKEOUT_MAX_QUATITY;
import static com.mdmd.constant.GameConstant.TAKEOUT_TAK;
import static com.mdmd.util.CommonUtil.formatDouble_two;
import static com.mdmd.util.WeiXinPayUtil.companyPayToUser;

@Service
public class TakeoutServiceImpl implements TakeoutService {


    @Autowired
    private UserDao userDao;

    @Autowired
    private TakeoutDao takeoutDao;
    @Autowired
    private CommonDao commonDao;
    @Autowired
    private RedisCustom  redisCustom;


    public String takeoutForUser(int userId, int num, int type) {
        try {
            UserEntity user = (UserEntity) commonDao.getEntity(UserEntity.class, userId);
            //不为0则无法提现
            if(user.getTakeoutBan() != 0)
            {
                return "无法提现，请联系客服";
            }
            //查看提现次数
            int takeOutTime = user.takeOutTime();
            //提现手续费
            double tax = 0;
            if(takeOutTime > 0)
            {
                tax =  num*TAKEOUT_TAK/100.0;
            }

            double cost = num + tax;

            //金币提现
            if(type == 0)
            {
                double gold = user.getGold();
                GoldEntity goldEntity = user.singleGoldEntity();
                //今日金币提现数
                int takeoutGoldCount_today = takeoutDao.getTakeoutCount_today_0gold_1commission(userId,0);
                int todayWater = (int) goldEntity.todayWater();
                //可提现金币数
                int ableTakeout = todayWater - takeoutGoldCount_today;

                if(num > ableTakeout)
                {
                    return "流水不足，剩余流水可供提现数量：" + ableTakeout;
                }

                if(gold < cost)
                {
                    return "金币不足";
                }

                //获取今日提现总量；
                int takeoutAllCount_today = takeoutDao.getTakeoutAllCount_today(userId);
                //剩余提现量
                int surp_qutity = TAKEOUT_MAX_QUATITY - takeoutAllCount_today;
                if(surp_qutity <= 0)
                {
                    return "今日剩余提现额度为0";
                }
                if(surp_qutity < num)
                {
                    return "提现额度不足，今日剩余提现数量：" + surp_qutity;
                }
                //商户号付款
                Map<String, String> record = companyPayToUser(userId, user.getUserOpenid(), num * 100, "金币提现");
//                Map<String, String> record = new HashMap<>();
//                record.put("payment_no","wx123456789");
//                record.put("partner_trade_no","sh987654321");
                //创建提现对象
                UserTakeoutEntity userTakeoutEntity = new UserTakeoutEntity();
                userTakeoutEntity.setUserEntity(user);
                userTakeoutEntity.setTakeoutQuantity(num);
                userTakeoutEntity.setTakeoutType(0);
                userTakeoutEntity.setTradeOrderNum(record.get("partner_trade_no"));
                userTakeoutEntity.setRealUse(cost);
                //有订单号表示成功
                if(record.containsKey("payment_no"))
                {
                    userTakeoutEntity.setTakeoutResult(1);
                    userTakeoutEntity.setWxOrderNum(record.get("payment_no"));
                }
                else
                {
                    if(record.containsKey(MSG))
                    {
                        userTakeoutEntity.setTakeoutResult(0);
                        userTakeoutEntity.setWxOrderNum("0");
                        commonDao.addEntity(userTakeoutEntity);
                        return record.get(MSG);
                    }
                    return "提现失败";
                }
                commonDao.addEntity(userTakeoutEntity);
                //新增消息到redis中
                redisCustom.addRecordListForRedis(userTakeoutEntity,userId);
                //修改提现总额
                goldEntity.setTakeoutGold(goldEntity.getTakeoutGold() + num);
                user.setGold(formatDouble_two(gold - cost));
                user.setTakeoutTime(  user.getTakeoutTime() + 1);
                commonDao.addEntity(user);
            }
            else
            {
                double commission = user.getCommission();
                CommissionEntity commissionEntity = user.singleCommissionEntity();
                //查看提现额度是否超标
                //获取今日提现总量；
                int takeoutAllCount_today = takeoutDao.getTakeoutAllCount_today(userId);
                //剩余提现量
                int surp_qutity = TAKEOUT_MAX_QUATITY - takeoutAllCount_today;
                if(surp_qutity <= 0)
                {
                    return "今日剩余提现额度为0";
                }
                if(surp_qutity < num)
                {
                    return "提现数量不足，今日剩余提现数量：" + surp_qutity;
                }
                if(commission < cost)
                {
                    return "佣金额度不足";
                }
                //商户号付款
                Map<String, String> record = companyPayToUser(userId, user.getUserOpenid(), num * 100, "佣金提现");
//                Map<String, String> record = new HashMap<>();
//                record.put("payment_no","wx123456789");
//                record.put("partner_trade_no","sh987654321");

                //创建提现对象
                UserTakeoutEntity userTakeoutEntity = new UserTakeoutEntity();
                userTakeoutEntity.setUserEntity(user);
                userTakeoutEntity.setTakeoutQuantity(num);
                userTakeoutEntity.setTakeoutType(1);
                userTakeoutEntity.setTradeOrderNum(record.get("partner_trade_no"));
                userTakeoutEntity.setRealUse(cost);
                //有订单号表示成功
                if(record.containsKey("payment_no"))
                {
                    userTakeoutEntity.setTakeoutResult(1);
                    userTakeoutEntity.setWxOrderNum(record.get("payment_no"));
                }
                else
                {
                    if(record.containsKey(MSG))
                    {
                        userTakeoutEntity.setTakeoutResult(0);
                        userTakeoutEntity.setWxOrderNum("0");
                        commonDao.addEntity(userTakeoutEntity);
                        return record.get(MSG);
                    }

                    return "提现失败";
                }
                commonDao.addEntity(userTakeoutEntity);
                //新增记录到redis中
                redisCustom.addRecordListForRedis(userTakeoutEntity,userId);
                //修改提现总额
                commissionEntity.setTakeoutCommission( commissionEntity.getTakeoutCommission() + num);
                user.setCommission(formatDouble_two(commission- cost));
                user.setTakeoutTime(  user.getTakeoutTime() + 1);
                commonDao.addEntity(user);

            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
