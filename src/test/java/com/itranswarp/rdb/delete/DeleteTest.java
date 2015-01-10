package com.itranswarp.rdb.delete;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;

import com.itranswarp.rdb.DbTestBase;
import com.itranswarp.rdb.SQLUtils;
import com.itranswarp.rdb.User;

/**
 * Test insert.
 */
public class DeleteTest extends DbTestBase {

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
    public void testDeleteTable() throws Exception {
        User a = prepareUser(50900001L, "User-A", LocalDate.of(1993, 11, 22));
        User b = prepareUser(50900002L, "User-B", LocalDate.of(1993, 11, 22));
        User c = prepareUser(50900003L, "User-C", LocalDate.of(2003, 2, 3));
        rdb.insert(User.class).runBatch(a, b, c);
        // select:
        assertEquals(3, rdb.select().from(User.class).where("name like ?", "User-%").list().size());
        // delete:
        rdb.delete("User").where("birth=?", SQLUtils.asSqlDate("1993-11-22")).run();
        // select:
        assertEquals(1, rdb.select().from(User.class).where("name like ?", "User-%").list().size());
    }

    @Test
    public void testDeleteBean() throws Exception {
        User a = prepareUser(8800001L, "BeanA", LocalDate.of(2001, 1, 1));
        User b = prepareUser(8800002L, "BeanB", LocalDate.of(2001, 1, 1));
        User c = prepareUser(8800003L, "BeanC", LocalDate.of(2001, 2, 2));
        rdb.insert(User.class).runBatch(a, b, c);
        // select:
        assertEquals(3, rdb.select().from(User.class).where("name like ?", "Bean%").list().size());
        // delete:
        rdb.delete(User.class).runBatch(a, b);
        // select:
        assertEquals(1, rdb.select().from(User.class).where("name like ?", "Bean%").list().size());
    }

}
