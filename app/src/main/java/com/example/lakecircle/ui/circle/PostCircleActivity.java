package com.example.lakecircle.ui.circle;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.lakecircle.R;
import com.example.lakecircle.commonUtils.ImageUtils;
import com.example.lakecircle.commonUtils.NetUtil;
import com.example.lakecircle.ui.home.upimage.UrlResponse;
import com.example.lakecircle.ui.mine.SimpleResponse;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import top.zibin.luban.OnCompressListener;

public class PostCircleActivity extends AppCompatActivity {

    private final int CAM_CODE = 0;
    private final int ALB_CODE = 1;

    private String currentPhotoPath;
    private File mPhotoFile;
    private Uri mPhotoUri;
    private String mPhotoUrl = "";

    private ImageButton mNavIb;
    private Button mPostBtn;
    private EditText mTextEt;
//    private ImagesLayout mImagesIl;
    private SimpleDraweeView mPicSdv;
    private ImageView mPicIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_circle);

        Objects.requireNonNull(this).setTheme(R.style.QMUITheme);

        mNavIb = findViewById(R.id.ib_post_circle);
        mPostBtn = findViewById(R.id.btn_post_circle);
        mTextEt = findViewById(R.id.et_post_circle);
        //mImagesIl = findViewById(R.id.il_post_circle);
        mPicIv = findViewById(R.id.iv_post_circle);
        mPicSdv = findViewById(R.id.sdv_post_circle);

        mPicIv.setVisibility(View.VISIBLE);
        mPicSdv.setVisibility(View.GONE);

        mPicIv.setOnClickListener(v -> showBottomSheet());

        mNavIb.setOnClickListener(v -> {
            finish();
            setResult(0);
        });

        mPostBtn.setOnClickListener(v -> {
            if ( checkNull() ) {
                QMUITipDialog tipDialog = new QMUITipDialog.Builder(this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                        .setTipWord("内容不能为空")
                        .create();
                tipDialog.show();
                mPostBtn.postDelayed(tipDialog::dismiss, 1500);
            }
            else postCircle();
        });

        mTextEt.setFocusable(true);
        mTextEt.setFocusableInTouchMode(true);
        mTextEt.requestFocus();
        //显示软键盘
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        //如果上面的代码没有弹出软键盘 可以使用下面另一种方式
        //InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.showSoftInput(editText, 0);
    }

    private void postCircle() {
        NetUtil.getInstance().getApi().postCircle(new PostCircleBean(mTextEt.getText().toString(), mPhotoUrl))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SimpleResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        showSuccess();
                        setResult(1);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("PostCircleActivity","post circle fail");
                        showError("发送失败");
                    }

                    @Override
                    public void onComplete() { }
                });
    }

    private void showBottomSheet() {
        QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(this);
        builder.setGravityCenter(true)
                .setSkinManager(QMUISkinManager.defaultInstance(this))
                .setAddCancelBtn(true)
                .setAllowDrag(true)
                .setNeedRightMark(false)
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    dialog.dismiss();
                    Objects.requireNonNull(this).setTheme(R.style.AppTheme);
                    switch (position) {
                        case 0:
                            try {
                                mPhotoFile = createImageFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            TakePicture();
                            break;
                        case 1:
                            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, ALB_CODE);
                            break;
                        default:
                            break;
                    }
                });

        builder.addItem("拍摄");
        builder.addItem("从相册选择");
        builder.build().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAM_CODE: {
                if (resultCode == RESULT_OK) {
                    mPhotoUri = new Uri.Builder()
                            .scheme(UriUtil.LOCAL_FILE_SCHEME)
                            .path(currentPhotoPath)
                            .build();
                    postImage();
                }
                break;
            }
            case ALB_CODE: {
                try {
                    mPhotoUri = data.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                postImage();
                break;
            }
            default:
                break;
        }
    }

    private void postImage( ) {
        String filepath = ImageUtils.uriToPath(mPhotoUri, this, this.getContentResolver());
        File originalImage = new File(filepath);
        String targetDir = originalImage.getParentFile().getAbsolutePath();
        ImageUtils.compressImage(this, originalImage, targetDir, new OnCompressListener() {
            @Override
            public void onStart() {
                Log.e("PostCircleActivity", "start compress image"); }

            @Override
            public void onSuccess(File file) {
                ImageUtils.uploadImage(file, new Observer<UrlResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(UrlResponse urlResponse) {
                        mPhotoUrl = urlResponse.getData().getUrl();
                        mPicSdv.setVisibility(View.VISIBLE);
                        mPicIv.setVisibility(View.GONE);
                        mPicSdv.setImageURI(urlResponse.getData().getUrl());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() { }
                });
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.e("PostCircleActivity", "compress image fail");
                showError("图片压缩失败");
            }
        });
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Objects.requireNonNull(this).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void TakePicture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = FileProvider.getUriForFile(this, "com.example.lakecircle.fileprovider", mPhotoFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        List<ResolveInfo> cameraActivities = this.getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for ( ResolveInfo activity : cameraActivities ) {
            this.grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        startActivityForResult(takePictureIntent, CAM_CODE);
    }

    private boolean checkNull() {
        return mPhotoUrl.length() == 0 || mTextEt.getText().toString().length() == 0 ;
    }


    private void showSuccess() {
        Objects.requireNonNull(this).setTheme(R.style.QMUITheme);
        QMUITipDialog tipDialog = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord("发送成功")
                .create();
        tipDialog.show();
        mPostBtn.postDelayed(() -> {
            Objects.requireNonNull(this).setTheme(R.style.AppTheme);
            tipDialog.dismiss();
        }, 1500);
    }

    private void showError(String error) {
        Objects.requireNonNull(this).setTheme(R.style.QMUITheme);
        QMUITipDialog tipDialog = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord(error)
                .create();
        tipDialog.show();
        mPostBtn.postDelayed(() -> {
            tipDialog.dismiss();
            Objects.requireNonNull(this).setTheme(R.style.AppTheme);
        }, 1500);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(this).setTheme(R.style.AppTheme);
    }
}
