package com.github.distanteye.rpg_scratchpad.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.AbstractAction;
import javax.swing.ComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.github.distanteye.rpg_scratchpad.model.Poi;
import com.github.distanteye.rpg_scratchpad.ui.validators.NonEmptyValidator;
import com.github.distanteye.rpg_scratchpad.ui.validators.NumericValidator;
import com.github.distanteye.rpg_scratchpad.wrappers.*;

/**
 * Main UI class organizing application interface for the user. Interfaced to allow for other UI possibilities, but is currently the only one
 * 
 * @author Vigilant
 *
 */
public class RPG_ScratchPad implements UI {
	private JMenuBar menuBar;
	private JMenu fileMenu, helpMenu;
	private JMenuItem save,load, helpChat;
	
	protected JTextArea mainStatus;
	protected JFrame mainWindow;
	
	protected JScrollPane mainScroll, secondaryScroll, bottomScroll;
	
	protected GBagPanel mainPanel, outputBox, rightPanel, bottomPanel;	
	private BorderLayout windowLayout;
	
	protected JTextPane mainOut, secondaryOut;
	protected JComboBox<String> chatAs;
	protected JTextField chatText;

	protected HashMap<String, Color> nameColor;
	
	protected LogEnabledText fullText;	
	protected LogEnabledText secondaryText;
	
	protected JTextField numSides;
	protected JComboBox<Integer> numDice;
	
	protected JTextField modifier1,modifier2;
	protected JTextField diceOut1,diceOut2;
	
	protected SecureRandom rng;
	
	protected int mainTextSize;
	
	protected boolean poiSelectedHeader;
	protected ArrayList<Poi> poiList;
	protected ArrayList<AccessWrapper<String>> poiNameFieldAccessors;
	protected ArrayList<AccessWrapper<String>> poiSkillLabelAccessors;
	
	protected ArrayList<AccessWrapper<String>> poiLifeLabelAccessors;
	protected ArrayList<AccessWrapper<String>> poiSkill1LabelAccessors;
	protected ArrayList<AccessWrapper<String>> poiSkill2LabelAccessors;
	
	protected double outputBoxMaxXProp = 0.7458, outputBoxMaxYProp = 0.5769;
	protected double secondaryOutMaxXProp = 0.2542, secondaryOutMaxYProp = 0.5769;
	protected double bottomScrollMaxXProp = 1.0, bottomScrollMaxYProp = 0.317;
	
	private PrintStream mainWriter, secondaryWriter;
	
	/**
	 * Creates and initializes the UI, setting up the full layout
	 * @throws HeadlessException
	 */
	public RPG_ScratchPad() throws HeadlessException
	{
		String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
		try {
			mainWriter = new PrintStream(new FileOutputStream(timeStamp+"_MAIN.txt"), true,"UTF-8");
			secondaryWriter = new PrintStream(new FileOutputStream(timeStamp+"_ROLLS.txt"), true,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		poiSelectedHeader = true;
		poiLifeLabelAccessors = new ArrayList<AccessWrapper<String>>();
		poiSkill1LabelAccessors = new ArrayList<AccessWrapper<String>>();
		poiSkill2LabelAccessors = new ArrayList<AccessWrapper<String>>();
		
		// start with a single blank Poi - we need to do this very early so the reference to the List is valid		
		poiList = new ArrayList<Poi>();		
		poiNameFieldAccessors = new ArrayList<AccessWrapper<String>>();
		poiSkillLabelAccessors = new ArrayList<AccessWrapper<String>>();
		
		mainTextSize = 24;
		rng = new SecureRandom();
		
		nameColor = new HashMap<String, Color>();
		
		fullText = new LogEnabledText(mainWriter);
		secondaryText = new LogEnabledText(secondaryWriter);
		
        mainWindow = new JFrame();  
        windowLayout = new BorderLayout();
		
        mainWindow.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        mainWindow.setLayout(windowLayout);
        mainPanel = new GBagPanel();
        
        mainWindow.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        mainWindow.add(mainPanel);   
		
		outputBox = new GBagPanel();
		//mainPanel.addC(outputBox, 0, 0, 8, 12, GridBagConstraints.RELATIVE);
		
		mainOut = new JTextPane();
		mainOut.setEditable(false);
		outputBox.setPreferredSize(new Dimension(900, 400));
		
		
		//outputBox.addTextArea(0, 0, 30, 90);
		
		mainScroll = new JScrollPane(mainOut);
        mainScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScroll.setMinimumSize(mainOut.getPreferredSize());
        
        outputBox.addC(mainScroll, 0, 0, 6, 5, GridBagConstraints.BOTH);
        
		// add mainScroll to window, then add mainPanel to that
        mainPanel.setNextWeights(1, 2).addC(outputBox, 0, 0, 6, 5, GridBagConstraints.BOTH);
		
        // right bar
        
        rightPanel = new GBagPanel();
		
		mainPanel.addC(rightPanel,6,0, 6, 6, GridBagConstraints.BOTH);
		rightPanel.addLabel(0, 0, "Roll d(");
		numSides = rightPanel.addTextF(1, 0, "6", 3, null);
		rightPanel.addLabel(2, 0, ") x ");
		numDice = new JComboBox<Integer>(new Integer[] {1,2});
		rightPanel.addC(numDice, 3, 0);
		
		
		rightPanel.endRow(4, 0);
		
		rightPanel.addMappedTF(EditState.NOTFIXED, 0, 1, "Roll Label", "rollLabel1", 6, "", Orientation.VERTICAL, null, null);
		rightPanel.addMappedTF(EditState.NOTFIXED, 1, 1, "Modifiers", "rollMod1", 6, "", Orientation.VERTICAL, null, null);
		rightPanel.addMappedTF(EditState.NOTFIXED, 2, 1, "Roll Label", "rollLabel2", 6, "", Orientation.VERTICAL, null, null);
		rightPanel.addMappedTF(EditState.NOTFIXED, 3, 1, "Modifiers", "rollMod2", 6, "", Orientation.VERTICAL, null, null);
		rightPanel.endRow(4, 1);
		rightPanel.endRow(5, 2);
		
		rightPanel.addButton(4, 2, "Roll").addActionListener(new ActionListener() {			
            public void actionPerformed(ActionEvent e)
            {
            	rightBarRoll();
            }	
		});
		
		
		
		rightPanel.endRow(3, 3);
				
		secondaryOut = new JTextPane();
		secondaryOut.setEditable(false);
		secondaryOut.setPreferredSize(new Dimension(400, 300));		
		
		secondaryScroll = new JScrollPane(secondaryOut);
		secondaryScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		secondaryScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		secondaryScroll.setMinimumSize(secondaryOut.getPreferredSize());
        
        rightPanel.addC(secondaryScroll, 0, 4, 4, 4, GridBagConstraints.BOTH);
        rightPanel.addButton(0, 9, "Add To Main").addActionListener(new ActionListener() {			
            public void actionPerformed(ActionEvent e)
            {
            	int secTextLen = secondaryText.size();
            	
            	if (secTextLen > 0)
            	{
            		String line = "***Roll*** " +secondaryText.get(secTextLen-1);
            		fullText.add(new NameStringPair(null, line));
            		appendColoredText(mainOut, line, Color.BLACK,mainTextSize, mainScroll);
            	}
            }	
		});
        
        rightPanel.addLabel(0, 10, "Roll Opposed");
        rightPanel.addLabel(1, 10, "Character");
        rightPanel.addLabel(2, 10, "Skill");
        rightPanel.addLabel(3, 10, "Special Modifier");
        rightPanel.addMappedComboBox(EditState.FIXED, 1, 11, "", "rollTarget1", null, 
        		new AccessWrapperMux(poiNameFieldAccessors, "|,|"), new String[0], "|,|");
        rightPanel.addMappedComboBox(EditState.FIXED, 2, 11, "", "rollTarget1Skill", null, 
        		new AccessWrapperMux(this.poiSkillLabelAccessors, "|,|"), new String[0], "|,|");
        rightPanel.addMappedTF(EditState.NOTFIXED, 3, 11, "Mod", "rollTarget1SpecialMod", 3, "0", null, null, null);
        
        rightPanel.addMappedComboBox(EditState.FIXED, 1, 12, "", "rollTarget2", null, 
        		new AccessWrapperMux(poiNameFieldAccessors, "|,|"), new String[0], "|,|");
        rightPanel.addMappedComboBox(EditState.FIXED, 2, 12, "", "rollTarget2Skill", null, 
        		new AccessWrapperMux(this.poiSkillLabelAccessors, "|,|"), new String[0], "|,|");
        rightPanel.addMappedTF(EditState.NOTFIXED, 3, 12, "Mod", "rollTarget2SpecialMod", 3, "0", null, null, null);
        rightPanel.addButton(0, 13, "Roll").addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				rightBarRollOpposed();
			}
        	
        });
        
        rightPanel.endVertical(1, 13);
		
        
        // chat manipulation part
		mainPanel.addLabel(0, 7, "As:");
		chatAs = new JComboBox<String>();
		chatAs.setEditable(true);
		mainPanel.addC(chatAs,1, 7);		
		chatText = mainPanel.addTextF(2, 7, "", 64, null);
		chatText.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
			    if (e.getKeyCode()==KeyEvent.VK_ENTER){
			        pushText();
			        chatText.setText("");
			    }
			}
		});		
		
		// adds a keyboard shortcut Ctrl+X to switch between the first through nine entries in the chat as combobox
		// we have to use Key Bindings to get around the focus issues, we want this to be global issuable
		// note that a lot of components can have issues hosting an inputmap, but textboxes are very cooperative, so we use one of the textboxes with window focus event
		chatText.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control 1"), new ChatAsSwitchAction(chatAs, 1));
		chatText.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control 2"), new ChatAsSwitchAction(chatAs, 2));
		chatText.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control 3"), new ChatAsSwitchAction(chatAs, 3));
		chatText.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control 4"), new ChatAsSwitchAction(chatAs, 4));
		chatText.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control 5"), new ChatAsSwitchAction(chatAs, 5));
		chatText.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control 6"), new ChatAsSwitchAction(chatAs, 6));
		chatText.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control 7"), new ChatAsSwitchAction(chatAs, 7));
		chatText.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control 8"), new ChatAsSwitchAction(chatAs, 8));
		chatText.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control 9"), new ChatAsSwitchAction(chatAs, 9));
		
		// alt c brings up a simple calculator
		chatText.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("alt C"), new ChatAsCalcAction());
		
		
		mainPanel.addMappedButton(3,7,"Say").addActionListener(new ActionListener() {
			
            public void actionPerformed(ActionEvent e)
            {            	
            	pushText();
            	chatText.setText("");
            }	
		});		
		
		mainPanel.endRow(4, 7);
		
		// at 8, start bottom panel?
		bottomPanel = new GBagPanel();		
		
		bottomScroll = new JScrollPane(bottomPanel);
		bottomScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		bottomScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		bottomScroll.setPreferredSize(new Dimension(900,300));
		
		mainPanel.addC(bottomScroll, 0, 8, 6, 12, GridBagConstraints.BOTH);	
		mainPanel.addSpecialChild(bottomPanel); // the scroll hides bottomPanel from being registered properly by the normal infrastructure
		
		renderPoiBar(bottomPanel, 0, bottomScroll);		
		
		//mainPanel.endVertical(5, 9);
		
		// There's strange distortions during text and mouse events that shouldn't be happening since they don't change sizing but are anyways
		// This enforces strict dimensioning which would appear to help with that
		mainWindow.getRootPane().addComponentListener(new ComponentAdapter() 
		{  
	        public void componentResized(ComponentEvent evt) {
	            Component c = (Component)evt.getSource();
	            int width = c.getWidth();
	            int height = c.getHeight();
	            
	            // swing is kinda shoddy at this so we mash commands until it hopefully works
	            
	            outputBox.setMinimumSize(new Dimension((int)(width*outputBoxMaxXProp), (int)(height*outputBoxMaxYProp)));
	            outputBox.setPreferredSize(new Dimension((int)(width*outputBoxMaxXProp), (int)(height*outputBoxMaxYProp)));	            
	            outputBox.setMaximumSize(new Dimension((int)(width*outputBoxMaxXProp), (int)(height*outputBoxMaxYProp)));
	            outputBox.setSize(new Dimension((int)(width*outputBoxMaxXProp), (int)(height*outputBoxMaxYProp)));
	            secondaryOut.setMinimumSize(new Dimension((int)(width*secondaryOutMaxXProp), (int)(height*secondaryOutMaxYProp)));
	            secondaryOut.setPreferredSize(new Dimension((int)(width*secondaryOutMaxXProp), (int)(height*secondaryOutMaxYProp)));
	            secondaryOut.setMaximumSize(new Dimension((int)(width*secondaryOutMaxXProp), (int)(height*secondaryOutMaxYProp)));
	            secondaryOut.setSize(new Dimension((int)(width*secondaryOutMaxXProp), (int)(height*secondaryOutMaxYProp)));
	            bottomScroll.setMinimumSize(new Dimension((int)(width*bottomScrollMaxXProp), (int)(height*bottomScrollMaxYProp)));
	            bottomScroll.setPreferredSize(new Dimension((int)(width*bottomScrollMaxXProp), (int)(height*bottomScrollMaxYProp)));
	            bottomScroll.setMaximumSize(new Dimension((int)(width*bottomScrollMaxXProp), (int)(height*bottomScrollMaxYProp)));
	            bottomScroll.setSize(new Dimension((int)(width*bottomScrollMaxXProp), (int)(height*bottomScrollMaxYProp)));
	            
	            mainWindow.revalidate();
	            mainWindow.repaint();
	        }
		});
		
		mainWindow.setSize(1600, 900);

		mainWindow.setVisible(true);
		
		// setup menu
        save = new JMenuItem("Save");
        load = new JMenuItem("Load");
        
        helpChat = new JMenuItem("ChatBox Shortcuts");
        
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        helpMenu = new JMenu("Help");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.add(save);
        fileMenu.add(load);
        menuBar.add(fileMenu);
        
        helpMenu.add(helpChat);
        menuBar.add(helpMenu);
        mainWindow.setJMenuBar(menuBar);
        save.addActionListener(new ClickListener());
        load.addActionListener(new ClickListener());
        
        helpChat.addActionListener(new ClickListener());
        
	}
	
	/**
	 * Creates (or recreates) the Poi section of the UI, returning the new y offset based on the number of rows used
	 * @param parent The Panel that will contain the Poi Section this is creating
	 * @param y The current y offset of Parent to start adding things at
	 * @param optionalScroll An optional reference to a scrollbar being used by this section 
	 * so the scroll position can be updated after (re)initialization is finished
	 * @return Returns the new y offset based on the original y plus the number of rows used by this method
	 */
	protected int renderPoiBar(GBagPanel parent, int y, JScrollPane optionalScroll)
	{
		String lifeLabel = "Life";
		String skill1Label = "Skill1";
		String skill2Label = "Skill2";
		
		// preserve existing labels if they exist
		JTextField lifeField = parent.getTextF("lifeLabel");
		JTextField sk1Field = parent.getTextF("skill1Label");
		JTextField sk2Field = parent.getTextF("skill2Label");
		if (lifeField != null)
		{
			lifeLabel = lifeField.getText();
		}		
		if (sk1Field != null)
		{
			skill1Label = sk1Field.getText();
		}
		if (sk2Field != null)
		{
			skill2Label = sk2Field.getText();
		}
		
		parent.removeAll();
		poiNameFieldAccessors.clear();
		poiSkillLabelAccessors.clear();
		poiLifeLabelAccessors.clear();
		poiSkill1LabelAccessors.clear();
		poiSkill2LabelAccessors.clear();
		
		// make sure we always have at least one stub
		if (poiList.size() == 0)
		{
			poiList.add(new Poi());
		}
		
		y = addPoiHeader(parent, y, lifeLabel, skill1Label,skill2Label, optionalScroll);
		int i = 0;
		for (Poi p : poiList)
		{			
			y = addPoiRow(parent, y, p, i++);
		}					
		
		y = addPoiFooter(parent,y);
		
		parent.endVertical(0, y);
		
		parent.revalidate();
		
		if (optionalScroll != null)
		{
			JScrollBar verticalBar = optionalScroll.getVerticalScrollBar();
        	verticalBar.setValue( verticalBar.getMaximum() );
			optionalScroll.revalidate();
			optionalScroll.repaint();
			
		}
		
		return y;
	}	
	
	/**
	 * Creates the table header aspects for the Poi section 
	 * @param parent The Panel that will contain the Poi subsection this is creating
	 * @param y The current y offset of Parent to start adding things at
	 * @param lifeLabel Label for the Poi's life stat
	 * @param s1Label Label for the first customizable skill for the table display
	 * @param s2Label Label for the second customizable skill for the table display
	 * @return Returns the new y offset based on the original y plus the number of rows used by this method
	 */
	protected int addPoiHeader(GBagPanel parent, int y, String lifeLabel, String s1Label, String s2Label, JScrollPane optionalScroll)
	{
		int x = 0;		
		
		JCheckBox selectedBox = new JCheckBox("",poiSelectedHeader);
		selectedBox.addActionListener(new AllBoxToggler(poiList,selectedBox, parent, optionalScroll));		
		parent.addC(selectedBox,x++,y);
		
		parent.addLabel(x++, y, "Name");
		parent.addMappedTF(EditState.NOTFIXED, x++, y, "", "lifeLabel", 6, lifeLabel, null, null, new MassAccessWrapper<String>(poiLifeLabelAccessors)).setInputVerifier(new NonEmptyValidator());
		parent.addLabel(x++, y, "All Modifier");
		parent.addMappedTF(EditState.NOTFIXED, x++, y, "", "skill1Label", 6, s1Label, null, this, new MassAccessWrapper<String>(poiSkill1LabelAccessors)).setInputVerifier(new NonEmptyValidator());
		parent.addLabel(x++, y, "Modifier");
		parent.addMappedTF(EditState.NOTFIXED, x++, y, "", "skill2Label", 6, s2Label, null, this, new MassAccessWrapper<String>(poiSkill2LabelAccessors)).setInputVerifier(new NonEmptyValidator());
		parent.addLabel(x++, y, "Modifier");
		parent.addLabel(x++, y, "Attack");
		parent.addLabel(x++, y, "Modifier");
		parent.addLabel(x++, y, "Defense");
		parent.addLabel(x++, y, "Modifier");
		parent.addLabel(x++, y, "Notes");
		
		JTextField sk1 = (JTextField) parent.getMappedComponent("skill1Label").getComp();
		JTextField sk2 = (JTextField) parent.getMappedComponent("skill2Label").getComp();
		
		poiSkillLabelAccessors.add(new TextComponentWrapper(sk1));
		poiSkillLabelAccessors.add(new TextComponentWrapper(sk2));
		poiSkillLabelAccessors.add(new StringWrapper("Attack"));
		poiSkillLabelAccessors.add(new StringWrapper("Defense"));
		
		return y+1;
	}	
	
	/**
	 * Completes the Poi table with the bottom set of buttons for acting on the Poi set as well as managing the finishing aspects of the section layout
	 * @param parent The Panel that will contain the Poi subsection this is creating
	 * @param y The current y offset of Parent to start adding things at
	 * @return Returns the new y offset based on the original y plus the number of rows used by this method
	 */
	protected int addPoiFooter(GBagPanel parent, int y)
	{
		int x = 0;
		parent.addLabel(x++, y, "");
		parent.addLabel(x++, y, "");
		parent.addLabel(x++, y, "");
		parent.addLabel(x++, y, "");
		parent.addButton(x++, y, "Roll Selected").addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
            {
				rollSelectedPoi("Skill1");
            }	
			
		});
		parent.addLabel(x++, y, "");
		parent.addButton(x++, y, "Roll Selected").addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
            {
				rollSelectedPoi("Skill2");
            }	
			
		});
		parent.addLabel(x++, y, "");
		parent.addButton(x++, y, "Roll Selected").addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
            {
				rollSelectedPoi("Attack");
            }	
			
		});
		parent.addLabel(x++, y, "");
		parent.addButton(x++, y, "Roll Selected").addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
            {
				rollSelectedPoi("Defense");
            }	
			
		});
		x++;
		parent.addButton(x++, y, "Add Line").addActionListener(new PoiAddListener(parent, bottomScroll, false));
		parent.addButton(x++, y, "Copy Last").addActionListener(new PoiAddListener(parent, bottomScroll, true));
		parent.addLabel(0, y+1, ""); 
		parent.addLabel(0, y+2, ""); 
		parent.addLabel(0, y+3, ""); // extra padding to help scroll still keep the bottom bar visible
		
		return y+4;
	}
	
	/**
	 * Adds a row in the table for a particular Poi, filling in values to match the stored values for that object
	 * @param parent The Panel that will contain the Poi subsection this is creating
	 * @param y The current y offset of Parent to start adding things at
	 * @param poi The linked Poi object to use
	 * @param poiIndex The current row number, starting at 0
	 * @return Returns the new y offset based on the original y plus the number of rows used by this method
	 */
	protected int addPoiRow(GBagPanel parent, int y, Poi poi, int poiIndex)
	{
		int x = 0;
		EditState nf = EditState.NOTFIXED;
		
		JCheckBox selectedBox = new JCheckBox("",poi.isSelected());
		selectedBox.addActionListener(new CheckBoxUpdater(new PoiSelectedWrapper(poi),selectedBox));		
		parent.addC(selectedBox,x++,y);
		parent.addMappedTF(nf, x++, y, "", "poiName"+poiIndex, 6, poi.getName(), null, this, new PoiNameWrapper(poi));
		parent.addMappedTF(nf, x++, y, "", "poiLife"+poiIndex, 6, ""+poi.getLife(), null, this, new PoiLifeWrapper(poi)).setInputVerifier(new NumericValidator());
		parent.addMappedTF(nf, x++, y, "", "poiAllMod"+poiIndex, 6, ""+poi.getAllModifier(), null, this, new PoiAllModifierWrapper(poi)).setInputVerifier(new NumericValidator());
		parent.addMappedTF(nf, x++, y, "", "poiSkill1Val"+poiIndex, 6, ""+poi.getSkillValue("Skill1"), null, this, new PoiSkillValueWrapper(poi,"Skill1")).setInputVerifier(new NumericValidator());
		parent.addMappedTF(nf, x++, y, "", "poiSkill1Mod"+poiIndex, 6, ""+poi.getSkillModifier("Skill1"), null, this, new PoiSkillModifierWrapper(poi,"Skill1")).setInputVerifier(new NumericValidator());
		parent.addMappedTF(nf, x++, y, "", "poiSkill2Val"+poiIndex, 6, ""+poi.getSkillValue("Skill2"), null, this, new PoiSkillValueWrapper(poi,"Skill2")).setInputVerifier(new NumericValidator());
		parent.addMappedTF(nf, x++, y, "", "poiSkill2Mod"+poiIndex, 6, ""+poi.getSkillModifier("Skill2"), null, this, new PoiSkillModifierWrapper(poi,"Skill2")).setInputVerifier(new NumericValidator());
		parent.addMappedTF(nf, x++, y, "", "poiAttackVal"+poiIndex, 6, ""+poi.getSkillValue("Attack"), null, this, new PoiSkillValueWrapper(poi,"Attack")).setInputVerifier(new NumericValidator());
		parent.addMappedTF(nf, x++, y, "", "poiAttackMod"+poiIndex, 6, ""+poi.getSkillModifier("Attack"), null, this, new PoiSkillModifierWrapper(poi,"Attack")).setInputVerifier(new NumericValidator());
		parent.addMappedTF(nf, x++, y, "", "poiDefenseVal"+poiIndex, 6, ""+poi.getSkillValue("Defense"), null, this, new PoiSkillValueWrapper(poi,"Defense")).setInputVerifier(new NumericValidator());
		parent.addMappedTF(nf, x++, y, "", "poiDefenseMod"+poiIndex, 6, ""+poi.getSkillModifier("Defense"), null, this, new PoiSkillModifierWrapper(poi,"Defense")).setInputVerifier(new NumericValidator());
		parent.addMappedTF(nf, x++, y, "", "poiNotes"+poiIndex, 60, ""+poi.getNotes(), null, this, new PoiNotesWrapper(poi));
		parent.addButton(x++, y, "X").addActionListener(new PoiRemoveListener(parent,poiIndex, bottomScroll));
		
		poiLifeLabelAccessors.add(new PoiSkillLabelWrapper(poi,"Life"));
		poiSkill1LabelAccessors.add(new PoiSkillLabelWrapper(poi,"Skill1"));
		poiSkill2LabelAccessors.add(new PoiSkillLabelWrapper(poi,"Skill2"));
		
		poiNameFieldAccessors.add(new TextComponentWrapper((JTextField)parent.getMappedComponent("poiName"+poiIndex).getComp()));
		
		return y+1;
	}
	
	/**
	 * For all Poi with isSelected as true, rolls dice (see RollDice) using the values for Skill specified by the string skill.
	 * This allows for rolling the same common skill across many actors at once
	 * @param skill The key of the Skill to roll. This skill should exist but does not need to because of the way Poi.getSkill methods work,
	 * it would just autocreate the missing Skill
	 */
	protected void rollSelectedPoi(String skill)
	{
		String sidesText = numSides.getText();
		if (!Utils.isInteger(sidesText))
		{
			handleError("Invalid number of dice sides!");
		}
		
		int numSides = Integer.parseInt(sidesText);
		
		ArrayList<Poi> selectedPois = new ArrayList<Poi>();
		for (Poi poi : poiList)
		{
			if (poi.isSelected()) { selectedPois.add(poi); }
		}
		
		RollInfo[] rInfo = new RollInfo[selectedPois.size()];
		int idx = 0;
		
    	for (Poi poi : selectedPois)
    	{
    		String label = "[" + poi.getName() + "] " + poi.getSkillLabel(skill);
    		int modifier = poi.getSkillValue(skill) + poi.getSkillModifierTotal(skill);
    		
    		rInfo[idx++] = new RollInfo(label,
					numSides,
					modifier);
    	}
    	
    	rollDice(rInfo, false);
	}	
	
	/**
	 * Rolls 1-2 dice based on the settings in the top right section. 
	 * The UI allows for configuring number of dice sides, labels, and modifiers.
	 * This information is then processed to rollDice for use
	 */
	protected void rightBarRoll()
	{
		String sidesText = numSides.getText();
		if (!Utils.isInteger(sidesText))
		{
			handleError("Invalid number of dice sides!");
		}
		
		int numSides = Integer.parseInt(sidesText);
		
    	int intNDice = (int)(numDice.getSelectedItem());
    	RollInfo[] rInfo = new RollInfo[intNDice];
    	rInfo[0] = new RollInfo(rightPanel.getTextF("rollLabel1").getText(),
    							numSides,
    							rightPanel.getTextFIntVal("rollMod1"));
    	
    	
    	if ((int)numDice.getSelectedItem() == 2)
    	{
    		rInfo[1] = new RollInfo(rightPanel.getTextF("rollLabel2").getText(),
					numSides,
					rightPanel.getTextFIntVal("rollMod2"));
    	}
    	
    	rollDice(rInfo, false);
	}
	
	/**
	 * Helper method that matches the redefinable skill labels to the equivalent fixed system keys for that skill
	 * @param label string containing Label to check against. Currently this will only return useful results for a certain set of values
	 * @return For Attack|Defense, returns label. For the value inside the skill1Label or skill2Label, returns Skill1/Skill2. Throws error otherwise
	 */
	protected String getKeyFromLabel(String label)
	{
		if (label.equals("Attack") || label.equals("Defense"))
		{
			return label;
		}
		else if (bottomPanel.getTextF("skill1Label").getText().equals(label))
		{
			return "Skill1";
		}
		else if (bottomPanel.getTextF("skill2Label").getText().equals(label))
		{
			return "Skill2";
		}
		else
		{
			throw new IllegalArgumentException(label + " not found");
		}
	}
		
	protected Poi getPoiFromName(String name)
	{
		for (Poi p : poiList)
		{
			if (p.getName().equals(name))
			{
				return p;
			}
		}
		
		throw new IllegalArgumentException(name + " not found");
	}
	
	/**
	 * Rolls two dice based on the settings in the middle right section
	 * This is a quick shortcut for rolling dice skills from two different Poi's against each other, based on selected skils
	 * The information and options are packaged together then set to rollDice for use and display
	 */
	protected void rightBarRollOpposed()
	{
		String sidesText = numSides.getText();
		if (!Utils.isInteger(sidesText))
		{
			handleError("Invalid number of dice sides!");
		}
		
		int numSides = Integer.parseInt(sidesText);
		
    	RollInfo[] rInfo = new RollInfo[2];
    	
    	for (int i = 1; i <= 2; i++)
    	{
    		String skillLabel = rightPanel.getComboBoxText("rollTarget"+i+"Skill");
    		String poiName = rightPanel.getComboBoxText("rollTarget"+i); 
    		String rollLabel = poiName + 
    				"[" + skillLabel  +"]";
    		
    		int localMod = rightPanel.getTextFIntVal("rollTarget"+i+"SpecialMod"); 
    		
    		String skillKey = getKeyFromLabel(skillLabel);
    		
    		Poi selectedPoi = getPoiFromName(poiName);
    		
    		int skillModifiers = selectedPoi.getSkillValue(skillKey) + selectedPoi.getSkillModifierTotal(skillKey);
    		
    		
    		rInfo[i-1] = new RollInfo(rollLabel,
					numSides,
					localMod+skillModifiers);
    	}
    	
    	rollDice(rInfo, true);
	}
	
	/**
	 * Rolls dice according to conditions specified in the first argument, sending the results to display in the secondary textbox
	 * @param rolls An array of RollInfo objects. RollInfo specifies the number of sides for the dice, any modifiers, and what label to display in text output 
	 * rolls.length is equal to the number of dice to roll.
	 * @param showDifference If true and rolls.length == 2, will display the difference between the two rolls
	 */
	protected void rollDice(RollInfo[] rolls, boolean showDifference)
	{
		// do some kind of highest to lowest sorting? It won't hurt most of the time, and enables Initiative rolls :D
		// also add some extra attack target dropdown to automate "this person attackrolls that person"
		
		ArrayList<RollStringPair> list = new ArrayList<RollStringPair>();
		
		
		for (RollInfo roll : rolls)
		{

			int roll1 = (rng.nextInt(roll.numSides)+1);
			int modifier1 = roll.modifier;
			int sum1 = roll1+modifier1;
			
			String line = roll.label + " : " + sum1 + "(" + roll1 + ")";
			list.add(new RollStringPair(sum1,line));
		}
		
		Collections.sort(list, Collections.reverseOrder());
		String resultLine = "";		
		for (RollStringPair r : list)
		{
			if (!resultLine.equals("")) { resultLine += "\n\t"; }
			resultLine += r.line;
		}
		
		if (showDifference && list.size() == 2)
		{
			if (!resultLine.equals("")) { resultLine += "\n        "; }
			resultLine += "[Difference:" + (list.get(0).value - list.get(1).value) + "]";
		}
		
		
		secondaryText.add(new NameStringPair(null,resultLine));
		String resultLineMod = resultLine.replaceAll("\t", "") + "\n";
		appendColoredText(secondaryOut, resultLineMod, Color.BLACK,16, secondaryScroll);
			
	}
	
	/**
	 * Pushes any text in the chatText JTextBox into both logs and the main textbox ouput, applying text coloring and formatting as appropriate.
	 * The chatAs JComboBox will be used to determine the label for the text output (the name of the person/entity speaking)
	 */
	protected void pushText()
	{
		String chatAsName = (String)chatAs.getSelectedItem();
		updateComboBoxList(chatAs,chatAsName);		
		
		NameStringPair newText = new NameStringPair(chatAsName, chatText.getText());
		fullText.add(newText);
		appendColoredText(mainOut, newText.toString(), getOrAddColor(newText.name),mainTextSize, mainScroll);
	}
	
	/**
	 * Static helper method for accessing the individual stored values in a ComboBox dropdown
	 * @param box string containing ComboBox to search through
	 * @return Returns a list of any string values contained in the Combobox
	 */
	public static ArrayList<String> getValuesComboBoxList(JComboBox<String> box)
	{
		ComboBoxModel<String> model = box.getModel();
		ArrayList<String> values = new ArrayList<String>();
		
		for (int x = 0; x < model.getSize(); x++)
		{
			values.add(model.getElementAt(x));			
		}
		
		return values;
	}
	
	/**
	 * Static helper method for adding values to Combobox dropdown : will do nothing if the input to be added already exists
	 * @param box string containing ComboBox to add to
	 * @param input valid string to to add to the Combobox dropdown
	 */
	public static void updateComboBoxList(JComboBox<String> box, String input)
	{
		ArrayList<String> values = getValuesComboBoxList(box);
		
		if (!values.contains(input))
		{
			box.addItem(input);
		}
	}
	
	/**
	 * Overload of updateComboList : adds multiple values to a Combobox dropdown, ignoring any that already exist
	 * @param box string containing ComboBox to add to
	 * @param input list of valid strings to to add to the Combobox dropdown
	 */
	public static void updateComboBoxList(JComboBox<String> box, ArrayList<String> input)
	{
		for(String str : input)
		{
			updateComboBoxList(box,str);		
		}
	}
	
	/**
	 * Accesses the mapping of string/color pairs, returning the Color that matches the string inputted
	 * or creates, maps, and returns a new Color if no matching one exists
	 * @param name Any valid string
	 * @return Color object tied to the input string
	 */
	protected Color getOrAddColor(String name)
	{
		if (name == null) { return Color.BLACK; }
		
		if (nameColor.containsKey(name))
		{
			return nameColor.get(name);		
		}
		else
		{
			Color newColor = generateNewRandomColor();
			nameColor.put(name, newColor);
			return newColor;
		}
	}
	
	/**
	 * Accesses the mapping of string/color pairs, returning the Color that matches the string inputted
	 * or, if no matching Color exists : maps the passed in Color to the string and returns it.
	 * Note this can be used to violate the normal uniqueness of color values
	 * @param name Any valid string
	 * @param newColor Color object to map to the string passed in (if no mapping already exists)
	 * @return Color object tied to the input string
	 */
	protected Color getOrAddColor(String name, Color newColor)
	{
		if (name == null) { return Color.BLACK; }
		
		if (nameColor.containsKey(name))
		{
			return nameColor.get(name);		
		}
		else
		{
			nameColor.put(name, newColor);
			return newColor;
		}
	}
	
	/**
	 * Generates a new randomized color, repeating the process as necessary until the result is something not stored in the current
	 * color/value mappings.
	 * @return A new Color object of random settings
	 */
	protected Color generateNewRandomColor()
	{
		int r, g, b;
		
		r = 75 + ((int)(Math.random() * 150));
		g = 75 + ((int)(Math.random() * 150));
		b = 75 + ((int)(Math.random() * 150));
		
		float[] hsb = Color.RGBtoHSB(r, g, b, null);
		Color result = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
		
		if (nameColor.containsValue(result))
		{
			// grab a new color to preserve uniqueness
			// this approach will not work on large datasets but the number of name/color pairs in the program
			// will never reach a high enough number to be a concern
			return generateNewRandomColor();
		}
		
		return result;
	}
	
	
	/**
	 * Adds text with the specified color and font settings to the passed in text pane. If JScrollPane is set, the scrollbar will be advanced as well
	 * @param pane Valid JTextPane that the text will be outputed to
	 * @param text Any valid string to be displayed
	 * @param color Instatiated Color object for the text
	 * @param fontSize Fontsize for the text to be displayed
	 * @param optionalScroll Optional JScrollPane reference, if set, will advance the scroll to the end to display the text just added
	 */
	public void appendColoredText(JTextPane pane, String text, Color color, int fontSize, JScrollPane optionalScroll) {
        StyledDocument doc = pane.getStyledDocument();

        Style style = pane.addStyle("ColoredText", null);
        StyleConstants.setForeground(style, color);
        StyleConstants.setFontSize(style, fontSize);
        try {
            doc.insertString(doc.getLength(), System.lineSeparator() + text, style);
            
            if (optionalScroll != null) // normally scroll panes will adjust automatically but won't in all circumstances, so we force it
            {
            	JScrollBar verticalBar = optionalScroll.getVerticalScrollBar();
            	verticalBar.setValue( verticalBar.getMaximum() );
            }
        } 
        catch (BadLocationException e) {
            e.printStackTrace();
        }           
    }
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				@SuppressWarnings("unused")
				RPG_ScratchPad ui = new RPG_ScratchPad();
			}
		});
		
	}
	
	/**
	 * Updates all relevant display fields for the character.
	 * 
	 * Note update should respect updateEnabled, and not act if updateEnabled=false
	 */
	public void update()
	{	
		mainPanel.updateAllComps(true);
	}

	@Override
	public String promptUser(String message, String extraContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean handleError(String message) {
		JOptionPane.showConfirmDialog(null,"Error Resulted: \n"+message, 
				"Error", JOptionPane.OK_OPTION);

		return true;
	}

	@Override
	public void statusUpdate(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}	
	
	/**
	 * Helper class packaging together all the information needed for a dice roll, beyond the roll result itself
	 * Contains the number of sides of the dice to roll, any modifiers, and an (optional) label to prefix the roll display with
	 * @author Vigilant	 
	 */
	private class RollInfo
	{
		public String label;
		public int numSides;
		public int modifier;
		
		public RollInfo(String label, int numSides, int modifier)
		{
			this.label = label;
			this.numSides = numSides;
			this.modifier = modifier;
		}
		
	}
	
	/**
	 * Small class to package together the ability to write to a logfile while retaining an internal store of 
	 * lines posted so far (stored in the form of "name" + "text", since knowing the name of who said the text can effect display color)
	 * 
	 * This should generally be limited to only one instance per text output pane in the UI
	 * @author Vigilant
	 */
	private class LogEnabledText
	{
		private ArrayList<NameStringPair> textLines;
		private PrintStream writer;
		
		public LogEnabledText(PrintStream writer)
		{
			this.writer = writer;
			textLines = new ArrayList<NameStringPair>();
		}
		
		public void add(NameStringPair input)
		{
			textLines.add(input);
			writer.println(input.toString());
			writer.println(); // spacing
		}
		
		public int size()
		{
			return textLines.size();
		}
		
		public NameStringPair get(int idx)
		{
			return textLines.get(idx);
		}
	}

	/**
	 * Structure packaging together text with an optional name for who "said" the text.
	 * Knowing the name allows later output to reference the nameColor mapping and consistently display named text in the same manner
	 * @author Vigilant
	 */
	private class NameStringPair
	{
		public String name;
		public String text;
	
		public NameStringPair(String name, String text)
		{
			this.name = name;
			this.text = text;
		}
		
		public String toString()
		{
			if (name == null || name.trim().equals(""))
			{
				return text;
			}
			
			return name + " :  " + text;
		}
		
	}
	
	/**
	 * Structure packaging together the numerical result of a roll and any text to be attached to it when displaying to the user
	 * @author Vigilant
	 *
	 */
	private class RollStringPair implements Comparable<RollStringPair>
	{
		public int value;
		public String line;
		
		/**
		 * @param value
		 * @param line
		 */
		public RollStringPair(int value, String line) {
			this.value = value;
			this.line = line;
		}

		@Override
		public int compareTo(RollStringPair o) {
			return Integer.compare(value, o.value);
		}


	}
	
	private class PoiRemoveListener implements ActionListener
	{
		private GBagPanel parent; 
		private int poiIdx;
		private JScrollPane scroll; 
		
		public PoiRemoveListener(GBagPanel parent, int poiIdx, JScrollPane scroll)
		{
			this.parent = parent;
			this.poiIdx = poiIdx;
			this.scroll = scroll;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			poiList.remove(poiIdx);
			
			renderPoiBar(parent,0, scroll);
			update();
		}
		
	}
	
	private class PoiAddListener implements ActionListener
	{
		private GBagPanel parent; 
		private JScrollPane scroll;
		private boolean copyLast; 
		
		public PoiAddListener(GBagPanel parent, JScrollPane scroll, boolean copyLast)
		{
			this.parent = parent;
			this.scroll = scroll;
			this.copyLast = copyLast;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Poi newRow = null;
			if (copyLast && poiList.size() > 0)
			{
				Poi lastRow = poiList.get(poiList.size()-1);
				newRow = new Poi(lastRow);
			}
			else
			{
				newRow = new Poi();
			}
			
			poiList.add(newRow);
			renderPoiBar(parent,0, scroll);
		}
		
	}
	
	private class CheckBoxUpdater implements ActionListener
	{
		private AccessWrapper<String> wrapper;
		private JCheckBox thisBox;
		
		public CheckBoxUpdater(AccessWrapper<String> wrapper, JCheckBox thisBox)
		{
			this.wrapper = wrapper;
			this.thisBox = thisBox;
			wrapper.setValue(""+thisBox.isSelected());
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			wrapper.setValue(""+thisBox.isSelected());
		}
		
	}
	
	private class AllBoxToggler implements ActionListener
	{
		private ArrayList<Poi> allPoi;
		private JCheckBox thisBox;
		
		private GBagPanel parent;
		private JScrollPane scroll; 
		
		public AllBoxToggler(ArrayList<Poi> allPoi, JCheckBox thisBox, GBagPanel parent, JScrollPane scroll)
		{
			this.allPoi = allPoi;
			this.thisBox = thisBox;
			this.parent = parent;
			this.scroll = scroll;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			for (Poi p : allPoi)
			{
				p.setSelected(thisBox.isSelected());
			}
			poiSelectedHeader = !poiSelectedHeader;
			renderPoiBar(parent,0, scroll);			
		}
		
	}
	
	/**
	 * Converts the UI data to XML, valid for storing/saving
	 * This includes all Pois as well as most labels in text fields and the results of the chatAs box 
	 * @return Returns a String containing an XML representation of all UI data
	 */
	public String getXML()
	{
		String result = "<Scratchpad>\n";
		
		result += this.getInnerXML();
		
		result += "\n</Scratchpad>";
		
		return result;
	}
	
	/**
	 * Collects the UI data into a set of XML tags, not enclosed in a greater tag,
	 * this less subclasses redefine saving while still being able to draw off the superclass
	 * @return Returns a String containing the UI vital data in a list of XML tags
	 */
	protected String getInnerXML()
	{
		StringWriter result = new StringWriter();
		Element root = new Element("Scratchpad");
		Document doc = new Document(root);
		
		Element rollSettings = new Element("RollSettings");
		rollSettings.addContent(new Element("DiceSides").setText(numSides.getText()));
		rollSettings.addContent(new Element("DiceNumber").setText(numDice.getSelectedItem().toString()));
		rollSettings.addContent(new Element("RollLabel1").setText(rightPanel.getTextF("rollLabel1").getText()));
		rollSettings.addContent(new Element("RollMod1").setText(rightPanel.getTextF("rollMod1").getText()));
		rollSettings.addContent(new Element("RollLabel2").setText(rightPanel.getTextF("rollLabel2").getText()));
		rollSettings.addContent(new Element("RollMod2").setText(rightPanel.getTextF("rollMod2").getText()));
		
		doc.getRootElement().addContent(rollSettings);
		
		Element chatBox = new Element("ChatBox");

		for(String name : this.nameColor.keySet())
		{
			Element person = new Element("Person");
			person.addContent(new Element("Name").setText(name));
			Color c = nameColor.get(name);
			String colorString = c.getRed() + ":" + c.getGreen() + ":" + c.getBlue();
			person.addContent(new Element("Color").setText(colorString));
			chatBox.addContent(person);
		}
		
		doc.getRootElement().addContent(chatBox);
		
		Element poiBar = new Element("PoiBar");
		Element poiBarConfig = new Element("Config");
		
		poiBarConfig.addContent(new Element("LifeSlotName").setText(bottomPanel.getTextF("lifeLabel").getText()));
		poiBarConfig.addContent(new Element("Skill1SlotName").setText(bottomPanel.getTextF("skill1Label").getText()));
		poiBarConfig.addContent(new Element("Skill2SlotName").setText(bottomPanel.getTextF("skill2Label").getText()));
		
		poiBar.addContent(poiBarConfig);
		
		for(Poi p : this.poiList)
		{
			Element poi = new Element("Poi");
			poi.addContent(new Element("Name").setText(p.getName()));
			poi.addContent(new Element("Life").setText("" + p.getLife()));
			poi.addContent(new Element("AllModifier").setText("" + p.getAllModifier()));
			poi.addContent(new Element("Skill1").setText("" + p.getSkillValue("Skill1")));
			poi.addContent(new Element("Skill1Modifier").setText("" + p.getSkillModifier("Skill1")));
			poi.addContent(new Element("Skill2").setText("" + p.getSkillValue("Skill2")));
			poi.addContent(new Element("Skill2Modifier").setText("" + p.getSkillModifier("Skill2")));
			poi.addContent(new Element("Attack").setText("" + p.getSkillValue("Attack")));
			poi.addContent(new Element("AttackModifier").setText("" + p.getSkillModifier("Attack")));
			poi.addContent(new Element("Defense").setText("" + p.getSkillValue("Defense")));
			poi.addContent(new Element("DefenseModifier").setText("" + p.getSkillModifier("Defense")));
			poi.addContent(new Element("Notes").setText(p.getNotes()));
			
			poiBar.addContent(poi);
		}
		
		doc.getRootElement().addContent(poiBar);
		
		
		XMLOutputter xmlOut = new XMLOutputter();
		xmlOut.setFormat(Format.getPrettyFormat().setEncoding("UTF-8").setOmitEncoding(false));
		
		
		try {
			xmlOut.outputElementContent(root, result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result.toString();
	}
	
	/**
	 * Discards the current UI data/display and replaces it with the data encoded into the xml string passed
	 * @param xml a validly formatted XML string as returned by getXML()
	 */
	public void loadXML(String xml)
	{
		//this.setToDefaults();
		
		Document document = Utils.getXMLDoc(xml);
		poiSelectedHeader = true;
		
		Element root = document.getRootElement();
		
		Utils.verifyTag(root, "Scratchpad");
		Utils.verifyChildren(root, new String[]{"RollSettings","ChatBox","PoiBar"});
		
		Element rollSettings = root.getChild("RollSettings");
		Utils.verifyChildren(rollSettings, new String[]{"DiceSides","DiceNumber","RollLabel1","RollMod1","RollLabel2","RollMod2"});
		this.numSides.setText(rollSettings.getChildText("DiceSides"));
		if (rollSettings.getChildText("DiceNumber").equals("1")) { this.numDice.setSelectedIndex(0); } else { this.numDice.setSelectedIndex(1); }
		
		// for 1 and 2 is nearly the same
		for (int x = 1; x <= 2; x++)
		{
			this.rightPanel.setTextF("rollLabel"+x, rollSettings.getChildText("RollLabel"+x));
			this.rightPanel.setTextF("rollMod"+x, rollSettings.getChildText("RollMod"+x));
		}
				
		Element chatBox = root.getChild("ChatBox");
		this.chatAs.removeAllItems();
		
		for (Element e : chatBox.getChildren())
		{
			if (e.getName().equals("Person"))
			{
				Utils.verifyChildren(e, new String[]{"Name", "Color"});
				String name = e.getChildText("Name");
				String[] cStrArr = e.getChildText("Color").split(":");
				if (cStrArr.length != 3) { throw new IllegalArgumentException("Improper number of colors in spec for ChatBoxPerson" + name); }
				int[] cInt = new int[] { Integer.parseInt(cStrArr[0]), Integer.parseInt(cStrArr[1]), Integer.parseInt(cStrArr[2]) };
				Color c = new Color(cInt[0], cInt[1], cInt[2]);
				
				this.getOrAddColor(name, c);
				updateComboBoxList(this.chatAs, name);
			}
		}
		
		Element poiBar = root.getChild("PoiBar");
		Utils.verifyChildren(poiBar, new String[]{"Config", "Poi"});
		
		Element poiBarConfig = poiBar.getChild("Config");
		Utils.verifyChildren(poiBarConfig, new String[]{"LifeSlotName","Skill1SlotName","Skill2SlotName"});
		
		String lifeSlotName = poiBarConfig.getChildText("LifeSlotName");
		String skill1SlotName = poiBarConfig.getChildText("Skill1SlotName");
		String skill2SlotName = poiBarConfig.getChildText("Skill2SlotName");
		
		poiLifeLabelAccessors.get(0).setValue(lifeSlotName);
		poiLifeLabelAccessors.get(0).setValue(skill1SlotName);
		poiLifeLabelAccessors.get(0).setValue(skill2SlotName);
		this.bottomPanel.setTextF("lifeLabel", lifeSlotName);
		this.bottomPanel.setTextF("skill1Label", skill1SlotName);
		this.bottomPanel.setTextF("skill2Label", skill2SlotName);

		this.poiList.clear();
		
		for (Element e : poiBar.getChildren())
		{
			if (e.getName().equals("Poi"))
			{
				Utils.verifyChildren(e, new String[]{"Name", "Life", "AllModifier", "Skill1", "Skill1Modifier", "Skill2", "Skill2Modifier",
													 "Attack", "AttackModifier", "Defense", "DefenseModifier", "Notes"});
				
				String name = e.getChildText("Name");
				int life = Integer.parseInt(e.getChildText("Life"));
				int allModifier = Integer.parseInt(e.getChildText("AllModifier"));
				int skill1 = Integer.parseInt(e.getChildText("Skill1"));
				int skill1Mod = Integer.parseInt(e.getChildText("Skill1Modifier"));
				int skill2 = Integer.parseInt(e.getChildText("Skill2"));
				int skill2Mod = Integer.parseInt(e.getChildText("Skill2Modifier"));
				int attack = Integer.parseInt(e.getChildText("Attack"));
				int attackMod = Integer.parseInt(e.getChildText("AttackModifier"));
				int defense = Integer.parseInt(e.getChildText("Defense"));
				int defenseMod = Integer.parseInt(e.getChildText("DefenseModifier"));
				String notes = e.getChildText("Notes");

				Poi p = new Poi();
				p.setName(name);
				p.setSkillLabel("Life", lifeSlotName);
				p.setLife(life);
				p.setAllModifier(allModifier);
				p.setSkillLabel("Skill1", skill1SlotName);
				p.setSkillValue("Skill1", skill1);
				p.setSkillModifier("Skill1", skill1Mod);
				p.setSkillLabel("Skill2", skill2SlotName);
				p.setSkillValue("Skill2", skill2);
				p.setSkillModifier("Skill2", skill2Mod);
				p.setSkillValue("Attack", attack);
				p.setSkillModifier("Attack", attackMod);
				p.setSkillValue("Defense", defense);
				p.setSkillModifier("Defense", defenseMod);
				p.setNotes(notes);
				
				poiList.add(p);
			}
		}
		
		renderPoiBar(bottomPanel, 0, bottomScroll);
		this.update();
		
	}
	
	public void save(String fileName)
	{
		try {
			PrintWriter fileO = new PrintWriter(new FileOutputStream(fileName));
			update();
			fileO.println(getXML());
			fileO.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File : \"" + fileName + "\" could not be created");
		}
	}

	public void load(String fileName) {
		try {
			File f = new File(fileName);
		    FileInputStream fin = new FileInputStream(f);
		    byte[] buffer = new byte[(int) f.length()];
		    new DataInputStream(fin).readFully(buffer);
		    fin.close();
		    String temp = new String(buffer, "UTF-8");
		    
			loadXML(temp);
			
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File : \"" + fileName + "\" could not be found");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public class ClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(save)) {
				JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
			    FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "Data Files", "xml", "txt");
			    chooser.setFileFilter(filter);
			    int returnVal = chooser.showOpenDialog(mainWindow);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	save(chooser.getSelectedFile().getName());
			    }
			}
			else if (e.getSource().equals(load)) {
				JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
			    FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "Data Files", "xml", "txt");
			    chooser.setFileFilter(filter);
			    int returnVal = chooser.showOpenDialog(mainWindow);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	load(chooser.getSelectedFile().getName());
			    }
			}
			else if (e.getSource().equals(helpChat)) {
				JOptionPane.showMessageDialog(null, "When the chat box is in focus, alt c will try and launch calculator 'calc' command, ctrl 1-9 switches chatAs");
			}
		}
	}
	
	/**
	 * Action listener meant to work with the chatAs box : 
	 * when actionPerformed is triggered (based on InputMap), switches the chatAs combobox to select a particular indexed name
	 * @author Vigilant
	 */
	public class ChatAsSwitchAction extends AbstractAction
	{

		private static final long serialVersionUID = 14624L;
		private JComboBox<String> chatSelector;
		private int selectedIdx;
		
		public ChatAsSwitchAction(JComboBox<String> chatSelector, int idx)
		{
			this.chatSelector = chatSelector;
			this.selectedIdx = idx;
		}
		
		public void actionPerformed(ActionEvent e) {
			if (chatSelector.getModel().getSize() >= selectedIdx)
	    	{
				chatSelector.setSelectedIndex(selectedIdx-1); // we convert to a 0 based system
	    	}
			else
			{
				// silent fail is preferable for this since misskeys may be common
			}
		}		
	}
	
	/**
	 * Action listener meant to work with the chatAs box : 
	 * when actionPerformed is triggered (based on InputMap), brings up a calculator. This is fragile and probably os specific but better than nothing
	 * @author Vigilant
	 */
	public class ChatAsCalcAction extends AbstractAction
	{

		private static final long serialVersionUID = 14625L;
		

		public ChatAsCalcAction()
		{			
		}
		
		public void actionPerformed(ActionEvent e) {
			Runtime run = Runtime.getRuntime();
			try {
				run.exec("calc");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}		
	}
	
}
