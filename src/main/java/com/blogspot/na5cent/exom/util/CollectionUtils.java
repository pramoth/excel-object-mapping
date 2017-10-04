/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package com.blogspot.na5cent.exom.util;

import java.util.Collection;

/**
 * @author redcrow
 */
public class CollectionUtils {
	
	public static boolean isEmpty(final Collection<?> collection) {
		return (collection == null) || collection.isEmpty();
	}
	
	public static boolean isEmpty(final Object[] object) {
		return (object == null) || (object.length < 1);
	}
}
