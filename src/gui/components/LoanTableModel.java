package gui.components;

import java.time.LocalDate;

import model.Loan;

public class LoanTableModel extends BaseTableModel<Loan> {
	private static final long serialVersionUID = 7151365076669489738L;

	public LoanTableModel() {
		super(new String[] { "Id", "MANR", "Navn", "Startdato", "Slutdato", "Afsluttet" });
	}

	@Override
	public Object getValueAt(int rowIndex, int colIndex) {
		switch (colIndex) {
			case 0: return getObjectAt(rowIndex).getId();
			case 1: return getObjectAt(rowIndex).getEmployee().getMANR();
			case 2: return getObjectAt(rowIndex).getEmployee().getName();
			case 3: return getObjectAt(rowIndex).getDateStart();
			case 4: return getObjectAt(rowIndex).getDateEnd();
			case 5: return getObjectAt(rowIndex).isFinished() ? "Ja" : "Nej";
			default: return null;
		}
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
			case 0: return Integer.class;
			case 1: return String.class;
			case 2: return String.class;
			case 3: return LocalDate.class;
			case 4: return LocalDate.class;
			case 5: return String.class;
			default: return null;
		}
	}
}