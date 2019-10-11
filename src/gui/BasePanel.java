package gui;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

public abstract class BasePanel extends JPanel {
	private static final long serialVersionUID = 559853322597306986L;

	//http://www.java2s.com/Code/Java/Event/CreatingaCustomEvent.htm
	public interface BaseListenerIF {
		void setStatusMessage(String message);
		void setStatusMessage(String message, boolean withTimeOut);
		void changePanel(BasePanel pnl);
		void goBack();
	}
	
	private String title;
	public String getTitle() {
		return title;
	}
	
	private List<BaseListenerIF> setStatusMessageListeners = new LinkedList<>();
	private List<BaseListenerIF> changePanelListeners = new LinkedList<>();
	private List<BaseListenerIF> goBackListeners = new LinkedList<>();

	public BasePanel(String title) {
		//Sets title of this panel (gets used by MainWindow to set Application title)
		this.title = title;
		
		//Adds MainWindow Singleton as listener to events
		addSetStatusMessageListener(MainWindow.getInstance());
		addChangePanelListener(MainWindow.getInstance());
		addGoBackListener(MainWindow.getInstance());
	}

	/**
	 * Adds a BaseListenerIF as listener to the setStatusMessage()-event
	 * @param pnl
	 */
	public void addSetStatusMessageListener(BaseListenerIF toAdd) {
		setStatusMessageListeners.add(toAdd);
	}
	
	/**
	 * Adds a BaseListenerIF as listener to the changePanel()-event
	 * @param pnl
	 */
	public void addChangePanelListener(BaseListenerIF toAdd) {
		changePanelListeners.add(toAdd);
	}
	
	/**
	 * Adds a BaseListenerIF as listener to the goBack()-event.
	 * @param toAdd
	 */
	public void addGoBackListener(BaseListenerIF toAdd) {
		goBackListeners.add(toAdd);
	}

	/**
	 * Tells all listeners to setStatusMessage() to execute their setStatusMessage()-method
	 * @param pnl
	 */
	public void setStatusMessage(String message) {
		setStatusMessage(message, false);
	}
	
	/**
	 * Tells all listeners to setStatusMessage() to execute their setStatusMessage()-method
	 * @param pnl
	 */
	public void setStatusMessage(String message, boolean withTimeOut) {
		for (BaseListenerIF cpl : setStatusMessageListeners)
			cpl.setStatusMessage(message, withTimeOut);
	}
	
	/**
	 * Tells all listeners to changePanel() to execute their changePanel()-method
	 * @param pnl
	 */
	public void changePanel(BasePanel pnl) {
		for (BaseListenerIF cpl : changePanelListeners)
			cpl.changePanel(pnl);
	}
	/**
	 * Tells all listeners to goBack() to execute their goBack()-method
	 * @param pnl
	 */
	public void goBack() {
		for (BaseListenerIF gbl : goBackListeners)
			gbl.goBack();
	}
}