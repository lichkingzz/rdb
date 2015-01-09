package com.itranswarp.rdb;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.itranswarp.rdb.adapter.LocalDateTimeTypeAdapter;
import com.itranswarp.rdb.adapter.LocalDateTypeAdapter;
import com.itranswarp.rdb.datasource.JdbcDataSourceManager;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Test select.
 */
public class SelectTest {

    Rdb rdb = null;

    Date toDate(LocalDate d) {
        if (d == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, d.getYear());
        c.set(Calendar.MONTH, d.getMonthValue()-1);
        c.set(Calendar.DAY_OF_MONTH, d.getDayOfMonth());
        return c.getTime();
    }

    Date toDate(LocalDateTime dt) {
        if (dt == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, dt.getYear());
        c.set(Calendar.MONTH, dt.getMonthValue()-1);
        c.set(Calendar.DAY_OF_MONTH, dt.getDayOfMonth());
        c.set(Calendar.HOUR_OF_DAY, dt.getHour());
        c.set(Calendar.MINUTE, dt.getMinute());
        c.set(Calendar.SECOND, dt.getSecond());
        return c.getTime();
    }

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
        Connection conn = rdb.dataSourceManager.getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("delete from User;");
        stmt.execute("delete from Book;");
        stmt.close();
        conn.close();
    }

    public Timestamp insertUser(long id, String name, LocalDate birth) throws Exception {
        return insertUser(id, name, birth, null);
    }

    public Timestamp insertUser(long id, String name, LocalDate birth, LocalDateTime lastLoginAt) throws Exception {
        Timestamp t = new Timestamp(System.currentTimeMillis() / 1000L * 1000);
        Object[] args = new Object[] {
                id,
                name + "@email.com",
                name + "-PASSWORD",
                name,
                true,
                null,
                toDate(birth),
                toDate(lastLoginAt),
                t,
                t,
                0
        };
        Connection conn = rdb.dataSourceManager.getConnection();
        PreparedStatement ps = conn.prepareStatement("insert into User(id, email, passwd, name, gender, aboutMe, birth, lastLoginAt, createdAt, updatedAt, version) values (?,?,?,?,?,?,?,?,?,?,?)");
        int n = 0;
        for (Object arg : args) {
            n ++;
            ps.setObject(n, arg);
        }
        ps.executeUpdate();
        ps.close();
        conn.close();
        return t;
    }

//    @Test
//    public void testSelectFromString() {
//        assertEquals("SELECT * FROM student",
//                newRdb().select()
//                        .from("student")
//                        .dryRun());
//
//        assertEquals("SELECT * FROM student",
//                newRdb().select("*")
//                        .from("student")
//                        .dryRun());
//
//        assertEquals("SELECT id, name, grade FROM student",
//                newRdb().select("id", "name", "grade")
//                        .from("student")
//                        .dryRun());
//
//        assertEquals("SELECT * FROM student WHERE id=?",
//                newRdb().select()
//                        .from("student")
//                        .where("id=?", 123)
//                        .dryRun());
//
//        assertEquals("SELECT * FROM student WHERE s>=? and g<?",
//                newRdb().select()
//                        .from("student")
//                        .where("s>=? and g<?", 123, 456)
//                        .dryRun());
//
//        assertEquals("SELECT * FROM student LIMIT 0, 100",
//                newRdb().select()
//                        .from("student")
//                        .limit(100)
//                        .dryRun(true));
//
//        assertEquals("SELECT * FROM student LIMIT ?, ?",
//                newRdb().select()
//                        .from("student")
//                        .limit(10, 100)
//                        .dryRun());
//
//        assertEquals("SELECT * FROM student WHERE s>=? and g<? LIMIT ?, ?",
//                newRdb().select()
//                        .from("student")
//                        .where("s>=? and g<?", 123, 456)
//                        .limit(10, 100)
//                        .dryRun());
//
//        assertEquals("SELECT * FROM student ORDER BY score DESC",
//                newRdb().select()
//                        .from("student")
//                        .orderBy("score").desc()
//                        .dryRun());
//
//        assertEquals("SELECT * FROM student ORDER BY score DESC LIMIT ?, ?",
//                newRdb().select()
//                        .from("student")
//                        .orderBy("score").desc()
//                        .limit(10, 100)
//                        .dryRun());
//
//        assertEquals("SELECT * FROM student WHERE s>=? and g<? ORDER BY score DESC, name LIMIT ?, ?",
//                newRdb().select()
//                        .from("student")
//                        .where("s>=? and g<?", 123, 456)
//                        .orderBy("score").desc()
//                        .orderBy("name")
//                        .limit(10, 100)
//                        .dryRun());
//    }
//
//    @Test
//    public void testSelectFromStringWithArgs() {
//        assertEquals("SELECT * FROM student",
//                newRdb().select()
//                        .from("student")
//                        .dryRun(true));
//
//        assertEquals("SELECT * FROM student",
//                newRdb().select("*")
//                        .from("student")
//                        .dryRun(true));
//
//        assertEquals("SELECT id, name, grade FROM student",
//                newRdb().select("id", "name", "grade")
//                        .from("student")
//                        .dryRun(true));
//
//        assertEquals("SELECT * FROM student WHERE id=123",
//                newRdb().select()
//                        .from("student")
//                        .where("id=?", 123)
//                        .dryRun(true));
//
//        assertEquals("SELECT * FROM student WHERE s>=123 and g<456",
//                newRdb().select()
//                        .from("student")
//                        .where("s>=? and g<?", 123, 456)
//                        .dryRun(true));
//
//        assertEquals("SELECT * FROM student LIMIT 0, 100",
//                newRdb().select()
//                        .from("student")
//                        .limit(100)
//                        .dryRun(true));
//
//        assertEquals("SELECT * FROM student LIMIT 10, 100",
//                newRdb().select()
//                        .from("student")
//                        .limit(10, 100)
//                        .dryRun(true));
//
//        assertEquals("SELECT * FROM student WHERE s>=\'A\' and g<\'B\' LIMIT 10, 100",
//                newRdb().select()
//                        .from("student")
//                        .where("s>=? and g<?", "A", "B")
//                        .limit(10, 100)
//                        .dryRun(true));
//
//        assertEquals("SELECT * FROM student ORDER BY score DESC",
//                newRdb().select()
//                        .from("student")
//                        .orderBy("score").desc()
//                        .dryRun(true));
//
//        assertEquals("SELECT * FROM student ORDER BY score DESC LIMIT 10, 100",
//                newRdb().select()
//                        .from("student")
//                        .orderBy("score").desc()
//                        .limit(10, 100)
//                        .dryRun(true));
//
//        assertEquals("SELECT * FROM student WHERE s=1 and g<\'2015-01-05\' ORDER BY score DESC, name LIMIT 10, 100",
//                newRdb().select()
//                        .from("student")
//                        .where("s=? and g<?", true, LocalDate.of(2015, 1, 5))
//                        .orderBy("score").desc()
//                        .orderBy("name")
//                        .limit(10, 100)
//                        .dryRun(true));
//    }
//
//    @Test
//    public void testSelectFromBean() {
//        assertEquals("SELECT * FROM User",
//                newRdb().select()
//                        .from(User.class)
//                        .dryRun());
//
//        assertEquals("SELECT * FROM User",
//                newRdb().select("*")
//                        .from(User.class)
//                        .dryRun());
//
//        assertEquals("SELECT id, name, grade FROM User",
//                newRdb().select("id", "name", "grade")
//                        .from(User.class)
//                        .dryRun());
//
//        assertEquals("SELECT * FROM User WHERE id=?",
//                newRdb().select()
//                        .from(User.class)
//                        .where("id=?", 123)
//                        .dryRun());
//
//        assertEquals("SELECT * FROM User WHERE s>=? and g<?",
//                newRdb().select()
//                        .from(User.class)
//                        .where("s>=? and g<?", 123, 456)
//                        .dryRun());
//
//        assertEquals("SELECT name FROM User WHERE s>=? and g<?",
//                newRdb().select()
//                        .from(User.class)
//                        .excludeFields("id", "email", "password", "birth", "createdAt", "updatedAt", "version")
//                        .where("s>=? and g<?", 123, 456)
//                        .dryRun());
//
//        assertEquals("SELECT name FROM User WHERE s>=? and g<?",
//                newRdb().select(" * ")
//                        .from(User.class)
//                        .excludeFields("id", "email", "password", "birth", "createdAt", "updatedAt", "version")
//                        .where("s>=? and g<?", 123, 456)
//                        .dryRun());
//
//        assertEquals("SELECT * FROM User LIMIT 0, 100",
//                newRdb().select()
//                        .from(User.class)
//                        .limit(100)
//                        .dryRun(true));
//
//        assertEquals("SELECT * FROM User LIMIT ?, ?",
//                newRdb().select()
//                        .from(User.class)
//                        .limit(10, 100)
//                        .dryRun());
//
//        assertEquals("SELECT * FROM User WHERE s>=? and g<? LIMIT ?, ?",
//                newRdb().select()
//                        .from(User.class)
//                        .where("s>=? and g<?", 123, 456)
//                        .limit(10, 100)
//                        .dryRun());
//
//        assertEquals("SELECT * FROM User ORDER BY score DESC",
//                newRdb().select()
//                        .from(User.class)
//                        .orderBy("score").desc()
//                        .dryRun());
//
//        assertEquals("SELECT * FROM User ORDER BY score DESC LIMIT ?, ?",
//                newRdb().select()
//                        .from(User.class)
//                        .orderBy("score").desc()
//                        .limit(10, 100)
//                        .dryRun());
//
//        assertEquals("SELECT * FROM User WHERE s>=? and g<? ORDER BY score DESC, name LIMIT ?, ?",
//                newRdb().select()
//                        .from(User.class)
//                        .where("s>=? and g<?", 123, 456)
//                        .orderBy("score").desc()
//                        .orderBy("name")
//                        .limit(10, 100)
//                        .dryRun());
//    }

    @Test
    public void testSelectListAsMap() throws Exception {
        Timestamp t = insertUser(123L, "Test Name", LocalDate.of(2000, 1, 1));
        List<Map<String, Object>> users = rdb.select()
                .from("User")
                .orderBy("name")
                .list();
        assertEquals(1, users.size());
        Map<String, Object> u = users.get(0);
        assertEquals(123L, u.get("id"));
        assertEquals("Test Name", u.get("name"));
        assertEquals("2000-01-01", new SimpleDateFormat("yyyy-MM-dd").format(u.get("birth")));
        assertEquals(t, u.get("createdAt"));
        assertEquals(t, u.get("updatedAt"));
        assertNull(u.get("aboutMe"));
    }

    @Test
    public void testSelectListAsUsers() throws Exception {
        Timestamp t = insertUser(234L, "Test Name", LocalDate.of(2000, 1, 1),
                LocalDateTime.of(2008, 1, 1, 12, 34, 56));
        List<User> users = rdb.select()
                .from(User.class)
                .orderBy("name")
                .list();
        assertEquals(1, users.size());
        User u = users.get(0);
        assertEquals(234L, u.id);
        assertEquals("Test Name", u.name);
        assertEquals("2000-01-01", u.birth.toString());
        assertEquals("2008-01-01T12:34:56", u.lastLoginAt.toString());
        assertEquals(t, u.createdAt);
        assertEquals(t, u.updatedAt);
        assertNull(u.aboutMe);
    }

    @Test
    public void testSelectUniqueUser() throws Exception {
        Timestamp t = insertUser(1111L, "Single", LocalDate.of(1911, 11, 11),
                LocalDateTime.of(2011, 11, 11, 11, 11, 11));
        User u = rdb.select()
                .from(User.class)
                .where("id=?", 1111L)
                .unique();
        assertEquals(1111L, u.id);
        assertEquals("Single", u.name);
        assertEquals("1911-11-11", u.birth.toString());
        assertEquals("2011-11-11T11:11:11", u.lastLoginAt.toString());
        assertEquals(t, u.createdAt);
        assertEquals(t, u.updatedAt);
        assertNull(u.aboutMe);
    }

    @Test
    public void testSelectUserById() throws Exception {
        insertUser(309000L, "Name-309000", LocalDate.of(2000, 2, 2));
        insertUser(309002L, "Name-309002", LocalDate.of(2000, 3, 3));
        User u1 = rdb.select()
                .from(User.class)
                .byId(309000L);
        assertEquals(309000L, u1.id);
        assertEquals("Name-309000", u1.name);
        assertEquals("2000-02-02", u1.birth.toString());
        User u2 = rdb.select("id", "name")
                .from(User.class)
                .byId(309002L);
        assertEquals(309002L, u2.id);
        assertEquals("Name-309002", u2.name);
        assertNull(u2.birth);
    }

    @Test(expected=NonUniqueRowException.class)
    public void testSelectFirstOkButUniqueFailedForMultipleRowsFound() throws Exception {
        insertUser(1212000L, "Double User", LocalDate.of(1922, 2, 2));
        insertUser(1212001L, "Double User", LocalDate.of(1933, 3, 3));
        User u = rdb.select()
                .from(User.class)
                .where("name=?", "Double User")
                .orderBy("id")
                .first();
        assertEquals(1212000L, u.id);
        rdb.select()
                .from(User.class)
                .where("name=?", "Double User")
                .unique();
    }

    @Test(expected=EmptyRowException.class)
    public void testSelectUniqueUserFailedForEmptyRowFound() throws Exception {
        rdb.select()
                .from(User.class)
                .where("name=?", "No Name of this User")
                .unique();
    }
}
