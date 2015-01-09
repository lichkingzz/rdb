package com.itranswarp.rdb.datasource;

import static org.junit.Assert.*;

import java.sql.Connection;

import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class JdbcDataSourceManagerTest {

    @Test
    public void testCreateDataSourceManager() throws Exception {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://localhost/test_rdb");
        dataSource.setUser("test_rdb");
        dataSource.setPassword("test_rdb");
        dataSource.setMinPoolSize(2);
        dataSource.setAcquireIncrement(2);
        dataSource.setMaxPoolSize(32);
        JdbcDataSourceManager dataSourceManager = new JdbcDataSourceManager(dataSource);
        Connection conn = dataSourceManager.getConnection();
        assertNotNull(conn);
        conn.close();
    }

}
