package com.blogspot.na5cent.sampleClient;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import com.blogspot.na5cent.exom.ExOM;

/**
 * Mapper example client, using the Withdrawal object 
 * 
 * @author edwin.njeru
 *
 */
public class MapperClient {
	
	public static void main(String[] args) throws Throwable {
		
		String filePath = "C:\\ExcelFilesForAnalysis\\All_Debits.xls";
		
		List<Withdrawal> withdrawals = 
				ExOM.mapFromExcel(new File(filePath))
				.toObjectOf(Withdrawal.class)// Object to be mapped containing @ExcelColumn annotations
				.map();
		
		Iterator<Withdrawal> withdrawalIterator = withdrawals.iterator();
		
		while (withdrawalIterator.hasNext()) {
			
			System.out.println(withdrawalIterator.next());
		}
	}
	
}
