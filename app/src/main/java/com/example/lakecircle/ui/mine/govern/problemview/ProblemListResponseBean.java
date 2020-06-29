package com.example.lakecircle.ui.mine.govern.problemview;

import java.util.List;

public class ProblemListResponseBean {

    /**
     * data : [{"id":0,"is_solved":true,"location":"string","picture_url":["string"]}]
     * page : 0
     * sum : 0
     */

    private int page;
    private int sum;
    private List<DataBean> data;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 0
         * is_solved : true
         * location : string
         * picture_url : ["string"]
         */

        private int id;
        private boolean is_solved;
        private String location;
        private List<String> picture_url;

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
}
