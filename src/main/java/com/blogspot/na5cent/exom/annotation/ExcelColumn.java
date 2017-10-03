/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package com.blogspot.na5cent.exom.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation uses the name variable to describe the name of the column of 
 * the excel file to be read. The name must be the same with the name displayed in the 
 * column
 * As for java.util.Date field a pattern must be provided for instance pattern = "dd/MM/yyyy"
 * 
 * Please note : If the field being mapped is a primitive, it MUST be wrapped in its corresponding
 * object by autoboxing.
 * 
 * Currently only the following types are supported : 
 * a) Boolean
 * b) Date (java.util.Date)
 * c) Double
 * d) Integer
 * e) String
 * 
 * @author edwin.njeru
 * @author redcrow
 *
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
public @interface ExcelColumn {
	
	String name() default "";
	
	String pattern() default "";
}
