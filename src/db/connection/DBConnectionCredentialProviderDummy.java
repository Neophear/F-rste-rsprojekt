package db.connection;

/***
 * Contain all the no-no parts in this file.
 * One should not use hard-coded passwords, but unfortunately we have to.
 * @author 1067477
 */
public class DBConnectionCredentialProviderDummy implements DBConnectionCredentialProviderIF {
	private static final String dbName        = "dmaa0218_1067477";
	private static final String serverAddress = "kraka.ucn.dk";
	private static final int serverPort       = 1433;
	private static final String userName      = "dmaa0218_1067477";
	private static final String password      = "Password1!";
	
	@Override
	public String getConnectionString() {
		return String.format("jdbc:sqlserver://%s:%d;databaseName=%s;user=%s;password=%s",
				serverAddress, serverPort, dbName, userName, password);
	}
}
