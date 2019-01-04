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
public class SaxThemeHandler extends DefaultHandler {

	private String characters = "";
	private int level = 0;
	private final int LEVEL_THEME = 1;

	private int errline = 0;
	private Locator locator;

	private final int ERR_READ_NO = 0;
	private final int ERR_READ_DOUBLE = -1;
	private final int ERR_READ_INT = -2;
	private final int ERR_READ_BOOL = -3;
	private final int ERR_READ_NOTEXIST = -6;
	private final int ERR_READ_COLOR = -7;
	private ColorTheme colors;
	private int ReadError = ERR_READ_NO;

 	
	/**
	 * Read the Theme file from disc
	 * 
	 * @param filename
	 *            Name of the theme file to read
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
	public int readDataFromTheme(String filename, ColorTheme colors)
			throws SAXException, IOException, ParserConfigurationException {

		this.colors = colors;
		characters = "";
		level = 0;
		ReadError = ERR_READ_NO;
		errline = 0;

		// try {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();

		File f = new File(filename);
		if (f.isFile() && f.canRead()) {
			// -- ok let's go

			// -- Parse the file
			parser.parse(f, this);
		} else
			ReadError = ERR_READ_NOTEXIST;
		// }
		// catch (SAXException | IOException | ParserConfigurationException e) {
		// Settings.ReadError=ERR_READ_NOTEXIST;
		// }

		return ReadError;
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
			ReadError = _errcode;
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
			ReadError = _errcode;
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
			ReadError = _errcode;
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
		if (qName.equalsIgnoreCase("THEME")) {
			level++;
		}
	}


	@Override
	public void endElement(String uri, String localname, String qName) throws SAXException {
		if (level == LEVEL_THEME) {
			if (qName.equalsIgnoreCase("COLORDIFFVERYEASY")) {
				colors.ColorVeryEasy = ManageColor(CgConst.CL_DIFF_VERYEASY, ERR_READ_COLOR);
			} else if (qName.equalsIgnoreCase("COLORDIFFEASY")) {
				colors.ColorEasy = ManageColor(CgConst.CL_DIFF_EASY, ERR_READ_COLOR);
			} else if (qName.equalsIgnoreCase("COLORDIFFAVERAGE")) {
				colors.ColorAverage = ManageColor(CgConst.CL_DIFF_AVERAGE, ERR_READ_COLOR);
			} else if (qName.equalsIgnoreCase("COLORDIFFHARD")) {
				colors.ColorHard = ManageColor(CgConst.CL_DIFF_HARD, ERR_READ_COLOR);
			} else if (qName.equalsIgnoreCase("COLORDIFFVERYHARD")) {
				colors.ColorVeryHard = ManageColor(CgConst.CL_DIFF_VERYHARD, ERR_READ_COLOR);
			} else if (qName.equalsIgnoreCase("COLORNIGHT")) {
				colors.ColorNight = ManageColor(CgConst.CL_MAP_NIGHT_HIGHLIGHT, ERR_READ_COLOR);
			} else if (qName.equalsIgnoreCase("NORMALTRACKWIDTH")) {
				colors.NormalTrackWidth = ManageInt(CgConst.TRACK_NORMAL_TICKNESS, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("NIGHTTRACKWIDTH")) {
				colors.NightTrackWidth = ManageInt(CgConst.TRACK_NIGHT_TICKNESS, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("NORMALTRACKTRANSPARENCY")) {
				colors.NormalTrackTransparency = ManageInt(CgConst.NORMAL_TRACK_TRANSPARENCY, ERR_READ_INT);
			} else if (qName.equalsIgnoreCase("NIGHTTRACKTRANSPARENCY")) {
				colors.NightTrackTransparency = ManageInt(CgConst.NIGHT_TRACK_TRANSPARENCY, ERR_READ_INT);
			}
			
			else if (qName.equalsIgnoreCase("THEME")) {
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
