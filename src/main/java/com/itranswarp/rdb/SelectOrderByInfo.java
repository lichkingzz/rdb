package com.itranswarp.rdb;

class SelectOrderByInfo {

    final String field;
    boolean desc;

    SelectOrderByInfo(String field, boolean desc) {
        this.field = field;
        this.desc = false;
    }

}
