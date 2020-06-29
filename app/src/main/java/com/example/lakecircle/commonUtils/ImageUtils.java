package com.example.lakecircle.commonUtils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.lakecircle.ui.home.upimage.ULQActivity;
import com.example.lakecircle.ui.home.upimage.UrlResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

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

public class ImageUtils {

    // Uri 转 filepath
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String uriToPath(Uri uri, Context context, ContentResolver contentResolver) {
        String filePath = null;
        if (uri != null && "content".equals(uri.getScheme())) {
            Log.e("ulq:", "uri=" + uri);

            if (DocumentsContract.isDocumentUri(context, uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    Log.d("ulq", uri.toString());
                    String id = docId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    filePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, contentResolver);
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Log.d("ulq", uri.toString());
                    Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(docId));
                    filePath = getImagePath(contentUri, null, contentResolver);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                Log.d("ulq", "content: " + uri.toString());
                filePath = getImagePath(uri, null, contentResolver);
            }

        } else {
            filePath = uri.getPath();
        }
        return filePath;
    }

    public static String getImagePath(Uri uri, String selection, ContentResolver contentResolver) {
        String path = null;
        Cursor cursor = contentResolver.query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     *
     * @param context
     * @param originalImage
     * @param targetDir 目标文件路径，不用包含文件名， "originalImage.getParentFile().getPath()"可以使用父路径，放在同一个文件夹下
     * @param listener
     */
    public static void compressImage(Context context, File originalImage, String targetDir, OnCompressListener listener) {
        Luban.with(context)
                .load(originalImage)
                .ignoreBy(100)
                .setTargetDir(targetDir)
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(listener).launch();
    }

    //上传图片获得url，url在observable中
    public static void uploadImage(File file,Observer<? super UrlResponse> observable ){
        RequestBody requestFile = RequestBody.create(
                MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part data = MultipartBody.Part.createFormData(
                "image", file.getName(), requestFile);

        NetUtil.getInstance().getApi().uploadImage(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observable);
    }


}
