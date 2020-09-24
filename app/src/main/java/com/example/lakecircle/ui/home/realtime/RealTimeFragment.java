package com.example.lakecircle.ui.home.realtime;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.lakecircle.R;
import com.example.lakecircle.commonUtils.BaseResponseModel;
import com.example.lakecircle.commonUtils.NetUtil;
import com.example.lakecircle.ui.home.journey.lake.LakeIntroBean;
import com.example.lakecircle.ui.home.light.LightFragment;
import com.example.lakecircle.ui.home.light.model.LakeNameBean;
import com.example.lakecircle.ui.home.light.model.LakeUrlResponse;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;
import java.util.Objects;
import java.util.Observable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;

public class RealTimeFragment extends Fragment {

    private Bundle mSavedInstanceState;
    private MapView mMapView;
    private AMap aMap;
    private RecyclerView mRecyclerView;
    private LatLng mCurrentLatLng;
    private SearchView mSearchView;
    private ImageView mBtDown;
    private BottomSheetBehavior<View> mBehavior;
    private ImageView mBtUp;
    private View mBottomSheet;
    private RealtimeAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_realtime, container, false);

        Toolbar toolbar = root.findViewById(R.id.realtime_toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        mMapView = root.findViewById(R.id.realtime_map);
        mMapView.onCreate(mSavedInstanceState);
        initMap();

        mSearchView = root.findViewById(R.id.realtime_searchView);
        initSearchView();

        mRecyclerView = root.findViewById(R.id.realtime_list);
        initRecyclerView();

        initBottomSheet(root);
        return root;
    }

    private void initBottomSheet(View root) {
        mBtDown = root.findViewById(R.id.realtime_button_down);
        mBtDown.setOnClickListener(v -> mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN));

        mBtUp = root.findViewById(R.id.realtime_button_up);
        mBtUp.setOnClickListener(v -> mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED));

        mBottomSheet = root.findViewById(R.id.realtime_bottom_sheet);
        mBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBehavior.setHideable(true);
        mBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void initSearchView() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLake(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void searchLake(String query) {
        LakeNameBean nameBean = new LakeNameBean();
        String lakeName = resolveQuery(query);
        nameBean.setName(lakeName);

        NetUtil.getInstance().getApi().searchLakeIntro(lakeName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseModel<LakeIntroBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponseModel<LakeIntroBean> lakeIntroBeanBaseResponseModel) {
                        if (mAdapter != null) {
                            mAdapter.changeDataset(lakeIntroBeanBaseResponseModel.getData());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(RealTimeFragment.this.getContext(), "搜索失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        requireActivity().getCurrentFocus().clearFocus();
                        mSearchView.clearFocus();
                    }
                });
    }

    private String resolveQuery(String query) {
        return query;
    }


    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));

        NetUtil.getInstance().getApi().getAllLakeIntro()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseModel<LakeIntroBean>>() {


                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponseModel<LakeIntroBean> lakeIntroBeanBaseResponseModel) {
                        List<LakeIntroBean> lakeIntroBeans = lakeIntroBeanBaseResponseModel.getData();
                        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
                            @Override
                            public void onMyLocationChange(Location location) {
                                mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            }
                        });
                        mAdapter = new RealtimeAdapter(lakeIntroBeans, mCurrentLatLng, RealTimeFragment.this);
                        mRecyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(RealTimeFragment.this.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initMap() {
        if (aMap == null) aMap = mMapView.getMap();
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        Bitmap myLocationMaker = BitmapFactory.decodeResource(getResources(), R.drawable.mylocation_maker);
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(changeBitmapSize(myLocationMaker,60,60)));
        myLocationStyle.interval(100000);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);

        showAllLakesPoint();
    }

    private void showAllLakesPoint() {
        NetUtil.getInstance().getApi().getAllLakeIntro()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseModel<LakeIntroBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponseModel<LakeIntroBean> lakeIntroBeanBaseResponseModel) {
                        List<LakeIntroBean> lakeIntroBeans = lakeIntroBeanBaseResponseModel.getData();
                        showAllLakesPoint(lakeIntroBeans);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void showAllLakesPoint(List<LakeIntroBean> lakeIntroBeans) {
        for (LakeIntroBean intro : lakeIntroBeans) {
            MarkerOptions markerOptions = new MarkerOptions().position(intro.getLatLng())
                    .snippet("DefaultMarker");

            Bitmap locationMaker = BitmapFactory.decodeResource(getResources(), R.drawable.location_maker);

            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(changeBitmapSize(locationMaker,50,60)));
            Marker marker = aMap.addMarker(markerOptions);
        }
    }

    private Bitmap changeBitmapSize(Bitmap originImage, int newWidth, int newHeight) {
        int width = originImage.getWidth();
        int height = originImage.getHeight();

        float scaleWidth = newWidth / (float) width;
        float scaleHeight = newHeight / (float) height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleWidth);

        return Bitmap.createBitmap(originImage, 0, 0, width, height, matrix, true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(mSavedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    public void jump2Retail(int lakeId) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", lakeId);
        NavOptions options = new NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_right)
                .setExitAnim(R.anim.hide_out)
                .setPopEnterAnim(R.anim.show_in)
                .setPopExitAnim(R.anim.slide_out_right)
                .build();

        NavHostFragment.findNavController(this)
                .navigate(R.id.lakedetail_dest, bundle, options);
    }
}
