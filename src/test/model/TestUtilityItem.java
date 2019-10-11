package test.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.item.UtilityItem;

class TestUtilityItem {
	UtilityItem uitem;

	@BeforeEach
	void setUp() throws Exception {
		uitem = new UtilityItem(1, "n", "d", 10, 11);
	}

	@AfterEach
	void tearDown() throws Exception {

	}

	@Test
	void testGetId() {
		assertEquals(uitem.getId(), 1);
	}

	@Test
	void testSetGet() {
		assertEquals(uitem.getName(), "n");
		uitem.setName("nn");
		assertEquals(uitem.getName(), "nn");

		assertEquals(uitem.getDescription(), "d");
		uitem.setDescription("nd");
		assertEquals(uitem.getDescription(), "nd");

		assertEquals(uitem.getMinStock(), 10);
		uitem.setMinStock(15);
		assertEquals(uitem.getMinStock(), 15);

		assertEquals(uitem.getCurrentStock(), 11);
		uitem.setCurrentStock(20);
		assertEquals(uitem.getCurrentStock(), 20);
		
	}
}
