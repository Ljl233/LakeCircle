package com.example.lakecircle.ui.mine.govern.problemview;

import java.util.List;

public class ProblemResponse {

    /**
     * code : 0
     * message : OK
     * data : {"location":" ","content":"","pictures":[""],"is_solved":false,"solved_pictures":[""],"reply":""}
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
         * location :
         * content :
         * pictures : [""]
         * is_solved : false
         * solved_pictures : [""]
         * reply :
         */

        private String location;
        private String content;
        private boolean is_solved;
        private String reply;
        private List<String> pictures;
        private List<String> solved_pictures;

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public boolean isIs_solved() {
            return is_solved;
        }

        public void setIs_solved(boolean is_solved) {
            this.is_solved = is_solved;
        }

        public String getReply() {
            return reply;
        }

        public void setReply(String reply) {
            this.reply = reply;
        }

        public List<String> getPictures() {
            return pictures;
        }

        public void setPictures(List<String> pictures) {
            this.pictures = pictures;
        }

        public List<String> getSolved_pictures() {
            return solved_pictures;
        }

        public void setSolved_pictures(List<String> solved_pictures) {
            this.solved_pictures = solved_pictures;
        }
    }
}
