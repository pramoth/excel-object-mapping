/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package com.blogspot.na5cent.exom.converter;

import com.google.common.base.Optional;

/**
 * @author redcrow
 */
public class StringTypeConverter implements TypeConverter<String> {
	
	/**
	 * Converts stringObject to string
	 * 
	 * @param stringObject
	 * @param pattern
	 * @return
	 */
	@Override
	public String convert(final Optional<Object> stringObject, final String... pattern) {
		
		Object value = null;
		
		if(stringObject.isPresent()) value = stringObject.get();
		
		if (value instanceof String) { return ((String) value).trim(); }
		
		return null;
	}
	
}
