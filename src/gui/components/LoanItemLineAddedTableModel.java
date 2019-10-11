package gui.components;

import model.line.LoanItemLine;

public class LoanItemLineAddedTableModel extends BaseTableModel<LoanItemLine> {
	private static final long serialVersionUID = -8911704761106556194L;
	
	public LoanItemLineAddedTableModel() {
		super(new String[] { "Id", "Serienummer", "Type", "Model", "Status" });
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
			case 0: return Integer.class;
			case 1: return String.class;
			case 2: return String.class;
			case 3: return String.class;
			case 4: return String.class;
			default: return null;
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
			case 0: return getObjectAt(rowIndex).getItem().getId();
			case 1: return getObjectAt(rowIndex).getItem().getSerial();
			case 2: return getObjectAt(rowIndex).getItem().getItemType().getName();
			case 3: return getObjectAt(rowIndex).getItem().getModel();
			case 4: return getObjectAt(rowIndex).getItem().getStatus().getName();
			default: return null;
		}
	}
}