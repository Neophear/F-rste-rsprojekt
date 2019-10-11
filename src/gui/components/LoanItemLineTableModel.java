package gui.components;

import model.line.LoanItemLine;

public class LoanItemLineTableModel extends BaseTableModel<LoanItemLine> {
	private static final long serialVersionUID = -6631606467503234402L;
	
	public LoanItemLineTableModel() {
		super(new String[] {"Serienummer",  "Model", "Type", "Notes", "Hentet", "Afleveret"});
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex){
		switch(columnIndex) {
			case 0: return String.class;
			case 1: return String.class;
			case 2: return String.class;
			case 3: return String.class;
			case 4: return String.class;
			case 5: return String.class;
			default: return null;
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnsIndex) {
		switch(columnsIndex) {
			case 0: return getObjectAt(rowIndex).getItem().getSerial();
			case 1: return getObjectAt(rowIndex).getItem().getModel();
			case 2: return getObjectAt(rowIndex).getItem().getItemType().getName();
			case 3: return getObjectAt(rowIndex).getItem().getNotes();
			case 4: return getObjectAt(rowIndex).getDatePickedUp();
			case 5: return getObjectAt(rowIndex).getDateReturned();
			default: return null;
		}
	}

}
