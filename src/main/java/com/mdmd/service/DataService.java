package com.mdmd.service;

import com.mdmd.entity.GameRecordEntity;
import com.mdmd.entity.JO.RankingListJO;
import com.mdmd.entity.UserEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface DataService {

    /**
     * tomcat启动时加载数据到缓存
     */
    void initCache();

    /**
     * 定时
     */
    void calcYesterdayCommissionList_Schedule();

    /**
     * 简单计算昨天排行榜 这个计算方法是只能计算今天的佣金排行 当在11.59分后计算，误差不大 既为简单计算
     * @return
     */
    List<RankingListJO> calcYesterdayCommissionRankingList();

    /**
     * 获取排行榜 如果从缓存中没有获取到 则使用精确计算，从数据库中累加得到
     * @return
     */
    List<RankingListJO> getYesterdayCommissionRankingList();


    /**
     * 查询一些数据 根据自定义的type返回数据 返回12条
     * @param type
     * @param userId
     * @return
     */
    List listDatas(int type,int userId) throws ClassNotFoundException;


    List getAllChilds(int userId);

    /**
     * 通过userId获取二维码ticket
     * @param userId
     * @return
     */
    String getQRCodeTicketWithUserId(int userId) throws Exception;


    UserEntity getUserWithQrcodeSceneId(int sceneId)throws Exception;

    void test();

}
