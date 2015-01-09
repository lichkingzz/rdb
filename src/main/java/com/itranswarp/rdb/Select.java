package com.itranswarp.rdb;

import java.util.Arrays;
import java.util.List;

public class Select {

    final SelectInfo selectInfo;

    Select(SelectInfo selectInfo, String[] fields) {
        selectInfo.fields = (fields.length == 0 || (fields.length==1 && fields[0].equals("*"))) ? null : Arrays.asList(fields);
        this.selectInfo = selectInfo;
    }

    public SelectFrom from(String table) {
        return new SelectFrom(this.selectInfo, table);
    }

    public <T> SelectFromT<T> from(Class<T> beanClass) {
        return new SelectFromT<T>(this.selectInfo, beanClass);
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

class SelectRunnerT<T> {

    final SelectInfo selectInfo;

    SelectRunnerT(SelectInfo selectInfo) {
        this.selectInfo = selectInfo;
    }

    public String dryRun() {
        return dryRun(false);
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
