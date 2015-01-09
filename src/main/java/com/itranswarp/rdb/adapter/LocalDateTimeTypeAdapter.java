package com.itranswarp.rdb.adapter;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;

import com.itranswarp.rdb.TypeAdapter;

/**
 * Convert object to `LocalDateTime`, and convert `LocalDateTime` to JDBC type `java.sql.Timestamp`.
 * 
 * @author Michael Liao
 */
public class LocalDateTimeTypeAdapter implements TypeAdapter<LocalDateTime> {

    @Override
    public LocalDateTime convert(Object value) {
        if (value instanceof String) {
            return LocalDateTime.parse((String) value);
        }
        if (value instanceof Timestamp) {
            return ((Timestamp) value).toLocalDateTime();
        }
        if (value instanceof Long) {
            long n = (Long) value;
            return Instant.ofEpochMilli(n).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        if (value instanceof java.util.Date) {
            long n = ((java.util.Date) value).getTime();
            return Instant.ofEpochMilli(n).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        if (value instanceof Calendar) {
            long n = ((Calendar) value).getTimeInMillis();
            return  Instant.ofEpochMilli(n).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        throw new IllegalArgumentException("Cannot convert object " + value + " to LocalDateTime.");
    }

    @Override
    public Object toJdbcType(LocalDateTime value) {
        long n = value.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
        return new java.sql.Date(n);
    }

}
