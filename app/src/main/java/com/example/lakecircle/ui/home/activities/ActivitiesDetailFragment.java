package com.example.lakecircle.ui.home.activities;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.lakecircle.R;
import com.example.lakecircle.commonUtils.NetUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ActivitiesDetailFragment extends Fragment {

    private int ID = 0;

    private Toolbar mToolbar;
    private TextView mTitleTv, mTypeTv, mPlaceTv, mTimeTv, mNumTv, mContentTv, mPointTv;
    private SimpleDraweeView mPicSdv;
    private Button mApplyBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ( getArguments() != null )
            ID = getArguments().getInt("id");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activities_detail, container, false);

        mToolbar = view.findViewById(R.id.tb_activities_detail);
        mTitleTv = view.findViewById(R.id.tv_title_activities_detail);
        mTypeTv = view.findViewById(R.id.tv_type2_activities_detail);
        mPlaceTv = view.findViewById(R.id.tv_place2_activities_detail);
        mTimeTv = view.findViewById(R.id.tv_time2_activities_detail);
        mNumTv = view.findViewById(R.id.tv_num2_activities_detail);
        mContentTv = view.findViewById(R.id.tv_content2_activities_detail);
        mPointTv = view.findViewById(R.id.tv_point2_activities_detail);
        mPicSdv = view.findViewById(R.id.sdv_activities_detail);
        mApplyBtn = view.findViewById(R.id.btn_apply_activities_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setNavigationOnClickListener(v -> {
                NavHostFragment.findNavController(this).popBackStack();
            });
        }

        getActivityInfo();

        mApplyBtn.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_activities_apply);
        });

        return view;
    }

    private void getActivityInfo() {
        NetUtil.getInstance().getApi().getActivity(ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ActivityInfoResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(ActivityInfoResponse activityInfoResponse) {
                        ActivityInfoResponse.DataBean d = activityInfoResponse.getData();
                        mTitleTv.setText(d.getName());
                        mContentTv.setText(d.getDetail());
                        mNumTv.setText(d.getUserNum());
                        mPlaceTv.setText(d.getLocation());
                        mPointTv.setText(d.getReward());
                        mTypeTv.setText(d.getKind());
                        mTimeTv.setText(d.getDuration());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("ActivityDetailFragment", "get activity info fail");
                        showError("加载失败");
                    }

                    @Override
                    public void onComplete() { }
                });
    }


    private void showError(String error) {
        Objects.requireNonNull(getContext()).setTheme(R.style.QMUITheme);
        QMUITipDialog tipDialog = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord(error)
                .create();
        tipDialog.show();
        this.getView().postDelayed(() -> {
            tipDialog.dismiss();
            Objects.requireNonNull(getContext()).setTheme(R.style.AppTheme);
        }, 1500);
    }
}
