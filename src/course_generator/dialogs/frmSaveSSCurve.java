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
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import course_generator.utils.Utils;

public class frmSaveSSCurve extends javax.swing.JDialog {
	private ResourceBundle bundle;
	private boolean ok;

	private JPanel jPanelButtons;
	private JButton btCancel;
	private JButton btOk;
	private JPanel panelMain;
	private JLabel lbName;
	private JTextField tfName;
	private JLabel lbComment;
	private JTextField tfComment;


	/**
	 * Creates new form frmSettings
	 */
	public frmSaveSSCurve() {
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		initComponents();
		setModal(true);
	}


	public boolean showDialog() {
		// End set field
		ok = false;

		// -- Show the dialog
		setVisible(true);
		if (ok) {

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
		// check that the parameters are ok

		String name = tfName.getText();

		if ((!Utils.isFilenameValid(name)) || (name.isEmpty())) {
			JOptionPane.showMessageDialog(this, bundle.getString("frmSaveSSCurve.MsgIncorrectName"));
			param_valid = false;
		} else if (Utils.FileExist(name)) {
			JOptionPane.showMessageDialog(this, bundle.getString("frmSaveSSCurve.MsgFileExist"));
			param_valid = false;
		}

		// -- Ok?
		if (param_valid) {
			ok = true;
			setVisible(false);
		}
	}


	private void initComponents() {
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(bundle.getString("frmSaveSSCurve.title"));
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
		Utils.addComponent(paneGlobal, panelMain, 0, 0, 1, 1, 1, 1, 10, 10, 0, 10, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.BOTH);

		lbName = new JLabel(bundle.getString("frmSaveSSCurve.lbName.Text"));
		Utils.addComponent(panelMain, lbName, 0, 0, 1, 1, 0, 0, 5, 5, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		tfName = new JTextField(30);
		Utils.addComponent(panelMain, tfName, 1, 0, 1, 1, 1, 0, 5, 5, 0, 5, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		lbComment = new JLabel(bundle.getString("frmSaveSSCurve.lbComment.Text"));
		Utils.addComponent(panelMain, lbComment, 0, 1, 1, 1, 0, 0, 0, 5, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		tfComment = new JTextField(30);
		Utils.addComponent(panelMain, tfComment, 1, 1, 1, 1, 1, 0, 0, 5, 0, 5, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

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


	public String getName() {
		if (tfName != null)
			return tfName.getText();
		else
			return "";
	}


	public String getComment() {
		if (tfComment != null)
			return tfComment.getText();
		else
			return "";
	}
}
