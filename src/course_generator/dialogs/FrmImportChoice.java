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

package course_generator.dialogs;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import course_generator.utils.Utils;

//import org.joda.time.DateTime;


public class FrmImportChoice  extends javax.swing.JDialog {
	public final static int RESULT_CANCEL=0;
	public final static int RESULT_AT_BEGIN=1;
	public final static int RESULT_AT_END=2;
	
	private ResourceBundle bundle;
	private boolean ok;
	
	private JPanel jPanelButtons;
	private JButton btCancel;
	private JButton btOk;
	private JRadioButton rbAtBeginning;
	private JRadioButton rbAtEnd;
	private ButtonGroup groupChoice;
	

		
	/**
	 * Creates new form frmSettings
	 */
	public FrmImportChoice() {
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		initComponents();
		setModal(true);
	}

	
	public int showDialog() {
		int res=RESULT_CANCEL;
		// Set field
		rbAtEnd.setSelected(true);
		
		// End set field
		ok = false;

		//-- Show the dialog
		setVisible(true);
		
		if (ok) {
			// Copy fields
			if (rbAtBeginning.isSelected())
				res=RESULT_AT_BEGIN;
			else if (rbAtEnd.isSelected())
				res=RESULT_AT_END;
		}
		return res;
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
		int line=0;
		
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(bundle.getString("FrmImportChoice.title"));
		setAlwaysOnTop(true);
		setResizable(false);
		setType(java.awt.Window.Type.UTILITY);
		addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
		

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new GridBagLayout());

		rbAtBeginning = new JRadioButton(bundle.getString("FrmImportChoice.chkAtBeginning.Text"));
		rbAtBeginning.setPreferredSize(new Dimension(50,25));
		Utils.addComponent(paneGlobal, rbAtBeginning, 
				0, line++, 
				1, 1, 
				1, 0, 
				10, 10, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		rbAtEnd = new JRadioButton(bundle.getString("FrmImportChoice.chkAtEnd.Text"));
		rbAtEnd.setPreferredSize(new Dimension(50,25));
		Utils.addComponent(paneGlobal, rbAtEnd, 
				0, line++, 
				1, 1, 
				1, 1, 
				5, 10, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		 groupChoice = new ButtonGroup();
		 groupChoice.add(rbAtBeginning);
		 groupChoice.add(rbAtEnd);
		
		// == BUTTONS
		// ===========================================================
		jPanelButtons = new javax.swing.JPanel();
		jPanelButtons.setLayout(new FlowLayout());
		Utils.addComponent(paneGlobal, jPanelButtons, 
				0, line, 
				GridBagConstraints.REMAINDER, 1, 
				1, 0, 
				10, 0, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);


		btOk = new javax.swing.JButton();
		btOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/valid.png")));
		btOk.setText(bundle.getString("FrmImportChoice.btOk.text"));
		btOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				RequestToClose();
			}
		});

		btCancel = new javax.swing.JButton();
		btCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/cancel.png")));
		btCancel.setText(bundle.getString("FrmImportChoice.btCancel.text"));
		btCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setVisible(false);
			}
		});

		// -- Add buttons
		jPanelButtons.add(btOk);
		jPanelButtons.add(btCancel);

		// --
		pack();

		setLocationRelativeTo(null);
	}

	
	protected void formComponentShown(ComponentEvent evt) {
		repaint();
	}
	
}
