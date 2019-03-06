package com.mdmd.dao;

import com.mdmd.entity.UserTakeoutEntity;

import java.util.List;

public interface TakeoutDao {

    /**
     * 获取今天的提现总额
     * @param userId
     * @return
     */
    int getTakeoutAllCount_today(int userId);

    /**
     * 获取今日的提现总额
     * @param userId
     * @param type 0为金币 1为佣金
     * @return
     */
    int getTakeoutCount_today_0gold_1commission(int userId,int type);

    /**
     * 获取用户提现记录成功的数据
     * @param userId
     * @param recordNum 数量
     * @return
     */
    List<UserTakeoutEntity> listUserTakeoutDatas(int userId,int recordNum);
}
