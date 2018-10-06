/**
 * 
 */
package com.github.distanteye.rpg_scratchpad.wrappers;

import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;



/**
 * @author Vigilant
 *
 */
public class ComboBoxDDMappedComponent extends MappedComponent {

	private JComboBox<String> comboComp;
	private String separator;
	
	/**
	 * @param df
	 * @param linkedData
	 * @param comp
	 */
	public ComboBoxDDMappedComponent(DataFlow df, AccessWrapper<String> linkedData, JComboBox<String> comp, String separator) {
		super(df, linkedData, comp);
		
		
		this.comboComp = comp;
		this.separator = separator;
	}
	
	/**
	 * Resolves whether comp is JTextComponent or JButton and calls setText accordingly
	 * @param val Valid string to set on comp
	 */
	protected void setCompText(String val)
	{
		String lastSelected = (String)comboComp.getSelectedItem();
		
		String[] arr = val.split(Pattern.quote(separator));
		comboComp.removeAllItems();
		for (String s : arr)
		{
			comboComp.addItem(s);
		}	
		
		if (lastSelected != null && getDDOptions().contains(lastSelected)) { 
			comboComp.setSelectedItem(lastSelected);
		}
	}
	
	protected ArrayList<String> getDDOptions()
	{
		ComboBoxModel<String> model = comboComp.getModel();
		ArrayList<String> res = new ArrayList<String>();
		
		for (int i = 0; i < model.getSize(); i++)
		{
			res.add(model.getElementAt(i));
		}
		
		return res;
	}
	
	protected void typeCheck(JComponent comp)
	{
		if (df!=DataFlow.STATIC && !(comp instanceof JComboBox<?>))
		{
			throw new IllegalArgumentException("Non-static DataFlow components must be JTextComponents or JButtons for MappedComponent");
		} 
	}
	
	/**
	 * Returns the current selected field (or "" if none selected)
	 * @return Text contents of comp (which field is selected)
	 */
	protected String getCompText()
	{
		if (comboComp == null)
		{
			return "";
		}
		
		String res = (String)comboComp.getSelectedItem();
		
		if (res == null) { return ""; }
		
		return res;
	}

}
