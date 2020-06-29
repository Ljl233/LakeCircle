package com.example.lakecircle.ui.circle;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lakecircle.R;
import com.example.lakecircle.ui.mine.AvatarView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class CircleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Circle> mCircles;
    private Context mContext;
    private OnCircleClickListener mOnCircleClickListener;

    public interface OnCircleClickListener {
        void onClick(String url);
        void onMoreClick();
        void onRefreshClick();
    }

    public CircleAdapter(List<Circle> circles, OnCircleClickListener onCircleClickListener, Context context) {
        mCircles = circles;
        mOnCircleClickListener = onCircleClickListener;
        mContext = context;
    }

    public void refresh(List<Circle> circles) {
        int preSize = mCircles.size();
        mCircles.clear();
        notifyItemRangeRemoved(0,preSize);
        mCircles.addAll(circles);
        notifyItemRangeInserted(0,circles.size());
    }

    public void add(List<Circle> circles) {
        int preSize = mCircles.size();
        mCircles.addAll(circles);
        notifyItemRangeInserted(preSize,preSize+circles.size());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0 :
                return new CircleViewHolder(LayoutInflater.from(mContext).inflate(
                        R.layout.item_circle, parent, false));
            case 1:
                return new NoneViewHolder(LayoutInflater.from(mContext).inflate(
                        android.R.layout.simple_list_item_1, parent, false));
            case 2:
                return new MoreViewHolder(LayoutInflater.from(mContext).inflate(
                        android.R.layout.simple_list_item_1, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if ( holder instanceof  CircleViewHolder ) {
            CircleViewHolder h = (CircleViewHolder) holder;
            Circle circle = mCircles.get(position);
            StringBuffer t = new StringBuffer();
            String date = circle.getCreated_at().substring(0,10);
            String time = circle.getCreated_at().substring(11,19);
            t.append(date);
            t.append(" ");
            t.append(time);
            h.mTimeTv.setText(t.toString());
            if ( circle.getContent().length() == 0 )
                h.mContentTv.setVisibility(View.GONE);
            else
                h.mContentTv.setText(circle.getContent());
            if ( circle.getPicture_url().length() == 0 )
                h.mImageSdv.setVisibility(View.GONE);
            else {
                h.mImageSdv.setImageURI(circle.getPicture_url());
                h.mImageSdv.setOnClickListener(v -> mOnCircleClickListener.onClick(circle.getPicture_url()));
            }

        } else if ( holder instanceof  MoreViewHolder ) {
            MoreViewHolder h = (MoreViewHolder) holder;
            h.textView.setText("点击加载更多");
            h.textView.setGravity(Gravity.CENTER);
            h.textView.setOnClickListener(v -> mOnCircleClickListener.onMoreClick());
        } else {
            NoneViewHolder h = (NoneViewHolder) holder;
            h.textView.setText("找不到河湖圈,请点击尝试刷新");
            h.textView.setGravity(Gravity.CENTER);
            h.textView.setOnClickListener(v -> mOnCircleClickListener.onRefreshClick());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if ( mCircles.size() == 0 )
            return 1;
        else if ( position == mCircles.size() )
            return 2;
        return 0;
    }

    @Override
    public int getItemCount() {
        if ( mCircles.size() == 0 )
            return 1;
        return mCircles.size()+1;
    }

    class CircleViewHolder extends RecyclerView.ViewHolder {

        private AvatarView mAvatar;
        private TextView mNameTv, mTimeTv, mContentTv;
        //private ImagesLayout mImagesIl;
        private SimpleDraweeView mImageSdv;

        public CircleViewHolder(@NonNull View itemView) {
            super(itemView);
            mAvatar = itemView.findViewById(R.id.avatar_circle_item);
            //mNameTv = itemView.findViewById(R.id.tv_name_circle_item);
            mTimeTv = itemView.findViewById(R.id.tv_time_circle_item);
            mContentTv = itemView.findViewById(R.id.tv_content_circle_item);
            //mImagesIl = itemView.findViewById(R.id.il_circle_item);
            mImageSdv = itemView.findViewById(R.id.sdv_pic_circle_item);
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
