/*
 * Course Generator
 * Copyright (C) 2008..2019 Pierre Delore and contributors
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
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import course_generator.CgData;
import course_generator.TrackData;
import course_generator.settings.CgSettings;
import course_generator.utils.CgConst;
import course_generator.utils.CgSpinner;
import course_generator.utils.CgSpinnerDouble;
import course_generator.utils.JTextFieldLimit;
import course_generator.utils.JTimeSetting;
import course_generator.utils.Utils;

public class frmSearchCurve extends javax.swing.JDialog{
	private static final long serialVersionUID = 2889867873080848278L;
	private ResourceBundle bundle;
	private boolean ok;
	private final int NbCurves = 145;
	private String strMin = "";
	private String strMax = "";	
	private CgSettings settings;
	private TrackData track;
	private CgData data;
	private JPanel panelFinalTime;
	private JTimeSetting hsFinalTime;
	private JButton btSearchCurve;
	private JPanel jPanelButtons;
	private JButton btCancel;
	private JComponent panelProgress;
	private JProgressBar progressBar;
	private JComponent panelResult;
	private JLabel lbFoundCurve1;
	private JLabel lbFoundTime1;
	private JLabel lbFoundTime1Val;
	private JButton btFoundSelect1;
	private JLabel lbFoundCurve2;
	private JLabel lbFoundTime2;
	private JLabel lbFoundTime2Val;
	private JButton btFoundSelect2;

	
	/**
	 * Creates new form frmSettings
	 */
	public frmSearchCurve(CgSettings _settings) {
		settings = _settings;
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		initComponents();
		setModal(true);
	}


	public boolean showDialog(CgSettings _settings, TrackData _track) {
		settings = _settings;
		track = _track;
		
		// Set field


		// End set field
		ok = false;

		// -- Update the display
		//Refresh();

		// -- Show the dialog
		setVisible(true);

		if (ok && !track.ReadOnly) {
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
		int line = 0;

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(bundle.getString("frmSearchCurve.title"));
		setAlwaysOnTop(true);
		setResizable(false);
		//setMinimumSize(new Dimension(1000, 400));
		setType(java.awt.Window.Type.UTILITY);

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new GridBagLayout());

		// == Final time panel
		panelFinalTime = new JPanel();
		panelFinalTime.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("frmSearchCurve.panelFinalTime"))); //TODO bundle...
		panelFinalTime.setOpaque(true);
		panelFinalTime.setLayout(new GridBagLayout());
		Utils.addComponent(paneGlobal, panelFinalTime, 
				0, 0, 
				1, 1, 
				0, 1, 
				10, 10, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		// -- Line
		line = 0;

		hsFinalTime = new JTimeSetting(999);
		Utils.addComponent(panelFinalTime, hsFinalTime, 
				0, line++, 
				1, 1, 
				1, 0, 
				5, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.VERTICAL);

		btSearchCurve = new javax.swing.JButton();
		btSearchCurve.setToolTipText(bundle.getString("frmSearchCurve.btSearch.tooltips")); //TODO change....
		btSearchCurve.setIcon(Utils.getIcon(this, "search.png", settings.DialogIconSize));		
		btSearchCurve.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Search();
			}
		});
		Utils.addComponent(panelFinalTime, btSearchCurve, 
				0, line, 
				1, 1, 
				1, 0, 
				5, 0, 0, 5, 
				GridBagConstraints.WEST, GridBagConstraints.BOTH);
		
		// == Progress panel
		panelProgress = new JPanel();
		panelProgress.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("frmSearchCurve.panelProgress"))); //TODO bundle
		panelProgress.setOpaque(true);
		panelProgress.setLayout(new GridBagLayout());
		Utils.addComponent(paneGlobal, panelProgress, 
				0, 1, 
				1, 1, 
				0, 1, 
				0, 10, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		// -- Line
		line = 0;

		progressBar = new JProgressBar(0, NbCurves);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		Utils.addComponent(panelProgress, progressBar, 
				0, line, 
				1, 1, 
				1, 1, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);


		// == Result panel
		panelResult = new JPanel();
		panelResult.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("frmSearchCurve.panelResult"))); //TODO bundle
		panelResult.setOpaque(true);
		panelResult.setLayout(new GridBagLayout());
		Utils.addComponent(paneGlobal, panelResult, 
				0, 2, 
				1, 1, 
				0, 1, 
				10, 10, 20, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		line = 0;

		// -- Line 1
		lbFoundCurve1 = new javax.swing.JLabel();
		lbFoundCurve1.setFocusable(false);
		lbFoundCurve1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
		lbFoundCurve1.setMinimumSize(new Dimension(250, 24));
		lbFoundCurve1.setPreferredSize(new Dimension(250, 24));
		lbFoundCurve1.setBackground(new java.awt.Color(255, 255, 255));
		lbFoundCurve1.setOpaque(true);
		lbFoundCurve1.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		Utils.addComponent(panelResult, lbFoundCurve1, 
				0, line, 
				1, 1, 
				1, 0, 
				0, 5, 2, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		lbFoundTime1 = new javax.swing.JLabel();
		lbFoundTime1.setText(bundle.getString("frmSearchCurve.lbFoundTime1.Text")); 
		Utils.addComponent(panelResult, lbFoundTime1, 
				1, line, 
				1, 1, 
				1, 0, 
				0, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		lbFoundTime1Val = new javax.swing.JLabel();
		lbFoundTime1Val.setFocusable(false);
		lbFoundTime1Val.setHorizontalAlignment(javax.swing.JTextField.CENTER);
		lbFoundTime1Val.setMinimumSize(new Dimension(120, 24));
		lbFoundTime1Val.setPreferredSize(new Dimension(120, 24));
		lbFoundTime1Val.setBackground(new java.awt.Color(255, 255, 255));
		lbFoundTime1Val.setOpaque(true);
		lbFoundTime1Val.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		Utils.addComponent(panelResult, lbFoundTime1Val, 
				2, line, 
				1, 1, 
				1, 0, 
				0, 5, 2, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		btFoundSelect1 = new javax.swing.JButton();
		btFoundSelect1.setText(bundle.getString("frmSearchCurve.btFoundSelect1.Text")); 
		btFoundSelect1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (!strMin.isEmpty()) {
					track.Paramfile = strMin;
					RequestToClose();
				}				
			}
		});
		Utils.addComponent(panelResult, btFoundSelect1, 
				3, line, 
				1, 1, 
				0, 0, 
				0, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		// -- Line 2
		line++;
		lbFoundCurve2 = new javax.swing.JLabel();
		lbFoundCurve2.setFocusable(false);
		lbFoundCurve2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
		lbFoundCurve2.setMinimumSize(new Dimension(250, 24));
		lbFoundCurve2.setPreferredSize(new Dimension(250, 24));
		lbFoundCurve2.setBackground(new java.awt.Color(255, 255, 255));
		lbFoundCurve2.setOpaque(true);
		lbFoundCurve2.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		Utils.addComponent(panelResult, lbFoundCurve2, 
				0, line, 
				1, 1, 
				1, 0, 
				0, 5, 2, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		lbFoundTime2 = new javax.swing.JLabel();
		lbFoundTime2.setText(bundle.getString("frmSearchCurve.lbFoundTime2.Text")); 
		Utils.addComponent(panelResult, lbFoundTime2, 
				1, line, 
				1, 1, 
				1, 0, 
				0, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		lbFoundTime2Val = new javax.swing.JLabel();
		lbFoundTime2Val.setFocusable(false);
		lbFoundTime2Val.setHorizontalAlignment(javax.swing.JTextField.CENTER);
		lbFoundTime2Val.setMinimumSize(new Dimension(120, 24));
		lbFoundTime2Val.setPreferredSize(new Dimension(120, 24));
		lbFoundTime2Val.setBackground(new java.awt.Color(255, 255, 255));
		lbFoundTime2Val.setOpaque(true);
		lbFoundTime2Val.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		Utils.addComponent(panelResult, lbFoundTime2Val, 
				2, line, 
				1, 1, 
				1, 0, 
				0, 5, 2, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		btFoundSelect2 = new javax.swing.JButton();
		btFoundSelect2.setText(bundle.getString("frmSearchCurve.btFoundSelect2.Text")); //TODO change....
		btFoundSelect2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (!strMax.isEmpty()) {
					track.Paramfile = strMax;
					RequestToClose();
				}
			}
		});
		Utils.addComponent(panelResult, btFoundSelect2, 
				3, line, 
				1, 1, 
				0, 0, 
				0, 10, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
				

		// == BUTTONS
		// ===========================================================
		jPanelButtons = new javax.swing.JPanel();
		jPanelButtons.setLayout(new FlowLayout());
		Utils.addComponent(paneGlobal, jPanelButtons, 
				0, 3, 
				GridBagConstraints.REMAINDER, 1, 
				0, 0, 
				0, 0, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);

		btCancel = new javax.swing.JButton();
		btCancel.setIcon(Utils.getIcon(this, "cancel.png", settings.DialogIconSize));
		btCancel.setText(bundle.getString("Global.btCancel.text"));
		btCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setVisible(false);
			}
		});


		// -- Add buttons
		jPanelButtons.add(btCancel);

		// --
		pack();

		setLocationRelativeTo(null);
	}
	
	private void Search() {
		int targetTime = hsFinalTime.getHMSinSecond();
		int timeMin = 0;
		int timeMax = 0;
		int cmpt=0;
		
		TrackData track_calc = new TrackData(settings);
		track.CopyTo(track_calc);

		//-- Init display 
		lbFoundCurve1.setText("");
		lbFoundTime1Val.setText("");
		
		lbFoundCurve2.setText("");
		lbFoundTime2Val.setText("");
		progressBar.setValue(0);
		
		boolean found=false;
		
		//-- Search
		for (double speed=3.6; speed<=18; speed=speed+0.1) {
			String name = String.format("Run_%02d_%dkm_h", (int)speed, (int) ((speed-(int)speed)*10) );
			track_calc.Paramfile=name;
			track_calc.Calculate();

			//Found exact time		
			if (track_calc.TotalTime==targetTime) {
				strMin=track_calc.Paramfile;
				timeMin=track_calc.TotalTime;
				found=true;
				break;
			}
			else if (track_calc.TotalTime>targetTime) {
				strMax=track_calc.Paramfile;
				timeMax=track_calc.TotalTime;
			}
			else {
				if (!strMax.isEmpty()) {
					strMin=track_calc.Paramfile;
					timeMin=track_calc.TotalTime;
					found=true;
				}
				break;				
			}
			
			//Update progressbar
			cmpt++;
			progressBar.setValue(cmpt);
		} //for
		
		progressBar.setValue(NbCurves);
		
		if (found) {
			lbFoundCurve1.setText(strMin);
			lbFoundTime1Val.setText(Utils.Second2DateString(timeMin));
			
			lbFoundCurve2.setText(strMax);
			lbFoundTime2Val.setText(Utils.Second2DateString(timeMax));
		}
		else
			lbFoundCurve1.setText(bundle.getString("frmSearchCurve.StrNotFound"));
	}
	
}
