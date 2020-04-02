package com.example.lakecircle.ui.home.light;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lakecircle.R;
import com.example.lakecircle.ui.login.login.model.User;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class UserRankFragment extends Fragment {
    private RecyclerView mRecyclerView;

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
        mRecyclerView.setAdapter(new UserListAdapter(UserRankBean.getDefaultBeans()));
    }

    class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.VH> {
        List<UserRankBean> users;

        UserListAdapter(List<UserRankBean> users) {
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
            holder.ranking.setText(String.valueOf(user.getRanking()));
            holder.userName.setText(user.getUserName());
            holder.starts.setText(String.valueOf(user.getStarts()));
            if (user.getProfile() != null) {
                holder.portrait.setImageURI(user.getProfile());
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
