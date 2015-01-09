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

    Object jdbcTypeToJavaType(Class<?> targetClass, Object originalValue) {
        TypeAdapter<?> t = typeAdapters.get(targetClass.getName());
        if (t == null) {
            throw new IllegalArgumentException("Cannot convert object " + originalValue + " with type \"" + originalValue.getClass().getName() + "\" to type: " + targetClass.getName());
        }
        return t.convert(originalValue);
    }

    Object javaTypeToJdbcType(Class<?> targetClass, Object originalValue) {
        TypeAdapter<?> t = typeAdapters.get(targetClass.getName());
        if (t == null) {
            throw new IllegalArgumentException("Cannot convert object " + originalValue + " with type \"" + originalValue.getClass().getName() + "\" to type: " + targetClass.getName());
        }
        return t.toJdbcType(originalValue);
    }

    /**
     * Register a TypeAdapter that convert between JDBC type and JavaBean property type.
     * 
     * @param clazz Class of the type.
     * @param typeAdapter TypeAdapter instance.
     */
    public <T> void registerTypeAdapter(Class<T> clazz, TypeAdapter<T> typeAdapter) {
        typeAdapters.put(clazz.getName(), typeAdapter);
    }

    /**
     * Start a SELECT SQL.
     * 
     * @param fields The fields to select. "*" will be used if no fields passed.
     * @return Select object for next operation.
     */
    public Select select(String... fields) {
        return new Select(new SelectInfo(this), fields);
    }

    /**
     * Start an INSERT SQL.
     * 
     * @param table The table name.
     * @return Insert object for next operation.
     */
    public Insert insert(String table) {
        return new Insert(new InsertInfo(this), table);
    }

    /**
     * Batch insert for a group of objects.
     * 
     * @param beanClass The JavaBean class.
     * @return InsertT object for next operation.
     */
    public <T> InsertT<T> insert(Class<T> beanClass) {
        return new InsertT<T>(new InsertInfo(this), beanClass);
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
