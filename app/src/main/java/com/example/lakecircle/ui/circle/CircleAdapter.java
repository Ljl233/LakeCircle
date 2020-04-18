package com.example.lakecircle.ui.circle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lakecircle.R;
import com.example.lakecircle.ui.mine.view.AvatarView;

import java.util.List;

public class CircleAdapter extends RecyclerView.Adapter<CircleAdapter.CircleViewHolder> {

    private List<Circle> mCircles;

    public CircleAdapter(List<Circle> circles){
        mCircles = circles;
    }

    @NonNull
    @Override
    public CircleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle, parent, false);
        return new CircleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CircleViewHolder holder, int position) {
        Circle circle = mCircles.get(position);

    }

    @Override
    public int getItemCount() {
        return mCircles.size();
    }

    class CircleViewHolder extends RecyclerView.ViewHolder {

        private AvatarView mAvatar;
        private TextView mNameTv, mTimeTv, mContentTv;
        private ImagesLayout mImagesIl;

        public CircleViewHolder(@NonNull View itemView) {
            super(itemView);
            mAvatar = itemView.findViewById(R.id.avatar_circle_item);
            mNameTv = itemView.findViewById(R.id.tv_name_circle_item);
            mTimeTv = itemView.findViewById(R.id.tv_time_circle_item);
            mContentTv = itemView.findViewById(R.id.tv_content_circle_item);
            mImagesIl = itemView.findViewById(R.id.il_circle_item);
        }
    }
}
