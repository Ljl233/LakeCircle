package com.example.lakecircle.ui.home.upimage;

public class UrlResponse {


    /**
     * code : 0
     * message : OK
     * data : {"url":"https://lake-circle.oss-cn-shenzhen.aliyuncs.com/1-1593395920.jpg"}
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
         * url : https://lake-circle.oss-cn-shenzhen.aliyuncs.com/1-1593395920.jpg
         */

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
