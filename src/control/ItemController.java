package control;

import java.time.LocalDate;
import java.util.List;

import control.interfaces.ItemControllerIF;
import db.ItemDB;
import db.common.DataAccessException;
import db.interfaces.ItemDBIF;
import model.common.Tuple;
import model.item.Item;

public class ItemController implements ItemControllerIF {
	private static ItemController instance;
	public static ItemController getInstance() throws DataAccessException {
		if (instance == null)
			instance = new ItemController();
		
		return instance;
	}
	
	// Implementations to foreign DB interfaces
	private ItemDBIF itemDB = ItemDB.getInstance();
	
	private ItemController() throws DataAccessException {/* Enable IF assignments */}
	
	@Override
	public List<Tuple<Item, Boolean>> getAllItemsWithAvailable(LocalDate dateStart, LocalDate dateEnd) throws DataAccessException {
		return itemDB.getAllItemsWithAvailable(dateStart, dateEnd);
	}
}