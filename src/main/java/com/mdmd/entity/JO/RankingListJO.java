package com.mdmd.entity.JO;

import java.io.Serializable;

/**
 * 排行榜
 */
public class RankingListJO implements Serializable {
    private int ranking;
    private int userId;
    private double commission;

    public RankingListJO() {
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }
}
