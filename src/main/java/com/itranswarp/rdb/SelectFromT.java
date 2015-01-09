package com.itranswarp.rdb;

import java.util.List;

public class SelectFromT<T> {

    final SelectInfo selectInfo;

    SelectFromT(SelectInfo selectInfo, Class<?> beanClass) {
        if (beanClass == null) {
            throw new NullPointerException("beanClass");
        }
        selectInfo.beanMapper = Mappers.getMapper(beanClass);
        selectInfo.table = selectInfo.beanMapper.table;
        selectInfo.beanClass = beanClass;
        this.selectInfo = selectInfo;
    }

    public SelectWhereT<T> where(String clause, Object... args) {
        return new SelectWhereT<T>(this.selectInfo, clause, args);
    }

    public SelectFromT<T> excludeFields(String... fields) {
        if (fields.length == 0) {
            throw new IllegalArgumentException("Cannot set empty fields.");
        }
        if (this.selectInfo.fields != null && !this.selectInfo.fields.isEmpty()) {
            throw new IllegalArgumentException("Cannot set excluded fields since fields are set by select().");
        }
        if (this.selectInfo.excludeFields != null) {
            throw new IllegalArgumentException("excludeFields() can only be called once.");
        }
        this.selectInfo.excludeFields = fields;
        return this;
    }

    public SelectLimitT<T> limit(int maxResults) {
        return new SelectLimitT<T>(this.selectInfo, 0, maxResults);
    }

    public SelectLimitT<T> limit(int offset, int maxResults) {
        return new SelectLimitT<T>(this.selectInfo, offset, maxResults);
    }

    public SelectOrderByT<T> orderBy(String field) {
        return new SelectOrderByT<T>(this.selectInfo, field);
    }

    public T byId(Object idValue) {
        String idField = this.selectInfo.beanMapper.primaryKey;
        return new SelectWhereT<T>(this.selectInfo, idField + "=?", new Object[] { idValue }).unique();
    }

    public List<T> list() {
        return new SelectRunner(this.selectInfo).list();
    }

    public T unique() {
        return new SelectRunner(this.selectInfo).unique();
    }

    public T first() {
        return new SelectRunner(this.selectInfo).first();
    }

    public String dryRun() {
        return new SelectRunnerT<T>(this.selectInfo).dryRun();
    }
}
