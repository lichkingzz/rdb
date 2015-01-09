package com.itranswarp.rdb;

import java.sql.Timestamp;

public class AbstractEntity {

    public long id;

    public Timestamp createdAt;
    public Timestamp updatedAt;
    public long version;

}
