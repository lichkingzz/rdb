package com.itranswarp.rdb;

import java.sql.Timestamp;

public class StandardEntityListener implements EntityListener {

    @Override
    public void beforeInsert(Object bean) {
        AbstractEntity entity = (AbstractEntity) bean;
        entity.createdAt = entity.updatedAt = new Timestamp(System.currentTimeMillis());
        entity.version = 1;
    }

    @Override
    public void beforeUpdate(Object bean) {
        AbstractEntity entity = (AbstractEntity) bean;
        entity.updatedAt = new Timestamp(System.currentTimeMillis());
        entity.version ++;
    }

    @Override
    public void beforeDelete(Object bean) {
    }

}
