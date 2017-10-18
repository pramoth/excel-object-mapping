/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package io.github.mahmoudi.converter;

import java.math.BigDecimal;

/**
 * @author redcrow
 */
public class BooleanTypeConverter implements TypeConverter<Boolean> {

    @Override
    public Boolean convert(Object value, String... pattern) {
        if (value == null) {
            return Boolean.FALSE;
        }

        if (value instanceof Boolean) {
            return (Boolean) value;
        }

        if (value instanceof Integer) {
            //try {
                value = value.toString().trim();
            //} catch (Exception ex) {
                //return Boolean.FALSE;
            //}
        }

        if (value instanceof String) {
            //try {
			if (((String) value).trim().equals("false")) {
				return Boolean.FALSE;
			} else if (((String) value).trim().equals("true")) {
				return Boolean.TRUE;
			} else {
				value = BigDecimal.valueOf(Long.parseLong(((String) value).trim()));
			}
            //} catch (Exception ex) {
                //return Boolean.FALSE;
            //}
        }

        if (value instanceof BigDecimal) {
            BigDecimal bd = (BigDecimal) value;
            int intValue = bd.intValue();
            return intValue == 1
                    ? Boolean.TRUE
                    : Boolean.FALSE;
        }

        return Boolean.FALSE;
    }

}
