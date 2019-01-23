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
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import course_generator.utils.CgConst;

/**
 *
 * @author pierre.delore
 */
public class SaxConfigHandler extends DefaultHandler {

	private String characters = "";
	private int level = 0;
	private final int LEVEL_CONFIG = 1;

	private int errline = 0;
	private Locator locator;

	private final int ERR_READ_NO = 0;
	private final int ERR_READ_DOUBLE = -1;
	private final int ERR_READ_INT = -2;
	private final int ERR_READ_BOOL = -3;
	private final int ERR_READ_NOTEXIST = -6;
	private final int ERR_READ_COLOR = -7;
	private CgSettings Settings;


	/**
	 * Read the GPX file from disc
	 * 
	 * @param filename
	 *            Name of the gpx file to read
	 * @param TData
	 *            TrackData object where to store the read data
	 * @param readmode
	 *            Reading mode 0=Load the complete file 1=Insert the read data at
	 *            the beginning of the current track 2=Insert the read data at the
	 *            end of the current track
	 * @return The error code Erroce explanation: ERR_READ_NO = No problem during
	 *         the reading of the file ERR_READ_LAT = Parsing error during the read
	 *         of a latitude (lat) element ERR_READ_LON = Parsing error during the
	 *         read of a longitude (lon) element ERR_READ_ELE = Parsing error during
	 *         the read of a elevation (ele) element ERR_READ_TIME = Parsing error
	 *         during the read of a time (time) element ERR_READ_VERSION = Parsing
	 *         error during the read of a version of the GPX file. Must be 1.1
	 *         ERR_READ_NOTEXIST = The file doesn't exist or can't by read
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public int readDataFromConfig(String filename, CgSettings _Settings)
			throws SAXException, IOException, ParserConfigurationException {

		Settings = _Settings;
		characters = "";
		level = 0;
		Settings.ReadError = ERR_READ_NO;
		Settings.LineError = 0;

		// try {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();

		File f = new File(filename);
		if (f.isFile() && f.canRead()) {
			// -- ok let's go

			// -- Parse the file
			parser.parse(f, this);
		} else
			Settings.ReadError = ERR_READ_NOTEXIST;
		// }
		// catch (SAXException | IOException | ParserConfigurationException e) {
		// Settings.ReadError=ERR_READ_NOTEXIST;
		// }

		return Settings.ReadError;
	}


	public int getErrLine() {
		return errline;
	}


	/**
	 * Parse a string element
	 * 
	 * @return Return the parsed value
	 */
	private String ManageString() {
		String S = characters;
		characters = "";
		return S;
	}


	/**
	 * Parse a double element
	 * 
	 * @param _default
	 *            Default value
	 * @param _errcode
	 *            Error code if a parse error occure
	 * @return Return the parsed value
	 */
	private double ManageDouble(double _default, int _errcode) {
		try {
			return Double.parseDouble(characters);
		} catch (NumberFormatException e) {
			Settings.ReadError = _errcode;
			errline = locator.getLineNumber();
			return _default;
		}
	}


	/**
	 * Parse a integer element
	 * 
	 * @param _default
	 *            Default value
	 * @param _errcode
	 *            Error code if a parse error occure
	 * @return Return the parsed value
	 */
	private int ManageInt(int _default, int _errcode) {
		try {
			return Integer.parseInt(characters);
		} catch (NumberFormatException e) {
			Settings.ReadError = _errcode;
			errline = locator.getLineNumber();
			return _default;
		}
	}


	/**
	 * Parse a boolean element
	 * 
	 * @param _default
	 *            Default value
	 * @param _errcode
	 *            Error code if a parse error occure
	 * @return Return the parsed value
	 */
	private boolean ManageBoolean(boolean _default, int _errcode) {
		try {
			return Boolean.parseBoolean(characters);
		} catch (NumberFormatException e) {
			Settings.ReadError = _errcode;
			errline = locator.getLineNumber();
			return _default;
		}
	}


	/**
	 * Parse a color element
	 * 
	 * @param _default
	 *            Default value
	 * @param _errcode
	 *            Error code if a parse error occur
	 * @return Return the parsed color
	 */
	private Color ManageColor(Color _default, int _errcode) {
		try {
			int v = Integer.parseInt(characters);
			characters = "";
			return new Color(v);
		} catch (NumberFormatException e) {
			// errcode = _errcode;
			errline = locator.getLineNumber();
			characters = "";
			return _default;
		}
	}


	@Override
	public void setDocumentLocator(final Locator locator) {
		this.locator = locator; // Save the locator, so that it can be used later for line tracking when
								// traversing nodes.
	}


	@Override
	public void startElement(String uri, String localname, String qName, Attributes attributs) throws SAXException {
		if (qName.equalsIgnoreCase("CONFIG")) {
			level++;
		}
	}


	@Override
	public void endElement(String uri, String localname, String qName) throws SAXException {
		if (level == LEVEL_CONFIG) {
			if (qName.equalsIgnoreCase("PARAMFILE")) {
				Settings.ParamFile = ManageString().trim();
			} else if (qName.equalsIgnoreCase("NOCONNECTIONONSTARTUP")) {
				Settings.bNoConnectOnStartup = ManageBoolean(true, ERR_READ_BOOL);
			} else if (qName.equalsIgnoreCase("CONNECTIONTIMEOUT")) {
				Settings.ConnectionTimeout = ManageInt(10000, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("LASTDIR")) {
				Settings.setLastDirectory(ManageString().trim());
			} else if (qName.equalsIgnoreCase("PREVIOUSCGXDIR")) {
				Settings.previousCGXDirectory = ManageString().trim();
			} else if (qName.equalsIgnoreCase("PREVIOUSGPXDIR")) {
				Settings.previousGPXDirectory = ManageString().trim();
			} else if (qName.equalsIgnoreCase("PREVIOUSCSVDIR")) {
				Settings.previousCSVDirectory = ManageString().trim();
			} else if (qName.equalsIgnoreCase("PREVIOUSPNGDIR")) {
				Settings.previousPNGDirectory = ManageString().trim();
			} else if (qName.equalsIgnoreCase("MEMOFORMAT1")) {
				Settings.MemoFormat[0] = ManageString();
			} else if (qName.equalsIgnoreCase("MEMOFORMAT2")) {
				Settings.MemoFormat[1] = ManageString();
			} else if (qName.equalsIgnoreCase("MEMOFORMAT3")) {
				Settings.MemoFormat[2] = ManageString();
			} else if (qName.equalsIgnoreCase("MEMOFORMAT4")) {
				Settings.MemoFormat[3] = ManageString().trim();
			} else if (qName.equalsIgnoreCase("MEMOFORMAT5")) {
				Settings.MemoFormat[4] = ManageString();
			} else if (qName.equalsIgnoreCase("MRUGPX1")) {
				Settings.mruGPX[0] = ManageString().trim();
			} else if (qName.equalsIgnoreCase("MRUGPX2")) {
				Settings.mruGPX[1] = ManageString().trim();
			} else if (qName.equalsIgnoreCase("MRUGPX3")) {
				Settings.mruGPX[2] = ManageString().trim();
			} else if (qName.equalsIgnoreCase("MRUGPX4")) {
				Settings.mruGPX[3] = ManageString().trim();
			} else if (qName.equalsIgnoreCase("MRUGPX5")) {
				Settings.mruGPX[4] = ManageString().trim();
			} else if (qName.equalsIgnoreCase("MRUCGX1")) {
				Settings.mruCGX[0] = ManageString().trim();
			} else if (qName.equalsIgnoreCase("MRUCGX2")) {
				Settings.mruCGX[1] = ManageString().trim();
			} else if (qName.equalsIgnoreCase("MRUCGX3")) {
				Settings.mruCGX[2] = ManageString().trim();
			} else if (qName.equalsIgnoreCase("MRUCGX4")) {
				Settings.mruCGX[3] = ManageString().trim();
			} else if (qName.equalsIgnoreCase("MRUCGX5")) {
				Settings.mruCGX[4] = ManageString().trim();
			} else if (qName.equalsIgnoreCase("TABLEMAINCOLWIDTH01")) {
				Settings.TableMainColWidth[0] = ManageInt(60, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("TABLEMAINCOLWIDTH02")) {
				Settings.TableMainColWidth[1] = ManageInt(60, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("TABLEMAINCOLWIDTH03")) {
				Settings.TableMainColWidth[2] = ManageInt(60, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("TABLEMAINCOLWIDTH04")) {
				Settings.TableMainColWidth[3] = ManageInt(60, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("TABLEMAINCOLWIDTH05")) {
				Settings.TableMainColWidth[4] = ManageInt(60, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("TABLEMAINCOLWIDTH06")) {
				Settings.TableMainColWidth[5] = ManageInt(60, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("TABLEMAINCOLWIDTH07")) {
				Settings.TableMainColWidth[6] = ManageInt(60, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("TABLEMAINCOLWIDTH08")) {
				Settings.TableMainColWidth[7] = ManageInt(60, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("TABLEMAINCOLWIDTH09")) {
				Settings.TableMainColWidth[8] = ManageInt(60, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("TABLEMAINCOLWIDTH10")) {
				Settings.TableMainColWidth[9] = ManageInt(60, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("TABLEMAINCOLWIDTH11")) {
				Settings.TableMainColWidth[10] = ManageInt(60, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("TABLEMAINCOLWIDTH12")) {
				Settings.TableMainColWidth[11] = ManageInt(60, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("TABLEMAINCOLWIDTH13")) {
				Settings.TableMainColWidth[12] = ManageInt(60, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("TABLEMAINCOLWIDTH14")) {
				Settings.TableMainColWidth[13] = ManageInt(60, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("TABLEMAINCOLWIDTH15")) {
				Settings.TableMainColWidth[14] = ManageInt(60, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("TABLEMAINCOLWIDTH16")) {
				Settings.TableMainColWidth[15] = ManageInt(60, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("UNIT")) {
				Settings.Unit = ManageInt(0, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("ISPACE")) {
				Settings.isPace = ManageBoolean(false, ERR_READ_BOOL);
			} else if (qName.equalsIgnoreCase("CHECK4UPDATEATSTART")) {
				Settings.Check4UpdateAtStart = ManageBoolean(false, ERR_READ_BOOL);
			} else if (qName.equalsIgnoreCase("LANGUAGE")) {
				Settings.Language = ManageString().trim();
			} else if (qName.equalsIgnoreCase("MAINWINDOWSWIDTH")) {
				Settings.MainWindowWidth = ManageInt(0, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("MAINWINDOWSHEIGHT")) {
				Settings.MainWindowHeight = ManageInt(0, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("VERTSPLITPOSITION")) {
				Settings.VertSplitPosition = ManageInt(0, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("HORIZSPLITPOSITION")) {
				Settings.HorizSplitPosition = ManageInt(50, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("MRBSPLITPOSITION")) {
				Settings.MRB_SplitPosition = ManageInt(220, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("MAP")) {
				Settings.map = ManageInt(0, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("DISTNEAR")) {
				Settings.DistNear = ManageDouble(100.0, ERR_READ_DOUBLE);
			} else if (qName.equalsIgnoreCase("DISTFAR")) {
				Settings.DistFar = ManageDouble(1000.0, ERR_READ_DOUBLE);
			} else if (qName.equalsIgnoreCase("POSFILTERASKTHRESHOLD")) {
				Settings.PosFilterAskThreshold = ManageInt(5, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("DEFAULTFONTNAME")) {
				Settings.DefaultFontName = ManageString().trim();
			} else if (qName.equalsIgnoreCase("DEFAULTFONTSTYLE")) {
				Settings.DefaultFontStyle = ManageInt(0, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("DEFAULTFONTSIZE")) {
				Settings.DefaultFontSize = ManageInt(14, ERR_READ_INT);

			} else if (qName.equalsIgnoreCase("STATUSBARICONSIZE")) {
				Settings.StatusbarIconSize = ManageInt(22, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("TABICONSIZE")) {
				Settings.TabIconSize = ManageInt(20, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("TOOLBARICONSIZE")) {
				Settings.ToolbarIconSize = ManageInt(20, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("MAPTOOLBARICONSIZE")) {
				Settings.MapToolbarIconSize = ManageInt(20, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("MENUICONSIZE")) {
				Settings.MenuIconSize = ManageInt(20, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("TAGICONSIZE")) {
				Settings.TagIconSize = ManageInt(16, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("DIALOGICONSIZE")) {
				Settings.DialogIconSize = ManageInt(20, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("MAPICONSIZE")) {
				Settings.MapIconSize = ManageInt(20, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("CURVEBUTTONSICONSIZE")) {
				Settings.CurveButtonsIconSize = ManageInt(32, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("THUNDERFORESTAPIKEY")) {
				Settings.setThunderForestApiKey(ManageString().trim());
			} else if (qName.equalsIgnoreCase("NOAATOKEN")) {
				Settings.setNoaaToken(ManageString().trim());
			} else if (qName.equalsIgnoreCase("COLORDIFFVERYEASY")) {
				Settings.Color_Diff_VeryEasy = ManageColor(CgConst.CL_DIFF_VERYEASY, ERR_READ_COLOR);
			} else if (qName.equalsIgnoreCase("COLORDIFFEASY")) {
				Settings.Color_Diff_Easy = ManageColor(CgConst.CL_DIFF_EASY, ERR_READ_COLOR);
			} else if (qName.equalsIgnoreCase("COLORDIFFAVERAGE")) {
				Settings.Color_Diff_Average = ManageColor(CgConst.CL_DIFF_AVERAGE, ERR_READ_COLOR);
			} else if (qName.equalsIgnoreCase("COLORDIFFHARD")) {
				Settings.Color_Diff_Hard = ManageColor(CgConst.CL_DIFF_HARD, ERR_READ_COLOR);
			} else if (qName.equalsIgnoreCase("COLORDIFFVERYHARD")) {
				Settings.Color_Diff_VeryHard = ManageColor(CgConst.CL_DIFF_VERYHARD, ERR_READ_COLOR);
			} else if (qName.equalsIgnoreCase("COLORMAPNIGHTHIGHLIGHT")) {
				Settings.Color_Map_NightHighlight = ManageColor(CgConst.CL_MAP_NIGHT_HIGHLIGHT, ERR_READ_COLOR);
			} else if (qName.equalsIgnoreCase("NORMALTRACKWIDTH")) {
				Settings.NormalTrackWidth = ManageInt(CgConst.TRACK_NORMAL_TICKNESS, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("NIGHTTRACKWIDTH")) {
				Settings.NightTrackWidth = ManageInt(CgConst.TRACK_NIGHT_TICKNESS, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("NORMALTRACKTRANSPARENCY")) {
				Settings.NormalTrackTransparency = ManageInt(CgConst.NORMAL_TRACK_TRANSPARENCY, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("MAPTOOLBARLAYOUT")) {
				Settings.MapToolBarLayout = ManageString();
			} else if (qName.equalsIgnoreCase("MAPTOOLBARORIENTATION")) {
				Settings.MapToolBarOrientation = ManageInt(javax.swing.SwingConstants.VERTICAL, ERR_READ_INT);
			}

			else if (qName.equalsIgnoreCase("CONFIG")) {
				level--;
			}
			characters = "";
		}
	}


	@Override
	public void characters(char[] chars, int start, int end) throws SAXException {
		characters = new String(chars, start, end);
	}

}
