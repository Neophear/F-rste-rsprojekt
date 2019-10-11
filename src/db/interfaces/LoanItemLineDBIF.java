package db.interfaces;

import java.util.List;

import db.common.DataAccessException;
import model.Loan;
import model.line.LoanItemLine;

public interface LoanItemLineDBIF {
	List<LoanItemLine> getLoanItemLinesByLoan(Loan l) throws DataAccessException;
	void saveLoanItemLines(Loan l, List<LoanItemLine> e) throws DataAccessException;
}
