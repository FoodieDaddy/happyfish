package com.mdmd.service;

import com.mdmd.entity.FishRuleEntity;
import com.mdmd.entity.JO.FishRuleJO;
import com.mdmd.entity.JO.SysPropResultJO;
import com.mdmd.entity.SysPropEntity;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface SysPropService {

    /**
     * 初始化配置到全局变量中
     */
    void initSysProps();

    /**
     * 更新某些配置
     */
    String updateSysprop( SysPropEntity sysPropEntity);

    String updateSysprops(List<SysPropEntity> sysPropEntities);
    /**
     * 通过配置编号获取某个配置信息
     * @param sysNum
     * @return
     */
    SysPropEntity getSyspropWithSysNum(int sysNum);

    SysPropResultJO getSyspropWithSysNum_JO(int sysNum);


    /**
     * 通过配置编号获取某些配置信息
     * @param sysNums
     * @return
     */
    List<SysPropEntity> getSyspropWithSysNums(List<Integer> sysNums);

    List<SysPropResultJO> getSyspropWithSysNums_JO(List<Integer> sysNums);
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


    List<FishRuleJO> listAllFishRule();

    boolean saveOrUpdateFishRules(List<FishRuleEntity> fishRuleEntities) throws RuntimeException;

    boolean deleteFishRules(List<Integer> ids) throws RuntimeException;
}
