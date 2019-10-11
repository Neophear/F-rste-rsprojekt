package control;

import java.time.LocalDate;
import java.util.List;

import control.interfaces.UtilityItemControllerIF;
import db.UtilityItemDB;
import db.common.DataAccessException;
import db.interfaces.UtilityItemDBIF;
import model.common.Tuple;
import model.item.UtilityItem;

public class UtilityItemController implements UtilityItemControllerIF {
	private static UtilityItemController instance;
	public static UtilityItemController getInstance() throws DataAccessException {
		if(instance == null)
			instance = new UtilityItemController();
		return instance;
	}
	
	// Implementations to foreign DB interfaces
	private UtilityItemDBIF utilityItemDB = UtilityItemDB.getInstance();
	
	private UtilityItemController() throws DataAccessException {/* Enable IF assignments */}
	
	@Override
	public List<Tuple<UtilityItem, Integer>> getAllUtilityItemsWithAvailable(LocalDate dateStart, LocalDate dateEnd) throws DataAccessException{
		return utilityItemDB.getAllUtilityItemsWithAvailable(dateStart, dateEnd);
	}
}