package control.interfaces;

import db.common.DataAccessException;
import model.Employee;

public interface EmployeeControllerIF {
	Employee findEmployee(String manr) throws DataAccessException;
}