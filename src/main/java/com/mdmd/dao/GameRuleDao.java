package com.mdmd.dao;

import com.mdmd.entity.FishRuleEntity;

import java.util.List;

public interface GameRuleDao extends BaseRepository{



    /**
     * 通过倍率和红包数值获取捕鱼规则
     * @param price
     * @param targetValue
     * @return
     */
    FishRuleEntity getFishRuleWithPriceAndTargetValue(int price,int targetValue);
}
