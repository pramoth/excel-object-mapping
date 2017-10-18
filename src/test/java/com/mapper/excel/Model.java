/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package com.mapper.excel;

import java.util.Date;

import com.mapper.excel.annotation.Column;

/**
 * @author redcrow
 * @author Mohsen.Mahmoudi
 */
public class Model {

	@Column(name = "first name")
	private String fistName;

	@Column(name = "last name")
	private String lastName;

	private Integer age;

	@Column(name = "birth date", pattern = "dd/MM/yyyy")
	private Date birthdate;

	@Column(index = 4)
	private String fatherName;

	@Column(index = 5)
	private Boolean iq;

	public String getFistName() {
		return fistName;
	}

	public void setFistName(String fistName) {
		this.fistName = fistName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public Boolean getIq() {
		return iq;
	}

	public void setIq(Boolean iq) {
		this.iq = iq;
	}

}
