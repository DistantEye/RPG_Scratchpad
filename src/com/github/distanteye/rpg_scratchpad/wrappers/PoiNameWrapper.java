/**
 * 
 */
package com.github.distanteye.rpg_scratchpad.wrappers;

import com.github.distanteye.rpg_scratchpad.model.Poi;

/**
 * @author Vigilant
 *
 */
public class PoiNameWrapper extends PoiAccessWrapper<String> {
	
	public PoiNameWrapper(Poi aChar)
	{
		super(aChar);
	}
	
	public String getValue()
	{
		return aChar.getName();	
	}
	
	public void setValue(String item)
	{
		aChar.setName(item);
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
		return false;
	}
	
	@Override
	public ReconfigurableWrapperData CloneWrapper() {
		return new PoiNameWrapper(aChar);
	}

}
