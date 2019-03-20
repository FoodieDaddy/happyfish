package com.mdmd.service;

import com.mdmd.entity.FishRuleEntity;
import com.mdmd.entity.JO.GameResultJO;
import com.mdmd.entity.bean.FishProbabilityBean;

import java.util.List;
import java.util.Map;

public interface GameRuleService {


    /**
     * 初始化捕鱼规则
     */
    void initFishRules();
    /**
     * 获取所有的规则
     * @return
     */
    List<FishRuleEntity> getAllFishRules();

    /**
     * 通过倍率和红包数值获取规则
     * @return
     */
    FishRuleEntity getFishRulesWithPriceAndTargetValue(int price, int targetValue);

    /**
     * 获取捕鱼结果和返回数据
     * @param price
     * @param targetValue
     * @param userId
     * @return
     */
    GameResultJO getFishResult(int price, int targetValue, int userId) throws RuntimeException;


    /**
     * 夺宝开始时提前扣除用户支付的钱
     * @param num
     * @param userId
     * @return
     * @throws Exception
     */
    String getTreasureResult_1_deductMoney(int num, int userId) throws Exception;
    /**
     * 在夺宝付款计算中失败时调用
     * 加上用户提前扣除的钱
     * @param num
     * @param userId
     * @throws Exception
     */
    void treasureCost_back(int num, int userId) throws Exception;
    /**
     * 获取夺宝结果和返回数据
     * @param type
     * @param num
     * @param userId
     * @return
     */
    GameResultJO getTreasureResult(int type, int num, int userId) throws Exception;
    /**
     * 计算父类佣金
     * @param userId
     * @param price
     */
    void calcuCommission(int userId, double price);



}
