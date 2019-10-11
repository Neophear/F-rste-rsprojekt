package test.db;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import db.ItemDB;
import db.common.DataAccessException;
import model.common.Tuple;
import model.item.Item;
import model.item.ItemStatus;
import model.item.ItemType;

class TestItemDB {

	ItemDB itemDB;
	
	@BeforeEach
	void setUp() throws Exception {
		itemDB = ItemDB.getInstance();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGetItemById() {
		Item item1 = null;
		Item item2 = null;
		try {
			item1 = itemDB.getItemById(1);
			item2 = itemDB.getItemById(2);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertEquals(item1.getSerial(), "1" );
		assertEquals(item2.getSerial(), "2" );
		
		assertEquals(item1.getDescription(), "15.6\" Workstation laptop. 8GB RAM, i5-2520M, 256GB SSD." );
		assertEquals(item2.getDescription(), "Alienware" );
		
		assertEquals(item1.getItemType(), ItemType.LAPTOP );
		assertEquals(item2.getItemType(), ItemType.DESKTOP );
		
		assertEquals(item1.getStatus(), ItemStatus.OK );
		assertEquals(item2.getStatus(), ItemStatus.OK );
		
		assertEquals(item1.getNotes(), "" );
		assertEquals(item2.getNotes(), "" );
	}
	
	
	
	@Test
	void testGetItemWithAvailable() {
		List<Tuple<Item, Boolean>> items = new ArrayList<>();
		try {
			items = itemDB.getAllItemsWithAvailable(LocalDate.parse("2018-12-11"), LocalDate.parse("2018-12-12"));
			
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertFalse(items.get(1).y);
	}

}
