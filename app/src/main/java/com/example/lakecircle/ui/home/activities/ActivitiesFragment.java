package com.example.lakecircle.ui.home.activities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;
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

public class ActivitiesFragment extends Fragment {

    private Toolbar mToolbar;
    private AppCompatSpinner mZoneSp;
    private ImageButton mZoneIb;
    private SearchView mSearchSv;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayAdapter<String> mSpAdapter;
    private List<String> mZoneList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activities, container, false);

        mToolbar = view.findViewById(R.id.tb_activities);
        mZoneSp = view.findViewById(R.id.spn_zone_activities);
        mZoneIb = view.findViewById(R.id.ib_zone_activities);
        mSearchSv = view.findViewById(R.id.sv_activities);
        mTabLayout = view.findViewById(R.id.tl_activities);
        mViewPager = view.findViewById(R.id.vp_activities);

        mZoneList.add("武汉");
        mZoneList.add("北京");
        mZoneList.add("上海");
        mZoneList.add("广州");
        mZoneList.add("深圳");

        mSpAdapter = new ArrayAdapter<>(getContext(),R.layout.spinner_activities_layout, mZoneList);
        mSpAdapter.setDropDownViewResource(R.layout.spinner_activities_dropdown_item);

        mZoneSp.setBackgroundColor(0x0);
        mZoneSp.setAdapter(new SpinnerAdapter(getContext(), mZoneList));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setNavigationOnClickListener(v -> {
                NavHostFragment.findNavController(this).popBackStack();
            });
        }

        mZoneIb.setOnClickListener(v -> mZoneSp.performClick());

        mViewPager.setAdapter(new MyAdapter(getActivity().getSupportFragmentManager(),2));
        mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    public class MyAdapter extends FragmentPagerAdapter {

        private String[] mTitles = new String[]{"正在招募的活动", "已结束的活动"};

        public MyAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if ( position == 0 )
                return new ActivitiesListFragment(1);
            else
                return new ActivitiesListFragment(0);
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

    public class SpinnerAdapter extends BaseAdapter {

        private List<String> dataList;
        private Context mContext;

        public SpinnerAdapter(Context mContext, List<String> list) {
            this.mContext = mContext;
            dataList = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            TextView textView = view.findViewById(android.R.id.text1);
            textView.setText(dataList.get(position));
            return view;
        }

        @Override
        public int getCount() {
            return dataList == null ? 0 : dataList.size();
        }

        @Override
        public String getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

    }

}
