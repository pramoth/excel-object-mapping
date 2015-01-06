/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package com.blogspot.na5cent.exom;

import com.blogspot.na5cent.exom.util.EachFieldCallback;
import com.blogspot.na5cent.exom.util.ReflectionUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author redcrow
 */
public class ExOM {

    private static final Logger LOG = LoggerFactory.getLogger(ExOM.class);

    private final File excelFile;
    private Class clazz;

    private ExOM(File excelFile) {
        this.excelFile = excelFile;
    }

    public static ExOM mapFromExcel(File excelFile) {
        return new ExOM(excelFile);
    }

    public ExOM toObjectOf(Class clazz) {
        this.clazz = clazz;
        return this;
    }

    private String getValueByName(String name, Row row, Map<String, Integer> cells) {
        if (cells.get(name) == null) {
            return null;
        }

        Cell cell = row.getCell(cells.get(name));
        return getCellValue(cell);
    }

    private void mapName2Index(String name, Row row, Map<String, Integer> cells) {
        int index = findIndexCellByName(name, row);
        if (index != -1) {
            cells.put(name, index);
        }
    }

    private void readExcelHeader(final Row row, final Map<String, Integer> cells) throws Throwable {
        ReflectionUtils.eachFields(clazz, new EachFieldCallback() {

            @Override
            public void each(Field field, String name) throws Throwable {
                mapName2Index(name, row, cells);
            }
        });
    }

    private Object readExcelContent(final Row row, final Map<String, Integer> cells) throws Throwable {
        final Object instance = clazz.newInstance();
        ReflectionUtils.eachFields(clazz, new EachFieldCallback() {

            @Override
            public void each(Field field, String name) throws Throwable {
                ReflectionUtils.setValueOnField(instance, field, getValueByName(
                        name,
                        row,
                        cells
                ));
            }
        });

        return instance;
    }

    public <T> List<T> map() throws Throwable {
        InputStream inputStream = null;
        List<T> items = new ArrayList<>();

        try {
            Iterator<Row> rowIterator;
            inputStream = new FileInputStream(excelFile);
            int numberOfSheets;
            Workbook workbook;

            if (excelFile.getName().endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream);
                numberOfSheets = workbook.getNumberOfSheets();
            } else { //2007+
                workbook = new XSSFWorkbook(inputStream);
                numberOfSheets = workbook.getNumberOfSheets();
            }

            for (int index = 0; index < numberOfSheets; index++) {
                Sheet sheet = workbook.getSheetAt(index);
                rowIterator = sheet.iterator();

                Map<String, Integer> nameIndexMap = new HashMap<>();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    if (row.getRowNum() == 0) {
                        readExcelHeader(row, nameIndexMap);
                    } else {
                        items.add((T) readExcelContent(row, nameIndexMap));
                    }
                }
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return items;
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
}
