/*
 * Course Generator
 * Copyright (C) 2017 Pierre Delore
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

package course_generator.mrb;

import java.awt.Color;
import java.awt.Container;
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
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import course_generator.TrackData;
import course_generator.dialogs.FrmColorChooser;
import course_generator.utils.CgConst;
import course_generator.utils.CgSpinner;
import course_generator.utils.Utils;

public class FrmConfigMrb  extends javax.swing.JDialog {

	private ResourceBundle bundle;
	private boolean ok;
	private JPanel panelButtons;
	private JButton btCancel;
	private JButton btOk;
	private JPanel panelGeneral;
	private JTabbedPane TabbedPaneConfig;
	private JPanel panelSimple;
	private JPanel panelSlope;
	private JPanel panelRoadTrack;
//	private JLabel lbNbCharPerLine;
	private CgSpinner spinCharPerLine;
	private JLabel lbCurveFilter;
	private CgSpinner spinCurveFilter;
	private JLabel lbTopSize;
	private CgSpinner spinTopSize;
	private JLabel lbSimpleFillColor;
	private JLabel lbSimpleFillColorView;
	private JButton btSimpleFillColor;
	private JLabel lbSimplePenColor;
	private JLabel lbSimplePenColorView;
	private JButton btSimplePenColor;
	private JLabel lbSimpleEmpty;
	private JLabel lbRTTrackFillColor;
	private JLabel lbRTTrackFillColorView;
	private JButton btRTTrackFillColor;
	private JLabel lbRTRoadFillColor;
	private JLabel lbRTRoadFillColorView;
	private JButton btRTRoadFillColor;
	private JLabel lbRTPenColor;
	private JLabel lbRTPenColorView;
	private JButton btRTPenColor;
	private JLabel lbRTEmpty;
	private JLabel lbSlopeInf5;
	private JLabel lbSlopeInf5View;
	private JButton btSlopeInf5;
	private JLabel lbSlopeInf10;
	private JLabel lbSlopeInf10View;
	private JButton btSlopeInf10;
	private JLabel lbSlopeInf15;
	private JLabel lbSlopeInf15View;
	private JButton btSlopeInf15;
	private JLabel lbSlopeSup15;
	private JLabel lbSlopeSup15View;
	private JButton btSlopeSup15;
	private JLabel lbSlopePenColor;
	private JLabel lbSlopePenColorView;
	private JButton btSlopePenColor;
	private JLabel lbEmpty;
	private Color ColorSimpleFill;
	private Color ColorSimpleBorder;
	private Color ColorRSPath;
	private Color ColorRSRoad;
	private Color ColorRSBorder;
	private Color ColorSlopeInf5;
	private Color ColorSlopeInf10;
	private Color ColorSlopeInf15;
	private Color ColorSlopeSup15;
	private Color ColorSlopeBorder;

	/**
	 * Creates new form frmSettings
	 */
	public FrmConfigMrb() {
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		initComponents();
		setModal(true);
	}

	
	public boolean showDialog(TrackData track) {

//		spinCharPerLine.setValue(track.WordWrapLength);
		spinCurveFilter.setValue(track.CurveFilter);
		spinTopSize.setValue(track.TopMargin);
		
		ColorSimpleFill=track.clProfil_Simple_Fill;
		ColorSimpleBorder=track.clProfil_Simple_Border;
		
		ColorRSPath=track.clProfil_RS_Path;
		ColorRSRoad=track.clProfil_RS_Road;
		ColorRSBorder=track.clProfil_RS_Border;

		ColorSlopeInf5=track.clProfil_SlopeInf5;
		ColorSlopeInf10=track.clProfil_SlopeInf10;
		ColorSlopeInf15=track.clProfil_SlopeInf15;
		ColorSlopeSup15=track.clProfil_SlopeSup15;
		ColorSlopeBorder=track.clProfil_SlopeBorder;
		
		Refresh();
		
		// End set field
		ok = false;
		
		//-- Show the dialog
		setVisible(true);

		if (ok) {
			// Copy fields
//			track.WordWrapLength=spinCharPerLine.getValueAsInt();
			track.CurveFilter=spinCurveFilter.getValueAsInt();
			track.TopMargin=spinTopSize.getValueAsInt();

			track.clProfil_Simple_Fill=ColorSimpleFill;
			track.clProfil_Simple_Border=ColorSimpleBorder;

			track.clProfil_RS_Path=ColorRSPath;
			track.clProfil_RS_Road=ColorRSRoad;
			track.clProfil_RS_Border=ColorRSBorder;

			track.clProfil_SlopeInf5=ColorSlopeInf5;
			track.clProfil_SlopeInf10=ColorSlopeInf10;
			track.clProfil_SlopeInf15=ColorSlopeInf15;
			track.clProfil_SlopeSup15=ColorSlopeSup15;
			track.clProfil_SlopeBorder=ColorSlopeBorder;			
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

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setResizable(false);
		setType(java.awt.Window.Type.UTILITY);
		addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                repaint();
            }
        });


		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new GridBagLayout());

		// -- Tabbed panel
		// ------------------------------------------------------
		TabbedPaneConfig = new javax.swing.JTabbedPane();
		Utils.addComponent(paneGlobal, TabbedPaneConfig, 
				0, 0, 
				1, 1, 
				1, 1, 
				10, 10, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		
		//== TAB : panel general config
		panelGeneral = new JPanel();
		panelGeneral.setLayout(new GridBagLayout());
		TabbedPaneConfig.add(panelGeneral, bundle.getString("FrmConfigMrb.panelGeneral.tabTitle"));

		//-- Content of the panel
//		lbNbCharPerLine = new JLabel(bundle.getString("FrmConfigMrb.lbNbCharPerLine.text"));		
//		Utils.addComponent(panelGeneral, lbNbCharPerLine, 
//				0, 0, 
//				1, 1, 
//				0, 0, 
//				10, 10, 0, 0, GridBagConstraints.WEST,
//				GridBagConstraints.BOTH);
//		
//		spinCharPerLine = new CgSpinner(25,1,100,1);
//		Utils.addComponent(panelGeneral, spinCharPerLine, 
//				1, 0, 
//				1, 1, 
//				1, 0, 
//				10, 10, 0, 10, 
//				GridBagConstraints.WEST, GridBagConstraints.BOTH);

		//--
		lbCurveFilter = new JLabel(bundle.getString("FrmConfigMrb.lbCurveFilter.text"));		
		Utils.addComponent(panelGeneral, lbCurveFilter, 
				0, 0, 
				1, 1, 
				0, 0, 
				5, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		spinCurveFilter = new CgSpinner(1,1,10,1);
		Utils.addComponent(panelGeneral, spinCurveFilter, 
				1, 0, 
				1, 1, 
				1, 0, 
				5, 10, 0, 10, 
				GridBagConstraints.WEST, GridBagConstraints.BOTH);
		
		//--
		lbTopSize = new JLabel(bundle.getString("FrmConfigMrb.lbTopSize.text"));		
		Utils.addComponent(panelGeneral, lbTopSize, 
				0, 1, 
				1, 1, 
				0, 0, 
				5, 10, 10, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		spinTopSize = new CgSpinner(100,1,1000,1);
		Utils.addComponent(panelGeneral, spinTopSize, 
				1, 1, 
				1, 1, 
				1, 0, 
				5, 10, 10, 10, 
				GridBagConstraints.WEST, GridBagConstraints.BOTH);

		//-- Empty line for resize purpose (not the best solution but it's simple ;) )
		lbEmpty = new JLabel();		
		Utils.addComponent(panelGeneral, lbEmpty, 
				0, 2, 
				1, 1, 
				0, 1, 
				10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		
		//== TAB :  panel simple graph config
		panelSimple = new JPanel();
		panelSimple.setLayout(new GridBagLayout());
		TabbedPaneConfig.add(panelSimple, bundle.getString("FrmConfigMrb.panelSimple.tabTitle"));

		//-- Content of the panel
		lbSimpleFillColor = new JLabel(" "+bundle.getString("FrmConfigMrb.lbSimpleFillColor.text")+" ");	
		Utils.addComponent(panelSimple, lbSimpleFillColor, 
				0, 0, 
				1, 1, 
				1, 0, 
				10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		lbSimpleFillColorView = new JLabel("          ");		
		lbSimpleFillColorView.setBorder(BorderFactory.createLineBorder(Color.black));
		lbSimpleFillColorView.setOpaque(true);
		Utils.addComponent(panelSimple, lbSimpleFillColorView, 
				1, 0, 
				1, 1, 
				0, 0, 
				10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		btSimpleFillColor = new JButton(bundle.getString("FrmConfigMrb.btColor.text"));
		btSimpleFillColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ColorSimpleFill=FrmColorChooser.showDialog("", ColorSimpleFill);
				Refresh();
			}
		});
		Utils.addComponent(panelSimple, btSimpleFillColor, 
				2, 0, 
				1, 1, 
				0, 0, 
				10, 10, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		//--
		lbSimplePenColor = new JLabel(" "+bundle.getString("FrmConfigMrb.lbSimplePenColor.text")+" ");	
		Utils.addComponent(panelSimple, lbSimplePenColor, 
				0, 1, 
				1, 1, 
				1, 0, 
				10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		lbSimplePenColorView = new JLabel("          ");		
		lbSimplePenColorView.setBorder(BorderFactory.createLineBorder(Color.black));
		lbSimplePenColorView.setOpaque(true);
		Utils.addComponent(panelSimple, lbSimplePenColorView, 
				1, 1, 
				1, 1, 
				0, 0, 
				10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		btSimplePenColor = new JButton(bundle.getString("FrmConfigMrb.btColor.text"));
		btSimplePenColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ColorSimpleBorder=FrmColorChooser.showDialog("", ColorSimpleBorder);
				Refresh();
			}
		});
		Utils.addComponent(panelSimple, btSimplePenColor, 
				2, 1, 
				1, 1, 
				0, 0, 
				10, 10, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		//-- Empty line for resize purpose (not the best solution but it's simple ;) )
		lbSimpleEmpty = new JLabel();		
		Utils.addComponent(panelSimple, lbSimpleEmpty, 
				0, 2, 
				1, 1, 
				0, 1, 
				10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		
		//== TAB :  panel road/track graph config
		panelRoadTrack = new JPanel();
		panelRoadTrack.setLayout(new GridBagLayout());
		TabbedPaneConfig.add(panelRoadTrack, bundle.getString("FrmConfigMrb.panelRoadTrack.tabTitle"));

		//-- Content of the panel
		lbRTTrackFillColor = new JLabel(" "+bundle.getString("FrmConfigMrb.lbRTTrackFillColor.text")+" ");	
		Utils.addComponent(panelRoadTrack, lbRTTrackFillColor, 
				0, 0, 
				1, 1, 
				1, 0, 
				10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		lbRTTrackFillColorView = new JLabel("          ");		
		lbRTTrackFillColorView.setBorder(BorderFactory.createLineBorder(Color.black));
		lbRTTrackFillColorView.setOpaque(true);
		Utils.addComponent(panelRoadTrack, lbRTTrackFillColorView, 
				1, 0, 
				1, 1, 
				0, 0, 
				10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		btRTTrackFillColor = new JButton(bundle.getString("FrmConfigMrb.btColor.text"));
		btRTTrackFillColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ColorRSPath=FrmColorChooser.showDialog("", ColorRSPath);
				Refresh();
			}
		});
		Utils.addComponent(panelRoadTrack, btRTTrackFillColor, 
				2, 0, 
				1, 1, 
				0, 0, 
				10, 10, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		//--
		lbRTRoadFillColor = new JLabel(" "+bundle.getString("FrmConfigMrb.lbRTRoadFillColor.text")+" ");	
		Utils.addComponent(panelRoadTrack, lbRTRoadFillColor, 
				0, 1, 
				1, 1, 
				1, 0, 
				10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		lbRTRoadFillColorView = new JLabel("          ");		
		lbRTRoadFillColorView.setBorder(BorderFactory.createLineBorder(Color.black));
		lbRTRoadFillColorView.setOpaque(true);
		Utils.addComponent(panelRoadTrack, lbRTRoadFillColorView, 
				1, 1, 
				1, 1, 
				0, 0, 
				10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		btRTRoadFillColor = new JButton(bundle.getString("FrmConfigMrb.btColor.text"));
		btRTRoadFillColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ColorRSRoad=FrmColorChooser.showDialog("", ColorRSRoad);
				Refresh();
			}
		});
		Utils.addComponent(panelRoadTrack, btRTRoadFillColor, 
				2, 1, 
				1, 1, 
				0, 0, 
				10, 10, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		//--
		lbRTPenColor = new JLabel(" "+bundle.getString("FrmConfigMrb.lbRTPenColor.text")+" ");	
		Utils.addComponent(panelRoadTrack, lbRTPenColor, 
				0, 2, 
				1, 1, 
				1, 0, 
				10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		lbRTPenColorView = new JLabel("          ");		
		lbRTPenColorView.setBorder(BorderFactory.createLineBorder(Color.black));
		lbRTPenColorView.setOpaque(true);
		Utils.addComponent(panelRoadTrack, lbRTPenColorView, 
				1, 2, 
				1, 1, 
				0, 0, 
				10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		btRTPenColor = new JButton(bundle.getString("FrmConfigMrb.btColor.text"));
		btRTPenColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ColorRSBorder=FrmColorChooser.showDialog("", ColorRSBorder);
				Refresh();
			}
		});
		Utils.addComponent(panelRoadTrack, btRTPenColor, 
				2, 2, 
				1, 1, 
				0, 0, 
				10, 10, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		//-- Empty line for resize purpose (not the best solution but it's simple ;) )
		lbRTEmpty = new JLabel();		
		Utils.addComponent(panelRoadTrack, lbRTEmpty, 
				0, 3, 
				1, 1, 
				0, 1, 
				10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		
		//== TAB :  panel slope graph config
		panelSlope = new JPanel();
		panelSlope.setLayout(new GridBagLayout());
		TabbedPaneConfig.add(panelSlope, bundle.getString("FrmConfigMrb.panelSlope.tabTitle"));
		
		//-- Content of the panel
		lbSlopeInf5 = new JLabel(" "+bundle.getString("FrmConfigMrb.lbSlopeInf5.text")+" ");	
		Utils.addComponent(panelSlope, lbSlopeInf5, 
				0, 0, 
				1, 1, 
				1, 0, 
				10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		lbSlopeInf5View = new JLabel("          ");		
		lbSlopeInf5View.setBorder(BorderFactory.createLineBorder(Color.black));
		lbSlopeInf5View.setOpaque(true);
		Utils.addComponent(panelSlope, lbSlopeInf5View, 
				1, 0, 
				1, 1, 
				0, 0, 
				10, 0, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		btSlopeInf5 = new JButton(bundle.getString("FrmConfigMrb.btColor.text"));
		btSlopeInf5.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ColorSlopeInf5=FrmColorChooser.showDialog("", ColorSlopeInf5);
				Refresh();
			}
		});
		Utils.addComponent(panelSlope, btSlopeInf5, 
				2, 0, 
				1, 1, 
				0, 0, 
				10, 10, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		//--
		lbSlopeInf10 = new JLabel(" "+bundle.getString("FrmConfigMrb.lbSlopeInf10.text")+" ");	
		Utils.addComponent(panelSlope, lbSlopeInf10, 
				0, 1, 
				1, 1, 
				1, 0, 
				10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		lbSlopeInf10View = new JLabel("          ");		
		lbSlopeInf10View.setBorder(BorderFactory.createLineBorder(Color.black));
		lbSlopeInf10View.setOpaque(true);
		Utils.addComponent(panelSlope, lbSlopeInf10View, 
				1, 1, 
				1, 1, 
				0, 0, 
				10, 0, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		btSlopeInf10 = new JButton(bundle.getString("FrmConfigMrb.btColor.text"));
		btSlopeInf10.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ColorSlopeInf10=FrmColorChooser.showDialog("", ColorSlopeInf10);
				Refresh();
			}
		});
		Utils.addComponent(panelSlope, btSlopeInf10, 
				2, 1, 
				1, 1, 
				0, 0, 
				10, 10, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		//--
		lbSlopeInf15 = new JLabel(" "+bundle.getString("FrmConfigMrb.lbSlopeInf15.text")+" ");	
		Utils.addComponent(panelSlope, lbSlopeInf15, 
				0, 2, 
				1, 1, 
				1, 0, 
				10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		lbSlopeInf15View = new JLabel("          ");		
		lbSlopeInf15View.setBorder(BorderFactory.createLineBorder(Color.black));
		lbSlopeInf15View.setOpaque(true);
		Utils.addComponent(panelSlope, lbSlopeInf15View, 
				1, 2, 
				1, 1, 
				0, 0, 
				10, 0, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		btSlopeInf15 = new JButton(bundle.getString("FrmConfigMrb.btColor.text"));
		btSlopeInf15.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ColorSlopeInf15=FrmColorChooser.showDialog("", ColorSlopeInf15);
				Refresh();
			}
		});
		Utils.addComponent(panelSlope, btSlopeInf15, 
				2, 2, 
				1, 1, 
				0, 0, 
				10, 10, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		//--
		lbSlopeSup15 = new JLabel(" "+bundle.getString("FrmConfigMrb.lbSlopeSup15.text")+" ");	
		Utils.addComponent(panelSlope, lbSlopeSup15, 
				0, 3, 
				1, 1, 
				1, 0, 
				10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		lbSlopeSup15View = new JLabel("          ");		
		lbSlopeSup15View.setBorder(BorderFactory.createLineBorder(Color.black));
		lbSlopeSup15View.setOpaque(true);
		Utils.addComponent(panelSlope, lbSlopeSup15View, 
				1, 3, 
				1, 1, 
				0, 0, 
				10, 0, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		btSlopeSup15 = new JButton(bundle.getString("FrmConfigMrb.btColor.text"));
		btSlopeSup15.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ColorSlopeSup15=FrmColorChooser.showDialog("", ColorSlopeSup15);
				Refresh();
			}
		});
		Utils.addComponent(panelSlope, btSlopeSup15, 
				2, 3, 
				1, 1, 
				0, 0, 
				10, 10, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		//--
		lbSlopePenColor = new JLabel(" "+bundle.getString("FrmConfigMrb.lbSlopePenColor.text")+" ");	
		Utils.addComponent(panelSlope, lbSlopePenColor, 
				0, 4, 
				1, 1, 
				1, 0, 
				10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		lbSlopePenColorView = new JLabel("          ");		
		lbSlopePenColorView.setBorder(BorderFactory.createLineBorder(Color.black));
		lbSlopePenColorView.setOpaque(true);
		Utils.addComponent(panelSlope, lbSlopePenColorView, 
				1, 4, 
				1, 1, 
				0, 0, 
				10, 0, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		btSlopePenColor = new JButton(bundle.getString("FrmConfigMrb.btColor.text"));
		btSlopePenColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ColorSlopeBorder=FrmColorChooser.showDialog("", ColorSlopeBorder);
				Refresh();
			}
		});
		Utils.addComponent(panelSlope, btSlopePenColor, 
				2, 4, 
				1, 1, 
				0, 0, 
				10, 10, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		//-- Empty line for resize purpose (not the best solution but it's simple ;) )
		lbRTEmpty = new JLabel();		
		Utils.addComponent(panelSlope, lbRTEmpty, 
				0, 5, 
				1, 1, 
				0, 1, 
				10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);
		
		
		// == BUTTONS
		// ===========================================================
		panelButtons = new javax.swing.JPanel();
		panelButtons.setLayout(new FlowLayout());
		Utils.addComponent(paneGlobal, panelButtons, 
				0, 1, 
				1, 1, 
				1, 0, 
				0, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		btCancel = new javax.swing.JButton();
		btCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/cancel.png")));
		btCancel.setText(bundle.getString("FrmConfigMrb.btCancel.text"));
		btCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setVisible(false);
			}
		});

		btOk = new javax.swing.JButton();
		btOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/valid.png")));
		btOk.setText(bundle.getString("FrmConfigMrb.btOk.text"));
		btOk.setMinimumSize(btCancel.getMinimumSize());
		btOk.setPreferredSize(btCancel.getPreferredSize());
		btOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				RequestToClose();
			}
		});

		// -- Add buttons
		panelButtons.add(btOk);
		panelButtons.add(btCancel);

		// --
		pack();

		setLocationRelativeTo(null);
	}

	private void Refresh() {
		lbSimpleFillColorView.setBackground(ColorSimpleFill);
		lbSimplePenColorView.setBackground(ColorSimpleBorder);

		lbRTTrackFillColorView.setBackground(ColorRSPath);
		lbRTRoadFillColorView.setBackground(ColorRSRoad);
		lbRTPenColorView.setBackground(ColorRSBorder);
		
		lbSlopeInf5View.setBackground(ColorSlopeInf5);
		lbSlopeInf10View.setBackground(ColorSlopeInf10);
		lbSlopeInf15View.setBackground(ColorSlopeInf15);
		lbSlopeSup15View.setBackground(ColorSlopeSup15);
		lbSlopePenColorView.setBackground(ColorSlopeBorder);
	}
	
}
