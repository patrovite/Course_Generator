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

package course_generator.profil;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
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
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

import course_generator.CgData;
import course_generator.TrackData;
import course_generator.settings.CgSettings;
import course_generator.utils.CgConst;
import course_generator.utils.Utils;

public class JPanelProfil  extends JPanel {
	private ResourceBundle bundle;
	private TrackData track = null;
	private CgSettings settings = null;
	private boolean showProfilMarker = true;
	private JToolBar ToolBarProfil;
	private JButton btProfilMarker;
	private JFreeChart chartProfil = null;
	private XYSeriesCollection datasetProfil = null;
	private ChartPanel ChartPanelProfil;
	private Crosshair xCrosshair;
	private Crosshair yCrosshair;
	private JPanel jPanelProfilInfo;
	private JLabel lbProfilDistance;
	private JLabel lbProfilTime;
	private JLabel lbProfilSlope;
	private JLabel lbProfilName;
	private JLabel lbProfilElevation;
	private JLabel lbProfilHour;
	private JLabel lbProfilSpeed;
	private JLabel lbProfilComment;
	private List<JPanelProfilListener> listeners = new ArrayList<JPanelProfilListener>();
	private int index;

	public JPanelProfil(CgSettings settings) {
		super();
		track = null;
		index=0;
		showProfilMarker=true;
		this.settings = settings;
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		datasetProfil = new XYSeriesCollection();
		chartProfil = CreateChartProfil(datasetProfil);
		initComponents();
	}

	public void addListener(JPanelProfilListener toAdd) {
		listeners.add(toAdd);
	}


	public void notifyProfilSelection() {
		for (JPanelProfilListener hl : listeners)
			hl.profilSelectionEvent();
	}


	private JFreeChart CreateChartProfil(XYDataset dataset) {
		JFreeChart chart = ChartFactory.createXYAreaChart("", 
				"Distance", // x axis label
				"Elevation", // y axis label
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

		XYAreaRenderer renderer = new XYAreaRenderer();
		renderer.setSeriesPaint(0, new Color(0x99, 0xff, 0x00));
		renderer.setOutline(true);
		renderer.setSeriesOutlineStroke(0, new BasicStroke(2.0f));
		plot.setRenderer(renderer);

		// NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		// rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		return chart;
	}

	
	private void initComponents() {
		setPreferredSize(new java.awt.Dimension(677, 150));
		setLayout(new java.awt.BorderLayout());

		// -- Profil tool bar
		// ---------------------------------------------------
		Create_Profil_Toolbar();
		add(ToolBarProfil, java.awt.BorderLayout.WEST);

		// -- Profil chart
		// ------------------------------------------------------
		ChartPanelProfil = new ChartPanel(chartProfil);
		CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
		xCrosshair = new Crosshair(Double.NaN, Color.RED, new BasicStroke(0f));
		// xCrosshair.setLabelVisible(true);
		xCrosshair.setLabelBackgroundPaint(Color.WHITE);

		yCrosshair = new Crosshair(Double.NaN, Color.RED, new BasicStroke(0f));
		// yCrosshair.setLabelVisible(true);
		yCrosshair.setLabelBackgroundPaint(Color.WHITE);

		crosshairOverlay.addDomainCrosshair(xCrosshair);
		crosshairOverlay.addRangeCrosshair(yCrosshair);

		ChartPanelProfil.addOverlay(crosshairOverlay);
		ChartPanelProfil.setBackground(new java.awt.Color(255, 0, 51));
		ChartPanelProfil.addChartMouseListener(new ChartMouseListener() {
			

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
					RefreshProfilInfo(i);
					notifyProfilSelection();
					
					index=i;
					
//					//Refresh the position on the data grid
//					TableMain.setRowSelectionInterval(i, i);
//					Rectangle rect = TableMain.getCellRect(i, 0, true);
//					TableMain.scrollRectToVisible(rect);
//					//Refresh the marker position on the map
//					RefreshCurrentPosMarker(Track.data.get(i).getLatitude(), Track.data.get(i).getLongitude());
					
				}
			}

			@Override
			public void chartMouseMoved(ChartMouseEvent event) {
			}
		});

		add(ChartPanelProfil, java.awt.BorderLayout.CENTER);

		// -- Profil info bar
		// ---------------------------------------------------
		jPanelProfilInfo = new javax.swing.JPanel();
		jPanelProfilInfo.setLayout(new GridBagLayout());
		add(jPanelProfilInfo, java.awt.BorderLayout.SOUTH);

		// -- Line 0
		// -- Distance
		// ----------------------------------------------------------
		lbProfilDistance = new javax.swing.JLabel();
		lbProfilDistance.setOpaque(true);
		lbProfilDistance.setBackground(Color.WHITE);		
		lbProfilDistance.setText(" " + bundle.getString("frmMain.lbProfilDistance.text") + "=0.000km ");
		lbProfilDistance.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelProfilInfo, lbProfilDistance, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH);

		// -- Time
		// --------------------------------------------------------------
		lbProfilTime = new javax.swing.JLabel();
		lbProfilTime.setOpaque(true);
		lbProfilTime.setBackground(Color.WHITE);		
		lbProfilTime.setText(" " + bundle.getString("frmMain.lbProfilTime.text") + "=00:00:00 ");
		lbProfilTime.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelProfilInfo, lbProfilTime, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH);

		// -- Slope
		// -------------------------------------------------------------
		lbProfilSlope = new javax.swing.JLabel();
		lbProfilSlope.setOpaque(true);
		lbProfilSlope.setBackground(Color.WHITE);		
		lbProfilSlope.setText(" " + bundle.getString("frmMain.lbProfilSlope.text") + "=0.0% ");
		lbProfilSlope.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelProfilInfo, lbProfilSlope, 2, 0, 1, 1, 0, 0, 0, 0, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH);

		// -- Name
		// --------------------------------------------------------------
		lbProfilName = new javax.swing.JLabel();
		lbProfilName.setOpaque(true);
		lbProfilName.setBackground(Color.WHITE);		
		lbProfilName.setText(" " + bundle.getString("frmMain.lbProfilName.text") + "= ");
		lbProfilName.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelProfilInfo, lbProfilName, 3, 0, 1, 1, 1, 0, 0, 0, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH);

		// -- Line 1
		// -- Elevation
		// ---------------------------------------------------------
		lbProfilElevation = new javax.swing.JLabel();
		lbProfilElevation.setOpaque(true);
		lbProfilElevation.setBackground(Color.WHITE);		
		lbProfilElevation.setText(" " + bundle.getString("frmMain.lbProfilElevation.text") + "=0m ");
		lbProfilElevation.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelProfilInfo, lbProfilElevation, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH);

		// -- Hour
		// --------------------------------------------------------------
		lbProfilHour = new javax.swing.JLabel();
		lbProfilHour.setOpaque(true);
		lbProfilHour.setBackground(Color.WHITE);		
		lbProfilHour.setText(" " + bundle.getString("frmMain.lbProfilHour.text") + "=00:00:00 ");
		lbProfilHour.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelProfilInfo, lbProfilHour, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH);

		// -- Speed
		// -------------------------------------------------------------
		lbProfilSpeed = new javax.swing.JLabel();
		lbProfilSpeed.setOpaque(true);
		lbProfilSpeed.setBackground(Color.WHITE);		
		lbProfilSpeed.setText(" " + bundle.getString("frmMain.lbProfilSpeed.text") + "=0.0km/h ");
		lbProfilSpeed.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelProfilInfo, lbProfilSpeed, 2, 1, 1, 1, 0, 0, 0, 0, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH);

		// -- Comment
		// -----------------------------------------------------------
		lbProfilComment = new javax.swing.JLabel();
		lbProfilComment.setOpaque(true);
		lbProfilComment.setBackground(Color.WHITE);		
		lbProfilComment.setText(" " + bundle.getString("frmMain.lbProfilComment.text") + "= ");
		lbProfilComment.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelProfilInfo, lbProfilComment, 3, 1, 1, 1, 1, 0, 0, 0, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH);
		// -- Distance / Time / Slope / Name
		// -- Elevation / Hour / Speed / Comment
	}

	/**
	 * Create the profil toolbar
	 */
	private void Create_Profil_Toolbar() {
		ToolBarProfil = new javax.swing.JToolBar();
		ToolBarProfil.setOrientation(javax.swing.SwingConstants.VERTICAL);
		ToolBarProfil.setFloatable(false);
		ToolBarProfil.setRollover(true);

		// -- Show/Hide profil marker
		// --------------------------------------------------------------
		btProfilMarker = new javax.swing.JButton();
		btProfilMarker.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/profil_marker.png")));
		btProfilMarker.setToolTipText(bundle.getString("frmMain.btProfilMarker.toolTipText"));
		btProfilMarker.setFocusable(false);
		btProfilMarker.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				showProfilMarker=!showProfilMarker;
				RefreshProfilButtons();
				RefreshProfilChart();
			}
		});
		ToolBarProfil.add(btProfilMarker);

	}

	
	/**
	 * Refresh the fields in the profil info panel
	 * 
	 * @param index
	 *            Index of the line in the track data list
	 * 
	 */
	public void RefreshProfilInfo(int index) {
		if (track==null) return;
		if ((index < 0) || (index >= track.data.size()))
			return;

		// -- Get the data
		CgData d = track.data.get(index);

		lbProfilDistance.setText(" " + bundle.getString("frmMain.lbProfilDistance.text") + "= "
				+ d.getTotalString(settings.Unit, true) + " ");
		lbProfilTime.setText(" " + bundle.getString("frmMain.lbProfilTime.text") + "= " + d.getTimeString() + " ");
		lbProfilSlope
				.setText(" " + bundle.getString("frmMain.lbProfilSlope.text") + "= " + d.getSlopeString(true) + " ");
		lbProfilName.setText(" " + bundle.getString("frmMain.lbProfilName.text") + "= " + d.getName() + " ");
		lbProfilElevation.setText(" " + bundle.getString("frmMain.lbProfilElevation.text") + "= "
				+ d.getElevationString(settings.Unit, true) + " ");
		lbProfilHour.setText(" " + bundle.getString("frmMain.lbProfilHour.text") + "= " + d.getHourString() + " ");
		lbProfilSpeed.setText(" " + bundle.getString("frmMain.lbProfilSpeed.text") + "= "
				+ d.getSpeedString(settings.Unit, true, settings.isPace) + " ");
		lbProfilComment.setText(" " + bundle.getString("frmMain.lbProfilComment.text") + "= " + d.getComment() + " ");
	}

	/**
	 * Update the profil chart
	 */
	public void RefreshProfilChart() {
		if (track==null) return;
		if (track.data.isEmpty())
			return;

		// -- Clear all series
		if (datasetProfil.getSeriesCount() > 0)
			datasetProfil.removeAllSeries();

		XYPlot plot = chartProfil.getXYPlot();
		plot.clearDomainMarkers();
		
		// -- Populate the serie
		XYSeries serie1 = new XYSeries("Elevation/Distance");
		int cmpt=1;
		for (CgData r : track.data) {
			double x=r.getTotal(settings.Unit) / 1000;
			double y=r.getElevation(settings.Unit);
			serie1.add(x, y);

			if ( ((r.getTag() & CgConst.TAG_MARK) !=0) & showProfilMarker)
			{
				Marker m = new ValueMarker(x);
			    m.setPaint(Color.GRAY);
		        m.setLabelFont(new Font("SansSerif", Font.PLAIN, 10));
			    m.setLabel(String.valueOf(cmpt));
			    m.setLabelOffset(new RectangleInsets(5, 0, 0, 2));
			    m.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
			    m.setLabelTextAnchor(TextAnchor.TOP_LEFT);
			    plot.addDomainMarker(m);
				cmpt++;
			}
		}
		datasetProfil.addSeries(serie1);

		if (track.getMaxElev(settings.Unit) > track.getMinElev(settings.Unit)) {
			//XYPlot plot = chart.getXYPlot();
			ValueAxis axisY = plot.getRangeAxis();
			axisY.setRange(Math.floor(track.getMinElev(settings.Unit) / 100.0) * 100.0, Math.ceil(track.getMaxElev(settings.Unit) / 100.0) * 100.0);
		}
	}

	
	/*
	 * Refresh the buttons status of the profil toolbar
	 */
	public void RefreshProfilButtons() {
		btProfilMarker.setSelected(showProfilMarker);
	}

	public int getIndex() {
		return index;
	}

	public void setCrosshairPosition(double x, double y) {
		xCrosshair.setValue(x);
		yCrosshair.setValue(y);
	}

	public void setTrack(TrackData track) {
		this.track = track;
	}

	public void setSettings(CgSettings settings) {
		this.settings = settings;
	}
}
