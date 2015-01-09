package com.itranswarp.rdb.adapter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import com.itranswarp.rdb.TypeAdapter;

/**
 * Convert object to `LocalDateTime`, and convert `LocalDateTime` to JDBC type `java.sql.Timestamp`.
 * 
 * @author Michael Liao
 */
public class SqlDateTypeAdapter implements TypeAdapter<java.sql.Date> {

    public java.sql.Date readFromJDBC(ResultSet rs, int sqlType, int columnIndex) throws SQLException {
        
        return rs.getDate(columnIndex);
    }

    @Override
    public java.sql.Date convert(Object value) {
        if (value instanceof Long) {
            long n = (Long) value;
            return new java.sql.Date(n);
        }
        if (value instanceof java.util.Date) {
            long n = ((java.util.Date) value).getTime();
            return new java.sql.Date(n);
        }
        if (value instanceof Calendar) {
            long n = ((Calendar) value).getTimeInMillis();
            return new java.sql.Date(n);
        }
        throw new IllegalArgumentException("Cannot convert object " + value + " to java.sql.Date.");
    }

    @Override
    public Object toJdbcType(Object value) {
        return value;
    }

}
