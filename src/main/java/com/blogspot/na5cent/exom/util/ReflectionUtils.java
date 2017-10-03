/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package com.blogspot.na5cent.exom.util;

import static com.blogspot.na5cent.exom.util.CollectionUtils.isEmpty;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blogspot.na5cent.exom.annotation.ExcelColumn;
import com.blogspot.na5cent.exom.converter.TypeConverter;
import com.blogspot.na5cent.exom.converter.TypeConverters;
import com.google.common.base.Optional;

/**
 * @author redcrow
 */
public class ReflectionUtils {
	
	private static final Logger LOG = LoggerFactory.getLogger(ReflectionUtils.class);
	
	/**
	 * This method is used to create a setter function. In most scenarios only the first 
	 * character needs to get capitaliSed
	 * 
	 * @param str
	 * @return
	 */
	public static String toUpperCaseFirstCharacter(final String str) {
		
		LOG.trace("Converting param : '{}' to pascal case",str);
		
		String pascalCase = StringUtils.capitalize(str);
		
		LOG.trace("{} converted to pascal case : {}",str,pascalCase);
		
		return pascalCase;
	}
	
	public static void setValueOnField(final Object instance, final Field field, final Object value){
		
		LOG.trace("setValueOnField called with args : {}, {},{}",instance,field,value);
		
		final Class<? extends Object> clazz = instance.getClass();
		
		LOG.trace("{} class derived from {}",clazz,instance);
		
		final String setMethodName = "set" + toUpperCaseFirstCharacter(field.getName());
		
		LOG.trace("Method name set to : {}",setMethodName);
		
		for (final Entry<Class<?>, TypeConverter<?>> entry : TypeConverters.getConverter().entrySet()) {
			
			if (field.getType().equals(entry.getKey())) {
				
				Method method = null;
				
				try {
					
					method = clazz.getDeclaredMethod(setMethodName, entry.getKey());
					
					LOG.trace("Method name : {} has been created " ,method.toString());
					
				} catch (NoSuchMethodException e) {
					
					LOG.error("{} Exception caused by {} Ocurring at {} ",e.getMessage(),e.getCause(),e.getStackTrace());
					e.printStackTrace();
				} catch (SecurityException e) {
					LOG.error("{} Exception caused by {} Ocurring at {} ",e.getMessage(),e.getCause(),e.getStackTrace());
					e.printStackTrace();
				}
				
				final ExcelColumn column = field.getAnnotation(ExcelColumn.class);
				
				try {
					
					LOG.trace("Invoking method : {} with args : {} and entry : {}",method.getName(),instance.toString(),entry.getValue());
					
					method.invoke(instance,
							entry.getValue().convert(Optional.of(value), column == null ? null : column.pattern()));
				} catch (IllegalAccessException e) {
					LOG.error("{} Exception caused by {} Ocurring at {} when invoking method : {}",
							e.getMessage(),e.getCause(),e.getStackTrace(),method.getName());
				} catch (IllegalArgumentException e) {
					LOG.error("{} Exception caused by {} Ocurring at {} when invoking method : {}",
							e.getMessage(),e.getCause(),e.getStackTrace(),method.getName());
				} catch (InvocationTargetException e) {
					LOG.error("{} Exception caused by {} Ocurring at {} when invoking method : {}",
							e.getMessage(),e.getCause(),e.getStackTrace(),method.getName());
				}
			}
		}
	}
	
	public static void eachFields(final Class<?> clazz, final EachFieldCallback callback){
		
		LOG.trace("eachFields method called with args : {}, and {}",clazz, callback);
		
		final Field[] fields = clazz.getDeclaredFields();
		
		LOG.trace("Fields : {} created from clazz {}",fields,clazz );
		
		if (!isEmpty(fields)) {
			
			for (final Field field : fields) {
				
				try {
					callback.each(
							field, 
							field.isAnnotationPresent(ExcelColumn.class) ? field.getAnnotation(ExcelColumn.class).name() : field.getName());
				} catch (Throwable e) {
					
					LOG.error("{} Exception caused by {}, Ocurrint at : {} when setting the field {}",
							e.getMessage(),e.getCause(),e.getStackTrace(), field.getName());
				}
				
			}
		}
	}
}
