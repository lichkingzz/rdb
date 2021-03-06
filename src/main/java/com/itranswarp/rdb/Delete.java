package com.itranswarp.rdb;

public class Delete {

    final DeleteInfo deleteInfo;

    Delete(DeleteInfo deleteInfo, String table) {
        deleteInfo.table = table;
        this.deleteInfo = deleteInfo;
    }

    public DeleteWhere where(String clause, Object... args) {
        return new DeleteWhere(this.deleteInfo, clause, args);
    }

    public String dryRun() {
        return dryRun(false);
    }

    public String dryRun(boolean includeParams) {
        return new DeleteRunner(this.deleteInfo).dryRun(includeParams);
    }

    /**
     * Danger! Will delete the whole table!
     */
    public void run() {
        new DeleteRunner(this.deleteInfo).run();
    }

}
