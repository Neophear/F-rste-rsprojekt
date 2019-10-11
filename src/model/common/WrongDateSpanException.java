package model.common;

public class WrongDateSpanException extends Exception {
	private static final long serialVersionUID = -5705923384512591811L;
	
	public WrongDateSpanException(String message) {
		super(message);
	}
	
	public WrongDateSpanException(String message, Throwable e) {
		super(message, e);
	}
}