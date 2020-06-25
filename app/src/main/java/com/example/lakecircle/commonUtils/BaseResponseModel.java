package com.example.lakecircle.commonUtils;

import java.util.List;

public class BaseResponseModel<T> {

    /**
     * code : 0
     * message : OK
     * data : [{"username":"bowser","avatar":"","star_num":10},{"username":"bowser1704","avatar":"","star_num":8}]
     */

    private int code;
    private String message;
    private List<T> data;

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

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }


}
