package com.example.lakecircle.ui.login.login.model;

public class User {
    private String username;
    private String psw;
    private String token;

    public User(){}

    public User(String username, String psw, String token) {
        this.username = username;
        this.psw = psw;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
