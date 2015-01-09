package com.itranswarp.rdb;

import java.util.HashMap;
import java.util.Map;

/**
 * Rdb object to drive DSL-style database operations. It is thread-safe and should be 
 * a global singleton object.
 * 
 * @author Michael Liao
 */
public class Rdb {

    DataSourceManager dataSourceManager = null;
    Map<String, TypeAdapter<?>> typeAdapters = new HashMap<String, TypeAdapter<?>>();

    public Rdb() {
    }

    public Rdb(DataSourceManager dataSourceManager) {
        this.dataSourceManager = dataSourceManager;
    }

    public DataSourceManager getDataSourceManager() {
        return this.dataSourceManager;
    }

    public void setDataSourceManager(DataSourceManager dataSourceManager) {
        this.dataSourceManager = dataSourceManager;
    }

    Object convertTo(Class<?> targetClass, Object originalValue) {
        TypeAdapter<?> t = typeAdapters.get(targetClass.getName());
        if (t == null) {
            throw new IllegalArgumentException("Cannot convert object " + originalValue + " with type \"" + originalValue.getClass().getName() + "\" to type: " + targetClass.getName());
        }
        return t.convert(originalValue);
    }

    public <T> void registerTypeAdapter(Class<T> clazz, TypeAdapter<T> typeAdapter) {
        typeAdapters.put(clazz.getName(), typeAdapter);
    }

    public Select select(String... fields) {
        return new Select(new SelectInfo(this), fields);
    }

    public Insert insert(String table) {
        return new Insert(new InsertInfo(this), table);
    }

    public InsertT insert(Object... objects) {
        return new InsertT(new InsertInfo(this), objects);
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
