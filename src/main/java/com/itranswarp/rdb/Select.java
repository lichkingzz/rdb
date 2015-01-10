package com.itranswarp.rdb;

import java.util.Arrays;

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
