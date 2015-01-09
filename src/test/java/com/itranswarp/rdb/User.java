package com.itranswarp.rdb;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class User extends AbstractEntity {

    public String email;

    public String passwd;

    public String name;

    public boolean gender;

    public String aboutMe;

    public LocalDate birth;

    public LocalDateTime lastLoginAt;

    transient boolean locked;

    @Transient
    public boolean isLocked() {
        return false;
    }
}
