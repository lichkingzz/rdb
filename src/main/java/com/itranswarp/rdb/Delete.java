package com.itranswarp.rdb;

class Delete {

    DeleteInfo deleteInfo;

    Delete(DeleteInfo deleteInfo) {
        this.deleteInfo = deleteInfo;
    }

    DeleteWhere where(String clause, Object... args) {
        this.deleteInfo.whereClause = clause;
        this.deleteInfo.whereArgs = args;
        return new DeleteWhere(this.deleteInfo);
    }
}

class DeleteT<T> {

    DeleteInfo deleteInfo;

    DeleteT(DeleteInfo deleteInfo) {
        this.deleteInfo = deleteInfo;
    }

    int run() {
        return new DeleteRunT<T>(this.deleteInfo).run();
    }
}

class DeleteInfo {

    final Rdb rdb;
    String table = null;
    Object bean = null;
    String whereClause;
    Object[] whereArgs;

    DeleteInfo(Rdb rdb) {
        this.rdb = rdb;
    }

}

class DeleteWhere {

    DeleteInfo deleteInfo;

    DeleteWhere(DeleteInfo deleteInfo) {
        this.deleteInfo = deleteInfo;
    }

    public int run() {
        return new DeleteRun(this.deleteInfo).run();
    }
}

class DeleteRun {

    DeleteInfo deleteInfo;

    DeleteRun(DeleteInfo deleteInfo) {
        this.deleteInfo = deleteInfo;
    }

    public DeleteRun dryRun() {
        return this;
    }

    public int run() {
        return 0;
    }
}

class DeleteRunT<T> {

    DeleteInfo deleteInfo;

    DeleteRunT(DeleteInfo deleteInfo) {
        this.deleteInfo = deleteInfo;
    }

    public DeleteRunT<T> dryRun() {
        return this;
    }

    public int run() {
        return 0;
    }
}
