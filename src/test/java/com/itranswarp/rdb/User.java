package com.itranswarp.rdb;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class User extends AbstractEntity {

    long id;

    String email;

    String passwd;

    String name;

    boolean gender;

    String aboutMe;

    LocalDate birth;

    LocalDateTime lastLoginAt;

    transient boolean locked;

    @Transient
    public boolean isLocked() {
        return false;
    }
}
