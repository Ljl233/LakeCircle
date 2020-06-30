package com.example.lakecircle.ui.home.merchant.detail.special;

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

public class SpecialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SpecialListResponse> mSpecials;
    private Context mContext;
    private OnSpecialClickListener mOnSpecialClickListener;

    public SpecialAdapter(List<SpecialListResponse> mSpecials, Context mContext, OnSpecialClickListener mOnSpecialClickListener) {
        this.mSpecials = mSpecials;
        this.mContext = mContext;
        this.mOnSpecialClickListener = mOnSpecialClickListener;
    }

    public interface OnSpecialClickListener {
        void onMore();
        void onRefresh();
    }

    public void refresh(List<SpecialListResponse> specials) {
        int preSize = mSpecials.size();
        mSpecials.clear();
        notifyItemRangeRemoved(0,preSize);
        mSpecials.addAll(specials);
        notifyItemRangeInserted(0, specials.size());
    }

    public void add(List<SpecialListResponse> specials) {
        int preSize = mSpecials.size();
        mSpecials.addAll(specials);
        notifyItemRangeInserted(preSize,preSize+specials.size());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new MyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.item_merchant_special, parent, false));
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
        if ( holder instanceof MyViewHolder) {
            MyViewHolder h = (MyViewHolder) holder;
            h.mIntro.setText(mSpecials.get(position).getIntro());
            h.mPic.setImageURI(mSpecials.get(position).getUrl());
        }else if ( holder instanceof NoneViewHolder) {
            NoneViewHolder mHolder = (NoneViewHolder) holder;
            mHolder.textView.setText("找不到特色,点击尝试刷新");
            mHolder.textView.setGravity(Gravity.CENTER);
            mHolder.textView.setOnClickListener(v -> mOnSpecialClickListener.onRefresh());
        } else {
            MoreViewHolder mHolder = (MoreViewHolder) holder;
            mHolder.textView.setText("点击加载更多");
            mHolder.textView.setGravity(Gravity.CENTER);
            mHolder.textView.setOnClickListener(v -> mOnSpecialClickListener.onMore());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if ( mSpecials.size() == 0 )
            return 1;
        else if ( position == mSpecials.size() )
            return 2;
        else
            return 0;
    }

    @Override
    public int getItemCount() {
        return mSpecials.size()+1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private SimpleDraweeView mPic;
        private TextView mIntro;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mPic = itemView.findViewById(R.id.sdv_item_merchant_special);
            mIntro = itemView.findViewById(R.id.tv_item_merchant_special);
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
