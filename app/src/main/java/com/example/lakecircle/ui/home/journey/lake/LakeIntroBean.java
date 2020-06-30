package com.example.lakecircle.ui.home.journey.lake;

import android.util.Log;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;

public class LakeIntroBean {


    /**
     * id : 0
     * longitude_and_latitude : string
     * name : string
     */

    private int id;
    private String longitude_and_latitude;
    private String name;

    public LatLng getLatLng() {
        String[] strings = longitude_and_latitude.split(",");
        LatLng latLng = new LatLng(Double.valueOf(strings[1]), Double.parseDouble(strings[0]));
        Log.e("longitude_and_latitude", "strings[0]:" + strings[0] + ",strings[1]" + strings[1]);
        Log.e("longitude_and_latitude", "Dstrings[0]:" +latLng.latitude + ",Dstrings[1]" + latLng.longitude);

        return latLng;
    }

    public float getDistance(LatLng latLng) {
        float distance = AMapUtils.calculateLineDistance(getLatLng(), latLng);
        return distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLongitude_and_latitude() {
        return longitude_and_latitude;
    }

    public void setLongitude_and_latitude(String longitude_and_latitude) {
        this.longitude_and_latitude = longitude_and_latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
