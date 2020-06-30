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
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocation privLocation;

    private String TAG = "JourneyFragment:";
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


        mMapView = root.findViewById(R.id.journey_map);
        mMapView.onCreate(mSavedInstanceState);

        initBottomSheet(root);

        initMap();

        requestAllLakes();
        initAmapLocation();
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

        btStart.setOnClickListener(v -> {
            if (!isStartWalk) {
                if (nearestLake != null && mCurrentLatLng != null) {
                    if (nearestLake.getDistance(mCurrentLatLng) > 1) {
                        Toast.makeText(JourneyFragment.this.getContext(), "您附近没有河湖", Toast.LENGTH_SHORT).show();
                    } else {
                        startWalk();
                    }
                }
            } else {
                btStart.setText("开始");
                isStartWalk = false;
            }

        });
    }

    private void startWalk() {
        requestAllLakes();

        tvWalktime.setText("00:00:00");
        tvMiles.setText("0");
        tvIntergral.setText("0");
        layoutWatch.setVisibility(View.VISIBLE);
        btStart.setText("结束");
        isStartWalk = true;

        polyline = aMap.addPolyline((new PolylineOptions())
                .add(mCurrentLatLng)
                .width(10)
                .setDottedLine(false) //关闭虚线
                .color(Color.YELLOW));

        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        aMap.setMapTextZIndex(2);

        //开始计时
        countTimer();

    }

    private Runnable TimerRunnable = new Runnable() {

        @Override
        public void run() {
            if (isStartWalk) {
                timer += 1000;
                String hh = new DecimalFormat("00").format(timer / 3600);
                String mm = new DecimalFormat("00").format(timer % 3600 / 60);
                String ss = new DecimalFormat("00").format(timer % 60);
                String timeFormat = hh + ":" + mm + ":" + ss;
                tvWalktime.setText(timeFormat);
            }
            countTimer();
        }
    };

    private void countTimer() {
        mHandler.postDelayed(TimerRunnable, 1000);
    }


    /**
     * 初始化定位
     */
    private void initAmapLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(this.getContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        // 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Transport);
        //设置定位模式为AMapLocationMode.Hight_Accuracy，设备定位模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(2000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.startLocation();
        }
    }

    /**
     * 定位回调每1秒调用一次
     */
    public AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    mListener.onLocationChanged(amapLocation);// 显示系统小蓝点,不写这一句无法显示到当前位置
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    Log.e(TAG, "获取经纬度集合" + privLocation);//打Log记录点是否正确

                    amapLocation.getAccuracy();//获取精度信息
                    amapLocation.getBearing();//获取方向角信息
                    amapLocation.getSpeed();//获取速度信息  单位：米/秒
                    amapLocation.getLocationType();//查看是什么类型的点
                    Log.e(TAG, "获取点的类型" + amapLocation.getLocationType());
                    if (amapLocation.getLocationType() == 1) {

                        LatLng location = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());

                        drawLines(amapLocation);//一边定位一边连线
                        totaldistance += tempdistance;
                        Toast.makeText(JourneyFragment.this.getContext(), "经纬度" + totaldistance + "KM", Toast.LENGTH_SHORT).show();

                        Log.e("DDDDDDDDD", String.valueOf(totaldistance));

                        Log.e(TAG, "获取点的类型" + amapLocation.getLocationType());
                        Log.e("LLLLL", String.valueOf(location.latitude));
                        Log.e("LLLLLLLL", String.valueOf(location.longitude));
                    }
                    //获取定位时间
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(amapLocation.getTime());
                    sdf.format(date);
                    privLocation = amapLocation;
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:" + amapLocation.getErrorInfo());
                }
            }
        }
    };

    /**
     * 绘制运动路线
     *
     * @param curLocation
     */
    public void drawLines(AMapLocation curLocation) {

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


    private void initMap() {
        if (aMap == null) aMap = mMapView.getMap();
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(2000);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);

        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            }
        });
    }

    private void setMilesAndIntegral(double totaldistance) {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            lakeIntroBeans.sort(new Comparator<LakeIntroBean>() {
                @Override
                public int compare(LakeIntroBean o1, LakeIntroBean o2) {
                    return (int) (o1.getDistance(currentLatLng) - o2.getDistance(currentLatLng));
                }
            });
        }
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
