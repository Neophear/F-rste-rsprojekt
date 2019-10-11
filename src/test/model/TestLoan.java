package test.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import model.Employee;
import model.Loan;
import model.User;
import model.common.WrongDateSpanException;
import model.item.Item;
import model.item.ItemStatus;
import model.item.ItemType;
import model.item.UtilityItem;

class TestLoan {
	Loan loan;
	
	@BeforeEach
	void setUp() throws Exception {
		loan = new Loan(new User(1, "123456", "TestUser", "TestUnit", "TestNotes", "password"));
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testFields() {
		//Test field createdByUser
		assertEquals(loan.getCreatedByUser().getName(), "TestUser");
		
		//Test field employee
		loan.setEmployee(new Employee(2, "222222", "TestEmployee", "TestUnit2", "TestNotes2"));
		assertEquals(loan.getEmployee().getName(), "TestEmployee");
		
		//Test field dateStart
		try {
			loan.setDateStart(LocalDate.now().plusDays(1));
		} catch (WrongDateSpanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(loan.getDateStart(), LocalDate.now().plusDays(1));
		
		//Test that dateEnd cannot be less than dateStart
		Executable exTestDateEnd = () -> loan.setDateEnd(LocalDate.now());
		assertThrows(WrongDateSpanException.class, exTestDateEnd);
		
		//Test that dateEnd can be equal to dateStart
		try {
			loan.setDateEnd(LocalDate.now().plusDays(1));
		} catch (WrongDateSpanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(loan.getDateEnd(), LocalDate.now().plusDays(1));
		
		//Test that dateEnd can be greater than dateStart
		try {
			loan.setDateEnd(LocalDate.now().plusDays(2));
		} catch (WrongDateSpanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(loan.getDateEnd(), LocalDate.now().plusDays(2));
	}
	
	@Test
	void testLoanItems() {
		//Test that loanItemList is instantiated and empty
		assertEquals(loan.getLoanItemLines().size(), 0);
		
		//Test that an item can be added to loanItemList
		Item i1 = new Item(1, "TestModel", "TestDescription", "TestSerial", ItemType.LAPTOP, ItemStatus.OK, "TestNotes");
		loan.addItem(i1);
		assertEquals(loan.getLoanItemLines().size(), 1);
		assertEquals(loan.getLoanItemLines().get(0).getItem().getId(), 1);
		
		//Test that the same item won't be added twice
		loan.addItem(i1);
		assertEquals(loan.getLoanItemLines().size(), 1);
		
		Item i2 = new Item(2, "TestModel2", "TestDescription2", "TestSerial2", ItemType.DESKTOP, ItemStatus.OK, "TestNotes2");
		loan.addItem(i2);
		assertEquals(loan.getLoanItemLines().size(), 2);
		
		//Test that removing an item not in the list, won't do anything
		Item i3 = new Item(3, "TestModel3", "TestDescription3", "TestSerial3", ItemType.DESKTOP, ItemStatus.OK, "TestNotes3");
		loan.removeItem(i3);
		assertEquals(loan.getLoanItemLines().size(), 2);
		
		//Test that removing an item IN the list, will remove it correctly
		loan.removeItem(i1);
		assertEquals(loan.getLoanItemLines().size(), 1);
		assertEquals(loan.getLoanItemLines().get(0).getItem().getId(), 2);
	}
	
	@Test
	void testLoanUtilityItems() {
		//Test that loanUtilityItemList is instantiated and empty
		assertEquals(loan.getLoanUtilityItemLines().size(), 0);
		
		//Test that an item can be added to loanUtilityItemList
		UtilityItem ui1 = new UtilityItem(1, "UtilityItem1", "Description1", 10, 100);
		loan.addUtilityItem(ui1);
		assertEquals(loan.getLoanUtilityItemLines().size(), 1);
		assertEquals(loan.getLoanUtilityItemLines().get(0).getItem().getId(), 1);
		
		//Test that multiple items can be added at the same time
		UtilityItem ui2 = new UtilityItem(2, "UtilityItem2", "Description2", 10, 100);
		loan.addUtilityItem(ui2, 10);
		assertEquals(loan.getLoanUtilityItemLines().size(), 11);
		assertEquals(loan.getLoanUtilityItemLines().get(1).getItem().getId(), 2);
		assertEquals(loan.getLoanUtilityItemLines().get(10).getItem().getId(), 2);
		
		//Test that an item can be removed from the list
		loan.removeUtilityItem(ui1);
		assertEquals(loan.getLoanUtilityItemLines().size(), 10);
		assertEquals(loan.getLoanUtilityItemLines().get(0).getItem().getId(), 2);
		
		//Test that an item can be removed in the middle of the list
		loan.addUtilityItem(ui1);
		UtilityItem ui3 = new UtilityItem(3, "UtilityItem3", "Description3", 10, 100);
		loan.addUtilityItem(ui3);
		loan.removeUtilityItem(ui1);
		assertEquals(loan.getLoanUtilityItemLines().size(), 11);
		assertEquals(loan.getLoanUtilityItemLines().get(10).getItem().getId(), 3);
		
		//Test that multiple of the same UtilityItem can be removed
		loan.removeUtilityItem(ui2, 3);
		assertEquals(loan.getLoanUtilityItemLines().size(), 8);
		assertEquals(loan.getLoanUtilityItemLines().get(0).getItem().getId(), 2);
		assertEquals(loan.getLoanUtilityItemLines().get(7).getItem().getId(), 3);
		
		//Test trying to remove more of an item, than in the list, will only remove those in the list
		loan.removeUtilityItem(ui2, 99);
		assertEquals(loan.getLoanUtilityItemLines().size(), 1);
		assertEquals(loan.getLoanUtilityItemLines().get(0).getItem().getId(), 3);
	}
}