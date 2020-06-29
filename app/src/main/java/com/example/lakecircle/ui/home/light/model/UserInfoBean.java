package com.example.lakecircle.ui.home.light.model;

public class UserInfoBean {

    /**
     * code : 0
     * message : OK
     * data : {"avatar":"https://lake-circle.oss-cn-shenzhen.aliyuncs.com/1-1593371506.jpg","username":"bowser","kind":0,"history_mileage":0,"using_mileage":0,"history_intergral":0,"using_intergral":100,"star_num":10,"rank":1}
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
         * avatar : https://lake-circle.oss-cn-shenzhen.aliyuncs.com/1-1593371506.jpg
         * username : bowser
         * kind : 0
         * history_mileage : 0
         * using_mileage : 0
         * history_intergral : 0
         * using_intergral : 100
         * star_num : 10
         * rank : 1
         */

        private String avatar;
        private String username;
        private int kind;
        private int history_mileage;
        private int using_mileage;
        private int history_intergral;
        private int using_intergral;
        private int star_num;
        private int rank;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getKind() {
            return kind;
        }

        public void setKind(int kind) {
            this.kind = kind;
        }

        public int getHistory_mileage() {
            return history_mileage;
        }

        public void setHistory_mileage(int history_mileage) {
            this.history_mileage = history_mileage;
        }

        public int getUsing_mileage() {
            return using_mileage;
        }

        public void setUsing_mileage(int using_mileage) {
            this.using_mileage = using_mileage;
        }

        public int getHistory_intergral() {
            return history_intergral;
        }

        public void setHistory_intergral(int history_intergral) {
            this.history_intergral = history_intergral;
        }

        public int getUsing_intergral() {
            return using_intergral;
        }

        public void setUsing_intergral(int using_intergral) {
            this.using_intergral = using_intergral;
        }

        public int getStar_num() {
            return star_num;
        }

        public void setStar_num(int star_num) {
            this.star_num = star_num;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }
    }
}
