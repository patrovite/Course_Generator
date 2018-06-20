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
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

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
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import course_generator.CgData;
import course_generator.TrackData;
import course_generator.dialogs.frmSaveSSCurve;
import course_generator.settings.CgSettings;
import course_generator.utils.CgConst;
import course_generator.utils.Utils;

public class JPanelAnalysisSpeedSlope extends JPanel {
	private JFreeChart chart = null;
	private XYSeriesCollection datasetSpeedSlopePoint = null;
	private XYSeriesCollection datasetSpeedSlopeLine = null;
	private ResourceBundle bundle;
	private JToolBar toolBar;
	private JButton btSpeedSlopeSave;
	private JPanel jPanelSpeedSlopeInfo;
	private JLabel lbSpeedSlopeInfoSpeed;
	private JLabel lbSpeedSlopeInfoSlope;
	private ChartPanel ChartPanelSpeedSlope;
	private Crosshair xCrosshair;
	private TrackData track = null;
	private CgSettings settings = null;
	private JButton btSpeedSlopeCorr;
	private JButton btSpeedSlopeFilter;


	public JPanelAnalysisSpeedSlope(CgSettings settings) {
		super();
		this.settings = settings;
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		datasetSpeedSlopePoint = new XYSeriesCollection();
		datasetSpeedSlopeLine = new XYSeriesCollection();
		chart = CreateChart(datasetSpeedSlopePoint, datasetSpeedSlopeLine);
		initComponents();
	}


	private void initComponents() {
		setLayout(new java.awt.BorderLayout());

		// -- Left - Toolbar
		Create_Toolbar();
		add(toolBar, java.awt.BorderLayout.WEST);

		// -- Bottom - Info
		jPanelSpeedSlopeInfo = new javax.swing.JPanel();
		jPanelSpeedSlopeInfo.setOpaque(true);
		jPanelSpeedSlopeInfo.setBackground(Color.WHITE);
		jPanelSpeedSlopeInfo.setLayout(new GridBagLayout());
		add(jPanelSpeedSlopeInfo, java.awt.BorderLayout.SOUTH);

		// -- Speed/Slope

		// -- Speed
		lbSpeedSlopeInfoSpeed = new javax.swing.JLabel();
		lbSpeedSlopeInfoSpeed.setOpaque(true);
		lbSpeedSlopeInfoSpeed.setBackground(Color.WHITE);
		lbSpeedSlopeInfoSpeed
				.setText(" " + bundle.getString("JPanelAnalysisSpeedSlope.lbSpeedSlopeInfoSpeed.text") + "=0km/h ");
		lbSpeedSlopeInfoSpeed.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelSpeedSlopeInfo, lbSpeedSlopeInfoSpeed, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH);

		// -- Slope
		lbSpeedSlopeInfoSlope = new javax.swing.JLabel();
		lbSpeedSlopeInfoSlope.setOpaque(true);
		lbSpeedSlopeInfoSlope.setBackground(Color.WHITE);
		lbSpeedSlopeInfoSlope
				.setText(" " + bundle.getString("JPanelAnalysisSpeedSlope.lbSpeedSlopeInfoSlope.text") + "=0.0% ");
		lbSpeedSlopeInfoSlope.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelSpeedSlopeInfo, lbSpeedSlopeInfoSlope, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.VERTICAL);

		// -- Chart Speed/Dist & Time/Dist
		ChartPanelSpeedSlope = new ChartPanel(chart, true /* Properties */, true /* save */, true /* print */,
				false /* zoom */, true /* tooltips */);
		ChartPanelSpeedSlope.setDomainZoomable(false);
		ChartPanelSpeedSlope.setRangeZoomable(false);

		CrosshairOverlay crosshairOverlay = new CrosshairOverlay();

		xCrosshair = new Crosshair(Double.NaN, Color.RED, new BasicStroke(0f));
		xCrosshair.setLabelBackgroundPaint(Color.WHITE);

		// yCrosshair = new Crosshair(Double.NaN, Color.RED, new
		// BasicStroke(0f));
		// yCrosshair.setLabelBackgroundPaint(Color.WHITE);

		crosshairOverlay.addDomainCrosshair(xCrosshair);
		// crosshairOverlay.addRangeCrosshair(yCrosshair);

		ChartPanelSpeedSlope.addOverlay(crosshairOverlay);
		ChartPanelSpeedSlope.setBackground(new java.awt.Color(255, 0, 51));
		ChartPanelSpeedSlope.addChartMouseListener(new ChartMouseListener() {
			@Override
			public void chartMouseClicked(ChartMouseEvent event) {

				ChartEntity chartentity = event.getEntity();
				if (chartentity instanceof XYItemEntity) {
					XYItemEntity e = (XYItemEntity) chartentity;
					XYDataset d = e.getDataset();
					int s = e.getSeriesIndex();
					int i = e.getItem();
					double x = d.getXValue(s, i);
					// double y = d.getYValue(s, i);
					xCrosshair.setValue(x);
					// yCrosshair.setValue(y);
					RefreshInfo(i);
					// //Refresh the position on the data grid
					// TableMain.setRowSelectionInterval(i, i);
					// Rectangle rect = TableMain.getCellRect(i, 0, true);
					// TableMain.scrollRectToVisible(rect);
					// //Refresh the marker position on the map
					// RefreshCurrentPosMarker(Track.data.get(i).getLatitude(),
					// Track.data.get(i).getLongitude());
				}
			}


			@Override
			public void chartMouseMoved(ChartMouseEvent event) {
			}
		});
		add(ChartPanelSpeedSlope, java.awt.BorderLayout.CENTER);

	}


	private void Create_Toolbar() {
		toolBar = new javax.swing.JToolBar();
		toolBar.setOrientation(javax.swing.SwingConstants.VERTICAL);
		toolBar.setFloatable(false);
		toolBar.setRollover(true);

		// -- Save
		// --------------------------------------------------------------
		btSpeedSlopeSave = new javax.swing.JButton();
		btSpeedSlopeSave.setIcon(Utils.getIcon(this, "save.png", settings.ToolbarIconSize));
		btSpeedSlopeSave.setToolTipText(bundle.getString("JPanelAnalysisSpeedSlope.btSpeedSlopeSave.toolTipText"));
		btSpeedSlopeSave.setFocusable(false);
		btSpeedSlopeSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (track.data.isEmpty())
					return;

				if (datasetSpeedSlopeLine.getSeriesCount() <= 0)
					return;

				frmSaveSSCurve dlg = new frmSaveSSCurve(settings);
				if (dlg.showDialog()) {
					SaveCurve(dlg.getName(), dlg.getComment());
				}
			}
		});
		toolBar.add(btSpeedSlopeSave);

		btSpeedSlopeCorr = new javax.swing.JButton();
		btSpeedSlopeCorr.setIcon(Utils.getIcon(this, "correction.png", settings.ToolbarIconSize));
		btSpeedSlopeCorr.setToolTipText(bundle.getString("JPanelAnalysisSpeedSlope.btSpeedSlopeCorr.toolTipText"));
		btSpeedSlopeCorr.setFocusable(false);
		btSpeedSlopeCorr.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btSpeedSlopeCorr.setSelected(!btSpeedSlopeCorr.isSelected());
				Refresh(track, settings);
			}
		});
		toolBar.add(btSpeedSlopeCorr);

		btSpeedSlopeFilter = new javax.swing.JButton();
		btSpeedSlopeFilter.setIcon(Utils.getIcon(this, "speed.png", settings.ToolbarIconSize));
		btSpeedSlopeFilter.setToolTipText(bundle.getString("JPanelAnalysisSpeedSlope.btSpeedSlopeFilter.toolTipText"));
		btSpeedSlopeFilter.setFocusable(false);
		btSpeedSlopeFilter.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btSpeedSlopeFilter.setSelected(!btSpeedSlopeFilter.isSelected());
				Refresh(track, settings);
			}
		});
		toolBar.add(btSpeedSlopeFilter);

	}


	/**
	 * Save the generated curve as a standard curve
	 * 
	 * @param name
	 *            Name of the curve
	 * @param comment
	 *            Comment for the curve
	 */
	private void SaveCurve(String name, String comment) {
		int n = datasetSpeedSlopeLine.getSeries(0).getItemCount();

		// -- Save the data in the home directory
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		try {
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
					new FileOutputStream(Utils.GetHomeDir() + "/" + CgConst.CG_DIR + "/" + name + ".par"));
			XMLStreamWriter writer = factory.createXMLStreamWriter(bufferedOutputStream, "UTF-8");

			writer.writeStartDocument("UTF-8", "1.0");
			writer.writeStartElement("Project");
			Utils.WriteStringToXML(writer, "Name", name);
			Utils.WriteStringToXML(writer, "Comment", comment);
			writer.writeStartElement("Param");
			for (int i = 0; i < n; i++) {
				writer.writeStartElement("Item");
				Utils.WriteStringToXML(writer, "Slope",
						String.format(Locale.ROOT, "%f", datasetSpeedSlopeLine.getSeries(0).getX(i)));

				if (settings.Unit == CgConst.UNIT_MILES_FEET) {
					if (settings.isPace)
						Utils.WriteStringToXML(writer, "Speed", String.format(Locale.ROOT, "%f", Utils
								.Miles2Km(Utils.Pace2Speed(datasetSpeedSlopeLine.getSeries(0).getY(i).doubleValue()))));
					else
						Utils.WriteStringToXML(writer, "Speed", String.format(Locale.ROOT, "%f",
								Utils.Miles2Km(datasetSpeedSlopeLine.getSeries(0).getY(i).doubleValue())));
				} else if (settings.isPace)
					Utils.WriteStringToXML(writer, "Speed", String.format(Locale.ROOT, "%f",
							Utils.Pace2Speed(datasetSpeedSlopeLine.getSeries(0).getY(i).doubleValue())));
				else
					Utils.WriteStringToXML(writer, "Speed",
							String.format(Locale.ROOT, "%f", datasetSpeedSlopeLine.getSeries(0).getY(i)));

				writer.writeEndElement(); // Item
			}
			writer.writeEndElement(); // Param
			writer.writeEndElement(); // Project
			writer.writeEndDocument();
			writer.flush();
			writer.close();
		} catch (XMLStreamException | IOException e) {
			e.printStackTrace();
		}

	}


	private JFreeChart CreateChart(XYDataset dataset1, XYDataset dataset2) {
		JFreeChart chart = ChartFactory.createScatterPlot("",
				// x axis label
				bundle.getString("JPanelAnalysisSpeedSlope.labelX"), // "Slope",
				// y axis label
				bundle.getString("JPanelAnalysisSpeedSlope.labelY"), // "Speed",
				// data
				dataset1, PlotOrientation.VERTICAL, false, // include legend
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

		XYDotRenderer renderer = new XYDotRenderer();
		renderer.setSeriesPaint(0, new Color(0x99, 0xff, 0x00));
		renderer.setDotWidth(4);
		renderer.setDotHeight(4);
		// renderer.setSeriesOutlinePaint(0, Color.DARK_GRAY);
		// renderer.setSeriesOutlineStroke(0, new BasicStroke(1.0f));
		plot.setRenderer(renderer);

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

		// -- Get the data
		CgData d = track.data.get(i);

		lbSpeedSlopeInfoSpeed.setText(" " + bundle.getString("JPanelAnalysisSpeedSlope.lbSpeedSlopeInfoSpeed.text")
				+ "=" + d.getSpeedString(settings.Unit, true, settings.isPace) + " ");

		lbSpeedSlopeInfoSlope.setText(" " + bundle.getString("JPanelAnalysisSpeedSlope.lbSpeedSlopeInfoSlope.text")
				+ "=" + d.getSlopeString(true) + " ");
	}


	/**
	 * Update the Slope/Speed chart
	 */
	public void Refresh(TrackData track, CgSettings settings) {
		if (track == null)
			return;

		if (track.data.isEmpty())
			return;

		this.track = track;
		this.settings = settings;

		// -- Clear all series
		if (datasetSpeedSlopePoint.getSeriesCount() > 0)
			datasetSpeedSlopePoint.removeAllSeries();
		if (datasetSpeedSlopeLine.getSeriesCount() > 0)
			datasetSpeedSlopeLine.removeAllSeries();

		XYPlot plot = chart.getXYPlot();
		plot.clearDomainMarkers();

		// -- Populate the series
		XYSeries serie1 = new XYSeries("Speed/Slope (points)");
		XYSeries serie2 = new XYSeries("Speed/Slope (line)");

		int[] nb;

		int j = 0;
		double fCurveSpeedSlope[] = new double[101];
		double fCurveSpeedSlope2[] = new double[101];

		Double[] Average = new Double[101];
		Double[] Variance = new Double[101];
		Double[] StandardDeviation = new Double[101];

		nb = new int[101];

		// -- init calc
		for (j = 0; j < 101; j++) {
			fCurveSpeedSlope[j] = 0;
			fCurveSpeedSlope2[j] = 0;
			nb[j] = 0;
		}

		// -- Execute the speed filtering calculation
		SpeedFilter();

		/*
		 * -- Pass 1 We calculate the average, variance and standard deviation of each
		 * area (1% per area)
		 */
		int n = 0;
		double s = 0.0;
		double coeff = 0.0;
		double diff = 0.0;
		for (CgData r : track.data) {
			// if (r.getSpeed(settings.Unit)>maxspeed)
			// maxspeed=r.getSpeed(settings.Unit);

			if (btSpeedSlopeFilter.isSelected())
				s = r.tmp1;
			else
				s = r.getSpeed(settings.Unit);

			if (btSpeedSlopeCorr.isSelected()) {
				coeff = r.getCoeff() / 100.0;
				diff = r.getDiff() / 100.0;
			} else {
				coeff = 1.00;
				diff = 1.00;
			}

			if (s > 1.5) {
				if (r.getSlope() < -49) {
					fCurveSpeedSlope[0] = fCurveSpeedSlope[0] + (s / coeff / diff);
					fCurveSpeedSlope2[0] = fCurveSpeedSlope2[0] + Math.pow((s / coeff / diff), 2);
					nb[0]++;
				} else if (r.getSlope() > 49) {
					fCurveSpeedSlope[100] = fCurveSpeedSlope[100] + (s / coeff / diff);
					fCurveSpeedSlope2[100] = fCurveSpeedSlope2[100] + Math.pow((s / coeff / diff), 2);
					nb[100]++;
				} else {
					n = (int) Math.floor(r.getSlope());
					fCurveSpeedSlope[n + 50] = fCurveSpeedSlope[n + 50] + (s / coeff / diff);
					fCurveSpeedSlope2[n + 50] = fCurveSpeedSlope2[n + 50] + Math.pow((s / coeff / diff), 2);
					nb[n + 50]++;
				}
			}
		}

		for (j = 0; j < 101; j++) {
			if ((nb[j] > 0)) {
				Average[j] = fCurveSpeedSlope[j] / nb[j];
				Variance[j] = fCurveSpeedSlope2[j] / nb[j] - Average[j] * Average[j];
				StandardDeviation[j] = Math.sqrt(Variance[j]);
			}
		}

		/*
		 * -- Pass 2 We filter the same data at +-100% of the standard deviation around
		 * the average
		 */

		// -- Init calc
		for (j = 0; j < 101; j++) {
			fCurveSpeedSlope[j] = 0;
			fCurveSpeedSlope2[j] = 0;
			nb[j] = 0;
		}

		n = 0;
		s = 0.0;
		coeff = 0.0;
		diff = 0.0;
		for (CgData r : track.data) {
			{
				if (btSpeedSlopeFilter.isSelected())
					s = r.tmp1;
				else
					s = r.getSpeed(settings.Unit);

				if (btSpeedSlopeCorr.isSelected()) {
					coeff = r.getCoeff() / 100.0;
					diff = r.getDiff() / 100.0;
				} else {
					coeff = 1.00;
					diff = 1.00;
				}

				if (s > 1.5) {
					if (r.getSlope() < -49) {
						if ((s <= Average[0] + StandardDeviation[0]) || (s >= Average[0] - StandardDeviation[0])) {
							fCurveSpeedSlope[0] = fCurveSpeedSlope[0] + (s / coeff / diff);
							fCurveSpeedSlope2[0] = fCurveSpeedSlope2[0] + Math.pow((s / coeff / diff), 2);
							nb[0]++;
						}
					} else if (r.getSlope() > 49) {
						if ((s <= Average[100] + StandardDeviation[100])
								|| (s >= Average[100] - StandardDeviation[100])) {
							fCurveSpeedSlope[100] = fCurveSpeedSlope[100] + (s / coeff / diff);
							fCurveSpeedSlope2[100] = fCurveSpeedSlope2[100] + Math.pow((s / coeff / diff), 2);
							nb[100]++;
						}
					} else {
						n = (int) Math.floor(r.getSlope());
						if ((s <= Average[n + 50] + StandardDeviation[n + 50])
								|| (s >= Average[n + 50] - StandardDeviation[n + 50])) {
							fCurveSpeedSlope[n + 50] = fCurveSpeedSlope[n + 50] + (s / coeff / diff);
							fCurveSpeedSlope2[n + 50] = fCurveSpeedSlope2[n + 50] + Math.pow((s / coeff / diff), 2);
							nb[n + 50]++;
						}
					}
				}
			}
		}

		double maxspeed = 0.0;

		// -- Populate the first serie
		for (CgData r : track.data) {
			if (btSpeedSlopeFilter.isSelected())
				s = r.tmp1;
			else
				s = r.getSpeed(settings.Unit);

			if (s > maxspeed)
				maxspeed = s;

			serie1.add(r.getSlope(), s);
		}
		datasetSpeedSlopePoint.addSeries(serie1);

		// -- Populate the second serie
		double m = 0.0;
		for (j = 0; j < 101; j++) {
			if ((nb[j] > 0)) {
				m = fCurveSpeedSlope[j] / nb[j];

				if (m > maxspeed)
					maxspeed = m;

				serie2.add(j - 50, m);
			}
		}
		// -- If there is no speed the exit (not already calculated)
		if (maxspeed == 0)
			return;

		datasetSpeedSlopeLine.addSeries(serie2);

		// -- Set the range of the X axis
		ValueAxis axisX = plot.getDomainAxis();
		axisX.setRange(-50.0, 50.0);

		ValueAxis axisY = plot.getRangeAxis(0);
		axisY.setRange(0, Math.ceil(maxspeed / 5.0) * 5.0);

		axisY = plot.getRangeAxis(1);
		axisY.setRange(1, Math.ceil(maxspeed / 5.0) * 5.0);

	}


	/**
	 * Sliding average speed over 5 points
	 */
	private void SpeedFilter() {
		int i = 0;
		double avr = 0.0;

		for (i = 0; i < track.data.size(); i++) {
			if (i == 0)
				avr = track.data.get(0).getSpeed(settings.Unit);
			else if (i == 1)
				avr = (track.data.get(1).getSpeed(settings.Unit) + track.data.get(0).getSpeed(settings.Unit)) / 2;
			else if (i == 2)
				avr = (track.data.get(2).getSpeed(settings.Unit) + track.data.get(1).getSpeed(settings.Unit)
						+ track.data.get(0).getSpeed(settings.Unit)) / 3;
			else if (i == 3)
				avr = (track.data.get(3).getSpeed(settings.Unit) + track.data.get(2).getSpeed(settings.Unit)
						+ track.data.get(1).getSpeed(settings.Unit) + track.data.get(0).getSpeed(settings.Unit)) / 4;
			else
				avr = (track.data.get(i).getSpeed(settings.Unit) + track.data.get(i - 1).getSpeed(settings.Unit)
						+ track.data.get(i - 2).getSpeed(settings.Unit) + track.data.get(i - 3).getSpeed(settings.Unit)
						+ track.data.get(i - 4).getSpeed(settings.Unit)) / 5;

			track.data.get(i).tmp1 = avr;
		}
	}

}
