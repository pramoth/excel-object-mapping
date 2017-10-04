/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package com.blogspot.na5cent.exom.converter;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

/**
 * Converts double object to Double
 * 
 * @author edwin.njeru
 *
 */
public class DoubleTypeConverter implements TypeConverter<Double> {
	
	private static final Logger LOG = LoggerFactory.getLogger(DoubleTypeConverter.class);
	
	/**
	 * Converts object to double
	 * 
	 * @param doubleObject
	 * @param pattern
	 * @return
	 */
	@Override
	public Double convert(final Optional<Object> doubleObject, final String... pattern) {
		
		Object value = null;
		
		if(doubleObject.isPresent()) value = doubleObject.get();
		
		if (value instanceof Double) { return (Double) value; }
		
		if (value instanceof String) {
			try {
				return Double.valueOf(((String) value).trim());
			} catch (final Exception ex) {
				LOG.warn(null, ex);
				return null;
			}
		}
		
		if (value instanceof BigDecimal) { return ((BigDecimal) value).doubleValue(); }
		
		return null;
	}
	
}
