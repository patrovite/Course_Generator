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
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import course_generator.TrackData;
import course_generator.settings.CgSettings;
import course_generator.utils.CgSpinner;
import course_generator.utils.CgSpinnerDouble;
import course_generator.utils.Utils;

public class frmFillCoeff  extends javax.swing.JDialog {

	private ResourceBundle bundle;
	private boolean ok;
	private int start;
	private int end;
	private double estimatedCoeff;
	private CgSettings settings;
	private JPanel jPanelButtons;
	private JButton btCancel;
	private JButton btOk;
	private TrackData track;
//	private CgData data;
	private JPanel panelStart;
	private JPanel panelEnd;
	private JPanel panelHelp;
	private JRadioButton rbFromStart;
	private JRadioButton rbFromLine;
	private ButtonGroup groupStart;
	private JRadioButton rbToEnd;
	private JRadioButton rbToLine;
	private ButtonGroup groupEnd;
//	private ButtonGroup groupDiff;
	private CgSpinner spinFromLine;
	private CgSpinner spinToLine;
//	private CgSpinner spinDiff;
	private JLabel lbFromValue;
	private CgSpinnerDouble spinFromValue;
	private JLabel lbToValue;
	private CgSpinnerDouble spinToValue;
	private JLabel lbEstimateTime;
	private JLabel lbHour;
	private CgSpinner spinHour;
	private JLabel lbMinute;
	private CgSpinner spinMinute;
	private JButton btCalc;
	private JLabel lbResultCoeff;
	private JLabel lbResultCoeffVal;
	private JButton btCopyToEnd;
	private JButton btCopyToStart;
//	private JTextField tfResultCoeffVal;
	private JPanel panelEstimateTime;
	private JPanel panelResult;

	
	public class EditCoeffResult {
		public int Start; //Start line 
		public int End; //End line 
		public double Start_Coeff; //Initial coefficient
		public double End_Coeff; //Finale coefficient
		public boolean Valid; //Indicate if the ok was pressed 
	}
	
	/**
	 * Creates new form frmSettings
	 */
	public frmFillCoeff() {
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		estimatedCoeff=100;
		initComponents();
		setModal(true);
	}

	public EditCoeffResult showDialog(CgSettings settings, TrackData track, int start_line, int end_line) {
		this.settings = settings;
		this.track = track;

		if (start_line==-1) {
			this.start=1;
			this.end=1;
		}
		else if (end_line==-1) {
			this.start = start_line+1;
			this.end=this.start;
		}
		else {
			this.start = start_line+1;
			this.end=end_line;			
		}

		// Set field
		spinFromLine.setValue(start);
		spinFromLine.setMaximum(track.data.size());
		spinToLine.setValue(end);
		spinToLine.setMaximum(track.data.size());
		
		// End set field
		ok = false;

		//-- Update the display
		Refresh();
		
		//-- Show the dialog
		setVisible(true);

		EditCoeffResult res = new EditCoeffResult();
		res.Valid=ok;
		
		if (ok) {
			// Copy fields
			if (rbFromStart.isSelected())
				res.Start=0;
			else 
				res.Start=spinFromLine.getValueAsInt()-1;
			
			res.Start_Coeff=spinFromValue.getValueAsDouble();
			
			if (rbToEnd.isSelected())
				res.End=track.data.size()-1;
			else
				res.End=spinToLine.getValueAsInt()-1;
			
			res.End_Coeff=spinToValue.getValueAsDouble();
			
			res.Start_Coeff=spinFromValue.getValueAsDouble();
			res.End_Coeff=spinToValue.getValueAsDouble();
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
		int line = 0;

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(bundle.getString("frmFillCoeff.title"));
		setAlwaysOnTop(true);
		setResizable(false);
		setMinimumSize(new Dimension(300,400));
		setType(java.awt.Window.Type.UTILITY);

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new GridBagLayout());

		//== Panel start
		panelStart = new JPanel();
		panelStart.setLayout(new GridBagLayout());
		panelStart.setBorder(BorderFactory.createTitledBorder(bundle.getString("frmFillCoeff.panelStart.Title"))); //Start
		panelStart.setLayout(new GridBagLayout());
		Utils.addComponent(paneGlobal, panelStart, 
				0, 0, 
				1, 1, 
				0, 0, 
				10, 10, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		rbFromStart = new JRadioButton(bundle.getString("frmFillCoeff.rbFromStart.Text")); //From the start
		rbFromStart.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Refresh();
			}
		});
		Utils.addComponent(panelStart, rbFromStart, 
				0, 0, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		rbFromLine = new JRadioButton(bundle.getString("frmFillCoeff.rbFromLine.Text")); //From line
		rbFromLine.setSelected(true);
		rbFromLine.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Refresh();
			}
		});
		Utils.addComponent(panelStart, rbFromLine, 
				0, 1, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		 groupStart = new ButtonGroup();
		 groupStart.add(rbFromStart);
		 groupStart.add(rbFromLine);
		 
		 spinFromLine = new CgSpinner(100,1,100,1);
		 Utils.addComponent(panelStart, spinFromLine, 
				1, 1, 
				1, 1, 
				1, 0, 
				5, 5, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		 
		 lbFromValue = new JLabel(bundle.getString("frmFillCoeff.lbFromValue.Text"));
		 Utils.addComponent(panelStart, lbFromValue, 
				0, 2, 
				1, 1, 
				1, 0, 
				5, 5, 5, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);
		 
		 spinFromValue = new CgSpinnerDouble(100.0, 0.1,200.0, 0.1);
		 Utils.addComponent(panelStart, spinFromValue, 
					1, 2, 
					1, 1, 
					1, 0, 
					5, 5, 5, 5, 
					GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		//== Panel end
		panelEnd = new JPanel();
		panelEnd.setLayout(new GridBagLayout());
		panelEnd.setBorder(BorderFactory.createTitledBorder(bundle.getString("frmFillCoeff.panelEnd.Title")));//End
		panelEnd.setLayout(new GridBagLayout());
		Utils.addComponent(paneGlobal, panelEnd, 
				0, 1, 
				1, 1, 
				0, 0, 
				10, 10, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		rbToEnd = new JRadioButton(bundle.getString("frmFillCoeff.rbToEnd.Text")); //To the end
		rbToEnd.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Refresh();
			}
		});
		Utils.addComponent(panelEnd, rbToEnd, 
				0, 0, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		rbToLine = new JRadioButton(bundle.getString("frmFillCoeff.rbToLine.Text"));//To line
		rbToLine.setSelected(true);
		rbToLine.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Refresh();
			}
		});
		Utils.addComponent(panelEnd, rbToLine, 
				0, 1, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		groupEnd = new ButtonGroup();
		groupEnd.add(rbToEnd);
		groupEnd.add(rbToLine);

		spinToLine = new CgSpinner(100,1,100,1);
		Utils.addComponent(panelEnd, spinToLine, 
				1, 1, 
				1, 1, 
				1, 0, 
				5, 5, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		lbToValue = new JLabel(bundle.getString("frmFillCoeff.lbToValue.Text"));
		Utils.addComponent(panelEnd, lbToValue, 
				0, 2, 
				1, 1, 
				1, 0, 
				5, 5, 5, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);
		 
		spinToValue = new CgSpinnerDouble(100.0, 0.1,200.0, 0.1);
		Utils.addComponent(panelEnd, spinToValue, 
					1, 2, 
					1, 1, 
					1, 0, 
					5, 5, 5, 5, 
					GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		//== Panel help
		panelHelp = new JPanel();
		panelHelp.setLayout(new GridBagLayout());
		panelHelp.setBorder(BorderFactory.createTitledBorder(bundle.getString("frmFillCoeff.panelHelp.Title")));//Difficulty
		Utils.addComponent(paneGlobal, panelHelp, 
				0, 2, 
				1, 1, 
				1, 1, 
				10, 10, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		
		panelEstimateTime = new JPanel();
		panelEstimateTime.setLayout(new GridBagLayout());
		Utils.addComponent(panelHelp, panelEstimateTime, 
				0, 0, 
				1, 1, 
				1, 0, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		lbEstimateTime = new JLabel(bundle.getString("frmFillCoeff.lbEstimateTime.Text"));
		Utils.addComponent(panelEstimateTime, lbEstimateTime, 
				0, 0, 
				GridBagConstraints.REMAINDER, 1, 
				1, 0, 
				5, 5, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);
		
		lbHour = new JLabel(bundle.getString("frmFillCoeff.lbHour.Text"));
		Utils.addComponent(panelEstimateTime, lbHour, 
				0, 1, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		spinHour = new CgSpinner(0,0,23,1);
		Utils.addComponent(panelEstimateTime, spinHour, 
				1, 1, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		lbMinute = new JLabel(bundle.getString("frmFillCoeff.lbMinute.Text"));
		Utils.addComponent(panelEstimateTime, lbMinute, 
				2, 1, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		spinMinute = new CgSpinner(0,0,59,1);
		Utils.addComponent(panelEstimateTime, spinMinute, 
				3, 1, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		btCalc = new javax.swing.JButton(bundle.getString("frmFillCoeff.btCalc.text"));
		btCalc.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Calc();
				Refresh();
			}
		});
		Utils.addComponent(panelEstimateTime, btCalc, 
				4, 1, 
				1, 1, 
				1, 0, 
				5, 5, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);
		//--
		panelResult = new JPanel();
		panelResult.setLayout(new GridBagLayout());
		Utils.addComponent(panelHelp, panelResult, 
				0, 1, 
				1, 1, 
				1, 1, 
				10, 0, 10, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		lbResultCoeff = new JLabel(bundle.getString("frmFillCoeff.lbResultCoeff.Text"));
		Utils.addComponent(panelResult, lbResultCoeff, 
				0, 1, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);
		
		lbResultCoeffVal = new JLabel("0.0",JLabel.CENTER);
		lbResultCoeffVal.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		lbResultCoeffVal.setOpaque(true);
		lbResultCoeffVal.setBackground(Color.WHITE);
		lbResultCoeffVal.setPreferredSize(new Dimension(60, 20));
		Utils.addComponent(panelResult, lbResultCoeffVal, 
				1, 1, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);
		
		btCopyToStart = new javax.swing.JButton(bundle.getString("frmFillCoeff.btCopyToStart.text"));
		btCopyToStart.setToolTipText(bundle.getString("frmFillCoeff.btCopyToStart.toolTipText"));
		btCopyToStart.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				spinFromValue.setValue(estimatedCoeff);
			}
		});
		Utils.addComponent(panelResult, btCopyToStart, 
				2, 1, 
				1, 1, 
				0, 0, 
				5, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		btCopyToEnd = new javax.swing.JButton(bundle.getString("frmFillCoeff.btCopyToEnd.text"));
		btCopyToEnd.setToolTipText(bundle.getString("frmFillCoeff.btCopyToEnd.toolTipText"));
		btCopyToEnd.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				spinToValue.setValue(estimatedCoeff);
			}
		});
		Utils.addComponent(panelResult, btCopyToEnd, 
				3, 1, 
				1, 1, 
				0, 0, 
				5, 5, 0, 5, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		
		// == BUTTONS
		// ===========================================================
		jPanelButtons = new javax.swing.JPanel();
		jPanelButtons.setLayout(new FlowLayout());
		Utils.addComponent(paneGlobal, jPanelButtons, 
				0, 3, 
				1, 1, 
				0, 0, 
				0, 0, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);

		btCancel = new javax.swing.JButton();
		btCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/cancel.png")));
		btCancel.setText(bundle.getString("frmFillCoeff.btCancel.text"));
		btCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setVisible(false);
			}
		});

		btOk = new javax.swing.JButton();
		btOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/valid.png")));
		btOk.setText(bundle.getString("frmFillCoeff.btOk.text"));
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

	private void Calc() {
		double t = spinHour.getValueAsDouble() * 3600 + spinMinute.getValueAsDouble() * 60;
	    if (t > 5400)
	    	estimatedCoeff = (-0.00000315 * t + 1.01701) * 100;
	    else 
	    	estimatedCoeff = 100;
	}
	
	protected void Refresh() {
		spinFromLine.setEnabled(rbFromLine.isSelected());
		spinToLine.setEnabled(rbToLine.isSelected());
		lbResultCoeffVal.setText(String.format("%1.1f ",estimatedCoeff));
	}
	
}
