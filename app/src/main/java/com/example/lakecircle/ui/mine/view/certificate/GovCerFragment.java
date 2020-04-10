package com.example.lakecircle.ui.mine.view.certificate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.lakecircle.R;
import com.example.lakecircle.ui.SuccessDialog;
import com.facebook.common.util.UriUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class GovCerFragment extends Fragment {

    private final int CAM_CODE = 0;
    private final int ALB_CODE = 1;

    private String currentPhotoPath;
    private File mPhotoFile;

    private Toolbar mToolbar;
    private ImageView mUploadIv;
    private EditText mLakeEt, mNameEt, mPhoneEt;
    private TextView mUploadTv;
    private Button mPostBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_government_certificate, container, false);

        mToolbar = view.findViewById(R.id.tb_govern_cer);
        mUploadIv = view.findViewById(R.id.iv_govern_cer);
        mLakeEt = view.findViewById(R.id.et_lake_govern_cer);
        mNameEt = view.findViewById(R.id.et_name_govern_cer);
        mPhoneEt = view.findViewById(R.id.et_phone_govern_cer);
        mPostBtn = view.findViewById(R.id.btn_govern_cer);
        mUploadTv = view.findViewById(R.id.tv_govern_cer);

        mUploadTv.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setNavigationOnClickListener(v -> {
                NavHostFragment.findNavController(this).popBackStack();
            });
        }

        mUploadIv.setOnClickListener(v -> {
            showBottomSheet();
        });
        mPostBtn.setOnClickListener(v -> {

            SuccessDialog.newInstance("您已成功提交申请，请等待审核");
        });

        return view;
    }

    private void showBottomSheet() {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_bottom_sheet_pic, null);
        dialog.setContentView(view);
        dialog.show();
        TextView camera = view.findViewById(R.id.tv_camera_dialog_pic);
        TextView album = view.findViewById(R.id.tv_album_dialog_pic);
        //相机
        camera.setOnClickListener(v -> {
            dialog.dismiss();//消失
            mPhotoFile = createImageFile();
            TakePicture();
        });
        //相册
        album.setOnClickListener(v -> {
            dialog.dismiss();//消失
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, ALB_CODE);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case CAM_CODE:{
                if ( resultCode == RESULT_OK ) {
                    Uri uri = new Uri.Builder()
                            .scheme(UriUtil.LOCAL_FILE_SCHEME)
                            .path(currentPhotoPath)
                            .build();
                    mUploadIv.setImageURI(uri);
                    mUploadTv.setVisibility(View.GONE);
                }
                break;
            }
            case ALB_CODE : {
                try {
                    Uri uri = data.getData();
                    mUploadIv.setImageURI(uri);
                    mUploadTv.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            default:
                break;
        }
    }

    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            Log.e("GovCerFragment","创建文件错误");
            e.printStackTrace();
        }
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void TakePicture() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = FileProvider.getUriForFile(getActivity(), "com.example.lakecircle.fileprovider", mPhotoFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        List<ResolveInfo> cameraActivities = getActivity().getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for ( ResolveInfo activity : cameraActivities ) {
            getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        startActivityForResult(takePictureIntent, CAM_CODE);
    }
}
