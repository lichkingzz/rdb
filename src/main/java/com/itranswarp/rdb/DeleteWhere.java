package com.itranswarp.rdb;

public class DeleteWhere {

    final DeleteInfo deleteInfo;

    DeleteWhere(DeleteInfo deleteInfo, String clause, Object[]... args) {
        deleteInfo.whereClause = clause;
        deleteInfo.whereArgs = args;
        this.deleteInfo = deleteInfo;
    }

    public String dryRun() {
        return dryRun(false);
    }

    public String dryRun(boolean includeParams) {
        return new DeleteRunner(this.deleteInfo).dryRun(includeParams);
    }

    /**
     * Run this delete.
     */
    public void run() {
        new DeleteRunner(this.deleteInfo).run();
    }
}
