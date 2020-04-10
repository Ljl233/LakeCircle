package com.example.lakecircle.ui.mine.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import com.example.lakecircle.R;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class MineFragment extends Fragment {

    private AvatarView mAvatarAv;
    private TextView mUsernameTv;
    private TextView mGovernTv;
    private TextView mMerchantTv;

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
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        NavController navController = findNavController(this);

        mAvatarAv = view.findViewById(R.id.av_mine);
        mUsernameTv = view.findViewById(R.id.tv_mine_username);
        mGovernTv = view.findViewById(R.id.tv_mine_government);
        mMerchantTv = view.findViewById(R.id.tv_mine_merchant);

        mAvatarAv.setCertificateGone();

        mMerchantTv.setOnClickListener(v -> {
            navController.navigate(R.id.action_certificate_merchant);
            //navController.navigate(R.id.action_entrance_merchant);
        });

        mGovernTv.setOnClickListener(v -> {
            //navController.navigate(R.id.action_certificate_government);
            navController.navigate(R.id.action_entrance_government);
        });

        return view;
    }
}
