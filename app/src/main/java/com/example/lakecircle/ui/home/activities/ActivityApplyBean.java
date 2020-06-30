package com.example.lakecircle.ui.home.activities;

public class ActivityApplyBean {

    /**
     * age : 0
     * gender : string
     * name : string
     * qq : string
     * tel : string
     */

    private int age;
    private String gender;
    private String name;
    private String qq;
    private String tel;

    public ActivityApplyBean(int age, String gender, String name, String qq, String tel) {
        this.age = age;
        this.gender = gender;
        this.name = name;
        this.qq = qq;
        this.tel = tel;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
