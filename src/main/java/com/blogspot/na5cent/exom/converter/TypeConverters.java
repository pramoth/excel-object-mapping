/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package com.blogspot.na5cent.exom.converter;

import java.util.Date;
import java.util.Map;

import org.eclipse.collections.impl.map.mutable.UnifiedMap;

/**
 * @author redcrow
 */
public class TypeConverters {
	
	private static Map<Class<?>, TypeConverter<?>> converter;
	
	private static void registerConverter() {
		converter = new UnifiedMap<Class<?>, TypeConverter<?>>();
		converter.put(String.class, new StringTypeConverter());
		converter.put(Integer.class, new IntegerTypeConverter());
		converter.put(Double.class, new DoubleTypeConverter());
		converter.put(Boolean.class, new BooleanTypeConverter());
		converter.put(Date.class, new DateTypeConverter());
	}
	
	public static Map<Class<?>, TypeConverter<?>> getConverter() {
		if (converter == null) {
			registerConverter();
		}
		
		return converter;
	}
}
