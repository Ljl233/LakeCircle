package com.example.lakecircle.ui.circle;

public class PostCircleBean {

    /**
     * content : string
     * picture_url : string
     */

    private String content;
    private String picture_url;

    public PostCircleBean(String content, String picture_url) {
        this.content = content;
        this.picture_url = picture_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }
}
