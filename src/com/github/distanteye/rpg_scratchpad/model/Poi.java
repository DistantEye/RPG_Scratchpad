/**
 * 
 */
package com.github.distanteye.rpg_scratchpad.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Poi objects store the information for a particular line in the characters/actors section of the UI
 * There can be more than one Poi (Person of Interest)
 * 
 * Although the UI may limit the number of skills stored/displayed, the structure itself can hold an arbitrary amount of skills
 * Aside from skills, basic simple data metrics about the Poi are stored such as modifiers, life, name, notes, etc
 * 
 * @author Vigilant
 *
 */
public class Poi {

	protected HashMap<String,Skill> skillMap;
	protected int allModifier;
	protected int life;
	protected String notes;
	protected String name;
	protected boolean selected;
	
	
	/**
	 * Creates a new Poi with all fields set to default values and empty maps
	 */
	public Poi() {
		skillMap = new HashMap<String, Skill>();
		allModifier = 0;
		life = 0;
		notes = "";
		selected = true;
	}
	
	/**
	 * Clones a new Poi based on old Data
	 */
	public Poi(Poi source) {
		skillMap = new HashMap<String, Skill>();
		
		var oldSkillMap = source.skillMap;
		for (String oldKey : oldSkillMap.keySet())
		{
			var oldSkill = oldSkillMap.get(oldKey);
			skillMap.put(oldKey, new Skill(oldSkill.label, oldSkill.value, oldSkill.modifier));
		}		
		
		allModifier = source.allModifier;
		life = source.life;
		name = source.name;
		notes = source.notes;
		selected = source.selected;
	}
	
	/**
	 * Adds or replaces a Skill to the Poi's mappings. The value and modifier will be zero (even if this is a replace)
	 * @param key Any valid string. Sensible skill names are preferred by convention but not enforced aside from skill names needing to be unique
	 * @return A new Skill object with label of key and 0 values for value and modifier
	 */
	protected Skill addSkill(String key)
	{
		if (getAllSkillLabels().contains(key))
		{
			throw new IllegalArgumentException("Name is already used by a different skill and must be unique");
		}
		
		Skill result = new Skill(key, 0 , 0);
		skillMap.put(key, result);
		return result;
	}
	
	/**
	 * Retrieves the skill object in the Poi mapped to key if it exists, else creates a default skill mapped to that key
	 * @param key Any valid string. Sensible skill names are preferred by convention but not enforced
	 * @return The existing Skill object matching key, if existing, if not, a new Skill object as if addSkill(key) had been called 
	 */
	protected Skill getSkill(String key)
	{
		if (skillMap.containsKey(key))
		{
			return skillMap.get(key);
		}
		else
		{
			// in this context autocreate is more orderly/convenient to do than a reject
			return addSkill(key);
		}
	}
	
	/**
	 * Attempts to look up a skill mapped to key and return the Skill name/label. Note that by default label=key, 
	 * this can be changed later so the return result of the method will not always equal the input. 
	 * As well, this will autocreate a default Skill if key doesn't reference a default Skill
	 * @param key Any valid string. Sensible skill names are preferred by convention but not enforced
	 * @return The existing Skill object matching key's label, if existing. If not, a new default Skill object will be created and its label will be returned 
	 */
	public String getSkillLabel(String key)
	{
		Skill skill = getSkill(key);
		return skill.label;
	}
	
	/**
	 * Returns a List of all mapped Skills' labels. Note as per getSkillLabel, the Skill label is not always equal to the mapped Skill's key
	 * @return ArrayList of all existing Skills labels
	 */
	public ArrayList<String> getAllSkillLabels()
	{
		ArrayList<String> res = new ArrayList<String>();
		for (Skill sk : skillMap.values())
		{
			res.add(sk.label);
		}
		
		return res;
	}
	
	/**
	 * Changes the label for a skill mapped to key (or, if no matching skill exists creates a default skill mapped to that key and acts on it)
	 * @param key Any valid string. Sensible skill names are preferred by convention but not enforced
	 * @param value A valid string to change the label to. It cannot be one already in use by a mapped skill (including the Skill mapped to key)
	 */
	public void setSkillLabel(String key, String value)
	{
		if (!getSkillLabel(key).equals(value) && getAllSkillLabels().contains(value))
		{
			throw new IllegalArgumentException("Label is already used by a different skill and must be unique");
		}
		
		Skill skill = getSkill(key);
		skill.label = value;
	}
	
	// the rest of methods are simple accessors/mutators that should be self documenting enough
	
	public int getSkillValue(String key)
	{		
		Skill skill = getSkill(key);
		return skill.value;		
	}
	
	public void setSkillValue(String key, int value)
	{
		Skill skill = getSkill(key);
		skill.value = value;		
	}
	
	public int getSkillModifier(String key)
	{
		Skill skill = getSkill(key);
		return skill.modifier;
	}
	
	public int getSkillModifierTotal(String key)
	{
		Skill skill = getSkill(key);
		return skill.modifier + getAllModifier();
	}
	
	public void setSkillModifier(String key, int value)
	{
		Skill skill = getSkill(key);
		skill.modifier = value;
	}

	public int getAllModifier() {
		return allModifier;
	}



	public void setAllModifier(int allModifier) {
		this.allModifier = allModifier;
	}



	public int getLife() {
		return life;
	}



	public void setLife(int life) {
		this.life = life;
	}	


	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}		

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * A small container class to capture the minimalist needs for a Skill : a name/label, a value, and a modifier that can offset that value
	 * @author Vigilant	 *
	 */
	private class Skill
	{
		public String label;
		public int value;
		public int modifier;		
		
		public Skill(String label, int value, int modifier)
		{
			this.label = label;
			this.value = value;
			this.modifier = modifier;
		}
	}
	
}
