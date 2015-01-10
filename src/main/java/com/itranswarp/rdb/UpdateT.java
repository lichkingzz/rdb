package com.itranswarp.rdb;

public class UpdateT<T> {

    final UpdateInfo updateInfo;

    UpdateT(UpdateInfo updateInfo, Class<T> beanClass) {
        updateInfo.beanClass = beanClass;
        updateInfo.beanMapper = Mappers.getMapper(beanClass);
        this.updateInfo = updateInfo;
    }

    public UpdateFieldsOnlyT<T> fieldsOnly(String... fields) {
        return new UpdateFieldsOnlyT<T>(this.updateInfo, fields);
    }

    public String dryRun() {
        return dryRun(false);
    }

    public String dryRun(boolean includeParams) {
        return new UpdateRunner(this.updateInfo).dryRun(includeParams);
    }

    public void run(T bean) {
        this.updateInfo.bean = bean;
        new UpdateRunner(this.updateInfo).run();
    }

}
