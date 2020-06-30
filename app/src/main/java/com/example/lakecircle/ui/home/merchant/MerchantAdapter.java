package com.example.lakecircle.ui.home.merchant;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lakecircle.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class MerchantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Merchant> merchants;
    private Context mContext;
    private OnMerchantClickListener mOnMerchantClickListener;

    public MerchantAdapter(List<Merchant> merchantList, Context context, OnMerchantClickListener onMerchantClickListener) {
        merchants = merchantList;
        mContext = context;
        mOnMerchantClickListener = onMerchantClickListener;
    }

    public interface OnMerchantClickListener {
        void onItemClick(Merchant merchant);
        void onMoreClick();
        void onRefreshClick();
    }

    public void refresh(List<Merchant> merchantList) {
        int preSize = merchants.size();
        merchants.clear();
        notifyItemRangeRemoved(0,preSize);
        merchants.addAll(merchantList);
        notifyItemRangeInserted(0, merchantList.size());
    }

    public void add(List<Merchant> merchantList) {
        int preSize = merchants.size();

        merchants.addAll(merchantList);
        notifyItemRangeInserted(preSize,preSize + merchantList.size());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new MerchantViewHolder(LayoutInflater.from(mContext).inflate(
                        R.layout.item_merchant_list, parent, false));
            case 1:
                return new NoneViewHolder(LayoutInflater.from(mContext).inflate(
                        android.R.layout.simple_list_item_1, parent, false));
            default:
                return new MoreViewHolder(LayoutInflater.from(mContext).inflate(
                        android.R.layout.simple_list_item_1, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if ( holder instanceof MerchantViewHolder ) {
            MerchantViewHolder h = (MerchantViewHolder) holder;
            h.mPicSdv.setImageURI(merchants.get(position).getAvatar_url());
            h.mNameTv.setText(merchants.get(position).getName());
            h.mIntroTv.setText(merchants.get(position).getIntro());
            h.itemView.setOnClickListener(v -> mOnMerchantClickListener.onItemClick(merchants.get(position)));
        } else if ( holder instanceof NoneViewHolder) {
            NoneViewHolder mHolder = (NoneViewHolder) holder;
            mHolder.textView.setText("找不到商家,点击尝试刷新");
            mHolder.textView.setGravity(Gravity.CENTER);
            mHolder.textView.setOnClickListener(v -> mOnMerchantClickListener.onRefreshClick());
        } else {
            MoreViewHolder mHolder = (MoreViewHolder) holder;
            mHolder.textView.setText("点击加载更多");
            mHolder.textView.setGravity(Gravity.CENTER);
            mHolder.textView.setOnClickListener(v -> mOnMerchantClickListener.onMoreClick());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if ( merchants.size() == 0 )
            return 1;
        else if ( position == merchants.size() )
            return 2;
        else
            return 0;
    }

    @Override
    public int getItemCount() {
        return merchants.size() + 1;
    }

    class MerchantViewHolder extends RecyclerView.ViewHolder {

        private SimpleDraweeView mPicSdv;
        private TextView mNameTv;
        private TextView mIntroTv;

        public MerchantViewHolder(@NonNull View itemView) {
            super(itemView);
            mPicSdv = itemView.findViewById(R.id.sdv_item_merchant_list);
            mNameTv = itemView.findViewById(R.id.tv_name_item_merchant_list);
            mIntroTv = itemView.findViewById(R.id.tv_intro_item_merchant_list);
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
