package com.example.lakecircle.ui.home.light.model;

import java.util.ArrayList;
import java.util.List;

public class UserRankBean {

//
    /**
     * avatar : string
     * star_num : 0
     * username : string
     */

    private String avatar;
    private int star_num;
    private String username;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getStar_num() {
        return star_num;
    }

    public void setStar_num(int star_num) {
        this.star_num = star_num;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public static List<UserRankBean> getDefaultBeans() {
        List<UserRankBean> beans = new ArrayList<>();
        UserRankBean b1 = new UserRankBean();
        b1.setAvatar(null);
        b1.star_num = 8;
        b1.username = "null";
        beans.add(b1);

        UserRankBean b2 = new UserRankBean();
        b2.avatar = null;
        b2.star_num = 8;
        b2.username = "用户名";
        beans.add(b2);
        return beans;
    }

}
