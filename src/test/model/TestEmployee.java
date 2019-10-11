package test.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Employee;

class TestEmployee {

	Employee employee;
	
	@BeforeEach
	void setUp() throws Exception {
		employee = new Employee(1, "manr", "name", "unit", "notes");
	}

	@AfterEach
	void tearDown() throws Exception {
		
	}

	@Test
	void testGetId() {
		assertEquals(employee.getId(), 1);
	}
	
	@Test
	void testSetGet() {
		assertEquals(employee.getMANR(), "manr");
		employee.setMANR("newManr");
		assertEquals(employee.getMANR(), "newManr");
		
		assertEquals(employee.getName(), "name");
		employee.setName("newName");
		assertEquals(employee.getName(), "newName");
		
		assertEquals(employee.getUnit(), "unit");
		employee.setUnit("newUnit");
		assertEquals(employee.getUnit(), "newUnit");
		
		assertEquals(employee.getNotes(), "notes");
		employee.setNotes("newNotes");
		assertEquals(employee.getNotes(), "newNotes");
	}

}
