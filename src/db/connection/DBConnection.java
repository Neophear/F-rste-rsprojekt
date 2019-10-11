package db.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import db.common.DataAccessException;

public class DBConnection {
	private Connection connection = null;
	private static DBConnection dbConnection;
	private static final String driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	DBConnectionCredentialProviderIF credentials = new DBConnectionCredentialProviderDummy();
	
	private DBConnection() throws DataAccessException {
		try {
			Class.forName(driverClass);
			connection = DriverManager.getConnection(credentials.getConnectionString());
		} catch(ClassNotFoundException e) {
			throw new DataAccessException("Missing JDBC driver", e);
		} catch (SQLException e) {
			throw new DataAccessException("Could not connect to database.", e);
		}
	}
	
	public static synchronized DBConnection getInstance() throws DataAccessException {
		if (dbConnection == null) {
			dbConnection = new DBConnection();
		}
		return dbConnection;
	}
	
	/**
	 * Tries to run a small query on the server, and returns if successful
	 * Statement self-disposing, and exception is suppressed on purpose.
	 * @return true if connection to DB is live.
	 */
	public boolean isConnected() {
		boolean connected = false;
		
		try (Statement s = connection.createStatement()) {
			s.executeQuery("select 1");
			connected = true;
		} catch (SQLException e) { /* Suppress */ }
		
		return connected;
	}
	
	public void startTransaction() throws DataAccessException { startTransaction(false); }
	public void startTransaction(boolean fullLock) throws DataAccessException {
		try {
			connection.setAutoCommit(false);
			
			if(fullLock)
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		} catch (SQLException e) {
			throw new DataAccessException("Could not start transaction.", e);
		}
	}
	
	public void commitTransaction() throws DataAccessException {
		try {
			try {
				connection.commit();
				//Sets transaction isolation level back to default
				connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			} finally {
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			throw new DataAccessException("Could not commit transaction", e);
		}
	}
	
	public void rollbackTransaction() throws DataAccessException {
		try {
			try {
				connection.rollback();
				//Sets transaction isolation level back to default
				connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			} finally {
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			throw new DataAccessException("Could not rollback transaction", e);
		}
	}
	
	public int executeInsertWithIdentity(String sql) throws DataAccessException {
		int res = -1;
		try (Statement s = connection.createStatement()) {
			res = s.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			if (res > 0) {
				try (ResultSet rs = s.getGeneratedKeys()) {
					rs.next();
					res = rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException("Could not execute insert (" + sql + ").", e);
		}
		return res;
	}

	public int executeInsertWithIdentity(PreparedStatement ps) throws DataAccessException {
		// requires prepared statement to be created with the additional argument PreparedStatement.RETURN_GENERATED_KEYS  
		int res = -1;
		try {
			res = ps.executeUpdate();
			if (res > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					rs.next();
					res = rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new DataAccessException("Could not execute insert", e);
		}
		return res;
	}

	public Connection getConnection() {
		return connection;
	}

	public void disconnect() {
		try {
			connection.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
}
