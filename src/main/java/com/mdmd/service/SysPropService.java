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


    List<SysPropResultJO> getSyspropWithSysNums_JO(List<Integer> ids);

    List<FishRuleJO> listAllFishRule();

    boolean saveOrUpdateFishRules(List<FishRuleEntity> fishRuleEntities) throws RuntimeException;

    boolean deleteFishRules(List<Integer> ids) throws RuntimeException;
}
