package com.itranswarp.rdb;

public class InsertT<T> {

    InsertInfo insertInfo;

    InsertT(InsertInfo insertInfo, Class<T> beanClass) {
        insertInfo.beanClass = beanClass;
        this.insertInfo = insertInfo;
    }

    public InsertBatchT<T> batch(@SuppressWarnings("unchecked") T... beans) {
        return new InsertBatchT<T>(this.insertInfo, beans);
    }
}
