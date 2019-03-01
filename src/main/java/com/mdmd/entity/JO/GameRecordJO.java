package com.mdmd.entity.JO;

import com.mdmd.entity.GameRecordEntity;
import com.mdmd.entity.UserEntity;

import java.io.Serializable;
import java.sql.Timestamp;

public class GameRecordJO implements Serializable {

    private int id;
    private Timestamp time;
    private String gameType;
    private String gameResult;
    private String gameContent;
    private double gameCost;
    private double principal;
    private int userId;

    public GameRecordJO() {
    }

    public GameRecordJO(GameRecordEntity gameRecordEntity) {
        this.id = gameRecordEntity.getId();
        this.time = gameRecordEntity.getTime();
        this.gameType = gameRecordEntity.getGameType();
        this.gameResult = gameRecordEntity.getGameResult();
        this.gameContent = gameRecordEntity.getGameContent();
        this.gameCost = gameRecordEntity.getGameCost();
        this.principal = gameRecordEntity.getPrincipal();
        this.userId = gameRecordEntity.getUserEntity().getUserid();
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
