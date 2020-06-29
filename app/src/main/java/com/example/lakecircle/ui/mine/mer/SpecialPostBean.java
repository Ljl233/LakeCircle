package com.example.lakecircle.ui.mine.mer;

public class SpecialPostBean {

    /**
     * intro : string
     * url : string
     */

    private String intro;
    private String url;

    public SpecialPostBean(String intro, String url) {
        this.intro = intro;
        this.url = url;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
