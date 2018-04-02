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

package course_generator.import_points;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxCGPHandler extends DefaultHandler {
	private String characters = "";
	private double tmp_lat = 0.0;
	private double tmp_lon = 0.0;
	private double tmp_ele = 0.0;
	private int tmp_tag = 0;
	private String tmp_name = "";
	private String tmp_comment = "";

	private int level = 0;
	private final int LEVEL_PTS = 2;
	private final int LEVEL_PT = 3;

	private double cgp_version = 0;
	private int errcode = 0;
	private Locator locator;
	private int errline = 0;
	private final int ERR_READ_NO = 0;
	private final int ERR_READ_DOUBLE = -1;
	private final int ERR_READ_INT = -2;
	private final int ERR_READ_VERSION = -3;
	private final int ERR_READ_NOTEXIST = -4;

	private ImportPtsData ptsdata;


	/*
	<?xml version="1.0" encoding="UTF-8"?>
	<COURSEGENERATOR VERSION="1">
		<Points>
			<Pt>
				<LatitudeDegrees>45.85291000000000</LatitudeDegrees>
				<LongitudeDegrees>6.72386000000000</LongitudeDegrees>
				<AltitudeMeters>1183.0</AltitudeMeters>
				<Comment> </Comment>
				<Name>St Nicolas</Name>
				<Tag>288</Tag>
			</Pt>
		</Points>
	</COURSEGENERATOR>
	*/

	/**
	 * Read the CGP file from disc
	 * @param filename Name of the gpx file to read
	 * @param CGPData ImportPtsData object where to store the read data
	 * @return The error code
	 *   Erroce explanation:
	 *   ERR_READ_NO = No problem during the reading of the file
	 *   ERR_READ_LAT = Parsing error during the read of a latitude (lat) element 
	 *   ERR_READ_LON = Parsing error during the read of a longitude (lon) element
	 *   ERR_READ_ELE = Parsing error during the read of a elevation (ele) element
	 *   ERR_READ_TIME = Parsing error during the read of a time (time) element
	 *   ERR_READ_VERSION = Parsing error during the read of a version of the GPX file. Must be 1.1
	 *   ERR_READ_NOTEXIST = The file doesn't exist or can't by read
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException 
	 */
	public int readDataFromCGP(String filename, ImportPtsData CGPData)
			throws SAXException, IOException, ParserConfigurationException {
		ptsdata = CGPData;
		tmp_lat = 0.0;
		tmp_lon = 0.0;
		tmp_ele = 0.0;
		tmp_tag = 0;
		tmp_name = "";
		tmp_comment = "";

		characters = "";

		level = 0;
		errcode = ERR_READ_NO;
		cgp_version = 0.0;

		ptsdata.ReadError = 0;

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();

		File f = new File(filename);
		if (f.isFile() && f.canRead()) {
			// -- ok let's go

			// -- Clear the DataList --
			ptsdata.data.clear();

			// -- Parse the file
			parser.parse(f, this);
		} else
			ptsdata.ReadError = ERR_READ_NOTEXIST;

		return ptsdata.ReadError;
	}


	public int getErrLine() {
		return errline;
	}


	/**
	 * Parse a string element
	 * @return Return the parsed value
	 */
	private String ManageString() {
		String S = characters;
		characters = "";
		return S;
	}


	/**
	 * Parse a double element
	 * @param _default Default value
	 * @param _errcode Error code if a parse error occur
	 * @return Return the parsed value
	 */
	private double ManageDouble(double _default, int _errcode) {
		try {
			Double d = Double.parseDouble(characters);
			characters = "";
			return d;
		} catch (NumberFormatException e) {
			errcode = _errcode;
			errline = locator.getLineNumber();
			characters = "";
			return _default;
		}
	}


	/**
	 * Parse a integer element
	 * @param _default Default value
	 * @param _errcode Error code if a parse error occur
	 * @return Return the parsed value
	 */
	private int ManageInt(int _default, int _errcode) {
		try {
			int i = Integer.parseInt(characters);
			characters = "";
			return i;
		} catch (NumberFormatException e) {
			errcode = _errcode;
			errline = locator.getLineNumber();
			characters = "";
			return _default;
		}
	}


	@Override
	public void setDocumentLocator(final Locator locator) {
		this.locator = locator; // Save the locator, so that it can be used
								// later for line tracking when traversing
								// nodes.
	}


	@Override
	public void startElement(String uri, String localname, String qName, Attributes attributs) throws SAXException {
		if (qName.equalsIgnoreCase("COURSEGENERATOR")) {
			level++;
			// Check all the attributes
			for (int index = 0; index < attributs.getLength(); index++) {
				if (attributs.getLocalName(index).equalsIgnoreCase("VERSION")) {
					try {
						cgp_version = Double.parseDouble(attributs.getValue(index));
					} catch (NumberFormatException e) {
						cgp_version = 0.0;
					}
					if (cgp_version != 1.0)
						errcode = ERR_READ_VERSION;
				}
			}
		} else if (qName.equalsIgnoreCase("POINTS")) {
			level++;
		} else if (qName.equalsIgnoreCase("PT")) {
			level++;
		}
	}


	@Override
	public void endElement(String uri, String localname, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("COURSEGENERATOR")) {
			level--;
		} else if (qName.equalsIgnoreCase("POINTS") && (level==LEVEL_PTS)) {
			level--;
		} else if (qName.equalsIgnoreCase("PT") && (level==LEVEL_PT)) {
			level--;
			// Add data at the of the array
			ptsdata.data.add(new CgImportPts(tmp_lat, tmp_lon, tmp_ele, tmp_tag, tmp_name, tmp_comment));
		}
		if (level == LEVEL_PT) {
			if (qName.equalsIgnoreCase("LATITUDEDEGREES")) {
				tmp_lat = ManageDouble(0.0, ERR_READ_DOUBLE);
			} else if (qName.equalsIgnoreCase("LONGITUDEDEGREES")) {
				tmp_lon = ManageDouble(0.0, ERR_READ_DOUBLE);
			} else if (qName.equalsIgnoreCase("ALTITUDEMETERS")) {
				tmp_ele = ManageDouble(0.0, ERR_READ_DOUBLE);
			} else if (qName.equalsIgnoreCase("COMMENT")) {
				tmp_comment = ManageString();
			} else if (qName.equalsIgnoreCase("NAME")) {
				tmp_name = ManageString();
			} else if (qName.equalsIgnoreCase("TAG")) {
				tmp_tag = ManageInt(0, ERR_READ_INT);
			}
		}

	}


	@Override
	public void characters(char[] chars, int start, int end) throws SAXException {
		characters = new String(chars, start, end);
	}

}
