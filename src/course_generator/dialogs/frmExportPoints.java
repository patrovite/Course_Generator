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
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import course_generator.settings.CgSettings;
import course_generator.utils.CgConst;
import course_generator.utils.Utils;

public class frmExportPoints extends javax.swing.JDialog {
	private static final long serialVersionUID = -1550046376301650415L;
	private ResourceBundle bundle;
	private CgSettings settings;
	private boolean ok;
	// private CgSettings settings;
	private JPanel jPanelButtons;
	private JButton btCancel;
	private JButton btOk;
	// private TrackData track;
	// private CgData data;
	// private int line;
	private JPanel panelMain;
	private JCheckBox chkIncludeHightPt;
	private JCheckBox chkIncludeLowPt;
	private JCheckBox chkIncludeEatStation;
	private JCheckBox chkIncludeDrinkStation;
	private JCheckBox chkIncludeMark;
	private JCheckBox chkIncludePhoto;
	private JCheckBox chkIncludeNotes;
	private JLabel lbIncludeHightPt;
	private JLabel lbIncludeLowPt;
	private JLabel lbIncludeEatStation;
	private JLabel lbIncludeDrinkStation;
	private JLabel lbIncludeMark;
	private JLabel lbIncludePhoto;
	private JLabel lbIncludeNotes;
	private JCheckBox chkIncludeRoadbook;
	private JLabel lbIncludeRoadbook;
	private JCheckBox chkIncludeInfo;
	private JLabel lbIncludeInfo;
	private JCheckBox chkIncludeDropBag;
	private JLabel lbIncludeDropBag;
	private JCheckBox chkIncludeCrew;
	private JLabel lbIncludeCrew;
	private JCheckBox chkIncludeFirstAid;
	private JLabel lbIncludeFirstAid;

	/**
	 * Creates new form frmSettings
	 */
	public frmExportPoints(CgSettings settings) {
		this.settings = settings;
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		initComponents();
		setModal(true);
	}

	public int showDialog() {
		chkIncludeHightPt.setSelected(true);
		chkIncludeLowPt.setSelected(true);
		chkIncludeEatStation.setSelected(true);
		chkIncludeDrinkStation.setSelected(true);
		chkIncludeMark.setSelected(true);
		chkIncludePhoto.setSelected(true);
		chkIncludeNotes.setSelected(true);
		chkIncludeRoadbook.setSelected(true);
		chkIncludeInfo.setSelected(true);
		chkIncludeDropBag.setSelected(true);
		chkIncludeCrew.setSelected(true);
		chkIncludeFirstAid.setSelected(true);

		// End set field
		ok = false;

		// -- Show the dialog
		setVisible(true);

		if (ok) {
			int tag = 0;
			// Higher point
			if (chkIncludeHightPt.isSelected())
				tag = tag | CgConst.TAG_HIGH_PT;

			// Lower point
			if (chkIncludeLowPt.isSelected())
				tag = tag | CgConst.TAG_LOW_PT;

			// Station
			if (chkIncludeEatStation.isSelected())
				tag = tag | CgConst.TAG_EAT_PT;

			// Drink
			if (chkIncludeDrinkStation.isSelected())
				tag = tag | CgConst.TAG_WATER_PT;

			// Mark
			if (chkIncludeMark.isSelected())
				tag = tag | CgConst.TAG_MARK;

			// Roadbook
			if (chkIncludeRoadbook.isSelected())
				tag = tag | CgConst.TAG_ROADBOOK;

			// Photo
			if (chkIncludePhoto.isSelected())
				tag = tag | CgConst.TAG_COOL_PT;

			// Note
			if (chkIncludeNotes.isSelected())
				tag = tag | CgConst.TAG_NOTE;

			// Info
			if (chkIncludeInfo.isSelected())
				tag = tag | CgConst.TAG_INFO;

			// Drop bag
			if (chkIncludeDropBag.isSelected())
				tag = tag | CgConst.TAG_DROPBAG;

			// Crew
			if (chkIncludeCrew.isSelected())
				tag = tag | CgConst.TAG_CREW;

			// First Aid
			if (chkIncludeFirstAid.isSelected())
				tag = tag | CgConst.TAG_FIRST_AID;

			return tag;
		} else
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
		setTitle(bundle.getString("frmExportPoints.title"));
		setAlwaysOnTop(true);
		setResizable(false);
		setType(java.awt.Window.Type.UTILITY);

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new GridBagLayout());

		// == Left panel
		panelMain = new JPanel();
		panelMain.setLayout(new GridBagLayout());
		Utils.addComponent(paneGlobal, panelMain, 0, 0, 1, 1, 1, 1, 10, 10, 10, 10, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.BOTH);

		// --
		chkIncludeHightPt = new javax.swing.JCheckBox();
		Utils.addComponent(panelMain, chkIncludeHightPt, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		lbIncludeHightPt = new JLabel(bundle.getString("frmExportPoints.lbIncludeHightPt"), // Include high points
				Utils.getIcon(this, "high_point.png", settings.DialogIconSize), JLabel.LEFT);
		Utils.addComponent(panelMain, lbIncludeHightPt, 1, 0, 1, 1, 1, 0, 0, 5, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		// --
		chkIncludeLowPt = new javax.swing.JCheckBox();
		Utils.addComponent(panelMain, chkIncludeLowPt, 0, 1, 1, 1, 0, 0, 5, 0, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		lbIncludeLowPt = new JLabel(bundle.getString("frmExportPoints.lbIncludeLowPt"), // Include low points
				Utils.getIcon(this, "low_point.png", settings.DialogIconSize), JLabel.LEFT);
		Utils.addComponent(panelMain, lbIncludeLowPt, 1, 1, 1, 1, 1, 0, 5, 5, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		// --
		chkIncludeEatStation = new javax.swing.JCheckBox();
		Utils.addComponent(panelMain, chkIncludeEatStation, 0, 2, 1, 1, 0, 0, 5, 0, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		lbIncludeEatStation = new JLabel(bundle.getString("frmExportPoints.lbIncludeEatStation"), // Include eat station
																									// points
				Utils.getIcon(this, "eat.png", settings.DialogIconSize), JLabel.LEFT);
		Utils.addComponent(panelMain, lbIncludeEatStation, 1, 2, 1, 1, 1, 0, 5, 5, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		// --
		chkIncludeDrinkStation = new javax.swing.JCheckBox();
		Utils.addComponent(panelMain, chkIncludeDrinkStation, 0, 3, 1, 1, 0, 0, 5, 0, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		lbIncludeDrinkStation = new JLabel(bundle.getString("frmExportPoints.lbIncludeDrinkStation"), // Include drink
																										// station
																										// points
				Utils.getIcon(this, "drink.png", settings.DialogIconSize), JLabel.LEFT);
		Utils.addComponent(panelMain, lbIncludeDrinkStation, 1, 3, 1, 1, 1, 0, 5, 5, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		// --
		chkIncludeMark = new javax.swing.JCheckBox();
		Utils.addComponent(panelMain, chkIncludeMark, 0, 4, 1, 1, 0, 0, 5, 0, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		lbIncludeMark = new JLabel(bundle.getString("frmExportPoints.lbIncludeMark"), // Include mark points
				Utils.getIcon(this, "flag.png", settings.DialogIconSize), JLabel.LEFT);
		Utils.addComponent(panelMain, lbIncludeMark, 1, 4, 1, 1, 1, 0, 5, 5, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		// --
		chkIncludePhoto = new javax.swing.JCheckBox();
		Utils.addComponent(panelMain, chkIncludePhoto, 0, 5, 1, 1, 0, 0, 5, 0, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		lbIncludePhoto = new JLabel(bundle.getString("frmExportPoints.lbIncludePhoto"), // Include photo points
				Utils.getIcon(this, "photo.png", settings.DialogIconSize), JLabel.LEFT);
		Utils.addComponent(panelMain, lbIncludePhoto, 1, 5, 1, 1, 1, 0, 5, 5, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		// --
		chkIncludeRoadbook = new javax.swing.JCheckBox();
		Utils.addComponent(panelMain, chkIncludeRoadbook, 0, 6, 1, 1, 0, 0, 5, 0, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		lbIncludeRoadbook = new JLabel(bundle.getString("frmExportPoints.lbIncludeRoadbook"), // Include roadbook points
				Utils.getIcon(this, "roadbook.png", settings.DialogIconSize), JLabel.LEFT);
		Utils.addComponent(panelMain, lbIncludeRoadbook, 1, 6, 1, 1, 1, 0, 5, 5, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		// --
		chkIncludeInfo = new javax.swing.JCheckBox();
		Utils.addComponent(panelMain, chkIncludeInfo, 0, 7, 1, 1, 0, 0, 5, 0, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		lbIncludeInfo = new JLabel(bundle.getString("frmExportPoints.lbIncludeInfo"), // Include information points
				Utils.getIcon(this, "info.png", settings.DialogIconSize), JLabel.LEFT);
		Utils.addComponent(panelMain, lbIncludeInfo, 1, 7, 1, 1, 1, 0, 5, 5, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		// --
		chkIncludeNotes = new javax.swing.JCheckBox();
		Utils.addComponent(panelMain, chkIncludeNotes, 0, 8, 1, 1, 0, 1, 5, 0, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		lbIncludeNotes = new JLabel(bundle.getString("frmExportPoints.lbIncludeNotes"), // Include notes points
				Utils.getIcon(this, "note.png", settings.DialogIconSize), JLabel.LEFT);
		Utils.addComponent(panelMain, lbIncludeNotes, 1, 8, 1, 1, 1, 1, 5, 5, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		// --
		chkIncludeDropBag = new javax.swing.JCheckBox();
		Utils.addComponent(panelMain, chkIncludeDropBag, 0, 9, 1, 1, 0, 1, 5, 0, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		lbIncludeDropBag = new JLabel(bundle.getString("frmExportPoints.lbIncludeDropBag"),
				Utils.getIcon(this, "dropbag.png", settings.DialogIconSize), JLabel.LEFT);
		Utils.addComponent(panelMain, lbIncludeDropBag, 1, 9, 1, 1, 1, 1, 5, 5, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		// --
		chkIncludeCrew = new javax.swing.JCheckBox();
		Utils.addComponent(panelMain, chkIncludeCrew, 0, 10, 1, 1, 0, 1, 5, 0, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		lbIncludeCrew = new JLabel(bundle.getString("frmExportPoints.lbIncludeCrew"),
				Utils.getIcon(this, "crew.png", settings.DialogIconSize), JLabel.LEFT);
		Utils.addComponent(panelMain, lbIncludeCrew, 1, 10, 1, 1, 1, 1, 5, 5, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		// --
		chkIncludeFirstAid = new javax.swing.JCheckBox();
		Utils.addComponent(panelMain, chkIncludeFirstAid, 0, 11, 1, 1, 0, 1, 5, 0, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL);

		lbIncludeFirstAid = new JLabel(bundle.getString("frmExportPoints.lbIncludeFirstAid"),
				Utils.getIcon(this, "first_aid.png", settings.DialogIconSize), JLabel.LEFT);
		Utils.addComponent(panelMain, lbIncludeFirstAid, 1, 11, 1, 1, 1, 1, 5, 5, 0, 0, GridBagConstraints.WEST,
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

		setLocationRelativeTo(null);
	}

}
