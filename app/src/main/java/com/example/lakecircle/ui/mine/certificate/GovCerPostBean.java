package com.example.lakecircle.ui.mine.certificate;

public class GovCerPostBean {

    /**
     * lake_name : string
     * picture_url : string
     * real_name : string
     * tel : string
     */

    private String lake_name;
    private String picture_url;
    private String real_name;
    private String tel;

    public GovCerPostBean(String lake_name, String picture_url, String real_name, String tel) {
        this.lake_name = lake_name;
        this.picture_url = picture_url;
        this.real_name = real_name;
        this.tel = tel;
    }

    public String getLake_name() {
        return lake_name;
    }

    public void setLake_name(String lake_name) {
        this.lake_name = lake_name;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
