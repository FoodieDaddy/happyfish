package com.mdmd.entity.bean;

import com.mdmd.util.DateFormatUtil;

import java.text.SimpleDateFormat;
import java.util.Date;


public class GameResultBean {

    private boolean gameResult;//记录游戏结果
    private double gameCost;//游戏输赢金额
    private String gameTime;//游戏时间

    public GameResultBean(boolean gameResult, double gameCost) {
        this.gameResult = gameResult;
        this.gameCost = gameCost;
        this.gameTime = DateFormatUtil.now_yyyyMMddHHmmss();
    }

    public boolean getGameResult() {
        return gameResult;
    }

    public double getGameCost() {
        return gameCost;
    }

    public String getGameTime() {
        return gameTime;
    }
}
