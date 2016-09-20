/*
 * Course Generator
 * Copyright (C) 2016 Pierre Delore
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package course_generator.settings;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import course_generator.cgConstants;
import course_generator.utils.Utils;
/**
*
* @author pierre.delore
*/
public class frmSettings extends javax.swing.JDialog {
	   private boolean ok;
	   private CgSettings settings;
	   private java.util.ResourceBundle bundle;
	private JLabel lbLanguage;
	private JComboBox cbLanguage;
	private JPanel jPanelButtons;
	private JButton btCancel;
	private JButton btOk;
	private JLabel lbUnit;
	private JComboBox cbUnit;

	   
	   /**
	     * Creates new form frmSettings
	     */
	    public frmSettings() {
	    	bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
	    	initComponents();
	        setModal(true); 
	    }

	    
	    public boolean showDialog(CgSettings s) {
	        settings = s;
	        // Set field

	        //-- Language
	        if (s.Language.isEmpty())
	            cbLanguage.setSelectedIndex(0);
	        else if (s.Language.equalsIgnoreCase("EN"))
	            cbLanguage.setSelectedIndex(1);
	        else if (s.Language.equalsIgnoreCase("FR"))
	            cbLanguage.setSelectedIndex(2);

	        //-- Units
	        if (s.Unit==cgConstants.UNIT_METER)
	        	cbUnit.setSelectedIndex(0);
	        else if (s.Unit==cgConstants.UNIT_MILES_FEET)
	        	cbUnit.setSelectedIndex(1);
	        else cbUnit.setSelectedIndex(0);
	        
	        // End set field
	        ok = false;

	        setVisible(true);
	      
	        if (ok) {
	            //Copy fields
	        	
	        	//-- Language
	            switch (cbLanguage.getSelectedIndex()) {
	                case 0: //Default
	                    s.Language="";
	                    break;
	                case 1: //English
	                    s.Language="EN";
	                    break;
	                case 2: //French
	                    s.Language="FR";
	                    break;
	                default: //Default
	                    s.Language="";
	            }

		        //-- Units
	            switch (cbUnit.getSelectedIndex()) {
                case 0: //Kilometer / Feet
                    s.Unit=cgConstants.UNIT_METER;
                    break;
                case 1: //Miles / Feet
                    s.Unit=cgConstants.UNIT_MILES_FEET;
                    break;
                default: //Default
                	s.Unit=cgConstants.UNIT_METER;
	            }
	            
            }
	        return ok;
	    }
	   
	    /**
	     * Manage low level key strokes ESCAPE : Close the window
	     *
	     * @return
	     */
	    protected JRootPane createRootPane() {
	        JRootPane rootPane = new JRootPane();
	        KeyStroke strokeEscape = KeyStroke.getKeyStroke("ESCAPE"); 
	        KeyStroke strokeEnter = KeyStroke.getKeyStroke("ENTER"); 

	        Action actionListener = new AbstractAction() {
	            public void actionPerformed(ActionEvent actionEvent) {
	                setVisible(false);
	            }
	        };

	        Action actionListenerEnter = new AbstractAction() {
	            public void actionPerformed(ActionEvent actionEvent) {
	                RequestToClose();
	            }
	        };

	        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	        inputMap.put(strokeEscape, "ESCAPE"); 
	        rootPane.getActionMap().put("ESCAPE", actionListener); 

	        inputMap.put(strokeEnter, "ENTER"); 
	        rootPane.getActionMap().put("ENTER", actionListenerEnter); 

	        return rootPane;
	    }

	    
	    private void RequestToClose() {
	        boolean param_valid = true;
	        //check that the parameters are ok

	        
	        //-- Ok?
	        if (param_valid) {
	            ok = true;
	            setVisible(false);
	        }
	    }

	    
	    /**
	     * This method is called to initialize the form.
	     */
	    private void initComponents() {

	        int line = 0;
	        //jPanelMainWindowsColor = new javax.swing.JPanel();

	        
	        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
	        setTitle(bundle.getString("frmSettings.title")); 
	        setAlwaysOnTop(true);
	        setType(java.awt.Window.Type.UTILITY);
	        
	        //-- Layout ------------------------------------------------------------
	        Container paneGlobal=getContentPane();
	        paneGlobal.setLayout(new GridBagLayout());
	        
	        //-- LANGUAGE - String
	        lbLanguage = new javax.swing.JLabel();
	        lbLanguage.setText(bundle.getString("frmSettings.lbLanguage.text")); 
	        Utils.addComponent(paneGlobal, lbLanguage, 
	        		0, line, 
	        		1, 1,
	        		1, 0,
	        		0, 5, 0, 0,
	        		GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

	        cbLanguage = new javax.swing.JComboBox<>();
	        String language[]={
	        		bundle.getString("frmSettings.LanguageDefault"),
	        		bundle.getString("frmSettings.LanguageEN"),
	        		bundle.getString("frmSettings.LanguageFR")};
	        cbLanguage.setModel(new javax.swing.DefaultComboBoxModel<>(language));
	        Utils.addComponent(paneGlobal, cbLanguage, 
	        		1, line++, 
	        		1, 1,
	        		0, 0,
	        		0, 5, 0, 5,
	        		GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
	        
	        line++;
	        
	        //-- UNIT - int - Unit
	        lbUnit = new javax.swing.JLabel();
	        lbUnit.setText(bundle.getString("frmSettings.lbUnit.text")); 
	        Utils.addComponent(paneGlobal, lbUnit, 
	        		0, line, 
	        		1, 1,
	        		1, 0,
	        		0, 5, 0, 0,
	        		GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

	        cbUnit = new javax.swing.JComboBox<>();
	        String units[]={
	        		bundle.getString("frmSettings.Units.KmM"),
	        		bundle.getString("frmSettings.Units.MilesFeet")};
	        cbUnit.setModel(new javax.swing.DefaultComboBoxModel<>(units));
	        Utils.addComponent(paneGlobal, cbUnit, 
	        		1, line++, 
	        		1, 1,
	        		0, 0,
	        		0, 5, 0, 5,
	        		GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
	        
	        //-- Separator
            //-- NOCONNECTIONONSTARTUP - Boolean -bNoConnectOnStartup
            //-- CONNECTIONTIMEOUT - int - ConnectionTimeout
	        
	        //== BUTTONS ===========================================================
	        jPanelButtons = new javax.swing.JPanel();
	        jPanelButtons.setLayout(new FlowLayout());
	        Utils.addComponent(paneGlobal, jPanelButtons, 
	        		0, line, 
	        		GridBagConstraints.REMAINDER, 1,
	        		0, 0,
	        		0, 0, 0, 0,
	        		GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);

	        btCancel = new javax.swing.JButton();
	        btCancel.setText(bundle.getString("frmSettings.btCancel.text")); 
	        btCancel.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	setVisible(false);	                
	            }
	        });
	        
	        btOk = new javax.swing.JButton();
	        btOk.setText(bundle.getString("frmSettings.btOk.text")); 
	        btOk.setMinimumSize(btCancel.getMinimumSize());
	        btOk.setPreferredSize(btCancel.getPreferredSize());
	        btOk.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                RequestToClose();
	            }
	        });
	        
	        //-- Add buttons
	        jPanelButtons.add(btOk);
	        jPanelButtons.add(btCancel);

	        //--
	        pack();
	        
	        setLocationRelativeTo(null);
	    }
}

