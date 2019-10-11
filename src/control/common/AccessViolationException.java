package control.common;

/**
 * Just a dumb exception to throw in login-related scenarios.
 * @author 1067477
 */
public class AccessViolationException extends Exception {
	private static final long serialVersionUID = -8807876418551262557L;

	public AccessViolationException() {	/**/ }
	public AccessViolationException(String arg0) {
		super(arg0);
	}
	public AccessViolationException(Throwable arg0) {
		super(arg0);
	}
	public AccessViolationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
	public AccessViolationException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}
}
