package gui.components;

import javax.swing.JOptionPane;

public class DialogBox {
	private DialogBox() {}
	public static void show(String title, String message) {
		show(title, message, false);
	}
	
	public static void show(String title, String message, boolean error) {
		JOptionPane.showMessageDialog(null, message, title, (error ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE));
	}
}