/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blogspot.na5cent.exom;

import java.io.File;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * @author redcrow
 */
public class LoadExcel2Model {

    private static final Logger LOG = LoggerFactory.getLogger(LoadExcel2Model.class);

    @Test
    public void test() throws Throwable {
        File excelFile = new File(getClass().getResource("/excel.xlsx").getPath());
        List<Model> items = ExOM.mapFromExcel(excelFile)
                .toObjectOf(Model.class)
                .map();

        for (Model item : items) {
            LOG.debug("first name --> {}", item.getFistName());
            LOG.debug("last name --> {}", item.getLastName());
            LOG.debug("age --> {}", item.getAge());
            LOG.debug("birth date --> {}", item.getBirthdate());
            LOG.debug("");
        }
    }
}
