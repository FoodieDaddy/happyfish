package com.mdmd.entity.JO;

import com.mdmd.entity.UserEntity;
import com.mdmd.entity.UserTakeoutEntity;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserTakeoutJO implements Serializable {
    private int id;
    private Timestamp time;
    private int takeoutType;
    private int takeoutResult;
    private int takeoutQuantity;
    private int userId;

    public UserTakeoutJO() {
    }

    public UserTakeoutJO(UserTakeoutEntity userTakeoutEntity) {
        this.id = userTakeoutEntity.getId();
        this.time = userTakeoutEntity.getTime();
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

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
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
