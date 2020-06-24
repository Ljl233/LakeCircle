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
import com.example.lakecircle.ui.home.light.model.UserRankBean;
import com.example.lakecircle.ui.login.login.model.User;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserRankFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private Context context = this.getContext();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rank_user, container, false);
        mRecyclerView = root.findViewById(R.id.rank_user_list);
        initList();
        return root;
    }

    private void initList() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
//        mRecyclerView.setAdapter(new UserListAdapter(UserRankBean.getDefaultBeans()));
        getUserList();
    }

    private void getUserList() {
        UserListAdapter adapter = new UserListAdapter();
        NetUtil.getInstance().getApi().getUserRank()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<UserRankBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<UserRankBean> userRankBeans) {
                        adapter.setUsers(userRankBeans);
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

    class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.VH> {
        List<UserRankBean> users;

        UserListAdapter() {
        }

        UserListAdapter(List<UserRankBean> users) {
            this.users = users;
        }

        public void setUsers(List<UserRankBean> users) {
            this.users = users;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rank_user, parent, false);
            return new VH(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            UserRankBean user = users.get(position);
            holder.ranking.setText(String.valueOf(position + 1));
            holder.userName.setText(user.getUsername());
            holder.starts.setText(String.valueOf(user.getStar_num()));
            if (user.getAvatar() != null) {
                holder.portrait.setImageURI(user.getAvatar());
            }
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        class VH extends RecyclerView.ViewHolder {
            TextView ranking, userName, starts;
            SimpleDraweeView portrait;

            public VH(@NonNull View itemView) {
                super(itemView);
                ranking = itemView.findViewById(R.id.item_rank_user_num);
                userName = itemView.findViewById(R.id.item_rank_user_name);
                portrait = itemView.findViewById(R.id.item_rank_user_portrait);
                starts = itemView.findViewById(R.id.item_rank_user_starts);
            }
        }
    }

}
