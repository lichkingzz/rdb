package com.itranswarp.rdb.insert;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.itranswarp.rdb.DbTestBase;
import com.itranswarp.rdb.SQLUtils;
import com.itranswarp.rdb.User;

/**
 * Test insert.
 */
public class InsertTest extends DbTestBase {

    User prepareUser(long id, String name, LocalDate birth) {
        User u = new User();
        u.id = id;
        u.name = name;
        u.email = name + "@insert.sql";
        u.passwd = "PASSWORD";
        u.gender = true;
        u.aboutMe = null;
        u.birth = birth;
        u.lastLoginAt = LocalDateTime.of(2015,  1, 1, 2, 3, 4);
        long t = System.currentTimeMillis();
        u.createdAt = new Timestamp(t);
        u.updatedAt = new Timestamp(t);
        u.version = 0;
        return u;
    }

    @Test
    public void testInsertBean() throws Exception {
        Date birth = new SimpleDateFormat("yyyy-MM-dd").parse("1991-01-02");
        Date dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2015-01-10 11:22:33");
        Date now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2001-02-03 12:34:56");
        Timestamp ts = new Timestamp(now.getTime());
        rdb.insert("User")
                .set("id", 909090)
                .set("name", "@_@")
                .set("email", "abc@xyz.com")
                .set("passwd", "^_^")
                .set("gender", true)
                .set("aboutMe", null)
                .set("birth", birth)
                .set("lastLoginAt", dt)
                .set("createdAt", ts)
                .set("updatedAt", ts)
                .set("version", 0)
                .run();
        // select:
        User u = rdb.select().from(User.class).byId(909090);
        assertEquals("@_@", u.name);
        assertEquals("^_^", u.passwd);
        assertTrue(u.gender);
        assertNull(u.aboutMe);
        assertEquals("1991-01-02", u.birth.toString());
        assertEquals("2015-01-10T11:22:33", u.lastLoginAt.toString());
        assertEquals("2001-02-03 12:34:56", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(u.createdAt));
        assertEquals("2001-02-03 12:34:56", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(u.updatedAt));
    }

    @Test
    public void testInsertBeans() throws Exception {
        User a = prepareUser(17023001L, "A", LocalDate.of(2000, 1, 1));
        User b = prepareUser(17023002L, "B", LocalDate.of(2000, 1, 1));
        User c = prepareUser(17023003L, "C", LocalDate.of(2000, 1, 1));
        rdb.insert(User.class).runBatch(a, b, c);
        // select:
        List<User> us = rdb.select().from(User.class).where("birth=?", SQLUtils.asSqlDate("2000-01-01")).orderBy("name").list();
        assertEquals(3, us.size());
        User u1 = us.get(0);
        User u2 = us.get(1);
        User u3 = us.get(2);
        assertEquals("A", u1.name);
        assertEquals("B", u2.name);
        assertEquals("C", u3.name);
    }
}
