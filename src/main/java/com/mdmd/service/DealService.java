package com.mdmd.service;

import java.util.Map;

public interface DealService {

    /**
     *    向用戶openid支付 默认在某些失败条件下会重试支付
     * @param userId
     * @param openId
     * @param money 最少100（分）
     * @param desc 支付说明

     */
    Map<String,String> companyPayToUser(int userId,String openId, int money, String desc);

    /**
     *    向用戶openid支付 一般在重新尝试付款时使用
     * @param userId
     * @param openId
     * @param money 最少100（分）
     * @param desc 支付说明
     * @param tradeOrderId 商户订单号
     * @param doAgain 是否在某些失败条件下重试
     */
    Map<String,String> companyPayToUser(int userId,String openId, int money, String desc,String tradeOrderId,boolean doAgain);

    /**
     * 通过商户订单号查询订单结果
     * @param tradeOrderId
     * @return
     */
    Map<String,String> searchCompanyPay(String tradeOrderId);

    /**
     * 通过商户订单号查询订单结果 返回结果 成功还返回微信订单号
     * @param tradeOrderId
     * @return
     */
    Map<String,String> searchCompanyPayStatusAndOrder(String tradeOrderId);

    /**
     * 用户提现
     * @param userId
     * @param num 提现数量
     * @param type 提现类型 0为金币 1为佣金
     * @return
     */
    String takeoutForUser(int userId,int num, int type);
}
