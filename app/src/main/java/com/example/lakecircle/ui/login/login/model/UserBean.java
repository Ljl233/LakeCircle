package com.example.lakecircle.ui.login.login.model;

public class UserBean {

    /**
     * password : 1
     * username : 1
     */

    private String password;
    private String username;

    public UserBean() {
    }

    public UserBean(String username, String password) {
        this.password = password;
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
