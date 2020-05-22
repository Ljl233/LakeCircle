package com.example.lakecircle.ui.login.logup.model;

public class LogupRequireBody {
    /**
     * password : string
     * secret_key : string
     * secret_value : string
     * username : string
     */

    private String password;
    private String secret_key;
    private String secret_value;
    private String username;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public String getSecret_value() {
        return secret_value;
    }

    public void setSecret_value(String secret_value) {
        this.secret_value = secret_value;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
