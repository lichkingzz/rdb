package com.itranswarp.rdb;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Rdb rdb = new Rdb();
        rdb.select()
           .from("user")
           .where("id=?", 123)
           .orderBy("name").desc()
           .orderBy("createdAt")
           .orderBy("id").desc()
           .limit(10)
           .list();

        int rows = rdb.update("user")
           .set("name", "newName")
           .set("version", 1)
           .where("createdAt >= ?", 20150101)
           .run();
        int deleted = rdb.delete("user")
                         .where("createdAt < ?", 109323L)
                         .run();
        // generic:
        List<User> users = rdb.select("id", "email", "password")
                .from(User.class)
                .where("id=?", 123)
                .orderBy("name").desc()
                .orderBy("createdAt")
                .orderBy("id").desc()
                .limit(10)
                .list();
        User a = rdb.select()
                .from(User.class)
                .excludeFields("password")
                .where("id=?", 123)
                .first();
        User b = rdb.select()
                .from(User.class)
                .byId(123);

        User u = new User();
        rdb.update(u)
           .fieldsOnly("name", "updatedAt")
           .run();

        rdb.insert(u, a, b).run();
        rdb.delete(u).run();
    }
}
