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
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import course_generator.CgData;
import course_generator.TrackData;
import course_generator.settings.CgSettings;
import course_generator.utils.Utils;

public class JPanelAnalysisTimeDist extends JPanel {

	private JFreeChart chartTimeDist = null;
	private XYSeriesCollection datasetTimeDist = null;
	private ResourceBundle bundle;

	private JToolBar toolBar;
	private JButton btTimeDistSave;
	private JPanel jPanelTimeDistInfo;
	private JLabel lbTimeDistInfoDistance;
	private JLabel lbTimeDistInfoElevation;
	private JLabel lbTimeDistInfoTime;
	private JLabel lbTimeDistInfoHour;
	private JLabel lbTimeDistSlope;
	private ChartPanel ChartPanelTimeDist;


	public JPanelAnalysisTimeDist() {
		super();
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		datasetTimeDist = new XYSeriesCollection();
		chartTimeDist = CreateChart(datasetTimeDist);
		initComponents();
	}
	
	private void initComponents() {
		setLayout(new java.awt.BorderLayout());

		//-- Left - Toolbar
		Create_Toolbar();
		add(toolBar, java.awt.BorderLayout.WEST);
		
		//-- Bottom - Info
		jPanelTimeDistInfo = new javax.swing.JPanel();
		jPanelTimeDistInfo.setOpaque(true);
		jPanelTimeDistInfo.setBackground(Color.WHITE);
		jPanelTimeDistInfo.setLayout(new GridBagLayout());
		add(jPanelTimeDistInfo, java.awt.BorderLayout.SOUTH);

		//-- Distance/Elevation/Time/Hour/Slope
		lbTimeDistInfoDistance = new javax.swing.JLabel();
		lbTimeDistInfoDistance.setOpaque(true);
		lbTimeDistInfoDistance.setBackground(Color.WHITE);		
		lbTimeDistInfoDistance.setText(" " + bundle.getString("JPanelAnalysisTimeDist.lbTimeDistInfoDistance.text") + "=0.000km ");
		lbTimeDistInfoDistance.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelTimeDistInfo, lbTimeDistInfoDistance, 
				0, 0, 
				1, 1, 
				0, 0, 
				0, 0, 0, 0, 
				GridBagConstraints.WEST, GridBagConstraints.BOTH);

		// -- Elevation
		lbTimeDistInfoElevation = new javax.swing.JLabel();
		lbTimeDistInfoElevation.setOpaque(true);
		lbTimeDistInfoElevation.setBackground(Color.WHITE);		
		lbTimeDistInfoElevation.setText(" " + bundle.getString("JPanelAnalysisTimeDist.lbTimeDistInfoElevation.text") + "=0m ");
		lbTimeDistInfoElevation.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelTimeDistInfo, lbTimeDistInfoElevation, 
				1, 0, 
				1, 1, 
				0, 0, 
				0, 0, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH);
		
		// -- Time
		lbTimeDistInfoTime = new javax.swing.JLabel();
		lbTimeDistInfoTime.setOpaque(true);
		lbTimeDistInfoTime.setBackground(Color.WHITE);		
		lbTimeDistInfoTime.setText(" " + bundle.getString("JPanelAnalysisTimeDist.lbTimeDistInfoTime.text") + "=00:00:00 ");
		lbTimeDistInfoTime.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelTimeDistInfo, lbTimeDistInfoTime, 
				2, 0, 
				1, 1, 
				0, 0, 
				0, 0, 0, 0, 
				GridBagConstraints.WEST, GridBagConstraints.BOTH);

		// -- Hour
		lbTimeDistInfoHour = new javax.swing.JLabel();
		lbTimeDistInfoHour.setOpaque(true);
		lbTimeDistInfoHour.setBackground(Color.WHITE);		
		lbTimeDistInfoHour.setText(" " + bundle.getString("JPanelAnalysisTimeDist.lbTimeDistInfoHour.text") + "=00:00:00 ");
		lbTimeDistInfoHour.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelTimeDistInfo, lbTimeDistInfoHour, 
				3, 0, 
				1, 1, 
				0, 0, 
				0, 0, 0, 0, 
				GridBagConstraints.WEST, GridBagConstraints.BOTH);
		
		// -- Slope
		lbTimeDistSlope = new javax.swing.JLabel();
		lbTimeDistSlope.setOpaque(true);
		lbTimeDistSlope.setBackground(Color.WHITE);		
		lbTimeDistSlope.setText(" " + bundle.getString("JPanelAnalysisTimeDist.lbTimeDistSlope.text") + "=0.0% ");
		lbTimeDistSlope.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelTimeDistInfo, lbTimeDistSlope, 
				4, 0, 
				1, 1, 
				1, 0, 
				0, 0, 0, 0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL);
		
		
		//-- Chart Time/Dist
		ChartPanelTimeDist = new ChartPanel(chartTimeDist);
	
//		CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
//
//		xCrosshair = new Crosshair(Double.NaN, Color.RED, new BasicStroke(0f));
//		xCrosshair.setLabelBackgroundPaint(Color.WHITE);
//
//		yCrosshair = new Crosshair(Double.NaN, Color.RED, new BasicStroke(0f));
//		yCrosshair.setLabelBackgroundPaint(Color.WHITE);
//
//		crosshairOverlay.addDomainCrosshair(xCrosshair);
//		crosshairOverlay.addRangeCrosshair(yCrosshair);

//		ChartPanelTimeDist.addOverlay(crosshairOverlay);
		ChartPanelTimeDist.setBackground(new java.awt.Color(255, 0, 51));
//		ChartPanelTimeDist.addChartMouseListener(new ChartMouseListener() {
//			@Override
//			public void chartMouseClicked(ChartMouseEvent event) {
//
//				ChartEntity chartentity = event.getEntity();
//				if (chartentity instanceof XYItemEntity) {
//					XYItemEntity e = (XYItemEntity) chartentity;
//					XYDataset d = e.getDataset();
//					int s = e.getSeriesIndex();
//					int i = e.getItem();
//					double x = d.getXValue(s, i);
//					double y = d.getYValue(s, i);
//					xCrosshair.setValue(x);
//					yCrosshair.setValue(y);
//					RefreshProfilInfo(i);
//					//Refresh the position on the data grid
//					TableMain.setRowSelectionInterval(i, i);
//					Rectangle rect = TableMain.getCellRect(i, 0, true);
//					TableMain.scrollRectToVisible(rect);
//					//Refresh the marker position on the map
//					RefreshCurrentPosMarker(Track.data.get(i).getLatitude(), Track.data.get(i).getLongitude());
//				}
//			}
//
//			@Override
//			public void chartMouseMoved(ChartMouseEvent event) {
//			}
//		});
		add(ChartPanelTimeDist, java.awt.BorderLayout.CENTER);

	}
	
	
	private void Create_Toolbar() {
		toolBar = new javax.swing.JToolBar();
		toolBar.setOrientation(javax.swing.SwingConstants.VERTICAL);
		toolBar.setFloatable(false);
		toolBar.setRollover(true);

		// -- Save
		// --------------------------------------------------------------
		btTimeDistSave = new javax.swing.JButton();
		btTimeDistSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/save.png")));
		btTimeDistSave.setToolTipText(bundle.getString("JPanelAnalysisTimeDist.btTimeDistSave.toolTipText"));
		btTimeDistSave.setFocusable(false);
		btTimeDistSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO
			}
		});
		toolBar.add(btTimeDistSave);
	}

	
	private JFreeChart CreateChart(XYDataset dataset) {
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

	
	/**
	 * Update the Time/Distance chart
	 */
	public void Refresh(TrackData track, CgSettings settings) {
		if (track.data.isEmpty())
			return;

		// -- Clear all series
		if (datasetTimeDist.getSeriesCount() > 0)
			datasetTimeDist.removeAllSeries();

		XYPlot plot = chartTimeDist.getXYPlot();
		plot.clearDomainMarkers();
		
		// -- Populate the serie
		XYSeries serie1 = new XYSeries("Elevation/Distance");
		int cmpt=1;
		for (CgData r : track.data) {
			double x=r.getTotal(settings.Unit) / 1000;
			double y=r.getElevation(settings.Unit);
			serie1.add(x, y);

//			if ( ((r.getTag() & CgConst.TAG_MARK) !=0) & showProfilMarker)
//			{
//				Marker m = new ValueMarker(x);
//			    m.setPaint(Color.GRAY);
//		        m.setLabelFont(new Font("SansSerif", Font.PLAIN, 10));
//			    m.setLabel(String.valueOf(cmpt));
//			    m.setLabelOffset(new RectangleInsets(5, 0, 0, 2));
//			    m.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
//			    m.setLabelTextAnchor(TextAnchor.TOP_LEFT);
//			    plot.addDomainMarker(m);
//				cmpt++;
//			}
		}
		datasetTimeDist.addSeries(serie1);

		if (track.getMaxElev(settings.Unit) > track.getMinElev(settings.Unit)) {
			//XYPlot plot = chart.getXYPlot();
			ValueAxis axisY = plot.getRangeAxis();
			axisY.setRange(Math.floor(track.getMinElev(settings.Unit) / 100.0) * 100.0, Math.ceil(track.getMaxElev(settings.Unit) / 100.0) * 100.0);
		}
	}

}
