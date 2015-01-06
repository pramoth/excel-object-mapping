/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blogspot.na5cent.exom.converter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author redcrow
 */
public class TypeConverters {

    private static Map<Class, TypeConverter> converter;

    private static void registerConverter() {
        converter = new HashMap<>();
        converter.put(String.class, new StringTypeConverter());
        converter.put(Integer.class, new IntegerTypeConverter());
        converter.put(Boolean.class, new BooleanTypeConverter());
        converter.put(Date.class, new DateTypeConverter());
    }

    public static Map<Class, TypeConverter> getConverter() {
        if (converter == null) {
            registerConverter();
        }

        return converter;
    }
}
