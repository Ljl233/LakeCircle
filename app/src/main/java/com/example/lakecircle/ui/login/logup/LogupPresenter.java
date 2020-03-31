package com.example.lakecircle.ui.login.logup;

public class LogupPresenter implements LogupContract.Presenter {

    private LogupContract.View mView;

    LogupPresenter(LogupContract.View view) {
        mView = view;
    }

    @Override
    public void logup() {
        if (mView.isMatch()) mView.showNotMatch();
        else {
            //todo:网络请求
        }
    }
}
