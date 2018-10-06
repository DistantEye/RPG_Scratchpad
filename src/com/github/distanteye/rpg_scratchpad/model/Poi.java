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
	protected HashMap<String,Skill> skillLabelMap;
	protected int allModifier;
	protected int life;
	protected String notes;
	protected String name;
	protected boolean selected;
	
	
	/**
	 * 
	 */
	public Poi() {
		skillMap = new HashMap<String, Skill>();
		allModifier = 0;
		life = 0;
		notes = "";
	}
	
	protected void addSkill(String key)
	{
		skillMap.put(key, new Skill(key, 0 , 0));
	}
	
	protected Skill getSkill(String key)
	{
		if (skillMap.containsKey(key))
		{
			return skillMap.get(key);
		}
		else
		{
			// in this context autocreate is more orderly/convenient to do than a reject
			Skill newSkill = new Skill("",0,0);
			skillMap.put(key, newSkill);
			return newSkill;
		}
	}
	
	public String getSkillLabel(String key)
	{
		Skill skill = getSkill(key);
		return skill.label;
	}
	
	public ArrayList<String> getAllSkillLabels()
	{
		ArrayList<String> res = new ArrayList<String>();
		for (Skill sk : skillMap.values())
		{
			res.add(sk.label);
		}
		
		return res;
	}
	
	public void setSkillLabel(String key, String value)
	{
		if (!getSkillLabel(key).equals(value) && getAllSkillLabels().contains(value))
		{
			throw new IllegalArgumentException("Label is already used by a different skill and must be unique");
		}
		
		Skill skill = getSkill(key);
		skill.label = value;
	}
	
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
