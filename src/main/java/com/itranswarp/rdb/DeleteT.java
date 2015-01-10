package com.itranswarp.rdb;

public class DeleteT<T> {

    final DeleteInfo deleteInfo;

    DeleteT(DeleteInfo deleteInfo, Class<T> beanClass) {
        deleteInfo.beanClass = beanClass;
        deleteInfo.beanMapper = Mappers.getMapper(beanClass);
        deleteInfo.table = deleteInfo.beanMapper.table;
        deleteInfo.primaryKey = deleteInfo.beanMapper.primaryKey;
        this.deleteInfo = deleteInfo;
    }

    /**
     * Batch delete the beans.
     * 
     * @param beans JavaBean objects.
     */
    public void runBatch(@SuppressWarnings("unchecked") T... beans) {
        if (beans.length == 0) {
            throw new IllegalArgumentException("objects is empty.");
        }
        this.deleteInfo.beans = beans;
        new DeleteRunner(this.deleteInfo).run();
    }
}
