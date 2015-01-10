package com.itranswarp.rdb;

import java.util.List;

class SelectRunnerT<T> {

    final SelectInfo selectInfo;

    SelectRunnerT(SelectInfo selectInfo) {
        this.selectInfo = selectInfo;
    }

    public String dryRun() {
        return dryRun(false);
    }

    public String dryRun(boolean includeParams) {
        return new SelectRunner(this.selectInfo).dryRun(includeParams);
    }

    public List<T> list() {
        return new SelectRunner(this.selectInfo).list();
    }

    public T unique() {
        return new SelectRunner(this.selectInfo).unique();
    }

    public T first() {
        return new SelectRunner(this.selectInfo).first();
    }
}
