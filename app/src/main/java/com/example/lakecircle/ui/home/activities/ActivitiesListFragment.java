package com.example.lakecircle.ui.home.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lakecircle.R;

public class ActivitiesListFragment extends Fragment{

    private RecyclerView mActivitiesRv;
    private ActivitiesAdapter mAdapter;
    private NavController mNavController;

    public ActivitiesListFragment (int size) {
        switch (size) {
            case 1: {
                break;
            }
            case 0 : {

                break;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activities_list, container, false);
        mNavController = NavHostFragment.findNavController(this);

        mActivitiesRv = view.findViewById(R.id.rv_activities_list);
        mAdapter = new ActivitiesAdapter();

        mActivitiesRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mActivitiesRv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ActivitiesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mNavController.navigate(R.id.action_activities_detail);
            }
        });
        return view;
    }
}
