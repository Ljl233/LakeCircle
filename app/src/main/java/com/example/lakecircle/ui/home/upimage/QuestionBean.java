package com.example.lakecircle.ui.home.upimage;

import java.util.List;

public class QuestionBean {

    /**
     * content : string
     * location : string
     * picture_url : ["string"]
     */

    private String content;
    private String location;
    private List<String> picture_url;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(List<String> picture_url) {
        this.picture_url = picture_url;
    }
}
