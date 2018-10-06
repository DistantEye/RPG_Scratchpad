package com.github.distanteye.rpg_scratchpad.wrappers;

import java.util.ArrayList;

/**
 * An abstract aggregation wrapper : it does not support setValue,
 * but it's getValue will call getValue from the full array of wrappers passed and join on separator
 * 
 * @author Vigilant
 *
 */
public class AggregateWrapper extends AccessWrapper<String> {
	protected AccessWrapper<String>[] wrappers;
	protected String separator;

	/**
	 * 
	 * @param wrappers Any AccessWrappers that support getValue
	 * @param separator String to Join the getValue Array results with
	 */
	public AggregateWrapper(AccessWrapper<String>[] wrappers, String separator) {
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
		throw new UnsupportedOperationException("AggregateWrapper can't use setValue()");
	}

	@Override
	public boolean isInt() {
		return false;
	}
	
}
