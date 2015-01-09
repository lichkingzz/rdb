package com.itranswarp.rdb.insert;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.itranswarp.rdb.DbTestBase;
import com.itranswarp.rdb.User;

/**
 * Test insert.
 */
public class InsertTest extends DbTestBase {

    @Test
    public void testInsertFromString() throws Exception {
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
    public void testInsertFromStringWithArgs() {
    }

}
