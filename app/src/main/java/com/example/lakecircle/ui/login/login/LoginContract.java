package com.example.lakecircle.ui.login.login;

public interface LoginContract {
    interface View {

        String getAccountFromView();

        String getPasswordFromView();

        void showLoad();

        void failLogin(String msg);


        void successLogin();
    }

    interface Presenter {
        void login();

        void onDestroy();
    }
}
