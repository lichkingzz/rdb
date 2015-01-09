package com.itranswarp.rdb;

public class InsertBatchT<T> {

    InsertInfo insertInfo;

    InsertBatchT(InsertInfo insertInfo, T[] beans) {
        if (beans.length == 0) {
            throw new IllegalArgumentException("objects is empty.");
        }
        insertInfo.beans = beans;
        this.insertInfo = insertInfo;
    }

    public void run() {
        new InsertRun(this.insertInfo).run();
    }
}
