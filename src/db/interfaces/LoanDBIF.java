package db.interfaces;

import java.util.List;
import db.common.DataAccessException;
import model.Loan;
import model.line.LoanItemLine;
import model.line.LoanUtilityItemLine;

public interface LoanDBIF {
	Loan       getLoanById(int id) throws DataAccessException;
	List<Loan> getAllLoans()       throws DataAccessException;
	void       saveLoan(Loan e)    throws DataAccessException;
	void       saveLoanItemLines(Loan l, List<LoanItemLine> e) throws DataAccessException;
	void       saveLoanUtilityItemLines(Loan l, List<LoanUtilityItemLine> e) throws DataAccessException;
}
