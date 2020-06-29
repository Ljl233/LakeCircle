package com.example.lakecircle.ui.mine.certificate;

public class MerCerPostBean {

    /**
     * name : string
     * picture_url : string
     */

    private String name;
    private String picture_url;

    public MerCerPostBean(String name, String picture_url) {
        this.name = name;
        this.picture_url = picture_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }
}
