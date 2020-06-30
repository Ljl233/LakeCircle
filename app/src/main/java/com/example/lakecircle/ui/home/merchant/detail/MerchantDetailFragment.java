package com.example.lakecircle.ui.home.merchant.detail;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import com.example.lakecircle.R;
import com.example.lakecircle.ui.home.merchant.Merchant;
import com.example.lakecircle.ui.home.merchant.detail.coupon.MerchantCouponListFragment;
import com.example.lakecircle.ui.home.merchant.detail.special.MerchantSpecialListFragment;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.tabs.TabLayout;

public class MerchantDetailFragment extends Fragment {

    private Merchant merchant = new Merchant();

    private TextView mNameTv, mIntroTv, mAddressTv;
    private Toolbar mToolbar;
    private SimpleDraweeView mPicSdv;
    private TabLayout mTl;
    private ViewPager mVp;

    public static Bundle getBundle(Merchant merchant) {
        Bundle bundle = new Bundle();
        bundle.putInt("id",merchant.getId());
        bundle.putString("name", merchant.getName());
        bundle.putString("intro", merchant.getIntro());
        bundle.putString("address", merchant.getAddress());
        bundle.putString("url", merchant.getAvatar_url());
        bundle.putString("tel", merchant.getTel());
        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ( getArguments() != null ) {
            int id = getArguments().getInt("id");
            String name = getArguments().getString("name");
            String intro = getArguments().getString("intro");
            String address = getArguments().getString("address");
            String url = getArguments().getString("url");
            String tel = getArguments().getString("tel");
            merchant.setId(id);
            merchant.setAddress(address);
            merchant.setAvatar_url(url);
            merchant.setIntro(intro);
            merchant.setTel(tel);
            merchant.setName(name);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_merchant_detail, container , false);

        mNameTv = view.findViewById(R.id.tv_name_merchant_detail);
        mIntroTv = view.findViewById(R.id.tv_intro_merchant_detail);
        mAddressTv = view.findViewById(R.id.tv_address_merchant_detail);
        mToolbar = view.findViewById(R.id.tb_merchant_detail);
        mPicSdv = view.findViewById(R.id.sdv_merchant_detail);
        mTl = view.findViewById(R.id.tl_merchant_detail);
        mVp = view.findViewById(R.id.vp_merchant_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setNavigationOnClickListener(v -> {
                NavHostFragment.findNavController(this).popBackStack();
            });
        }
        mVp.setAdapter(new MyAdapter(getActivity().getSupportFragmentManager(),2));
        mTl.setupWithViewPager(mVp);

        mNameTv.setText(merchant.getName());
        mIntroTv.setText(merchant.getIntro());
        mAddressTv.setText(merchant.getAddress());
        mPicSdv.setImageURI(merchant.getAvatar_url());

        return view;
    }

    class MyAdapter extends FragmentPagerAdapter {

        private String[] mTitles = new String[]{"积分兑换", "特色"};

        public MyAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if ( position == 0 )
                return MerchantCouponListFragment.getInstance(merchant.getId());
            else
                return MerchantSpecialListFragment.getInstance(merchant.getId());
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }
}