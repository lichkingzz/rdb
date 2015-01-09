package com.itranswarp.rdb;

import java.sql.Connection;

/**
 * This interface is used to get connection.
 * 
 * @author Michael Liao
 */
public interface DataSourceManager {

    /**
     * Get a connection from underlying data source.
     * 
     * @return A connection object.
     */
    Connection getConnection();

    /**
     * Should connection be closed? Return true if it is a JDBC connection, 
     * return false if it is managed by container.
     * 
     * @return True if should close the connection.
     */
    boolean shouldCloseConnection();
}
