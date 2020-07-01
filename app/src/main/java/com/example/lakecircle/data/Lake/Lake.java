package com.example.lakecircle.data.Lake;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.amap.api.maps.model.LatLng;

@Entity(tableName = "Lake")
public final class Lake {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private final int id;
    @ColumnInfo(name = "name")
    private final String name;
    @ColumnInfo(name = "latitude")
    private final double latitude;
    @ColumnInfo(name = "longitude")
    private final double longitude;

    public Lake(int id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
