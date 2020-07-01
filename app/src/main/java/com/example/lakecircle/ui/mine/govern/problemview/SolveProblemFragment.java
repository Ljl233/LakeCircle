package com.example.lakecircle.ui.mine.govern.problemview;

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
import com.facebook.drawee.view.SimpleDraweeView;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class SolveProblemFragment extends Fragment {

    private final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private final int MY_PERMISSIONS_REQUEST_ALBUM = 1;

    private final int CAM_CODE = 0;
    private final int ALB_CODE = 1;

    private int id;

    private Uri mPhotoUri;
    private File mPhotoFile;
    private String currentPhotoPath;

    private Toolbar mTb;
    private SimpleDraweeView mSdv;
    private EditText mEt;
    private ImageView mIv;
    private TextView mPicTv, mContentTv;
    private Button mBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            id = getArguments().getInt("id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solve_problem, container, false);

        mTb = view.findViewById(R.id.tb_solve_problem);
        mSdv = view.findViewById(R.id.sdv_solve_problem);
        mEt = view.findViewById(R.id.et_solve_problem);
        mIv = view.findViewById(R.id.iv_solve_problem);
        mPicTv = view.findViewById(R.id.tv_solve_problem_tip);
        mContentTv = view.findViewById(R.id.tv_solve_problem_content);
        mBtn = view.findViewById(R.id.btn_solve_problem);

        mPicTv.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTb.setNavigationOnClickListener(v -> {
                NavHostFragment.findNavController(this).popBackStack();
            });
        }

        mIv.setClickable(true);
        mIv.setOnClickListener(v -> {
            showBottomSheet();
        });

        mBtn.setOnClickListener(v -> {
            if (checkNull())
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

        getProblem();

        return view;
    }

    private void getProblem() {
        NetUtil.getInstance().getApi().getProblem(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ProblemResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(ProblemResponse problemResponse) {
                        if ( !problemResponse.getMessage().equals("OK") )
                            onError(new Throwable());
                        mContentTv.setText(problemResponse.getData().getContent());
                        mSdv.setImageURI(problemResponse.getData().getPictures().get(0));
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("SolveProblemFragment", "get problem fail");
                        showError("获取问题详情失败");
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
                    mIv.setImageURI(mPhotoUri);
                    mPicTv.setVisibility(View.GONE);
                }
                break;
            }
            case ALB_CODE : {
                try {
                    mPhotoUri = data.getData();
                    mIv.setImageURI(mPhotoUri);
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

    private void compressImage( ) {
        String filepath = ImageUtils.uriToPath(mPhotoUri, getContext(), getActivity().getContentResolver());
        File originalImage = new File(filepath);
        String targetDir = originalImage.getParentFile().getAbsolutePath();
        ImageUtils.compressImage(getContext(), originalImage, targetDir, new OnCompressListener() {
            @Override
            public void onStart() {Log.e("MineFragment", "start compress image"); }

            @Override
            public void onSuccess(File file) {
                postSolution(file);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.e("MineFragment", "compress image fail");
                showError("图片压缩失败");
            }
        });
    }

    private void postSolution(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        NetUtil.getInstance().getApi().uploadImage(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(stringResponse -> Log.e("SolveProblemFragment", "Image post Success"))
                .observeOn(Schedulers.io())
                .flatMap((Function<UrlResponse, Observable<SimpleResponse>>) urlResponse -> {
                    String solution = mEt.getText().toString();
                    String pic = urlResponse.getData().getUrl();
                    List<String> pics = new ArrayList<>();
                    pics.add(pic);
                    SolveProblemPostBean bean = new SolveProblemPostBean(solution, id, pics);
                    return NetUtil.getInstance().getApi().postSolution(bean);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SimpleResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                        if (!simpleResponse.getMessage().equals("OK"))
                            onError(new Throwable());
                        showSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("SolveProblemFragment", "post fail");
                        showError("提交失败");
                    }

                    @Override
                    public void onComplete() { }
                });
    }

    private boolean checkNull() {
        if ( mPhotoUri == null )
            return false;
        if ( mEt.getText().toString().length() == 0 )
            return false;
        return true;
    }

    private void showSuccess() {
        Objects.requireNonNull(getContext()).setTheme(R.style.QMUITheme);
        QMUITipDialog tipDialog = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord("提交成功")
                .create();
        tipDialog.show();
        SolveProblemFragment fragment = this;
        mBtn.postDelayed(() -> {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getContext()).setTheme(R.style.AppTheme);
    }
}
