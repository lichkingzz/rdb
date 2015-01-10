package com.itranswarp.rdb;

import java.util.List;

class InsertInfo {

    final Rdb rdb;
    String table = null;
    Class<?> beanClass = null;
    Object[] beans = null;
    List<InsertFieldInfo> sets = null;

    InsertInfo(Rdb rdb) {
        this.rdb = rdb;
    }
}

class InsertFieldInfo {

    String field;
    Object value;

    InsertFieldInfo(String field, Object value) {
        this.field = field;
        this.value = value;
    }
}
