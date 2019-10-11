package model.item;

public class Item {
	private int id = -1;			// Id of this item, derived from ItemDB.
	private ItemStatus status;		// Status is if item is lost, decomissioned, available etc.
	private ItemType itemType;		// Item type is "Laptops", "Peripherals", "Desktops" or the like. General category of equipment.
	private String model,			// Item model. eg. "IBM ThinkPad T60"
				   description,		// Item description. eg. "17-inch office display, takes VGA and DVI."
				   serial,			// Internal serial number of item. Used by barcode system.				   
				   notes;			// Misc notes. eg. "Has surface scratches and crack in left-hand corner."
	
	public Item (int id, String model, String description, String serial, ItemType itemType, ItemStatus status, String notes) {
		this.id = id;
		this.model = model;
		this.description = description;
		this.serial = serial;
		this.itemType = itemType;
		this.status = status;
		this.notes = notes;
	}

	// Getters
	public int getId() {
		return id;
	}
	public String getModel() {
		return model;
	}
	public String getDescription() {
		return description;
	}
	public String getSerial() {
		return serial;
	}
	public ItemType getItemType() {
		return itemType;
	}
	public ItemStatus getStatus() {
		return status;
	}
	public String getNotes() {
		return notes;
	}

	// Setters
	public void setModel(String model) {
		this.model = model;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}
	public void setStatus(ItemStatus status) {
		this.status = status;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
}