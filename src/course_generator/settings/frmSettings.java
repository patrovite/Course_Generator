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
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import org.jfree.ui.FontChooserDialog;

import course_generator.dialogs.FontChooser;
import course_generator.utils.CgConst;
import course_generator.utils.CgSpinner;
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
	private JLabel lbSpeed;
	private JComboBox cbSpeed;
	private JLabel lbCheck;
	private JCheckBox chkCheck;
	private JLabel lbThresholdAsk;
	private CgSpinner spinThresholdAsk;
	private JLabel lbDefaultFont;
	private JButton btDefaultFont;
	private Font DefaultFont;


	/**
	 * Creates new form frmSettings
	 */
	public frmSettings(CgSettings settings) {
		this.settings = settings;
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		initComponents();
		setModal(true);
	}


	public boolean showDialog() {
		// Set field

		// -- Language
		if (settings.Language.isEmpty())
			cbLanguage.setSelectedIndex(0);
		else if (settings.Language.equalsIgnoreCase("EN"))
			cbLanguage.setSelectedIndex(1);
		else if (settings.Language.equalsIgnoreCase("FR"))
			cbLanguage.setSelectedIndex(2);

		// -- Units
		if (settings.Unit == CgConst.UNIT_METER)
			cbUnit.setSelectedIndex(0);
		else if (settings.Unit == CgConst.UNIT_MILES_FEET)
			cbUnit.setSelectedIndex(1);
		else
			cbUnit.setSelectedIndex(0);

		// -- Speed format
		if (settings.isPace)
			cbSpeed.setSelectedIndex(1);
		else
			cbSpeed.setSelectedIndex(0);
		
		// -- Check for update
		chkCheck.setSelected(settings.Check4UpdateAtStart);

		// -- Threshold
		spinThresholdAsk.setValue((int)	settings.PosFilterAskThreshold);
		
		//-- Default font
		DefaultFont = new Font(settings.DefaultFontName, settings.DefaultFontStyle, settings.DefaultFontSize);
					
		// End set field
		ok = false;

		setVisible(true);

		if (ok) {
			// Copy fields

			String old_language = settings.Language;

			// -- Language
			switch (cbLanguage.getSelectedIndex()) {
			case 0: // Default
				settings.Language = "";
				break;
			case 1: // English
				settings.Language = "EN";
				break;
			case 2: // French
				settings.Language = "FR";
				break;
			default: // Default
				settings.Language = "";
			}

			if (!old_language.equalsIgnoreCase(settings.Language))
				JOptionPane.showMessageDialog(this, bundle.getString("frmSettings.MsgRestart"));

			// -- Units
			switch (cbUnit.getSelectedIndex()) {
			case 0: // Kilometer / Feet
				settings.Unit = CgConst.UNIT_METER;
				break;
			case 1: // Miles / Feet
				settings.Unit = CgConst.UNIT_MILES_FEET;
				break;
			default: // Default
				settings.Unit = CgConst.UNIT_METER;
			}

			// -- Speed format
			switch (cbSpeed.getSelectedIndex()) {
			case 0: // Speed
				settings.isPace = false;
				break;
			case 1: // Miles / Feet
				settings.isPace = true;
				break;
			default: // Default
				settings.isPace = false;
			}

			// -- Check for update
			settings.Check4UpdateAtStart = chkCheck.isSelected();

			// -- Threshold
			settings.PosFilterAskThreshold = spinThresholdAsk.getValueAsInt();
			
			//-- Default font
			settings.DefaultFontName = DefaultFont.getFontName();
			settings.DefaultFontStyle = DefaultFont.getStyle();
			settings.DefaultFontSize = DefaultFont.getSize();
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

		// -- Ok?
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
		// jPanelMainWindowsColor = new javax.swing.JPanel();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(bundle.getString("frmSettings.title"));
		setAlwaysOnTop(true);
		setType(java.awt.Window.Type.UTILITY);

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new GridBagLayout());

		// -- LANGUAGE - String
		lbLanguage = new javax.swing.JLabel();
		lbLanguage.setText(bundle.getString("frmSettings.lbLanguage.text"));
		Utils.addComponent(paneGlobal, lbLanguage, 0, line, 1, 1, 1, 0, 10, 10, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		cbLanguage = new javax.swing.JComboBox<>();
		String language[] = { bundle.getString("frmSettings.LanguageDefault"),
				bundle.getString("frmSettings.LanguageEN"), bundle.getString("frmSettings.LanguageFR") };
		cbLanguage.setModel(new javax.swing.DefaultComboBoxModel<>(language));
		Utils.addComponent(paneGlobal, cbLanguage, 1, line++, 1, 1, 0, 0, 10, 5, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		line++;

		// -- UNIT - int - Unit
		lbUnit = new javax.swing.JLabel();
		lbUnit.setText(bundle.getString("frmSettings.lbUnit.text"));
		Utils.addComponent(paneGlobal, lbUnit, 0, line, 1, 1, 1, 0, 2, 10, 0, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		cbUnit = new javax.swing.JComboBox<>();
		String units[] = { bundle.getString("frmSettings.Units.KmM"), bundle.getString("frmSettings.Units.MilesFeet") };
		cbUnit.setModel(new javax.swing.DefaultComboBoxModel<>(units));
		Utils.addComponent(paneGlobal, cbUnit, 1, line++, 1, 1, 0, 0, 2, 5, 0, 10, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		// -- Speed/Pace
		lbSpeed = new javax.swing.JLabel();
		lbSpeed.setText(bundle.getString("frmSettings.lbSpeed.text"));
		Utils.addComponent(paneGlobal, lbSpeed, 0, line, 1, 1, 1, 0, 2, 10, 0, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		cbSpeed = new javax.swing.JComboBox<>();
		String speeddisplay[] = { bundle.getString("frmSettings.SpeedDisplay.Speed"),
				bundle.getString("frmSettings.SpeedDisplay.Pace") };
		cbSpeed.setModel(new javax.swing.DefaultComboBoxModel<>(speeddisplay));
		Utils.addComponent(paneGlobal, cbSpeed, 1, line++, 1, 1, 0, 0, 2, 5, 0, 10, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		// -- Minimum Threshold for asking if PosFilterAskThreshold
		lbThresholdAsk = new javax.swing.JLabel();
		lbThresholdAsk.setText(bundle.getString("frmSettings.lbThresholdAsk.Text"));// "Elevation"
		Utils.addComponent(paneGlobal, lbThresholdAsk, 0, line, 1, 1, 1, 0, 2, 10, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		spinThresholdAsk = new CgSpinner(0, 0, 100, 1);
		Utils.addComponent(paneGlobal, spinThresholdAsk, 1, line++, 1, 1, 0, 0, 2, 5, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		// -- Check for update
		lbCheck = new javax.swing.JLabel();
		lbCheck.setText(bundle.getString("frmSettings.lbCheck.text"));
		Utils.addComponent(paneGlobal, lbCheck, 0, line, 1, 1, 1, 0, 7, 10, 0, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		chkCheck = new javax.swing.JCheckBox();
		Utils.addComponent(paneGlobal, chkCheck, 1, line++, 1, 1, 0, 0, 7, 5, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Default font
		lbDefaultFont = new javax.swing.JLabel();
		lbDefaultFont.setText(bundle.getString("frmSettings.lbDefaultFont.text"));
		Utils.addComponent(paneGlobal, lbDefaultFont, 0, line, 1, 1, 1, 0, 7, 10, 0, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		
		btDefaultFont = new javax.swing.JButton("...");
		btDefaultFont.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//spinDiff.setValue(100);
				ChooseFont();
			}
		});
		Utils.addComponent(paneGlobal, btDefaultFont, 1, line++, 1, 1, 0, 0, 7, 5, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		
		// -- Separator
		// -- NOCONNECTIONONSTARTUP - Boolean -bNoConnectOnStartup
		// -- CONNECTIONTIMEOUT - int - ConnectionTimeout

		// == BUTTONS
		// ===========================================================
		jPanelButtons = new javax.swing.JPanel();
		jPanelButtons.setLayout(new FlowLayout());
		Utils.addComponent(paneGlobal, jPanelButtons, 0, line, GridBagConstraints.REMAINDER, 1, 0, 0, 10, 0, 0, 0,
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
	
	/**
	 * Display the font chooser dialog
	 */
	private void ChooseFont() {
		Font res = FontChooser.showDialog("", DefaultFont);
		if (res!=null)
			DefaultFont = res;
	}
}
