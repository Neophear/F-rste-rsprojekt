package db.interfaces;

import db.common.DataAccessException;
import model.Employee;

public interface EmployeeDBIF {
	Employee	getEmployeeById(int id) throws DataAccessException;
	Employee	findEmployee(String manr)  throws DataAccessException;
}