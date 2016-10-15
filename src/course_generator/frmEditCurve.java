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

package course_generator;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;

import course_generator.settings.CgSettings;
import course_generator.utils.Utils;

public class frmEditCurve extends javax.swing.JDialog {
	private boolean ok;
	private CgSettings settings;
	private java.util.ResourceBundle bundle;
	private JFreeChart chart = null;
	
	private JPanel jPanelButtons;
	private JButton btCancel;
	private JButton btOk;
	private JList jListCurves;
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
	private JList jListPoint;
	private JScrollPane jScrollPanePoint;
	private JToolBar ToolBarEdit;
	private JButton btAddLine;
	private JButton btDeleteLine;
	private JButton btSaveEdit;
	private JButton btCancelEdit;
	private ChartPanel jPanelProfilChart;
	private XYSeriesCollection dataset;

	/**
	 * Creates new form frmSettings
	 */
	public frmEditCurve() {
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		dataset = new XYSeriesCollection();
		chart = CreateChartProfil(dataset);

		initComponents();
		setModal(true);
	}

	public boolean showDialog(CgSettings s) {
		settings = s;
		// Set field

		// End set field
		ok = false;

		setVisible(true);

		if (ok) {
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

	private JFreeChart CreateChartProfil(XYDataset dataset) {
		JFreeChart chart = ChartFactory.createXYAreaChart("",
				bundle.getString("frmEditCurve.chart.slope"), //"Slope"  x axis label
				bundle.getString("frmEditCurve.chart.speed"), //"speed"  y axis label
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
		renderer.setSeriesPaint(0, new Color(0x99, 0xff, 0x00)); // Green (safe
																	// color)
		renderer.setOutline(true);
		renderer.setSeriesOutlineStroke(0, new BasicStroke(2.0f)); // Width of
																	// the
																	// outline
		plot.setRenderer(renderer);

		// NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		// rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		return chart;
	}

	
	
	
	/**
	 * Refresh the curve list
	 */
    private void RefreshCurveList()
    {
    	File[] files = new File(Utils.GetHomeDir() + "/Course Generator/").listFiles(new FilenameFilter() 
    		{ 
    			@Override 
    			public boolean accept(File dir, String name) {
    				return name.toLowerCase().endsWith(".par"); 
    			} 
    		});
    		
    	ParamListModel model = (ParamListModel) jListCurves.getModel();
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
	private void initComponents() {
		int line = 0;

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(bundle.getString("frmEditCurve.title"));
		setAlwaysOnTop(true);
		setResizable(false);
		setType(java.awt.Window.Type.UTILITY);

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new GridBagLayout());

		//-- Curves list
        jListCurves = new javax.swing.JList<>();
        jListCurves.setModel(new ParamListModel()); //filelist));
        jListCurves.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListCurves.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                jListBindersMouseClicked(evt);
            }
        });
        jScrollPaneCurves = new javax.swing.JScrollPane();
        jScrollPaneCurves.setViewportView(jListCurves);

		Utils.addComponent(paneGlobal, jScrollPaneCurves,
				0, 0, 
				1, 4, 
				1, 0, 
				10, 10, 0, 0, 
				GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		//-- Curve management toolbar
		CreateCurvesToolbar();
		Utils.addComponent(paneGlobal, ToolBarAction,
				1, 0, 
				1, 4, 
				0, 0, 
				10, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.BOTH);
		
		lbSelectedCurve = new javax.swing.JLabel();
		lbSelectedCurve.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		lbSelectedCurve.setText("Selected");
		lbSelectedCurve.setHorizontalAlignment(JLabel.CENTER);
		Utils.addComponent(paneGlobal, lbSelectedCurve,
				2, 0, 
				GridBagConstraints.REMAINDER, 1, 
				0, 0, 
				10, 0, 5, 10, 
				GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		//-- Curve name
		lbName = new javax.swing.JLabel();
		lbName.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		lbName.setText(" "+bundle.getString("frmEditCurve.lbName.text")+" ");
		Utils.addComponent(paneGlobal, lbName,
				2, 1, 
				1, 1, 
				0, 0, 
				0, 0, 5, 0, 
				GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);
		
		lbNameVal = new javax.swing.JLabel();
		lbNameVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		lbNameVal.setText("xxxx");
		Utils.addComponent(paneGlobal, lbNameVal,
				3, 1, 
				GridBagConstraints.REMAINDER, 1, 
				0, 0, 
				0, 5, 5, 10, 
				GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);
		
		//-- Curve comment
		lbComment = new javax.swing.JLabel();
		lbComment.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		lbComment.setText(" "+bundle.getString("frmEditCurve.lbComment.text")+" ");
		Utils.addComponent(paneGlobal, lbComment,
				2, 2, 
				1, 1, 
				0, 0, 
				0, 0, 5, 0, 
				GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);
		
		tfComment = new JTextField();
		tfComment.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		tfComment.setText("xxxx");
		Utils.addComponent(paneGlobal, tfComment,
				3, 2, 
				GridBagConstraints.REMAINDER, 1, 
				0, 0, 
				0, 5, 5, 10, 
				GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);
		
		//-- Point list
        jListPoint = new javax.swing.JList<>();
        jListPoint.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        //jListCurves.setToolTipText(bundle.getString("frmMain.jListBinders.toolTipText")); 
        jListPoint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                jListBindersMouseClicked(evt);
            }
        });
        jScrollPanePoint = new javax.swing.JScrollPane();
        jScrollPanePoint.setViewportView(jListPoint);
		Utils.addComponent(paneGlobal, jScrollPanePoint,
				2, 3, 
				2, 1, 
				0, 0, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.BOTH);
		
		
		//-- Edit toolbar
		CreateEditToolbar();
		Utils.addComponent(paneGlobal, ToolBarEdit,
				4, 3, 
				1, 1, 
				0, 0, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.BOTH);
		
		jPanelProfilChart = new ChartPanel(chart);
		Utils.addComponent(paneGlobal, jPanelProfilChart,
				5, 3, 
				1, 1, 
				0, 0, 
				0, 0, 0, 10, 
				GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.BOTH);
		
		// == Bottom button
		// ===========================================================
		btOk = new javax.swing.JButton();
		btOk.setText(bundle.getString("frmEditCurve.btOk.text"));
		btOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/valid.png")));
		btOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				RequestToClose();
			}
		});
		Utils.addComponent(paneGlobal, btOk,
				0, 5, 
				GridBagConstraints.REMAINDER, 1, 
				0, 0, 
				10, 0, 10, 0, 
				GridBagConstraints.CENTER,
				GridBagConstraints.NONE);


		// --
		pack();
		
		RefreshCurveList();

	}

	private void CreateEditToolbar() {
		//-- Buttons bar
		ToolBarEdit = new javax.swing.JToolBar();
		ToolBarEdit.setFloatable(false);
		ToolBarEdit.setRollover(true);
		ToolBarEdit.setOrientation(JToolBar.VERTICAL);
		
		//-- Add line
		btAddLine = new javax.swing.JButton();
		btAddLine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/add.png")));
		btAddLine.setToolTipText(bundle.getString("frmEditCurve.btAddLine.toolTipText"));
		btAddLine.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				OpenGPXDialog();
			}
		});
		ToolBarEdit.add(btAddLine);

		//-- Delete line
		btDeleteLine = new javax.swing.JButton();
		btDeleteLine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/delete.png")));
		btDeleteLine.setToolTipText(bundle.getString("frmEditCurve.btDeleteLine.toolTipText"));
		btDeleteLine.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				OpenGPXDialog();
			}
		});
		ToolBarEdit.add(btDeleteLine);

		// -- Separator
		ToolBarEdit.add(new javax.swing.JToolBar.Separator());

		//-- Save edit
		btSaveEdit = new javax.swing.JButton();
		btSaveEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/save.png")));
		btSaveEdit.setToolTipText(bundle.getString("frmEditCurve.btSaveEdit.toolTipText"));
		btSaveEdit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				OpenGPXDialog();
			}
		});
		ToolBarEdit.add(btSaveEdit);
		
		//-- Cancel edit
		btCancelEdit = new javax.swing.JButton();
		btCancelEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/cancel.png")));
		btCancelEdit.setToolTipText(bundle.getString("frmEditCurve.btCancelEdit.toolTipText"));
		btCancelEdit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				OpenGPXDialog();
			}
		});
		ToolBarEdit.add(btCancelEdit);

	}
	
	private void CreateCurvesToolbar() {
		//-- Buttons bar
		ToolBarAction = new javax.swing.JToolBar();
		ToolBarAction.setFloatable(false);
		ToolBarAction.setRollover(true);
		ToolBarAction.setOrientation(JToolBar.VERTICAL);
		
		//-- Load curve
		btLoadCurve = new javax.swing.JButton();
		btLoadCurve.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/chart_curve_open.png")));
		btLoadCurve.setToolTipText(bundle.getString("frmEditCurve.btLoadCurve.toolTipText"));
		btLoadCurve.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				OpenGPXDialog();
			}
		});
		ToolBarAction.add(btLoadCurve);

		//-- Load curve
		btEditCurve = new javax.swing.JButton();
		btEditCurve.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/chart_curve_edit.png")));
		btEditCurve.setToolTipText(bundle.getString("frmEditCurve.btEditCurve.toolTipText"));
		btEditCurve.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				OpenGPXDialog();
			}
		});
		ToolBarAction.add(btEditCurve);
		
		//-- Add curve
		btAddCurve = new javax.swing.JButton();
		btAddCurve.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/chart_curve_add.png")));
		btAddCurve.setToolTipText(bundle.getString("frmEditCurve.btAddCurve.toolTipText"));
		btAddCurve.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				OpenGPXDialog();
			}
		});
		ToolBarAction.add(btAddCurve);
		
		//-- Duplicate curve
		btDuplicateCurve = new javax.swing.JButton();
		btDuplicateCurve.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/chart_curve_duplicate.png")));
		btDuplicateCurve.setToolTipText(bundle.getString("frmEditCurve.btDuplicateCurve.toolTipText"));
		btDuplicateCurve.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				OpenGPXDialog();
			}
		});
		ToolBarAction.add(btDuplicateCurve);
		
		//-- Delete curve
		btDeleteCurve = new javax.swing.JButton();
		btDeleteCurve.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/chart_curve_delete.png")));
		btDeleteCurve.setToolTipText(bundle.getString("frmEditCurve.btDeleteCurve.toolTipText"));
		btDeleteCurve.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				OpenGPXDialog();
			}
		});
		ToolBarAction.add(btDeleteCurve);
	}

}
