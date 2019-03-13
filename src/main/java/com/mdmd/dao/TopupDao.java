package com.mdmd.dao;

import com.mdmd.entity.UserOrderTopupEntity;
import com.mdmd.entity.UserTopupEntity;

public interface TopupDao {

    UserOrderTopupEntity getUserOrderTopupEntityFromOrderNum(String orderNum, int userId);
}
