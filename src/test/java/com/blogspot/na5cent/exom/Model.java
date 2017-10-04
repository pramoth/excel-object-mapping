/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package com.blogspot.na5cent.exom;

import java.util.Date;

import com.blogspot.na5cent.exom.annotation.ExcelColumn;

/**
 * @author redcrow
 */
public class Model {
	
	@ExcelColumn(name = "first name")
	private String fistName;
	@ExcelColumn(name = "last name")
	private String lastName;
	private Integer age; // Wrapper only
	@ExcelColumn(name = "birth date", pattern = "dd/MM/yyyy")
	private Date birthdate;
	
	public String getFistName() {
		return fistName;
	}
	
	public void setFistName(final String fistName) {
		this.fistName = fistName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}
	
	public Integer getAge() {
		return age;
	}
	
	public void setAge(final Integer age) {
		this.age = age;
	}
	
	public Date getBirthdate() {
		return birthdate;
	}
	
	public void setBirthdate(final Date birthdate) {
		this.birthdate = birthdate;
	}
	
}
