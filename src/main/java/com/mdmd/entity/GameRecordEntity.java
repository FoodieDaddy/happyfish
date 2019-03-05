package com.mdmd.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class GameRecordEntity{
    private int id;
    private Timestamp time;
    private String gameType;
    private String gameResult;
    private String gameContent;
    private double gameCost;
    private double principal;
    private String gameOrder;
    private UserEntity userEntity;

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

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getGameResult() {
        return gameResult;
    }

    public void setGameResult(String gameResult) {
        this.gameResult = gameResult;
    }

    public String getGameContent() {
        return gameContent;
    }

    public void setGameContent(String gameContent) {
        this.gameContent = gameContent;
    }

    public double getGameCost() {
        return gameCost;
    }

    public void setGameCost(double gameCost) {
        this.gameCost = gameCost;
    }

    public double getPrincipal() {
        return principal;
    }

    public void setPrincipal(double principal) {
        this.principal = principal;
    }

    public String getGameOrder() {
        return gameOrder;
    }

    public void setGameOrder(String gameOrder) {
        this.gameOrder = gameOrder;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

}
