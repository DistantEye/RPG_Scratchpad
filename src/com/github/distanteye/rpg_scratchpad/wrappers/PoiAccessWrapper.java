/**
 * 
 */
package com.github.distanteye.rpg_scratchpad.wrappers;

import com.github.distanteye.rpg_scratchpad.model.Poi;

/**
 * Parent class holding common code across all the various accessors to get/set an aspect of information from a Poi object
 * The data being accessed by those subclasses should be aparrent by the naming convention
 * 
 * @author Vigilant
 *
 */
public abstract class PoiAccessWrapper<T> extends AccessWrapper<T> implements ReconfigurableWrapperData {
	protected Poi aChar;
	
	public PoiAccessWrapper(Poi aChar)
	{
		this.aChar = aChar;
	}
	
	public Poi getChar() {
		return aChar;
	}
	public void setChar(Poi aChar) {
		this.aChar = aChar;
	}
	
	abstract public T getValue();
	abstract public void setValue(T item);
	
	public void SetData(Object o)
	{
		if (!(o instanceof Poi)) { throw new IllegalArgumentException("Object miust be a Poi object"); }
		setChar((Poi)o);
	}
	
	abstract public ReconfigurableWrapperData CloneWrapper();
	
	/**
	 * Reflects whether the underlying value is an Integer or not
	 * 
	 * This provides an accessibility/performance bonus to some contexts
	 * 
	 * @return True/False as appropriate 
	 */
	abstract public boolean isInt();

}
