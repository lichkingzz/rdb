package com.itranswarp.rdb.insert;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;

import com.itranswarp.rdb.DbTestBase;
import com.itranswarp.rdb.SQLUtils;
import com.itranswarp.rdb.StandardEntityListener;
import com.itranswarp.rdb.User;

/**
 * Test insert.
 */
public class InsertEventTest extends DbTestBase {

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
        return u;
    }

    @Test
    public void testInsertBeans() throws Exception {
        User a = prepareUser(17023001L, "A", LocalDate.of(2000, 1, 1));
        User b = prepareUser(17023002L, "B", LocalDate.of(2000, 1, 1));
        User c = prepareUser(17023003L, "C", LocalDate.of(2000, 1, 1));
        rdb.setEntityListener(new StandardEntityListener());
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
        double t = System.currentTimeMillis();
        assertEquals(t, u1.createdAt.getTime(), DELTA_3_SECONDS);
        assertEquals(t, u2.createdAt.getTime(), DELTA_3_SECONDS);
        assertEquals(t, u3.createdAt.getTime(), DELTA_3_SECONDS);
        assertEquals(t, u1.updatedAt.getTime(), DELTA_3_SECONDS);
        assertEquals(t, u2.updatedAt.getTime(), DELTA_3_SECONDS);
        assertEquals(t, u3.updatedAt.getTime(), DELTA_3_SECONDS);
        assertEquals(1, u1.version);
        assertEquals(1, u2.version);
        assertEquals(1, u3.version);
    }
}
