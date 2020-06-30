package com.example.lakecircle.ui.mine.govern.problemview;

import java.util.List;

public class ProblemListResponseBean {

    /**
     * code : 0
     * message : OK
     * data : {"sum":0,"page":1,"data":null}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        /**
         * data : [{"id":0,"is_solved":true,"location":"string","picture_url":["string"]}]
         * page : 0
         * sum : 0
         */

        private int page;
        private int sum;
        private List<Problem> data;

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

        public List<Problem> getData() {
            return data;
        }

        public void setData(List<Problem> data) {
            this.data = data;
        }

        public static class Problem {
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
}
