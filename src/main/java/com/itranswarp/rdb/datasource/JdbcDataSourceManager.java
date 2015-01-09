package com.itranswarp.rdb.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.itranswarp.rdb.DataException;
import com.itranswarp.rdb.DataSourceManager;

public class JdbcDataSourceManager implements DataSourceManager {

    DataSource dataSource;

    public JdbcDataSourceManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() {
        try {
            Connection conn = dataSource.getConnection();
            conn.setAutoCommit(true);
            return conn;
        }
        catch (SQLException e) {
            throw new DataException(e);
        }
    }

    @Override
    public boolean shouldCloseConnection() {
        return true;
    }

}
