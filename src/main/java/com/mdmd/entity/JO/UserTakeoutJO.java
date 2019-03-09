package com.mdmd.entity.JO;

import com.mdmd.entity.UserEntity;
import com.mdmd.entity.UserTakeoutEntity;
import com.mdmd.util.DateFormatUtil;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserTakeoutJO implements Serializable {
    private int id;
    private String time;
    private int takeoutType;
    private int takeoutResult;
    private int takeoutQuantity;
    private int userId;

    public UserTakeoutJO() {
    }

    public UserTakeoutJO(UserTakeoutEntity userTakeoutEntity) {
        this.id = userTakeoutEntity.getId();
        if(userTakeoutEntity.getTime() != null)
        {
            this.time = userTakeoutEntity.getTime().toString().split("\\.")[0];
        }
        else
        {
            this.time = DateFormatUtil.now_yyyyMMddHHmmss();
        }
        this.takeoutType = userTakeoutEntity.getTakeoutType();
        this.takeoutResult = userTakeoutEntity.getTakeoutResult();
        this.takeoutQuantity = userTakeoutEntity.getTakeoutQuantity();
        this.userId = userTakeoutEntity.getUserEntity().getUserid();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTakeoutType() {
        return takeoutType;
    }

    public void setTakeoutType(int takeoutType) {
        this.takeoutType = takeoutType;
    }

    public int getTakeoutResult() {
        return takeoutResult;
    }

    public void setTakeoutResult(int takeoutResult) {
        this.takeoutResult = takeoutResult;
    }

    public int getTakeoutQuantity() {
        return takeoutQuantity;
    }

    public void setTakeoutQuantity(int takeoutQuantity) {
        this.takeoutQuantity = takeoutQuantity;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
