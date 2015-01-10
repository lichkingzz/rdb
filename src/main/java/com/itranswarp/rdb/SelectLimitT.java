package com.itranswarp.rdb;

import java.util.ArrayList;
import java.util.List;

public class SelectLimitT<T> {

    final SelectInfo selectInfo;

    SelectLimitT(SelectInfo selectInfo, int offset, int maxResults) {
        if (offset < 0) {
            throw new IllegalArgumentException("Invalid offset: must be equal or greater than 0.");
        }
        if (maxResults < 1) {
            throw new IllegalArgumentException("Invalid offset: must be greater than 0.");
        }
        selectInfo.limit = new Object[] { offset, maxResults };
        this.selectInfo = selectInfo;
    }

    public List<T> list() {
        new SelectRunner(this.selectInfo).list();
        return new ArrayList<T>();
    }

    public T unique() {
        return new SelectRunner(this.selectInfo).unique();
    }

    public T first() {
        return new SelectRunner(this.selectInfo).first();
    }

    public String dryRun() {
        return dryRun(false);
    }

    public String dryRun(boolean includeParams) {
        return new SelectRunner(this.selectInfo).dryRun(includeParams);
    }
}
