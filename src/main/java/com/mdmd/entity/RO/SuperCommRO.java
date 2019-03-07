package com.mdmd.entity.RO;

import java.io.Serializable;

public class SuperCommRO implements Serializable {
    private int userId;
    private int price;

    public SuperCommRO() {
    }

    public SuperCommRO(int userId, int price) {
        this.userId = userId;
        this.price = price;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
