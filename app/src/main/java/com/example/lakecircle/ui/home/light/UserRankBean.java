package com.example.lakecircle.ui.home.light;

import java.util.ArrayList;
import java.util.List;

public class UserRankBean {
    private int ranking;
    private String profile;
    private String userName;
    private int starts;

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getStarts() {
        return starts;
    }

    public void setStarts(int starts) {
        this.starts = starts;
    }

    static List<UserRankBean> getDefaultBeans() {
        List<UserRankBean> beans = new ArrayList<>();
        UserRankBean b1 = new UserRankBean();
        b1.profile = null;
        b1.ranking = 1;
        b1.starts = 8;
        b1.userName = null;
        beans.add(b1);

        UserRankBean b2 = new UserRankBean();
        b2.profile = null;
        b2.ranking = 2;
        b2.starts = 8;
        b2.userName = "用户名";
        beans.add(b2);
        return beans;
    }
}
