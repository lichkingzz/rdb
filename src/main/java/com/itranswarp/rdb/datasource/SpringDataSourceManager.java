package com.itranswarp.rdb.datasource;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;

import com.itranswarp.rdb.DataSourceManager;

/**
 * Using Spring-managed transaction to obtain a connection.
 * 
 * @author Michael Liao
 */
public class SpringDataSourceManager implements DataSourceManager {

    DataSource dataSource;

    /**
     * Set Spring-managed data source.
     * 
     * @param dataSource The DataSource object.
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }

    @Override
    public boolean shouldCloseConnection() {
        return false;
    }

}
