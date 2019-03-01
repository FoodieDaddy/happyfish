package com.mdmd.entity;

import java.io.Serializable;

public class FishRuleEntity implements Serializable {
    private int id;
    private int price;
    private int targetValue;
    private int probability;
    private int volume;
    private double minReturn;
    private double maxReturn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(int targetValue) {
        this.targetValue = targetValue;
    }

    public int getProbability() {
        return probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public double getMinReturn() {
        return minReturn;
    }

    public void setMinReturn(double minReturn) {
        this.minReturn = minReturn;
    }

    public double getMaxReturn() {
        return maxReturn;
    }

    public void setMaxReturn(double maxReturn) {
        this.maxReturn = maxReturn;
    }
}
