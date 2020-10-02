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

package course_generator.param;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxParamHandler extends DefaultHandler {
	private ParamData paramdata;
	private String characters = "";
	private int level = 0;
	private final int LEVEL_PROJECT = 1;
	private final int LEVEL_PARAM = 2;
	private final int LEVEL_ITEM = 3;
	private int mode = 0;
	private int errcode = 0;
	private int errline = 0;
	private final int ERR_READ_NO = 0;
	private final int ERR_READ_DOUBLE = -1;
	private final int ERR_READ_NOTEXIST = -6;
	private double slope = 0.0;
	private String speed = "0.0";

	private Locator locator;

	/**
	 * Read the CGX file from disc
	 * 
	 * @param filename Name of the cgx file to read
	 * @return The error code Erroce explanation: ERR_READ_NO = No problem during
	 *         the reading of the file ERR_READ_DOUBLE = Parsing error during the
	 *         read of a double element ERR_READ_INT = Parsing error during the read
	 *         of a integer element ERR_READ_BOOL = Parsing error during the read of
	 *         a boolean element ERR_READ_TIME = Parsing error during the read of a
	 *         time element ERR_READ_VERSION = Parsing error during the read of a
	 *         version of the GPX file. Must be 1.1 ERR_READ_NOTEXIST = The file
	 *         doesn't exist or can't by read ERR_READ_COLOR = Parsing error during
	 *         the read of a color element
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public int readDataFromParam(String filename, ParamData pdata)
			throws SAXException, IOException, ParserConfigurationException {
		paramdata = pdata;

		level = 0;
		errcode = ERR_READ_NO;

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();

		File f = new File(filename);
		if (f.isFile() && f.canRead()) {
			if (mode == 0)
				paramdata.data.clear();
			parser.parse(f, this);
		} else
			errcode = ERR_READ_NOTEXIST;

		return errcode;
	}

	public int getErrLine() {
		return errline;
	}

	@Override
	public void setDocumentLocator(final Locator locator) {
		this.locator = locator; // Save the locator, so that it can be used later for line tracking when
								// traversing nodes.
	}

	@Override
	public void startElement(String uri, String localname, String qName, Attributes attributs) throws SAXException {
		if (qName.equalsIgnoreCase("PROJECT")) {
			level++;
		} else if (qName.equalsIgnoreCase("PARAM")) {
			// trk_nb++;
			level++;
		} else if (qName.equalsIgnoreCase("ITEM")) {
			// trk_nb++;
			level++;
		}
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
	 * @param _default Default value
	 * @param _errcode Error code if a parse error occure
	 * @return Return the parsed value
	 */
	private double ManageDouble(double _default, int _errcode) {
		try {
			return Double.parseDouble(characters);
		} catch (NumberFormatException e) {
			errcode = _errcode;
			errline = locator.getLineNumber();
			return _default;
		}
	}

	/**
	 * Parse a integer element
	 * 
	 * @param _default Default value
	 * @param _errcode Error code if a parse error occure
	 * @return Return the parsed value
	 */
	/*
	 * private int ManageInt(int _default, int _errcode) { try { return
	 * Integer.parseInt(characters); } catch (NumberFormatException e) { errcode =
	 * _errcode; errline = locator.getLineNumber(); return _default; } }
	 */

	/**
	 * Parse a boolean element
	 * 
	 * @param _default Default value
	 * @param _errcode Error code if a parse error occur
	 * @return Return the parsed value
	 */
	/*
	 * private boolean ManageBoolean(boolean _default, int _errcode) { try { return
	 * Boolean.parseBoolean(characters); } catch (NumberFormatException e) { errcode
	 * = _errcode; errline = locator.getLineNumber(); return _default; } }
	 */

	@Override
	public void endElement(String uri, String localname, String qName) throws SAXException {
		if (level == LEVEL_PROJECT) {
			if (qName.equalsIgnoreCase("NAME")) {
				paramdata.name = ManageString();
			} else if (qName.equalsIgnoreCase("COMMENT")) {
				paramdata.comment = ManageString();
			} else if (qName.equalsIgnoreCase("PROJECT")) {
				level--;
			}
		}

		if (level == LEVEL_PARAM) {
			if (qName.equalsIgnoreCase("PARAM"))
				level--;
		}

		if (level == LEVEL_ITEM) {
			if (qName.equalsIgnoreCase("SLOPE")) {
				slope = ManageDouble(0.0, ERR_READ_DOUBLE);
			} else if (qName.equalsIgnoreCase("SPEED")) {
				speed = characters;
			} else if (qName.equalsIgnoreCase("ITEM")) {
				level--;
				paramdata.data.add(new CgParam(slope, speed));
			}
		} // End LEVEL_ITEM

	}

	@Override
	public void characters(char[] chars, int start, int end) throws SAXException {
		characters = new String(chars, start, end);
	}

}
