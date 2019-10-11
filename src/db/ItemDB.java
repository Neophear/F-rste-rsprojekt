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
import db.interfaces.ItemDBIF;
import model.common.Tuple;
import model.item.Item;
import model.item.ItemStatus;
import model.item.ItemType;

public class ItemDB extends AbstractDB<Item> implements ItemDBIF {
	private PreparedStatement ps_selectById,
							  ps_selectAllWithAvailable;

	// Singleton boilerplate
	private static ItemDB instance;
	public static ItemDB getInstance() throws DataAccessException {
		if(instance == null)
			instance = new ItemDB();
		return instance;
	}

	private ItemDB() throws DataAccessException {
		try {
			Connection dbcon = DBConnection.getInstance().getConnection();
			this.ps_selectById = dbcon.prepareStatement("select * from Item where id = ?");
			this.ps_selectAllWithAvailable = dbcon.prepareStatement("exec dbo.GetItemsWithAvailable ?, ?");
		}
		catch (SQLException e) {
			throw new DataAccessException("Error while preparing statements", e);
		}
	}
	
	@Override
	public Item getItemById(int id) throws DataAccessException {
		try {
			ps_selectById.setInt(1, id);
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return preparedStatementToClass(ps_selectById);
	}

	@Override
	protected Item rsToClass(ResultSet rs) throws DataAccessException, SQLException {
		return new Item(rs.getInt("id"), 
						rs.getString("model"), 
						rs.getString("description"), 
						rs.getString("serial"), 
						ItemType.fromInt(rs.getInt("itemType_id")), 
						ItemStatus.fromInt(rs.getInt("itemStatus_id")), 
						rs.getString("notes"));
	}

	@Override
	public List<Tuple<Item, Boolean>> getAllItemsWithAvailable(LocalDate dateStart, LocalDate dateEnd) throws DataAccessException {
		List<Tuple<Item, Boolean>> lst = new ArrayList<>();
		
		try {
			ps_selectAllWithAvailable.setDate(1, Date.valueOf(dateStart));
			ps_selectAllWithAvailable.setDate(2, Date.valueOf(dateEnd));
		} catch (SQLException e1) {
			throw new DataAccessException(e1.getMessage(), e1);
		}
		
		try (ResultSet rs = ps_selectAllWithAvailable.executeQuery()) {
			while (rs.next())
				lst.add(new Tuple<>(rsToClass(rs), rs.getBoolean("available")));
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return lst;
	}
}