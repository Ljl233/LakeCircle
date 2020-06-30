package com.example.lakecircle.ui.home.activities;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lakecircle.R;
import com.example.lakecircle.ui.mine.coupon.MyCouponFragment;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class ActivitiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Activities> activitiesList;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    interface OnItemClickListener {
        void onItemClick(int id);
        void onMoreClick();
        void onRefreshClick();
    }

    public ActivitiesAdapter(List<Activities> activities, Context context, OnItemClickListener onItemClickListener) {
        activitiesList = activities;
        mContext = context;
        mOnItemClickListener = onItemClickListener;
    }

    public void refresh(List<Activities> activities) {
        int preSize = activitiesList.size();
        activitiesList.clear();
        notifyItemRangeRemoved(0,preSize);
        activitiesList.addAll(activities);
        notifyItemRangeInserted(0,activities.size());
    }

    public void add(List<Activities> activities) {
        int preSize = activitiesList.size();
        activitiesList.addAll(activities);
        notifyItemRangeInserted(preSize,preSize + activities.size());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new MyVH(LayoutInflater.from(mContext).inflate(
                        R.layout.item_activities_list, parent, false));
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
        if ( holder instanceof MyVH ) {
            MyVH h = (MyVH) holder;
            h.mPicSdv.setImageURI(activitiesList.get(position).getPictureUrl());
            h.mTitleTv.setText(activitiesList.get(position).getName());
            h.itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(activitiesList.get(position).getId()));
        } else if ( holder instanceof NoneViewHolder) {
            NoneViewHolder mHolder = (NoneViewHolder) holder;
            mHolder.textView.setText("暂时没有这类活动,或者点击尝试刷新");
            mHolder.textView.setGravity(Gravity.CENTER);
            mHolder.textView.setOnClickListener(v -> mOnItemClickListener.onRefreshClick());
        } else {
            MoreViewHolder mHolder = (MoreViewHolder) holder;
            mHolder.textView.setText("点击加载更多");
            mHolder.textView.setGravity(Gravity.CENTER);
            mHolder.textView.setOnClickListener(v -> mOnItemClickListener.onMoreClick());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if ( activitiesList.size() == 0 )
            return 1;
        else if ( position == activitiesList.size() )
            return 2;
        else
            return 0;
    }

    @Override
    public int getItemCount() {
        return activitiesList.size() + 1;
    }

    class MyVH extends RecyclerView.ViewHolder {

        private SimpleDraweeView mPicSdv;
        private TextView mTitleTv;

        public MyVH(@NonNull View itemView) {
            super(itemView);
            mPicSdv = itemView.findViewById(R.id.sdv_activities_item);
            mTitleTv = itemView.findViewById(R.id.tv_activities_item);
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


