package com.example.lakecircle.ui.home.journey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.lakecircle.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_journey, container, false);

        mMapView = root.findViewById(R.id.journey_map);
        mMapView.onCreate(mSavedInstanceState);
        initMap();

        initBottomSheet(root);

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
        myLocationStyle.interval(1000);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);

        showLakes();

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
