package db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import db.common.AbstractDB;
import db.common.DataAccessException;
import db.connection.DBConnection;
import db.interfaces.EmployeeDBIF;
import db.interfaces.LoanDBIF;
import db.interfaces.LoanItemLineDBIF;
import db.interfaces.LoanUtilityItemLineDBIF;
import db.interfaces.UserDBIF;
import model.Loan;
import model.common.WrongDateSpanException;
import model.line.LoanItemLine;
import model.line.LoanUtilityItemLine;

public class LoanDB extends AbstractDB<Loan> implements LoanDBIF {
	private PreparedStatement ps_selectById,
							  ps_selectAll,
							  ps_create,
							  ps_update;
	
	// Implementations to foreign DB interfaces
	EmployeeDBIF            employeeDB            = EmployeeDB.getInstance();
	UserDBIF                userDB				  = UserDB.getInstance();
	LoanItemLineDBIF        loanItemLineDB        = LoanItemLineDB.getInstance();
	LoanUtilityItemLineDBIF loanUtilityItemLineDB = LoanUtilityItemLineDB.getInstance();
	
	// Singleton boilerplate
	private static LoanDB instance;
	public static LoanDB getInstance() throws DataAccessException {
		if (instance == null)
			instance = new LoanDB();
		
		return instance;
	}
	
	private LoanDB() throws DataAccessException {
		try {
			Connection dbcon = DBConnection.getInstance().getConnection();
			this.ps_selectById          = dbcon.prepareStatement("select * from Loan where id=?");
			this.ps_selectAll           = dbcon.prepareStatement("select * from Loan");
			this.ps_create              = dbcon.prepareStatement("insert into Loan(user_id,employee_id,dateStart,dateEnd,notes) values(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			this.ps_update				= dbcon.prepareStatement("update Loan set notes=? where id=?");
		} catch (SQLException e) {
			throw new DataAccessException("Error while preparing statements", e);
		}
	}

	@Override
	public Loan getLoanById(int id) throws DataAccessException {
		try {
			ps_selectById.setInt(1, id);
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return preparedStatementToClass(ps_selectById);
	}

	@Override
	public List<Loan> getAllLoans() throws DataAccessException {
		return preparedStatementToClassList(ps_selectAll);
	}
	
	@Override
	public void saveLoan(Loan l) throws DataAccessException {
		//Start new transaction, with TRANSACTION_SERIALIZABLE. This so that all lines will be inserted before another new loan can start.
		DBConnection.getInstance().startTransaction(true);
		
		try {
			if (l.getId() < 1)
				createLoan(l);
			else
				updateLoan(l);
			
			loanItemLineDB.saveLoanItemLines(l, l.getLoanItemLines());
			loanUtilityItemLineDB.saveLoanUtilityItemLines(l, l.getLoanUtilityItemLines());
			
			//Commit transaction if nothing went wrong
			DBConnection.getInstance().commitTransaction();
		} catch (DataAccessException e1) {
			//Rollback transaction if something went wrong
			DBConnection.getInstance().rollbackTransaction();
			throw e1;
		}
	}
	
	@Override
	public void saveLoanItemLines(Loan l, List<LoanItemLine> e) throws DataAccessException {
		loanItemLineDB.saveLoanItemLines(l, e);
	}

	@Override
	public void saveLoanUtilityItemLines(Loan l, List<LoanUtilityItemLine> e) throws DataAccessException {
		loanUtilityItemLineDB.saveLoanUtilityItemLines(l, e);
	}

	private void createLoan(Loan l) throws DataAccessException {
		try {
			ps_create.setInt(1, l.getCreatedByUser().getId());
			ps_create.setInt(2, l.getEmployee().getId());
			ps_create.setDate(3, Date.valueOf(l.getDateStart()));
			ps_create.setDate(4, Date.valueOf(l.getDateEnd()));
			ps_create.setString(5, l.getNotes());
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		int id = preparedStatementExecuteWithIdentity(ps_create);
		l.setId(id);
	}
	
	private void updateLoan(Loan l) throws DataAccessException {
		try {
			ps_update.setString(1, l.getNotes());
			ps_update.setInt(2, l.getId());
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
	}
	
	@Override
	protected Loan rsToClass(ResultSet rs) throws DataAccessException, SQLException {
		Loan l = null;
		try {
			l = new Loan(rs.getInt("id"),
						 userDB.getUserById(rs.getInt("user_id")),
						 employeeDB.getEmployeeById(rs.getInt("employee_id")),
						 rs.getDate("dateStart").toLocalDate(),
						 rs.getDate("dateEnd").toLocalDate(),
						 rs.getString("notes"));
			
			// Stitch Item- and UtilityItem Lines onto Loan
			l.addLoanItemLines(loanItemLineDB.getLoanItemLinesByLoan(l));
			l.addLoanUtilityItemLines(loanUtilityItemLineDB.getLoanUtilityItemLinesByLoan(l));
		} catch (WrongDateSpanException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return l;
	}
}
