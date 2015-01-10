package com.itranswarp.rdb;

import java.util.List;

class UpdateInfo {

    final Rdb rdb;
    Class<?> beanClass = null;
    BeanMapper beanMapper = null;
    Object bean = null;
    String[] fieldsOnly = null;
    String table = null;
    List<UpdateSetInfo> sets = null;
    String whereClause;
    Object[] whereArgs;

    UpdateInfo(Rdb rdb) {
        this.rdb = rdb;
    }

}
