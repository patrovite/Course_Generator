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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import course_generator.CgData;
import course_generator.TrackData;
import course_generator.TrackData.CalcClimbResult;
import course_generator.settings.CgSettings;
import course_generator.utils.CgConst;
import course_generator.utils.CgSpinner;
import course_generator.utils.JTimeSetting;
import course_generator.utils.Utils;

public class FrmElevationFilter extends javax.swing.JDialog{
	//private static final long serialVersionUID = -4653557858071353735L;
	private ResourceBundle bundle;
	private boolean ok;
	private CgSettings settings;
	private TrackData track;
	private CgData data;
	private JFreeChart chartProfil = null;
	private XYSeriesCollection datasetElevDist = null;
	private XYSeriesCollection datasetElevDistSmooth = null;	
	private ChartPanel ChartPanelProfil;
	private JPanel jPanelButtons;
	private JLabel lbFilter;
	private CgSpinner spinFilter;
	private JButton btSelectNormal;
	private JButton btSelectSmoothed;
	private JLabel lbInfo;
	private JButton btCancel;


	/**
	 * Creates new form frmSettings
	 */
	public FrmElevationFilter(CgSettings _settings) {
		settings = _settings;
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		datasetElevDist = new XYSeriesCollection();
		datasetElevDistSmooth = new XYSeriesCollection();		
		chartProfil = CreateChart(datasetElevDist, datasetElevDistSmooth);
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
		Refresh();

		// -- Show the dialog
		setVisible(true);

		if (ok && !track.ReadOnly) {
			// Copy fields
			//spinFilter.getValueAsDouble()
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
		setTitle("Elevation smoothing"); //bundle.getString("frmSearchCurve.title"));
		setAlwaysOnTop(true);
		setResizable(false);
		//setMinimumSize(new Dimension(1000, 400));
		setType(java.awt.Window.Type.UTILITY);

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new GridBagLayout());

		
		// -- Profil chart
		// ------------------------------------------------------
		ChartPanelProfil = new ChartPanel(chartProfil);
		ChartPanelProfil.setBackground(new java.awt.Color(255, 0, 51));

		Utils.addComponent(paneGlobal, ChartPanelProfil, 
				0, line++, 
				2, 1, 
				0, 1, 
				10, 10, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		//
		lbFilter = new javax.swing.JLabel();
		lbFilter.setText("Filter"); //bundle.getString("frmSearchCurve.lbFoundTime1.Text")); 
		Utils.addComponent(paneGlobal, lbFilter, 
				0, line, 
				1, 1, 
				0, 0, 
				10, 10, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		spinFilter = new CgSpinner(0, 0, 100, 2);
		spinFilter.addChangeListener(new ChangeListener() {

	        @Override
	        public void stateChanged(ChangeEvent e) {
				Smooth(spinFilter.getValueAsInt());
				Refresh();
	        }
	    });
		
		Utils.addComponent(paneGlobal, spinFilter, 
				1, line++, 
				1, 1, 
				1, 0, 
				10, 10, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.VERTICAL);

		//-- Info line
		lbInfo = new javax.swing.JLabel();
		lbInfo.setFocusable(false);
		lbInfo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
		lbInfo.setMinimumSize(new Dimension(250, 24));
		lbInfo.setPreferredSize(new Dimension(250, 24));
		lbInfo.setBackground(new java.awt.Color(255, 255, 255));
		lbInfo.setOpaque(true);
		lbInfo.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		Utils.addComponent(paneGlobal, lbInfo, 
				0, line++, 
				2, 1, 
				1, 0, 
				10, 10, 0, 10, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		
		//
		btSelectNormal = new javax.swing.JButton();
		btSelectNormal.setText("Select normal elevations"); //bundle.getString("frmSearchCurve.btSearch.tooltips"));
		btSelectNormal.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				track.SelectNotSmoothedElevation();
				RequestToClose();
			}
		});
		Utils.addComponent(paneGlobal, btSelectNormal, 
				0, line++, 
				2, 1, 
				1, 0, 
				10, 10, 0, 10, 
				GridBagConstraints.WEST, GridBagConstraints.BOTH);

		//
		btSelectSmoothed = new javax.swing.JButton();
		btSelectSmoothed.setText("Select smoothed elevations"); //bundle.getString("frmSearchCurve.btSearch.tooltips"));
		btSelectSmoothed.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				track.SelectSmoothedElevation();
				RequestToClose();
			}
		});
		Utils.addComponent(paneGlobal, btSelectSmoothed, 
				0, line++, 
				2, 1, 
				1, 0, 
				10, 10, 0, 10, 
				GridBagConstraints.WEST, GridBagConstraints.BOTH);

		//
		btCancel = new javax.swing.JButton();
		btCancel.setIcon(Utils.getIcon(this, "cancel.png", settings.DialogIconSize));
		btCancel.setText(bundle.getString("Global.btCancel.text"));
		btCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setVisible(false);
			}
		});
		Utils.addComponent(paneGlobal, btCancel, 
				0, line++, 
				2, 1, 
				1, 0, 
				10, 10, 10, 10, 
				GridBagConstraints.WEST, GridBagConstraints.BOTH);

		// --
		pack();

		setLocationRelativeTo(null);
	}
	

	/**
	 * Update the chart
	 * @param dataset1
	 * @param dataset2
	 * @return
	 */
	private JFreeChart CreateChart(XYDataset dataset1, XYDataset dataset2) {
		JFreeChart chart = ChartFactory.createXYAreaChart("",
				// x axis label
				bundle.getString("JPanelAnalysisTimeDist.labelX"), // "Distance"
				// y axis label
				bundle.getString("JPanelAnalysisTimeDist.labelY1"), // "Elevation"
				dataset1, // data
				PlotOrientation.VERTICAL, false, // include legend
				true, // tooltips
				false // urls
		);

		// -- Background color
		chart.setBackgroundPaint(Color.white);
		chart.setAntiAlias(true);

		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(Color.gray);
		plot.setRangeGridlinePaint(Color.gray);
		plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

		XYAreaRenderer renderer = new XYAreaRenderer();
		renderer.setSeriesPaint(0, new Color(0x99, 0xff, 0x00));
		renderer.setOutline(true);
		renderer.setSeriesOutlineStroke(0, new BasicStroke(2.0f));
		plot.setRenderer(0, renderer);


		NumberAxis rangeAxis2 = new NumberAxis("");//bundle.getString("JPanelAnalysisTimeDist.labelY2")); // "Time"
		plot.setRangeAxis(1, rangeAxis2);
		plot.setDataset(1, dataset2);
		plot.setRangeAxis(1, rangeAxis2);
		//plot.mapDatasetToRangeAxis(1, 1);
		StandardXYItemRenderer renderer2 = new StandardXYItemRenderer();
		renderer2.setSeriesPaint(0, Color.red);
		plot.setRenderer(1, renderer2);

		// -- Select the display order
		plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

		return chart;
	}
	
	
	/**
	 * Update the Time/Distance chart
	 */
	public void Refresh() {
		// -- Clear all series
		if (datasetElevDist.getSeriesCount() > 0)
			datasetElevDist.removeAllSeries();

		if (datasetElevDistSmooth.getSeriesCount() > 0)
			datasetElevDistSmooth.removeAllSeries();

		XYPlot plot = chartProfil.getXYPlot();

		// -- Populate the serie
		XYSeries serie1 = new XYSeries("Elevation/Distance");
		XYSeries serie2 = new XYSeries("Elevation smoothed/Distance");
		for (CgData r : track.data) {
			double x = r.getTotal(settings.Unit) / 1000;
			double y = r.getElevationNotSmoothed(settings.Unit);
			double ys = r.getElevationSmoothed(settings.Unit);
			serie1.add(x, y);
			serie2.add(x, ys);
		}
		datasetElevDist.addSeries(serie1);
		datasetElevDistSmooth.addSeries(serie2);

		if (track.getMaxElev(settings.Unit) > track.getMinElev(settings.Unit)) {
			ValueAxis axisYElev = plot.getRangeAxis(0);
			axisYElev.setRange(Math.floor(track.getMinElev(settings.Unit) / 100.0) * 100.0,
					Math.ceil(track.getMaxElev(settings.Unit) / 100.0) * 100.0);

			ValueAxis axisYElevS = plot.getRangeAxis(1);
			axisYElevS.setRange(Math.floor(track.getMinElev(settings.Unit) / 100.0) * 100.0,
					Math.ceil(track.getMaxElev(settings.Unit) / 100.0) * 100.0);		
		}
		chartProfil = CreateChart(datasetElevDist, datasetElevDistSmooth);
		
		//-- Calc the climb for the none smoothed elevation
		CalcClimbResult ccrNS = new CalcClimbResult();
		ccrNS = track.CalcClimb(CgConst.ELEV_NOTSMOOTHED, 0, track.data.size() - 1, ccrNS);

		//-- Calc the climb for the smoothed elevation
		CalcClimbResult ccrS = new CalcClimbResult();
		ccrS = track.CalcClimb(CgConst.ELEV_SMOOTHED, 0, track.data.size() - 1, ccrS);
		
		//-- Display the result
		lbInfo.setText(	"Climb + (normal):"+ ccrNS.cp + "m - Climb - (normal):"+ ccrNS.cm +
						"m -- Climb + (smoothed):"+ ccrS.cp + "m - Climb - (smoothed):"+ ccrS.cm + "m");
	}

	
	/**
	 * Smooth the elevation profil 
	 * Algorithm from Brenda Zysman (brendaz@rogers.com)
	 * https://github.com/brenzy/gpx-smoother
	 * https://www.potter.ca/
	 */
	private void Smooth(int filter) {
        if (filter==0)
        	return;
		int dataLength = track.data.size();
        if (dataLength == 0) {
        	return;
        }

        //-- Calc the smooth size => multiple of 2 with a min of 2
        int smoothingSize = (int) Math.floor(filter/2);
        if (smoothingSize < 2 || smoothingSize > dataLength / 2) {
        	smoothingSize = 2;
        }
        
        //-- Copy none smoothed elevation in smoothed elevation
        track.CopyNotSmoothedInSmoothedElevation();

   
        for (int i = 0; i < dataLength; i++) {
        	data = track.data.get(i);
        
        	int sumValues = 0;
            
        	//-- Start = Actual pos - smoothingSize
            int start = i - smoothingSize;

            //-- Overflow?
            if (start < 0) {
            	start = 0;
            }

            //-- End = Actual pos + smoothingSize
            int end = i + smoothingSize;

            //-- Overflow?
            if (end > dataLength - 1){
            	end = dataLength - 1;
            }

           //== Calculate the dynamic average with X points before and after the actual position

           //-- Add the "smoothed" elevation between "start" and "end"
           for (int j = start; j <= end; j++) {
        	   sumValues += track.data.get(j).getElevationSmoothed(CgConst.UNIT_METER);
           }

           //-- Divide the sum by the number of value (=>average!)
          data.setElevationSmoothed(sumValues / (end - start + 1));
        }
	}
	
}
