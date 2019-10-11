package model;

public class User extends Employee {
	/**
	 * This is the users actual password.
	 * This could've been a salted and hashed string,
	 * which would've been fine to throw around like this,
	 * but sadly it's not.
	 * We're running full plaintext here.
	 * Welcome to Fort Knox.
	 */
	private String password;
	
	public User(int id, String manr, String name, String unit, String notes, String password) {
		super(id, manr, name, unit, notes);
		this.setPassword(password);
	}

	// Getters
	public String getPassword() {
		return password;
	}

	// Setters
	public void setPassword(String password) {
		this.password = password;
	}
}
