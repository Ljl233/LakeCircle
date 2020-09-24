package com.example.lakecircle.ui.login.logup;

public interface LogupContract {
    interface View {

        String getAccount();

        String getPsw();

        String getConfirmPsw();

        boolean isMatch();

        void showNotMatch();

        String getQuestion();

        String getAnswer();

        void backToLogin();

        void showError(Throwable e);
    }

    interface Presenter {
        void logup();
    }
}
