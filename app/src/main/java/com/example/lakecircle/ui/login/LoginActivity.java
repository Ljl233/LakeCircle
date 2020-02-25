package com.example.lakecircle.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lakecircle.R;
import com.example.lakecircle.ui.forgetpwd.PasswordActivity;
import com.example.lakecircle.ui.logup.LogupActivity;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private Button mBtLogin;
    private TextView mTvForgetPwd, mTvLogup;
    private EditText mEtAccount, mEtPwd;
    private LoginContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

    }

    void initView() {
        mBtLogin = findViewById(R.id.login_bt);
        mBtLogin.setOnClickListener(v -> {
            mPresenter.login();
        });

        mTvForgetPwd = findViewById(R.id.login_tv_forget_pwd);
        mTvLogup = findViewById(R.id.login_tv_logup);

        mTvLogup.setOnClickListener(v -> {
            startActivity(new Intent(this, LogupActivity.class));
        });

        mTvForgetPwd.setOnClickListener(v -> {
            startActivity(new Intent(this, PasswordActivity.class));
        });

        mEtAccount = findViewById(R.id.login_et_account);
        mEtPwd = findViewById(R.id.login_et_password);
    }

    @Override
    public String getAccountFromView() {
        return mEtAccount.getText().toString();
    }

    @Override
    public String getPasswordFromView() {
        return mEtPwd.getText().toString();
    }

    @Override
    public void showLoad() {
        mBtLogin.setText("登录中...");
        mBtLogin.setClickable(false);
        mEtPwd.setClickable(false);
        mEtAccount.setClickable(false);
    }

    @Override
    public void failLogin(String msg) {
        Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();

        mBtLogin.setText("登录");
        mBtLogin.setClickable(true);
        mEtPwd.setClickable(true);
        mEtAccount.setClickable(true);
    }

    @Override
    public void successLogin() {
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
