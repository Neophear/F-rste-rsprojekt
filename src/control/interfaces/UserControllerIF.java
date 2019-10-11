package control.interfaces;

import control.common.AccessViolationException;
import db.common.DataAccessException;
import model.User;

public interface UserControllerIF {
	User findUser(String manr) throws DataAccessException;
	User getCurrentUser() throws AccessViolationException;
	void login(String manr, String password) throws DataAccessException, AccessViolationException;
}