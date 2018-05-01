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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
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

public class FrmSelectMap extends javax.swing.JDialog {
	private ResourceBundle bundle;
	private boolean ok;

	private JPanel jPanelButtons;
	private JButton btCancel;
	private JButton btOk;
	private JPanel panelMain;
	private JRadioButton rbOpenStreetMap;
	private JRadioButton rbOpenTopoMap;
	private ButtonGroup groupMap;
	private JRadioButton rbBingAerialMap;


	/**
	 * Creates new form frmSettings
	 */
	public FrmSelectMap() {
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		initComponents();
		setModal(true);
	}


	/**
	 * Show the dialog to selct the map
	 * 
	 * @param map
	 *            map code : 0 : OpenStreetMap 1 : OpenTopoMap 2 : BingAerial
	 * @return Return the select map code. -1 if the selection has been cancelled
	 */
	public int showDialog(int map) {
		switch (map) {
		case 0:
			rbOpenStreetMap.setSelected(true);
			break;
		case 1:
			rbOpenTopoMap.setSelected(true);
			break;
		case 2:
			rbBingAerialMap.setSelected(true);
			break;
		default:
			rbOpenStreetMap.setSelected(true);
		}
		// End set field
		ok = false;

		// -- Show the dialog
		setVisible(true);
		if (ok) {
			if (rbOpenStreetMap.isSelected())
				return 0;
			if (rbOpenTopoMap.isSelected())
				return 1;
			if (rbBingAerialMap.isSelected())
				return 2;
		}
		return -1;
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
		setTitle(bundle.getString("FrmSelectMap.title"));
		setAlwaysOnTop(true);
		setResizable(false);
		setType(java.awt.Window.Type.UTILITY);

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new GridBagLayout());

		// == Panel start
		panelMain = new JPanel();
		panelMain.setLayout(new GridBagLayout());
		Utils.addComponent(paneGlobal, panelMain, 0, 0, 1, 1, 1, 1, 10, 10, 10, 10, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.BOTH);

		rbOpenStreetMap = new JRadioButton(bundle.getString("FrmSelectMap.rbOpenStreetMap.Text"));
		Utils.addComponent(panelMain, rbOpenStreetMap, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		rbOpenTopoMap = new JRadioButton(bundle.getString("FrmSelectMap.rbOpenTopoMap.Text"));
		Utils.addComponent(panelMain, rbOpenTopoMap, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		rbBingAerialMap = new JRadioButton(bundle.getString("FrmSelectMap.rbBingAerialMap.Text"));
		Utils.addComponent(panelMain, rbBingAerialMap, 0, 2, 1, 1, 1, 1, 0, 0, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		groupMap = new ButtonGroup();
		groupMap.add(rbOpenStreetMap);
		groupMap.add(rbOpenTopoMap);
		groupMap.add(rbBingAerialMap);

		// == BUTTONS
		// ===========================================================
		jPanelButtons = new javax.swing.JPanel();
		jPanelButtons.setLayout(new FlowLayout());
		Utils.addComponent(paneGlobal, jPanelButtons, 0, 3, 1, 1, 0, 0, 0, 0, 0, 0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL);

		btCancel = new javax.swing.JButton();
		btCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/cancel.png")));
		btCancel.setText(bundle.getString("Global.btCancel.text"));
		btCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setVisible(false);
			}
		});

		btOk = new javax.swing.JButton();
		btOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/valid.png")));
		btOk.setText(bundle.getString("Global.btOk.text"));
		btOk.setMinimumSize(btCancel.getMinimumSize());
		btOk.setPreferredSize(btCancel.getPreferredSize());
		btOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				RequestToClose();
			}
		});

		// -- Add buttons
		jPanelButtons.add(btOk);
		jPanelButtons.add(btCancel);

		// --
		pack();

		setLocationRelativeTo(null);
	}

}
