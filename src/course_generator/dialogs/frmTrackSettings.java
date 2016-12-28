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
import java.util.Calendar;
import java.util.Date;
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
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerDateModel;

import org.jdesktop.swingx.JXMonthView;
import org.joda.time.DateTime;

import course_generator.CgData;
import course_generator.TrackData;
import course_generator.dialogs.FrmCalcSunriseSunset.ResCalcSunriseSunset;
import course_generator.settings.CgSettings;
import course_generator.utils.CgSpinnerDouble;
import course_generator.utils.JTextFieldLimit;
import course_generator.utils.Utils;

public class frmTrackSettings extends javax.swing.JDialog {

	private ResourceBundle bundle;
	private boolean ok;
	private Double timezone;
	private boolean summertime;
	private CgSettings settings;
	private JPanel jPanelButtons;
	private JButton btCancel;
	private JButton btOk;
	private TrackData track;
	private CgData data;
	private JPanel panelTrackName;
	private JPanel panelDescription;
	private JPanel panelDateTime;
	private JPanel panelEstimateTime;
	private JPanel panelNightEffect;
	private JTextFieldLimit tfTrackName;
	private JTextField tfDescription;
	private JRadioButton rbNightEffect;
	private JPanel panelElevationEffect;
	private JRadioButton rbElevationEffect;
	private JCheckBox chkNightEffect;
	private JCheckBox chkElevationEffect;
	private JXMonthView jMonthView;
	private SpinnerDateModel spinStartTimeModel;
	private JSpinner spinStartTime;
	private SpinnerDateModel spinStartNightModel;
	private JSpinner spinStartNight;
	private JLabel lbStartNight;
	private JLabel lbEndNight;
	private SpinnerDateModel spinEndNightModel;
	private JSpinner spinEndNight;
	private JButton btCalc;
	private JLabel lbAscCoeff;
	private CgSpinnerDouble spinAscCoeff;
	private JLabel lbDescCoeff;
	private CgSpinnerDouble spinDescCoeff;
	private JPanel panelCoeff;
	
	/**
	 * Creates new form frmSettings
	 */
	public frmTrackSettings() {
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		initComponents();
		setModal(true);
	}

	public boolean showDialog(CgSettings settings, TrackData track) {
		this.settings = settings;
		this.track = track;
		this.timezone=track.TrackTimeZone;
		this.summertime=track.TrackUseSumerTime;
		
		// Set field
		tfTrackName.setText(this.track.CourseName);
		tfDescription.setText(this.track.Description);
		jMonthView.setSelectionDate(this.track.StartTime.toDate()); 
		spinStartTimeModel.setValue(this.track.StartTime.toDate());
		chkElevationEffect.setSelected(this.track.bElevEffect);
		chkNightEffect.setSelected(this.track.bNightCoeff);
		spinStartNightModel.setValue(this.track.StartNightTime.toDate());
		spinEndNightModel.setValue(this.track.EndNightTime.toDate());
		spinAscCoeff.setValue(this.track.NightCoeffAsc);
		spinDescCoeff.setValue(this.track.NightCoeffDesc);
		
		// End set field
		ok = false;

		//-- Update the display
		Refresh();
		
		//-- Show the dialog
		setVisible(true);

		if (ok) {
			// Copy fields
			track.CourseName=tfTrackName.getText();
			track.Description=tfDescription.getText();
			DateTime std=new DateTime(jMonthView.getSelectionDate()); 
			DateTime stt=new DateTime(spinStartTimeModel.getValue());
			std=std.withTime(stt.getHourOfDay(), stt.getMinuteOfHour(), 0, 0);
			track.StartTime=std;
			
			track.bElevEffect=chkElevationEffect.isSelected();
			track.bNightCoeff=chkNightEffect.isSelected();
			track.StartNightTime=new DateTime(spinStartNightModel.getValue());
			track.EndNightTime=new DateTime(spinEndNightModel.getValue());
			
			track.NightCoeffAsc=spinAscCoeff.getValueAsDouble();
			track.NightCoeffDesc=spinDescCoeff.getValueAsDouble();
			
			track.TrackTimeZone=this.timezone;
			track.TrackUseSumerTime=this.summertime;
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

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(bundle.getString("frmTrackSettings.title"));
		setAlwaysOnTop(true);
		setResizable(false);
		setMinimumSize(new Dimension(300,400));
		setType(java.awt.Window.Type.UTILITY);

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new GridBagLayout());

		//== Panel Track name
		panelTrackName = new JPanel();
		panelTrackName.setLayout(new GridBagLayout());
		panelTrackName.setBorder(BorderFactory.createTitledBorder(bundle.getString("frmTrackSettings.panelTrackName.Title"))); //Start
		panelTrackName.setLayout(new GridBagLayout());
		Utils.addComponent(paneGlobal, panelTrackName, 
				0, 0, 
				1, 1, 
				0, 0, 
				10, 10, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		tfTrackName = new JTextFieldLimit(15);
		Utils.addComponent(panelTrackName, tfTrackName, 
				0, 0, 
				1, 1, 
				1, 0, 
				5, 5, 5, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		
		//== Panel description
		panelDescription = new JPanel();
		panelDescription.setLayout(new GridBagLayout());
		panelDescription.setBorder(BorderFactory.createTitledBorder(bundle.getString("frmTrackSettings.panelDescription.Title")));//End
		panelDescription.setLayout(new GridBagLayout());
		Utils.addComponent(paneGlobal, panelDescription, 
				0, 1, 
				1, 1, 
				0, 0, 
				10, 10, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		tfDescription = new JTextField();
		Utils.addComponent(panelDescription, tfDescription, 
				0, 0, 
				1, 1, 
				1, 0, 
				5, 5, 5, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		
		//== Panel Date & time
		panelDateTime = new JPanel();
		panelDateTime.setLayout(new GridBagLayout());
		panelDateTime.setBorder(BorderFactory.createTitledBorder(bundle.getString("frmTrackSettings.panelDateTime.Title")));//Difficulty
		Utils.addComponent(paneGlobal, panelDateTime, 
				0, 2, 
				1, 1, 
				1, 1, 
				10, 10, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		jMonthView = new org.jdesktop.swingx.JXMonthView();
		jMonthView.setBackground(new java.awt.Color(255, 255, 255));
		jMonthView.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
		jMonthView.setBoxPaddingX(1);
		jMonthView.setBoxPaddingY(1);
//		jMonthView.setShowingWeekNumber(true);
//		jMonthView.setTraversable(true);		
		Utils.addComponent(panelDateTime, jMonthView, 
				0, 0, 
				1, 1, 
				0, 0, 
				5, 5, 5, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);
		
		spinStartTimeModel = new SpinnerDateModel(new Date(), null, null, Calendar.HOUR_OF_DAY);
		spinStartTime = new javax.swing.JSpinner(spinStartTimeModel);
		JSpinner.DateEditor deStartTime = new JSpinner.DateEditor(spinStartTime, "HH:mm");
		spinStartTime.setEditor(deStartTime);
		Utils.addComponent(panelDateTime, spinStartTime, 
				1, 0, 
				1, 1, 
				1, 0, 
				5, 10, 5, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);
		
		
		//-- Panel elevation effect
		panelElevationEffect = new JPanel();
		panelElevationEffect.setLayout(new GridBagLayout());
		panelElevationEffect.setBorder(BorderFactory.createTitledBorder(bundle.getString("frmTrackSettings.panelElevationEffect.Title")));
		panelElevationEffect.setLayout(new GridBagLayout());
		Utils.addComponent(paneGlobal, panelElevationEffect, 
				0, 3, 
				1, 1, 
				1, 1, 
				10, 10, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		chkElevationEffect = new JCheckBox(bundle.getString("frmTrackSettings.rbElevationEffect.Text"));
		Utils.addComponent(panelElevationEffect, chkElevationEffect, 
				0, 1, 
				1, 1, 
				1, 0, 
				5, 5, 5, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);
		
		
		//-- Panel night effect
		panelNightEffect = new JPanel();
		panelNightEffect.setLayout(new GridBagLayout());
		panelNightEffect.setBorder(BorderFactory.createTitledBorder(bundle.getString("frmTrackSettings.panelNightEffect.Title")));
		Utils.addComponent(paneGlobal, panelNightEffect, 
				0, 4, 
				1, 1, 
				1, 1, 
				10, 10, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		chkNightEffect = new JCheckBox(bundle.getString("frmTrackSettings.rbNightEffect.Text"));
		chkNightEffect.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Refresh();
			}
		});
		Utils.addComponent(panelNightEffect, chkNightEffect, 
				0, 0, 
				GridBagConstraints.REMAINDER, 1, 
				1, 0, 
				5, 5, 5, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		
		lbStartNight = new JLabel(bundle.getString("frmTrackSettings.lbStartNight.Text"));
		Utils.addComponent(panelNightEffect, lbStartNight, 
				0, 1, 
				1, 1, 
				0, 0, 
				5, 5, 5, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);
		
		spinStartNightModel = new SpinnerDateModel(new Date(), null, null, Calendar.HOUR_OF_DAY);
		spinStartNight = new javax.swing.JSpinner(spinStartNightModel);
		JSpinner.DateEditor deStartNight = new JSpinner.DateEditor(spinStartNight, "HH:mm");
		spinStartNight.setEditor(deStartNight);
		Utils.addComponent(panelNightEffect, spinStartNight, 
				1, 1, 
				1, 1, 
				0, 0, 
				5, 5, 5, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		lbEndNight = new JLabel(bundle.getString("frmTrackSettings.lbEndNight.Text"));
		Utils.addComponent(panelNightEffect, lbEndNight, 
				2, 1, 
				1, 1, 
				0, 0, 
				5, 10, 5, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);
		
		spinEndNightModel = new SpinnerDateModel(new Date(), null, null, Calendar.HOUR_OF_DAY);
		spinEndNight = new javax.swing.JSpinner(spinEndNightModel);
		JSpinner.DateEditor deEndNight = new JSpinner.DateEditor(spinEndNight, "HH:mm");
		spinEndNight.setEditor(deEndNight);
		Utils.addComponent(panelNightEffect, spinEndNight, 
				3, 1, 
				1, 1, 
				0, 0, 
				5, 5, 5, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);
		
		btCalc = new JButton(bundle.getString("frmTrackSettings.btCalc.text"));
		btCalc.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				FrmCalcSunriseSunset frm = new FrmCalcSunriseSunset();
				ResCalcSunriseSunset res = frm.showDialog(
						track.data.get(0).getLongitude(),
						track.data.get(0).getLatitude(),
						track.StartTime,
						track.TrackTimeZone.intValue(),
						track.TrackUseSumerTime);
				if (res.valid) {
					timezone=Double.valueOf(res.TimeZone);
					summertime=res.SummerTime;
					spinEndNightModel.setValue(res.Sunrise.toDate());
					spinStartNightModel.setValue(res.Sunset.toDate());
				}
			}
		});
		Utils.addComponent(panelNightEffect, btCalc, 
				4, 1, 
				1, 1, 
				1, 0, 
				5, 10, 5, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);
		
		panelCoeff = new JPanel();
		Utils.addComponent(panelNightEffect, panelCoeff, 
				0, 2, 
				GridBagConstraints.REMAINDER, 1, 
				1, 0, 
				5, 5, 5, 1, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);
		
		lbAscCoeff = new JLabel(bundle.getString("frmTrackSettings.lbAscCoeff.Text"));
		panelCoeff.add(lbAscCoeff);

		spinAscCoeff = new CgSpinnerDouble(100,0,100,1);
		panelCoeff.add(spinAscCoeff);

		lbDescCoeff = new JLabel(bundle.getString("frmTrackSettings.lbDescCoeff.Text"));
		panelCoeff.add(lbDescCoeff);

		spinDescCoeff = new CgSpinnerDouble(100,0,100,1);
		panelCoeff.add(spinDescCoeff);
		
		
		// == BUTTONS
		// ===========================================================
		jPanelButtons = new javax.swing.JPanel();
		jPanelButtons.setLayout(new FlowLayout());
		Utils.addComponent(paneGlobal, jPanelButtons, 
				0, 5, 
				1, 1, 
				0, 0, 
				10, 0, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);

		btCancel = new javax.swing.JButton();
		btCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/cancel.png")));
		btCancel.setText(bundle.getString("frmTrackSettings.btCancel.text"));
		btCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setVisible(false);
			}
		});

		btOk = new javax.swing.JButton();
		btOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/valid.png")));
		btOk.setText(bundle.getString("frmTrackSettings.btOk.text"));
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

	
	protected void Refresh() {
		spinStartNight.setEnabled(chkNightEffect.isSelected());
		spinEndNight.setEnabled(chkNightEffect.isSelected());
		btCalc.setEnabled(chkNightEffect.isSelected());
		spinAscCoeff.setEnabled(chkNightEffect.isSelected());
		spinDescCoeff.setEnabled(chkNightEffect.isSelected());
	}
	
	
}
