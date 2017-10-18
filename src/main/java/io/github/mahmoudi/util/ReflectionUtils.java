/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package io.github.mahmoudi.util;

import static io.github.mahmoudi.util.CollectionUtils.isEmpty;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mahmoudi.annotation.Column;
import io.github.mahmoudi.converter.TypeConverter;
import io.github.mahmoudi.converter.TypeConverters;

/**
 * @author redcrow
 * @author Mohsen.Mahmoudi
 */
public class ReflectionUtils {

	private static final Logger LOG = LoggerFactory.getLogger(ReflectionUtils.class);

	private static String toUpperCaseFirstCharacter(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public static void setValueOnField(Object instance, Field field, Object value) throws Exception {
		Class<?> clazz = instance.getClass();
		String setMethodName = "set" + toUpperCaseFirstCharacter(field.getName());

		for (Map.Entry<Class, TypeConverter> entry : TypeConverters.getConverter().entrySet()) {
			if (field.getType().equals(entry.getKey())) {
				Method method = clazz.getDeclaredMethod(setMethodName, entry.getKey());
				Column column = field.getAnnotation(Column.class);

				method.invoke(instance, entry.getValue().convert(value, column == null ? null : column.pattern()));
			}
		}
	}

	public static void eachFields(Class<?> clazz, EachFieldCallback callback) throws Throwable {
		Field[] fields = clazz.getDeclaredFields();
		if (!isEmpty(fields)) {
			for (Field field : fields) {
				if (field.isAnnotationPresent(Column.class)) {
					if (!field.getAnnotation(Column.class).name().isEmpty()) {
						callback.each(field, field.getAnnotation(Column.class).name(), null);
					} else {
						callback.each(field, null, field.getAnnotation(Column.class).index());
					}
				} else {
					callback.each(field, field.getName(), null);
				}
			}
		}
	}

	public static Field mapNameToField(Class<?> clazz, String name) throws Throwable {
		Field[] fields = clazz.getDeclaredFields();
		if (!isEmpty(fields)) {
			for (Field field : fields) {
				if (field.getName().equals(name)) {
					return field;
				}
			}
		}
		throw new Exception("Error -- " + name + " Property of Class is not Found...");
	}
}
