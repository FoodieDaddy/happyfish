package com.mdmd.service;

import com.mdmd.entity.SysPropEntity;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface SysPropService {

    /**
     * 初始化配置到全局变量中
     */
    void initSysProps();

    /**
     * 更新某个配置
     * @param sysNum
     * @param sysValue
     */
    void updateSysprop( int sysNum, String sysValue);

    /**
     * 通过配置编号获取某个配置信息
     * @param sysNum
     * @return
     */
    SysPropEntity getSyspropWithSysNum(int sysNum);
    /**
     * 获取所有配置
     * @return
     */
    List<SysPropEntity> getAllSysprop();

    /**
     * 是否为双倍佣金时间
     * @return
     */
    boolean isDoubleCommissionTime();



}
