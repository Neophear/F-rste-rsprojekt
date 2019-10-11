package db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import db.common.AbstractDB;
import db.common.DataAccessException;
import db.connection.DBConnection;
import db.interfaces.*;
import model.Loan;
import model.line.LoanUtilityItemLine;

public class LoanUtilityItemLineDB extends AbstractDB<LoanUtilityItemLine> implements LoanUtilityItemLineDBIF {
	private PreparedStatement ps_selectByLoan,
							  ps_update;
	private CallableStatement cs_create;
	
	// Implementations to foreign DB interfaces
	private UtilityItemDBIF utilityItemDB = UtilityItemDB.getInstance();
	
	// Singleton boilerplate
	private static LoanUtilityItemLineDB instance;
	public static LoanUtilityItemLineDB getInstance() throws DataAccessException {
		if (instance == null) {
			instance = new LoanUtilityItemLineDB();
		}
		return instance;
	}
	
	private LoanUtilityItemLineDB() throws DataAccessException {
		try {
			Connection dbcon = DBConnection.getInstance().getConnection();
			this.ps_selectByLoan = dbcon.prepareStatement("select * from LoanUtilityItemLine where loan_id=?");
			this.cs_create       = dbcon.prepareCall("{call dbo.InsertLoanUtilityItemLine(?, ?, ?)}");
			this.ps_update       = dbcon.prepareStatement("update LoanUtilityItemLine set datePickedUp=?,dateReturned=?,lost=? where id=?");
		} catch (SQLException e) {
			throw new DataAccessException("Error while preparing statements", e);
		}
	}
	
	@Override
	public List<LoanUtilityItemLine> getLoanUtilityItemLinesByLoan(Loan l) throws DataAccessException {
		try {
			ps_selectByLoan.setInt(1, l.getId());
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return preparedStatementToClassList(ps_selectByLoan);
	}

	@Override
	public void saveLoanUtilityItemLines(Loan l, List<LoanUtilityItemLine> e) throws DataAccessException {
		for (LoanUtilityItemLine line : e) {
			if (line.getId() < 1) {
				createLoanUtilityItemLine(l, line);
			} else {
				updateLoanUtilityItemLine(line);
			}
		}
	}
	
	private void createLoanUtilityItemLine(Loan l, LoanUtilityItemLine line) throws DataAccessException {
		try {
			cs_create.setInt(1, l.getId());
			cs_create.setInt(2, line.getItem().getId());
			cs_create.registerOutParameter(3, java.sql.Types.INTEGER);
			cs_create.execute();
			int lastId = cs_create.getInt(3);
			
			if (lastId == -1)
				throw new DataAccessException("Not enough UtilityItem with id " + line.getItem().getId() + " is available");
			else
				line.setId(lastId);
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	private void updateLoanUtilityItemLine(LoanUtilityItemLine line) throws DataAccessException {
		try {
			ps_update.setTimestamp(1, line.getDatePickedUp() == null ? null : Timestamp.valueOf(line.getDatePickedUp()));
			ps_update.setTimestamp(2, line.getDateReturned() == null ? null : Timestamp.valueOf(line.getDateReturned()));
			ps_update.setBoolean(3, line.isLost());
			ps_update.setInt(4, line.getId());
			
			preparedStatementExecute(ps_update);
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	@Override
	protected LoanUtilityItemLine rsToClass(ResultSet rs) throws DataAccessException, SQLException {
		return new LoanUtilityItemLine(rs.getInt("id"),
									   utilityItemDB.getUtilityItemById(rs.getInt("utilityItem_id")),
									   rs.getTimestamp("datePickedUp") == null ? null : rs.getTimestamp("datePickedUp").toLocalDateTime(),
									   rs.getTimestamp("dateReturned") == null ? null : rs.getTimestamp("dateReturned").toLocalDateTime(),
									   rs.getBoolean("lost"));
	}
}