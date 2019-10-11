package model.item;

public enum ItemStatus {
	OK				(1, "OK"), 
	DECOMISSIONED	(2, "Decomissioned"), 
	LOST			(3, "Lost");
	
	private int id;
	private String name;
	
	private ItemStatus(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return String.format("%s (ID: %d)", this.getName(), id);
	}
	
	public static ItemStatus fromInt(int id) {
		switch(id) {
			case 1:
				return ItemStatus.OK;
			case 2:
				return ItemStatus.DECOMISSIONED;
			case 3:
				return ItemStatus.LOST;
			default:
				throw new EnumConstantNotPresentException(ItemStatus.class, String.format("ItemStatus does not contain a value matching int %d", id));
		}
	}
	
	public int toInt() {
		return id;
	}
	
	public String getName() {
		return name;
	}
}
