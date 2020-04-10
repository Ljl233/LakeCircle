package com.example.lakecircle.ui.mine.view.govern;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import com.example.lakecircle.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ProblemViewFragment extends Fragment {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_problem_view, container, false);

        mToolbar = view.findViewById(R.id.tb_problem_view);
        mTabLayout = view.findViewById(R.id.tl_problem_view);
        mViewPager = view.findViewById(R.id.vp_problem_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setNavigationOnClickListener(v -> {
                NavHostFragment.findNavController(this).popBackStack();
            });
        }

        mViewPager.setAdapter(new MyAdapter(getActivity().getSupportFragmentManager(),2));
        mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    class MyAdapter extends FragmentPagerAdapter {

        private String[] mTitles = new String[]{"待解决", "已解决"};

        public MyAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if ( position == 0 )
                return new ProblemListFragment(1);
            else
                return new ProblemListFragment(0);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }
}
