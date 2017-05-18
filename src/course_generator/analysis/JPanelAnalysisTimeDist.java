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

package course_generator.analysis;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

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
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import course_generator.CgData;
import course_generator.TrackData;
import course_generator.settings.CgSettings;
import course_generator.utils.Utils;

public class JPanelAnalysisTimeDist extends JPanel {
	private JFreeChart chart = null;
	private XYSeriesCollection datasetElevDist = null;
	private XYSeriesCollection datasetTimeDist = null;
	private ResourceBundle bundle;
	private TrackData track=null;
	private CgSettings settings=null;
	
	private JToolBar toolBar;
	private JButton btTimeDistSave;
	private JPanel jPanelTimeDistInfo;
	private JLabel lbTimeDistInfoDistance;
	private JLabel lbTimeDistInfoElevation;
	private JLabel lbTimeDistInfoTime;
	private JLabel lbTimeDistInfoHour;
	private JLabel lbTimeDistSlope;
	private ChartPanel ChartPanelTimeDist;
	private Crosshair xCrosshair;


	public JPanelAnalysisTimeDist() {
		super();
		track=null;
		settings=null;
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		datasetElevDist = new XYSeriesCollection();
		datasetTimeDist = new XYSeriesCollection();
		chart = CreateChart(datasetElevDist, datasetTimeDist);
		initComponents();
	}


	private void initComponents() {
		setLayout(new java.awt.BorderLayout());

		// -- Bottom - Info
		jPanelTimeDistInfo = new javax.swing.JPanel();
		jPanelTimeDistInfo.setOpaque(true);
		jPanelTimeDistInfo.setBackground(Color.WHITE);
		jPanelTimeDistInfo.setLayout(new GridBagLayout());
		add(jPanelTimeDistInfo, java.awt.BorderLayout.SOUTH);

		// -- Distance/Elevation/Time/Hour/Slope
		lbTimeDistInfoDistance = new javax.swing.JLabel();
		lbTimeDistInfoDistance.setOpaque(true);
		lbTimeDistInfoDistance.setBackground(Color.WHITE);
		lbTimeDistInfoDistance
				.setText(" " + bundle.getString("JPanelAnalysisTimeDist.lbTimeDistInfoDistance.text") + "=0.000km ");
		lbTimeDistInfoDistance.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelTimeDistInfo, lbTimeDistInfoDistance, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH);

		// -- Elevation
		lbTimeDistInfoElevation = new javax.swing.JLabel();
		lbTimeDistInfoElevation.setOpaque(true);
		lbTimeDistInfoElevation.setBackground(Color.WHITE);
		lbTimeDistInfoElevation
				.setText(" " + bundle.getString("JPanelAnalysisTimeDist.lbTimeDistInfoElevation.text") + "=0m ");
		lbTimeDistInfoElevation.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelTimeDistInfo, lbTimeDistInfoElevation, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH);

		// -- Time
		lbTimeDistInfoTime = new javax.swing.JLabel();
		lbTimeDistInfoTime.setOpaque(true);
		lbTimeDistInfoTime.setBackground(Color.WHITE);
		lbTimeDistInfoTime
				.setText(" " + bundle.getString("JPanelAnalysisTimeDist.lbTimeDistInfoTime.text") + "=00:00:00 ");
		lbTimeDistInfoTime.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelTimeDistInfo, lbTimeDistInfoTime, 2, 0, 1, 1, 0, 0, 0, 0, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH);

		// -- Hour
		lbTimeDistInfoHour = new javax.swing.JLabel();
		lbTimeDistInfoHour.setOpaque(true);
		lbTimeDistInfoHour.setBackground(Color.WHITE);
		lbTimeDistInfoHour
				.setText(" " + bundle.getString("JPanelAnalysisTimeDist.lbTimeDistInfoHour.text") + "=00:00:00 ");
		lbTimeDistInfoHour.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelTimeDistInfo, lbTimeDistInfoHour, 3, 0, 1, 1, 0, 0, 0, 0, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH);

		// -- Slope
		lbTimeDistSlope = new javax.swing.JLabel();
		lbTimeDistSlope.setOpaque(true);
		lbTimeDistSlope.setBackground(Color.WHITE);
		lbTimeDistSlope.setText(" " + bundle.getString("JPanelAnalysisTimeDist.lbTimeDistSlope.text") + "=0.0% ");
		lbTimeDistSlope.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelTimeDistInfo, lbTimeDistSlope, 4, 0, 1, 1, 1, 0, 0, 0, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.VERTICAL);

		// -- Chart Time/Dist
		ChartPanelTimeDist = new ChartPanel(chart, true /*Properties*/, true /*save*/, true /*print*/, false /*zoom*/,
				true /*tooltips*/);
		ChartPanelTimeDist.setDomainZoomable(false);
		ChartPanelTimeDist.setRangeZoomable(false);
		
		CrosshairOverlay crosshairOverlay = new CrosshairOverlay();

		xCrosshair = new Crosshair(Double.NaN, Color.RED, new BasicStroke(0f));
		xCrosshair.setLabelBackgroundPaint(Color.WHITE);
		crosshairOverlay.addDomainCrosshair(xCrosshair);

		ChartPanelTimeDist.addOverlay(crosshairOverlay);
		ChartPanelTimeDist.setBackground(new java.awt.Color(255, 0, 51));
		ChartPanelTimeDist.addChartMouseListener(new ChartMouseListener() {
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
					RefreshInfo(i);
					// Refresh the position on the data grid
					// TableMain.setRowSelectionInterval(i, i);
					// Rectangle rect = TableMain.getCellRect(i, 0, true);
					// TableMain.scrollRectToVisible(rect);
					// Refresh the marker position on the map
					// RefreshCurrentPosMarker(Track.data.get(i).getLatitude(),
					// Track.data.get(i).getLongitude());
				}
			}


			@Override
			public void chartMouseMoved(ChartMouseEvent event) {
			}
		});
		add(ChartPanelTimeDist, java.awt.BorderLayout.CENTER);
	}


	private void RefreshInfo(int i) {
		if ((track==null) || (settings==null)) return;
		
		// -- Get the data
		CgData d = track.data.get(i);

		
		lbTimeDistInfoDistance.setText(" " + bundle.getString("JPanelAnalysisTimeDist.lbTimeDistInfoDistance.text") 
			+ "="+d.getTotalString(settings.Unit, true)+" ");
		
		lbTimeDistInfoElevation.setText(" " + bundle.getString("JPanelAnalysisTimeDist.lbTimeDistInfoElevation.text") 
			+ "="+d.getElevationString(settings.Unit, true)+" ");
		
		lbTimeDistInfoTime.setText(" " + bundle.getString("JPanelAnalysisTimeDist.lbTimeDistInfoTime.text") 
			+ "="+d.getTimeString()+" ");
		
		lbTimeDistInfoHour.setText(" " + bundle.getString("JPanelAnalysisTimeDist.lbTimeDistInfoHour.text") 
			+ "="+d.getHourString()+" ");
		
		lbTimeDistSlope.setText(" " + bundle.getString("JPanelAnalysisTimeDist.lbTimeDistSlope.text") 
			+ "="+d.getSlopeString(true)+" ");
	}


	private JFreeChart CreateChart(XYDataset dataset1, XYDataset dataset2) {
		JFreeChart chart = ChartFactory.createXYAreaChart("", 
				// x  axis label
				bundle.getString("JPanelAnalysisTimeDist.labelX"), //"Distance" 
				// y axis label
				bundle.getString("JPanelAnalysisTimeDist.labelY1"), //"Elevation" 
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

		NumberAxis rangeAxis2 = new NumberAxis(bundle.getString("JPanelAnalysisTimeDist.labelY2")); //"Time"
		plot.setRangeAxis(1, rangeAxis2);
		plot.setDataset(1, dataset2);
		plot.setRangeAxis(1, rangeAxis2);
		plot.mapDatasetToRangeAxis(1, 1);
		StandardXYItemRenderer renderer2 = new StandardXYItemRenderer();
		renderer2.setSeriesPaint(0, Color.red);
		plot.setRenderer(1, renderer2);

		//-- Select the display order
		plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

		return chart;
	}


	/**
	 * Update the Time/Distance chart
	 */
	public void Refresh(TrackData track, CgSettings settings) {
		if (track==null) return;
		
		if (track.data.isEmpty())
			return;
		this.track=track;
		this.settings=settings;

		// -- Clear all series
		if (datasetElevDist.getSeriesCount() > 0)
			datasetElevDist.removeAllSeries();

		if (datasetTimeDist.getSeriesCount() > 0)
			datasetTimeDist.removeAllSeries();

		XYPlot plot = chart.getXYPlot();

		// -- Populate the serie
		XYSeries serie1 = new XYSeries("Elevation/Distance");
		XYSeries serie2 = new XYSeries("Time/Distance");
		for (CgData r : track.data) {
			double x = r.getTotal(settings.Unit) / 1000;
			double y = r.getElevation(settings.Unit);
			double t = r.getTime();
			serie1.add(x, y);
			serie2.add(x, t);
		}
		datasetElevDist.addSeries(serie1);
		datasetTimeDist.addSeries(serie2);

		if (track.getMaxElev(settings.Unit) > track.getMinElev(settings.Unit)) {
			ValueAxis axisYElev = plot.getRangeAxis(0);
			axisYElev.setRange(Math.floor(track.getMinElev(settings.Unit) / 100.0) * 100.0,
					Math.ceil(track.getMaxElev(settings.Unit) / 100.0) * 100.0);
		}

		chart = CreateChart(datasetElevDist, datasetTimeDist);
		RefreshInfo(0);
	}

}
