package com.example.lakecircle.ui.home.journey;

import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.lakecircle.R;
import com.example.lakecircle.commonUtils.BaseResponseModel;
import com.example.lakecircle.commonUtils.NetUtil;
import com.example.lakecircle.ui.home.journey.lake.LakeIntroBean;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 河湖行
 */
public class JourneyFragment extends Fragment {

    private MapView mMapView;
    private AMap aMap;
    private Bundle mSavedInstanceState;
    private List<LatLng> mLatLngs;
    private ArrayList<MarkerOptions> markerOptions;
    private View mBottomSheet;
    private BottomSheetBehavior mBehavior;
    private ImageView mBtDown, mBtUp;
    private LatLng mCurrentLatLng;
    private boolean getLatLngOnly = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_journey, container, false);

        Toolbar toolbar = root.findViewById(R.id.journey_toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        mMapView = root.findViewById(R.id.journey_map);
        mMapView.onCreate(mSavedInstanceState);
        initMap();

        initBottomSheet(root);
        requestAllLakes();
        return root;
    }

    private void initBottomSheet(View root) {
        mBtDown = root.findViewById(R.id.walk_button_down);
        mBtDown.setOnClickListener(v -> mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN));

        mBtUp = root.findViewById(R.id.walk_button_up);
        mBtUp.setOnClickListener(v -> mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED));

        mBottomSheet = root.findViewById(R.id.bottom_sheet);
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

    private void initMap() {
        if (aMap == null) aMap = mMapView.getMap();
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(100000);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);

        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                if (getLatLngOnly) {

                }
            }
        });
        showLakes();
    }

    private String getNearestLake(List<LakeIntroBean> lakeIntroBeans, LatLng currentLatLng) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            lakeIntroBeans.sort(new Comparator<LakeIntroBean>() {
                @Override
                public int compare(LakeIntroBean o1, LakeIntroBean o2) {
                    return (int) (o1.getDistance(currentLatLng) - o2.getDistance(currentLatLng));
                }
            });
        }
        return lakeIntroBeans.get(0).getName();
    }

    private void requestAllLakes() {
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
                        String nearestLake = getNearestLake(lakeIntroBeans, mCurrentLatLng);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void showLakes() {
        mLatLngs = getLakesLatLngs();
        markerOptions = new ArrayList<>();

        for (LatLng latLng : mLatLngs) {
            MarkerOptions markerOption = new MarkerOptions();
            markerOption.position(latLng);
            markerOptions.add(markerOption);
        }

        aMap.addMarkers(markerOptions, true);
    }

    private List<LatLng> getLakesLatLngs() {
        List<LatLng> latLngs = new ArrayList<>();
        //todo:get all lakes latlngs
        return latLngs;
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
}
