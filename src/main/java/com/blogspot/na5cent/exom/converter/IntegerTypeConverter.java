/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package com.blogspot.na5cent.exom.converter;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

/**
 * @author redcrow
 */
public class IntegerTypeConverter implements TypeConverter<Integer> {
	
	private static final Logger LOG = LoggerFactory.getLogger(IntegerTypeConverter.class);
	
	/**
	 * Converts integer object to integer
	 * 
	 * @param rawInteger
	 * @param pattern
	 * @return
	 */
	@Override
	public Integer convert(final Optional<Object> rawInteger, final String... pattern) {
		
		Object value = null;
		
		if(rawInteger.isPresent()) value = rawInteger.get();
		
		if (value instanceof Integer) { return (Integer) value; }
		
		if (value instanceof String) {
			try {
				return Integer.valueOf(((String) value).trim());
			} catch (final Exception ex) {
				LOG.warn(null, ex);
				return null;
			}
		}
		
		if (value instanceof BigDecimal) { return ((BigDecimal) value).intValue(); }
		
		return null;
	}
	
}
