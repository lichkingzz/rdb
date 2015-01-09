package com.itranswarp.rdb;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Before;

import com.itranswarp.rdb.adapter.LocalDateTimeTypeAdapter;
import com.itranswarp.rdb.adapter.LocalDateTypeAdapter;
import com.itranswarp.rdb.datasource.JdbcDataSourceManager;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DbTestBase {

    public Rdb rdb = null;

    @Before
    public void setUp() throws Exception {
        if (rdb == null) {
            ComboPooledDataSource dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass("com.mysql.jdbc.Driver");
            dataSource.setJdbcUrl("jdbc:mysql://localhost/test_rdb");
            dataSource.setUser("test_rdb");
            dataSource.setPassword("test_rdb");
            dataSource.setMinPoolSize(2);
            dataSource.setAcquireIncrement(2);
            dataSource.setMaxPoolSize(32);
            JdbcDataSourceManager dataSourceManager = new JdbcDataSourceManager(dataSource);
            rdb = new Rdb(dataSourceManager);
            rdb.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter());
            rdb.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        }
        Connection conn = rdb.getDataSourceManager().getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("delete from User;");
        stmt.execute("delete from Book;");
        stmt.close();
        conn.close();
    }

}
