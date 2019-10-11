package gui;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;

import control.UserController;
import control.common.AccessViolationException;
import control.interfaces.UserControllerIF;
import db.common.DataAccessException;
import gui.components.DialogBox;

import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.util.LinkedList;
import java.util.List;
import java.awt.event.ActionEvent;
import gui.components.ValidatingTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PnlLogin extends BasePanel {
	private static final long serialVersionUID = -2603716042901198945L;
	private ValidatingTextField txtName;
	private JTextField txtPassword;
	List<UserLoggedInListenerIF> userLoggedInListener = new LinkedList<>();
	
	interface UserLoggedInListenerIF{
		public void userLoggedIn();
	}

	/**
	 * Create the panel.
	 */
	public PnlLogin() {
		super("Log ind");

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		JLabel lblLogin = new JLabel("Log ind");
		GridBagConstraints gbc_lblLogin = new GridBagConstraints();
		gbc_lblLogin.insets = new Insets(0, 0, 5, 0);
		gbc_lblLogin.gridx = 0;
		gbc_lblLogin.gridy = 0;
		add(lblLogin, gbc_lblLogin);

		JLabel lblUsername = new JLabel("MANR (kun tal tilladt)");
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.insets = new Insets(0, 0, 5, 0);
		gbc_lblUsername.gridx = 0;
		gbc_lblUsername.gridy = 1;
		add(lblUsername, gbc_lblUsername);

		txtName = new ValidatingTextField();
		txtName.setRegex("[0-9]*");
		txtName.setCharacterLimit(8);
		txtName.setText("00123456");
		txtName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				txt_keyPress(arg0);
			}
		});
		GridBagConstraints gbc_txtName = new GridBagConstraints();
		gbc_txtName.insets = new Insets(0, 0, 5, 0);
		gbc_txtName.gridx = 0;
		gbc_txtName.gridy = 2;
		add(txtName, gbc_txtName);
		txtName.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.insets = new Insets(0, 0, 5, 0);
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 3;
		add(lblPassword, gbc_lblPassword);

		txtPassword = new JTextField();
		txtPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				txt_keyPress(arg0);
			}
		});
		txtPassword.setText("anything");
		GridBagConstraints gbc_txtPassword = new GridBagConstraints();
		gbc_txtPassword.insets = new Insets(0, 0, 5, 0);
		gbc_txtPassword.gridx = 0;
		gbc_txtPassword.gridy = 4;
		add(txtPassword, gbc_txtPassword);
		txtPassword.setColumns(10);

		JButton btnLogin = new JButton("Log ind");
		btnLogin.addActionListener(this::btnLogin_clicked);
		GridBagConstraints gbc_btnLogin = new GridBagConstraints();
		gbc_btnLogin.gridx = 0;
		gbc_btnLogin.gridy = 5;
		add(btnLogin, gbc_btnLogin);
		
		init();
	}
	
	private void init() {
		addUserLoggedInListener(MainWindow.getInstance());
	}
	
	public void addUserLoggedInListener(UserLoggedInListenerIF toAdd) {
		userLoggedInListener.add(toAdd);
	}
	
	private void userLoggedIn() {
		for(UserLoggedInListenerIF ulil : userLoggedInListener)
			ulil.userLoggedIn();
	}
	
	private void txt_keyPress(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			login();
	}

	private void btnLogin_clicked(ActionEvent e) {
		login();
	}
	
	private void login() {
		if(!txtName.getText().trim().isEmpty() && !txtPassword.getText().trim().isEmpty()) {
			try {
				UserControllerIF userCtrl = UserController.getInstance(); // No point in using the interface here...
				userCtrl.login(txtName.getText(), txtPassword.getText());
				userLoggedIn();
			} catch (AccessViolationException | DataAccessException e1) {
				DialogBox.show("Error", e1.getMessage(), true);
			}
		}
		else {
			txtName.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
			txtPassword.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		}
	}

}
