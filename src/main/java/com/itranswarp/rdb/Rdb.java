package com.itranswarp.rdb;

import java.util.Arrays;

public class Rdb {

    public Select select(String... fields) {
        SelectInfo selectInfo = new SelectInfo(this);
        selectInfo.fields = Arrays.asList(fields);
        return new Select(selectInfo);
    }

    public Insert insert(String table) {
        InsertInfo insertInfo = new InsertInfo(this);
        insertInfo.table = table;
        return new Insert(insertInfo);
    }

    public InsertT<Object> insert(Object... objects) {
        InsertInfo insertInfo = new InsertInfo(this);
        insertInfo.beans = objects;
        return new InsertT<Object>(insertInfo);
    }

    public Update update(String table) {
        UpdateInfo updateInfo = new UpdateInfo(this);
        updateInfo.table = table;
        return new Update(updateInfo);
    }

    public <T> UpdateT<T> update(T object) {
        UpdateInfo updateInfo = new UpdateInfo(this);
        updateInfo.table = object.getClass().getSimpleName();
        updateInfo.bean = object;
        return new UpdateT<T>(updateInfo);
    }

    public Delete delete(String table) {
        DeleteInfo deleteInfo = new DeleteInfo(this);
        deleteInfo.table = table;
        return new Delete(deleteInfo);
    }

    public <T> DeleteT<T> delete(T object) {
        DeleteInfo deleteInfo = new DeleteInfo(this);
        deleteInfo.table = object.getClass().getSimpleName();
        deleteInfo.bean = object;
        return new DeleteT<T>(deleteInfo);
    }
}
