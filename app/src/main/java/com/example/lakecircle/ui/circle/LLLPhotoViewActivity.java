package com.example.lakecircle.ui.circle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;

import com.example.lakecircle.R;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class LLLPhotoViewActivity extends AppCompatActivity {

    private List<String> ImageList = new ArrayList<>();

    private ViewPager mPicVp;
    private TextView mIndexTv;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view_lll);
        Fresco.initialize(this);

        index = getIntent().getIntExtra("index",0);
        ImageList = getIntent().getStringArrayListExtra("ImageList");

        mIndexTv = findViewById(R.id.tv_index_photo);
        mIndexTv.setText(String.format("%1$d/%2$d",index+1,ImageList.size()));

        mPicVp = findViewById(R.id.vp_photo);
        mPicVp.setAdapter(new MyAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        mPicVp.setCurrentItem(index);

        mPicVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIndexTv.setText(String.format("%1$d/%2$d",position+1,ImageList.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return new PhotoFragment(ImageList.get(position));
        }

        @Override
        public int getCount() {
            return ImageList.size();
        }
    }
}
