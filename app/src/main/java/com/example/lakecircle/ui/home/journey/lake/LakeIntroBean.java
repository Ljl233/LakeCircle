package com.example.lakecircle.ui.home.journey.lake;

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
        LatLng latLng = new LatLng(Double.parseDouble(strings[1]), Double.parseDouble(strings[0]));

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
