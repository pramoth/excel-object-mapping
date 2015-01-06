/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blogspot.na5cent.exom.converter;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author redcrow
 */
public class IntegerTypeConverter implements TypeConverter<Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(IntegerTypeConverter.class);
    
    @Override
    public Integer convert(Object value, String... pattern) {
        if (value == null) {
            return null;
        }

        if (value instanceof Integer) {
            return (Integer) value;
        }

        if (value instanceof String) {
            try {
                return Integer.valueOf(((String) value).trim());
            } catch (Exception ex) {
                LOG.warn(null, ex);
                return null;
            }
        }

        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).intValue();
        }

        return null;
    }

}
