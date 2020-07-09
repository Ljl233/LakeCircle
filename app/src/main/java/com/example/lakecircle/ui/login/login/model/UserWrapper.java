package com.example.lakecircle.ui.login.login.model;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.example.lakecircle.commonUtils.SPUtils;

import java.util.Collection;


public class UserWrapper {
    private final SPUtils spUtils;
    public static User user;

    public static UserWrapper getInstance() {
        return new UserWrapper();
    }

    private UserWrapper() {
        spUtils = SPUtils.getInstance(SPUtils.SP_CONFIG);
        String value = spUtils.getString("User", "");
        if (value.length() == 0) {
            Log.e("token","user==null");
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
        String jsonString = JSONObject.toJSONString(user);
        spUtils.put("User", jsonString, false);
    }

    public String getToken() {
        Log.e("token", "getToken" + (user == null ? "null" : user.toString()));
        if (user == null)
            return "";
        return user.getToken();
    }
}
