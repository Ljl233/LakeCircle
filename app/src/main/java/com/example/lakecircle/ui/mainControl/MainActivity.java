package com.example.lakecircle.ui.mainControl;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.lakecircle.R;
import com.example.lakecircle.ui.circle.CircleFragment;
import com.example.lakecircle.ui.home.HomeFragment;
import com.example.lakecircle.ui.journey.JourneyFragment;
import com.example.lakecircle.ui.mine.MineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().
                    setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        initFragment();
        initView();


    }

    void initView() {
        mViewPager = findViewById(R.id.view_pager);
        mBottomNavView = findViewById(R.id.nav_view);

        mViewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager(), initFragment()));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBottomNavView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_menu_home:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.nav_menu_journey:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.nav_menu_circle:
                        mViewPager.setCurrentItem(2);
                        break;
                    case R.id.nav_menu_mine:
                        mViewPager.setCurrentItem(3);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    List<Fragment> initFragment() {
        List<Fragment> fragments = new ArrayList<>();

        fragments.add(new HomeFragment());
        fragments.add(new JourneyFragment());
        fragments.add(new CircleFragment());
        fragments.add(new MineFragment());

        return fragments;
    }
}
