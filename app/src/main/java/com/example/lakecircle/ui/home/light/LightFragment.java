package com.example.lakecircle.ui.home.light;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.lakecircle.R;

public class LightFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_light, container, false);

        Toolbar toolbar = root.findViewById(R.id.light_toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });
        return root;
    }
}
