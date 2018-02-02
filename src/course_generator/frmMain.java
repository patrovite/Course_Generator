/*
 * Course Generator - Main form
 * Copyright (C) 2008-2018 Pierre Delore
 *
 * Contributor(s) :
 * Frédéric (frederic@freemovin.com)
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
 * Settings for the development :
 * - In order to have the log message in the eclipse console set in the 
 * run configuration "-DrunInEclipse=true" in Arguments/VM argument 
 * otherwise the message go to a log file 
 * 
 * Used libraries:
 *  - Joda-time - http://www.joda.org/joda-time/
 *  - SwingX - LGPL 2.1 - https://swingx.java.net/
 *  - JMapViewer - GPL - http://wiki.openstreetmap.org/wiki/JMapViewer
 *  - jcommon - LGPL - http://www.jfree.org/jcommon/
 *  - jfreechart - LGPL - http://www.jfree.org/index.html
 *  - TinyLaF - LGPL - Hans Bickel - http://www.muntjak.de/hans/java/tinylaf/ 
 *  - SunCalculator - Patrick Kalkman - pkalkie@gmail.com
 *  - Log4j - Apache V2 - https://logging.apache.org/log4j
 *  
 * Copyrights:
 * Maps :
 * - Openstreetmap : http://www.openstreetmap.org/
 * - OpenTopoMap : https://opentopomap.org/
 * - Bing map : (c) Microsoft
 */

/*
 * IN PROGRESS:
 *
 * Hot:
 *
 * TODO:
 * - DistNear & DistFar in frmSettings
 * - Dialog to display the tile directory size and a button to empty the directory
 * - Statistic: Add the highest ascend and descend
 * - Analyze : bar chart speed/km or miles
 * - Analyze : Click in charts sometime doesn't works. Probably due to "find the nearest point" function
 * - Crosshair on curve editor => it select the line on the grid 
 * 
 * Notes:
 * Ctrl+C shortcut doesn't work because it probably managed directly by the JTable. Currently there is no shortcut.
 * 
 */

package course_generator;

import static course_generator.dialogs.frmAbout.showDialogAbout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.OsmFileCacheTileLoader;

import course_generator.TrackData.CalcClimbResult;
import course_generator.TrackData.SearchPointResult;
import course_generator.analysis.JPanelAnalysisSpeed;
import course_generator.analysis.JPanelAnalysisSpeedSlope;
import course_generator.analysis.JPanelAnalysisTimeDist;
import course_generator.dialogs.FrmExportWaypoints;
import course_generator.dialogs.FrmImportChoice;
import course_generator.dialogs.frmEditPosition;
import course_generator.dialogs.frmExportPoints;
import course_generator.dialogs.frmFillCoeff;
import course_generator.dialogs.frmFillCoeff.EditCoeffResult;
import course_generator.dialogs.frmFillDiff;
import course_generator.dialogs.frmFillDiff.EditDiffResult;
import course_generator.dialogs.frmSearchPoint;
import course_generator.dialogs.frmSearchPointListener;
import course_generator.dialogs.frmTrackSettings;
import course_generator.import_points.frmImportPoints;
import course_generator.maps.JPanelMaps;
import course_generator.maps.JPanelMapsListener;
import course_generator.mrb.FrmMiniroadbook;
import course_generator.param.frmEditCurve;
import course_generator.profil.JPanelProfil;
import course_generator.profil.JPanelProfilListener;
//import course_generator.resume.JPanelListener;
import course_generator.resume.JPanelResume;
import course_generator.resume.JPanelResumeListener;
import course_generator.settings.CgSettings;
import course_generator.settings.frmSettings;
import course_generator.statistics.JPanelStatistics;
import course_generator.trackdata.JPanelTrackData;
import course_generator.trackdata.JPanelTrackDataListener;
import course_generator.trackdata_table.TrackDataModel;
import course_generator.utils.CgConst;
import course_generator.utils.CgLog;
import course_generator.utils.FileTypeFilter;
import course_generator.utils.OsCheck;
import course_generator.utils.Utils;
import course_generator.utils.Utils.CalcLineResult;

/**
 * This is the main class of the project.
 *
 * @author pierre.delore
 */
public class frmMain extends javax.swing.JFrame
{
	private final String Version = "4.0.0.BETA 8";

	public static boolean inEclipse = false;
	public static CgLog log = null;

	public TrackData Track, Backup_Track;
	private ResumeData Resume;
	private final TrackDataModel ModelTableMain;
	public CgSettings Settings;
	public String DataDir;
	public String ProgDir;
	private java.util.ResourceBundle bundle = null;
	private int cmptInternetConnexion = 0;
	private int cmptMinute = 0;
	private boolean InternetConnectionActive = false;
	private Timer timer1s; // 1 second timer object
	private boolean bNoBackup = true;
	private String StrMapsDirSize = "";
	private String Lang4Help = "";
	// private boolean showProfilMarker = true;

	/**
	 * Creates new form frmMain
	 */
	OsmFileCacheTileLoader offlineTileCache;
	DefaultTableModel model;
	private static JMenuItem mnuSaveCGX;
	private static JMenuItem mnuSaveGPX;
	private static JMenuItem mnuSaveCSV;
	private static JMenuItem mnuSavePartCGX;
	private static JMenuItem mnuSavePartGPX;
	private static JMenuItem mnuSavePartCSV;
	private JMenuItem mnuImportPoints;
	private static JMenuItem mnuExportPoints;
	private static JMenuItem mnuExportTagAsWaypoints;
	private JMenuItem mnuOffLine;
	private JMenuItem mnuQuit;
	private static JMenuItem mnuCopy;
	private JMenuItem mnuSelectLines;
	private static JMenuItem mnuSearchPoint;
	private static JMenuItem mnuMarkPosition;
	private static JMenuItem mnuGotoNextMark;
	private static JMenuItem mnuGotoPrevMark;
	private JMenu mnuDisplay;
	private JMenuItem mnuHTMLReport;
	private JMenuItem mnuGenerateKML;
	private static JMenuItem mnuGenerateRoadbook;
	private static JMenuItem mnuGenerateMiniRoadbook;
	private JMenuItem mnuDisplaySpeed;
	private JMenuItem mnuDisplaySlope;
	private JMenu mnuTools;
	private static JMenuItem mnuCalculateTrackTime;
	private static JMenuItem mnuFindMinMax;
	private static JMenuItem mnuInvertTrack;
	private static JMenuItem mnuDefineNewStart;
	private JMenuItem mnuInternetTools;
	private JMenuItem mnuDisplaySSDir;
	private JMenu mnuSettings;
	private static JMenuItem mnuTrackSettings;
	private static JMenuItem mnuSpeedSlopeCurves;
	private JMenuItem mnuCGSettings;
	private JMenu mnuHelp;
	private JMenuItem mnuCGHelp;
	private JMenuItem mnuReward;
	private JMenuItem mnuAbout;
	private static JButton btSaveCGX;
	private static JButton btUndo;
	private static JButton btSearch;
	private static JButton btGotoPreviousMark;
	private static JButton btGotoNextMark;
	private static JButton btDisplaySSCurves;
	private static JButton btTrackSettings;
	private static JButton btCalculateTrackTime;
	private JLabel LbInfoUnit;
	private JLabel LbInfoTotalDistVal;
	private JLabel LbInfoDpVal;
	private JLabel LbInfoDmVal;
	private JLabel LbInfoTimeVal;
	private JLabel LbInfoCalculateVal;
	private JLabel LbInfoInternetVal;
	private JLabel LbInfoUnitVal;

	private JPanel StatusBar;
	private JLabel LbInfoCurve;
	private JLabel LbInfoCurveVal;
	private JLabel LbModified;
	private JLabel LbModifiedVal;
	private static JButton btFillDiff;
	private static JButton btFillCoeff;
	private JLabel LbTimeLimit;
	private JComponent sepTimeLimit;
	private static JButton btMiniRoadbook;
	private JMenuItem mnuImportGPX;
	private JMenuItem mnuImportCGX;

	private JTabbedPane TabbedPaneAnalysis;
	private JPanelAnalysisTimeDist jPanelTimeDist;
	private JPanelAnalysisSpeed jPanelSpeed;
	private JPanelAnalysisSpeedSlope jPanelSpeedSlope;
	// private JButton btMapOfflineSelection;

	private JPanelResume PanelResume;

	private JPanelStatistics panelStatistics;

	private JPanelProfil panelProfil;

	private JPanelTrackData panelTrackData;

	private JPanelMaps panelMap;

	private JLabel LbInfoMapDirSize;

	private JMenuItem mnuCGWebsite;

	// -- Called every second
	class TimerActionListener implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
			cmptInternetConnexion++;
			if (cmptInternetConnexion > Settings.ConnectionTimeout)
			{
				cmptInternetConnexion = 0;
				InternetConnectionActive = Utils.isInternetReachable();
				RefreshStatusbar(Track);
			}
			cmptMinute++;
			if (cmptMinute > 60)
			{
				cmptMinute = 0;
				// -- Check every minute if we need to switch log file
				CgLog.checkFileSize();
				CheckOfflineMapsSize();
			}

		}
	}

	/**
	 * Creates new form frmMain !!!! Everything start here !!!!
	 */
	public frmMain(String args[])
	{
		// -- Get the current time to measure the initialization time
		long ts = System.currentTimeMillis();

		// -- Initialize data dir
		DataDir = Utils.GetHomeDir();

		// -- Initialize program dir
		ProgDir = new File(".").getAbsolutePath();
		ProgDir = ProgDir.replaceAll("\\\\", "/");
		if (ProgDir.endsWith("/."))
			ProgDir = ProgDir.substring(0, ProgDir.length() - 2);

		// -- Initialize data
		Track = new TrackData();
		Backup_Track = new TrackData();
		Resume = new ResumeData();
		Settings = new CgSettings();
		ModelTableMain = new TrackDataModel(Track, Settings);
		// showProfilMarker=true;

		// -- Load configuration
		LoadConfig();

		CgLog.info("Start Course Generator version " + Version);
		CgLog.info("Java version : " + System.getProperty("java.version"));
		CgLog.info("java.runtime.name : "
				+ System.getProperty("java.runtime.name"));
		CgLog.info(
				"java.vm.version : " + System.getProperty("java.vm.version"));
		CgLog.info("java.vm.vendor : " + System.getProperty("java.vm.vendor"));
		CgLog.info("path.separator : " + System.getProperty("path.separator"));
		CgLog.info("user.country : " + System.getProperty("user.country"));
		CgLog.info("java.runtime.version : "
				+ System.getProperty("java.runtime.version"));
		CgLog.info("os.arch : " + System.getProperty("os.arch"));
		CgLog.info("os.name : " + System.getProperty("os.name"));
		CgLog.info("user.timezone : " + System.getProperty("user.timezone"));
		CgLog.info("file.encoding : " + System.getProperty("file.encoding"));
		CgLog.info("java.specification.version : "
				+ System.getProperty("java.specification.version"));
		CgLog.info("java.vm.specification.version : "
				+ System.getProperty("java.vm.specification.version"));
		CgLog.info("sun.arch.data.model : "
				+ System.getProperty("sun.arch.data.model"));
		CgLog.info("user.language : " + System.getProperty("user.language"));
		CgLog.info("java.version : " + System.getProperty("java.version"));
		CgLog.info("java.vendor : " + System.getProperty("java.vendor"));
		CgLog.info("file.separator : " + System.getProperty("file.separator"));
		CgLog.info("sun.cpu.endian : " + System.getProperty("sun.cpu.endian"));
		CgLog.info("sun.desktop : " + System.getProperty("sun.desktop"));
		CgLog.info(
				"sun.cpu.isalist : " + System.getProperty("sun.cpu.isalist"));
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		CgLog.info("Screen size : " + screen.width + "x" + screen.height);
		CgLog.info("AppDir = " + ProgDir);

		// -- List the java properties
		// -- To activate only if necessary. It talks a lot!
		// System.getProperties().list(System.out);

		// -- Set the language
		CgLog.info("System language : " + Locale.getDefault().toString());
		// -- Set the language
		if (Settings.Language.isEmpty())
		{
			// -- System language
			CgLog.info("Configured language : System");
		}
		else
		{
			CgLog.info("Configured language : " + Settings.Language);
			if (Settings.Language.equalsIgnoreCase("FR"))
			{
				Locale.setDefault(Locale.FRANCE);
			}
			else if (Settings.Language.equalsIgnoreCase("EN"))
			{
				Locale.setDefault(Locale.US);
			}
			else
			{
				Locale.setDefault(Locale.US);
			}
		}

		CgLog.info("Selected language : " + Locale.getDefault().toString());

		// -- Select the language for help
		String tmpLang = Locale.getDefault().getLanguage();
		if (tmpLang.equalsIgnoreCase("fr"))
			Lang4Help = "fr";
		else
			Lang4Help = "en";

		// -- Set default font
		setUIFont(
				new javax.swing.plaf.FontUIResource("Tahoma", Font.PLAIN, 14));

		// -- Initialize the string resource for internationalization
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");

		// -- Configure the main form
		initComponents();

		// -- Set the icon of the application
		setIconImage(createImageIcon("/course_generator/images/cg.png", "")
				.getImage());

		// -- Set the preferred column width
		panelTrackData.setColumnWidth();

		// -- Set the windows size and center it in the screen - Not tested on
		// multi-screen configuration
		// -- Not currently but I leave the code for the moment
		/*
		 * Rectangle r = getBounds(); r.width = Settings.MainWindowWidth;
		 * r.height = Settings.MainWindowHeight; Dimension screensize =
		 * Toolkit.getDefaultToolkit().getScreenSize(); r.x = (screensize.width
		 * - r.width) / 2; r.y = (screensize.height - r.height) / 2;
		 * setBounds(r);
		 */

		// -- Maximize the window
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);

		// -- Check the maps dir size
		CheckOfflineMapsSize();

		// -- Set the horizontal splitter position
		SplitPaneMainRight.setDividerLocation(Settings.HorizSplitPosition);

		// -- Set the vertical splitter position (currently because the feature
		// is not implemented)
		// SplitPaneMain.setDividerLocation(Settings.VertSplitPosition);
		SplitPaneMain.setDividerLocation(0);

		// -- Configure the tile source for the map
		panelMap.RefreshMapType();

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
		panelMap.RefreshMapButtons();
		panelProfil.RefreshProfilButtons();

		ExportCurvesFromResource();

		// -- Display the splash screen
		showDialogAbout(this, true, false, Version);

		// -- Log the initialization time
		CgLog.info("Application initialization time : "
				+ (System.currentTimeMillis() - ts) + "ms");
	}

	/**
	 * If the "default.par" file is missing copy the curves files from the
	 * resource to the config directory
	 */
	private void ExportCurvesFromResource()
	{

		String dst = DataDir + "/" + CgConst.CG_DIR + "/";
		if (!Utils.FileExist(
				DataDir + "/" + CgConst.CG_DIR + "/" + "Default.par"))
		{
			if (JOptionPane.showConfirmDialog(this,
					bundle.getString("frmMain.QuestionInstallCurves"), "",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
			{

				CgLog.info("Export curves from resource");

				try
				{
					Utils.ExportResource(this,
							"/course_generator/curves/Default.par",
							dst + "Default.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_10_5km_h.par",
							dst + "Run_10_5km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_10km_h.par",
							dst + "Run_10km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_11_5km_h.par",
							dst + "Run_11_5km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_11km_h.par",
							dst + "Run_11km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_12_5km_h.par",
							dst + "Run_12_5km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_12km_h.par",
							dst + "Run_12km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_13_5km_h.par",
							dst + "Run_13_5km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_13km_h.par",
							dst + "Run_13km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_14_5km_h.par",
							dst + "Run_14_5km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_14km_h.par",
							dst + "Run_14km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_15_5km_h.par",
							dst + "Run_15_5km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_15km_h.par",
							dst + "Run_15km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_16_5km_h.par",
							dst + "Run_16_5km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_16km_h.par",
							dst + "Run_16km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_17_5km_h.par",
							dst + "Run_17_5km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_17km_h.par",
							dst + "Run_17km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_18km_h.par",
							dst + "Run_18km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_4_5km_h.par",
							dst + "Run_4_5km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_4km_h.par",
							dst + "Run_4km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_5_5km_h.par",
							dst + "Run_5_5km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_5km_h.par",
							dst + "Run_5km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_6_5km_h.par",
							dst + "Run_6_5km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_6km_h.par",
							dst + "Run_6km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_7_5km_h.par",
							dst + "Run_7_5km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_7km_h.par",
							dst + "Run_7km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_8_5km_h.par",
							dst + "Run_8_5km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_8km_h.par",
							dst + "Run_8km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_9_5km_h.par",
							dst + "Run_9_5km_h.par");
					Utils.ExportResource(this,
							"/course_generator/curves/Run_9km_h.par",
							dst + "Run_9km_h.par");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Launch the calculation on the track
	 */
	private void CalcTrackTime()
	{
		if (Track.data.isEmpty())
			return;

		Track.Calculate();

		RefreshStatusbar(Track);

		CalcClimbResult ccr = new CalcClimbResult();
		ccr = Track.CalcClimb(0, Track.data.size() - 1, ccr);
		Track.setClimbP(ccr.cp);
		Track.setClimbM(ccr.cm);
		Track.AscTime = ccr.tp;
		Track.DescTime = ccr.tm;

		Track.CheckTimeLimit();

		Track.isCalculated = true;
		Track.isModified = true;

		// -- Refresh statusbar
		RefreshStatusbar(Track);
		panelTrackData.refresh();
		PanelResume.refresh();
		panelStatistics.refresh();
	}

	/**
	 * Create the main menu
	 */

	private void Create_MenuBarMain()
	{
		// Create the menu
		mnuMain = new javax.swing.JMenuBar();
		mnuMain.setName("mnuMain");

		// == File menu ========================================================
		mnuFile = new javax.swing.JMenu();
		mnuFile.setText(bundle.getString("frmMain.mnuFile.text"));

		// -- Open GPX
		mnuOpenGPX = new javax.swing.JMenuItem();
		mnuOpenGPX.setAccelerator(
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O,
						java.awt.event.InputEvent.CTRL_MASK
								| java.awt.event.InputEvent.SHIFT_MASK));
		mnuOpenGPX.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/course_generator/images/openGPX.png")));
		mnuOpenGPX.setText(bundle.getString("frmMain.mnuOpenGPX.text"));
		mnuOpenGPX.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				OpenGPXDialog();
			}
		});
		mnuFile.add(mnuOpenGPX);

		// -- Open CGX
		mnuOpenCGX = new javax.swing.JMenuItem();
		mnuOpenCGX.setAccelerator(
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O,
						java.awt.event.InputEvent.CTRL_MASK));
		mnuOpenCGX.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/course_generator/images/openCGX.png")));
		mnuOpenCGX.setText(bundle.getString("frmMain.mnuOpenCGX.text"));
		mnuOpenCGX.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				OpenCGXDialog();
			}
		});
		mnuFile.add(mnuOpenCGX);

		// -- Recent GPX files
		// --------------------------------------------------
		mnuLastGPX = new javax.swing.JMenu();
		mnuLastGPX.setText(bundle.getString("frmMain.mnuLastGPX.text"));

		// -- Mru GPX n°1
		mnuMruGPX1 = new javax.swing.JMenuItem();
		mnuMruGPX1.setText(bundle.getString("frmMain.mnuMruGPX1.text"));
		mnuMruGPX1.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				mnuMruGPXActionPerformed(evt);
			}
		});
		mnuLastGPX.add(mnuMruGPX1);

		// -- Mru GPX n°2
		mnuMruGPX2 = new javax.swing.JMenuItem();
		mnuMruGPX2.setText(bundle.getString("frmMain.mnuMruGPX2.text"));
		mnuMruGPX2.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				mnuMruGPXActionPerformed(evt);
			}
		});
		mnuLastGPX.add(mnuMruGPX2);

		// -- Mru GPX n°3
		mnuMruGPX3 = new javax.swing.JMenuItem();
		mnuMruGPX3.setText(bundle.getString("frmMain.mnuMruGPX3.text"));
		mnuMruGPX3.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				mnuMruGPXActionPerformed(evt);
			}
		});
		mnuLastGPX.add(mnuMruGPX3);

		// -- Mru GPX n°4
		mnuMruGPX4 = new javax.swing.JMenuItem();
		mnuMruGPX4.setText(bundle.getString("frmMain.mnuMruGPX4.text"));
		mnuMruGPX4.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				mnuMruGPXActionPerformed(evt);
			}
		});
		mnuLastGPX.add(mnuMruGPX4);

		// -- Mru GPX n°5
		mnuMruGPX5 = new javax.swing.JMenuItem();
		mnuMruGPX5.setText(bundle.getString("frmMain.mnuMruGPX5.text"));
		mnuMruGPX5.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				mnuMruGPXActionPerformed(evt);
			}
		});
		mnuLastGPX.add(mnuMruGPX5);

		mnuFile.add(mnuLastGPX);

		// -- Recent CGX files
		// --------------------------------------------------
		mnuLastCGX = new javax.swing.JMenu();
		mnuLastCGX.setText(bundle.getString("frmMain.mnuLastCGX.text"));

		// -- Mru CGX n°1
		mnuMruCGX1 = new javax.swing.JMenuItem();
		mnuMruCGX1.setText(bundle.getString("frmMain.mnuMruCGX1.text"));
		mnuMruCGX1.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				mnuMruCGXActionPerformed(evt);
			}
		});
		mnuLastCGX.add(mnuMruCGX1);

		// -- Mru CGX n°2
		mnuMruCGX2 = new javax.swing.JMenuItem();
		mnuMruCGX2.setText(bundle.getString("frmMain.mnuMruCGX2.text"));
		mnuMruCGX2.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				mnuMruCGXActionPerformed(evt);
			}
		});
		mnuLastCGX.add(mnuMruCGX2);

		// -- Mru CGX n°3
		mnuMruCGX3 = new javax.swing.JMenuItem();
		mnuMruCGX3.setText(bundle.getString("frmMain.mnuMruCGX3.text"));
		mnuMruCGX3.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				mnuMruCGXActionPerformed(evt);
			}
		});
		mnuLastCGX.add(mnuMruCGX3);

		// -- Mru CGX n°4
		mnuMruCGX4 = new javax.swing.JMenuItem();
		mnuMruCGX4.setText(bundle.getString("frmMain.mnuMruCGX4.text"));
		mnuMruCGX4.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				mnuMruCGXActionPerformed(evt);
			}
		});
		mnuLastCGX.add(mnuMruCGX4);

		// -- Mru CGX n°5
		mnuMruCGX5 = new javax.swing.JMenuItem();
		mnuMruCGX5.setText(bundle.getString("frmMain.mnuMruCGX5.text"));
		mnuMruCGX5.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
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
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S,
						java.awt.event.InputEvent.CTRL_MASK));
		mnuSaveCGX.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/course_generator/images/saveCGX.png")));
		mnuSaveCGX.setText(bundle.getString("frmMain.mnuSaveCGX.text"));
		mnuSaveCGX.setEnabled(false);
		mnuSaveCGX.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				SaveCGX();
			}
		});
		mnuFile.add(mnuSaveCGX);

		// -- Save GPX
		mnuSaveGPX = new javax.swing.JMenuItem();
		mnuSaveGPX.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/course_generator/images/saveGPX.png")));
		mnuSaveGPX.setText(bundle.getString("frmMain.mnuSaveGPX.text"));
		mnuSaveGPX.setEnabled(false);
		mnuSaveGPX.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				SaveGPX();
			}
		});
		mnuFile.add(mnuSaveGPX);

		// -- Save CSV
		mnuSaveCSV = new javax.swing.JMenuItem();
		mnuSaveCSV.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/course_generator/images/saveCSV.png")));
		mnuSaveCSV.setText(bundle.getString("frmMain.mnuSaveCSV.text"));
		mnuSaveCSV.setEnabled(false);
		mnuSaveCSV.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				SaveCSV();
			}
		});
		mnuFile.add(mnuSaveCSV);

		// -- Separator
		mnuFile.add(new javax.swing.JPopupMenu.Separator());

		// -- Import GPX
		mnuImportGPX = new javax.swing.JMenuItem();
		mnuImportGPX.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/course_generator/images/openGPX.png")));
		mnuImportGPX.setText(bundle.getString("frmMain.mnuImportGPX.text"));
		mnuImportGPX.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				ImportGPX();
			}
		});
		mnuFile.add(mnuImportGPX);

		// -- Import CGX
		mnuImportCGX = new javax.swing.JMenuItem();
		mnuImportCGX.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/course_generator/images/openCGX.png")));
		mnuImportCGX.setText(bundle.getString("frmMain.mnuImportCGX.text"));
		mnuImportCGX.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				ImportCGX();
			}
		});
		mnuFile.add(mnuImportCGX);

		// -- Save a part of the track in CGX
		mnuSavePartCGX = new javax.swing.JMenuItem();
		mnuSavePartCGX.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/course_generator/images/saveCGX.png")));
		mnuSavePartCGX.setText(bundle.getString("frmMain.mnuSavePartCGX.text"));
		mnuSavePartCGX.setEnabled(false);
		mnuSavePartCGX.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				SavePartCGX();
			}
		});
		mnuFile.add(mnuSavePartCGX);

		// -- Save a part of the track in GPX
		mnuSavePartGPX = new javax.swing.JMenuItem();
		mnuSavePartGPX.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/course_generator/images/saveGPX.png")));
		mnuSavePartGPX.setText(bundle.getString("frmMain.mnuSavePartGPX.text"));
		mnuSavePartGPX.setEnabled(false);
		mnuSavePartGPX.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				SavePartGPX();
			}
		});
		mnuFile.add(mnuSavePartGPX);

		// -- Save a part of the track in CSV
		mnuSavePartCSV = new javax.swing.JMenuItem();
		mnuSavePartCSV.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/course_generator/images/saveCSV.png")));
		mnuSavePartCSV.setText(bundle.getString("frmMain.mnuSavePartCSV.text"));
		mnuSavePartCSV.setEnabled(false);
		mnuSavePartCSV.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				SavePartCSV();
			}
		});
		mnuFile.add(mnuSavePartCSV);

		// -- Separator
		mnuFile.add(new javax.swing.JPopupMenu.Separator());

		// -- Import points
		mnuImportPoints = new javax.swing.JMenuItem();
		mnuImportPoints.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/import.png")));
		mnuImportPoints
				.setText(bundle.getString("frmMain.mnuImportPoints.text"));
		mnuImportPoints.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				BackupInCGX();
				ImportPoints();
			}
		});
		mnuFile.add(mnuImportPoints);

		// -- Export points
		mnuExportPoints = new javax.swing.JMenuItem();
		mnuExportPoints.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/export.png")));
		mnuExportPoints
				.setText(bundle.getString("frmMain.mnuExportPoints.text"));
		mnuExportPoints.setEnabled(false);
		mnuExportPoints.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				ExportPoints();
			}
		});
		mnuFile.add(mnuExportPoints);

		// -- Separator
		mnuFile.add(new javax.swing.JPopupMenu.Separator());

		// -- Export tags as waypoints
		mnuExportTagAsWaypoints = new javax.swing.JMenuItem();
		mnuExportTagAsWaypoints.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/export.png")));
		mnuExportTagAsWaypoints.setText(
				bundle.getString("frmMain.mnuExportTagAsWaypoints.text"));
		mnuExportTagAsWaypoints.setEnabled(false);
		mnuExportTagAsWaypoints
				.addActionListener(new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent evt)
					{
						ExportTagsAsWaypoints();
					}
				});
		mnuFile.add(mnuExportTagAsWaypoints);

		// -- Separator
		mnuFile.add(new javax.swing.JPopupMenu.Separator());

		// -- Offline
		// mnuOffLine = new javax.swing.JMenuItem();
		// mnuOffLine.setText(bundle.getString("frmMain.mnuOffLine.text"));
		// mnuOffLine.addActionListener(new java.awt.event.ActionListener() {
		// public void actionPerformed(java.awt.event.ActionEvent evt) {
		// // mnuSaveGPXActionPerformed(evt); //TODO
		// }
		// });
		// mnuOffLine.setEnabled(false);
		// mnuFile.add(mnuOffLine);

		// -- Separator
		// mnuFile.add(new javax.swing.JPopupMenu.Separator());

		// -- Quit
		mnuQuit = new javax.swing.JMenuItem();
		mnuQuit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_F4,
				java.awt.event.InputEvent.ALT_MASK));
		mnuQuit.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/quit.png")));
		mnuQuit.setText(bundle.getString("frmMain.mnuQuit.text"));
		mnuQuit.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
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
		// mnuCopy.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C,
		// java.awt.event.InputEvent.CTRL_MASK));
		mnuCopy.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/copy.png")));
		mnuCopy.setText(bundle.getString("frmMain.mnuCopy.text"));
		mnuCopy.setEnabled(false);
		mnuCopy.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				panelTrackData.Copy2Clipboard();
			}
		});
		mnuEdit.add(mnuCopy);

		// -- Select lines...
		// mnuSelectLines = new javax.swing.JMenuItem();
		// mnuSelectLines.setText(bundle.getString("frmMain.mnuSelectLines.text"));
		// mnuSelectLines.addActionListener(new java.awt.event.ActionListener()
		// {
		// public void actionPerformed(java.awt.event.ActionEvent evt) {
		// // mnuSaveCGXActionPerformed(evt); //TODO
		// }
		// });
		// mnuSelectLines.setEnabled(false);
		// mnuEdit.add(mnuSelectLines);

		// -- Separator
		mnuEdit.add(new javax.swing.JPopupMenu.Separator());

		// -- Search a point...
		mnuSearchPoint = new javax.swing.JMenuItem();
		mnuSearchPoint.setAccelerator(
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F,
						java.awt.event.InputEvent.CTRL_MASK));
		mnuSearchPoint.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/search.png")));
		mnuSearchPoint.setText(bundle.getString("frmMain.mnuSearchPoint.text"));
		mnuSearchPoint.setEnabled(false);
		mnuSearchPoint.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				SearchPointDialog();
			}
		});
		mnuEdit.add(mnuSearchPoint);

		// -- Separator
		mnuEdit.add(new javax.swing.JPopupMenu.Separator());

		// -- Mark the current position
		mnuMarkPosition = new javax.swing.JMenuItem();
		mnuMarkPosition.setAccelerator(javax.swing.KeyStroke
				.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
		mnuMarkPosition.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/flag.png")));
		mnuMarkPosition
				.setText(bundle.getString("frmMain.mnuMarkPosition.text"));
		mnuMarkPosition.setEnabled(false);
		mnuMarkPosition.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				SetMapMarker();
			}
		});
		mnuEdit.add(mnuMarkPosition);

		// -- Go to the next mark
		mnuGotoNextMark = new javax.swing.JMenuItem();
		mnuGotoNextMark.setAccelerator(javax.swing.KeyStroke
				.getKeyStroke(java.awt.event.KeyEvent.VK_F7, 0));
		mnuGotoNextMark.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/next.png")));
		mnuGotoNextMark
				.setText(bundle.getString("frmMain.mnuGotoNextMark.text"));
		mnuGotoNextMark.setEnabled(false);
		mnuGotoNextMark.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				GotoNextTag();
			}
		});
		mnuEdit.add(mnuGotoNextMark);

		// -- Go to the previous mark
		mnuGotoPrevMark = new javax.swing.JMenuItem();
		mnuGotoPrevMark.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_F7,
				java.awt.event.InputEvent.SHIFT_MASK));
		mnuGotoPrevMark.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/prev.png")));
		mnuGotoPrevMark
				.setText(bundle.getString("frmMain.mnuGotoPrevMark.text"));
		mnuGotoPrevMark.setEnabled(false);
		mnuGotoPrevMark.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
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
		mnuHTMLReport.setAccelerator(javax.swing.KeyStroke
				.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
		mnuHTMLReport.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/html.png")));
		mnuHTMLReport.setText(bundle.getString("frmMain.mnuHTMLReport.text"));
		mnuHTMLReport.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				// mnuSaveCGXActionPerformed(evt); //TODO
			}
		});
		mnuHTMLReport.setEnabled(false);
		mnuHTMLReport.setVisible(false);
		mnuDisplay.add(mnuHTMLReport);

		// -- Separator
		// ---------------------------------------------------------
		// mnuDisplay.add(new javax.swing.JPopupMenu.Separator());

		// -- Generate KML file (Google earth)
		// ----------------------------------
		mnuGenerateKML = new javax.swing.JMenuItem();
		mnuGenerateKML.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/earth.png")));
		mnuGenerateKML.setText(bundle.getString("frmMain.mnuGenerateKML.text"));
		mnuGenerateKML.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				// mnuSaveCGXActionPerformed(evt); //TODO
			}
		});
		mnuGenerateKML.setEnabled(false);
		mnuGenerateKML.setVisible(false);
		mnuDisplay.add(mnuGenerateKML);

		// -- Separator
		// ---------------------------------------------------------
		// mnuDisplay.add(new javax.swing.JPopupMenu.Separator());

		// -- Generate a roadbook
		// -----------------------------------------------
		mnuGenerateRoadbook = new javax.swing.JMenuItem();
		mnuGenerateRoadbook.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/course_generator/images/roadbook.png")));
		mnuGenerateRoadbook
				.setText(bundle.getString("frmMain.mnuGenerateRoadbook.text"));
		mnuGenerateRoadbook
				.addActionListener(new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent evt)
					{
						// TODO
					}
				});
		mnuGenerateRoadbook.setEnabled(false);
		mnuGenerateRoadbook.setVisible(false);
		mnuDisplay.add(mnuGenerateRoadbook);

		// -- Mini roadbook
		// -----------------------------------------------------
		mnuGenerateMiniRoadbook = new javax.swing.JMenuItem();
		mnuGenerateMiniRoadbook.setAccelerator(javax.swing.KeyStroke
				.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
		mnuGenerateMiniRoadbook.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/profil.png")));
		mnuGenerateMiniRoadbook.setText(
				bundle.getString("frmMain.mnuGenerateMiniRoadbook.text"));
		mnuGenerateMiniRoadbook.setEnabled(false);
		mnuGenerateMiniRoadbook
				.addActionListener(new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent evt)
					{
						ShowMRB();
					}
				});
		mnuDisplay.add(mnuGenerateMiniRoadbook);

		// -- Separator
		// ---------------------------------------------------------
		// mnuDisplay.add(new javax.swing.JPopupMenu.Separator());

		// -- Display the speed in the data grid
		// --------------------------------
		mnuDisplaySpeed = new javax.swing.JMenuItem();
		mnuDisplaySpeed
				.setText(bundle.getString("frmMain.mnuDisplaySpeed.text"));
		mnuDisplaySpeed.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				// mnuSaveCGXActionPerformed(evt); //TODO
			}
		});
		mnuDisplaySpeed.setEnabled(false);
		mnuDisplaySpeed.setVisible(false);
		mnuDisplay.add(mnuDisplaySpeed);

		// -- Display the slope in the data grid
		// --------------------------------
		mnuDisplaySlope = new javax.swing.JMenuItem();
		mnuDisplaySlope
				.setText(bundle.getString("frmMain.mnuDisplaySlope.text"));
		mnuDisplaySlope.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				// mnuSaveCGXActionPerformed(evt); //TODO
			}
		});
		mnuDisplaySlope.setEnabled(false);
		mnuDisplaySlope.setVisible(false);
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
		mnuFindMinMax.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/minmax.png")));
		mnuFindMinMax.setText(bundle.getString("frmMain.mnuFindMinMax.text"));
		mnuFindMinMax.setEnabled(false);
		mnuFindMinMax.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				BackupInCGX();
				Track.CalcMinMax();
			}
		});
		mnuTools.add(mnuFindMinMax);

		// -- Invert track
		// ------------------------------------------------------
		mnuInvertTrack = new javax.swing.JMenuItem();
		mnuInvertTrack.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/invert.png")));
		mnuInvertTrack.setText(bundle.getString("frmMain.mnuInvertTrack.text"));
		mnuInvertTrack.setEnabled(false);
		mnuInvertTrack.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				if (Track.data.size() > 0)
				{
					BackupInCGX();
					Track.Invert();
					panelProfil.RefreshProfilChart();
					jPanelTimeDist.Refresh(Track, Settings);
					jPanelSpeed.Refresh(Track, Settings);
					jPanelSpeedSlope.Refresh(Track, Settings);
					RefreshStatusbar(Track);
					panelTrackData.refresh();
					PanelResume.refresh();
				}
			}
		});
		mnuTools.add(mnuInvertTrack);

		// -- Define a new start
		// ------------------------------------------------
		mnuDefineNewStart = new javax.swing.JMenuItem();
		mnuDefineNewStart
				.setText(bundle.getString("frmMain.mnuDefineNewStart.text"));
		mnuDefineNewStart.setEnabled(false);
		mnuDefineNewStart.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				NewStartPoint();
			}
		});
		mnuTools.add(mnuDefineNewStart);

		// -- Calculate the track time
		// -------------------------------------------
		mnuCalculateTrackTime = new javax.swing.JMenuItem();
		mnuCalculateTrackTime.setAccelerator(javax.swing.KeyStroke
				.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
		mnuCalculateTrackTime.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/course_generator/images/refresh.png")));
		mnuCalculateTrackTime
				.setText(bundle.getString("frmMain.mnuCalculateTackTime.text"));
		mnuCalculateTrackTime.setEnabled(false);
		mnuCalculateTrackTime
				.addActionListener(new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent evt)
					{
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
		mnuInternetTools
				.setText(bundle.getString("frmMain.mnuInternetTools.text"));
		mnuInternetTools.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				// mnuSaveCGXActionPerformed(evt); //TODO
			}
		});
		mnuInternetTools.setEnabled(false);
		mnuInternetTools.setVisible(false);
		mnuTools.add(mnuInternetTools);

		// -- Display the directory containing the speed/slope files
		// ------------
		mnuDisplaySSDir = new javax.swing.JMenuItem();
		mnuDisplaySSDir
				.setText(bundle.getString("frmMain.mnuDisplaySSDir.text"));
		mnuDisplaySSDir.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				try
				{
					Desktop.getDesktop()
							.open(new File(DataDir + "/" + CgConst.CG_DIR));
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
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
		mnuTrackSettings.setAccelerator(javax.swing.KeyStroke
				.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0));
		mnuTrackSettings.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/course_generator/images/settings.png")));
		mnuTrackSettings
				.setText(bundle.getString("frmMain.mnuTrackSettings.text"));
		mnuTrackSettings.setEnabled(false);
		mnuTrackSettings.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				TrackSettings();
			}
		});
		mnuSettings.add(mnuTrackSettings);

		// -- Speed/Slope curves
		// ------------------------------------------------
		mnuSpeedSlopeCurves = new javax.swing.JMenuItem();
		mnuSpeedSlopeCurves.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/curve.png")));
		mnuSpeedSlopeCurves
				.setText(bundle.getString("frmMain.mnuSpeedSlopeCurves.text"));
		mnuSpeedSlopeCurves.setEnabled(false);
		mnuSpeedSlopeCurves
				.addActionListener(new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent evt)
					{
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
		mnuCGSettings.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/course_generator/images/settings.png")));
		mnuCGSettings.setText(bundle.getString("frmMain.mnuCGSettings.text"));
		mnuCGSettings.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				frmSettings frm = new frmSettings();
				frm.showDialog(Settings);

				// -- Refresh data and display
				RefreshStatusbar(Track);
				PanelResume.refresh();
				panelProfil.RefreshProfilChart();
				jPanelTimeDist.Refresh(Track, Settings);
				jPanelSpeed.Refresh(Track, Settings);
				jPanelSpeedSlope.Refresh(Track, Settings);
				panelStatistics.refresh();

				int row = panelTrackData.getSelectedRow();
				if (row >= 0)
					panelProfil.RefreshProfilInfo(row);

				panelTrackData.refresh();
				PanelResume.RefreshTableResume();
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
		mnuCGHelp.setAccelerator(javax.swing.KeyStroke
				.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
		mnuCGHelp.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/help.png")));
		mnuCGHelp.setText(bundle.getString("frmMain.mnuCGHelp.text"));
		mnuCGHelp.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				if (Utils.DirExist(ProgDir + "/help"))
				{
					try
					{
						Desktop.getDesktop().browse(new URI("file://" + ProgDir
								+ "/help/" + Lang4Help + "/cg_doc_4.00.html"));
					}
					catch (IOException | URISyntaxException e)
					{
						try
						{
							CgLog.info("Fail to open help for language "
									+ Settings.Language
									+ ". Default language loaded.");
							Desktop.getDesktop().browse(new URI("file://"
									+ ProgDir + "/help/en/cg_doc_4.00.html"));
						}
						catch (IOException | URISyntaxException e1)
						{
							e1.printStackTrace();
						}
					}
				}
				else CgLog.info("mnuCGHelp : <"+ ProgDir + "/help> doesn't exist!");
				// TODO link to website (when ready)
			}
		});
		mnuHelp.add(mnuCGHelp);

		// -- Reward the author
		// -------------------------------------------------
		mnuReward = new javax.swing.JMenuItem();
		mnuReward.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/pouce.png")));
		mnuReward.setText(bundle.getString("frmMain.mnuReward.text"));
		mnuReward.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				try
				{
					Desktop.getDesktop()
							.browse(new URI("http://www.techandrun.com/dons/"));
				}
				catch (IOException | URISyntaxException e)
				{
					e.printStackTrace();
				}
			}
		});
		mnuHelp.add(mnuReward);

		// -- Course Generator web site
		// -------------------------------------------------
		mnuCGWebsite = new javax.swing.JMenuItem();
		mnuCGWebsite.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/earth.png")));
		mnuCGWebsite.setText(bundle.getString("frmMain.mnuCGWebsite.text"));
		mnuCGWebsite.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				try
				{
					Desktop.getDesktop()
							.browse(new URI("http://www.techandrun.com/"));
				}
				catch (IOException | URISyntaxException e)
				{
					e.printStackTrace();
				}
			}
		});
		mnuHelp.add(mnuCGWebsite);

		// -- About
		// -------------------------------------------------------------
		mnuAbout = new javax.swing.JMenuItem();
		mnuAbout.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/about.png")));
		mnuAbout.setText(bundle.getString("frmMain.mnuAbout.text"));
		mnuAbout.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				mnuAbout();
			}
		});
		mnuHelp.add(mnuAbout);

		// --
		mnuMain.add(mnuHelp);

		// -- Add the menu at the window
		setJMenuBar(mnuMain);

		// TODO check why it's necessary
		mnuMain.getAccessibleContext().setAccessibleParent(this);
	}

	protected void ImportPoints()
	{
		if (Track == null)
			return;
		if (Track.data.isEmpty())
			return;

		String s = Utils.LoadDialog(this, Settings.LastDir, ".cgp",
				bundle.getString("frmMain.CGPFile"));
		if (!s.isEmpty())
		{
			frmImportPoints frm = new frmImportPoints(Settings);
			frm.showDialog(s, Track);

			panelTrackData.refresh();
			RefreshStatusbar(Track);
			RefreshTitle();
			panelProfil.RefreshProfilChart();
			jPanelTimeDist.Refresh(Track, Settings);
			jPanelSpeed.Refresh(Track, Settings);
			jPanelSpeedSlope.Refresh(Track, Settings);
			panelMap.RefreshTrack(Track, true);
			PanelResume.refresh();
			panelStatistics.refresh();
		}
	}

	/**
	 * Open a dialog to export a selection of points
	 */
	private void ExportPoints()
	{
		if (Track == null)
			return;
		if (Track.data.isEmpty())
			return;

		frmExportPoints frm = new frmExportPoints();
		int mask = frm.showDialog();
		if (mask != -1)
		{
			String s = Utils.SaveDialog(this, Settings.LastDir, "", ".cgp",
					bundle.getString("frmMain.CGPFile"), true,
					bundle.getString("frmMain.FileExist"));

			if (!s.isEmpty())
			{
				Track.ExportCGP(s, mask);
			}
		}
	}

	/**
	 * Save a part of the data in a CSV file
	 */
	private void SavePartCSV()
	{
		String s;

		if (Track.data.isEmpty())
			return;

		s = Utils.SaveDialog(this, Settings.previousCSVDirectory, "", ".csv",
				bundle.getString("frmMain.CSVFile"), true,
				bundle.getString("frmMain.FileExist"));

		if (!s.isEmpty())
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			// -- Save track
			int start = panelTrackData.getSelectedRow();
			int end = start + panelTrackData.getSelectedRowCount() - 1;

			Track.SaveCSV(s, start, end, Settings.Unit);
			// -- Store the directory
			Settings.previousCSVDirectory = Utils.GetDirFromFilename(s);

			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * Save data in a CSV file
	 */
	private void SaveCSV()
	{
		String s;

		if (Track.data.isEmpty())
			return;

		s = Utils.SaveDialog(this, Settings.previousCSVDirectory, "", ".csv",
				bundle.getString("frmMain.CSVFile"), true,
				bundle.getString("frmMain.FileExist"));

		if (!s.isEmpty())
		{
			// -- Save track
			Track.SaveCSV(s, 0, Track.data.size() - 1, Settings.Unit);
			// -- Store the directory
			Settings.previousCSVDirectory = Utils.GetDirFromFilename(s);
		}
	}

	/**
	 * Import a CGX file
	 */
	protected void ImportCGX()
	{
		if (Track.data.isEmpty())
			return;

		FrmImportChoice frm = new FrmImportChoice();

		int res = frm.showDialog();

		if (res != FrmImportChoice.RESULT_CANCEL)
		{
			String s = Utils.LoadDialog(this, Settings.previousCGXDirectory,
					".cgx", bundle.getString("frmMain.CGXFile"));
			if (!s.isEmpty())
			{
				int mode = FrmImportChoice.RESULT_AT_END;
				if (res == FrmImportChoice.RESULT_AT_END)
					mode = CgConst.IMPORT_MODE_INS_START;
				else
					mode = CgConst.IMPORT_MODE_ADD_END;

				BackupInCGX();
				// bAutorUpdatePos = false;
				try
				{
					Track.OpenCGX(this, s, mode, false);
					panelTrackData.refresh();
					RefreshStatusbar(Track);
					RefreshTitle();
					panelProfil.RefreshProfilChart();
					jPanelTimeDist.Refresh(Track, Settings);
					jPanelSpeed.Refresh(Track, Settings);
					jPanelSpeedSlope.Refresh(Track, Settings);
					panelMap.RefreshTrack(Track, true);
					PanelResume.refresh();
					panelStatistics.refresh();

					Settings.previousCGXDirectory = Utils.GetDirFromFilename(s);
					// bAutorUpdatePos = true;
				}
				catch (Exception e)
				{
					CgLog.error(
							"ImportCGX : Impossible to import the CGX file");
					e.printStackTrace();
				}
			}
		}
	}

	private void mnuAbout()
	{
		showDialogAbout(this, false, true, Version);
	}

	private void BackupInCGX()
	{
		Backup_Track = Track.CopyTo(Backup_Track);
		bNoBackup = false;

		// Track.SaveCGX(DataDir + "/" + CgConst.CG_DIR + "/backup.cgx", 0,
		// Track.data.size() - 1, true);
		// bNoBackup = false;
	}

	private void RestoreInCGX()
	{
		if ((Track.data.size() <= 0) || (bNoBackup))
			return;
		// bAutorUpdatePos = false;

		// Track.OpenCGX(this, DataDir + "/" + CgConst.CG_DIR + "/backup.cgx",
		// CgConst.IMPORT_MODE_LOAD, true);
		Track = Backup_Track.CopyTo(Track);

		// -- Update the viewer
		panelMap.setTrack(Track);
		// -- Refresh the track information
		RefreshStatusbar(Track);
		// -- Refresh resume grid
		PanelResume.setTrack(Track);

		// -- Refresh profil tab
		panelProfil.setTrack(Track);
		panelProfil.setSettings(Settings);
		panelProfil.RefreshProfilChart();
		// -- Refresh analysis tab
		jPanelTimeDist.Refresh(Track, Settings);
		jPanelSpeed.Refresh(Track, Settings);
		jPanelSpeedSlope.Refresh(Track, Settings);
		// -- Refresh the form title
		RefreshTitle();
		// -- Refresh statistic
		panelStatistics.setTrack(Track);

		// -- Force the update of the main table
		panelTrackData.setTrack(Track);

		// bAutorUpdatePos = true;
		bNoBackup = true;
	}

	/**
	 * Define a new starting point from the current position in the main table
	 */
	protected void NewStartPoint()
	{
		if (Track.data.isEmpty())
			return;

		int start = panelTrackData.getSelectedRow();
		if (start < 0)
			return;

		// -- Confirmation dialog
		Object[] options =
		{ " " + bundle.getString("frmMain.NewStartYes") + " ",
				" " + bundle.getString("frmMain.NewStartNo") + " " };
		int ret = JOptionPane.showOptionDialog(this,
				bundle.getString("frmMain.NewStartMessage"),
				bundle.getString("frmMain.NewStartTitle"),
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null,
				options, options[1]);

		// -- Ok! Let's go
		if (ret == JOptionPane.YES_OPTION)
		{
			BackupInCGX();

			Track.NewStartingPoint(start);

			// -- Move the cursor to the first line of the data table
			panelTrackData.setSelectedRow(0);

			Track.isCalculated = false;
			Track.isModified = true;

			// -- Refresh
			RefreshStatusbar(Track);
			panelTrackData.refresh();
			PanelResume.refresh();
			panelStatistics.refresh();

			// Refresh the marker position on the map
			panelMap.RefreshCurrentPosMarker(Track.data.get(0).getLatitude(),
					Track.data.get(0).getLongitude());
		}
	}

	/**
	 * Save the selected data to disk in GPX format
	 */
	private void SavePartGPX()
	{
		String s;

		if (Track.data.isEmpty())
			return;

		s = Utils.SaveDialog(this, Settings.previousGPXDirectory, "", ".gpx",
				bundle.getString("frmMain.GPXFile"), true,
				bundle.getString("frmMain.FileExist"));

		if (!s.isEmpty())
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			// -- Save track
			int start = panelTrackData.getSelectedRow();
			int end = start + panelTrackData.getSelectedRowCount() - 1;

			Track.SaveGPX(s, start, end);
			// -- Store the directory
			Settings.previousGPXDirectory = Utils.GetDirFromFilename(s);

			// -- Update GPX MRU
			AddMruGPX(s);
			RefreshMruGPX();

			// -- We don't reset the track modified flag because we save only a
			// part of the track!
			Track.isModified = false;

			// -- Refresh info panel
			RefreshStatusbar(Track);

			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * Save the selected data to disk in CGX format
	 */
	private void SavePartCGX()
	{
		String s;

		if (Track.data.isEmpty())
			return;

		s = Utils.SaveDialog(this, Settings.previousCGXDirectory, "", ".cgx",
				bundle.getString("frmMain.CGXFile"), true,
				bundle.getString("frmMain.FileExist"));

		if (!s.isEmpty())
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			// -- Save track
			int start = panelTrackData.getSelectedRow();
			int end = start + panelTrackData.getSelectedRowCount() - 1;

			Track.SaveCGX(s, start, end, false);
			// -- Store the directory
			Settings.previousCGXDirectory = Utils.GetDirFromFilename(s);

			// -- Update GPX MRU
			AddMruCGX(s);
			RefreshMruCGX();

			// -- We don't reset the track modified flag because we save only a
			// part of the track!
			Track.isModified = false;

			// -- Refresh info panel
			RefreshStatusbar(Track);

			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * Separator for the status bar
	 * 
	 * @return Separator object
	 */
	static JComponent createStatusbarSeparator()
	{
		JSeparator x = new JSeparator(SwingConstants.VERTICAL);
		x.setPreferredSize(new Dimension(3, 20));
		return x;
	}

	/**
	 * Create the status bar
	 */
	private void Create_Statusbar()
	{
		StatusBar = new javax.swing.JPanel();
		FlowLayout fl = new FlowLayout(FlowLayout.RIGHT);
		fl.setVgap(0);
		StatusBar.setLayout(fl);

		// -- Separator
		StatusBar.add(createStatusbarSeparator());

		// -- Total distance
		// ----------------------------------------------------
		LbInfoTotalDist = new javax.swing.JLabel();
		LbInfoTotalDist.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/course_generator/images/distance.png")));
		LbInfoTotalDist.setToolTipText(
				bundle.getString("frmMain.LbInfoTotalDist.toolTipText")); // Total
																			// distance
																			// of
																			// the
																			// track
		StatusBar.add(LbInfoTotalDist);

		// -- Total distance - value
		// ----------------------------------------------------
		LbInfoTotalDistVal = new javax.swing.JLabel();
		LbInfoTotalDistVal.setToolTipText(
				bundle.getString("frmMain.LbInfoTotalDist.toolTipText")); // Total
																			// distance
																			// of
																			// the
																			// track
		StatusBar.add(LbInfoTotalDistVal);

		// -- Separator
		StatusBar.add(createStatusbarSeparator());

		// -- Ascent
		// ------------------------------------------------------
		LbInfoDp = new javax.swing.JLabel();
		LbInfoDp.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/dp.png")));
		LbInfoDp.setToolTipText(
				bundle.getString("frmMain.LbInfoDp.toolTipText")); // Total
																	// ascent
																	// elevation
		StatusBar.add(LbInfoDp);

		// -- Ascent value
		// ------------------------------------------------------
		LbInfoDpVal = new javax.swing.JLabel();
		LbInfoDpVal.setToolTipText(
				bundle.getString("frmMain.LbInfoDp.toolTipText")); // Total
																	// ascent
																	// elevation
		StatusBar.add(LbInfoDpVal);

		// -- Separator
		StatusBar.add(createStatusbarSeparator());

		// -- Descent
		// -----------------------------------------------------
		LbInfoDm = new javax.swing.JLabel();
		LbInfoDm.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/dm.png")));
		LbInfoDm.setToolTipText(
				bundle.getString("frmMain.LbInfoDm.toolTipText")); // Total
																	// descent
																	// elevation
		StatusBar.add(LbInfoDm);

		// -- Descent value
		// -----------------------------------------------------
		LbInfoDmVal = new javax.swing.JLabel();
		LbInfoDmVal.setToolTipText(
				bundle.getString("frmMain.LbInfoDm.toolTipText")); // Total
																	// descent
																	// elevation
		StatusBar.add(LbInfoDmVal);

		// -- Separator
		StatusBar.add(createStatusbarSeparator());

		// -- Total time
		// --------------------------------------------------------
		LbInfoTime = new javax.swing.JLabel();
		LbInfoTime.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/course_generator/images/chronometer.png")));
		LbInfoTime.setToolTipText(
				bundle.getString("frmMain.LbInfoTime.toolTipText")); // Total
																		// time
		StatusBar.add(LbInfoTime);

		// -- Total time value
		// --------------------------------------------------------
		LbInfoTimeVal = new javax.swing.JLabel();
		LbInfoTimeVal.setToolTipText(
				bundle.getString("frmMain.LbInfoTime.toolTipText")); // Total
																		// time
		StatusBar.add(LbInfoTimeVal);

		// -- Separator
		StatusBar.add(createStatusbarSeparator());

		// -- Curve
		// --------------------------------------------------------
		LbInfoCurve = new javax.swing.JLabel();
		LbInfoCurve.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/curve.png")));
		LbInfoCurve.setToolTipText(
				bundle.getString("frmMain.LbInfoCurve.toolTipText")); // Selected
																		// curve
		StatusBar.add(LbInfoCurve);

		// -- Curve value
		// --------------------------------------------------------
		LbInfoCurveVal = new javax.swing.JLabel();
		LbInfoCurveVal.setToolTipText(
				bundle.getString("frmMain.LbInfoCurve.toolTipText")); // Selected
																		// curve
		StatusBar.add(LbInfoCurveVal);

		// -- Separator
		StatusBar.add(createStatusbarSeparator());

		// -- Time limit
		// --------------------------------------------------------
		LbTimeLimit = new javax.swing.JLabel(
				" " + bundle.getString("frmMain.LbTimeLimit.text") + " ");
		LbTimeLimit.setOpaque(true);
		LbTimeLimit.setBackground(Color.RED);
		LbTimeLimit.setForeground(Color.WHITE);
		LbTimeLimit.setToolTipText(
				bundle.getString("frmMain.LbTimeLimit.toolTipText")); // Time
																		// limit
																		// reached
																		// in a
																		// part
																		// of
																		// the
																		// track
		LbTimeLimit.addMouseListener(new java.awt.event.MouseAdapter()
		{
			public void mouseClicked(java.awt.event.MouseEvent evt)
			{
				JumpToTimelimitLine();
			}
		});
		StatusBar.add(LbTimeLimit);

		// -- Separator
		sepTimeLimit = createStatusbarSeparator();
		StatusBar.add(sepTimeLimit);

		// -- Modified
		// --------------------------------------------------------
		LbModified = new javax.swing.JLabel();
		LbModified.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/edit.png")));
		LbModified.setToolTipText(
				bundle.getString("frmMain.LbModified.toolTipText")); // Track
																		// modified
		StatusBar.add(LbModified);

		// -- Modified status
		// --------------------------------------------------------
		LbModifiedVal = new javax.swing.JLabel();
		LbModifiedVal.setToolTipText(
				bundle.getString("frmMain.LbModified.toolTipText")); // Track
																		// modified
		StatusBar.add(LbModifiedVal);

		// -- Separator
		StatusBar.add(createStatusbarSeparator());

		// -- Calculation needed
		// ------------------------------------------------
		LbInfoCalculate = new javax.swing.JLabel();
		LbInfoCalculate.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/calc.png")));
		LbInfoCalculate.setToolTipText(
				bundle.getString("frmMain.LbInfoCalculate.toolTipText")); // Track
																			// data
																			// need
																			// to
																			// be
																			// updated.
																			// Press
																			// F5
																			// to
																			// update.
		StatusBar.add(LbInfoCalculate);

		// -- Calculation needed value
		// ------------------------------------------------
		LbInfoCalculateVal = new javax.swing.JLabel();
		LbInfoCalculateVal.setToolTipText(
				bundle.getString("frmMain.LbInfoCalculate.toolTipText")); // Track
																			// data
																			// need
																			// to
																			// be
																			// updated.
																			// Press
																			// F5
																			// to
																			// update.
		StatusBar.add(LbInfoCalculateVal);

		// -- Separator
		StatusBar.add(createStatusbarSeparator());

		// -- Internet connection present
		// ----------------------------------------
		LbInfoInternet = new javax.swing.JLabel();
		LbInfoInternet.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/earth.png")));
		LbInfoInternet.setToolTipText(
				bundle.getString("frmMain.LbInfoInternet.toolTipText")); // Internet
																			// status
		StatusBar.add(LbInfoInternet);

		// -- Internet connection present value
		// ----------------------------------------
		LbInfoInternetVal = new javax.swing.JLabel();
		LbInfoInternetVal.setToolTipText(
				bundle.getString("frmMain.LbInfoInternet.toolTipText")); // Internet
																			// status
		StatusBar.add(LbInfoInternetVal);

		// -- Separator
		StatusBar.add(createStatusbarSeparator());

		// -- Unit
		// ----------------------------------------
		LbInfoUnit = new javax.swing.JLabel();
		LbInfoUnit.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/unit.png")));
		LbInfoUnit.setToolTipText(
				bundle.getString("frmMain.LbInfoUnit.toolTipText")); // Unit
																		// selected
																		// for
																		// the
																		// display
		StatusBar.add(LbInfoUnit);

		// -- Unit value
		// ----------------------------------------
		LbInfoUnitVal = new javax.swing.JLabel();
		LbInfoUnitVal.setToolTipText(
				bundle.getString("frmMain.LbInfoUnit.toolTipText")); // Unit
																		// selected
																		// for
																		// the
																		// display
		StatusBar.add(LbInfoUnitVal);

		// -- Separator
		StatusBar.add(createStatusbarSeparator());

		// -- Map dir size
		// ----------------------------------------
		LbInfoMapDirSize = new javax.swing.JLabel();
		LbInfoMapDirSize.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/course_generator/images/earth_bw.png")));
		LbInfoMapDirSize.setToolTipText(
				bundle.getString("frmMain.LbInfoMapDirSize.toolTipText")); // Size
																			// of
																			// maps
																			// stored
																			// in
																			// the
																			// disk
		StatusBar.add(LbInfoMapDirSize);
	}

	/**
	 * Create the Main toolbar
	 */
	private void Create_MainToolbar()
	{
		ToolBarMain = new javax.swing.JToolBar();
		ToolBarMain.setFloatable(false);
		ToolBarMain.setRollover(true);

		// -- Open GPX
		// ----------------------------------------------------------
		btOpenGPX = new javax.swing.JButton();
		btOpenGPX.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/course_generator/images/openGPX.png")));
		btOpenGPX.setToolTipText(
				bundle.getString("frmMain.btOpenGPX.toolTipText"));
		btOpenGPX.setFocusable(false);
		btOpenGPX.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				OpenGPXDialog();
			}
		});
		ToolBarMain.add(btOpenGPX);

		// -- Open CGX
		// ----------------------------------------------------------
		btOpenCGX = new javax.swing.JButton();
		btOpenCGX.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/course_generator/images/openCGX.png")));
		btOpenCGX.setToolTipText(
				bundle.getString("frmMain.btOpenCGX.toolTipText"));
		btOpenCGX.setFocusable(false);
		btOpenCGX.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
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
		btSaveCGX.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/course_generator/images/saveCGX.png")));
		btSaveCGX.setToolTipText(
				bundle.getString("frmMain.btSaveCGX.toolTipText"));
		btSaveCGX.setFocusable(false);
		btSaveCGX.setEnabled(false);
		btSaveCGX.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
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
		btUndo.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/undo.png")));
		btUndo.setToolTipText(bundle.getString("frmMain.btUndo.toolTipText"));
		btUndo.setFocusable(false);
		btUndo.setEnabled(false);
		btUndo.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				RestoreInCGX();
			}
		});
		ToolBarMain.add(btUndo);

		// -- Separator
		// ---------------------------------------------------------
		ToolBarMain.add(new javax.swing.JToolBar.Separator());

		// -- Search
		// ------------------------------------------------------------
		btSearch = new javax.swing.JButton();
		btSearch.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/search.png")));
		btSearch.setToolTipText(
				bundle.getString("frmMain.btSearch.toolTipText"));
		btSearch.setFocusable(false);
		btSearch.setEnabled(false);
		btSearch.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				SearchPointDialog();
			}
		});
		ToolBarMain.add(btSearch);

		// -- Previous mark
		// -----------------------------------------------------
		btGotoPreviousMark = new javax.swing.JButton();
		btGotoPreviousMark.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/prev.png")));
		btGotoPreviousMark.setToolTipText(
				bundle.getString("frmMain.btGotoPreviousMark.toolTipText"));
		btGotoPreviousMark.setFocusable(false);
		btGotoPreviousMark.setEnabled(false);
		btGotoPreviousMark.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				int p = GotoPrevTag();
				if ((p >= 0) && (p < Track.data.size()))
					panelMap.RefreshCurrentPosMarker(
							Track.data.get(p).getLatitude(),
							Track.data.get(p).getLongitude());
			}
		});
		ToolBarMain.add(btGotoPreviousMark);

		// -- Next mark
		// ---------------------------------------------------------
		btGotoNextMark = new javax.swing.JButton();
		btGotoNextMark.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/next.png")));
		btGotoNextMark.setToolTipText(
				bundle.getString("frmMain.btGotoNextMark.toolTipText"));
		btGotoNextMark.setFocusable(false);
		btGotoNextMark.setEnabled(false);
		btGotoNextMark.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				int p = GotoNextTag();
				if ((p >= 0) && (p < Track.data.size()))
					panelMap.RefreshCurrentPosMarker(
							Track.data.get(p).getLatitude(),
							Track.data.get(p).getLongitude());
			}
		});
		ToolBarMain.add(btGotoNextMark);

		// -- Separator
		// ---------------------------------------------------------
		ToolBarMain.add(new javax.swing.JToolBar.Separator());

		// -- Mini roadbook
		// ------------------------------------------------
		btMiniRoadbook = new javax.swing.JButton();
		btMiniRoadbook.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/profil.png")));
		btMiniRoadbook.setToolTipText(
				bundle.getString("frmMain.btMiniRoadbook.toolTipText"));
		btMiniRoadbook.setFocusable(false);
		btMiniRoadbook.setEnabled(false);
		btMiniRoadbook.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				ShowMRB();
			}
		});
		ToolBarMain.add(btMiniRoadbook);

		// -- Display S/S curves
		// ------------------------------------------------
		btDisplaySSCurves = new javax.swing.JButton();
		btDisplaySSCurves.setIcon(new javax.swing.ImageIcon(
				getClass().getResource("/course_generator/images/curve.png")));
		btDisplaySSCurves.setToolTipText(
				bundle.getString("frmMain.btDisplaySSCurves.toolTipText"));
		btDisplaySSCurves.setFocusable(false);
		btDisplaySSCurves.setEnabled(false);
		btDisplaySSCurves.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				EditSSCurves();
			}
		});
		ToolBarMain.add(btDisplaySSCurves);

		// -- Track settings
		// ----------------------------------------------------
		btTrackSettings = new javax.swing.JButton();
		btTrackSettings.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/course_generator/images/settings.png")));
		btTrackSettings.setToolTipText(
				bundle.getString("frmMain.btTrackSettings.toolTipText"));
		btTrackSettings.setFocusable(false);
		btTrackSettings.setEnabled(false);
		btTrackSettings.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
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
		btFillDiff.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/course_generator/images/fill_diff.png")));
		btFillDiff.setToolTipText(
				bundle.getString("frmMain.btFillDiff.toolTipText"));
		btFillDiff.setFocusable(false);
		btFillDiff.setEnabled(false);
		btFillDiff.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				if (Track.data.isEmpty())
					return;

				int start = panelTrackData.getSelectedRow();
				int end = start + panelTrackData.getSelectedRowCount();

				frmFillDiff frm = new frmFillDiff();
				EditDiffResult res = frm.showDialog(Settings, Track, start,
						end);
				if (res.Valid)
				{
					BackupInCGX();

					for (int i = res.Start; i <= res.End; i++)
					{
						Track.data.get(i).setDiff(res.Difficulty);
					}

					Track.isCalculated = false;
					Track.isModified = true;
					panelTrackData.refresh();
					panelProfil.RefreshProfilChart();
					jPanelTimeDist.Refresh(Track, Settings);
					jPanelSpeed.Refresh(Track, Settings);
					jPanelSpeedSlope.Refresh(Track, Settings);
					RefreshStatusbar(Track);
				}
			}
		});
		ToolBarMain.add(btFillDiff);

		// -- Fill coeff
		// ----------------------------------------------------
		btFillCoeff = new javax.swing.JButton();
		btFillCoeff.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/course_generator/images/tiredness.png")));
		btFillCoeff.setToolTipText(
				bundle.getString("frmMain.btFillCoeff.toolTipText"));
		btFillCoeff.setFocusable(false);
		btFillCoeff.setEnabled(false);
		btFillCoeff.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				if (Track.data.size() <= 0)
					return;

				int start = panelTrackData.getSelectedRow();
				int end = start + panelTrackData.getSelectedRowCount();

				frmFillCoeff frm = new frmFillCoeff();
				EditCoeffResult res = frm.showDialog(Settings, Track, start,
						end);
				if (res.Valid)
				{
					BackupInCGX();

					if (res.Start == res.End)
					{
						Track.data.get(res.Start).setCoeff(res.Start_Coeff);
					}
					else
					{
						double x1 = Track.data.get(res.Start)
								.getTotal(CgConst.UNIT_METER);// cd.data[frm.start].Total;
						double y1 = res.Start_Coeff; // frm.startval;

						double x2 = Track.data.get(res.End)
								.getTotal(CgConst.UNIT_METER); // cd.data[frm.end].Total;
						double y2 = res.End_Coeff; // frm.endval;

						CalcLineResult rc = new CalcLineResult();
						rc = Utils.CalcLine(x1, y1, x2, y2, rc);

						// Line equation calc with "Y"=distance and "X"=Coeff
						double x = 0.0;
						double offset = 0.0;
						double coeff = 0;

						for (int i = res.Start; i <= res.End; i++)
						{
							x = Track.data.get(i).getTotal(CgConst.UNIT_METER);
							offset = offset + Track.data.get(i).getRecovery();

							coeff = (rc.a * x + rc.b) + offset;

							// Validity tests
							if (coeff > CgConst.MAX_COEFF)
								coeff = CgConst.MAX_COEFF;
							if (coeff < 0)
								coeff = 0;

							Track.data.get(i).setCoeff(coeff);
						}
					}

					Track.isCalculated = false;
					Track.isModified = true;
					panelTrackData.refresh();
					panelProfil.RefreshProfilChart();
					jPanelTimeDist.Refresh(Track, Settings);
					jPanelSpeed.Refresh(Track, Settings);
					jPanelSpeedSlope.Refresh(Track, Settings);
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
		btCalculateTrackTime.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/course_generator/images/refresh.png")));
		btCalculateTrackTime.setToolTipText(
				bundle.getString("frmMain.btCalculateTrackTime.toolTipText"));
		btCalculateTrackTime.setFocusable(false);
		btCalculateTrackTime.setEnabled(false);
		btCalculateTrackTime
				.addActionListener(new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent evt)
					{
						CalcTrackTime();
					}
				});
		ToolBarMain.add(btCalculateTrackTime);

	}

	/**
	 * Display the mini roadbook
	 */
	protected void ShowMRB()
	{
		if (Track.data.isEmpty())
			return;

		FrmMiniroadbook frm = new FrmMiniroadbook(Settings);
		frm.showDialog(Track);
	}

	protected void TrackSettings()
	{
		if (Track.data.size() <= 0)
			return;

		frmTrackSettings frm = new frmTrackSettings();
		if (frm.showDialog(Settings, Track))
		{
			panelTrackData.refresh();
			panelProfil.RefreshProfilChart();
			jPanelTimeDist.Refresh(Track, Settings);
			jPanelSpeed.Refresh(Track, Settings);
			jPanelSpeedSlope.Refresh(Track, Settings);
			RefreshTitle();
			RefreshStatusbar(Track);
			Track.isModified = true;
			Track.isCalculated = false;
		}
	}

	/**
	 * Display the edition curves dialog
	 */
	private void EditSSCurves()
	{
		frmEditCurve frm = new frmEditCurve();
		frm.showDialog(Track);
		RefreshStatusbar(Track);
	}

	private void JumpToTimelimitLine()
	{

		if (Track.TimeLimit_Line != -1)
		{
			// -- Select the line and scroll to it
			panelTrackData.setSelectedRow(Track.TimeLimit_Line);
			// -- Update the profil position
			panelProfil.setCrosshairPosition(
					Track.data.get(Track.TimeLimit_Line).getTotal(Settings.Unit)
							/ 1000.0,
					Track.data.get(Track.TimeLimit_Line)
							.getElevation(Settings.Unit));
			panelProfil.RefreshProfilInfo(Track.TimeLimit_Line);
		}
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
	private void addTab(JTabbedPane tabbedPane, Component tab, String title,
			Icon icon)
	{
		tabbedPane.add(tab);

		// Create bespoke component for rendering the tab.
		javax.swing.JLabel lbl = new javax.swing.JLabel(title);
		if (icon != null)
			lbl.setIcon(icon);

		// Add some spacing between text and icon, and position text to the RHS.
		lbl.setIconTextGap(5);
		lbl.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

		tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, lbl);
	}

	/**
	 * This method is called to initialize the form.
	 */
	private void initComponents()
	{
		// -- Main windows
		// ------------------------------------------------------
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle(bundle.getString("frmMain.title"));
		setIconImages(null);

		addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(java.awt.event.WindowEvent evt)
			{
				formWindowClosing(evt);
			}
		});

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
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
		//-- Disable the default management of F6
		SplitPaneMain.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
		  .put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), "none");
		
		paneGlobal.add(SplitPaneMain, BorderLayout.CENTER);

		// -- Left side of the split bar
		// ----------------------------------------
		jPanelLeft = new javax.swing.JPanel();
		jPanelLeft.setLayout(new java.awt.BorderLayout());

		// -- Add the left panel to the main split panel
		// ------------------------
		SplitPaneMain.setLeftComponent(jPanelLeft);
		SplitPaneMain.setDividerLocation(0);

		// // -- Content of the tree
		// javax.swing.tree.DefaultMutableTreeNode treeNode1 = new
		// javax.swing.tree.DefaultMutableTreeNode(
		// "Course Generator");
		// javax.swing.tree.DefaultMutableTreeNode treeNode2 = new
		// javax.swing.tree.DefaultMutableTreeNode("Parcours");
		// javax.swing.tree.DefaultMutableTreeNode treeNode3 = new
		// javax.swing.tree.DefaultMutableTreeNode("Utmb 2011");
		// javax.swing.tree.DefaultMutableTreeNode treeNode4 = new
		// javax.swing.tree.DefaultMutableTreeNode("Prévu");
		// treeNode3.add(treeNode4);
		// treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Fait");
		// treeNode3.add(treeNode4);
		// treeNode2.add(treeNode3);
		// treeNode3 = new
		// javax.swing.tree.DefaultMutableTreeNode("Montagnard");
		// treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Prévu");
		// treeNode3.add(treeNode4);
		// treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Fait");
		// treeNode3.add(treeNode4);
		// treeNode2.add(treeNode3);
		// treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("UCDHL2008");
		// treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Prévu");
		// treeNode3.add(treeNode4);
		// treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Fait");
		// treeNode3.add(treeNode4);
		// treeNode2.add(treeNode3);
		// treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("UCDHL2009");
		// treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Prévu");
		// treeNode3.add(treeNode4);
		// treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Fait");
		// treeNode3.add(treeNode4);
		// treeNode2.add(treeNode3);
		// treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("UCDHL2010");
		// treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Prévu");
		// treeNode3.add(treeNode4);
		// treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Fait");
		// treeNode3.add(treeNode4);
		// treeNode2.add(treeNode3);
		// treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("UCDHL2011");
		// treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Prévu");
		// treeNode3.add(treeNode4);
		// treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Fait");
		// treeNode3.add(treeNode4);
		// treeNode2.add(treeNode3);
		// treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("UCDHL2012");
		// treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Prévu");
		// treeNode3.add(treeNode4);
		// treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Fait");
		// treeNode3.add(treeNode4);
		// treeNode2.add(treeNode3);
		// treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("UCDHL2013");
		// treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Prévu");
		// treeNode3.add(treeNode4);
		// treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Fait");
		// treeNode3.add(treeNode4);
		// treeNode2.add(treeNode3);
		// treeNode1.add(treeNode2);
		// treeNode2 = new
		// javax.swing.tree.DefaultMutableTreeNode("Configuration");
		// treeNode1.add(treeNode2);
		//
		// // -- Tree
		// // --------------------------------------------------------------
		// jTreeMain = new javax.swing.JTree();
		// jTreeMain.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
		// jTreeMain.setPreferredSize(new java.awt.Dimension(109, 25));
		//
		// // -- Add the tree to a scroll panel
		// // ------------------------------------
		// jScrollPaneTree = new javax.swing.JScrollPane();
		// jScrollPaneTree.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		// jScrollPaneTree.setViewportView(jTreeMain);
		// jScrollPaneTree.setMaximumSize(new Dimension(0,0));
		//
		// // -- Add the scroll panel to the left panel
		// // ----------------------------
		// jPanelLeft.add(jScrollPaneTree, java.awt.BorderLayout.CENTER);

		// -- Right split pane
		// --------------------------------------------------
		SplitPaneMainRight = new javax.swing.JSplitPane();
		SplitPaneMainRight
				.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
		//-- Disable the default management of F6
		SplitPaneMainRight.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
		  .put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), "none");
		SplitPaneMain.setRightComponent(SplitPaneMainRight);

		// -- Tabbed panel
		// ------------------------------------------------------
		TabbedPaneMain = new javax.swing.JTabbedPane();
		// -- Create the listener
		ChangeListener changeListener = new ChangeListener()
		{
			public void stateChanged(ChangeEvent changeEvent)
			{
				JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent
						.getSource();
				int index = sourceTabbedPane.getSelectedIndex();
				if (index == 4) // Tab Resume
					PanelResume.refresh();
			}
		};
		TabbedPaneMain.addChangeListener(changeListener);

		SplitPaneMainRight.setTopComponent(TabbedPaneMain);

		// -- Tab - data (grid)
		// -------------------------------------------------

		panelTrackData = new JPanelTrackData(ModelTableMain, Settings);
		panelTrackData.addListener(new JPanelTrackDataListener()
		{

			@Override
			public void doubleClickEvent()
			{
				BackupInCGX();
				frmEditPosition frm = new frmEditPosition();
				if (frm.showDialog(Settings, Track,
						panelTrackData.getSelectedRow()))
				{
					Track.isModified = true;
					panelTrackData.refresh();
					panelProfil.RefreshProfilChart();
					Track.CheckTimeLimit();
					RefreshStatusbar(Track);
				}
			}

			@Override
			public void simpleClickEvent()
			{
				if (Track.data.size() > 0)
				{
					int row = panelTrackData.getSelectedRow();
					// -- Refresh marker position on the map
					panelMap.RefreshCurrentPosMarker(
							Track.data.get(row).getLatitude(),
							Track.data.get(row).getLongitude());
					// -- Refresh profil crooshair position and profil info
					panelProfil.RefreshProfilInfo(row);
					panelProfil.setCrosshairPosition(
							Track.data.get(row).getTotal(Settings.Unit)
									/ 1000.0,
							Track.data.get(row).getElevation(Settings.Unit));
				}
			}

			@Override
			public void keyRealeasedEvent()
			{
				if (Track.data.size() > 0)
				{
					int row = panelTrackData.getSelectedRow();
					panelMap.RefreshCurrentPosMarker(
							Track.data.get(row).getLatitude(),
							Track.data.get(row).getLongitude());
				}
			}
		});

		addTab(TabbedPaneMain, panelTrackData,
				bundle.getString("frmMain.TabData.tabTitle"),
				new javax.swing.ImageIcon(getClass().getResource(
						"/course_generator/images/satellite16.png")));

		// -- Tab - Profil
		// ------------------------------------------------------
		panelProfil = new JPanelProfil(Settings);
		panelProfil.addListener(new JPanelProfilListener()
		{
			@Override
			public void profilSelectionEvent()
			{
				int i = panelProfil.getIndex();
				// Refresh the position on the data grid
				panelTrackData.setSelectedRow(i);
				// Refresh the marker position on the map
				panelMap.RefreshCurrentPosMarker(
						Track.data.get(i).getLatitude(),
						Track.data.get(i).getLongitude());
			}
		});

		addTab(TabbedPaneMain, panelProfil,
				bundle.getString("frmMain.TabProfil.tabTitle"),
				new javax.swing.ImageIcon(getClass()
						.getResource("/course_generator/images/profil.png")));

		// -- Tab - Statistic
		// ---------------------------------------------------
		panelStatistics = new JPanelStatistics(Settings);
		addTab(TabbedPaneMain, panelStatistics,
				bundle.getString("frmMain.TabStatistic.tabTitle"),
				new javax.swing.ImageIcon(getClass()
						.getResource("/course_generator/images/stat.png")));

		// -- Tab - Analysis
		// ----------------------------------------------------
		jPanelAnalyze = new javax.swing.JPanel();
		jPanelAnalyze.setLayout(new java.awt.BorderLayout());
		addTab(TabbedPaneMain, jPanelAnalyze,
				bundle.getString("frmMain.TabAnalyze.tabTitle"),
				new javax.swing.ImageIcon(getClass()
						.getResource("/course_generator/images/search.png")));

		// -- Create the tab bar
		TabbedPaneAnalysis = new javax.swing.JTabbedPane(JTabbedPane.LEFT);
		jPanelAnalyze.add(TabbedPaneAnalysis, java.awt.BorderLayout.CENTER);

		// -- Tab Analysis : Time/Dist
		jPanelTimeDist = new JPanelAnalysisTimeDist();
		addTab(TabbedPaneAnalysis, jPanelTimeDist,
				bundle.getString("frmMain.TabTimeDist.tabTitle"), null);

		// -- Tab Analysis : Speed
		jPanelSpeed = new JPanelAnalysisSpeed();
		addTab(TabbedPaneAnalysis, jPanelSpeed,
				bundle.getString("frmMain.TabSpeed.tabTitle"), null);

		// -- Tab Analysis : Speed/slope
		jPanelSpeedSlope = new JPanelAnalysisSpeedSlope();
		addTab(TabbedPaneAnalysis, jPanelSpeedSlope,
				bundle.getString("frmMain.TabSpeedSlope.tabTitle"), null);

		// -- Tab - Resume
		// ------------------------------------------------------
		PanelResume = new JPanelResume(Resume, Settings);
		PanelResume.addListener(new JPanelResumeListener()
		{
			@Override
			public void lineChangeEvent()
			{
				SelectPositionFromResume(PanelResume.getSelectedLine());
			}
		});
		addTab(TabbedPaneMain, PanelResume,
				bundle.getString("frmMain.TabResume.tabTitle"),
				new javax.swing.ImageIcon(getClass()
						.getResource("/course_generator/images/grid.png")));

		// -- Map panel
		// ---------------------------------------------------------
		panelMap = new JPanelMaps(Settings);
		panelMap.addListener(new JPanelMapsListener()
		{
			@Override
			public void requestPositionIndexEvent()
			{
				panelMap.setRow(panelTrackData.getSelectedRow());
			}

			@Override
			public void changeEvent()
			{
				panelTrackData.refresh();
			}

			@Override
			public void mouseClicked(MouseEvent evt)
			{
				Coordinate c = panelMap.getSelectedPosition();

				int i = Track.FindNearestPoint(c.getLat(), c.getLon());
				// Selection the position on the data grid
				panelTrackData.setSelectedRow(i);

				// Refresh profil position
				panelProfil.RefreshProfilInfo(i);
				panelProfil.setCrosshairPosition(
						Track.data.get(i).getTotal(Settings.Unit) / 1000.0,
						Track.data.get(i).getElevation(Settings.Unit));
			}
		});

		SplitPaneMainRight.setRightComponent(panelMap);

		// -- Finished - Pack
		// ---------------------------------------------------
		pack();
	}

	protected void SelectPositionFromResume(int selectedLine)
	{
		if (Resume.data.size() > 0)
		{
			int r = (int) (Resume.data.get(selectedLine).getLine()) - 1;
			// -- Set table main selection
			panelTrackData.setSelectedRow(r);
			// -- Refresh marker position on the map
			panelMap.RefreshCurrentPosMarker(Track.data.get(r).getLatitude(),
					Track.data.get(r).getLongitude());
			// -- Refresh profil crosshair position and profil info
			panelProfil.RefreshProfilInfo(r);
			panelProfil.setCrosshairPosition(
					Track.data.get(r).getTotal(Settings.Unit) / 1000.0,
					Track.data.get(r).getElevation(Settings.Unit));
		}
	}

	
	private void SetMapMarker() {
		int p = -1;
		if (Track.data.size() > 0)
		{
			int row = panelTrackData.getSelectedRow();
			if (row < 0)
				return;
				
			int tag = Track.data.get(row).getTag();

			if ((tag & CgConst.TAG_MARK) == 0)
				tag = tag | CgConst.TAG_MARK;
			else
				tag = tag & (~CgConst.TAG_MARK);

			Track.data.get(row).setTag(tag);

			//-- Set the flags
			Track.isCalculated = false; //Necessary?
			Track.isModified = true;

			//-- Refresh the table and map
			panelTrackData.refresh();
			panelMap.RefreshTrack(Track, false);
			// ShowMapMarker();
		 }
	 }
	
	
	/**
	 * Go to the next tag
	 */
	private int GotoNextTag()
	{
		int p = -1;
		if (Track.data.size() > 0)
		{
			CgData d;

			int row = panelTrackData.getSelectedRow();
			if (row < 0)
				return p;

			p = row + 1;

			if (p < Track.data.size())
			{
				d = Track.data.get(p);

				while ((p < Track.data.size() - 1) && (d.getTag() == 0))
				{
					p++;
					d = Track.data.get(p);
				}

				if (d.getTag() != 0)
				{
					// -- Select the line and scroll to it
					panelTrackData.setSelectedRow(p);
					// -- Update the profil position
					panelProfil.setCrosshairPosition(
							Track.data.get(p).getTotal(Settings.Unit) / 1000.0,
							Track.data.get(p).getElevation(Settings.Unit));
					panelProfil.RefreshProfilInfo(p);
				}

			}
		}
		return p;
	}

	/**
	 * Go to the previous tag
	 */
	private int GotoPrevTag()
	{
		int p = -1;
		if (Track.data.size() > 0)
		{
			CgData d;

			int row = panelTrackData.getSelectedRow();
			if (row < 0)
				return p;

			p = row - 1;

			if (p >= 0)
			{
				d = Track.data.get(p);

				while ((p > 0) && (d.getTag() == 0))
				{
					p--;
					d = Track.data.get(p);
				}

				if (d.getTag() != 0)
				{
					// -- Select the line and scroll to it
					panelTrackData.setSelectedRow(p);
					// -- Update the profil position
					panelProfil.setCrosshairPosition(
							Track.data.get(p).getTotal(Settings.Unit) / 1000.0,
							Track.data.get(p).getElevation(Settings.Unit));
					panelProfil.RefreshProfilInfo(p);
				}
			}
		}
		return p;
	}

	/**
	 * Display the search dialog
	 */
	private void SearchPointDialog()
	{
		if (Track.data.size() <= 0)
			return;

		final frmSearchPoint frm = new frmSearchPoint();
		frm.addListener(new frmSearchPointListener()
		{

			@Override
			public void UpdateUIEvent()
			{
				// -- Refresh the position of the map marker
				SearchPointResult result = frm.getResult();
				panelMap.RefreshCurrentPosMarker(
						Track.data.get(result.Point).getLatitude(),
						Track.data.get(result.Point).getLongitude());

				panelTrackData.setSelectedRow(result.Point);

				// -- Refresh the profil cursor position
				panelProfil.RefreshProfilInfo(result.Point);
				panelProfil.setCrosshairPosition(
						Track.data.get(result.Point).getTotal(Settings.Unit)
								/ 1000.0,
						Track.data.get(result.Point)
								.getElevation(Settings.Unit));
			}
		});
		frm.showDialog(Settings, Track);
	}

	public void ImportGPX()
	{
		if (Track.data.isEmpty())
			return;

		FrmImportChoice frm = new FrmImportChoice();

		int res = frm.showDialog();

		if (res != FrmImportChoice.RESULT_CANCEL)
		{
			String s = Utils.LoadDialog(this, Settings.previousGPXDirectory,
					".gpx", bundle.getString("frmMain.GPXFile"));
			if (!s.isEmpty())
			{
				int mode = FrmImportChoice.RESULT_AT_END;
				if (res == FrmImportChoice.RESULT_AT_END)
					mode = 1;
				else
					mode = 2;

				BackupInCGX();
				// bAutorUpdatePos = false;
				try
				{
					if (Track.OpenGPX(s, mode))
						JOptionPane.showMessageDialog(this,
								bundle.getString("frmMain.NoTimeData"));
					panelTrackData.refresh();
					RefreshStatusbar(Track);
					RefreshTitle();
					panelProfil.RefreshProfilChart();
					jPanelTimeDist.Refresh(Track, Settings);
					jPanelSpeed.Refresh(Track, Settings);
					jPanelSpeedSlope.Refresh(Track, Settings);
					panelMap.RefreshTrack(Track, true);
					PanelResume.refresh();
					panelStatistics.refresh();
					Settings.previousGPXDirectory = Utils.GetDirFromFilename(s);
					// bAutorUpdatePos = true;
				}
				catch (Exception e)
				{
					CgLog.error(
							"ImportGPX : Impossible to import the GPX file");
					e.printStackTrace();
				}
			}
		}
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path, String description)
	{
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null)
		{
			return new ImageIcon(imgURL, description);
		}
		else
		{
			System.err.println("CreateImageIcon : Couldn't find file: " + path);
			return null;
		}
	}

	/**
	 * Display a dialog box to open a GPX file
	 */
	private void OpenGPXDialog()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser
				.setCurrentDirectory(new File(Settings.previousGPXDirectory)); // System.getProperty("user.home")));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		FileFilter gpxFilter = new FileTypeFilter(".gpx",
				bundle.getString("frmMain.GPXFile")); // "GPX
														// file");
		fileChooser.addChoosableFileFilter(gpxFilter);
		fileChooser.setFileFilter(gpxFilter);

		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION)
		{
			File selectedFile = fileChooser.getSelectedFile();
			LoadGPX(selectedFile.getAbsolutePath());
			Settings.previousGPXDirectory = Utils
					.GetDirFromFilename(selectedFile.getAbsolutePath());
		}
	}

	/**
	 * Load a GPX file
	 * 
	 * @param filename
	 *            file name
	 */
	private void LoadGPX(String filename)
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		try
		{
			Track.OpenGPX(filename, 0);
			AddMruGPX(filename);
		}
		catch (Exception e)
		{
		}

		// -- Update the viewer
		panelMap.setTrack(Track);
		// -- Refresh the track information
		RefreshStatusbar(Track);

		// -- Force the update of the main table
		panelTrackData.setTrack(Track);
		panelTrackData.setSelectedRow(0);

		// -- Refresh resume grid
		PanelResume.setTrack(Track);
		// -- Refresh statistic
		panelStatistics.setTrack(Track);

		RefreshMruGPX();
		// -- Refresh profil tab
		panelProfil.setTrack(Track);
		panelProfil.setSettings(Settings);
		panelProfil.RefreshProfilChart();
		// -- Refresh analysis tab
		jPanelTimeDist.Refresh(Track, Settings);
		jPanelSpeed.Refresh(Track, Settings);
		jPanelSpeedSlope.Refresh(Track, Settings);
		// -- Refresh the form title
		RefreshTitle();
		// Refresh the main toolbar
		RefreshMainToolbar();
		// Refresh the main menu
		RefreshMainMenu();

		bNoBackup = true;

		if (Track.data.size() > 0)
			panelMap.RefreshCurrentPosMarker(Track.data.get(0).getLatitude(),
					Track.data.get(0).getLongitude());

		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/**
	 * Save the track in GPX format
	 */
	private void SaveGPX()
	{
		String s;

		if (Track.data.isEmpty())
			return;

		s = Utils.SaveDialog(this, Settings.previousGPXDirectory, "", ".gpx",
				bundle.getString("frmMain.GPXFile"), true,
				bundle.getString("frmMain.FileExist"));

		if (!s.isEmpty())
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			// -- Save track
			Track.SaveGPX(s, 0, Track.data.size() - 1);
			// -- Store the directory
			Settings.previousGPXDirectory = Utils.GetDirFromFilename(s);

			// -- Update GPX MRU
			AddMruGPX(s);
			RefreshMruGPX();

			// -- Reset the track modified flag
			Track.isModified = false;
			// -- Refresh info panel
			RefreshStatusbar(Track);

			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * Display a dialog box to open a CGX file
	 */
	private void OpenCGXDialog()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser
				.setCurrentDirectory(new File(Settings.previousCGXDirectory)); // System.getProperty("user.home")));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		FileFilter cgxFilter = new FileTypeFilter(".cgx",
				bundle.getString("frmMain.CGXFile")); // "CGX
														// file");
		fileChooser.addChoosableFileFilter(cgxFilter);
		fileChooser.setFileFilter(cgxFilter);

		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION)
		{
			File selectedFile = fileChooser.getSelectedFile();
			LoadCGX(selectedFile.getAbsolutePath());
			Settings.previousCGXDirectory = Utils
					.GetDirFromFilename(selectedFile.getAbsolutePath());
		}
	}

	/**
	 * Load a CGX file
	 * 
	 * @param filename
	 *            File name
	 */
	private void LoadCGX(String filename)
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		try
		{
			Track.OpenCGX(this, filename, CgConst.IMPORT_MODE_LOAD, false);
			AddMruCGX(filename);
		}
		catch (Exception e)
		{
		}

		// -- Update the viewer
		panelMap.setTrack(Track);
		// -- Refresh the track information
		RefreshStatusbar(Track);
		// -- Refresh resume grid
		PanelResume.setTrack(Track);

		// -- Refresh profil tab
		panelProfil.setTrack(Track);
		panelProfil.setSettings(Settings);
		panelProfil.RefreshProfilChart();
		// -- Refresh analysis tab
		jPanelTimeDist.Refresh(Track, Settings);
		jPanelSpeed.Refresh(Track, Settings);
		jPanelSpeedSlope.Refresh(Track, Settings);
		// -- Refresh the form title
		RefreshTitle();
		// Refresh the main toolbar
		RefreshMainToolbar();
		// Refresh the main menu
		RefreshMainMenu();

		// -- Refresh statistic
		panelStatistics.setTrack(Track);

		// -- Force the update of the main table
		panelTrackData.setTrack(Track);
		panelTrackData.setSelectedRow(0);
		RefreshMruCGX();
		bNoBackup = true;

		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/**
	 * Save the track in CGX format
	 */
	private void SaveCGX()
	{
		String s;

		if (Track.data.isEmpty())
			return;

		s = Utils.SaveDialog(this, Settings.previousCGXDirectory, "", ".cgx",
				bundle.getString("frmMain.CGXFile"), true,
				bundle.getString("frmMain.FileExist"));

		if (!s.isEmpty())
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			// -- Save track
			Track.SaveCGX(s, 0, Track.data.size() - 1, false);
			// -- Store the directory
			Settings.previousCGXDirectory = Utils.GetDirFromFilename(s);

			// -- Update GPX MRU
			AddMruCGX(s);
			RefreshMruCGX();

			// -- Reset the track modified flag
			Track.isModified = false;
			// -- Refresh info panel
			RefreshStatusbar(Track);

			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * Export tags as waypoints
	 */
	private void ExportTagsAsWaypoints()
	{
		String s;

		if (Track.data.isEmpty())
			return;

		s = Utils.SaveDialog(this, Settings.LastDir, "", ".gpx",
				bundle.getString("frmMain.GPXFile"), true,
				bundle.getString("frmMain.FileExist"));

		if (!s.isEmpty())
		{
			FrmExportWaypoints frm = new FrmExportWaypoints();
			if (frm.showDialog())
			{
				Track.SaveWaypoint(s, frm.getTag());
			}
		}
	}

	private void CheckOfflineMapsSize()
	{
		StrMapsDirSize = Utils.humanReadableByteCount(Utils.folderSize(new File(
				DataDir + "/" + CgConst.CG_DIR + "/OpenStreetMapTileCache")),
				true);
	}

	/**
	 * Load the configuration file
	 */
	private void LoadConfig()
	{
		Settings.Load(DataDir + "/" + CgConst.CG_DIR);
	}

	/**
	 * Save the settings in a configuration file
	 */
	private void SaveConfig()
	{
		// -- Get the form dimension
		Rectangle r = getBounds();
		Settings.MainWindowWidth = r.width;
		Settings.MainWindowHeight = r.height;

		Settings.VertSplitPosition = SplitPaneMain.getDividerLocation();
		Settings.HorizSplitPosition = SplitPaneMainRight.getDividerLocation();

		// -- Update the column width in the settings
		panelTrackData.updateColumnWidth();

		Settings.Save(DataDir + "/" + CgConst.CG_DIR);
	}

	/**
	 * Called by the quit application menu
	 * 
	 * @param evt
	 */
	private void mnuQuitActionPerformed(java.awt.event.ActionEvent evt)
	{
		this.processWindowEvent(
				new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	// private void btTestActionPerformed(java.awt.event.ActionEvent evt) {
	// // Bouge la carte de 100 pixel
	// // MapViewer.moveMap(100, 100);
	//
	// // Essai lecture ressource image
	// ImageIcon icon = createImageIcon("images/save.png", "a pretty but
	// meaningless splat");
	// // jLabelFooterTotalTime1.setIcon(icon);
	// // jLabelFooterTotalTime1.setText("");
	// // Fin essai lecture ressource image
	//
	// // Met un marqueur au coordonnée indiqué
	// Coordinate paris = new Coordinate(48.8567, 2.3508);
	// MapViewer.addMapMarker(new MapMarkerImg(paris, icon.getImage()));
	//
	// // Coordinate one = new Coordinate(48.8567, 2.3508);
	// // Coordinate two = new Coordinate(48.8567, 2.4508);
	// // Coordinate three = new Coordinate(48.9567, 2.4508);
	// // List<Coordinate> route = new ArrayList<Coordinate>(Arrays.asList(one,
	// // two, two));
	// List<Coordinate> route = new ArrayList<Coordinate>();
	// route.add(new Coordinate(48.8567, 2.3508));
	// route.add(new Coordinate(48.8567, 2.4508));
	// route.add(new Coordinate(48.9567, 2.4508));
	// route.add(new Coordinate(48.9668, 2.4518));
	// MapPolyLine polyLine = new MapPolyLine(route);
	// polyLine.setColor(Color.red);
	// MapViewer.addMapPolygon(polyLine);
	//
	// List<Coordinate> route1 = new ArrayList<Coordinate>();
	// route1.add(new Coordinate(48.9668, 2.4518));
	// route1.add(new Coordinate(48.9669, 2.4510));
	// route1.add(new Coordinate(48.9670, 2.4508));
	// route1.add(new Coordinate(48.9668, 2.4518));
	// MapPolyLine polyLine1 = new MapPolyLine(route1);
	// polyLine1.setColor(Color.green);
	// MapViewer.addMapPolygon(polyLine1);
	//
	// // MapPolygonImpl polyLine = new MapPolygonImpl(route);
	//
	// // MapViewer.setTileSource(new Thunderforest_Landscape());
	//
	// //MapViewer.setTileSource(new Thunderforest_Outdoors());
	// RefreshMapType();
	// //MapViewer.setTileSource(new OpenTopoMap());
	//
	// // MapViewer.setTileSource(new Thunderforest_Transport());
	//
	// // MapViewer.setTileSource(new OsmTileSource.Mapnik());
	// // MapViewer.setTileSource(new OsmTileSource.CycleMap());
	// // MapViewer.setTileSource(new BingAerialTileSource());
	// // MapViewer.setTileSource(new MapQuestOsmTileSource());
	// // MapViewer.setTileSource(new MapQuestOpenAerialTileSource());
	//
	// }

	/**
	 * Called when the main form is closing
	 * 
	 * @param evt
	 */
	private void formWindowClosing(java.awt.event.WindowEvent evt)
	{
		if (Track.isModified)
		{
			Object[] options =
			{ " " + bundle.getString("frmMain.ClosingYes") + " ",
					" " + bundle.getString("frmMain.ClosingNo") + " " };
			int ret = JOptionPane.showOptionDialog(this,
					bundle.getString("frmMain.ClosingMessage"),
					bundle.getString("frmMain.ClosingTitle"),
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
					null, options, options[1]);

			if (ret == JOptionPane.YES_OPTION)
			{
				setDefaultCloseOperation(EXIT_ON_CLOSE);
				SaveConfig();
				CgLog.info("End of the application! Bye :(");
			}
			else
			{
				setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			}
		}
		else
		{ // No modification! Bye
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			SaveConfig();
			CgLog.info("End of the application! Bye :(");
		}
	}

	/**
	 * Set form title
	 */
	private void RefreshTitle()
	{
		String s = "Course Generator " + Version;
		if (!Track.Name.isEmpty())
			s = s + " - " + Track.Name;
		setTitle(s);
	}

	private void mnuMruGPXActionPerformed(java.awt.event.ActionEvent evt)
	{
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

	private void mnuMruCGXActionPerformed(java.awt.event.ActionEvent evt)
	{
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

	/**
	 * Refresh the main toolbar
	 */
	private void RefreshMainToolbar()
	{
		if (Track == null)
			return;

		boolean isLoaded = !Track.Name.isEmpty();

		btSaveCGX.setEnabled(isLoaded);
		btUndo.setEnabled(isLoaded);
		btSearch.setEnabled(isLoaded);
		btGotoPreviousMark.setEnabled(isLoaded);
		btGotoNextMark.setEnabled(isLoaded);
		btDisplaySSCurves.setEnabled(isLoaded);
		btTrackSettings.setEnabled(isLoaded);
		btFillCoeff.setEnabled(isLoaded);
		btFillDiff.setEnabled(isLoaded);
		btCalculateTrackTime.setEnabled(isLoaded);
		btMiniRoadbook.setEnabled(isLoaded);
	}

	/**
	 * Refresh the main menu
	 */
	private void RefreshMainMenu()
	{
		if (Track == null)
			return;

		boolean isLoaded = !Track.Name.isEmpty();

		mnuSaveCGX.setEnabled(isLoaded);
		mnuSaveGPX.setEnabled(isLoaded);
		mnuSaveCSV.setEnabled(isLoaded);
		mnuSavePartCGX.setEnabled(isLoaded);
		mnuSavePartGPX.setEnabled(isLoaded);
		mnuSavePartCSV.setEnabled(isLoaded);
		mnuExportPoints.setEnabled(isLoaded);
		mnuExportTagAsWaypoints.setEnabled(isLoaded);
		mnuCopy.setEnabled(isLoaded);
		mnuSearchPoint.setEnabled(isLoaded);
		mnuMarkPosition.setEnabled(isLoaded);
		mnuGotoNextMark.setEnabled(isLoaded);
		mnuGotoPrevMark.setEnabled(isLoaded);
		mnuGenerateMiniRoadbook.setEnabled(isLoaded);
		mnuFindMinMax.setEnabled(isLoaded);
		mnuInvertTrack.setEnabled(isLoaded);
		mnuDefineNewStart.setEnabled(isLoaded);
		mnuCalculateTrackTime.setEnabled(isLoaded);
		mnuTrackSettings.setEnabled(isLoaded);
		mnuSpeedSlopeCurves.setEnabled(isLoaded);
	}

	/**
	 * Refresh the statusbar
	 * 
	 * @param tdata
	 *            Track data object
	 */
	private void RefreshStatusbar(TrackData tdata)
	{

		// -- Distance
		LbInfoTotalDistVal.setText(String.format("%1.3f ",
				tdata.getTotalDistance(Settings.Unit) / 1000.0)
				+ Settings.getDistanceUnitString());

		// -- Ascent
		LbInfoDpVal
				.setText(String.format("%1.0f ", tdata.getClimbP(Settings.Unit))
						+ Settings.getElevationUnitString());

		// -- Descent
		LbInfoDmVal
				.setText(String.format("%1.0f ", tdata.getClimbM(Settings.Unit))
						+ Settings.getElevationUnitString());

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
		if (Track.isModified)
		{
			LbModifiedVal.setText(
					bundle.getString("frmMain.LbModified_Modified.text"));
		}
		else
		{
			LbModifiedVal
					.setText(bundle.getString("frmMain.LbModified_Ok.text"));
		}

		// -- Calculation
		if (Track.isCalculated)
		{
			LbInfoCalculateVal.setText(
					bundle.getString("frmMain.LbInfoCalculateOk.text"));
			LbInfoCalculateVal.setBackground(new Color(112, 232, 6));
		}
		else
		{
			LbInfoCalculateVal.setText(
					bundle.getString("frmMain.LbInfoCalculateTodo.text"));
			LbInfoCalculateVal.setBackground(new Color(255, 209, 7));
		}

		// -- Internet
		if (InternetConnectionActive)
		{
			LbInfoInternetVal.setText(
					bundle.getString("frmMain.LbInfoInternetOnline.text"));
			LbInfoInternetVal.setBackground(new Color(112, 232, 6));
		}
		else
		{
			LbInfoInternetVal.setText(
					bundle.getString("frmMain.LbInfoInternetOffline.text"));
			LbInfoInternetVal.setBackground(new Color(255, 209, 7));
		}

		// -- Unit
		switch (Settings.Unit)
		{
		case CgConst.UNIT_METER:
			LbInfoUnitVal
					.setText(bundle.getString("frmMain.LbInfoUnitMeter.text"));
			break;
		case CgConst.UNIT_MILES_FEET:
			LbInfoUnitVal.setText(
					bundle.getString("frmMain.LbInfoUnitMilesFeet.text"));
			break;
		default:
			LbInfoUnitVal
					.setText(bundle.getString("frmMain.LbInfoUnitMeter.text"));
		}

		// -- Map dir size
		LbInfoMapDirSize.setText(StrMapsDirSize);
	}

	/**
	 * Refresh the GPX most recent used files menu
	 */
	private void RefreshMruGPX()
	{
		// -- Mru 1
		if (Settings.mruGPX[0].isEmpty())
		{
			mnuMruGPX1.setVisible(false);
		}
		else
		{
			mnuMruGPX1.setText(Settings.mruGPX[0]);
			mnuMruGPX1.setVisible(true);
		}

		// -- Mru 2
		if (Settings.mruGPX[1].isEmpty())
		{
			mnuMruGPX2.setVisible(false);
		}
		else
		{
			mnuMruGPX2.setText(Settings.mruGPX[1]);
			mnuMruGPX2.setVisible(true);
		}

		// -- Mru 3
		if (Settings.mruGPX[2].isEmpty())
		{
			mnuMruGPX3.setVisible(false);
		}
		else
		{
			mnuMruGPX3.setText(Settings.mruGPX[2]);
			mnuMruGPX3.setVisible(true);
		}

		// -- Mru 4
		if (Settings.mruGPX[3].isEmpty())
		{
			mnuMruGPX4.setVisible(false);
		}
		else
		{
			mnuMruGPX4.setText(Settings.mruGPX[3]);
			mnuMruGPX4.setVisible(true);
		}

		// -- Mru 5
		if (Settings.mruGPX[4].isEmpty())
		{
			mnuMruGPX5.setVisible(false);
		}
		else
		{
			mnuMruGPX5.setText(Settings.mruGPX[4]);
			mnuMruGPX5.setVisible(true);
		}

	}

	/**
	 * Add "filename" to the CGX mru menu
	 * 
	 * @param filename
	 *            name of the file to add
	 */
	public void AddMruGPX(String filename)
	{
		int i;
		for (i = 4; i >= 0; i--)
		{
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
	private void RefreshMruCGX()
	{
		// -- Mru 1
		if (Settings.mruCGX[0].isEmpty())
		{
			mnuMruCGX1.setVisible(false);
		}
		else
		{
			mnuMruCGX1.setText(Settings.mruCGX[0]);
			mnuMruCGX1.setVisible(true);
		}

		// -- Mru 2
		if (Settings.mruCGX[1].isEmpty())
		{
			mnuMruCGX2.setVisible(false);
		}
		else
		{
			mnuMruCGX2.setText(Settings.mruCGX[1]);
			mnuMruCGX2.setVisible(true);
		}

		// -- Mru 3
		if (Settings.mruCGX[2].isEmpty())
		{
			mnuMruCGX3.setVisible(false);
		}
		else
		{
			mnuMruCGX3.setText(Settings.mruCGX[2]);
			mnuMruCGX3.setVisible(true);
		}

		// -- Mru 4
		if (Settings.mruCGX[3].isEmpty())
		{
			mnuMruCGX4.setVisible(false);
		}
		else
		{
			mnuMruCGX4.setText(Settings.mruCGX[3]);
			mnuMruCGX4.setVisible(true);
		}

		// -- Mru 5
		if (Settings.mruCGX[4].isEmpty())
		{
			mnuMruCGX5.setVisible(false);
		}
		else
		{
			mnuMruCGX5.setText(Settings.mruCGX[4]);
			mnuMruCGX5.setVisible(true);
		}

	}

	/**
	 * Add "filename" to the CGX mru menu
	 * 
	 * @param filename
	 *            name of the file to add
	 */
	public void AddMruCGX(String filename)
	{
		int i;
		for (i = 4; i >= 0; i--)
		{
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
	public static void setUIFont(javax.swing.plaf.FontUIResource f)
	{
		java.util.Enumeration keys = javax.swing.UIManager.getDefaults().keys();
		while (keys.hasMoreElements())
		{
			Object key = keys.nextElement();
			Object value = javax.swing.UIManager.get(key);
			if (value != null
					&& value instanceof javax.swing.plaf.FontUIResource)
			{
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
	public static void main(final String args[])
	{
		// -- Get the VM parameters
		String inEclipseStr = System.getProperty("runInEclipse");
		inEclipse = "true".equalsIgnoreCase(inEclipseStr);

		// -- Set the data dir
		String DataDir = Utils.GetHomeDir();

		// -- ensure log directory exists
		new File(new File(DataDir + "/" + CgConst.CG_DIR), "logs").mkdirs();

		// -- Initialize the log directory
		log = new CgLog(DataDir + "/" + CgConst.CG_DIR + "/logs/logs.txt",
				10 * 1024 * 1024, !inEclipse); // 10Mb
												// file

		// -- Set the look and feel
		try
		{
			// Set System L&F
			OsCheck.OSType ostype = OsCheck.getOperatingSystemType();
			switch (ostype)
			{
			case Windows:
				for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
						.getInstalledLookAndFeels())
				{
					if ("Windows".equals(info.getName()))
					{
						javax.swing.UIManager
								.setLookAndFeel(info.getClassName());
						break;
					}
					else
					{
						javax.swing.UIManager
								.setLookAndFeel(javax.swing.UIManager
										.getSystemLookAndFeelClassName());
					}
				}
				break;
			case MacOS:
				javax.swing.UIManager.setLookAndFeel(
						javax.swing.UIManager.getSystemLookAndFeelClassName());
				break;
			case Linux:
				// Toolkit.getDefaultToolkit().setDynamicLayout(true);
				// System.setProperty("sun.awt.noerasebackground", "true");
				// //JFrame.setDefaultLookAndFeelDecorated(true);
				// //JDialog.setDefaultLookAndFeelDecorated(true);

				try
				{
					javax.swing.UIManager.setLookAndFeel(
							"de.muntjak.tinylookandfeel.TinyLookAndFeel");
					// continuous layout on frame resize
					Toolkit.getDefaultToolkit().setDynamicLayout(true);
					// no flickering on resize
					System.setProperty("sun.awt.noerasebackground", "true");
					// Theme.loadTheme(de.muntjak.tinylookandfeel.TinyLookAndFeel.class.getResource("/themes/CG_Gray.theme"));
					// Theme.loadTheme(course_generator.class().getResource("/course_generator/CG_Gray.theme"));
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
					javax.swing.UIManager.getSystemLookAndFeelClassName();
				}
				break;
			case Other:
				javax.swing.UIManager.setLookAndFeel(
						javax.swing.UIManager.getSystemLookAndFeelClassName());
				break;
			}
		}
		catch (ClassNotFoundException ex)
		{
			ex.printStackTrace();
		}
		catch (InstantiationException ex)
		{
			ex.printStackTrace();
		}
		catch (IllegalAccessException ex)
		{
			ex.printStackTrace();
		}
		catch (javax.swing.UnsupportedLookAndFeelException ex)
		{
			ex.printStackTrace();
		}

		// -- Set things according to the OS
		OsCheck.OSType ostype = OsCheck.getOperatingSystemType();
		switch (ostype)
		{
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
		default:
			// setUIFont(new javax.swing.plaf.FontUIResource("Arial",
			// Font.PLAIN, 12));
			break;
		}

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
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
	private javax.swing.JSplitPane SplitPaneMain;
	private javax.swing.JSplitPane SplitPaneMainRight;
	private javax.swing.JTabbedPane TabbedPaneMain;
	private javax.swing.JToolBar ToolBarMain;
	private javax.swing.JButton btOpenCGX;
	private javax.swing.JButton btOpenGPX;
	private javax.swing.JMenu mnuFile;
	private javax.swing.JMenu mnuEdit;
	private javax.swing.JPanel jPanelAnalyze;
	private javax.swing.JPanel jPanelLeft;
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
