package test.db;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

import db.common.DataAccessException;
import db.connection.DBConnection;

class DBConnectionBasicTest {
	@Test
	void CanConnectToDB () throws SQLException, DataAccessException {
		// Assert that isClosed is false
		assertFalse(DBConnection.getInstance().getConnection().isClosed());
	}
}
