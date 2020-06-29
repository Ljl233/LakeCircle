package com.example.lakecircle.ui.home.light.model;

public class LakeUrlResponse {

    /**
     * code : 0
     * message : OK
     * data : {"outline_before_url":"https://lake-circle.oss-cn-shenzhen.aliyuncs.com/lake-42-1.png","outline_after_url":"https://lake-circle.oss-cn-shenzhen.aliyuncs.com/lake-42-2.png","is_star":false}
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
         * outline_before_url : https://lake-circle.oss-cn-shenzhen.aliyuncs.com/lake-42-1.png
         * outline_after_url : https://lake-circle.oss-cn-shenzhen.aliyuncs.com/lake-42-2.png
         * is_star : false
         */

        private String outline_before_url;
        private String outline_after_url;
        private boolean is_star;

        public String getOutline_before_url() {
            return outline_before_url;
        }

        public void setOutline_before_url(String outline_before_url) {
            this.outline_before_url = outline_before_url;
        }

        public String getOutline_after_url() {
            return outline_after_url;
        }

        public void setOutline_after_url(String outline_after_url) {
            this.outline_after_url = outline_after_url;
        }

        public boolean isIs_star() {
            return is_star;
        }

        public void setIs_star(boolean is_star) {
            this.is_star = is_star;
        }
    }
}
