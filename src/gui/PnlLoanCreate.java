package gui;

import java.awt.GridBagLayout;
import com.github.lgooddatepicker.components.DatePicker;

import control.LoanController;
import control.UserController;
import control.common.AccessViolationException;
import db.common.DataAccessException;
import gui.components.DialogBox;
import gui.components.ItemWithAvailableTableModel;
import gui.components.LoanItemLineAddedTableModel;
import gui.components.LoanUtilityItemLineSummaryTableModel;
import gui.components.UtilityItemWithAvailableTableModel;
import gui.components.ValidatingTextField;
import model.Employee;
import model.common.Tuple;
import model.common.WrongDateSpanException;
import model.item.Item;
import model.item.UtilityItem;
import model.line.LoanItemLine;

import java.awt.Color;
import java.awt.GridBagConstraints;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.DateTimeException;
import java.util.Map.Entry;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.Dimension;

public class PnlLoanCreate extends BasePanel {
	private static final long serialVersionUID = -8225927768927473070L;
	private ValidatingTextField txtMANR;
	private DatePicker dpDateStart;
	private DatePicker dpDateEnd;
	
	private JTable tblItemsAdded;
	private JTable tblUtilityItems;
	private JTable tblItems;
	private ItemWithAvailableTableModel itemWithAvailableTableModel;
	private LoanItemLineAddedTableModel loanItemLineAddedTableModel;
	
	private UtilityItemWithAvailableTableModel utilityItemWithAvailableTableModel;
	private LoanUtilityItemLineSummaryTableModel loanUtilityItemLineSummaryTableModel;
	
	private LoanController loanCtrl;
	private JLabel lblChosenEmployee;
	private JButton btnGetItems;
	private JButton btnFindEmployee;
	private JLabel lblUtilityItemsAdded;
	private JScrollPane spUtilityItemsAdded;
	private JTable tblUtilityItemsAdded;
	private JTextArea txtNotes;
	private JPanel panel;
	private JButton btnCreate;
	private JButton btnReset;

	/**
	 * Create the panel.
	 */
	public PnlLoanCreate() {
		super("Opret nyt udlån");
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		btnFindEmployee = new JButton("Hent medarbejder");
		btnFindEmployee.addActionListener(this::btnFindEmployee_click);
		
		txtMANR = new ValidatingTextField();
		txtMANR.setRegex("[0-9]*");
		txtMANR.setCharacterLimit(8);
		txtMANR.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				txtMANR_keyPress(arg0);
			}
		});
		GridBagConstraints gbc_txtMANR = new GridBagConstraints();
		gbc_txtMANR.insets = new Insets(0, 0, 5, 5);
		gbc_txtMANR.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMANR.gridx = 1;
		gbc_txtMANR.gridy = 0;
		add(txtMANR, gbc_txtMANR);
		txtMANR.setColumns(10);
		GridBagConstraints gbc_btnFindEmployee = new GridBagConstraints();
		gbc_btnFindEmployee.insets = new Insets(0, 0, 5, 5);
		gbc_btnFindEmployee.gridx = 2;
		gbc_btnFindEmployee.gridy = 0;
		add(btnFindEmployee, gbc_btnFindEmployee);
		
		lblChosenEmployee = new JLabel("");
		GridBagConstraints gbc_lblChosenEmployee = new GridBagConstraints();
		gbc_lblChosenEmployee.gridwidth = 2;
		gbc_lblChosenEmployee.insets = new Insets(0, 0, 5, 5);
		gbc_lblChosenEmployee.gridx = 0;
		gbc_lblChosenEmployee.gridy = 1;
		add(lblChosenEmployee, gbc_lblChosenEmployee);
		
		JLabel lblNotes = new JLabel("Noter");
		GridBagConstraints gbc_lblNotes = new GridBagConstraints();
		gbc_lblNotes.gridwidth = 2;
		gbc_lblNotes.insets = new Insets(0, 0, 5, 0);
		gbc_lblNotes.gridx = 2;
		gbc_lblNotes.gridy = 1;
		add(lblNotes, gbc_lblNotes);
		
		JLabel lblDateStart = new JLabel("Start dato ");
		lblDateStart.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblDateStart = new GridBagConstraints();
		gbc_lblDateStart.insets = new Insets(0, 0, 5, 5);
		gbc_lblDateStart.anchor = GridBagConstraints.EAST;
		gbc_lblDateStart.gridx = 0;
		gbc_lblDateStart.gridy = 2;
		add(lblDateStart, gbc_lblDateStart);
		
		dpDateStart = new DatePicker();
		dpDateStart.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		dpDateStart.addDateChangeListener(this::dpDateStart_dateChanged);
		GridBagConstraints gbc_dpDateStart = new GridBagConstraints();
		gbc_dpDateStart.insets = new Insets(0, 0, 5, 5);
		gbc_dpDateStart.fill = GridBagConstraints.HORIZONTAL;
		gbc_dpDateStart.gridx = 1;
		gbc_dpDateStart.gridy = 2;
		add(dpDateStart, gbc_dpDateStart);
		
		txtNotes = new JTextArea();
		txtNotes.setMaximumSize(new Dimension(500, 150));
		GridBagConstraints gbc_txtNotes = new GridBagConstraints();
		gbc_txtNotes.gridheight = 2;
		gbc_txtNotes.gridwidth = 2;
		gbc_txtNotes.insets = new Insets(0, 0, 5, 0);
		gbc_txtNotes.fill = GridBagConstraints.BOTH;
		gbc_txtNotes.gridx = 2;
		gbc_txtNotes.gridy = 2;
		add(txtNotes, gbc_txtNotes);
		
		JLabel lblDateEnd = new JLabel("Slut dato");
		lblDateEnd.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblDateEnd = new GridBagConstraints();
		gbc_lblDateEnd.anchor = GridBagConstraints.EAST;
		gbc_lblDateEnd.insets = new Insets(0, 0, 5, 5);
		gbc_lblDateEnd.gridx = 0;
		gbc_lblDateEnd.gridy = 3;
		add(lblDateEnd, gbc_lblDateEnd);
		
		dpDateEnd = new DatePicker();
		dpDateEnd.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		dpDateEnd.addDateChangeListener(this::dpDateEnd_dateChanged);
		GridBagConstraints gbc_dpDateEnd = new GridBagConstraints();
		gbc_dpDateEnd.insets = new Insets(0, 0, 5, 5);
		gbc_dpDateEnd.fill = GridBagConstraints.HORIZONTAL;
		gbc_dpDateEnd.gridx = 1;
		gbc_dpDateEnd.gridy = 3;
		add(dpDateEnd, gbc_dpDateEnd);
		
		btnGetItems = new JButton("Hent materiel");
		btnGetItems.setEnabled(false);
		btnGetItems.addActionListener(this::btnGetItems_clicked);
		GridBagConstraints gbc_btnGetItems = new GridBagConstraints();
		gbc_btnGetItems.insets = new Insets(0, 0, 5, 5);
		gbc_btnGetItems.gridx = 1;
		gbc_btnGetItems.gridy = 4;
		add(btnGetItems, gbc_btnGetItems);
		
		JLabel lblItems = new JLabel("Materiel");
		GridBagConstraints gbc_lblItems = new GridBagConstraints();
		gbc_lblItems.gridwidth = 2;
		gbc_lblItems.insets = new Insets(0, 0, 5, 5);
		gbc_lblItems.gridx = 0;
		gbc_lblItems.gridy = 5;
		add(lblItems, gbc_lblItems);
		
		JLabel lblItemsAdded = new JLabel("Tilføjet");
		GridBagConstraints gbc_lblItemsAdded = new GridBagConstraints();
		gbc_lblItemsAdded.gridwidth = 2;
		gbc_lblItemsAdded.insets = new Insets(0, 0, 5, 0);
		gbc_lblItemsAdded.gridx = 2;
		gbc_lblItemsAdded.gridy = 5;
		add(lblItemsAdded, gbc_lblItemsAdded);
		
		JScrollPane spItems = new JScrollPane();
		GridBagConstraints gbc_spItems = new GridBagConstraints();
		gbc_spItems.gridwidth = 2;
		gbc_spItems.insets = new Insets(0, 0, 5, 5);
		gbc_spItems.fill = GridBagConstraints.BOTH;
		gbc_spItems.gridx = 0;
		gbc_spItems.gridy = 6;
		add(spItems, gbc_spItems);
		
		itemWithAvailableTableModel = new ItemWithAvailableTableModel();
		tblItems = new JTable(itemWithAvailableTableModel);
		tblItems.setAutoCreateRowSorter(true);
		tblItems.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				tblItems_mouseClicked(arg0);
			}
		});
		spItems.setViewportView(tblItems);
		
		JScrollPane spItemsAdded = new JScrollPane();
		GridBagConstraints gbc_spItemsAdded = new GridBagConstraints();
		gbc_spItemsAdded.gridwidth = 2;
		gbc_spItemsAdded.insets = new Insets(0, 0, 5, 0);
		gbc_spItemsAdded.fill = GridBagConstraints.BOTH;
		gbc_spItemsAdded.gridx = 2;
		gbc_spItemsAdded.gridy = 6;
		add(spItemsAdded, gbc_spItemsAdded);
		
		loanItemLineAddedTableModel = new LoanItemLineAddedTableModel();
		tblItemsAdded = new JTable(loanItemLineAddedTableModel);
		tblItemsAdded.setAutoCreateRowSorter(true);
		tblItemsAdded.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				tblItemsAdded_mouseClicked(arg0);
			}
		});
		spItemsAdded.setViewportView(tblItemsAdded);
		
		JLabel lblUtilityItems = new JLabel("Forbrugsmateriel");
		GridBagConstraints gbc_lblUtilityItems = new GridBagConstraints();
		gbc_lblUtilityItems.gridwidth = 2;
		gbc_lblUtilityItems.insets = new Insets(0, 0, 5, 5);
		gbc_lblUtilityItems.gridx = 0;
		gbc_lblUtilityItems.gridy = 7;
		add(lblUtilityItems, gbc_lblUtilityItems);
		
		lblUtilityItemsAdded = new JLabel("Tilføjet");
		GridBagConstraints gbc_lblUtilityItemsAdded = new GridBagConstraints();
		gbc_lblUtilityItemsAdded.gridwidth = 2;
		gbc_lblUtilityItemsAdded.insets = new Insets(0, 0, 5, 0);
		gbc_lblUtilityItemsAdded.gridx = 2;
		gbc_lblUtilityItemsAdded.gridy = 7;
		add(lblUtilityItemsAdded, gbc_lblUtilityItemsAdded);
		
		JScrollPane spUtilityItems = new JScrollPane();
		GridBagConstraints gbc_spUtilityItems = new GridBagConstraints();
		gbc_spUtilityItems.gridwidth = 2;
		gbc_spUtilityItems.insets = new Insets(0, 0, 5, 5);
		gbc_spUtilityItems.fill = GridBagConstraints.BOTH;
		gbc_spUtilityItems.gridx = 0;
		gbc_spUtilityItems.gridy = 8;
		add(spUtilityItems, gbc_spUtilityItems);
		
		utilityItemWithAvailableTableModel = new UtilityItemWithAvailableTableModel();
		tblUtilityItems = new JTable(utilityItemWithAvailableTableModel);
		tblUtilityItems.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				tblUtilityItems_mouseClicked(arg0);
			}
		});
		spUtilityItems.setViewportView(tblUtilityItems);

		loanUtilityItemLineSummaryTableModel = new LoanUtilityItemLineSummaryTableModel();
		tblUtilityItemsAdded = new JTable(loanUtilityItemLineSummaryTableModel);
		tblUtilityItemsAdded.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				tblUtilityItemsAdded_mouseClicked(arg0);
			}
		});
		spUtilityItemsAdded = new JScrollPane();
		spUtilityItemsAdded.setViewportView(tblUtilityItemsAdded);
		GridBagConstraints gbc_spUtilityItemsAdded = new GridBagConstraints();
		gbc_spUtilityItemsAdded.insets = new Insets(0, 0, 5, 0);
		gbc_spUtilityItemsAdded.gridwidth = 2;
		gbc_spUtilityItemsAdded.fill = GridBagConstraints.BOTH;
		gbc_spUtilityItemsAdded.gridx = 2;
		gbc_spUtilityItemsAdded.gridy = 8;
		add(spUtilityItemsAdded, gbc_spUtilityItemsAdded);
		
		panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 4;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 9;
		add(panel, gbc_panel);
		
		btnReset = new JButton("Nulstil");
		btnReset.addActionListener(this::btnReset_clicked);
		panel.add(btnReset);
		
		btnCreate = new JButton("Opret");
		btnCreate.setEnabled(false);
		btnCreate.addActionListener(this::btnCreate_clicked);
		panel.add(btnCreate);
		
		init();
	}
	
	private void init() {
		try {
			loanCtrl = LoanController.getInstance();
			loanCtrl.startNewLoan(UserController.getInstance().getCurrentUser());
			
			loanItemLineAddedTableModel.setList(loanCtrl.getNewLoanItems());
			loanUtilityItemLineSummaryTableModel.setList(loanCtrl.getNewLoanUtilityItemSummary());
			setStatusMessage("Udfyld info og tryk på Hent materiel");
		} catch (DataAccessException | AccessViolationException e) {
			txtMANR.setEditable(false);
			dpDateStart.setEnabled(false);
			dpDateEnd.setEnabled(false);
			DialogBox.show("Error", e.getMessage(), true);
		}
	}
	
	private void txtMANR_keyPress(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			findEmployee();
	}
	
	private void btnFindEmployee_click(ActionEvent e) {
		findEmployee();
	}
	
	private void findEmployee() {
		String search = txtMANR.getText().trim();
		
		if (!search.isEmpty()) {
			Employee employee = null;
			
			try {
				employee = loanCtrl.findEmployee(txtMANR.getText());
			} catch (DataAccessException e1) {
				DialogBox.show("Error", e1.getMessage(), true);
			}
			
			loanCtrl.setNewLoanEmployee(employee);
			lblChosenEmployee.setText(employee == null ? "Medarbejder ikke fundet" : employee.getName());
		}
		else
			setStatusMessage("Du skal udfylde MANR!", true);
		
		btnGetItems.setEnabled(loanCtrl.isInfoSetOnNewLoan());
	}
	
	private void dpDateStart_dateChanged(DateChangeEvent arg0) {
		try {
			loanCtrl.setNewLoanDateStart(arg0.getNewDate());
			dpDateStart.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		} catch (WrongDateSpanException e) {
			dpDateStart.setBorder(BorderFactory.createLineBorder(Color.RED));
			setStatusMessage("Start-dato skal være før slut-dato", true);
		}
		btnGetItems.setEnabled(loanCtrl.isInfoSetOnNewLoan());
	}
	
	private void dpDateEnd_dateChanged(DateChangeEvent arg0) {
		try {
			loanCtrl.setNewLoanDateEnd(arg0.getNewDate());
			dpDateEnd.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		} catch (WrongDateSpanException e) {
			dpDateEnd.setBorder(BorderFactory.createLineBorder(Color.RED));
			setStatusMessage("Slut-dato skal være efter start-dato", true);
		}
		
		btnGetItems.setEnabled(loanCtrl.isInfoSetOnNewLoan());
	}
	
	private void btnGetItems_clicked(ActionEvent e) {
		if (loanCtrl.isInfoSetOnNewLoan()) {
			try {
				itemWithAvailableTableModel.setList(loanCtrl.getAllItemsWithAvailable());
				utilityItemWithAvailableTableModel.setList(loanCtrl.getAllUtilityItemsWithAvailable());
				
				txtMANR.setEnabled(false);
				btnFindEmployee.setEnabled(false);
				dpDateStart.setEnabled(false);
				dpDateEnd.setEnabled(false);
				btnGetItems.setEnabled(false);
				btnCreate.setEnabled(true);
				
				setStatusMessage(String.format("%d genstand(e) og %d forbrugsgenstand(e) fundet!", itemWithAvailableTableModel.getRowCount(), utilityItemWithAvailableTableModel.getRowCount()));
			} catch (DataAccessException | DateTimeException e1) {
				DialogBox.show("Error", e1.getMessage(), true);
			}
		}
	}
	
	private void tblItems_mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			Tuple<Item, Boolean> selectedTuple = itemWithAvailableTableModel.getSelected(tblItems);
			
			if (selectedTuple != null) {
				if (selectedTuple.y) {
					loanCtrl.addItemToNewLoan(selectedTuple.x);
					loanItemLineAddedTableModel.fireTableDataChanged();
				}
				else
					setStatusMessage(String.format("%s er ikke tilgængelig i den valgte periode!", selectedTuple.x.getSerial()), true);
			}
		}
	}

	private void tblItemsAdded_mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			LoanItemLine selectedLine = loanItemLineAddedTableModel.getSelected(tblItems);
			
			if (selectedLine != null) {
				loanCtrl.removeItemFromNewLoan(selectedLine.getItem());
				loanItemLineAddedTableModel.fireTableDataChanged();
			}
		}
	}
	
	private void tblUtilityItems_mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			Tuple<UtilityItem, Integer> selectedTuple = utilityItemWithAvailableTableModel.getSelected(tblUtilityItems);
			
			if (selectedTuple != null) {
				int currentCount = loanCtrl.getNewLoanUtilityItemSummary().stream()
					.filter(x -> x.getKey().getId() == selectedTuple.x.getId())
					.findFirst()
					.map(Entry<UtilityItem, Integer>::getValue)
					.orElse(0);
				
				if (currentCount + 1 > selectedTuple.y)
					setStatusMessage(String.format("%s er ikke tilgængelig i den valgte periode!", selectedTuple.x.getName()), true);
				else {
					loanCtrl.addUtilityItemToNewLoan(selectedTuple.x);
					loanUtilityItemLineSummaryTableModel.fireTableDataChanged();
				}
			}
		}
	}
	
	private void tblUtilityItemsAdded_mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			Entry<UtilityItem, Integer> selectedEntry = loanUtilityItemLineSummaryTableModel.getSelected(tblUtilityItemsAdded);
			
			if (selectedEntry != null) {
				loanCtrl.removeUtilityItemFromNewLoan(selectedEntry.getKey());
				loanUtilityItemLineSummaryTableModel.fireTableDataChanged();
			}
		}
	}
	
	private void btnReset_clicked(ActionEvent e) {
		changePanel(new PnlLoanCreate());
	}
	
	private void btnCreate_clicked(ActionEvent e) {
		try {
			loanCtrl.setNewLoanNotes(txtNotes.getText());
			int newId = loanCtrl.finalizeLoan();
			
			changePanel(new PnlLoanView(newId));
		} catch (DataAccessException e1) {
			DialogBox.show("Error", e1.getMessage(), true);
		}
	}
}