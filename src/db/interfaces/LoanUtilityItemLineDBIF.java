package db.interfaces;

import java.util.List;

import db.common.DataAccessException;
import model.Loan;
import model.line.LoanUtilityItemLine;

public interface LoanUtilityItemLineDBIF {
	List<LoanUtilityItemLine> getLoanUtilityItemLinesByLoan(Loan l) throws DataAccessException;
	void saveLoanUtilityItemLines(Loan l, List<LoanUtilityItemLine> e) throws DataAccessException;
}
