package db.interfaces;

import java.time.LocalDate;
import java.util.List;

import db.common.DataAccessException;
import model.common.Tuple;
import model.item.UtilityItem;

public interface UtilityItemDBIF {
	UtilityItem       getUtilityItemById(int id)        throws DataAccessException;
	List<UtilityItem> getAllUtilityItems()              throws DataAccessException;
	List<Tuple<UtilityItem, Integer>> getAllUtilityItemsWithAvailable(LocalDate dateStart, LocalDate dateEnd) throws DataAccessException;
}
