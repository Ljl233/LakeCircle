package com.example.lakecircle.ui.login.logup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lakecircle.R;
import com.example.lakecircle.ui.login.login.LoginActivity;

public class LogupActivity extends AppCompatActivity implements LogupContract.View {

    private Spinner mSpinner;
    private String mQuestion;
    private EditText mEtAccount, mEtPsw, mEtConfirm, mEtAnswer;
    private Button mButton;
    private LogupContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logup);

        mSpinner = findViewById(R.id.logup_spinner);
        mEtAccount = findViewById(R.id.logup_et_account);
        mEtPsw = findViewById(R.id.logup_et_password);
        mEtConfirm = findViewById(R.id.logup_et_confirm_pwd);
        mEtAnswer = findViewById(R.id.logup_ed_answer);
        mButton = findViewById(R.id.logup_bt);
        initView();
    }

    private void initView() {

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(
                this, R.array.pwd_question, android.R.layout.simple_spinner_item);
        mSpinner.setAdapter(arrayAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mQuestion = mSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mButton.setOnClickListener(v -> {
            mPresenter.logup();
        });
    }

    @Override
    public String getAccount() {
        return mEtAccount.getText().toString();
    }

    @Override
    public String getPsw() {
        return mEtPsw.getText().toString();
    }

    @Override
    public String getConfirmPsw() {
        return mEtConfirm.getText().toString();
    }

    @Override
    public boolean isMatch() {
        return getPsw().equals(getConfirmPsw());
    }

    @Override
    public void showNotMatch() {
        Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getQuestion() {
        return mSpinner.getSelectedItem().toString();
    }

    @Override
    public String getAnswer() {
        return mEtAnswer.getText().toString();
    }

    @Override
    public void backToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
    }

}
