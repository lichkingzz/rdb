package com.itranswarp.rdb;

import java.sql.PreparedStatement;
import java.sql.SQLException;

class SQLUtils {

    static String toArgString(Object arg) {
        if (arg == null) {
            return "NULL";
        }
        if (arg instanceof Boolean) {
            return ((Boolean) arg).booleanValue() ? "1" : "0";
        }
        if (arg instanceof Number) {
            return arg.toString();
        }
        return escapeString(arg.toString());
    }

    /**
     * Not a safe escape function. Just for display SQL with actual parameters.
     * 
     * @param arg String arg.
     * @return Escaped string arg.
     */
    static String escapeString(String arg) {
        if (arg.indexOf('\'') != (-1)) {
            arg = arg.replace("\'", "\\\'");
        }
        return "'" + arg + "'";
    }

    static void setPreparedStatementParameters(PreparedStatement ps, Object[] values) throws SQLException {
        for (int i=0; i<values.length; i++) {
            ps.setObject(i+1, values[i]);
        }
    }
}
