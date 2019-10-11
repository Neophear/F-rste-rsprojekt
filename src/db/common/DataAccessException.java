package db.common;

/**
 * 
 * @author knol
 * @version 2018-08-30
 */
public class DataAccessException extends Exception {
	private static final long serialVersionUID = 4011881544989284445L;
	public DataAccessException(String message) {
		super(message);
	}
	public DataAccessException(String message, Throwable e) {
		super(message, e);
	}
}
