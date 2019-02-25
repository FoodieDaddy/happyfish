package com.mdmd.service;

import com.mdmd.entity.FishRuleEntity;
import com.mdmd.entity.JO.GameResultJO;

import java.util.List;

public interface GameRuleService {

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


    GameResultJO getTreasureResult(int type, int num, int userId);

}
