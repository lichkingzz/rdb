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

    EntityListener entityListener = null;
    DataSourceManager dataSourceManager = null;
    Map<String, TypeAdapter<?>> typeAdapters = new HashMap<String, TypeAdapter<?>>();

    /**
     * Construct a new Rdb object.
     */
    public Rdb() {
    }

    /**
     * Construct a new Rdb object with a DataSourceManager.
     * 
     * @param dataSourceManager The DataSourceManager object.
     */
    public Rdb(DataSourceManager dataSourceManager) {
        this.dataSourceManager = dataSourceManager;
    }

    /**
     * Get DataSourceManager of rdb.
     * 
     * @return The DataSourceManager object.
     */
    public DataSourceManager getDataSourceManager() {
        return this.dataSourceManager;
    }

    /**
     * Set DataSourceManager for rdb object.
     * 
     * @param dataSourceManager The DataSourceManager object.
     */
    public void setDataSourceManager(DataSourceManager dataSourceManager) {
        this.dataSourceManager = dataSourceManager;
    }

    /**
     * Set an EntityListener object for rdb object.
     * 
     * @param entityListener The EntityListener object.
     */
    public void setEntityListener(EntityListener entityListener) {
        this.entityListener = entityListener;
    }

    void beforeInsert(Object bean) {
        if (entityListener!=null) {
            entityListener.beforeInsert(bean);
        }
    }

    void beforeUpdate(Object bean) {
        if (entityListener!=null) {
            entityListener.beforeUpdate(bean);
        }
    }

    void beforeDelete(Object bean) {
        if (entityListener!=null) {
            entityListener.beforeDelete(bean);
        }
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
     * @param <T> The generic type.
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
     * @param <T> The generic type.
     * @param beanClass The JavaBean class.
     * @return InsertT object for next operation.
     */
    public <T> InsertT<T> insert(Class<T> beanClass) {
        return new InsertT<T>(new InsertInfo(this), beanClass);
    }

    /**
     * Start an UPDATE SQL.
     * 
     * @param table The table name.
     * @return Update object for next operation.
     */
    public Update update(String table) {
        return new Update(new UpdateInfo(this), table);
    }

    /**
     * Start an update of JavaBean.
     * 
     * @param <T> The generic type.
     * @param beanClass The JavaBean class.
     * @return Update object for next operation.
     */
    public <T> UpdateT<T> update(Class<T> beanClass) {
        return new UpdateT<T>(new UpdateInfo(this), beanClass);
    }

    /**
     * Start a DELETE SQL.
     * 
     * @param table The table name.
     * @return Delete object for next operation.
     */
    public Delete delete(String table) {
        return new Delete(new DeleteInfo(this), table);
    }

    /**
     * Start a delete of JavaBean.
     * 
     * @param <T> The generic type.
     * @param beanClass The JavaBean class.
     * @return Delete object for next operation.
     */
    public <T> DeleteT<T> delete(Class<T> beanClass) {
        return new DeleteT<T>(new DeleteInfo(this), beanClass);
    }

}
