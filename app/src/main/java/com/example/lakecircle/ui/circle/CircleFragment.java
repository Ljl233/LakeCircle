package com.example.lakecircle.ui.circle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lakecircle.R;

import java.util.ArrayList;

public class CircleFragment extends Fragment {

    private Toolbar mToolbar;
    private RecyclerView mCircleRv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_circle, container, false);

        mToolbar = view.findViewById(R.id.tb_circle);
        mCircleRv = view.findViewById(R.id.rv_circle);

        mCircleRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mCircleRv.setAdapter(new CircleAdapter(new ArrayList<>()));

        return view;
    }
}
