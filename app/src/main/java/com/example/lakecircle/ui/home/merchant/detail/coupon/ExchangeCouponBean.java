package com.example.lakecircle.ui.home.merchant.detail.coupon;

public class ExchangeCouponBean {
    private int bid;
    private int cost;

    public ExchangeCouponBean(int bid, int cost) {
        this.bid = bid;
        this.cost = cost;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}