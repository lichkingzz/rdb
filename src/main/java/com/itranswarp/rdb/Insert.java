package com.itranswarp.rdb;

import java.util.Map;

public class Insert {

    final InsertInfo insertInfo;

    Insert(InsertInfo insertInfo, String table) {
        insertInfo.table = table;
        this.insertInfo = insertInfo;
    }

    public InsertSet set(String field, Object value) {
        return new InsertSet(this.insertInfo, field, value);
    }

    public InsertSets sets(Map<String, Object> values) {
        return new InsertSets(this.insertInfo, values);
    }
}
