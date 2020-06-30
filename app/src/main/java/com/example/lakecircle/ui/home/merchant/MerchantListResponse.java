package com.example.lakecircle.ui.home.merchant;

import java.util.List;

public class MerchantListResponse {

    /**
     * code : 0
     * message : OK
     * data : {"sum":7,"page":1,"data":[{"id":0,"name":"东湖","intro":"","avatar_url":"","change_num":0},{"id":1,"name":"东湖","intro":"","avatar_url":"","change_num":9}]}
     */

    private int code;
    private String message;
    private DataBeanX data;

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

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * sum : 7
         * page : 1
         * data : [{"id":0,"name":"东湖","intro":"","avatar_url":"","change_num":0},{"id":1,"name":"东湖","intro":"","avatar_url":"","change_num":9}]
         */

        private int sum;
        private int page;
        private List<DataBean> data;

        public int getSum() {
            return sum;
        }

        public void setSum(int sum) {
            this.sum = sum;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
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
             * name : 东湖
             * intro :
             * avatar_url :
             * change_num : 0
             */

            private int id;
            private String name;
            private String intro;
            private String avatar_url;
            private int change_num;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getIntro() {
                return intro;
            }

            public void setIntro(String intro) {
                this.intro = intro;
            }

            public String getAvatar_url() {
                return avatar_url;
            }

            public void setAvatar_url(String avatar_url) {
                this.avatar_url = avatar_url;
            }

            public int getChange_num() {
                return change_num;
            }

            public void setChange_num(int change_num) {
                this.change_num = change_num;
            }
        }
    }
}
