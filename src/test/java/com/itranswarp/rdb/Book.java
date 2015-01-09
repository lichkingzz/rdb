package com.itranswarp.rdb;

import java.time.LocalDate;

public class Book extends AbstractEntity {

    long id;

    long author_id;

    String name;

    String description;

    LocalDate publishAt;

}
