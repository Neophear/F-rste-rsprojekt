package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.common.WrongDateSpanException;
import model.item.Item;
import model.item.UtilityItem;
import model.line.LoanItemLine;
import model.line.LoanUtilityItemLine;

public class Loan {
	private int 	  id = -1;			// The actual Id of the loan, derived from DB.
	private LocalDate dateStart,		// Date on which loan is started
					  dateEnd;			// Date on which loan is supposed to end. Does not 
										// necessarily correspond with actual return date of the item.
	private String 	  notes;			// Notes for this loan. 
	private User	  createdByUser;	// User that signed off on the loan
	private Employee  employee;			// Employee which the loan belongs to
	private Boolean   finished = null;
	
	private List<LoanItemLine> loanItemLines = new ArrayList<>();
	private List<LoanUtilityItemLine> loanUtilityItemLines = new ArrayList<>();
	
	/**
	 * Basic constructor for the start-new-loan flow
	 */
	public Loan(User createdByUser) {
		this.createdByUser = createdByUser;
	}
	
	public Loan(int id, User createdByUser, Employee employee, LocalDate dateStart, LocalDate dateEnd, String notes) throws WrongDateSpanException {
		if (dateStart.isAfter(dateEnd))
			throw new WrongDateSpanException("Start-date cannot be after end-date");
		
		this.id = id;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.notes = notes;
		this.createdByUser = createdByUser;
		this.employee = employee;
	}

	// Getters
	public int getId() {
		return id;
	}
	public LocalDate getDateStart() {
		return dateStart;
	}
	public LocalDate getDateEnd() {
		return dateEnd;
	}
	public String getNotes() {
		return notes;
	}
	public User getCreatedByUser() {
		return createdByUser;
	}
	public Employee getEmployee() {
		return employee;
	}
	public boolean isFinished() {
		if (finished == null) {
			boolean result = loanItemLines.stream().allMatch(x -> x.getDateReturned() != null);
			
			if (result)
				result = loanUtilityItemLines.stream().allMatch(x -> x.getDateReturned() != null || x.isLost());
			
			finished = result;
		}
		
		return finished.booleanValue();
	}
	public List<LoanItemLine> getLoanItemLines() {
		return loanItemLines;
	}
	public List<LoanUtilityItemLine> getLoanUtilityItemLines() {
		return loanUtilityItemLines;
	}

	// Setters
	public void setId(int id) { // Needed in LoanDB.
		this.id=id;
	}
	public void setDateStart(LocalDate dateStart) throws WrongDateSpanException {
		if (dateStart != null && dateEnd != null && dateStart.isAfter(dateEnd)) {
			this.dateStart = null;
			throw new WrongDateSpanException("Start-date can't be after end-date");
		}
		
		this.dateStart = dateStart;
	}
	public void setDateEnd(LocalDate dateEnd) throws WrongDateSpanException {
		if (dateStart != null && dateEnd != null && dateStart.isAfter(dateEnd)) {
			this.dateEnd = null;
			throw new WrongDateSpanException("End-date can't be before start-date");
		}
		
		this.dateEnd = dateEnd;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public void setCreatedByUser(User createdByUser) {
		this.createdByUser = createdByUser;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	// Add/remove functionality for lines
	/**
	 * Adds item to the list, if it doesn't already exist. 
	 * @param item
	 */
	public void addItem(Item item) {
		if (loanItemLines.stream().noneMatch(x -> x.getItem().getId() == item.getId()))
			loanItemLines.add(new LoanItemLine(item));
	}
	
	/**
	 * Removes item from the list if it exists
	 * @param item
	 */
	public void removeItem(Item item) {
		loanItemLines.removeIf(x -> x.getItem().getId() == item.getId());
	}
	
	/**
	 * Adds one utilityItem  to the list.
	 * @param utilityItem
	 */
	public void addUtilityItem(UtilityItem utilityItem) {
		addUtilityItem(utilityItem, 1);
	}
	
	/**
	 * Adds an amount of utilityItem to the list.
	 * @param utilityItem
	 * @param amount
	 */
	public void addUtilityItem(UtilityItem utilityItem, int amount) {
		for (int i = 1; i <= amount; i++)
			loanUtilityItemLines.add(new LoanUtilityItemLine(utilityItem));
	}
	
	/**
	 * Removes an utilityItem from the list. If none exists, nothing happens.
	 * @param utilityItem
	 */
	public void removeUtilityItem(UtilityItem utilityItem) {
		removeUtilityItem(utilityItem, 1);
	}
	
	/**
	 * Removes up to an amount of UtilityItem. If parameter amount is equal or greater
	 * than the actual amount contained list, all utilityItems gets removed.
	 * @param utilityItem Item to remove
	 * @param amount Number of items to remove
	 */
	public void removeUtilityItem(UtilityItem utilityItem, int amount) {
		final List<LoanUtilityItemLine> duplicate = new ArrayList<>(loanUtilityItemLines);
		duplicate.stream()
			.filter(x -> x.getItem().getId() == utilityItem.getId())
			.limit(amount)
			.forEach(loanUtilityItemLines::remove);
	}
	
	/***
	 * Adds a number of LoanItemLines after class has been created.
	 * Exclusively used in LoanDB.
	 * @param loanItemLines lines to add
	 */
	public void addLoanItemLines(List<LoanItemLine> loanItemLines) {
		this.loanItemLines.addAll(loanItemLines);
	}
	
	/***
	 * Adds a number of LoanUtilityItemLines after class has been created.
	 * Exclusively used in LoanDB.
	 * @param loanUtilityItemLines lines to add
	 */
	public void addLoanUtilityItemLines(List<LoanUtilityItemLine> loanUtilityItemLines) {
		this.loanUtilityItemLines.addAll(loanUtilityItemLines);
	}	
}
