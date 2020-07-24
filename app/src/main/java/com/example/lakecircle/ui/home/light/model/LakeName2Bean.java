package com.example.lakecircle.ui.home.light.model;

import androidx.annotation.Nullable;

public class LakeName2Bean {

    /**
     * lake_name : 鄱阳湖
     */

    private String lake_name;

    public String getLake_name() {
        return lake_name;
    }

    public void setLake_name(String lake_name) {
        this.lake_name = lake_name;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof LakeName2Bean)
            return lake_name.equals(((LakeName2Bean) obj).getLake_name());
        return false;
    }
}