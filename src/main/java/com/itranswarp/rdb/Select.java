package com.itranswarp.rdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Select {

    SelectInfo selectInfo;

    public Select(SelectInfo selectInfo) {
        this.selectInfo = selectInfo;
    }

    public From from(String table) {
        return new From(this.selectInfo, table);
    }

    public <T> FromT<T> from(Class<T> beanClass) {
        return new FromT<T>(this.selectInfo, beanClass);
    }
}

class SelectInfo {

    final Rdb rdb;
    List<String> fields = null;
    String[] excludeFields;
    String table = null;
    Class<?> beanClass = null;
    BeanMapper beanMapper = null;
    String whereClause = null;
    Object[] whereArgs = null;
    OrderByInfo[] orderBys = null;
    Object[] limit = null;

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

    From(SelectInfo selectInfo, String table) {
        if (table == null) {
            throw new NullPointerException("table");
        }
        if (table.trim().isEmpty()) {
            throw new IllegalArgumentException("table cannot be blank.");
        }
        selectInfo.table = table.trim();
        this.selectInfo = selectInfo;
    }

    public SelectWhere where(String clause, Object... args) {
        return new SelectWhere(this.selectInfo, clause, args);
    }

    public OrderBy orderBy(String field) {
        return new OrderBy(this.selectInfo, field);
    }

    public Limit limit(int offset, int maxResults) {
        return new Limit(this.selectInfo, offset, maxResults);
    }

    public Limit limit(int maxResults) {
        return new Limit(this.selectInfo, 0, maxResults);
    }

    public String dryRun() {
        return new SelectRunner(this.selectInfo).dryRun();
    }

    public String dryRun(boolean includeParams) {
        return new SelectRunner(this.selectInfo).dryRun(includeParams);
    }

    public List<Map<String, Object>> list() {
        return new SelectRunner(this.selectInfo).list();
    }

    public Map<String, Object> first() {
        return new SelectRunner(this.selectInfo).first();
    }

    public Map<String, Object> unique() {
        return new SelectRunner(this.selectInfo).unique();
    }

}

class FromT<T> {

    SelectInfo selectInfo;

    FromT(SelectInfo selectInfo, Class<?> beanClass) {
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

    public LimitT<T> limit(int maxResults) {
        return new LimitT<T>(this.selectInfo, 0, maxResults);
    }

    public LimitT<T> limit(int offset, int maxResults) {
        return new LimitT<T>(this.selectInfo, offset, maxResults);
    }

    public OrderByT<T> orderBy(String field) {
        return new OrderByT<T>(this.selectInfo, field);
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

class SelectWhere {

    SelectInfo selectInfo;

    SelectWhere(SelectInfo selectInfo, String clause, Object[] args) {
        Utils.validateClause(clause, args);
        selectInfo.whereClause = clause.trim();
        selectInfo.whereArgs = args;
        this.selectInfo = selectInfo;
    }

    public OrderBy orderBy(String field) {
        return new OrderBy(this.selectInfo, field);
    }

    public Limit limit(int offset, int maxResults) {
        return new Limit(this.selectInfo, offset, maxResults);
    }

    public Limit limit(int maxResults) {
        return new Limit(this.selectInfo, 0, maxResults);
    }

    public String dryRun() {
        return new SelectRunner(this.selectInfo).dryRun();
    }

    public String dryRun(boolean includeParams) {
        return new SelectRunner(this.selectInfo).dryRun(includeParams);
    }
}

class SelectWhereT<T> {

    SelectInfo selectInfo;

    SelectWhereT(SelectInfo selectInfo, String clause, Object[] args) {
        Utils.validateClause(clause, args);
        selectInfo.whereClause = clause.trim();
        selectInfo.whereArgs = args;
        this.selectInfo = selectInfo;
    }

    public OrderByT<T> orderBy(String field) {
        return new OrderByT<T>(this.selectInfo, field);
    }

    public LimitT<T> limit(int offset, int maxResults) {
        return new LimitT<T>(this.selectInfo, offset, maxResults);
    }

    public LimitT<T> limit(int maxResults) {
        return new LimitT<T>(this.selectInfo, 0, maxResults);
    }

    public T first() {
        return new SelectRunnerT<T>(this.selectInfo).first();
    }

    public T unique() {
        return new SelectRunnerT<T>(this.selectInfo).unique();
    }

    public String dryRun() {
        return new SelectRunner(this.selectInfo).dryRun();
    }

    public String dryRun(boolean includeParams) {
        return new SelectRunner(this.selectInfo).dryRun(includeParams);
    }
}

class OrderBy extends OrderByT<Map<String, Object>> {

    public OrderBy(SelectInfo selectInfo, String field) {
        super(selectInfo, field);
    }

    public Limit limit(int offset, int maxResults) {
        return new Limit(this.selectInfo, offset, maxResults);
    }

    public Limit limit(int maxResults) {
        return new Limit(this.selectInfo, 0, maxResults);
    }

}

class OrderByT<T> {

    SelectInfo selectInfo;

    public OrderByT(SelectInfo selectInfo, String field) {
        if (selectInfo.orderBys == null) {
            // first orderBy():
            selectInfo.orderBys = new OrderByInfo[] { new OrderByInfo(field, false) };
        }
        else {
            OrderByInfo[] newOrderBys = new OrderByInfo[selectInfo.orderBys.length + 1];
            System.arraycopy(selectInfo.orderBys, 0, newOrderBys, 0, selectInfo.orderBys.length);
            newOrderBys[newOrderBys.length - 1] = new OrderByInfo(field, false);
            selectInfo.orderBys = newOrderBys;
        }
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
        return new OrderByT<T>(this.selectInfo, field);
    }

    public LimitT<T> limit(int offset, int maxResults) {
        return new LimitT<T>(this.selectInfo, offset, maxResults);
    }

    public LimitT<T> limit(int maxResults) {
        return limit(0, maxResults);
    }

    public List<T> list() {
        return new SelectRunnerT<T>(this.selectInfo).list();
    }

    public T first() {
        return new SelectRunnerT<T>(this.selectInfo).first();
    }

    public String dryRun() {
        return new SelectRunner(this.selectInfo).dryRun();
    }

    public String dryRun(boolean includeParams) {
        return new SelectRunner(this.selectInfo).dryRun(includeParams);
    }
}

class LimitT<T> {

    final SelectInfo selectInfo;

    public LimitT(SelectInfo selectInfo, int offset, int maxResults) {
        if (offset < 0) {
            throw new IllegalArgumentException("Invalid offset: must be equal or greater than 0.");
        }
        if (maxResults < 1) {
            throw new IllegalArgumentException("Invalid offset: must be greater than 0.");
        }
        selectInfo.limit = new Object[] { offset, maxResults };
        this.selectInfo = selectInfo;
    }

    public List<T> list() {
        new SelectRunner(this.selectInfo).list();
        return new ArrayList<T>();
    }

    public T unique() {
        return new SelectRunner(this.selectInfo).unique();
    }

    public T first() {
        return new SelectRunner(this.selectInfo).first();
    }

    public String dryRun() {
        return new SelectRunner(this.selectInfo).dryRun();
    }

    public String dryRun(boolean includeParams) {
        return new SelectRunner(this.selectInfo).dryRun(includeParams);
    }
}

class Limit extends LimitT<Map<String, Object>> {

    public Limit(SelectInfo selectInfo, int offset, int maxResults) {
        super(selectInfo, offset, maxResults);
    }

}

class SelectRunnerT<T> {

    SelectInfo selectInfo;

    SelectRunnerT(SelectInfo selectInfo) {
        this.selectInfo = selectInfo;
    }

    public String dryRun() {
        return new SelectRunner(this.selectInfo).dryRun();
    }

    public String dryRun(boolean includeParams) {
        return new SelectRunner(this.selectInfo).dryRun(includeParams);
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
}
