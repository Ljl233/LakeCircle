package com.example.lakecircle.ui.home.light.model;

import java.util.ArrayList;
import java.util.List;

public class LakeRankBean {

    /**
     * name : string
     * star_num : 0
     */

    private String name;
    private int star_num;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStar_num() {
        return star_num;
    }

    public void setStar_num(int star_num) {
        this.star_num = star_num;
    }


    public static List<LakeRankBean> getDefaultLakeRanks() {
        List<LakeRankBean> lakeRankBeanList = new ArrayList<>();
        LakeRankBean b1 = new LakeRankBean();
        b1.setName("东湖");
        b1.setStar_num(20);
        lakeRankBeanList.add(b1);
        LakeRankBean b2 = new LakeRankBean();
        b2.setName("沙湖");
        b2.setStar_num(18);
        lakeRankBeanList.add(b2);
        return lakeRankBeanList;
    }
}
