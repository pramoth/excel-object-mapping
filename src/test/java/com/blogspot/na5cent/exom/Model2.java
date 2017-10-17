/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package com.blogspot.na5cent.exom;

import java.util.Date;

/**
 * @author Mohsen.Mahmoudi
 */
public class Model2 {

	private String fistName;
	private String lastName;
	private Integer age;
	private Date birthdate;
	private String fatherName;
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
