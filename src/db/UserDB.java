package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.common.AbstractDB;
import db.common.DataAccessException;
import db.connection.DBConnection;
import db.interfaces.UserDBIF;
import model.User;

public class UserDB extends AbstractDB<User> implements UserDBIF{
	private PreparedStatement ps_selectById,
							  ps_selectByMANR;
	
	// Singleton boilerplate
	private static UserDB instance;
	public static UserDB getInstance() throws DataAccessException {
		if(instance == null)
			instance = new UserDB();
		return instance;
	}
	
	private UserDB() throws DataAccessException {
		try {
			Connection dbcon = DBConnection.getInstance().getConnection();
			ps_selectById = dbcon.prepareStatement("select * from vwUser where id=?");
			ps_selectByMANR = dbcon.prepareStatement("select * from vwUser where manr=?");
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
	}
	
	@Override
	public User getUserById(int id) throws DataAccessException {
		try {
			ps_selectById.setInt(1, id);
		} catch(SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return preparedStatementToClass(ps_selectById);
	}
	
	@Override
	public User findUser(String manr) throws DataAccessException {
		try {
			ps_selectByMANR.setString(1, manr);
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return preparedStatementToClass(ps_selectByMANR);
	}
	
	@Override
	protected User rsToClass(ResultSet rs) throws DataAccessException, SQLException {
		return new User(rs.getInt("id"),
						rs.getString("manr"),
						rs.getString("name"),
						rs.getString("unit"),
						rs.getString("notes"),
						rs.getString("password"));
	}
}
