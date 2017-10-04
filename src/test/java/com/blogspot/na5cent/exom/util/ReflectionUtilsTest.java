package com.blogspot.na5cent.exom.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ReflectionUtilsTest {
	
	ReflectionUtils utils;
	
	@Before
	public void setUpBeforeClass() throws Exception {
		
		 utils = new ReflectionUtils();
	}
	
	@Test
	public void testToUpperCaseFirstCharacter() {
		
		String testField1 = "north";
		
		assertEquals("North", utils.toUpperCaseFirstCharacter(testField1));
	}
	
	@Test
	public void test2ToUpperCaseFirstCharacter() {
		
		String testField1 = "NORTH";
		
		assertEquals("NORTH", utils.toUpperCaseFirstCharacter(testField1));
	}
	
	@Test
	public void test3ToUpperCaseFirstCharacter() {
		
		String testField1 = "nORTH";
		
		assertEquals("NORTH", utils.toUpperCaseFirstCharacter(testField1));
	}
	
	@Test
	public void test4ToUpperCaseFirstCharacter() {
		
		String testField1 = "nORtH";
		
		assertEquals("NORtH", utils.toUpperCaseFirstCharacter(testField1));
	}
	
}
