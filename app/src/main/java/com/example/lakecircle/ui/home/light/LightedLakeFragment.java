package com.example.lakecircle.ui.home.light;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lakecircle.R;
import com.example.lakecircle.commonUtils.BaseResponseModel;
import com.example.lakecircle.commonUtils.NetUtil;
import com.example.lakecircle.ui.home.light.model.LakeName2Bean;
import com.example.lakecircle.ui.home.light.model.LakeNameBean;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LightedLakeFragment extends Fragment {
    private RecyclerView recyclerView;
    private MyAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lighted_lake, container, false);
        Toolbar toolbar = root.findViewById(R.id.lighted_toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        recyclerView = root.findViewById(R.id.lighted_list);
        recyclerView.setLayoutManager(new GridLayoutManager(LightedLakeFragment.this.getContext(), 4));

        getList();
        return root;
    }

    private void getList() {
        NetUtil.getInstance().getApi().getStarLakes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseModel<LakeName2Bean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponseModel<LakeName2Bean> lakeNameBeanBaseResponseModel) {
                        mAdapter = new MyAdapter(lakeNameBeanBaseResponseModel.getData());
                        recyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(LightedLakeFragment.this.getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.VH> {
        List<LakeName2Bean> names;

        MyAdapter(List<LakeName2Bean> names) {
            this.names = names;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_lighted_lake, parent, false);

            return new VH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            holder.textView.setText(names.get(position).getLake_name());
            Log.e("Lighted", names.get(position).getLake_name());
        }

        @Override
        public int getItemCount() {
            return names.size();
        }

        class VH extends RecyclerView.ViewHolder {

            TextView textView;

            public VH(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.lighted_lakename);
            }
        }
    }
}
