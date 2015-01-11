package com.itranswarp.rdb;

public interface EntityListener {

    void beforeInsert(Object bean);

    void beforeUpdate(Object bean);

    void beforeDelete(Object bean);

}
