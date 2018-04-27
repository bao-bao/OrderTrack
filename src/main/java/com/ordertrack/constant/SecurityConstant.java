package com.ordertrack.constant;

/* Created by AMXPC on 2018/4/25. */

public enum SecurityConstant {
    CURRENT_USER_LOGIN("ms_user"),
    CURRENT_AUTHORITY("authority");

    private String label;

    SecurityConstant(String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public SecurityConstant setLabel(String label) {
        this.label = label;
        return this;
    }
}
