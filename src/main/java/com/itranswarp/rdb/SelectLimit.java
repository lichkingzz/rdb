package com.itranswarp.rdb;

import java.util.Map;

public class SelectLimit extends SelectLimitT<Map<String, Object>> {

    SelectLimit(SelectInfo selectInfo, int offset, int maxResults) {
        super(selectInfo, offset, maxResults);
    }

}
