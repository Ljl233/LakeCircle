package com.example.lakecircle.ui.circle;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lakecircle.R;

public class PostCircleActivity extends AppCompatActivity {

    private ImageButton mNavIb;
    private Button mPostBtn;
    private EditText mTextEt;
    private ImagesLayout mImagesIl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_circle);

        mNavIb = findViewById(R.id.ib_post_circle);
        mPostBtn = findViewById(R.id.btn_post_circle);
        mTextEt = findViewById(R.id.et_post_circle);
        mImagesIl = findViewById(R.id.il_post_circle);

        mNavIb.setOnClickListener(v -> finish());

        mPostBtn.setOnClickListener(v -> {
            postCircle();
            finish();
        });

    }

    private void postCircle() {

    }
}
