package com.example.lakecircle.ui.home.realtime;

public class RealtimeResponse {

    /**
     * code : 0
     * message : OK
     * data : {"name":"秦淮河","introduction":"秦淮河，中国长江下游右岸支流。秦淮河大部分在南京市境内，是南京市最大的地区性河流，历史上极富盛名，被称为\u201c中国第一历史文化名河\u201d。","sight_url":"https://lake-circle.oss-cn-shenzhen.aliyuncs.com/lake-1.jpg"}
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
         * name : 秦淮河
         * introduction : 秦淮河，中国长江下游右岸支流。秦淮河大部分在南京市境内，是南京市最大的地区性河流，历史上极富盛名，被称为“中国第一历史文化名河”。
         * sight_url : https://lake-circle.oss-cn-shenzhen.aliyuncs.com/lake-1.jpg
         */

        private String name;
        private String introduction;
        private String sight_url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getSight_url() {
            return sight_url;
        }

        public void setSight_url(String sight_url) {
            this.sight_url = sight_url;
        }
    }
}
