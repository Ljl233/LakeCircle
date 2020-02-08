package com.example.lakecircle.ui.login;

public interface LoginContract {
    interface View {

        String getAccountFromView();

        String getPasswordFromView();

        void showLoad();//超时失败

        void failLogin(String msg);

        void successLogin();
    }

    interface Presenter {
        void login();

        void onDestroy();
    }
}
