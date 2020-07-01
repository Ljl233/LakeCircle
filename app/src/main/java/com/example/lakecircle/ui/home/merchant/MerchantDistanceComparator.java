package com.example.lakecircle.ui.home.merchant;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.example.lakecircle.data.Merchant.Merchant;

import java.util.Comparator;


public class MerchantDistanceComparator implements Comparator<Merchant> {

    private LatLng mLocation;//我的坐标

    public MerchantDistanceComparator(LatLng location) {
        mLocation = location;
    }

    @Override
    public int compare(Merchant o1, Merchant o2) {
        return (int)
                (AMapUtils.calculateLineDistance(mLocation, new LatLng(o1.getLatitude(), o1.getLongitude()))
               - AMapUtils.calculateLineDistance(mLocation, new LatLng(o2.getLatitude(), o2.getLongitude())));

    }

}
