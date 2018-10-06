/**
 * 
 */
package com.github.distanteye.rpg_scratchpad.wrappers;

import com.github.distanteye.rpg_scratchpad.model.Poi;

/**
 * @author Vigilant
 *
 */
public class PoiSelectedWrapper extends PoiAccessWrapper<String> {

	public PoiSelectedWrapper(Poi aChar)
	{
		super(aChar);
	}
	
	public String getValue()
	{
		return ""+aChar.isSelected();
	}
	
	public void setValue(String item)
	{
		aChar.setSelected(item.equalsIgnoreCase("true"));		
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
		return new PoiSelectedWrapper(aChar);
	}

}
