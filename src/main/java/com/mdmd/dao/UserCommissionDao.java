package com.mdmd.dao;

import java.util.List;

public interface UserCommissionDao {

    /**
     * 查找前一天的佣金获得排行,通过佣金明细查找(精确，快速在精确在listTopCommissionFromCommission_limit）
     * @param count
     * @return
     */
    List listTopCommissionFromUserCommission_limit(int count);
}
