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
import db.interfaces.ItemDBIF;
import db.interfaces.LoanItemLineDBIF;
import model.Loan;
import model.line.LoanItemLine;

public class LoanItemLineDB extends AbstractDB<LoanItemLine> implements LoanItemLineDBIF {
	private PreparedStatement ps_selectByLoan,
							  ps_update;
	private CallableStatement cs_create;
	
	// Implementations to foreign DB interfaces
	private ItemDBIF itemDB = ItemDB.getInstance();
	
	// Singleton boilerplate
	private static LoanItemLineDB instance;
	public static LoanItemLineDB getInstance() throws DataAccessException {
		if (instance == null) {
			instance = new LoanItemLineDB();
		}
		return instance;
	}

	private LoanItemLineDB() throws DataAccessException {
		try {
			Connection dbcon = DBConnection.getInstance().getConnection();
			this.ps_selectByLoan        = dbcon.prepareStatement("select * from LoanItemLine where loan_id=?");
			this.ps_update				= dbcon.prepareStatement("update LoanItemLine set datePickedUp = ?, dateReturned = ? where id = ?");
			this.cs_create				= dbcon.prepareCall("{call dbo.InsertLoanItemLine(?, ?, ?)}");
		} catch (SQLException e) {
			throw new DataAccessException("Error while preparing statements", e);
		}
	}

	@Override
	public List<LoanItemLine> getLoanItemLinesByLoan(Loan l) throws DataAccessException {
		try {
			ps_selectByLoan.setInt(1, l.getId());
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return preparedStatementToClassList(ps_selectByLoan);
	}

	@Override
	public void saveLoanItemLines(Loan l, List<LoanItemLine> e) throws DataAccessException {
		for (LoanItemLine line : e) {
			if (line.getId() < 1) {
				createLoanItemLine(l, line);
			} else {
				updateLoanItemLine(line);
			}
		}
	}

	private void createLoanItemLine(Loan l, LoanItemLine line) throws DataAccessException {
		try {
			cs_create.setInt(1, l.getId());
			cs_create.setInt(2, line.getItem().getId());
			cs_create.registerOutParameter(3, java.sql.Types.INTEGER);
			cs_create.execute();
			int lastId = cs_create.getInt(3);
			
			if (lastId == -1)
				throw new DataAccessException("Item with id " + line.getItem().getId() + " is not available");
			else
				line.setId(lastId);
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
	}
	
	private void updateLoanItemLine(LoanItemLine line) throws DataAccessException {
		try {
			ps_update.setTimestamp(1, line.getDatePickedUp() == null ? null : Timestamp.valueOf(line.getDatePickedUp()));
			ps_update.setTimestamp(2, line.getDateReturned() == null ? null : Timestamp.valueOf(line.getDateReturned()));
			ps_update.setInt(3, line.getId());
			ps_update.execute();
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	@Override
	protected LoanItemLine rsToClass(ResultSet rs) throws DataAccessException, SQLException {
		return new LoanItemLine(rs.getInt("id"),
								itemDB.getItemById(rs.getInt("item_id")),
								rs.getTimestamp("datePickedUp") == null ? null : rs.getTimestamp("datePickedUp").toLocalDateTime(),
								rs.getTimestamp("dateReturned") == null ? null : rs.getTimestamp("dateReturned").toLocalDateTime());
	}

}
