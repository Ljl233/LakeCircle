package com.example.lakecircle.ui.home.merchant.detail.coupon;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lakecircle.R;
import com.example.lakecircle.commonUtils.NetUtil;
import com.example.lakecircle.ui.mine.SimpleResponse;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MerchantCouponListFragment extends Fragment {

    private int ID;
    private RecyclerView mRv;
    private CouponAdapter mAdapter;
    private CouponAdapter.OnCouponClickListener mOnCouponClickListener = new CouponAdapter.OnCouponClickListener() {
        @Override
        public void onClick(int cost) {
            exchangeCoupon(new ExchangeCouponBean(ID, cost));
        }
    };

    public static MerchantCouponListFragment getInstance(int id) {
        MerchantCouponListFragment fragment = new MerchantCouponListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ID = getArguments().getInt("id");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_merchant_detail_list, container, false);

        mRv = view.findViewById(R.id.rv_merchant_detail_list);

        mAdapter = new CouponAdapter(getContext(), mOnCouponClickListener);
        mRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mRv.setAdapter(mAdapter);

        return view;
    }

    private void exchangeCoupon(ExchangeCouponBean bean) {
        NetUtil.getInstance().getApi().exchangeCoupon(bean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SimpleResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        if (!simpleResponse.getMessage().equals("OK"))
                            onError(new Throwable());
                        showSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("MerCouponListFragment", "exchange coupon fail");
                        showError("兑换失败");
                    }

                    @Override
                    public void onComplete() { }
                });
    }

    private void showSuccess() {
        Objects.requireNonNull(getContext()).setTheme(R.style.QMUITheme);
        QMUITipDialog tipDialog = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord("兑换成功")
                .create();
        tipDialog.show();
        getView().postDelayed(() -> {
            Objects.requireNonNull(getContext()).setTheme(R.style.AppTheme);
            tipDialog.dismiss();
        }, 1500);
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
