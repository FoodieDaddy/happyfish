package com.mdmd.service;

import java.util.Map;

public interface DealService {

    /**
     *    向戶openid支付
     * @param userId
     * @param openId
     * @param money
     * @param desc
     */
    Map<String,String> payToUser(int userId,String openId, int money, String desc);
}
