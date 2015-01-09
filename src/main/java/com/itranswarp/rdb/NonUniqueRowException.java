package com.itranswarp.rdb;

/**
 * Throw when multiple rows fetched by expected only one row.
 * 
 * @author Michael Liao
 */
public class NonUniqueRowException extends DataException {

    public NonUniqueRowException() {
    }

    public NonUniqueRowException(String message) {
        super(message);
    }

    public NonUniqueRowException(Throwable cause) {
        super(cause);
    }

    public NonUniqueRowException(String message, Throwable cause) {
        super(message, cause);
    }
}
