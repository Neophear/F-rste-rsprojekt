package model;

public class Employee {
	private int id = -1;
	private String name,
				   unit,
				   notes,
				   manr;
	
	public Employee(int id, String manr, String name, String unit, String notes) {
		this.id = id;
		this.manr = manr;
		this.name = name;
		this.unit = unit;
		this.notes = notes;
	}
	
	public int getId() { return id; }
	public String getMANR() { return manr; }
	public String getName() { return name; }
	public String getUnit() { return unit; }
	public String getNotes() {return notes; }
	
	public void setMANR(String manr) { this.manr = manr; }
	public void setName(String name) { this.name = name; }
	public void setUnit(String unit) { this.unit = unit; }
	public void setNotes(String notes) {this.notes = notes; }
}
