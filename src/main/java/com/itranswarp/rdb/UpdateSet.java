package com.itranswarp.rdb;

import java.util.ArrayList;

public class UpdateSet {

    final UpdateInfo updateInfo;

    UpdateSet(UpdateInfo updateInfo, String field, Object value) {
        updateInfo.sets = new ArrayList<UpdateSetInfo>();
        updateInfo.sets.add(new UpdateSetInfo(field, value));
        this.updateInfo = updateInfo;
    }

    public UpdateSet set(String field, Object value) {
        updateInfo.sets.add(new UpdateSetInfo(field, value));
        return this;
    }

    public UpdateWhere where(String clause, Object... args) {
        SQLUtils.validateClause(clause, args);
        return new UpdateWhere(this.updateInfo, clause, args);
    }

    public String dryRun() {
        return dryRun(false);
    }

    public String dryRun(boolean includeParams) {
        return new UpdateRunner(this.updateInfo).dryRun(includeParams);
    }

    public void run() {
        new UpdateRunner(this.updateInfo).run();
    }
}
