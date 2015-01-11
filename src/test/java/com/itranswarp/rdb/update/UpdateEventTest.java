package com.itranswarp.rdb.update;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;

import com.itranswarp.rdb.DbTestBase;
import com.itranswarp.rdb.StandardEntityListener;
import com.itranswarp.rdb.User;

/**
 * Test update.
 */
public class UpdateEventTest extends DbTestBase {

    long getCreatedTime() throws Exception {
        return new SimpleDateFormat("yyyy-MM-dd").parse("2008-8-8").getTime();
    }

    User prepareUser(long id, String name, LocalDate birth) throws Exception {
        User u = new User();
        u.id = id;
        u.name = name;
        u.email = name + "@update.sql";
        u.passwd = "PASSWORD";
        u.gender = true;
        u.aboutMe = null;
        u.birth = birth;
        u.lastLoginAt = LocalDateTime.of(2015,  1, 1, 2, 3, 4);
        u.createdAt = u.updatedAt = new Timestamp(getCreatedTime());
        u.version = 1;
        return u;
    }

    @Test
    public void testUpdateBean() throws Exception {
        User a = prepareUser(70008800001L, "BeanA", LocalDate.of(2001, 1, 1));
        User b = prepareUser(70008800002L, "BeanB", LocalDate.of(2001, 1, 1));
        User c = prepareUser(70008800003L, "BeanC", LocalDate.of(2001, 2, 2));
        rdb.insert(User.class).runBatch(a, b, c);
        rdb.setEntityListener(new StandardEntityListener());
        // update:
        a.name = "Changed";
        rdb.update(User.class)
            .run(a);
        double now = System.currentTimeMillis();
        // select
        User a2 = rdb.select().from(User.class).byId(a.id);
        assertEquals("Changed", a2.name);
        assertEquals(getCreatedTime(), a2.createdAt.getTime());
        assertEquals(now, a2.updatedAt.getTime(), DELTA_3_SECONDS);
        assertEquals(2, a2.version);
        
        // update b:
        b.name = "Also Changed";
        rdb.update(User.class)
            .fieldsOnly("name", "version")
            .run(b);
        // select again:
        User b2 = rdb.select().from(User.class).byId(b.id);
        assertEquals("Also Changed", b2.name);
        assertEquals((double) getCreatedTime(), b2.createdAt.getTime(), DELTA_3_SECONDS);
        // updatedAt not change:
        assertEquals((double) getCreatedTime(), b2.updatedAt.getTime(), DELTA_3_SECONDS);
        // version updated:
        assertEquals(2, b2.version);
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
