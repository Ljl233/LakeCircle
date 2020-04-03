package com.example.lakecircle.ui.home.light;

import java.util.ArrayList;
import java.util.List;

public class LakeRankBean {
    private int num;
    private String lakeName;
    private int lightedTimes;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getLakeName() {
        return lakeName;
    }

    public void setLakeName(String lakeName) {
        this.lakeName = lakeName;
    }

    public int getLightedTimes() {
        return lightedTimes;
    }

    public void setLightedTimes(int lightedTimes) {
        this.lightedTimes = lightedTimes;
    }

    static List<LakeRankBean> getDefaultLakeRanks() {
        List<LakeRankBean> lakeRankBeanList = new ArrayList<>();
        LakeRankBean b1 = new LakeRankBean();
        b1.setNum(1);
        b1.setLakeName("东湖");
        b1.setLightedTimes(20);
        lakeRankBeanList.add(b1);
        LakeRankBean b2 = new LakeRankBean();
        b2.setNum(2);
        b2.setLakeName("沙湖");
        b2.setLightedTimes(18);
        lakeRankBeanList.add(b2);
        return lakeRankBeanList;
    }
}
