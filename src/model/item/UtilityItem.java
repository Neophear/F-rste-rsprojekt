package model.item;

public class UtilityItem {
	private String	name,			// Item name, eg. "USB laser mouse"
				  	description;	// Item description, eg. "Common USB laser mouse. 1.5m cord."
	private int   	id = -1,		// Item id in DB
					minStock,		// Minimum number of items required in stock
					currentStock;   // Current number of items in stock
	
	public UtilityItem(int id, String name, String description, int minStock, int currentStock) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.minStock = minStock;
		this.currentStock = currentStock;
	}

	// Getters
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public int getMinStock() {
		return minStock;
	}
	public int getCurrentStock() {
		return currentStock;
	}

	// Setters
	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setMinStock(int minStock) {
		this.minStock = minStock;
	}
	public void setCurrentStock(int currentStock) {
		this.currentStock = currentStock;
	}
}
