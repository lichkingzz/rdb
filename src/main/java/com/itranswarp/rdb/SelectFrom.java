package com.itranswarp.rdb;

import java.util.List;
import java.util.Map;

public class SelectFrom {

    final SelectInfo selectInfo;

    SelectFrom(SelectInfo selectInfo, String table) {
        if (table == null) {
            throw new NullPointerException("table");
        }
        if (table.trim().isEmpty()) {
            throw new IllegalArgumentException("table cannot be blank.");
        }
        selectInfo.table = table.trim();
        this.selectInfo = selectInfo;
    }

    public SelectWhere where(String clause, Object... args) {
        return new SelectWhere(this.selectInfo, clause, args);
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
        return dryRun(false);
    }

    public String dryRun(boolean includeParams) {
        return new SelectRunner(this.selectInfo).dryRun(includeParams);
    }

    public List<Map<String, Object>> list() {
        return new SelectRunner(this.selectInfo).list();
    }

    public Map<String, Object> first() {
        return new SelectRunner(this.selectInfo).first();
    }

    public Map<String, Object> unique() {
        return new SelectRunner(this.selectInfo).unique();
    }

}
