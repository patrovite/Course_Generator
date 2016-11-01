package course_generator;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.font.TextAttribute;
import java.util.Collections;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
import course_generator.utils.CgConst;
import course_generator.utils.CgSpinner;
import course_generator.utils.JHourSetting;
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
	private CgSpinner spinElevation;
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
	private JLabel lbStation;
	private JHourSetting hsStation;
	private JHourSetting hsTimelimit;
	private JPanel panelLeft;
	private JPanel panelRight;
	private JLabel lbElevationHelp;
	private JLabel lbTags;
	private JLabel lbDiffHelp;
	private JLabel lbRecoveryHelp;

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

        //Higher point
		
		chkHighPoint.setSelected((data.getTag() & CgConst.TAG_HIGH_PT)!=0);
		chkLowPoint.setSelected((data.getTag() & CgConst.TAG_LOW_PT)!=0);
		chkEat.setSelected((data.getTag() & CgConst.TAG_EAT_PT)!=0);
		chkDrink.setSelected((data.getTag() & CgConst.TAG_WATER_PT)!=0);
		chkMark.setSelected((data.getTag() & CgConst.TAG_MARK)!=0);
		chkRoadbook.setSelected((data.getTag() & CgConst.TAG_ROADBOOK)!=0);
		chkPhoto.setSelected((data.getTag() & CgConst.TAG_COOL_PT)!=0);
		chkNote.setSelected((data.getTag() & CgConst.TAG_NOTE)!=0);
		chkInfo.setSelected((data.getTag() & CgConst.TAG_INFO)!=0);
		
		
		// End set field
		ok = false;

		//-- Update the display
		Refresh();
		
		//-- Show the dialog
		setVisible(true);

		if (ok) {
			// Copy fields
			track.data.get(line).setName(tfName.getText());
			int tag=0;
            //Higher point
			if (chkHighPoint.isSelected())
				tag=tag | CgConst.TAG_HIGH_PT;

			//Lower point
			if (chkLowPoint.isSelected())
				tag=tag | CgConst.TAG_LOW_PT;

            //Station
			if (chkEat.isSelected())
            	tag=tag | CgConst.TAG_EAT_PT;

            //Drink
			if (chkDrink.isSelected())
				tag=tag | CgConst.TAG_WATER_PT;
            
            //Mark
			if (chkMark.isSelected())
				tag=tag | CgConst.TAG_MARK;
            
            //Roadbook
			if (chkRoadbook.isSelected())
				tag=tag | CgConst.TAG_ROADBOOK;
            
            //Photo
			if (chkPhoto.isSelected())
				tag=tag | CgConst.TAG_COOL_PT;
            
            //Note
			if (chkNote.isSelected())
				tag=tag | CgConst.TAG_NOTE;

            //Info
			if (chkInfo.isSelected())
				tag=tag | CgConst.TAG_INFO;
			track.data.get(line).setTag(tag);
			
			track.data.get(line).setElevation(spinElevation.getValueAsDouble());
			track.data.get(line).setDiff(spinDiff.getValueAsDouble());
			track.data.get(line).setCoeff(spinCoeff.getValueAsDouble());
			track.data.get(line).setRecovery(spinRecovery.getValueAsDouble());

			track.data.get(line).setTimeLimit(hsTimelimit.getHMSinSecond());
			track.data.get(line).setStation(hsStation.getHMSinSecond());
			
			track.data.get(line).setComment(tfComment.getText());
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
		setMinimumSize(new Dimension(1000,400));
		setType(java.awt.Window.Type.UTILITY);

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new GridBagLayout());

		//== Left panel
		panelLeft = new JPanel();
		panelLeft.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		panelLeft.setBackground(Color.WHITE);
		panelLeft.setOpaque(true);
		panelLeft.setLayout(new GridBagLayout());
		Utils.addComponent(paneGlobal, panelLeft, 
				0, 0, 
				1, 1, 
				0, 1, 
				10, 10, 20, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		
		//-- Line
		line=0;
		
		lbLine = new javax.swing.JLabel();

		Font font = lbLine.getFont();
		font = font.deriveFont(
		    Collections.singletonMap(
		        TextAttribute.WEIGHT, TextAttribute.WEIGHT_EXTRABOLD));
		
		lbLine.setFont(font);
		lbLine.setText(bundle.getString("frmEditPosition.lbLine.Text"));//"Line"
		
		Utils.addComponent(panelLeft, lbLine, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		lbLineVal = new javax.swing.JLabel();
		Utils.addComponent(panelLeft, lbLineVal, 
				1, line++, 
				1, 1, 
				1, 0, 
				5, 0, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//======================================================================
		//-- Latitude
		lbLatitude = new javax.swing.JLabel();
		lbLatitude.setFont(font);
		lbLatitude.setText(bundle.getString("frmEditPosition.lbLatitude.Text"));//"Latitude"
		Utils.addComponent(panelLeft, lbLatitude, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		lbLatitudeVal = new javax.swing.JLabel();
		Utils.addComponent(panelLeft, lbLatitudeVal, 
				1, line++, 
				1, 1, 
				0, 0, 
				5, 0, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//-- Longitude
		lbLongitude = new javax.swing.JLabel();
		lbLongitude.setFont(font);
		lbLongitude.setText(bundle.getString("frmEditPosition.lbLongitude.Text")); //"Longitude"
		Utils.addComponent(panelLeft, lbLongitude, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		lbLongitudeVal = new javax.swing.JLabel();
		Utils.addComponent(panelLeft, lbLongitudeVal, 
				1, line++, 
				1, 1, 
				1, 0, 
				5, 0, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//======================================================================
		//-- Distance
		lbDistance = new javax.swing.JLabel();
		lbDistance.setFont(font);
		lbDistance.setText(bundle.getString("frmEditPosition.lbDistance.Text"));//"Distance"
		Utils.addComponent(panelLeft, lbDistance, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		lbDistanceVal = new javax.swing.JLabel();
		Utils.addComponent(panelLeft, lbDistanceVal, 
				1, line++, 
				1, 1, 
				0, 0, 
				5, 0, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//-- Total
		lbTotal = new javax.swing.JLabel();
		lbTotal.setFont(font);
		lbTotal.setText(bundle.getString("frmEditPosition.lbTotal.Text"));//"Total distance"
		Utils.addComponent(panelLeft, lbTotal, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		lbTotalVal = new javax.swing.JLabel();
		Utils.addComponent(panelLeft, lbTotalVal, 
				1, line++, 
				1, 1, 
				1, 0, 
				5, 0, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//======================================================================
		//-- Time
		lbTime = new javax.swing.JLabel();
		lbTime.setFont(font);
		lbTime.setText(bundle.getString("frmEditPosition.lbTime.Text"));//"Time"
		Utils.addComponent(panelLeft, lbTime, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		lbTimeVal = new javax.swing.JLabel();
		Utils.addComponent(panelLeft, lbTimeVal, 
				1, line++, 
				1, 1, 
				0, 0, 
				5, 0, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//-- Hour
		lbHour = new javax.swing.JLabel();
		lbHour.setFont(font);
		lbHour.setText(bundle.getString("frmEditPosition.lbHout.Text"));//"Hour"
		Utils.addComponent(panelLeft, lbHour, 
				0, line, 
				1, 1, 
				0, 1, 
				5, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		lbHourVal = new javax.swing.JLabel();
		Utils.addComponent(panelLeft, lbHourVal, 
				1, line++, 
				1, 1, 
				1, 1, 
				5, 0, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		
		//== Right panel
		line=0;
		panelRight = new JPanel();
		panelRight.setLayout(new GridBagLayout());
		Utils.addComponent(paneGlobal, panelRight, 
				1, 0, 
				1, 1, 
				1, 1, 
				10, 10, 20, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		//-- Name
		lbName = new javax.swing.JLabel();
		lbName.setFont(font);
		lbName.setText(bundle.getString("frmEditPosition.lbName.Text"));//"Name"
		Utils.addComponent(panelRight, lbName, 
				0, line, 
				1, 1, 
				0, 0, 
				0, 0, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		tfName = new javax.swing.JTextField(16);
		Utils.addComponent(panelRight, tfName, 
				1, line++, 
				GridBagConstraints.REMAINDER, 1, 
				1, 0, 
				0, 0, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//-- Elevation
		lbElevation = new javax.swing.JLabel();
		lbElevation.setFont(font);
		lbElevation.setText(bundle.getString("frmEditPosition.lbElevation.Text"));//"Elevation"
		Utils.addComponent(panelRight, lbElevation, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 0, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		spinElevation = new CgSpinner(0,0,100000,1);
		Utils.addComponent(panelRight, spinElevation, 
				1, line, 
				2, 1, 
				0, 0, 
				5, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		
		lbElevationHelp = new javax.swing.JLabel(
				bundle.getString("frmEditPosition.lbElevationHelp.Text"),	
			new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/attention.png")),
			JLabel.LEFT);
		lbElevationHelp.setToolTipText(bundle.getString("frmEditPosition.lbElevationHelp.toolTipText"));
		Utils.addComponent(panelRight, lbElevationHelp, 
				3, line++, 
				GridBagConstraints.REMAINDER, 1, 
				1, 0, 
				5, 10, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		
		lbTags = new javax.swing.JLabel();
		lbTags.setFont(font);
		lbTags.setText(bundle.getString("frmEditPosition.lbTags.Text"));//"Tags"
		Utils.addComponent(panelRight, lbTags, 
				0, line, 
				1, 1, 
				0, 0, 
				0, 0, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		//-- Tag : Mark
		lbMark = new javax.swing.JLabel(
				bundle.getString("frmEditPosition.lbMark.Text"),//"Mark position",	
			new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/flag.png")),
			JLabel.LEFT);
		Utils.addComponent(panelRight, lbMark, 
				1, line, 
				1, 1, 
				0, 0, 
				5, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		chkMark = new javax.swing.JCheckBox();
		Utils.addComponent(panelRight, chkMark, 
				2, line++, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//-- Tag : High point
		lbHighPoint = new javax.swing.JLabel(
				bundle.getString("frmEditPosition.lbHighPoint.Text"), //"High point",	
			new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/high_point.png")),
			JLabel.LEFT);
		Utils.addComponent(panelRight, lbHighPoint, 
				1, line, 
				1, 1, 
				0, 0, 
				5, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		chkHighPoint = new javax.swing.JCheckBox();
		chkHighPoint.setBackground(Color.GREEN);
		chkHighPoint.setOpaque(true);	
		chkHighPoint.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (chkHighPoint.isSelected() && chkLowPoint.isSelected()) {
					chkLowPoint.setSelected(false);
				}
			}
		});
		Utils.addComponent(panelRight, chkHighPoint, 
				2, line++, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		
		//-- Tag : Low point
		lbLowPoint = new javax.swing.JLabel(
				bundle.getString("frmEditPosition.lbLowPoint.Text"), //"Low point",	
			new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/low_point.png")),
			JLabel.LEFT);
		Utils.addComponent(panelRight, lbLowPoint, 
				1, line, 
				1, 1, 
				0, 0, 
				5, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		chkLowPoint = new javax.swing.JCheckBox();
		chkLowPoint.setBackground(Color.GREEN);
		chkLowPoint.setOpaque(true);		
		chkLowPoint.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (chkLowPoint.isSelected() && chkHighPoint.isSelected()) {
					chkHighPoint.setSelected(false);
				}
			}
		});
		Utils.addComponent(panelRight, chkLowPoint, 
				2, line++, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//-- Tag : eat station
		lbEat = new javax.swing.JLabel(
				bundle.getString("frmEditPosition.lbEat.Text"), //"Eat station",	
			new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/eat.png")),
			JLabel.LEFT);
		Utils.addComponent(panelRight, lbEat, 
				1, line, 
				1, 1, 
				0, 0, 
				5, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		chkEat = new javax.swing.JCheckBox();
		chkEat.setBackground(Color.YELLOW);
		chkEat.setOpaque(true);
		chkEat.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (chkEat.isSelected() && chkDrink.isSelected()) {
					chkDrink.setSelected(false);
				}
			}
		});
		Utils.addComponent(panelRight, chkEat, 
				2, line++, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		//-- Tag : drink station
		lbDrink = new javax.swing.JLabel(
				bundle.getString("frmEditPosition.lbDrink.Text"), //"Drink station",	
			new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/drink.png")),
			JLabel.LEFT);
		Utils.addComponent(panelRight, lbDrink, 
				1, line, 
				1, 1, 
				0, 0, 
				5, 
				0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		chkDrink = new javax.swing.JCheckBox();
		chkDrink.setBackground(Color.YELLOW);
		chkDrink.setOpaque(true);
		chkDrink.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (chkDrink.isSelected() && chkEat.isSelected()) {
					chkEat.setSelected(false);
				}
			}
		});
		Utils.addComponent(panelRight, chkDrink, 
				2, line++, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		

		//-- Tag : Place to see
		lbPhoto = new javax.swing.JLabel(
				bundle.getString("frmEditPosition.lbPhoto.Text"), //"Place to see",	
				new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/photo.png")),
				JLabel.LEFT);
		Utils.addComponent(panelRight, lbPhoto, 
				1, line, 
				1, 1, 
				0, 0, 
				5, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		chkPhoto = new javax.swing.JCheckBox();
		Utils.addComponent(panelRight, chkPhoto, 
				2, line++, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//-- Tag : note
		lbNote = new javax.swing.JLabel(
				bundle.getString("frmEditPosition.lbNote.Text"), //"Note",	
			new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/note.png")),
			JLabel.LEFT);
		Utils.addComponent(panelRight, lbNote, 
				1, line, 
				1, 1, 
				0, 0, 
				5, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		chkNote = new javax.swing.JCheckBox();
		Utils.addComponent(panelRight, chkNote, 
				2, line++, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//-- Tag : info
		lbInfo = new javax.swing.JLabel(
				bundle.getString("frmEditPosition.lbInfo.Text"), //"Information",	
			new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/info.png")),
			JLabel.LEFT);
		Utils.addComponent(panelRight, lbInfo, 
				1, line, 
				1, 1, 
				0, 0, 
				5, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		chkInfo = new javax.swing.JCheckBox();
		Utils.addComponent(panelRight, chkInfo, 
				2, line++, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//-- Tag : Roadbook
		lbRoadbook = new javax.swing.JLabel(
				bundle.getString("frmEditPosition.lbRoadbook.Text"), //"Roadbook",	
			new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/roadbook.png")),
			JLabel.LEFT);
		Utils.addComponent(panelRight, lbRoadbook, 
				1, line, 
				1, 1, 
				0, 0, 
				5, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		chkRoadbook = new javax.swing.JCheckBox();
		Utils.addComponent(panelRight, chkRoadbook, 
				2, line++, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		//-- Diff
		lbDiff = new javax.swing.JLabel();
		lbDiff.setFont(font);
		lbDiff.setText(bundle.getString("frmEditPosition.lbDiff.Text"));//"Difficulty"
		Utils.addComponent(panelRight, lbDiff, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 0, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		spinDiff = new CgSpinner(100,0,100,1);
		Utils.addComponent(panelRight, spinDiff, 
				1, line, 
				2, 1, 
				0, 0, 
				5, 0, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		lbDiffHelp = new javax.swing.JLabel(
				bundle.getString("frmEditPosition.lbDiffHelp.Text"),	
			new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/help_dialog.png")),
			JLabel.LEFT);
		lbDiffHelp.setToolTipText(bundle.getString("frmEditPosition.lbDiffHelp.toolTipText"));
		Utils.addComponent(panelRight, lbDiffHelp, 
				3, line++, 
				1, 1, 
				0, 0, 
				5, 0, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		
		//-- Coeff
		lbCoeff = new javax.swing.JLabel();
		lbCoeff.setFont(font);
		lbCoeff.setText(bundle.getString("frmEditPosition.lbCoeff.Text"));//"Health coefficient"
		Utils.addComponent(panelRight, lbCoeff, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 0, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		
		spinCoeff = new CgSpinner(100,0,200,1);
		Utils.addComponent(panelRight, spinCoeff, 
				1, line++, 
				2, 1, 
				0, 0, 
				5, 0, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		//-- Recup
		lbRecovery = new javax.swing.JLabel();
		lbRecovery.setFont(font);
		lbRecovery.setText(bundle.getString("frmEditPosition.lbRecovery.Text"));//"Recovery"
		Utils.addComponent(panelRight, lbRecovery, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 0, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		
		spinRecovery = new CgSpinner(0,0,100,1);
		Utils.addComponent(panelRight, spinRecovery, 
				1, line, 
				2, 1, 
				0, 0, 
				5, 0, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		lbRecoveryHelp = new javax.swing.JLabel(
				bundle.getString("frmEditPosition.lbRecoveryHelp.Text"),	
			new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/help_dialog.png")),
			JLabel.LEFT);
		lbRecoveryHelp.setToolTipText(bundle.getString("frmEditPosition.lbRecoveryHelp.toolTipText"));
		Utils.addComponent(panelRight, lbRecoveryHelp, 
				3, line++, 
				1, 1, 
				0, 0, 
				5, 0, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		//-- Time limit
		lbTimelimit = new javax.swing.JLabel();
		lbTimelimit.setFont(font);
		lbTimelimit.setText(bundle.getString("frmEditPosition.lbTimelimit.Text"));//"Time limit"
		Utils.addComponent(panelRight, lbTimelimit, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 0, 0, 5, 
				GridBagConstraints.WEST, GridBagConstraints.BOTH);

		hsTimelimit = new JHourSetting();
		Utils.addComponent(panelRight, hsTimelimit, 
				1, line++, 
				GridBagConstraints.REMAINDER, 1, 
				0, 0, 
				5, 0, 0, 10, 
				GridBagConstraints.WEST, GridBagConstraints.VERTICAL);

		//-- Station
		lbStation = new javax.swing.JLabel();
		lbStation.setFont(font);
		lbStation.setText(bundle.getString("frmEditPosition.lbStation.Text"));//"Station time" 
		Utils.addComponent(panelRight, lbStation, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 0, 0, 5, 
				GridBagConstraints.WEST, GridBagConstraints.BOTH);

		hsStation = new JHourSetting();
		Utils.addComponent(panelRight, hsStation, 
				1, line++, 
				GridBagConstraints.REMAINDER, 1, 
				0, 0, 
				5, 0, 0, 10, 
				GridBagConstraints.WEST, GridBagConstraints.VERTICAL);

		//-- Comment
		lbComment = new javax.swing.JLabel();
		lbComment.setFont(font);
		lbComment.setText(bundle.getString("frmEditPosition.lbComment.Text"));//"Comment"
		Utils.addComponent(panelRight, lbComment, 
				0, line, 
				1, 1, 
				0, 1, 
				5, 0, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		tfComment = new javax.swing.JTextField();
		Utils.addComponent(panelRight, tfComment, 
				1, line++, 
				4, 1, 
				1, 1, 
				5, 0, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		
		// == BUTTONS
		// ===========================================================
		jPanelButtons = new javax.swing.JPanel();
		jPanelButtons.setLayout(new FlowLayout());
		Utils.addComponent(paneGlobal, jPanelButtons, 
				0, 1, 
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
		lbLineVal.setText(String.format(" : %1.0f", data.getNum()));
        lbLatitudeVal.setText(String.format(" : %1.7f",data.getLatitude()));
        lbLongitudeVal.setText(String.format(" : %1.7f",data.getLongitude()));
        lbDistanceVal.setText(" : "+data.getDistString(settings.Unit,true));
        lbTotalVal.setText(" : "+data.getTotalString(settings.Unit,true));
        lbTimeVal.setText(" : "+data.getTimeString());        
        lbHourVal.setText(" : "+data.getHourString());
        
        spinElevation.setValue((int)data.getElevation(settings.Unit));
        spinDiff.setValue((int)data.getDiff());
        spinCoeff.setValue((int)data.getCoeff());
        spinRecovery.setValue((int)data.getRecovery());
        
        hsStation.setHMSinSecond(data.getStation());
        hsTimelimit.setHMSinSecond(data.getTimeLimit());
	}
}
