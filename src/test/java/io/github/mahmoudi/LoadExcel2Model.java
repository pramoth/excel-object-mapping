/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package io.github.mahmoudi;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mahmoudi.ExcelMapper;
import io.github.mahmoudi.util.WorkbookCallback;

/**
 * @author redcrow
 * @author Mohsen.Mahmoudi
 */
public class LoadExcel2Model {

    private static final Logger LOG = LoggerFactory.getLogger(LoadExcel2Model.class);

    @Test
    public void test() throws Throwable {
        // sample 1 : basic usage
    	File excelFile = new File(getClass().getResource("/excel.xlsx").getPath());
        LOG.debug("------Sample 1------");
        List<Model> items = ExcelMapper.mapFromExcel(excelFile)
                .toObjectOf(Model.class)
                .fromSheet(0) // if this method not used , called all sheets
                .map();

        for (Model item : items) {
            LOG.debug("first name --> {}", item.getFistName());
            LOG.debug("last name --> {}", item.getLastName());
            LOG.debug("age --> {}", item.getAge());
            LOG.debug("birth date --> {}", item.getBirthdate());
            LOG.debug("father name --> {}", item.getFatherName());
            LOG.debug("IQ --> {}", item.getIq());
            LOG.debug("");
        }
        
        // sample 2 : without annotation
        LOG.debug("------Sample 2------");
        File excelFile2 = new File(getClass().getResource("/excel2.xlsx").getPath());
        Map<String, Integer> fieldToColumnIndex = new HashMap<>(); // map all property of model to index of excel column
        fieldToColumnIndex.put("fistName", 0);
        fieldToColumnIndex.put("lastName", 1);
        fieldToColumnIndex.put("age", 2);
        fieldToColumnIndex.put("birthdate", 5);
        fieldToColumnIndex.put("fatherName", 8);
        fieldToColumnIndex.put("iq", 11);

        List<Model2> items2 = ExcelMapper.mapFromExcel(excelFile2)
        		.toObjectOf(Model2.class)
        		.fromSheet(0)
        		.mapFieldFrom(fieldToColumnIndex) // map dynamically
        		.map();
        
        for (Model2 item : items2) {
        	LOG.debug("first name --> {}", item.getFistName());
        	LOG.debug("last name --> {}", item.getLastName());
        	LOG.debug("age --> {}", item.getAge());
        	LOG.debug("birth date --> {}", item.getBirthdate());
        	LOG.debug("father name --> {}", item.getFatherName());
        	LOG.debug("IQ --> {}", item.getIq());
        	LOG.debug("");
        }
        
        // sample 3 : customization headerRowNum & startRowNum & endRowNum
    	File excelFile3 = new File(getClass().getResource("/excel3.xlsx").getPath());
        LOG.debug("------Sample 3------");
        List<Model> items3 = ExcelMapper.mapFromExcel(excelFile3)
                .toObjectOf(Model.class)
                .fromSheet(0)
                .headerRowNumber(1)
                .startRowNumber(3)
                .endRowNumber(4)
                .map();

        for (Model item : items3) {
            LOG.debug("first name --> {}", item.getFistName());
            LOG.debug("last name --> {}", item.getLastName());
            LOG.debug("age --> {}", item.getAge());
            LOG.debug("birth date --> {}", item.getBirthdate());
            LOG.debug("father name --> {}", item.getFatherName());
            LOG.debug("IQ --> {}", item.getIq());
            LOG.debug("");
        }
        
        // sample 4: exception management
        LOG.debug("------Sample 4------");
        File excelFile4 = new File(getClass().getResource("/excel4.xlsx").getPath());
        ArrayList<Model> listExceptions = new ArrayList<Model>();
        List<Model> items4 = ExcelMapper.mapFromExcel(excelFile4)
        		.toObjectOf(Model.class)
        		.fromSheet(0)
        		.handleCellExceptions() // for manage exception, must called this method first
        		.cellExceptionsColor(IndexedColors.YELLOW) // manage color cell
        		.getWorkbookExceptions(new WorkbookCallback() { // get workbook that exception shown that
        			@Override
        			public void getWorkbook(Workbook workbook) throws Throwable {
        				// write workbook on disk
        				FileOutputStream fileOut = new FileOutputStream(
        						getClass().getResource("/").getPath() + "exception.xlsx");
        				workbook.write(fileOut);
        				fileOut.flush();
        				fileOut.close();
        				LOG.debug("file writed on disk");
        			}
        		})
        		.getAllExceptions(listExceptions) // get all list rows that have exceptions
        		.map();
        
        LOG.debug("List Count --> {} ", items4.size());
        LOG.debug("Exceptions List Count --> {} ", listExceptions.size());
        LOG.debug("");
        for (Model item : listExceptions) {
        	LOG.debug("exception first name --> {}", item.getFistName());
        	LOG.debug("exception last name --> {}", item.getLastName());
        	LOG.debug("exception age --> {}", item.getAge());
        	LOG.debug("exception birth date --> {}", item.getBirthdate());
        	LOG.debug("exception father name --> {}", item.getFatherName());
        	LOG.debug("exception IQ --> {}", item.getIq()); // iq data has exception and is null
        	LOG.debug("");
        }
        
    }
}
