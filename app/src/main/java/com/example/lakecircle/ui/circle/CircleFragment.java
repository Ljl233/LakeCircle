package com.example.lakecircle.ui.circle;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lakecircle.R;
import com.example.lakecircle.commonUtils.BaseResponseModel;
import com.example.lakecircle.commonUtils.NetUtil;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CircleFragment extends Fragment {

    private static int page = 1;

    private Toolbar mToolbar;
    private RecyclerView mCircleRv;
    private CircleAdapter mAdapter;
    private AppCompatImageButton mAddIb;

    private CircleAdapter.OnCircleClickListener mOnCircleClickListener = new CircleAdapter.OnCircleClickListener() {
        @Override
        public void onClick(String url) { PhotoViewActivity.start(getContext(), url); }

        @Override
        public void onMoreClick() { getCircles(false); }

        @Override
        public void onRefreshClick() { getCircles(true); }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_circle, container, false);

        mToolbar = view.findViewById(R.id.tb_circle);
        mCircleRv = view.findViewById(R.id.rv_circle);
        mAddIb = view.findViewById(R.id.ib_circle);

        mAddIb.setOnClickListener(v -> startActivityForResult(new Intent(getContext(), PostCircleActivity.class), 1));

        mAdapter = new CircleAdapter(new ArrayList<>(),mOnCircleClickListener, getContext());
        mCircleRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mCircleRv.setAdapter(mAdapter);

        getCircles(true);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if ( requestCode == 1 )
            if ( resultCode == 1 )
                getCircles(true);

    }

    private void getCircles(boolean ifRefresh) {
        if ( ifRefresh ) page = 1;
        NetUtil.getInstance().getApi().getCircles(page+"", "15")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseModel<Circle>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(BaseResponseModel<Circle> circleBaseResponseModel) {
                        List<Circle> circles = circleBaseResponseModel.getData();
                        if ( page == 1 )
                            mAdapter.refresh(circles);
                        else {
                            mAdapter.add(circles);
                            page++;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showError("河湖圈获取失败，请重试");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getContext()).setTheme(R.style.AppTheme);
    }

}
