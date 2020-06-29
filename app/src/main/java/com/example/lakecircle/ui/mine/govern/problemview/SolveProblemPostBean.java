package com.example.lakecircle.ui.mine.govern.problemview;

import java.util.List;

public class SolveProblemPostBean {

    /**
     * content : string
     * picture_url : ["string"]
     * qid : 0
     */

    private String content;
    private int qid;
    private List<String> picture_url;

    public SolveProblemPostBean(String content, int qid, List<String> picture_url) {
        this.content = content;
        this.qid = qid;
        this.picture_url = picture_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public List<String> getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(List<String> picture_url) {
        this.picture_url = picture_url;
    }
}
