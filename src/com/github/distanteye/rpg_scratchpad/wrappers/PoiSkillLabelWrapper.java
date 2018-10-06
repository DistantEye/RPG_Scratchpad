/**
 * 
 */
package com.github.distanteye.rpg_scratchpad.wrappers;

import com.github.distanteye.rpg_scratchpad.model.Poi;

/**
 * @author Vigilant
 *
 */
public class PoiSkillLabelWrapper extends PoiAccessWrapper<String> {
	protected String key;
	
	public PoiSkillLabelWrapper(Poi aChar, String key)
	{
		super(aChar);
		this.key = key;
	}	
	
	public String getValue()
	{
		return aChar.getSkillLabel(key);
	}
	
	public void setValue(String item)
	{
		aChar.setSkillLabel(key, item);
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
		return new PoiSkillLabelWrapper(aChar, key);
	}

}
