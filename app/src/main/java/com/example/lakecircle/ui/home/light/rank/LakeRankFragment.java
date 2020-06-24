package com.example.lakecircle.ui.home.light.rank;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lakecircle.R;
import com.example.lakecircle.commonUtils.NetUtil;
import com.example.lakecircle.ui.home.light.model.LakeRankBean;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LakeRankFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private Context context = this.getContext();

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

    private void getLakeList() {
        MyAdapter adapter = new MyAdapter();

        NetUtil.getInstance().getApi().getLakeRank()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<LakeRankBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<LakeRankBean> lakeRankBeans) {
                        adapter.setLakes(lakeRankBeans);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        mRecyclerView.setAdapter(adapter);
                    }
                });

    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.VH> {

        List<LakeRankBean> lakes;

        MyAdapter() {
        }

        MyAdapter(List<LakeRankBean> lakes) {
            this.lakes = lakes;
        }

        public void setLakes(List<LakeRankBean> lakes) {
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
            holder.num.setText(String.valueOf(position + 1));
            holder.lakeName.setText(lakeInf.getName());
            holder.lightedTimes.setText(String.valueOf(lakeInf.getStar_num()));
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
