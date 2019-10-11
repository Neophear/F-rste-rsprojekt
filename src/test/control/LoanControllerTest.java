package test.control;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import control.*;
import db.*;
import db.common.*;
import model.*;
import model.common.WrongDateSpanException;
import model.item.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class LoanControllerTest {
	LoanController loanCtrl;
	User user;
	Employee employee;
	List<Item> items;
	List<UtilityItem> utilityItems;
	
	@Before
	void setup() throws DataAccessException {
		loanCtrl = LoanController.getInstance();
		user = UserDB.getInstance().getUserById(1);
		employee = EmployeeDB.getInstance().getEmployeeById(1);
		items = ItemDB.getInstance().getAllItemsWithAvailable(LocalDate.MIN, LocalDate.MAX)
				.stream().filter(i -> i.y).map(i -> i.x).collect(Collectors.toList());
		utilityItems = UtilityItemDB.getInstance().getAllUtilityItemsWithAvailable(LocalDate.MIN, LocalDate.MAX)
				.stream().filter(i -> i.y>0).map(i -> i.x).collect(Collectors.toList());
	}
	
	@BeforeEach
	void prepEach() {
		
	}
	
	@After
	void tearDown() {
		
	}

	@Test
	void test01FindEmployee() throws DataAccessException {
		Employee e = loanCtrl.findEmployee("00123456"); // MANR 00123456 is superadmin
		
		// Verify that Employee object is real and valid
		assertNotNull(e);
		assertEquals("Admin", e.getName());
	}

	@Test
	void test02GetLoan() throws DataAccessException {
		Loan l = loanCtrl.getLoan(1); // ID is guaranteed to exist in DB.
		
		// Verify that Loan object is real and valid
		assertNotNull(l);
		assertEquals(1, l.getId());
	}

	@Test
	void test03GetNewLoanItems() throws DataAccessException, WrongDateSpanException {
		LocalDate date = LocalDate.parse("2018-12-10");
		loanCtrl.startNewLoan(user);
		loanCtrl.setNewLoanDateStart(date);
		loanCtrl.setNewLoanDateEnd(date);
		
		// Add a bunch of items to new loan
		items.stream().forEach(i -> loanCtrl.addItemToNewLoan(i));
		
		// Make sure we have as many item-lines as we added earlier.
		assertEquals(loanCtrl.getNewLoanItems().size(), items.size());
	}

	@Test
	void test04GetAllItemsWithAvailable() {
		fail("Not yet implemented");
	}

	@Test
	void test05GetAllUtilityItemsWithAvailable() {
		fail("Not yet implemented");
	}

	@Test
	void test06StartNewLoan() {
		fail("Not yet implemented");
	}

	@Test
	void test07SetNewLoanEmployee() {
		fail("Not yet implemented");
	}

	@Test
	void test08SetNewLoanDateStart() {
		fail("Not yet implemented");
	}

	@Test
	void test09SetNewLoanDateEnd() {
		fail("Not yet implemented");
	}

	@Test
	void test10AddItemToNewLoan() {
		fail("Not yet implemented");
	}

	@Test
	void test11AddUtilityItemToNewLoan() {
		fail("Not yet implemented");
	}

	@Test
	void test12FinalizeLoan() {
		fail("Not yet implemented");
	}
}
