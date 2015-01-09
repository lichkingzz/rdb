package com.itranswarp.rdb;

/**
 * Base exception for all JDBC-related exception.
 * 
 * @author Michael Liao
 */
public class DataException extends RuntimeException {

    public DataException() {
    }

    public DataException(String message) {
        super(message);
    }

    public DataException(Throwable cause) {
        super(cause);
    }

    public DataException(String message, Throwable cause) {
        super(message, cause);
    }
}
