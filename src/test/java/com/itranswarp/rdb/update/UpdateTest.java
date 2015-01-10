package com.itranswarp.rdb.update;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;

import com.itranswarp.rdb.DbTestBase;
import com.itranswarp.rdb.User;

/**
 * Test update.
 */
public class UpdateTest extends DbTestBase {

    User prepareUser(long id, String name, LocalDate birth) {
        User u = new User();
        u.id = id;
        u.name = name;
        u.email = name + "@update.sql";
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
    public void testUpdateTable() throws Exception {
        User a = prepareUser(50900001L, "User-A", LocalDate.of(1993, 11, 22));
        User b = prepareUser(50900002L, "User-B", LocalDate.of(1993, 11, 22));
        User c = prepareUser(50900003L, "User-C", LocalDate.of(2003, 2, 3));
        rdb.insert(User.class).runBatch(a, b, c);
        // select:
        assertEquals(3, rdb.select().from(User.class).where("name like ?", "User-%").list().size());
        // update:
        rdb.update("User")
            .set("name", "X")
            .run();
        // select:
        assertEquals(3, rdb.select().from(User.class).where("name = ?", "X").list().size());
        // update:
        rdb.update("User")
            .set("name", "Y")
            .where("id>=?", 50900002L)
            .run();
        // select:
        assertEquals(2, rdb.select().from(User.class).where("name = ?", "Y").list().size());
    }

    @Test
    public void testUpdateBean() throws Exception {
        User a = prepareUser(70008800001L, "BeanA", LocalDate.of(2001, 1, 1));
        User b = prepareUser(70008800002L, "BeanB", LocalDate.of(2001, 1, 1));
        User c = prepareUser(70008800003L, "BeanC", LocalDate.of(2001, 2, 2));
        rdb.insert(User.class).runBatch(a, b, c);
        // update:
        a.name = "Changed";
        a.email = "changed@email.com";
        rdb.update(User.class)
            .run(a);
        // select
        assertEquals("Changed", rdb.select().from(User.class).byId(a.id).name);
        assertEquals("changed@email.com", rdb.select().from(User.class).byId(a.id).email);
        // update b:
        b.name = "Also Changed";
        b.email = "changed@email.com";
        b.aboutMe = "about update";
        rdb.update(User.class)
            .fieldsOnly("name", "aboutMe")
            .run(b);
        // select:
        assertEquals("Also Changed", rdb.select().from(User.class).byId(b.id).name);
        assertEquals("about update", rdb.select().from(User.class).byId(b.id).aboutMe);
        // email not updated:
        assertEquals("BeanB@update.sql", rdb.select().from(User.class).byId(b.id).email);
    }

    @Test(expected=NullPointerException.class)
    public void testUpdateWhereIsNull() {
        rdb.update("User").set("name", "x").where(null, 1, 2, 3);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testDeleteWhereArgIsEmpty() {
        rdb.update("User").set("name", "x").where("name=?");
    }
}
