/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blogspot.na5cent.exom;

import com.blogspot.na5cent.exom.util.EachFieldCallback;
import com.blogspot.na5cent.exom.util.ReflectionUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
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

/**
 * @author redcrow
 */
public class ExOM {

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

    private Object getValueByName(String name, Row row, Map<String, Integer> cells) {
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

    private void readTitle(final Row row, final Map<String, Integer> cells) throws Throwable {
        ReflectionUtils.eachFields(clazz, new EachFieldCallback() {

            @Override
            public void each(Field field, String name) throws Throwable {
                mapName2Index(name, row, cells);
            }
        });
    }

    private Object readValue(final Row row, final Map<String, Integer> cells) throws Throwable {
        final Object instance = clazz.newInstance();
        ReflectionUtils.eachFields(clazz, new EachFieldCallback() {

            @Override
            public void each(Field field, String name) throws Throwable {
                Object value = getValueByName(name, row, cells);
                ReflectionUtils.setValueOnField(instance, field, value);
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

                Map<String, Integer> cells = new HashMap<>();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    if (row.getRowNum() == 0) {
                        readTitle(row, cells);
                    } else if (row.getRowNum() > 0) {
                        items.add((T) readValue(row, cells));
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
            Object cellValue = getCellValue(cell);
            if (cellValue instanceof String) {
                if (((String) cellValue).trim().contains(name)) {
                    return cell.getColumnIndex();
                }
            }
        }

        return -1;
    }

    private Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        Object value = null;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                value = cell.getNumericCellValue();
                break;
            case Cell.CELL_TYPE_STRING:
                value = cell.getStringCellValue();
                break;
        }

        return value;
    }
}
