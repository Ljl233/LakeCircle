package com.example.lakecircle.ui.home.light;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.lakecircle.R;
import com.facebook.drawee.view.SimpleDraweeView;

public class LightFragment extends Fragment {

    SearchView mSearchView;
    TextView mTvGrades, mTvStarts, mTvRank;
    SimpleDraweeView mIvLake;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_light, container, false);

        Toolbar toolbar = root.findViewById(R.id.light_toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        mTvRank = root.findViewById(R.id.light_rank);
        mTvRank.setOnClickListener(
                v -> NavHostFragment.findNavController(this).navigate(R.id.rank_dest)
        );
        mTvGrades = root.findViewById(R.id.light_grades);
        mTvStarts = root.findViewById(R.id.light_starts);
        return root;
    }

    public void setGrades(String grades) {
        mTvGrades.setText(grades);
    }

    public void setStarts(String starts) {
        mTvStarts.setText(starts);
    }
}
