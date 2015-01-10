package com.itranswarp.rdb;

public class InsertT<T> {

    InsertInfo insertInfo;

    InsertT(InsertInfo insertInfo, Class<T> beanClass) {
        insertInfo.beanClass = beanClass;
        this.insertInfo = insertInfo;
    }

    public void runBatch(@SuppressWarnings("unchecked") T... beans) {
        new InsertBatchT<T>(this.insertInfo, beans).run();
    }
}
