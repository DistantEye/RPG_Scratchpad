package com.github.distanteye.rpg_scratchpad.wrappers;

import java.util.ArrayList;
import java.util.Collection;

/**
 * An abstract aggregation wrapper : it takes a Collection of Wrappers and getValue/setValue is applied to all of them evenly
 * 
 * @author Vigilant
 *
 */
public class AccessWrapperDynamicMux<WType extends AccessWrapper<String> & ReconfigurableWrapperData> extends AccessWrapper<String> {
	protected Collection<?> data;
	protected String separator;
	private WType example;


	public AccessWrapperDynamicMux(Collection<?> data, WType example, String separator) {
		this.data = data;
		
		this.example = example;
		this.separator = separator;
	}
	
	public ArrayList<WType> GetWrappers()
	{
		ArrayList<WType> res = new ArrayList<WType>();
		for (Object o : data)
		{			
			@SuppressWarnings("unchecked")
			WType newWrap = (WType)example.CloneWrapper();
			newWrap.SetData(o);
			res.add(newWrap);
		}
		return res;
	}
	
	public String getValue()
	{
		ArrayList<String> results = new ArrayList<String>();
		ArrayList<WType> wrappers = GetWrappers();
		for (WType w : wrappers)
		{
			AccessWrapper<String> wrapper = (AccessWrapper<String>)w;
			results.add(wrapper.getValue());
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
		ArrayList<WType> wrappers = GetWrappers();
		for (WType w : wrappers)
		{
			AccessWrapper<String> wrapper = (AccessWrapper<String>)w;
			wrapper.setValue(item);
		}		
	}

	@Override
	public boolean isInt() {
		return false;
	}
	
}
