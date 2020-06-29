package com.example.lakecircle.ui.mine.coupon;

import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
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

public class MyCouponFragment extends Fragment {

    private static int page = 1;

    private Toolbar mTb;
    private RecyclerView mRv;
    private CouponAdapter mAdapter;
    private CouponItemListener mCouponItemListener = new CouponItemListener() {
        @Override
        public void onItemClickListener() {
            useCoupon();
        }

        @Override
        public void onMoreClickListener() {
            getData(false);
        }

        @Override
        public void onRefreshClickListener() {
            getData(true);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_coupon, container, false);

        mTb = view.findViewById(R.id.tb_my_coupon);
        mRv = view.findViewById(R.id.rv_my_coupon);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTb.setNavigationOnClickListener(v -> {
                NavHostFragment.findNavController(this).popBackStack();
            });
        }

        mAdapter = new CouponAdapter(new ArrayList<>(), mCouponItemListener);
        mRv.setLayoutManager(new LinearLayoutManager(getContext()));

        getData(true);

        return view;
    }

    private void useCoupon( ) {
//        NetUtil.getInstance().getApi().consumeCoupon()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<SimpleResponse>() {
//                    @Override
//                    public void onSubscribe(Disposable d) { }
//
//                    @Override
//                    public void onNext(SimpleResponse simpleResponse) {
//                        if ( !simpleResponse.getMessage().equals("OK") )
//                            onError(new Throwable());
//                        showSuccess("使用成功");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        showError("使用失败");
//                    }
//
//                    @Override
//                    public void onComplete() { }
//                });
    }


    private void getData(boolean ifRefresh) {
        if ( ifRefresh ) page = 1;
        NetUtil.getInstance().getApi().getCoupons(page+"", "10")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseModel<Coupon>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(BaseResponseModel<Coupon> couponBaseResponseModel) {
                        List<Coupon> coupons = couponBaseResponseModel.getData();
                        if ( page == 1 )
                            mAdapter.refresh(coupons);
                        else {
                            mAdapter.add(coupons);
                            page++;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showError("获取失败");
                    }

                    @Override
                    public void onComplete() { }
                });
    }

    public interface CouponItemListener {
        void onItemClickListener();
        void onMoreClickListener();
        void onRefreshClickListener();
    }

    public class CouponAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Coupon> coupons;
        private CouponItemListener mCouponItemListener;

        public CouponAdapter(List<Coupon> couponList, CouponItemListener couponItemListener) {
            coupons = couponList;
            mCouponItemListener = couponItemListener;
        }

        public void refresh(List<Coupon> couponList) {
            int preSize = coupons.size();
            coupons.clear();
            notifyItemRangeRemoved(0,preSize);
            coupons.addAll(couponList);
            notifyItemRangeInserted(0,couponList.size());
        }

        public void add(List<Coupon> couponList) {
            int preSize = coupons.size();
            coupons.addAll(couponList);
            notifyItemRangeInserted(preSize,preSize+couponList.size());
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {
                case 0:
                    return new CouponViewHolder(
                            LayoutInflater.from(parent.getContext()).inflate(
                                    R.layout.item_coupon, parent, false));
                case 1:
                    return new NoneViewHolder(
                            LayoutInflater.from(parent.getContext()).inflate(
                                    android.R.layout.simple_list_item_1, parent, false));
                default:
                    return new MoreViewHolder(
                            LayoutInflater.from(parent.getContext()).inflate(
                                    android.R.layout.simple_list_item_1, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if ( holder instanceof CouponViewHolder ) {
                CouponViewHolder h = (CouponViewHolder) holder;
                h.mDenominationTv.setText(coupons.get(position).getDenomination());
                h.mNameTv.setText(coupons.get(position).getName());
                h.mUserBtn.setOnClickListener(v -> {
                    mCouponItemListener.onItemClickListener();
                    notifyItemRemoved(position);
                    coupons.remove(position);
                });
            } else if ( holder instanceof NoneViewHolder ) {
                NoneViewHolder h = (NoneViewHolder) holder;
                h.textView.setText("暂时没有这类问题,点击尝试刷新");
                h.textView.setGravity(Gravity.CENTER);
                h.textView.setOnClickListener(v -> mCouponItemListener.onRefreshClickListener());
            } else {
                MoreViewHolder h = (MoreViewHolder) holder;

                h.textView.setText("点击加载更多");
                h.textView.setGravity(Gravity.CENTER);
                h.textView.setOnClickListener(v -> mCouponItemListener.onMoreClickListener());
            }
        }

        @Override
        public int getItemViewType(int position) {
            if ( coupons.size() == 0 )
                return 1;
            else if ( position == coupons.size() )
                return 2;
            else
                return 0;
        }

        @Override
        public int getItemCount() {
            if ( coupons.size() == 0 )
                return 1;
            else
                return coupons.size()+1;
        }

        public class CouponViewHolder extends RecyclerView.ViewHolder {
            private TextView mDenominationTv, mNameTv;
            private Button mUserBtn;

            public CouponViewHolder(View itemView) {
                super(itemView);

                mDenominationTv = itemView.findViewById(R.id.tv_item_coupon_amount);
                mNameTv = itemView.findViewById(R.id.tv_item_coupon_merchant);
                mUserBtn = itemView.findViewById(R.id.btn_item_coupon_use);
            }
        }


        public class NoneViewHolder extends RecyclerView.ViewHolder {

            private TextView textView;

            public NoneViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(android.R.id.text1);
            }
        }

        public class MoreViewHolder extends RecyclerView.ViewHolder {

            private TextView textView;

            public MoreViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(android.R.id.text1);
            }
        }
    }


    private void showSuccess(String s) {
        Objects.requireNonNull(getContext()).setTheme(R.style.QMUITheme);
        QMUITipDialog tipDialog = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord(s)
                .create();
        tipDialog.show();
        this.getView().postDelayed(() -> {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getContext()).setTheme(R.style.AppTheme);
    }

}
