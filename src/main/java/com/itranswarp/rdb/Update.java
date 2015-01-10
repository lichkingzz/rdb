package com.itranswarp.rdb;

public class Update {

    final UpdateInfo updateInfo;

    Update(UpdateInfo updateInfo, String table) {
        updateInfo.table = table;
        this.updateInfo = updateInfo;
    }

    public UpdateSet set(String field, Object value) {
        return new UpdateSet(this.updateInfo, field, value);
    }

}
