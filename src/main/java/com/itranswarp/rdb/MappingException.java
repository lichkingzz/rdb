package com.itranswarp.rdb;

/**
 * Throw if set bean property failed when mapping JDBC result to JavaBean.
 * 
 * @author Michael Liao
 */
public class MappingException extends DataException {

    public MappingException() {
    }

    public MappingException(String message) {
        super(message);
    }

    public MappingException(Throwable cause) {
        super(cause);
    }

    public MappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
