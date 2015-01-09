package com.itranswarp.rdb;

public class SelectWhere {

    final SelectInfo selectInfo;

    SelectWhere(SelectInfo selectInfo, String clause, Object[] args) {
        Utils.validateClause(clause, args);
        selectInfo.whereClause = clause.trim();
        selectInfo.whereArgs = args;
        this.selectInfo = selectInfo;
    }

    public SelectOrderBy orderBy(String field) {
        return new SelectOrderBy(this.selectInfo, field);
    }

    public SelectLimit limit(int offset, int maxResults) {
        return new SelectLimit(this.selectInfo, offset, maxResults);
    }

    public SelectLimit limit(int maxResults) {
        return new SelectLimit(this.selectInfo, 0, maxResults);
    }

    public String dryRun() {
        return new SelectRunner(this.selectInfo).dryRun();
    }

    public String dryRun(boolean includeParams) {
        return new SelectRunner(this.selectInfo).dryRun(includeParams);
    }
}
