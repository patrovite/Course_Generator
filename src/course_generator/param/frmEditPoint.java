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

package course_generator.param;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import course_generator.utils.Utils;

public class frmEditPoint extends javax.swing.JDialog {
	private java.util.ResourceBundle bundle;
	private boolean ok;
	private CgParam param;
	private JLabel lbSlope;
	private JTextField tfSlope;
	private JLabel lbSpeed;
	private JTextField tfSpeed;
	private JPanel pnButtons;
	private JButton btCancel;
	private JButton btOk;
	private double slope;
	private double speed;
	
	/**
	 * Creates new form frmSettings
	 */
	public frmEditPoint() {
		super();
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		initComponents();
		setModal(true);
	}

	private void initComponents() {
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(bundle.getString("frmEditPoint.title"));
		setAlwaysOnTop(true);
		setResizable(false);
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

		//-- Slope
		lbSlope = new javax.swing.JLabel();
		lbSlope.setText(bundle.getString("frmEditPoint.lbSlope.text")+" ");
		Utils.addComponent(paneGlobal, lbSlope,
				0, 0, 
				1, 1, 
				0, 0, 
				10, 5, 5, 0, 
				GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);
		
		tfSlope = new JTextField();
		Utils.addComponent(paneGlobal, tfSlope,
				1, 0, 
				GridBagConstraints.REMAINDER, 1, 
				1, 0, 
				0, 5, 5, 10, 
				GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);
		
		//-- Speed
		lbSpeed = new javax.swing.JLabel();
		lbSpeed.setText(bundle.getString("frmEditPoint.lbSpeed.text")+" ");
		Utils.addComponent(paneGlobal, lbSpeed,
				0, 1, 
				1, 1, 
				0, 0, 
				0, 5, 5, 0, 
				GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);
		
		tfSpeed = new JTextField();
		Utils.addComponent(paneGlobal, tfSpeed,
				1, 1, 
				GridBagConstraints.REMAINDER, 1, 
				1, 0, 
				0, 5, 5, 10, 
				GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);
		
        //-- Create the panel for the bottom buttons ---------------------------
        pnButtons = new javax.swing.JPanel();
        pnButtons.setLayout(new FlowLayout());
        
        //----------------------------------------------------------------------
        btCancel = new javax.swing.JButton();
        btCancel.setText(bundle.getString("frmEditPoint.btCancel.text")); 
        btCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/cancel.png")));
        btCancel.addActionListener(new java.awt.event.ActionListener() {
        	public void actionPerformed(java.awt.event.ActionEvent evt) {
        		setVisible(false);
        	}
        });

        //----------------------------------------------------------------------
		btOk = new javax.swing.JButton();
		btOk.setText(bundle.getString("frmEditPoint.btOk.text")); 
		btOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/valid.png")));
		btOk.setMinimumSize(btCancel.getMinimumSize());
        btOk.setPreferredSize(btCancel.getPreferredSize());
		btOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				RequestToClose();
			}
		});

		//-- Add buttons to the buttons panel
        pnButtons.add(btOk);
        pnButtons.add(btCancel);

        //-- Add the buttons panel
        Utils.addComponent(paneGlobal, pnButtons, 
        		0, 2, 
        		GridBagConstraints.REMAINDER, 1, 
        		0, 1,
        		10, 0, 5, 5,
        		GridBagConstraints.PAGE_END, GridBagConstraints.NONE);

		// --
		pack();
		
		//-- Center the windows
		setLocationRelativeTo(null);
	}

	public boolean showDialog(CgParam p) {
		param=p;
		slope=p.Slope;
		speed=p.Speed;
		
		// Set field
		tfSlope.setText(String.valueOf(param.Slope));
		tfSpeed.setText(String.valueOf(param.Speed));
		// End set field
		ok = false;

		setVisible(true);

		if (ok) {
			// Copy fields
			p.Slope=slope;
			p.Speed=speed;
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

		slope = Utils.ParseDoubleEx(tfSlope.getText(), -1000.0);
		if ((slope>=-50) && (slope<=50))
			tfSlope.setBackground(Color.WHITE);
		else {
			tfSlope.setBackground(Color.MAGENTA);
			param_valid=false;
		}

		speed = Utils.ParseDoubleEx(tfSpeed.getText(), -1000.0);
		if ((speed>0) && (speed<100))
			tfSpeed.setBackground(Color.WHITE);
		else {
			tfSpeed.setBackground(Color.MAGENTA);
			param_valid=false;
		}
		
		// -- Ok?
		if (param_valid) {
			ok = true;
			setVisible(false);
		}
	}
	
	
    private void formComponentShown(java.awt.event.ComponentEvent evt) {
        repaint();
    }

}
