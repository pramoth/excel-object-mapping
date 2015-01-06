/*
 * code https://github.com/jittagornp/excel-object-mapping
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
