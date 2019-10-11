package control;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import control.interfaces.*;
import db.interfaces.*;
import db.LoanDB;
import db.common.DataAccessException;
import model.*;
import model.common.Tuple;
import model.common.WrongDateSpanException;
import model.item.Item;
import model.item.UtilityItem;
import model.line.LoanItemLine;
import model.line.LoanUtilityItemLine;

public class LoanController {
	private Loan newLoan = null;
	
	// Foreign DBs and Controllers
	private LoanDBIF loanDB = LoanDB.getInstance();
	private ItemControllerIF itemController = ItemController.getInstance();
	private EmployeeControllerIF employeeController = EmployeeController.getInstance();
	private UtilityItemControllerIF utilityItemController = UtilityItemController.getInstance();
	
	private List<Entry<UtilityItem, Integer>> newLoanUtilityItemSummary;
	
	private LoanController() throws DataAccessException {
		/* Define a constructor with throws declaration to allow
		 * easy assignment of implementation of interfaces
		 * above.
		 */
	}
	
	// Singleton boilerplate
	private static LoanController instance;
	public static LoanController getInstance() throws DataAccessException {
		if (instance == null)
			instance = new LoanController();
		
		return instance;
	}
	
	// ============= Getters =============
	public Employee findEmployee(String manr) throws DataAccessException {
		return employeeController.findEmployee(manr);
	}
	
	public Loan getLoan(int id) throws DataAccessException {
		return loanDB.getLoanById(id);
	}
	
	public List<LoanItemLine> getNewLoanItems(){
		if (newLoan == null)
			throw noLoan;
		
		return newLoan.getLoanItemLines();
	}
	
	public List<LoanUtilityItemLine> getNewLoanUtilityItems(){
		if (newLoan == null)
			throw noLoan;
		
		return newLoan.getLoanUtilityItemLines();
	}
	
	public List<Entry<UtilityItem, Integer>> getNewLoanUtilityItemSummary(){
		if (newLoan == null)
			throw noLoan;
		
		return newLoanUtilityItemSummary;
	}
	
	public boolean isInfoSetOnNewLoan() {
		return newLoan.getEmployee() != null && newLoan.getDateStart() != null && newLoan.getDateEnd() != null;
	}
	
	public List<Tuple<Item, Boolean>> getAllItemsWithAvailable() throws DataAccessException {
		errorCheck();
		return itemController.getAllItemsWithAvailable(newLoan.getDateStart(), newLoan.getDateEnd());
	}
	
	public List<Tuple<UtilityItem, Integer>> getAllUtilityItemsWithAvailable() throws DataAccessException {
		errorCheck();
		return utilityItemController.getAllUtilityItemsWithAvailable(newLoan.getDateStart(), newLoan.getDateEnd());
	}
	
	public List<Loan> getAllLoans() throws DataAccessException {
		return loanDB.getAllLoans();
	}
	
	// ============= Setters =============
	private final IllegalStateException noLoan = new IllegalStateException("No new loan has been started");
	public void startNewLoan(User createdByUser) {
		newLoan = new Loan(createdByUser);
		newLoanUtilityItemSummary = new ArrayList<>();
	}
	
	public void setNewLoanEmployee(Employee employee) {
		if (newLoan == null)
			throw noLoan;
		
		newLoan.setEmployee(employee);
	}
	
	public void setNewLoanDateStart(LocalDate dateStart) throws WrongDateSpanException {
		if (newLoan == null)
			throw noLoan;
		
		newLoan.setDateStart(dateStart);
	}
	
	public void setNewLoanDateEnd(LocalDate dateEnd) throws WrongDateSpanException {
		if (newLoan == null)
			throw noLoan;
		
		newLoan.setDateEnd(dateEnd);
	}
	
	public void setNewLoanNotes(String notes) {
		if (newLoan == null)
			throw noLoan;
		
		newLoan.setNotes(notes);
	}
	
	/**
	 * Contain error checks in a single method
	 * for the sake of simplicity
	 */
	private void errorCheck() {
		if (newLoan == null)
			throw noLoan;
		else if (newLoan.getEmployee() == null)
			throw new IllegalStateException("Employee has not been set on new loan");
		else if (newLoan.getDateStart() == null)
			throw new IllegalStateException("Start-date has not been set on new loan");
		else if (newLoan.getDateEnd() == null)
			throw new IllegalStateException("End-date has not been set on new loan");
	}
	
	public void addItemToNewLoan(Item item) {
		errorCheck();
		newLoan.addItem(item);
	}
	
	public void removeItemFromNewLoan(Item item) {
		errorCheck();
		newLoan.removeItem(item);
	}
	
	public void addUtilityItemToNewLoan(UtilityItem utilityItem) {
		addUtilityItemToNewLoan(utilityItem, 1);
	}
	
	public void addUtilityItemToNewLoan(UtilityItem utilityItem, int amount) {
		errorCheck();
		newLoan.addUtilityItem(utilityItem, amount);
		
		Entry<UtilityItem, Integer> se = newLoanUtilityItemSummary.stream()
			.filter(x -> x.getKey().getId() == utilityItem.getId())
			.findFirst()
			.orElse(null);
		
		if (se != null)
			se.setValue(se.getValue() + amount);
		else
			newLoanUtilityItemSummary.add(new SimpleEntry<UtilityItem, Integer>(utilityItem, amount));
	}
	
	public void removeUtilityItemFromNewLoan(UtilityItem utilityItem) {
		removeUtilityItemFromNewLoan(utilityItem, 1);
	}
	
	public void removeUtilityItemFromNewLoan(UtilityItem utilityItem, int amount) {
		errorCheck();
		newLoan.removeUtilityItem(utilityItem, amount);
		
		Entry<UtilityItem, Integer> se = newLoanUtilityItemSummary.stream()
			.filter(x -> x.getKey().getId() == utilityItem.getId())
			.findFirst()
			.orElse(null);

		if (se != null) {
			se.setValue(se.getValue() - amount);
			if (se.getValue() <= 0)
				newLoanUtilityItemSummary.remove(se);
		}
	}
	
	public void pickupItemsFromExistingLoan(Loan l, List<LoanItemLine> items) throws DataAccessException {
		// Only process items which have not yet been picked up
		List<LoanItemLine> availItems = items.stream()
				.filter(i -> i.getDatePickedUp() == null)
				.collect(Collectors.toList());
		
		LocalDateTime now = LocalDateTime.now();
		availItems.forEach(i -> i.setDatePickedUp(now));
		
		loanDB.saveLoanItemLines(l, availItems);
	}
	
	public void returnItemsFromExistingLoan(Loan l, List<LoanItemLine> items) throws DataAccessException {
		// Only process items which have been picked up already, but not returned yet.
		List<LoanItemLine> pickedUpItems = items.stream()
				.filter(i -> i.getDatePickedUp() != null)
				.filter(i -> i.getDateReturned() == null)
				.collect(Collectors.toList());
		
		LocalDateTime now = LocalDateTime.now();
		pickedUpItems.stream().forEach(i -> i.setDateReturned(now));
		
		loanDB.saveLoanItemLines(l, pickedUpItems);
	}
	
	public void pickupUtilityItemsFromExistingLoan(Loan l, List<LoanUtilityItemLine> utilityItems) throws DataAccessException {
		// Only process items which have not yet been picked up
		List<LoanUtilityItemLine> availItems = utilityItems.stream()
				.filter(i -> i.getDatePickedUp() == null)
				.collect(Collectors.toList());
		
		LocalDateTime now = LocalDateTime.now();
		availItems.forEach(i -> i.setDatePickedUp(now));
		
		loanDB.saveLoanUtilityItemLines(l, availItems);
	}
	
	public void returnUtilityItemsFromExistingLoan(Loan l, List<LoanUtilityItemLine> utilityItems) throws DataAccessException {
		// Only process items which have been picked up already, but not returned yet.
		List<LoanUtilityItemLine> availItems = utilityItems.stream()
				.filter(i -> i.getDatePickedUp() != null)
				.filter(i -> i.getDateReturned() == null)
				.collect(Collectors.toList());
		
		LocalDateTime now = LocalDateTime.now();
		availItems.stream().forEach(i -> i.setDateReturned(now));

		loanDB.saveLoanUtilityItemLines(l, availItems);
	}
	
	/**
	 * Creates the loan and returns the loan-id
	 * @return Id of the newly created loan
	 * @throws DataAccessException
	 */
	public int finalizeLoan() throws DataAccessException {
		errorCheck();
		
		if (newLoan.getLoanItemLines().isEmpty() && newLoan.getLoanUtilityItemLines().isEmpty())
			throw new IllegalStateException("No items has been added to new loan");
		
		loanDB.saveLoan(newLoan);
		int newId = newLoan.getId();
		newLoan = null;
		return newId;
	}
}