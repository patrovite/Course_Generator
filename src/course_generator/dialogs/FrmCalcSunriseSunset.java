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

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
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
import javax.swing.KeyStroke;

import org.joda.time.DateTime;

import course_generator.utils.CgSpinner;
import course_generator.utils.SunCalculator;
import course_generator.utils.Utils;


public class FrmCalcSunriseSunset extends javax.swing.JDialog {

	private ResourceBundle bundle;
	private boolean ok;
	private double longitude;
	private double latitude;
	private DateTime sunrise;
	private DateTime sunset;
	private DateTime date;
	
	private JPanel jPanelButtons;
	private JButton btCancel;
	private JButton btOk;
	private JLabel lbLongitude;
	private JLabel lbLongitudeVal;
	private JLabel lbLatitude;
	private JLabel lbLatitudeVal;
	private JLabel lbTimeZone;
	private CgSpinner spinTimeZone;
	private JLabel lbSunrise;
	private JLabel lbSunriseVal;
	private JLabel lbSunset;
	private JLabel lbSunsetVal;
	private JCheckBox chkSummerTime;
	private JLabel lbDate;
	private JLabel lbDateVal;

	public class ResCalcSunriseSunset {
		DateTime Sunrise;
		DateTime Sunset;
		int TimeZone;
		boolean SummerTime;
		boolean valid;
	}

		
	/**
	 * Creates new form frmSettings
	 */
	public FrmCalcSunriseSunset() {
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		initComponents();
		setModal(true);
	}

	public ResCalcSunriseSunset showDialog(double longitude, double latitude, DateTime starttime, int timezone, boolean summertime) {
		this.longitude=longitude;
		this.latitude=latitude;
		this.date=starttime;
		
		// Set field
		lbLongitudeVal.setText(String.format("%10.7f°", longitude));
		lbLatitudeVal.setText(String.format("%10.7f°", latitude));
		lbSunriseVal.setText("00:00");
		lbSunsetVal.setText("00:00");
		spinTimeZone.setValue(timezone);
		chkSummerTime.setSelected(summertime);
		
		lbDateVal.setText(this.date.toString("dd/MM/yyyy",getLocale()));
		
		// End set field
		ok = false;

		//-- Calculation
		Calc();
		
		//-- Update the display
		Refresh();
		
		//-- Show the dialog
		setVisible(true);
		
		ResCalcSunriseSunset res = new ResCalcSunriseSunset();

		res.valid=ok;
		
		if (ok) {
			// Copy fields
			res.Sunrise=this.sunrise;
			res.Sunset=this.sunset;
			res.TimeZone=spinTimeZone.getValueAsInt();
			res.SummerTime=chkSummerTime.isSelected();
		}
		return res;
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
		int line=0;
		
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(bundle.getString("FrmCalcSunriseSunset.title"));
		setAlwaysOnTop(true);
		setResizable(false);
		setMinimumSize(new Dimension(300,200));
		setType(java.awt.Window.Type.UTILITY);
		addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
		

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new GridBagLayout());

		//== Date
		lbDate = new JLabel(bundle.getString("FrmCalcSunriseSunset.lbDate.Text"));
		Utils.addComponent(paneGlobal, lbDate, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		lbDateVal = new JLabel("",JLabel.CENTER);
		lbDateVal.setOpaque(true);
		lbDateVal.setBackground(Color.WHITE);
		lbDateVal.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		Utils.addComponent(paneGlobal, lbDateVal, 
				1, line++, 
				1, 1, 
				1, 0, 
				5, 5, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//== Longitude
		lbLongitude = new JLabel(bundle.getString("FrmCalcSunriseSunset.lbLongitude.Text"));
		Utils.addComponent(paneGlobal, lbLongitude, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		lbLongitudeVal = new JLabel("",JLabel.CENTER);
		lbLongitudeVal.setOpaque(true);
		lbLongitudeVal.setBackground(Color.WHITE);
		lbLongitudeVal.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		Utils.addComponent(paneGlobal, lbLongitudeVal, 
				1, line++, 
				1, 1, 
				1, 0, 
				5, 5, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		//== Latitude
		lbLatitude = new JLabel(bundle.getString("FrmCalcSunriseSunset.lbLatitude.Text"));
		Utils.addComponent(paneGlobal, lbLatitude, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		lbLatitudeVal = new JLabel("",JLabel.CENTER);
		lbLatitudeVal.setOpaque(true);
		lbLatitudeVal.setBackground(Color.WHITE);
		lbLatitudeVal.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		Utils.addComponent(paneGlobal, lbLatitudeVal, 
				1, line++, 
				1, 1, 
				1, 0, 
				5, 5, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		//Time zone
		lbTimeZone = new JLabel(bundle.getString("FrmCalcSunriseSunset.lbTimeZone.Text"));
		Utils.addComponent(paneGlobal, lbTimeZone, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		spinTimeZone = new CgSpinner(0,-12,12,1);
		spinTimeZone.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				Calc();
				Refresh();
			}
		});

		Utils.addComponent(paneGlobal, spinTimeZone, 
				1, line++, 
				1, 1, 
				1, 0, 
				5, 5, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		
		//== Sunrise
		lbSunrise = new JLabel(bundle.getString("FrmCalcSunriseSunset.lbSunrise.Text"));
		Utils.addComponent(paneGlobal, lbSunrise, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		lbSunriseVal = new JLabel("",JLabel.CENTER);
		lbSunriseVal.setOpaque(true);
		lbSunriseVal.setBackground(Color.WHITE);
		lbSunriseVal.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		Utils.addComponent(paneGlobal, lbSunriseVal, 
				1, line++, 
				1, 1, 
				1, 0, 
				5, 5, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		//== Sunset
		lbSunset = new JLabel(bundle.getString("FrmCalcSunsetSunset.lbSunset.Text"));
		Utils.addComponent(paneGlobal, lbSunset, 
				0, line, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		lbSunsetVal = new JLabel("",JLabel.CENTER);
		lbSunsetVal.setOpaque(true);
		lbSunsetVal.setBackground(Color.WHITE);
		lbSunsetVal.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		Utils.addComponent(paneGlobal, lbSunsetVal, 
				1, line++, 
				1, 1, 
				1, 0, 
				5, 5, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		//== Summer time
		chkSummerTime = new JCheckBox(bundle.getString("FrmCalcSunsetSunset.chkSummerTime.Text"));
		chkSummerTime.setPreferredSize(new Dimension(50,25));
		chkSummerTime.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Calc();
				Refresh();
			}
		});
		Utils.addComponent(paneGlobal, chkSummerTime, 
				1, line++, 
				1, 1, 
				1, 1, 
				5, 5, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		
		// == BUTTONS
		// ===========================================================
		jPanelButtons = new javax.swing.JPanel();
		jPanelButtons.setLayout(new FlowLayout());
		Utils.addComponent(paneGlobal, jPanelButtons, 
				0, line, 
				GridBagConstraints.REMAINDER, 1, 
				1, 0, 
				10, 0, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);


		btOk = new javax.swing.JButton();
		btOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/valid.png")));
		btOk.setText(bundle.getString("FrmCalcSunsetSunset.btOk.text"));
		btOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				RequestToClose();
			}
		});

		btCancel = new javax.swing.JButton();
		btCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/cancel.png")));
		btCancel.setText(bundle.getString("FrmCalcSunsetSunset.btCancel.text"));
		btCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setVisible(false);
			}
		});

		// -- Add buttons
		jPanelButtons.add(btOk);
		jPanelButtons.add(btCancel);

		// --
		pack();

		setLocationRelativeTo(null);
	}

	
	protected void Calc() {
		double LongituteTimeZone = spinTimeZone.getValueAsDouble() * 15.0;

	    SunCalculator sunCalculator = new SunCalculator(longitude, latitude, LongituteTimeZone, chkSummerTime.isSelected());

	    sunrise = sunCalculator.CalculateSunRise(date);
	    sunset = sunCalculator.CalculateSunSet(date);
	}

	protected void formComponentShown(ComponentEvent evt) {
		repaint();
	}

	protected void Refresh() {		
		lbSunriseVal.setText(sunrise.toString("HH:mm"));
		lbSunsetVal.setText(sunset.toString("HH:mm"));
	}
	
	
}
