package com.example.lakecircle.ui.mine.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lakecircle.R;
import com.facebook.drawee.view.SimpleDraweeView;

public class AvatarView extends ConstraintLayout {

    private SimpleDraweeView mAvatarSdv;
    private ImageView mCertificateIv;

    public AvatarView(Context context) {
        super(context);
        initView(context);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_avatar_mine_fragment, this, true);
        mAvatarSdv = findViewById(R.id.sdv_avatar_view_mine);
        mCertificateIv = findViewById(R.id.iv_certificate_avatar_view_mine);
    }

    public void setCertificateSrc(int resId) {
        mCertificateIv.setImageResource(resId);
    }

    public void setCertificateVisible() {
        mCertificateIv.setVisibility(VISIBLE);
    }

    public void setCertificateGone() {
        mCertificateIv.setVisibility(GONE);
    }

    public void setAvatar(String avatar) {
        mAvatarSdv.setImageURI(avatar);
    }

}