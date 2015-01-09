package com.itranswarp.rdb;

import java.util.ArrayList;
import java.util.List;

public class Insert {

    final InsertInfo insertInfo;

    Insert(InsertInfo insertInfo, String table) {
        insertInfo.table = table;
        this.insertInfo = insertInfo;
    }

    public Insert set(String field, Object value) {
        if (this.insertInfo.sets == null) {
            this.insertInfo.sets = new ArrayList<InsertFieldInfo>();
        }
        this.insertInfo.sets.add(new InsertFieldInfo(field, value));
        return this;
    }

    public void run() {
        new InsertRun(this.insertInfo).run();
    }
}

class InsertT {

    InsertInfo insertInfo;

    InsertT(InsertInfo insertInfo, Object[] beans) {
        insertInfo.beans = beans;
        this.insertInfo = insertInfo;
    }

    public void run() {
        new InsertRun(this.insertInfo).run();
    }
}

class InsertInfo {

    final Rdb rdb;
    String table = null;
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

class InsertRun {

    InsertInfo insertInfo;

    InsertRun(InsertInfo insertInfo) {
        this.insertInfo = insertInfo;
    }

    public void run() {
        //TODO
    }
}
