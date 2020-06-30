package com.example.lakecircle.ui.home.upimage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lakecircle.R;
import com.example.lakecircle.commonUtils.BaseResponseModel;
import com.example.lakecircle.commonUtils.ImageUtils;
import com.example.lakecircle.commonUtils.NetUtil;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class ULQActivity extends AppCompatActivity {

    private SimpleDraweeView mDraweeULQ;
    private EditText mEditText;
    private TextView mTvHint;
    private ConstraintLayout parent;
    private Button mBtSubmit;
    private String[] mPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private final static int ALBUM_REQUEST_CODE = 1;
    private final static int CAMERA_REQUEST_CODE = 2;
    private final static int REQUEST_ALBUM = 3;
    private final static int REQUEST_CAMERA = 4;
    private String mCurrentPhoto = "";
    private EditText test_edit;
    private Uri uri;
    private String mPictureUrl;
    private boolean havePicture = false;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_load_question);

        Toolbar toolbar = findViewById(R.id.ulq_toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        parent = findViewById(R.id.ulq_content);
        mDraweeULQ = findViewById(R.id.ulq_image);
        mEditText = findViewById(R.id.ed_ulq);
        mTvHint = findViewById(R.id.tv_ulq);
        mBtSubmit = findViewById(R.id.ulq_submit);
        mBtSubmit.setOnClickListener(v -> {
            if (havePicture) {
                setUploadStatus();
                getPictureUrl();
            } else {
                finish();
                Toast.makeText(this, "请先上传图片", Toast.LENGTH_SHORT).show();
            }
        });
        mDraweeULQ.setOnClickListener(v -> {
            showPopupWindow();
        });
        test_edit = findViewById(R.id.ed_ulq);
    }

    private void showPopupWindow() {
        View popView = LayoutInflater.from(this).inflate(R.layout.pop_window_ulq, null);
        Button btAlbum = popView.findViewById(R.id.pop_photo);
        Button btPhoto = popView.findViewById(R.id.pop_camera);
        Button btDismiss = popView.findViewById(R.id.pop_dismiss);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        PopupWindow popupWindow = new PopupWindow(popView, width, height, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popView.setOnClickListener(v -> popupWindow.dismiss());
        btAlbum.setOnClickListener(v -> {
            checkOrRequestPermission("Album");
            popupWindow.dismiss();
        });

        btPhoto.setOnClickListener(v -> {
            checkOrRequestPermission("Camera");
            popupWindow.dismiss();
        });

        btDismiss.setOnClickListener(v -> {
            popupWindow.dismiss();
        });

    }

    private void checkOrRequestPermission(String permission) {
        if (permission.equals("Album")) {
            if (ContextCompat.checkSelfPermission(this, mPermissions[0])
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{mPermissions[0]}, ALBUM_REQUEST_CODE);
            } else {
                pickAlbum();
            }
        } else if (permission.equals("Camera")) {
            if (ContextCompat.checkSelfPermission(this, mPermissions[1])
                    != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, mPermissions[2])
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{mPermissions[1], mPermissions[2]}, CAMERA_REQUEST_CODE);
            } else {
                takePhoto();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == ALBUM_REQUEST_CODE) {
            pickAlbum();
        }
        if (requestCode == CAMERA_REQUEST_CODE) {
            takePhoto();
        }
    }

    private void pickAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_ALBUM);
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                    this,
                    "com.example.lakecircle.fileprovider",
                    photoFile
            ));
            startActivityForResult(intent, REQUEST_CAMERA);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhoto = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mTvHint.setText("");
            if (requestCode == REQUEST_ALBUM) {
                uri = data.getData();
                mDraweeULQ.setImageURI(uri);
            }
            if (requestCode == REQUEST_CAMERA) {
                uri = new Uri.Builder()
                        .scheme(UriUtil.LOCAL_FILE_SCHEME)
                        .path(mCurrentPhoto)
                        .build();
                mDraweeULQ.setImageURI(uri);
                galleryAddPic();
            }
            havePicture = true;
        }
    }

    //将图片保存到相册
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhoto);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private String getDescription() {
        return test_edit.getText().toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getPictureUrl() {
        String filepath = ImageUtils.uriToPath(uri, this, getContentResolver());

        File originalImage = new File(filepath);
        Log.e("uri=>", uri.toString());
        Log.e("file=>", originalImage.toString());

        // /storage/emulated/0/DCIM/Camera/
        String targetDir = originalImage.getParentFile().getAbsolutePath();

        //压缩图片
        ImageUtils.compressImage(this, originalImage, targetDir, new OnCompressListener() {
            @Override
            public void onStart() {
                // TODO 压缩开始前调用，可以在方法内启动 loading UI
//                mBtSubmit.setText("图片压缩中...");
            }

            @Override
            public void onSuccess(File file) {
                // TODO 压缩成功后调用，返回压缩后的图片文件
                ImageUtils.uploadImage(file, new Observer<UrlResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UrlResponse response) {
                        mPictureUrl = response.getData().getUrl();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ULQActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
                        mBtSubmit.setText("上传");
//                        Log.e("ulq:", e.getMessage());
//                        Log.e("ulq:", e.toString());
//                        e.printStackTrace();
//                        Log.e("ulq:", "null");
                    }

                    @Override
                    public void onComplete() {
                        List<UrlResponse> urlResponseList = new ArrayList<>();
                        UrlResponse urlResponse = new UrlResponse();
                        UrlResponse.DataBean dataBean = new UrlResponse.DataBean();
                        dataBean.setUrl(mPictureUrl);
                        urlResponse.setData(dataBean);
                        submit(urlResponseList);
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                // TODO 当压缩过程出现问题时调用
                e.printStackTrace();
                Toast.makeText(ULQActivity.this, "压缩问题：" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String getTargetDir(File originalImage) throws IOException {
        String parentPath = originalImage.getParentFile().getPath();
//        String targetDir = parentPath + "/shot_" + name;

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "shot_JPEG_" + timeStamp + "_";

        File storageDir = new File(parentPath);
        File targetDir = File.createTempFile(imageFileName, ".jpg", storageDir);

        Log.e("ulq:", "target:" + targetDir.getPath());

        return targetDir.getPath();
    }


    private void submit(List<UrlResponse> urls) {
        QuestionBean question = new QuestionBean();

        question.setContent(getDescription());
        question.setLocation(" ");

        question.setUrls(urls);

        NetUtil.getInstance().getApi().newQuestion(question)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QuestionResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(QuestionResponse response) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("ulq:", e.getMessage());
                        Log.e("ulq:", e.toString());
                        e.printStackTrace();
                        Log.e("ulq:", "null");
                        Toast.makeText(ULQActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                        mBtSubmit.setText("上传");
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(ULQActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void setUploadStatus() {
        mBtSubmit.setText("上传中...");
    }

}
