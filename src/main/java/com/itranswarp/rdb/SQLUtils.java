package com.itranswarp.rdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SQLUtils {

    /**
     * Convert string as format "yyyy-MM-dd HH:mm:ss" or "yyyy-MM-dd HH:mm" to java.util.Date.
     * 
     * @param date String date.
     * @return java.util.Date object.
     */
    public static java.util.Date asDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        }
        catch (ParseException e) {
        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
        }
        catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Convert string as format "yyyy-MM-dd" to java.sql.Date.
     * 
     * @param date String date.
     * @return java.sql.Date object.
     */
    public static java.sql.Date asSqlDate(String date) {
        try {
            java.util.Date dt = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            return new java.sql.Date(dt.getTime());
        }
        catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

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

    static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            }
            catch (SQLException e) {
            }
        }
    }

    static void close(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            }
            catch (SQLException e) {
            }
        }
    }

    static void close(Connection conn, boolean shouldClose) {
        if (shouldClose && conn != null) {
            try {
                conn.close();
            }
            catch (SQLException e) {
            }
        }
    }

}
