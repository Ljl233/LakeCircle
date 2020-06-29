package com.example.lakecircle.ui.mine.govern.activitypost;

public class ActivityPostBean {

    /**
     * detail : string
     * duration : string
     * kind : string
     * location : string
     * name : string
     * picture_url : string
     * reward : 0
     * user_num : 0
     */

    private String detail;
    private String duration;
    private String kind;
    private String location;
    private String name;
    private String picture_url;
    private int reward;
    private int user_num;

    public ActivityPostBean(String detail, String duration, String kind, String location, String name, String picture_url, int reward, int user_num) {
        this.detail = detail;
        this.duration = duration;
        this.kind = kind;
        this.location = location;
        this.name = name;
        this.picture_url = picture_url;
        this.reward = reward;
        this.user_num = user_num;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public int getUser_num() {
        return user_num;
    }

    public void setUser_num(int user_num) {
        this.user_num = user_num;
    }
}
