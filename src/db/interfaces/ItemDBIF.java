package db.interfaces;

import java.time.LocalDate;
import java.util.List;

import db.common.DataAccessException;
import model.common.Tuple;
import model.item.Item;

public interface ItemDBIF {
	Item       getItemById(int id) throws DataAccessException;
	/**
	 * Function to return a list of Tuples which consists of Item and Boolean, represents availability, between dateStart and dateEnd
	 * @param dateStart
	 * @param dateEnd
	 * @return List of Tuple<Item, Boolean>
	 * @throws DataAccessException
	 */
	List<Tuple<Item, Boolean>> getAllItemsWithAvailable(LocalDate dateStart, LocalDate dateEnd) throws DataAccessException;
}