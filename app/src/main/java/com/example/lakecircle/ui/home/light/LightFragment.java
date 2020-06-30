package com.example.lakecircle.ui.home.light;

import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.lakecircle.R;
import com.example.lakecircle.commonUtils.NetUtil;
import com.example.lakecircle.ui.home.light.model.LakeNameBean;
import com.example.lakecircle.ui.home.light.model.LakeUrlResponse;
import com.example.lakecircle.ui.home.light.model.StarResponse;
import com.example.lakecircle.ui.home.light.model.UserInfoBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LightFragment extends Fragment {

    private SearchView mSearchView;
    private TextView mTvGrades, mTvStarts, mTvRank;
    private SimpleDraweeView mIvLake, mImAvatar;
    private ImageView mIvStar;
    private String lakeName;
    private String unStarLakeUrl;
    private String staredLakeUrl;
    private boolean isStared;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_light, container, false);

        Toolbar toolbar = root.findViewById(R.id.light_toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        mImAvatar = root.findViewById(R.id.light_avatar);
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
        getUserInfo();

        mSearchView = root.findViewById(R.id.light_searchview);
        initSearchView();
        mIvLake = root.findViewById(R.id.iv_light_lake);
        return root;
    }

    private void initSearchView() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLake(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void searchLake(String query) {
        LakeNameBean nameBean = new LakeNameBean();
        lakeName = resolveQuery(query);

        nameBean.setName(lakeName);
        NetUtil.getInstance().getApi().getStartInfo(nameBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LakeUrlResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LakeUrlResponse lakeUrlResponse) {
                        staredLakeUrl = lakeUrlResponse.getData().getOutline_after_url();
                        unStarLakeUrl = lakeUrlResponse.getData().getOutline_before_url();
                        isStared = lakeUrlResponse.getData().isIs_star();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("Light:", e.getMessage());
                        Toast.makeText(LightFragment.this.getContext(), "搜索失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        if (isStared) {
                            mIvStar.setClickable(false);
                            mIvStar.setImageResource(R.drawable.ic_star_24dp);
                            showLakePicture(staredLakeUrl);
                        } else {
                            mIvStar.setClickable(true);
                            showLakePicture(unStarLakeUrl);
                        }

                        Objects.requireNonNull(getActivity()).getCurrentFocus().clearFocus();
                        mSearchView.clearFocus();

                    }
                });
    }

    private String resolveQuery(String query) {
        return query;
    }

    private void getUserInfo() {
        NetUtil.getInstance().getApi().getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserInfoBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserInfoBean userInfoBean) {
                        setGrades(String.valueOf(
                                userInfoBean.getData().getUsing_mileage()));
                        setStarts(String.valueOf(
                                userInfoBean.getData().getStar_num()
                        ));
                        mImAvatar.setImageURI(userInfoBean.getData().getAvatar());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(LightFragment.this.getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        mIvStar.setOnClickListener(v -> {
                            lightLake();
                        });
                    }
                });
    }

    private void lightLake() {
        if (lakeName == null || staredLakeUrl == null || unStarLakeUrl == null) {
            Toast.makeText(LightFragment.this.getContext(), "请搜索想要点亮的河湖", Toast.LENGTH_SHORT).show();
        } else {
            LakeNameBean nameBean = new LakeNameBean();
            nameBean.setName(lakeName);
            NetUtil.getInstance().getApi().starLake(nameBean)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<StarResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(StarResponse starResponse) {
                            Toast.makeText(LightFragment.this.getContext(),
                                    "点亮成功",
                                    Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(LightFragment.this.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                            mIvStar.setImageResource(R.drawable.ic_star_24dp);
                            showLakePicture(staredLakeUrl);
                            isStared = true;
                            mIvStar.setClickable(false);
                            getUserInfo();
                        }
                    });

        }
    }


    public void showLakePicture(String lakeAddress) {
        mIvLake.setImageURI(lakeAddress);
    }

    public void setGrades(String grades) {
        mTvGrades.setText(grades);
    }

    public void setStarts(String starts) {
        mTvStarts.setText(starts);
    }
}
