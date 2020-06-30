package com.example.lakecircle.ui.home.merchant;

public class Merchant {

    private int id;
    private int change_num;
    private String near;
    private String name;
    private String intro;
    private String avatar_url;
    private String address;
    private String tel;

    public Merchant(){}

    public Merchant(int id, int change_num, String name, String intro, String avatar_url) {
        this.id = id;
        this.change_num = change_num;
        this.name = name;
        this.intro = intro;
        this.avatar_url = avatar_url;
    }

    public Merchant(int id, int change_num, String near, String name, String intro, String avatar_url, String address, String tel) {
        this.id = id;
        this.change_num = change_num;
        this.near = near;
        this.name = name;
        this.intro = intro;
        this.avatar_url = avatar_url;
        this.address = address;
        this.tel = tel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChange_num() {
        return change_num;
    }

    public void setChange_num(int change_num) {
        this.change_num = change_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNear() {
        return near;
    }

    public void setNear(String near) {
        this.near = near;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }


}
