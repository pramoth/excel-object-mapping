/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blogspot.na5cent.exom.converter;

/**
 * @author redcrow
 */
public class StringTypeConverter implements TypeConverter<String> {

    @Override
    public String convert(Object value, String... pattern) {
        if (value instanceof String) {
            return ((String) value).trim();
        }
        
        return null;
    }

}
