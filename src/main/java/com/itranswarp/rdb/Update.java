package com.itranswarp.rdb;

import java.util.ArrayList;
import java.util.List;

public class Update {

    UpdateInfo updateInfo;

    Update(UpdateInfo updateInfo) {
        this.updateInfo = updateInfo;
    }

    public Update set(String field, Object value) {
        if (this.updateInfo.sets == null) {
            this.updateInfo.sets = new ArrayList<UpdateSetInfo>();
        }
        this.updateInfo.sets.add(new UpdateSetInfo(field, value));
        return this;
    }

    public UpdateWhere where(String clause, Object... args) {
        this.updateInfo.whereClause = clause;
        this.updateInfo.whereArgs = args;
        return new UpdateWhere(this.updateInfo);
    }
}

class UpdateT<T> {

    UpdateInfo updateInfo;

    UpdateT(UpdateInfo updateInfo) {
        this.updateInfo = updateInfo;
    }

    FieldsOnlyT<T> fieldsOnly(String... fields) {
        this.updateInfo.sets = new ArrayList<UpdateSetInfo>();
        for (int i=0; i < fields.length; i++) {
            String field = fields[i];
            this.updateInfo.sets.add(new UpdateSetInfo(field, null));
        }
        return new FieldsOnlyT<T>(this.updateInfo);
    }
}

class UpdateInfo {

    final Rdb rdb;
    String table = null;
    Object bean = null;
    List<UpdateSetInfo> sets = null;
    String whereClause;
    Object[] whereArgs;

    UpdateInfo(Rdb rdb) {
        this.rdb = rdb;
    }

}

class UpdateSetInfo {

    final String field;
    final Object value;

    public UpdateSetInfo(String field, Object value) {
        this.field = field;
        this.value = value;
    }
}

class FieldsOnlyT<T> {

    UpdateInfo updateInfo;

    FieldsOnlyT(UpdateInfo updateInfo) {
        this.updateInfo = updateInfo;
    }

    public int run() {
        return new UpdateRunT<T>(this.updateInfo).run();
    }
}

class UpdateWhere {

    UpdateInfo updateInfo;

    UpdateWhere(UpdateInfo updateInfo) {
        this.updateInfo = updateInfo;
    }

    public int run() {
        return 0;
    }
}

class UpdateRun {

    UpdateInfo updateInfo;

    UpdateRun(UpdateInfo updateInfo) {
        this.updateInfo = updateInfo;
    }

    public UpdateRun dryRun() {
        return this;
    }

    public int run() {
        return 0;
    }
}

class UpdateRunT<T> {

    UpdateInfo updateInfo;

    UpdateRunT(UpdateInfo updateInfo) {
        this.updateInfo = updateInfo;
    }

    public UpdateRunT<T> dryRun() {
        return this;
    }

    public int run() {
        return 0;
    }
}
