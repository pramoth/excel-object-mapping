/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package com.blogspot.na5cent.exom.converter;

import java.math.BigDecimal;

import com.google.common.base.Optional;


/**
 * Implements the TypeConverter interface and the method convert is only called when a boolena value 
 * is encountered
 * @author edwin.njeru
 *
 */
public class BooleanTypeConverter implements TypeConverter<Boolean> {
	
	
	/**
	 * Converts value to boolean
	 * @param value
	 * @param pattern
	 * @return Boolean value
	 */
	@Override
	public Boolean convert(final Optional<Object> value, final String... pattern) {
		
		Object booleanObject = null;
		
		if(value.isPresent()) booleanObject = value.get();
		
		if (booleanObject instanceof Boolean) { return (Boolean) booleanObject; }
		
		if (booleanObject instanceof Integer) {
			try {
				booleanObject = booleanObject.toString().trim();
			} catch (final Exception ex) {
				return Boolean.FALSE;
			}
		}
		
		if (booleanObject instanceof String) {
			try {
				booleanObject = BigDecimal.valueOf(Long.parseLong(((String) booleanObject).trim()));
			} catch (final Exception ex) {
				return Boolean.FALSE;
			}
		}
		
		if (booleanObject instanceof BigDecimal) {
			final BigDecimal bd = (BigDecimal) booleanObject;
			final int intValue = bd.intValue();
			return intValue == 1 ? Boolean.TRUE : Boolean.FALSE;
		}
		
		return Boolean.FALSE;
	}
	
}
