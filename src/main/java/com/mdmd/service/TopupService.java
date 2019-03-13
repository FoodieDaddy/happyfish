package com.mdmd.service;

import com.mdmd.entity.UserTopupEntity;

import java.util.Map;
import java.util.SortedMap;

public interface TopupService {

    /**
     * @param orderNum
     * @param foreignOrderNum
     * @param collector
     * @param money
     * @param userId
     * @param success
     */
    boolean updateUserTopupEntityData(String orderNum,String foreignOrderNum,String collector,double money,int userId,boolean success) throws Exception;

    /**
     *
     * @param userId
     * @param money
     * @param type 1：微信支付 2：支付宝支付
     * @return
     * @throws Exception
     */
    SortedMap<String,Object> getParametersForTopup(int userId, int money, int type) throws Exception;

}
