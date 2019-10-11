package gui.components;

import java.util.Map.Entry;

import model.item.UtilityItem;

public class LoanUtilityItemLineSummaryTableModel extends BaseTableModel<Entry<UtilityItem, Integer>> {
	private static final long serialVersionUID = 1763946308596282212L;

	public LoanUtilityItemLineSummaryTableModel() {
		super(new String[] { "Navn", "Beskrivelse", "Antal tilføjet" });
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
			case 0: return getObjectAt(rowIndex).getKey().getName();
			case 1: return getObjectAt(rowIndex).getKey().getDescription();
			case 2: return getObjectAt(rowIndex).getValue();
			default: return null;
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch(columnIndex) {
			case 0: return String.class;
			case 1: return String.class;
			case 2: return Integer.class;
			default: return null;
		}
	}
}