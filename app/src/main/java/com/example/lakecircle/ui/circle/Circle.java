package com.example.lakecircle.ui.circle;

public class Circle {

    /**
     * content	string 文字
     * created_at	string  时间
     * picture_url	string 图片url
     * sub_id	 integer 发布id
     *
     * content : string
     * created_at : string
     * picture_url : string
     * sub_id : 0
     */

    private String content;
    private String created_at;
    private String picture_url;
    private int sub_id;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public int getSub_id() {
        return sub_id;
    }

    public void setSub_id(int sub_id) {
        this.sub_id = sub_id;
    }
}
