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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import course_generator.TrackData;
import course_generator.dialogs.FrmColorChooser;
import course_generator.settings.CgSettings;
import course_generator.utils.CgConst;
import course_generator.utils.CgSpinner;
import course_generator.utils.JTextFieldLimit;
import course_generator.utils.Utils;

public class FrmConfigMrb extends javax.swing.JDialog {

	private static final long serialVersionUID = -3560107066619391286L;
	private ResourceBundle bundle;
	private CgSettings settings;
	private boolean ok;
	private JPanel panelButtons;
	private JButton btCancel;
	private JButton btOk;
	private JPanel panelGeneral;
	private JTabbedPane TabbedPaneConfig;
	private JPanel panelSimple;
	private JPanel panelSlope;
	private JPanel panelRoadTrack;
	// private JLabel lbNbCharPerLine;
	private JLabel lbCurveFilter;
	private CgSpinner spinCurveFilter;
	private JLabel lbTopSize;
	private CgSpinner spinTopSize;
	private JLabel lbSimpleFillColor;
	private JLabel lbSimpleFillColorView;
	private JLabel lbSimplePenColor;
	private JLabel lbSimplePenColorView;
	private JLabel lbSimpleEmpty;
	private JLabel lbRTTrackFillColor;
	private JLabel lbRTTrackFillColorView;
	private JLabel lbRTRoadFillColor;
	private JLabel lbRTRoadFillColorView;
	private JLabel lbRTPenColor;
	private JLabel lbRTPenColorView;
	private JLabel lbRTEmpty;
	private JLabel lbSlopeInf5;
	private JLabel lbSlopeInf5View;
	private JLabel lbSlopeInf10;
	private JLabel lbSlopeInf10View;
	private JLabel lbSlopeInf15;
	private JLabel lbSlopeInf15View;
	private JLabel lbSlopeSup15;
	private JLabel lbSlopeSup15View;
	private JLabel lbSlopePenColor;
	private JLabel lbSlopePenColorView;
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
	private JButton btSimpleDefaultColor;
	private JButton btRTDefaultColor;
	private JButton btSlopeDefaultColor;
	private JLabel lbDefaultFormat;
	private JTextFieldLimit tfDefaultFormat;
	private JLabel lbMrbDefWidth;
	private CgSpinner spinMrbDefWidth;
	private JLabel lbMrbDefHeight;
	private CgSpinner spinMrbDefHeight;

	/**
	 * Creates new form frmSettings
	 */
	public FrmConfigMrb(Window parent, CgSettings settings) {
		super(parent);
		this.settings = settings;
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		initComponents();
		setModal(true);
	}

	public boolean showDialog(TrackData track) {

		// spinCharPerLine.setValue(track.WordWrapLength);
		spinCurveFilter.setValue(track.CurveFilter);
		spinMrbDefWidth.setValue(settings.DefMrbWidth);
		spinMrbDefHeight.setValue(settings.DefMrbHeight);

		spinTopSize.setValue(track.TopMargin);
		tfDefaultFormat.setText(settings.DefaultFormat);

		ColorSimpleFill = track.clProfil_Simple_Fill;
		ColorSimpleBorder = track.clProfil_Simple_Border;

		ColorRSPath = track.clProfil_RS_Path;
		ColorRSRoad = track.clProfil_RS_Road;
		ColorRSBorder = track.clProfil_RS_Border;

		ColorSlopeInf5 = track.clProfil_SlopeInf5;
		ColorSlopeInf10 = track.clProfil_SlopeInf10;
		ColorSlopeInf15 = track.clProfil_SlopeInf15;
		ColorSlopeSup15 = track.clProfil_SlopeSup15;
		ColorSlopeBorder = track.clProfil_SlopeBorder;

		Refresh();

		// End set field
		ok = false;

		// -- Show the dialog
		setVisible(true);

		if (ok) {
			// Copy fields
			// track.WordWrapLength=spinCharPerLine.getValueAsInt();
			track.CurveFilter = spinCurveFilter.getValueAsInt();
			track.TopMargin = spinTopSize.getValueAsInt();
			settings.DefMrbWidth = spinMrbDefWidth.getValueAsInt();
			settings.DefMrbHeight = spinMrbDefHeight.getValueAsInt();

			settings.DefaultFormat = tfDefaultFormat.getText();

			track.clProfil_Simple_Fill = ColorSimpleFill;
			track.clProfil_Simple_Border = ColorSimpleBorder;

			track.clProfil_RS_Path = ColorRSPath;
			track.clProfil_RS_Road = ColorRSRoad;
			track.clProfil_RS_Border = ColorRSBorder;

			track.clProfil_SlopeInf5 = ColorSlopeInf5;
			track.clProfil_SlopeInf10 = ColorSlopeInf10;
			track.clProfil_SlopeInf15 = ColorSlopeInf15;
			track.clProfil_SlopeSup15 = ColorSlopeSup15;
			track.clProfil_SlopeBorder = ColorSlopeBorder;
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
		Utils.addComponent(paneGlobal, TabbedPaneConfig, 0, 0, 1, 1, 1, 1, 10, 10, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		// == TAB : panel general config
		panelGeneral = new JPanel();
		panelGeneral.setLayout(new GridBagLayout());
		TabbedPaneConfig.add(panelGeneral, bundle.getString("FrmConfigMrb.panelGeneral.tabTitle"));

		// --
		lbCurveFilter = new JLabel(bundle.getString("FrmConfigMrb.lbCurveFilter.text"));
		Utils.addComponent(panelGeneral, lbCurveFilter, 0, 0, 1, 1, 0, 0, 5, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		spinCurveFilter = new CgSpinner(1, 1, 10, 1);
		Utils.addComponent(panelGeneral, spinCurveFilter, 1, 0, 1, 1, 1, 0, 5, 10, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		// --
		lbTopSize = new JLabel(bundle.getString("FrmConfigMrb.lbTopSize.text"));
		Utils.addComponent(panelGeneral, lbTopSize, 0, 1, 1, 1, 0, 0, 5, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		spinTopSize = new CgSpinner(100, 1, 1000, 1);
		Utils.addComponent(panelGeneral, spinTopSize, 1, 1, 1, 1, 1, 0, 5, 10, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		// --
		lbDefaultFormat = new JLabel(bundle.getString("FrmConfigMrb.lbDefaultFormat.Text")); // "FrmConfigMrb.lbTopSize.text"));
		Utils.addComponent(panelGeneral, lbDefaultFormat, 0, 2, 1, 1, 0, 0, 5, 10, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		tfDefaultFormat = new JTextFieldLimit(200);
		tfDefaultFormat.setPreferredSize(new Dimension(200, 25));
		Utils.addComponent(panelGeneral, tfDefaultFormat, 1, 2, 1, 1, 1, 0, 5, 10, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		// --
		lbMrbDefWidth = new JLabel(bundle.getString("FrmConfigMrb.lbMrbDefWidth.text"));
		Utils.addComponent(panelGeneral, lbMrbDefWidth, 0, 3, 1, 1, 0, 0, 5, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		spinMrbDefWidth = new CgSpinner(1000, 1, 4000, 1);
		Utils.addComponent(panelGeneral, spinMrbDefWidth, 1, 3, 1, 1, 1, 0, 5, 10, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		// --
		lbMrbDefHeight = new JLabel(bundle.getString("FrmConfigMrb.lbMrbDefHeight.text"));
		Utils.addComponent(panelGeneral, lbMrbDefHeight, 0, 4, 1, 1, 0, 0, 5, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		spinMrbDefHeight = new CgSpinner(480, 1, 2000, 1);
		Utils.addComponent(panelGeneral, spinMrbDefHeight, 1, 4, 1, 1, 1, 0, 5, 10, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		// -- Empty line for resize purpose (not the best solution but it's simple ;) )
		lbEmpty = new JLabel();
		Utils.addComponent(panelGeneral, lbEmpty, 0, 5, 1, 1, 0, 1, 10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		// == TAB : panel simple graph config
		panelSimple = new JPanel();
		panelSimple.setLayout(new GridBagLayout());
		TabbedPaneConfig.add(panelSimple, bundle.getString("FrmConfigMrb.panelSimple.tabTitle"));

		// -- Content of the panel
		lbSimpleFillColor = new JLabel(" " + bundle.getString("FrmConfigMrb.lbSimpleFillColor.text") + " ");
		Utils.addComponent(panelSimple, lbSimpleFillColor, 0, 0, 1, 1, 1, 0, 10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		lbSimpleFillColorView = new JLabel("          ");
		lbSimpleFillColorView.setBorder(BorderFactory.createLineBorder(Color.black));
		lbSimpleFillColorView.setOpaque(true);
		lbSimpleFillColorView.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColorSimpleFill = ChooseColor(ColorSimpleFill);
				Refresh();
			}
		});
		Utils.addComponent(panelSimple, lbSimpleFillColorView, 2, 0, 1, 1, 0, 0, 10, 10, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		// --
		lbSimplePenColor = new JLabel(" " + bundle.getString("FrmConfigMrb.lbSimplePenColor.text") + " ");
		Utils.addComponent(panelSimple, lbSimplePenColor, 0, 1, 1, 1, 1, 0, 10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		lbSimplePenColorView = new JLabel("          ");
		lbSimplePenColorView.setBorder(BorderFactory.createLineBorder(Color.black));
		lbSimplePenColorView.setOpaque(true);
		lbSimplePenColorView.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColorSimpleBorder = ChooseColor(ColorSimpleBorder);
				Refresh();
			}
		});
		Utils.addComponent(panelSimple, lbSimplePenColorView, 2, 1, 1, 1, 0, 0, 10, 10, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		// --
		btSimpleDefaultColor = new JButton(bundle.getString("FrmConfigMrb.btDefaultColors.text"));
		btSimpleDefaultColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ColorSimpleFill = CgConst.CL_PROFIL_SIMPLE_FILL;
				ColorSimpleBorder = CgConst.CL_PROFIL_SIMPLE_BORDER;
				Refresh();
			}
		});
		Utils.addComponent(panelSimple, btSimpleDefaultColor, 0, 2, GridBagConstraints.REMAINDER, 1, 0, 0, 10, 10, 0,
				10, GridBagConstraints.EAST, GridBagConstraints.VERTICAL);

		// -- Empty line for resize purpose (not the best solution but it's simple ;) )
		lbSimpleEmpty = new JLabel();
		Utils.addComponent(panelSimple, lbSimpleEmpty, 0, 3, 1, 1, 0, 1, 10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		// == TAB : panel road/track graph config
		panelRoadTrack = new JPanel();
		panelRoadTrack.setLayout(new GridBagLayout());
		TabbedPaneConfig.add(panelRoadTrack, bundle.getString("FrmConfigMrb.panelRoadTrack.tabTitle"));

		// -- Content of the panel
		lbRTTrackFillColor = new JLabel(" " + bundle.getString("FrmConfigMrb.lbRTTrackFillColor.text") + " ");
		Utils.addComponent(panelRoadTrack, lbRTTrackFillColor, 0, 0, 1, 1, 1, 0, 10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		lbRTTrackFillColorView = new JLabel("          ");
		lbRTTrackFillColorView.setBorder(BorderFactory.createLineBorder(Color.black));
		lbRTTrackFillColorView.setOpaque(true);
		lbRTTrackFillColorView.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColorRSPath = ChooseColor(ColorRSPath);
				Refresh();
			}
		});
		Utils.addComponent(panelRoadTrack, lbRTTrackFillColorView, 2, 0, 1, 1, 0, 0, 10, 10, 0, 10,
				GridBagConstraints.WEST, GridBagConstraints.BOTH);

		// --
		lbRTRoadFillColor = new JLabel(" " + bundle.getString("FrmConfigMrb.lbRTRoadFillColor.text") + " ");
		Utils.addComponent(panelRoadTrack, lbRTRoadFillColor, 0, 1, 1, 1, 1, 0, 10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		lbRTRoadFillColorView = new JLabel("          ");
		lbRTRoadFillColorView.setBorder(BorderFactory.createLineBorder(Color.black));
		lbRTRoadFillColorView.setOpaque(true);
		lbRTRoadFillColorView.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColorRSRoad = ChooseColor(ColorRSRoad);
				Refresh();
			}
		});
		Utils.addComponent(panelRoadTrack, lbRTRoadFillColorView, 2, 1, 1, 1, 0, 0, 10, 10, 0, 10,
				GridBagConstraints.WEST, GridBagConstraints.BOTH);

		// --
		lbRTPenColor = new JLabel(" " + bundle.getString("FrmConfigMrb.lbRTPenColor.text") + " ");
		Utils.addComponent(panelRoadTrack, lbRTPenColor, 0, 2, 1, 1, 1, 0, 10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		lbRTPenColorView = new JLabel("          ");
		lbRTPenColorView.setBorder(BorderFactory.createLineBorder(Color.black));
		lbRTPenColorView.setOpaque(true);
		lbRTPenColorView.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColorRSBorder = ChooseColor(ColorRSBorder);
				Refresh();
			}
		});
		Utils.addComponent(panelRoadTrack, lbRTPenColorView, 2, 2, 1, 1, 0, 0, 10, 10, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		// --
		btRTDefaultColor = new JButton(bundle.getString("FrmConfigMrb.btDefaultColors.text"));
		btRTDefaultColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ColorRSPath = CgConst.CL_PROFIL_RS_PATH;
				ColorRSRoad = CgConst.CL_PROFIL_RS_ROAD;
				ColorRSBorder = CgConst.CL_PROFIL_RS_BORDER;
				Refresh();
			}
		});
		Utils.addComponent(panelRoadTrack, btRTDefaultColor, 0, 3, GridBagConstraints.REMAINDER, 1, 0, 0, 10, 10, 0, 10,
				GridBagConstraints.EAST, GridBagConstraints.VERTICAL);

		// -- Empty line for resize purpose (not the best solution but it's simple ;) )
		lbRTEmpty = new JLabel();
		Utils.addComponent(panelRoadTrack, lbRTEmpty, 0, 4, 1, 1, 0, 1, 10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		// == TAB : panel slope graph config
		panelSlope = new JPanel();
		panelSlope.setLayout(new GridBagLayout());
		TabbedPaneConfig.add(panelSlope, bundle.getString("FrmConfigMrb.panelSlope.tabTitle"));

		// -- Content of the panel
		lbSlopeInf5 = new JLabel(" " + bundle.getString("FrmConfigMrb.lbSlopeInf5.text") + " ");
		Utils.addComponent(panelSlope, lbSlopeInf5, 0, 0, 1, 1, 1, 0, 10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		lbSlopeInf5View = new JLabel("          ");
		lbSlopeInf5View.setBorder(BorderFactory.createLineBorder(Color.black));
		lbSlopeInf5View.setOpaque(true);
		lbSlopeInf5View.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColorSlopeInf5 = ChooseColor(ColorSlopeInf5);
				Refresh();
			}
		});
		Utils.addComponent(panelSlope, lbSlopeInf5View, 2, 0, 1, 1, 0, 0, 10, 0, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		// --
		lbSlopeInf10 = new JLabel(" " + bundle.getString("FrmConfigMrb.lbSlopeInf10.text") + " ");
		Utils.addComponent(panelSlope, lbSlopeInf10, 0, 1, 1, 1, 1, 0, 10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		lbSlopeInf10View = new JLabel("          ");
		lbSlopeInf10View.setBorder(BorderFactory.createLineBorder(Color.black));
		lbSlopeInf10View.setOpaque(true);
		lbSlopeInf10View.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColorSlopeInf10 = ChooseColor(ColorSlopeInf10);
				Refresh();
			}
		});
		Utils.addComponent(panelSlope, lbSlopeInf10View, 2, 1, 1, 1, 0, 0, 10, 0, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		// --
		lbSlopeInf15 = new JLabel(" " + bundle.getString("FrmConfigMrb.lbSlopeInf15.text") + " ");
		Utils.addComponent(panelSlope, lbSlopeInf15, 0, 2, 1, 1, 1, 0, 10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		lbSlopeInf15View = new JLabel("          ");
		lbSlopeInf15View.setBorder(BorderFactory.createLineBorder(Color.black));
		lbSlopeInf15View.setOpaque(true);
		lbSlopeInf15View.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColorSlopeInf15 = ChooseColor(ColorSlopeInf15);
				Refresh();
			}
		});
		Utils.addComponent(panelSlope, lbSlopeInf15View, 2, 2, 1, 1, 0, 0, 10, 0, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		// --
		lbSlopeSup15 = new JLabel(" " + bundle.getString("FrmConfigMrb.lbSlopeSup15.text") + " ");
		Utils.addComponent(panelSlope, lbSlopeSup15, 0, 3, 1, 1, 1, 0, 10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		lbSlopeSup15View = new JLabel("          ");
		lbSlopeSup15View.setBorder(BorderFactory.createLineBorder(Color.black));
		lbSlopeSup15View.setOpaque(true);
		lbSlopeSup15View.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColorSlopeSup15 = ChooseColor(ColorSlopeSup15);
				Refresh();
			}
		});
		Utils.addComponent(panelSlope, lbSlopeSup15View, 2, 3, 1, 1, 0, 0, 10, 0, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		// --
		lbSlopePenColor = new JLabel(" " + bundle.getString("FrmConfigMrb.lbSlopePenColor.text") + " ");
		Utils.addComponent(panelSlope, lbSlopePenColor, 0, 4, 1, 1, 1, 0, 10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		lbSlopePenColorView = new JLabel("          ");
		lbSlopePenColorView.setBorder(BorderFactory.createLineBorder(Color.black));
		lbSlopePenColorView.setOpaque(true);
		lbSlopePenColorView.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ColorSlopeBorder = ChooseColor(ColorSlopeBorder);
				Refresh();
			}
		});
		Utils.addComponent(panelSlope, lbSlopePenColorView, 2, 4, 1, 1, 0, 0, 10, 0, 0, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		// --
		btSlopeDefaultColor = new JButton(bundle.getString("FrmConfigMrb.btDefaultColors.text"));
		btSlopeDefaultColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ColorSlopeInf5 = CgConst.CL_PROFIL_SLOPE_INF5;
				ColorSlopeInf10 = CgConst.CL_PROFIL_SLOPE_INF10;
				ColorSlopeInf15 = CgConst.CL_PROFIL_SLOPE_INF15;
				ColorSlopeSup15 = CgConst.CL_PROFIL_SLOPE_SUP15;
				ColorSlopeBorder = CgConst.CL_PROFIL_SLOPE_BORDER;
				Refresh();
			}
		});
		Utils.addComponent(panelSlope, btSlopeDefaultColor, 0, 5, GridBagConstraints.REMAINDER, 1, 0, 0, 10, 10, 0, 10,
				GridBagConstraints.EAST, GridBagConstraints.VERTICAL);

		// -- Empty line for resize purpose (not the best solution but it's simple ;) )
		lbRTEmpty = new JLabel();
		Utils.addComponent(panelSlope, lbRTEmpty, 0, 6, 1, 1, 0, 1, 10, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		// == BUTTONS
		// ===========================================================
		panelButtons = new javax.swing.JPanel();
		panelButtons.setLayout(new FlowLayout());
		Utils.addComponent(paneGlobal, panelButtons, 0, 1, 1, 1, 1, 0, 0, 10, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

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
		panelButtons.add(btOk);
		panelButtons.add(btCancel);

		// --
		pack();

		setLocationRelativeTo(null);
	}

	/**
	 * Open the color chooser dialog
	 * 
	 * @param cl Current color
	 * @return Color choose
	 */
	private Color ChooseColor(Color cl) {
		return FrmColorChooser.showDialog(this, "", cl, settings);
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
