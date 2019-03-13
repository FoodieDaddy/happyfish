package com.mdmd.custom;

import com.mdmd.dao.CommonDao;
import com.mdmd.entity.GoldEntity;
import com.mdmd.entity.UserEntity;
import com.mdmd.util.CommonUtil;
import com.mdmd.util.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * custom类是防止service层调用service层的一种疏通手段 此类中不应该有增删改操作
 */
@Component
public class UserCustom {
    @Autowired
    private CommonDao commonDao;


    /**
     * 获取一个用户的5个父级
     * @param userId
     * @return
     * @throws RuntimeException
     */
    public List<UserEntity> get5_SuperUserEntity(int userId) throws RuntimeException{
        UserEntity user =(UserEntity) commonDao.getEntity(UserEntity.class, userId);
        List<UserEntity> superUserList = new LinkedList<>();
        int superUserId_a = user.getSuperUserId_a();
        int superUserId_b = user.getSuperUserId_b();
        int superUserId_c = user.getSuperUserId_c();
        int superUserId_d = user.getSuperUserId_d();
        int superUserId_e = user.getSuperUserId_e();
        int [] superUserids = {superUserId_a,superUserId_b,superUserId_c,superUserId_d,superUserId_e};
        for (int i = 0; i < superUserids.length; i++) {
            int superId = superUserids[i];
            if(superId > 0)
            {
                UserEntity superUser = (UserEntity) commonDao.getEntity(UserEntity.class, superId);
                if(superUser == null)
                    break;
                superUserList.add(superUser);
            }
            else
            {
                break;
            }
        }
        return superUserList;
    }

    /**
     * 计算用户金币对象
     * @param price
     * @param addGold
     * @param goldEntity
     */
    public void calcuGoldForGoldEntity(double price,double addGold, GoldEntity goldEntity){
        int calcDate = goldEntity.getCalcDate();
        int today = DateFormatUtil.now_yyMMdd_intVal();
        double todayWater = goldEntity.getTodayWater();
        double todayGold = goldEntity.getTodayGold();
        if(today != calcDate)
        {
            goldEntity.setPreGold(CommonUtil.formatDouble_two(goldEntity.getPreGold() + goldEntity.getTodayGold()));
            goldEntity.setTodayGold(CommonUtil.formatDouble_three(addGold - price));
            goldEntity.setTodayWater(price);
            goldEntity.setCalcDate(today);

        }
        else
        {
            //记录今日赢得金币，正为今天赢得（需要在充值时减掉充值金额）
            goldEntity.setTodayGold(todayGold + addGold - price );
            //记录今日流水
            goldEntity.setTodayWater(todayWater + price);
        }

    }
}
