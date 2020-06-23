package com.example.lakecircle.ui.login.login.model;

import com.alibaba.fastjson.JSONObject;
import com.example.lakecircle.commonUtils.SPUtils;

import java.util.Collection;


public class UserWrapper {
    public User user;

    public static UserWrapper getInstance() {
        return new UserWrapper();
    }

    private UserWrapper() {
        SPUtils spUtils = SPUtils.getInstance(SPUtils.SP_CONFIG);
        String value = spUtils.getString("User", "");
        if (value.length() == 0) {
            this.user = null;
        } else {
            this.user = JSONObject.parseObject(value, User.class);
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return null;
    }
}
