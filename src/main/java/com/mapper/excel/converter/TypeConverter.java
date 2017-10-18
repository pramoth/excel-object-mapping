/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package com.mapper.excel.converter;

/**
 * @author redcrow
 */
public interface TypeConverter<T> {

    T convert(Object value, String... pattern);
}
