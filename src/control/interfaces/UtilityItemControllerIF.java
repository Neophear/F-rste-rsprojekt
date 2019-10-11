package control.interfaces;

import java.time.LocalDate;
import java.util.List;

import db.common.DataAccessException;
import model.common.Tuple;
import model.item.UtilityItem;

public interface UtilityItemControllerIF {
	List<Tuple<UtilityItem, Integer>> getAllUtilityItemsWithAvailable(LocalDate dateStart, LocalDate dateEnd) throws DataAccessException;
}