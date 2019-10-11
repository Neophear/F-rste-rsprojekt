package gui.components;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public abstract class BaseTableModel<T> extends AbstractTableModel {
	private static final long serialVersionUID = 1548001057652965432L;
	private List<T> list;
	private String[] header;
	public BaseTableModel(String[] header) {
		this(header, new ArrayList<T>());
	}
	
	@Override
	public String getColumnName(int colIndex) {
		return header[colIndex];
	}
	
	public BaseTableModel(String[] header, List<T> list) {
		this.header = header;
		this.list = list;
	}
	
	public void setList(List<T> list) {
		this.list = list;
		fireTableDataChanged();
	}
	
	public void fillList(List<T> list) {
		this.list.addAll(list);
		fireTableDataChanged();
	}
	
	@Override
	public int getColumnCount() {
		return header.length;
	}
	
	protected T getObjectAt(int rowIndex) {
		return list.get(rowIndex);
	}
	
	/**
	 * Gets the selected <T> in the JTable, even after sorting/filtering
	 * @param tbl	JTable with selected <T>
	 * @return		Selected <T>
	 */
	public T getSelected(JTable tbl) {
		return tbl.getSelectedRow() >= 0 ? list.get(tbl.convertRowIndexToModel(tbl.getSelectedRow())) : null;
	}
	
	/**
	 * Gets all selected <T> in JTable, even after sorting/filtering. If none is selected, returns an empty List
	 * @param tbl
	 * @return List<T>
	 */
	public List<T> getAllSelected(JTable tbl) {
		int[] selectedRows = tbl.getSelectedRows();
		
		List<T> selectedItems = new ArrayList<>(selectedRows.length);
		
		for (int i : selectedRows)
			selectedItems.add(list.get(tbl.convertRowIndexToModel(i)));
		
		return selectedItems;
	}

	@Override
	public int getRowCount() {
		return list.size();
	}
}
