package course_generator;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import course_generator.settings.CgSettings;
import course_generator.utils.CgSpinner;
import course_generator.utils.Utils;

public class frmEditPosition  extends javax.swing.JDialog {

	private ResourceBundle bundle;
	private boolean ok;
	private CgSettings settings;
	private JPanel jPanelButtons;
	private JButton btCancel;
	private JButton btOk;
	private TrackData track;
	private CgData data;
	private int line;
	private int col;
	private Color ColorValNotEditable = new java.awt.Color(255, 253, 240);
	private Color ColorValEditable = new java.awt.Color(255, 255, 255);
	private Color ColorLabel = new java.awt.Color(200, 200, 200);
//	private SpinnerModel model;
	private JLabel lbLine;
	private JLabel lbLineVal;
	private JLabel lbLatitude;
	private JLabel lbLatitudeVal;
	private JLabel lbLongitude;
	private JLabel lbLongitudeVal;
	private JLabel lbDistanceVal;
	private JLabel lbDistance;
	private JLabel lbTotal;
	private JLabel lbTotalVal;
	private JLabel lbTime;
	private JLabel lbTimeVal;
	private JLabel lbHour;
	private JLabel lbHourVal;
	private JLabel lbName;
	private JTextField tfName;
	private JLabel lbElevation;
	private JSpinner spinElevation;
	private JLabel lbDiff;
	private CgSpinner spinDiff;
	private CgSpinner spinCoeff;
	private JLabel lbCoeff;
	private JLabel lbRecovery;
	private CgSpinner spinRecovery;
	private JLabel lbComment;
	private JTextField tfComment;
	private JCheckBox chkHighPoint;
	private JCheckBox chkLowPoint;
	private JLabel lbHighPoint;
	private JLabel lbLowPoint;
	private JLabel lbEat;
	private JCheckBox chkEat;
	private JLabel lbDrink;
	private JCheckBox chkDrink;
	private JLabel lbMark;
	private JCheckBox chkMark;
	private JLabel lbPhoto;
	private JCheckBox chkPhoto;
	private JLabel lbNote;
	private JCheckBox chkNote;
	private JLabel lbInfo;
	private JCheckBox chkInfo;
	private JLabel lbRoadbook;
	private JCheckBox chkRoadbook;
	private JLabel lbTimelimit;
	private JTextField tfTimelimit;
	private JLabel lbStation;
	private JTextField tfStation;

	/**
	 * Creates new form frmSettings
	 */
	public frmEditPosition() {
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		initComponents();
		setModal(true);
	}

	public boolean showDialog(CgSettings _settings, TrackData _track, int _line, int _col) {
		settings = _settings;
		track = _track;
		line = _line;
		col = _col;
		data = track.data.get(line);
		// Set field

		// End set field
		ok = false;

		//-- Update the display
		Refresh();
		//-- Show the dialog
		setVisible(true);

		if (ok) {
			// Copy fields

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

	
	private void initComponents() {
		int line = 0;
		// jPanelMainWindowsColor = new javax.swing.JPanel();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(bundle.getString("frmEditPosition.title"));
		setAlwaysOnTop(true);
		setResizable(false);
		setMinimumSize(new Dimension(500,400));
		setType(java.awt.Window.Type.UTILITY);

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new GridBagLayout());
		
		//-- Line
		lbLine = new javax.swing.JLabel();
		lbLine.setBackground(ColorLabel);
		lbLine.setOpaque(true);
		lbLine.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		lbLine.setText(" "+"Line"+" "); //bundle.getString("frmSettings.lbUnit.text"));
		Utils.addComponent(paneGlobal, lbLine, 
				0, line, 
				1, 1, 
				0, 0, 
				10, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		lbLineVal = new javax.swing.JLabel();
		lbLineVal.setHorizontalAlignment(SwingConstants.CENTER);
		lbLineVal.setBackground(ColorValNotEditable);
		lbLineVal.setOpaque(true);
		lbLineVal.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		Utils.addComponent(paneGlobal, lbLineVal, 
				1, line++, 
				1, 1, 
				1, 0, 
				10, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		
		//======================================================================
		//-- Latitude
		lbLatitude = new javax.swing.JLabel();
		lbLatitude.setBackground(ColorLabel);
		lbLatitude.setOpaque(true);
		lbLatitude.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		lbLatitude.setText(" "+"Latitude"+" "); //bundle.getString("frmSettings.lbUnit.text"));
		Utils.addComponent(paneGlobal, lbLatitude, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		lbLatitudeVal = new javax.swing.JLabel();
		lbLatitudeVal.setHorizontalAlignment(SwingConstants.CENTER);
		lbLatitudeVal.setBackground(ColorValNotEditable);
		lbLatitudeVal.setOpaque(true);
		lbLatitudeVal.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		Utils.addComponent(paneGlobal, lbLatitudeVal, 
				1, line, 
				1, 1, 
				0, 0, 
				5, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//-- Longitude
		lbLongitude = new javax.swing.JLabel();
		lbLongitude.setBackground(ColorLabel);
		lbLongitude.setOpaque(true);
		lbLongitude.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		lbLongitude.setText(" "+"Longitude"+" "); //bundle.getString("frmSettings.lbUnit.text"));
		Utils.addComponent(paneGlobal, lbLongitude, 
				2, line, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		lbLongitudeVal = new javax.swing.JLabel();
		lbLongitudeVal.setHorizontalAlignment(SwingConstants.CENTER);
		lbLongitudeVal.setBackground(ColorValNotEditable);
		lbLongitudeVal.setOpaque(true);
		lbLongitudeVal.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));		
		Utils.addComponent(paneGlobal, lbLongitudeVal, 
				3, line++, 
				1, 1, 
				1, 0, 
				5, 0, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//======================================================================
		//-- Distance
		lbDistance = new javax.swing.JLabel();
		lbDistance.setBackground(ColorLabel);
		lbDistance.setOpaque(true);
		lbDistance.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		lbDistance.setText(" "+"Distance"+" "); //bundle.getString("frmSettings.lbUnit.text"));
		Utils.addComponent(paneGlobal, lbDistance, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		lbDistanceVal = new javax.swing.JLabel();
		lbDistanceVal.setHorizontalAlignment(SwingConstants.CENTER);
		lbDistanceVal.setBackground(ColorValNotEditable);
		lbDistanceVal.setOpaque(true);
		lbDistanceVal.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));		
		Utils.addComponent(paneGlobal, lbDistanceVal, 
				1, line, 
				1, 1, 
				0, 0, 
				5, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//-- Total
		lbTotal = new javax.swing.JLabel();
		lbTotal.setBackground(ColorLabel);
		lbTotal.setOpaque(true);
		lbTotal.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		lbTotal.setText(" "+"Total distance"+" "); //bundle.getString("frmSettings.lbUnit.text"));
		Utils.addComponent(paneGlobal, lbTotal, 
				2, line, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		lbTotalVal = new javax.swing.JLabel();
		lbTotalVal.setHorizontalAlignment(SwingConstants.CENTER);
		lbTotalVal.setBackground(ColorValNotEditable);
		lbTotalVal.setOpaque(true);
		lbTotalVal.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));		
		Utils.addComponent(paneGlobal, lbTotalVal, 
				3, line++, 
				1, 1, 
				1, 0, 
				5, 0, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//======================================================================
		//-- Time
		lbTime = new javax.swing.JLabel();
		lbTime.setBackground(ColorLabel);
		lbTime.setOpaque(true);
		lbTime.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		lbTime.setText(" "+"Time"+" "); //bundle.getString("frmSettings.lbUnit.text"));
		Utils.addComponent(paneGlobal, lbTime, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		lbTimeVal = new javax.swing.JLabel();
		lbTimeVal.setHorizontalAlignment(SwingConstants.CENTER);
		lbTimeVal.setBackground(ColorValNotEditable);
		lbTimeVal.setOpaque(true);
		lbTimeVal.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));		
		Utils.addComponent(paneGlobal, lbTimeVal, 
				1, line, 
				1, 1, 
				0, 0, 
				5, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//-- Hour
		lbHour = new javax.swing.JLabel();
		lbHour.setBackground(ColorLabel);
		lbHour.setOpaque(true);
		lbHour.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		lbHour.setText(" "+"Hour"+" "); //bundle.getString("frmSettings.lbUnit.text"));
		Utils.addComponent(paneGlobal, lbHour, 
				2, line, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		lbHourVal = new javax.swing.JLabel();
		lbHourVal.setHorizontalAlignment(SwingConstants.CENTER);
		lbHourVal.setBackground(ColorValNotEditable);
		lbHourVal.setOpaque(true);
		lbHourVal.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));		
		Utils.addComponent(paneGlobal, lbHourVal, 
				3, line++, 
				1, 1, 
				1, 0, 
				5, 0, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		//-- Name
		lbName = new javax.swing.JLabel();
		lbName.setBackground(ColorLabel);
		lbName.setOpaque(true);
		lbName.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		lbName.setText(" "+"Name"+" "); //bundle.getString("frmSettings.lbUnit.text"));
		Utils.addComponent(paneGlobal, lbName, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		tfName = new javax.swing.JTextField(16);
		Utils.addComponent(paneGlobal, tfName, 
				1, line++, 
				GridBagConstraints.REMAINDER, 1, 
				1, 0, 
				5, 0, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//-- Elevation
		lbElevation = new javax.swing.JLabel();
		lbElevation.setBackground(ColorLabel);
		lbElevation.setOpaque(true);
		lbElevation.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		lbElevation.setText(" "+"Elevation"+" "); //bundle.getString("frmSettings.lbUnit.text"));
		Utils.addComponent(paneGlobal, lbElevation, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		
		spinElevation = new CgSpinner(0,0,100000,1);
		Utils.addComponent(paneGlobal, spinElevation, 
				1, line++, 
				1, 1, 
				1, 0, 
				5, 0, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//-- Tag : Mark
		lbMark = new javax.swing.JLabel(
			"Mark position",	
			new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/flag.png")),
			JLabel.LEFT);
		Utils.addComponent(paneGlobal, lbMark, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		chkMark = new javax.swing.JCheckBox();
		Utils.addComponent(paneGlobal, chkMark, 
				1, line, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//-- Tag : Place to see
		lbPhoto = new javax.swing.JLabel(
				"Place to see",	
				new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/photo.png")),
				JLabel.LEFT);
		Utils.addComponent(paneGlobal, lbPhoto, 
				2, line, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		chkPhoto = new javax.swing.JCheckBox();
		Utils.addComponent(paneGlobal, chkPhoto, 
				3, line++, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		//-- Tag : High point
		lbHighPoint = new javax.swing.JLabel(
			"High point",	
			new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/high_point.png")),
			JLabel.LEFT);
		Utils.addComponent(paneGlobal, lbHighPoint, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		chkHighPoint = new javax.swing.JCheckBox();
		Utils.addComponent(paneGlobal, chkHighPoint, 
				1, line, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//-- Tag : note
		lbNote = new javax.swing.JLabel(
			"Note",	
			new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/note.png")),
			JLabel.LEFT);
		Utils.addComponent(paneGlobal, lbNote, 
				2, line, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		chkNote = new javax.swing.JCheckBox();
		Utils.addComponent(paneGlobal, chkNote, 
				3, line++, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		//-- Tag : Low point
		lbLowPoint = new javax.swing.JLabel(
			"Low point",	
			new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/low_point.png")),
			JLabel.LEFT);
		Utils.addComponent(paneGlobal, lbLowPoint, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		chkLowPoint = new javax.swing.JCheckBox();
		Utils.addComponent(paneGlobal, chkLowPoint, 
				1, line, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//-- Tag : info
		lbInfo = new javax.swing.JLabel(
			"Information",	
			new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/info.png")),
			JLabel.LEFT);
		Utils.addComponent(paneGlobal, lbInfo, 
				2, line, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		chkInfo = new javax.swing.JCheckBox();
		Utils.addComponent(paneGlobal, chkInfo, 
				3, line++, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		//-- Tag : eat station
		lbEat = new javax.swing.JLabel(
			"Eat station",	
			new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/eat.png")),
			JLabel.LEFT);
		Utils.addComponent(paneGlobal, lbEat, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		chkEat = new javax.swing.JCheckBox();
		Utils.addComponent(paneGlobal, chkEat, 
				1, line, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		//-- Tag : Roadbook
		lbRoadbook = new javax.swing.JLabel(
			"Roadbook",	
			new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/roadbook.png")),
			JLabel.LEFT);
		Utils.addComponent(paneGlobal, lbRoadbook, 
				2, line, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		chkRoadbook = new javax.swing.JCheckBox();
		Utils.addComponent(paneGlobal, chkRoadbook, 
				3, line++, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		
		//-- Tag : drink station
		lbDrink = new javax.swing.JLabel(
			"Drink station",	
			new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/drink.png")),
			JLabel.LEFT);
		Utils.addComponent(paneGlobal, lbDrink, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		chkDrink = new javax.swing.JCheckBox();
		Utils.addComponent(paneGlobal, chkDrink, 
				1, line++, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		
		//-- Diff
		lbDiff = new javax.swing.JLabel();
		lbDiff.setBackground(ColorLabel);
		lbDiff.setOpaque(true);
		lbDiff.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		lbDiff.setText(" "+"Difficulty"+" "); //bundle.getString("frmSettings.lbUnit.text"));
		Utils.addComponent(paneGlobal, lbDiff, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		
		spinDiff = new CgSpinner(100,0,100,1);
		Utils.addComponent(paneGlobal, spinDiff, 
				1, line++, 
				1, 1, 
				1, 0, 
				5, 0, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//-- Coeff
		lbCoeff = new javax.swing.JLabel();
		lbCoeff.setBackground(ColorLabel);
		lbCoeff.setOpaque(true);
		lbCoeff.setText(" "+"Health coefficient"+" "); //bundle.getString("frmSettings.lbUnit.text"));
		Utils.addComponent(paneGlobal, lbCoeff, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		
		spinCoeff = new CgSpinner(100,0,200,1);
		Utils.addComponent(paneGlobal, spinCoeff, 
				1, line++, 
				1, 1, 
				1, 0, 
				5, 0, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		//-- Recup
		lbRecovery = new javax.swing.JLabel();
		lbRecovery.setBackground(ColorLabel);
		lbRecovery.setOpaque(true);
		lbRecovery.setText(" "+"Recovery"+" "); //bundle.getString("frmSettings.lbUnit.text"));
		Utils.addComponent(paneGlobal, lbRecovery, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		
		spinRecovery = new CgSpinner(0,0,100,1);
		Utils.addComponent(paneGlobal, spinRecovery, 
				1, line++, 
				1, 1, 
				1, 0, 
				5, 0, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		//-- Time limit
		lbTimelimit = new javax.swing.JLabel();
		lbTimelimit.setBackground(ColorLabel);
		lbTimelimit.setOpaque(true);
		lbTimelimit.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		lbTimelimit.setText(" "+"Time limit"+" "); //bundle.getString("frmSettings.lbUnit.text"));
		Utils.addComponent(paneGlobal, lbTimelimit, 
				0, line, 
				1, 1, 
				0, 1, 
				5, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		tfTimelimit = new javax.swing.JTextField();
		Utils.addComponent(paneGlobal, tfTimelimit, 
				1, line++, 
				GridBagConstraints.REMAINDER, 1, 
				1, 1, 
				5, 0, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//-- Station
		lbStation = new javax.swing.JLabel();
		lbStation.setBackground(ColorLabel);
		lbStation.setOpaque(true);
		lbStation.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		lbStation.setText(" "+"Station time"+" "); //bundle.getString("frmSettings.lbUnit.text"));
		Utils.addComponent(paneGlobal, lbStation, 
				0, line, 
				1, 1, 
				0, 1, 
				5, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		tfStation = new javax.swing.JTextField();
		Utils.addComponent(paneGlobal, tfStation, 
				1, line++, 
				GridBagConstraints.REMAINDER, 1, 
				1, 1, 
				5, 0, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//-- Comment
		lbComment = new javax.swing.JLabel();
		lbComment.setBackground(ColorLabel);
		lbComment.setOpaque(true);
		lbComment.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		lbComment.setText(" "+"Comment"+" "); //bundle.getString("frmSettings.lbUnit.text"));
		Utils.addComponent(paneGlobal, lbComment, 
				0, line, 
				1, 1, 
				0, 1, 
				5, 10, 10, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		tfComment = new javax.swing.JTextField();
		Utils.addComponent(paneGlobal, tfComment, 
				1, line++, 
				GridBagConstraints.REMAINDER, 1, 
				1, 1, 
				5, 0, 10, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		
		// == BUTTONS
		// ===========================================================
		jPanelButtons = new javax.swing.JPanel();
		jPanelButtons.setLayout(new FlowLayout());
		Utils.addComponent(paneGlobal, jPanelButtons, 
				0, line, 
				GridBagConstraints.REMAINDER, 1, 
				0, 0, 
				0, 0, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);

		btCancel = new javax.swing.JButton();
		btCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/cancel.png")));
		btCancel.setText(bundle.getString("frmEditPosition.btCancel.text"));
		btCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setVisible(false);
			}
		});

		btOk = new javax.swing.JButton();
		btOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/valid.png")));
		btOk.setText(bundle.getString("frmEditPosition.btOk.text"));
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

	private void Refresh() {
		lbLineVal.setText(String.format("%1.0f", data.getNum()));
        lbLatitudeVal.setText(String.format("%1.7f ",data.getLatitude()));
        lbLongitudeVal.setText(String.format("%1.7f ",data.getLongitude()));
        lbDistanceVal.setText(data.getDistString(settings.Unit,true));
        lbTotalVal.setText(data.getTotalString(settings.Unit,true));
        lbTimeVal.setText(data.getTimeString());        
        lbHourVal.setText(data.getHourString());
        spinElevation.setValue(data.getElevation(settings.Unit));
	}
}
