package com.example.lakecircle.ui.home.upimage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lakecircle.R;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_load_question);

        parent = findViewById(R.id.ulq_content);
        mDraweeULQ = findViewById(R.id.ulq_image);
        mEditText = findViewById(R.id.ed_ulq);
        mTvHint = findViewById(R.id.tv_ulq);
        mBtSubmit = findViewById(R.id.ulq_submit);
        mBtSubmit.setOnClickListener(v -> {
            Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
            finish();
        });
        mDraweeULQ.setOnClickListener(v -> {
            showPopupWindow();
        });
        EditText test_edit = findViewById(R.id.test_edit);
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
                Uri uri = data.getData();
                mDraweeULQ.setImageURI(uri);
            }
            if (requestCode == REQUEST_CAMERA) {
                Uri uri = new Uri.Builder()
                        .scheme(UriUtil.LOCAL_FILE_SCHEME)
                        .path(mCurrentPhoto)
                        .build();
                mDraweeULQ.setImageURI(uri);
                galleryAddPic();
            }
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
}
