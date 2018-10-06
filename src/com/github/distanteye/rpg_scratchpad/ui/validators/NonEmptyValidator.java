package com.github.distanteye.rpg_scratchpad.ui.validators;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Validates that a number has been entered
 * @author Vigilant
 *
 */
public class NonEmptyValidator extends InputVerifier {	
	
	@Override
	public boolean shouldYieldFocus(JComponent input)
	{

			if (super.shouldYieldFocus(input))
			{
				return true;
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Please enter a valid non-empty entry for this field", "Invalid Field", JOptionPane.ERROR_MESSAGE);
				return false;
			}
	}

	@Override
	public boolean verify(JComponent input) {
		JTextField temp = (JTextField)input;
		
		return temp.getText().length() > 0;
	}
}
