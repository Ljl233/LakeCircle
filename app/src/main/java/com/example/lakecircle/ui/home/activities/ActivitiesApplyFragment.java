package com.example.lakecircle.ui.home.activities;

import android.os.Build;
import android.os.Bundle;
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
import com.example.lakecircle.ui.SuccessDialog;

public class ActivitiesApplyFragment extends Fragment {

    private Toolbar mToolbar;
    private EditText mNameEt, mAgeEt, mNumEt, mQQEt;
    private Button mApplyBtn;
    private RadioGroup mSexRg;

    private boolean mSex; //false-男 true-女

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
            SuccessDialog.newInstance("恭喜您报名成功！");
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
}
