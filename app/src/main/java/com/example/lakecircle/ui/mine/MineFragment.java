package com.example.lakecircle.ui.mine;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import com.example.lakecircle.R;
import com.example.lakecircle.commonUtils.FileUtils;
import com.example.lakecircle.commonUtils.NetUtil;
import com.example.lakecircle.commonUtils.SPUtils;
import com.example.lakecircle.ui.home.light.model.UserInfoBean;
import com.example.lakecircle.ui.home.upimage.UrlResponse;
import com.example.lakecircle.ui.login.login.LoginActivity;
import com.example.lakecircle.ui.login.login.model.UserWrapper;
import com.facebook.common.util.UriUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

import static android.app.Activity.RESULT_OK;
import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class MineFragment extends Fragment {

    private final int CAM_CODE = 0;
    private final int ALB_CODE = 1;

    private String currentPhotoPath;
    private File mPhotoFile;
    private Uri mPhotoUri;

    private String mAvatar;
    private int kind; //0-普通, 1-商家, 2-政府
    private String userName;
    private SPUtils spUtils = SPUtils.getInstance("userInfo");


    private AvatarView mAvatarAv;
    private TextView mUsernameTv;
    private TextView mGovernTv;
    private TextView mMerchantTv;
    private LinearLayout mGovernLl, mMerchantLl,mCouponLl, mLogoutLl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getUserInfo() {
        NetUtil.getInstance().getApi().getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserInfoBean>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(UserInfoBean userInfoResponseResponse) {
                        spUtils.put("avatar", userInfoResponseResponse.getData().getAvatar());
                        spUtils.put("kind", userInfoResponseResponse.getData().getKind());
                        spUtils.put("username", userInfoResponseResponse.getData().getUsername());

                        mAvatar = userInfoResponseResponse.getData().getAvatar();
                        kind = userInfoResponseResponse.getData().getKind();
                        userName = userInfoResponseResponse.getData().getUsername();

                        mAvatarAv.setAvatar(mAvatar);
                        mUsernameTv.setText(userName);
                        if (kind == 0) mAvatarAv.setCertificateGone();
                        else mAvatarAv.setCertificateVisible();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("MineFragment", "getUserInfo fail");
                    }

                    @Override
                    public void onComplete() { }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        NavController navController = findNavController(this);
        userName = spUtils.getString("username", "");
        mAvatar = spUtils.getString("avatar", "");
        kind = spUtils.getInt("kind", 0);
        getUserInfo();

        mAvatarAv = view.findViewById(R.id.av_mine);
        mUsernameTv = view.findViewById(R.id.tv_mine_username);
        mGovernTv = view.findViewById(R.id.tv_mine_government);
        mMerchantTv = view.findViewById(R.id.tv_mine_merchant);
        mGovernLl = view.findViewById(R.id.ll_mine_government);
        mMerchantLl = view.findViewById(R.id.ll_mine_merchant);
        mCouponLl = view.findViewById(R.id.ll_mine_coupon);
        mLogoutLl = view.findViewById(R.id.ll_mine_logout);

        mUsernameTv.setText(userName);

        switch (kind) {
            case 0: {
                mGovernTv.setText(R.string.govern_cer);
                mMerchantTv.setText(R.string.mer_cer);
                break;
            }
            case 1 : {
                mAvatarAv.setCertificateSrc(R.drawable.avatar_certificate);
                mAvatarAv.setCertificateVisible();

                mGovernLl.setVisibility(View.GONE);
                mMerchantLl.setVisibility(View.VISIBLE);

                mMerchantTv.setText(R.string.mer_entrance);
                break;
            }
            case 2 :
                mAvatarAv.setCertificateSrc(R.drawable.avatar_certificate);
                mAvatarAv.setCertificateVisible();

                mGovernLl.setVisibility(View.VISIBLE);
                mMerchantLl.setVisibility(View.GONE);

                mGovernTv.setText(R.string.govern_entrance);
                break;
        }

        mAvatarAv.setCertificateGone();
        mAvatarAv.setOnClickListener(v -> {
            showBottomSheet();
        });

        mMerchantLl.setOnClickListener(v -> {
            if ( kind == 0 )
                navController.navigate(R.id.action_certificate_merchant);
            else if ( kind == 1)
                navController.navigate(R.id.action_entrance_merchant);
        });

        mGovernLl.setOnClickListener(v -> {
            if ( kind == 0 )
                navController.navigate(R.id.action_certificate_government);
            else if ( kind == 2)
                navController.navigate(R.id.action_entrance_government);
        });

        mCouponLl.setOnClickListener(v -> navController.navigate(R.id.action_my_coupon));

        mLogoutLl.setOnClickListener(v -> {
            SPUtils.getInstance(SPUtils.SP_CONFIG).clear();
            SPUtils.getInstance("userInfo").clear();
            UserWrapper.getInstance().setUser(null);
            startActivity(new Intent(getContext(), LoginActivity.class));
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
            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            TakePicture();
        });
        //相册
        album.setOnClickListener(v -> {
            dialog.dismiss();//消失
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, ALB_CODE);
        });
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
                    postAvatar();
                }
                break;
            }
            case ALB_CODE : {
                try {
                    mPhotoUri = data.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                postAvatar();
                break;
            }
            default:
                break;
        }
    }

    private void postAvatar() {
        File file = FileUtils.getFile(getContext(), mPhotoUri);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        NetUtil.getInstance().getApi().uploadImage(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(stringResponse -> Log.e("MineFragment", "Image post Success"))
                .observeOn(Schedulers.io())
                .flatMap((Function<UrlResponse, Observable<SimpleResponse>>) imageUploadResponse -> {
                    mAvatar = imageUploadResponse.getData().getUrl();
                    return NetUtil.getInstance().getApi().postAvatar(new AvatarPostBean(mAvatar));
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SimpleResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        if ( !simpleResponse.getMessage().equals("OK"))
                            onError(new Throwable());
                        showSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("MineFragment", "post fail");
                        showError("上传头像失败");
                    }

                    @Override
                    public void onComplete() { }
                });

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

    private void showSuccess() {
        mAvatarAv.setAvatar(mAvatar);
        Objects.requireNonNull(getContext()).setTheme(R.style.QMUITheme);
        QMUITipDialog tipDialog = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord("头像修改成功")
                .create();
        tipDialog.show();
        this.getView().postDelayed(() -> {
            Objects.requireNonNull(getContext()).setTheme(R.style.AppTheme);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getContext()).setTheme(R.style.AppTheme);
    }

    public class AvatarPostBean {
        String avatar;

        public AvatarPostBean(String avatar) {
            this.avatar = avatar;
        }
    }


}
