package com.mdmd.entity.JO;

import com.mdmd.entity.UserEntity;
import com.mdmd.entity.UserOrderTopupEntity;
import com.mdmd.util.DateFormatUtil;

import java.sql.Timestamp;

public class UserOrderTopupJO {
    private int id;
    private String time;
    private String orderNumber;
    private String topupType;
    private int topupQuantity;
    private int doCheck;
    private String doCheckResult;
    private int userId;
    public UserOrderTopupJO(UserOrderTopupEntity userOrderTopupEntity) {
        this.id = userOrderTopupEntity.getId();
        if(userOrderTopupEntity.getTime() != null)
        {
            this.time = userOrderTopupEntity.getTime().toString().split("\\.")[0];
        }
        else
        {
            this.time = DateFormatUtil.now_yyyyMMddHHmmss();
        }
        this.orderNumber = userOrderTopupEntity.getOrderNumber();
        this.topupType = userOrderTopupEntity.getTopupType();
        this.topupQuantity = userOrderTopupEntity.getTopupQuantity();
        this.doCheck = userOrderTopupEntity.getDoCheck();
        this.doCheckResult = userOrderTopupEntity.getDoCheckResult();
        this.userId = userOrderTopupEntity.getUserEntity().getUserid();
    }
}
