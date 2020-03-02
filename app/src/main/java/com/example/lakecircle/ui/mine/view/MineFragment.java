package com.example.lakecircle.ui.mine.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lakecircle.R;

public class MineFragment extends Fragment {

    private AvatarView mAvatarAv;
    private TextView mUsernameTv;
    private TextView mGovernTv;
    private TextView mBusinessTv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ( getArguments() != null ) {
            //传信息
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mine, container, false);

        mAvatarAv = v.findViewById(R.id.av_mine);
        mUsernameTv = v.findViewById(R.id.tv_mine_username);
        mGovernTv = v.findViewById(R.id.tv_mine_government);
        mBusinessTv = v.findViewById(R.id.tv_mine_business);

        return v;
    }
}
