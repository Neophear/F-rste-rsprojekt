package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.common.AbstractDB;
import db.common.DataAccessException;
import db.connection.DBConnection;
import db.interfaces.EmployeeDBIF;
import model.Employee;

public class EmployeeDB extends AbstractDB<Employee> implements EmployeeDBIF{
	private PreparedStatement ps_selectById,
							  ps_selectByMANR;

	// Singleton boilerplate
	private static EmployeeDB instance;
	public static EmployeeDB getInstance() throws DataAccessException {
		if(instance == null)
			instance = new EmployeeDB();
		return instance;
	}
	
	private EmployeeDB() throws DataAccessException {
		try {
			Connection dbcon = DBConnection.getInstance().getConnection();
			this.ps_selectById = dbcon.prepareStatement("select * from Employee where id = ?");
			this.ps_selectByMANR = dbcon.prepareStatement("select * from Employee where manr = ?");
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	@Override
	public Employee getEmployeeById(int id) throws DataAccessException {
		try {
			ps_selectById.setInt(1, id);
		} catch(SQLException e) {
			throw new DataAccessException (e.getMessage(), e);
		}
		return preparedStatementToClass(ps_selectById);
	}

	@Override
	public Employee findEmployee(String manr) throws DataAccessException {
		try {
			ps_selectByMANR.setString(1, manr);
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return preparedStatementToClass(ps_selectByMANR);
	}

	@Override
	protected Employee rsToClass(ResultSet rs) throws DataAccessException, SQLException {
		return new Employee(rs.getInt("id"),
							rs.getString("manr"),
							rs.getString("name"),
							rs.getString("unit"),
							rs.getString("notes"));
	}
}
