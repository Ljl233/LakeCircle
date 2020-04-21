package com.example.lakecircle.ui.home.home;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.example.lakecircle.R;

public class HomeFragment extends Fragment {

    private SearchView mSearchView;
    private TextView mTvAddress, mTvTimeLake, mTvTravel, mTvLakeWalk, mTvLightLake, mTvWelcome;
    private ImageView mIvProblem, mIvActivity, mIvMerchant, mIvNotice;
    private NavController mNavController;
    private String TAG = "HomeFragment: ";
    private MapView mMapView = null;
    private Bundle mSaveInstanceState;
    private AMap aMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mSaveInstanceState = savedInstanceState;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        view.setOnClickListener(v -> {
            if (mSearchView.isFocused())
                mSearchView.clearFocus();
        });
        mSearchView = view.findViewById(R.id.searchView);
        mTvAddress = view.findViewById(R.id.home_address);
        mTvLakeWalk = view.findViewById(R.id.home_tv_lakewalk);
        mTvLightLake = view.findViewById(R.id.home_tv_lightlake);
        mTvTimeLake = view.findViewById(R.id.home_tv_timelake);
        mTvTravel = view.findViewById(R.id.home_tv_travel);
        mIvActivity = view.findViewById(R.id.home_iv_detail);
        mIvMerchant = view.findViewById(R.id.home_iv_merchant);
        mIvProblem = view.findViewById(R.id.home_iv_problem);
        mIvNotice = view.findViewById(R.id.home_iv_notice);
        mTvWelcome = view.findViewById(R.id.home_tv_welcome);
        mMapView = view.findViewById(R.id.home_map);
        mMapView.onCreate(mSaveInstanceState);
        mNavController = NavHostFragment.findNavController(this);

        initView();
        initMap();
        return view;
    }

    private void initMap() {
        //1.显示地图
        //2.获得位置-》市
        //3.控制缩放

        //1.显示地图
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        GeocodeSearch geocodeSearch = new GeocodeSearch(this.getContext());
        //2.get location
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLonPoint latLonPoint = new LatLonPoint(location.getLatitude(), location.getLongitude());
                // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                RegeocodeQuery regeocodeQuery = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
                geocodeSearch.getFromLocationAsyn(regeocodeQuery);
            }
        });

        //通过搜索获得具体城市名
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            //逆地理编码（坐标转地址）
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                //成功码1000,获取城市名
                if (i == 1000) {
                    String city = regeocodeResult.getRegeocodeAddress().getCity();
                    String s = "欢迎来到" + city;
                    mTvWelcome.setText(s);
                }
            }

            //地理编码（地址转坐标）
            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });


    }

    private void initView() {
        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.e(TAG, v.toString() + ",hasFocus:" + hasFocus);
            }
        });
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "OnSearchClickListener", Toast.LENGTH_SHORT).show();
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e(TAG, ":" + query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e(TAG, ":" + newText);
                return false;
            }
        });

        mTvAddress.setOnClickListener(v -> {

        });

        mTvLakeWalk.setOnClickListener(v -> {

        });

        mTvLightLake.setOnClickListener(v -> {
            NavOptions options = new NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_in_right)
                    .setExitAnim(R.anim.hide_out)
                    .setPopEnterAnim(R.anim.show_in)
                    .setPopExitAnim(R.anim.slide_out_right)
                    .build();
            mNavController.navigate(R.id.light_dest, null, options);
        });

        mTvTimeLake.setOnClickListener(v -> {

        });

        mTvTravel.setOnClickListener(v -> {

        });

        mIvActivity.setOnClickListener(v -> {

        });

        mIvMerchant.setOnClickListener(v -> {

        });

        mIvProblem.setOnClickListener(v -> {
            NavOptions options = new NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_in_right)
                    .setExitAnim(R.anim.hide_out)
                    .setPopEnterAnim(R.anim.show_in)
                    .setPopExitAnim(R.anim.slide_out_right)
                    .build();
            mNavController.navigate(R.id.ulq_dest, null, options);
        });
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

}
