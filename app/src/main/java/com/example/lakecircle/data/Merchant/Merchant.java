package com.example.lakecircle.data.Merchant;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Merchant")
public final class Merchant {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private final int id;
    @ColumnInfo(name = "change_num")
    private final int change_num;
    @ColumnInfo(name = "avatar_url")
    private final String avatar_url;
    @ColumnInfo(name = "near")
    private final String near;
    @ColumnInfo(name = "name")
    private final String name;
    @ColumnInfo(name = "intro")
    private final String intro;
    @ColumnInfo(name = "address")
    private final String address;
    @ColumnInfo(name = "latitude")
    private final double latitude;
    @ColumnInfo(name = "longitude")
    private final double longitude;
    @ColumnInfo(name = "tel")
    private final String tel;

    public Merchant(int id, int change_num, String avatar_url, String near, String name,
                    String intro, String address, double latitude, double longitude, String tel) {
        this.id = id;
        this.change_num = change_num;
        this.avatar_url = avatar_url;
        this.near = near;
        this.name = name;
        this.intro = intro;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tel = tel;
    }

    public int getId() {
        return id;
    }

    public int getChange_num() {
        return change_num;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public String getNear() {
        return near;
    }

    public String getName() {
        return name;
    }

    public String getIntro() {
        return intro;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTel() {
        return tel;
    }
}
