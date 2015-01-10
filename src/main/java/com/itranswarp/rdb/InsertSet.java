package com.itranswarp.rdb;

import java.util.ArrayList;

public class InsertSet {

    final InsertInfo insertInfo;

    InsertSet(InsertInfo insertInfo, String field, Object value) {
        if (insertInfo.sets == null) {
            insertInfo.sets = new ArrayList<InsertFieldInfo>();
        }
        insertInfo.sets.add(new InsertFieldInfo(field, value));
        this.insertInfo = insertInfo;
    }

    public InsertSet set(String field, Object value) {
        this.insertInfo.sets.add(new InsertFieldInfo(field, value));
        return this;
    }

    public String dryRun() {
        return dryRun(false);
    }

    public String dryRun(boolean includeParams) {
        return new InsertRunner(this.insertInfo).dryRun(includeParams);
    }

    public void run() {
        new InsertRunner(this.insertInfo).run();
    }
}

