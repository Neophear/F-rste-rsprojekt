package model.item;

public enum ItemType {
	LAPTOP		(1, "Laptop"), 
	DESKTOP		(2, "Desktop"), 
	PRINTER		(3, "Printer");
	
	private int id;
	private String name;
	
	private ItemType(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return String.format("%s (ID: %d)", this.getName(), id);
	}
	
	public static ItemType fromInt(int id) {
		switch(id) {
			case 1:
				return ItemType.LAPTOP;
			case 2:
				return ItemType.DESKTOP;
			case 3:
				return ItemType.PRINTER;
			default:
				throw new EnumConstantNotPresentException(ItemType.class, String.format("ItemType does not contain a value matching int %d", id));
		}
	}
	
	public int toInt() {
		return id;
	}
	
	public String getName() {
		return name;
	}
}
