package com.example.lakecircle.ui.login.login;

import com.example.lakecircle.commonUtils.NetUtil;
import com.example.lakecircle.ui.login.login.model.LoginResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mView;

    LoginPresenter(LoginContract.View view) {
        mView = view;
    }

    @Override
    public void login() {
        String account = mView.getAccountFromView();
        String password = mView.getPasswordFromView();

        NetUtil.getInstance().getApi().login(password, account)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(v -> mView.showLoad())
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        mView.successLogin();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.failLogin(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    @Override
    public void onDestroy() {
        mView = null;
    }
}
