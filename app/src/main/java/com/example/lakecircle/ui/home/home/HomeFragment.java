package com.example.lakecircle.ui.home.home;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.lakecircle.R;

public class HomeFragment extends Fragment {

    private SearchView mSearchView;
    private TextView mTvAddress, mTvTimeLake, mTvTravel, mTvLakeWalk, mTvLightLake;
    private ImageView mIvProblem, mIvActivity, mIvMerchant, mIvNotice;
    private NavController mNavController;
    private String TAG = "HomeFragment: ";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        view.setOnClickListener(v -> {
            if (mSearchView.isFocused())
                mSearchView.clearFocus();
        });
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

        mNavController = NavHostFragment.findNavController(this);

        initView();

        return view;
    }

    private void initView() {
        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.e(TAG, v.toString() + ",hasFocus:" + hasFocus);
            }
        });
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "OnSearchClickListener", Toast.LENGTH_SHORT).show();
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e(TAG, ":" + query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e(TAG, ":" + newText);
                return false;
            }
        });

        mTvAddress.setOnClickListener(v -> {

        });

        mTvLakeWalk.setOnClickListener(v -> {

        });

        mTvLightLake.setOnClickListener(v -> {
            NavOptions options = new NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_in_right)
                    .setExitAnim(R.anim.hide_out)
                    .setPopEnterAnim(R.anim.show_in)
                    .setPopExitAnim(R.anim.slide_out_right)
                    .build();
            mNavController.navigate(R.id.light_dest, null, options);
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
            NavOptions options = new NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_in_right)
                    .setExitAnim(R.anim.hide_out)
                    .setPopEnterAnim(R.anim.show_in)
                    .setPopExitAnim(R.anim.slide_out_right)
                    .build();
            mNavController.navigate(R.id.ulq_dest, null, options);
        });
    }
}
