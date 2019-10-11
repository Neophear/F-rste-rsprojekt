package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import control.UserController;
import control.common.AccessViolationException;
import control.interfaces.UserControllerIF;
import db.common.DataAccessException;
import db.connection.DBConnection;
import gui.BasePanel.BaseListenerIF;
import gui.PnlLogin.UserLoggedInListenerIF;
import gui.components.DialogBox;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;

public class MainWindow extends JFrame implements BaseListenerIF, UserLoggedInListenerIF {
	private static final long serialVersionUID = 5847976970940756430L;
	private JMenuItem mntmFileLogin;
	private JPanel pnlContent;
	private static MainWindow instance;
	private JLabel lblStatusMessage;
	private String statusTextOld;
	private JPanel statusLight;
	private Timer tmrResetStatus;
	private AtomicInteger resetStatusCounter = new AtomicInteger(0);
	private JLabel lblLoggedInUser;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				// Make program look native
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				
				MainWindow frame = MainWindow.getInstance();
				frame.init();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	/**
	 * Singleton pattern
	 * @return One single instance of MainWindow.
	 */
	public static MainWindow getInstance() {
		if (instance == null)
			instance = new MainWindow();
		
		return instance;
	}
	
	/**
	 * Create the frame.
	 */
	private MainWindow() {
		setTitle(Branding.PROGRAM_NAME);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 500);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("Filer");
		menuBar.add(mnFile);

		mntmFileLogin = new JMenuItem("Log ind");
		mntmFileLogin.addActionListener(this::mntmFileLogin_click);
		mnFile.add(mntmFileLogin);
		
		JMenuItem mntmFileExit = new JMenuItem("Afslut");
		mntmFileExit.addActionListener(this::mntmFileExit_click);
		mnFile.add(mntmFileExit);
		
		JMenu mnEmployee = new JMenu("Medarbejdere");
		mnEmployee.setEnabled(false);
		menuBar.add(mnEmployee);
		
		JMenuItem mntmEmployeeListAll = new JMenuItem("Alle medarbejdere");
		mntmEmployeeListAll.setEnabled(false);
		mnEmployee.add(mntmEmployeeListAll);
		
		JMenuItem mntmEmployeeCreate = new JMenuItem("Opret medarbejder");
		mntmEmployeeCreate.setEnabled(false);
		mnEmployee.add(mntmEmployeeCreate);
		
		JMenu mnItem = new JMenu("Materiel");
		mnItem.setEnabled(false);
		menuBar.add(mnItem);
		
		JMenuItem mntmItemListAll = new JMenuItem("Al materiel");
		mntmItemListAll.setEnabled(false);
		mnItem.add(mntmItemListAll);
		
		JMenuItem mntmItemCreate = new JMenuItem("Opret materiel");
		mntmItemCreate.setEnabled(false);
		mnItem.add(mntmItemCreate);
		
		JMenu mnLoan = new JMenu("Udlån");
		menuBar.add(mnLoan);

		JMenuItem mntmLoanListAll = new JMenuItem("Alle udlån");
		mntmLoanListAll.addActionListener(this::mntmLoanListAll_click);
		mnLoan.add(mntmLoanListAll);
		
		JMenuItem mntmLoanCreate = new JMenuItem("Opret udlån");
		mntmLoanCreate.addActionListener(this::mntmLoanCreate_click);
		mntmLoanCreate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		mnLoan.add(mntmLoanCreate);
		
		JPanel pnlWindow = new JPanel();
		pnlWindow.setBorder(new EmptyBorder(5, 5, 5, 5));
		pnlWindow.setLayout(new BorderLayout(0, 0));
		setContentPane(pnlWindow);
		
		pnlContent = new JPanel();
		pnlWindow.add(pnlContent, BorderLayout.CENTER);
		pnlContent.setLayout(new BorderLayout(0, 0));
		
		JPanel pnlStatusBar = new JPanel();
		pnlWindow.add(pnlStatusBar, BorderLayout.SOUTH);
		GridBagLayout gbl_pnlStatusBar = new GridBagLayout();
		gbl_pnlStatusBar.columnWidths = new int[]{0, 0, 0, 0};
		gbl_pnlStatusBar.rowHeights = new int[]{0, 0, 0};
		gbl_pnlStatusBar.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_pnlStatusBar.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		pnlStatusBar.setLayout(gbl_pnlStatusBar);
		
		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.anchor = GridBagConstraints.NORTH;
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridwidth = 3;
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 0;
		pnlStatusBar.add(separator, gbc_separator);
		
		lblStatusMessage = new JLabel("Login");
		GridBagConstraints gbc_lblStatusMessage = new GridBagConstraints();
		gbc_lblStatusMessage.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblStatusMessage.insets = new Insets(0, 0, 0, 5);
		gbc_lblStatusMessage.gridx = 0;
		gbc_lblStatusMessage.gridy = 1;
		pnlStatusBar.add(lblStatusMessage, gbc_lblStatusMessage);
		
		statusLight = new JPanel();
		statusLight.setBackground(Color.RED);
		GridBagConstraints gbc_statusLight = new GridBagConstraints();
		gbc_statusLight.insets = new Insets(0, 0, 0, 5);
		gbc_statusLight.gridx = 1;
		gbc_statusLight.gridy = 1;
		pnlStatusBar.add(statusLight, gbc_statusLight);
		
		lblLoggedInUser = new JLabel("Ikke logget ind");
		GridBagConstraints gbc_lblLoggedInUser = new GridBagConstraints();
		gbc_lblLoggedInUser.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblLoggedInUser.gridx = 2;
		gbc_lblLoggedInUser.gridy = 1;
		pnlStatusBar.add(lblLoggedInUser, gbc_lblLoggedInUser);
	}
	
	/**
	 * Initializes the first panel
	 */
	private void init() {
		setLoggedIn(false);
		changePanel(new PnlLogin());
		tmrResetStatus = new Timer();
		
		//Anonymous method to check for db connection
		new Thread(() -> {
			while (true) {
				try {
					setStatusLight(DBConnection.getInstance().isConnected());
					Thread.sleep(1000);
				} catch (InterruptedException | DataAccessException e) {
					DialogBox.show("Error", e.getMessage(), true);
				}
			}
		}).start();
	}
	
	private void setStatusLight(boolean connected) {
		statusLight.setBackground(connected ? Color.GREEN : Color.RED);
	}
	
	private void mntmFileExit_click(ActionEvent e) {
		this.dispose();
	}
	
	private void mntmFileLogin_click(ActionEvent e) {
		try {
			if (UserController.getInstance().isLoggedIn()) {
				UserController.getInstance().logout();
				setLoggedIn(false);
			}
		} catch (DataAccessException e1) {
			DialogBox.show("Error", e1.getMessage(), true);
		}
		
		changePanel(new PnlLogin());
	}
	
	private void mntmLoanListAll_click(ActionEvent e) {
		changePanel(new PnlLoanListAll());
	}
	
	private void mntmLoanCreate_click(ActionEvent e) {
		changePanel(new PnlLoanCreate());
	}
	
	@Override
	public void setStatusMessage(String message) {
		setStatusMessage(message, false);
	}
	
	@Override
	public void setStatusMessage(String message, boolean withTimeOut) {
		if (!withTimeOut)
			statusTextOld = message;
		else{
			resetStatusCounter.incrementAndGet();
			tmrResetStatus.schedule(new TimerTask() {
				@Override
				public void run() {
					if (resetStatusCounter.decrementAndGet() == 0)
						lblStatusMessage.setText(statusTextOld);
				}
			}, 2000);
		}
		
		lblStatusMessage.setText(message);
	}
	
	/**
	 * Changes to a new panel, after removing any old one.
	 */
	@Override
	public void changePanel(BasePanel pnl) {
		pnlContent.removeAll();
		pnlContent.add(pnl);
		setTitle(Branding.PROGRAM_NAME + " - " + pnl.getTitle());
		pnl.setVisible(true);
		revalidate();
		repaint();
	}
	
	/**
	 * Goes back to the front page.
	 */
	@Override
	public void goBack() {
		changePanel(new PnlFrontpage());
	}

	@Override
	public void userLoggedIn() {
		try {
			UserControllerIF userCtrl = UserController.getInstance(); // No point in using the interface here...
			lblLoggedInUser.setText(userCtrl.getCurrentUser().getName());
			setLoggedIn(true);
			changePanel(new PnlFrontpage());
		} catch (DataAccessException | AccessViolationException e) {
			DialogBox.show("Error", e.getMessage(), true);
		}
	}
	
	private void setLoggedIn(boolean isLoggedIn) {
		mntmFileLogin.setText(isLoggedIn ? "Log ud" : "Log ind");
		getJMenuBar().getMenu(3).setEnabled(isLoggedIn);
		
		if (!isLoggedIn)
			lblLoggedInUser.setText("Ikke logget ind");
	}
}