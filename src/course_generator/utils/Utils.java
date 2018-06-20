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
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import javax.swing.ImageIcon;
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

	public static final String htmlDocFile = "cg_doc_4.00.html";


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
	 * Return the icon in the resource library
	 * 
	 * @param name
	 *            name of the icon (ie "distance.png")
	 * @param size
	 *            size of the icon (16,24,32,48,64,96,128)
	 * @return
	 */
	public static ImageIcon getIcon(Component Parent, String name, int size) {
		return new javax.swing.ImageIcon(
				Parent.getClass().getResource("/course_generator/images/" + size + "/" + name));
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
	 * Convert °C to °F
	 * 
	 * @param c
	 *            Temperature in °C
	 * @return Temperature in °F
	 */
	public static double C2F(double c) {
		return c * 9 / 5 + 32;
	}


	/**
	 * Convert °F to °C
	 * 
	 * @param f
	 *            Temperature in °F
	 * @return Temperature in °C
	 */
	public static double F2C(double f) {
		return (f - 32) * 5 / 9;
	}


	/**
	 * Return the speed unit as string (km/h, miles/h, min/km or min/mile)
	 * 
	 * @param unit
	 *            Unit
	 * @param pace
	 *            if "true" the speed type is pace (min/km or min/mile) otherwise
	 *            it's a speed (km/h or miles/h)
	 * @return String with the unit
	 */
	public static String uSpeed2String(int unit, boolean pace) {
		if (!pace) {
			switch (unit) {
			case CgConst.UNIT_METER:
				return "km/h";
			case CgConst.UNIT_MILES_FEET:
				return "miles/h";
			default:
				return "km/h";
			}
		} else {
			switch (unit) {
			case CgConst.UNIT_METER:
				return "min/km";
			case CgConst.UNIT_MILES_FEET:
				return "min/mile";
			default:
				return "min/km";
			}
		}
	}


	/**
	 * Return the (long) distance unit as string (km or miles)
	 * 
	 * @param unit
	 *            Unit
	 * @return String with the unit
	 */
	public static String uLDist2String(int unit) {
		switch (unit) {
		case CgConst.UNIT_METER:
			return "km";
		case CgConst.UNIT_MILES_FEET:
			return "miles";
		default:
			return "km";
		}
	}


	/**
	 * Return the elevation unit as string (m or feet)
	 * 
	 * @param unit
	 *            Unit
	 * @return String with the unit
	 */
	public static String uElev2String(int unit) {
		switch (unit) {
		case CgConst.UNIT_METER:
			return "m";
		case CgConst.UNIT_MILES_FEET:
			return "feet";
		default:
			return "m";
		}
	}


	/**
	 * Calculate the pace from a speed and return the result as a string
	 * 
	 * @param speed
	 *            Speed in km/h or miles/h
	 * @return pace as string in min/km or min/mile (8:30min/mile =>"8:3")
	 */
	public static String SpeedToPace(double speed) {
		if (speed == 0.0)
			return "0:0";

		double p = 60.0 / speed;
		double min = (long) p;
		double sec = (p - min) * 60;
		return String.format("%1.0f:%02.0f", min, sec);
	}


	/**
	 * Calculate the speed from a pace
	 * 
	 * @param pace
	 *            Pace in min/km or min/mile (8.30min/mile =>8.3)
	 * @return speed in km/h or miles/h
	 */
	public static double Pace2Speed(double pace) {
		if (pace == 0.0)
			return 0.0;

		double min = (long) pace;
		double sec60 = pace - min;
		double sec100 = sec60 / 0.6;

		return 60 / (min + sec100);
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

		return String.format("%02d:%02d ", nbh, nbm);
	}


	/**
	 * Calculate the distance between two GPS points (without the elevation)
	 * https://en.wikipedia.org/wiki/Great-circle_distance
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

		double kEarthRadiusKms = 6371; // 6378.14; // 6376.5

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
			writer.writeCharacters(String.format(Locale.ROOT, "%f", Data));
			writer.writeEndElement();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}


	public static void WriteBooleanToXML(XMLStreamWriter writer, String Element, boolean Data) {
		try {
			writer.writeStartElement(Element);
			writer.writeCharacters((Data ? "true" : "false"));
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
			urlConnect.getContent();

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
	 * Return the application directory path. Cross platform
	 * 
	 * @return Home directory path
	 */
	public static String GetAppDir() {
		return System.getProperty("user.dir");
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
		 * %N:Name %A:Elevation %D:Distance from the start %T:Time (hh:mm) %Ts:Time
		 * (hh:mm) %Tl:Time (hh:mm:ss) %H: Hour (ddd hh:mm) %h: Hour (hh:mm) %hs:Hour
		 * (hh:mm) %hl:Hour (hh:mm:s) %B :Time limit (hh:mm) -> Time from the start %b
		 * :Time limit (hh:mm) -> Limit hour %C :Comment %c :Comment from the main data
		 * %L :Carriage return %R :Station time (hh:mm) %Rs:Station time (hh:mm)
		 * %Rl:Station time (Duration) (hh:mm:ss) %+ :Sum ascend %- :Sum descend
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
		 * try { return fname.substring(fname.lastIndexOf(".") + 1); } catch (Exception
		 * e) { return ""; }
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


	/**
	 * Return if a directory exist
	 * 
	 * @param fname
	 *            file with the full path to test
	 * @return Return 'true' if the directory exist
	 */
	public static boolean DirExist(String fname) {
		return new File(fname).isDirectory();
	}


	/**
	 * Returns true if the given name is a valid resource name on this operating
	 * system, and false otherwise.
	 */
	public static boolean isFilenameValid(String name) {
		String INVALID_RESOURCE_CHARACTERS;
		String[] INVALID_RESOURCE_BASENAMES;
		String[] INVALID_RESOURCE_FULLNAMES;

		name = name.trim();

		OsCheck.OSType ostype = OsCheck.getOperatingSystemType();
		if (ostype == OsCheck.OSType.Windows) {
			// valid names and characters taken from
			// http://msdn.microsoft.com/library/default.asp?url=/library/en-us/fileio/fs/naming_a_file.asp
			INVALID_RESOURCE_CHARACTERS = "\\/:*\"?<>|\0";
			INVALID_RESOURCE_BASENAMES = new String[] { "aux", "com1", "com2", "com3", "com4", "com5", "com6", "com7",
					"com8", "com9", "con", "lpt1", "lpt2", "lpt3", "lpt4", "lpt5", "lpt6", "lpt7", "lpt8", "lpt9",
					"nul", "prn" };
			Arrays.sort(INVALID_RESOURCE_BASENAMES);
			// CLOCK$ may be used if an extension is provided
			INVALID_RESOURCE_FULLNAMES = new String[] { "clock$" }; //$NON-NLS-1$
		} else {
			// only front slash and null char are invalid on UNIXes
			// taken from
			// http://www.faqs.org/faqs/unix-faq/faq/part2/section-2.html
			INVALID_RESOURCE_CHARACTERS = "\\/:*\"?<>|\0";
			INVALID_RESOURCE_BASENAMES = null;
			INVALID_RESOURCE_FULLNAMES = null;
		}

		// . and .. have special meaning on all platforms
		if (name.equals(".") || name.equals("..")) //$NON-NLS-1$ //$NON-NLS-2$
			return false;

		for (int i = 0; i < INVALID_RESOURCE_CHARACTERS.length(); i++) {
			if (name.indexOf(INVALID_RESOURCE_CHARACTERS.charAt(i)) != -1)
				return false;
		}

		if (ostype == OsCheck.OSType.Windows) {
			// empty names are not valid
			final int length = name.length();
			if (length == 0)
				return false;

			final char lastChar = name.charAt(length - 1);
			// filenames ending in dot are not valid
			if (lastChar == '.')
				return false;

			// file names ending with whitespace are truncated (bug 118997)
			if (Character.isWhitespace(lastChar))
				return false;

			int dot = name.indexOf('.');
			// on windows, filename suffixes are not relevant to name
			// validity
			String basename = dot == -1 ? name : name.substring(0, dot);

			if (Arrays.binarySearch(INVALID_RESOURCE_BASENAMES, basename.toLowerCase()) >= 0)
				return false;
			return Arrays.binarySearch(INVALID_RESOURCE_FULLNAMES, name.toLowerCase()) < 0;
		}
		return true;
	}


	/**
	 * Return the size of a folder
	 * 
	 * @param directory
	 * @return Size of the directory
	 */
	public static long folderSize(File directory) {
		long length = 0;
		for (File file : directory.listFiles()) {
			if (file == null)
				return 0;

			if (file.isFile())
				length += file.length();
			else
				length += folderSize(file);
		}
		return length;
	}


	public static String humanReadableByteCount(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}


	/**
	 * Search and replace all strings in a stringbuilder object
	 * 
	 * @param sb
	 *            StringBuilder object
	 * @param from
	 *            String to search
	 * @param to
	 *            Replacement string
	 * @return StringBuilder object
	 */
	public static StringBuilder sbReplace(StringBuilder sb, String from, String to) {
		int position = sb.indexOf(from);
		while (position != -1) {
			sb.replace(position, position + from.length(), to);
			position += to.length(); // Move to the end of the replacement
			position = sb.indexOf(from, position);
		}
		return sb;
	}


	public static boolean ExportResource(Object obj, String resourceName, String dst) throws Exception {
		boolean ok = false;

		File file = new File(dst);
		if (!file.exists()) {
			InputStream link = (obj.getClass().getResourceAsStream(resourceName));
			Files.copy(link, file.getAbsoluteFile().toPath());
			ok = true;
			System.out.println(resourceName + " exported to " + dst);
		}
		return ok;

		/*
		 * InputStream stream = null; OutputStream resStreamOut = null; boolean
		 * ok=false; String jarFolder; try { stream =
		 * obj.getClass().getResourceAsStream(resourceName); if (stream == null) { throw
		 * new Exception ("Cannot get resource \"" + resourceName + "\" from jar file");
		 * } int readBytes; byte[] buffer = new byte[4096]; String
		 * s=obj.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().
		 * getPath(); System.out.println("resourceName="+resourceName);
		 * System.out.println("s="+s); //jarFolder = new
		 * File(obj.getClass().getProtectionDomain().getCodeSource().getLocation().toURI
		 * ().getPath()).getParentFile().getPath().replace('\\', '/'); jarFolder = new
		 * File(s).getParentFile().getPath().replace('\\', '/'); resStreamOut = new
		 * FileOutputStream(dst); while ((readBytes = stream.read(buffer)) >0 ) {
		 * resStreamOut.write(buffer, 0, readBytes); } ok=true; } catch (Exception ex) {
		 * throw ex; } finally { if (stream!=null) stream.close(); if
		 * (resStreamOut!=null) resStreamOut.close(); } return ok;
		 */
	}


	public static boolean OpenHelp(String language) {
		boolean success = false;
		Map<String, String> environmentVariables = System.getenv();
		String helpFolder = environmentVariables.get("CGInstallFolder");

		String helpFilePath = helpFolder + "/help/" + language + "/" + language + "_" + htmlDocFile;
		File helpFile = new File(helpFilePath);
		if (helpFile.exists() && !helpFile.isDirectory()) {
			try {
				Desktop.getDesktop().browse(helpFile.toURI());
				success = true;
			} catch (IOException ex) {
				CgLog.info("mnuCGHelp : Error when loading : " + helpFilePath + ".");
				ex.printStackTrace();
			}
		} else {
			CgLog.info("The help file '" + helpFilePath + "' was not found.");
		}
		return success;
	}

} // Class
