package com.example.lakecircle.ui.home.activities;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.lakecircle.R;
import com.example.lakecircle.commonUtils.NetUtil;
import com.example.lakecircle.ui.SuccessDialog;
import com.example.lakecircle.ui.mine.SimpleResponse;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ActivitiesApplyFragment extends Fragment {

    private int ID = 0;

    private Toolbar mToolbar;
    private EditText mNameEt, mAgeEt, mNumEt, mQQEt;
    private Button mApplyBtn;
    private RadioGroup mSexRg;

    private boolean mSex = false; //false-男 true-女

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            ID = getArguments().getInt("id");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activities_apply, container, false);

        mToolbar = view.findViewById(R.id.tb_activities_apply);
        mNameEt = view.findViewById(R.id.et_name_activities_apply);
        mAgeEt = view.findViewById(R.id.et_age_activities_apply);
        mNumEt = view.findViewById(R.id.et_num_activities_apply);
        mQQEt = view.findViewById(R.id.et_qq_activities_apply);
        mApplyBtn = view.findViewById(R.id.btn_activities_apply);
        mSexRg = view.findViewById(R.id.rg_sex_activities_apply);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setNavigationOnClickListener(v -> {
                NavHostFragment.findNavController(this).popBackStack();
            });
        }

        mApplyBtn.setOnClickListener(v -> {
            if ( checkNull() ) {
                Objects.requireNonNull(getContext()).setTheme(R.style.QMUITheme);
                QMUITipDialog tipDialog = new QMUITipDialog.Builder(getContext())
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                        .setTipWord("请将信息填写完整")
                        .create();
                tipDialog.show();
                this.getView().postDelayed(() -> {
                    tipDialog.dismiss();
                    Objects.requireNonNull(getContext()).setTheme(R.style.AppTheme);
                }, 1500);
            } else
                postApply();

        });

        mSexRg.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_male_activities_apply: {
                    mSex = false;
                    break;
                }
                case R.id.rb_female_activities_apply : {
                    mSex = true;
                    break;
                }
            }
        });
        return view;
    }

    private void postApply() {
        int age = Integer.parseInt( mAgeEt.getText().toString());
        String gender = !mSex ? "男" : "女";
        String tel = mNumEt.getText().toString();
        String name = mNameEt.getText().toString();
        String qq = mQQEt.getText().toString();
        ActivityApplyBean b = new ActivityApplyBean(age, gender, name, qq, tel);
        NetUtil.getInstance().getApi().applyActivity(ID, b)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SimpleResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        if ( !simpleResponse.getMessage().equals("OK") )
                            onError(new Throwable());
                        showSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("ActivityApplyFragment", "apply activity fail");
                        showError("提交失败");
                    }

                    @Override
                    public void onComplete() { }
                });
    }


    private boolean checkNull() {
        return mNameEt.getText().toString().length() == 0 ||
                mAgeEt.getText().toString().length() == 0 ||
                mNumEt.getText().length() == 0 ;
    }

    private void showSuccess() {
        Objects.requireNonNull(getContext()).setTheme(R.style.QMUITheme);
        QMUITipDialog tipDialog = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord("报名成功")
                .create();
        tipDialog.show();
        ActivitiesApplyFragment fragment = this;
        mApplyBtn.postDelayed(() -> {
            Objects.requireNonNull(getContext()).setTheme(R.style.AppTheme);
            NavHostFragment.findNavController(fragment).popBackStack();
            tipDialog.dismiss();
        }, 1500);
    }

    private void showError(String error) {
        Objects.requireNonNull(getContext()).setTheme(R.style.QMUITheme);
        QMUITipDialog tipDialog = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord(error)
                .create();
        tipDialog.show();
        this.getView().postDelayed(() -> {
            tipDialog.dismiss();
            Objects.requireNonNull(getContext()).setTheme(R.style.AppTheme);
        }, 1500);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getContext()).setTheme(R.style.AppTheme);
    }
}
