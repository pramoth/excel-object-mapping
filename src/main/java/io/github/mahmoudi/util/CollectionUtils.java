/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package io.github.mahmoudi.util;

import java.util.Collection;

/**
 * @author redcrow
 */
public class CollectionUtils {

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Object[] object) {
        return object == null || object.length < 1;
    }
}
