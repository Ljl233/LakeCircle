package com.example.lakecircle.ui.circle;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lakecircle.R;

import me.relex.photodraweeview.PhotoDraweeView;

public class PhotoFragment extends Fragment {
    private String ImageUrl;

    private PhotoDraweeView mImagePdv;

    public PhotoFragment(String imageUrl) {
        ImageUrl = imageUrl;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo,container,false);

        mImagePdv = view.findViewById(R.id.pdv_image);
        mImagePdv.setPhotoUri(Uri.parse(ImageUrl));

        return view;
    }
}
