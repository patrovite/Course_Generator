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

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import course_generator.dialogs.FontChooser;
import course_generator.dialogs.FrmColorChooser;
import course_generator.utils.CgConst;
import course_generator.utils.CgSpinner;
import course_generator.utils.Utils;

/**
 *
 * @author pierre.delore
 */
public class frmSettings extends javax.swing.JDialog {
	private static final long serialVersionUID = -6225811216874887041L;
	private boolean ok;
	private CgSettings settings;
	private java.util.ResourceBundle bundle;
	private JLabel lbLanguage;
	private JComboBox<Object> cbLanguage;
	private JPanel jPanelButtons;
	private JButton btCancel;
	private JButton btOk;
	private JLabel lbUnit;
	private JComboBox<Object> cbUnit;
	private JLabel lbSpeed;
	private JComboBox<Object> cbSpeed;
	private JLabel lbCheck;
	private JCheckBox chkCheck;
	private JLabel lbThresholdAsk;
	private CgSpinner spinThresholdAsk;
	private JLabel lbDefaultFont;
	private JButton btDefaultFont;
	private Font DefaultFont;
	private JTabbedPane TabbedPaneGlobal;
	private JPanel panelGeneral;
	private JPanel panelDisplay;
	private JPanel panelMaps;
	private JLabel lbStatusBarIconSize;
	private JComboBox<Object> cbStatusBarIconSize;
	private JLabel lbTabIconSize;
	private JComboBox<Object> cbTabIconSize;
	private JLabel lbToolbarIconSize;
	private JComboBox<Object> cbToolbarIconSize;
	private JLabel lbMapToolbarIconSize;
	private JComboBox<Object> cbMapToolbarIconSize;
	private JLabel lbMenuIconSize;
	private JComboBox<Object> cbMenuIconSize;
	private JLabel lbTagIconSize;
	private JComboBox<Object> cbTagIconSize;
	private JLabel lbDialogIconSize;
	private JComboBox<Object> cbDialogIconSize;
	private JLabel lbMapIconSize;
	private JComboBox<Object> cbMapIconSize;
	private JLabel lbCurveButtonsIconSize;
	private JComboBox<Object> cbCurveButtonsIconSize;
	private JTextField edThunderForestApiKey;
	private JTextArea DarkSkyApiKey;

	private int fontSize[] = { 16, 20, 22, 24, 32, 64, 96, 128 };
	private JLabel lbThunderForestApiKey;
	private JPanel panelColors;
	private JLabel lbVeryEasyColor;
	private JLabel lbVeryEasyColorView;
	private JButton btVeryEasyColor;

	private Color ColorVeryEasy;
	private Color ColorEasy;
	private Color ColorAverage;
	private Color ColorHard;
	private Color ColorVeryHard;
	private Color ColorNight;

	private JButton btDefaultColor;
	private JLabel lbColorsEmpty;
	private JLabel lbEasyColorView;
	private JButton btEasyColor;
	private JLabel lbEasyColor;
	private JLabel lbAverageColor;
	private JLabel lbAverageColorView;
	private JButton btAverageColor;
	private JLabel lbHardColor;
	private JLabel lbHardColorView;
	private JButton btHardColor;
	private JLabel lbVeryHardColor;
	private JLabel lbVeryHardColorView;
	private JButton btVeryHardColor;
	private JLabel lbNormalTrackWidth;
	private CgSpinner spinNormalTrackWidth;
	private JLabel lbNightTrackWidth;
	private CgSpinner spinNightTrackWidth;
	private JLabel lbNormalTrackTransparency;
	private CgSpinner spinNormalTrackTransparency;
	private JLabel lbNightTrackTransparency;
	private CgSpinner spinNightTrackTransparency;
	private JLabel lbNightColor;
	private JLabel lbNightColorView;
	private JButton btNightColor;
	private JLabel lbDisplayEmpty;


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
		else if (settings.Language.equalsIgnoreCase("ES"))
			cbLanguage.setSelectedIndex(3);
		

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
		spinThresholdAsk.setValue((int) settings.PosFilterAskThreshold);

		// -- Default font
		DefaultFont = new Font(settings.DefaultFontName, settings.DefaultFontStyle, settings.DefaultFontSize);

		// -- Icon size
		cbStatusBarIconSize.setSelectedIndex(FontSize2Index(settings.StatusbarIconSize));
		cbTabIconSize.setSelectedIndex(FontSize2Index(settings.TabIconSize));
		cbToolbarIconSize.setSelectedIndex(FontSize2Index(settings.ToolbarIconSize));
		cbMapToolbarIconSize.setSelectedIndex(FontSize2Index(settings.MapToolbarIconSize));
		cbMenuIconSize.setSelectedIndex(FontSize2Index(settings.MenuIconSize));
		cbTagIconSize.setSelectedIndex(FontSize2Index(settings.TagIconSize));
		cbDialogIconSize.setSelectedIndex(FontSize2Index(settings.DialogIconSize));
		cbMapIconSize.setSelectedIndex(FontSize2Index(settings.MapIconSize));
		cbCurveButtonsIconSize.setSelectedIndex(FontSize2Index(settings.CurveButtonsIconSize));

		// Maps & Weather tab
		edThunderForestApiKey.setText(settings.getThunderForestApiKey());
		DarkSkyApiKey.setText(settings.getDarkSkyApiKey());

		// -- Colors
		ColorVeryEasy = settings.Color_Diff_VeryEasy;
		ColorEasy = settings.Color_Diff_Easy;
		ColorAverage = settings.Color_Diff_Average;
		ColorHard = settings.Color_Diff_Hard;
		ColorVeryHard = settings.Color_Diff_VeryHard;
		ColorNight = settings.Color_Map_NightHighlight;

		// -- Track width
		spinNormalTrackWidth.setValue((int) settings.NormalTrackWidth);
		spinNightTrackWidth.setValue((int) settings.NightTrackWidth);
		spinNormalTrackTransparency.setValue((int) (settings.NormalTrackTransparency * 100 / 255));
		spinNightTrackTransparency.setValue((int) (settings.NightTrackTransparency * 100 / 255));

		// --
		Refresh();

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
			case 3: // Spanish
				settings.Language = "ES";
				break;				
			default: // Default
				settings.Language = "";
			}

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

			// -- Default font
			settings.DefaultFontName = DefaultFont.getFontName();
			settings.DefaultFontStyle = DefaultFont.getStyle();
			settings.DefaultFontSize = DefaultFont.getSize();

			// -- Icons size

			int OldStatusbarIconSize = settings.StatusbarIconSize;
			int OldTabIconSize = settings.TabIconSize;
			int OldToolbarIconSize = settings.ToolbarIconSize;
			int OldMapToolbarIconSize = settings.MapToolbarIconSize;
			int OldMenuIconSize = settings.MenuIconSize;
			int OldTagIconSize = settings.TagIconSize;
			int OldDialogIconSize = settings.DialogIconSize;
			int OldMapIconSize = settings.MapIconSize;
			int OldCurveButtonsIconSize = settings.CurveButtonsIconSize;

			settings.StatusbarIconSize = Index2FontSize(cbStatusBarIconSize.getSelectedIndex());
			settings.TabIconSize = Index2FontSize(cbTabIconSize.getSelectedIndex());
			settings.ToolbarIconSize = Index2FontSize(cbToolbarIconSize.getSelectedIndex());
			settings.MapToolbarIconSize = Index2FontSize(cbMapToolbarIconSize.getSelectedIndex());
			settings.MenuIconSize = Index2FontSize(cbMenuIconSize.getSelectedIndex());
			settings.TagIconSize = Index2FontSize(cbTagIconSize.getSelectedIndex());
			settings.DialogIconSize = Index2FontSize(cbDialogIconSize.getSelectedIndex());
			settings.MapIconSize = Index2FontSize(cbMapIconSize.getSelectedIndex());
			settings.CurveButtonsIconSize = Index2FontSize(cbCurveButtonsIconSize.getSelectedIndex());

			// -- Maps
			settings.setThunderForestApiKey(edThunderForestApiKey.getText());

			// -- Colors
			settings.Color_Diff_VeryEasy = ColorVeryEasy;
			settings.Color_Diff_Easy = ColorEasy;
			settings.Color_Diff_Average = ColorAverage;
			settings.Color_Diff_Hard = ColorHard;
			settings.Color_Diff_VeryHard = ColorVeryHard;
			settings.Color_Map_NightHighlight = ColorNight;

			// -- Track width
			settings.NormalTrackWidth = spinNormalTrackWidth.getValueAsInt();
			settings.NightTrackWidth = spinNightTrackWidth.getValueAsInt();
			settings.NormalTrackTransparency = spinNormalTrackTransparency.getValueAsInt() * 255 / 100;
			settings.NightTrackTransparency = spinNightTrackTransparency.getValueAsInt() * 255 / 100;

			// -- Restart of the application needed?
			if ((!old_language.equalsIgnoreCase(settings.Language))
					|| (OldStatusbarIconSize != settings.StatusbarIconSize) || (OldTabIconSize != settings.TabIconSize)
					|| (OldToolbarIconSize != settings.ToolbarIconSize)
					|| (OldMapToolbarIconSize != settings.MapToolbarIconSize)
					|| (OldMenuIconSize != settings.MenuIconSize) || (OldTagIconSize != settings.TagIconSize)
					|| (OldDialogIconSize != settings.DialogIconSize) || (OldMapIconSize != settings.MapIconSize)
					|| (OldCurveButtonsIconSize != settings.CurveButtonsIconSize))
				JOptionPane.showMessageDialog(this, bundle.getString("frmSettings.MsgRestart"));

			// Maps & Weather
			if (edThunderForestApiKey.getText() != ""
					&& edThunderForestApiKey.getText() != settings.getThunderForestApiKey()) {
				settings.setThunderForestApiKey(edThunderForestApiKey.getText());
			}

			if (DarkSkyApiKey.getText() != "" && DarkSkyApiKey.getText() != settings.getDarkSkyApiKey()) {
				settings.setDarkSkyApiKey(DarkSkyApiKey.getText());
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


	/**
	 * This method is called to initialize the form.
	 */
	private void initComponents() {

		int line = 0;
		String fontSizeStr[] = { "16 px", "20 px", "22 px", "24 px", "32 px", "64 px", "96 px", "128 px" };

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(bundle.getString("frmSettings.title"));
		setAlwaysOnTop(true);
		setType(java.awt.Window.Type.UTILITY);

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new GridBagLayout());

		// -- Tabbed panel
		// ------------------------------------------------------
		TabbedPaneGlobal = new javax.swing.JTabbedPane();
		paneGlobal.add(TabbedPaneGlobal);
		Utils.addComponent(paneGlobal, TabbedPaneGlobal, 0, 0, GridBagConstraints.REMAINDER, 1, 0, 1, 10, 0, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);

		// ## Tab "General" ##
		panelGeneral = new JPanel();
		panelGeneral.setLayout(new GridBagLayout());

		line = 0;

		// -- LANGUAGE - String
		lbLanguage = new javax.swing.JLabel();
		lbLanguage.setText(bundle.getString("frmSettings.lbLanguage.text"));
		Utils.addComponent(panelGeneral, lbLanguage, 0, line, 1, 1, 1, 0, 10, 10, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		cbLanguage = new javax.swing.JComboBox<>();
		String language[] = { bundle.getString("frmSettings.LanguageDefault"),
				bundle.getString("frmSettings.LanguageEN"), bundle.getString("frmSettings.LanguageFR"), 
				bundle.getString("frmSettings.LanguageES") };
		cbLanguage.setModel(new javax.swing.DefaultComboBoxModel<>(language));
		Utils.addComponent(panelGeneral, cbLanguage, 1, line++, 1, 1, 0, 0, 10, 5, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		line++;

		// -- UNIT - int - Unit
		lbUnit = new javax.swing.JLabel();
		lbUnit.setText(bundle.getString("frmSettings.lbUnit.text"));
		Utils.addComponent(panelGeneral, lbUnit, 0, line, 1, 1, 1, 0, 2, 10, 0, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		cbUnit = new javax.swing.JComboBox<>();
		String units[] = { bundle.getString("frmSettings.Units.KmM"), bundle.getString("frmSettings.Units.MilesFeet") };
		cbUnit.setModel(new javax.swing.DefaultComboBoxModel<>(units));
		Utils.addComponent(panelGeneral, cbUnit, 1, line++, 1, 1, 0, 0, 2, 5, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Speed/Pace
		lbSpeed = new javax.swing.JLabel();
		lbSpeed.setText(bundle.getString("frmSettings.lbSpeed.text"));
		Utils.addComponent(panelGeneral, lbSpeed, 0, line, 1, 1, 1, 0, 2, 10, 0, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		cbSpeed = new javax.swing.JComboBox<>();
		String speeddisplay[] = { bundle.getString("frmSettings.SpeedDisplay.Speed"),
				bundle.getString("frmSettings.SpeedDisplay.Pace") };
		cbSpeed.setModel(new javax.swing.DefaultComboBoxModel<>(speeddisplay));
		Utils.addComponent(panelGeneral, cbSpeed, 1, line++, 1, 1, 0, 0, 2, 5, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Minimum Threshold for asking if PosFilterAskThreshold
		lbThresholdAsk = new javax.swing.JLabel();
		lbThresholdAsk.setText(bundle.getString("frmSettings.lbThresholdAsk.Text"));// "Elevation"
		Utils.addComponent(panelGeneral, lbThresholdAsk, 0, line, 1, 1, 1, 0, 2, 10, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		spinThresholdAsk = new CgSpinner(0, 0, 100, 1);
		Utils.addComponent(panelGeneral, spinThresholdAsk, 1, line++, 1, 1, 0, 0, 2, 5, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Check for update
		lbCheck = new javax.swing.JLabel();
		lbCheck.setText(bundle.getString("frmSettings.lbCheck.text"));
		Utils.addComponent(panelGeneral, lbCheck, 0, line, 1, 1, 1, 1, 7, 10, 0, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		chkCheck = new javax.swing.JCheckBox();
		Utils.addComponent(panelGeneral, chkCheck, 1, line++, 1, 1, 0, 1, 7, 5, 0, 10, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL);

		addTab(TabbedPaneGlobal, panelGeneral, bundle.getString("frmSettings.TabGeneral.tabTitle"), null);

		// ## Tab "Display" ##
		panelDisplay = new JPanel();
		panelDisplay.setLayout(new GridBagLayout());

		line = 0;

		// -- Default font
		lbDefaultFont = new javax.swing.JLabel();
		lbDefaultFont.setText(bundle.getString("frmSettings.lbDefaultFont.text"));
		Utils.addComponent(panelDisplay, lbDefaultFont, 0, line, 1, 1, 1, 0, 7, 10, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		btDefaultFont = new javax.swing.JButton("...");
		btDefaultFont.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// spinDiff.setValue(100);
				ChooseFont();
			}
		});
		Utils.addComponent(panelDisplay, btDefaultFont, 1, line++, 1, 1, 0, 0, 7, 5, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Statusbar icon size
		lbStatusBarIconSize = new javax.swing.JLabel();
		lbStatusBarIconSize.setText(bundle.getString("frmSettings.lbStatusbarIconSize.text"));
		Utils.addComponent(panelDisplay, lbStatusBarIconSize, 0, line, 1, 1, 1, 0, 2, 10, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		cbStatusBarIconSize = new javax.swing.JComboBox<>();
		cbStatusBarIconSize.setModel(new javax.swing.DefaultComboBoxModel<>(fontSizeStr));
		Utils.addComponent(panelDisplay, cbStatusBarIconSize, 1, line++, 1, 1, 0, 0, 2, 5, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Tab icon size
		lbTabIconSize = new javax.swing.JLabel();
		lbTabIconSize.setText(bundle.getString("frmSettings.lbTabIconSize.text"));
		Utils.addComponent(panelDisplay, lbTabIconSize, 0, line, 1, 1, 1, 0, 2, 10, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		cbTabIconSize = new javax.swing.JComboBox<>();
		cbTabIconSize.setModel(new javax.swing.DefaultComboBoxModel<>(fontSizeStr));
		Utils.addComponent(panelDisplay, cbTabIconSize, 1, line++, 1, 1, 0, 0, 2, 5, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Toolbar icon size
		lbToolbarIconSize = new javax.swing.JLabel();
		lbToolbarIconSize.setText(bundle.getString("frmSettings.lbToolbarIconSize.text"));
		Utils.addComponent(panelDisplay, lbToolbarIconSize, 0, line, 1, 1, 1, 0, 2, 10, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		cbToolbarIconSize = new javax.swing.JComboBox<>();
		cbToolbarIconSize.setModel(new javax.swing.DefaultComboBoxModel<>(fontSizeStr));
		Utils.addComponent(panelDisplay, cbToolbarIconSize, 1, line++, 1, 1, 0, 0, 2, 5, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Map Toolbar icon size
		lbMapToolbarIconSize = new javax.swing.JLabel();
		lbMapToolbarIconSize.setText(bundle.getString("frmSettings.MapToolbarIconSize.text"));
		Utils.addComponent(panelDisplay, lbMapToolbarIconSize, 0, line, 1, 1, 1, 0, 2, 10, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		cbMapToolbarIconSize = new javax.swing.JComboBox<>();
		cbMapToolbarIconSize.setModel(new javax.swing.DefaultComboBoxModel<>(fontSizeStr));
		Utils.addComponent(panelDisplay, cbMapToolbarIconSize, 1, line++, 1, 1, 0, 0, 2, 5, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Menu icon size
		lbMenuIconSize = new javax.swing.JLabel();
		lbMenuIconSize.setText(bundle.getString("frmSettings.lbMenuIconSize.text"));
		Utils.addComponent(panelDisplay, lbMenuIconSize, 0, line, 1, 1, 1, 0, 2, 10, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		cbMenuIconSize = new javax.swing.JComboBox<>();
		cbMenuIconSize.setModel(new javax.swing.DefaultComboBoxModel<>(fontSizeStr));
		Utils.addComponent(panelDisplay, cbMenuIconSize, 1, line++, 1, 1, 0, 0, 2, 5, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Tag icon size
		lbTagIconSize = new javax.swing.JLabel();
		lbTagIconSize.setText(bundle.getString("frmSettings.lbTagIconSize.text"));
		Utils.addComponent(panelDisplay, lbTagIconSize, 0, line, 1, 1, 1, 0, 2, 10, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		cbTagIconSize = new javax.swing.JComboBox<>();
		cbTagIconSize.setModel(new javax.swing.DefaultComboBoxModel<>(fontSizeStr));
		Utils.addComponent(panelDisplay, cbTagIconSize, 1, line++, 1, 1, 0, 0, 2, 5, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Dialog icon size
		lbDialogIconSize = new javax.swing.JLabel();
		lbDialogIconSize.setText(bundle.getString("frmSettings.lbDialogIconSize.text"));
		Utils.addComponent(panelDisplay, lbDialogIconSize, 0, line, 1, 1, 1, 0, 2, 10, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		cbDialogIconSize = new javax.swing.JComboBox<>();
		cbDialogIconSize.setModel(new javax.swing.DefaultComboBoxModel<>(fontSizeStr));
		Utils.addComponent(panelDisplay, cbDialogIconSize, 1, line++, 1, 1, 0, 0, 2, 5, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Map icon size
		lbMapIconSize = new javax.swing.JLabel();
		lbMapIconSize.setText(bundle.getString("frmSettings.lbMapIconSize.text"));
		Utils.addComponent(panelDisplay, lbMapIconSize, 0, line, 1, 1, 1, 0, 2, 10, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		cbMapIconSize = new javax.swing.JComboBox<>();
		cbMapIconSize.setModel(new javax.swing.DefaultComboBoxModel<>(fontSizeStr));
		Utils.addComponent(panelDisplay, cbMapIconSize, 1, line++, 1, 1, 0, 0, 2, 5, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Curve buttons icon size
		lbCurveButtonsIconSize = new javax.swing.JLabel();
		lbCurveButtonsIconSize.setText(bundle.getString("frmSettings.lbCurveButtonIconSize.text"));
		Utils.addComponent(panelDisplay, lbCurveButtonsIconSize, 0, line, 1, 1, 1, 0, 2, 10, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		cbCurveButtonsIconSize = new javax.swing.JComboBox<>();
		cbCurveButtonsIconSize.setModel(new javax.swing.DefaultComboBoxModel<>(fontSizeStr));
		Utils.addComponent(panelDisplay, cbCurveButtonsIconSize, 1, line++, 1, 1, 0, 0, 2, 5, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Empty line for resize purpose (not the best solution but it's simple ;) )
		lbDisplayEmpty = new JLabel();
		Utils.addComponent(panelDisplay, lbDisplayEmpty, 0, line++, 1, 1, 0, 1, 10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		addTab(TabbedPaneGlobal, panelDisplay, bundle.getString("frmSettings.TabDisplay.tabTitle"), null);

		// ## Tab "Maps" ##
		panelMaps = new JPanel();
		panelMaps.setLayout(new GridBagLayout());

		line = 0;

		// Thunderforest API Key
		lbThunderForestApiKey = new javax.swing.JLabel();
		lbThunderForestApiKey.setText(bundle.getString("frmSettings.lbThunderForestApiKey.text"));
		Utils.addComponent(panelMaps, lbThunderForestApiKey, 0, line, 1, 1, 0, 1, 10, 10, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		edThunderForestApiKey = new javax.swing.JTextField();
		Utils.addComponent(panelMaps, edThunderForestApiKey, 1, line++, 1, 1, 1, 1, 10, 5, 0, 10,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);

		addTab(TabbedPaneGlobal, panelMaps, bundle.getString("frmSettings.TabMaps.tabTitle"), null);

		// Dark Sky API Key
		JLabel lbDarkSkyApiKey = new javax.swing.JLabel();
		lbDarkSkyApiKey.setText(bundle.getString("frmSettings.lbDarkSkyApiKey.text"));
		Utils.addComponent(panelMaps, lbDarkSkyApiKey, 1, line, 1, 1, 0, 0, 5, 0, 0, 0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL);

		DarkSkyApiKey = new JTextArea();
		Utils.addComponent(panelMaps, DarkSkyApiKey, 2, line, 1, 1, 5, 5, 5, 5, 5, 5, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL);

		// ## Tab "Color" ##
		panelColors = new JPanel();
		panelColors.setLayout(new GridBagLayout());

		line = 0;

		// -- VeryEasy Color
		lbVeryEasyColor = new JLabel(" " + bundle.getString("frmSettings.lbVeryEasyColor.text") + " ");
		Utils.addComponent(panelColors, lbVeryEasyColor, 0, line, 1, 1, 1, 0, 10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		lbVeryEasyColorView = new JLabel("          ");
		lbVeryEasyColorView.setBorder(BorderFactory.createLineBorder(Color.black));
		lbVeryEasyColorView.setOpaque(true);
		lbVeryEasyColorView.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColorVeryEasy = ChooseColor(ColorVeryEasy);
				Refresh();
			}
		});
		Utils.addComponent(panelColors, lbVeryEasyColorView, 2, line++, 1, 1, 0, 0, 10, 10, 0, 10,
				GridBagConstraints.WEST, GridBagConstraints.BOTH);

		// -- Easy Color
		lbEasyColor = new JLabel(" " + bundle.getString("frmSettings.lbEasyColor.text") + " ");
		Utils.addComponent(panelColors, lbEasyColor, 0, line, 1, 1, 1, 0, 10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		lbEasyColorView = new JLabel("          ");
		lbEasyColorView.setBorder(BorderFactory.createLineBorder(Color.black));
		lbEasyColorView.setOpaque(true);
		lbEasyColorView.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColorEasy = ChooseColor(ColorEasy);
				Refresh();
			}
		});
		Utils.addComponent(panelColors, lbEasyColorView, 2, line++, 1, 1, 0, 0, 10, 10, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		// -- Average Color
		lbAverageColor = new JLabel(" " + bundle.getString("frmSettings.lbAverageColor.text") + " ");
		Utils.addComponent(panelColors, lbAverageColor, 0, line, 1, 1, 1, 0, 10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		lbAverageColorView = new JLabel("          ");
		lbAverageColorView.setBorder(BorderFactory.createLineBorder(Color.black));
		lbAverageColorView.setOpaque(true);
		lbAverageColorView.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColorAverage = ChooseColor(ColorAverage);
				Refresh();
			}
		});
		Utils.addComponent(panelColors, lbAverageColorView, 2, line++, 1, 1, 0, 0, 10, 10, 0, 10,
				GridBagConstraints.WEST, GridBagConstraints.BOTH);

		// -- Hard Color
		lbHardColor = new JLabel(" " + bundle.getString("frmSettings.lbHardColor.text") + " ");
		Utils.addComponent(panelColors, lbHardColor, 0, line, 1, 1, 1, 0, 10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		lbHardColorView = new JLabel("          ");
		lbHardColorView.setBorder(BorderFactory.createLineBorder(Color.black));
		lbHardColorView.setOpaque(true);
		lbHardColorView.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColorHard = ChooseColor(ColorHard);
				Refresh();
			}
		});
		Utils.addComponent(panelColors, lbHardColorView, 2, line++, 1, 1, 0, 0, 10, 10, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		// -- Very Hard Color
		lbVeryHardColor = new JLabel(" " + bundle.getString("frmSettings.lbVeryHardColor.text") + " ");
		Utils.addComponent(panelColors, lbVeryHardColor, 0, line, 1, 1, 1, 0, 10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		lbVeryHardColorView = new JLabel("          ");
		lbVeryHardColorView.setBorder(BorderFactory.createLineBorder(Color.black));
		lbVeryHardColorView.setOpaque(true);
		lbVeryHardColorView.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColorVeryHard = ChooseColor(ColorVeryHard);
				Refresh();
			}
		});
		Utils.addComponent(panelColors, lbVeryHardColorView, 2, line++, 1, 1, 0, 0, 10, 10, 0, 10,
				GridBagConstraints.WEST, GridBagConstraints.BOTH);

		// -- NightHighLight Color
		lbNightColor = new JLabel(" " + bundle.getString("frmSettings.lbNightColor.text") + " ");
		Utils.addComponent(panelColors, lbNightColor, 0, line, 1, 1, 1, 0, 10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		lbNightColorView = new JLabel("          ");
		lbNightColorView.setBorder(BorderFactory.createLineBorder(Color.black));
		lbNightColorView.setOpaque(true);
		lbNightColorView.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColorNight = ChooseColor(ColorNight);
				Refresh();
			}
		});
		Utils.addComponent(panelColors, lbNightColorView, 2, line++, 1, 1, 0, 0, 10, 10, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		// -- Default colors
		btDefaultColor = new JButton(bundle.getString("frmSettings.btDefaultColors.text"));
		btDefaultColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ColorVeryEasy = CgConst.CL_DIFF_VERYEASY;
				ColorEasy = CgConst.CL_DIFF_EASY;
				ColorAverage = CgConst.CL_DIFF_AVERAGE;
				ColorHard = CgConst.CL_DIFF_HARD;
				ColorVeryHard = CgConst.CL_DIFF_VERYHARD;
				Refresh();
			}
		});
		Utils.addComponent(panelColors, btDefaultColor, 0, line++, GridBagConstraints.REMAINDER, 1, 0, 0, 10, 10, 0, 10,
				GridBagConstraints.EAST, GridBagConstraints.VERTICAL);

		// -- Width of track
		lbNormalTrackWidth = new javax.swing.JLabel();
		lbNormalTrackWidth.setText(bundle.getString("frmSettings.lbNormalTrackWidth.Text"));
		Utils.addComponent(panelColors, lbNormalTrackWidth, 0, line, 1, 1, 1, 0, 10, 10, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		spinNormalTrackWidth = new CgSpinner(1, 1, 100, 1);
		Utils.addComponent(panelColors, spinNormalTrackWidth, 2, line++, 1, 1, 0, 0, 10, 5, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Transparency of track
		lbNormalTrackTransparency = new javax.swing.JLabel();
		lbNormalTrackTransparency.setText(bundle.getString("frmSettings.lbNormalTrackTransparency.Text"));
		Utils.addComponent(panelColors, lbNormalTrackTransparency, 0, line, 1, 1, 1, 0, 10, 10, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		spinNormalTrackTransparency = new CgSpinner(100, 0, 100, 1);
		Utils.addComponent(panelColors, spinNormalTrackTransparency, 2, line++, 1, 1, 0, 0, 10, 5, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Width of night track
		lbNightTrackWidth = new javax.swing.JLabel();
		lbNightTrackWidth.setText(bundle.getString("frmSettings.lbNightTrackWidth.Text"));
		Utils.addComponent(panelColors, lbNightTrackWidth, 0, line, 1, 1, 1, 0, 10, 10, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		spinNightTrackWidth = new CgSpinner(1, 1, 100, 1);
		Utils.addComponent(panelColors, spinNightTrackWidth, 2, line++, 1, 1, 0, 0, 10, 5, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Transparency of night track
		lbNightTrackTransparency = new javax.swing.JLabel();
		lbNightTrackTransparency.setText(bundle.getString("frmSettings.lbNightTrackTransparency.Text"));
		Utils.addComponent(panelColors, lbNightTrackTransparency, 0, line, 1, 1, 1, 0, 10, 10, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		spinNightTrackTransparency = new CgSpinner(100, 0, 100, 1);
		Utils.addComponent(panelColors, spinNightTrackTransparency, 2, line++, 1, 1, 0, 0, 10, 5, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Empty line for resize purpose (not the best solution but it's simple ;) )
		lbColorsEmpty = new JLabel();
		Utils.addComponent(panelColors, lbColorsEmpty, 0, line++, 1, 1, 0, 1, 10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		addTab(TabbedPaneGlobal, panelColors, bundle.getString("frmSettings.TabColors.tabTitle"), null);

		// -- Separator
		// -- NOCONNECTIONONSTARTUP - Boolean -bNoConnectOnStartup
		// -- CONNECTIONTIMEOUT - int - ConnectionTimeout

		// == BUTTONS
		// ===========================================================
		jPanelButtons = new javax.swing.JPanel();
		jPanelButtons.setLayout(new FlowLayout());
		Utils.addComponent(paneGlobal, jPanelButtons, 0, 1, GridBagConstraints.REMAINDER, 1, 0, 0, 10, 0, 0, 0,
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
		Font res = FontChooser.showDialog(this, "", DefaultFont);
		if (res != null)
			DefaultFont = res;
	}


	/**
	 * Add a tab to JTabbedPane. The icon is at the left of the text and there some
	 * space between the icon and the label
	 * 
	 * @param tabbedPane
	 *            JTabbedPane where we want to add the tab
	 * @param tab
	 *            Tab to add
	 * @param title
	 *            Title of the tab
	 * @param icon
	 *            Icon of the tab
	 */
	private void addTab(JTabbedPane tabbedPane, Component tab, String title, Icon icon) {
		tabbedPane.add(tab);

		// Create bespoke component for rendering the tab.
		javax.swing.JLabel lbl = new javax.swing.JLabel(title);
		if (icon != null)
			lbl.setIcon(icon);

		// Add some spacing between text and icon, and position text to the RHS.
		lbl.setIconTextGap(5);
		lbl.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

		tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, lbl);
	}


	/**
	 * Return the index in the "fontSize" array of the value containing the font
	 * size
	 * 
	 * @param value
	 *            Font size
	 * @return Index in the "fontSize" array
	 */
	private int FontSize2Index(int value) {
		for (int i = 0; i < fontSize.length; i++) {
			if (fontSize[i] == value)
				return i;
		}
		return 0; // Default value if not found
	}


	/**
	 * Return the font size corresponding at an index
	 * 
	 * @param value
	 *            index
	 * @return Font size
	 */
	private int Index2FontSize(int value) {
		if (value >= fontSize.length)
			return 16; // Default value if not found

		return fontSize[value];
	}


	private Color ChooseColor(Color cl) {
		return FrmColorChooser.showDialog(this, "", cl, settings);
	}


	/**
	 * Refresh some dialog contents
	 */
	private void Refresh() {
		lbVeryEasyColorView.setBackground(ColorVeryEasy);
		lbEasyColorView.setBackground(ColorEasy);
		lbAverageColorView.setBackground(ColorAverage);
		lbHardColorView.setBackground(ColorHard);
		lbVeryHardColorView.setBackground(ColorVeryHard);
		lbNightColorView.setBackground(ColorNight);
	}

}
