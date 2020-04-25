package com.example.lakecircle.ui.home.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lakecircle.R;
import com.facebook.drawee.view.SimpleDraweeView;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.MyVH> {

    //List<Activities> activitiesList;
    private OnItemClickListener mOnItemClickListener;

    public ActivitiesAdapter(){}

    @NonNull
    @Override
    public MyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activities_list, parent, false);
        return new MyVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVH holder, int position) {
        if (mOnItemClickListener!=null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(position);
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return 0;
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

    interface OnItemClickListener {
        void onItemClick(int position);
    }

}


