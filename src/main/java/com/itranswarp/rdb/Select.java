package com.itranswarp.rdb;

import java.util.ArrayList;
import java.util.List;

class Select {

    SelectInfo selectInfo;

    public Select(SelectInfo selectInfo) {
        this.selectInfo = selectInfo;
    }

    public From from(String table) {
        this.selectInfo.table = table;
        return new From(this.selectInfo);
    }

    public <T> FromT<T> from(Class<T> clazz) {
        this.selectInfo.table = clazz.getSimpleName();
        this.selectInfo.clazz = clazz;
        return new FromT<T>(this.selectInfo);
    }
}

class SelectInfo {

    final Rdb rdb;
    List<String> fields = null;
    String[] excludeFields;
    String table = null;
    Class<?> clazz = null;
    String whereClause = null;
    Object[] whereArgs = null;
    OrderByInfo[] orderBys = null;
    int[] limit = null;

    public SelectInfo(Rdb rdb) {
        this.rdb = rdb;
    }

}

class OrderByInfo {

    String field;
    boolean desc;

    public OrderByInfo(String field, boolean desc) {
        this.field = field;
        this.desc = false;
    }

}

class From {

    SelectInfo selectInfo;

    From(SelectInfo selectInfo) {
        this.selectInfo = selectInfo;
    }

    public SelectWhere where(String clause, Object... args) {
        this.selectInfo.whereClause = clause;
        this.selectInfo.whereArgs = args;
        return new SelectWhere(this.selectInfo);
    }
}

class FromT<T> {

    SelectInfo selectInfo;

    FromT(SelectInfo selectInfo) {
        this.selectInfo = selectInfo;
    }

    public SelectWhereT<T> where(String clause, Object... args) {
        this.selectInfo.whereClause = clause;
        this.selectInfo.whereArgs = args;
        return new SelectWhereT<T>(this.selectInfo);
    }

    public FromT<T> excludeFields(String... fields) {
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

    public T byId(Object id) {
        this.selectInfo.whereClause = ":id=?";
        this.selectInfo.whereArgs = new Object[] { id };
        return new SelectRunT<T>(this.selectInfo).first();
    }

    public T first() {
        return new SelectRunT<T>(this.selectInfo).first();
    }
}

class SelectWhere {

    SelectInfo selectInfo;

    SelectWhere(SelectInfo selectInfo) {
        this.selectInfo = selectInfo;
    }

    public OrderBy orderBy(String field) {
        this.selectInfo.orderBys = new OrderByInfo[] { new OrderByInfo(field, false) };
        return new OrderBy(this.selectInfo);
    }
}

class SelectWhereT<T> {

    SelectInfo selectInfo;

    SelectWhereT(SelectInfo selectInfo) {
        this.selectInfo = selectInfo;
    }

    public OrderByT<T> orderBy(String field) {
        this.selectInfo.orderBys = new OrderByInfo[] { new OrderByInfo(field, false) };
        return new OrderByT<T>(this.selectInfo);
    }

    public T first() {
        return new SelectRunT<T>(this.selectInfo).first();
    }

    public T one() {
        List<T> list = new SelectRunT<T>(this.selectInfo).list();
        return list.get(0);
    }
}

class OrderBy {

    SelectInfo selectInfo;

    public OrderBy(SelectInfo selectInfo) {
        this.selectInfo = selectInfo;
    }

    public OrderBy asc() {
        this.selectInfo.orderBys[this.selectInfo.orderBys.length-1].desc = false;
        return this;
    }

    public OrderBy desc() {
        this.selectInfo.orderBys[this.selectInfo.orderBys.length-1].desc = true;
        return this;
    }

    public OrderBy orderBy(String field) {
        OrderByInfo[] newOrderBys = new OrderByInfo[this.selectInfo.orderBys.length + 1];
        System.arraycopy(this.selectInfo.orderBys, 0, newOrderBys, 0, this.selectInfo.orderBys.length);
        newOrderBys[newOrderBys.length - 1] = new OrderByInfo(field, false);
        this.selectInfo.orderBys = newOrderBys;
        return new OrderBy(this.selectInfo);
    }

    public Limit limit(int offset, int maxResults) {
        this.selectInfo.limit = new int[] { offset, maxResults };
        return new Limit(this.selectInfo);
    }

    public Limit limit(int maxResults) {
        this.selectInfo.limit = new int[] { 0, maxResults };
        return new Limit(this.selectInfo);
    }
}

class OrderByT<T> {

    SelectInfo selectInfo;

    public OrderByT(SelectInfo selectInfo) {
        this.selectInfo = selectInfo;
    }

    public OrderByT<T> asc() {
        this.selectInfo.orderBys[this.selectInfo.orderBys.length-1].desc = false;
        return this;
    }

    public OrderByT<T> desc() {
        this.selectInfo.orderBys[this.selectInfo.orderBys.length-1].desc = true;
        return this;
    }

    public OrderByT<T> orderBy(String field) {
        OrderByInfo[] newOrderBys = new OrderByInfo[this.selectInfo.orderBys.length + 1];
        System.arraycopy(this.selectInfo.orderBys, 0, newOrderBys, 0, this.selectInfo.orderBys.length);
        newOrderBys[newOrderBys.length - 1] = new OrderByInfo(field, false);
        this.selectInfo.orderBys = newOrderBys;
        return new OrderByT<T>(this.selectInfo);
    }

    public LimitT<T> limit(int offset, int maxResults) {
        this.selectInfo.limit = new int[] { offset, maxResults };
        return new LimitT<T>(this.selectInfo);
    }

    public LimitT<T> limit(int maxResults) {
        return limit(0, maxResults);
    }

    public List<T> list() {
        return new SelectRunT<T>(this.selectInfo).list();
    }

    public T first() {
        return new SelectRunT<T>(this.selectInfo).first();
    }

}

class Limit {

    SelectInfo selectInfo;

    public Limit(SelectInfo selectInfo) {
        this.selectInfo = selectInfo;
    }

    public List<?> list() {
        return null;
    }
}

class LimitT<T> {

    SelectInfo selectInfo;

    public LimitT(SelectInfo selectInfo) {
        this.selectInfo = selectInfo;
    }

    public List<T> list() {
        new SelectRun(this.selectInfo).list();
        return new ArrayList<T>();
    }
}

class SelectRun {

    SelectInfo selectInfo;

    SelectRun(SelectInfo selectInfo) {
        this.selectInfo = selectInfo;
    }

    public SelectRun dryRun() {
        return this;
    }

    public int list() {
        return 0;
    }
}

class SelectRunT<T> {

    SelectInfo selectInfo;

    SelectRunT(SelectInfo selectInfo) {
        this.selectInfo = selectInfo;
    }

    public SelectRunT<T> dryRun() {
        return this;
    }

    public List<T> list() {
        return new ArrayList<T>();
    }

    public T first() {
        return null;
    }
}
