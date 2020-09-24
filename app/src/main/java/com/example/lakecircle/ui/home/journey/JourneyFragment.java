package com.example.lakecircle.ui.home.journey;

import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.example.lakecircle.R;
import com.example.lakecircle.commonUtils.BaseResponseModel;
import com.example.lakecircle.commonUtils.NetUtil;
import com.example.lakecircle.ui.home.journey.lake.LakeIntroBean;
import com.example.lakecircle.ui.home.journey.lake.MilesBean;
import com.example.lakecircle.ui.home.journey.lake.MilesResponse;
import com.example.lakecircle.ui.home.realtime.RealtimeResponse;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.amap.api.maps.model.MyLocationStyle.LOCATION_TYPE_LOCATE;
import static com.amap.api.maps.model.MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE;

/**
 * 河湖行
 */
public class JourneyFragment extends Fragment {

    private Bundle mSavedInstanceState;

    private View mBottomSheet;
    private BottomSheetBehavior mBehavior;
    private ImageView mBtDown, mBtUp;
    private LatLng mCurrentLatLng;
    private Button btStart;
    private ConstraintLayout layoutWatch;
    private TextView tvLakename, tvDescription, tvWalktime, tvMiles, tvIntergral;
    private ImageView ivCamera, ivFlag;

    private List<LatLng> mLatLngs;
    private ArrayList<MarkerOptions> markerOptions;
    private boolean getLatLngOnly = true;
    private boolean isStartWalk = false;
    private List<LatLng> latLngs = new ArrayList<>();
    private LakeIntroBean nearestLake;
    private Map<Integer, RealtimeResponse.DataBean> lakeInfoMap = new HashMap<>();
    private List<LakeIntroBean> lakeIntroBeans;
    private double tempdistance, totaldistance;

    private MapView mMapView;
    private AMap aMap;
    private Polyline polyline;
    private Location privLocation;

    private String TAG = "Loglog:";
    private Handler mHandler = new Handler();
    private String timeStr = "";
    private int timer = 0;

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
        ivCamera = root.findViewById(R.id.journey_camera);
        ivCamera.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.ulq_dest);
        });
        ivFlag = root.findViewById(R.id.journey_flag);
        ivFlag.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.activities_dest);
        });


        mMapView = root.findViewById(R.id.journey_map);
        mMapView.onCreate(mSavedInstanceState);

        initBottomSheet(root);

        initMap();

        requestAllLakes();
        btStart.setOnClickListener(v -> {
            if (!isStartWalk) {
                isStartWalk = true;
                if (nearestLake != null && mCurrentLatLng != null) {
//                    if (nearestLake.getDistance(mCurrentLatLng) > 1) {
//                        Toast.makeText(JourneyFragment.this.getContext(), "您附近没有河湖", Toast.LENGTH_SHORT).show();
//                    } else {
                    startWalk();
                    Log.e("draw line", "========================================>start");
//                    }
                }

            } else {
                btStart.setText("开始");
                Log.e("draw line", "======================================>end");
                isStartWalk = false;
                addMiles(totaldistance);
            }
        });
        return root;
    }

    private void initBottomSheet(View root) {
        tvLakename = root.findViewById(R.id.walk_lake_name);
        tvDescription = root.findViewById(R.id.walk_lake_description);

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

        layoutWatch = root.findViewById(R.id.walk_watch);
        layoutWatch.setVisibility(View.GONE);
        btStart = root.findViewById(R.id.walk_start);

        tvMiles = root.findViewById(R.id.walk_distance);
        tvIntergral = root.findViewById(R.id.walk_integral);
        tvWalktime = root.findViewById(R.id.walk_time);


    }

    private void startWalk() {
        requestAllLakes();

        tvWalktime.setText("00:00:00");
        tvMiles.setText("0");
        tvIntergral.setText("0");
        aMap.clear();
        layoutWatch.setVisibility(View.VISIBLE);
        btStart.setText("结束");
        isStartWalk = true;

        polyline = aMap.addPolyline((new PolylineOptions())
                .add(mCurrentLatLng)
                .width(10)
                .setDottedLine(false) //关闭虚线
                .color(Color.YELLOW));

        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        aMap.setMapTextZIndex(2);

//        initAmapLocation();

        //开始计时
        countTimer();
        totaldistance = 0;
        timer = 0;

    }

    private Runnable TimerRunnable = new Runnable() {

        @Override
        public void run() {
            if (isStartWalk) {
                timer += 1;
                String hh = new DecimalFormat("00").format(timer / 3600);
                String mm = new DecimalFormat("00").format(timer % 3600 / 60);
                String ss = new DecimalFormat("00").format(timer % 60);
                String timeFormat = hh + ":" + mm + ":" + ss;
                tvWalktime.setText(timeFormat);
                countTimer();
            }
        }
    };

    private void countTimer() {
        mHandler.postDelayed(TimerRunnable, 1000);
    }

    private void initMap() {
        if (aMap == null) aMap = mMapView.getMap();
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(2000);
        myLocationStyle.showMyLocation(true);
        myLocationStyle.myLocationType(LOCATION_TYPE_LOCATION_ROTATE);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);

        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                drawLines(location);//一边定位一边连线
                totaldistance += tempdistance;
                setMilesAndIntegral(totaldistance);

                privLocation = location;
            }
        });
    }

    /**
     * 绘制运动路线
     *
     * @param curLocation
     */
    public void drawLines(Location curLocation) {

        if (!isStartWalk) return;
        if (null == privLocation) {
            return;
        }
        PolylineOptions options = new PolylineOptions();
        //上一个点的经纬度
        options.add(new LatLng(privLocation.getLatitude(), privLocation.getLongitude()));
        //当前的经纬度
        options.add(new LatLng(curLocation.getLatitude(), curLocation.getLongitude()));
        options.width(10).geodesic(true).color(Color.GREEN);
        aMap.addPolyline(options);
        //距离的计算
        tempdistance = AMapUtils.calculateLineDistance(new LatLng(privLocation.getLatitude(),
                privLocation.getLongitude()), new LatLng(curLocation.getLatitude(),
                curLocation.getLongitude()));

    }

    private void setMilesAndIntegral(double totaldistance) {
        //传进的参数的单位是米，需要千米
        totaldistance = totaldistance / 1000.0;
        tvMiles.setText(String.valueOf((int) totaldistance));
        mile2integral((int) totaldistance);
    }

    private void mile2integral(int mile) {
        tvIntergral.setText(String.valueOf(10 * mile));
    }

    //获得所有河湖的id，name，latlng
    private void requestAllLakes() {
        if (lakeIntroBeans != null) {
            nearestLake = getNearestLake(lakeIntroBeans, mCurrentLatLng);
            tvLakename.setText(nearestLake.getName());
            getLakeInfo(nearestLake.getId());
        } else {
            NetUtil.getInstance().getApi().getAllLakeIntro()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<BaseResponseModel<LakeIntroBean>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(BaseResponseModel<LakeIntroBean> lakeIntroBeanBaseResponseModel) {
                            lakeIntroBeans = lakeIntroBeanBaseResponseModel.getData();
                            nearestLake = getNearestLake(lakeIntroBeans, mCurrentLatLng);
                            tvLakename.setText(nearestLake.getName());
                            getLakeInfo(nearestLake.getId());
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    private LakeIntroBean getNearestLake(List<LakeIntroBean> lakeIntroBeans, LatLng currentLatLng) {
        if (nearestLake != null) return nearestLake;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            lakeIntroBeans.sort(new Comparator<LakeIntroBean>() {
                @Override
                public int compare(LakeIntroBean o1, LakeIntroBean o2) {
                    return (int) (o1.getDistance(currentLatLng) - o2.getDistance(currentLatLng));
                }
            });
        }
        nearestLake = lakeIntroBeans.get(0);
        return lakeIntroBeans.get(0);
    }

    //用于显示河湖描述
    private void getLakeInfo(int id) {
        if (lakeInfoMap.get(id) != null) {
            tvDescription.setText(lakeInfoMap.get(id).getIntroduction());
        } else {
            NetUtil.getInstance().getApi().getLakeInfo(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<RealtimeResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(RealtimeResponse realtimeResponse) {
                            lakeInfoMap.put(id, realtimeResponse.getData());
                            tvDescription.setText(realtimeResponse.getData().getIntroduction());
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    private void addMiles(double miles) {

        MilesBean milesBean = new MilesBean();
        milesBean.setMileage((int) miles);
        milesBean.setIntergral(((int) miles) * 10);
        NetUtil.getInstance().getApi()
                .addMiles(milesBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MilesResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MilesResponse milesResponse) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(JourneyFragment.this.getContext(), "上传失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

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
