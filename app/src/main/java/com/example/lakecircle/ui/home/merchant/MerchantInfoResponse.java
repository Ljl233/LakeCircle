package com.example.lakecircle.ui.home.merchant;

public class MerchantInfoResponse {

    /**
     * code : 0
     * message : OK
     * data : {"address":"","near":"","tel":"","intro":"","avatar_url":""}
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
         * address :
         * near :
         * tel :
         * intro :
         * avatar_url :
         */

        private String address;
        private String near;
        private String tel;
        private String intro;
        private String avatar_url;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getNear() {
            return near;
        }

        public void setNear(String near) {
            this.near = near;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
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
    }
}
