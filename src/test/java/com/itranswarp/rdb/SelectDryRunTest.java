package com.itranswarp.rdb;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

/**
 * Test select.
 */
public class SelectDryRunTest {

    Rdb newRdb() {
        return new Rdb();
    }

    @Test
    public void testSelectFromString() {
        assertEquals("SELECT * FROM student",
                newRdb().select()
                        .from("student")
                        .dryRun());

        assertEquals("SELECT * FROM student",
                newRdb().select("*")
                        .from("student")
                        .dryRun());

        assertEquals("SELECT id, name, grade FROM student",
                newRdb().select("id", "name", "grade")
                        .from("student")
                        .dryRun());

        assertEquals("SELECT * FROM student WHERE id=?",
                newRdb().select()
                        .from("student")
                        .where("id=?", 123)
                        .dryRun());

        assertEquals("SELECT * FROM student WHERE s>=? and g<?",
                newRdb().select()
                        .from("student")
                        .where("s>=? and g<?", 123, 456)
                        .dryRun());

        assertEquals("SELECT * FROM student LIMIT 0, 100",
                newRdb().select()
                        .from("student")
                        .limit(100)
                        .dryRun(true));

        assertEquals("SELECT * FROM student LIMIT ?, ?",
                newRdb().select()
                        .from("student")
                        .limit(10, 100)
                        .dryRun());

        assertEquals("SELECT * FROM student WHERE s>=? and g<? LIMIT ?, ?",
                newRdb().select()
                        .from("student")
                        .where("s>=? and g<?", 123, 456)
                        .limit(10, 100)
                        .dryRun());

        assertEquals("SELECT * FROM student ORDER BY score DESC",
                newRdb().select()
                        .from("student")
                        .orderBy("score").desc()
                        .dryRun());

        assertEquals("SELECT * FROM student ORDER BY score DESC LIMIT ?, ?",
                newRdb().select()
                        .from("student")
                        .orderBy("score").desc()
                        .limit(10, 100)
                        .dryRun());

        assertEquals("SELECT * FROM student WHERE s>=? and g<? ORDER BY score DESC, name LIMIT ?, ?",
                newRdb().select()
                        .from("student")
                        .where("s>=? and g<?", 123, 456)
                        .orderBy("score").desc()
                        .orderBy("name")
                        .limit(10, 100)
                        .dryRun());
    }

    @Test
    public void testSelectFromStringWithArgs() {
        assertEquals("SELECT * FROM student",
                newRdb().select()
                        .from("student")
                        .dryRun(true));

        assertEquals("SELECT * FROM student",
                newRdb().select("*")
                        .from("student")
                        .dryRun(true));

        assertEquals("SELECT id, name, grade FROM student",
                newRdb().select("id", "name", "grade")
                        .from("student")
                        .dryRun(true));

        assertEquals("SELECT * FROM student WHERE id=123",
                newRdb().select()
                        .from("student")
                        .where("id=?", 123)
                        .dryRun(true));

        assertEquals("SELECT * FROM student WHERE s>=123 and g<456",
                newRdb().select()
                        .from("student")
                        .where("s>=? and g<?", 123, 456)
                        .dryRun(true));

        assertEquals("SELECT * FROM student LIMIT 0, 100",
                newRdb().select()
                        .from("student")
                        .limit(100)
                        .dryRun(true));

        assertEquals("SELECT * FROM student LIMIT 10, 100",
                newRdb().select()
                        .from("student")
                        .limit(10, 100)
                        .dryRun(true));

        assertEquals("SELECT * FROM student WHERE s>=\'A\' and g<\'B\' LIMIT 10, 100",
                newRdb().select()
                        .from("student")
                        .where("s>=? and g<?", "A", "B")
                        .limit(10, 100)
                        .dryRun(true));

        assertEquals("SELECT * FROM student ORDER BY score DESC",
                newRdb().select()
                        .from("student")
                        .orderBy("score").desc()
                        .dryRun(true));

        assertEquals("SELECT * FROM student ORDER BY score DESC LIMIT 10, 100",
                newRdb().select()
                        .from("student")
                        .orderBy("score").desc()
                        .limit(10, 100)
                        .dryRun(true));

        assertEquals("SELECT * FROM student WHERE s=1 and g<\'2015-01-05\' ORDER BY score DESC, name LIMIT 10, 100",
                newRdb().select()
                        .from("student")
                        .where("s=? and g<?", true, LocalDate.of(2015, 1, 5))
                        .orderBy("score").desc()
                        .orderBy("name")
                        .limit(10, 100)
                        .dryRun(true));
    }

    @Test
    public void testSelectFromBean() {
        assertEquals("SELECT * FROM User",
                newRdb().select()
                        .from(User.class)
                        .dryRun());

        assertEquals("SELECT * FROM User",
                newRdb().select("*")
                        .from(User.class)
                        .dryRun());

        assertEquals("SELECT id, name, grade FROM User",
                newRdb().select("id", "name", "grade")
                        .from(User.class)
                        .dryRun());

        assertEquals("SELECT * FROM User WHERE id=?",
                newRdb().select()
                        .from(User.class)
                        .where("id=?", 123)
                        .dryRun());

        assertEquals("SELECT * FROM User WHERE s>=? and g<?",
                newRdb().select()
                        .from(User.class)
                        .where("s>=? and g<?", 123, 456)
                        .dryRun());

        assertEquals("SELECT name FROM User WHERE s>=? and g<?",
                newRdb().select()
                        .from(User.class)
                        .excludeFields("id", "email", "password", "gender", "aboutMe", "birth", "lastLoginAt", "createdAt", "updatedAt", "version")
                        .where("s>=? and g<?", 123, 456)
                        .dryRun());

        assertEquals("SELECT name FROM User WHERE s>=? and g<?",
                newRdb().select(" * ")
                        .from(User.class)
                        .excludeFields("id", "email", "password", "gender", "aboutMe", "birth", "lastLoginAt", "createdAt", "updatedAt", "version")
                        .where("s>=? and g<?", 123, 456)
                        .dryRun());

        assertEquals("SELECT * FROM User LIMIT 0, 100",
                newRdb().select()
                        .from(User.class)
                        .limit(100)
                        .dryRun(true));

        assertEquals("SELECT * FROM User LIMIT ?, ?",
                newRdb().select()
                        .from(User.class)
                        .limit(10, 100)
                        .dryRun());

        assertEquals("SELECT * FROM User WHERE s>=? and g<? LIMIT ?, ?",
                newRdb().select()
                        .from(User.class)
                        .where("s>=? and g<?", 123, 456)
                        .limit(10, 100)
                        .dryRun());

        assertEquals("SELECT * FROM User ORDER BY score DESC",
                newRdb().select()
                        .from(User.class)
                        .orderBy("score").desc()
                        .dryRun());

        assertEquals("SELECT * FROM User ORDER BY score DESC LIMIT ?, ?",
                newRdb().select()
                        .from(User.class)
                        .orderBy("score").desc()
                        .limit(10, 100)
                        .dryRun());

        assertEquals("SELECT * FROM User WHERE s>=? and g<? ORDER BY score DESC, name LIMIT ?, ?",
                newRdb().select()
                        .from(User.class)
                        .where("s>=? and g<?", 123, 456)
                        .orderBy("score").desc()
                        .orderBy("name")
                        .limit(10, 100)
                        .dryRun());
    }

}
