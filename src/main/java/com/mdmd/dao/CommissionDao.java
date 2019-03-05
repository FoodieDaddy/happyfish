package com.mdmd.dao;

import com.mdmd.entity.CommissionEntity;

import java.util.List;

public interface CommissionDao {

    /**
     * 查找前一天的佣金获得排行,通过佣金查找(快速，精确在listTopCommissionFromUserCommission_limit)
     * @param count 查询的数量
     * @return
     */
    List<Object[]> listTopCommissionFromCommission_limit(int count);
}
