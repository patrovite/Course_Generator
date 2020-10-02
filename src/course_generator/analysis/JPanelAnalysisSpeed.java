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

import javax.swing.JLabel;
import javax.swing.JPanel;

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

public class JPanelAnalysisSpeed extends JPanel {
	private static final long serialVersionUID = 8675654746142038326L;
	private JFreeChart chart = null;
	private XYSeriesCollection datasetSpeedReg = null;
	private XYSeriesCollection datasetSpeed = null;
	private ResourceBundle bundle;
	private JPanel jPanelSpeedInfo;
	private JLabel lbSpeedInfoStartSpeed;
	private JLabel lbSpeedInfoEndSpeed;
	private JLabel lbSpeedInfoSpeed;
	private JLabel lbSpeedInfoDistance;
	private ChartPanel ChartPanelSpeed;
	private Crosshair xCrosshair;
	private TrackData track = null;
	private CgSettings settings = null;
	private double startSpeed = 0.0;
	private double endSpeed = 0.0;
	private int index = 0;

	public JPanelAnalysisSpeed(CgSettings settings) {
		super();
		track = null;
		this.settings = settings;
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		datasetSpeedReg = new XYSeriesCollection();
		datasetSpeed = new XYSeriesCollection();
		chart = CreateChart(datasetSpeedReg, datasetSpeed);
		initComponents();
	}

	private void initComponents() {
		setLayout(new java.awt.BorderLayout());

		// //-- Left - Toolbar
		// Create_Toolbar();
		// add(toolBar, java.awt.BorderLayout.WEST);
		//
		// -- Bottom - Info
		jPanelSpeedInfo = new javax.swing.JPanel();
		jPanelSpeedInfo.setOpaque(true);
		jPanelSpeedInfo.setBackground(Color.WHITE);
		jPanelSpeedInfo.setLayout(new GridBagLayout());
		add(jPanelSpeedInfo, java.awt.BorderLayout.SOUTH);

		// -- Start speed/End speed/Speed/Distance

		// -- Start speed
		lbSpeedInfoStartSpeed = new javax.swing.JLabel();
		lbSpeedInfoStartSpeed.setOpaque(true);
		lbSpeedInfoStartSpeed.setBackground(Color.WHITE);
		lbSpeedInfoStartSpeed.setText(" " + bundle.getString("JPanelAnalysisSpeed.lbSpeedInfoStartSpeed.text") + "="
				+ Utils.FormatSpeed(0.0, settings.Unit, settings.isPace, true));
		lbSpeedInfoStartSpeed.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelSpeedInfo, lbSpeedInfoStartSpeed, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH);

		// -- End speed
		lbSpeedInfoEndSpeed = new javax.swing.JLabel();
		lbSpeedInfoEndSpeed.setOpaque(true);
		lbSpeedInfoEndSpeed.setBackground(Color.WHITE);
		lbSpeedInfoEndSpeed.setText(" " + bundle.getString("JPanelAnalysisSpeed.lbSpeedInfoEndSpeed.text") + "="
				+ Utils.FormatSpeed(0.0, settings.Unit, settings.isPace, true));
		lbSpeedInfoEndSpeed.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelSpeedInfo, lbSpeedInfoEndSpeed, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		// -- Speed
		lbSpeedInfoSpeed = new javax.swing.JLabel();
		lbSpeedInfoSpeed.setOpaque(true);
		lbSpeedInfoSpeed.setBackground(Color.WHITE);
		lbSpeedInfoSpeed.setText(" " + bundle.getString("JPanelAnalysisSpeed.lbSpeedInfoSpeed.text") + "="
				+ Utils.FormatSpeed(0.0, settings.Unit, settings.isPace, true));
		lbSpeedInfoSpeed.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelSpeedInfo, lbSpeedInfoSpeed, 2, 0, 1, 1, 0, 0, 0, 0, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		// -- Distance
		lbSpeedInfoDistance = new javax.swing.JLabel();
		lbSpeedInfoDistance.setOpaque(true);
		lbSpeedInfoDistance.setBackground(Color.WHITE);
		lbSpeedInfoDistance.setText(" " + bundle.getString("JPanelAnalysisSpeed.lbSpeedInfoDistance.text") + "=0.0 "
				+ Utils.uLDist2String(settings.Unit) + " ");
		lbSpeedInfoDistance.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelSpeedInfo, lbSpeedInfoDistance, 3, 0, 1, 1, 1, 0, 0, 0, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.VERTICAL);

		// -- Chart Speed/Dist & Time/Dist
		ChartPanelSpeed = new ChartPanel(chart, true /* Properties */, true /* save */, true /* print */,
				false /* zoom */, true /* tooltips */);
		ChartPanelSpeed.setDomainZoomable(false);
		ChartPanelSpeed.setRangeZoomable(false);

		CrosshairOverlay crosshairOverlay = new CrosshairOverlay();

		xCrosshair = new Crosshair(Double.NaN, Color.RED, new BasicStroke(0f));
		xCrosshair.setLabelBackgroundPaint(Color.WHITE);

		crosshairOverlay.addDomainCrosshair(xCrosshair);

		ChartPanelSpeed.addOverlay(crosshairOverlay);
		ChartPanelSpeed.setBackground(new java.awt.Color(255, 0, 51));

		ChartPanelSpeed.addChartMouseListener(new ChartMouseListener() {
			@Override
			public void chartMouseClicked(ChartMouseEvent event) {

				ChartEntity chartentity = event.getEntity();
				if (chartentity instanceof XYItemEntity) {
					XYItemEntity e = (XYItemEntity) chartentity;
					XYDataset d = e.getDataset();
					int s = e.getSeriesIndex();
					int i = e.getItem();
					double x = d.getXValue(s, i);
					xCrosshair.setValue(x);
					RefreshInfo(i);
				}
			}

			@Override
			public void chartMouseMoved(ChartMouseEvent event) {
			}
		});
		add(ChartPanelSpeed, java.awt.BorderLayout.CENTER);

	}

	private JFreeChart CreateChart(XYDataset dataset1, XYDataset dataset2) {
		JFreeChart chart = ChartFactory.createXYAreaChart("",
				// x axis label
				bundle.getString("JPanelAnalysisSpeed.labelX"), // "Distance",
				// y axis label
				bundle.getString("JPanelAnalysisSpeed.labelY"), // "Speed"
				dataset1, // data
				PlotOrientation.VERTICAL, false, // include legend
				true, // tooltips
				false // urls
		);

		chart.setBackgroundPaint(Color.white); // Panel background color
		chart.setAntiAlias(true);

		XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(Color.gray);
		plot.setRangeGridlinePaint(Color.gray);
		plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

		XYAreaRenderer renderer = new XYAreaRenderer();
		renderer.setSeriesPaint(0, new Color(0x99, 0xff, 0x00));
		renderer.setOutline(true);
		renderer.setSeriesOutlineStroke(0, new BasicStroke(2.0f));
		plot.setRenderer(0, renderer);

		NumberAxis rangeAxis2 = new NumberAxis();
		plot.setRangeAxis(1, rangeAxis2);
		plot.setDataset(1, dataset2);
		plot.setRangeAxis(1, rangeAxis2);
		plot.mapDatasetToRangeAxis(1, 1);

		StandardXYItemRenderer renderer2 = new StandardXYItemRenderer();
		renderer2.setSeriesPaint(0, Color.red);
		plot.setRenderer(1, renderer2);

		// -- Select the display order
		plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

		return chart;
	}

	private void RefreshInfo(int i) {
		if ((track == null) || (settings == null))
			return;

		index = i;

		// -- Get the data
		CgData d = track.data.get(i);

		lbSpeedInfoStartSpeed.setText(" " + bundle.getString("JPanelAnalysisSpeed.lbSpeedInfoStartSpeed.text") + "="
				+ Utils.FormatSpeed(startSpeed, settings.Unit, settings.isPace, true));

		lbSpeedInfoEndSpeed.setText(" " + bundle.getString("JPanelAnalysisSpeed.lbSpeedInfoEndSpeed.text") + "="
				+ Utils.FormatSpeed(endSpeed, settings.Unit, settings.isPace, true));

		lbSpeedInfoSpeed.setText(" " + bundle.getString("JPanelAnalysisSpeed.lbSpeedInfoSpeed.text") + "="
				+ d.getSpeedString(settings.Unit, true, settings.isPace) + " ");

		lbSpeedInfoDistance.setText(" " + bundle.getString("JPanelAnalysisSpeed.lbSpeedInfoDistance.text") + "="
				+ d.getTotalString(settings.Unit, true) + " ");
	}

	/**
	 * Update the Time/Distance chart
	 */
	public void Refresh(TrackData track, CgSettings settings) {
		if (track == null)
			return;

		if (track.data.isEmpty())
			return;

		this.track = track;
		this.settings = settings;

		// -- Calculate the speed regression
		/*
		 * y = ax + b a = the slope of the trend line. b = the intercept of the trend
		 * line.
		 */
		double xAvg = 0;
		double yAvg = 0;
		double v = 0;
		CgData r;

		for (int x = 0; x < track.data.size(); x++) {
			r = track.data.get(x);
			xAvg += x;
			yAvg += (r.getSpeed(settings.Unit) / (100 / r.getDiff())) / (100 / r.getCoeff());
		}

		xAvg = xAvg / track.data.size();
		yAvg = yAvg / track.data.size();

		double v1 = 0;
		double v2 = 0;

		for (int x = 0; x < track.data.size(); x++) {
			r = track.data.get(x);
			v = (r.getSpeed(settings.Unit) / (100 / r.getDiff())) / (100 / r.getCoeff());
			v1 += (x - xAvg) * (v - yAvg);
			v2 += Math.pow(x - xAvg, 2);
		}
		double a = v1 / v2;
		double b = yAvg - a * xAvg;

		// -- Clear all series
		if (datasetSpeedReg.getSeriesCount() > 0)
			datasetSpeedReg.removeAllSeries();

		if (datasetSpeed.getSeriesCount() > 0)
			datasetSpeed.removeAllSeries();

		XYPlot plot = chart.getXYPlot();
		plot.clearDomainMarkers();

		// -- Populate the serie
		XYSeries serie1 = new XYSeries("Speed regression/Distance");
		XYSeries serie2 = new XYSeries("Speed/Distance");

		startSpeed = 0.0;
		endSpeed = 0.0;
		double maxspeed = 0.0;
		double cmpt = 0.0;
		for (CgData d : track.data) {
			double x = d.getTotal(settings.Unit) / 1000;
			double y = d.getSpeed(settings.Unit);

			if (x < 0.001)
				x = 0;
			if (y > maxspeed)
				maxspeed = y;

			if (cmpt == 0)
				startSpeed = (b / (100 / d.getDiff())) / (100 / d.getCoeff());
			if (cmpt == track.data.size() - 1)
				endSpeed = ((a * x + b) / (100 / d.getDiff())) / (100 / d.getCoeff());

			cmpt++;

			serie1.add(x, y / (100.0 / d.getDiff()) / (100.0 / d.getCoeff()));
			serie2.add(x, a * cmpt + b);

		}
		// -- If there is no speed the exit (not already calculated)
		if (maxspeed == 0.0)
			return;

		datasetSpeedReg.addSeries(serie2);
		datasetSpeed.addSeries(serie1);

		ValueAxis axisY = plot.getRangeAxis(0);
		axisY.setRange(0.0, Math.ceil(maxspeed / 5.0) * 5.0);

		axisY = plot.getRangeAxis(1);
		axisY.setRange(0.0, Math.ceil(maxspeed / 5.0) * 5.0);

		chart = CreateChart(datasetSpeedReg, datasetSpeed);
		RefreshInfo(0);
	}

	public void ChangLang() {
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		chart.getXYPlot().getDomainAxis(0).setAttributedLabel(bundle.getString("JPanelAnalysisSpeed.labelX")); // X
		chart.getXYPlot().getRangeAxis(0).setAttributedLabel(bundle.getString("JPanelAnalysisSpeed.labelY")); // Y
		RefreshInfo(index);
	}
}
