package gui;
import java.awt.GridBagLayout;
import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import javax.swing.JTextField;

import control.LoanController;
import db.common.DataAccessException;
import gui.components.DialogBox;
import gui.components.LoanItemLineTableModel;
import gui.components.LoanUtilityItemLineTableModel;
import model.Loan;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.BoxLayout;

public class PnlLoanView extends BasePanel {
	private static final long serialVersionUID = -8984032584203532844L;
	private JTextField txtDateStart;
	private JTextField txtDateEnd;
	private JTable tblUtilityItems;
	private JTable tblItems;

	private JLabel lblName = new JLabel();
	private LoanItemLineTableModel loanItemLineTableModel;
	private LoanUtilityItemLineTableModel loanUtilityItemLineTableModel;
	private Loan loan;
	private LoanController loanCtrl;

	/**
	 * Create the panel.
	 */
	public PnlLoanView(int id) {
		super("Udlån " + id);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0};
		setLayout(gridBagLayout);

		lblName.setText("lblName");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		add(lblName, gbc_lblName);
		
		JTextArea txtNote = new JTextArea();
		txtNote.setEditable(false);
		GridBagConstraints gbc_txtNote = new GridBagConstraints();
		gbc_txtNote.gridwidth = 2;
		gbc_txtNote.gridheight = 2;
		gbc_txtNote.anchor = GridBagConstraints.NORTH;
		gbc_txtNote.insets = new Insets(0, 0, 5, 0);
		gbc_txtNote.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNote.gridx = 2;
		gbc_txtNote.gridy = 0;
		add(txtNote, gbc_txtNote);
		
		txtDateStart = new JTextField();
		txtDateStart.setEditable(false);
		txtDateStart.setText("");
		GridBagConstraints gbc_txtDateStart = new GridBagConstraints();
		gbc_txtDateStart.gridwidth = 1;
		gbc_txtDateStart.insets = new Insets(0, 0, 5, 5);
		gbc_txtDateStart.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDateStart.gridx = 0;
		gbc_txtDateStart.gridy = 1;
		add(txtDateStart, gbc_txtDateStart);
		txtDateStart.setColumns(10);
		
		txtDateEnd = new JTextField();
		txtDateEnd.setEditable(false);
		txtDateEnd.setText("");
		GridBagConstraints gbc_txtDateEnd = new GridBagConstraints();
		gbc_txtDateEnd.gridwidth = 1;
		gbc_txtDateEnd.insets = new Insets(0, 0, 5, 5);
		gbc_txtDateEnd.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDateEnd.gridx = 0;
		gbc_txtDateEnd.gridy = 2;
		add(txtDateEnd, gbc_txtDateEnd);
		txtDateEnd.setColumns(10);

		JLabel lblItems = new JLabel("Materiel");
		GridBagConstraints gbc_lblItems = new GridBagConstraints();
		gbc_lblItems.gridwidth = 3;
		gbc_lblItems.insets = new Insets(0, 0, 5, 5);
		gbc_lblItems.gridx = 0;
		gbc_lblItems.gridy = 3;
		add(lblItems, gbc_lblItems);

		JScrollPane spItems = new JScrollPane();
		GridBagConstraints gbc_spItems = new GridBagConstraints();
		gbc_spItems.gridwidth = 3;
		gbc_spItems.insets = new Insets(0, 0, 5, 5);
		gbc_spItems.fill = GridBagConstraints.BOTH;
		gbc_spItems.gridx = 0;
		gbc_spItems.gridy = 4;
		add(spItems, gbc_spItems);
		
		loanItemLineTableModel = new LoanItemLineTableModel();
		tblItems = new JTable(loanItemLineTableModel);
		spItems.setViewportView(tblItems);
				
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 3;
		gbc_panel_1.gridy = 4;
		add(panel_1, gbc_panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
				
		JButton btnHandOutItem = new JButton("Udlever");
		panel_1.add(btnHandOutItem);
						
		JButton btnReturnItem = new JButton("Aflever");
		panel_1.add(btnReturnItem);
		btnReturnItem.addActionListener(this::btnReturnItem_clicked);
		btnHandOutItem.addActionListener(this::btnHandOutItem_clicked);

		JLabel lblUtilityItems = new JLabel("Forbrugsmateriel");
		GridBagConstraints gbc_lblUtilityItems = new GridBagConstraints();
		gbc_lblUtilityItems.gridwidth = 3;
		gbc_lblUtilityItems.insets = new Insets(0, 0, 5, 5);
		gbc_lblUtilityItems.gridx = 0;
		gbc_lblUtilityItems.gridy = 5;
		add(lblUtilityItems, gbc_lblUtilityItems);

		JScrollPane spUtilityItems = new JScrollPane();
		GridBagConstraints gbc_spUtilityItems = new GridBagConstraints();
		gbc_spUtilityItems.gridwidth = 3;
		gbc_spUtilityItems.insets = new Insets(0, 0, 0, 5);
		gbc_spUtilityItems.fill = GridBagConstraints.BOTH;
		gbc_spUtilityItems.gridx = 0;
		gbc_spUtilityItems.gridy = 6;
		add(spUtilityItems, gbc_spUtilityItems);

		loanUtilityItemLineTableModel = new LoanUtilityItemLineTableModel();
		tblUtilityItems = new JTable(loanUtilityItemLineTableModel);
		spUtilityItems.setViewportView(tblUtilityItems);
								
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 3;
		gbc_panel.gridy = 6;
		add(panel, gbc_panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JButton btnHandOutUItem = new JButton("Udlever");
		panel.add(btnHandOutUItem);
		
		JButton btnReturnUItem = new JButton("Aflever");
		panel.add(btnReturnUItem);
		btnReturnUItem.setVerticalAlignment(SwingConstants.TOP);
		btnReturnUItem.addActionListener(this::btnReturnUtilityItem_clicked);
		btnHandOutUItem.addActionListener(this::btnHandOutUtilityItem_clicked);

		init(id);
	}
	
	private void init(int id) {
		try {
			this.loan = LoanController.getInstance().getLoan(id);
			this.loanCtrl = LoanController.getInstance();
			
			loanUtilityItemLineTableModel.setList(loan.getLoanUtilityItemLines());
			loanItemLineTableModel.setList(loan.getLoanItemLines());
			
			lblName.setText(loan.getEmployee().getName());
			txtDateStart.setText(loan.getDateStart().toString());
			txtDateEnd.setText(loan.getDateEnd().toString());
		} catch (DataAccessException e) {
			DialogBox.show("Error", e.getMessage(), true);
		}
	}

	private void btnHandOutItem_clicked(ActionEvent e) {
		try {
			loanCtrl.pickupItemsFromExistingLoan(loan, loanItemLineTableModel.getAllSelected(tblItems));
		} catch (DataAccessException e1) {
			DialogBox.show("Udlevering mislykkedes", e1.getMessage(), true);
		}
		loanItemLineTableModel.fireTableDataChanged();
	}

	private void btnReturnItem_clicked(ActionEvent e) {
		try {
			loanCtrl.returnItemsFromExistingLoan(loan, loanItemLineTableModel.getAllSelected(tblItems));
		} catch (DataAccessException e1) {
			DialogBox.show("Udlevering mislykkedes", e1.getMessage(), true);
		}
		loanItemLineTableModel.fireTableDataChanged();
	}

	private void btnHandOutUtilityItem_clicked(ActionEvent e) {
		try {
			loanCtrl.pickupUtilityItemsFromExistingLoan(loan, loanUtilityItemLineTableModel.getAllSelected(tblUtilityItems));
		} catch (DataAccessException e1) {
			DialogBox.show("Udlevering mislykkedes", e1.getMessage(), true);
		}
		loanUtilityItemLineTableModel.fireTableDataChanged();
	}

	private void btnReturnUtilityItem_clicked(ActionEvent e) {
		try {
			loanCtrl.returnUtilityItemsFromExistingLoan(loan, loanUtilityItemLineTableModel.getAllSelected(tblUtilityItems));
		} catch (DataAccessException e1) {
			DialogBox.show("Udlevering mislykkedes", e1.getMessage(), true);
		}
		loanUtilityItemLineTableModel.fireTableDataChanged();
	}
}
