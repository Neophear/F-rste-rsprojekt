package db.common;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.connection.DBConnection;

/**
 * AbstractDB is a simple DB-class that attempts to remove
 * a good portion of the boilerplate code from the actual
 * DB-classes. Parsing ResultSets is practically the same
 * every single time.
 * @author 1067477
 *
 * @param <T>
 */
public abstract class AbstractDB<T> {
	/**
	 * Execute a PreparedStatement and return the resulting list of objects
	 * @param ps PreparedStatement to execute
	 * @return a List of objects
	 * @throws DataAccessException
	 */
	protected List<T> preparedStatementToClassList(PreparedStatement ps) throws DataAccessException {
		List<T> lst = new ArrayList<>();
		try (ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				lst.add(rsToClass(rs));
			}
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return lst;
	}
	
	/**
	 * Execute a PreparedStatement and return the resulting object
	 * @param ps PreparedStatement to execute
	 * @return An object
	 * @throws DataAccessException
	 */
	protected T preparedStatementToClass(PreparedStatement ps) throws DataAccessException {
		try (ResultSet rs = ps.executeQuery()) {
			if (rs.next())
				return rsToClass(rs);
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * Execute a PreparedStatement using a transaction
	 * @param ps PreparedStatement
	 * @throws DataAccessException
	 */
	protected void preparedStatementExecute(PreparedStatement ps) throws DataAccessException {
		try {
			ps.execute();
		} catch (SQLException ex) {
			throw new DataAccessException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * Execute a PreparedStatement using a transaction, and get the key from the affected fow in return.
	 * @param ps PreparedStatement
	 * @return Key from the affected row
	 * @throws DataAccessException
	 */
	protected int preparedStatementExecuteWithIdentity(PreparedStatement ps) throws DataAccessException {
		return DBConnection.getInstance().executeInsertWithIdentity(ps);
	}
	
	/**
	 * Convert a ResultSet to a class. Is overwritten by child classes. 
	 * @param rs ResultSet to interpret
	 * @return A class
	 * @throws DataAccessException
	 * @throws SQLException
	 */
	protected abstract T rsToClass(ResultSet rs) throws DataAccessException, SQLException;
}
