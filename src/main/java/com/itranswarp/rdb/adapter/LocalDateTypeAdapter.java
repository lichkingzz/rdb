package com.itranswarp.rdb.adapter;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;

import com.itranswarp.rdb.TypeAdapter;

/**
 * Convert object to `LocalDate`, and convert `LocalDate` to JDBC type `java.sql.Date`.
 * 
 * @author Michael Liao
 */
public class LocalDateTypeAdapter implements TypeAdapter<LocalDate> {

    @Override
    public LocalDate convert(Object value) {
        if (value instanceof String) {
            return LocalDate.parse((String) value);
        }
        if (value instanceof Timestamp) {
            return ((Timestamp) value).toLocalDateTime().toLocalDate();
        }
        if (value instanceof Long) {
            long n = (Long) value;
            return Instant.ofEpochMilli(n).atZone(ZoneId.systemDefault()).toLocalDate();
        }
        if (value instanceof java.util.Date) {
            long n = ((java.util.Date) value).getTime();
            return Instant.ofEpochMilli(n).atZone(ZoneId.systemDefault()).toLocalDate();
        }
        if (value instanceof Calendar) {
            long n = ((Calendar) value).getTimeInMillis();
            return  Instant.ofEpochMilli(n).atZone(ZoneId.systemDefault()).toLocalDate();
        }
        throw new IllegalArgumentException("Cannot convert object " + value + " to LocalDate.");
    }

    @Override
    public Object toJdbcType(LocalDate value) {
        long n = value.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
        return new java.sql.Date(n);
    }

}
