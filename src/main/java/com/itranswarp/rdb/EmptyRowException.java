package com.itranswarp.rdb;

/**
 * Throw when empty row fetched but expect at least one row.
 * 
 * @author Michael Liao
 */
public class EmptyRowException extends DataException {

    public EmptyRowException() {
    }

    public EmptyRowException(String message) {
        super(message);
    }

    public EmptyRowException(Throwable cause) {
        super(cause);
    }

    public EmptyRowException(String message, Throwable cause) {
        super(message, cause);
    }
}
