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

public class GovernEnFragment extends Fragment {

    private LinearLayout mActivityLl, mProblemLl;
    private Toolbar mToolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_government_entrance,container,false);
        NavController navController = findNavController(this);

        mActivityLl = view.findViewById(R.id.ll_activity_govern_en);
        mProblemLl = view.findViewById(R.id.ll_problem_govern_en);
        mToolbar = view.findViewById(R.id.tb_govern_en);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setNavigationOnClickListener(v -> {
                NavHostFragment.findNavController(this).popBackStack();
            });
        }

        mActivityLl.setClickable(true);
        mProblemLl.setClickable(true);

        mActivityLl.setOnClickListener(v -> {
            navController.navigate(R.id.action_activity_post);
        });

        mProblemLl.setOnClickListener(v -> {
            navController.navigate(R.id.action_problem_view);
        });

        return view;
    }
}
