package com.itranswarp.rdb;

class UpdateSetInfo {

    final String field;
    final Object value;

    public UpdateSetInfo(String field, Object value) {
        this.field = field;
        this.value = value;
    }
}
