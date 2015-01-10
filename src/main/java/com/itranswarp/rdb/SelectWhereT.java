package com.itranswarp.rdb;

import java.util.List;

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

    public List<T> list() {
        return new SelectRunner(this.selectInfo).list();
    }

    public T first() {
        return new SelectRunner(this.selectInfo).first();
    }

    public T unique() {
        return new SelectRunner(this.selectInfo).unique();
    }

    public String dryRun() {
        return dryRun(false);
    }

    public String dryRun(boolean includeParams) {
        return new SelectRunner(this.selectInfo).dryRun(includeParams);
    }
}
