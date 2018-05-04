package com.ordertrack.constant;

/* Created by AMXPC on 2018/5/4. */

public enum PackageType {
    PACK("袋", 1), BOX("纸箱", 2), TRAY("托盘", 3), BUCKET("桶", 4), OTHER("其他", 5);

    private String name;
    private int index;

    PackageType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (PackageType p : PackageType.values()) {
            if (p.getIndex() == index) {
                return p.name;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
