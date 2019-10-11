package gui;
import javax.swing.JLabel;

public class PnlFrontpage extends BasePanel {
	private static final long serialVersionUID = -6610444599269423013L;

	/**
	 * Create the panel.
	 */
	public PnlFrontpage() {
		super("Forside");
		JLabel lblDetteErForsiden = new JLabel("Vælg en handling fra menuen.");
		add(lblDetteErForsiden);
		
		setStatusMessage("Velkommen!");
	}
}
