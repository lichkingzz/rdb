package com.itranswarp.rdb.adapter;

import java.sql.Timestamp;
import java.util.Calendar;

import com.itranswarp.rdb.TypeAdapter;

/**
 * Convert object to `LocalDateTime`, and convert `LocalDateTime` to JDBC type `java.sql.Timestamp`.
 * 
 * @author Michael Liao
 */
public class TimestampTypeAdapter implements TypeAdapter<Timestamp> {

    @Override
    public Timestamp convert(Object value) {
        if (value instanceof Long) {
            long n = (Long) value;
            return new Timestamp(n);
        }
        if (value instanceof java.util.Date) {
            long n = ((java.util.Date) value).getTime();
            return new Timestamp(n);
        }
        if (value instanceof Calendar) {
            long n = ((Calendar) value).getTimeInMillis();
            return new Timestamp(n);
        }
        throw new IllegalArgumentException("Cannot convert object " + value + " to Timestamp.");
    }

    @Override
    public Object toJdbcType(Timestamp value) {
        return value;
    }

}
