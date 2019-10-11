package gui.components;

import model.common.Tuple;
import model.item.Item;

public class ItemWithAvailableTableModel extends BaseTableModel<Tuple<Item, Boolean>> {
	private static final long serialVersionUID = -8911704761106556194L;
	
	public ItemWithAvailableTableModel() {
		super(new String[] { "Id", "Serienummer", "Type", "Model", "Status", "Tilgængelig" });
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
			case 0: return Integer.class;
			case 1: return String.class;
			case 2: return String.class;
			case 3: return String.class;
			case 4: return String.class;
			case 5: return String.class;
			default: return null;
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
			case 0: return getObjectAt(rowIndex).x.getId();
			case 1: return getObjectAt(rowIndex).x.getSerial();
			case 2: return getObjectAt(rowIndex).x.getItemType().getName();
			case 3: return getObjectAt(rowIndex).x.getModel();
			case 4: return getObjectAt(rowIndex).x.getStatus().getName();
			case 5: return getObjectAt(rowIndex).y ? "Ja" : "Nej";
			default: return null;
		}
	}
}