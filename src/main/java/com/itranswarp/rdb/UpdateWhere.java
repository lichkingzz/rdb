package com.itranswarp.rdb;

public class UpdateWhere {

    final UpdateInfo updateInfo;

    UpdateWhere(UpdateInfo updateInfo, String clause, Object[] args) {
        updateInfo.whereClause = clause;
        updateInfo.whereArgs = args;
        this.updateInfo = updateInfo;
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
