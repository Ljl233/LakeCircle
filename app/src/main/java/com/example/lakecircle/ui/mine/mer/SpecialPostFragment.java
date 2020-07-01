package com.example.lakecircle.ui.mine.mer;

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
import com.example.lakecircle.commonUtils.ImageUtils;
import com.example.lakecircle.commonUtils.NetUtil;
import com.example.lakecircle.ui.home.upimage.UrlResponse;
import com.example.lakecircle.ui.mine.SimpleResponse;
import com.facebook.common.util.UriUtil;
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

import static android.app.Activity.RESULT_OK;

public class SpecialPostFragment extends Fragment {

    private final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private final int MY_PERMISSIONS_REQUEST_ALBUM = 1;

    private final int CAM_CODE = 0;
    private final int ALB_CODE = 1;

    private String currentPhotoPath;
    private File mPhotoFile;
    private Uri mPhotoUri;

    private Toolbar mToolbar;
    private ImageView mPicIv;
    private Button mPostBtn;
    private TextView mPicTv;
    //private EditText mNameEt;
    private EditText mIntroEt;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_special_post, container, false);


        mToolbar = view.findViewById(R.id.tb_special_post);
        mPicIv = view.findViewById(R.id.iv_special_post);
        mPostBtn = view.findViewById(R.id.btn_special_post);
        //mNameEt = view.findViewById(R.id.et_name_special_post);
        mIntroEt = view.findViewById(R.id.et_intro_special_post);
        mPicTv = view.findViewById(R.id.tv_special_post);

        mPicTv.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setNavigationOnClickListener(v -> {
                NavHostFragment.findNavController(this).popBackStack();
            });
        }

        mPicIv.setClickable(true);
        mPicIv.setOnClickListener(v -> {
            showBottomSheet();
        });

        mPostBtn.setOnClickListener(v -> {
            if (checkNonNull())
                compressImage();
            else {
                Objects.requireNonNull(getContext()).setTheme(R.style.QMUITheme);
                QMUITipDialog tipDialog = new QMUITipDialog.Builder(getContext())
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                        .setTipWord("请将信息填写完整")
                        .create();
                tipDialog.show();
                this.getView().postDelayed(() -> {
                    tipDialog.dismiss();
                    Objects.requireNonNull(getContext()).setTheme(R.style.AppTheme);
                }, 1500);
            }
        });
        return view;
    }

    private void compressImage( ) {
        String filepath = ImageUtils.uriToPath(mPhotoUri, getContext(), getActivity().getContentResolver());
        File originalImage = new File(filepath);
        String targetDir = originalImage.getParentFile().getAbsolutePath();
        ImageUtils.compressImage(getContext(), originalImage, targetDir, new OnCompressListener() {
            @Override
            public void onStart() {Log.e("SpecialPostFragment", "start compress image"); }

            @Override
            public void onSuccess(File file) {
                postSpecial(file);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.e("SpecialPostFragment", "compress image fail");
                showError("图片压缩失败");
            }
        });
    }

    private void postSpecial(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        NetUtil.getInstance().getApi().uploadImage(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(stringResponse -> Log.e("SpecialPostFragment", "Image post Success"))
                .observeOn(Schedulers.io())
                .flatMap((Function<UrlResponse, Observable<SimpleResponse>>) urlResponse -> {
                    String intro = mIntroEt.getText().toString();
                    //String name = mNameEt.getText().toString();
                    String picture_url = urlResponse.getData().getUrl();
                    SpecialPostBean bean = new SpecialPostBean(intro, picture_url);
                    return NetUtil.getInstance().getApi().postMerSpecial(bean);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SimpleResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        if ( !simpleResponse.getMessage().equals("OK") )
                            onError(new Throwable());
                        showSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("SpecialPostFragment", "post fail");
                        showError("上传失败");
                    }

                    @Override
                    public void onComplete() { }
                });
    }


    private void showBottomSheet() {
        Objects.requireNonNull(getContext()).setTheme(R.style.QMUITheme);
        QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(getActivity());
        builder.setGravityCenter(true)
                .setSkinManager(QMUISkinManager.defaultInstance(getContext()))
                .setAddCancelBtn(true)
                .setAllowDrag(true)
                .setNeedRightMark(false)
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    dialog.dismiss();
                    Objects.requireNonNull(getContext()).setTheme(R.style.AppTheme);
                    switch (position) {
                        case 0:
                            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                            TakePicture();
                            break;
                        case 1:
                            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_ALBUM);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    TakePicture();
                break;
            }
            case MY_PERMISSIONS_REQUEST_ALBUM : {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, ALB_CODE);
                }
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case CAM_CODE:{
                if ( resultCode == RESULT_OK ) {
                    mPhotoUri = new Uri.Builder()
                            .scheme(UriUtil.LOCAL_FILE_SCHEME)
                            .path(currentPhotoPath)
                            .build();
                    mPicIv.setImageURI(mPhotoUri);
                    mPicTv.setVisibility(View.GONE);
                }
                break;
            }
            case ALB_CODE : {
                try {
                    mPhotoUri = data.getData();
                    mPicIv.setImageURI(mPhotoUri);
                    mPicTv.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            default:
                break;
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Objects.requireNonNull(getActivity()).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void TakePicture() {
        try {
            mPhotoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = FileProvider.getUriForFile(getActivity(), "com.example.lakecircle.fileprovider", mPhotoFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        List<ResolveInfo> cameraActivities = getActivity().getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for ( ResolveInfo activity : cameraActivities ) {
            getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        startActivityForResult(takePictureIntent, CAM_CODE);
    }


    private void showSuccess() {
        Objects.requireNonNull(getContext()).setTheme(R.style.QMUITheme);
        QMUITipDialog tipDialog = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord("上传成功")
                .create();
        tipDialog.show();
        SpecialPostFragment fragment = this;
        mPostBtn.postDelayed(() -> {
            Objects.requireNonNull(getContext()).setTheme(R.style.AppTheme);
            NavHostFragment.findNavController(fragment).popBackStack();
            tipDialog.dismiss();
        }, 1500);
    }

    private void showError(String error) {
        Objects.requireNonNull(getContext()).setTheme(R.style.QMUITheme);
        QMUITipDialog tipDialog = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord(error)
                .create();
        tipDialog.show();
        this.getView().postDelayed(() -> {
            tipDialog.dismiss();
            Objects.requireNonNull(getContext()).setTheme(R.style.AppTheme);
        }, 1500);
    }

    private boolean checkNonNull() {
        if ( mPhotoUri == null )
            return false;
        //if ( mNameEt.getText().toString().length() == 0 )
          //  return false;
        if ( mIntroEt.getText().toString().length() == 0 )
            return false;
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getContext()).setTheme(R.style.AppTheme);
    }

}
