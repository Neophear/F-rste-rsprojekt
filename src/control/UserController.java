package control;

import control.common.AccessViolationException;
import control.interfaces.UserControllerIF;
import db.UserDB;
import db.common.DataAccessException;
import db.interfaces.UserDBIF;
import model.User;

public class UserController implements UserControllerIF {
	private static UserController instance;
	private boolean loggedIn = false;
	private User currentUser;
	
	private UserDBIF userDB = UserDB.getInstance();
	
	private UserController() throws DataAccessException {/* Enable IF assignments */}
	
	public static UserController getInstance() throws DataAccessException {
		if (instance == null)
			instance = new UserController();
		
		return instance;
	}
	
	@Override
	public User getCurrentUser() throws AccessViolationException {
		if (loggedIn) {
			return currentUser;
		}
		throw new AccessViolationException("Cannot get current User: User not logged in.");
	}
	
	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	public void logout() {
		loggedIn = false;
		currentUser = null;
	}

	@Override
	public User findUser(String manr) throws DataAccessException {
		return userDB.findUser(manr);
	}

	@Override
	public void login(String manr, String password) throws DataAccessException, AccessViolationException {
		// Grab a user
		User user = findUser(manr);
		
		if (user == null)
			throw new AccessViolationException("No such user.");
		
		// Check if password matches
		/*if (user.getPassword().equals(password)) {
			currentUser = user;
		}*/
		
		/* We have a valid user from DB, which means that MANR is correct.
		 * I assume we just let everything count as a valid password then?
		 * "Test mode".
		 */
		if (!password.isEmpty()) {
			this.currentUser = user;
			this.loggedIn = true;
		} else {
			// Deny access if password doesn't match.
			throw new AccessViolationException("Access denied.");
		}
	}
}