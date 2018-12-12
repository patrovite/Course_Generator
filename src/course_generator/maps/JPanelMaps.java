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

package course_generator.maps;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.OsmFileCacheTileLoader;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.openstreetmap.gui.jmapviewer.tilesources.BingAerialTileSource;

import course_generator.CgData;
import course_generator.TrackData;
import course_generator.dialogs.FrmSelectMap;
import course_generator.settings.CgSettings;
import course_generator.tiles.openstreetmap.OpenStreetMap;
import course_generator.tiles.opentopomap.OpenTopoMap;
import course_generator.tiles.thunderforest.Outdoors;
import course_generator.utils.CgConst;
import course_generator.utils.CgLog;
import course_generator.utils.Utils;

public class JPanelMaps extends JPanel implements PropertyChangeListener {
	private static final long serialVersionUID = 8019423088047841193L;
	private TrackData Track = null;
	private String DataDir;
	private ResourceBundle bundle;
	private int IndexMarker = -1;
	private int Old_MarkerStart = -1;
	private int Old_MarkerEnd = -1;
	private int row = -1;
	private Coordinate selectedPosition = null;
	private CgSettings Settings;
	private MapMarker CurrentPosMarker = null;
	private MapMarker MapMarker = null;
	private MapMarker SummariesWeatherStationMarker = null;
	private MapMarker NormalsWeatherStationMarker = null;
	private ArrayList<Double> UndoDiff;
	private List<JPanelMapsListener> listeners = new ArrayList<JPanelMapsListener>();
	private JToolBar jToolBarMapViewer;
	private JButton btMapCenterOnTrack;
	private JButton btMapAddMarker;
	private JButton btMapHideMarker;
	private JButton btMapTrackVeryEasy;
	private JButton btMapTrackEasy;
	private JButton btMapTrackAverage;
	private AbstractButton btMapTrackHard;
	private AbstractButton btMapTrackVeryHard;
	private JButton btMapMark;
	private JButton btMapEat;
	private JButton btMapDrink;
	private JButton btMapSelect;
	private JButton btShowWeatherStation;
	private JPanel panelMain;
	private JMapViewer MapViewer;
	private OsmFileCacheTileLoader offlineTileCache;
	private JScrollPane jScrollPanelMap;
	private JButton btMapUndo;
	private boolean showWeatherStations;
	private JButton btShowHideMarkers;
	private boolean ShowMarkers;
	private JButton btSaveMap;
	private FrmSelectMap selectMap;


	public JPanelMaps(CgSettings settings) {
		super();
		Settings = settings;
		Settings.addPropertyChangeListener(this);
		ShowMarkers = true;
		DataDir = Utils.GetHomeDir();
		UndoDiff = new ArrayList<Double>();
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		initComponents();
	}


	public void addListener(JPanelMapsListener toAdd) {
		listeners.add(toAdd);
	}


	public void notifyRequestPosition() {
		for (JPanelMapsListener hl : listeners)
			hl.requestPositionIndexEvent();
	}


	public void notifyChange() {
		for (JPanelMapsListener hl : listeners)
			hl.changeEvent();
	}



//	public void notifyMouseClicked(MouseEvent evt) {
	public void notifyMouseClicked(java.awt.event.MouseEvent evt) {
		for (JPanelMapsListener hl : listeners)
			hl.mouseClicked(evt);
	}


//	public void propertyChange(PropertyChangeEvent evt) {
//		if (!evt.getPropertyName().equals("ThunderForestApiKeyChanged"))
//			return;

	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("ThunderForestApiKeyChanged")) {
			if (!Settings.isThunderForestApiKeyValid()) {
				if (Settings.map == 2) {
					Settings.map = 0;
					selectMap.rbOutdoors.setEnabled(false);
					RefreshMapType();
				}
			} else {
				selectMap.rbOutdoors.setEnabled(true);
			}
		}
		if (evt.getPropertyName().equals("HistoricalWeatherDataChanged")) {
			UpdateShowWeatherStationsButton();
		}
	}


	private void initComponents() {
		setLayout(new BorderLayout());

		panelMain = new JPanel();
		panelMain.setLayout(new BorderLayout());

		create_Toolbar();
		if (Settings.MapToolBarLayout == null || Settings.MapToolBarLayout.equals(""))
			panelMain.add(jToolBarMapViewer, BorderLayout.WEST);
		else {
			switch (Settings.MapToolBarLayout) {
			case "NORTH":
				panelMain.add(jToolBarMapViewer, BorderLayout.NORTH);
				break;
			case "EAST":
				panelMain.add(jToolBarMapViewer, BorderLayout.EAST);
				break;
			case "WEST":
				panelMain.add(jToolBarMapViewer, BorderLayout.WEST);
				break;
			case "SOUTH":
				panelMain.add(jToolBarMapViewer, BorderLayout.SOUTH);
				break;
			}
		}

		// MapViewer = new org.openstreetmap.gui.jmapviewer.JMapViewer();
		MapViewer = new JMapViewerCG();

		// -- Tile cache definition
		try {
			// File cacheDir = new File(DataDir + "/" + CgConst.CG_DIR,
			// "OpenStreetMapTileCache/"+CgConst.OPENSTREETMAP_CACHE_DIR);
			// cacheDir.mkdirs();

			File cacheDir = getTileCacheDir();
			offlineTileCache = new OsmFileCacheTileLoader(MapViewer, cacheDir);
			MapViewer.setTileLoader(offlineTileCache);
		} catch (IOException ex) {
			CgLog.error("Exception creating OsmFileCacheTileLoader");
			ex.printStackTrace();
		}

		MapViewer.setMapMarkerVisible(true);
		MapViewer.setScrollWrapEnabled(true);
		MapViewer.setZoomButtonStyle(org.openstreetmap.gui.jmapviewer.JMapViewer.ZOOM_BUTTON_STYLE.VERTICAL);
		MapViewer.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				MapViewerMouseClicked(evt);
			}
		});
		panelMain.add(MapViewer, BorderLayout.CENTER);

		jScrollPanelMap = new javax.swing.JScrollPane();
		jScrollPanelMap.setViewportView(panelMain);

		selectMap = new FrmSelectMap(Settings);

		add(panelMain, BorderLayout.CENTER);
	}


	/**
	 * Create the map toolbar
	 */
	private void create_Toolbar() {
		jToolBarMapViewer = new javax.swing.JToolBar();
		jToolBarMapViewer.setOrientation(Settings.MapToolBarOrientation);
		jToolBarMapViewer.setFloatable(true);
		jToolBarMapViewer.setRollover(true);
		jToolBarMapViewer.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub
			}


			@Override
			public void componentResized(ComponentEvent arg0) {
				LayoutManager layout = arg0.getComponent().getParent().getLayout();

				if (layout instanceof BorderLayout) {
					BorderLayout b = (BorderLayout) layout;
					Object con = b.getConstraints(arg0.getComponent());
					if (con == BorderLayout.SOUTH) {
						Settings.MapToolBarLayout = "SOUTH";
					} else if (con == BorderLayout.WEST) {
						Settings.MapToolBarLayout = "WEST";
					} else if (con == BorderLayout.EAST) {
						Settings.MapToolBarLayout = "EAST";
					} else if (con == BorderLayout.NORTH) {
						Settings.MapToolBarLayout = "NORTH";
					} else
						Settings.MapToolBarLayout = "WEST";
				}

				Settings.MapToolBarOrientation = jToolBarMapViewer.getOrientation();

			}


			@Override
			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub
			}


			@Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub
			}
		});

		// -- Center map on screen
		btMapCenterOnTrack = new javax.swing.JButton();
		btMapCenterOnTrack.setIcon(Utils.getIcon(this, "center.png", Settings.MapToolbarIconSize));
		btMapCenterOnTrack.setToolTipText(bundle.getString("frmMain.btMapCenterOnTrack.toolTipText"));
		btMapCenterOnTrack.setFocusable(false);
		btMapCenterOnTrack.setEnabled(false);
		btMapCenterOnTrack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				MapViewer.setDisplayToFitMapPolygons();
			}
		});
		jToolBarMapViewer.add(btMapCenterOnTrack);

		// -- Add marker
		btMapAddMarker = new javax.swing.JButton();
		btMapAddMarker.setIcon(Utils.getIcon(this, "marker.png", Settings.MapToolbarIconSize));
		btMapAddMarker.setToolTipText(bundle.getString("frmMain.btMapAddMarker.toolTipText"));
		btMapAddMarker.setFocusable(false);
		btMapAddMarker.setEnabled(false);
		btMapAddMarker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				ShowMapMarker();
			}
		});
		jToolBarMapViewer.add(btMapAddMarker);

		// -- Hide marker
		btMapHideMarker = new javax.swing.JButton();
		btMapHideMarker.setIcon(Utils.getIcon(this, "hide_marker.png", Settings.MapToolbarIconSize));
		btMapHideMarker.setToolTipText(bundle.getString("frmMain.btMapHideMarker.toolTipText"));
		btMapHideMarker.setFocusable(false);
		btMapHideMarker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				HideMapMarker();
			}
		});
		jToolBarMapViewer.add(btMapHideMarker);

		// -- Separator
		jToolBarMapViewer.add(new javax.swing.JToolBar.Separator());

		// -- Undo
		btMapUndo = new javax.swing.JButton();
		btMapUndo.setIcon(Utils.getIcon(this, "undo.png", Settings.MapToolbarIconSize));
		btMapUndo.setToolTipText(bundle.getString("frmMain.btMapUndo.toolTipText"));
		btMapUndo.setFocusable(false);
		btMapUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				UndoMapFillDiff();
			}
		});
		jToolBarMapViewer.add(btMapUndo);

		// -- Separator
		jToolBarMapViewer.add(new javax.swing.JToolBar.Separator());

		// -- Track very easy
		btMapTrackVeryEasy = new javax.swing.JButton();
		btMapTrackVeryEasy.setIcon(Utils.getIcon(this, "track_very_easy.png", Settings.MapToolbarIconSize));
		btMapTrackVeryEasy.setToolTipText(bundle.getString("frmMain.btMapTrackVeryEasy.toolTipText"));
		btMapTrackVeryEasy.setFocusable(false);
		btMapTrackVeryEasy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				MapTrackDifficulty(CgConst.DIFF_VERYEASY);
			}
		});
		jToolBarMapViewer.add(btMapTrackVeryEasy);

		// -- Track easy
		btMapTrackEasy = new javax.swing.JButton();
		btMapTrackEasy.setIcon(Utils.getIcon(this, "track_easy.png", Settings.MapToolbarIconSize));
		btMapTrackEasy.setToolTipText(bundle.getString("frmMain.btMapTrackEasy.toolTipText"));
		btMapTrackEasy.setFocusable(false);
		btMapTrackEasy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				MapTrackDifficulty(CgConst.DIFF_EASY);
			}
		});
		jToolBarMapViewer.add(btMapTrackEasy);

		// -- Track average
		btMapTrackAverage = new javax.swing.JButton();
		btMapTrackAverage.setIcon(Utils.getIcon(this, "track_average.png", Settings.MapToolbarIconSize));
		btMapTrackAverage.setToolTipText(bundle.getString("frmMain.btMapTrackAverage.toolTipText"));
		btMapTrackAverage.setFocusable(false);
		btMapTrackAverage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				MapTrackDifficulty(CgConst.DIFF_AVERAGE);
			}
		});
		jToolBarMapViewer.add(btMapTrackAverage);

		// -- Track hard
		btMapTrackHard = new javax.swing.JButton();
		btMapTrackHard.setIcon(Utils.getIcon(this, "track_hard.png", Settings.MapToolbarIconSize));
		btMapTrackHard.setToolTipText(bundle.getString("frmMain.btMapTrackHard.toolTipText"));
		btMapTrackHard.setFocusable(false);
		btMapTrackHard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				MapTrackDifficulty(CgConst.DIFF_HARD);
			}
		});
		jToolBarMapViewer.add(btMapTrackHard);

		// -- Track average
		btMapTrackVeryHard = new javax.swing.JButton();
		btMapTrackVeryHard.setIcon(Utils.getIcon(this, "track_very_hard.png", Settings.MapToolbarIconSize));
		btMapTrackVeryHard.setToolTipText(bundle.getString("frmMain.btMapTrackVeryHard.toolTipText"));
		btMapTrackVeryHard.setFocusable(false);
		btMapTrackVeryHard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				MapTrackDifficulty(CgConst.DIFF_VERYHARD);
			}
		});
		jToolBarMapViewer.add(btMapTrackVeryHard);

		// -- Separator
		jToolBarMapViewer.add(new javax.swing.JToolBar.Separator());

		// -- Mark
		btMapMark = new javax.swing.JButton();
		btMapMark.setIcon(Utils.getIcon(this, "flag.png", Settings.MapToolbarIconSize));
		btMapMark.setToolTipText(bundle.getString("frmMain.btMapMark.toolTipText"));
		btMapMark.setFocusable(false);
		btMapMark.setEnabled(false);
		btMapMark.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SetMarkMapMarker();
			}
		});
		jToolBarMapViewer.add(btMapMark);

		// -- Eat
		btMapEat = new javax.swing.JButton();
		btMapEat.setIcon(Utils.getIcon(this, "eat.png", Settings.MapToolbarIconSize));
		btMapEat.setToolTipText(bundle.getString("frmMain.btMapEat.toolTipText"));
		btMapEat.setFocusable(false);
		btMapEat.setEnabled(false);
		btMapEat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SetEatMapMarker();
			}
		});
		jToolBarMapViewer.add(btMapEat);

		// -- Drink
		btMapDrink = new javax.swing.JButton();
		btMapDrink.setIcon(Utils.getIcon(this, "drink.png", Settings.MapToolbarIconSize));
		btMapDrink.setToolTipText(bundle.getString("frmMain.btMapDrink.toolTipText"));
		btMapDrink.setFocusable(false);
		btMapDrink.setEnabled(false);
		btMapDrink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SetDrinkMapMarker();
			}
		});
		jToolBarMapViewer.add(btMapDrink);

		// -- Separator
		jToolBarMapViewer.add(new javax.swing.JToolBar.Separator());

		// -- Show/Hide markers
		btShowHideMarkers = new javax.swing.JButton();
		btShowHideMarkers.setIcon(Utils.getIcon(this, "show_hide_markers.png", Settings.MapToolbarIconSize));
		btShowHideMarkers.setToolTipText(bundle.getString("frmMain.btShowHideMarkers.toolTipText"));
		btShowHideMarkers.setFocusable(false);
		btShowHideMarkers.setEnabled(false);
		btShowHideMarkers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				ShowHideMarkers();
			}
		});
		jToolBarMapViewer.add(btShowHideMarkers);

		// -- Separator
		jToolBarMapViewer.add(new javax.swing.JToolBar.Separator());

		// -- Save map on disk
		btSaveMap = new javax.swing.JButton();
		btSaveMap.setIcon(Utils.getIcon(this, "save_png.png", Settings.MapToolbarIconSize));
		btSaveMap.setToolTipText(bundle.getString("frmMain.btSaveMap.toolTipText"));
		btSaveMap.setFocusable(false);
		btSaveMap.setEnabled(false);
		btSaveMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SaveMap();
			}
		});
		jToolBarMapViewer.add(btSaveMap);

		// -- Separator
		jToolBarMapViewer.add(new javax.swing.JToolBar.Separator());

		// -- Select the type of map
		btMapSelect = new javax.swing.JButton();
		btMapSelect.setIcon(Utils.getIcon(this, "select_map.png", Settings.MapToolbarIconSize));
		btMapSelect.setToolTipText(bundle.getString("frmMain.btMapSelect.toolTipText"));
		btMapSelect.setFocusable(false);
		btMapSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SelectMap();
			}
		});
		jToolBarMapViewer.add(btMapSelect);

		// -- Show the weather station
		btShowWeatherStation = new javax.swing.JButton();
		btShowWeatherStation.setIcon(Utils.getIcon(this, "weather_station.png", Settings.MapToolbarIconSize));
		btShowWeatherStation.setToolTipText(bundle.getString("frmMain.btMapShowWeatherStations.toolTipText"));
		btShowWeatherStation.setFocusable(false);
		btShowWeatherStation.setEnabled(false);
		btShowWeatherStation.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ShowHideWeatherStations();
			}
		});
		jToolBarMapViewer.add(btShowWeatherStation);

	}


	private void SaveMap() {
		String s = Utils.SaveDialog(this, Settings.previousPNGDirectory, "", ".png",
				bundle.getString("FrmMiniroadbook.PNGFile"), true, bundle.getString("frmMain.FileExist"));

		if (!s.isEmpty()) {
			MapViewer.saveToFile(s);
			Settings.previousPNGDirectory = Utils.GetDirFromFilename(s);
		}
	}


//	private void MapViewerMouseClicked(MouseEvent evt) {
	private void MapViewerMouseClicked(java.awt.event.MouseEvent evt) {
		if (Track == null)
			return;
		if (Track.data == null)
			return;
		if (Track.data.size() <= 0)
			return;

		selectedPosition = MapViewer.getPosition(evt.getX(), evt.getY());
		notifyMouseClicked(evt);

		// Find the nearest point
		Coordinate c = MapViewer.getPosition(evt.getX(), evt.getY());

		// Refresh position marker on the map
		int i = Track.FindNearestPoint(c.getLat(), c.getLon());
		RefreshCurrentPosMarker(Track.data.get(i).getLatitude(), Track.data.get(i).getLongitude());
	}


	public Coordinate getSelectedPosition() {
		return selectedPosition;
	}


	/**
	 * Display track stored in a TrackData class
	 * 
	 * @param tdata
	 *            TrackData object to display
	 * @param zoom2fit
	 *            If true the zoom is set have the complete display of the track
	 * 
	 */
	public void RefreshTrack(TrackData tdata, boolean zoom2fit) {
		if (tdata == null)
			return;
		if (tdata.data.size() <= 0)
			return;

		// Enabling the map tools
		btMapCenterOnTrack.setEnabled(true);
		btMapAddMarker.setEnabled(true);
		btMapMark.setEnabled(true);
		btMapEat.setEnabled(true);
		btMapDrink.setEnabled(true);
		btShowHideMarkers.setEnabled(true);
		btSaveMap.setEnabled(true);

		// -- Remove the previous track
		MapViewer.removeAllMapPolygons();
		MapViewer.removeAllMapMarkers();

		MapMarker = null;
		CurrentPosMarker = null;
		SummariesWeatherStationMarker = null;
		NormalsWeatherStationMarker = null;

		if (tdata.historicalWeatherData == null) {
			showWeatherStations = false;
			btShowWeatherStation.setSelected(false);
		} else if (showWeatherStations == true) {
			// If the new track or the recalculated track contains historical weather
			// we keep the current button state
			// otherwise, we hide the stations.
			showWeatherStations = false;
			ShowHideWeatherStations();
		}
		UpdateShowWeatherStationsButton();

		// -- Create the night tracks
		int cmpt = 0;
		boolean found = false;
		List<Coordinate> routeNight = null;
		MapPolyLine polyLineNight = null;

		Color cl_Transp = new Color(Settings.Color_Map_NightHighlight.getRed(),
				Settings.Color_Map_NightHighlight.getGreen(), Settings.Color_Map_NightHighlight.getBlue(),
				Settings.NightTrackTransparency);

		for (int i = 0; i < tdata.data.size(); i++) {
			CgData r = tdata.data.get(i);

			if (r.getNight() && !found) {
				routeNight = new ArrayList<Coordinate>();
				routeNight.add(new Coordinate(r.getLatitude(), r.getLongitude()));

				polyLineNight = new MapPolyLine(routeNight);
				polyLineNight.setColor(cl_Transp);

				polyLineNight.setStroke(new BasicStroke(Settings.NightTrackWidth));
				found = true;
			} else if (r.getNight() && found) {
				routeNight.add(new Coordinate(r.getLatitude(), r.getLongitude()));
			} else if (!r.getNight() && found) {
				MapViewer.addMapPolygon(polyLineNight);
				found = false;
			}
		}

		// -- Create the tracks (over the night tracks if necessary)
		cmpt = 0;
		List<Coordinate> routeNormal = new ArrayList<Coordinate>();
		double last_diff = tdata.data.get(0).getDiff();
		Color cl = getDiffColor(last_diff);

		for (CgData r : tdata.data) {
			if (r.getDiff() == last_diff) {
				// -- Add the point to the list
				routeNormal.add(new Coordinate(r.getLatitude(), r.getLongitude()));
				cmpt++;
			} else {
				// -- Add the point to the list
				routeNormal.add(new Coordinate(r.getLatitude(), r.getLongitude()));
				if (cmpt <= 1)
					routeNormal.add(new Coordinate(r.getLatitude(), r.getLongitude()));

				// -- Polyline creation
				MapPolyLine polyLineNormal = new MapPolyLine(routeNormal);
				// -- Set the line color
				cl = getDiffColor(last_diff);
				// -- Set the transparency
				cl = new Color(cl.getRed(), cl.getGreen(), cl.getBlue(), Settings.NormalTrackTransparency);
				polyLineNormal.setColor(cl);
				// -- Set the track width
				polyLineNormal.setStroke(new BasicStroke(Settings.NormalTrackWidth));

				// -- Add the polyline to the viewer
				MapViewer.addMapPolygon(polyLineNormal);

				// -- Start a new list of points
				routeNormal = new ArrayList<Coordinate>();
				routeNormal.add(new Coordinate(r.getLatitude(), r.getLongitude()));
				cmpt = 0;
			}
			last_diff = r.getDiff();
		}

		// -- Polyline creation
		MapPolyLine polyLineNormal = new MapPolyLine(routeNormal);
		// -- Set the line color
		cl = getDiffColor(last_diff);
		// -- Set the transparency
		cl = new Color(cl.getRed(), cl.getGreen(), cl.getBlue(), Settings.NormalTrackTransparency);
		polyLineNormal.setColor(cl);
		// -- Set the track width
		polyLineNormal.setStroke(new BasicStroke(Settings.NormalTrackWidth));

		// -- Add the polyline to the viewer
		MapViewer.addMapPolygon(polyLineNormal);

		/*
		 * 
		 * // -- Add the last polyline MapPolyLine polyLineEnd = new
		 * MapPolyLine(routeNormal); // -- Set the line color
		 * cl=getDiffColor(last_diff); cl= new Color(cl.getRed(), cl.getGreen(),
		 * cl.getBlue(), Settings.NormalTrackTransparency);
		 * 
		 * polyLineEnd.setColor(cl);
		 * 
		 * // -- Set the stroke polyLineEnd.setStroke(new
		 * BasicStroke(Settings.NormalTrackWidth)); //CgConst.TRACK_NORMAL_TICKNESS));
		 * // -- Upddate the viewer MapViewer.addMapPolygon(polyLineEnd);
		 */
		// -- Zoom to display the track
		if (zoom2fit)
			MapViewer.setDisplayToFitMapPolygons();

		// -- Add marks
		if (ShowMarkers) {
			for (CgData r : tdata.data) {
				int t = r.getTag();
				int v = 0;
				if ((t & CgConst.TAG_MARK) != 0)
					v = v + 1;
				if ((t & CgConst.TAG_EAT_PT) != 0)
					v = v + 2;
				if ((t & CgConst.TAG_WATER_PT) != 0)
					v = v + 4;

				if (v != 0)
					MapViewer.addMapMarker(new MapMarkerImg(new Coordinate(r.getLatitude(), r.getLongitude()),
							getImage("markers_" + v + ".png", Settings.MapIconSize)));
			}
		}
	}


	/***
	 * Return the color corresponding of the difficulty
	 * 
	 * @param diff
	 *            difficulty between 0..100
	 * @return color corresponding to the difficulty
	 */
	private Color getDiffColor(double diff) {
		if (diff >= CgConst.DIFF_VERYEASY)
			return Settings.Color_Diff_VeryEasy; // CgConst.CL_MAP_DIFF_VERYEASY);
		else if (diff >= CgConst.DIFF_EASY)
			return Settings.Color_Diff_Easy; // CgConst.CL_MAP_DIFF_EASY);
		else if (diff >= CgConst.DIFF_AVERAGE)
			return Settings.Color_Diff_Average; // CgConst.CL_MAP_DIFF_AVERAGE);
		else if (diff >= CgConst.DIFF_HARD)
			return Settings.Color_Diff_Hard; // CgConst.CL_MAP_DIFF_HARD);
		else
			return Settings.Color_Diff_VeryHard; // CgConst.CL_MAP_DIFF_VERYHARD);
	}


	/**
	 * Set the track difficulty
	 * 
	 * @param diff
	 */
	private void MapTrackDifficulty(double diff) {
		if ((Track.data.size() > 0) && (IndexMarker >= 0)) {
			// int row = panelTrackData.getSelectedRow();
			notifyRequestPosition();

			if (row < 0)
				return;

			Old_MarkerStart = IndexMarker;
			Old_MarkerEnd = row;

			// -- Check the start line is after the end line
			int start = IndexMarker;
			int end = row;
			if (IndexMarker > row) {
				start = row;
				end = IndexMarker;
			}

			// -- Clear the undo array
			UndoDiff.clear();

			// -- Fill the table
			for (int i = start; i <= end - 1; i++) {
				UndoDiff.add(Track.data.get(i).getDiff());
				Track.data.get(i).setDiff(diff);
			}

			IndexMarker = row;

			// -- Set the flags
			Track.isCalculated = false;
			Track.isModified = true;

			// -- Refresh the table and map
			// panelTrackData.refresh();
			notifyChange();
			RefreshTrack(Track, false);
			ShowMapMarker();
		}
	}


	private void SetMarkMapMarker() {
		if (Track.data.size() > 0) {
			// int row = panelTrackData.getSelectedRow();
			notifyRequestPosition();

			if (row < 0)
				return;

			int tag = Track.data.get(row).getTag();

			if ((tag & CgConst.TAG_MARK) == 0)
				tag = tag | CgConst.TAG_MARK;
			else
				tag = tag & (~CgConst.TAG_MARK);

			Track.data.get(row).setTag(tag);

			// -- Set the flags
			Track.isCalculated = false;
			Track.isModified = true;

			// -- Refresh the table and map
			// panelTrackData.refresh();
			notifyChange();
			RefreshTrack(Track, false);
			ShowMapMarker();
		}
	}


	private void HideMapMarker() {
		if (Track.data.size() > 0) {
			IndexMarker = -1;
			if (MapMarker != null) {
				MapViewer.removeMapMarker(MapMarker);
				MapMarker = null;
			}
			RefreshMapButtons();
		}
	}


	private void ShowMapMarker() {
		if (Track.data.size() > 0) {
			// int row = panelTrackData.getSelectedRow();
			notifyRequestPosition();

			if (row < 0)
				return;

			IndexMarker = row;

			double lat = Track.data.get(row).getLatitude();
			double lon = Track.data.get(row).getLongitude();

			// -- Display the marker at "lat,lon" position
			RefreshMapMarker(lat, lon);
			RefreshMapButtons();
		}
	}


//<<<<<<< floatabletoolbar
//=======
	private void ShowHideWeatherStations() {
		showWeatherStations = !showWeatherStations;

		btShowWeatherStation.setSelected(showWeatherStations);

		if (!ShowMarkers)
			return;

		if (showWeatherStations) {
			if (Track.historicalWeatherData != null) {
				if (SummariesWeatherStationMarker == null
						&& Track.historicalWeatherData.noaaSummariesWeatherStation != null) {
					double lat = Double
							.parseDouble(Track.historicalWeatherData.noaaSummariesWeatherStation.getLatitude());
					double lon = Double
							.parseDouble(Track.historicalWeatherData.noaaSummariesWeatherStation.getLongitude());
					SummariesWeatherStationMarker = new MapMarkerImg(new Coordinate(lat, lon),
							getImage("weather_station.png", Settings.MapIconSize));
				}
				if (NormalsWeatherStationMarker == null
						&& Track.historicalWeatherData.noaaNormalsWeatherStation != null) {

					double lat = Double
							.parseDouble(Track.historicalWeatherData.noaaNormalsWeatherStation.getLatitude());
					double lon = Double
							.parseDouble(Track.historicalWeatherData.noaaNormalsWeatherStation.getLongitude());
					NormalsWeatherStationMarker = new MapMarkerImg(new Coordinate(lat, lon),
							getImage("weather_station.png", Settings.MapIconSize));

				}

				if (SummariesWeatherStationMarker != null) {
					MapViewer.addMapMarker(SummariesWeatherStationMarker);
				}
				if (NormalsWeatherStationMarker != null) {
					MapViewer.addMapMarker(NormalsWeatherStationMarker);
				}

				// In case the station is out of the screen, it will not be seen
				// by the user. Hence, we center the map on the track and the markers.
				if (SummariesWeatherStationMarker != null || NormalsWeatherStationMarker != null) {
					MapViewer.setDisplayToFitMapMarkersAndPolygons();
				}

			}
		} else {
			if (SummariesWeatherStationMarker != null) {
				MapViewer.removeMapMarker(SummariesWeatherStationMarker);
			}
			if (NormalsWeatherStationMarker != null) {
				MapViewer.removeMapMarker(NormalsWeatherStationMarker);
			}
		}

	}


//>>>>>>> master
	private void SetEatMapMarker() {
		if (Track.data.size() > 0) {
			// int row = panelTrackData.getSelectedRow();
			notifyRequestPosition();

			if (row < 0)
				return;

			int tag = Track.data.get(row).getTag();

			if ((tag & CgConst.TAG_EAT_PT) == 0) {
				tag = tag | CgConst.TAG_EAT_PT;
				tag = tag & (~CgConst.TAG_WATER_PT);
			} else
				tag = tag & (~CgConst.TAG_EAT_PT);

			Track.data.get(row).setTag(tag);

			// -- Set the flags
			Track.isCalculated = false;
			Track.isModified = true;

			// -- Refresh the table and map
			// panelTrackData.refresh();
			notifyChange();
			RefreshTrack(Track, false);
			ShowMapMarker();
		}
	}


	private void SetDrinkMapMarker() {
		if (Track.data.size() > 0) {
			// int row = panelTrackData.getSelectedRow();
			notifyRequestPosition();

			if (row < 0)
				return;

			int tag = Track.data.get(row).getTag();

			if ((tag & CgConst.TAG_WATER_PT) == 0) {
				tag = tag | CgConst.TAG_WATER_PT;
				tag = tag & (~CgConst.TAG_EAT_PT);
			} else
				tag = tag & (~CgConst.TAG_WATER_PT);

			Track.data.get(row).setTag(tag);

			// -- Set the flags
			Track.isCalculated = false;
			Track.isModified = true;

			// -- Refresh the table and map
			// panelTrackData.refresh();
			notifyChange();
			RefreshTrack(Track, false);
			ShowMapMarker();
		}
	}


	public void RefreshMapType() {
		switch (Settings.map) {
		case 0:
			MapViewer.setTileSource(new OpenStreetMap());
			offlineTileCache
					.setTileCacheDir(DataDir + "/" + CgConst.CG_DIR + "/TileCache/" + CgConst.OPENSTREETMAP_CACHE_DIR);
			break;
		case 1:
			MapViewer.setTileSource(new OpenTopoMap());
			offlineTileCache
					.setTileCacheDir(DataDir + "/" + CgConst.CG_DIR + "/TileCache/" + CgConst.OPENTOPOMAP_CACHE_DIR);
			break;
		case 2:
			MapViewer.setTileSource(new Outdoors(Settings));
			offlineTileCache
					.setTileCacheDir(DataDir + "/" + CgConst.CG_DIR + "/TileCache/" + CgConst.OUTDOORS_CACHE_DIR);
			break;
		case 3:
			MapViewer.setTileSource(new BingAerialTileSource());
			offlineTileCache.setTileCacheDir(DataDir + "/" + CgConst.CG_DIR + "/TileCache/" + CgConst.BING_CACHE_DIR);
			break;
		default:
			MapViewer.setTileSource(new OpenStreetMap());
			offlineTileCache
					.setTileCacheDir(DataDir + "/" + CgConst.CG_DIR + "/TileCache/" + CgConst.OPENSTREETMAP_CACHE_DIR);
		}
	}


	public File getTileCacheDir() {
		switch (Settings.map) {
		case 0:
			return new File(DataDir + "/" + CgConst.CG_DIR, "TileCache/" + CgConst.OPENSTREETMAP_CACHE_DIR);
		case 1:
			return new File(DataDir + "/" + CgConst.CG_DIR, "TileCache/" + CgConst.OPENTOPOMAP_CACHE_DIR);
		case 2:
			return new File(DataDir + "/" + CgConst.CG_DIR, "TileCache/" + CgConst.OUTDOORS_CACHE_DIR);
		case 3:
			return new File(DataDir + "/" + CgConst.CG_DIR, "TileCache/" + CgConst.BING_CACHE_DIR);
		default:
			return new File(DataDir + "/" + CgConst.CG_DIR, "TileCache/" + CgConst.OPENSTREETMAP_CACHE_DIR);
		}
	}


	/**
	 * Refresh the marker on the map
	 * 
	 * @param lat
	 *            latitude of the position of the marker
	 * @param lon
	 *            longitude of the position of the marker
	 */
	public void RefreshMapMarker(double lat, double lon) {
		if (MapMarker == null) {
			// -- Define the current position marker
			MapMarker = new MapMarkerImg(new Coordinate(lat, lon), getImage("marker.png", Settings.MapIconSize));
			MapViewer.addMapMarker(MapMarker);
		} else {
			MapMarker.setLat(lat);
			MapMarker.setLon(lon);
			MapViewer.setDisplayPosition(new Coordinate(lat, lon), MapViewer.getZoom());
		}
	}


	private void UndoMapFillDiff() {
		if ((Old_MarkerStart >= 0) && (Old_MarkerEnd >= 0) && (UndoDiff.size() > 0)) {
			int i1 = Old_MarkerStart;
			int i2 = Old_MarkerEnd;
			if (Old_MarkerStart > Old_MarkerEnd) {
				i1 = Old_MarkerEnd;
				i2 = Old_MarkerStart;
			}
			if (i1 != 0)
				i1++;

			for (int i = i1; i <= i2; i++) {
				Track.data.get(i).setDiff(UndoDiff.get(i - i1));
			}

			Old_MarkerStart = -1;
			Old_MarkerEnd = -1;

			UndoDiff.clear();

			// -- Set the flags
			Track.isCalculated = false;
			Track.isModified = true;

			// -- Refresh the table and map
			// panelTrackData.refresh();
			notifyChange();
			RefreshTrack(Track, false);
			ShowMapMarker();
		}
	}


	public void setTrack(TrackData track) {
		Track = track;
		Track.addHistoricalWeatherListener(this);
		MapViewer.removeAllMapMarkers();
		RefreshTrack(Track, true);
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
	 * Refresh the position of the marker on the map
	 * 
	 * @param lat
	 *            latitude of the position of the marker
	 * @param lon
	 *            longitude of the position of the marker
	 */
	public void RefreshCurrentPosMarker(double lat, double lon) {
		if (CurrentPosMarker == null) {
			// -- Define the current position marker
			CurrentPosMarker = new MapMarkerImg(new Coordinate(lat, lon),
					getImage("current_marker.png", Settings.MapIconSize));
			MapViewer.addMapMarker(CurrentPosMarker);
		} else {
			CurrentPosMarker.setLat(lat);
			CurrentPosMarker.setLon(lon);
			MapViewer.setDisplayPosition(new Coordinate(lat, lon), MapViewer.getZoom());
		}
	}


	public void RefreshMapButtons() {
		btMapTrackVeryHard.setEnabled(IndexMarker != -1);
		btMapTrackHard.setEnabled(IndexMarker != -1);
		btMapTrackAverage.setEnabled(IndexMarker != -1);
		btMapTrackEasy.setEnabled(IndexMarker != -1);
		btMapTrackVeryEasy.setEnabled(IndexMarker != -1);
		btMapHideMarker.setEnabled(IndexMarker != -1);
		btMapUndo.setEnabled(Old_MarkerStart != -1);
	}


	public void setRow(int row) {
		this.row = row;
	}


	private void SelectMap() {
		int ret = selectMap.showDialog(Settings.map);
		if (ret >= 0) {
			Settings.map = ret;
			RefreshMapType();
		}
	}


	/**
	 * Show/Hide markers on the maps
	 */
	private void ShowHideMarkers() {
		ShowMarkers = !ShowMarkers;
		btShowHideMarkers.setSelected(ShowMarkers);
		RefreshTrack(Track, false);

		if (!ShowMarkers) {
			showWeatherStations = false;
			btShowWeatherStation.setSelected(false);
		}
	}


	private Image getImage(String name, int size) {
		return createImageIcon("/course_generator/images/" + size + "/" + name, "").getImage();
	}


	private void UpdateShowWeatherStationsButton() {
		if (Track.historicalWeatherData != null && (Track.historicalWeatherData.noaaNormalsWeatherStation != null
				|| Track.historicalWeatherData.noaaSummariesWeatherStation != null)) {
			btShowWeatherStation.setEnabled(true);
		} else {
			btShowWeatherStation.setEnabled(false);
		}
	}

}
