package com.example.lakecircle.ui.home.merchant;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lakecircle.R;
import com.example.lakecircle.commonUtils.NetUtil;
import com.example.lakecircle.ui.home.merchant.detail.MerchantDetailFragment;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MerchantListFragment extends Fragment {

    private static int preState = 0;

    private List<Merchant> mMerchants = new ArrayList<>();

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
                public void onMoreClick() {

                }

                @Override
                public void onRefreshClick() {

                }
            };

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(int position) {
            if ( position != preState ) {
                preState = position;
                if (position == 0) {
                    Collections.sort(mMerchants, new MerchantDistanceComparator());
                } else {
                    /*mMerchants.sort(mMerchants, new MerchantChangeNumComparator());*/
                }
            }

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNavController = NavHostFragment.findNavController(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_merchant, container, false);

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
        mSortSp.setAdapter(new SpinnerAdapter(getContext(), mSortList,mOnClickListener));
        mSortIb.setOnClickListener(v -> mSortSp.performClick());

        mAdapter = new MerchantAdapter(new ArrayList<>(), getContext(), mOnMerchantClickListener);


        mSearchSv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { getMerchants(query); return false;}

            @Override
            public boolean onQueryTextChange(String newText) { return false; }
        });

        getMerchants("");

        return view;
    }

    private void getMerchants(String name) {
        MerchantNameBean b = new MerchantNameBean(name);
        mMerchants.clear();
        NetUtil.getInstance().getApi().getMerchants("1", "50", b)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MerchantListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(MerchantListResponse merchantListResponse) {
                        List<Observable<MerchantInfoResponse>> list = new ArrayList<>();
                        for (int i = 0 ; i <merchantListResponse.getData().getData().size() ; i++ ) {
                            MerchantListResponse.DataBeanX.DataBean b = merchantListResponse.getData().getData().get(i);
                            mMerchants.add(new Merchant(b.getId(),b.getChange_num(),b.getName(),b.getIntro(),b.getAvatar_url()));
                            getMerchant(b.getId(),i);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("MerchantListFragment", "get merchants fail");
                        showError("加载失败");
                    }

                    @Override
                    public void onComplete() { }
                });

    }

    private void getMerchant(int id,int i) {
        NetUtil.getInstance().getApi().getMerchant(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MerchantInfoResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(MerchantInfoResponse merchantInfoResponse) {
                        MerchantInfoResponse.DataBean d = merchantInfoResponse.getData();
                        Merchant t = mMerchants.get(i);
                        t.setTel(d.getTel());
                        t.setAddress(d.getAddress());
                        mMerchants.set(i,t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("MerchantListFragment", "get merchant fail");
                        showError("加载失败");
                    }

                    @Override
                    public void onComplete() { }
                });
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

    public interface OnClickListener {
        void onClick(int position);
    }

    public class SpinnerAdapter extends BaseAdapter {

        private List<String> dataList;
        private Context mContext;
        private OnClickListener mOnClickListener;

        public SpinnerAdapter(Context mContext, List<String> list, OnClickListener onClickListener) {
            this.mContext = mContext;
            dataList = list;
            mOnClickListener = onClickListener;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            TextView textView = view.findViewById(android.R.id.text1);
            textView.setText(dataList.get(position));
            view.setOnClickListener(v -> {mOnClickListener.onClick(position);});
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

    }



}
