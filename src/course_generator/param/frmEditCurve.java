/*
 * Course Generator - Main form
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import course_generator.TrackData;
import course_generator.settings.CgSettings;
import course_generator.utils.CgConst;
import course_generator.utils.Utils;

public class frmEditCurve extends javax.swing.JDialog {
	private static final long serialVersionUID = 1466576207663718609L;
	private boolean ok;
	private boolean bEditMode;
	private TrackData track;
	private java.util.ResourceBundle bundle;
	private JFreeChart chart = null;
	private ParamListModel model;
	public ParamData param = null;
	private ParamPointsModel tablemodel;
	private JButton btOk;
	private JList<Object> ListCurves;
	private JScrollPane jScrollPaneCurves;
	private JToolBar ToolBarAction;
	private JButton btLoadCurve;
	private JButton btEditCurve;
	private JButton btAddCurve;
	private JButton btDuplicateCurve;
	private JButton btDeleteCurve;
	private JLabel lbSelectedCurve;
	private JLabel lbName;
	private JLabel lbNameVal;
	private JLabel lbComment;
	private JTextField tfComment;
	// private JList jListPoint;
	private JScrollPane jScrollPanePoint;
	private JToolBar ToolBarEdit;
	private JButton btAddLine;
	private JButton btDeleteLine;
	private JButton btSaveEdit;
	private JButton btCancelEdit;
	private ChartPanel jPanelProfilChart;
	private XYSeriesCollection dataset;
	private JTable TablePoints;
	private JButton btEditLine;
	private String Paramfile;
	private String Old_Paramfile;
	private Crosshair xCrosshair;
	private Crosshair yCrosshair;
	private CgSettings settings;


	/**
	 * Creates new form frmSettings
	 * 
	 * @param settings
	 *            The Course Generator settings
	 */
	public frmEditCurve(CgSettings settings) {
		super();
		bEditMode = false;
		this.settings = settings;
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		dataset = new XYSeriesCollection();
		chart = CreateChartProfile(dataset);
		param = new ParamData();
		tablemodel = new ParamPointsModel(param);
		initComponents();
		setModal(true);
	}


	/**
	 * Show the dialog
	 * 
	 * @return
	 */
	public boolean showDialog(TrackData t) {
		track = t;
		Paramfile = track.Paramfile;
		Old_Paramfile = Paramfile;
		bEditMode = false;
		tablemodel.setSettings(settings);

		LoadCurve(Utils.GetHomeDir() + "/" + CgConst.CG_DIR + "/" + Paramfile + ".par");
		ChangeEditStatus();
		RefreshView();
		// Set field

		// End set field
		ok = false;

		setVisible(true);

		if (ok) {
			// Copy fields
			track.Paramfile = Paramfile;
		}
		return ok;
	}


	/**
	 * Called when we want to close the dialog
	 */
	private void RequestToClose() {
		boolean param_valid = true;
		// check that the parameters are ok

		// -- Ok?
		if (param_valid) {
			ok = true;
			setVisible(false);
		}
	}


	/**
	 * Creates the chart
	 * 
	 * @param dataset
	 *            Dataset to display
	 * @return Return a JFreeChart object
	 */
	private JFreeChart CreateChartProfile(XYDataset dataset) {
		JFreeChart chart = ChartFactory.createXYAreaChart("", bundle.getString("frmEditCurve.chart.slope"), // "Slope" x
																											// axis
																											// label
				bundle.getString("frmEditCurve.chart.speed") + " ("
						+ Utils.uSpeed2String(settings.Unit, settings.isPace) + ")", // "speed" y axis label
				dataset, // data
				PlotOrientation.VERTICAL, false, // include legend
				true, // tooltips
				false // urls
		);

		chart.setBackgroundPaint(Color.white); // Panel background color
		XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(Color.gray);
		plot.setRangeGridlinePaint(Color.gray);

		// XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		XYAreaRenderer renderer = new XYAreaRenderer();
		// Green (safe color)
		renderer.setSeriesPaint(0, new Color(0x99, 0xff, 0x00));
		renderer.setOutline(true);
		// Width of the outline
		renderer.setSeriesOutlineStroke(0, new BasicStroke(2.0f));
		plot.setRenderer(renderer);

		// NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		// rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		return chart;
	}


	/**
	 * Refresh the curve list
	 */
	private void RefreshCurveList() {
		File[] files = new File(Utils.GetHomeDir() + "/" + CgConst.CG_DIR + "/").listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".par");
			}
		});

		model.clear();

		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				model.addElement(Utils.getFileNameWithoutExtension(files[i].getName()));
			}
		}
		model.sort();
	}


	/**
	 * This method is called to initialize the form.
	 */
	@SuppressWarnings("unchecked")
	private void initComponents() {
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(bundle.getString("frmEditCurve.title"));
		setPreferredSize(new Dimension(1200, 600));
		setAlwaysOnTop(true);
		setResizable(false);
		setType(java.awt.Window.Type.UTILITY);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				formWindowClosing(evt);
			}
		});

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new GridBagLayout());

		// -- Curves list
		ListCurves = new javax.swing.JList<>();
		model = new ParamListModel();
		ListCurves.setModel(model);
		ListCurves.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		ListCurves.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				SelectCurve();
			}
		});
		jScrollPaneCurves = new javax.swing.JScrollPane();
		jScrollPaneCurves.setViewportView(ListCurves);

		Utils.addComponent(paneGlobal, jScrollPaneCurves, 0, 0, 1, 4, 0.5, 1, 10, 10, 0, 0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH);

		// -- Curve management toolbar
		CreateCurvesToolbar();
		Utils.addComponent(paneGlobal, ToolBarAction, 1, 0, 1, 4, 0, 0, 10, 0, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		lbSelectedCurve = new javax.swing.JLabel();
		lbSelectedCurve.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		lbSelectedCurve.setText("Selected");
		lbSelectedCurve.setHorizontalAlignment(JLabel.LEFT);
		Utils.addComponent(paneGlobal, lbSelectedCurve, 2, 0, GridBagConstraints.REMAINDER, 1, 1, 0, 10, 0, 5, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Curve name
		lbName = new javax.swing.JLabel();
		lbName.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		lbName.setText(" " + bundle.getString("frmEditCurve.lbName.text") + " ");
		Utils.addComponent(paneGlobal, lbName, 2, 1, 1, 1, 0, 0, 0, 0, 5, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		lbNameVal = new javax.swing.JLabel();
		lbNameVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(paneGlobal, lbNameVal, 3, 1, GridBagConstraints.REMAINDER, 1, 1, 0, 0, 5, 5, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		// -- Curve comment
		lbComment = new javax.swing.JLabel();
		lbComment.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		lbComment.setText(" " + bundle.getString("frmEditCurve.lbComment.text") + " ");
		Utils.addComponent(paneGlobal, lbComment, 2, 2, 1, 1, 0, 0, 0, 0, 5, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		tfComment = new JTextField();
		tfComment.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(paneGlobal, tfComment, 3, 2, GridBagConstraints.REMAINDER, 1, 1, 0, 0, 5, 5, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Point list
		TablePoints = new javax.swing.JTable();
		TablePoints.setModel(tablemodel);// new ParamPointsModel(param));
		TablePoints.getTableHeader().setReorderingAllowed(false);
		TablePoints.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				// TableMainMouseClicked(evt);
				// TODO
			}
		});
		TablePoints.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				// TableMainKeyReleased(evt);
				// TODO
			}
		});
		TablePoints.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				btEditLine.setEnabled(TablePoints.getSelectedRow() > 0);
			}
		});

		jScrollPanePoint = new javax.swing.JScrollPane();
		jScrollPanePoint.setViewportView(TablePoints);
		Utils.addComponent(paneGlobal, jScrollPanePoint, 2, 3, 2, 1, 0.5, 0, 0, 0, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		// -- Edit toolbar
		CreateEditToolbar();
		Utils.addComponent(paneGlobal, ToolBarEdit, 4, 3, 1, 1, 0, 0, 0, 0, 0, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.BOTH);

		jPanelProfilChart = new ChartPanel(chart);
		Utils.addComponent(paneGlobal, jPanelProfilChart, 5, 3, 1, 1, 1, 0, 0, 0, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
		xCrosshair = new Crosshair(Double.NaN, Color.DARK_GRAY, new BasicStroke(0f));
		xCrosshair.setLabelVisible(true);
		xCrosshair.setLabelBackgroundPaint(Color.WHITE);

		yCrosshair = new Crosshair(Double.NaN, Color.DARK_GRAY, new BasicStroke(0f));
		yCrosshair.setLabelVisible(true);
		yCrosshair.setLabelBackgroundPaint(Color.WHITE);

		crosshairOverlay.addDomainCrosshair(xCrosshair);
		crosshairOverlay.addRangeCrosshair(yCrosshair);

		jPanelProfilChart.addOverlay(crosshairOverlay);
		jPanelProfilChart.setBackground(new java.awt.Color(255, 0, 51));
		jPanelProfilChart.addChartMouseListener(new ChartMouseListener() {
			@Override
			public void chartMouseClicked(ChartMouseEvent event) {

				ChartEntity chartentity = event.getEntity();
				if (chartentity instanceof XYItemEntity) {
					XYItemEntity e = (XYItemEntity) chartentity;
					XYDataset d = e.getDataset();
					int s = e.getSeriesIndex();
					int i = e.getItem();
					double x = d.getXValue(s, i);
					double y = d.getYValue(s, i);
					xCrosshair.setValue(x);
					yCrosshair.setValue(y);
				}
			}


			@Override
			public void chartMouseMoved(ChartMouseEvent event) {
			}
		});

		// == Bottom button
		// ===========================================================
		btOk = new javax.swing.JButton();
		btOk.setText(bundle.getString("Global.btOk.text"));
		btOk.setIcon(Utils.getIcon(this, "valid.png", settings.DialogIconSize));
		btOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				RequestToClose();
			}
		});
		Utils.addComponent(paneGlobal, btOk, 0, 5, GridBagConstraints.REMAINDER, 1, 0, 0, 10, 0, 10, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE);

		// --
		pack();

		// -- Refresh the curve list
		RefreshCurveList();

		// -- Center the windows
		setLocationRelativeTo(null);
	}


	/**
	 * Called when the form is closing. Check if we are still in edit mode
	 * 
	 * @param evt
	 *            Event
	 */
	protected void formWindowClosing(WindowEvent evt) {
		if (bEditMode)
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		else
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}


	/**
	 * Select a curve
	 */
	protected void SelectCurve() {
		int index = ListCurves.getSelectedIndex();
		if (index >= 0) {
			Paramfile = (String) model.getElementAt(index);
			LoadCurve(Utils.GetHomeDir() + "/" + CgConst.CG_DIR + "/" + (String) model.getElementAt(index) + ".par");
			bEditMode = false;
			RefreshView();
		}
	}


	/**
	 * Load a curve
	 * 
	 * @param filename
	 *            Curve file name
	 */
	protected void LoadCurve(String filename) {

		if (Utils.FileExist(filename)) {

			try {
				param.Load(filename);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	private void CreateEditToolbar() {
		// -- Buttons bar
		ToolBarEdit = new javax.swing.JToolBar();
		ToolBarEdit.setFloatable(false);
		ToolBarEdit.setRollover(true);
		ToolBarEdit.setOrientation(JToolBar.VERTICAL);

		// -- Edit line
		btEditLine = new javax.swing.JButton();
		btEditLine.setIcon(Utils.getIcon(this, "edit.png", settings.DialogIconSize));
		btEditLine.setToolTipText(bundle.getString("frmEditCurve.btEditLine.toolTipText"));
		btEditLine.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				EditLine();
			}
		});
		ToolBarEdit.add(btEditLine);

		// -- Separator
		ToolBarEdit.add(new javax.swing.JToolBar.Separator());

		// -- Add line
		btAddLine = new javax.swing.JButton();
		btAddLine.setIcon(Utils.getIcon(this, "add.png", settings.DialogIconSize));
		btAddLine.setToolTipText(bundle.getString("frmEditCurve.btAddLine.toolTipText"));
		btAddLine.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				AddLine();
			}
		});
		ToolBarEdit.add(btAddLine);

		// -- Delete line
		btDeleteLine = new javax.swing.JButton();
		btDeleteLine.setIcon(Utils.getIcon(this, "delete.png", settings.DialogIconSize));
		btDeleteLine.setToolTipText(bundle.getString("frmEditCurve.btDeleteLine.toolTipText"));
		btDeleteLine.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				DeleteLine();
			}
		});
		ToolBarEdit.add(btDeleteLine);

		// -- Separator
		ToolBarEdit.add(new javax.swing.JToolBar.Separator());

		// -- Save edit
		btSaveEdit = new javax.swing.JButton();
		btSaveEdit.setIcon(Utils.getIcon(this, "save.png", settings.DialogIconSize));
		btSaveEdit.setToolTipText(bundle.getString("frmEditCurve.btSaveEdit.toolTipText"));
		btSaveEdit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (bEditMode) {
					param.comment = tfComment.getText();
					param.name = lbNameVal.getText();
					param.SaveCurve(Utils.GetHomeDir() + "/" + CgConst.CG_DIR + "/" + Paramfile + ".par",
							settings.Unit);
					bEditMode = false;
					ChangeEditStatus();
					RefreshView();
					RefreshCurveList();
				}
			}
		});
		ToolBarEdit.add(btSaveEdit);

		// -- Cancel edit
		btCancelEdit = new javax.swing.JButton();
		btCancelEdit.setIcon(Utils.getIcon(this, "cancel.png", settings.DialogIconSize));
		btCancelEdit.setToolTipText(bundle.getString("frmEditCurve.btCancelEdit.toolTipText"));
		btCancelEdit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (bEditMode) {
					bEditMode = false;
					Paramfile = Old_Paramfile;
					LoadCurve(Utils.GetHomeDir() + "/" + CgConst.CG_DIR + "/" + Paramfile + ".par");
					ChangeEditStatus();
					RefreshView();
				}
			}
		});
		ToolBarEdit.add(btCancelEdit);

	}


	protected void RefreshView() {
		// -- Refresh the fields
		lbSelectedCurve.setText(" " + bundle.getString("frmEditCurve.lbSelectedCurve.text") + " " + Paramfile);
		lbNameVal.setText(param.name);
		tfComment.setText(param.comment);
		// -- Refresh points table
		RefreshTablePoints();
		// -- Refresh chart
		RefreshChart();
	}


	private void RefreshChart() {
		if (param.data.size() <= 0)
			return;

		if (dataset.getSeriesCount() > 0)
			dataset.removeAllSeries();

		XYSeries slopeVsSpeedSerie = new XYSeries("Slope/Speed");
		double speed;
		for (CgParam p : param.data) {
			if (settings.Unit == CgConst.UNIT_MILES_FEET) {
				speed = Utils.Km2Miles(p.getSpeedNumber());
			} else {
				speed = p.getSpeedNumber();
			}

			if (settings.isPace) {
				speed = Utils.SpeedToPaceNumber(speed);
			}

			slopeVsSpeedSerie.add(p.getSlope(), speed);
		}
		dataset.addSeries(slopeVsSpeedSerie);
	}


	/**
	 * Add a new line to the point list
	 */
	protected void AddLine() {
		CgParam p = new CgParam(0, "0");
		frmEditPoint frm = new frmEditPoint(settings);
		if (frm.showDialog(p)) {
			param.data.add(p);
			Collections.sort(param.data);
			RefreshView();
		}
	}


	/**
	 * Edit the selected line
	 */
	protected void EditLine() {
		if (!bEditMode)
			return;

		int r = TablePoints.getSelectedRow();
		if (r >= 0) {
			CgParam p = new CgParam(param.data.get(r).getSlope(), param.data.get(r).getSpeed());
			frmEditPoint frm = new frmEditPoint(settings);
			if (frm.showDialog(p)) {
				param.data.set(r, p);
				Collections.sort(param.data);
				RefreshView();
			}
		}
	}


	/**
	 * Delete the selected line in the points table
	 */
	protected void DeleteLine() {
		if (!bEditMode)
			return;

		int r = TablePoints.getSelectedRow();
		if (r >= 0) {
			Object[] options = { " " + bundle.getString("frmEditCurve.DeleteYes") + " ",
					" " + bundle.getString("frmEditCurve.DeleteNo") + " " };
			int ret = JOptionPane.showOptionDialog(this, bundle.getString("frmEditCurve.DeleteLineMessage"),
					bundle.getString("frmEditCurve.DeleteLineTitle"), JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE, null, options, options[1]);

			if (ret == JOptionPane.YES_OPTION) {
				param.data.remove(r);
				Collections.sort(param.data);
				RefreshView();
			}
		}
	}


	/**
	 * Create the left toolbar
	 */
	private void CreateCurvesToolbar() {
		// -- Buttons bar
		ToolBarAction = new javax.swing.JToolBar();
		ToolBarAction.setFloatable(false);
		ToolBarAction.setRollover(true);
		ToolBarAction.setOrientation(JToolBar.VERTICAL);

		// -- Load curve
		btLoadCurve = new javax.swing.JButton();
		btLoadCurve.setIcon(Utils.getIcon(this, "chart_curve_open.png", settings.CurveButtonsIconSize));
		btLoadCurve.setToolTipText(bundle.getString("frmEditCurve.btLoadCurve.toolTipText"));
		btLoadCurve.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SelectCurve();
			}
		});
		ToolBarAction.add(btLoadCurve);

		// -- Edit curve
		btEditCurve = new javax.swing.JButton();
		btEditCurve.setIcon(Utils.getIcon(this, "chart_curve_edit.png", settings.CurveButtonsIconSize));
		btEditCurve.setToolTipText(bundle.getString("frmEditCurve.btEditCurve.toolTipText"));
		btEditCurve.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				TablePoints.setRowSelectionInterval(0, 0);
				bEditMode = true;
				ChangeEditStatus();
				Old_Paramfile = Paramfile;
			}
		});
		ToolBarAction.add(btEditCurve);

		// -- Add curve
		btAddCurve = new javax.swing.JButton();
		btAddCurve.setIcon(Utils.getIcon(this, "chart_curve_add.png", settings.CurveButtonsIconSize));
		btAddCurve.setToolTipText(bundle.getString("frmEditCurve.btAddCurve.toolTipText"));
		btAddCurve.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				AddCurve();
			}
		});
		ToolBarAction.add(btAddCurve);

		// -- Duplicate curve
		btDuplicateCurve = new javax.swing.JButton();
		btDuplicateCurve.setIcon(Utils.getIcon(this, "chart_curve_duplicate.png", settings.CurveButtonsIconSize));
		btDuplicateCurve.setToolTipText(bundle.getString("frmEditCurve.btDuplicateCurve.toolTipText"));
		btDuplicateCurve.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				DuplicateCurve();
			}
		});
		ToolBarAction.add(btDuplicateCurve);

		// -- Delete curve
		btDeleteCurve = new javax.swing.JButton();
		btDeleteCurve.setIcon(Utils.getIcon(this, "chart_curve_delete.png", settings.CurveButtonsIconSize));
		btDeleteCurve.setToolTipText(bundle.getString("frmEditCurve.btDeleteCurve.toolTipText"));
		btDeleteCurve.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				DeleteCurve();
			}
		});
		ToolBarAction.add(btDeleteCurve);
	}


	/**
	 * Refresh the status of form component
	 */
	protected void ChangeEditStatus() {
		tfComment.setEnabled(bEditMode);
		btEditLine.setEnabled(bEditMode);
		btAddLine.setEnabled(bEditMode);
		btDeleteLine.setEnabled(bEditMode);
		btSaveEdit.setEnabled(bEditMode);
		btCancelEdit.setEnabled(bEditMode);
		TablePoints.setEnabled(bEditMode);

		ListCurves.setEnabled(!bEditMode);
		btLoadCurve.setEnabled(!bEditMode);
		btEditCurve.setEnabled(!bEditMode);
		btAddCurve.setEnabled(!bEditMode);
		btDuplicateCurve.setEnabled(!bEditMode);
		btDeleteCurve.setEnabled(!bEditMode);
		btOk.setEnabled(!bEditMode);
	}


	/**
	 * Refresh the table content
	 */
	private void RefreshTablePoints() {
		tablemodel.fireTableDataChanged();
	}


	/**
	 * Duplicate the selected curve Its new name is requested
	 */
	private void DuplicateCurve() {
		if (!bEditMode) {
			Old_Paramfile = Paramfile;

			// -- Configuration of the panel
			JPanel panel = new JPanel(new GridLayout(0, 1));
			panel.add(new JLabel(bundle.getString("frmEditCurve.DuplicatePanel.name.text")));
			JTextField tfName = new JTextField("");
			panel.add(tfName);
			int result = JOptionPane.showConfirmDialog(this, panel,
					bundle.getString("frmEditCurve.DuplicatePanel.title"), JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE);
			if ((result == JOptionPane.OK_OPTION) && (!tfName.getText().isEmpty())) {
				if (Utils.FileExist(Utils.GetHomeDir() + "/" + CgConst.CG_DIR + "/" + tfName.getText() + ".par")) {
					JOptionPane.showMessageDialog(this, bundle.getString("frmEditCurve.DuplicatePanel.fileexist"));
					return;
				}
				param.name = tfName.getText();
				Paramfile = param.name;
				param.SaveCurve(Utils.GetHomeDir() + "/" + CgConst.CG_DIR + "/" + param.name + ".par", settings.Unit);
				ChangeEditStatus();
				RefreshCurveList();
				RefreshView();
			}
		}
	}


	/**
	 * Delete the selected curve
	 */
	protected void DeleteCurve() {
		if (!bEditMode) {
			int index = ListCurves.getSelectedIndex();
			if (index >= 0) {
				String s = Paramfile = (String) model.getElementAt(index);
				if (s.toUpperCase() != "DEFAULT") {
					Object[] options = { " " + bundle.getString("frmEditCurve.DeleteYes") + " ",
							" " + bundle.getString("frmEditCurve.DeleteNo") + " " };
					int ret = JOptionPane.showOptionDialog(this, bundle.getString("frmEditCurve.DeleteCurveMessage"),
							bundle.getString("frmEditCurve.DeleteCurveTitle"), JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE, null, options, options[1]);

					if (ret == JOptionPane.YES_OPTION) {
						File f = new File(Utils.GetHomeDir() + "/" + CgConst.CG_DIR + "/" + s + ".par");
						f.delete();
						LoadCurve(Utils.GetHomeDir() + "/" + CgConst.CG_DIR + "/default.par");
						Paramfile = "Default";
						RefreshView();
						RefreshCurveList();
					}
				}
			}
		}
	}


	/**
	 * Add a new curve to the curve list
	 */
	protected void AddCurve() {
		if (!bEditMode) {

			JPanel panel = new JPanel(new GridLayout(0, 1));
			panel.add(new JLabel(bundle.getString("frmEditCurve.AddCurvePanel.name.text")));
			JTextField tfName = new JTextField("");
			panel.add(tfName);
			int result = JOptionPane.showConfirmDialog(this, panel,
					bundle.getString("frmEditCurve.AddCurvePanel.title"), JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE);
			if ((result == JOptionPane.OK_OPTION) && (!tfName.getText().isEmpty())) {
				if (Utils.FileExist(Utils.GetHomeDir() + "/" + CgConst.CG_DIR + "/" + tfName.getText() + ".par")) {
					JOptionPane.showMessageDialog(this, bundle.getString("frmEditCurve.AddCurvePanelPanel.fileexist"));
					return;
				}

				// -- Add the 2 extrem points to the list and sort the list (not really
				// necessary...)
				param = new ParamData();
				param.name = tfName.getText();
				param.data.add(new CgParam(-50.0, "0"));
				param.data.add(new CgParam(50.0, "0"));
				Collections.sort(param.data);

				// -- Update
				tablemodel.setParam(param);

				Old_Paramfile = Paramfile;
				Paramfile = param.name;

				bEditMode = true;
				ChangeEditStatus();
				RefreshView();
			}
		}
	}

}
