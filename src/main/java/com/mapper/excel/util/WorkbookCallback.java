/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package com.mapper.excel.util;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author Mohsen.Mahmoudi
 */
public interface WorkbookCallback {

	void getWorkbook(Workbook workbook) throws Throwable;
}
