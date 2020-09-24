package com.example.lakecircle.ui.login.logup;

import com.example.lakecircle.commonUtils.NetUtil;
import com.example.lakecircle.ui.login.logup.model.LogupRequireBody;
import com.example.lakecircle.ui.login.logup.model.LogupResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LogupPresenter implements LogupContract.Presenter {

    private LogupContract.View mView;

    LogupPresenter(LogupContract.View view) {
        mView = view;
    }

    @Override
    public void logup() {
        if (!mView.isMatch()) mView.showNotMatch();
        else {
            //todo:网络请求
            LogupRequireBody body = new LogupRequireBody();
            body.setPassword(mView.getPsw());
            body.setSecret_key(mView.getQuestion());
            body.setUsername(mView.getAccount());
            body.setSecret_value(mView.getAnswer());

            NetUtil.getInstance().getApi().logup(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<LogupResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(LogupResponse s) {
                            mView.backToLogin();
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.showError(e);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }
}
