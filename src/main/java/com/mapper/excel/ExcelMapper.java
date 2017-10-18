/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package com.mapper.excel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mapper.excel.util.EachFieldCallback;
import com.mapper.excel.util.ReflectionUtils;
import com.mapper.excel.util.WorkbookCallback;

/**
 * @author redcrow
 * 
 * @author Mohsen.Mahmoudi
 * 
 */
public class ExcelMapper {

	private static final Logger LOG = LoggerFactory.getLogger(ExcelMapper.class);

	private Class<?> clazz;
	private final Workbook workbook;

	private int[] sheet;
	private boolean handleCellExceptions = false;
	private boolean mustMapRowThatCellsHaveException = false;
	private boolean cellHasException = false;
	private List listExceptions;
	private WorkbookCallback callback;
	private CellStyle cellExceptionBackground;
	private Map<String, Integer> fieldToColumnIndex = new HashMap<>();
	private Integer headerRowNumber = 0;
	private Integer startRowNumber;
	private Integer endRowNumber;

	private ExcelMapper(File excelFile) throws Throwable {
		this.workbook = WorkbookFactory.create(excelFile);
	}

	private ExcelMapper(byte[] excelByteFile) throws Throwable {
		this.workbook = WorkbookFactory.create(new ByteArrayInputStream(excelByteFile));
	}

	public static ExcelMapper mapFromExcel(File excelFile) throws Throwable {
		return new ExcelMapper(excelFile);
	}

	public static ExcelMapper mapFromExcel(byte[] excelByteFile) throws Throwable {
		return new ExcelMapper(excelByteFile);
	}

	public ExcelMapper toObjectOf(Class<?> clazz) {
		this.clazz = clazz;
		return this;
	}

	public ExcelMapper mapFieldFrom(Map<String, Integer> fieldToColumnIndex) {
		this.fieldToColumnIndex = fieldToColumnIndex;
		return this;
	}

	public ExcelMapper fromSheet(int... sheetNumbers) {
		this.sheet = sheetNumbers;
		return this;
	}

	public ExcelMapper handleCellExceptions() {
		this.handleCellExceptions = true;
		return this;
	}

	public ExcelMapper mapRowThatCellsHaveException() {
		this.mustMapRowThatCellsHaveException = true;
		return this;
	}

	public ExcelMapper getAllExceptions(List listExceptions) {
		this.listExceptions = listExceptions;
		return this;
	}

	public ExcelMapper headerRowNumber(Integer headerRowNumber) {
		this.headerRowNumber = headerRowNumber;
		return this;
	}

	public ExcelMapper startRowNumber(Integer startRowNumber) {
		this.startRowNumber = startRowNumber;
		return this;
	}

	public ExcelMapper endRowNumber(Integer endRowNumber) {
		this.endRowNumber = endRowNumber;
		return this;
	}

	public ExcelMapper getWorkbookExceptions(WorkbookCallback callback) {
		this.callback = callback;
		return this;
	}

	public ExcelMapper cellExceptionsColor(IndexedColors indexColor) throws Throwable {
		cellExceptionBackground = this.workbook.createCellStyle();
		cellExceptionBackground.setFillForegroundColor(indexColor.index);
		cellExceptionBackground.setFillPattern(CellStyle.SOLID_FOREGROUND);
		return this;
	}

	public <T> List<T> map() throws Throwable {
		List<T> items = new ArrayList<>();

		if (this.sheet == null) {
			for (int index = 0; index < this.workbook.getNumberOfSheets(); index++) {
				processSheet(items, index);
			}
		} else {
			for (int index = 0; index < this.sheet.length; index++) {
				processSheet(items, this.sheet[index]);
			}
		}

		if (this.handleCellExceptions) {
			this.callback.getWorkbook(this.workbook);
		}

		return items;
	}

	private <T> void processSheet(List<T> items, int sheetNumber) throws Throwable {
		Sheet sheet = this.workbook.getSheetAt(sheetNumber);
		Iterator<Row> rowIterator = sheet.iterator();

		Map<Field, Integer> fieldToIndexMap = new HashMap<>();

		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			this.cellHasException = false;

			if (row.getRowNum() == this.headerRowNumber) {
				if (this.fieldToColumnIndex.size() > 0) {
					readExcelHeaderFromMap(fieldToIndexMap);
				} else {
					readExcelHeaderFromAnnotations(row, fieldToIndexMap);
				}
			} else {

				if ((this.startRowNumber == null || row.getRowNum() >= this.startRowNumber)
						&& (this.endRowNumber == null || row.getRowNum() <= this.endRowNumber)) {
					T readExcelContent = (T) readExcelContent(row, fieldToIndexMap);

					if (!this.cellHasException || this.mustMapRowThatCellsHaveException) {
						items.add(readExcelContent);
					}

					if (this.listExceptions != null && this.handleCellExceptions && this.cellHasException) {
						this.listExceptions.add(readExcelContent);
					}
				}
			}
		}
	}

	private void readExcelHeaderFromMap(Map<Field, Integer> fieldToIndexMap) throws Throwable {
		Iterator<Entry<String, Integer>> iterator = this.fieldToColumnIndex.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Integer> next = iterator.next();
			fieldToIndexMap.put(ReflectionUtils.mapNameToField(clazz, next.getKey()), next.getValue());
		}
	}

	private void readExcelHeaderFromAnnotations(final Row row, final Map<Field, Integer> fieldToIndexMap)
			throws Throwable {
		ReflectionUtils.eachFields(clazz, new EachFieldCallback() {
			@Override
			public void each(Field field, String name, Integer index) throws Throwable {
				if (name != null) {
					mapNameToIndex(field, name, row, fieldToIndexMap);
				} else {
					fieldToIndexMap.put(field, index);
				}
			}
		});
	}

	private void mapNameToIndex(Field field, String name, Row row, Map<Field, Integer> cells) {
		int idx = findIndexCellByName(name, row);
		if (idx != -1) {
			cells.put(field, idx);
		}
	}

	private int findIndexCellByName(String name, Row row) {
		Iterator<Cell> iterator = row.cellIterator();
		while (iterator.hasNext()) {
			Cell cell = iterator.next();
			if (getCellValue(cell).trim().equalsIgnoreCase(name)) {
				return cell.getColumnIndex();
			}
		}

		return -1;
	}

	private Object readExcelContent(final Row row, final Map<Field, Integer> fieldToIndexMap) throws Throwable {
		final Object instance = clazz.newInstance();

		Iterator<Entry<Field, Integer>> iterator = fieldToIndexMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Field, Integer> next = iterator.next();
			Cell cell = row.getCell(next.getValue());
			try {
				ReflectionUtils.setValueOnField(instance, next.getKey(), getCellValue(cell));
			} catch (Exception e) {
				this.cellHasException = true;
				LOG.error("Error raise on Sheet: " + row.getSheet().getSheetName() + ", Row: " + row.getRowNum()
						+ ", Cell: " + cell.getColumnIndex());
				e.printStackTrace();
				if (this.handleCellExceptions) {
					setCellBackground(row, cell.getColumnIndex(), cellExceptionBackground);
				}
			}
		}

		return instance;
	}

	private String getCellValue(Cell cell) {
		if (cell == null) {
			return null;
		}

		String value = "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BOOLEAN:
			value += String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_NUMERIC:
			value += new BigDecimal(cell.getNumericCellValue()).toString();
			break;
		case Cell.CELL_TYPE_STRING:
			value += cell.getStringCellValue();
			break;
		}

		return value;
	}

	public static Boolean setCellBackground(Row row, int colNum, CellStyle cellBackground) {
		Cell cell = row.getCell(colNum);
		if (cell == null)
			row.createCell(colNum).setCellStyle(cellBackground);
		else
			row.getCell(colNum).setCellStyle(cellBackground);
		return false;
	}
}
