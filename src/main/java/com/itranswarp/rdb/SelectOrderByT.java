package com.itranswarp.rdb;

import java.util.List;

public class SelectOrderByT<T> {

    final SelectInfo selectInfo;

    SelectOrderByT(SelectInfo selectInfo, String field) {
        if (selectInfo.orderBys == null) {
            // first orderBy():
            selectInfo.orderBys = new SelectOrderByInfo[] { new SelectOrderByInfo(field, false) };
        }
        else {
            SelectOrderByInfo[] newOrderBys = new SelectOrderByInfo[selectInfo.orderBys.length + 1];
            System.arraycopy(selectInfo.orderBys, 0, newOrderBys, 0, selectInfo.orderBys.length);
            newOrderBys[newOrderBys.length - 1] = new SelectOrderByInfo(field, false);
            selectInfo.orderBys = newOrderBys;
        }
        this.selectInfo = selectInfo;
    }

    public SelectOrderByT<T> asc() {
        this.selectInfo.orderBys[this.selectInfo.orderBys.length-1].desc = false;
        return this;
    }

    public SelectOrderByT<T> desc() {
        this.selectInfo.orderBys[this.selectInfo.orderBys.length-1].desc = true;
        return this;
    }

    public SelectOrderByT<T> orderBy(String field) {
        return new SelectOrderByT<T>(this.selectInfo, field);
    }

    public SelectLimitT<T> limit(int offset, int maxResults) {
        return new SelectLimitT<T>(this.selectInfo, offset, maxResults);
    }

    public SelectLimitT<T> limit(int maxResults) {
        return limit(0, maxResults);
    }

    public List<T> list() {
        return new SelectRunnerT<T>(this.selectInfo).list();
    }

    public T first() {
        return new SelectRunnerT<T>(this.selectInfo).first();
    }

    public String dryRun() {
        return dryRun(false);
    }

    public String dryRun(boolean includeParams) {
        return new SelectRunner(this.selectInfo).dryRun(includeParams);
    }
}
