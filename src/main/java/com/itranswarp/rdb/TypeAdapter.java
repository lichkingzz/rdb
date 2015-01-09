package com.itranswarp.rdb;

/**
 * Convert a JDBC object to bean property type. For example, convert 
 * java.sql.Timestamp to java.time.LocalDateTime.
 * 
 * @author Michael Liao
 */
public interface TypeAdapter<T> {

    /**
     * Convert object to type T when load from JDBC.
     * 
     * @param value Non-null object value.
     * @return Object of type T.
     */
    T convert(Object value);

    /**
     * To JDBC type.
     * 
     * @param value Java object.
     * @return JDBC-compatible type.
     */
    Object toJdbcType(T value);
}
