package com.example.lakecircle.ui.mine.view.govern;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lakecircle.R;
import com.example.lakecircle.ui.home.home.HomeContract;

public class ProblemListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private int mType; //列表种类 1-待解决 0-已解决

    public ProblemListFragment (int type) {
        mType = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_problem_list, container, false);

        mRecyclerView = view.findViewById(R.id.rv_problem_list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new ProblemListAdapter(mType));

        return view;
    }
}
