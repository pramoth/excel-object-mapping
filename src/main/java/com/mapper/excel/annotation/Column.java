/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package com.mapper.excel.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author redcrow
 * @author Mohsen.Mahmoudi </br>
 *         set <b>name</b> of column like <code>@Column(name = "abcd")</code> or
 *         <b>index</b> of column like <code>@Column(index = 0)</code> based on
 *         requirement. <b>Be careful, order of name is higher than
 *         index.</b>
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
public @interface Column {

	String name() default "";

	int index() default 0;

	String pattern() default "";

}
