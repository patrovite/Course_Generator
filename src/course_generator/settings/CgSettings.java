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

package course_generator.settings;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.SAXException;

import com.sun.xml.txw2.output.IndentingXMLStreamWriter;

import course_generator.utils.CgConst;
import course_generator.utils.Utils;

/**
 *
 * @author pierre.delore
 */
public class CgSettings {
	public String ParamFile = "";
	public boolean bNoConnectOnStartup = true;
	public int ConnectionTimeout = 10; // Time in second between internet test
	/**
	 * Map selected : 0 => OpenStreetMap. 1 => OpenTopoMap. 2 => Outdoors. 3=>
	 * BingAerial
	 **/
	public int map = 0;

	public boolean offlineMap = true;

	public String MemoFormat[] = new String[5];
	public String mruGPX[] = new String[5];
	public String mruCGX[] = new String[5];
	public int TableMainColWidth[] = new int[16];
	public String Language;
	public int MainWindowWidth;
	public int MainWindowHeight;
	public int VertSplitPosition;
	public int HorizSplitPosition;
	/** Mini roadbook split position **/
	public int MRB_SplitPosition;
	/** Below this distance we consider the distance short (in m) **/
	public double DistNear = 100.0;
	/** Over this distance we consider the distance far (in m) **/
	public double DistFar = 1000.0;

	/** Threshold where CG ask if a position filter must be apply (m) **/
	public int PosFilterAskThreshold = 10;

	public int Unit = CgConst.UNIT_METER; // Unit for the display 0=meter
											// 1=Miles/feet
	public boolean isPace = false; // 'true' the speed is display as pace
									// otherwise it's a speed
	public boolean Check4UpdateAtStart = true;

	public int ReadError = 0;
	public int LineError = 0;
	public String LastDir; // Store the last directory
	public String previousGPXDirectory;
	public String previousCGXDirectory;
	public String previousCSVDirectory;
	public String previousPNGDirectory;

	public String DefaultFontName;
	public int DefaultFontStyle;
	public int DefaultFontSize;

	public int StatusbarIconSize;
	public int TabIconSize;
	public int ToolbarIconSize;
	public int MapToolbarIconSize;
	public int MenuIconSize;
	public int TagIconSize;
	public int DialogIconSize;
	public int MapIconSize;
	public int CurveButtonsIconSize;

	private String ThunderForestApiKey;
	private PropertyChangeSupport ThunderForestApiKeyChanged = new PropertyChangeSupport(this);

	private String NoaaToken;
	private PropertyChangeSupport NoaaTokenChanged = new PropertyChangeSupport(this);

	public Color Color_Diff_VeryEasy;
	public Color Color_Diff_Easy;
	public Color Color_Diff_Average;
	public Color Color_Diff_Hard;
	public Color Color_Diff_VeryHard;
	public Color Color_Map_NightHighlight;
	public int NormalTrackWidth;
	public int NightTrackWidth;
	public int NormalTrackTransparency;
	public int NightTrackTransparency;
	public String MapToolBarLayout;
	public int MapToolBarOrientation;


	public CgSettings() {
		int i = 0;

		ParamFile = "Default";
		bNoConnectOnStartup = true;
		ConnectionTimeout = 10;
		Language = "en";

		MainWindowWidth = 800;
		MainWindowHeight = 600;
		VertSplitPosition = 200;
		HorizSplitPosition = 50;
		MRB_SplitPosition = 220;

		DistNear = 100.0;
		DistFar = 1000.0;

		MemoFormat = new String[5];
		mruGPX = new String[5];
		mruCGX = new String[5];
		TableMainColWidth = new int[16];

		Unit = CgConst.UNIT_METER;
		isPace = false;
		Check4UpdateAtStart = true;

		ReadError = 0;
		LineError = 0;

		for (i = 0; i < 5; i++) {
			MemoFormat[i] = "";
			mruGPX[i] = "";
			mruCGX[i] = "";
		}

		for (i = 0; i < 16; i++) {
			TableMainColWidth[i] = 60;
		}
		LastDir = "";
		previousCGXDirectory = "";
		previousGPXDirectory = "";
		previousCSVDirectory = "";
		previousPNGDirectory = "";

		offlineMap = true;
		map = 0;
		PosFilterAskThreshold = 5;

		DefaultFontName = "Arial";
		DefaultFontStyle = 0; // Normal
		DefaultFontSize = 14;

		StatusbarIconSize = 22;
		TabIconSize = 20;
		ToolbarIconSize = 22;
		MapToolbarIconSize = 22;
		MapIconSize = 32;
		MenuIconSize = 22;
		TagIconSize = 16;
		DialogIconSize = 20;
		CurveButtonsIconSize = 32;

		ThunderForestApiKey = "";

		Color_Diff_VeryEasy = CgConst.CL_DIFF_VERYEASY;
		Color_Diff_Easy = CgConst.CL_DIFF_EASY;
		Color_Diff_Average = CgConst.CL_DIFF_AVERAGE;
		Color_Diff_Hard = CgConst.CL_DIFF_HARD;
		Color_Diff_VeryHard = CgConst.CL_DIFF_VERYHARD;
		Color_Map_NightHighlight = CgConst.CL_MAP_NIGHT_HIGHLIGHT;
		NormalTrackWidth = CgConst.TRACK_NORMAL_TICKNESS;
		NightTrackWidth = CgConst.TRACK_NIGHT_TICKNESS;
		NormalTrackTransparency = CgConst.NORMAL_TRACK_TRANSPARENCY;
		NightTrackTransparency = CgConst.NIGHT_TRACK_TRANSPARENCY;

		MapToolBarLayout = "WEST";
		MapToolBarOrientation = javax.swing.SwingConstants.VERTICAL;
	}


	/**
	 * Save the settings to the disk
	 * 
	 * @param path
	 *            Path where the setting file is stored
	 */
	public void Save(String path) {
		// -- Check if the data directory exist. If not! creation
		Path DataFolder = Paths.get(path);
		if (Files.notExists(DataFolder)) {
			try {
				Files.createDirectory(DataFolder);
			} catch (IOException e) {
				System.out.println("CgSettings.Save : Impossible to create " + "the data/config directory");
				return;
			}
		}

		// -- Save the data in the home directory
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		try {
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
					new FileOutputStream(path + "/config.xml"));
			XMLStreamWriter writer = new IndentingXMLStreamWriter(
					factory.createXMLStreamWriter(bufferedOutputStream, "UTF-8"));

			writer.writeStartDocument("UTF-8", "1.0");
			writer.writeComment("Course Generator (C) Pierre DELORE");
			writer.writeStartElement("CONFIG");
			Utils.WriteStringToXML(writer, "PARAMFILE", ParamFile);
			Utils.WriteBooleanToXML(writer, "NOCONNECTIONONSTARTUP", bNoConnectOnStartup);
			Utils.WriteIntToXML(writer, "CONNECTIONTIMEOUT", ConnectionTimeout);
			Utils.WriteStringToXML(writer, "LASTDIR", LastDir);
			Utils.WriteStringToXML(writer, "PREVIOUSCGXDIR", previousCGXDirectory);
			Utils.WriteStringToXML(writer, "PREVIOUSGPXDIR", previousGPXDirectory);
			Utils.WriteStringToXML(writer, "PREVIOUSCSVDIR", previousCSVDirectory);
			Utils.WriteStringToXML(writer, "PREVIOUSPNGDIR", previousPNGDirectory);

			Utils.WriteStringToXML(writer, "MEMOFORMAT1", MemoFormat[0]);
			Utils.WriteStringToXML(writer, "MEMOFORMAT2", MemoFormat[1]);
			Utils.WriteStringToXML(writer, "MEMOFORMAT3", MemoFormat[2]);
			Utils.WriteStringToXML(writer, "MEMOFORMAT4", MemoFormat[3]);
			Utils.WriteStringToXML(writer, "MEMOFORMAT5", MemoFormat[4]);

			Utils.WriteStringToXML(writer, "MRUGPX1", mruGPX[0]);
			Utils.WriteStringToXML(writer, "MRUGPX2", mruGPX[1]);
			Utils.WriteStringToXML(writer, "MRUGPX3", mruGPX[2]);
			Utils.WriteStringToXML(writer, "MRUGPX4", mruGPX[3]);
			Utils.WriteStringToXML(writer, "MRUGPX5", mruGPX[4]);

			Utils.WriteStringToXML(writer, "MRUCGX1", mruCGX[0]);
			Utils.WriteStringToXML(writer, "MRUCGX2", mruCGX[1]);
			Utils.WriteStringToXML(writer, "MRUCGX3", mruCGX[2]);
			Utils.WriteStringToXML(writer, "MRUCGX4", mruCGX[3]);
			Utils.WriteStringToXML(writer, "MRUCGX5", mruCGX[4]);

			Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH01", TableMainColWidth[0]);
			Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH02", TableMainColWidth[1]);
			Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH03", TableMainColWidth[2]);
			Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH04", TableMainColWidth[3]);
			Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH05", TableMainColWidth[4]);
			Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH06", TableMainColWidth[5]);
			Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH07", TableMainColWidth[6]);
			Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH08", TableMainColWidth[7]);
			Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH09", TableMainColWidth[8]);
			Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH10", TableMainColWidth[9]);
			Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH11", TableMainColWidth[10]);
			Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH12", TableMainColWidth[11]);
			Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH13", TableMainColWidth[12]);
			Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH14", TableMainColWidth[13]);
			Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH15", TableMainColWidth[14]);
			Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH16", TableMainColWidth[15]);

			Utils.WriteIntToXML(writer, "UNIT", Unit);
			Utils.WriteBooleanToXML(writer, "ISPACE", isPace);
			Utils.WriteBooleanToXML(writer, "CHECK4UPDATEATSTART", Check4UpdateAtStart);
			Utils.WriteStringToXML(writer, "LANGUAGE", Language);
			Utils.WriteIntToXML(writer, "MAINWINDOWSWIDTH", MainWindowWidth);
			Utils.WriteIntToXML(writer, "MAINWINDOWSHEIGHT", MainWindowHeight);
			Utils.WriteIntToXML(writer, "VERTSPLITPOSITION", VertSplitPosition);
			Utils.WriteIntToXML(writer, "HORIZSPLITPOSITION", HorizSplitPosition);
			Utils.WriteIntToXML(writer, "MRBSPLITPOSITION", MRB_SplitPosition);
			Utils.WriteIntToXML(writer, "MAP", map);

			Utils.WriteDoubleToXML(writer, "DISTNEAR", DistNear);
			Utils.WriteDoubleToXML(writer, "DISTFAR", DistFar);

			Utils.WriteIntToXML(writer, "POSFILTERASKTHRESHOLD", PosFilterAskThreshold);

			Utils.WriteStringToXML(writer, "DEFAULTFONTNAME", DefaultFontName);
			Utils.WriteIntToXML(writer, "DEFAULTFONTSTYLE", DefaultFontStyle);
			Utils.WriteIntToXML(writer, "DEFAULTFONTSIZE", DefaultFontSize);

			Utils.WriteIntToXML(writer, "STATUSBARICONSIZE", StatusbarIconSize);
			Utils.WriteIntToXML(writer, "TABICONSIZE", TabIconSize);
			Utils.WriteIntToXML(writer, "TOOLBARICONSIZE", ToolbarIconSize);
			Utils.WriteIntToXML(writer, "MAPTOOLBARICONSIZE", MapToolbarIconSize);
			Utils.WriteIntToXML(writer, "MENUICONSIZE", MenuIconSize);
			Utils.WriteIntToXML(writer, "TAGICONSIZE", TagIconSize);
			Utils.WriteIntToXML(writer, "DIALOGICONSIZE", DialogIconSize);
			Utils.WriteIntToXML(writer, "MAPICONSIZE", MapIconSize);
			Utils.WriteIntToXML(writer, "CURVEBUTTONSICONSIZE", CurveButtonsIconSize);

			Utils.WriteStringToXML(writer, "THUNDERFORESTAPIKEY", ThunderForestApiKey);
			Utils.WriteStringToXML(writer, "NOAATOKEN", NoaaToken);

			Utils.WriteIntToXML(writer, "COLORDIFFVERYEASY", Color_Diff_VeryEasy.getRGB());
			Utils.WriteIntToXML(writer, "COLORDIFFEASY", Color_Diff_Easy.getRGB());
			Utils.WriteIntToXML(writer, "COLORDIFFAVERAGE", Color_Diff_Average.getRGB());
			Utils.WriteIntToXML(writer, "COLORDIFFHARD", Color_Diff_Hard.getRGB());
			Utils.WriteIntToXML(writer, "COLORDIFFVERYHARD", Color_Diff_VeryHard.getRGB());
			Utils.WriteIntToXML(writer, "COLORMAPNIGHTHIGHLIGHT", Color_Map_NightHighlight.getRGB());

			Utils.WriteIntToXML(writer, "NORMALTRACKWIDTH", NormalTrackWidth);
			Utils.WriteIntToXML(writer, "NIGHTTRACKWIDTH", NightTrackWidth);

			Utils.WriteIntToXML(writer, "NORMALTRACKTRANSPARENCY", NormalTrackTransparency);
			Utils.WriteIntToXML(writer, "NIGHTTRACKTRANSPARENCY", NightTrackTransparency);

			Utils.WriteStringToXML(writer, "MAPTOOLBARLAYOUT", MapToolBarLayout);
			Utils.WriteIntToXML(writer, "MAPTOOLBARORIENTATION", MapToolBarOrientation);

			writer.writeEndElement();
			writer.writeEndDocument();

			writer.flush();
			writer.close();
		} catch (XMLStreamException | IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Load the settings from disk
	 * 
	 * @param _Path
	 *            Path where the setting file is stored
	 */
	public void Load(String _Path) {
		// -- Test if the config file exist
		if (!(new File(_Path + "/config.xml").isFile()))
			return;

		SaxConfigHandler Confighandler = new SaxConfigHandler();

		int ret = 0;
		try {
			ret = Confighandler.readDataFromConfig(_Path + "/config.xml", this);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		if (ret != 0)
			System.out.println("CgSettings.Load : Error while loading config file '" + _Path + "'- line ="
					+ Confighandler.getErrLine());
	}


	/**
	 * Return the distance unit as string
	 * 
	 * @return string with the unit
	 */
	public String getDistanceUnitString() {
		switch (Unit) {
		case CgConst.UNIT_METER:
			return "km";
		case CgConst.UNIT_MILES_FEET:
			return "miles";
		default:
			return "Km";
		}
	}


	/**
	 * Return the distance unit as string (abbreviation)
	 * 
	 * @return string with the unit
	 */
	public String getShortDistanceUnitString() {
		switch (Unit) {
		case CgConst.UNIT_METER:
			return "km";
		case CgConst.UNIT_MILES_FEET:
			return "mi";
		default:
			return "km";
		}
	}


	/**
	 * Return the elevation unit as string
	 * 
	 * @return string with the unit
	 */
	public String getElevationUnitString() {
		switch (Unit) {
		case CgConst.UNIT_METER:
			return "m";
		case CgConst.UNIT_MILES_FEET:
			return "feet";
		default:
			return "m";
		}
	}


	/**
	 * Return the elevation unit as string (abbreviation)
	 * 
	 * @return string with the unit
	 */
	public String getShortElevationUnitString() {
		switch (Unit) {
		case CgConst.UNIT_METER:
			return "m";
		case CgConst.UNIT_MILES_FEET:
			return "ft";
		default:
			return "m";
		}
	}


	/**
	 * Returns the user's thunderforest's API Key
	 * 
	 * @return string with the key
	 */
	public String getThunderForestApiKey() {
		return ThunderForestApiKey == null ? "" : ThunderForestApiKey;
	}


	/**
	 * Sets the user's thunderforest's API Key
	 * 
	 * @param key
	 *            The entered key
	 */
	public void setThunderForestApiKey(String newKey) {
		String oldKey = ThunderForestApiKey;
		ThunderForestApiKey = newKey;
		ThunderForestApiKeyChanged.firePropertyChange("ThunderForestApiKeyChanged", oldKey, newKey);
	}


	public void addPropertyChangeListener(PropertyChangeListener listener) {
		ThunderForestApiKeyChanged.addPropertyChangeListener(listener);
	}


	/**
	 * Verifies that the thunderforest's API Key is a valid one
	 * 
	 */
	public boolean isThunderForestApiKeyValid() {
		boolean isKeyValid = false;

		if (ThunderForestApiKey != null && ThunderForestApiKey.length() == 32)
			isKeyValid = true;

		return isKeyValid;
	}


	/**
	 * Returns the user's NOAA token.
	 * 
	 * @return string with the key
	 */
	public String getNoaaToken() {
		return NoaaToken == null ? "" : NoaaToken;
	}


	/**
	 * Sets the user's NOAA token.
	 * 
	 * @param token
	 *            The entered token
	 */
	public void setNoaaToken(String token) {
		String oldToken = NoaaToken;
		NoaaToken = token;
		NoaaTokenChanged.firePropertyChange("NoaaTokenChanged", oldToken, token);
	}


	public void addNoaaTokenChangeListener(PropertyChangeListener listener) {
		NoaaTokenChanged.addPropertyChangeListener(listener);
	}


	/**
	 * Verifies that the NOAA token is a valid one
	 * 
	 */
	public boolean isNoaaTokenValid() {
		boolean isTokenValid = false;

		if (NoaaToken != null && NoaaToken.length() == 32)
			isTokenValid = true;

		return isTokenValid;
	}

}
