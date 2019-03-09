package com.mdmd.entity.JO;

import com.mdmd.entity.GameRecordEntity;
import com.mdmd.entity.UserEntity;
import com.mdmd.util.DateFormatUtil;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GameRecordJO implements Serializable {

    private int id;
    private String time;
    private String gameType;
    private String gameResult;
    private String gameContent;
    private double gameCost;
    private double principal;
    private String gameOrder;
    private int userId;

    public GameRecordJO() {
    }

    public GameRecordJO(GameRecordEntity gameRecordEntity) {
        this.id = gameRecordEntity.getId();
        if(gameRecordEntity.getTime() != null)
        {
            String s = gameRecordEntity.getTime().toString();
            this.time = s.split("\\.")[0];
        }
        else
        {
            this.time = DateFormatUtil.now_yyyyMMddHHmmss();
        }

        this.gameType = gameRecordEntity.getGameType();
        this.gameResult = gameRecordEntity.getGameResult();
        this.gameContent = gameRecordEntity.getGameContent();
        this.gameCost = gameRecordEntity.getGameCost();
        this.principal = gameRecordEntity.getPrincipal();
        this.gameOrder = gameRecordEntity.getGameOrder();
        this.userId = gameRecordEntity.getUserEntity().getUserid();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
