/*
 * Course Generator
 * Copyright (C) 2019 Pierre Delore
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

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import course_generator.settings.CgSettings;
import course_generator.utils.JTextFieldLimit;
import course_generator.utils.Utils;

public class frmSearchDistance extends javax.swing.JDialog {
	private static final long serialVersionUID = 2353353406416913549L;
	private double value;
	private CgSettings settings;
	private java.util.ResourceBundle bundle;
	private JLabel lbDistance;
	private JTextFieldLimit tfDistance;
	private JPanel jPanelButtons;
	private JButton btCancel;
	private JButton btOk;

	/**
	 * Creates new form frmSettings
	 */
	public frmSearchDistance(CgSettings settings) {
		super();
		this.settings = settings;
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		initComponents();
		setModal(true);
	}

	/**
	 * Display the dialog box
	 * 
	 * @param settings
	 * @param track
	 * @return -1 if invalid input or cancel
	 */
	public double showDialog(CgSettings settings) {
		if (settings == null)
			return -1;

		this.value = -1;
		this.settings = settings;
		setVisible(true);
		return value;
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

		@SuppressWarnings("serial")
		Action actionListener = new AbstractAction() {
			public void actionPerformed(ActionEvent actionEvent) {
				setVisible(false);
			}
		};

		@SuppressWarnings("serial")
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
		value = Utils.ParseDoubleEx(tfDistance.getText(), -1.0);
		if (value == -1.0)
			tfDistance.setBackground(Color.MAGENTA);
		else
			setVisible(false);
	}

	/**
	 * This method is called to initialize the form.
	 */
	private void initComponents() {
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(bundle.getString("frmSearchDistance.title"));
		setAlwaysOnTop(true);
		setType(java.awt.Window.Type.NORMAL);
		setResizable(false);

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new GridBagLayout());

		// -- Label distance
		lbDistance = new javax.swing.JLabel();
		lbDistance.setFocusable(false);
		lbDistance.setText(bundle.getString("frmSearchDistance.lbDistance.text"));
		Utils.addComponent(paneGlobal, lbDistance, 0, 0, 1, 1, 0, 0, 10, 5, 2, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		tfDistance = new JTextFieldLimit(10);
		tfDistance.setFocusable(true);
		tfDistance.setMinimumSize(new Dimension(100, 20));
		tfDistance.setHorizontalAlignment(javax.swing.JTextField.CENTER);
		Utils.addComponent(paneGlobal, tfDistance, 1, 0, 1, 1, 1, 0, 10, 5, 2, 5, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		// == BUTTONS
		// ===========================================================
		jPanelButtons = new javax.swing.JPanel();
		jPanelButtons.setLayout(new FlowLayout());
		Utils.addComponent(paneGlobal, jPanelButtons, 0, 1, GridBagConstraints.REMAINDER, 1, 0, 0, 0, 0, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);

		btCancel = new javax.swing.JButton();
		btCancel.setIcon(Utils.getIcon(this, "cancel.png", settings.DialogIconSize));
		btCancel.setText(bundle.getString("Global.btCancel.text"));
		btCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				value = -1.0;
				setVisible(false);
			}
		});

		btOk = new javax.swing.JButton();
		btOk.setIcon(Utils.getIcon(this, "valid.png", settings.DialogIconSize));
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

		// -- Center the windows
		setLocationRelativeTo(null);
	}
}
