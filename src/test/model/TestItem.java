package test.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.item.Item;
import model.item.ItemStatus;
import model.item.ItemType;

class TestItem {
	Item item;
	
	@BeforeEach
	void setUp() throws Exception {
		item = new Item(1, "model", "desc", "serial", ItemType.DESKTOP, ItemStatus.OK, "notes");
	}

	@AfterEach
	void tearDown() throws Exception {
		
	}

	@Test
	void testGetId() {
		assertEquals(item.getId(), 1);
	}
	
	@Test
	void testSetGet() {
		assertEquals(item.getModel(), "model");
		item.setModel("nm");
		assertEquals(item.getModel(), "nm");
		
		assertEquals(item.getDescription(), "desc");
		item.setDescription("nd");
		assertEquals(item.getDescription(), "nd");
		
		assertEquals(item.getSerial(), "serial");
		item.setSerial("ns");
		assertEquals(item.getSerial(), "ns");
		
		assertEquals(item.getItemType(), ItemType.DESKTOP);
		item.setItemType(ItemType.LAPTOP);
		assertEquals(item.getItemType(), ItemType.LAPTOP);
		
		assertEquals(item.getStatus(), ItemStatus.OK);
		item.setStatus(ItemStatus.DECOMISSIONED);
		assertEquals(item.getStatus(), ItemStatus.DECOMISSIONED);
		
		assertEquals(item.getNotes(), "notes");
		item.setNotes("nn");
		assertEquals(item.getNotes(), "nn");
	}
}
