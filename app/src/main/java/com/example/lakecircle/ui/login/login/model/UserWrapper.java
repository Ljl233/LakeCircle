package com.example.lakecircle.ui.login.login.model;

import com.alibaba.fastjson.JSONObject;
import com.example.lakecircle.commonUtils.SPUtils;

import java.util.Collection;


public class UserWrapper {
    private final SPUtils spUtils;
    public User user;

    public static UserWrapper getInstance() {
        return new UserWrapper();
    }

    private UserWrapper() {
        spUtils = SPUtils.getInstance(SPUtils.SP_CONFIG);
        String value = spUtils.getString("User", "");
        if (value.length() == 0) {
            this.user = null;
        } else {
            this.user = JSONObject.parseObject(value, User.class);
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        if (user == null)
            return "";
        return user.getToken();
    }
}
