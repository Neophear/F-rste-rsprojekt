package gui;

import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import javax.swing.JTable;

import control.LoanController;
import db.common.DataAccessException;
import gui.components.DialogBox;
import gui.components.LoanTableModel;
import model.Loan;

public class PnlLoanListAll extends BasePanel {
	private static final long serialVersionUID = -5376093401942344619L;
	private JTable tblLoans;
	private LoanTableModel loanTableModel;
	
	/**
	 * Create the panel.
	 */
	public PnlLoanListAll() {
		super("Alle udlån");
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JScrollPane spLoans = new JScrollPane();
		GridBagConstraints gbc_spLoans = new GridBagConstraints();
		gbc_spLoans.fill = GridBagConstraints.BOTH;
		gbc_spLoans.gridx = 1;
		gbc_spLoans.gridy = 1;
		add(spLoans, gbc_spLoans);
		
		loanTableModel = new LoanTableModel();
		tblLoans = new JTable(loanTableModel);
		tblLoans.setAutoCreateRowSorter(true);
		tblLoans.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				tblLoans_clicked(arg0);
			}
		});
		spLoans.setViewportView(tblLoans);
		
		init();
	}
	
	private void init() {
		try {
			loanTableModel.setList(LoanController.getInstance().getAllLoans());
			setStatusMessage(String.format("%d udlån hentet", loanTableModel.getRowCount()));
		} catch (DataAccessException e) {
			DialogBox.show("Error", e.getMessage(), true);
		}
	}
	
	private void tblLoans_clicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			Loan l = loanTableModel.getSelected(tblLoans);
			
			if (l != null)
				changePanel(new PnlLoanView(l.getId()));
		}
	}
}
