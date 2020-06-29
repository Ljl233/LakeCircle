package com.example.lakecircle.ui.mine.govern.problemview;

import java.util.List;

public class Problem {

    private int id;
    private boolean is_solved;
    private String location;
    private List<String> picture_url;

    private String content;
    private String reply;
    private List<String> solved_pictures;

    /**
     * 获取问题列表的构造器
     */
    public Problem(int id, boolean is_solved, String location, List<String> picture_url) {
        this.id = id;
        this.is_solved = is_solved;
        this.location = location;
        this.picture_url = picture_url;
    }

    /**
     * 获取问题详情的构造器
     */
    public Problem(String content, boolean is_solved, String location, List<String> picture_url, String reply, List<String> pictures, List<String> solved_pictures) {
        this.location = location;
        this.is_solved = is_solved;
        this.picture_url = picture_url;
        this.content = content;
        this.reply = reply;
        this.picture_url = pictures;
        this.solved_pictures = solved_pictures;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public List<String> getSolved_pictures() {
        return solved_pictures;
    }

    public void setSolved_pictures(List<String> solved_pictures) {
        this.solved_pictures = solved_pictures;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIs_solved() {
        return is_solved;
    }

    public void setIs_solved(boolean is_solved) {
        this.is_solved = is_solved;
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
