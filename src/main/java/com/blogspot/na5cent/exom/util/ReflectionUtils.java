/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blogspot.na5cent.exom.util;

import com.blogspot.na5cent.exom.annotation.Column;
import com.blogspot.na5cent.exom.converter.TypeConverter;
import com.blogspot.na5cent.exom.converter.TypeConverters;
import static com.blogspot.na5cent.exom.util.CollectionUtils.isEmpty;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author redcrow
 */
public class ReflectionUtils {
    
    private static final Logger LOG = LoggerFactory.getLogger(ReflectionUtils.class);

    private static String toUpperCaseFirstCharacter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static void setValueOnField(Object instance, Field field, Object value) throws Exception {
        Class clazz = instance.getClass();
        String setMethodName = "set" + toUpperCaseFirstCharacter(field.getName());

        for (Map.Entry<Class, TypeConverter> entry : TypeConverters.getConverter().entrySet()) {
            if (field.getType().equals(entry.getKey())) {
                Method method = clazz.getDeclaredMethod(setMethodName, entry.getKey());
                Column column = field.getAnnotation(Column.class);
                        
                method.invoke(
                        instance,
                        entry.getValue().convert(
                                value,
                                column == null ? null : column.datePattern()
                        )
                );
            }
        }
    }

    public static void eachFields(Class clazz, EachFieldCallback callback) throws Throwable {
        Field[] fields = clazz.getDeclaredFields();
        if (!isEmpty(fields)) {
            for (Field field : fields) {
                callback.each(
                        field,
                        field.isAnnotationPresent(Column.class)
                        ? field.getAnnotation(Column.class).name()
                        : field.getName()
                );
            }
        }
    }
}
