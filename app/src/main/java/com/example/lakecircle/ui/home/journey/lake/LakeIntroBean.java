package com.example.lakecircle.ui.home.journey.lake;

import android.util.Log;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;

public class LakeIntroBean {

    /**
     * name : 外沙湖（武汉）
         * longitude_and_latitude : 114.334675,30.565848
     */

    private String name;
    private String longitude_and_latitude;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongitude_and_latitude() {
        return longitude_and_latitude;
    }

    public void setLongitude_and_latitude(String longitude_and_latitude) {
        this.longitude_and_latitude = longitude_and_latitude;
    }

    public LatLng getLatLng() {
        String[] strings = longitude_and_latitude.split(",");
        LatLng latLng = new LatLng(Double.parseDouble(strings[1]), Double.parseDouble(strings[0]));

        return latLng;
    }

    public float getDistance(LatLng latLng) {
        float distance = AMapUtils.calculateLineDistance(getLatLng(),latLng);
        return distance;
    }
}
