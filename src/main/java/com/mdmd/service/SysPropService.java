package com.mdmd.service;

import com.mdmd.entity.FishRuleEntity;
import com.mdmd.entity.JO.AccountDatasJO;
import com.mdmd.entity.JO.FishRuleJO;
import com.mdmd.entity.JO.PageJO;
import com.mdmd.entity.JO.SysPropResultJO;
import com.mdmd.entity.SysPropEntity;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Map;

public interface SysPropService {

    /**
     * 初始化配置到全局变量中
     */
    void initSysProps();

    /**
     * 更新某些配置
     */
    String updateSysprop(int sysNum, String sysValue);

    String updateSysprops(Map<Integer,String> sysPropMap);


    List<SysPropResultJO> getSysprops_JO();

    List<FishRuleJO> listAllFishRule();

    boolean saveOrUpdateFishRules(List<FishRuleEntity> fishRuleEntities) throws RuntimeException;

    boolean deleteFishRules(List<Integer> ids) throws Exception;

    <T> List<T> listDatas_page(Class<T> tClass, PageJO pageJO, Map<String,Object> filter)throws Exception;

    /**
     * @param DayNum 几天
     * @return
     */
    List<AccountDatasJO> listAccountDatas(int DayNum);
    List<AccountDatasJO> listAccountDatas_today_all();

    boolean doSetUser(int adminid);

    boolean dosetSys(int adminid);

    void setUser(int adminid,int userId,String value,String type) throws Exception;
}
