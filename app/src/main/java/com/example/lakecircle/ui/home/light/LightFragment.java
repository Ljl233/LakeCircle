package com.example.lakecircle.ui.home.light;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.lakecircle.R;
import com.facebook.drawee.view.SimpleDraweeView;

public class LightFragment extends Fragment {

    private SearchView mSearchView;
    private TextView mTvGrades, mTvStarts, mTvRank;
    private SimpleDraweeView mIvLake;
    private ImageView mIvStar;

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
                v -> NavHostFragment.findNavController(this)
                        .navigate(R.id.rank_dest, null,
                                new NavOptions.Builder()
                                        .setEnterAnim(R.anim.slide_in_right)
                                        .setExitAnim(R.anim.hide_out)
                                        .setPopEnterAnim(R.anim.show_in)
                                        .setPopExitAnim(R.anim.slide_out_right)
                                        .build())
        );
        mTvGrades = root.findViewById(R.id.light_grades);
        mTvStarts = root.findViewById(R.id.rank_starts);
        mIvStar = root.findViewById(R.id.light_star);
        mIvStar.setOnClickListener(v -> {
            lightLake();
        });
        return root;
    }

    private void lightLake() {
        mIvStar.setImageResource(R.drawable.ic_star_24dp);
    }

    public void showLakePicture(String lakeAddress) {
        mIvLake.setImageURI(lakeAddress);
    }

    public void showLakePicture(int resource) {
        mIvLake.setImageResource(resource);
    }


    public void setGrades(String grades) {
        mTvGrades.setText(grades);
    }

    public void setStarts(String starts) {
        mTvStarts.setText(starts);
    }
}
