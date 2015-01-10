package com.itranswarp.rdb;

class DeleteInfo {

    final Rdb rdb;
    String table = null;
    BeanMapper beanMapper = null;
    String primaryKey = null;
    Class<?> beanClass = null;
    Object[] beans = null;
    String whereClause = null;
    Object[] whereArgs = null;

    DeleteInfo(Rdb rdb) {
        this.rdb = rdb;
    }

}
