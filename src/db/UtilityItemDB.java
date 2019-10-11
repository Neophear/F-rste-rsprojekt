package db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import db.common.AbstractDB;
import db.common.DataAccessException;
import db.connection.DBConnection;
import db.interfaces.UtilityItemDBIF;
import model.common.Tuple;
import model.item.UtilityItem;

public class UtilityItemDB extends AbstractDB<UtilityItem> implements UtilityItemDBIF{
	private PreparedStatement ps_selectById, 
							  ps_selectAll,
							  ps_selectAllWithAvailable;
	
	// Singleton boilerplate
	private static UtilityItemDB instance;
	public static UtilityItemDB getInstance() throws DataAccessException {
		if(instance == null)
			instance = new UtilityItemDB();
		return instance;
	}
	
	private UtilityItemDB() throws DataAccessException {
		try {
			Connection dbcon = DBConnection.getInstance().getConnection();
			this.ps_selectById = dbcon.prepareStatement("select * from UtilityItem where id = ?");
			this.ps_selectAll  = dbcon.prepareStatement("select * UtilityItem");
			this.ps_selectAllWithAvailable = dbcon.prepareStatement("exec dbo.GetUtilityItemsWithAvailable ?, ?");
		} catch(SQLException e) {
			throw new DataAccessException("Error while preparing statements", e);
		}
	}
	
	public UtilityItem getUtilityItemById(int id) throws DataAccessException {
		try {
			ps_selectById.setInt(1,id);
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return preparedStatementToClass(ps_selectById);
	}

	public List<UtilityItem> getAllUtilityItems() throws DataAccessException {
		return preparedStatementToClassList(ps_selectAll);
	}
	
	public List<Tuple<UtilityItem, Integer>> getAllUtilityItemsWithAvailable(LocalDate dateStart, LocalDate dateEnd) throws DataAccessException {
		List<Tuple<UtilityItem, Integer>> lst = new ArrayList<>();
		
		try {
			ps_selectAllWithAvailable.setDate(1, Date.valueOf(dateStart));
			ps_selectAllWithAvailable.setDate(2, Date.valueOf(dateEnd));
		} catch (SQLException e1) {
			throw new DataAccessException(e1.getMessage(), e1);
		}
		
		try (ResultSet rs = ps_selectAllWithAvailable.executeQuery()) {
			while (rs.next())
				lst.add(new Tuple<>(rsToClass(rs), rs.getInt("available")));
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return lst;
	}

	protected UtilityItem rsToClass(ResultSet rs) throws DataAccessException, SQLException {
		return new UtilityItem(rs.getInt("id"), 
							   rs.getString("name"), 
							   rs.getString("description"), 
							   rs.getInt("minStock"), 
							   rs.getInt("currentStock"));
	}
}
