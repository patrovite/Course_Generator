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

/*
* Used libraries:
*  - Joda-time - http://www.joda.org/joda-time/
*  - SwingX - LGPL 2.1 - https://swingx.java.net/
*  - JMapViewer - GPL - http://wiki.openstreetmap.org/wiki/JMapViewer
*  - jcommon - LGPL - http://www.jfree.org/jcommon/
*  - jfreechart - LGPL - http://www.jfree.org/index.html
*  - TinyLaF - LGPL - Hans Bickel - http://www.muntjak.de/hans/java/tinylaf/ 
*  - SunCalculator - Patrick Kalkman - pkalkie@gmail.com
*/

/*
 * IN PROGRESS:
 * Hot:
 * - Check how the isBH/is TimeLimit is managed in order to have the display in the statusbar (CheckTimeLimit())
 * - Test the import
 *  
 * TODO:
 * - Utils.CalcDistance calculate the distance between 2 points without the elevation. Something is done after?
 * - Move the track info on the a bottom line and add the curve name on this line
 * - Application icon
 * - About dialog
 * - Memorize the horizontal split position
 * - Add default font choice in the settings => Save in the configuration
 * - Save the Resume and data grids columns size in the config
 * - Map source selection
 * 
 * RefresResume Called by (TODO):
 * DoubleClick on a cell of data grid (false)
 * Keydown sur coeff (false)
 * Calculation (false)
 * LoadCgx(false)
 * ImportGPX(false)
 * ImportCGX(false)
 * RestoreInCGX(false)
 * Tab Resume Selection(true)
 */

package course_generator;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

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
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

import course_generator.TrackData.CalcAvrSlopeResult;
import course_generator.TrackData.CalcAvrSpeedResult;
import course_generator.TrackData.CalcClimbResult;
import course_generator.dialogs.FrmImportChoice;
import course_generator.dialogs.frmEditPosition;
import course_generator.dialogs.frmFillCoeff;
import course_generator.dialogs.frmFillCoeff.EditCoeffResult;
import course_generator.dialogs.frmFillDiff;
import course_generator.dialogs.frmFillDiff.EditDiffResult;
import course_generator.dialogs.frmSearchPoint;
import course_generator.dialogs.frmTrackSettings;
import course_generator.mrb.FrmMiniroadbook;
import course_generator.param.frmEditCurve;
import course_generator.resume_table.ResumeAvgSlopeNClass;
import course_generator.resume_table.ResumeAvgSlopeNRenderer;
import course_generator.resume_table.ResumeAvgSlopePClass;
import course_generator.resume_table.ResumeAvgSlopePRenderer;
import course_generator.resume_table.ResumeAvgSpeedClass;
import course_generator.resume_table.ResumeAvgSpeedRenderer;
import course_generator.resume_table.ResumeClimbNClass;
import course_generator.resume_table.ResumeClimbNRenderer;
import course_generator.resume_table.ResumeClimbPClass;
import course_generator.resume_table.ResumeClimbPRenderer;
import course_generator.resume_table.ResumeCommentClass;
import course_generator.resume_table.ResumeCommentRenderer;
import course_generator.resume_table.ResumeDistanceClass;
import course_generator.resume_table.ResumeDistanceRenderer;
import course_generator.resume_table.ResumeElevationClass;
import course_generator.resume_table.ResumeElevationRenderer;
import course_generator.resume_table.ResumeHeaderRenderer;
import course_generator.resume_table.ResumeHourClass;
import course_generator.resume_table.ResumeHourRenderer;
import course_generator.resume_table.ResumeLineClass;
import course_generator.resume_table.ResumeLineRenderer;
import course_generator.resume_table.ResumeModel;
import course_generator.resume_table.ResumeNameClass;
import course_generator.resume_table.ResumeNameRenderer;
import course_generator.resume_table.ResumeNumClass;
import course_generator.resume_table.ResumeNumRenderer;
import course_generator.resume_table.ResumeSpeedNClass;
import course_generator.resume_table.ResumeSpeedNRenderer;
import course_generator.resume_table.ResumeSpeedPClass;
import course_generator.resume_table.ResumeSpeedPRenderer;
import course_generator.resume_table.ResumeStationTimeClass;
import course_generator.resume_table.ResumeStationTimeRenderer;
import course_generator.resume_table.ResumeTimeClass;
import course_generator.resume_table.ResumeTimeLimitClass;
import course_generator.resume_table.ResumeTimeLimitRenderer;
import course_generator.resume_table.ResumeTimeRenderer;
import course_generator.resume_table.ResumedtClimbNClass;
import course_generator.resume_table.ResumedtClimbNRenderer;
import course_generator.resume_table.ResumedtClimbPClass;
import course_generator.resume_table.ResumedtClimbPRenderer;
import course_generator.resume_table.ResumedtDistanceClass;
import course_generator.resume_table.ResumedtDistanceRenderer;
import course_generator.resume_table.ResumedtTimeClass;
import course_generator.resume_table.ResumedtTimeRenderer;
import course_generator.settings.CgSettings;
import course_generator.settings.frmSettings;
import course_generator.tiles.opentopomap.OpenTopoMap;
import course_generator.tiles.thunderforest.Thunderforest_Outdoors;
import course_generator.trackdata_table.CoeffClass;
import course_generator.trackdata_table.CoeffRenderer;
import course_generator.trackdata_table.DiffClass;
import course_generator.trackdata_table.DiffRenderer;
import course_generator.trackdata_table.DistClass;
import course_generator.trackdata_table.DistRenderer;
import course_generator.trackdata_table.ElevationClass;
import course_generator.trackdata_table.ElevationRenderer;
import course_generator.trackdata_table.HourClass;
import course_generator.trackdata_table.HourRenderer;
import course_generator.trackdata_table.LatClass;
import course_generator.trackdata_table.LatRenderer;
import course_generator.trackdata_table.LonClass;
import course_generator.trackdata_table.LonRenderer;
import course_generator.trackdata_table.MainHeaderRenderer;
import course_generator.trackdata_table.RecupClass;
import course_generator.trackdata_table.RecupRenderer;
import course_generator.trackdata_table.StationClass;
import course_generator.trackdata_table.StationRenderer;
import course_generator.trackdata_table.TagClass;
import course_generator.trackdata_table.TagRenderer;
import course_generator.trackdata_table.TimeClass;
import course_generator.trackdata_table.TimeRenderer;
import course_generator.trackdata_table.TimelimitClass;
import course_generator.trackdata_table.TimelimitRenderer;
import course_generator.trackdata_table.TotalClass;
import course_generator.trackdata_table.TotalRenderer;
import course_generator.trackdata_table.TrackDataModel;
import course_generator.utils.CgConst;
import course_generator.utils.FileTypeFilter;
import course_generator.utils.OsCheck;
import course_generator.utils.Utils;
import course_generator.utils.Utils.CalcLineResult;

/**
 * This is the main class of the project.
 *
 * @author pierre.delore
 */
public class frmMain extends javax.swing.JFrame {
	private final String Version = "4.0.0.ALPHA 4";
	public TrackData Track;
	private ResumeData Resume;
	private final TrackDataModel ModelTableMain;
	private final ResumeModel ModelTableResume;
	public CgSettings Settings;
	public String DataDir;
	private MapMarker CurrentPosMarker = null;
	private int old_row = -1;
	private JFreeChart chart = null;
	private XYSeriesCollection dataset = null;
	private java.util.ResourceBundle bundle = null;
	private int cmptInternetConnexion = 0;
	private boolean InternetConnectionActive = false;
	private Timer timer1s; // 1 second timer object

	/**
	 * Creates new form frmMain
	 */
	DefaultTableModel model;
	private JMenuItem mnuSaveCGX;
	private JMenuItem mnuSaveGPX;
	private JMenuItem mnuSaveCSV;
	private JMenuItem mnuSavePartCGX;
	private JMenuItem mnuSavePartGPX;
	private JMenuItem mnuSavePartCSV;
	private JMenuItem mnuImportPoints;
	private JMenuItem mnuExportPoints;
	private JMenuItem mnuExportTagAsWaypoints;
	private JMenuItem mnuOffLine;
	private JMenuItem mnuQuit;
	private JMenuItem mnuCopy;
	private JMenuItem mnuSelectLines;
	private JMenuItem mnuSearchPoint;
	private JMenuItem mnuMarkPosition;
	private JMenuItem mnuGotoNextMark;
	private JMenuItem mnuGotoPrevMark;
	private JMenu mnuDisplay;
	private JMenuItem mnuHTMLReport;
	private JMenuItem mnuGenerateKML;
	private JMenuItem mnuGenerateRoadbook;
	private JMenuItem mnuGenerateMiniRoadbook;
	private JMenuItem mnuDisplaySpeed;
	private JMenuItem mnuDisplaySlope;
	private JMenu mnuTools;
	private JMenuItem mnuCalculateTrackTime;
	private JMenuItem mnuFindMinMax;
	private JMenuItem mnuInvertTrack;
	private JMenuItem mnuDefineNewStart;
	private JMenuItem mnuInternetTools;
	private JMenuItem mnuDisplaySSDir;
	private JMenu mnuSettings;
	private JMenuItem mnuTrackSettings;
	private JMenuItem mnuSpeedSlopeCurves;
	private JMenuItem mnuCGSettings;
	private JMenu mnuHelp;
	private JMenuItem mnuCGHelp;
	private JMenuItem mnuReward;
	private JMenuItem mnuAbout;
	private JButton btSaveCGX;
	private JButton btUndo;
	private JButton btSearch;
	private JButton btGotoPreviousMark;
	private JButton btGotoNextMark;
	private JButton btDisplaySSCurves;
	private JButton btTirednessSettings;
	private JButton btTrackSettings;
	private JButton btCalculateTrackTime;
	private JButton btProfilSave;
	private JButton btProfilZoomX;
	private JButton btProfilZoomY;
	private JButton btProfilSettings;
	private JLabel lbProfilDistance;
	private JLabel lbProfilTime;
	private JLabel lbProfilSlope;
	private JLabel lbProfilName;
	private JLabel lbProfilElevation;
	private JLabel lbProfilHour;
	private JLabel lbProfilSpeed;
	private JLabel lbProfilComment;
	private JToolBar ToolBarStatistic;
	private JButton btStatisticSave;
	private JButton btStatisticRefresh;
	private JToolBar ToolBarResume;
	private JButton btResumeSave;
	private JTable TableResume;
	private JScrollPane jScrollPaneResume;
	private JButton btMapMarker;
	private JButton btMapHideMarker;
	private JButton btMapAddMarker;
	private JButton btMapUndo;
	private JButton btMapTrackEasy;
	private JButton btMapTrackAverage;
	private JButton btMapTrackVeryEasy;
	private JButton btMapTrackHard;
	private JButton btMapTrackVeryHard;
	private JButton btMapMark;
	private JButton btMapEat;
	private JButton btMapDrink;
	private JLabel LbInfoUnit;
	private JLabel LbInfoTotalDistVal;
	private JLabel LbInfoDpVal;
	private JLabel LbInfoDmVal;
	private JLabel LbInfoTimeVal;
	private JLabel LbInfoCalculateVal;
	private JLabel LbInfoInternetVal;
	private JLabel LbInfoUnitVal;
	private JScrollPane jScrollPanelMap;
	private JButton btRefreshRefresh;
	private ChartPanel jPanelProfilChart;

	public Crosshair xCrosshair;

	public Crosshair yCrosshair;
	private JPanel StatusBar;
	private JLabel lbStatusBarSpeed;
	private JLabel LbInfoCurve;
	private JLabel LbInfoCurveVal;
	private JLabel LbModified;
	private JLabel LbModifiedVal;
	private JButton btFillDiff;
	private JButton btFillCoeff;
	private JLabel LbTimeLimit;
	private JComponent sepTimeLimit;
	private JButton btMiniRoadbook;
	private JMenuItem mnuImportGPX;
	private JMenuItem mnuImportCGX;

	// -- Called every second
	class TimerActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			cmptInternetConnexion++;
			if (cmptInternetConnexion > Settings.ConnectionTimeout) {
				cmptInternetConnexion = 0;
				InternetConnectionActive = Utils.isInternetReachable();
				RefreshStatusbar(Track);
			}
		}
	}

	/**
	 * Creates new form frmMain ------------------------------- !!!! Everything
	 * start here !!!! ------------------------
	 */
	public frmMain(String args[]) {
		// Initialize data
		DataDir = Utils.GetHomeDir();
		Track = new TrackData();
		Resume = new ResumeData();
		Settings = new CgSettings();
		ModelTableMain = new TrackDataModel(Track, Settings);
		ModelTableResume = new ResumeModel(Resume, Settings);

		dataset = new XYSeriesCollection();
		// dataset = CreateDataset();
		chart = CreateChartProfil(dataset);
		
		// -- Load configuration
		LoadConfig();

		// -- Set the language
		if (Settings.Language.equalsIgnoreCase("FR")) {
			Locale.setDefault(Locale.FRANCE);
		} else if (Settings.Language.equalsIgnoreCase("EN")) {
			Locale.setDefault(Locale.US);
		}

		// -- Set default font
		setUIFont(new javax.swing.plaf.FontUIResource("Tahoma", // Settings.DefaultFont.getFontName(),
																// //"Tahoma"
				Font.PLAIN, // Settings.DefaultFont.getStyle(), // Font.PLAIN,
				14)); // Settings.DefaultFont.getSize())); //24));

		// -- Initialize the string resource for internationalization
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");

		// -- Configure the main form
		initComponents();

		// -- Set the icon of the application
		setIconImage(createImageIcon("/course_generator/images/cg.png", "").getImage());

		// -- Set the preferred column width
		for (int i = 0; i < 15; i++) {
			TableMain.getColumnModel().getColumn(i).setPreferredWidth(Settings.TableMainColWidth[i]);
		}
		RefreshTableMain();

		// -- Set the windows size and center it in the screen - Not tested on
		// multi-screen configuration
		Rectangle r = getBounds();
		r.width = Settings.MainWindowWidth;
		r.height = Settings.MainWindowHeight;
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		r.x = (screensize.width - r.width) / 2;
		r.y = (screensize.height - r.height) / 2;
		setBounds(r);

		// -- Set the left panel width
		SplitPaneMain.setDividerLocation(Settings.VertSplitPosition);
		SplitPaneMainRight.setDividerLocation(Settings.HorizSplitPosition);

		// -- Tests - To Remove...

		// -- Configure the tile source for the map
		MapViewer.setTileSource(new Thunderforest_Outdoors());
		//TODO Switch to OpenTopomap
		//MapViewer.setTileSource(new OpenTopoMap());

		// -- Set the counter in order near the end in order to start the
		// connection test
		cmptInternetConnexion = Settings.ConnectionTimeout - 1;

		// -- Start the 1 second timer
		timer1s = new Timer(1000, new TimerActionListener());
		timer1s.start();

		// -- Refresh
		RefreshMruCGX();
		RefreshMruGPX();
		RefreshStatusbar(Track);
	}
	
	
	
	
	//-- Permet de lancer un calcul sur le tableau --
	
    private void CalcTrackTime() {
    	if (Track.data.isEmpty()) return;

        Track.Calculate();

        RefreshStatusbar(Track);
        
		CalcClimbResult ccr = new CalcClimbResult();	
		ccr = Track.CalcClimb(0, Track.data.size()-1, ccr);
		Track.ClimbP=ccr.cp;
		Track.ClimbM=ccr.cm;
		Track.AscTime=ccr.tp;
		Track.DescTime=ccr.tm;
		
		Track.CheckTimeLimit();

        Track.isCalculated = true;
        Track.isModified = true;
        
        //-- Refresh statusbar
        RefreshStatusbar(Track);
		RefreshTableMain();
        RefreshResume();
        RefreshStat(false);
    }

    
	private void RefreshStat(boolean b) {
		// TODO Auto-generated method stub
		
	}




	/**
	 * Create the main menu
	 */
	private void Create_MenuBarMain() {
		// Create the menu
		mnuMain = new javax.swing.JMenuBar();
		mnuMain.setName("mnuMain");

		// == File menu ========================================================
		mnuFile = new javax.swing.JMenu();
		mnuFile.setText(bundle.getString("frmMain.mnuFile.text"));

		// -- Open GPX
		mnuOpenGPX = new javax.swing.JMenuItem();
		mnuOpenGPX.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O,
				java.awt.event.InputEvent.CTRL_MASK | java.awt.event.InputEvent.SHIFT_MASK));
		mnuOpenGPX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/openGPX.png")));
		mnuOpenGPX.setText(bundle.getString("frmMain.mnuOpenGPX.text"));
		mnuOpenGPX.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				OpenGPXDialog();
			}
		});
		mnuFile.add(mnuOpenGPX);

		// -- Open CGX
		mnuOpenCGX = new javax.swing.JMenuItem();
		mnuOpenCGX.setAccelerator(
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
		mnuOpenCGX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/openCGX.png")));
		mnuOpenCGX.setText(bundle.getString("frmMain.mnuOpenCGX.text"));
		mnuOpenCGX.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// mnuOpenCGXActionPerformed(evt);
				OpenCGXDialog();
			}
		});
		mnuFile.add(mnuOpenCGX);

		// -- Recent GPX files
		// --------------------------------------------------
		mnuLastGPX = new javax.swing.JMenu();
		mnuLastGPX.setText(bundle.getString("frmMain.mnuLastGPX.text"));

		// -- Mru GPX n�1
		mnuMruGPX1 = new javax.swing.JMenuItem();
		mnuMruGPX1.setText(bundle.getString("frmMain.mnuMruGPX1.text"));
		mnuMruGPX1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuMruGPXActionPerformed(evt);
			}
		});
		mnuLastGPX.add(mnuMruGPX1);

		// -- Mru GPX n�2
		mnuMruGPX2 = new javax.swing.JMenuItem();
		mnuMruGPX2.setText(bundle.getString("frmMain.mnuMruGPX2.text"));
		mnuMruGPX2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuMruGPXActionPerformed(evt);
			}
		});
		mnuLastGPX.add(mnuMruGPX2);

		// -- Mru GPX n�3
		mnuMruGPX3 = new javax.swing.JMenuItem();
		mnuMruGPX3.setText(bundle.getString("frmMain.mnuMruGPX3.text"));
		mnuMruGPX3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuMruGPXActionPerformed(evt);
			}
		});
		mnuLastGPX.add(mnuMruGPX3);

		// -- Mru GPX n�4
		mnuMruGPX4 = new javax.swing.JMenuItem();
		mnuMruGPX4.setText(bundle.getString("frmMain.mnuMruGPX4.text"));
		mnuMruGPX4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuMruGPXActionPerformed(evt);
			}
		});
		mnuLastGPX.add(mnuMruGPX4);

		// -- Mru GPX n�5
		mnuMruGPX5 = new javax.swing.JMenuItem();
		mnuMruGPX5.setText(bundle.getString("frmMain.mnuMruGPX5.text"));
		mnuMruGPX5.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuMruGPXActionPerformed(evt);
			}
		});
		mnuLastGPX.add(mnuMruGPX5);

		mnuFile.add(mnuLastGPX);

		// -- Recent CGX files
		// --------------------------------------------------
		mnuLastCGX = new javax.swing.JMenu();
		mnuLastCGX.setText(bundle.getString("frmMain.mnuLastCGX.text"));

		// -- Mru CGX n�1
		mnuMruCGX1 = new javax.swing.JMenuItem();
		mnuMruCGX1.setText(bundle.getString("frmMain.mnuMruCGX1.text"));
		mnuMruCGX1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuMruCGXActionPerformed(evt);
			}
		});
		mnuLastCGX.add(mnuMruCGX1);

		// -- Mru CGX n�2
		mnuMruCGX2 = new javax.swing.JMenuItem();
		mnuMruCGX2.setText(bundle.getString("frmMain.mnuMruCGX2.text"));
		mnuMruCGX2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuMruCGXActionPerformed(evt);
			}
		});
		mnuLastCGX.add(mnuMruCGX2);

		// -- Mru CGX n�3
		mnuMruCGX3 = new javax.swing.JMenuItem();
		mnuMruCGX3.setText(bundle.getString("frmMain.mnuMruCGX3.text"));
		mnuMruCGX3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuMruCGXActionPerformed(evt);
			}
		});
		mnuLastCGX.add(mnuMruCGX3);

		// -- Mru CGX n�4
		mnuMruCGX4 = new javax.swing.JMenuItem();
		mnuMruCGX4.setText(bundle.getString("frmMain.mnuMruCGX4.text"));
		mnuMruCGX4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuMruCGXActionPerformed(evt);
			}
		});
		mnuLastCGX.add(mnuMruCGX4);

		// -- Mru CGX n�5
		mnuMruCGX5 = new javax.swing.JMenuItem();
		mnuMruCGX5.setText(bundle.getString("frmMain.mnuMruCGX5.text"));
		mnuMruCGX5.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuMruCGXActionPerformed(evt);
			}
		});
		mnuLastCGX.add(mnuMruCGX5);

		mnuFile.add(mnuLastCGX);

		// -- Separator
		mnuFile.add(new javax.swing.JPopupMenu.Separator());

		// -- Save CGX
		mnuSaveCGX = new javax.swing.JMenuItem();
		mnuSaveCGX.setAccelerator(
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
		mnuSaveCGX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/saveCGX.png")));
		mnuSaveCGX.setText(bundle.getString("frmMain.mnuSaveCGX.text"));
		mnuSaveCGX.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SaveCGX();
			}
		});
		mnuFile.add(mnuSaveCGX);

		// -- Save GPX
		mnuSaveGPX = new javax.swing.JMenuItem();
		mnuSaveGPX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/saveGPX.png")));
		mnuSaveGPX.setText(bundle.getString("frmMain.mnuSaveGPX.text"));
		mnuSaveGPX.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SaveGPX();
			}
		});
		mnuFile.add(mnuSaveGPX);

		// -- Save CSV
		mnuSaveCSV = new javax.swing.JMenuItem();
		mnuSaveCSV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/saveCSV.png")));
		mnuSaveCSV.setText(bundle.getString("frmMain.mnuSaveCSV.text"));
		mnuSaveCSV.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// mnuSaveGPXActionPerformed(evt); //TODO
			}
		});
		mnuSaveCSV.setEnabled(false);
		mnuFile.add(mnuSaveCSV);

		// -- Separator
		mnuFile.add(new javax.swing.JPopupMenu.Separator());

		
		// -- Import GPX
		mnuImportGPX = new javax.swing.JMenuItem();
		mnuImportGPX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/openGPX.png")));
		mnuImportGPX.setText(bundle.getString("frmMain.mnuImportGPX.text"));
		mnuImportGPX.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ImportGPX();
			}
		});
		mnuFile.add(mnuImportGPX);

		// -- Import CGX
		mnuImportCGX = new javax.swing.JMenuItem();
		mnuImportCGX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/openCGX.png")));
		mnuImportCGX.setText(bundle.getString("frmMain.mnuImportCGX.text"));
		mnuImportCGX.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				ImportCGX();
			}
		});
		mnuFile.add(mnuImportCGX);

		
		
		// -- Save a part of the track in CGX
		mnuSavePartCGX = new javax.swing.JMenuItem();
		mnuSavePartCGX
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/saveCGX.png")));
		mnuSavePartCGX.setText(bundle.getString("frmMain.mnuSavePartCGX.text"));
		mnuSavePartCGX.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SavePartCGX();
			}
		});
		mnuFile.add(mnuSavePartCGX);

		// -- Save a part of the track in GPX
		mnuSavePartGPX = new javax.swing.JMenuItem();
		mnuSavePartGPX
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/saveGPX.png")));
		mnuSavePartGPX.setText(bundle.getString("frmMain.mnuSavePartGPX.text"));
		mnuSavePartGPX.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SavePartGPX();
			}
		});
		mnuFile.add(mnuSavePartGPX);

		// -- Save a part of the track in CSV
		mnuSavePartCSV = new javax.swing.JMenuItem();
		mnuSavePartCSV
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/saveCSV.png")));
		mnuSavePartCSV.setText(bundle.getString("frmMain.mnuSavePartCSV.text"));
		mnuSavePartCSV.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// mnuSaveGPXActionPerformed(evt); //TODO
			}
		});
		mnuSavePartCSV.setEnabled(false);
		mnuFile.add(mnuSavePartCSV);

		// -- Separator
		mnuFile.add(new javax.swing.JPopupMenu.Separator());

		// -- Import points
		mnuImportPoints = new javax.swing.JMenuItem();
		mnuImportPoints
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/import.png")));
		mnuImportPoints.setText(bundle.getString("frmMain.mnuImportPoints.text"));
		mnuImportPoints.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// mnuSaveGPXActionPerformed(evt); //TODO
			}
		});
		mnuImportPoints.setEnabled(false);
		mnuFile.add(mnuImportPoints);

		// -- Export points
		mnuExportPoints = new javax.swing.JMenuItem();
		mnuExportPoints
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/export.png")));
		mnuExportPoints.setText(bundle.getString("frmMain.mnuExportPoints.text"));
		mnuExportPoints.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// mnuSaveGPXActionPerformed(evt); //TODO
			}
		});
		mnuExportPoints.setEnabled(false);
		mnuFile.add(mnuExportPoints);

		// -- Separator
		mnuFile.add(new javax.swing.JPopupMenu.Separator());

		// -- Export tags as waypoints
		mnuExportTagAsWaypoints = new javax.swing.JMenuItem();
		mnuExportTagAsWaypoints
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/export.png")));
		mnuExportTagAsWaypoints.setText(bundle.getString("frmMain.mnuExportTagAsWaypoints.text"));
		mnuExportTagAsWaypoints.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// mnuSaveGPXActionPerformed(evt); //TODO
			}
		});
		mnuExportTagAsWaypoints.setEnabled(false);
		mnuFile.add(mnuExportTagAsWaypoints);

		// -- Separator
		mnuFile.add(new javax.swing.JPopupMenu.Separator());

		// -- Offline
		mnuOffLine = new javax.swing.JMenuItem();
		// mnuOffLine.setIcon(new
		// javax.swing.ImageIcon(getClass().getResource("/course_generator/images/export.png")));
		mnuOffLine.setText(bundle.getString("frmMain.mnuOffLine.text"));
		mnuOffLine.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// mnuSaveGPXActionPerformed(evt); //TODO
			}
		});
		mnuOffLine.setEnabled(false);
		mnuFile.add(mnuOffLine);

		// -- Separator
		mnuFile.add(new javax.swing.JPopupMenu.Separator());

		// -- Quit
		mnuQuit = new javax.swing.JMenuItem();
		mnuQuit.setAccelerator(
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
		mnuQuit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/quit.png")));
		mnuQuit.setText(bundle.getString("frmMain.mnuQuit.text"));
		mnuQuit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuQuitActionPerformed(evt);
			}
		});
		mnuFile.add(mnuQuit);

		// --
		mnuMain.add(mnuFile);

		// -- Menu Edit --------------------------------------------------------
		mnuEdit = new javax.swing.JMenu();
		mnuEdit.setText(bundle.getString("frmMain.mnuEdit.text"));

		// -- Copy
		mnuCopy = new javax.swing.JMenuItem();
		mnuCopy.setAccelerator(
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
		mnuCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/copy.png")));
		mnuCopy.setText(bundle.getString("frmMain.mnuCopy.text"));
		mnuCopy.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// mnuSaveCGXActionPerformed(evt); //TODO
			}
		});
		mnuCopy.setEnabled(false);
		mnuEdit.add(mnuCopy);

		// -- Select lines...
		mnuSelectLines = new javax.swing.JMenuItem();
		mnuSelectLines.setText(bundle.getString("frmMain.mnuSelectLines.text"));
		mnuSelectLines.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// mnuSaveCGXActionPerformed(evt); //TODO
			}
		});
		mnuSelectLines.setEnabled(false);
		mnuEdit.add(mnuSelectLines);

		// -- Separator
		mnuEdit.add(new javax.swing.JPopupMenu.Separator());

		// -- Search a point...
		mnuSearchPoint = new javax.swing.JMenuItem();
		mnuSearchPoint.setAccelerator(
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
		mnuSearchPoint
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/search.png")));
		mnuSearchPoint.setText(bundle.getString("frmMain.mnuSearchPoint.text"));
		mnuSearchPoint.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SearchPointDialog();
			}
		});
		mnuEdit.add(mnuSearchPoint);

		// -- Separator
		mnuEdit.add(new javax.swing.JPopupMenu.Separator());

		// -- Mark the current position
		mnuMarkPosition = new javax.swing.JMenuItem();
		mnuMarkPosition.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
		mnuMarkPosition.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/flag.png")));
		mnuMarkPosition.setText(bundle.getString("frmMain.mnuMarkPosition.text"));
		mnuMarkPosition.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// mnuSaveCGXActionPerformed(evt); //TODO
			}
		});
		mnuEdit.add(mnuMarkPosition);

		// -- Go to the next mark
		mnuGotoNextMark = new javax.swing.JMenuItem();
		mnuGotoNextMark.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F7, 0));
		mnuGotoNextMark.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/next.png")));
		mnuGotoNextMark.setText(bundle.getString("frmMain.mnuGotoNextMark.text"));
		mnuGotoNextMark.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				GotoNextTag();
			}
		});
		mnuEdit.add(mnuGotoNextMark);

		// -- Go to the previous mark
		mnuGotoPrevMark = new javax.swing.JMenuItem();
		mnuGotoPrevMark.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F7,
				java.awt.event.InputEvent.SHIFT_MASK));
		mnuGotoPrevMark.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/prev.png")));
		mnuGotoPrevMark.setText(bundle.getString("frmMain.mnuGotoPrevMark.text"));
		mnuGotoPrevMark.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				GotoPrevTag();
			}
		});
		mnuEdit.add(mnuGotoPrevMark);

		// --
		mnuMain.add(mnuEdit);

		// == Display
		// ===========================================================
		mnuDisplay = new javax.swing.JMenu();
		mnuDisplay.setText(bundle.getString("frmMain.mnuDisplay.text"));

		// -- HTML report
		// -------------------------------------------------------
		mnuHTMLReport = new javax.swing.JMenuItem();
		mnuHTMLReport.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
		mnuHTMLReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/html.png")));
		mnuHTMLReport.setText(bundle.getString("frmMain.mnuHTMLReport.text"));
		mnuHTMLReport.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// mnuSaveCGXActionPerformed(evt); //TODO
			}
		});
		mnuHTMLReport.setEnabled(false);
		mnuDisplay.add(mnuHTMLReport);

		// -- Separator
		// ---------------------------------------------------------
		mnuDisplay.add(new javax.swing.JPopupMenu.Separator());

		// -- Generate KML file (Google earth)
		// ----------------------------------
		mnuGenerateKML = new javax.swing.JMenuItem();
		mnuGenerateKML.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/earth.png")));
		mnuGenerateKML.setText(bundle.getString("frmMain.mnuGenerateKML.text"));
		mnuGenerateKML.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// mnuSaveCGXActionPerformed(evt); //TODO
			}
		});
		mnuGenerateKML.setEnabled(false);
		mnuDisplay.add(mnuGenerateKML);

		// -- Separator
		// ---------------------------------------------------------
		mnuDisplay.add(new javax.swing.JPopupMenu.Separator());

		// -- Generate a roadbook
		// -----------------------------------------------
		mnuGenerateRoadbook = new javax.swing.JMenuItem();
		mnuGenerateRoadbook
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/roadbook.png")));
		mnuGenerateRoadbook.setText(bundle.getString("frmMain.mnuGenerateRoadbook.text"));
		mnuGenerateRoadbook.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO
			}
		});
		mnuGenerateRoadbook.setEnabled(false);
		mnuDisplay.add(mnuGenerateRoadbook);

		// -- Mini roadbook
		// -----------------------------------------------------
		mnuGenerateMiniRoadbook = new javax.swing.JMenuItem();
		mnuGenerateMiniRoadbook.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
		mnuGenerateMiniRoadbook
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/profil.png")));
		mnuGenerateMiniRoadbook.setText(bundle.getString("frmMain.mnuGenerateMiniRoadbook.text"));
		mnuGenerateMiniRoadbook.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ShowMRB();
			}
		});
		mnuDisplay.add(mnuGenerateMiniRoadbook);

		// -- Separator
		// ---------------------------------------------------------
		mnuDisplay.add(new javax.swing.JPopupMenu.Separator());

		// -- Display the speed in the data grid
		// --------------------------------
		mnuDisplaySpeed = new javax.swing.JMenuItem();
		mnuDisplaySpeed.setText(bundle.getString("frmMain.mnuDisplaySpeed.text"));
		mnuDisplaySpeed.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// mnuSaveCGXActionPerformed(evt); //TODO
			}
		});
		mnuDisplaySpeed.setEnabled(false);
		mnuDisplay.add(mnuDisplaySpeed);

		// -- Display the slope in the data grid
		// --------------------------------
		mnuDisplaySlope = new javax.swing.JMenuItem();
		mnuDisplaySlope.setText(bundle.getString("frmMain.mnuDisplaySlope.text"));
		mnuDisplaySlope.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// mnuSaveCGXActionPerformed(evt); //TODO
			}
		});
		mnuDisplaySlope.setEnabled(false);
		mnuDisplay.add(mnuDisplaySlope);

		// --
		mnuMain.add(mnuDisplay);

		// == Tools
		// =============================================================
		mnuTools = new javax.swing.JMenu();
		mnuTools.setText(bundle.getString("frmMain.mnuTools.text"));

		// -- Find Min / Max
		// ----------------------------------------------------
		mnuFindMinMax = new javax.swing.JMenuItem();
		mnuFindMinMax.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/minmax.png")));
		mnuFindMinMax.setText(bundle.getString("frmMain.mnuFindMinMax.text"));
		mnuFindMinMax.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Track.CalcMinMax();
			}
		});
		mnuTools.add(mnuFindMinMax);

		// -- Invert track
		// ------------------------------------------------------
		mnuInvertTrack = new javax.swing.JMenuItem();
		mnuInvertTrack
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/invert.png")));
		mnuInvertTrack.setText(bundle.getString("frmMain.mnuInvertTrack.text"));
		mnuInvertTrack.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (Track.data.size() > 0) {
					// BackupInCGX(); TODO BackupCGX
					Track.Invert();
					RefreshProfil();
					RefreshStatusbar(Track);
					RefreshTableMain();
					RefreshResume();
					// DrawMap();
					// DrawProfil(Constantes.SRC_MAIN);
					// RefreshPosMap(Constantes.SRC_MAIN);
					// MainGrid.Refresh();
					// RefreshMarker();
				}
			}
		});
		mnuTools.add(mnuInvertTrack);

		// -- Define a new start
		// ------------------------------------------------
		mnuDefineNewStart = new javax.swing.JMenuItem();
		mnuDefineNewStart.setText(bundle.getString("frmMain.mnuDefineNewStart.text"));
		mnuDefineNewStart.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				NewStartPoint();
			}
		});
		mnuTools.add(mnuDefineNewStart);

		// -- Calculate the track time
		// -------------------------------------------
		mnuCalculateTrackTime = new javax.swing.JMenuItem();
		mnuCalculateTrackTime.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
		mnuCalculateTrackTime
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/refresh.png")));
		mnuCalculateTrackTime.setText(bundle.getString("frmMain.mnuCalculateTackTime.text"));
		mnuCalculateTrackTime.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				CalcTrackTime();
			}
		});
		mnuTools.add(mnuCalculateTrackTime);

		// -- Separator
		// ---------------------------------------------------------
		mnuTools.add(new javax.swing.JPopupMenu.Separator());

		// -- Internet tools
		// ----------------------------------------------------
		mnuInternetTools = new javax.swing.JMenuItem();
		mnuInternetTools.setText(bundle.getString("frmMain.mnuInternetTools.text"));
		mnuInternetTools.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// mnuSaveCGXActionPerformed(evt); //TODO
			}
		});
		mnuInternetTools.setEnabled(false);
		mnuTools.add(mnuInternetTools);

		// -- Display the directory containing the speed/slope files
		// ------------
		mnuDisplaySSDir = new javax.swing.JMenuItem();
		mnuDisplaySSDir.setText(bundle.getString("frmMain.mnuDisplaySSDir.text"));
		mnuDisplaySSDir.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// mnuSaveCGXActionPerformed(evt); //TODO
			}
		});
		mnuDisplaySSDir.setEnabled(false);
		mnuTools.add(mnuDisplaySSDir);

		// --
		mnuMain.add(mnuTools);

		// == Settings
		// ==========================================================
		mnuSettings = new javax.swing.JMenu();
		mnuSettings.setText(bundle.getString("frmMain.mnuSettings.text"));

		// -- Track settings
		// ----------------------------------------------------
		mnuTrackSettings = new javax.swing.JMenuItem();
		mnuTrackSettings.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0));
		mnuTrackSettings
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/settings.png")));
		mnuTrackSettings.setText(bundle.getString("frmMain.mnuTrackSettings.text"));
		mnuTrackSettings.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				TrackSettings();
			}
		});
		mnuSettings.add(mnuTrackSettings);

		// -- Speed/Slope curves
		// ------------------------------------------------
		mnuSpeedSlopeCurves = new javax.swing.JMenuItem();
		mnuSpeedSlopeCurves
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/curve.png")));
		mnuSpeedSlopeCurves.setText(bundle.getString("frmMain.mnuSpeedSlopeCurves.text"));
		mnuSpeedSlopeCurves.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				EditSSCurves();
			}
		});
		mnuSettings.add(mnuSpeedSlopeCurves);

		// -- Separator
		// ---------------------------------------------------------
		mnuSettings.add(new javax.swing.JPopupMenu.Separator());

		// -- Settings Course Generator
		// -----------------------------------------
		mnuCGSettings = new javax.swing.JMenuItem();
		mnuCGSettings
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/settings.png")));
		mnuCGSettings.setText(bundle.getString("frmMain.mnuCGSettings.text"));
		mnuCGSettings.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				frmSettings frm = new frmSettings();
				frm.showDialog(Settings);

				// -- Refresh data and display
				RefreshStatusbar(Track);
			}
		});
		mnuSettings.add(mnuCGSettings);

		// --
		mnuMain.add(mnuSettings);

		// == Help
		// ==============================================================
		mnuHelp = new javax.swing.JMenu();
		mnuHelp.setText(bundle.getString("frmMain.mnuHelp.text"));

		// -- Help
		// --------------------------------------------------------------
		mnuCGHelp = new javax.swing.JMenuItem();
		mnuCGHelp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
		mnuCGHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/help.png")));
		mnuCGHelp.setText(bundle.getString("frmMain.mnuCGHelp.text"));
		mnuCGHelp.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// mnuSaveCGXActionPerformed(evt); //TODO
			}
		});
		mnuCGHelp.setEnabled(false);
		mnuHelp.add(mnuCGHelp);

		// -- Reward the author
		// -------------------------------------------------
		mnuReward = new javax.swing.JMenuItem();
		mnuReward.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/pouce.png")));
		mnuReward.setText(bundle.getString("frmMain.mnuReward.text"));
		mnuReward.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// mnuSaveCGXActionPerformed(evt); //TODO
			}
		});
		mnuReward.setEnabled(false);
		mnuHelp.add(mnuReward);

		// -- About
		// -------------------------------------------------------------
		mnuAbout = new javax.swing.JMenuItem();
		mnuAbout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/about.png")));
		mnuAbout.setText(bundle.getString("frmMain.mnuAbout.text"));
		mnuAbout.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// mnuSaveCGXActionPerformed(evt); //TODO
			}
		});
		mnuAbout.setEnabled(false);
		mnuHelp.add(mnuAbout);

		// --
		mnuMain.add(mnuHelp);

		// -- Add the menu at the window
		setJMenuBar(mnuMain);

		mnuMain.getAccessibleContext().setAccessibleParent(this); // TODO check
																	// why it's
																	// necessary

	}

	/**
	 * Define a new starting point from the current position in the main table
	 */
	protected void NewStartPoint() {
		if (Track.data.isEmpty())
			return;

		int start = TableMain.getSelectedRow();
		if (start<0) return;

		//-- Confirmation dialog
		Object[] options = { " " + bundle.getString("frmMain.NewStartYes") + " ",
				" " + bundle.getString("frmMain.NewStartNo") + " " };
		int ret = JOptionPane.showOptionDialog(this, bundle.getString("frmMain.NewStartMessage"),
				bundle.getString("frmMain.NewStartTitle"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
				null, options, options[1]);

		//-- Ok! Let's go
		if (ret == JOptionPane.YES_OPTION) {
			Track.NewStartingPoint(start);
			
			//-- Move the cursor to the first line of the data table
			TableMain.setRowSelectionInterval(0, 0);
			Rectangle rect = TableMain.getCellRect(0, 0, true);
			TableMain.scrollRectToVisible(rect);
			
	        Track.isCalculated = false;
	        Track.isModified = true;
	        
	        //-- Refresh
	        RefreshStatusbar(Track);
			RefreshTableMain();
	        RefreshResume();
	        RefreshStat(false);

	        //Refresh the marker position on the map
			RefreshCurrentPosMarker(Track.data.get(0).getLatitude(), Track.data.get(0).getLongitude());
		}
	}




	/**
	 * Save the selected data to disk in GPX format
	 */
	private void SavePartGPX() {
		String s, ext;

		if (Track.data.isEmpty())
			return;

		s = Utils.SaveDialog(this, Settings.LastDir, "", ".gpx", bundle.getString("frmMain.GPXFile"), true,
				bundle.getString("frmMain.FileExist"));
						
		if (!s.isEmpty()) {
			// -- Save track
    		int start = TableMain.getSelectedRow();
    		int end = start+TableMain.getSelectedRowCount();

    		Track.SaveGPX(s, start, end);
			// -- Store the directory
			Settings.LastDir = Utils.GetDirFromFilename(s);

			// -- Update GPX MRU
			AddMruGPX(s);
			RefreshMruGPX();

			// -- We don't reset the track modified flag because we save only a part of the track!
			//Track.isModified = false;

			// -- Refresh info panel
			RefreshStatusbar(Track);
		}
	}




	/**
	 * Save the selected data to disk in CGX format
	 */
	private void SavePartCGX() {
		String s, ext;

		if (Track.data.isEmpty())
			return;

		s = Utils.SaveDialog(this, Settings.LastDir, "", ".cgx", bundle.getString("frmMain.CGXFile"), true,
				bundle.getString("frmMain.FileExist"));
						
		if (!s.isEmpty()) {
			// -- Save track
    		int start = TableMain.getSelectedRow();
    		int end = start+TableMain.getSelectedRowCount();

			Track.SaveCGX(s, start, end);
			// -- Store the directory
			Settings.LastDir = Utils.GetDirFromFilename(s);

			// -- Update GPX MRU
			AddMruCGX(s);
			RefreshMruCGX();

			// -- We don't reset the track modified flag because we save only a part of the track!
			//Track.isModified = false;

			// -- Refresh info panel
			RefreshStatusbar(Track);
		}
	}




	/**
	 * Separator for the status bar
	 * @return Separator object
	 */
	static JComponent createStatusbarSeparator() {
        JSeparator x = new JSeparator(SwingConstants.VERTICAL);
        x.setPreferredSize(new Dimension(3,20));
        return x;
    }
	
	
	/**
	 * Create the status bar
	 */
	private void Create_Statusbar() {
		StatusBar = new javax.swing.JPanel();
		FlowLayout fl = new FlowLayout(FlowLayout.RIGHT);
		fl.setVgap(0);
		StatusBar.setLayout(fl);

		//-- Separator
		StatusBar.add(createStatusbarSeparator());
				
		// -- Total distance
		// ----------------------------------------------------
		LbInfoTotalDist = new javax.swing.JLabel();
		LbInfoTotalDist
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/distance.png")));
		StatusBar.add(LbInfoTotalDist);
		

		// -- Total distance - value
		// ----------------------------------------------------
		LbInfoTotalDistVal = new javax.swing.JLabel();
		StatusBar.add(LbInfoTotalDistVal);

		//-- Separator
		StatusBar.add(createStatusbarSeparator());
		
		// -- Ascent
		// ------------------------------------------------------
		LbInfoDp = new javax.swing.JLabel();
		LbInfoDp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/dp.png")));
		StatusBar.add(LbInfoDp);

		// -- Ascent value
		// ------------------------------------------------------
		LbInfoDpVal = new javax.swing.JLabel();
		StatusBar.add(LbInfoDpVal);

		//-- Separator
		StatusBar.add(createStatusbarSeparator());
		
		// -- Descent
		// -----------------------------------------------------
		LbInfoDm = new javax.swing.JLabel();
		LbInfoDm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/dm.png")));
		StatusBar.add(LbInfoDm);
		
		// -- Descent value
		// -----------------------------------------------------
		LbInfoDmVal = new javax.swing.JLabel();
		StatusBar.add(LbInfoDmVal);

		//-- Separator
		StatusBar.add(createStatusbarSeparator());
		
		// -- Total time
		// --------------------------------------------------------
		LbInfoTime = new javax.swing.JLabel();
		LbInfoTime
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/chronometer.png")));
		StatusBar.add(LbInfoTime);
		
		// -- Total time value
		// --------------------------------------------------------
		LbInfoTimeVal = new javax.swing.JLabel();
		StatusBar.add(LbInfoTimeVal);
		
		//-- Separator
		StatusBar.add(createStatusbarSeparator());

		// -- Curve
		// --------------------------------------------------------
		LbInfoCurve = new javax.swing.JLabel();
		LbInfoCurve
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/curve.png")));
		StatusBar.add(LbInfoCurve);
		
		// -- Curve value
		// --------------------------------------------------------
		LbInfoCurveVal = new javax.swing.JLabel();
		StatusBar.add(LbInfoCurveVal);
		
		//-- Separator
		StatusBar.add(createStatusbarSeparator());

		// -- Time limit
		// --------------------------------------------------------
		LbTimeLimit = new javax.swing.JLabel(" "+bundle.getString("frmMain.LbTimeLimit.text")+" ");
		LbTimeLimit.setOpaque(true);
		LbTimeLimit.setBackground(Color.RED);
		LbTimeLimit.setForeground(Color.WHITE);
		StatusBar.add(LbTimeLimit);
		
		//-- Separator
		sepTimeLimit=createStatusbarSeparator();
		StatusBar.add(sepTimeLimit);

		// -- Modified
		// --------------------------------------------------------
		LbModified = new javax.swing.JLabel();
		LbModified
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/edit.png")));
		StatusBar.add(LbModified);
		
		// -- Modified status
		// --------------------------------------------------------
		LbModifiedVal = new javax.swing.JLabel();
		StatusBar.add(LbModifiedVal);
		
		//-- Separator
		StatusBar.add(createStatusbarSeparator());
		
		// -- Calculation needed
		// ------------------------------------------------
		LbInfoCalculate = new javax.swing.JLabel();
		LbInfoCalculate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/calc.png")));
		StatusBar.add(LbInfoCalculate);
		
		// -- Calculation needed value
		// ------------------------------------------------
		LbInfoCalculateVal = new javax.swing.JLabel();
		StatusBar.add(LbInfoCalculateVal);

		//-- Separator
		StatusBar.add(createStatusbarSeparator());
		
		// -- Internet connection present
		// ----------------------------------------
		LbInfoInternet = new javax.swing.JLabel();
		LbInfoInternet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/earth.png")));
		StatusBar.add(LbInfoInternet);
		
		// -- Internet connection present value
		// ----------------------------------------
		LbInfoInternetVal = new javax.swing.JLabel();

		StatusBar.add(LbInfoInternetVal);
		
		//-- Separator
		StatusBar.add(createStatusbarSeparator());
		
		// -- Unit
		// ----------------------------------------
		LbInfoUnit = new javax.swing.JLabel();
		LbInfoUnit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/unit.png")));
		StatusBar.add(LbInfoUnit);
		
		// -- Unit value
		// ----------------------------------------
		LbInfoUnitVal = new javax.swing.JLabel();
		StatusBar.add(LbInfoUnitVal);
		
	}
	
	/**
	 * Create the Main toolbar
	 */
	private void Create_MainToolbar() {
		ToolBarMain = new javax.swing.JToolBar();
		ToolBarMain.setFloatable(false);
		ToolBarMain.setRollover(true);

		// -- Open GPX
		// ----------------------------------------------------------
		btOpenGPX = new javax.swing.JButton();
		btOpenGPX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/openGPX.png")));
		btOpenGPX.setToolTipText(bundle.getString("frmMain.btOpenGPX.toolTipText"));
		btOpenGPX.setFocusable(false);
		btOpenGPX.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				OpenGPXDialog();
				// btOpenGPXActionPerformed(evt);
			}
		});
		ToolBarMain.add(btOpenGPX);

		// -- Open CGX
		// ----------------------------------------------------------
		btOpenCGX = new javax.swing.JButton();
		btOpenCGX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/openCGX.png")));
		btOpenCGX.setToolTipText(bundle.getString("frmMain.btOpenCGX.toolTipText"));
		btOpenCGX.setFocusable(false);
		btOpenCGX.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				OpenCGXDialog();
			}
		});
		ToolBarMain.add(btOpenCGX);

		// -- Separator
		// ---------------------------------------------------------
		ToolBarMain.add(new javax.swing.JToolBar.Separator());

		// -- Save CGX
		// ----------------------------------------------------------
		btSaveCGX = new javax.swing.JButton();
		btSaveCGX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/saveCGX.png")));
		btSaveCGX.setToolTipText(bundle.getString("frmMain.btSaveCGX.toolTipText"));
		btSaveCGX.setFocusable(false);
		btSaveCGX.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SaveCGX();
			}
		});
		ToolBarMain.add(btSaveCGX);

		// -- Separator
		// ---------------------------------------------------------
		ToolBarMain.add(new javax.swing.JToolBar.Separator());

		// -- Undo
		// --------------------------------------------------------------
		btUndo = new javax.swing.JButton();
		btUndo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/undo.png")));
		btUndo.setToolTipText(bundle.getString("frmMain.btUndo.toolTipText"));
		btUndo.setFocusable(false);
		btUndo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// btOpenCGXActionPerformed(evt); //TODO
			}
		});
		btUndo.setEnabled(false);
		ToolBarMain.add(btUndo);

		// -- Separator
		// ---------------------------------------------------------
		ToolBarMain.add(new javax.swing.JToolBar.Separator());

		// -- Search
		// ------------------------------------------------------------
		btSearch = new javax.swing.JButton();
		btSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/search.png")));
		btSearch.setToolTipText(bundle.getString("frmMain.btSearch.toolTipText"));
		btSearch.setFocusable(false);
		btSearch.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SearchPointDialog();
			}
		});
		ToolBarMain.add(btSearch);

		// -- Previous mark
		// -----------------------------------------------------
		btGotoPreviousMark = new javax.swing.JButton();
		btGotoPreviousMark
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/prev.png")));
		btGotoPreviousMark.setToolTipText(bundle.getString("frmMain.btGotoPreviousMark.toolTipText"));
		btGotoPreviousMark.setFocusable(false);
		btGotoPreviousMark.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				int p = GotoPrevTag();
				if ((p >= 0) && (p < Track.data.size()))
					RefreshCurrentPosMarker(Track.data.get(p).getLatitude(), Track.data.get(p).getLongitude());
			}
		});
		ToolBarMain.add(btGotoPreviousMark);

		// -- Next mark
		// ---------------------------------------------------------
		btGotoNextMark = new javax.swing.JButton();
		btGotoNextMark.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/next.png")));
		btGotoNextMark.setToolTipText(bundle.getString("frmMain.btGotoNextMark.toolTipText"));
		btGotoNextMark.setFocusable(false);
		btGotoNextMark.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				int p = GotoNextTag();
				if ((p >= 0) && (p < Track.data.size()))
					RefreshCurrentPosMarker(Track.data.get(p).getLatitude(), Track.data.get(p).getLongitude());
			}
		});
		ToolBarMain.add(btGotoNextMark);

		// -- Separator
		// ---------------------------------------------------------
		ToolBarMain.add(new javax.swing.JToolBar.Separator());

		// -- Mini roadbook
		// ------------------------------------------------
		btMiniRoadbook = new javax.swing.JButton();
		btMiniRoadbook
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/profil.png")));
		btMiniRoadbook.setToolTipText(bundle.getString("frmMain.btMiniRoadbook.toolTipText"));
		btMiniRoadbook.setFocusable(false);
		btMiniRoadbook.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ShowMRB();
			}
		});
		ToolBarMain.add(btMiniRoadbook);

		// -- Display S/S curves
		// ------------------------------------------------
		btDisplaySSCurves = new javax.swing.JButton();
		btDisplaySSCurves
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/curve.png")));
		btDisplaySSCurves.setToolTipText(bundle.getString("frmMain.btDisplaySSCurves.toolTipText"));
		btDisplaySSCurves.setFocusable(false);
		btDisplaySSCurves.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				EditSSCurves();
			}
		});
		ToolBarMain.add(btDisplaySSCurves);

		// -- Track settings
		// ----------------------------------------------------
		btTrackSettings = new javax.swing.JButton();
		btTrackSettings
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/settings.png")));
		btTrackSettings.setToolTipText(bundle.getString("frmMain.btTrackSettings.toolTipText"));
		btTrackSettings.setFocusable(false);
		btTrackSettings.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				TrackSettings();
			}
		});
		ToolBarMain.add(btTrackSettings);

		// -- Separator
		// ---------------------------------------------------------
		ToolBarMain.add(new javax.swing.JToolBar.Separator());

		// -- Fill difficulty
		// ----------------------------------------------------
		btFillDiff = new javax.swing.JButton();
		btFillDiff
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/fill_diff.png")));
		btFillDiff.setToolTipText(bundle.getString("frmMain.btFillDiff.toolTipText"));
		btFillDiff.setFocusable(false);
		btFillDiff.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (Track.data.isEmpty())
					return;
				
	    		int start = TableMain.getSelectedRow();
	    		int end = start+TableMain.getSelectedRowCount();

				frmFillDiff frm = new frmFillDiff();
				EditDiffResult res=frm.showDialog(Settings, Track, start, end);
				if (res.Valid) {
					for(int i=res.Start; i<=res.End;i++) {
						Track.data.get(i).setDiff(res.Difficulty);
					}

					Track.isCalculated=false;
					Track.isModified=true;
					RefreshTableMain();
					RefreshProfil();
					RefreshStatusbar(Track);
				}
			}
		});
		ToolBarMain.add(btFillDiff);

		// -- Fill coeff
		// ----------------------------------------------------
		btFillCoeff = new javax.swing.JButton();
		btFillCoeff
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/tiredness.png")));
		btFillCoeff.setToolTipText(bundle.getString("frmMain.btFillCoeff.toolTipText"));
		btFillCoeff.setFocusable(false);
		btFillCoeff.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (Track.data.size() <= 0)
					return;
				
	    		int start = TableMain.getSelectedRow();
	    		int end = start+TableMain.getSelectedRowCount();

				frmFillCoeff frm = new frmFillCoeff();
				EditCoeffResult res=frm.showDialog(Settings, Track, start, end);
				if (res.Valid) {
			        if (res.Start == res.End) {
			        	Track.data.get(res.Start).setCoeff(res.Start_Coeff);
			        }
			        else {
			        	double x1 = Track.data.get(res.Start).getTotal();//   cd.data[frm.start].Total;
			        	double y1 = res.Start_Coeff; //frm.startval;

			        	double x2 =  Track.data.get(res.End).getTotal(); //cd.data[frm.end].Total;
			        	double y2 = res.End_Coeff; //frm.endval;

			        	CalcLineResult rc = new CalcLineResult(); 
			        	rc=Utils.CalcLine(x1, y1, x2, y2, rc);
			        
			        	// Line equation calc with "Y"=distance and "X"=Coeff
			        	double x = 0.0;
			        	double offset = 0.0;
			        	double coeff=0; 
			        	
			        	for ( int i= res.Start; i<= res.End; i++) {
			        		x = Track.data.get(i).getTotal();
			        		offset = offset + Track.data.get(i).getRecovery();
			        		
			        		coeff=(rc.a * x + rc.b)+offset;
//			        		Track.data.get(i).setCoeff((rc.a * x + rc.b)+offset);
			          
			        		//Validity tests
			        		if (coeff > CgConst.MAX_COEFF) coeff=CgConst.MAX_COEFF;
			        		if (coeff < 0) coeff=0;
			        		
			        		Track.data.get(i).setCoeff(coeff);
			        	}
			        }
			        
					Track.isCalculated=false;
					Track.isModified=true;
					RefreshTableMain();
					RefreshProfil();
					RefreshStatusbar(Track);
				}
			}
		});
		ToolBarMain.add(btFillCoeff);
		
		// -- Separator
		// ---------------------------------------------------------
		ToolBarMain.add(new javax.swing.JToolBar.Separator());
		
		// -- Calculate track time
		// ----------------------------------------------
		btCalculateTrackTime = new javax.swing.JButton();
		btCalculateTrackTime
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/refresh.png")));
		btCalculateTrackTime.setToolTipText(bundle.getString("frmMain.btCalculateTrackTime.toolTipText"));
		btCalculateTrackTime.setFocusable(false);
		btCalculateTrackTime.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				CalcTrackTime();
			}
		});
		ToolBarMain.add(btCalculateTrackTime);

	}

	
	/**
	 * Display the mini roadbook
	 */
	protected void ShowMRB() {
		if (Track.data.isEmpty())
			return;
		
		FrmMiniroadbook frm = new FrmMiniroadbook(Settings);
		frm.showDialog(Track);
//		if (res.Valid)  {
//			for(int i=res.Start; i<=res.End;i++) {
//				Track.data.get(i).setDiff(res.Difficulty);
//			}
//
//			Track.isCalculated=false;
//			Track.isModified=true;
//			RefreshTableMain();
//			RefreshProfil();
//			RefreshStatusbar(Track);
//		}
	}




	protected void TrackSettings() {
		if (Track.data.size() <= 0)
			return;

		frmTrackSettings frm= new frmTrackSettings();
		if (frm.showDialog(Settings,Track)) {
	        RefreshTableMain();
	        RefreshProfil();
			RefreshTitle();
			RefreshStatusbar(Track);
			Track.isModified=true;
			Track.isCalculated=false;
		}
	}




	private void EditSSCurves() {
		frmEditCurve frm= new frmEditCurve();
		frm.showDialog(Track);
		RefreshStatusbar(Track);
	}

	/**
	 * Create the profil toolbar
	 */
	private void Create_Profil_Toolbar() {
		ToolBarProfil = new javax.swing.JToolBar();
		ToolBarProfil.setOrientation(javax.swing.SwingConstants.VERTICAL);
		ToolBarProfil.setFloatable(false);
		ToolBarProfil.setRollover(true);

		// -- Save
		// --------------------------------------------------------------
		btProfilSave = new javax.swing.JButton();
		btProfilSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/save.png")));
		btProfilSave.setToolTipText(bundle.getString("frmMain.btProfilSave.toolTipText"));
		btProfilSave.setFocusable(false);
		btProfilSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// btOpenCGXActionPerformed(evt); //TODO
			}
		});
		btProfilSave.setEnabled(false);
		ToolBarProfil.add(btProfilSave);

		// -- Separator
		// ---------------------------------------------------------
		ToolBarProfil.add(new javax.swing.JToolBar.Separator());

		// -- Zoom X
		// ------------------------------------------------------------
		btProfilZoomX = new javax.swing.JButton();
		btProfilZoomX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/zoom_x.png")));
		btProfilZoomX.setToolTipText(bundle.getString("frmMain.btProfilZoomX.toolTipText"));
		btProfilZoomX.setFocusable(false);
		btProfilZoomX.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// btOpenCGXActionPerformed(evt); //TODO
			}
		});
		btProfilZoomX.setEnabled(false);
		ToolBarProfil.add(btProfilZoomX);

		// -- Zoom Y
		// ------------------------------------------------------------
		btProfilZoomY = new javax.swing.JButton();
		btProfilZoomY.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/zoom_y.png")));
		btProfilZoomY.setToolTipText(bundle.getString("frmMain.btProfilZoomY.toolTipText"));
		btProfilZoomY.setFocusable(false);
		btProfilZoomY.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// btOpenCGXActionPerformed(evt); //TODO
			}
		});
		btProfilZoomY.setEnabled(false);
		ToolBarProfil.add(btProfilZoomY);

		// -- Separator
		// ---------------------------------------------------------
		ToolBarProfil.add(new javax.swing.JToolBar.Separator());

		// -- Settings
		// ----------------------------------------------------------
		btProfilSettings = new javax.swing.JButton();
		btProfilSettings
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/settings.png")));
		btProfilSettings.setToolTipText(bundle.getString("frmMain.btProfilSettings.toolTipText"));
		btProfilSettings.setFocusable(false);
		btProfilSettings.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// btOpenCGXActionPerformed(evt); //TODO
			}
		});
		btProfilSettings.setEnabled(false);
		ToolBarProfil.add(btProfilSettings);

	}

	/**
	 * Create the status toolbar
	 */
	private void Create_Statistic_Toolbar() {
		ToolBarStatistic = new javax.swing.JToolBar();
		ToolBarStatistic.setOrientation(javax.swing.SwingConstants.HORIZONTAL);
		ToolBarStatistic.setFloatable(false);
		ToolBarStatistic.setRollover(true);

		// -- Save
		// --------------------------------------------------------------
		btStatisticSave = new javax.swing.JButton();
		btStatisticSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/save.png")));
		btStatisticSave.setToolTipText(bundle.getString("frmMain.btStatisticSave.toolTipText"));
		btStatisticSave.setFocusable(false);
		btStatisticSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// btOpenCGXActionPerformed(evt); //TODO
			}
		});
		btStatisticSave.setEnabled(false);
		ToolBarStatistic.add(btStatisticSave);

		// -- Separator
		// ---------------------------------------------------------
		ToolBarStatistic.add(new javax.swing.JToolBar.Separator());

		// -- Refresh
		// --------------------------------------------------------------
		btStatisticRefresh = new javax.swing.JButton();
		btStatisticRefresh
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/refresh.png")));
		btStatisticRefresh.setToolTipText(bundle.getString("frmMain.btStatisticRefresh.toolTipText"));
		btStatisticRefresh.setFocusable(false);
		btStatisticRefresh.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// btOpenCGXActionPerformed(evt); //TODO
			}
		});
		btStatisticRefresh.setEnabled(false);
		ToolBarStatistic.add(btStatisticRefresh);

	}

	/**
	 * Create resume toolbar
	 */
	private void Create_Resume_Toolbar() {
		ToolBarResume = new javax.swing.JToolBar();
		ToolBarResume.setOrientation(javax.swing.SwingConstants.HORIZONTAL);
		ToolBarResume.setFloatable(false);
		ToolBarResume.setRollover(true);

		// -- Save
		// --------------------------------------------------------------
		btResumeSave = new javax.swing.JButton();
		btResumeSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/save.png")));
		btResumeSave.setToolTipText(bundle.getString("frmMain.btResumeSave.toolTipText"));
		btResumeSave.setFocusable(false);
		btResumeSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// btOpenCGXActionPerformed(evt); //TODO
			}
		});
		btResumeSave.setEnabled(false);
		ToolBarResume.add(btResumeSave);

		// -- Separator
		// ---------------------------------------------------------
		ToolBarResume.add(new javax.swing.JToolBar.Separator());

		// -- Refresh
		// --------------------------------------------------------------
		btRefreshRefresh = new javax.swing.JButton();
		btRefreshRefresh
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/refresh.png")));
		btRefreshRefresh.setToolTipText(bundle.getString("frmMain.btRefreshRefresh.toolTipText"));
		btRefreshRefresh.setFocusable(false);
		btRefreshRefresh.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				RefreshResume();
			}
		});
		ToolBarResume.add(btRefreshRefresh);

	}

	/**
	 * Create the map toolbar
	 */
	private void Create_Map_Toolbar() {
		jToolBarMapViewer = new javax.swing.JToolBar();
		jToolBarMapViewer.setFloatable(false);
		jToolBarMapViewer.setOrientation(javax.swing.SwingConstants.VERTICAL);
		jToolBarMapViewer.setRollover(true);

		// -- Add marker
		btMapAddMarker = new javax.swing.JButton();
		btMapAddMarker
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/marker.png")));
		btMapAddMarker.setToolTipText(bundle.getString("frmMain.btMapAddMarker.toolTipText"));
		btMapAddMarker.setFocusable(false);
		btMapAddMarker.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// btOpenCGXActionPerformed(evt); //TODO
			}
		});
		btMapAddMarker.setEnabled(false);
		jToolBarMapViewer.add(btMapAddMarker);

		// -- Hide marker
		btMapHideMarker = new javax.swing.JButton();
		btMapHideMarker
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/hide_marker.png")));
		btMapHideMarker.setToolTipText(bundle.getString("frmMain.btMapHideMarker.toolTipText"));
		btMapHideMarker.setFocusable(false);
		btMapHideMarker.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// btOpenCGXActionPerformed(evt); //TODO
			}
		});
		btMapHideMarker.setEnabled(false);
		jToolBarMapViewer.add(btMapHideMarker);

		// -- Separator
		jToolBarMapViewer.add(new javax.swing.JToolBar.Separator());

		// -- Undo
		btMapUndo = new javax.swing.JButton();
		btMapUndo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/undo.png")));
		btMapUndo.setToolTipText(bundle.getString("frmMain.btMapUndo.toolTipText"));
		btMapUndo.setFocusable(false);
		btMapUndo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// btOpenCGXActionPerformed(evt); //TODO
			}
		});
		btMapUndo.setEnabled(false);
		jToolBarMapViewer.add(btMapUndo);

		// -- Separator
		jToolBarMapViewer.add(new javax.swing.JToolBar.Separator());

		// -- Track very easy
		btMapTrackVeryEasy = new javax.swing.JButton();
		btMapTrackVeryEasy.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/track_very_easy.png")));
		btMapTrackVeryEasy.setToolTipText(bundle.getString("frmMain.btMapTrackVeryEasy.toolTipText"));
		btMapTrackVeryEasy.setFocusable(false);
		btMapTrackVeryEasy.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// btOpenCGXActionPerformed(evt); //TODO
			}
		});
		btMapTrackVeryEasy.setEnabled(false);
		jToolBarMapViewer.add(btMapTrackVeryEasy);

		// -- Track easy
		btMapTrackEasy = new javax.swing.JButton();
		btMapTrackEasy
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/track_easy.png")));
		btMapTrackEasy.setToolTipText(bundle.getString("frmMain.btMapTrackEasy.toolTipText"));
		btMapTrackEasy.setFocusable(false);
		btMapTrackEasy.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// btOpenCGXActionPerformed(evt); //TODO
			}
		});
		btMapTrackEasy.setEnabled(false);
		jToolBarMapViewer.add(btMapTrackEasy);

		// -- Track average
		btMapTrackAverage = new javax.swing.JButton();
		btMapTrackAverage.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/track_average.png")));
		btMapTrackAverage.setToolTipText(bundle.getString("frmMain.btMapTrackAverage.toolTipText"));
		btMapTrackAverage.setFocusable(false);
		btMapTrackAverage.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// btOpenCGXActionPerformed(evt); //TODO
			}
		});
		btMapTrackAverage.setEnabled(false);
		jToolBarMapViewer.add(btMapTrackAverage);

		// -- Track hard
		btMapTrackHard = new javax.swing.JButton();
		btMapTrackHard
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/track_hard.png")));
		btMapTrackHard.setToolTipText(bundle.getString("frmMain.btMapTrackHard.toolTipText"));
		btMapTrackHard.setFocusable(false);
		btMapTrackHard.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// btOpenCGXActionPerformed(evt); //TODO
			}
		});
		btMapTrackHard.setEnabled(false);
		jToolBarMapViewer.add(btMapTrackHard);

		// -- Track average
		btMapTrackVeryHard = new javax.swing.JButton();
		btMapTrackVeryHard.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/track_very_hard.png")));
		btMapTrackVeryHard.setToolTipText(bundle.getString("frmMain.btMapTrackVeryHard.toolTipText"));
		btMapTrackVeryHard.setFocusable(false);
		btMapTrackVeryHard.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// btOpenCGXActionPerformed(evt); //TODO
			}
		});
		btMapTrackVeryHard.setEnabled(false);
		jToolBarMapViewer.add(btMapTrackVeryHard);

		// -- Separator
		jToolBarMapViewer.add(new javax.swing.JToolBar.Separator());

		// -- Mark
		btMapMark = new javax.swing.JButton();
		btMapMark.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/flag.png")));
		btMapMark.setToolTipText(bundle.getString("frmMain.btMapMark.toolTipText"));
		btMapMark.setFocusable(false);
		btMapMark.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// btOpenCGXActionPerformed(evt); //TODO
			}
		});
		btMapMark.setEnabled(false);
		jToolBarMapViewer.add(btMapMark);

		// -- Eat
		btMapEat = new javax.swing.JButton();
		btMapEat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/eat.png")));
		btMapEat.setToolTipText(bundle.getString("frmMain.btMapEat.toolTipText"));
		btMapEat.setFocusable(false);
		btMapEat.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// btOpenCGXActionPerformed(evt); //TODO
			}
		});
		btMapEat.setEnabled(false);
		jToolBarMapViewer.add(btMapEat);

		// -- Drink
		btMapDrink = new javax.swing.JButton();
		btMapDrink.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/drink.png")));
		btMapDrink.setToolTipText(bundle.getString("frmMain.btMapDrink.toolTipText"));
		btMapDrink.setFocusable(false);
		btMapDrink.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// btOpenCGXActionPerformed(evt); //TODO
			}
		});
		btMapDrink.setEnabled(false);
		jToolBarMapViewer.add(btMapDrink);

	}

	/**
	 * Add a tab to JTabbedPane. The icon is at the left of the text and there
	 * some space between the icon and the label
	 * 
	 * @param tabbedPane
	 *            JTabbedPane where we want to add the tab
	 * @param tab
	 *            Tab to add
	 * @param title
	 *            Title of the tab
	 * @param icon
	 *            Icon of the tab
	 */
	private void addTab(JTabbedPane tabbedPane, Component tab, String title, Icon icon) {
		tabbedPane.add(tab);

		// Create bespoke component for rendering the tab.
		javax.swing.JLabel lbl = new javax.swing.JLabel(title);
		lbl.setIcon(icon);

		// Add some spacing between text and icon, and position text to the RHS.
		lbl.setIconTextGap(5);
		lbl.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

		tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, lbl);
	}

	/**
	 * This method is called to initialize the form.
	 */
	private void initComponents() {
		// -- Main windows
		// ------------------------------------------------------
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		// java.util.ResourceBundle bundle =
		// java.util.ResourceBundle.getBundle("course_generator/Bundle");
		setTitle(bundle.getString("frmMain.title"));
		setIconImages(null);
		// setName("FrameMain");
		// setPreferredSize(new java.awt.Dimension(812, 800));
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				formWindowClosing(evt);
			}
		});

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		// paneGlobal.setLayout(new GridBagLayout());
		paneGlobal.setLayout(new BorderLayout());

		// -- Menu bar
		// ----------------------------------------------------------
		Create_MenuBarMain();

		// -- Main toolbar
		// ------------------------------------------------------
		Create_MainToolbar();
		paneGlobal.add(ToolBarMain, BorderLayout.NORTH);

		// -- Status bar
		// ------------------------------------------------------
		Create_Statusbar();
		paneGlobal.add(StatusBar, BorderLayout.SOUTH);
		
		// -- Main split bar (vertical)
		// -----------------------------------------
		SplitPaneMain = new javax.swing.JSplitPane();
		paneGlobal.add(SplitPaneMain, BorderLayout.CENTER);

		// -- Left side of the split bar
		// ----------------------------------------
		jPanelLeft = new javax.swing.JPanel();
		jPanelLeft.setLayout(new java.awt.BorderLayout());

		// -- Add the left panel to the main split panel
		// ------------------------
		SplitPaneMain.setLeftComponent(jPanelLeft);

		// -- Content of the tree
		javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode(
				"Course Generator");
		javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Parcours");
		javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Utmb 2011");
		javax.swing.tree.DefaultMutableTreeNode treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Prévu");
		treeNode3.add(treeNode4);
		treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Fait");
		treeNode3.add(treeNode4);
		treeNode2.add(treeNode3);
		treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Montagnard");
		treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Prévu");
		treeNode3.add(treeNode4);
		treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Fait");
		treeNode3.add(treeNode4);
		treeNode2.add(treeNode3);
		treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("UCDHL2008");
		treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Prévu");
		treeNode3.add(treeNode4);
		treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Fait");
		treeNode3.add(treeNode4);
		treeNode2.add(treeNode3);
		treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("UCDHL2009");
		treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Prévu");
		treeNode3.add(treeNode4);
		treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Fait");
		treeNode3.add(treeNode4);
		treeNode2.add(treeNode3);
		treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("UCDHL2010");
		treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Prévu");
		treeNode3.add(treeNode4);
		treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Fait");
		treeNode3.add(treeNode4);
		treeNode2.add(treeNode3);
		treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("UCDHL2011");
		treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Prévu");
		treeNode3.add(treeNode4);
		treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Fait");
		treeNode3.add(treeNode4);
		treeNode2.add(treeNode3);
		treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("UCDHL2012");
		treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Prévu");
		treeNode3.add(treeNode4);
		treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Fait");
		treeNode3.add(treeNode4);
		treeNode2.add(treeNode3);
		treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("UCDHL2013");
		treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Prévu");
		treeNode3.add(treeNode4);
		treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Fait");
		treeNode3.add(treeNode4);
		treeNode2.add(treeNode3);
		treeNode1.add(treeNode2);
		treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Configuration");
		treeNode1.add(treeNode2);

		// -- Tree
		// --------------------------------------------------------------
		jTreeMain = new javax.swing.JTree();
		jTreeMain.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
		jTreeMain.setPreferredSize(new java.awt.Dimension(109, 25));

		// -- Add the tree to a scroll panel
		// ------------------------------------
		jScrollPaneTree = new javax.swing.JScrollPane();
		jScrollPaneTree.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		jScrollPaneTree.setViewportView(jTreeMain);

		// -- Add the scroll panel to the left panel
		// ----------------------------
		jPanelLeft.add(jScrollPaneTree, java.awt.BorderLayout.CENTER);

		// -- Right split pane
		// --------------------------------------------------
		SplitPaneMainRight = new javax.swing.JSplitPane();
		SplitPaneMainRight.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
		SplitPaneMain.setRightComponent(SplitPaneMainRight);

		// -- Tabbed panel
		// ------------------------------------------------------
		TabbedPaneMain = new javax.swing.JTabbedPane();
		// -- Create the listener
		ChangeListener changeListener = new ChangeListener() {
			public void stateChanged(ChangeEvent changeEvent) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
				int index = sourceTabbedPane.getSelectedIndex();
				if (index == 4) // Tab Resume
					RefreshResume();
			}
		};
		TabbedPaneMain.addChangeListener(changeListener);

		SplitPaneMainRight.setTopComponent(TabbedPaneMain);

		// -- Tab - data (grid)
		// -------------------------------------------------
		TableMain = new javax.swing.JTable();
		TableMain.setModel(ModelTableMain);
		TableMain.getTableHeader()
				.setDefaultRenderer(new MainHeaderRenderer(TableMain.getTableHeader().getDefaultRenderer()));
		TableMain.getTableHeader().setReorderingAllowed(false);

		TableMain.setDefaultRenderer(ElevationClass.class, new ElevationRenderer());
		TableMain.setDefaultRenderer(DistClass.class, new DistRenderer());
		TableMain.setDefaultRenderer(TotalClass.class, new TotalRenderer());
		TableMain.setDefaultRenderer(DiffClass.class, new DiffRenderer());
		TableMain.setDefaultRenderer(CoeffClass.class, new CoeffRenderer());
		TableMain.setDefaultRenderer(LatClass.class, new LatRenderer());
		TableMain.setDefaultRenderer(LonClass.class, new LonRenderer());
		TableMain.setDefaultRenderer(RecupClass.class, new RecupRenderer());
		TableMain.setDefaultRenderer(TimeClass.class, new TimeRenderer());
		TableMain.setDefaultRenderer(TimelimitClass.class, new TimelimitRenderer());
		TableMain.setDefaultRenderer(HourClass.class, new HourRenderer());
		TableMain.setDefaultRenderer(StationClass.class, new StationRenderer());
		TableMain.setDefaultRenderer(TagClass.class, new TagRenderer());

		TableMain.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		TableMain.setRowHeight(20);
		TableMain.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				if 	(evt.getButton()==evt.BUTTON1 && evt.getClickCount() >= 2 && !evt.isConsumed()) {
		            evt.consume();
		    		int row = TableMain.rowAtPoint(evt.getPoint());
		    		int col = TableMain.columnAtPoint(evt.getPoint());
					frmEditPosition frm = new frmEditPosition();
					if (frm.showDialog(Settings, Track, row, col)) {
						Track.isModified=true;
						RefreshTableMain();
						RefreshProfil();
						RefreshStatusbar(Track);
					}
				}
				else
					TableMainMouseClicked(evt);
			}
		});
		TableMain.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				TableMainKeyReleased(evt);
			}
		});

		// -- Add the grid to a scroll panel
		// ------------------------------------
		jScrollPaneData = new javax.swing.JScrollPane();
		jScrollPaneData.setViewportView(TableMain);

		// -- Add the scroll panel to the tabbed panel
		// --------------------------
		addTab(TabbedPaneMain, jScrollPaneData, bundle.getString("frmMain.TabData.tabTitle"),
				new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/satellite16.png")));

		// -- Tab - Profil
		// ------------------------------------------------------
		jPanelProfil = new javax.swing.JPanel();
		jPanelProfil.setPreferredSize(new java.awt.Dimension(677, 150));
		jPanelProfil.setLayout(new java.awt.BorderLayout());

		// -- Profil tool bar
		// ---------------------------------------------------
		// Create_Profil_Toolbar();
		// jPanelProfil.add(ToolBarProfil, java.awt.BorderLayout.WEST);

		// -- Profil chart
		// ------------------------------------------------------
		jPanelProfilChart = new ChartPanel(chart);
		CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
		xCrosshair = new Crosshair(Double.NaN, Color.DARK_GRAY, new BasicStroke(0f));
		// xCrosshair.setLabelVisible(true);
		xCrosshair.setLabelBackgroundPaint(Color.WHITE);

		yCrosshair = new Crosshair(Double.NaN, Color.DARK_GRAY, new BasicStroke(0f));
		// yCrosshair.setLabelVisible(true);
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
					RefreshProfilInfo(i);
					//Refresh the position on the data grid
					TableMain.setRowSelectionInterval(i, i);
					Rectangle rect = TableMain.getCellRect(i, 0, true);
					TableMain.scrollRectToVisible(rect);
					//Refresh the marker position on the map
					RefreshCurrentPosMarker(Track.data.get(i).getLatitude(), Track.data.get(i).getLongitude());
				}
			}

			@Override
			public void chartMouseMoved(ChartMouseEvent event) {
			}
		});

		jPanelProfil.add(jPanelProfilChart, java.awt.BorderLayout.CENTER);

		// -- Profil info bar
		// ---------------------------------------------------
		jPanelProfilInfo = new javax.swing.JPanel();
		jPanelProfilInfo.setLayout(new GridBagLayout());
		jPanelProfil.add(jPanelProfilInfo, java.awt.BorderLayout.SOUTH);

		// -- Line 0
		// -- Distance
		// ----------------------------------------------------------
		lbProfilDistance = new javax.swing.JLabel();
		lbProfilDistance.setText(" " + bundle.getString("frmMain.lbProfilDistance.text") + "=0.000km ");
		lbProfilDistance.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelProfilInfo, lbProfilDistance, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH);

		// -- Time
		// --------------------------------------------------------------
		lbProfilTime = new javax.swing.JLabel();
		lbProfilTime.setText(" " + bundle.getString("frmMain.lbProfilTime.text") + "=00:00:00 ");
		lbProfilTime.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelProfilInfo, lbProfilTime, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH);

		// -- Slope
		// -------------------------------------------------------------
		lbProfilSlope = new javax.swing.JLabel();
		lbProfilSlope.setText(" " + bundle.getString("frmMain.lbProfilSlope.text") + "=0.0% ");
		lbProfilSlope.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelProfilInfo, lbProfilSlope, 2, 0, 1, 1, 0, 0, 0, 0, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH);

		// -- Name
		// --------------------------------------------------------------
		lbProfilName = new javax.swing.JLabel();
		lbProfilName.setText(" " + bundle.getString("frmMain.lbProfilName.text") + "= ");
		lbProfilName.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelProfilInfo, lbProfilName, 3, 0, 1, 1, 1, 0, 0, 0, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH);

		// -- Line 1
		// -- Elevation
		// ---------------------------------------------------------
		lbProfilElevation = new javax.swing.JLabel();
		lbProfilElevation.setText(" " + bundle.getString("frmMain.lbProfilElevation.text") + "=0m ");
		lbProfilElevation.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelProfilInfo, lbProfilElevation, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH);

		// -- Hour
		// --------------------------------------------------------------
		lbProfilHour = new javax.swing.JLabel();
		lbProfilHour.setText(" " + bundle.getString("frmMain.lbProfilHour.text") + "=00:00:00 ");
		lbProfilHour.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelProfilInfo, lbProfilHour, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH);

		// -- Speed
		// -------------------------------------------------------------
		lbProfilSpeed = new javax.swing.JLabel();
		lbProfilSpeed.setText(" " + bundle.getString("frmMain.lbProfilSpeed.text") + "=0.0km/h ");
		lbProfilSpeed.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelProfilInfo, lbProfilSpeed, 2, 1, 1, 1, 0, 0, 0, 0, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH);

		// -- Comment
		// -----------------------------------------------------------
		lbProfilComment = new javax.swing.JLabel();
		lbProfilComment.setText(" " + bundle.getString("frmMain.lbProfilComment.text") + "= ");
		lbProfilComment.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		Utils.addComponent(jPanelProfilInfo, lbProfilComment, 3, 1, 1, 1, 1, 0, 0, 0, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH);
		// -- Distance / Temps / Pente / Nom
		// -- Altitude / Heure / Vitesse / Commentaire

		// -- Add the panel to the tabbed panel
		// ---------------------------------
		addTab(TabbedPaneMain, jPanelProfil, bundle.getString("frmMain.TabProfil.tabTitle"),
				new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/profil.png")));

		// -- Tab - Statistic
		// ---------------------------------------------------
		jPanelStatistic = new javax.swing.JPanel();
		jPanelStatistic.setLayout(new java.awt.BorderLayout());

		// -- Statistic tool bar
		// ---------------------------------------------------
		Create_Statistic_Toolbar();
		jPanelStatistic.add(ToolBarStatistic, java.awt.BorderLayout.NORTH);

		// TODO Add the component to display the statistics

		addTab(TabbedPaneMain, jPanelStatistic, bundle.getString("frmMain.TabStatistic.tabTitle"),
				new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/stat.png")));

		// -- Tab - Analysis
		// ----------------------------------------------------
		jPanelAnalyze = new javax.swing.JPanel();

		// TODO Define this panel

		addTab(TabbedPaneMain, jPanelAnalyze, bundle.getString("frmMain.TabAnalyze.tabTitle"),
				new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/search.png")));

		// -- Tab - Resume
		// ------------------------------------------------------
		jPanelResume = new javax.swing.JPanel();
		jPanelResume.setLayout(new java.awt.BorderLayout());

		// -- Resume tool bar
		// ---------------------------------------------------
		Create_Resume_Toolbar();
		jPanelResume.add(ToolBarResume, java.awt.BorderLayout.NORTH);

		TableResume = new javax.swing.JTable();
		TableResume.setModel(ModelTableResume);
		TableResume.setRowHeight(20);
		TableResume.getTableHeader().setReorderingAllowed(false);

		TableResume.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);

		TableResume.getTableHeader()
				.setDefaultRenderer(new ResumeHeaderRenderer(TableResume.getTableHeader().getDefaultRenderer()));

		// TODO Change the Renderer name. Add Resume...
		TableResume.setDefaultRenderer(ResumeNumClass.class, new ResumeNumRenderer());
		TableResume.setDefaultRenderer(ResumeNameClass.class, new ResumeNameRenderer());
		TableResume.setDefaultRenderer(ResumeLineClass.class, new ResumeLineRenderer());
		TableResume.setDefaultRenderer(ResumeElevationClass.class, new ResumeElevationRenderer());
		TableResume.setDefaultRenderer(ResumeClimbPClass.class, new ResumeClimbPRenderer());
		TableResume.setDefaultRenderer(ResumeClimbNClass.class, new ResumeClimbNRenderer());
		TableResume.setDefaultRenderer(ResumeDistanceClass.class, new ResumeDistanceRenderer());
		TableResume.setDefaultRenderer(ResumeTimeClass.class, new ResumeTimeRenderer());
		TableResume.setDefaultRenderer(ResumeHourClass.class, new ResumeHourRenderer());
		TableResume.setDefaultRenderer(ResumedtTimeClass.class, new ResumedtTimeRenderer());
		TableResume.setDefaultRenderer(ResumeTimeLimitClass.class, new ResumeTimeLimitRenderer());
		TableResume.setDefaultRenderer(ResumeStationTimeClass.class, new ResumeStationTimeRenderer());
		TableResume.setDefaultRenderer(ResumedtDistanceClass.class, new ResumedtDistanceRenderer());
		TableResume.setDefaultRenderer(ResumedtClimbPClass.class, new ResumedtClimbPRenderer());
		TableResume.setDefaultRenderer(ResumedtClimbNClass.class, new ResumedtClimbNRenderer());
		TableResume.setDefaultRenderer(ResumeSpeedPClass.class, new ResumeSpeedPRenderer());
		TableResume.setDefaultRenderer(ResumeSpeedNClass.class, new ResumeSpeedNRenderer());
		TableResume.setDefaultRenderer(ResumeAvgSlopePClass.class, new ResumeAvgSlopePRenderer());
		TableResume.setDefaultRenderer(ResumeAvgSlopeNClass.class, new ResumeAvgSlopeNRenderer());
		TableResume.setDefaultRenderer(ResumeAvgSpeedClass.class, new ResumeAvgSpeedRenderer());
		TableResume.setDefaultRenderer(ResumeCommentClass.class, new ResumeCommentRenderer());

		// TableResume.addMouseListener(new java.awt.event.MouseAdapter() {
		// public void mouseClicked(java.awt.event.MouseEvent evt) {
		// TableMainMouseClicked(evt);
		// }
		// });
		// TableResume.addKeyListener(new java.awt.event.KeyAdapter() {
		// public void keyReleased(java.awt.event.KeyEvent evt) {
		// TableMainKeyReleased(evt);
		// }
		// });

		// -- Add the grid to a scroll panel
		// ------------------------------------
		jScrollPaneResume = new javax.swing.JScrollPane();
		jScrollPaneResume.setViewportView(TableResume);
		jPanelResume.add(jScrollPaneResume, java.awt.BorderLayout.CENTER);

		addTab(TabbedPaneMain, jPanelResume, bundle.getString("frmMain.TabResume.tabTitle"),
				new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/grid.png")));

		// -- Map panel
		// ---------------------------------------------------------
		jPanelMap = new javax.swing.JPanel();
		jPanelMap.setLayout(new java.awt.BorderLayout());

		Create_Map_Toolbar();
		jPanelMap.add(jToolBarMapViewer, java.awt.BorderLayout.WEST);

		MapViewer = new org.openstreetmap.gui.jmapviewer.JMapViewer();
		MapViewer.setMapMarkerVisible(true);
		MapViewer.setScrollWrapEnabled(true);
		MapViewer.setZoomButtonStyle(org.openstreetmap.gui.jmapviewer.JMapViewer.ZOOM_BUTTON_STYLE.VERTICAL);
		MapViewer.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				MapViewerMouseClicked(evt);
			}
		});
		jPanelMap.add(MapViewer, java.awt.BorderLayout.CENTER);

		jScrollPanelMap = new javax.swing.JScrollPane();
		jScrollPanelMap.setViewportView(jPanelMap);

		SplitPaneMainRight.setRightComponent(jScrollPanelMap);

		// -- Finished - Pack
		// ---------------------------------------------------
		pack();
	}

	/*
	 * private XYDataset CreateDataset() { XYSeries serie1 = new
	 * XYSeries("First"); serie1.add(1.0,1.0); serie1.add(2.0,1.5);
	 * serie1.add(3.0,2.5); serie1.add(4.0,2.0); serie1.add(5.0,5.0);
	 * serie1.add(6.0,7.0); serie1.add(7.0,3.0); serie1.add(8.0,6.0);
	 * serie1.add(9.0,3.0); serie1.add(10.0,3.0);
	 * 
	 * XYSeriesCollection dataset = new XYSeriesCollection();
	 * dataset.addSeries(serie1);
	 * 
	 * return dataset; }
	 */

	private JFreeChart CreateChartProfil(XYDataset dataset) {
		JFreeChart chart = ChartFactory.createXYAreaChart("", "Distance", // x
																			// axis
																			// label
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
	 * Go to the next tag
	 */
	private int GotoNextTag() {
		int p = -1;
		if (Track.data.size() > 0) {
			CgData d;

			int row = TableMain.getSelectedRow();
			int col = TableMain.getSelectedColumn();
			if ((row < 0) || (col < 0))
				return p;

			p = row + 1;

			if (p < Track.data.size()) {
				d = Track.data.get(p);

				while ((p < Track.data.size() - 1) && (d.getTag() == 0)) {
					p++;
					d = Track.data.get(p);
				}

				if (d.getTag() != 0) {
					// -- Select the line and scroll to it
					TableMain.setRowSelectionInterval(p, p);
					TableMain.scrollRectToVisible(new Rectangle(TableMain.getCellRect(p, 0, true)));
					// MainGrid.CurrentCell = MainGrid["Tag", p];
				}

			}
		}
		return p;
	}

	/**
	 * Go to the previous tag
	 */
	private int GotoPrevTag() {
		int p = -1;
		if (Track.data.size() > 0) {
			CgData d;

			int row = TableMain.getSelectedRow();
			int col = TableMain.getSelectedColumn();
			if ((row < 0) || (col < 0))
				return p;

			p = row - 1;

			if (p >= 0) {
				d = Track.data.get(p);

				while ((p > 0) && (d.getTag() == 0)) {
					p--;
					d = Track.data.get(p);
				}

				if (d.getTag() != 0) {
					// -- Select the line and scroll to it
					TableMain.setRowSelectionInterval(p, p);
					TableMain.scrollRectToVisible(new Rectangle(TableMain.getCellRect(p, 0, true)));
					// MainGrid.CurrentCell = MainGrid["Tag", p];
				}
			}
		}
		return p;
	}

	/**
	 * Display the search dialog
	 */
	private void SearchPointDialog() {
		if (Track.data.size() <= 0)
			return;

		frmSearchPoint frm = new frmSearchPoint();
		frm.showDialog(Settings, Track, this);
	}

	/**
	 * Update the profil chart
	 */
	private void RefreshProfil() {
		if (Track.data.isEmpty())
			return;

		// -- Clear all series
		if (dataset.getSeriesCount() > 0)
			dataset.removeAllSeries();

		// -- Populate the serie
		XYSeries serie1 = new XYSeries("Elevation/Distance");
		for (CgData r : Track.data) {
			serie1.add(r.getTotal() / 1000, r.getElevation()); // TODO miles/km
		}
		dataset.addSeries(serie1);

		if (Track.getMaxElev(Settings.Unit) > Track.getMinElev(Settings.Unit)) {
			XYPlot plot = chart.getXYPlot();
			ValueAxis axisY = plot.getRangeAxis();
			axisY.setRange(Math.floor(Track.getMinElev(Settings.Unit) / 100.0) * 100.0, Math.ceil(Track.getMaxElev(Settings.Unit) / 100.0) * 100.0);
		}
	}

	
	public void ImportGPX() {
		if (Track.data.isEmpty())
			return;

	    FrmImportChoice frm = new FrmImportChoice();

	    int res=frm.showDialog();
	    
	    if (res!=FrmImportChoice.RESULT_CANCEL) {
		    String s = Utils.LoadDialog(this, Settings.LastDir, ".gpx", bundle.getString("frmMain.GPXFile"));
	        if (!s.isEmpty()) {
        		int mode = FrmImportChoice.RESULT_AT_END;
        		if (res==FrmImportChoice.RESULT_AT_END)
        			mode = 1;
        		else
        			mode = 2;
        			
        		//BackupInCGX();
        		//bAutorUpdatePos = false;
        		try {
					if (Track.OpenGPX(s, mode)) {
						JOptionPane.showMessageDialog(this, bundle.getString("frmMain.NoTimeData"));
		        		RefreshTableMain();
		        		RefreshStatusbar(Track);
		        		RefreshTitle();
		        		RefreshProfil();
		        		RefreshTrack(Track);
		        		RefreshResume();
		        		RefreshStat(false);
		        		//RefreshInfoAnalyseSpeed(0);
		        		//bAutorUpdatePos = true;	            		
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}        		
	        }
	    }
        
//	    if (frm.ShowDialog() == DialogResult.OK) {
//	    	string s, ext;
//
//	    	openFileDialog.Filter = "Fichier GPX (*.gpx)|*.gpx";
//	        openFileDialog.DefaultExt = "gpx";
//	        DialogResult res = openFileDialog.ShowDialog();
//	        if (res == DialogResult.OK) {
//	        	s = openFileDialog.FileName;
//	        	if (s == "") return;
//
//	        	ext = System.IO.Path.GetExtension(s);
//	        	if (ext.ToUpper() == ".GPX") {
//	        		int mode = Constantes.IMPORT_MODE_ADD_END;
//	        		if (frm.insDeb) mode = Constantes.IMPORT_MODE_INS_START;
//
//	        		BackupInCGX();
//	        		bAutorUpdatePos = false;
//	        		if (!cd.OpenGPX(s, mode))
//	        			MessageBox.Show("Le fichier que vous avez chargé n'a pas de donnée temporelle.\nRéglez les paramètres du parcours et appuyez sur le bouton de calcul pour déterminer le temps de passage pour chaque point du parcours.", "Course Generator",
//	        					MessageBoxButtons.OK, MessageBoxIcon.Exclamation);
//
//	        		InitMainGrid();
//	        		RefreshStatusBar();
//	        		RefreshTitle();
//	        		DrawProfil(Constantes.SRC_MAIN);
//	        		DrawMap();
//	        		RefreshResume(false);
//	        		RefreshStat(false);
//	        		RefreshInfoAnalyseSpeed(0);
//	        		bAutorUpdatePos = true;
//	        		//bNoBackup = true;
//	        	}
//	        }
//	    }		
	}
	
	
	/**
	 * Refresh the fields in the profil info panel
	 * 
	 * @param index
	 *            Index of the line in the track data list
	 * 
	 */
	public void RefreshProfilInfo(int index) {
		if ((index < 0) || (index >= Track.data.size()))
			return;

		// -- Get the data
		CgData d = Track.data.get(index);

		lbProfilDistance.setText(" " + bundle.getString("frmMain.lbProfilDistance.text") + "= "
				+ d.getTotalString(Settings.Unit, true) + " ");
		lbProfilTime.setText(" " + bundle.getString("frmMain.lbProfilTime.text") + "= " + d.getTimeString() + " ");
		lbProfilSlope
				.setText(" " + bundle.getString("frmMain.lbProfilSlope.text") + "= " + d.getSlopeString(true) + " ");
		lbProfilName.setText(" " + bundle.getString("frmMain.lbProfilName.text") + "= " + d.getName() + " ");
		lbProfilElevation.setText(" " + bundle.getString("frmMain.lbProfilElevation.text") + "= "
				+ d.getElevationString(Settings.Unit, true) + " ");
		lbProfilHour.setText(" " + bundle.getString("frmMain.lbProfilHour.text") + "= " + d.getHourString() + " ");
		lbProfilSpeed.setText(" " + bundle.getString("frmMain.lbProfilSpeed.text") + "= "
				+ d.getSpeedString(Settings.Unit, true) + " ");
		lbProfilComment.setText(" " + bundle.getString("frmMain.lbProfilComment.text") + "= " + d.getComment() + " ");
	}

	/**
	 * Refresh the resume grid
	 * 
	 * @param force
	 * 
	 */
	private void RefreshResume() {
		// Exit if the tab is not displayed
		if (TabbedPaneMain.getSelectedIndex() != 4) // Resume
			return;

		// StatusBar.Items["Message"].Visible = true;
		// StatusBar.Items["Message"].Text = "Mise à jour resumé en cours...";
		// StatusBar.Refresh();

		int i = 0;
		int j = -1;
		int k = 0;
		int old = 0;
		CgData OldData;

		if (Track.data.isEmpty())
			return;
		
		CalcClimbResult ccr = new CalcClimbResult();
		CalcAvrSlopeResult casr = new CalcAvrSlopeResult();
		CalcAvrSpeedResult speedResult = new CalcAvrSpeedResult();

		Resume.data.clear();

		OldData = Track.data.get(0);

		for (CgData src : Track.data) {
			if ((src.getTag() & 32) != 0) {
				j++;
				k++;
				CgResume dst = new CgResume();
				// ResGrid.Rows.Add();

				dst.setNum(k);
				dst.setName(src.getName());
				dst.setLine(src.getNum());
				dst.setElevation(src.getElevation());

				ccr = Track.CalcClimb(0, i, ccr);
				dst.setClimbP(ccr.cp);
				dst.setClimbM(ccr.cm);

				dst.setDist(src.getTotal() / 1000.0);
				dst.setTime(src.getTime());
				dst.setHour(src.getHour());

				dst.setTimeLimit(src.getTimeLimit());
				dst.setStationTime(src.getStation());

				dst.setdTime_f(src.getTime() - OldData.getTime());
				dst.setdDist((src.getTotal() - OldData.getTotal()) / 1000.0);

				ccr = Track.CalcClimb(old, i, ccr);
				casr = Track.CalcAvrSlope(old, i, casr);
				speedResult = Track.CalcAvrSpeed(old, i, speedResult);

				dst.setdClimbP(ccr.cp);
				dst.setdClimbM(ccr.cm);

				dst.setSpeedP(ccr.cp * 3600 / Math.abs(ccr.tp));
				dst.setSpeedM(ccr.cm * 3600 / Math.abs(ccr.tm));

				// if ((AvrSlopeP == 0) || (Double.IsNaN(AvrSlopeP) ))
				dst.setAvgSlopeP(casr.AvrSlopeP);
				dst.setAvgSlopeM(casr.AvrSlopeM);

				dst.setAvgSpeed(speedResult.avrspeed);

				dst.setComment(src.getComment());

				Resume.data.add(dst);

				OldData = src;
				old = i;
			}
			i++;
		}
		// -- Refresh the grid
		TableResume.invalidate();

		// StatusBar.Items["Message"].Visible = false;
	}

	class GPX_Filter extends javax.swing.filechooser.FileFilter {

		@Override
		public boolean accept(File file) {
			// Allow only directories, or files with ".txt" extension
			return file.isDirectory() || file.getAbsolutePath().endsWith(".gpx");
		}

		@Override
		public String getDescription() {
			// This description will be displayed in the dialog,
			// hard-coded = ugly, should be done via I18N
			return "Text documents (*.gpx)";
		}
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	/**
	 * Display a dialog box to open a GPX file
	 */
	private void OpenGPXDialog() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		FileFilter gpxFilter = new FileTypeFilter(".gpx", "GPX file");
		fileChooser.addChoosableFileFilter(gpxFilter);
		fileChooser.setFileFilter(gpxFilter);

		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			LoadGPX(selectedFile.getAbsolutePath());
		}
	}

	/**
	 * Load a GPX file
	 * 
	 * @param filename
	 *            file name
	 */
	private void LoadGPX(String filename) {
		try {
			Track.OpenGPX(filename, 0);
			AddMruGPX(filename);
		} catch (Exception e) {
		}

		// -- Update the viewer
		MapViewer.removeAllMapMarkers();
		RefreshTrack(Track);
		// -- Refresh the track information
		RefreshStatusbar(Track);

		// -- Force the update of the main table
		RefreshTableMain();
		old_row = -1;

		// -- Refresh resume grid
		RefreshResume();

		RefreshMruGPX();
		RefreshProfil();
		// -- Refresh the form title
		RefreshTitle();

		if (Track.data.size() > 0)
			RefreshCurrentPosMarker(Track.data.get(0).getLatitude(), Track.data.get(0).getLongitude());
	}

	/**
	 * Save the track in GPX format
	 */
	private void SaveGPX() {
		String s, ext;

		if (Track.data.isEmpty())
			return;

		s = Utils.SaveDialog(this, Settings.LastDir, "", ".gpx", bundle.getString("frmMain.GPXFile"), true,
				bundle.getString("frmMain.FileExist"));
						
		if (!s.isEmpty()) {
			// -- Save track
			Track.SaveGPX(s, 0, Track.data.size() - 1);
			// -- Store the directory
			Settings.LastDir = Utils.GetDirFromFilename(s);

			// -- Update GPX MRU
			AddMruGPX(s);
			RefreshMruGPX();

			// -- Reset the track modified flag
			Track.isModified = false;
			// -- Refresh info panel
			RefreshStatusbar(Track);
		}
	}

	/**
	 * Display a dialog box to open a CGX file
	 */
	private void OpenCGXDialog() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		FileFilter cgxFilter = new FileTypeFilter(".cgx", "CGX file");
		fileChooser.addChoosableFileFilter(cgxFilter);
		fileChooser.setFileFilter(cgxFilter);

		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			LoadCGX(selectedFile.getAbsolutePath());
		}
	}

	/**
	 * Load a CGX file
	 * 
	 * @param filename
	 *            File name
	 */
	private void LoadCGX(String filename) {
		try {
			Track.OpenCGX(this, filename, 0);
			AddMruCGX(filename);
		} catch (Exception e) {
		}

		// -- Update the viewer
		RefreshTrack(Track);
		// -- Refresh the track information
		RefreshStatusbar(Track);
		// -- Refresh resume grid
		RefreshResume();
		// -- Refresh profil tab
		RefreshProfil();
		// -- Refresh the form title 
		RefreshTitle();
		
		// -- Force the update of the main table
		RefreshTableMain();
		RefreshMruCGX();
	}

	/**
	 * Save the track in CGX format
	 */
	private void SaveCGX() {
		String s, ext;

		if (Track.data.isEmpty())
			return;

		s = Utils.SaveDialog(this, Settings.LastDir, "", ".cgx", bundle.getString("frmMain.CGXFile"), true,
				bundle.getString("frmMain.FileExist"));
						
		if (!s.isEmpty()) {
			// -- Save track
			Track.SaveCGX(s, 0, Track.data.size() - 1);
			// -- Store the directory
			Settings.LastDir = Utils.GetDirFromFilename(s);

			// -- Update GPX MRU
			AddMruCGX(s);
			RefreshMruCGX();

			// -- Reset the track modified flag
			Track.isModified = false;
			// -- Refresh info panel
			RefreshStatusbar(Track);
		}
	}
	
	/**
	 * Load the configuration file
	 */
	private void LoadConfig() {
		Settings.Load(DataDir + "/Course Generator");
	}

	/**
	 * Save the settings in a configuration file
	 */
	private void SaveConfig() {
		// -- Get the form dimension
		Rectangle r = getBounds();
		Settings.MainWindowWidth = r.width;
		Settings.MainWindowHeight = r.height;

		Settings.VertSplitPosition = SplitPaneMain.getDividerLocation();
		Settings.HorizSplitPosition = SplitPaneMainRight.getDividerLocation();

		// -- Update the column width in the settings
		for (int i = 0; i < 16; i++)
			Settings.TableMainColWidth[i] = TableMain.getColumnModel().getColumn(i).getWidth();

		Settings.Save(DataDir + "/Course Generator");
	}

	/**
	 * Called by the quit application menu
	 * 
	 * @param evt
	 */
	private void mnuQuitActionPerformed(java.awt.event.ActionEvent evt) {
		this.processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	private void btTestActionPerformed(java.awt.event.ActionEvent evt) {
		// Bouge la carte de 100 pixel
		// MapViewer.moveMap(100, 100);

		// Essai lecture ressource image
		ImageIcon icon = createImageIcon("images/save.png", "a pretty but meaningless splat");
		// jLabelFooterTotalTime1.setIcon(icon);
		// jLabelFooterTotalTime1.setText("");
		// Fin essai lecture ressource image

		// Met un marqueur au coordonnée indiqué
		Coordinate paris = new Coordinate(48.8567, 2.3508);
		MapViewer.addMapMarker(new MapMarkerImg(paris, icon.getImage()));

		// Coordinate one = new Coordinate(48.8567, 2.3508);
		// Coordinate two = new Coordinate(48.8567, 2.4508);
		// Coordinate three = new Coordinate(48.9567, 2.4508);
		// List<Coordinate> route = new ArrayList<Coordinate>(Arrays.asList(one,
		// two, two));
		List<Coordinate> route = new ArrayList<Coordinate>();
		route.add(new Coordinate(48.8567, 2.3508));
		route.add(new Coordinate(48.8567, 2.4508));
		route.add(new Coordinate(48.9567, 2.4508));
		route.add(new Coordinate(48.9668, 2.4518));
		MapPolyLine polyLine = new MapPolyLine(route);
		polyLine.setColor(Color.red);
		MapViewer.addMapPolygon(polyLine);

		List<Coordinate> route1 = new ArrayList<Coordinate>();
		route1.add(new Coordinate(48.9668, 2.4518));
		route1.add(new Coordinate(48.9669, 2.4510));
		route1.add(new Coordinate(48.9670, 2.4508));
		route1.add(new Coordinate(48.9668, 2.4518));
		MapPolyLine polyLine1 = new MapPolyLine(route1);
		polyLine1.setColor(Color.green);
		MapViewer.addMapPolygon(polyLine1);

		// MapPolygonImpl polyLine = new MapPolygonImpl(route);

		// MapViewer.setTileSource(new Thunderforest_Landscape());
		
		//MapViewer.setTileSource(new Thunderforest_Outdoors());
		MapViewer.setTileSource(new OpenTopoMap());
		
		// MapViewer.setTileSource(new Thunderforest_Transport());

		// MapViewer.setTileSource(new OsmTileSource.Mapnik());
		// MapViewer.setTileSource(new OsmTileSource.CycleMap());
		// MapViewer.setTileSource(new BingAerialTileSource());
		// MapViewer.setTileSource(new MapQuestOsmTileSource());
		// MapViewer.setTileSource(new MapQuestOpenAerialTileSource());

	}

	// private void jBtTestActionPerformed(java.awt.event.ActionEvent evt) {
	// try {
	// Track.OpenGPX("C:/Tmp/Test.gpx", 0);
	// } catch (Exception e) {
	// }
	//
	// // -- Update the viewer
	// DisplayTrack(Track);
	//
	// // -- Force the update of the main table
	// RefreshTableMain();
	//
	// RefreshInfo(Track);
	// }

	/**
	 * Called when the main form is closing
	 * 
	 * @param evt
	 */
	private void formWindowClosing(java.awt.event.WindowEvent evt) {
		if (Track.isModified) {
			Object[] options = { " " + bundle.getString("frmMain.ClosingYes") + " ",
					" " + bundle.getString("frmMain.ClosingNo") + " " };
			int ret = JOptionPane.showOptionDialog(this, bundle.getString("frmMain.ClosingMessage"),
					bundle.getString("frmMain.ClosingTitle"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
					null, options, options[1]);

			if (ret == JOptionPane.YES_OPTION) {
				setDefaultCloseOperation(EXIT_ON_CLOSE);
				SaveConfig();
			} else {
				setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			}
		} else { // No modification! Bye
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			SaveConfig();
		}
	}
	
	/**
	 * Set form title
	 */
	private void RefreshTitle() {
		String s="Course Generator "+Version;
		if (!Track.Name.isEmpty())
			s=s+" - "+Track.Name;
		setTitle(s);
	}

	private void mnuMruGPXActionPerformed(java.awt.event.ActionEvent evt) {
		JMenuItem m = (JMenuItem) evt.getSource();
		if (m == mnuMruGPX1)
			LoadGPX(Settings.mruGPX[0]);
		else if (m == mnuMruGPX2)
			LoadGPX(Settings.mruGPX[1]);
		else if (m == mnuMruGPX3)
			LoadGPX(Settings.mruGPX[2]);
		else if (m == mnuMruGPX4)
			LoadGPX(Settings.mruGPX[3]);
		else if (m == mnuMruGPX5)
			LoadGPX(Settings.mruGPX[4]);
	}

	private void mnuMruCGXActionPerformed(java.awt.event.ActionEvent evt) {
		JMenuItem m = (JMenuItem) evt.getSource();
		if (m == mnuMruCGX1)
			LoadCGX(Settings.mruCGX[0]);
		else if (m == mnuMruCGX2)
			LoadCGX(Settings.mruCGX[1]);
		else if (m == mnuMruCGX3)
			LoadCGX(Settings.mruCGX[2]);
		else if (m == mnuMruCGX4)
			LoadCGX(Settings.mruCGX[3]);
		else if (m == mnuMruCGX5)
			LoadCGX(Settings.mruCGX[4]);
	}

	private void TableMainMouseClicked(java.awt.event.MouseEvent evt) {
		int row = TableMain.rowAtPoint(evt.getPoint());
		int col = TableMain.columnAtPoint(evt.getPoint());
		if ((row < 0) || (col < 0) || (row == old_row))
			return;
		
		old_row = row;
		if (Track.data.size() > 0) {
			// -- Refresh marker position on the map
			RefreshCurrentPosMarker(Track.data.get(row).getLatitude(), Track.data.get(row).getLongitude());
			// -- Refresh profil crooshair position and profil info
			RefreshProfilInfo(row);
			xCrosshair.setValue(Track.data.get(row).getTotal(Settings.Unit) / 1000.0);
			yCrosshair.setValue(Track.data.get(row).getElevation(Settings.Unit));
		}			
	}

	private void TableMainKeyReleased(java.awt.event.KeyEvent evt) {
		int row = TableMain.getSelectedRow();
		int col = TableMain.getSelectedColumn();
		if ((row < 0) || (col < 0) || (row == old_row))
			return;
		old_row = row;
		if (Track.data.size() > 0)
			RefreshCurrentPosMarker(Track.data.get(row).getLatitude(), Track.data.get(row).getLongitude());
	}

	private void MapViewerMouseClicked(java.awt.event.MouseEvent evt) {
		if (Track.data.size() <= 0)
			return;
		
		//Find the nearest point
		Coordinate c = MapViewer.getPosition(evt.getX(), evt.getY());
		int i = Track.FindNearestPoint(c.getLat(), c.getLon());
		//Selection the position on the data grid
		TableMain.setRowSelectionInterval(i, i);
		Rectangle rect = TableMain.getCellRect(i, 0, true);
		TableMain.scrollRectToVisible(rect);

		//Refresh profil position
		RefreshProfilInfo(i);
		xCrosshair.setValue(Track.data.get(i).getTotal(Settings.Unit) / 1000.0);
		yCrosshair.setValue(Track.data.get(i).getElevation(Settings.Unit));

		//Refresh position marker on the map
		RefreshCurrentPosMarker(Track.data.get(i).getLatitude(), Track.data.get(i).getLongitude());
	}

	/**
	 * Display track stored in a TrackData class
	 * 
	 * @param tdata
	 *            TrackData object to display
	 */
	private void RefreshTrack(TrackData tdata) {
		// -- Remove the previous track
		MapViewer.removeAllMapPolygons();

		/*
		 * //-- Create the route List<Coordinate> route1 = new
		 * ArrayList<Coordinate>(); for (CgData r: tdata.data) { route1.add(new
		 * Coordinate(r.Latitude, r.Longitude)); } MapPolyLine polyLine1 = new
		 * MapPolyLine(route1); //-- Set the line color
		 * polyLine1.setColor(Color.MAGENTA); //-- Set the line width
		 * polyLine1.setStroke(new BasicStroke(3)); //-- Upddate the viewer
		 * MapViewer.addMapPolygon(polyLine1); //-- Zoom to display the track
		 * MapViewer.setDisplayToFitMapPolygons();
		 */

		// -- Create the route
		List<Coordinate> route1 = new ArrayList<Coordinate>();
		double last_diff = tdata.data.get(0).getDiff();
		for (CgData r : tdata.data) {
			if (r.getDiff() == last_diff) {
				route1.add(new Coordinate(r.getLatitude(), r.getLongitude()));
			} else {
				route1.add(new Coordinate(r.getLatitude(), r.getLongitude()));
				MapPolyLine polyLine1 = new MapPolyLine(route1);
				// -- Set the line color
				if (last_diff >= 98.0)
					polyLine1.setColor(Color.GREEN);
				else if (last_diff >= 95.0)
					polyLine1.setColor(Color.BLUE);
				else if (last_diff >= 88)
					polyLine1.setColor(Color.RED);
				else
					polyLine1.setColor(Color.BLACK);

				polyLine1.setStroke(new BasicStroke(3));
				// -- Upddate the viewer
				MapViewer.addMapPolygon(polyLine1);

				route1 = new ArrayList<Coordinate>();
				route1.add(new Coordinate(r.getLatitude(), r.getLongitude()));
			}
			last_diff = r.getDiff();
		}
		// -- Add the last polyline
		MapPolyLine polyLine1 = new MapPolyLine(route1);
		// -- Set the line color
		if (last_diff >= 98.0)
			polyLine1.setColor(Color.GREEN);
		else if (last_diff >= 95.0)
			polyLine1.setColor(Color.BLUE);
		else if (last_diff >= 88)
			polyLine1.setColor(Color.RED);
		else
			polyLine1.setColor(Color.BLACK);
		// -- Set the stroke
		polyLine1.setStroke(new BasicStroke(3));
		// -- Upddate the viewer
		MapViewer.addMapPolygon(polyLine1);

		// -- Zoom to display the track
		MapViewer.setDisplayToFitMapPolygons();
		
		
		for (CgData r : tdata.data) {
			int t=r.getTag();
			int v=0;
			if ((t & CgConst.TAG_MARK)!=0)
				v=v+1;
			if ((t & CgConst.TAG_EAT_PT)!=0)
				v=v+2;
			if ((t & CgConst.TAG_WATER_PT)!=0)
				v=v+4;
			
			if (v!=0) 
				MapViewer.addMapMarker(new MapMarkerImg(new Coordinate(r.getLatitude(), r.getLongitude()), createImageIcon("images/markers_"+v+".png", "").getImage()));
		}		
	}

	
	/**
	 * Refresh the statusbar
	 * 
	 * @param tdata
	 *            Track data object
	 */
	private void RefreshStatusbar(TrackData tdata) {
				
		// -- Distance
		LbInfoTotalDistVal.setText(String.format("%1.3f ", tdata.getTotalDistance(Settings.Unit) / 1000.0)
				+ Settings.getDistanceUnitString());

		// -- Ascent
		LbInfoDpVal.setText(String.format("%1.0f ", tdata.ClimbP) + Settings.getElevationUnitString());

		// -- Descent
		LbInfoDmVal.setText(String.format("%1.0f ", tdata.ClimbM) + Settings.getElevationUnitString());

		// -- Time
		int nbh = tdata.TotalTime / 3600;
		int nbm = (tdata.TotalTime % 3600) / 60;
		int nbs = (tdata.TotalTime % 3600) % 60;
		LbInfoTimeVal.setText(String.format("%02d:%02d:%02d ", nbh, nbm, nbs));

		// -- Curve
		LbInfoCurveVal.setText(tdata.Paramfile);
		
		// -- Time limit
		LbTimeLimit.setVisible(Track.isTimeLimit);
		sepTimeLimit.setVisible(Track.isTimeLimit);
		
		// -- Modified
		if (Track.isModified) {
			LbModifiedVal.setText(bundle.getString("frmMain.LbModified_Modified.text"));
		}
		else {
			LbModifiedVal.setText(bundle.getString("frmMain.LbModified_Ok.text"));
		}
			
		
		// -- Calculation
		if (Track.isCalculated) {
			LbInfoCalculateVal.setText(bundle.getString("frmMain.LbInfoCalculateOk.text"));
			LbInfoCalculateVal.setBackground(new Color(112, 232, 6));
		} else {
			LbInfoCalculateVal.setText(bundle.getString("frmMain.LbInfoCalculateTodo.text"));
			LbInfoCalculateVal.setBackground(new Color(255, 209, 7));
		}

		// -- Internet
		if (InternetConnectionActive) {
			LbInfoInternetVal.setText(bundle.getString("frmMain.LbInfoInternetOnline.text"));
			LbInfoInternetVal.setBackground(new Color(112, 232, 6));
		} else {
			LbInfoInternetVal.setText(bundle.getString("frmMain.LbInfoInternetOffline.text"));
			LbInfoInternetVal.setBackground(new Color(255, 209, 7));
		}

		// -- Unit
		switch (Settings.Unit) {
		case CgConst.UNIT_METER:
			LbInfoUnitVal.setText(bundle.getString("frmMain.LbInfoUnitMeter.text"));
			break;
		case CgConst.UNIT_MILES_FEET:
			LbInfoUnitVal.setText(bundle.getString("frmMain.LbInfoUnitMilesFeet.text"));
			break;
		default:
			LbInfoUnitVal.setText(bundle.getString("frmMain.LbInfoUnitMeter.text"));
		}
	}

	/**
	 * Refresh the content of the data table
	 */
	private void RefreshTableMain() {
		int r=TableMain.getSelectedRow();
		ModelTableMain.fireTableDataChanged();
		if (r>=0)
			TableMain.setRowSelectionInterval(r, r);
	}

	
	/**
	 * Refresh the content of the resume table
	 */
	private void RefreshTableResume() {
		int r=TableResume.getSelectedRow();
		ModelTableResume.fireTableDataChanged();
		if (r>=0)
			TableResume.setRowSelectionInterval(r, r);
	}
	
	
	/**
	 * Refresh the position of the marker on the map
	 * @param lat
	 *            latitude of the position of the marker
	 * @param lon
	 *            longitude of the position of the marker
	 */
	public void RefreshCurrentPosMarker(double lat, double lon) {
		if (CurrentPosMarker == null) {
			// -- Define the current position marker
			CurrentPosMarker = new MapMarkerImg(new Coordinate(lat, lon),
					createImageIcon("images/current_marker.png", "").getImage());
			MapViewer.addMapMarker(CurrentPosMarker);
		} else {
			CurrentPosMarker.setLat(lat);
			CurrentPosMarker.setLon(lon);
			MapViewer.setDisplayPosition(new Coordinate(lat, lon), MapViewer.getZoom());
		}
	}

	
	/**
	 * Refresh the GPX most recent used files menu
	 */
	private void RefreshMruGPX() {
		// -- Mru 1
		if (Settings.mruGPX[0].isEmpty()) {
			mnuMruGPX1.setVisible(false);
		} else {
			mnuMruGPX1.setText(Settings.mruGPX[0]);
			mnuMruGPX1.setVisible(true);
		}

		// -- Mru 2
		if (Settings.mruGPX[1].isEmpty()) {
			mnuMruGPX2.setVisible(false);
		} else {
			mnuMruGPX2.setText(Settings.mruGPX[1]);
			mnuMruGPX2.setVisible(true);
		}

		// -- Mru 3
		if (Settings.mruGPX[2].isEmpty()) {
			mnuMruGPX3.setVisible(false);
		} else {
			mnuMruGPX3.setText(Settings.mruGPX[2]);
			mnuMruGPX3.setVisible(true);
		}

		// -- Mru 4
		if (Settings.mruGPX[3].isEmpty()) {
			mnuMruGPX4.setVisible(false);
		} else {
			mnuMruGPX4.setText(Settings.mruGPX[3]);
			mnuMruGPX4.setVisible(true);
		}

		// -- Mru 5
		if (Settings.mruGPX[4].isEmpty()) {
			mnuMruGPX5.setVisible(false);
		} else {
			mnuMruGPX5.setText(Settings.mruGPX[4]);
			mnuMruGPX5.setVisible(true);
		}

	}

	
	/**
	 * Add "filename" to the CGX mru menu
	 * @param filename
	 *            name of the file to add
	 */
	public void AddMruGPX(String filename) {
		int i;
		for (i = 4; i >= 0; i--) {
			if (filename.equalsIgnoreCase(Settings.mruGPX[i]))
				return;
		}
		for (i = 4; i > 0; i--)
			Settings.mruGPX[i] = Settings.mruGPX[i - 1];
		Settings.mruGPX[0] = filename;
	}

	
	/**
	 * Refresh the CGX most recent used files menu
	 */
	private void RefreshMruCGX() {
		// -- Mru 1
		if (Settings.mruCGX[0].isEmpty()) {
			mnuMruCGX1.setVisible(false);
		} else {
			mnuMruCGX1.setText(Settings.mruCGX[0]);
			mnuMruCGX1.setVisible(true);
		}

		// -- Mru 2
		if (Settings.mruCGX[1].isEmpty()) {
			mnuMruCGX2.setVisible(false);
		} else {
			mnuMruCGX2.setText(Settings.mruCGX[1]);
			mnuMruCGX2.setVisible(true);
		}

		// -- Mru 3
		if (Settings.mruCGX[2].isEmpty()) {
			mnuMruCGX3.setVisible(false);
		} else {
			mnuMruCGX3.setText(Settings.mruCGX[2]);
			mnuMruCGX3.setVisible(true);
		}

		// -- Mru 4
		if (Settings.mruCGX[3].isEmpty()) {
			mnuMruCGX4.setVisible(false);
		} else {
			mnuMruCGX4.setText(Settings.mruCGX[3]);
			mnuMruCGX4.setVisible(true);
		}

		// -- Mru 5
		if (Settings.mruCGX[4].isEmpty()) {
			mnuMruCGX5.setVisible(false);
		} else {
			mnuMruCGX5.setText(Settings.mruCGX[4]);
			mnuMruCGX5.setVisible(true);
		}

	}

	/**
	 * Add "filename" to the CGX mru menu
	 * @param filename
	 *            name of the file to add
	 */
	public void AddMruCGX(String filename) {
		int i;
		for (i = 4; i >= 0; i--) {
			if (filename.equalsIgnoreCase(Settings.mruCGX[i]))
				return;
		}
		for (i = 4; i > 0; i--)
			Settings.mruCGX[i] = Settings.mruCGX[i - 1];
		Settings.mruCGX[0] = filename;
	}

	/**
	 * Set the default font
	 * 
	 * @param f
	 *            font
	 */
	public static void setUIFont(javax.swing.plaf.FontUIResource f) {
		java.util.Enumeration keys = javax.swing.UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = javax.swing.UIManager.get(key);
			if (value != null && value instanceof javax.swing.plaf.FontUIResource) {
				javax.swing.UIManager.put(key, f);
			}
		}
	}

	/**
	 * Everything start here!!!
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(final String args[]) {
		try {
			// Set System L&F
			OsCheck.OSType ostype = OsCheck.getOperatingSystemType();
			switch (ostype) {
			case Windows:
				for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
					if ("Windows".equals(info.getName())) {
						javax.swing.UIManager.setLookAndFeel(info.getClassName());
						break;
					} else {
						javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
					}
				}
				break;
			case MacOS:
				javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
				break;
			case Linux:
				// Toolkit.getDefaultToolkit().setDynamicLayout(true);
				// System.setProperty("sun.awt.noerasebackground", "true");
				// //JFrame.setDefaultLookAndFeelDecorated(true);
				// //JDialog.setDefaultLookAndFeelDecorated(true);

				try {
					javax.swing.UIManager.setLookAndFeel("de.muntjak.tinylookandfeel.TinyLookAndFeel");
					// continuous layout on frame resize
					Toolkit.getDefaultToolkit().setDynamicLayout(true);
					// no flickering on resize
					System.setProperty("sun.awt.noerasebackground", "true");
					// Theme.loadTheme(de.muntjak.tinylookandfeel.TinyLookAndFeel.class.getResource("/themes/CG_Gray.theme"));
					// Theme.loadTheme(course_generator.class().getResource("/course_generator/CG_Gray.theme"));
				} catch (Exception ex) {
					ex.printStackTrace();
					javax.swing.UIManager.getSystemLookAndFeelClassName();
				}

				// javax.swing.UIManager.setLookAndFeel("de.muntjak.tinylookandfeel.TinyLookAndFeel");
				// boolean found = false;
				// for (javax.swing.UIManager.LookAndFeelInfo info :
				// javax.swing.UIManager.getInstalledLookAndFeels()) {
				// if ("Nimbus".equals(info.getName())) {
				// javax.swing.UIManager.setLookAndFeel(info.getClassName());
				// found = true;
				// break;
				// }
				// }
				// if (!found)
				// javax.swing.UIManager.getSystemLookAndFeelClassName();
				break;
			case Other:
				javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
				break;
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}

		// -- Set thing according to the OS
		OsCheck.OSType ostype = OsCheck.getOperatingSystemType();
		switch (ostype) {
		case Windows:
			// setUIFont(new javax.swing.plaf.FontUIResource("Tahoma",
			// Font.PLAIN, 12));
			break;
		case MacOS:
			// setUIFont(new javax.swing.plaf.FontUIResource("Arial",
			// Font.PLAIN, 12));
			break;
		case Linux:
			// setUIFont(new javax.swing.plaf.FontUIResource("Arial",
			// Font.PLAIN, 12));
			break;
		case Other:
			// setUIFont(new javax.swing.plaf.FontUIResource("Arial",
			// Font.PLAIN, 12));
			break;
		}

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new frmMain(args).setVisible(true);
			}
		});
	}

	private javax.swing.JLabel LbInfoCalculate;
	private javax.swing.JLabel LbInfoDm;
	private javax.swing.JLabel LbInfoDp;
	private javax.swing.JLabel LbInfoInternet;
	private javax.swing.JLabel LbInfoTime;
	private javax.swing.JLabel LbInfoTotalDist;
	private org.openstreetmap.gui.jmapviewer.JMapViewer MapViewer;
	private javax.swing.JSplitPane SplitPaneMain;
	private javax.swing.JSplitPane SplitPaneMainRight;
	private javax.swing.JTabbedPane TabbedPaneMain;
	public javax.swing.JTable TableMain;
	private javax.swing.JButton Test2;
	private javax.swing.JToolBar ToolBarMain;
	private javax.swing.JButton btOpenCGX;
	private javax.swing.JButton btOpenGPX;
	private javax.swing.JButton btTest;
	private javax.swing.JButton jBtTest;
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JMenu mnuFile;
	private javax.swing.JMenu mnuEdit;
	private javax.swing.JPanel jPanelAnalyze;
	private javax.swing.JPanel jPanelInfo;
	private javax.swing.JPanel jPanelLeft;
	private javax.swing.JPanel jPanelMap;
	private javax.swing.JPanel jPanelProfil;
	// private javax.swing.JPanel jPanelProfilChart;
	private javax.swing.JPanel jPanelProfilInfo;
	private javax.swing.JPanel jPanelResume;
	private javax.swing.JPanel jPanelStatistic;
	private javax.swing.JScrollPane jScrollPaneData;
	private javax.swing.JScrollPane jScrollPaneTree;
	private javax.swing.JPopupMenu.Separator jSeparator1;
	private javax.swing.JToolBar jToolBarMapViewer;
	private javax.swing.JToolBar ToolBarProfil;
	private javax.swing.JTree jTreeMain;
	private javax.swing.JMenu mnuLastCGX;
	private javax.swing.JMenu mnuLastGPX;
	private javax.swing.JMenuBar mnuMain;
	private javax.swing.JMenuItem mnuMruCGX1;
	private javax.swing.JMenuItem mnuMruCGX2;
	private javax.swing.JMenuItem mnuMruCGX3;
	private javax.swing.JMenuItem mnuMruCGX4;
	private javax.swing.JMenuItem mnuMruCGX5;
	private javax.swing.JMenuItem mnuMruGPX1;
	private javax.swing.JMenuItem mnuMruGPX2;
	private javax.swing.JMenuItem mnuMruGPX3;
	private javax.swing.JMenuItem mnuMruGPX4;
	private javax.swing.JMenuItem mnuMruGPX5;
	private javax.swing.JMenuItem mnuOpenCGX;
	private javax.swing.JMenuItem mnuOpenGPX;
}
