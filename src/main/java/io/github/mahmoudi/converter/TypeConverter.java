/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package io.github.mahmoudi.converter;

/**
 * @author redcrow
 */
public interface TypeConverter<T> {

    T convert(Object value, String... pattern);
}
