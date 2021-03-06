rdb
===

Simple, lightweight and DSL-driven library to operate RDBMS.

Rdb does not re-invent SQL-like query. It just uses the SQL-style with chained calls.

Let's see how simple to query with "SQL":

```
// each row contains a Map:
List<Map<string, Object>> rows = rdb.select("id", "name", "lastLoginAt")
                  .from("users")
                  .where("lastLoginAt > ?", loginDate)
                  .orderBy("id")
                  .orderBy("name").desc()
                  .list();
```

Cool! But Rdb can do more! You can query with JavaBeans, and it is type-safe:

```
// each row contains a User object:
List<User> users = rdb.select()
                      .from(User.class)
                      .where("lastLoginAt > ?", loginDate)
                      .orderBy("name").desc()
                      .list();
```

It is a simple ORM but does not have "ORM" magic. There is no magic object: 
you just get Java beans you defined. It is no proxied nor cglibed. Say goodbye 
to StaleObjectException.

Read more on [http://rdb.itranswarp.com](http://rdb.itranswarp.com).
