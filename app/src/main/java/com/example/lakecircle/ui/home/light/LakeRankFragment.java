package com.example.lakecircle.ui.home.light;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lakecircle.R;

import java.util.List;

public class LakeRankFragment extends Fragment {
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_rank_lake, container, false);
        mRecyclerView = root.findViewById(R.id.rank_lake_list);
        initList();
        return root;
    }

    private void initList() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mRecyclerView.setAdapter(new MyAdapter(LakeRankBean.getDefaultLakeRanks()));
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.VH> {

        List<LakeRankBean> lakes;

        MyAdapter(List<LakeRankBean> lakes) {
            this.lakes = lakes;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_rank_lake, parent, false);
            return new VH(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            LakeRankBean lakeInf = lakes.get(position);
            holder.num.setText(String.valueOf(lakeInf.getNum()));
            holder.lakeName.setText(lakeInf.getLakeName());
            holder.lightedTimes.setText(String.valueOf(lakeInf.getLightedTimes()));
        }

        @Override
        public int getItemCount() {
            return lakes.size();
        }

        class VH extends RecyclerView.ViewHolder {
            TextView num, lakeName, lightedTimes;

            public VH(@NonNull View itemView) {
                super(itemView);
                num = itemView.findViewById(R.id.item_rank_lake_num);
                lakeName = itemView.findViewById(R.id.item_rank_lake_name);
                lightedTimes = itemView.findViewById(R.id.item_rank_lake_times);
            }
        }
    }
}
