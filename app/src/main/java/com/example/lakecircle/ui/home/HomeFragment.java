package com.example.lakecircle.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.lakecircle.R;

public class HomeFragment extends Fragment {

    private SearchView mSearchView;
    private TextView mTvAddress, mTvTimeLake, mTvTravel, mTvLakeWalk, mTvLightLake;
    private ImageView mIvProblem, mIvActivity, mIvMerchant, mIvNotice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mSearchView = view.findViewById(R.id.searchView);
        mTvAddress = view.findViewById(R.id.home_address);
        mTvLakeWalk = view.findViewById(R.id.home_tv_lakewalk);
        mTvLightLake = view.findViewById(R.id.home_tv_lightlake);
        mTvTimeLake = view.findViewById(R.id.home_tv_timelake);
        mTvTravel = view.findViewById(R.id.home_tv_travel);
        mIvActivity = view.findViewById(R.id.home_iv_detail);
        mIvMerchant = view.findViewById(R.id.home_iv_merchant);
        mIvProblem = view.findViewById(R.id.home_iv_problem);
        mIvNotice = view.findViewById(R.id.home_iv_notice);

        initView();

        return view;
    }

    private void initView() {

        mTvAddress.setOnClickListener(v -> {

        });

        mTvLakeWalk.setOnClickListener(v -> {

        });

        mTvLightLake.setOnClickListener(v -> {

        });

        mTvTimeLake.setOnClickListener(v -> {

        });

        mTvTravel.setOnClickListener(v -> {

        });

        mIvActivity.setOnClickListener(v -> {

        });

        mIvMerchant.setOnClickListener(v -> {

        });

        mIvProblem.setOnClickListener(v -> {

        });
    }
}
