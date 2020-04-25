package com.example.lakecircle.ui.home.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.lakecircle.R;
import com.facebook.drawee.view.SimpleDraweeView;

public class ActivitiesDetailFragment extends Fragment {

    private Toolbar mToolbar;
    private TextView mTitleTv, mTypeTv, mPlaceTv, mTimeTv, mNumTv, mContentTv, mPointTv;
    private SimpleDraweeView mPicSdv;
    private Button mApplyBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activities_detail, container, false);

        mToolbar = view.findViewById(R.id.tb_activities_detail);
        mTitleTv = view.findViewById(R.id.tv_title_activities_detail);
        mTypeTv = view.findViewById(R.id.tv_type2_activities_detail);
        mPlaceTv = view.findViewById(R.id.tv_place2_activities_detail);
        mTimeTv = view.findViewById(R.id.tv_time2_activities_detail);
        mNumTv = view.findViewById(R.id.tv_num2_activities_detail);
        mContentTv = view.findViewById(R.id.tv_content2_activities_detail);
        mPointTv = view.findViewById(R.id.tv_point2_activities_detail);
        mPicSdv = view.findViewById(R.id.sdv_activities_detail);
        mApplyBtn = view.findViewById(R.id.btn_apply_activities_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setNavigationOnClickListener(v -> {
                NavHostFragment.findNavController(this).popBackStack();
            });
        }

        mApplyBtn.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_activities_apply);
        });

        return view;
    }
}
