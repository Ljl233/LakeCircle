package com.example.lakecircle.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.lakecircle.R;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }

    void initView() {

    }

    @Override
    public void getAccount() {

    }

    @Override
    public void getPassword() {

    }

    @Override
    public void showLoad() {

    }

    @Override
    public void failLogin(String msg) {

    }

    @Override
    public void successLogin() {

    }
}
