package com.example.lakecircle.ui.circle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.lakecircle.R;

import me.relex.photodraweeview.PhotoDraweeView;

public class PhotoViewActivity extends AppCompatActivity {

    private PhotoDraweeView mPhoto;
    private FrameLayout mFl;

    public static void start(Context context, String url) {
        Intent intent = new Intent(context, PhotoViewActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        mFl = findViewById(R.id.fl_photo_view);
        mFl.setOnClickListener(v -> this.finish());

        mPhoto = findViewById(R.id.pdv_image);
        mPhoto.setPhotoUri(Uri.parse(getIntent().getStringExtra("url")));

    }
}
