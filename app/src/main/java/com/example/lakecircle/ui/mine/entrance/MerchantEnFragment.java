package com.example.lakecircle.ui.mine.entrance;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.lakecircle.R;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class MerchantEnFragment extends Fragment {

    private Toolbar mToolbar;
    private LinearLayout mInfoLl, mSpecialLl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_merchant_entrance, container, false);
        NavController navController = findNavController(this);

        mToolbar = view.findViewById(R.id.tb_mer_en);
        mInfoLl = view.findViewById(R.id.ll_info_mer_en);
        mSpecialLl = view.findViewById(R.id.ll_special_mer_en);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setNavigationOnClickListener(v -> {
                NavHostFragment.findNavController(this).popBackStack();
            });
        }

        mSpecialLl.setClickable(true);
        mInfoLl.setClickable(true);

        mInfoLl.setOnClickListener(v -> {
            navController.navigate(R.id.action_activity_post);
        });

        mSpecialLl.setOnClickListener(v -> {
            navController.navigate(R.id.action_problem_view);
        });

        return view;
    }
}
