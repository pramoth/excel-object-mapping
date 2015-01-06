/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blogspot.na5cent.exom.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 * @author redcrow
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
public @interface Column {

    String name() default "";

    String datePattern() default "yyyy-MM-ddd";
}
