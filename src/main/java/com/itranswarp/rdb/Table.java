package com.itranswarp.rdb;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define table name of the entity.
 * 
 * @author Michael Liao
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    /**
     * Table name of the entity.
     * 
     * @return Table name.
     */
    String value();

}
