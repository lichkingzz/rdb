package com.itranswarp.rdb;

import java.util.List;

class SelectInfo {

    final Rdb rdb;
    List<String> fields = null;
    String[] excludeFields;
    String table = null;
    Class<?> beanClass = null;
    BeanMapper beanMapper = null;
    String whereClause = null;
    Object[] whereArgs = null;
    SelectOrderByInfo[] orderBys = null;
    Object[] limit = null;

    public SelectInfo(Rdb rdb) {
        this.rdb = rdb;
    }

}
