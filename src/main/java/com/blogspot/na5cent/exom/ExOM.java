package com.blogspot.na5cent.exom;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blogspot.na5cent.exom.util.ReflectionUtils;

/**
 * This is the entry point whose factory methods could create a file, while also
 * specifying the object into which they are to be mapped
 * 
 * @author edwin.njeru
 * 
 * @author redcrow
 */
public class ExOM {
	
	private static final Logger log = LoggerFactory.getLogger(ExOM.class);
	
	private final File excelFile;
	private Class<?> clazz;
	
	private ExOM(final File excelFile) {
		
		log.debug("ExOM created with excelFile : {} as argument ", excelFile);
		
		this.excelFile = excelFile;
	}
	
	/**
	 * The main factory, whose only param is the excelfile. Once this is declared it 
	 * must be followed up by the toObjectOf() method, to specify the object into
	 * which the excel row is to be mapped and afterwards the map() method which creates
	 * a map of the excel sheets converting them into List<MappedObject> object
	 * 
	 * @param excelFile
	 * @return
	 */
	public static ExOM mapFromExcel(final File excelFile) {
		
		log.debug("mapFromExcel called with excelFile : {} as argument ", excelFile);
		
		return new ExOM(excelFile);
	}
	
	/**
	 * This method is called after mapFromExcel method to specify the class into which we are 
	 * mapping the excel Row
	 * 
	 * @param clazz
	 * @return
	 */
	public ExOM toObjectOf(final Class<?> clazz) {
		
		log.debug("excelFile to be mapped toObjectOf {} ", clazz);
		this.clazz = clazz;
		return this;
	}
	
	private String getValueByName(final String name, final Row row, final Map<String, Integer> cells) {
		
		log.trace("getValueByName called with args name={}, row={}, cellMap={}", name, row.getClass(),
				cells.getClass());
		
		if (cells.get(name) == null) { return null; }
		
		final Cell cell = row.getCell(cells.get(name));
		
		return getCellValue(cell);
	}
	
	private void mapName2Index(final String name, final Row row, final Map<String, Integer> cells) {
		
		log.trace("mapName2Index called with args name={}, row={}, cellMap={}", name, row.getClass(), cells.getClass());
		
		log.trace("Calling the method findIndexCellByName() with args {} and {}", name, row.getClass());
		final int index = findIndexCellByName(name, row);
		
		log.trace("Index  : {}", index);
		
		if (index != -1) {
			
			log.trace("Index not equsl to -1, inserting name : {} into the cellMap : {}", name, cells.getClass());
			
			cells.put(name, index);
			
			log.trace("Name : added into the cellMap. CellMap now contains :{} items", cells.size());
		}
	}
	
	private void readExcelHeader(final Row row, final Map<String, Integer> cells) {
		
		log.trace("readExcelHeader() has been called with args : row={}, cellMap={}", row.getClass(), cells.getClass());
		
		log.trace("calling eachFields() method in the ReflectionUtils with class={}", clazz);
		
		ReflectionUtils.eachFields(clazz, (field, name) ->
		
		mapName2Index(name, row, cells));
	}
	
	private Object readExcelContent(final Row row, final Map<String, Integer> cells) throws Exception {
		
		log.trace("readExcelContent() has been called with args row={},cellMap={}", row.getClass(), cells.getClass());
		
		final Object instance = clazz.newInstance();
		log.trace("final object instance : {} created from clazz : {}", instance.getClass(), clazz.getClass());
		
		log.trace("calling eachFields() method with args clazz={}, and EachFieldsCallBack lambda", clazz.getClass());
		
		ReflectionUtils.eachFields(clazz, (field, name) ->
		
		ReflectionUtils.setValueOnField(instance, field, getValueByName(name, row, cells)));
		
		return instance;
	}
	
	/**
	 * This method is the final step in declaring this class where we finally the 
	 * factory pattern
	 * 
	 * @return List<T> where T is the Object we are mapping
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> map() {
		
		log.trace("map method has been called, with no args");
		
		InputStream inputStream = null;
		
		final List<T> items = new FastList<>();
		
		Workbook workbook = null;
		
		int numberOfSheets = 0;
		
		try {
			
			Iterator<Row> rowIterator;
			
			inputStream = new FileInputStream(excelFile);
			
			log.debug("inputstream has been created : {}", inputStream.getClass());
			
			if (excelFile.getName().endsWith(".xls")) {
				
				log.debug("Using the OLD FORMAT excel file, creating workbook...");
				
				workbook = new HSSFWorkbook(inputStream);
				
				log.debug("Workbook object : {} successfully created", workbook);
				
				numberOfSheets = workbook.getNumberOfSheets();
				
				log.debug("# of sheets in workbook : {}", numberOfSheets);
				
			} else {
				
				log.debug("Using the NEW FORMAT excel file, creating workbook...");
				
				workbook = new XSSFWorkbook(inputStream);
				
				log.debug("Workbook object : {} successfully created", workbook);
				
				numberOfSheets = workbook.getNumberOfSheets();
				
				log.debug("# of sheets in workbook : {}", numberOfSheets);
			}
			
			for (int index = 0; index < numberOfSheets; index++) {
				
				log.debug("Reading sheet # : {}",index);
				
				final Sheet sheet = workbook.getSheetAt(index);
				
				log.debug("sheet object : {} created",sheet.getSheetName());
				
				rowIterator = sheet.iterator();
				
				final Map<String, Integer> nameIndexMap = new UnifiedMap<>();
				
				while (rowIterator.hasNext()) {
					
					final Row row = rowIterator.next();
					
					log.trace("Iterating over row # : {}",row.getRowNum());
					
					if (row.getRowNum() == 0) {
						
						log.trace("We are at row 0, calling the readExcelHeader method with"
								+ "with args : {} and {}",row,nameIndexMap.getClass());
						
						readExcelHeader(row, nameIndexMap);
						
					} else {
						
						log.trace("Reading excel body content on row # : {}",row.getRowNum());
						
						items.add((T) readExcelContent(row, nameIndexMap));
						
					}
				}
			}
			
		} catch (IOException e) {
			
			log.error("{} Exception caused by : {}, Occuring at : {} while trying to read excel file",
					e.getMessage(),e.getCause(),e.getStackTrace());
			
		} catch (Exception e) {
			
			log.error("{} Exception caused by : {}, Occuring at : {} while trying to read excel file",
					e.getMessage(),e.getCause(),e.getStackTrace());

		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					log.error("{} Exception caused by: {}, Occuring at : {} while trying to close the inputStream",
							e.getMessage(),e.getCause(),e.getStackTrace());

				}
			}
			
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					log.error("{} Exception caused by : {}, Occuring at : {} while trying to close the workbook",
							e.getMessage(),e.getCause(),e.getStackTrace());

				}
			}
		}
		
		return items;
	}
	
	private int findIndexCellByName(final String name, final Row row) {
		
		log.trace("findIndexByName method called with args name={} and row={}",name,row.getClass());
		final Iterator<Cell> iterator = row.cellIterator();
		while (iterator.hasNext()) {
			
			final Cell cell = iterator.next();
			
			if (getCellValue(cell).trim().equalsIgnoreCase(name)) { 
				
				int colIndex = cell.getColumnIndex();
				
				log.trace("finding the column index for cell : colIndex={}",colIndex);
				
				return colIndex;
				
				}
		}
		
		return -1;
	}
	
	private String getCellValue(final Cell cell) {
		
		if (cell == null) { return null; }
		
		String value = "";
		
		/*
		 * switch (cell.getCellType()) { case Cell.CELL_TYPE_BOOLEAN: value +=
		 * String.valueOf(cell.getBooleanCellValue()); break; case
		 * Cell.CELL_TYPE_NUMERIC: value += new
		 * BigDecimal(cell.getNumericCellValue()).toString(); break; case
		 * Cell.CELL_TYPE_STRING: value += cell.getStringCellValue(); break; }
		 */
		
		switch (cell.getCellTypeEnum()) {
			case BOOLEAN:
				value += String.valueOf(cell.getBooleanCellValue());
				break;
			case NUMERIC:
				value += new BigDecimal(cell.getNumericCellValue()).toString();
				break;
			case STRING:
				value += cell.getStringCellValue();
				break;
			case BLANK:
				break;
			case ERROR:
				// throw new ExcelReadException("Error reading cell : "+
				// cell.getRowIndex()+cell.getColumnIndex());
				break;
			case FORMULA:
				//
				break;
			case _NONE:
				break;
			default:
				break;
		}
		
		return value;
	}
	
	/*
	 * private Object readCellValue(Cell cell) { return readCellValue(cell,
	 * cell.getCellTypeEnum()); }
	 * 
	 * private Object readCellValue(Cell cell, CellType type) { switch (type) { case
	 * NUMERIC: if (DateUtil.isCellDateFormatted(cell)) { return
	 * cell.getDateCellValue(); } double n = cell.getNumericCellValue(); CellStyle
	 * style = cell.getCellStyle(); String format = style.getDataFormatString(); if
	 * (format.indexOf('.') < 0) { return (long) n; } return n; case FORMULA: return
	 * readCellValue(cell, cell.getCachedFormulaResultTypeEnum()); case BOOLEAN:
	 * return cell.getBooleanCellValue(); case ERROR: return
	 * tolerantLevel.readErrorCell(cell); case BLANK: return null; case STRING:
	 * return cell.getStringCellValue(); default: return
	 * tolerantLevel.readUnknownCellType(cell); }
	 */
}
