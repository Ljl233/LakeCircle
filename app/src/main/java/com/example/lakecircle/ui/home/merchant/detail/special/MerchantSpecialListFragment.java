package com.example.lakecircle.ui.home.merchant.detail.special;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class MerchantSpecialListFragment extends Fragment {

    private int ID = 0;
    private static int page = 1;

    private RecyclerView mRv;
    private SpecialAdapter mAdapter;
    private SpecialAdapter.OnSpecialClickListener mOnSpecialClickListener = new SpecialAdapter.OnSpecialClickListener() {
        @Override
        public void onMore() { getSpecials(false);}

        @Override
        public void onRefresh() { getSpecials(true);}
    };

    public static MerchantSpecialListFragment getInstance(int id) {
        MerchantSpecialListFragment fragment = new MerchantSpecialListFragment();
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
        View view = inflater.inflate(R.layout.fragment_merchant_detail_list, container , false);

        mRv = view.findViewById(R.id.rv_merchant_detail_list);
        mAdapter = new SpecialAdapter(new ArrayList<>(), getContext(), mOnSpecialClickListener);
        mRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mRv.setAdapter(mAdapter);

        getSpecials(true);

        return view;
    }

    private void getSpecials(boolean ifRefresh) {
        if (ifRefresh) page = 1 ;
        NetUtil.getInstance().getApi().getSpecials(ID,page+"", "20")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseModel<SpecialListResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(BaseResponseModel<SpecialListResponse> specialListResponseBaseResponseModel) {
                        List<SpecialListResponse> specials = specialListResponseBaseResponseModel.getData();
                        if (page == 1)
                            mAdapter.refresh(specials);
                        else {
                            mAdapter.add(specials);
                            page++;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("MerSpecialListFragment", "get special list fail");
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
