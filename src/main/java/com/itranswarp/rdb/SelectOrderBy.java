package com.itranswarp.rdb;

import java.util.Map;

public class SelectOrderBy extends SelectOrderByT<Map<String, Object>> {

    SelectOrderBy(SelectInfo selectInfo, String field) {
        super(selectInfo, field);
    }

}
