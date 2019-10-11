package control;

import control.interfaces.EmployeeControllerIF;
import db.EmployeeDB;
import db.common.DataAccessException;
import db.interfaces.EmployeeDBIF;
import model.Employee;

public class EmployeeController implements EmployeeControllerIF {
	private static EmployeeController instance;
	
	// Implementations to foreign DB interfaces
	private EmployeeDBIF employeeDB = EmployeeDB.getInstance();
	
	private EmployeeController() throws DataAccessException {/* Enable IF assignments */}
	
	public static EmployeeController getInstance() throws DataAccessException {
		if (instance == null)
			instance = new EmployeeController();
		
		return instance;
	}

	@Override
	public Employee findEmployee(String manr) throws DataAccessException {
		return employeeDB.findEmployee(manr);
	}
}