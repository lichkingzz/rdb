package com.itranswarp.rdb;

import java.time.LocalDate;

public class User extends AbstractEntity {

    @Id
    String id;

    String name;

    String email;

    String password;

    boolean activate;

    LocalDate birth;

    transient boolean locked;
}
