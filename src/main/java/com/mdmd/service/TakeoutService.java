package com.mdmd.service;

import java.util.Map;

public interface TakeoutService {

    /**
     * 用户提现
     * @param userId
     * @param num 提现数量
     * @param type 提现类型 0为金币 1为佣金
     * @return
     */
    String takeoutForUser(int userId,int num, int type);

    int getTakeoutTime(int userId);
}
