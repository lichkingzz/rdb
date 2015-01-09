package com.itranswarp.rdb;

/**
 * Throw when cannot build SQL.
 * 
 * @author Michael Liao
 */
public class SqlBuildingException extends DataException {

    public SqlBuildingException() {
    }

    public SqlBuildingException(String message) {
        super(message);
    }

    public SqlBuildingException(Throwable cause) {
        super(cause);
    }

    public SqlBuildingException(String message, Throwable cause) {
        super(message, cause);
    }
}
