package com.github.distanteye.rpg_scratchpad.wrappers;

import com.github.distanteye.rpg_scratchpad.ui.Utils;

/**
 * AccessWrapper governs the basic set of methods needed for retrieving and storing data
 * from an arbitrary element : getValue and setValue, along with any significantly relevant
 * shortcut methods to boost usability 
 * 
 * @author Vigilant
 */
public class MassAccessWrapper<T> extends AccessWrapper<T> {
	public T getValue()
	{
		return GetFirst();
	}
	
	public void setValue(T item)
	{
		for ( AccessWrapper<T> access : set)
		{
			access.setValue(item);
		}
	}
	
	private Iterable<AccessWrapper<T>> set;
	
	public MassAccessWrapper(Iterable<AccessWrapper<T>> set)
	{
		this.set = set;
	}
	
	/**
	 * Reflects whether the underlying value is an Integer or not
	 * 
	 * This provides an accessibility/performance bonus to some contexts
	 * 
	 * @return True/False as appropriate 
	 */
	public boolean isInt()
	{
		return Utils.isInteger(GetFirst().toString());
	}
	
	protected T GetFirst()
	{
		for(AccessWrapper<T> access : set)
		{
			return access.getValue(); // will return the first value. a bit clunky but it works
		}
		
		return null;
	}
	
}
