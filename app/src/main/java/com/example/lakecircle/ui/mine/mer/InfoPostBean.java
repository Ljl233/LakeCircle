package com.example.lakecircle.ui.mine.mer;

public class InfoPostBean {

    /**
     * address : string
     * avatar_url : string
     * intro : string
     * near : string
     * tel : string
     */

    private String address;
    private String avatar_url;
    private String intro;
    private String near;
    private String tel;

    public InfoPostBean(String address, String avatar_url, String intro, String near, String tel) {
        this.address = address;
        this.avatar_url = avatar_url;
        this.intro = intro;
        this.near = near;
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getNear() {
        return near;
    }

    public void setNear(String near) {
        this.near = near;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
