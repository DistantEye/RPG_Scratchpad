/**
 * 
 */
package com.github.distanteye.rpg_scratchpad.wrappers;

import com.github.distanteye.rpg_scratchpad.model.Poi;
import com.github.distanteye.rpg_scratchpad.ui.Utils;

/**
 * @author Vigilant
 *
 */
public class PoiLifeWrapper extends PoiAccessWrapper<String> {
	
	public PoiLifeWrapper(Poi aChar)
	{
		super(aChar);
	}
	
	public String getValue()
	{
		return ""+aChar.getLife();
	}
	
	public void setValue(String item)
	{
		if (!Utils.isInteger(item))
		{
			throw new IllegalArgumentException("Life must be an integer parsable string");
		}
		
		aChar.setLife(Integer.parseInt(item));
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
		return true;
	}
	
	@Override
	public ReconfigurableWrapperData CloneWrapper() {
		return new PoiLifeWrapper(aChar);
	}

}
