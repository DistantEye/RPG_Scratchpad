package com.github.distanteye.rpg_scratchpad.wrappers;

import java.util.ArrayList;
import java.util.Collection;

/**
 * An abstract aggregation wrapper : it takes a Collection of Wrappers and getValue/setValue is applied to all of them evenly
 * 
 * @author Vigilant
 *
 */
public class AccessWrapperMux extends AccessWrapper<String> {
	protected Collection<AccessWrapper<String>> wrappers;
	protected String separator;

	/**
	 * 
	 * @param wrappers Any AccessWrappers that support getValue
	 * @param separator String to Join the getValue Array results with
	 */
	public AccessWrapperMux(Collection<AccessWrapper<String>> wrappers, String separator) {
		this.wrappers = wrappers;
		this.separator = separator;
	}
	
	public String getValue()
	{
		ArrayList<String> results = new ArrayList<String>();
		for (AccessWrapper<String> w : wrappers)
		{
			results.add(w.getValue());
		}
		
		String line = "";
		for (String s : results)
		{
			if (!line.equals("")) { line += separator; }
			line += s;
		}
		
		return line;
	}

	@Override
	public void setValue(String item) {
		for (AccessWrapper<String> w : wrappers)
		{
			w.setValue(item);
		}
	}

	@Override
	public boolean isInt() {
		return false;
	}
	
}
