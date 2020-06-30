package com.example.lakecircle.ui.mine.govern.problemview;

import android.view.Gravity;
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

import java.util.List;

public class ProblemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int mType; //列表种类 1-待解决 0-已解决
    private List<Problem> problemList;
    private ProblemItemListener mProblemItemListener;

    public ProblemListAdapter (List<Problem> problems, int type, ProblemItemListener problemItemListener) {
        mType = type;
        mProblemItemListener = problemItemListener;
        problemList = problems;
    }

    public interface ProblemItemListener {
        void onItemClickListener(int id);
        void onMoreClickListener();
        void onRefreshClickListener();
    }

    public void refresh(List<Problem> problems) {
        int preSize = problemList.size();
        problemList.clear();
        notifyItemRangeRemoved(0,preSize);
        problemList.addAll(problems);
        notifyItemRangeInserted(0,problems.size());
    }

    public void add(List<Problem> problems) {
        int preSize = problemList.size();
        problemList.addAll(problems);
        notifyItemRangeInserted(preSize,preSize+problems.size());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new MyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.item_problem_list, parent, false));
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
        if ( holder instanceof MyViewHolder ) {
            MyViewHolder mHolder = (MyViewHolder) holder;
            mHolder.mPic.setImageURI(problemList.get(position).getPicture_url().get(0));
            mHolder.mPlace.setText(problemList.get(position).getLocation());
            if (mType == 1) {
                mHolder.mReplyBtn.setVisibility(View.VISIBLE);
                mHolder.mResolvedIv.setVisibility(View.GONE);
                mHolder.mReplyBtn.setOnClickListener(v -> mProblemItemListener.onItemClickListener(problemList.get(position).getId()));
            } else {
                mHolder.mResolvedIv.setVisibility(View.VISIBLE);
                mHolder.mReplyBtn.setVisibility(View.GONE);
            }
        } else if ( holder instanceof NoneViewHolder) {
            NoneViewHolder mHolder = (NoneViewHolder) holder;
            mHolder.textView.setText("暂时没有这类问题,点击尝试刷新");
            mHolder.textView.setGravity(Gravity.CENTER);
            mHolder.textView.setOnClickListener(v -> mProblemItemListener.onRefreshClickListener());
        } else {
            MoreViewHolder mHolder = (MoreViewHolder) holder;
            mHolder.textView.setText("点击加载更多");
            mHolder.textView.setGravity(Gravity.CENTER);
            mHolder.textView.setOnClickListener(v -> mProblemItemListener.onMoreClickListener());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if ( problemList.size() == 0 )
            return 1;
        else if ( position == problemList.size() )
            return 2;
        else
            return 0;
    }

    @Override
    public int getItemCount() {
        return problemList.size()+1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

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
