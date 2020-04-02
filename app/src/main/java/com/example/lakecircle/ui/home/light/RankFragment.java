package com.example.lakecircle.ui.home.light;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import com.example.lakecircle.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class RankFragment extends Fragment {

    private TextView mTvUsername, mTvRanking, mTvStarts;
    private TabLayout mTabs;
    private ViewPager mViewPager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rank, container, false);
        mTvUsername = root.findViewById(R.id.rank_user_name);
        mTvRanking = root.findViewById(R.id.rank_ranking);
        mTvStarts = root.findViewById(R.id.rank_starts);
        mTabs = root.findViewById(R.id.rank_tabs);
        mViewPager = root.findViewById(R.id.rank_content);
        Toolbar toolbar = root.findViewById(R.id.rank_toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });
        initTabsAndViewPager();
        return root;
    }

    private void initTabsAndViewPager() {
        mTabs.setupWithViewPager(mViewPager);
        //这个里的fm应该由getChildFragmentManager()获取，而不是getParentFragmentManager()
        mViewPager.setAdapter(new MyViewPagerAdapter(getChildFragmentManager(), initFragments()));
    }


    public void setUserName(String name) {
        mTvUsername.setText(name);
    }

    public void setRanking(String ranking) {
        ranking = "第" + ranking + "名";
        mTvRanking.setText(ranking);
    }

    public void setStarts(int starts) {
        mTvStarts.setText(String.valueOf(starts));
    }


    private List<Fragment> initFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new UserRankFragment());
        fragments.add(new LakeRankFragment());
        return fragments;
    }

    class MyViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        private String[] titles = {"用户排行榜", "河湖排行榜"};

        MyViewPagerAdapter(@NonNull FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

}
