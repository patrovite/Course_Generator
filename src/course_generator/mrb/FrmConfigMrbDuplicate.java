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

package course_generator.mrb;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import course_generator.utils.CgConst;
import course_generator.utils.Utils;


public class FrmConfigMrbDuplicate  extends javax.swing.JDialog {

	private ResourceBundle bundle;
	private boolean ok;
	private int config;
	private JPanel panelButtons;
	private JButton btCancel;
	private JButton btOk;
//	private JPanel panelGlobal;
	private JCheckBox chkPosition;
	private JCheckBox chkAlign;
	private JCheckBox chkFormat;
	private JCheckBox chkSize;
	private JCheckBox chkTags;
	private JPanel panelCheck;

	/**
	 * Creates new form frmSettings
	 */
	public FrmConfigMrbDuplicate() {
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		initComponents();
		setModal(true);
	}

	public int showDialog(int config) {
		this.config = config;
		
		chkPosition.setSelected((config & CgConst.MRB_DUP_POS)!=0);
		chkAlign.setSelected((config & CgConst.MRB_DUP_ALIGN)!=0);
		chkFormat.setSelected((config & CgConst.MRB_DUP_FORMAT)!=0);
		chkSize.setSelected((config & CgConst.MRB_DUP_SIZE)!=0);
		chkTags.setSelected((config & CgConst.MRB_DUP_TAGS)!=0);
		
		
		// End set field
		ok = false;
		
		//-- Show the dialog
		setVisible(true);

		if (ok) {
			// Copy fields
			this.config=0;
			if (chkPosition.isSelected())
				this.config=this.config | CgConst.MRB_DUP_POS;
			if (chkAlign.isSelected())
				this.config=this.config | CgConst.MRB_DUP_ALIGN;
			if (chkFormat.isSelected())
				this.config=this.config | CgConst.MRB_DUP_FORMAT;
			if (chkSize.isSelected())
				this.config=this.config | CgConst.MRB_DUP_SIZE;
			if (chkTags.isSelected())
				this.config=this.config | CgConst.MRB_DUP_TAGS;
			
			return this.config;
		}
		return config;
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
		// check that the parameters are ok

		// -- Ok?
		if (param_valid) {
			ok = true;
			setVisible(false);
		}
	}

	
	private void initComponents() {

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setResizable(false);
		setType(java.awt.Window.Type.UTILITY);

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new GridBagLayout());

		//== panelGlobal
		panelCheck = new JPanel();
		panelCheck.setLayout(new GridBagLayout());
		panelCheck.setBorder(BorderFactory.createTitledBorder(bundle.getString("FrmConfigMrbDuplicate.panelCheck.text")));
		Utils.addComponent(paneGlobal, panelCheck, 
				0, 0, 
				1, 1, 
				1, 1, 
				10, 10, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		chkPosition = new javax.swing.JCheckBox(bundle.getString("FrmConfigMrbDuplicate.chkPosition.text"));
		Utils.addComponent(panelCheck, chkPosition, 
				0, 0, 
				1, 1, 
				1, 0, 
				5, 5, 0, 5, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		chkAlign = new javax.swing.JCheckBox(bundle.getString("FrmConfigMrbDuplicate.chkAlign.text"));
		Utils.addComponent(panelCheck, chkAlign, 
				0, 1, 
				1, 1, 
				1, 0, 
				0, 5, 0, 5, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		chkFormat = new javax.swing.JCheckBox(bundle.getString("FrmConfigMrbDuplicate.chkFormat.text"));
		Utils.addComponent(panelCheck, chkFormat, 
				0, 2, 
				1, 1, 
				1, 0, 
				0, 5, 0, 5, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		chkSize = new javax.swing.JCheckBox(bundle.getString("FrmConfigMrbDuplicate.chkSize.text"));
		Utils.addComponent(panelCheck, chkSize, 
				0, 3, 
				1, 1, 
				1, 0, 
				0, 5, 0, 5, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		chkTags = new javax.swing.JCheckBox(bundle.getString("FrmConfigMrbDuplicate.chkTags.text"));
		Utils.addComponent(panelCheck, chkTags, 
				0, 4, 
				1, 1, 
				1, 1, 
				0, 5, 10, 5, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		// == BUTTONS
		// ===========================================================
		panelButtons = new javax.swing.JPanel();
		panelButtons.setLayout(new FlowLayout());
		Utils.addComponent(paneGlobal, panelButtons, 
				0, 1, 
				1, 1, 
				1, 0, 
				0, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		btCancel = new javax.swing.JButton();
		btCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/cancel.png")));
		btCancel.setText(bundle.getString("FrmConfigMrbDuplicate.btCancel.text"));
		btCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setVisible(false);
			}
		});

		btOk = new javax.swing.JButton();
		btOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/valid.png")));
		btOk.setText(bundle.getString("FrmConfigMrbDuplicate.btOk.text"));
		btOk.setMinimumSize(btCancel.getMinimumSize());
		btOk.setPreferredSize(btCancel.getPreferredSize());
		btOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				RequestToClose();
			}
		});

		// -- Add buttons
		panelButtons.add(btOk);
		panelButtons.add(btCancel);

		// --
		pack();

		setLocationRelativeTo(null);
	}

}
