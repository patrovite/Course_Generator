/*
 * Course Generator - Main form
 * Copyright (C) 2008-2018 Pierre Delore
 *
 * Contributor(s) :
 * Frederic
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
 * - Under Linux add "-Dgnu.java.awt.peer.gtk.Graphics=Graphics2D -Dawt.useSystemAAFontSettings=on -Dswing.aatext=true"  in Arguments/VM argument 
 * This will allow to have the anti aliasing for the font.
 *  
  * Used libraries:
 *  - Commons-Suncalc - Apache License 2.0 - https://shredzone.org/maven/commons-suncalc/index.html
 *  - jcommon - LGPL - http://www.jfree.org/jcommon/
 *  - jfreechart - LGPL - http://www.jfree.org/index.html
 *  - Joda-time - http://www.joda.org/joda-time/
 *  - JXMapViewer2 - LGPL - https://wiki.openstreetmap.org/wiki/JXMapViewer2
  *  - SwingX - LGPL 2.1 - https://swingx.java.net/
 *  - Timeshape - MIT - https://github.com/RomanIakovlev/timeshape
 *  - TinyLaF - LGPL - Hans Bickel - http://www.muntjak.de/hans/java/tinylaf/ 
 *  
 * Copyrights:
 * Maps :
 * - Openstreetmap : http://www.openstreetmap.org/
 * - OpenTopoMap : https://opentopomap.org/
 * - Bing map : (c) Microsoft
 * - Stamen toner and terrain : Stamen Design, under CC BY 3.0. Data by OpenStreetMap, under ODbL.
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
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
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
import javax.swing.SwingUtilities;
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
import course_generator.dialogs.FrmElevationFilter;
import course_generator.dialogs.FrmExportWaypoints;
import course_generator.dialogs.FrmImportChoice;
import course_generator.dialogs.frmEditPosition;
import course_generator.dialogs.frmExportPoints;
import course_generator.dialogs.frmFillCoeff;
import course_generator.dialogs.frmFillCoeff.EditCoeffResult;
import course_generator.dialogs.frmFillDiff;
import course_generator.dialogs.frmFillDiff.EditDiffResult;
import course_generator.dialogs.frmSearchCurve;
import course_generator.dialogs.frmSearchDistance;
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
import course_generator.releaseNote.frmReleaseNote;
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
import course_generator.weather.JPanelWeather;
/**
 * This is the main class of the project.
 *
 * @author pierre.delore
 */
public class frmMain extends javax.swing.JFrame {
	private static final long serialVersionUID = 6484405417503538528L;

	private final static String Version = "4.5.0";

	public static boolean inEclipse = false;
	public static CgLog log = null;

	public TrackData Track, Backup_Track;
	private ResumeData Resume;
	private final TrackDataModel ModelTableMain;
	public CgSettings Settings;
	public String DataDir;
	private java.util.ResourceBundle bundle = null;
	private int cmptInternetConnexion = 0;
	private int cmptMinute = 0;
	private int cmptRelease = -1;
	private boolean InternetConnectionActive = false;
	private Timer timer1s; // 1 second timer object
	private boolean bNoBackup = true;
	private String StrMapsDirSize = "";
	private String CurrentLanguage = "";

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
	private JMenuItem mnuQuit;
	private static JMenuItem mnuCopy;
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
	private JMenuItem menuCGFaq;
	private JMenuItem menuCGCoursesLibrary;
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
	private JMenuItem mnuCheckUpdate;
	private JCheckBoxMenuItem mnuReadOnly;
	private JLabel LbReadOnly;
	private JMenuItem mnuDisplayCopyCurves;
	private Component TabMain;
	private JLabel LbTabMain;
	private JLabel LbTabProfil;
	private JLabel LbTabStatistics;
	private JLabel LbTabAnalyze;
	private JLabel LbTabTimeDist;
	private JLabel LbTabSpeed;
	private JLabel LbTabSpeedSlope;
	private JLabel LbTabResume;
	private JMenu mnuImport;
	private JMenu mnuExport;
	private JMenu mnuTags;
	private JMenuItem mnuSaveAsCGX;

	private JMenuItem mnuDisplayLogDir;

	private JMenuItem mnuSearchCurveFromFinalTime;

	private JMenuItem mnuSmoothElevation;
	private JPanelWeather panelWeather;
	private JMenuItem mnuReleaseNotes;

	private JMenuItem mnuSearchDistance;

	
	// -- Called every second
	class TimerActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			cmptInternetConnexion++;
			if (cmptInternetConnexion > Settings.ConnectionTimeout) {
				cmptInternetConnexion = 0;
				InternetConnectionActive = Utils.isInternetReachable();
				RefreshStatusbar(Track);
			}
			cmptMinute++;
			if (cmptMinute > 60) {
				cmptMinute = 0;
				// -- Check every minute if we need to switch log file
				CgLog.checkFileSize();
				CalcOfflineMapsSize();
			}
			if (cmptRelease>0) {
				cmptRelease--;
				if (cmptRelease==0) {
					ShowReleaseNote();
					cmptRelease=-1;
				}
			}

		}
	}


	/**
	 * Creates new form frmMain !!!! Everything start here !!!!
	 */
	public frmMain(String args[]) {
		// -- Get the current time to measure the initialization time
		long ts = System.currentTimeMillis();
		
		// -- Initialize data dir
		DataDir = Utils.GetHomeDir();

		// -- Initialize program dir
		Utils.SetProgDir(inEclipse);
	
		// -- Create the tiles cache folders if necessary
		File dirs = new File(DataDir + "/" + CgConst.CG_DIR, "TileCache/" + CgConst.OPENSTREETMAP_CACHE_DIR);
		dirs.mkdirs();
		dirs = new File(DataDir + "/" + CgConst.CG_DIR, "TileCache/" + CgConst.OPENTOPOMAP_CACHE_DIR);
		dirs.mkdirs();
		dirs = new File(DataDir + "/" + CgConst.CG_DIR, "TileCache/" + CgConst.OUTDOORS_CACHE_DIR);
		dirs.mkdirs();
		dirs = new File(DataDir + "/" + CgConst.CG_DIR, "TileCache/" + CgConst.BING_CACHE_DIR);
		dirs.mkdirs();

		// -- Create the theme folders if necessary
		dirs = new File(DataDir + "/" + CgConst.CG_DIR, "themes/");
		dirs.mkdirs();

		// -- Create the curves folders if necessary
		dirs = new File(DataDir + "/" + CgConst.CG_DIR, "curves/");
		dirs.mkdirs();
		
		// -- Initialize data
		Resume = new ResumeData();
		Settings = new CgSettings();
		Track = new TrackData(Settings);
		Backup_Track = new TrackData(Settings);

		//-- Say to the system that we want to use the default proxy settings (the simpliest solution!)
		System.setProperty("java.net.useSystemProxies", "true");
		
		// showProfilMarker=true;

		// -- Load configuration
		LoadConfig();
		
		//-- Post configuration loading settings
		Track.MrbSizeW = Settings.DefMrbWidth;
		Track.MrbSizeH = Settings.DefMrbHeight;
				
		//-- Application and system info in the log. Nothing exit from the computer!
		CgLog.info("Start Course Generator version " + Version);
		CgLog.info("Java version : " + System.getProperty("java.version"));
		CgLog.info("java.runtime.name : " + System.getProperty("java.runtime.name"));
		CgLog.info("java.vm.version : " + System.getProperty("java.vm.version"));
		CgLog.info("java.vm.vendor : " + System.getProperty("java.vm.vendor"));
		CgLog.info("path.separator : " + System.getProperty("path.separator"));
		CgLog.info("user.country : " + System.getProperty("user.country"));
		CgLog.info("java.runtime.version : " + System.getProperty("java.runtime.version"));
		CgLog.info("os.arch : " + System.getProperty("os.arch"));
		CgLog.info("os.name : " + System.getProperty("os.name"));
		CgLog.info("user.timezone : " + System.getProperty("user.timezone"));
		CgLog.info("file.encoding : " + System.getProperty("file.encoding"));
		CgLog.info("java.specification.version : " + System.getProperty("java.specification.version"));
		CgLog.info("java.vm.specification.version : " + System.getProperty("java.vm.specification.version"));
		CgLog.info("sun.arch.data.model : " + System.getProperty("sun.arch.data.model"));
		CgLog.info("user.language : " + System.getProperty("user.language"));
		CgLog.info("java.version : " + System.getProperty("java.version"));
		CgLog.info("java.vendor : " + System.getProperty("java.vendor"));
		CgLog.info("file.separator : " + System.getProperty("file.separator"));
		CgLog.info("sun.cpu.endian : " + System.getProperty("sun.cpu.endian"));
		CgLog.info("sun.desktop : " + System.getProperty("sun.desktop"));
		CgLog.info("sun.cpu.isalist : " + System.getProperty("sun.cpu.isalist"));
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		CgLog.info("Screen size : " + screen.width + "x" + screen.height);
		CgLog.info("AppDir = " + Utils.ProgDir);

		// -- List the java properties
		// -- To activate only if necessary. It talks a lot!
		// System.getProperties().list(System.out);

		// -- Set the language
		CgLog.info("System language : " + Locale.getDefault().toString());
		// -- Set the language
		if (Settings.Language.isEmpty()) {
			// -- System language
			CgLog.info("Configured language : System");
		} else {
			CgLog.info("Configured language : " + Settings.Language);
			if (Settings.Language.equalsIgnoreCase("FR")) {
				Locale.setDefault(Locale.FRANCE);
			} else if (Settings.Language.equalsIgnoreCase("EN")) {
				Locale.setDefault(Locale.US);
			} else if (Settings.Language.equalsIgnoreCase("ES")) {
				Locale.setDefault(new Locale("es", "ES"));
			} else if (Settings.Language.equalsIgnoreCase("PT")) {
				Locale.setDefault(new Locale("pt", "PT"));
			} else {
				Locale.setDefault(Locale.US);
			}
		}

		CgLog.info("Selected language : " + Locale.getDefault().toString());

		// -- Select the language for help
		String tmpLang = Locale.getDefault().getLanguage();
		if (tmpLang.equalsIgnoreCase("fr"))
			CurrentLanguage = "fr";
		else
			CurrentLanguage = "en";

		// -- Set default font
		SetDefaultFont();

		/*
		 * if (Settings.DefaultFontName.isEmpty()) Settings.DefaultFontName="Arial";
		 * 
		 * //setUIFont(new javax.swing.plaf.FontUIResource("Arial", Font.PLAIN, 14));
		 * setUIFont(new javax.swing.plaf.FontUIResource(Settings.DefaultFontName,
		 * Settings.DefaultFontStyle, Settings.DefaultFontSize));
		 * 
		 * CgLog.info("Default font: "+javax.swing.UIManager.getDefaults().getFont(
		 * "TabbedPane.font").toString());
		 */

		// -- Initialize the track data model (here because we need to know the current
		// language
		ModelTableMain = new TrackDataModel(Track, Settings);

		// -- Initialize the string resource for internationalization
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");

		// -- Configure the main form
		initComponents();

		// -- Set the icon of the application
		setIconImage(createImageIcon("/course_generator/images/png/logo_cg.png", "").getImage());

		// -- Set the preferred column width
		panelTrackData.setColumnWidth();

		// -- Set the windows size and center it in the screen - Not tested on
		// multi-screen configuration
		// -- Not currently but I leave the code for the moment
		/*
		 * Rectangle r = getBounds(); r.width = Settings.MainWindowWidth; r.height =
		 * Settings.MainWindowHeight; Dimension screensize =
		 * Toolkit.getDefaultToolkit().getScreenSize(); r.x = (screensize.width -
		 * r.width) / 2; r.y = (screensize.height - r.height) / 2; setBounds(r);
		 */

		// -- Maximize the window
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);

		// -- Check the maps dir size
		CalcOfflineMapsSize();

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

		//-- Used map dir. Ask to the user to delete it!
		String tmpstr = DataDir + "/" + CgConst.CG_DIR + "/OpenStreetMapTileCache";
		Path DataFolder = Paths.get(tmpstr);
		if (Files.exists(DataFolder)) {
			JOptionPane.showMessageDialog(this, String.format(bundle.getString("frmMain.UnusedTileCacheDir"), tmpstr),
					"Course Generator", JOptionPane.INFORMATION_MESSAGE);
		}

		//ExportCurvesFromResource(false); //No more used since V4.4. When sure delete this line and the function

		// -- Display the splash screen
		showDialogAbout(this, true, false, Version);

		// -- Check if we need to display the release notes
		if (!Settings.ReleaseVersion.equalsIgnoreCase(Version)) {
			cmptRelease=3; //Delay the display of the release notes dialog box by x seconds
		}

		// -- Log the initialization time
		CgLog.info("Application initialization time : " + (System.currentTimeMillis() - ts) + "ms");

		// -- Check for update
		if (Settings.Check4UpdateAtStart)
			Check4Update();
		
	}


	/**
	 * Launch the calculation on the track
	 */
	private void CalcTrackTime() {
		if (Track.data.isEmpty())
			return;

		// -- Read only? Exit
		if (Track.ReadOnly)
			return;

		// -- Calculation
		Track.Calculate();

		// -- Refresh
		RefreshStatusbar(Track);

		CalcClimbResult ccr = new CalcClimbResult();
		ccr = Track.CalcClimb(CgConst.ELEV_NORM, 0, Track.data.size() - 1, ccr);
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
		jPanelSpeed.Refresh(Track, Settings);
		jPanelSpeedSlope.Refresh(Track, Settings);

		// Refresh map
		panelMap.RefreshTrack(Track, true);

	}


	/**
	 * Create the main menu
	 */

	@SuppressWarnings("deprecation")
	private void Create_MenuBarMain() {
		// Create the menu
		mnuMain = new javax.swing.JMenuBar();
		mnuMain.setName("mnuMain");

		// == File menu ========================================================
		mnuFile = new javax.swing.JMenu();

		// -- Open CGX
		mnuOpenCGX = new javax.swing.JMenuItem();
		mnuOpenCGX.setAccelerator(
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
		mnuOpenCGX.setIcon(Utils.getIcon(this, "open_cgx.png", Settings.MenuIconSize));
		mnuOpenCGX.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				OpenCGXDialog();
			}
		});
		mnuFile.add(mnuOpenCGX);

		// -- Recent CGX files
		// --------------------------------------------------
		mnuLastCGX = new javax.swing.JMenu();
		mnuLastCGX.setIcon(Utils.getIcon(this, "open_cgx.png", Settings.MenuIconSize));

		// -- Mru CGX n°1
		mnuMruCGX1 = new javax.swing.JMenuItem();
		mnuMruCGX1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuMruCGXActionPerformed(evt);
			}
		});
		mnuLastCGX.add(mnuMruCGX1);

		// -- Mru CGX n°2
		mnuMruCGX2 = new javax.swing.JMenuItem();
		mnuMruCGX2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuMruCGXActionPerformed(evt);
			}
		});
		mnuLastCGX.add(mnuMruCGX2);

		// -- Mru CGX n°3
		mnuMruCGX3 = new javax.swing.JMenuItem();
		mnuMruCGX3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuMruCGXActionPerformed(evt);
			}
		});
		mnuLastCGX.add(mnuMruCGX3);

		// -- Mru CGX n°4
		mnuMruCGX4 = new javax.swing.JMenuItem();
		mnuMruCGX4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuMruCGXActionPerformed(evt);
			}
		});
		mnuLastCGX.add(mnuMruCGX4);

		// -- Mru CGX n°5
		mnuMruCGX5 = new javax.swing.JMenuItem();
		mnuMruCGX5.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuMruCGXActionPerformed(evt);
			}
		});
		mnuLastCGX.add(mnuMruCGX5);

		mnuFile.add(mnuLastCGX);
		
		// -- Save CGX
		mnuSaveCGX = new javax.swing.JMenuItem();
		mnuSaveCGX.setAccelerator(
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
		mnuSaveCGX.setIcon(Utils.getIcon(this, "save_cgx.png", Settings.MenuIconSize));
		mnuSaveCGX.setEnabled(false);
		mnuSaveCGX.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SaveCGX(); 
			}
		});
		mnuFile.add(mnuSaveCGX);

		// -- Save as CGX
		mnuSaveAsCGX = new javax.swing.JMenuItem();
		mnuSaveAsCGX.setIcon(Utils.getIcon(this, "save_cgx.png", Settings.MenuIconSize));
		mnuSaveAsCGX.setEnabled(false);
		mnuSaveAsCGX.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SaveAsCGX();
			}
		});
		mnuFile.add(mnuSaveAsCGX);
		
		//##########################
		
		// -- Separator
		mnuFile.add(new javax.swing.JPopupMenu.Separator());
				
		//##########################	
		
		// -- Open GPX
		mnuOpenGPX = new javax.swing.JMenuItem();
		mnuOpenGPX.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O,
				java.awt.event.InputEvent.CTRL_MASK | java.awt.event.InputEvent.SHIFT_MASK));
		mnuOpenGPX.setIcon(Utils.getIcon(this, "open_gpx.png", Settings.MenuIconSize));
		mnuOpenGPX.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				OpenGPXDialog();
			}
		});
		mnuFile.add(mnuOpenGPX);

		// -- Recent GPX files
		// --------------------------------------------------
		mnuLastGPX = new javax.swing.JMenu();
		mnuLastGPX.setIcon(Utils.getIcon(this, "open_gpx.png", Settings.MenuIconSize));

		// -- Mru GPX n°1
		mnuMruGPX1 = new javax.swing.JMenuItem();
		mnuMruGPX1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuMruGPXActionPerformed(evt);
			}
		});
		mnuLastGPX.add(mnuMruGPX1);

		// -- Mru GPX n°2
		mnuMruGPX2 = new javax.swing.JMenuItem();
		mnuMruGPX2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuMruGPXActionPerformed(evt);
			}
		});
		mnuLastGPX.add(mnuMruGPX2);

		// -- Mru GPX n°3
		mnuMruGPX3 = new javax.swing.JMenuItem();
		mnuMruGPX3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuMruGPXActionPerformed(evt);
			}
		});
		mnuLastGPX.add(mnuMruGPX3);

		// -- Mru GPX n°4
		mnuMruGPX4 = new javax.swing.JMenuItem();
		mnuMruGPX4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuMruGPXActionPerformed(evt);
			}
		});
		mnuLastGPX.add(mnuMruGPX4);

		// -- Mru GPX n°5
		mnuMruGPX5 = new javax.swing.JMenuItem();
		mnuMruGPX5.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuMruGPXActionPerformed(evt);
			}
		});
		mnuLastGPX.add(mnuMruGPX5);

		mnuFile.add(mnuLastGPX);

		//##########################
		
		// -- Separator
		mnuFile.add(new javax.swing.JPopupMenu.Separator());
		
		//##########################

		// -- Sous-menu "Importer"
		mnuImport = new javax.swing.JMenu();
		mnuImport.setIcon(Utils.getIcon(this, "import.png", Settings.MenuIconSize));
		
		// -- Import GPX
		mnuImportGPX = new javax.swing.JMenuItem();
		mnuImportGPX.setIcon(Utils.getIcon(this, "import.png", Settings.MenuIconSize));
		mnuImportGPX.setEnabled(false);
		mnuImportGPX.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ImportGPX();
			}
		});
		mnuImport.add(mnuImportGPX);

		// -- Import CGX
		mnuImportCGX = new javax.swing.JMenuItem();
		mnuImportCGX.setIcon(Utils.getIcon(this, "import.png", Settings.MenuIconSize));
		mnuImportCGX.setEnabled(false);
		mnuImportCGX.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ImportCGX();
			}
		});
		mnuImport.add(mnuImportCGX);

		mnuFile.add(mnuImport);
		
		//##########################
		
		// -- Separator
		mnuFile.add(new javax.swing.JPopupMenu.Separator());
		
		//##########################

		// -- Sous-menu "Exporter"
		mnuExport = new javax.swing.JMenu();	
		mnuExport.setIcon(Utils.getIcon(this, "export.png", Settings.MenuIconSize));
		
		// -- Save CSV
		mnuSaveCSV = new javax.swing.JMenuItem();
		mnuSaveCSV.setIcon(Utils.getIcon(this, "save_csv.png", Settings.MenuIconSize));
		mnuSaveCSV.setEnabled(false);
		mnuSaveCSV.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SaveCSV();
			}
		});
		mnuExport.add(mnuSaveCSV);

		// -- Save GPX
		mnuSaveGPX = new javax.swing.JMenuItem();
		mnuSaveGPX.setIcon(Utils.getIcon(this, "save_gpx.png", Settings.MenuIconSize));
		mnuSaveGPX.setEnabled(false);
		mnuSaveGPX.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SaveAsGPX();
			}
		});
		mnuExport.add(mnuSaveGPX);
		
		// -- Save a part of the track in CGX
		mnuSavePartCGX = new javax.swing.JMenuItem();
		mnuSavePartCGX.setIcon(Utils.getIcon(this, "save_cgx.png", Settings.MenuIconSize));
		mnuSavePartCGX.setEnabled(false);
		mnuSavePartCGX.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SavePartCGX();
			}
		});
		mnuExport.add(mnuSavePartCGX);

		// -- Save a part of the track in GPX
		mnuSavePartGPX = new javax.swing.JMenuItem();
		mnuSavePartGPX.setIcon(Utils.getIcon(this, "save_gpx.png", Settings.MenuIconSize));
		mnuSavePartGPX.setEnabled(false);
		mnuSavePartGPX.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SavePartGPX();
			}
		});
		mnuExport.add(mnuSavePartGPX);

		// -- Save a part of the track in CSV
		mnuSavePartCSV = new javax.swing.JMenuItem();
		mnuSavePartCSV.setIcon(Utils.getIcon(this, "save_csv.png", Settings.MenuIconSize));
		mnuSavePartCSV.setEnabled(false);
		mnuSavePartCSV.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SavePartCSV();
			}
		});
		mnuExport.add(mnuSavePartCSV);
		
		mnuFile.add(mnuExport);
		//##########################
		
		// -- Separator
		mnuFile.add(new javax.swing.JPopupMenu.Separator());

		//##########################
		
		// -- Set marker
		mnuTags = new javax.swing.JMenu();
		mnuTags.setIcon(Utils.getIcon(this, "flag.png", Settings.MenuIconSize));
		
		// -- Import points
		mnuImportPoints = new javax.swing.JMenuItem();
		mnuImportPoints.setIcon(Utils.getIcon(this, "import.png", Settings.MenuIconSize));
		mnuImportPoints.setEnabled(false);
		mnuImportPoints.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				BackupInCGX();
				ImportPoints();
			}
		});
		mnuTags.add(mnuImportPoints);

		// -- Export points
		mnuExportPoints = new javax.swing.JMenuItem();
		mnuExportPoints.setIcon(Utils.getIcon(this, "export.png", Settings.MenuIconSize));
		mnuExportPoints.setEnabled(false);
		mnuExportPoints.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ExportPoints();
			}
		});
		mnuTags.add(mnuExportPoints);

		// -- Separator
		mnuTags.add(new javax.swing.JPopupMenu.Separator());

		// -- Export tags as waypoints
		mnuExportTagAsWaypoints = new javax.swing.JMenuItem();
		mnuExportTagAsWaypoints.setIcon(Utils.getIcon(this, "export.png", Settings.MenuIconSize));
		mnuExportTagAsWaypoints.setEnabled(false);
		mnuExportTagAsWaypoints.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ExportTagsAsWaypoints();
			}
		});
		mnuTags.add(mnuExportTagAsWaypoints);

		mnuFile.add(mnuTags);
		
		//##########################
		
		// -- Separator
		mnuFile.add(new javax.swing.JPopupMenu.Separator());

		// -- Quit
		mnuQuit = new javax.swing.JMenuItem();
		mnuQuit.setAccelerator(
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
		mnuQuit.setIcon(Utils.getIcon(this, "quit.png", Settings.MenuIconSize));
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
		//mnuEdit.setText(bundle.getString("frmMain.mnuEdit.text"));

		// -- Copy
		mnuCopy = new javax.swing.JMenuItem();
		// mnuCopy.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C,
		// java.awt.event.InputEvent.CTRL_MASK));
		mnuCopy.setIcon(Utils.getIcon(this, "copy.png", Settings.MenuIconSize));
		mnuCopy.setEnabled(false);
		mnuCopy.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				panelTrackData.Copy2Clipboard();
			}
		});
		mnuEdit.add(mnuCopy);

		// -- Separator
		mnuEdit.add(new javax.swing.JPopupMenu.Separator());

		// -- Search a point...
		mnuSearchPoint = new javax.swing.JMenuItem();
		mnuSearchPoint.setAccelerator(
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
		mnuSearchPoint.setIcon(Utils.getIcon(this, "search.png", Settings.MenuIconSize));
		mnuSearchPoint.setEnabled(false);
		mnuSearchPoint.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SearchPointDialog();
			}
		});
		mnuEdit.add(mnuSearchPoint);

		// -- Search a distance...
		mnuSearchDistance = new javax.swing.JMenuItem();
		mnuSearchDistance.setAccelerator(
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
		mnuSearchDistance.setIcon(Utils.getIcon(this, "search.png", Settings.MenuIconSize));
		mnuSearchDistance.setEnabled(false);
		mnuSearchDistance.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SearchDistanceDialog();
			}
		});
		mnuEdit.add(mnuSearchDistance);
		
		// -- Separator
		mnuEdit.add(new javax.swing.JPopupMenu.Separator());

		// -- Read only mode
		mnuReadOnly = new javax.swing.JCheckBoxMenuItem();
		mnuReadOnly.setEnabled(false);
		mnuReadOnly.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ReadOnly();
			}
		});
		mnuEdit.add(mnuReadOnly);

		// -- Separator
		mnuEdit.add(new javax.swing.JPopupMenu.Separator());

		// -- Mark the current position
		mnuMarkPosition = new javax.swing.JMenuItem();
		mnuMarkPosition.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
		mnuMarkPosition.setIcon(Utils.getIcon(this, "flag.png", Settings.MenuIconSize));
		mnuMarkPosition.setEnabled(false);
		mnuMarkPosition.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SetMapMarker();
			}
		});
		mnuEdit.add(mnuMarkPosition);

		// -- Go to the next mark
		mnuGotoNextMark = new javax.swing.JMenuItem();
		mnuGotoNextMark.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F7, 0));
		mnuGotoNextMark.setIcon(Utils.getIcon(this, "next.png", Settings.MenuIconSize));
		mnuGotoNextMark.setEnabled(false);
		mnuGotoNextMark.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				GoToNextMark();
			}
		});
		mnuEdit.add(mnuGotoNextMark);

		// -- Go to the previous mark
		mnuGotoPrevMark = new javax.swing.JMenuItem();
		mnuGotoPrevMark.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F7,
				java.awt.event.InputEvent.SHIFT_MASK));
		mnuGotoPrevMark.setIcon(Utils.getIcon(this, "prev.png", Settings.MenuIconSize));
		mnuGotoPrevMark.setEnabled(false);
		mnuGotoPrevMark.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				GoToPreviousMark();
			}
		});
		mnuEdit.add(mnuGotoPrevMark);

		// --
		mnuMain.add(mnuEdit);

		// == Display
		// ===========================================================
		mnuDisplay = new javax.swing.JMenu();
		//mnuDisplay.setText(bundle.getString("frmMain.mnuDisplay.text"));

		// -- HTML report
		// -------------------------------------------------------
		mnuHTMLReport = new javax.swing.JMenuItem();
		mnuHTMLReport.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
		mnuHTMLReport.setIcon(Utils.getIcon(this, "html.png", Settings.MenuIconSize));
		mnuHTMLReport.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
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
		mnuGenerateKML.setIcon(Utils.getIcon(this, "world.png", Settings.MenuIconSize));
		mnuGenerateKML.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
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
		mnuGenerateRoadbook.setIcon(Utils.getIcon(this, "roadbook.png", Settings.MenuIconSize));
		mnuGenerateRoadbook.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// TODO
			}
		});
		mnuGenerateRoadbook.setEnabled(false);
		mnuGenerateRoadbook.setVisible(false);
		mnuDisplay.add(mnuGenerateRoadbook);

		// -- Mini roadbook
		// -----------------------------------------------------
		mnuGenerateMiniRoadbook = new javax.swing.JMenuItem();
		mnuGenerateMiniRoadbook.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
		mnuGenerateMiniRoadbook.setIcon(Utils.getIcon(this, "roadbook.png", Settings.MenuIconSize));
		mnuGenerateMiniRoadbook.setEnabled(false);
		mnuGenerateMiniRoadbook.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ShowMRB();
			}
		});
		mnuDisplay.add(mnuGenerateMiniRoadbook);

		// -- Separator
		// ---------------------------------------------------------
		// mnuDisplay.add(new javax.swing.JPopupMenu.Separator());

		// -- Display the speed in the data grid
		// --------------------------------
		// TODO : Probably to remove in the near futur
		mnuDisplaySpeed = new javax.swing.JMenuItem();
		mnuDisplaySpeed.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// mnuSaveCGXActionPerformed(evt); //TODO
			}
		});
		mnuDisplaySpeed.setEnabled(false);
		mnuDisplaySpeed.setVisible(false);
		mnuDisplay.add(mnuDisplaySpeed);

		// -- Display the slope in the data grid
		// --------------------------------
		// TODO: Probably to remove in the near futur
		mnuDisplaySlope = new javax.swing.JMenuItem();
		mnuDisplaySlope.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
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
		//mnuTools.setText(bundle.getString("frmMain.mnuTools.text"));

		// -- Find Min / Max
		// ----------------------------------------------------
		mnuFindMinMax = new javax.swing.JMenuItem();
		mnuFindMinMax.setIcon(Utils.getIcon(this, "minmax.png", Settings.MenuIconSize));
		mnuFindMinMax.setEnabled(false);
		mnuFindMinMax.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				BackupInCGX();
				Track.CalcMinMaxElevation();
			}
		});
		mnuTools.add(mnuFindMinMax);

		// -- Invert track
		// ------------------------------------------------------
		mnuInvertTrack = new javax.swing.JMenuItem();
		mnuInvertTrack.setIcon(Utils.getIcon(this, "inverse.png", Settings.MenuIconSize));
		mnuInvertTrack.setEnabled(false);
		mnuInvertTrack.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (Track.data.size() > 0) {
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
		mnuDefineNewStart.setIcon(Utils.getIcon(this, "flag_new.png", Settings.MenuIconSize));
		mnuDefineNewStart.setEnabled(false);
		mnuDefineNewStart.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				NewStartPoint();
			}
		});
		mnuTools.add(mnuDefineNewStart);
		
		// -- Smooth the elevation values
		// ------------------------------------------------
		mnuSmoothElevation = new javax.swing.JMenuItem();
		mnuSmoothElevation.setIcon(Utils.getIcon(this, "elev_smoothing.png", Settings.MenuIconSize));
		mnuSmoothElevation.setEnabled(false);
		mnuSmoothElevation.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SmoothElevation();
			}
		});
		mnuTools.add(mnuSmoothElevation);
				

		// -- Calculate the track time
		// -------------------------------------------
		mnuCalculateTrackTime = new javax.swing.JMenuItem();
		mnuCalculateTrackTime.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
		mnuCalculateTrackTime.setIcon(Utils.getIcon(this, "refresh.png", Settings.MenuIconSize));
		mnuCalculateTrackTime.setEnabled(false);
		mnuCalculateTrackTime.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				CalcTrackTime();
			}
		});
		mnuTools.add(mnuCalculateTrackTime);

		// -- Search curve from final time
		// ------------------------------------------------
		mnuSearchCurveFromFinalTime = new javax.swing.JMenuItem();
		mnuSearchCurveFromFinalTime.setEnabled(false);
		mnuSearchCurveFromFinalTime.setIcon(Utils.getIcon(this, "search.png", Settings.MenuIconSize));
		mnuSearchCurveFromFinalTime.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SearchCurveFromFinalTime();
			}
		});
		mnuTools.add(mnuSearchCurveFromFinalTime);
		
		// -- Separator
		// ---------------------------------------------------------
		mnuTools.add(new javax.swing.JPopupMenu.Separator());

		// -- Internet tools
		// ----------------------------------------------------
		mnuInternetTools = new javax.swing.JMenuItem();
		mnuInternetTools.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// mnuSaveCGXActionPerformed(evt); //TODO
			}
		});
		mnuInternetTools.setEnabled(false);
		mnuInternetTools.setVisible(false);
		mnuTools.add(mnuInternetTools);

		// -- Force curves copy from resources
		// ------------
		/*
		mnuDisplayCopyCurves = new javax.swing.JMenuItem();
		mnuDisplayCopyCurves.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ExportCurvesFromResource(true);
			}
		});
		mnuTools.add(mnuDisplayCopyCurves);

		// -- Separator
		// ---------------------------------------------------------
		mnuTools.add(new javax.swing.JPopupMenu.Separator());
		*/
		
		// -- Display the directory containing the speed/slope files
		// ------------
		mnuDisplaySSDir = new javax.swing.JMenuItem();
		mnuDisplaySSDir.setIcon(Utils.getIcon(this, "open.png", Settings.MenuIconSize));
		mnuDisplaySSDir.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					Desktop.getDesktop().open(new File(DataDir + "/" + CgConst.CG_DIR + "/curves"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		mnuTools.add(mnuDisplaySSDir);

		// -- Separator
		// ---------------------------------------------------------
		mnuTools.add(new javax.swing.JPopupMenu.Separator());
				
		// -- Display the directory containing the logs
		// ------------
		mnuDisplayLogDir = new javax.swing.JMenuItem();
		mnuDisplayLogDir.setIcon(Utils.getIcon(this, "open.png", Settings.MenuIconSize));
		mnuDisplayLogDir.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				DisplayLogDir();
			}
		});
		mnuTools.add(mnuDisplayLogDir);
		
		
		// --
		mnuMain.add(mnuTools);

		// == Settings
		// ==========================================================
		mnuSettings = new javax.swing.JMenu();
		//mnuSettings.setText(bundle.getString("frmMain.mnuSettings.text"));

		// -- Track settings
		// ----------------------------------------------------
		mnuTrackSettings = new javax.swing.JMenuItem();
		mnuTrackSettings.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0));
		mnuTrackSettings.setIcon(Utils.getIcon(this, "setting.png", Settings.MenuIconSize));
		mnuTrackSettings.setEnabled(false);
		mnuTrackSettings.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				TrackSettings();
			}
		});
		mnuSettings.add(mnuTrackSettings);

		// -- Speed/Slope curves
		// ------------------------------------------------
		mnuSpeedSlopeCurves = new javax.swing.JMenuItem();
		mnuSpeedSlopeCurves.setIcon(Utils.getIcon(this, "chart_curve.png", Settings.MenuIconSize));
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
		mnuCGSettings.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9,
				java.awt.event.InputEvent.SHIFT_MASK));
		mnuCGSettings.setIcon(Utils.getIcon(this, "setting.png", Settings.MenuIconSize));
		mnuCGSettings.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				frmSettings frm = new frmSettings(Settings);
				frm.showDialog();

				if (Settings.Language.isEmpty()) {
					// -- System language
					CgLog.info("Configured language : System");
				} else {
					CgLog.info("Configured language : " + Settings.Language);
					Locale loc = Locale.US;
					if (Settings.Language.equalsIgnoreCase("FR")) {
						loc = Locale.FRANCE;
					} else if (Settings.Language.equalsIgnoreCase("EN")) {
						loc = Locale.US;
					} else if (Settings.Language.equalsIgnoreCase("ES")) {
						loc = new Locale("es", "ES");
					} else if (Settings.Language.equalsIgnoreCase("PT")) {
						loc = new Locale("pt", "PT");
					}
					//-- Change the language
					Locale.setDefault(loc);
					JComponent.setDefaultLocale(loc);
					JFileChooser.setDefaultLocale(loc);
					setVisible(true);
					bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
					SetText_MenuBarMain();
					RefreshMruGPX();
					RefreshMruCGX();
					SetText_Statusbar();
					SetText_MainToolbar();
					SetText_Main();
					panelStatistics.refresh();
					panelProfil.ChangeLang();
					jPanelTimeDist.ChangLang();
					jPanelSpeed.ChangLang();
					jPanelSpeedSlope.ChangeLang();
					PanelResume.ChangLang();
					panelTrackData.ChangeLang();
					panelMap.ChangLang();
				}
				
				
				// -- Refresh data and display
				SetDefaultFont();
				RefreshWindows(); // Refresh the main window (after a font change)
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
				panelMap.RefreshTrack(Track, false);
				PanelResume.RefreshTableResume();
			}
		});
		mnuSettings.add(mnuCGSettings);

		// --
		mnuMain.add(mnuSettings);

		// == Help
		// ==============================================================
		mnuHelp = new javax.swing.JMenu();
		//mnuHelp.setText(bundle.getString("frmMain.mnuHelp.text"));

		// -- Help
		// --------------------------------------------------------------
		mnuCGHelp = new javax.swing.JMenuItem();
		mnuCGHelp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
		mnuCGHelp.setIcon(Utils.getIcon(this, "help.png", Settings.MenuIconSize));
		mnuCGHelp.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (!Utils.OpenHelp(Utils.ProgDir, CurrentLanguage)) {
					CgLog.info("Failed to open help for the default language '" + CurrentLanguage);

					// By default, we should be able to open the French .
					if (!Utils.OpenHelp(Utils.ProgDir, "fr")) {
						CgLog.info("Failed to open help for language 'fr'.");
					}
				}
			}
		});
		mnuHelp.add(mnuCGHelp);

		// -- Release notes
		// --------------------------------------------------------------
		mnuReleaseNotes = new javax.swing.JMenuItem();
		mnuReleaseNotes.setIcon(Utils.getIcon(this, "edit.png", Settings.MenuIconSize));
		mnuReleaseNotes.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ShowReleaseNote();
			}
		});
		mnuHelp.add(mnuReleaseNotes);

		// -- F.A.Q.
		// --------------------------------------------------------------
		menuCGFaq = new javax.swing.JMenuItem();
		menuCGFaq.setIcon(Utils.getIcon(this, "faq.png", Settings.MenuIconSize));
		menuCGFaq.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					Desktop.getDesktop().browse(new URI(bundle.getString("frmMain.menuCGFaq.url")));
				} catch (IOException | URISyntaxException e) {
					e.printStackTrace();
				}
			}
		});
		mnuHelp.add(menuCGFaq);

		// -- Separator
		// ---------------------------------------------------------
		mnuHelp.add(new javax.swing.JPopupMenu.Separator());
		
		// -- Courses to download
		// --------------------------------------------------------------
		menuCGCoursesLibrary = new javax.swing.JMenuItem();
		menuCGCoursesLibrary.setIcon(Utils.getIcon(this, "help.png", Settings.MenuIconSize));
		menuCGCoursesLibrary.setVisible(false); //Hide the menu. Maybe in the future a new web page will be created 
		menuCGCoursesLibrary.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					Desktop.getDesktop().browse(new URI(bundle.getString("frmMain.menuCGCoursesLibrary.url")));
				} catch (IOException | URISyntaxException e) {
					e.printStackTrace();
				}
			}
		});
		mnuHelp.add(menuCGCoursesLibrary);

		// -- Check for update
		// -------------------------------------------------
		mnuCheckUpdate = new javax.swing.JMenuItem();
		mnuCheckUpdate.setIcon(Utils.getIcon(this, "update.png", Settings.MenuIconSize));
		mnuCheckUpdate.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Check4Update();
			}
		});
		mnuHelp.add(mnuCheckUpdate);

		// -- Separator
		// ---------------------------------------------------------
		mnuHelp.add(new javax.swing.JPopupMenu.Separator());

		// -- Reward the author
		// -------------------------------------------------
		mnuReward = new javax.swing.JMenuItem();
		mnuReward.setIcon(Utils.getIcon(this, "pouce.png", Settings.MenuIconSize));
		mnuReward.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					Desktop.getDesktop().browse(new URI("http://www.techandrun.com/dons/"));
				} catch (IOException | URISyntaxException e) {
					e.printStackTrace();
				}
			}
		});
		mnuHelp.add(mnuReward);

		// -- Course Generator web site
		// -------------------------------------------------
		mnuCGWebsite = new javax.swing.JMenuItem();
		mnuCGWebsite.setIcon(Utils.getIcon(this, "www.png", Settings.MenuIconSize));
		mnuCGWebsite.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					Desktop.getDesktop().browse(new URI("http://www.techandrun.com/"));
				} catch (IOException | URISyntaxException e) {
					e.printStackTrace();
				}
			}
		});
		mnuHelp.add(mnuCGWebsite);

		// -- Separator
		// ---------------------------------------------------------
		mnuHelp.add(new javax.swing.JPopupMenu.Separator());

		// -- About
		// -------------------------------------------------------------
		mnuAbout = new javax.swing.JMenuItem();
		mnuAbout.setIcon(Utils.getIcon(this, "about.png", Settings.MenuIconSize));
		mnuAbout.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuAbout();
			}
		});
		mnuHelp.add(mnuAbout);

		// --
		mnuMain.add(mnuHelp);

		//-- Set Text
		SetText_MenuBarMain();
		
		// -- Add the menu at the window
		setJMenuBar(mnuMain);

		// TODO check why it's necessary
		mnuMain.getAccessibleContext().setAccessibleParent(this);
	}


	/**
	 * Set the texts for the menu bar
	 * Separated in order to have "live" translation
	 */
	private void SetText_MenuBarMain() {

		// -- Menu File --------------------------------------------------------
		mnuFile.setText(bundle.getString("frmMain.mnuFile.text"));
		mnuImport.setText(bundle.getString("frmMain.mnuImport.text"));
		mnuExport.setText(bundle.getString("frmMain.mnuExport.text"));
		mnuTags.setText(bundle.getString("frmMain.mnuTags.text"));
		mnuOpenGPX.setText(bundle.getString("frmMain.mnuOpenGPX.text"));
		mnuLastGPX.setText(bundle.getString("frmMain.mnuLastGPX.text"));
		mnuMruGPX1.setText(bundle.getString("frmMain.mnuMruGPX1.text"));
		mnuMruGPX2.setText(bundle.getString("frmMain.mnuMruGPX2.text"));
		mnuMruGPX3.setText(bundle.getString("frmMain.mnuMruGPX3.text"));
		mnuMruGPX4.setText(bundle.getString("frmMain.mnuMruGPX4.text"));
		mnuMruGPX5.setText(bundle.getString("frmMain.mnuMruGPX5.text"));
		mnuOpenCGX.setText(bundle.getString("frmMain.mnuOpenCGX.text"));
		mnuLastCGX.setText(bundle.getString("frmMain.mnuLastCGX.text"));
		mnuMruCGX1.setText(bundle.getString("frmMain.mnuMruCGX1.text"));
		mnuMruCGX2.setText(bundle.getString("frmMain.mnuMruCGX2.text"));
		mnuMruCGX3.setText(bundle.getString("frmMain.mnuMruCGX3.text"));
		mnuMruCGX4.setText(bundle.getString("frmMain.mnuMruCGX4.text"));
		mnuMruCGX5.setText(bundle.getString("frmMain.mnuMruCGX5.text"));
		mnuSaveCGX.setText(bundle.getString("frmMain.mnuSaveCGX.text"));
		mnuSaveAsCGX.setText(bundle.getString("frmMain.mnuSaveAsCGX.text"));
		mnuSaveGPX.setText(bundle.getString("frmMain.mnuSaveGPX.text"));
		mnuSaveCSV.setText(bundle.getString("frmMain.mnuSaveCSV.text"));
		mnuImportGPX.setText(bundle.getString("frmMain.mnuImportGPX.text"));
		mnuImportCGX.setText(bundle.getString("frmMain.mnuImportCGX.text"));
		mnuSavePartCGX.setText(bundle.getString("frmMain.mnuSavePartCGX.text"));
		mnuSavePartGPX.setText(bundle.getString("frmMain.mnuSavePartGPX.text"));
		mnuSavePartCSV.setText(bundle.getString("frmMain.mnuSavePartCSV.text"));
		mnuImportPoints.setText(bundle.getString("frmMain.mnuImportPoints.text"));
		mnuExportPoints.setText(bundle.getString("frmMain.mnuExportPoints.text"));
		mnuExportTagAsWaypoints.setText(bundle.getString("frmMain.mnuExportTagAsWaypoints.text"));
		mnuQuit.setText(bundle.getString("frmMain.mnuQuit.text"));

		// -- Menu Edit --------------------------------------------------------
		mnuEdit.setText(bundle.getString("frmMain.mnuEdit.text"));

		// -- Copy
		mnuCopy.setText(bundle.getString("frmMain.mnuCopy.text"));
		mnuSearchPoint.setText(bundle.getString("frmMain.mnuSearchPoint.text"));
		mnuSearchDistance.setText(bundle.getString("frmMain.mnuSearchDistance.text"));
		mnuReadOnly.setText(bundle.getString("frmMain.mnuReadOnly.text"));
		mnuMarkPosition.setText(bundle.getString("frmMain.mnuMarkPosition.text"));
		mnuGotoNextMark.setText(bundle.getString("frmMain.mnuGotoNextMark.text"));
		mnuGotoPrevMark.setText(bundle.getString("frmMain.mnuGotoPrevMark.text"));
		
		// -- Menu Display ----------------------------------------------------
		mnuDisplay.setText(bundle.getString("frmMain.mnuDisplay.text"));

		mnuHTMLReport.setText(bundle.getString("frmMain.mnuHTMLReport.text"));
		mnuGenerateKML.setText(bundle.getString("frmMain.mnuGenerateKML.text"));
		mnuGenerateRoadbook.setText(bundle.getString("frmMain.mnuGenerateRoadbook.text"));
		mnuGenerateMiniRoadbook.setText(bundle.getString("frmMain.mnuGenerateMiniRoadbook.text"));
		mnuDisplaySpeed.setText(bundle.getString("frmMain.mnuDisplaySpeed.text"));
		mnuDisplaySlope.setText(bundle.getString("frmMain.mnuDisplaySlope.text"));

		// -- Menu Tools ------------------------------------------------------
		mnuTools.setText(bundle.getString("frmMain.mnuTools.text"));
		mnuFindMinMax.setText(bundle.getString("frmMain.mnuFindMinMax.text"));
		mnuInvertTrack.setText(bundle.getString("frmMain.mnuInvertTrack.text"));
		mnuDefineNewStart.setText(bundle.getString("frmMain.mnuDefineNewStart.text"));
		mnuSmoothElevation.setText(bundle.getString("frmMain.mnuSmoothElevation.text"));
		mnuCalculateTrackTime.setText(bundle.getString("frmMain.mnuCalculateTackTime.text"));
		mnuSearchCurveFromFinalTime.setText(bundle.getString("frmMain.mnuSearchCurveFromCurve.text"));
		mnuInternetTools.setText(bundle.getString("frmMain.mnuInternetTools.text"));
		//mnuDisplayCopyCurves.setText(bundle.getString("frmMain.mnuDisplayCopyCurves.text"));
		mnuDisplaySSDir.setText(bundle.getString("frmMain.mnuDisplaySSDir.text"));
		mnuDisplayLogDir.setText(bundle.getString("frmMain.mnuDisplayLogDir.text"));
		
		// -- Menu Settings ---------------------------------------------------
		mnuSettings.setText(bundle.getString("frmMain.mnuSettings.text"));
		mnuTrackSettings.setText(bundle.getString("frmMain.mnuTrackSettings.text"));
		mnuSpeedSlopeCurves.setText(bundle.getString("frmMain.mnuSpeedSlopeCurves.text"));
		mnuCGSettings.setText(bundle.getString("frmMain.mnuCGSettings.text"));

		//-- Menu Help --------------------------------------------------------
		mnuHelp.setText(bundle.getString("frmMain.mnuHelp.text"));
		mnuCGHelp.setText(bundle.getString("frmMain.mnuCGHelp.text"));
		mnuReleaseNotes.setText(bundle.getString("frmMain.mnuReleaseNotes.text"));
		menuCGFaq.setText(bundle.getString("frmMain.menuCGFaq.text"));
		menuCGCoursesLibrary.setText(bundle.getString("frmMain.menuCGCoursesLibrary.text"));
		mnuCheckUpdate.setText(bundle.getString("frmMain.mnuCheckUpdate.text"));
		mnuReward.setText(bundle.getString("frmMain.mnuReward.text"));
		mnuCGWebsite.setText(bundle.getString("frmMain.mnuCGWebsite.text"));
		mnuAbout.setText(bundle.getString("frmMain.mnuAbout.text"));
	}
	
	
	
	protected void ImportPoints() {
		if (Track == null)
			return;
		if (Track.data.isEmpty())
			return;

		String s = Utils.LoadDialog(this, Settings.getLastDirectory(), ".cgp", bundle.getString("frmMain.CGPFile"));
		if (!s.isEmpty()) {
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
			
		// -- Store the directory
		Settings.setLastDirectory( Utils.GetDirFromFilename(s));
		}
	}


	/**
	 * Open a dialog to export a selection of points
	 */
	private void ExportPoints() {
		if (Track == null || Track.data.isEmpty())
			return;

		frmExportPoints frm = new frmExportPoints(Settings);
		int mask = frm.showDialog();
		if (mask != -1) {
			//if current file is cgx we get the last dir from cgx
			String s = Utils.SaveDialog(this, Settings.getLastDirectory(), "", ".cgp", bundle.getString("frmMain.CGPFile"), true,
					bundle.getString("frmMain.FileExist"));

			if (!s.isEmpty()) {
				Track.ExportCGP(s, mask);
				
			// -- Store the directory
			Settings.setLastDirectory(Utils.GetDirFromFilename(s));
			}
		}
	}

	
	/**
	 * Open a dialog where you select the number separator
	 * @return 0=dot 1=comma
	 */
	private int SelectSeparator() {
		//-- Dot or comma for the numbers
		String[] options = {bundle.getString("frmMain.Dot"),bundle.getString("frmMain.Comma")}; //"Dot '.' ", "Comma ',' "
		
        return JOptionPane.showOptionDialog(null, bundle.getString("frmMain.SelectSeparator"), "", //"Select the decimal separator"
        		JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        
	}

	/**
	 * Save a part of the data in a CSV file
	 */
	private void SavePartCSV() {
		String s;

		if (Track.data.isEmpty())
			return;

		s = Utils.SaveDialog(this, Settings.previousCSVDirectory, "", ".csv", bundle.getString("frmMain.CSVFile"), true,
				bundle.getString("frmMain.FileExist"));

		if (!s.isEmpty()) {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			// -- Save track
			int start = panelTrackData.getSelectedRow();
			int end = start + panelTrackData.getSelectedRowCount() - 1;

			Track.SaveCSV(s, start, end, Settings.Unit, SelectSeparator());
			// -- Store the directory
			Settings.previousCSVDirectory = Utils.GetDirFromFilename(s);

			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}


	/**
	 * Save data in a CSV file
	 */
	private void SaveCSV() {
		String s;

		if (Track.data.isEmpty())
			return;

		s = Utils.SaveDialog(this, Settings.previousCSVDirectory, "", ".csv", bundle.getString("frmMain.CSVFile"), true,
				bundle.getString("frmMain.FileExist"));

		if (!s.isEmpty()) {
			// -- Save track
			Track.SaveCSV(s, 0, Track.data.size() - 1, Settings.Unit, SelectSeparator());
			// -- Store the directory
			Settings.previousCSVDirectory = Utils.GetDirFromFilename(s);
		}
	}


	/**
	 * Import a CGX file
	 */
	protected void ImportCGX() {
		if (Track.data.isEmpty())
			return;

		FrmImportChoice frm = new FrmImportChoice(Settings);

		int res = frm.showDialog();

		if (res != FrmImportChoice.RESULT_CANCEL) {
			String s = Utils.LoadDialog(this, Settings.previousCGXDirectory, ".cgx",
					bundle.getString("frmMain.CGXFile"));
			if (!s.isEmpty()) {
				int mode = FrmImportChoice.RESULT_AT_END;
				if (res == FrmImportChoice.RESULT_AT_END)
					mode = CgConst.IMPORT_MODE_ADD_END;
				else
					mode = CgConst.IMPORT_MODE_INS_START;

				BackupInCGX();
				// bAutorUpdatePos = false;
				try {
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
					panelWeather.refresh(Track, false);

					Settings.previousCGXDirectory = Utils.GetDirFromFilename(s);
					// bAutorUpdatePos = true;
				} catch (Exception e) {
					CgLog.error("ImportCGX : Impossible to import the CGX file");
					e.printStackTrace();
				}
			}
		}
	}


	private void mnuAbout() {
		showDialogAbout(this, false, true, Version);
	}


	private void BackupInCGX() {
		Backup_Track = Track.CopyTo(Backup_Track);
		bNoBackup = false;

		// Track.SaveCGX(DataDir + "/" + CgConst.CG_DIR + "/backup.cgx", 0,
		// Track.data.size() - 1, true);
		// bNoBackup = false;
	}


	private void RestoreInCGX() {
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
	protected void NewStartPoint() {
		if (Track.data.isEmpty())
			return;

		int start = panelTrackData.getSelectedRow();
		if (start < 0)
			return;

		// -- Confirmation dialog
		Object[] options = { " " + bundle.getString("frmMain.NewStartYes") + " ",
				" " + bundle.getString("frmMain.NewStartNo") + " " };
		int ret = JOptionPane.showOptionDialog(this, bundle.getString("frmMain.NewStartMessage"),
				bundle.getString("frmMain.NewStartTitle"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null,
				options, options[1]);

		// -- Ok! Let's go
		if (ret == JOptionPane.YES_OPTION) {
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
			panelMap.RefreshCurrentPosMarker(Track.data.get(0).getLatitude(), Track.data.get(0).getLongitude());
		}
	}


	/**
	 * Save the selected data to disk in GPX format
	 */
	private void SavePartGPX() {
		String s;

		if (Track.data.isEmpty())
			return;

		s = Utils.SaveDialog(this, Settings.previousGPXDirectory, "", ".gpx", bundle.getString("frmMain.GPXFile"), true,
				bundle.getString("frmMain.FileExist"));

		if (!s.isEmpty()) {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			// -- Save track
			int start = panelTrackData.getSelectedRow();
			int end = start + panelTrackData.getSelectedRowCount() - 1;

			Track.ExportGPX(s, start, end);
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
	private void SavePartCGX() {
		String s;

		if (Track.data.isEmpty())
			return;

		s = Utils.SaveDialog(this, Settings.previousCGXDirectory, "", ".cgx", bundle.getString("frmMain.CGXFile"), true,
				bundle.getString("frmMain.FileExist"));

		if (!s.isEmpty()) {
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
	 * Open the dialog to search the curve from an estimated final time
	 */
	private void SearchCurveFromFinalTime() {
		if (Track==null) 
			return;
		if (Track.data.isEmpty())
			return;
		
		frmSearchCurve frm = new frmSearchCurve(Settings);
		if (frm.showDialog(Settings, Track))
			CalcTrackTime();
		//RefreshStatusbar(Track);
	}
	
	
	/**
	 * Open the dialog to smooth the elevation data
	 */
	private void SmoothElevation() {
		if (Track==null) 
			return;
		if (Track.data.isEmpty())
			return;
		
		FrmElevationFilter frm = new FrmElevationFilter(Settings);
		if (frm.showDialog(Settings, Track)) {	
			Track.CalcMainData(false,true); //No hour calculation
			
			// -- Refresh
			Track.isModified = true;
			RefreshStatusbar(Track);
			
			panelTrackData.refresh();
			PanelResume.refresh();
			panelStatistics.refresh();
			panelProfil.RefreshProfilChart();
			jPanelSpeed.Refresh(Track, Settings);
			jPanelTimeDist.Refresh(Track, Settings);
			jPanelSpeedSlope.Refresh(Track, Settings);
		}
		//RefreshStatusbar(Track);
	}

	
	/**
	 * Affiche le répertoire des logs
	 */
	void DisplayLogDir() {
		try {
			JOptionPane.showMessageDialog(this, bundle.getString("frmMain.LogReadme"),
					"Course Generator", JOptionPane.INFORMATION_MESSAGE);

			Desktop.getDesktop().open(new File(DataDir + "/" + CgConst.CG_DIR + "/" +CgConst.CG_LOGS));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Separator for the status bar
	 * 
	 * @return Separator object
	 */
	static JComponent createStatusbarSeparator() {
		JSeparator x = new JSeparator(SwingConstants.VERTICAL);
		x.setPreferredSize(new Dimension(3, 20));
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

		// -- Separator
		StatusBar.add(createStatusbarSeparator());

		// -- Total distance of the track
		// ----------------------------------------------------
		LbInfoTotalDist = new javax.swing.JLabel();
		LbInfoTotalDist.setIcon(Utils.getIcon(this, "distance.png", Settings.StatusbarIconSize));
		StatusBar.add(LbInfoTotalDist);

		// -- Total distance - value
		// ----------------------------------------------------
		LbInfoTotalDistVal = new javax.swing.JLabel();
		StatusBar.add(LbInfoTotalDistVal);

		// -- Separator
		StatusBar.add(createStatusbarSeparator());

		// -- Ascent
		// ------------------------------------------------------
		LbInfoDp = new javax.swing.JLabel();
		LbInfoDp.setIcon(Utils.getIcon(this, "dp.png", Settings.StatusbarIconSize));
		StatusBar.add(LbInfoDp);

		// -- Ascent value
		// ------------------------------------------------------
		LbInfoDpVal = new javax.swing.JLabel();
		StatusBar.add(LbInfoDpVal);

		// -- Separator
		StatusBar.add(createStatusbarSeparator());

		// -- Descent
		// -----------------------------------------------------
		LbInfoDm = new javax.swing.JLabel();
		LbInfoDm.setIcon(Utils.getIcon(this, "dm.png", Settings.StatusbarIconSize));
		StatusBar.add(LbInfoDm);

		// -- Descent value
		// -----------------------------------------------------
		LbInfoDmVal = new javax.swing.JLabel();
		StatusBar.add(LbInfoDmVal);

		// -- Separator
		StatusBar.add(createStatusbarSeparator());

		// -- Total time
		// --------------------------------------------------------
		LbInfoTime = new javax.swing.JLabel();
		LbInfoTime.setIcon(Utils.getIcon(this, "chronometer.png", Settings.StatusbarIconSize));
		StatusBar.add(LbInfoTime);

		// -- Total time value
		// --------------------------------------------------------
		LbInfoTimeVal = new javax.swing.JLabel();
		StatusBar.add(LbInfoTimeVal);

		// -- Separator
		StatusBar.add(createStatusbarSeparator());

		// -- Curve
		// --------------------------------------------------------
		LbInfoCurve = new javax.swing.JLabel();
		LbInfoCurve.setIcon(Utils.getIcon(this, "chart_curve.png", Settings.StatusbarIconSize));
		LbInfoCurve.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				EditSSCurves();
			}
		});
		StatusBar.add(LbInfoCurve);

		// -- Curve value
		// --------------------------------------------------------
		LbInfoCurveVal = new javax.swing.JLabel();
		LbInfoCurveVal.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				EditSSCurves();
			}
		});
		StatusBar.add(LbInfoCurveVal);

		// -- Separator
		StatusBar.add(createStatusbarSeparator());

		// -- Time limit
		// --------------------------------------------------------
		LbTimeLimit = new javax.swing.JLabel(" " + bundle.getString("frmMain.LbTimeLimit.text") + " ");
		LbTimeLimit.setOpaque(true);
		LbTimeLimit.setBackground(Color.RED);
		LbTimeLimit.setForeground(Color.WHITE);
		LbTimeLimit.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
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
		LbModified.setIcon(Utils.getIcon(this, "edit.png", Settings.StatusbarIconSize));
		StatusBar.add(LbModified);

		// -- Modified status
		// --------------------------------------------------------
		LbModifiedVal = new javax.swing.JLabel();
		StatusBar.add(LbModifiedVal);

		// -- Separator
		StatusBar.add(createStatusbarSeparator());

		// -- Readonly indicator
		LbReadOnly = new javax.swing.JLabel(" " + bundle.getString("frmMain.LbReadOnly.text") + " ");
		LbReadOnly.setOpaque(true);
		LbReadOnly.setBackground(Color.lightGray);
		LbReadOnly.setForeground(Color.BLACK);
		LbReadOnly.setVisible(false);
		StatusBar.add(LbReadOnly);

		// -- Separator
		StatusBar.add(createStatusbarSeparator());

		// -- Calculation needed
		// ------------------------------------------------
		LbInfoCalculate = new javax.swing.JLabel();
		LbInfoCalculate.setIcon(Utils.getIcon(this, "calc.png", Settings.StatusbarIconSize));
		StatusBar.add(LbInfoCalculate);

		// -- Calculation needed value
		// ------------------------------------------------
		LbInfoCalculateVal = new javax.swing.JLabel();
		StatusBar.add(LbInfoCalculateVal);

		// -- Separator
		StatusBar.add(createStatusbarSeparator());

		// -- Internet connection present
		// ----------------------------------------
		LbInfoInternet = new javax.swing.JLabel();
		LbInfoInternet.setIcon(Utils.getIcon(this, "www.png", Settings.StatusbarIconSize));
		StatusBar.add(LbInfoInternet);

		// -- Internet connection present value
		// ----------------------------------------
		LbInfoInternetVal = new javax.swing.JLabel();
		StatusBar.add(LbInfoInternetVal);

		// -- Separator
		StatusBar.add(createStatusbarSeparator());

		// -- Unit
		// ----------------------------------------
		LbInfoUnit = new javax.swing.JLabel();
		LbInfoUnit.setIcon(Utils.getIcon(this, "unit.png", Settings.StatusbarIconSize));
		StatusBar.add(LbInfoUnit);

		// -- Unit value
		// ----------------------------------------
		LbInfoUnitVal = new javax.swing.JLabel();
		StatusBar.add(LbInfoUnitVal);

		// -- Separator
		StatusBar.add(createStatusbarSeparator());

		// -- Map dir size
		// ----------------------------------------
		LbInfoMapDirSize = new javax.swing.JLabel();
		LbInfoMapDirSize.setIcon(Utils.getIcon(this, "world.png", Settings.StatusbarIconSize));
		StatusBar.add(LbInfoMapDirSize);
		
		//-- Set text
		SetText_Statusbar();
	}


	private void SetText_Statusbar() {
		LbInfoTotalDist.setToolTipText(bundle.getString("frmMain.LbInfoTotalDist.toolTipText"));
		LbInfoTotalDistVal.setToolTipText(bundle.getString("frmMain.LbInfoTotalDist.toolTipText"));
		LbInfoDp.setToolTipText(bundle.getString("frmMain.LbInfoDp.toolTipText"));
		LbInfoDpVal.setToolTipText(bundle.getString("frmMain.LbInfoDp.toolTipText"));
		LbInfoDm.setToolTipText(bundle.getString("frmMain.LbInfoDm.toolTipText"));
		LbInfoDmVal.setToolTipText(bundle.getString("frmMain.LbInfoDm.toolTipText"));
		LbInfoTime.setToolTipText(bundle.getString("frmMain.LbInfoTime.toolTipText"));
		LbInfoTimeVal.setToolTipText(bundle.getString("frmMain.LbInfoTime.toolTipText"));
		LbInfoCurve.setToolTipText(bundle.getString("frmMain.LbInfoCurve.toolTipText"));
		LbInfoCurveVal.setToolTipText(bundle.getString("frmMain.LbInfoCurve.toolTipText"));
		LbTimeLimit.setToolTipText(bundle.getString("frmMain.LbTimeLimit.toolTipText"));
		LbModified.setToolTipText(bundle.getString("frmMain.LbModified.toolTipText"));
		LbModifiedVal.setToolTipText(bundle.getString("frmMain.LbModified.toolTipText"));
		LbInfoCalculate.setToolTipText(bundle.getString("frmMain.LbInfoCalculate.toolTipText"));
		LbInfoCalculateVal.setToolTipText(bundle.getString("frmMain.LbInfoCalculate.toolTipText"));
		LbInfoInternet.setToolTipText(bundle.getString("frmMain.LbInfoInternet.toolTipText"));
		LbInfoInternetVal.setToolTipText(bundle.getString("frmMain.LbInfoInternet.toolTipText"));
		LbInfoUnit.setToolTipText(bundle.getString("frmMain.LbInfoUnit.toolTipText"));
		LbInfoUnitVal.setToolTipText(bundle.getString("frmMain.LbInfoUnit.toolTipText"));
		LbInfoMapDirSize.setToolTipText(bundle.getString("frmMain.LbInfoMapDirSize.toolTipText"));
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
		btOpenGPX.setIcon(Utils.getIcon(this, "open_gpx.png", Settings.ToolbarIconSize));
		btOpenGPX.setFocusable(false);
		btOpenGPX.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				OpenGPXDialog();
			}
		});
		ToolBarMain.add(btOpenGPX);

		// -- Open CGX
		// ----------------------------------------------------------
		btOpenCGX = new javax.swing.JButton();
		btOpenCGX.setIcon(Utils.getIcon(this, "open_cgx.png", Settings.ToolbarIconSize));
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
		btSaveCGX.setIcon(Utils.getIcon(this, "save_cgx.png", Settings.ToolbarIconSize));
		btSaveCGX.setFocusable(false);
		btSaveCGX.setEnabled(false);
		btSaveCGX.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SaveAsCGX();
			}
		});
		ToolBarMain.add(btSaveCGX);

		// -- Separator
		// ---------------------------------------------------------
		ToolBarMain.add(new javax.swing.JToolBar.Separator());

		// -- Undo
		// --------------------------------------------------------------
		btUndo = new javax.swing.JButton();
		btUndo.setIcon(Utils.getIcon(this, "undo.png", Settings.ToolbarIconSize));
		btUndo.setFocusable(false);
		btUndo.setEnabled(false);
		btUndo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
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
		btSearch.setIcon(Utils.getIcon(this, "search.png", Settings.ToolbarIconSize));
		btSearch.setFocusable(false);
		btSearch.setEnabled(false);
		btSearch.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SearchDistanceDialog();
			}
		});
		ToolBarMain.add(btSearch);

		// -- Previous mark
		// -----------------------------------------------------
		btGotoPreviousMark = new javax.swing.JButton();
		btGotoPreviousMark.setIcon(Utils.getIcon(this, "prev.png", Settings.ToolbarIconSize));
		btGotoPreviousMark.setFocusable(false);
		btGotoPreviousMark.setEnabled(false);
		btGotoPreviousMark.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				int p = GoToPreviousMark();
				if ((p >= 0) && (p < Track.data.size())) {
					panelMap.RefreshCurrentPosMarker(Track.data.get(p).getLatitude(), Track.data.get(p).getLongitude());
				}
			}
		});
		ToolBarMain.add(btGotoPreviousMark);

		// -- Next mark
		// ---------------------------------------------------------
		btGotoNextMark = new javax.swing.JButton();
		btGotoNextMark.setIcon(Utils.getIcon(this, "next.png", Settings.ToolbarIconSize));
		btGotoNextMark.setFocusable(false);
		btGotoNextMark.setEnabled(false);
		btGotoNextMark.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				int p = GoToNextMark();
				if ((p >= 0) && (p < Track.data.size())) {
					panelMap.RefreshCurrentPosMarker(Track.data.get(p).getLatitude(), Track.data.get(p).getLongitude());
				}
			}
		});
		ToolBarMain.add(btGotoNextMark);

		// -- Separator
		// ---------------------------------------------------------
		ToolBarMain.add(new javax.swing.JToolBar.Separator());

		// -- Mini roadbook
		// ------------------------------------------------
		btMiniRoadbook = new javax.swing.JButton();
		btMiniRoadbook.setIcon(Utils.getIcon(this, "roadbook.png", Settings.ToolbarIconSize));
		btMiniRoadbook.setFocusable(false);
		btMiniRoadbook.setEnabled(false);
		btMiniRoadbook.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ShowMRB();
			}
		});
		ToolBarMain.add(btMiniRoadbook);

		// -- Display S/S curves
		// ------------------------------------------------
		btDisplaySSCurves = new javax.swing.JButton();
		btDisplaySSCurves.setIcon(Utils.getIcon(this, "chart_curve.png", Settings.ToolbarIconSize));
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
		btTrackSettings.setIcon(Utils.getIcon(this, "setting.png", Settings.ToolbarIconSize));
		btTrackSettings.setFocusable(false);
		btTrackSettings.setEnabled(false);
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
		btFillDiff.setIcon(Utils.getIcon(this, "fill_diff.png", Settings.ToolbarIconSize));
		btFillDiff.setFocusable(false);
		btFillDiff.setEnabled(false);
		btFillDiff.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (Track.data.isEmpty())
					return;

				int start = panelTrackData.getSelectedRow();
				int end = start + panelTrackData.getSelectedRowCount();

				frmFillDiff frm = new frmFillDiff(Settings);
				EditDiffResult res = frm.showDialog(Settings, Track, start, end);
				if (res.Valid) {
					BackupInCGX();

					for (int i = res.Start; i <= res.End; i++) {
						Track.data.get(i).setDiff(res.Difficulty);
					}

					Track.isCalculated = false;
					Track.isModified = true;
					panelTrackData.refresh();
					panelProfil.RefreshProfilChart();
					jPanelTimeDist.Refresh(Track, Settings);
					jPanelSpeed.Refresh(Track, Settings);
					jPanelSpeedSlope.Refresh(Track, Settings);
					panelMap.RefreshTrack(Track, false);
					RefreshStatusbar(Track);
				}
			}
		});
		ToolBarMain.add(btFillDiff);

		// -- Fill coeff
		// ----------------------------------------------------
		btFillCoeff = new javax.swing.JButton();
		btFillCoeff.setIcon(Utils.getIcon(this, "fill_coeff.png", Settings.ToolbarIconSize));
		btFillCoeff.setFocusable(false);
		btFillCoeff.setEnabled(false);
		btFillCoeff.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (Track.data.size() <= 0)
					return;

				int start = panelTrackData.getSelectedRow();
				int end = start + panelTrackData.getSelectedRowCount();

				frmFillCoeff frm = new frmFillCoeff(Settings);
				EditCoeffResult res = frm.showDialog(Settings, Track, start, end);
				if (res.Valid) {
					BackupInCGX();

					if (res.Start == res.End) {
						Track.data.get(res.Start).setCoeff(res.Start_Coeff);
					} else {
						double x1 = Track.data.get(res.Start).getTotal(CgConst.UNIT_METER);// cd.data[frm.start].Total;
						double y1 = res.Start_Coeff; // frm.startval;

						double x2 = Track.data.get(res.End).getTotal(CgConst.UNIT_METER); // cd.data[frm.end].Total;
						double y2 = res.End_Coeff; // frm.endval;

						CalcLineResult rc = new CalcLineResult();
						rc = Utils.CalcLine(x1, y1, x2, y2, rc);

						// Line equation calc with "Y"=distance and "X"=Coeff
						double x = 0.0;
						double offset = 0.0;
						double coeff = 0;

						for (int i = res.Start; i <= res.End; i++) {
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
		btCalculateTrackTime.setIcon(Utils.getIcon(this, "refresh.png", Settings.ToolbarIconSize));
		btCalculateTrackTime.setFocusable(false);
		btCalculateTrackTime.setEnabled(false);
		btCalculateTrackTime.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				CalcTrackTime();
			}
		});
		ToolBarMain.add(btCalculateTrackTime);

		//-- Set texts
		SetText_MainToolbar();
	}

	private void SetText_MainToolbar() {
		btOpenGPX.setToolTipText(bundle.getString("frmMain.btOpenGPX.toolTipText"));
		btOpenCGX.setToolTipText(bundle.getString("frmMain.btOpenCGX.toolTipText"));
		btSaveCGX.setToolTipText(bundle.getString("frmMain.btSaveCGX.toolTipText"));
		btUndo.setToolTipText(bundle.getString("frmMain.btUndo.toolTipText"));
		btSearch.setToolTipText(bundle.getString("frmMain.btSearch.toolTipText"));
		btGotoPreviousMark.setToolTipText(bundle.getString("frmMain.btGotoPreviousMark.toolTipText"));
		btGotoNextMark.setToolTipText(bundle.getString("frmMain.btGotoNextMark.toolTipText"));
		btMiniRoadbook.setToolTipText(bundle.getString("frmMain.btMiniRoadbook.toolTipText"));
		btDisplaySSCurves.setToolTipText(bundle.getString("frmMain.btDisplaySSCurves.toolTipText"));
		btTrackSettings.setToolTipText(bundle.getString("frmMain.btTrackSettings.toolTipText"));
		btFillDiff.setToolTipText(bundle.getString("frmMain.btFillDiff.toolTipText"));
		btFillCoeff.setToolTipText(bundle.getString("frmMain.btFillCoeff.toolTipText"));
		btCalculateTrackTime.setToolTipText(bundle.getString("frmMain.btCalculateTrackTime.toolTipText"));
	}

	
	/**
	 * Display the mini roadbook
	 */
	protected void ShowMRB() {
		if (Track.data.isEmpty())
			return;

		FrmMiniroadbook frm = new FrmMiniroadbook(Settings);
		frm.showDialog(Track);
	}


	protected void TrackSettings() {
		if (Track.data.size() <= 0)
			return;

		frmTrackSettings frm = new frmTrackSettings(Settings);
		if (frm.showDialog(Track)) {
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
	private void EditSSCurves() {
		if (!Track.ReadOnly) {
			frmEditCurve frm = new frmEditCurve(Settings);
			frm.showDialog(Track);

			RefreshStatusbar(Track);
		}
	}


	private void JumpToTimelimitLine() {

		if (Track.TimeLimit_Line != -1) {
			// -- Select the line and scroll to it
			panelTrackData.setSelectedRow(Track.TimeLimit_Line);
			// -- Update the profil position
			panelProfil.setCrosshairPosition(Track.data.get(Track.TimeLimit_Line).getTotal(Settings.Unit) / 1000.0,
					Track.data.get(Track.TimeLimit_Line).getElevation(Settings.Unit));
			panelProfil.RefreshProfilInfo(Track.TimeLimit_Line);
		}
	}


	/**
	 * Add a tab to JTabbedPane. The icon is at the left of the text and there some
	 * space between the icon and the label
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
	private JLabel addTab(JTabbedPane tabbedPane, Component tab, String title, Icon icon) {
		tabbedPane.add(tab);
		
		// Create bespoke component for rendering the tab.
		javax.swing.JLabel lbl = new javax.swing.JLabel(title);
		if (icon != null)
			lbl.setIcon(icon);

		// Add some spacing between text and icon, and position text to the RHS.
		lbl.setIconTextGap(5);
		lbl.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

		tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, lbl);
		
		return lbl;
	}


	/**
	 * Refresh the main window Used after a font change
	 */
	private void RefreshWindows() {
		SwingUtilities.updateComponentTreeUI(this);
	}


	/**
	 * Set the default interface font
	 */
	private void SetDefaultFont() {
		if (Settings.DefaultFontName.isEmpty())
			Settings.DefaultFontName = "Arial";

		// setUIFont(new javax.swing.plaf.FontUIResource("Arial", Font.PLAIN, 14));
		setUIFont(new javax.swing.plaf.FontUIResource(Settings.DefaultFontName, Settings.DefaultFontStyle,
				Settings.DefaultFontSize));

		CgLog.info("Default font: " + javax.swing.UIManager.getDefaults().getFont("TabbedPane.font").toString());
	}


	/**
	 * This method is called to initialize the form.
	 */
	private void initComponents() {
		// -- Main windows
		// ------------------------------------------------------
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle(bundle.getString("frmMain.title"));
		setIconImages(null);

		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
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
		// -- Disable the default management of F6
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
		SplitPaneMainRight.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
		// -- Disable the default management of F6
		SplitPaneMainRight.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), "none");
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
					PanelResume.refresh();
			}
		};
		TabbedPaneMain.addChangeListener(changeListener);

		SplitPaneMainRight.setTopComponent(TabbedPaneMain);

		// -- Tab - data (grid)
		// -------------------------------------------------

		panelTrackData = new JPanelTrackData(ModelTableMain, Settings);
		panelTrackData.addListener(new JPanelTrackDataListener() {

			@Override
			public void doubleClickEvent() {
				BackupInCGX();
				frmEditPosition frm = new frmEditPosition(Settings);
				if (frm.showDialog(Settings, Track, panelTrackData.getSelectedRow())) {
					Track.isModified = true;
					panelTrackData.refresh();
					panelProfil.RefreshProfilChart();
					Track.CheckTimeLimit();
					panelMap.RefreshTrack(Track, false);
					RefreshStatusbar(Track);
				}
			}


			@Override
			public void simpleClickEvent() {
				if (Track.data.size() > 0) {
					int row = panelTrackData.getSelectedRow();
					// -- Refresh marker position on the map
					panelMap.RefreshCurrentPosMarker(Track.data.get(row).getLatitude(),
							Track.data.get(row).getLongitude());
					// -- Refresh profil crooshair position and profil info
					panelProfil.RefreshProfilInfo(row);
					panelProfil.setCrosshairPosition(Track.data.get(row).getTotal(Settings.Unit) / 1000.0,
							Track.data.get(row).getElevation(Settings.Unit));
				}
			}


			@Override
			public void keyRealeasedEvent() {
				if (Track.data.size() > 0) {
					int row = panelTrackData.getSelectedRow();
					panelMap.RefreshCurrentPosMarker(Track.data.get(row).getLatitude(),
							Track.data.get(row).getLongitude());
				}
			}
		});

		LbTabMain = addTab(TabbedPaneMain, panelTrackData, bundle.getString("frmMain.TabData.tabTitle"),
				Utils.getIcon(this, "gps.png", Settings.TabIconSize));

		// -- Tab - Profil
		// ------------------------------------------------------
		panelProfil = new JPanelProfil(Settings);
		panelProfil.addListener(new JPanelProfilListener() {
			@Override
			public void profilSelectionEvent() {
				int i = panelProfil.getIndex();
				// Refresh the position on the data grid
				panelTrackData.setSelectedRow(i);
				// Refresh the marker position on the map
				panelMap.RefreshCurrentPosMarker(Track.data.get(i).getLatitude(), Track.data.get(i).getLongitude());
			}
		});

		LbTabProfil = addTab(TabbedPaneMain, panelProfil, bundle.getString("frmMain.TabProfil.tabTitle"),
				Utils.getIcon(this, "profil.png", Settings.TabIconSize));

		// -- Tab - Statistic
		// ---------------------------------------------------
		panelStatistics = new JPanelStatistics(Settings);
		LbTabStatistics = addTab(TabbedPaneMain, panelStatistics, bundle.getString("frmMain.TabStatistic.tabTitle"),
				Utils.getIcon(this, "stat.png", Settings.TabIconSize));
		
		// -- Tab - Weather
				// ---------------------------------------------------
				panelWeather = new JPanelWeather(Settings, this);
				addTab(TabbedPaneMain, panelWeather, bundle.getString("frmMain.TabWeather.tabTitle"),
						Utils.getIcon(this, "weather.png", Settings.TabIconSize));

		// -- Tab - Analysis
		// ----------------------------------------------------
		jPanelAnalyze = new javax.swing.JPanel();
		jPanelAnalyze.setLayout(new java.awt.BorderLayout());
		LbTabAnalyze = addTab(TabbedPaneMain, jPanelAnalyze, bundle.getString("frmMain.TabAnalyze.tabTitle"),
				Utils.getIcon(this, "search.png", Settings.TabIconSize));

		// -- Create the tab bar
		TabbedPaneAnalysis = new javax.swing.JTabbedPane(JTabbedPane.LEFT);
		jPanelAnalyze.add(TabbedPaneAnalysis, java.awt.BorderLayout.CENTER);

		// -- Tab Analysis : Time/Distance
		jPanelTimeDist = new JPanelAnalysisTimeDist(Settings);
		LbTabTimeDist = addTab(TabbedPaneAnalysis, jPanelTimeDist, bundle.getString("frmMain.TabTimeDist.tabTitle"), null);

		// -- Tab Analysis : Speed
		jPanelSpeed = new JPanelAnalysisSpeed(Settings);
		LbTabSpeed = addTab(TabbedPaneAnalysis, jPanelSpeed, bundle.getString("frmMain.TabSpeed.tabTitle"), null);

		// -- Tab Analysis : Speed/slope
		jPanelSpeedSlope = new JPanelAnalysisSpeedSlope(Settings);
		LbTabSpeedSlope = addTab(TabbedPaneAnalysis, jPanelSpeedSlope, bundle.getString("frmMain.TabSpeedSlope.tabTitle"), null);

		// -- Tab - Summary
		// ------------------------------------------------------
		PanelResume = new JPanelResume(Resume, Settings);

		PanelResume.addListener(new JPanelResumeListener() {

			@Override
			public void doubleClickEvent() {
				BackupInCGX();
				frmEditPosition frm = new frmEditPosition(Settings);
				if (frm.showDialog(Settings, Track, PanelResume.getDataTrackLine())) {
					Track.isModified = true;
					panelTrackData.refresh();
					panelProfil.RefreshProfilChart();
					Track.CheckTimeLimit();
					panelMap.RefreshTrack(Track, false);
					RefreshStatusbar(Track);
				}
			}


			@Override
			public void lineChangeEvent() {
				SelectPositionFromResume(PanelResume.getSelectedLine());
			}
		});
		LbTabResume = addTab(TabbedPaneMain, PanelResume, bundle.getString("frmMain.TabResume.tabTitle"),
				Utils.getIcon(this, "grid.png", Settings.TabIconSize));

		// -- Map panel
		// ---------------------------------------------------------
		panelMap = new JPanelMaps(Settings);
		panelMap.addListener(new JPanelMapsListener() {
			@Override
			public void requestPositionIndexEvent() {
				panelMap.setRow(panelTrackData.getSelectedRow());
			}


			@Override
			public void changeEvent() {
				panelTrackData.refresh();
			}


			@Override
			public void mouseClicked(MouseEvent evt) {
				Coordinate c = panelMap.getSelectedPosition();

				int i = Track.FindNearestPoint(c.getLat(), c.getLon());
				// Selection the position on the data grid
				panelTrackData.setSelectedRow(i);

				// Refresh profil position
				panelProfil.RefreshProfilInfo(i);
				panelProfil.setCrosshairPosition(Track.data.get(i).getTotal(Settings.Unit) / 1000.0,
						Track.data.get(i).getElevation(Settings.Unit));
			}
		});

		SplitPaneMainRight.setRightComponent(panelMap);

		SetText_Main();
		
		// -- Finished - Pack
		// ---------------------------------------------------
		pack();
	}


	private void SetText_Main() {
		setTitle(bundle.getString("frmMain.title"));
		LbTabMain.setText(bundle.getString("frmMain.TabData.tabTitle"));
		LbTabProfil.setText(bundle.getString("frmMain.TabProfil.tabTitle"));
		LbTabStatistics.setText(bundle.getString("frmMain.TabStatistic.tabTitle"));
		LbTabAnalyze.setText(bundle.getString("frmMain.TabAnalyze.tabTitle"));
		LbTabTimeDist.setText(bundle.getString("frmMain.TabTimeDist.tabTitle"));
		LbTabSpeed.setText(bundle.getString("frmMain.TabSpeed.tabTitle"));
		LbTabSpeedSlope.setText(bundle.getString("frmMain.TabSpeedSlope.tabTitle"));
		LbTabResume.setText(bundle.getString("frmMain.TabResume.tabTitle"));
	}

	
	
	protected void SelectPositionFromResume(int selectedLine) {
		if (Resume.data.size() > 0) {
			int r = (int) (Resume.data.get(selectedLine).getLine()) - 1;
			// -- Set table main selection
			panelTrackData.setSelectedRow(r);
			// -- Refresh marker position on the map
			panelMap.RefreshCurrentPosMarker(Track.data.get(r).getLatitude(), Track.data.get(r).getLongitude());
			// -- Refresh profil crosshair position and profil info
			panelProfil.RefreshProfilInfo(r);
			panelProfil.setCrosshairPosition(Track.data.get(r).getTotal(Settings.Unit) / 1000.0,
					Track.data.get(r).getElevation(Settings.Unit));
		}
	}


	private void SetMapMarker() {
		if (Track.data.size() > 0) {
			int row = panelTrackData.getSelectedRow();
			if (row < 0)
				return;

			int tag = Track.data.get(row).getTag();

			if ((tag & CgConst.TAG_MARK) == 0)
				tag = tag | CgConst.TAG_MARK;
			else
				tag = tag & (~CgConst.TAG_MARK);

			Track.data.get(row).setTag(tag);

			// -- Set the flags
			Track.isCalculated = false; // Necessary?
			Track.isModified = true;

			// -- Refresh the table and map
			panelTrackData.refresh();
			panelMap.RefreshTrack(Track, false);
			// ShowMapMarker();
		}
	}


	/**
	 * Go to the next mark.
	 */
	private int GoToNextMark() {
		int indexNextMark = FindMark(panelTrackData.getSelectedRow(), "forward");

		// -- Select the line and scroll to it
		panelTrackData.setSelectedRow(indexNextMark);
		// -- Update the profil position
		panelProfil.setCrosshairPosition(Track.data.get(indexNextMark).getTotal(Settings.Unit) / 1000.0,
				Track.data.get(indexNextMark).getElevation(Settings.Unit));
		panelProfil.RefreshProfilInfo(indexNextMark);

		return indexNextMark;
	}


	/**
	 * Find the previous or next mark from the current position.
	 * 
	 * @param currentPosition
	 *            The index of the current position in the table.
	 * @param direction
	 *            The direction that should be used to find the next mark :
	 *            "forward", "backward"
	 * @return The position of the previous or next mark.
	 */
	private int FindMark(int currentPosition, String direction) {
		// We save the original given position as we need to avoid going into
		// an infinite loop.
		int originalPosition = currentPosition;

		currentPosition = direction == "forward" ? currentPosition + 1 : currentPosition - 1;

		int trackDataSize = Track.data.size();

		if (currentPosition >= trackDataSize)
			currentPosition = 0;

		if (currentPosition <= -1 && direction == "backward") {
			currentPosition = trackDataSize - 1;
		}
		else if (currentPosition <= -1) {
			currentPosition = 0;
		}

		CgData positionData = Track.data.get(currentPosition);

		// -- Next position contain a tag => Exit
		if (positionData.getTag() != 0)
			return currentPosition;

		// -- Search the next position
		while (currentPosition != originalPosition && positionData.getTag() == 0) {
			positionData = Track.data.get(currentPosition);

			if (positionData.getTag() != 0) {
				return currentPosition;
			}

			currentPosition = direction == "forward" ? currentPosition + 1 : currentPosition - 1;

			if (currentPosition >= trackDataSize) {
				currentPosition = 0;
			}
		}

		return -1;
	}


	/**
	 * Go to the previous mark.
	 */
	private int GoToPreviousMark() {
		int indexPreviousMark = FindMark(panelTrackData.getSelectedRow(), "backward");

		if (indexPreviousMark != -1) {
			// -- Select the line and scroll to it
			panelTrackData.setSelectedRow(indexPreviousMark);
			// -- Update the profil position
			panelProfil.setCrosshairPosition(Track.data.get(indexPreviousMark).getTotal(Settings.Unit) / 1000.0,
					Track.data.get(indexPreviousMark).getElevation(Settings.Unit));
			panelProfil.RefreshProfilInfo(indexPreviousMark);
		}

		return indexPreviousMark;
	}


	/**
	 * Display the search point dialog
	 */
	private void SearchPointDialog() {
		if (Track.data.size() <= 0)
			return;

		final frmSearchPoint frm = new frmSearchPoint(Settings);
		frm.addListener(new frmSearchPointListener() {

			@Override
			public void UpdateUIEvent() {
				// -- Refresh the position of the map marker
				SearchPointResult result = frm.getResult();
				panelMap.RefreshCurrentPosMarker(Track.data.get(result.Point).getLatitude(),
						Track.data.get(result.Point).getLongitude());

				panelTrackData.setSelectedRow(result.Point);

				// -- Refresh the profil cursor position
				panelProfil.RefreshProfilInfo(result.Point);
				panelProfil.setCrosshairPosition(Track.data.get(result.Point).getTotal(Settings.Unit) / 1000.0,
						Track.data.get(result.Point).getElevation(Settings.Unit));
			}
		});
		frm.showDialog(Settings, Track);
	}


	/**
	 * Display the search distance dialog
	 */
	private void SearchDistanceDialog() {
		if (Track.data.size() <= 0)
			return;

		final frmSearchDistance frm = new frmSearchDistance(Settings);
		double retDist=frm.showDialog(Settings);
		if (retDist!=-1) {
			int index=Track.SearchDistance(retDist);
			if (index!=-1)
			{
				panelMap.RefreshCurrentPosMarker(Track.data.get(index).getLatitude(), Track.data.get(index).getLongitude());
				panelTrackData.setSelectedRow(index);
				// -- Refresh the profil cursor position
				panelProfil.RefreshProfilInfo(index);
				panelProfil.setCrosshairPosition(Track.data.get(index).getTotal(Settings.Unit) / 1000.0,
						Track.data.get(index).getElevation(Settings.Unit));
			}
		}
	}
	
	

	public void ImportGPX() {
		if (Track.data.isEmpty())
			return;

		FrmImportChoice frm = new FrmImportChoice(Settings);

		int res = frm.showDialog();

		if (res != FrmImportChoice.RESULT_CANCEL) {
			String s = Utils.LoadDialog(this, Settings.previousGPXDirectory, ".gpx",
					bundle.getString("frmMain.GPXFile"));
			if (!s.isEmpty()) {
				int mode = FrmImportChoice.RESULT_AT_END;
				if (res == FrmImportChoice.RESULT_AT_END)
					mode = CgConst.IMPORT_MODE_ADD_END;
				else
					mode = CgConst.IMPORT_MODE_INS_START;

				BackupInCGX();
				// bAutorUpdatePos = false;
				try {
					if (Track.OpenGPX(s, mode, (double) Settings.PosFilterAskThreshold))
						JOptionPane.showMessageDialog(this, bundle.getString("frmMain.NoTimeData"));
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
					panelWeather.refresh(Track, false);
					Settings.previousGPXDirectory = Utils.GetDirFromFilename(s);
					// bAutorUpdatePos = true;
				} catch (Exception e) {
					CgLog.error("ImportGPX : Impossible to import the GPX file");
					e.printStackTrace();
				}
			}
		}
	}


	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("CreateImageIcon : Couldn't find file: " + path);
			return null;
		}
	}


	/**
	 * Display a dialog box to open a GPX file
	 */
	private void OpenGPXDialog() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(Settings.previousGPXDirectory)); // System.getProperty("user.home")));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		FileFilter gpxFilter = new FileTypeFilter(".gpx", bundle.getString("frmMain.GPXFile")); // "GPX
																								// file");
		fileChooser.addChoosableFileFilter(gpxFilter);
		fileChooser.setFileFilter(gpxFilter);

		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			LoadGPX(selectedFile.getAbsolutePath());
			Settings.previousGPXDirectory = Utils.GetDirFromFilename(selectedFile.getAbsolutePath());
		}
	}


	/**
	 * Load a GPX file
	 * 
	 * @param filename
	 *            file name
	 */
	private void LoadGPX(String filename) {
		//-- File Exist?
		if (!Utils.FileExist(filename)) {
			JOptionPane.showMessageDialog(this, bundle.getString("frmMain.FileError"),
					"Course Generator", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//-- File size = 0?
		File f = new File(filename);
	    long size = f.length();
	    if (size==0) {
		    JOptionPane.showMessageDialog(this, bundle.getString("frmMain.FileError"),
					"Course Generator", JOptionPane.ERROR_MESSAGE);
		    return;
	    }
	    
	    //-- Go!
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		try {
			Track.OpenGPX(filename, 0, (double) Settings.PosFilterAskThreshold);
			AddMruGPX(filename);

		// -- Update the viewer
		panelMap.setTrack(Track);
		// -- Refresh the track information
		RefreshStatusbar(Track);

		// -- Force the update of the main table
		panelTrackData.setSelectedRow(0);
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
		// Refresh map
		panelMap.RefreshTrack(Track, true);
		// Refresh weather
				panelWeather.refresh(Track, false);
		bNoBackup = true;

		if (Track.data.size() > 0)
			panelMap.RefreshCurrentPosMarker(Track.data.get(0).getLatitude(), Track.data.get(0).getLongitude());

			if (Track.data.size() > 0)
				panelMap.RefreshCurrentPosMarker(Track.data.get(0).getLatitude(), Track.data.get(0).getLongitude());			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, bundle.getString("frmMain.FileError"),
					"Course Generator", JOptionPane.ERROR_MESSAGE);			
		}
		
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}


	/**
	 * Save the track in GPX format with file name input
	 */
	private void SaveAsGPX() {
		String s;

		if (Track.data.isEmpty())
			return;

		s = Utils.SaveDialog(this, Settings.previousGPXDirectory, "", ".gpx", bundle.getString("frmMain.GPXFile"), true,
				bundle.getString("frmMain.FileExist"));

		if (!s.isEmpty()) {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			// -- Save track
			Track.ExportGPX(s, 0, Track.data.size() - 1);
			// -- Store the directory
			Settings.previousGPXDirectory = Utils.GetDirFromFilename(s);

			// -- Update GPX MRU
			AddMruGPX(s);
			RefreshMruGPX();

			// -- Reset the track modified flag
			Track.isModified = false;
			// -- Refresh info panel
			RefreshStatusbar(Track);
			RefreshTitle();

			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	
	/**
	 * Display a dialog box to open a CGX file
	 */
	private void OpenCGXDialog() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(Settings.previousCGXDirectory)); // System.getProperty("user.home")));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		FileFilter cgxFilter = new FileTypeFilter(".cgx", bundle.getString("frmMain.CGXFile")); // "CGX
																								// file");
		fileChooser.addChoosableFileFilter(cgxFilter);
		fileChooser.setFileFilter(cgxFilter);

		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			LoadCGX(selectedFile.getAbsolutePath());
			Settings.previousCGXDirectory = Utils.GetDirFromFilename(selectedFile.getAbsolutePath());
		}
	}


	/**
	 * Load a CGX file
	 * 
	 * @param filename
	 *            File name
	 */
	private void LoadCGX(String filename) {
		//-- File Exist?
		if (!Utils.FileExist(filename)) {
			JOptionPane.showMessageDialog(this, bundle.getString("frmMain.FileError"),
					"Course Generator", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//-- File size = 0?
		File f = new File(filename);
	    long size = f.length();
	    if (size==0) {
		    JOptionPane.showMessageDialog(this, bundle.getString("frmMain.FileError"),
					"Course Generator", JOptionPane.ERROR_MESSAGE);
		    return;
	    }
	    
	    //-- Go!
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		try {
			Track.OpenCGX(this, filename, CgConst.IMPORT_MODE_LOAD, false);
			AddMruCGX(filename);

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
		panelTrackData.setSelectedRow(0);
		panelTrackData.setTrack(Track);
		panelTrackData.setSelectedRow(0);

		// Refresh map
		panelMap.RefreshTrack(Track, true);
		// Refresh weather
				panelWeather.refresh(Track, false);
		RefreshMruCGX();
		bNoBackup = true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, bundle.getString("frmMain.FileError"),
					"Course Generator", JOptionPane.ERROR_MESSAGE);
		}

		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}


	/**
	 * Save the track in CGX format with file name input
	 */
	private void SaveAsCGX() {
		String s;

		if (Track.data.isEmpty())
			return;

		s = Utils.SaveDialog(this, Settings.previousCGXDirectory, "", ".cgx", bundle.getString("frmMain.CGXFile"), true,
				bundle.getString("frmMain.FileExist"));

		if (!s.isEmpty()) {
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
			Track.isNewTrack = false;
			
			// -- Refresh info panel
			RefreshStatusbar(Track);
			RefreshTitle();

			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}


	/**
	 * Save the track in CGX format
	 */
	private void SaveCGX() {
		String s;

		//-- New? 
		if (Track.isNewTrack) 
			SaveAsCGX();
		else {		
			if (Track.data.isEmpty())
				return;
	
			if (!Track.FullName.isEmpty()) {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	
				// -- Save track
				Track.SaveCGX(Track.FullName, 0, Track.data.size() - 1, false);
	
				// -- Reset the track modified flag
				Track.isModified = false;
				Track.isNewTrack = false;
				
				// -- Refresh info panel
				RefreshStatusbar(Track);
				RefreshTitle();
	
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}

	
	/**
	 * Export tags as waypoints
	 */
	private void ExportTagsAsWaypoints() {
		String s;

		if (Track.data.isEmpty())
			return;

		s = Utils.SaveDialog(this, Settings.getLastDirectory(), "", ".gpx", bundle.getString("frmMain.GPXFile"), true,
				bundle.getString("frmMain.FileExist"));

		if (!s.isEmpty()) {
			FrmExportWaypoints frm = new FrmExportWaypoints(Settings);
			if (frm.showDialog()) {
				Track.SaveWaypoint(s, frm.getTag());
			}
			
			// -- Store the directory
			Settings.setLastDirectory(Utils.GetDirFromFilename(s));
		}
	}


	/**
	 * Calculate the offline map directories size
	 */
	private void CalcOfflineMapsSize() {
		StrMapsDirSize = Utils.humanReadableByteCount(
				Utils.folderSize(new File(DataDir + "/" + CgConst.CG_DIR + "/TileCache")), true);
	}


	/**
	 * Load the configuration file
	 */
	private void LoadConfig() {
		Settings.Load(DataDir + "/" + CgConst.CG_DIR);
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
		panelTrackData.updateColumnWidth();

		Settings.Save(DataDir + "/" + CgConst.CG_DIR);
	}


	/**
	 * Called by the quit application menu
	 * 
	 * @param evt
	 */
	private void mnuQuitActionPerformed(java.awt.event.ActionEvent evt) {
		this.processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}


	/**
	 * Called by the Read only menu item
	 */
	private void ReadOnly() {
		if (Track == null)
			return;

		// -- Invert the status
		Track.ReadOnly = !Track.ReadOnly;

		// -- Refresh the menu item
		mnuReadOnly.setSelected(Track.ReadOnly);

		// -- Refresh the statusbar
		RefreshStatusbar(Track);
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
				CgLog.info("End of the application! Bye :(");
			} else {
				setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			}
		} else { // No modification! Bye
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			SaveConfig();
			CgLog.info("End of the application! Bye :(");
		}
	}


	/**
	 * Set form title
	 */
	private void RefreshTitle() {
		String s = "Course Generator " + Version;
		if (!Track.Name.isEmpty())
			s = s + " - " + Track.Name;
		setTitle(s);
	}


	private void mnuMruGPXActionPerformed(java.awt.event.ActionEvent evt) {
		JMenuItem m = (JMenuItem) evt.getSource();
		if (m == mnuMruGPX1)
			tryOpenRecentFile(Settings.mruGPX[0]);
		else if (m == mnuMruGPX2)
			tryOpenRecentFile(Settings.mruGPX[1]);
		else if (m == mnuMruGPX3)
			tryOpenRecentFile(Settings.mruGPX[2]);
		else if (m == mnuMruGPX4)
			tryOpenRecentFile(Settings.mruGPX[3]);
		else if (m == mnuMruGPX5)
			tryOpenRecentFile(Settings.mruGPX[4]);
	}


	private void mnuMruCGXActionPerformed(java.awt.event.ActionEvent evt) {
		JMenuItem m = (JMenuItem) evt.getSource();
		if (m == mnuMruCGX1)
			tryOpenRecentFile(Settings.mruCGX[0]);
		else if (m == mnuMruCGX2)
			tryOpenRecentFile(Settings.mruCGX[1]);
		else if (m == mnuMruCGX3)
			tryOpenRecentFile(Settings.mruCGX[2]);
		else if (m == mnuMruCGX4)
			tryOpenRecentFile(Settings.mruCGX[3]);
		else if (m == mnuMruCGX5)
			tryOpenRecentFile(Settings.mruCGX[4]);
	}


	/**
	 * Attempts to open a file (GPX or CGX).
	 * 
	 * @param filePath
	 *            The absolute file path.
	 */
	private void tryOpenRecentFile(String filePath) {
		File file = new File(filePath);
		String fileExtension = filePath.substring(filePath.length() - 3).toLowerCase();
		if (!file.exists()) {
			int dialogResult = JOptionPane.showConfirmDialog(this,
					filePath + " :\n" + bundle.getString("frmMain.QuestionRemoveRecentFile"),
					bundle.getString("frmMain.FileNotFound"), JOptionPane.YES_NO_OPTION);
			if (dialogResult == JOptionPane.YES_OPTION) {
				if (fileExtension.equals("cgx")) {
					for (int i = 0; i < Settings.mruCGX.length; ++i) {
						if (Settings.mruCGX[i].equals(filePath)) {
							Settings.mruCGX[i] = "";
							RefreshMruCGX();
							break;
						}
					}
				}
				if (fileExtension.equals("gpx")) {
					for (int i = 0; i < Settings.mruCGX.length; ++i) {
						if (Settings.mruGPX[i].equals(filePath)) {
							Settings.mruGPX[i] = "";
							RefreshMruGPX();
							break;
						}
					}
				}
			}
		} else {
			if (fileExtension.equals("cgx")) {
				LoadCGX(filePath);
			}
			if (fileExtension.equals("gpx")) {
				LoadGPX(filePath);
			}
		}
	}


	/**
	 * Refresh the main toolbar
	 */
	private void RefreshMainToolbar() {
		if (Track == null)
			return;

		boolean isLoaded = !Track.Name.isEmpty();

		btSaveCGX.setEnabled(isLoaded);
		btUndo.setEnabled(isLoaded);
		btSearch.setEnabled(isLoaded);
		btGotoPreviousMark.setEnabled(isLoaded);
		btGotoNextMark.setEnabled(isLoaded);
		btTrackSettings.setEnabled(isLoaded);
		btFillCoeff.setEnabled(isLoaded);
		btFillDiff.setEnabled(isLoaded);
		btCalculateTrackTime.setEnabled(isLoaded);
		btMiniRoadbook.setEnabled(isLoaded);
	}


	/**
	 * Refresh the main menu
	 */
	private void RefreshMainMenu() {
		if (Track == null)
			return;

		boolean isLoaded = !Track.Name.isEmpty();

		mnuSaveCGX.setEnabled(isLoaded);
		mnuSaveAsCGX.setEnabled(isLoaded);
		mnuSaveGPX.setEnabled(isLoaded);
		mnuSaveCSV.setEnabled(isLoaded);
		mnuSavePartCGX.setEnabled(isLoaded);
		mnuSavePartGPX.setEnabled(isLoaded);
		mnuSavePartCSV.setEnabled(isLoaded);
		mnuExportPoints.setEnabled(isLoaded);
		mnuExportTagAsWaypoints.setEnabled(isLoaded);
		mnuCopy.setEnabled(isLoaded);
		mnuSearchPoint.setEnabled(isLoaded);
		mnuSearchDistance.setEnabled(isLoaded);
		mnuReadOnly.setEnabled(isLoaded);
		mnuMarkPosition.setEnabled(isLoaded);
		mnuGotoNextMark.setEnabled(isLoaded);
		mnuGotoPrevMark.setEnabled(isLoaded);
		mnuGenerateMiniRoadbook.setEnabled(isLoaded);
		mnuFindMinMax.setEnabled(isLoaded);
		mnuInvertTrack.setEnabled(isLoaded);
		mnuDefineNewStart.setEnabled(isLoaded);
		mnuCalculateTrackTime.setEnabled(isLoaded);
		mnuSearchCurveFromFinalTime.setEnabled(isLoaded);
		mnuTrackSettings.setEnabled(isLoaded);
		mnuImportPoints.setEnabled(isLoaded);
		mnuImportCGX.setEnabled(isLoaded);
		mnuImportGPX.setEnabled(isLoaded);
		mnuSmoothElevation.setEnabled(isLoaded);
	}


	/**
	 * Refresh the statusbar
	 * 
	 * @param tdata
	 *            Track data object
	 */
	private void RefreshStatusbar(TrackData tdata) {

		// -- Distance
		LbInfoTotalDistVal.setText(String.format("%1.1f ", tdata.getTotalDistance(Settings.Unit) / 1000.0)
				+ Settings.getDistanceUnitString());

		// -- Ascent
		LbInfoDpVal
				.setText(String.format("%1.0f ", tdata.getClimbP(Settings.Unit)) + Settings.getElevationUnitString());

		// -- Descent
		LbInfoDmVal
				.setText(String.format("%1.0f ", tdata.getClimbM(Settings.Unit)) + Settings.getElevationUnitString());

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
		} else {
			LbModifiedVal.setText(bundle.getString("frmMain.LbModified_Ok.text"));
		}

		// -- Read only mode
		LbReadOnly.setVisible(Track.ReadOnly);

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

		// -- Map dir size
		LbInfoMapDirSize.setText(StrMapsDirSize);
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
	 * Display the release note dialog
	 */
	public void ShowReleaseNote() {
		frmReleaseNote frm = new frmReleaseNote(this, Settings, Version);
		frm.showDialog(Settings);
	}


	/**
	 * Add "filename" to the CGX mru menu
	 * 
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
	 * 
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


	/***
	 * Check if a new version exist
	 */
	public void Check4Update() {
		boolean new_version = false;
		String msg_version = "";
		String msg = "";
		String lang = Locale.getDefault().getLanguage();

		// Version = "4.0.0.BETA 8"
		try {
			String str;
			URL url = new URL("https://www.techandrun.com/cg_version.txt");
			Scanner s = new Scanner(url.openStream());
			while (s.hasNext()) {
				str = s.nextLine();
				String[] part = str.split("=");

				if (part[0].equalsIgnoreCase("version")) {
					if (isRemoteVersionNewer(part[1])) {
						msg_version = part[1];
						new_version = true;
					}
				}

				if (new_version) {
					if (lang.equalsIgnoreCase("en") && part[0].equalsIgnoreCase("text_en"))
						msg = part[1];
					else if (lang.equalsIgnoreCase("fr") && part[0].equalsIgnoreCase("text_fr"))
						msg = part[1];
				}
			}
			s.close();
		} catch (IOException ex) {
			CgLog.info("No internet connexion to check the update status");
		}

		if (new_version) {
			// -- Console message
			System.out.println("Version=" + msg_version);
			System.out.println("Text=" + msg);

			// -- GUI message
			JOptionPane.showMessageDialog(this,
					bundle.getString("frmMain.Check4Update.Version") + ": " + msg_version + "\n"
							+ bundle.getString("frmMain.Check4Update.MsgLabel") + ": " + msg,
					bundle.getString("frmMain.Check4Update.Title"), JOptionPane.INFORMATION_MESSAGE);

		}
	}


	/**
	 * Set the default font
	 * 
	 * @param f
	 *            font
	 */
	public static void setUIFont(javax.swing.plaf.FontUIResource f) {
		Enumeration<Object> keys = javax.swing.UIManager.getDefaults().keys();
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
		// -- Get the VM parameters
		String inEclipseStr = System.getProperty("runInEclipse");
		inEclipse = "true".equalsIgnoreCase(inEclipseStr);

		// -- Set the data dir
		String DataDir = Utils.GetHomeDir();

		// -- ensure log directory exists
		new File(new File(DataDir + "/" + CgConst.CG_DIR), "logs").mkdirs();

		// -- Initialize the log directory
		log = new CgLog(DataDir + "/" + CgConst.CG_DIR + "/logs/logs.txt", 10 * 1024 * 1024, !inEclipse); // 10Mb
																											// file

		// -- Set the look and feel
		try {
			// Set System L&F
			OsCheck.OSType ostype = OsCheck.getOperatingSystemType();
			switch (ostype) {
			case Windows:
				//Force L&F and not the system L&F otherwise the the application crash (Java problem)
				//javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); => Metal L&F works but it looks old!
				javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel"); //Windows look but also a bit old fashion
				break;
			case MacOS:
				javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
				break;
			case Linux:
				try {
					javax.swing.UIManager.setLookAndFeel("de.muntjak.tinylookandfeel.TinyLookAndFeel");
					// continuous layout on frame resize
					Toolkit.getDefaultToolkit().setDynamicLayout(true);
					// no flickering on resize
					System.setProperty("sun.awt.noerasebackground", "true");
					// Theme.loadTheme(de.muntjak.tinylookandfeel.TinyLookAndFeel.class.getResource("/themes/CG_Gray.theme"));
					// Theme.loadTheme(course_generator.class().getResource("/course_generator/CG_Gray.theme"));
					System.setProperty("awt.useSystemAAFontSettings", "on");
					System.setProperty("swing.aatext", "true");

				} catch (Exception ex) {
					ex.printStackTrace();
					javax.swing.UIManager.getSystemLookAndFeelClassName();
				}
				break;
			case Other:
				javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
				break;
			}
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}

		// -- Set things according to the OS
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
		default:
			// setUIFont(new javax.swing.plaf.FontUIResource("Arial",
			// Font.PLAIN, 12));
			break;
		}

		/* Create and display the form */
/*
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new frmMain(args).setVisible(true);
			}
		});
	*/	
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmMain window = new frmMain(args);
					window.setVisible(true);
                    //window.frame.setVisible(true);
                    //new frmMain(args).setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
			}
		});
		
		/*
		EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ChangeLocale window = new ChangeLocale();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        */
	}


	/**
	 * Determines if a more recent version is available.
	 * 
	 * @param remoteVersionNumber
	 *            The current version on the server.
	 */
	public static boolean isRemoteVersionNewer(String remoteVersionNumber) {
		StringTokenizer localVersion = new StringTokenizer(Version, ".");
		StringTokenizer remoteVersion = new StringTokenizer(remoteVersionNumber, ".");
		while (localVersion.hasMoreTokens() && remoteVersion.hasMoreTokens()) {
			int remoteVersionCurrentToken = Integer.parseInt(remoteVersion.nextToken());
			int localVersionCurrentToken = Integer.parseInt(localVersion.nextToken());
			if (remoteVersionCurrentToken > localVersionCurrentToken)
				return true;
			else if (remoteVersionCurrentToken < localVersionCurrentToken)
				return false;
		}
		return false;
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
	// -- Don't remove!!! May be one day I'll use it ;) (see comment block in
	// "initcomponent")
	// --
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
