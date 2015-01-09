package com.itranswarp.rdb.insert;

import static org.junit.Assert.*;

import org.junit.Test;

import com.itranswarp.rdb.Rdb;

/**
 * Test insert.
 */
public class InsertDryRunTest {

    Rdb newRdb() {
        return new Rdb();
    }

    @Test
    public void testInsertFromString() {
        assertEquals("INSERT INTO student (id) VALUES (?)",
                newRdb().insert("student")
                        .set("id", 123)
                        .dryRun());

        assertEquals("INSERT INTO student (id, name) VALUES (?, ?)",
                newRdb().insert("student")
                        .set("id", 123)
                        .set("name", "Bob")
                        .dryRun());

        assertEquals("INSERT INTO student (id, name, gender) VALUES (?, ?, ?)",
                newRdb().insert("student")
                        .set("id", 123)
                        .set("name", "Bob")
                        .set("gender", true)
                        .dryRun());
    }

    @Test
    public void testInsertFromStringWithArgs() {
        assertEquals("INSERT INTO student (id) VALUES (123)",
                newRdb().insert("student")
                        .set("id", 123)
                        .dryRun(true));

        assertEquals("INSERT INTO student (id, name) VALUES (123, 'Bob')",
                newRdb().insert("student")
                        .set("id", 123)
                        .set("name", "Bob")
                        .dryRun(true));

        assertEquals("INSERT INTO student (id, name, gender) VALUES (123, 'Bob', 1)",
                newRdb().insert("student")
                        .set("id", 123)
                        .set("name", "Bob")
                        .set("gender", true)
                        .dryRun(true));
    }

}
