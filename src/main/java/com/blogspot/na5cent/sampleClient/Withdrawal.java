package com.blogspot.na5cent.sampleClient;

import java.time.LocalDate;
import java.util.Date;

import com.blogspot.na5cent.exom.annotation.ExcelColumn;

/**
 * This is the sample class to be mapped
 * 
 * @author edwin.njeru
 *
 */
public class Withdrawal {
	
	public Withdrawal() {
	}
	
	/**
	 * This column is not in the excel file hence it is not annotated or to be
	 * annotated with the @ExcelColumn annotation
	 */
	private int id;
	
	/**
	 * @ExcelColumn annotation is added to identify the header column in the excel file
	 * in the column that resonates with this field. Because it is a date type(java.util.Date)
	 * the annotation must specify the pattern currently used in the excel file like so :
	 * @ExcelColumn(name = "DATE", pattern = "dd/MM/yyyy")
	 * 
	 */
	@ExcelColumn(name = "DATE", pattern = "dd/MM/yyyy")
	private Date date;
	
	/**
	 * @ExcelColumn annotation is added to identify the header column in the excel file
	 * in the column that resonates with this field.
	 * The name value must ALWAYS be the same with the name in the header row of the excel
	 * file being read
	 */
	@ExcelColumn(name = "NUMBER")
	private String number;
	
	/**
	 * @ExcelColumn annotation is added to identify the header column in the excel file
	 * in the column that resonates with this field.
	 * The name value must ALWAYS be the same with the name in the header row of the excel
	 * file being read
	 */
	@ExcelColumn(name = "NAME")
	private String name;
	
	/**
	 * @ExcelColumn annotation is added to identify the header column in the excel file
	 * in the column that resonates with this field.
	 * The name value must ALWAYS be the same with the name in the header row of the excel
	 * file being read
	 */
	@ExcelColumn(name = "CURRENCY")
	private String currency;
	
	/**
	 * @ExcelColumn annotation is added to identify the header column in the excel file
	 * in the column that resonates with this field.
	 * The name value must ALWAYS be the same with the name in the header row of the excel
	 * file being read
	 * The primitives MUST be wrapped in their corresponding objects
	 */
	@ExcelColumn(name = "AMOUNT")
	private Double amount;
	
	@Override
	public String toString() {
		return "Withdrawal [date=" + this.date + ", number=" + this.number + ", name=" + this.name + ", currency="
				+ this.currency + ", amount=" + this.amount + "]";
	}
	
	/**
	 * @return the id
	 */
	public synchronized int getId() {
		return this.id;
	}
	
	/**
	 * @param id
	 *            the id to set
	 */
	public synchronized void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return the number
	 */
	public synchronized String getNumber() {
		return this.number;
	}
	
	/**
	 * @return the date
	 */
	public synchronized Date getDate() {
		return this.date;
	}
	
	/**
	 * @param date
	 *            the date to set
	 */
	public synchronized void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * @param number
	 *            the number to set
	 */
	public synchronized void setNumber(String number) {
		this.number = number;
	}
	
	/**
	 * @return the name
	 */
	public synchronized String getName() {
		return this.name;
	}
	
	/**
	 * @param name
	 *            the name to set
	 */
	public synchronized void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the currency
	 */
	public synchronized String getCurrency() {
		return this.currency;
	}
	
	/**
	 * @param currency
	 *            the currency to set
	 */
	public synchronized void setCurrency(String currency) {
		this.currency = currency;
	}
	
	/**
	 * @return the amount
	 */
	public synchronized Double getAmount() {
		return this.amount;
	}
	
	/**
	 * @param amount
	 *            the amount to set
	 */
	public synchronized void setAmount(Double amount) {
		this.amount = amount;
	}
	
}
