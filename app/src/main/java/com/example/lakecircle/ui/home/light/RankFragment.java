package com.example.lakecircle.ui.home.light;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import com.example.lakecircle.R;
import com.example.lakecircle.commonUtils.NetUtil;
import com.example.lakecircle.ui.home.light.model.UserInfoBean;
import com.example.lakecircle.ui.home.light.rank.LakeRankFragment;
import com.example.lakecircle.ui.home.light.rank.UserRankFragment;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class RankFragment extends Fragment {

    private TextView mTvUsername, mTvRanking, mTvStarts, mTvMyLakes;
    private TabLayout mTabs;
    private ViewPager mViewPager;
    private SimpleDraweeView mIvAvatar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rank, container, false);
        mTvUsername = root.findViewById(R.id.rank_user_name);
        mTvRanking = root.findViewById(R.id.rank_ranking);
        mTvStarts = root.findViewById(R.id.rank_starts);
        mTabs = root.findViewById(R.id.rank_tabs);
        mViewPager = root.findViewById(R.id.rank_content);
        mIvAvatar = root.findViewById(R.id.rank_portrait);
        Toolbar toolbar = root.findViewById(R.id.rank_toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        mTvMyLakes = root.findViewById(R.id.textView13);
        mTvMyLakes.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.lighted_dest);
        });
        initTabsAndViewPager();
        initView();
        return root;
    }

    private void initView() {
        NetUtil.getInstance().getApi().getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserInfoBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserInfoBean userInfoBean) {
                        mTvUsername.setText(userInfoBean.getData().getUsername());
                        mTvStarts.setText(String.valueOf(userInfoBean.getData().getStar_num()));
                        String s = "第" + userInfoBean.getData().getRank() + "名";
                        mTvRanking.setText(s);
                        mIvAvatar.setImageURI(userInfoBean.getData().getAvatar());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(RankFragment.this.getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
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
        UserRankFragment userRankFragment = new UserRankFragment();
        userRankFragment.setContext(this.getContext());
        LakeRankFragment lakeRankFragment = new LakeRankFragment();
        lakeRankFragment.setContext(this.getContext());
        fragments.add(userRankFragment);
        fragments.add(lakeRankFragment);
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
