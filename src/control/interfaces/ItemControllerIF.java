package control.interfaces;

import java.time.LocalDate;
import java.util.List;

import db.common.DataAccessException;
import model.common.Tuple;
import model.item.Item;

public interface ItemControllerIF {
	List<Tuple<Item, Boolean>> getAllItemsWithAvailable(LocalDate dateStart, LocalDate dateEnd)	throws DataAccessException;
}