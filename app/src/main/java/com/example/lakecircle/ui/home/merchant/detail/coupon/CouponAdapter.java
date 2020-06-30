package com.example.lakecircle.ui.home.merchant.detail.coupon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lakecircle.R;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.VH> {

    private Context mContext;
    private OnCouponClickListener mOnCouponClickListener;

    public CouponAdapter(Context context, OnCouponClickListener onCouponClickListener) {
        mContext = context;
        mOnCouponClickListener = onCouponClickListener;
    }

    public interface OnCouponClickListener {
        void onClick(int cost);
    }

    @NonNull
    @Override
    public CouponAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(mContext).inflate(
                R.layout.item_merchant_coupon, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CouponAdapter.VH holder, int position) {
        String format1 = mContext.getResources().getResourceName(R.string.merchant_coupon);
        String format2 = mContext.getResources().getResourceName(R.string.merchant_coupon_denomination);
        switch (position) {
            case 0 : {
                holder.mCostTv.setText(String.format(format1, 5));
                holder.mDenominationTv.setText(String.format(format2, 100));
                holder.mExchangeBtn.setOnClickListener(v -> mOnCouponClickListener.onClick(5));
                break;
            }
            case 1: {
                holder.mCostTv.setText(String.format(format1, 30));
                holder.mDenominationTv.setText(String.format(format2, 500));
                holder.mExchangeBtn.setOnClickListener(v -> mOnCouponClickListener.onClick(30));
                break;
            }
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    class VH extends RecyclerView.ViewHolder {
        private TextView mCostTv, mDenominationTv;
        private Button mExchangeBtn;

        public VH(View itemView) {
            super(itemView);
            mCostTv = itemView.findViewById(R.id.tv_cost_item_merchant_coupon);
            mDenominationTv = itemView.findViewById(R.id.tv_denomination_item_merchant_coupon);
            mExchangeBtn = itemView.findViewById(R.id.btn_item_merchant_coupon);
        }
    }
}
