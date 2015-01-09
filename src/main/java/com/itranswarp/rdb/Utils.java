package com.itranswarp.rdb;

class Utils {

    static void validateClause(String clause, Object[] args) {
        if (clause == null) {
            throw new NullPointerException("clause object is null.");
        }
        if (clause.trim().isEmpty()) {
            throw new IllegalArgumentException("clause cannot be empty.");
        }
        int founds = 0;
        for (int i=0; i<clause.length(); i++) {
            if (clause.charAt(i) == '?') {
                founds ++;
            }
        }
        if (founds != args.length) {
            throw new IllegalArgumentException("Arguments not match the clause.");
        }
    }
}
