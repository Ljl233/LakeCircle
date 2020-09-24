package com.example.lakecircle.ui.mainControl;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.example.lakecircle.R;
import com.example.lakecircle.commonUtils.NetUtil;
import com.example.lakecircle.commonUtils.SPUtils;
import com.example.lakecircle.ui.circle.CircleFragment;
import com.example.lakecircle.ui.home.home.HomeFragment;
import com.example.lakecircle.ui.home.journey.JourneyFragment;
import com.example.lakecircle.ui.home.light.model.UserInfoBean;
import com.example.lakecircle.ui.mine.MineFragment;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        getUserInfo();


        NavHostFragment host = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        NavController navController = host.getNavController();

        setUpBottomNav(navController);

    }

    private void setUpBottomNav(NavController controller) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(bottomNavigationView, controller);
    }

    void initView() {
//        mViewPager = findViewById(R.id.view_pager);
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

//        mBottomNavView.setOnNavigationItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.nav_menu_home:
//                    mViewPager.setCurrentItem(0);
//                    break;
//                case R.id.nav_menu_journey:
//                    mViewPager.setCurrentItem(1);
//                    break;
//                case R.id.nav_menu_circle:
//                    mViewPager.setCurrentItem(2);
//                    break;
//                case R.id.nav_menu_mine:
//                    mViewPager.setCurrentItem(3);
//                    break;
//                default:
//                    break;
//            }
//            return false;
//        });
    }

    List<Fragment> initFragment() {
        List<Fragment> fragments = new ArrayList<>();

        fragments.add(new HomeFragment());
        fragments.add(new JourneyFragment());
        fragments.add(new CircleFragment());
        fragments.add(new MineFragment());

        return fragments;
    }

    private void getUserInfo() {
        SPUtils spUtils = SPUtils.getInstance("userInfo");
        NetUtil.getInstance().getApi().getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserInfoBean>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(UserInfoBean userInfoBean) {
                        if( userInfoBean != null ) {
                            spUtils.put("avatar", userInfoBean.getData().getAvatar());
                            spUtils.put("kind", userInfoBean.getData().getKind());
                            spUtils.put("username", userInfoBean.getData().getUsername());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
