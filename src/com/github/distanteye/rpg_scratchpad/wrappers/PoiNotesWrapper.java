/**
 * 
 */
package com.github.distanteye.rpg_scratchpad.wrappers;

import com.github.distanteye.rpg_scratchpad.model.Poi;

/**
 * @author Vigilant
 *
 */
public class PoiNotesWrapper extends PoiAccessWrapper<String> {
	
	public PoiNotesWrapper(Poi aChar)
	{
		super(aChar);
	}
	
	public String getValue()
	{
		return aChar.getNotes();
	}
	
	public void setValue(String item)
	{
		aChar.setNotes(item);
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
		return new PoiNotesWrapper(aChar);
	}

}
