package gui.components;

import model.line.LoanUtilityItemLine;

public class LoanUtilityItemLineTableModel extends BaseTableModel<LoanUtilityItemLine>{
	private static final long serialVersionUID = 2445855753964168693L;
	
	public LoanUtilityItemLineTableModel() {
		super(new String[] {"Navn", "Beskrivelse", "Hentet", "Afleveret"});
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex){
		switch(columnIndex) {
			case 0: return String.class;
			case 1: return String.class;
			case 2: return String.class;
			case 3: return String.class;
			default: return null;
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex) {
			case 0: return getObjectAt(rowIndex).getItem().getName();
			case 1: return getObjectAt(rowIndex).getItem().getDescription();
			case 2: return getObjectAt(rowIndex).getDatePickedUp();
			case 3: return getObjectAt(rowIndex).getDateReturned();
			default: return null;
		}
	}

}
