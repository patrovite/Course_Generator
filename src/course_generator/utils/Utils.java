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

package course_generator.utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

//import org.jdom2.Element;
import org.joda.time.DateTime;

import course_generator.CgData;
import course_generator.TrackData;
import course_generator.TrackData.CalcClimbResult;
import course_generator.settings.CgSettings;

/**
 *
 * @author pierre.delore
 */
public class Utils {

	/**
	 * Display a load dialog
	 * 
	 * @param Parent
	 *            Parent windows
	 * @param Directory
	 *            Directory to select when the dialog is displayed
	 * @param Extension
	 *            File extention (ie: ".myp")
	 * @param FilterText
	 *            Text filter
	 * @return Filename with path. Empty if cancel
	 */
	public static String LoadDialog(Component Parent, String Directory, String Extension, String FilterText) {

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(Directory));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		FileFilter mypFilter = new FileTypeFilter(Extension, FilterText);
		fileChooser.addChoosableFileFilter(mypFilter);
		fileChooser.setFileFilter(mypFilter);

		int result = fileChooser.showOpenDialog(Parent);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			return selectedFile.getAbsolutePath();
		} else
			return "";
	}


	/**
	 * Display a save dialog
	 * 
	 * @param Parent
	 *            Parent windows
	 * @param Directory
	 *            Directory to select when the dialog is displayed
	 * @param DefaultFileName
	 *            Default filename. Empty string if none
	 * @param Extension
	 *            File extention (ie: ".myp")
	 * @param FilterText
	 *            Text filter (ie: "File GPX (*.gpx)|*.gpx")
	 * @param TestFileExist
	 *            Test the file exist
	 * @param FileExistText
	 *            Text displayed if the selected file exist (Over write
	 *            confirmation)
	 * @return Filename with path. Empty if cancel
	 */
	public static String SaveDialog(Component Parent, String Directory, String DefaultFileName, String Extension,
			String FilterText, Boolean TestFileExist, String FileExistText) {
		JFileChooser fileChooser = new JFileChooser();

		if (DefaultFileName.isEmpty())
			fileChooser.setCurrentDirectory(new File(Directory));
		else
			fileChooser.setSelectedFile(new File(DefaultFileName));

		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);

		FileFilter mypFilter = new FileTypeFilter(Extension, FilterText);
		fileChooser.addChoosableFileFilter(mypFilter);
		fileChooser.setFileFilter(mypFilter);

		int result = fileChooser.showSaveDialog(Parent);
		if (result == JFileChooser.APPROVE_OPTION) {
			String selectedFile = fileChooser.getSelectedFile().getAbsolutePath();
			if (Utils.GetFileExtension(selectedFile).isEmpty()) {
				selectedFile = selectedFile + Extension;
			}
			boolean ok = true;
			if (Utils.FileExist(selectedFile) && (TestFileExist)) {
				if (JOptionPane.showConfirmDialog(Parent, FileExistText, "",
						JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
					ok = false;
			}
			if (ok)
				return selectedFile;
			else
				return "";
		} else
			return "";
	}


	/**
	 * Parse a string containing a double. The separator can be "." or ","
	 * 
	 * @param s
	 *            Input string containing the double
	 * @return Parsed value in double format
	 * @throws ParseException
	 */
	public static double ParseDoubleEx(String s, double value_if_fault) { // throws
																			// ParseException{
		int pos, step;
		double v;
		String str, s1;
		boolean ok, error;

		char decimalseparator = new DecimalFormat().getDecimalFormatSymbols().getDecimalSeparator();

		v = 0.0;
		str = "";

		// -- Input string empty
		s1 = s.trim();
		if (s1.isEmpty()) {
			return value_if_fault; // Return the fault value
		}

		// -- Let go
		ok = true;
		error = false;
		step = 0;
		pos = 0;
		while ((pos < s1.length()) && (ok) && (!error)) {
			switch (step) {
				case 0: { // '+' ou '-'
					if (s1.charAt(pos) == '+') {
						str = str + s1.charAt(pos);
						pos++;
					} else if (s1.charAt(pos) == '-') {
						str = str + s1.charAt(pos);
						pos++;
					}
					step = 1;
					break;
				} // Case 0

				case 1: { // Digit?
					if (Character.isDigit(s1.charAt(pos))) {
						str = str + s1.charAt(pos);
						pos++;
					} else { // Separator?
						if ((s1.charAt(pos) == ',') || (s1.charAt(pos) == '.')) {
							str = str + decimalseparator;
							pos++;
							step = 2;
						} else {
							error = true;
							// step = 10;
						}
					}
					break;
				} // Case 1

				case 2: { // Digit?
					if (Character.isDigit(s1.charAt(pos))) {
						str = str + s1.charAt(pos);
						pos++;
					} else {
						error = true;
					}
					break;
				} // Case 2
					// case 10: { //Error
					// error=true;
					// ok=false;
					// break;
					// }

			} // Case
		} // While

		if (error)
			v = value_if_fault;
		else {
			// -- Convert the string to double
			try {
				DecimalFormat decimalFormat = new DecimalFormat("#");
				v = decimalFormat.parse(str).doubleValue();
			} catch (ParseException e) {
				// A problem! set the "fault" value
				v = value_if_fault;
			}
		}

		return v;
	}


	/**
	 * Define the parameter for the gridbaglayout constraints
	 * 
	 * @param container
	 *            Container where to place the component
	 * @param component
	 *            Component to place
	 * @param xPos
	 *            X position in the grid
	 * @param yPos
	 *            Y position in the grid
	 * @param compWidth
	 *            Component width
	 * @param compHeight
	 *            Component height
	 * @param weightX
	 *            X weight (1=can be resized in X)
	 * @param weightY
	 *            Y weight (1=can be resized in Y)
	 * @param insetTop
	 *            Inner top space in pixel
	 * @param insetLeft
	 *            Inner left space in pixel
	 * @param insetBottom
	 *            Inner bottom space in pixel
	 * @param insetRight
	 *            Inner right space in pixel
	 * @param anchor
	 *            Position of the component in the "cell"
	 * @param stretch
	 *            How the component will fill the "cell"
	 */
	public static void addComponent(Container container, JComponent component, int xPos, int yPos, int width,
			int height, double weightX, double weightY, int insetTop, int insetLeft, int insetBottom, int insetRight,
			int anchor, int stretch) {

		GridBagConstraints gridConstraints = new GridBagConstraints();

		gridConstraints.gridx = xPos;
		gridConstraints.gridy = yPos;
		gridConstraints.gridwidth = width;
		gridConstraints.gridheight = height;
		gridConstraints.weightx = weightX;
		gridConstraints.weighty = weightY;
		gridConstraints.insets = new Insets(insetTop, insetLeft, insetBottom, insetRight);
		gridConstraints.anchor = anchor;
		gridConstraints.fill = stretch;

		container.add(component, gridConstraints);

	}


	/**
	 * Convert meters in feet
	 * 
	 * @param m
	 *            meters to convert
	 * @return Converted value
	 */

	public static double Meter2Feet(double m) {
		return m * 3.28083989501;
	}


	/**
	 * Convert feet in meter
	 * 
	 * @param m
	 *            feet to convert
	 * @return Converted value
	 */
	public static double Feet2Meter(double f) {
		return f * 0.3048;
	}


	/**
	 * Convert meters in 1/1000 miles
	 * 
	 * @param m
	 *            meters to convert
	 * @return Converted value
	 */
	public static double Meter2uMiles(double m) {
		return m * 0.62137119223;
	}


	/**
	 * Convert miles in meters
	 * 
	 * @param m
	 *            miles to convert
	 * @return Converted value
	 */
	public static double Miles2Meter(double m) {
		return m * 1.609344 * 1000;
	}


	/**
	 * Convert kilometers in miles
	 * 
	 * @param m
	 *            kilometers to convert
	 * @return Converted value
	 */
	public static double Km2Miles(double m) {
		return m * 0.62137119223;
	}


	/**
	 * Convert miles in kilometer
	 * 
	 * @param m
	 *            Value in miles to convert
	 * @return Converted value
	 */
	public static double Miles2Km(double m) {
		return m * 1.609344;
	}


	/**
	 * Convert the seconds in string. Format hh:mm:ss
	 * 
	 * @param v
	 *            Number of second to convert
	 * @return Result string
	 */
	public static String Second2DateString(int v) {
		int nbh = v / 3600;
		int nbm = (v % 3600) / 60;
		int nbs = (v % 3600) % 60;

		return String.format("%02d:%02d:%02d ", nbh, nbm, nbs);
	}


	/**
	 * Convert the seconds in string. Format hh:mm
	 * 
	 * @param v
	 *            Number of second to convert
	 * @return Result string
	 */
	public static String Second2DateString_HM(int v) {
		int nbh = v / 3600;
		int nbm = (v % 3600) / 60;

		return String.format("%02d:%02d:%02d ", nbh, nbm);
	}


	/**
	 * Calculate the distance between two GPS points (without the elevation)
	 * 
	 * @param lat1
	 *            Latitude of the first point
	 * @param lon1
	 *            Longitude of the first point
	 * @param lat2
	 *            Latitude of the second point
	 * @param lon2
	 *            Longitude of the second point
	 * @return Distance in meter
	 */
	public static double CalcDistance(double lat1, double lon1, double lat2, double lon2) {
		double a, c, dDistance, dLat1InRad, dLong1InRad, dLat2InRad, dLong2InRad, dLongitude, dLatitude;
		double kEarthRadiusKms;

		kEarthRadiusKms = 6378.14; // 6376.5

		dDistance = 0; // Double.MinValue
		dLat1InRad = lat1 * (Math.PI / 180.0);
		dLong1InRad = lon1 * (Math.PI / 180.0);
		dLat2InRad = lat2 * (Math.PI / 180.0);
		dLong2InRad = lon2 * (Math.PI / 180.0);

		dLongitude = dLong2InRad - dLong1InRad;
		dLatitude = dLat2InRad - dLat1InRad;

		// Intermediate result a.
		a = Math.pow(Math.sin(dLatitude / 2.0), 2.0)
				+ Math.cos(dLat1InRad) * Math.cos(dLat2InRad) * Math.pow(Math.sin(dLongitude / 2.0), 2.0);

		// Intermediate result c (great circle distance in Radians)
		c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));

		// Distance.
		dDistance = kEarthRadiusKms * c;

		// Result in meter
		return dDistance * 1000.0;
	}

	public static class CalcLineResult {
		public double a, b;
		/*
		 * public CalcLineResult() { a=b=0.0; }
		 */
	}


	// -- Calculate 'a' and 'b' from Y=aX+b --
	/**
	 * Calculate 'a' and 'b' from Y=aX+b
	 * 
	 * @param x1
	 *            X value of the first point
	 * @param y1
	 *            Y value of the first point
	 * @param x2
	 *            X value of the second point
	 * @param y2
	 *            Y value of the second point
	 * @param r
	 *            Result object
	 * @return Result object
	 */
	public static CalcLineResult CalcLine(double x1, double y1, double x2, double y2, CalcLineResult r) {
		r.a = (y1 - y2) / (x1 - x2);
		r.b = ((y1 * x2) - (y2 * x1)) / (x2 - x1);
		return r;
	}


	/**
	 * Compare two DateTime
	 * 
	 * @param t1
	 *            First DateTime
	 * @param t2
	 *            Second DateTime
	 * @return Return 0 if t1=t2 Return 1 if t1>t2 Return -1 if t1<t2
	 */
	public static int CompareHMS(DateTime t1, DateTime t2) {
		int h1, h2, m1, m2, s1, s2;

		h1 = t1.getHourOfDay();
		m1 = t1.getMinuteOfHour();
		s1 = t1.getSecondOfMinute();

		h2 = t2.getHourOfDay();
		m2 = t2.getMinuteOfHour();
		s2 = t2.getSecondOfMinute();

		if ((h1 == h2) && (m1 == m2) && (s1 == s2))
			return 0;
		if ((h1 > h2) || ((h1 == h2) && (m1 > m2)) || ((h1 == h2) && (m1 == m2) && (s1 > s2)))
			return 1;
		return -1;
	}


	public static Dimension StringDimension(Graphics g, String text) {
		// get metrics from the graphics
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		// get the height of a line of text in this
		// font and render context
		int hgt = metrics.getHeight();
		// get the advance of my text in this font
		// and render context
		int adv = metrics.stringWidth(text);
		// calculate the size of a box to hold the
		// text
		return new Dimension(adv, hgt);
	}


	public static void WriteStringToXML(XMLStreamWriter writer, String Element, String Data) {
		try {
			writer.writeStartElement(Element);
			writer.writeCharacters(Data);
			writer.writeEndElement();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}


	public static void WriteIntToXML(XMLStreamWriter writer, String Element, int Data) {
		try {
			writer.writeStartElement(Element);
			writer.writeCharacters(Integer.toString(Data));
			writer.writeEndElement();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}


	public static void WriteDoubleToXML(XMLStreamWriter writer, String Element, double Data) {
		try {
			writer.writeStartElement(Element);
			writer.writeCharacters(Double.toString(Data));
			writer.writeEndElement();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}


	public static void WriteBooleanToXML(XMLStreamWriter writer, String Element, boolean Data) {
		try {
			writer.writeStartElement(Element);
			writer.writeCharacters((Data ? "1" : "0"));
			writer.writeEndElement();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}


	/**
	 * checks for connection to the internet through dummy request
	 * 
	 * @return
	 */
	public static boolean isInternetReachable() {
		try {
			// make a URL to a known source
			URL url = new URL("http://www.google.com");

			// open a connection to that source
			HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();

			// trying to retrieve data from the source. If there
			// is no connection, this line will fail
			Object objData = urlConnect.getContent();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return false;
		}
		return true;
	}


	/**
	 * Return the home directory path. Cross platform
	 * 
	 * @return Home directory path
	 */
	public static String GetHomeDir() {
		// return Environment.GetEnvironmentVariable("USERPROFILE");
		return System.getProperty("user.home");
	}


	// TODO Test if cross platform!
	public static String GetTempDir() {
		return System.getProperty("java.io.tmpdir");
	}


	/**
	 * Test if a bit of a word is true
	 * 
	 * @param Value
	 *            Value of the word to test
	 * @param Mask
	 *            Mask of the bit
	 * @return New value of the word
	 */
	public static boolean IsBitOn(int Value, byte Bit) {
		return (Value >> Bit & 1) == 1;
	}


	/**
	 * Reset a bit of a word
	 * 
	 * @param Value
	 *            Value of the word to modify
	 * @param Mask
	 *            Mask of the bit
	 * @return New value of the word
	 */
	public static int Reset(int Value, int Mask) {
		return Value & ~Mask;
	}


	/**
	 * Set a bit of a word
	 * 
	 * @param Value
	 *            Value of the word to modify
	 * @param Mask
	 *            Mask of the bit
	 * @return New value of the word
	 */
	public static int Set(int Value, int Mask) {
		return Value | Mask;
	}


	/**
	 * Word wraps the given text to fit within the specified width.
	 * 
	 * @param s
	 *            Text to be word wrapped
	 * @param l
	 *            Width, in characters, to which the text should be word wrapped
	 * @param trim
	 *            true=The text is trimmed -- false=no modification
	 * @return The modified text
	 */
	public static String WordWrap(String s, int l, boolean trim) {
		String sr = "";
		String st = "";

		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != '\n') {
				st = st + s.charAt(i);
			} else {
				sr = sr + WordWrapOneLine(st, l, true) + '\n';
				st = "";
			}
		}
		if (st.length() > 0) {
			sr = sr + WordWrapOneLine(st, l, true);
		}
		return sr;
	}


	public static String WordWrapOneLine(String text, int width, boolean trim) {
		int ps = 0;
		int pe = 0;
		int pt = 0;
		String r = "";
		int step = 0;

		// Trim the spaces
		if (trim) {
			text = text.trim();
		}

		while (step != 100) {
			switch (step) {
				// Add the size
				case 0: {
					if (ps + width < text.length()) {
						pe = ps + width;
						step = 1;
					} else {
						pe = text.length() - 1;
						step = 3;
					}
					break;
				}

				// Search the first space
				case 1: {
					pt = pe;
					while ((text.charAt(pe) != ' ') && (pe > 0) && (pe > ps)) {
						pe--;
					}
					if ((pe == ps) || (pe == 0)) {
						// Search before the space because size overflow
						pe = pt;
						step = 5;
					} else {
						step = 2;
					}
					break;
				}

				// Backward search of the first "none-space"
				case 2: {
					pt = pe;
					while ((text.charAt(pe) == ' ') && (pe > 0) && (pe > ps)) {
						pe--;
					}
					if ((pe == ps) || (pe == 0)) {
						// Forward search of the first search
						pe = pt;
						step = 99;
					} else {
						step = 3;
					}
					break;
				}

				// Copy of ps..pe to the result string
				case 3: {
					for (int i = ps; i <= pe; i++) {
						r = r + text.charAt(i);
					}
					pe++;
					if (pe >= text.length()) {
						step = 99;
					} else {
						r = r + '\n';
						ps = pe;
						step = 4;
					}
					break;
				}

				// Forward search of the first "none-space"
				case 4: {
					while ((text.charAt(ps) == ' ') && (ps < text.length())) {
						ps++;
					}
					if (ps > text.length()) {
						step = 99;
					} else {
						pe = ps;
						step = 0;
					}
					break;
				}

				// Forward search of the first space because size overflow
				case 5: {
					while ((text.charAt(pe) != ' ') && (pe < text.length() - 1)) {
						pe++;
					}
					if (pe >= text.length()) {
						step = 99;
					} else {
						// pe = ps;
						step = 3;
					}
					break;
				}

				case 99: // End
				{
					step = 100;
					break;
				}

			}

		}
		return r;
	}


	public static String GenLabel(String s, CgData r, TrackData cd, CgSettings settings) {
		/*
		 * %N:Name 
		 * %A:Elevation 
		 * %D:Distance from the start 
		 * %T:Time (hh:mm)
		 * %Ts:Time (hh:mm) 
		 * %Tl:Time (hh:mm:ss) 
		 * %H: Hour (ddd hh:mm) 
		 * %h: Hour (hh:mm) 
		 * %hs:Hour (hh:mm) 
		 * %hl:Hour (hh:mm:s) 
		 * %B :Time limit (hh:mm) -> Time from the start 
		 * %b :Time limit (hh:mm) -> Limit hour 
		 * %C :Comment
		 * %c :Comment from the main data 
		 * %L :Carriage return 
		 * %R :Station time (hh:mm) 
		 * %Rs:Station time (hh:mm) 
		 * %Rl:Station time (Duration) (hh:mm:ss) 
		 * %+ :Sum ascend 
		 * %- :Sum descend
		 */
		int i = 0;
		int step = 0;
		String sr = "";
		DateTime dt;

		if (cd != null) {
			CalcClimbResult res = new CalcClimbResult();
			res = cd.CalcClimb(0, (int) (r.getNum() - 1), res);

			for (i = 0; i < s.length(); i++) {
				switch (step) {
					case 0: {
						if (s.charAt(i) == '%') {
							step = 1;
						} else {
							sr = sr + s.charAt(i);
						}
						break;
					}
					case 1: {
						switch (s.charAt(i)) {
							// %N:Name
							case 'N':
								sr = sr + r.getName();
								step = 0;
								break;

							// A:Elevation
							case 'A':
								sr = sr + String.format("%.0f", r.getElevation(settings.Unit));
								step = 0;
								break;

							// %D:Distance from the start
							case 'D':
								sr = sr + String.format("%.1f", r.getTotal(settings.Unit) / 1000.0);
								step = 0;
								break;

							// %T:Time (hh:mm)
							case 'T':
								step = 3;
								break;

							// %H:Hour (ddd hh:mm)
							case 'H':
								sr = sr + r.getHour().toString("E HH:mm");
								step = 0;
								break;

							// %h:Time (hh:mm)
							case 'h':
								step = 2;
								break;

							// %R:Station time (hh:mm:ss)
							case 'R':
								step = 4;
								break;

							// %B:Time limit (hh:mm) -> Time from the start
							case 'B':
								sr = sr + Utils.Second2DateString_HM(r.getTimeLimit());
								step = 0;
								break;

							// %b:Time limit (hh:mm) -> Limit hour
							case 'b':
								if (cd != null) {
									dt = cd.StartTime.plusSeconds(r.getTimeLimit());
									sr = sr + dt.toString("HH:mm");
								} else {
									sr = sr + "00:00";
								}
								step = 0;
								break;

							// %C:Comment from the MRB data
							case 'C':
								sr = sr + r.CommentMiniRoadbook;
								step = 0;
								break;

							// %c:Comment from the main data
							case 'c':
								sr = sr + r.getComment();
								step = 0;
								break;

							// %+: Sum ascend
							case '+':
								sr = sr + String.format("%.0f", res.cp);
								step = 0;
								break;

							// %-: Sum descend
							case '-':
								sr = sr + String.format("%.0f", res.cm);
								step = 0;
								break;

							// %L: Carriage return
							case 'L':
								sr = sr + "\n";
								step = 0;
								break;

							default:
								sr = sr + s.charAt(i);
								break;
						}
						break;
					}

					case 2: // %h
					{
						switch (s.charAt(i)) {
							case 's':
								sr = sr + r.getHour().toString("HH:mm");
								step = 0;
								break;
							case 'l':
								sr = sr + r.getHour().toString("HH:mm:ss");
								step = 0;
								break;
							default:
								sr = sr + r.getHour().toString("HH:mm");
								sr = sr + s.charAt(i);
								step = 0;
								break;
						}
						break;
					}

					case 3: // %T
					{
						switch (s.charAt(i)) {
							case 's':
								sr = sr + Utils.Second2DateString_HM(r.getTime());
								step = 0;
								break;
							case 'l':
								sr = sr + Utils.Second2DateString(r.getTime());
								step = 0;
								break;
							default:
								sr = sr + Utils.Second2DateString_HM(r.getTime());
								sr = sr + s.charAt(i);
								step = 0;
								break;
						}
						break;
					}

					case 4: // %R
					{
						switch (s.charAt(i)) {
							case 's':
								sr = sr + Utils.Second2DateString_HM(r.getStation());
								step = 0;
								break;
							case 'l':
								sr = sr + Utils.Second2DateString(r.getStation());
								step = 0;
								break;
							default:
								sr = sr + Utils.Second2DateString(r.getStation());
								sr = sr + s.charAt(i);
								step = 0;
								break;
						}
						break;
					}

				}
			}

			// Manage a command if it's the last character
			// If a command with 1 or 2 characters => Default value
			if (step == 2) {
				sr = sr + r.getHour().toString("HH:mm");
			} else if (step == 3) {
				sr = sr + Utils.Second2DateString_HM(r.getTime());
			} else if (step == 4) {
				sr = sr + Utils.Second2DateString(r.getStation());
			}

			if (cd != null) {
				return Utils.WordWrap(sr, cd.WordWrapLength, true);
			}
			// else {
			// return sr;
			// }
		}
		return sr;
	}


	public static String GetFileExtension(String fname) {
		// String name = file.getName();
		/*
		 * try { return fname.substring(fname.lastIndexOf(".") + 1); } catch
		 * (Exception e) { return ""; }
		 */
		for (int i = fname.length() - 1; i >= 0; i--) {
			if ((fname.charAt(i) == '.') && (i != fname.length() - 1)) {
				return fname.substring(i + 1);
			}
			if (fname.charAt(i) == File.separatorChar) {
				return "";
			}
		}
		return "";
	}


	/**
	 * Remove extension form the filename
	 * 
	 * @param filename
	 *            Filename without path
	 * @return filename without extension
	 */
	public static String getFileNameWithoutExtension(String filename) {
		// Handle null case specially.

		if (filename == null)
			return null;

		// Get position of last '.'.

		int pos = filename.lastIndexOf(".");

		// If there wasn't any '.' just return the string as is.

		if (pos == -1)
			return filename;

		// Otherwise return the string, up to the dot.

		return filename.substring(0, pos);
	}


	/**
	 * Return the path of a filename Cross platform??
	 *
	 * @param fname
	 *            File name where to extract the path
	 * @return String containing the path
	 */
	public static String GetDirFromFilename(String fname) {
		File f = new File(fname);
		return f.getParentFile().toString();
	}


	/**
	 * Return if a file exist
	 * 
	 * @param fname
	 *            file with the full path to test
	 * @return Return 'true' if the file exist
	 */
	public static boolean FileExist(String fname) {
		return new File(fname).isFile();
	}

} // Class
