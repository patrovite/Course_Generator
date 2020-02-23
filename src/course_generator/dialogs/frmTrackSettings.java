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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerDateModel;

import org.joda.time.DateTime;

import com.github.lgooddatepicker.components.CalendarPanel;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import com.github.lgooddatepicker.components.TimePickerSettings.TimeIncrement;
import com.github.lgooddatepicker.optionalusertools.CalendarListener;
import com.github.lgooddatepicker.zinternaltools.CalendarSelectionEvent;
import com.github.lgooddatepicker.zinternaltools.YearMonthChangeEvent;

import course_generator.TrackData;
import course_generator.dialogs.FrmCalcSunriseSunset.ResCalcSunriseSunset;
import course_generator.settings.CgSettings;
import course_generator.utils.CgSpinnerDouble;
import course_generator.utils.JTextFieldLimit;
import course_generator.utils.Utils;

public class frmTrackSettings extends javax.swing.JDialog {

	private static final long serialVersionUID = 4203790672206475952L;
	private ResourceBundle bundle;
	private boolean ok;
	private int timeZone;
	private String timeZoneId;
	private boolean summertime;
	private CgSettings settings;
	private JPanel jPanelButtons;
	private JButton btCancel;
	private JButton btOk;
	private TrackData track;
	private JPanel panelTrackName;
	private JPanel panelDescription;
	private JPanel panelDateTime;
	private JPanel panelNightEffect;
	private JTextFieldLimit tfTrackName;
	private JTextField tfDescription;
	private JPanel panelElevationEffect;
	private JCheckBox chkNightEffect;
	private JCheckBox chkElevationEffect;
	private CalendarPanel calendar;
	private LocalDate newSelectedDate;
	private SpinnerDateModel spinStartNightModel;
	private JSpinner spinStartNight;
	private JLabel lbStartNight;
	private JLabel lbEndNight;
	private SpinnerDateModel spinEndNightModel;
	private JSpinner spinEndNight;
	private TimePickerSettings timePickerSettings;
	private TimePicker timePicker;
	private JButton btCalc;
	private JLabel lbAscCoeff;
	private CgSpinnerDouble spinAscCoeff;
	private JLabel lbDescCoeff;
	private CgSpinnerDouble spinDescCoeff;
	private JPanel panelCoeff;
	private static FrmCalcSunriseSunset calcSunriseSunset;

	/**
	 * Creates new form frmSettings
	 */
	public frmTrackSettings(CgSettings settings) {
		this.settings = settings;
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle"); //$NON-NLS-1$
		initComponents();
		setModal(true);
	}

	public boolean showDialog(TrackData track) {
		this.track = track;
		this.timeZone = track.timeZoneOffsetHours;
		this.timeZoneId = track.timeZoneId;

		this.summertime = track.TrackUseDaylightSaving;

		// Set field
		tfTrackName.setText(this.track.CourseName);
		tfDescription.setText(this.track.Description);

		newSelectedDate = LocalDate.of(this.track.StartTime.getYear(), this.track.StartTime.getMonthOfYear(),
				this.track.StartTime.getDayOfMonth());
		calendar.setSelectedDate(newSelectedDate);

		timePickerSettings.initialTime = LocalTime.of(this.track.StartTime.getHourOfDay(),
				this.track.StartTime.getMinuteOfHour());
		timePicker.setTime(timePickerSettings.initialTime);
		chkElevationEffect.setSelected(this.track.bElevEffect);
		chkNightEffect.setSelected(this.track.bNightCoeff);
		spinStartNightModel.setValue(Utils.DateTimetoSpinnerDate(this.track.StartNightTime));
		spinEndNightModel.setValue(Utils.DateTimetoSpinnerDate(this.track.EndNightTime));
		spinAscCoeff.setValue(this.track.NightCoeffAsc);
		spinDescCoeff.setValue(this.track.NightCoeffDesc);

		// End set field
		ok = false;

		// -- Update the display
		Refresh();

		// -- Show the dialog
		setVisible(true);

		if (ok && !this.track.ReadOnly) {
			// Copy fields
			track.CourseName = tfTrackName.getText();
			track.Description = tfDescription.getText();
			DateTime std = new DateTime(newSelectedDate.getYear(), newSelectedDate.getMonthValue(),
					newSelectedDate.getDayOfMonth(), 0, 0, 0);
			std = std.withTime(timePicker.getTime().getHour(), timePicker.getTime().getMinute(), 0, 0);
			track.StartTime = new DateTime(std.getYear(), std.getMonthOfYear(), std.getDayOfMonth(),
					timePicker.getTime().getHour(), timePicker.getTime().getMinute());

			track.bElevEffect = chkElevationEffect.isSelected();
			track.bNightCoeff = chkNightEffect.isSelected();
			track.StartNightTime = new DateTime(spinStartNightModel.getValue());
			track.EndNightTime = new DateTime(spinEndNightModel.getValue());

			track.NightCoeffAsc = spinAscCoeff.getValueAsDouble();
			track.NightCoeffDesc = spinDescCoeff.getValueAsDouble();

			track.timeZoneOffsetHours = this.timeZone;
			track.timeZoneId = this.timeZoneId;
			track.TrackUseDaylightSaving = this.summertime;

			JOptionPane.showMessageDialog(this, bundle.getString("frmTrackSettings.ModificationMsg")); //$NON-NLS-1$
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
		KeyStroke strokeEscape = KeyStroke.getKeyStroke("ESCAPE"); //$NON-NLS-1$
		KeyStroke strokeEnter = KeyStroke.getKeyStroke("ENTER"); //$NON-NLS-1$

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
		inputMap.put(strokeEscape, "ESCAPE"); //$NON-NLS-1$
		rootPane.getActionMap().put("ESCAPE", actionListener); //$NON-NLS-1$

		inputMap.put(strokeEnter, "ENTER"); //$NON-NLS-1$
		rootPane.getActionMap().put("ENTER", actionListenerEnter); //$NON-NLS-1$

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
		setTitle(bundle.getString("frmTrackSettings.title")); //$NON-NLS-1$
		setAlwaysOnTop(true);
		setResizable(false);
		setMinimumSize(new Dimension(300, 400));
		setType(java.awt.Window.Type.UTILITY);

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new GridBagLayout());

		// == Panel Track name
		panelTrackName = new JPanel();
		panelTrackName.setLayout(new GridBagLayout());
		panelTrackName
				.setBorder(BorderFactory.createTitledBorder(bundle.getString("frmTrackSettings.panelTrackName.Title"))); // Start //$NON-NLS-1$
		panelTrackName.setLayout(new GridBagLayout());
		Utils.addComponent(paneGlobal, panelTrackName, 0, 0, 1, 1, 0, 0, 10, 10, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		tfTrackName = new JTextFieldLimit(15);
		Utils.addComponent(panelTrackName, tfTrackName, 0, 0, 1, 1, 1, 0, 5, 5, 5, 5,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// == Panel description
		panelDescription = new JPanel();
		panelDescription.setLayout(new GridBagLayout());
		panelDescription.setBorder(
				BorderFactory.createTitledBorder(bundle.getString("frmTrackSettings.panelDescription.Title")));// End //$NON-NLS-1$
		panelDescription.setLayout(new GridBagLayout());
		Utils.addComponent(paneGlobal, panelDescription, 0, 1, 1, 1, 0, 0, 10, 10, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		tfDescription = new JTextField();
		Utils.addComponent(panelDescription, tfDescription, 0, 0, 1, 1, 1, 0, 5, 5, 5, 5,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// == Panel Date & time
		panelDateTime = new JPanel();
		panelDateTime.setLayout(new GridBagLayout());
		panelDateTime
				.setBorder(BorderFactory.createTitledBorder(bundle.getString("frmTrackSettings.panelDateTime.Title")));// Difficulty //$NON-NLS-1$
		Utils.addComponent(paneGlobal, panelDateTime, 0, 2, 1, 1, 1, 1, 10, 10, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		DatePickerSettings datePickerSettings = new DatePickerSettings();
		calendar = new CalendarPanel(datePickerSettings);
		calendar.addCalendarListener(new CalendarListener() {

			@Override
			public void selectedDateChanged(CalendarSelectionEvent event) {
				if (event.getNewDate() == null) {
					// If the user has clicked the "Clear" button
					// we go back to the original date.
					calendar.setSelectedDate(LocalDate.of(track.StartTime.getYear(), track.StartTime.getMonthOfYear(),
							track.StartTime.getDayOfMonth()));
				} else if (!newSelectedDate.equals(event.getNewDate())) {
					newSelectedDate = event.getNewDate();
				}
			}

			@Override
			public void yearMonthChanged(YearMonthChangeEvent event) {
				try {
					YearMonth newYearMonth = event.getNewYearMonth();
					newSelectedDate = LocalDate.of(newYearMonth.getYear(), newYearMonth.getMonth(),	newSelectedDate.getDayOfMonth());
				}
				catch (Exception e) {
					// TODO: handle exception
				}
			}

		});

		Utils.addComponent(panelDateTime, calendar, 0, 0, 1, 1, 0, 0, 5, 5, 5, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.NONE);

		timePickerSettings = new TimePickerSettings();
		timePickerSettings.use24HourClockFormat();
		timePickerSettings.generatePotentialMenuTimes(TimeIncrement.ThirtyMinutes, null, null);
		timePicker = new TimePicker(timePickerSettings);

		Utils.addComponent(panelDateTime, timePicker, 1, 0, 1, 1, 1, 0, 5, 10, 5, 5,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		// -- Panel elevation effect
		panelElevationEffect = new JPanel();
		panelElevationEffect.setLayout(new GridBagLayout());
		panelElevationEffect.setBorder(
				BorderFactory.createTitledBorder(bundle.getString("frmTrackSettings.panelElevationEffect.Title"))); //$NON-NLS-1$
		panelElevationEffect.setLayout(new GridBagLayout());
		Utils.addComponent(paneGlobal, panelElevationEffect, 0, 3, 1, 1, 1, 1, 10, 10, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		chkElevationEffect = new JCheckBox(bundle.getString("frmTrackSettings.rbElevationEffect.Text")); //$NON-NLS-1$
		Utils.addComponent(panelElevationEffect, chkElevationEffect, 0, 1, 1, 1, 1, 0, 5, 5, 5, 5,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		// -- Panel night effect
		panelNightEffect = new JPanel();
		panelNightEffect.setLayout(new GridBagLayout());
		panelNightEffect.setBorder(
				BorderFactory.createTitledBorder(bundle.getString("frmTrackSettings.panelNightEffect.Title"))); //$NON-NLS-1$
		Utils.addComponent(paneGlobal, panelNightEffect, 0, 4, 1, 1, 1, 1, 10, 10, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		chkNightEffect = new JCheckBox(bundle.getString("frmTrackSettings.rbNightEffect.Text")); //$NON-NLS-1$
		chkNightEffect.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Refresh();
			}
		});
		Utils.addComponent(panelNightEffect, chkNightEffect, 0, 0, GridBagConstraints.REMAINDER, 1, 1, 0, 5, 5, 5, 5,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		lbStartNight = new JLabel(bundle.getString("frmTrackSettings.lbStartNight.Text")); //$NON-NLS-1$
		Utils.addComponent(panelNightEffect, lbStartNight, 0, 1, 1, 1, 0, 0, 5, 5, 5, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		spinStartNightModel = new SpinnerDateModel(new Date(), null, null, Calendar.HOUR_OF_DAY);
		spinStartNight = new javax.swing.JSpinner(spinStartNightModel);
		JSpinner.DateEditor deStartNight = new JSpinner.DateEditor(spinStartNight, "HH:mm"); //$NON-NLS-1$
		spinStartNight.setEditor(deStartNight);
		Utils.addComponent(panelNightEffect, spinStartNight, 1, 1, 1, 1, 0, 0, 5, 5, 5, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		lbEndNight = new JLabel(bundle.getString("frmTrackSettings.lbEndNight.Text")); //$NON-NLS-1$
		Utils.addComponent(panelNightEffect, lbEndNight, 2, 1, 1, 1, 0, 0, 5, 10, 5, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		spinEndNightModel = new SpinnerDateModel(new Date(), null, null, Calendar.HOUR_OF_DAY);
		spinEndNight = new javax.swing.JSpinner(spinEndNightModel);
		JSpinner.DateEditor deEndNight = new JSpinner.DateEditor(spinEndNight, "HH:mm"); //$NON-NLS-1$
		spinEndNight.setEditor(deEndNight);
		Utils.addComponent(panelNightEffect, spinEndNight, 3, 1, 1, 1, 0, 0, 5, 5, 5, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		btCalc = new JButton(bundle.getString("frmTrackSettings.btCalc.text")); //$NON-NLS-1$
		btCalc.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				ResCalcSunriseSunset res = ShowCalcSunriseSunset();

				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

				if (res.valid) {
					timeZone = res.TimeZone;
					timeZoneId = res.TimeZoneId;
					summertime = res.SummerTime;

					Date date = Utils.DateTimetoSpinnerDate(res.Sunrise);

					spinEndNightModel.setValue(date);

					date = Utils.DateTimetoSpinnerDate(res.Sunset);

					spinStartNightModel.setValue(date);
				}
			}
		});
		Utils.addComponent(panelNightEffect, btCalc, 4, 1, 1, 1, 1, 0, 5, 10, 5, 5, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.NONE);

		panelCoeff = new JPanel();
		Utils.addComponent(panelNightEffect, panelCoeff, 0, 2, GridBagConstraints.REMAINDER, 1, 1, 0, 5, 5, 5, 1,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		lbAscCoeff = new JLabel(bundle.getString("frmTrackSettings.lbAscCoeff.Text")); //$NON-NLS-1$
		panelCoeff.add(lbAscCoeff);

		spinAscCoeff = new CgSpinnerDouble(100, 0, 100, 1);
		panelCoeff.add(spinAscCoeff);

		lbDescCoeff = new JLabel(bundle.getString("frmTrackSettings.lbDescCoeff.Text")); //$NON-NLS-1$
		panelCoeff.add(lbDescCoeff);

		spinDescCoeff = new CgSpinnerDouble(100, 0, 100, 1);
		panelCoeff.add(spinDescCoeff);

		// == BUTTONS
		// ===========================================================
		jPanelButtons = new javax.swing.JPanel();
		jPanelButtons.setLayout(new FlowLayout());
		Utils.addComponent(paneGlobal, jPanelButtons, 0, 5, 1, 1, 0, 0, 10, 0, 0, 0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL);

		btCancel = new javax.swing.JButton();
		btCancel.setIcon(Utils.getIcon(this, "cancel.png", settings.DialogIconSize)); //$NON-NLS-1$
		btCancel.setText(bundle.getString("Global.btCancel.text")); //$NON-NLS-1$
		btCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setVisible(false);
			}
		});

		btOk = new javax.swing.JButton();
		btOk.setIcon(Utils.getIcon(this, "valid.png", settings.DialogIconSize)); //$NON-NLS-1$
		btOk.setText(bundle.getString("Global.btOk.text")); //$NON-NLS-1$
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

	private ResCalcSunriseSunset ShowCalcSunriseSunset() {
		if (calcSunriseSunset == null)
			calcSunriseSunset = new FrmCalcSunriseSunset(this, settings);

		return calcSunriseSunset.showDialog(track.data.get(0).getLongitude(), track.data.get(0).getLatitude(),
				new DateTime(newSelectedDate.getYear(), newSelectedDate.getMonthValue(),
						newSelectedDate.getDayOfMonth(), 0, 0, 0));
	}

	protected void Refresh() {
		spinStartNight.setEnabled(chkNightEffect.isSelected());
		spinEndNight.setEnabled(chkNightEffect.isSelected());
		btCalc.setEnabled(chkNightEffect.isSelected());
		spinAscCoeff.setEnabled(chkNightEffect.isSelected());
		spinDescCoeff.setEnabled(chkNightEffect.isSelected());
	}

}
