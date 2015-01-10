package com.itranswarp.rdb.delete;

import static org.junit.Assert.*;

import org.junit.Test;

import com.itranswarp.rdb.Rdb;

/**
 * Test delete.
 */
public class DeleteDryRunTest {

    Rdb newRdb() {
        return new Rdb();
    }

    @Test
    public void testDeleteTableDryRun() {
        assertEquals("DELETE FROM student",
                newRdb().delete("student")
                        .dryRun());

        assertEquals("DELETE FROM student WHERE name=?",
                newRdb().delete("student")
                        .where("name=?", "Bob")
                        .dryRun());

        assertEquals("DELETE FROM student WHERE name is null",
                newRdb().delete("student")
                        .where("name is null")
                        .dryRun());

        assertEquals("DELETE FROM student WHERE score>? and grade<?",
                newRdb().delete("student")
                        .where("score>? and grade<?", 60, "A")
                        .dryRun());
    }

}
