package com.itranswarp.rdb;

public class SelectWhereT<T> {

    final SelectInfo selectInfo;

    SelectWhereT(SelectInfo selectInfo, String clause, Object[] args) {
        Utils.validateClause(clause, args);
        selectInfo.whereClause = clause.trim();
        selectInfo.whereArgs = args;
        this.selectInfo = selectInfo;
    }

    public SelectOrderByT<T> orderBy(String field) {
        return new SelectOrderByT<T>(this.selectInfo, field);
    }

    public SelectLimitT<T> limit(int offset, int maxResults) {
        return new SelectLimitT<T>(this.selectInfo, offset, maxResults);
    }

    public SelectLimitT<T> limit(int maxResults) {
        return new SelectLimitT<T>(this.selectInfo, 0, maxResults);
    }

    public T first() {
        return new SelectRunnerT<T>(this.selectInfo).first();
    }

    public T unique() {
        return new SelectRunnerT<T>(this.selectInfo).unique();
    }

    public String dryRun() {
        return new SelectRunner(this.selectInfo).dryRun();
    }

    public String dryRun(boolean includeParams) {
        return new SelectRunner(this.selectInfo).dryRun(includeParams);
    }
}
