package db.interfaces;

import db.common.DataAccessException;
import model.User;

public interface UserDBIF {
	User getUserById(int id) throws DataAccessException;
	User findUser(String manr) throws DataAccessException;
}
