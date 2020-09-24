package com.example.lakecircle.ui.home.merchant;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.model.LatLng;
import com.example.lakecircle.MyApp;
import com.example.lakecircle.R;
import com.example.lakecircle.data.Lake.LakeDatabase;
import com.example.lakecircle.data.Merchant.Merchant;
import com.example.lakecircle.data.Merchant.MerchantDatabase;
import com.example.lakecircle.ui.home.merchant.detail.MerchantDetailFragment;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MerchantListFragment extends Fragment {

    private MerchantPresenter mPresenter;

    private AMapLocationClient locationClient = null;
    private LatLng mLocation = null;

    private Toolbar mToolbar;
    private RecyclerView mRv;
    private AppCompatSpinner mSortSp;
    private ImageButton mSortIb;
    private SearchView mSearchSv;
    private ArrayAdapter<String> mSpAdapter;
    private List<String> mSortList = new ArrayList<>();
    private MerchantAdapter mAdapter;
    private NavController mNavController;

    private MerchantAdapter.OnMerchantClickListener mOnMerchantClickListener =
            new MerchantAdapter.OnMerchantClickListener() {
                @Override
                public void onItemClick(Merchant merchant) {
                    mNavController.navigate(R.id.action_merchant_detail,
                            MerchantDetailFragment.getBundle(merchant));
                }
                @Override
                public void onRefreshClick() {
                    if ( mLocation == null )
                        locationClient.startLocation();
                    else
                        mPresenter.getMerchants(mLocation, true);
                }
            };

    private void initPermission () {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.RECORD_AUDIO}, 1);
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(getContext(), "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPermission();
        mNavController = NavHostFragment.findNavController(this);
        mPresenter = new MerchantPresenter(this, MerchantDataSource.getInstance(
                LakeDatabase.getInstance(getContext()).lakeDao(),
                MerchantDatabase.getInstance(getContext()).merchantDao()));
        initLocation();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_merchant, container, false);

        mAdapter = new MerchantAdapter(new ArrayList<>(), getContext(), mOnMerchantClickListener, 0, null);
        locationClient.startLocation();

        mToolbar = view.findViewById(R.id.tb_merchant);
        mRv = view.findViewById(R.id.rv_merchant);
        mSearchSv = view.findViewById(R.id.sv_merchant);
        mSortIb = view.findViewById(R.id.ib_sort_merchant);
        mSortSp = view.findViewById(R.id.spn_sort_merchant);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setNavigationOnClickListener(v -> {
                NavHostFragment.findNavController(this).popBackStack();
            });
        }

        mSortList.add("按兑换次数");
        mSortList.add("按距离   ");
        mSpAdapter = new ArrayAdapter<>(getContext(),R.layout.spinner_activities_layout, mSortList);
        mSpAdapter.setDropDownViewResource(R.layout.spinner_activities_dropdown_item);
        mSortSp.setBackgroundColor(0x0);
        mSortSp.setAdapter(new SpinnerAdapter(getContext(), mSortList));
        mSortIb.setOnClickListener(v -> mSortSp.performClick());

        mRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mRv.setAdapter(mAdapter);

        mSearchSv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { mPresenter.getMerchants(query); return false;}

            @Override
            public boolean onQueryTextChange(String newText) { return false; }
        });

        return view;
    }

    private void initLocation() {
        //初始化client
        //Toast.makeText(getContext(), "initLocation", Toast.LENGTH_LONG).show();
        locationClient = new AMapLocationClient(MyApp.getAppContext());

        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mOption.setHttpTimeOut(30000);
        mOption.setInterval(500);
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        // 设置定位参数
        locationClient.setLocationOption(mOption);
        //设置定位监听
        locationClient.setLocationListener(location -> {
            if (null != location) {
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {
                    Log.e("location",location.toString());
                    mLocation = new LatLng(location.getLatitude(),location.getLongitude());
                    mPresenter.getMerchants(mLocation, true);
                    mAdapter.setLocation(mLocation);
                    //Toast.makeText(getContext(), "mLocation = ("+location.getLongitude()+", "+location.getLatitude()+")", Toast.LENGTH_LONG).show();
                    locationClient.stopLocation();
                }
                else{}
                    //showError("定位失败");
            } else
                showError("定位失败，loc is null");
            });
    }

    public void setPresenter(MerchantPresenter presenter) {
        mPresenter = presenter;
    }

    public void put(List<Merchant> merchants) {
        mAdapter.setData(merchants);
    }

    public void showError(String error) {
        requireContext().setTheme(R.style.QMUITheme);
        QMUITipDialog tipDialog = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord(error)
                .create();
        tipDialog.show();
        this.getView().postDelayed(() -> {
            tipDialog.dismiss();
            requireContext().setTheme(R.style.AppTheme);
        }, 1500);
    }

    public class SpinnerAdapter extends ArrayAdapter<String> implements AdapterView.OnItemClickListener {

        private List<String> dataList;
        private Context mContext;
        public SpinnerAdapter(Context mContext, List<String> list) {
            super(mContext, android.R.layout.simple_list_item_1, list);
            this.mContext = mContext;
            dataList = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            TextView textView = view.findViewById(android.R.id.text1);
            textView.setText(dataList.get(position));
            return view;
        }

        @Override
        public int getCount() {
            return dataList == null ? 0 : dataList.size();
        }

        @Override
        public String getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mAdapter.setSortMode(position);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        locationClient.stopLocation();
    }
}
