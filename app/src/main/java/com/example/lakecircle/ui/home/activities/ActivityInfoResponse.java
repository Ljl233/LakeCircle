package com.example.lakecircle.ui.home.activities;

public class ActivityInfoResponse {

    /**
     * code : 200
     * message : OK
     * data : {"detail":"string","duration":"string","finished":true,"id":0,"kind":"string","location":"string","name":"string","pictureURL":"string","reward":0,"subUID":0,"userNum":0}
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
         * detail : string
         * duration : string
         * finished : true
         * id : 0
         * kind : string
         * location : string
         * name : string
         * pictureURL : string
         * reward : 0
         * subUID : 0
         * userNum : 0
         */

        private String detail;
        private String duration;
        private boolean finished;
        private int id;
        private String kind;
        private String location;
        private String name;
        private String pictureURL;
        private int reward;
        private int subUID;
        private int userNum;

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public boolean isFinished() {
            return finished;
        }

        public void setFinished(boolean finished) {
            this.finished = finished;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPictureURL() {
            return pictureURL;
        }

        public void setPictureURL(String pictureURL) {
            this.pictureURL = pictureURL;
        }

        public int getReward() {
            return reward;
        }

        public void setReward(int reward) {
            this.reward = reward;
        }

        public int getSubUID() {
            return subUID;
        }

        public void setSubUID(int subUID) {
            this.subUID = subUID;
        }

        public int getUserNum() {
            return userNum;
        }

        public void setUserNum(int userNum) {
            this.userNum = userNum;
        }
    }
}
