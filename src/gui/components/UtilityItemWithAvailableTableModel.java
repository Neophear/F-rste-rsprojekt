package gui.components;

import model.common.Tuple;
import model.item.UtilityItem;

public class UtilityItemWithAvailableTableModel extends BaseTableModel<Tuple<UtilityItem, Integer>> {
	private static final long serialVersionUID = 2445855753964168693L;
	
	public UtilityItemWithAvailableTableModel() {
		super(new String[] {"Navn", "Beskrivelse", "Antal tilgængelig"});
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex){
		switch(columnIndex) {
			case 0: return String.class;
			case 1: return String.class;
			case 2: return Integer.class;
			default: return null;
		}
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex) {
			case 0: return getObjectAt(rowIndex).x.getName();
			case 1: return getObjectAt(rowIndex).x.getDescription();
			case 2: return getObjectAt(rowIndex).y;
			default: return null;
		}
	}
}
