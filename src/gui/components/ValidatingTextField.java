package gui.components;

import java.util.regex.Pattern;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

public class ValidatingTextField extends JTextField {
	private static final int CHARLIMITLIMIT = 254; // Limit for characterLimit.
	private static final long serialVersionUID = 4205174835863028728L;
	private String regex = "[A-Za-z0-9]*"; // Relatively sane default
	private int characterLimit = 4;        // Low enough limit to annoy devs enough to change it.

	/**
	 * Add pattern matching to Swings text fields.
	 * Why is this not a standard feature? C'mon.
	 * @author 1067477
	 */
	private class InputValidationFilter extends DocumentFilter  {
		private Pattern pattern;
		private int characterLimit;
		public InputValidationFilter(String pattern, int characterLimit) {
			this.pattern = Pattern.compile(pattern); // Precompile pattern, to avoid wasting cycles and help response time.
			this.characterLimit = characterLimit;
		}
		
		@Override
		public void replace(FilterBypass fb, int offset, int length, String str, AttributeSet attrs) throws BadLocationException {
			// Current (old) text value in field
			String old = fb.getDocument().getText(0, fb.getDocument().getLength());
			// New value. Simply old value appended with proposed string
			String replacement = String.format("%s%s", old, str);
			// If replacement data is within limits and matches regex, accept it.
			if (replacement.length() <= characterLimit && pattern.matcher(replacement).matches()) {
				super.replace(fb, offset, length, str, attrs);
			}
		}
	}
	
	/**
	 * Update the pattern matching filter for this text field
	 */
	private void updateFilter() {
		((AbstractDocument)this.getDocument()).setDocumentFilter(new InputValidationFilter(this.regex, this.characterLimit));
	}
	
	/**
	 * Set the regular expression to match text field contents against.
	 * @param Regex regular expression to match against
	 */
	public void setRegex(String Regex) {
		this.regex = Regex;
		updateFilter();
	}
	/**
	 * Set a limit to the length of this text field.
	 * Max characters is 254 to allow space for null terminator, which
	 * Java doesn't count as length.
	 * If you need more chars than this in a text field, wtf are you doing?
	 * @param CharacterLimit number of characters to allow. Max 254.
	 */
	public void setCharacterLimit(int CharacterLimit) {
		this.characterLimit = CharacterLimit > CHARLIMITLIMIT ? CHARLIMITLIMIT : CharacterLimit; // This statement is 98% "characterlimit".
		updateFilter();
	}
	
	// Getters
	public String getRegex() {
		return regex;
	}
	public int getCharacterLimit() {
		return characterLimit;
	}

	// Constructors, not interesting.
	public ValidatingTextField() {
		updateFilter();
	}
	public ValidatingTextField(String arg0) {
		super(arg0);
		updateFilter();
	}
	public ValidatingTextField(int arg0) {
		super(arg0);
		updateFilter();
	}
	public ValidatingTextField(String arg0, int arg1) {
		super(arg0, arg1);
		updateFilter();
	}
	public ValidatingTextField(Document arg0, String arg1, int arg2) {
		super(arg0, arg1, arg2);
		updateFilter();
	}
}
