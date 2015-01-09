package com.itranswarp.rdb;

import java.util.Map;

public class SelectOrderBy extends SelectOrderByT<Map<String, Object>> {

    SelectOrderBy(SelectInfo selectInfo, String field) {
        super(selectInfo, field);
    }

    public SelectLimit limit(int offset, int maxResults) {
        return new SelectLimit(this.selectInfo, offset, maxResults);
    }

    public SelectLimit limit(int maxResults) {
        return new SelectLimit(this.selectInfo, 0, maxResults);
    }

}
