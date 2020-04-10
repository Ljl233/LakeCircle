package com.example.lakecircle.ui.mine.view.govern;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lakecircle.R;
import com.facebook.drawee.view.SimpleDraweeView;

public class ProblemListAdapter extends RecyclerView.Adapter<ProblemListAdapter.MyViewHolder> {

    int mType; //列表种类 1-待解决 0-已解决
    //List<Problem> problemList;


    public ProblemListAdapter (int type) {
        mType = type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_problem_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if ( mType == 1 ) {
            holder.mReplyBtn.setVisibility(View.VISIBLE);
            holder.mResolvedIv.setVisibility(View.GONE);
        } else {
            holder.mResolvedIv.setVisibility(View.VISIBLE);
            holder.mReplyBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return 0;

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private SimpleDraweeView mPic;
        private TextView mPlace;
        private Button mReplyBtn;
        private ImageView mResolvedIv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mPic = itemView.findViewById(R.id.sdv_problem_item);
            mPlace = itemView.findViewById(R.id.tv_problem_item);
            mReplyBtn = itemView.findViewById(R.id.btn_problem_item);
            mResolvedIv = itemView.findViewById(R.id.iv_problem_item);
        }
    }
}
