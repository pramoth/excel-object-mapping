/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package com.blogspot.na5cent.exom.converter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

/**
 * Converter for the date type
 * 
 * @author edwin.njeru
 *
 */
public class DateTypeConverter implements TypeConverter<Date> {
	
	private static final Logger LOG = LoggerFactory.getLogger(DateTypeConverter.class);
	
	/**
	 * Converts object to java.util.Date type
	 * 
	 * @param value
	 * @param pattern
	 * @return
	 */
	@Override
	public Date convert(final Optional<Object> value, final String... pattern) {
		
		final Object convertible = value.get();
		
		LOG.trace("Convert has been called with args {} and {}", convertible, pattern);
		
		final Calendar calendar = Calendar.getInstance();
		if (convertible instanceof Timestamp) {
			calendar.setTimeInMillis(((Timestamp) convertible).getTime());
			LOG.trace("Convert object construed to be instance of : {}", Timestamp.class);
		} else if (convertible instanceof Date) {
			
			calendar.setTimeInMillis(((Date) convertible).getTime());
			
			LOG.trace("Convert object construed to be instance of : {}", Date.class);
		} else if (convertible instanceof String) {
			
			LOG.trace("Convert object probably an instance of : {}", String.class);
			
			try {
				
				LOG.trace("Attempting to Parse using the format : {}", pattern);
				
				return new SimpleDateFormat(pattern[0]).parse((String) convertible);
				
			} catch (final Exception ex1) {
				
				ex1.printStackTrace();
				
				LOG.error("{} While trying to parse date object{} using the patter {}. " + "Caused by : {}",
						ex1.getMessage(), convertible, pattern, ex1.getCause());
				return null;
			}
		} else {
			
			LOG.warn("The date object {} is not an instance of TimeStsmp,java.util.Date, or string"
					+ "and cannot be converted to date", convertible);
			return null;
		}
		
		return calendar.getTime();
	}
	
}
