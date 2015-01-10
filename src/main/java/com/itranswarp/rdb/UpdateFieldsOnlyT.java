package com.itranswarp.rdb;

public class UpdateFieldsOnlyT<T> {

    final UpdateInfo updateInfo;

    UpdateFieldsOnlyT(UpdateInfo updateInfo, String[] fields) {
        updateInfo.fieldsOnly = fields;
        this.updateInfo = updateInfo;
    }

    public void run(T bean) {
        this.updateInfo.bean = bean;
        new UpdateRunner(this.updateInfo).run();
    }
}
