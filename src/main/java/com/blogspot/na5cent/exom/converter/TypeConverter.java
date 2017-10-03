/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package com.blogspot.na5cent.exom.converter;

import com.google.common.base.Optional;

/**
 * TypeConverter interface has only one method which is used to convert a given value
 * into a java type
 * 
 * @author redcrow
 */
public interface TypeConverter<T> {
	
	/**
	 * Create a java type using value object and a string pattern for java.util.Date type
	 * conversion
	 * Currently only the following types are supported, Boolean, java.util.Date,
	 * Double,Integer and String
	 * All these types must be wrapped into their respective objects for the corresponding
	 * primitives inorder for this library to parse them into the object
	 * 
	 * @param value
	 * @param pattern
	 * @return
	 */
	T convert(Optional<Object> value, String... pattern);
}
