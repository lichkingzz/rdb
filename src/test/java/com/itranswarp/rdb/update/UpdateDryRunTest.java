package com.itranswarp.rdb.update;

import static org.junit.Assert.*;

import org.junit.Test;

import com.itranswarp.rdb.Rdb;

/**
 * Test update.
 */
public class UpdateDryRunTest {

    Rdb newRdb() {
        return new Rdb();
    }

    @Test
    public void testUpdateTableDryRun() {
        assertEquals("UPDATE student SET name=?",
                newRdb().update("student")
                        .set("name", "Bob")
                        .dryRun());

        assertEquals("UPDATE student SET name=?, age=?",
                newRdb().update("student")
                        .set("name", "Bob")
                        .set("age", 12)
                        .dryRun());

        assertEquals("UPDATE student SET name=? WHERE id=?",
                newRdb().update("student")
                        .set("name", "Bob")
                        .where("id=?", 12345)
                        .dryRun());

        assertEquals("UPDATE student SET name=?, age=? WHERE id=?",
                newRdb().update("student")
                        .set("name", "Bob")
                        .set("age", 12)
                        .where("id=?", 12345)
                        .dryRun());
    }

    @Test
    public void testUpdateTableDryRunWithParams() {
        assertEquals("UPDATE student SET name=\'Bob\'",
                newRdb().update("student")
                        .set("name", "Bob")
                        .dryRun(true));

        assertEquals("UPDATE student SET name=\'Bob\', age=12",
                newRdb().update("student")
                        .set("name", "Bob")
                        .set("age", 12)
                        .dryRun(true));

        assertEquals("UPDATE student SET name=\'Bob\' WHERE id=12345",
                newRdb().update("student")
                        .set("name", "Bob")
                        .where("id=?", 12345)
                        .dryRun(true));

        assertEquals("UPDATE student SET name=\'Bob\', age=12 WHERE id=12345",
                newRdb().update("student")
                        .set("name", "Bob")
                        .set("age", 12)
                        .where("id=?", 12345)
                        .dryRun(true));
    }

}
