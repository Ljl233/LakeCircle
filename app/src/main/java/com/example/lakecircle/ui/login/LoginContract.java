package com.example.lakecircle.ui.login;

public interface LoginContract {
    interface View {

        void getAccount();

        void getPassword();

        void showLoad();//超时失败

        void failLogin(String msg);

        void successLogin();
    }

    interface Presenter {

    }
}
