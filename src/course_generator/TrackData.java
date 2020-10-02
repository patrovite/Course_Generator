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

package course_generator;

import static course_generator.utils.Utils.CalcDistance;

import java.awt.Color;
import java.awt.Component;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import javax.swing.JOptionPane;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.joda.time.DateTime;

import com.sun.xml.txw2.output.IndentingXMLStreamWriter;

import course_generator.param.ParamData;
import course_generator.settings.CgSettings;
import course_generator.utils.CgConst;
import course_generator.utils.CgLog;
import course_generator.utils.StatData;
import course_generator.utils.Utils;
import course_generator.utils.Utils.CalcLineResult;

/**
 * @author pierre.delore
 */
public class TrackData {

	private static TrackData instance;

	/** Slope/Speed parameters **/
	public ParamData param = null;
	/** Parameters file name **/
	public String Paramfile = "";

	/** Arraylist containing the main data **/
	public ArrayList<CgData> data;

	/** Statistics data for 'in night' **/
	public StatData tInNight;
	/** Statistics data for 'in day' **/
	public StatData tInDay;

	/** Statistics data for 'slope' **/
	public StatData[] StatSlope;
	/** Statistics data for 'elevation' **/
	public StatData[] StatElev;
	/** Statistics data for 'elevation during night' **/
	public StatData[] StatElevNight;
	/** Statistics data for 'elevation during day' **/
	public StatData[] StatElevDay;

	/** Track name **/
	public String Name; // Track name
	/** Full track name with **/
	public String FullName; // Track name
	/** New track **/
	public boolean isNewTrack;

	/** Name of the track that appear in the track setting **/
	public String CourseName = "";
	/** Total distance in meters **/
	private double TotalDistance = 0.0;
	/** Total time in seconde **/
	public int TotalTime = 0;
	/** Indicate if during the last loading the time field as been loaded **/
	public boolean isTimeLoaded = false;
	/**
	 * Indicate if the data has been calculated. false=They must be calculate
	 **/
	public boolean isCalculated = false;
	/** If 'true' this indicate that the data has been modified **/
	public boolean isModified = false;
	/** Contain the ascent climb of the whole track (in m) **/
	private double ClimbP = 0.0;
	/** Contain the descent climb of the whole track (in m) **/
	private double ClimbM = 0.0;
	/** Contain the ascent time of the whole track (in s) **/
	public int AscTime = 0;
	/** Contain the descent time of the whole track (in s) **/
	public int DescTime = 0;
	/** Description of the track **/
	public String Description = "";
	/** Indicate if the autocalc function is active **/
	public boolean isAutoCalc = false;
	/** Start time of the track **/
	public DateTime StartTime = new DateTime();
	/** Minimum elevation of the track (in m) **/
	private double MinElev = -1;
	/** Maximum elevation of the track (in m) **/
	private double MaxElev = -1;
	/** Filter value for the elevation filter **/
	public int SmoothFilter = 0;
	/** Global start health coefficient **/
	public double StartGlobalCoeff = 100;
	/** Global end health coefficient **/
	public double EndGlobalCoeff = 100;
	/** If 'true' this indicate that the time limit has been reached **/
	public boolean isTimeLimit = false;
	/** Indicate where the timelimit has been reached (-1 if none) **/
	public int TimeLimit_Line = -1;
	/** Timezone offset for the sunrise/sunset calculation **/
	public int timeZoneOffsetHours = 0;
	/** Time Zone Id **/
	public String timeZoneId = "";
	/** Are we using the daylight saving time for the sunrise/sunset calculation **/
	public boolean TrackUseDaylightSaving = false;
	public double StartSpeed = 0.0;
	public double EndSpeed = 0.0;
	/** Number of meter of road in the track **/
	private double DistRoad = 0.0;
	/** Indicate the type of error during the file reading **/
	public int ReadError = 0;
	/** Indicate the at which line the error appear **/
	public int ReadLineError = 0;

	// -- Night Coeff --
	/** Start night time **/
	public DateTime StartNightTime;
	/** End night time **/
	public DateTime EndNightTime;
	private final static DateTime defaultSunriseSunsetTime = new DateTime(2010, 1, 1, 0, 0, 0);
	/** If 'true' this indicate that the night coefficients are used ***/
	public boolean bNightCoeff = false;
	/** Ascent nigth coefficient (100%=normal) **/
	public double NightCoeffAsc = 100.0;
	/** Descent nigth coefficient (100%=normal) **/
	public double NightCoeffDesc = 100.0;

	// -- Elevation coeff
	/** Indicate that the elevation effect is used during the calculation **/
	public boolean bElevEffect = false;

	// -- Profil mini-roadbook
	/** Width of the mini roadbook (in pixels) **/
	public int MrbSizeW = 1000;
	/** Height of the mini roadbook (in pixels) **/
	public int MrbSizeH = 480;
	/** Curve filter in the mini roadbook **/
	public int CurveFilter = 1;
	/** Number of characters before the line is word wrapped **/
	public int WordWrapLength = 25;
	/** Position of the label. 'true'=bottom **/
	public boolean LabelToBottom = false;
	/** Type of profil in the mini roadbook **/
	public int MRBType = 0;
	/** Top margin size in pixels in the mini roadbook **/
	public int TopMargin = 100;
	/** Show the night and day on the mrb profil **/
	public boolean bShowNightDay = true;
	// ** Indicate this track is read only mode **/
	public boolean ReadOnly = false;

	// -- Profil colors
	public Color clProfil_Simple_Fill = Color.BLACK;
	public Color clProfil_Simple_Border = Color.BLACK;
	public Color clProfil_RS_Road = Color.BLACK;
	public Color clProfil_RS_Path = Color.BLACK;
	public Color clProfil_RS_Border = Color.BLACK;

	public Color clProfil_SlopeInf5 = Color.BLACK;
	public Color clProfil_SlopeInf10 = Color.BLACK;
	public Color clProfil_SlopeInf15 = Color.BLACK;
	public Color clProfil_SlopeSup15 = Color.BLACK;
	public Color clProfil_SlopeBorder = Color.BLACK;

	private static CgSettings Settings;

	// -- Constructor --
	public TrackData(CgSettings settings) {
		Name = "";
		FullName = "";
		isNewTrack = true;
		param = new ParamData();
		Paramfile = "Default";
		data = new ArrayList<CgData>();
		tInNight = new StatData();
		tInDay = new StatData();
		StatSlope = new StatData[13]; // : Array [0..12] of TStat;
		for (int i = 0; i < 13; i++) {
			StatSlope[i] = new StatData();
		}

		StatElev = new StatData[6]; // : Array [0..5] of TStat;
		StatElevNight = new StatData[6]; // : Array [0..5] of TStat;
		StatElevDay = new StatData[6]; // : Array [0..5] of TStat;

		for (int i = 0; i < 6; i++) {
			StatElev[i] = new StatData();
			StatElevNight[i] = new StatData();
			StatElevDay[i] = new StatData();
		}

		timeZoneOffsetHours = 0;
		timeZoneId = "";
		StartNightTime = defaultSunriseSunsetTime;
		EndNightTime = defaultSunriseSunsetTime;

		MrbSizeW = settings.DefMrbWidth;
		MrbSizeH = settings.DefMrbHeight;
		CurveFilter = 1;
		WordWrapLength = 25;
		SmoothFilter = 0;

		DefaultMRBProfilSimpleColor();
		DefaultMRBProfilRSColor();
		DefaultMRBProfilSlopeColor();

		ReadOnly = false;
		Settings = settings;
	}

	public static synchronized TrackData getInstance() {
		if (instance == null) {
			instance = new TrackData(Settings);
		}
		return instance;
	}

	/**
	 * Initialize the mini roadbook profil color (simple mode)
	 */
	public void DefaultMRBProfilSimpleColor() {
		clProfil_Simple_Fill = CgConst.CL_PROFIL_SIMPLE_FILL;
		clProfil_Simple_Border = CgConst.CL_PROFIL_SIMPLE_BORDER;
	}

	/**
	 * Initialize the mini roadbook profil color (Road and track mode)
	 */
	public void DefaultMRBProfilRSColor() {
		clProfil_RS_Road = CgConst.CL_PROFIL_RS_ROAD;
		clProfil_RS_Path = CgConst.CL_PROFIL_RS_PATH;
		clProfil_RS_Border = CgConst.CL_PROFIL_RS_BORDER;
	}

	/**
	 * Initialize the mini roadbook profil color (Slope mode)
	 */
	public void DefaultMRBProfilSlopeColor() {
		clProfil_SlopeInf5 = CgConst.CL_PROFIL_SLOPE_INF5;
		clProfil_SlopeInf10 = CgConst.CL_PROFIL_SLOPE_INF10;
		clProfil_SlopeInf15 = CgConst.CL_PROFIL_SLOPE_INF15;
		clProfil_SlopeSup15 = CgConst.CL_PROFIL_SLOPE_SUP15;
		clProfil_SlopeBorder = CgConst.CL_PROFIL_SLOPE_BORDER;
	}

	/**
	 * Return the total distance in meter
	 * 
	 * @return
	 */
	public double getTotalDistance() {
		return TotalDistance;
	}

	/**
	 * Return the total distance. Unit depend of the 'unit'
	 * 
	 * @param unit Unit
	 * @return distance
	 */
	public double getTotalDistance(int unit) {
		switch (unit) {
		case CgConst.UNIT_METER:
			return TotalDistance;
		case CgConst.UNIT_MILES_FEET:
			// meter to miles
			return Utils.Meter2uMiles(TotalDistance);
		default:
			return TotalDistance;
		}
	}

	public void setTotalDistance(double totalDistance) {
		TotalDistance = totalDistance;
	}

	/***
	 * Check the GPS point density on the track. If the result is too high, ask if
	 * CG need to filter the track
	 * 
	 * @return true : filter must be apply
	 */
	private boolean CheckPointDensity(double PosFilterAskThreshold) {
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");

		CalcDist();

		int nb = 0;
		boolean ok = false;

		// Scan the data
		for (CgData r : data) {
			if (r.getDist(CgConst.UNIT_METER) < 10.0)
				nb += 1;
		}
		double p = nb * 100.0 / data.size();
		CgLog.info("Point density calculation = " + p + "%");

		// Question?
		if (p >= PosFilterAskThreshold) {
			if (JOptionPane.showConfirmDialog(null,
					String.format(bundle.getString("TrackData.PositionFilterQuestion"), p), "",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				ok = true;
			}
		}

		return ok;
	}

	/**
	 * Read a GPX file and store the data in the array
	 * 
	 * @param name Full name of the file
	 * @param mode 0 = Replace the existing data by the new data 1 = Insert the new
	 *             data at the beginning of the existing data 2 = Add the new data
	 *             at the end of the existing data
	 * @return Return true if time data have been loaded
	 * @throws Exception
	 */
	public boolean OpenGPX(String name, int mode, double PosFilterAskThreshold) throws Exception {
		SaxGPXHandler GPXhandler = new SaxGPXHandler();

		int ret = GPXhandler.readDataFromGPX(name, this, mode);
		if (ret != 0)
			CgLog.error("TrackData.OpenGPX : Error while reading '" + name + "'. Line =" + GPXhandler.getErrLine());

		CgLog.info(data.size() + " positions loaded.");

		boolean filter = CheckPointDensity(PosFilterAskThreshold);

		// -- Positions filter
		if (filter) {
			PositionFilter();
			CgLog.info(data.size() + " positions after positions filter.");
		}

		// -- Set the line number
		int cmpt = 1;
		for (CgData r : data) {
			r.setNum(cmpt);
			cmpt++;
		}

		isCalculated = false;
		isModified = false;

		if (mode == 0)
			isNewTrack = true;

		// -- Calculate the main data of the track
		CalcMainData(true, false);

		// StartTime = data.get(0).getHour().equals(new DateTime(1970, 1, 1, 0, 0, 0)) ?
		// new DateTime(2010, 1, 1, 0, 0) : data.get(0).getHour();
		StartTime = data.get(0).getHour().equals(new DateTime(1970, 1, 1, 0, 0, 0)) ? DateTime.now()
				: data.get(0).getHour();

		timeZoneOffsetHours = 0;
		timeZoneId = "";
		StartNightTime = defaultSunriseSunsetTime;
		EndNightTime = defaultSunriseSunsetTime;

		SetNightBit();

		isCalculated = true;
		Name = new File(name).getName();

		switch (mode) {
		case 1:
			CgLog.info("TrackData.OpenGPX : '" + name + "' imported at the end of the data");
			break;
		case 2:
			CgLog.info("TrackData.OpenGPX : '" + name + "' imported at the start of the data");
			break;
		default:
			CgLog.info("TrackData.OpenGPX : '" + name + "' loaded");
		}

		return isTimeLoaded;
	} // -- OpenGPX

	// -- Save GPX file (complet or partial) --
	/**
	 * Save the track in GPX format
	 * 
	 * @param name  Name of the file
	 * @param start Index of the first point to save
	 * @param end   Index of the last point to save
	 */
	public void ExportGPX(String name, int start, int end) {
		/*
		 * <?xml version="1.0"?> <gpx creator=
		 * "GPS Visualizer http://www.gpsvisualizer.com/" version="1.0"
		 * xmlns="http://www.topografix.com/GPX/1/0"
		 * xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation=
		 * "http://www.topografix.com/GPX/1/0 http://www.topografix.com/GPX/1/0/gpx.xsd"
		 * > <trk> <name>Lamontagnhard</name> <desc>http://www.lamontagnhard.fr</desc>
		 * <trkseg> <trkpt lat="45.8547528" lon="6.7226378"> <ele>1180.4</ele>
		 * <time>2010-06-11T11:41:10.000Z</time> </trkpt> </trkseg> </trk> </gpx>
		 */

		if (data.size() <= 0) {
			return;
		}

		long ts = System.currentTimeMillis();

		// -- Save the data in the home directory
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		try {
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(name));
			XMLStreamWriter writer = new IndentingXMLStreamWriter(
					factory.createXMLStreamWriter(bufferedOutputStream, "UTF-8"));

			// writer.writeStartDocument("UTF-8", "1.0");
			// writer.writeComment("Course Generator (C) Pierre DELORE");
			// writer.writeStartElement("CONFIG");
			// Utils.WriteStringToXML(writer, "PARAMFILE", ParamFile);

			// DateTime dt = new DateTime();

			writer.writeStartDocument("UTF-8", "1.0");
			writer.writeStartElement("gpx");
			writer.writeAttribute("creator", "Course Generator http://www.techandrun.com");
			writer.writeAttribute("version", "1.1");
			writer.writeAttribute("xsi:schemaLocation",
					"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd http://www.garmin.com/xmlschemas/WaypointExtension/v1 http://www8.garmin.com/xmlschemas/WaypointExtensionv1.xsd http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www8.garmin.com/xmlschemas/GpxExtensionsv3.xsd http://www.garmin.com/xmlschemas/ActivityExtension/v1 http://www8.garmin.com/xmlschemas/ActivityExtensionv1.xsd http://www.garmin.com/xmlschemas/AdventuresExtensions/v1 http://www8.garmin.com/xmlschemas/AdventuresExtensionv1.xsd http://www.garmin.com/xmlschemas/PressureExtension/v1 http://www.garmin.com/xmlschemas/PressureExtensionv1.xsd http://www.garmin.com/xmlschemas/TripExtensions/v1 http://www.garmin.com/xmlschemas/TripExtensionsv1.xsd http://www.garmin.com/xmlschemas/TripMetaDataExtensions/v1 http://www.garmin.com/xmlschemas/TripMetaDataExtensionsv1.xsd http://www.garmin.com/xmlschemas/ViaPointTransportationModeExtensions/v1 http://www.garmin.com/xmlschemas/ViaPointTransportationModeExtensionsv1.xsd http://www.garmin.com/xmlschemas/CreationTimeExtension/v1 http://www.garmin.com/xmlschemas/CreationTimeExtensionsv1.xsd http://www.garmin.com/xmlschemas/AccelerationExtension/v1 http://www.garmin.com/xmlschemas/AccelerationExtensionv1.xsd http://www.garmin.com/xmlschemas/PowerExtension/v1 http://www.garmin.com/xmlschemas/PowerExtensionv1.xsd http://www.garmin.com/xmlschemas/VideoExtension/v1 http://www.garmin.com/xmlschemas/VideoExtensionv1.xsd");
			writer.writeAttribute("xmlns", "http://www.topografix.com/GPX/1/1");
			writer.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			writer.writeAttribute("xmlns:wptx1", "http://www.garmin.com/xmlschemas/WaypointExtension/v1");
			writer.writeAttribute("xmlns:gpxtrx", "http://www.garmin.com/xmlschemas/GpxExtensions/v3");
			writer.writeAttribute("xmlns:gpxtpx", "http://www.garmin.com/xmlschemas/TrackPointExtension/v1");
			writer.writeAttribute("xmlns:gpxx", "http://www.garmin.com/xmlschemas/GpxExtensions/v3");
			writer.writeAttribute("xmlns:trp", "http://www.garmin.com/xmlschemas/TripExtensions/v1");
			writer.writeAttribute("xmlns:adv", "http://www.garmin.com/xmlschemas/AdventuresExtensions/v1");
			writer.writeAttribute("xmlns:prs", "http://www.garmin.com/xmlschemas/PressureExtension/v1");
			writer.writeAttribute("xmlns:tmd", "http://www.garmin.com/xmlschemas/TripMetaDataExtensions/v1");
			writer.writeAttribute("xmlns:vptm",
					"http://www.garmin.com/xmlschemas/ViaPointTransportationModeExtensions/v1");
			writer.writeAttribute("xmlns:ctx", "http://www.garmin.com/xmlschemas/CreationTimeExtension/v1");
			writer.writeAttribute("xmlns:gpxacc", "http://www.garmin.com/xmlschemas/AccelerationExtension/v1");
			writer.writeAttribute("xmlns:gpxpx", "http://www.garmin.com/xmlschemas/PowerExtension/v1");
			writer.writeAttribute("xmlns:vidx1", "http://www.garmin.com/xmlschemas/VideoExtension/v1");

			// Utils.WriteStringToXML(writer, "creator", "Course Generator
			// http://www.techandrun.com");
			// Utils.WriteStringToXML(writer, "version", "1.1");
			//
			// Utils.WriteStringToXML(writer, "xsi:schemaLocation",
			// "http://www.topografix.com/GPX/1/1
			// http://www.topografix.com/GPX/1/1/gpx.xsd
			// http://www.garmin.com/xmlschemas/WaypointExtension/v1
			// http://www8.garmin.com/xmlschemas/WaypointExtensionv1.xsd
			// http://www.garmin.com/xmlschemas/TrackPointExtension/v1
			// http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd
			// http://www.garmin.com/xmlschemas/GpxExtensions/v3
			// http://www8.garmin.com/xmlschemas/GpxExtensionsv3.xsd
			// http://www.garmin.com/xmlschemas/ActivityExtension/v1
			// http://www8.garmin.com/xmlschemas/ActivityExtensionv1.xsd
			// http://www.garmin.com/xmlschemas/AdventuresExtensions/v1
			// http://www8.garmin.com/xmlschemas/AdventuresExtensionv1.xsd");
			// Utils.WriteStringToXML(writer, "xmlns",
			// "http://www.topografix.com/GPX/1/1");
			// Utils.WriteStringToXML(writer, "xmlns:xsi",
			// "http://www.w3.org/2001/XMLSchema-instance");
			// Utils.WriteStringToXML(writer, "xmlns:wptx1",
			// "http://www.garmin.com/xmlschemas/WaypointExtension/v1");
			// Utils.WriteStringToXML(writer, "xmlns:gpxtrx",
			// "http://www.garmin.com/xmlschemas/GpxExtensions/v3");
			// Utils.WriteStringToXML(writer, "xmlns:gpxtpx",
			// "http://www.garmin.com/xmlschemas/TrackPointExtension/v1");
			// Utils.WriteStringToXML(writer, "xmlns:gpxx",
			// "http://www.garmin.com/xmlschemas/GpxExtensions/v3");
			// Utils.WriteStringToXML(writer, "xmlns:trp",
			// "http://www.garmin.com/xmlschemas/TripExtensions/v1");
			// Utils.WriteStringToXML(writer, "xmlns:adv",
			// "http://www.garmin.com/xmlschemas/AdventuresExtensions/v1");

			// <trk> node
			writer.writeStartElement("trk");

			// <name> node
			Utils.WriteStringToXML(writer, "name", CourseName);

			// <trkseg> node
			writer.writeStartElement("trkseg");

			NumberFormat nf = NumberFormat.getNumberInstance(Locale.ROOT);
			nf.setGroupingUsed(false);
			nf.setMaximumFractionDigits(7);

			int cmpt = 0;
			for (int i = start; i <= end; i++) {
				CgData r = data.get(i);

				// <trkpt>
				// <trkpt lat="45.8547528" lon="6.7226378">
				writer.writeStartElement("trkpt");
				// writer.writeAttribute("lat", String.format(Locale.ROOT,
				// "%1.14f", r.getLatitude()));
				// writer.writeAttribute("lon", String.format(Locale.ROOT,
				// "%1.14f", r.getLongitude()));
				nf.setMaximumFractionDigits(14);
				writer.writeAttribute("lat", nf.format(r.getLatitude()));
				writer.writeAttribute("lon", nf.format(r.getLongitude()));

				// <ele>1180.4</ele>
				// Utils.WriteStringToXML(writer, "ele",
				// String.format(Locale.ROOT, "%1.7f",
				// r.getElevation(CgConst.UNIT_METER)));
				nf.setMaximumFractionDigits(7);
				Utils.WriteStringToXML(writer, "ele", nf.format(r.getElevation(CgConst.UNIT_METER)));

				// <time>2010-08-18T07:57:07.000Z</time>
				// dt = r.getHour().ToUniversalTime();
				Utils.WriteStringToXML(writer, "time", r.getHour().toString());

				// <name>toto</name>
				Utils.WriteStringToXML(writer, "name", String.valueOf(i));

				writer.writeEndElement();// Trkpt

				cmpt++;
			} // for
			writer.writeEndElement();// Trkseg
			writer.writeEndElement();// trk
			writer.writeEndElement();// gpx
			writer.writeEndDocument();
			writer.flush();
			writer.close();
			bufferedOutputStream.close();

			isModified = false;
			// Name = new File(name).getName();

			CgLog.info("TrackData.SaveGPX : '" + name + "' saved");
			CgLog.info(cmpt + " positions saved.");
		} catch (XMLStreamException | IOException e) {
			e.printStackTrace();
		}

		CgLog.info("Save time : " + (System.currentTimeMillis() - ts) + "ms");
	}

	// -- Save tags as waypoint in a GPX file --
	public void SaveWaypoint(String name, int mask) {
		if (data.size() <= 0) {
			return;
		}

		// -- Save the data in the home directory
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		try {
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(name));
			XMLStreamWriter writer = new IndentingXMLStreamWriter(
					factory.createXMLStreamWriter(bufferedOutputStream, "UTF-8"));

			writer.writeStartDocument("UTF-8", "1.0");
			writer.writeStartElement("gpx");
			writer.writeAttribute("creator", "Course Generator http://www.techandrun.com");
			writer.writeAttribute("version", "1.1");
			writer.writeAttribute("xsi:schemaLocation",
					"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd http://www.garmin.com/xmlschemas/WaypointExtension/v1 http://www8.garmin.com/xmlschemas/WaypointExtensionv1.xsd http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www8.garmin.com/xmlschemas/GpxExtensionsv3.xsd http://www.garmin.com/xmlschemas/ActivityExtension/v1 http://www8.garmin.com/xmlschemas/ActivityExtensionv1.xsd http://www.garmin.com/xmlschemas/AdventuresExtensions/v1 http://www8.garmin.com/xmlschemas/AdventuresExtensionv1.xsd http://www.garmin.com/xmlschemas/PressureExtension/v1 http://www.garmin.com/xmlschemas/PressureExtensionv1.xsd http://www.garmin.com/xmlschemas/TripExtensions/v1 http://www.garmin.com/xmlschemas/TripExtensionsv1.xsd http://www.garmin.com/xmlschemas/TripMetaDataExtensions/v1 http://www.garmin.com/xmlschemas/TripMetaDataExtensionsv1.xsd http://www.garmin.com/xmlschemas/ViaPointTransportationModeExtensions/v1 http://www.garmin.com/xmlschemas/ViaPointTransportationModeExtensionsv1.xsd http://www.garmin.com/xmlschemas/CreationTimeExtension/v1 http://www.garmin.com/xmlschemas/CreationTimeExtensionsv1.xsd http://www.garmin.com/xmlschemas/AccelerationExtension/v1 http://www.garmin.com/xmlschemas/AccelerationExtensionv1.xsd http://www.garmin.com/xmlschemas/PowerExtension/v1 http://www.garmin.com/xmlschemas/PowerExtensionv1.xsd http://www.garmin.com/xmlschemas/VideoExtension/v1 http://www.garmin.com/xmlschemas/VideoExtensionv1.xsd");
			writer.writeAttribute("xmlns", "http://www.topografix.com/GPX/1/1");
			writer.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			writer.writeAttribute("xmlns:wptx1", "http://www.garmin.com/xmlschemas/WaypointExtension/v1");
			writer.writeAttribute("xmlns:gpxtrx", "http://www.garmin.com/xmlschemas/GpxExtensions/v3");
			writer.writeAttribute("xmlns:gpxtpx", "http://www.garmin.com/xmlschemas/TrackPointExtension/v1");
			writer.writeAttribute("xmlns:gpxx", "http://www.garmin.com/xmlschemas/GpxExtensions/v3");
			writer.writeAttribute("xmlns:trp", "http://www.garmin.com/xmlschemas/TripExtensions/v1");
			writer.writeAttribute("xmlns:adv", "http://www.garmin.com/xmlschemas/AdventuresExtensions/v1");
			writer.writeAttribute("xmlns:prs", "http://www.garmin.com/xmlschemas/PressureExtension/v1");
			writer.writeAttribute("xmlns:tmd", "http://www.garmin.com/xmlschemas/TripMetaDataExtensions/v1");
			writer.writeAttribute("xmlns:vptm",
					"http://www.garmin.com/xmlschemas/ViaPointTransportationModeExtensions/v1");
			writer.writeAttribute("xmlns:ctx", "http://www.garmin.com/xmlschemas/CreationTimeExtension/v1");
			writer.writeAttribute("xmlns:gpxacc", "http://www.garmin.com/xmlschemas/AccelerationExtension/v1");
			writer.writeAttribute("xmlns:gpxpx", "http://www.garmin.com/xmlschemas/PowerExtension/v1");
			writer.writeAttribute("xmlns:vidx1", "http://www.garmin.com/xmlschemas/VideoExtension/v1");

			int i = 1;
			String s;
			for (CgData r : data) {
				if ((r.getTag() != 0) && ((r.getTag() & mask) != 0)) {
					// <wpt>
					writer.writeStartElement("wpt");
					writer.writeAttribute("lat", String.format(Locale.ROOT, "%1.14f", r.getLatitude()));
					writer.writeAttribute("lon", String.format(Locale.ROOT, "%1.14f", r.getLongitude()));

					// <time>2010-08-18T07:57:07.000Z</time>
					// Utils.WriteStringToXML(writer,"time",
					// r.getHour().toString()+"Z");

					// <name>toto</name>
					if (r.getName().isEmpty()) {
						Utils.WriteStringToXML(writer, "name", String.format("NoName%d", i));
						i++;
					} else
						Utils.WriteStringToXML(writer, "name", r.getName());

					// <sym>Flag, Green</sym>
					s = "Flag, Green"; // Par defaut
					if ((r.getTag() & CgConst.TAG_HIGH_PT) != 0)
						s = "Summit";
					if ((r.getTag() & CgConst.TAG_WATER_PT) != 0)
						s = "Bar";
					if ((r.getTag() & CgConst.TAG_EAT_PT) != 0)
						s = "Restaurant";

					Utils.WriteStringToXML(writer, "sym", s);

					// <type>user</type>
					Utils.WriteStringToXML(writer, "type", "user");

					writer.writeEndElement();// wpt
				} // if
			} // for
			writer.writeEndElement();// gpx
			writer.writeEndDocument();
			writer.flush();
			writer.close();
			CgLog.info("TrackData.SaveWaypoint : '" + name + "' saved");
		} catch (XMLStreamException | IOException e) {
			e.printStackTrace();
		}
	}

	public void ExportCGP(String name, int mask) {
		if (data.size() <= 0) {
			return;
		}

		// -- Save the data in the home directory
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		try {
			XMLStreamWriter writer = new IndentingXMLStreamWriter(
					factory.createXMLStreamWriter(new FileOutputStream(name), "UTF-8"));

			writer.writeStartDocument("UTF-8", "1.0");
			writer.writeStartElement("COURSEGENERATOR");
			writer.writeAttribute("VERSION", "1");

			// <Points> node
			writer.writeStartElement("Points");
			for (CgData r : data) {
				if ((((mask & CgConst.TAG_HIGH_PT) != 0) && ((r.getTag() & CgConst.TAG_HIGH_PT) != 0))
						|| (((mask & CgConst.TAG_LOW_PT) != 0) && ((r.getTag() & CgConst.TAG_LOW_PT) != 0))
						|| (((mask & CgConst.TAG_EAT_PT) != 0) && ((r.getTag() & CgConst.TAG_EAT_PT) != 0))
						|| (((mask & CgConst.TAG_WATER_PT) != 0) && ((r.getTag() & CgConst.TAG_WATER_PT) != 0))
						|| (((mask & CgConst.TAG_MARK) != 0) && ((r.getTag() & CgConst.TAG_MARK) != 0))
						|| (((mask & CgConst.TAG_COOL_PT) != 0) && ((r.getTag() & CgConst.TAG_COOL_PT) != 0))
						|| (((mask & CgConst.TAG_NOTE) != 0) && ((r.getTag() & CgConst.TAG_NOTE) != 0))
						|| (((mask & CgConst.TAG_ROADBOOK) != 0) && ((r.getTag() & CgConst.TAG_ROADBOOK) != 0))
						|| (((mask & CgConst.TAG_DROPBAG) != 0) && ((r.getTag() & CgConst.TAG_DROPBAG) != 0))
						|| (((mask & CgConst.TAG_CREW) != 0) && ((r.getTag() & CgConst.TAG_CREW) != 0))
						|| (((mask & CgConst.TAG_FIRST_AID) != 0) && ((r.getTag() & CgConst.TAG_FIRST_AID) != 0))
						|| (((mask & CgConst.TAG_INFO) != 0) && ((r.getTag() & CgConst.TAG_INFO) != 0))) {
					// <Pt>
					writer.writeStartElement("Pt");

					// <LatitudeDegrees>123.456</LatitudeDegrees>
					Utils.WriteStringToXML(writer, "LatitudeDegrees",
							String.format(Locale.ROOT, "%1.14f", r.getLatitude()));

					// <LongitudeDegrees>12345.678</LongitudeDegrees>
					Utils.WriteStringToXML(writer, "LongitudeDegrees",
							String.format(Locale.ROOT, "%1.14f", r.getLongitude()));

					// <AltitudeMeters>625.03515</AltitudeMeters>
					Utils.WriteStringToXML(writer, "AltitudeMeters",
							String.format(Locale.ROOT, "%1.1f", r.getElevation(CgConst.UNIT_METER)));

					// <Comment>AAA</Comment>
					String s = r.getComment().trim();
					if (s.equals(""))
						s = " ";
					Utils.WriteStringToXML(writer, "Comment", s);

					// <Name>AAA</Name>
					s = r.getName().trim();
					if (s.equals(""))
						s = " ";
					Utils.WriteStringToXML(writer, "Name", s);

					// <Tag>1234</Tag>
					Utils.WriteStringToXML(writer, "Tag", String.format("%d", r.getTag()));

					// <EatTime>1234</EatTime>
					Utils.WriteIntToXML(writer, "EatTime", r.getStation());

					// <TimeLimit>1234</TimeLimit>
					Utils.WriteIntToXML(writer, "TimeLimit", r.getTimeLimit());

					// <FmtLbMiniRoadbook>1234</FmtLbMiniRoadbook>
					Utils.WriteStringToXML(writer, "FmtLbMiniRoadbook", r.FmtLbMiniRoadbook);

					// <OptMiniRoadbook>1234</OptMiniRoadbook>
					Utils.WriteIntToXML(writer, "OptMiniRoadbook", r.OptionMiniRoadbook);

					// <VPosMiniRoadbook>1234</VPosMiniRoadbook>
					Utils.WriteIntToXML(writer, "VPosMiniRoadbook", r.VPosMiniRoadbook);

					// <CommentMiniRoadbook>1234</CommentMiniRoadbook>
					Utils.WriteStringToXML(writer, "CommentMiniRoadbook", r.CommentMiniRoadbook);

					// <FontSizeMiniRoadbook>1234</FontSizeMiniRoadbook>
					Utils.WriteIntToXML(writer, "FontSizeMiniRoadbook", r.FontSizeMiniRoadbook);

					writer.writeEndElement(); // Pt
				} // if

			} // for

			writer.writeEndElement(); // Points
			writer.writeEndElement(); // COURSEGENERATOR

			writer.writeEndDocument();
			writer.flush();
			writer.close();
		} catch (XMLStreamException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Class used to store the result of a point search
	 * 
	 * @author pierre
	 */
	public static class SearchPointResult {

		public double Distance; // Distance in meter
		public int Point; // -1= no point

		public SearchPointResult(int _point, double _distance) {
			Distance = _distance;
			Point = _point;
		}
	}

	/**
	 * Search the best point from the latitude and longitude
	 * 
	 * @param lat Latitude of the point to search
	 * @param lon Longitude of the point to search
	 * @return Object that contain the found point and the distance from the
	 *         searched point
	 */
	public SearchPointResult SearchPoint(double lat, double lon) {
		CgData cdata;
		double d, best;
		int p;

		best = 10000000000.0; // Big value. It's normal
		p = -1;

		if (data.size() >= 0) {
			for (int i = 0; i < data.size() - 1; i++) {
				cdata = data.get(i);
				d = Math.abs(Utils.CalcDistance(cdata.getLatitude(), cdata.getLongitude(), lat, lon));
				if (d < best) {
					best = d;
					p = i;
				}
			}
		}
		return new SearchPointResult(p, best);
	}

	/**
	 * Search the best point from the latitude and longitude
	 * 
	 * @param dist Distance to search
	 * 
	 * @return Index of the found point
	 */
	public int SearchDistance(double dist) {
		CgData cdata;
		double d, best;
		int p;

		double val = Utils.Distance2Meter(dist, Settings.Unit);

		best = 10000000000.0; // Big value. It's normal
		p = -1;

		if (data.size() >= 0) {
			for (int i = 0; i < data.size() - 1; i++) {
				cdata = data.get(i);
				d = Math.abs(val - cdata.getTotal(CgConst.UNIT_METER));
				if (d < best) {
					best = d;
					p = i;
				}
			}
		}
		return p;
	}

	/*
	 * private void AltitudeFilter() { if (data.size() <= 1) { return; } CgData r =
	 * null; double threshold = 4.0; double oldAlt =
	 * data.get(0).getElevation(CgConst.UNIT_METER); // We don't use the first and
	 * last point for (int i = 1; i < data.size() - 2; i++) { r = data.get(i); if
	 * (Math.abs(r.getElevation(CgConst.UNIT_METER) - oldAlt) < threshold)
	 * r.setElevation(oldAlt); else oldAlt = r.getElevation(CgConst.UNIT_METER); } }
	 */

	/**
	 * Position filter
	 */
	private void PositionFilter() {
		if (data.size() <= 0) {
			return;
		}

		int i = 0;
		CgData r = null;
		CgData r1 = null;
		CgData r2 = null;
		CgData r3 = null;
		CgData r4 = null;

		// Threshold. Point at a distance less than +- threshold are removed
		double threshold = 15.0; // 8.0;

		// Scan the data
		for (i = 0; i < data.size() - 6; i++) {
			r = data.get(i);
			if (r.ToDelete)
				continue;

			r1 = data.get(i + 1);
			r2 = data.get(i + 2);
			r3 = data.get(i + 3);
			r4 = data.get(i + 4);

			double dist1 = CalcDistance(r.getLatitude(), r.getLongitude(), r1.getLatitude(), r1.getLongitude());
			double dist2 = CalcDistance(r.getLatitude(), r.getLongitude(), r2.getLatitude(), r2.getLongitude());
			double dist3 = CalcDistance(r.getLatitude(), r.getLongitude(), r3.getLatitude(), r3.getLongitude());
			double dist4 = CalcDistance(r.getLatitude(), r.getLongitude(), r4.getLatitude(), r4.getLongitude());

			if (dist4 < threshold) {
				r4.ToDelete = true;
			}
			if (dist3 < threshold) {
				r3.ToDelete = true;
			}
			if (dist2 < threshold) {
				r2.ToDelete = true;
			}
			if (dist1 < threshold) {
				r1.ToDelete = true;
			}
		}

		// Removed the marked points
		for (i = data.size() - 1; i >= 0; i--) {
			if (data.get(i).ToDelete)
				data.remove(i);
		}

	}

	/**
	 * Calculate the main data of the track
	 * 
	 * @param CalcHour Indicate hour calculation need to be done
	 */
	public void CalcMainData(boolean CalcHour, boolean SpeedwithTime) {
		CalcDist();
		CalcSpeed(SpeedwithTime);
		CalcSlope();

		CalcClimbResult resClimb = new CalcClimbResult();
		CalcClimb(CgConst.ELEV_NORM, 0, data.size() - 1, resClimb);
		ClimbP = resClimb.cp;
		ClimbM = resClimb.cm;
		AscTime = resClimb.tp;
		DescTime = resClimb.tm;

		if (CalcHour)
			TotalTime = CalcHour();

		SearchMinMaxElevationResult resMinMaxElev = new SearchMinMaxElevationResult();
		resMinMaxElev = SearchMinMaxElevation(0, (data.size() - 1), resMinMaxElev);
		MinElev = resMinMaxElev.min;
		MaxElev = resMinMaxElev.max;
	}

	// -- Calculate Distance ---

	/**
	 * Calculate the distance of each portion of the track and the total distance
	 * The calculation take into account the elevation
	 */
	public void CalcDist() {
		double dist = 0.0;
		double mLat = 0.0;
		double mLon = 0.0;
		double mEle = 0.0;
		double v = 0.0;
		double Lat = 0;
		double Lon = 0;
		double Ele = 0;

		TotalDistance = 0;

		boolean b = false;
		for (CgData r : data) {
			Lat = r.getLatitude();
			Lon = r.getLongitude();
			Ele = r.getElevation(CgConst.UNIT_METER);
			if (b) {
				// -- Calculate the "flat" distance
				dist = CalcDistance(mLat, mLon, Lat, Lon);
				// -- A little bit of Pythagoras theorem in order to include the
				// difference of elevation between the points
				v = Math.sqrt(dist * dist + (Ele - mEle) * (Ele - mEle));

				TotalDistance = TotalDistance + v;
				r.setDist(v);
				r.setTotal(TotalDistance);
			} else {
				r.setDist(0.0);
				r.setTotal(0.0);
				b = true;
			} // if
			mLat = Lat;
			mLon = Lon;
			mEle = Ele;
		}
	} // Calcdist

	/**
	 * Calculate speed !!! To call after Calcdist()
	 */
	public void CalcSpeed(boolean withTime) {
		if (withTime)
//			CalcSpeedWithTime();
		{
			int prevTime = data.get(0).getTime();
			// -- Calculation loop --
			for (CgData r : data) {
				int dTime = r.getTime() - prevTime;

				//double dist = r.getDist(CgConst.UNIT_METER);
				if (dTime != 0.0) {
					r.setSpeed(r.getDist(CgConst.UNIT_METER) / dTime * 3.6);
				} else {
					r.setSpeed(0.0);
				}
				prevTime = r.getTime();
			} // End of the calculation loop --
		} else {
			boolean b = false;
			for (CgData r : data) {
				if (b) {
					if (r.getdTime_f() != 0.0) {
						r.setSpeed(r.getDist(CgConst.UNIT_METER) / r.getdTime_f() * 3.6);
					} else {
						r.setSpeed(0.0);
					}
				} else {
					r.setSpeed(0.0);
					b = true;
				} // if
			}
		}
	} // CalcSpeed

	/**
	 * Calculate slope
	 */
	public void CalcSlope() {
		double delta = 0;
		double dist = 0;
		double mLat = 0;
		double mLon = 0;
		double mEle = 0;
		double Lat = 0;
		double Lon = 0;
		double Ele = 0;

		boolean b = false;
		for (CgData r : data) {

			r.setSlope(0.0);
			Lat = (double) r.getLatitude();
			Lon = (double) r.getLongitude();
			Ele = (double) r.getElevation(CgConst.UNIT_METER);

			if (b) {
				dist = CalcDistance(mLat, mLon, Lat, Lon);

				delta = Ele - mEle;
				r.setdElevation(delta);
				if (dist != 0) {
					r.setSlope(delta / dist * 100);
				} else {
					r.setSlope(0);
				}
			}
			b = true;

			mLat = Lat;
			mLon = Lon;
			mEle = Ele;
		} // for i
	} // Calcslope

	public static class CalcAvrSlopeResult {

		/** Average positive slope **/
		public double AvrSlopeP;
		/** Average negative slope **/
		public double AvrSlopeM;

		/** Total distance with positive climb (stored in m) **/
		private double TotClimbP;
		/** Totale distance with flat (stored in m) **/
		private double TotFlat;
		/** Total distance with negative climb (stored in m) **/
		private double TotClimbM;

		public double getTotClimbP(int unit) {
			switch (unit) {
			case CgConst.UNIT_METER:
				return TotClimbP;
			case CgConst.UNIT_MILES_FEET:
				// meter to miles
				return Utils.Meter2uMiles(TotClimbP);
			default:
				return TotClimbP;
			}
		}

		public void setTotClimbP(double totClimbP) {
			TotClimbP = totClimbP;
		}

		public double getTotFlat(int unit) {
			switch (unit) {
			case CgConst.UNIT_METER:
				return TotFlat;
			case CgConst.UNIT_MILES_FEET:
				// meter to miles
				return Utils.Meter2uMiles(TotFlat);
			default:
				return TotFlat;
			}
		}

		public void setTotFlat(double totFlat) {
			TotFlat = totFlat;
		}

		public double getTotClimbM(int unit) {
			switch (unit) {
			case CgConst.UNIT_METER:
				return TotClimbM;
			case CgConst.UNIT_MILES_FEET:
				// meter to miles
				return Utils.Meter2uMiles(TotClimbM);
			default:
				return TotClimbM;
			}
		}

		public void setTotClimbM(double totClimbM) {
			TotClimbM = totClimbM;
		}

	}

	// -- Calculate climb - and + ---
	public CalcAvrSlopeResult CalcAvrSlope(int StartLine, int EndLine, CalcAvrSlopeResult r) {
		int i = 0;
		int ip = 0;
		int im = 0;
		double slope = 0.0;

		r.AvrSlopeP = 0.0;
		r.AvrSlopeM = 0.0;
		r.TotClimbP = 0.0;
		r.TotFlat = 0.0;
		r.TotClimbM = 0.0;

		if (data.size() > 0) {

			for (i = StartLine; i <= EndLine; i++) {
				slope = data.get(i).getSlope();
				if (slope > 0) {
					r.AvrSlopeP += slope;
					ip++;
				} else {
					r.AvrSlopeM += slope;
					im++;
				}

				if (slope <= -2) {
					r.TotClimbM += data.get(i).getDist(CgConst.UNIT_METER);
				} else if ((slope > -2) && (slope < 2)) {
					r.TotFlat += data.get(i).getDist(CgConst.UNIT_METER);
				} else if (slope >= 2) {
					r.TotClimbP += data.get(i).getDist(CgConst.UNIT_METER);
				}

			} // for i
		} // if
		r.AvrSlopeP = r.AvrSlopeP / ip;
		r.AvrSlopeM = r.AvrSlopeM / im;
		return r;
	} // CalcAvrSlope

	public static class CalcClimbResult {
		public double cp, cm;
		public int tp, tm;
				
		public double getAscendClimbSum(int unit) {
			switch (unit) {
			case CgConst.UNIT_METER:
				return cp;
			case CgConst.UNIT_MILES_FEET:
				// meter to feet
				return Utils.Meter2Feet(cp);
			default:
				return cp;
			}
		}
		
		
		public String getAscendClimbSumString(int unit, boolean withunit) {

			double e = getAscendClimbSum(unit);

			String s = "";
			switch (unit) {
			case CgConst.UNIT_METER:
				s = String.format("%1.0f", e);
				if (withunit)
					s = s + "m";
				break;
			case CgConst.UNIT_MILES_FEET:
				s = String.format("%1.0f", e);
				if (withunit)
					s = s + "feet";
				break;
			default:
				s = String.format("%1.0f", e);
				if (withunit)
					s = s + "m";
				break;
			}
			return s;
		}

		public double getDescentClimbSum(int unit) {
			switch (unit) {
			case CgConst.UNIT_METER:
				return cm;
			case CgConst.UNIT_MILES_FEET:
				// meter to feet
				return Utils.Meter2Feet(cm);
			default:
				return cm;
			}
		}
		
		public String getDescentClimbSumString(int unit, boolean withunit) {

			double e = getDescentClimbSum(unit);

			String s = "";
			switch (unit) {
			case CgConst.UNIT_METER:
				s = String.format("%1.0f", e);
				if (withunit)
					s = s + "m";
				break;
			case CgConst.UNIT_MILES_FEET:
				s = String.format("%1.0f", e);
				if (withunit)
					s = s + "feet";
				break;
			default:
				s = String.format("%1.0f", e);
				if (withunit)
					s = s + "m";
				break;
			}
			return s;
		}
	}

	
	// -- Calculate climb - and + ---
	// cp: cumul D+ (m)
	// cm: cumul D- (m)
	// tp: cumul temps en montée (s)
	// tm: cumul temps en descente (s)
	public CalcClimbResult CalcClimb(int elevType, int StartLine, int EndLine, CalcClimbResult r) {
		int i = 0;
		int oldTime = 0;
		int dt = 0;
		double oldElev = 0.0;
		double de = 0.0;
		double elev = 0.0;
		int time = 0;

		r.cp = 0;
		r.cm = 0;
		r.tp = 0;
		r.tm = 0;

		if (data.size() > 0) {
			switch (elevType) {
			case CgConst.ELEV_NOTSMOOTHED:
				oldElev = data.get(StartLine).getElevationNotSmoothed(CgConst.UNIT_METER);
				break;
			case CgConst.ELEV_SMOOTHED:
				oldElev = data.get(StartLine).getElevationSmoothed(CgConst.UNIT_METER);
				break;
			default:
				oldElev = data.get(StartLine).getElevation(CgConst.UNIT_METER);
			}

			oldTime = data.get(StartLine).getTime();

			for (i = StartLine; i <= EndLine; i++) {
				switch (elevType) {
				case CgConst.ELEV_NOTSMOOTHED:
					elev = data.get(i).getElevationNotSmoothed(CgConst.UNIT_METER);
					break;
				case CgConst.ELEV_SMOOTHED:
					elev = data.get(i).getElevationSmoothed(CgConst.UNIT_METER);
					break;
				default:
					elev = data.get(i).getElevation(CgConst.UNIT_METER);
				}

				time = data.get(i).getTime();

				de = (elev - oldElev);
				dt = (time - oldTime);

				if (Math.abs(de) > (double) Settings.ClimbThresholdForCalculation) {
					if (de > 0) {
						r.cp += de;
						r.tp += dt;
					}
					if (de < 0) {
						r.cm -= de;
						r.tm += dt;
					}
					oldElev = elev;
					oldTime = time;
				}
			} // for i
		} // if
		return r;
	} // CalcClimb

	/**
	 * Class to store the result of Average speed calculation
	 * 
	 * @author pierre
	 */
	public static class CalcAvrSpeedResult {

		/** Average speed (km/h) **/
		private double avrspeed;

		public void setAvrspeed(double avrspeed) {
			this.avrspeed = avrspeed;
		}

		public double getAvrspeed() {
			return avrspeed;
		}
	}

	// -- Calculate the average speed between 2 points
	public CalcAvrSpeedResult CalcAvrSpeed(int StartLine, int EndLine, CalcAvrSpeedResult r) {
		int t = 0;

		if (data.size() > 0) {
			t = (data.get(EndLine).getTime() - data.get(StartLine).getTime());
			if (t != 0) {
				r.avrspeed = (data.get(EndLine).getTotal(CgConst.UNIT_METER)
						- data.get(StartLine).getTotal(CgConst.UNIT_METER)) * 3.600 / t;
			} else {
				r.avrspeed = 0.0;
			}
		} // if
		return r;
	} // CalcAvrSpeed

	
	// -- Calculate Hour --
	/**
	 * Calculate the hour for each point
	 * 
	 * @return the total time in second
	 */
	public int CalcHour() {
		int last = 0;
		for (CgData r : data) {
			r.setHour(StartTime.plusSeconds(r.getTime()));
			last = r.getTime();
		} // for i
		return last;
	}

	// -- Calculate road distance (in meter) --
	public double CalcRoad(int start, int end) {
		double road = 0.0;
		// DistRoad = 0.0;
		// for (CgData r : data) {
		for (int i = start; i < end; i++) {
			CgData r = data.get(i);
			if (r.getDiff() == 100) {
				// DistRoad = DistRoad + r.getDist(CgConst.UNIT_METER);
				road = road + r.getDist(CgConst.UNIT_METER);
			}
		}
		return road;
	}

	class SearchMinMaxElevationResult {

		public double min, max;
	}

	/**
	 * Search the minmimum and maximum elevation of the track betwwen two points
	 * 
	 * @param start Starting point
	 * @param end   Ending point
	 * @param r     SearchMinMaxElevationResult object
	 * @return Result in a SearchMinMaxElevationResult object
	 */
	private SearchMinMaxElevationResult SearchMinMaxElevation(int start, int end, SearchMinMaxElevationResult r) {
		r.min = 9999.0;
		r.max = -1.0;
		int i = 0;
		for (i = start; i < end; i++) {
			double e = data.get(i).getElevation(CgConst.UNIT_METER);
			if (e < r.min) {
				r.min = e;
			}
			if (e > r.max) {
				r.max = e;
			}
		}
		return r;
	}

	/*
	 * public void SearchMinMaxElevation(int start, int end, ref min, ref max) {
	 * MinElev = 9999; MaxElev = -1; foreach (cgData r in data) { double e =
	 * r.Elevation; if (e < MinElev) MinElev = e; if (e > MaxElev) MaxElev = e; } }
	 */

	/**
	 * Check if the limit time is reached
	 * 
	 * @return True if the limit time is reached
	 */
	public boolean CheckTimeLimit() {
		int i = 0;
		isTimeLimit = false;
		TimeLimit_Line = -1;
		for (CgData r : data) {
			if ((r.getTimeLimit() != 0) && (r.getTime() > r.getTimeLimit())) {
				isTimeLimit = true;
				TimeLimit_Line = i;
			}
			i++;
		}
		return isTimeLimit;
	}

	/**
	 * Calculate the time for each position of the track
	 */
	public void Calculate() {
		int j = 0;
		int k = 0;
		double ts = 0.0;
		double x = 0.0;
		double x1 = 0.0;
		double y1 = 0.0;
		double x2 = 0.0;
		double y2 = 0.0;
		double y = 0.0;
		double tmp = 0.0;
		double night = 0.0;
		double dt = 0.0;
		double ef = 1.0;
		boolean ok;

		if (param == null) {
			return;
		}

		isTimeLoaded = false;

		// String sParamfile = Utils.GetHomeDir() + "/" + CgConst.CG_DIR + "/" +
		// Paramfile + ".par";

		// -- Search the curve in a special order (user>min_miles>km_h)
		int FolderType = Utils.searchCurveFolder(Paramfile);
		String sParamfile = Utils.getSelectedCurveFolder(FolderType) + Paramfile + ".par";

		try {
			param.Load(sParamfile);
		} catch (Exception e) {
			return;
		}

		ef = 1;

		dt = 0;
		// -- Calculation loop --
		for (CgData r : data) {
			x = r.getSlope();
			if (x > CgConst.MAX_CLIMB) {
				x = CgConst.MAX_CLIMB;
			}
			if (x < CgConst.MIN_CLIMB) {
				x = CgConst.MIN_CLIMB;
			}
			ok = false;
			for (j = 0; j <= param.data.size() - 1; j++) // -2
			{
				tmp = param.data.get(j).getSlope();
				if (tmp >= x) {
					k = j - 1;
					if (j == 0) {
						k = 0;
					}
					x1 = param.data.get(k).getSlope();
					y1 = param.data.get(k).getSpeedNumber();
					x2 = param.data.get(j).getSlope();
					y2 = param.data.get(j).getSpeedNumber();
					ok = true;
					break;
				}
			} // for j

			if (ok) {
				// We give 2 points (slope/speed) and we get "a" and
				// "b" from the line equation
				if (x1 != x2) {
					Utils.CalcLineResult res = new CalcLineResult();
					res = Utils.CalcLine(x1, y1, x2, y2, res);
					// We calculate the line equation. "Y"=speed in km/h
					// and "X"=slope
					y = res.a * x + res.b;
				} else {
					y = y1;
				}
			} else {
				y = param.data.get(param.data.size() - 2).getSpeedNumber();
			}

			// --Night coeff --
			DateTime t = r.getHour();
			if (bNightCoeff
					&& ((Utils.CompareHMS(t, StartNightTime) >= 0) || (Utils.CompareHMS(t, EndNightTime) <= 0))) {
				if (r.getSlope() < -2.0) {
					night = 100.0 / NightCoeffDesc;
					r.setNight(true);
				} else {
					night = 100.0 / NightCoeffAsc;
					r.setNight(true);
				}
			} else {
				night = 1;
				r.setNight(false);
			}

			// --Elev effect --
			if (bElevEffect && ((double) r.getElevation(CgConst.UNIT_METER) > 1500.0)) {
				ef = 1.0 + (Math.round(((double) r.getElevation(CgConst.UNIT_METER) - 1500.0) / 100.0) / 100.0);
			} else {
				ef = 1.0;
			}
			// --
			double dist = r.getDist(CgConst.UNIT_METER);
			double coeff = 100.0;
			double diff = 100.0;

			if (coeff != 0.0) {
				coeff = 100.0 / r.getCoeff();
			}
			if (diff != 0) {
				diff = 100.0 / r.getDiff();
			}

			double station = (double) (r.getStation());

			if (y != 0.0) {
				// Calculate the travel time in second in a part of the track
				ts = (dist / (y / 3.6)) * coeff * diff * night * ef + station;
			} else {
				ts = 0.0;
			}
			dt = dt + ts;

			r.setdTime_f(ts);
			r.setTime((int) Math.round(dt));

			if (ts != 0.0) {
				r.setSpeed(dist * 3.6 / ts);
			} else {
				r.setSpeed(0.0);
			}
			r.setHour(StartTime.plusSeconds((int) (Math.round(dt))));
		} // End of the calculation loop --

		// -- Update the road distance for the track
		DistRoad = CalcRoad(0, data.size() - 1);

		TotalTime = (int) Math.round(dt);
		isCalculated = true;
		isModified = true;
	} // Calculate

	public void CalcSpeedWithTime() {
		if (param == null) {
			return;
		}

		isTimeLoaded = false;

		int prevTime = data.get(0).getTime();
		// -- Calculation loop --
		for (CgData r : data) {
			int dTime = r.getTime() - prevTime;

			double dist = r.getDist(CgConst.UNIT_METER);
			if (dTime != 0.0) {
				r.setSpeed(dist * 3.6 / dTime);
			} else {
				r.setSpeed(0.0);
			}
			prevTime = r.getTime();
		} // End of the calculation loop --
		isModified = true;
	} // Calculate

	/**
	 * Set night bit. Used when we load a track to avoid to launch a new calculation
	 * to have the night display on the map
	 */
	public void SetNightBit() {
		// -- Calculation loop --
		for (CgData r : data) {
			DateTime t = r.getHour();
			if (bNightCoeff
					&& ((Utils.CompareHMS(t, StartNightTime) >= 0) || (Utils.CompareHMS(t, EndNightTime) <= 0))) {
				r.setNight(true);
			} else {
				r.setNight(false);
			}
		}
	} // SetNightBit

	/**
	 * Search the min/max elevation of the track
	 */
	public void CalcMinMaxElevation() {
		int i = 0;
		int j = 0;
		int k = 0;
		double dist = 0.0;
		double valmax = 0.0;
		double valmin = 0.0;
		boolean minm = false;
		boolean maxm = false;
		boolean minp = false;
		boolean maxp = false;
		boolean findmax = false;
		boolean findmin = false;
		CgData r1 = null;
		CgData r2 = null;
		CgData r3 = null;

		// -- Reset bit 0 & 1 of 'Tag' variable
		for (CgData r : data)
			r.setTag((int) r.getTag() & 0xFC);

		// -- Main loop
		for (i = 0; i < data.size(); i++) {
			r1 = data.get(i);

			dist = 0;
			minm = true;
			maxm = true;
			findmax = false;
			findmin = false;
			valmax = -1;
			valmin = -1;

			for (j = i; j >= 0; j--) {
				r2 = data.get(j);

				if ((r2.getTag() & (CgConst.TAG_LOW_PT | CgConst.TAG_HIGH_PT)) == 1) {
					findmax = true;
					valmax = r2.getElevation(CgConst.UNIT_METER);
				}
				if ((r2.getTag() & (CgConst.TAG_LOW_PT | CgConst.TAG_HIGH_PT)) == 2) {
					findmin = true;
					valmin = r2.getElevation(CgConst.UNIT_METER);
				}

				if (r2.getElevation(CgConst.UNIT_METER) > r1.getElevation(CgConst.UNIT_METER)) {
					maxm = false;
				}

				if (r2.getElevation(CgConst.UNIT_METER) < r1.getElevation(CgConst.UNIT_METER)) {
					minm = false;
				}

				dist = dist + r2.getDist(CgConst.UNIT_METER);
				if (dist > CgConst.DIST_MAX_MINMAX) {
					break;
				}
			} // For j

			if ((r1 == null) || (r2 == null)) {
				return;
			}
			if (maxm && (findmax
					|| (Math.abs(r2.getElevation(CgConst.UNIT_METER) - valmax) < CgConst.MIN_ELEV_MINMAX))) {
				maxm = false;
			}
			if (minm && (findmin
					|| (Math.abs(r2.getElevation(CgConst.UNIT_METER) - valmin) < CgConst.MIN_ELEV_MINMAX))) {
				minm = false;
			}

			dist = 0;
			minp = true;
			maxp = true;

			for (k = i; k < data.size(); k++) {
				r3 = data.get(k);
				if (r3.getElevation(CgConst.UNIT_METER) > r1.getElevation(CgConst.UNIT_METER)) {
					maxp = false;
				}

				if (r3.getElevation(CgConst.UNIT_METER) < r1.getElevation(CgConst.UNIT_METER)) {
					minp = false;
				}

				if (k < data.size() - 1) {
					dist = dist + (double) data.get(k + 1).getDist(CgConst.UNIT_METER);
				}

				if (dist > CgConst.DIST_MAX_MINMAX) {
					break;
				}
			}

			if (maxm && maxp) {
				r1.setTag(r1.getTag() | CgConst.TAG_HIGH_PT);
			}
			if (minm && minp) {
				r1.setTag(r1.getTag() | CgConst.TAG_LOW_PT);
			}

		} // Main loop
	} // CalcMinMax

	/**
	 * Invert track
	 */
	public void Invert() {

		if (data.size() >= 0) {

			// -- Invert the list
			Collections.reverse(data);

			// -- Set the line number
			int n = 1;
			for (CgData r : data)
				r.setNum(n++);

			// -- Refresh
			CalcDist();
			CalcSlope();

			CalcClimbResult resClimb = new CalcClimbResult();
			CalcClimb(CgConst.ELEV_NORM, 0, data.size() - 1, resClimb);
			ClimbP = resClimb.cp;
			ClimbM = resClimb.cm;
			AscTime = resClimb.tp;
			DescTime = resClimb.tm;

			CalcHour();

			SearchMinMaxElevationResult resMinMaxElev = new SearchMinMaxElevationResult();
			resMinMaxElev = SearchMinMaxElevation(0, (data.size() - 1), resMinMaxElev);
			MinElev = resMinMaxElev.min;
			MaxElev = resMinMaxElev.max;

			isCalculated = true;
			isModified = true;
		}
	}

	/**
	 * Invert track
	 * 
	 * @param start Index of the starting point
	 */
	public void NewStartingPoint(int start) {

		if (data.size() >= 0) {

			ArrayList<CgData> datatmp;

			datatmp = new ArrayList<CgData>();

			int n = start;
			int nb = 0;
			while (nb < data.size()) {
				datatmp.add(new CgData((double) (nb + 1), data.get(n).getLatitude(), data.get(n).getLongitude(),
						data.get(n).getElevation(CgConst.UNIT_METER),
						data.get(n).getElevationNotSmoothed(CgConst.UNIT_METER),
						data.get(n).getElevationSmoothed(CgConst.UNIT_METER), data.get(n).getElevationMemo(),
						data.get(n).getTag(), data.get(n).getDist(CgConst.UNIT_METER),
						data.get(n).getTotal(CgConst.UNIT_METER), data.get(n).getDiff(), data.get(n).getCoeff(), 0.0,
						data.get(n).getSlope(), data.get(n).getSpeed(CgConst.UNIT_METER),
						data.get(n).getdElevation(CgConst.UNIT_METER), data.get(n).getTime(), data.get(n).getdTime_f(),
						data.get(n).getTimeLimit(), data.get(n).getHour(), data.get(n).getStation(),
						data.get(n).getName(), data.get(n).getComment(), 0.0, 0.0, data.get(n).FmtLbMiniRoadbook,
						data.get(n).OptionMiniRoadbook, data.get(n).VPosMiniRoadbook, data.get(n).CommentMiniRoadbook,
						data.get(n).FontSizeMiniRoadbook));

				nb++;
				n++;
				if (n >= data.size()) {
					n = 0;
				}
			}

			n = 0;
			for (CgData r : data) {
				r.setNum(datatmp.get(n).getNum());
				r.setLatitude(datatmp.get(n).getLatitude());
				r.setLongitude(datatmp.get(n).getLongitude());
				r.setElevation(datatmp.get(n).getElevation(CgConst.UNIT_METER));
				r.setElevationNotSmoothed(datatmp.get(n).getElevationNotSmoothed(CgConst.UNIT_METER));
				r.setElevationSmoothed(datatmp.get(n).getElevationSmoothed(CgConst.UNIT_METER));
				r.setElevationMemo(datatmp.get(n).getElevationMemo());
				r.setTag(datatmp.get(n).getTag());
				r.setDist(datatmp.get(n).getDist(CgConst.UNIT_METER));
				r.setTotal(datatmp.get(n).getTotal(CgConst.UNIT_METER));
				r.setDiff(datatmp.get(n).getDiff());
				r.setCoeff(datatmp.get(n).getCoeff());
				r.setSlope(datatmp.get(n).getSlope());
				r.setSpeed(datatmp.get(n).getSpeed(CgConst.UNIT_METER));
				r.setdElevation(datatmp.get(n).getdElevation(CgConst.UNIT_METER));
				r.setTime(datatmp.get(n).getTime());
				r.setdTime_f(datatmp.get(n).getdTime_f());
				r.setTimeLimit(datatmp.get(n).getTimeLimit());
				r.setHour(datatmp.get(n).getHour());
				r.setStation(datatmp.get(n).getStation());
				r.setName(datatmp.get(n).getName());
				r.setComment(datatmp.get(n).getComment());
				r.FmtLbMiniRoadbook = data.get(n).FmtLbMiniRoadbook;
				r.OptionMiniRoadbook = data.get(n).OptionMiniRoadbook;
				r.VPosMiniRoadbook = data.get(n).VPosMiniRoadbook;
				r.CommentMiniRoadbook = data.get(n).CommentMiniRoadbook;
				n++;
			}

			CalcDist();
			CalcSpeed(false);
			CalcSlope();

			CalcClimbResult resClimb = new CalcClimbResult();
			resClimb = CalcClimb(CgConst.ELEV_NORM, 0, data.size() - 1, resClimb);
			ClimbP = resClimb.cp;
			ClimbM = resClimb.cm;
			AscTime = resClimb.tp;
			DescTime = resClimb.tm;

			CalcHour();

			SearchMinMaxElevationResult resMinMaxElev = new SearchMinMaxElevationResult();
			resMinMaxElev = SearchMinMaxElevation(0, (data.size() - 1), resMinMaxElev);
			MinElev = resMinMaxElev.min;
			MaxElev = resMinMaxElev.max;

			isCalculated = true;
			isModified = true;
		}
	}

	/**
	 * Load a CGX file
	 * 
	 * @param name   name of the file
	 * @param mode   reading mode (0=complet 1=partial)
	 * @param backup Indicate if the load is a backup or not. If it's a backup the
	 *               name will not be updated
	 */
	public void OpenCGX(Component parent, String name, int mode, boolean backup) {
		SaxCGXHandler CGXhandler = new SaxCGXHandler();

		int ret = 0;
		try {
			ret = CGXhandler.readDataFromCGX(parent, name, this, mode);
		} catch (Exception e) {
		}

		if (ret != 0)
			CgLog.error("TrackData.OpenCGX : Error while reading '" + name + "'. Line =" + CGXhandler.getErrLine());

		CgLog.info(data.size() + " positions loaded.");

		/*
		 * Currently not used. Useful for CGX???? // -- Positions filter
		 * PositionFilter(); CgLog.info(data.size() +
		 * " positions after positions filter.");
		 */

		// -- Set the line number
		int cmpt = 1;
		for (CgData r : data) {
			r.setNum(cmpt);
			cmpt++;
		}

		isCalculated = false;
		isModified = false;
		if (mode == 0)
			isNewTrack = true;

		// -- Calculate the main data of the track
		CalcMainData(true, false);

		/*
		 * CalcDist(); CalcSpeed(); CalcSlope();
		 * 
		 * CalcClimbResult resClimb = new CalcClimbResult();
		 * CalcClimb(CgConst.ELEV_NORM, 0, data.size() - 1, resClimb); ClimbP =
		 * resClimb.cp; ClimbM = resClimb.cm; AscTime = resClimb.tp; DescTime =
		 * resClimb.tm;
		 * 
		 * TotalTime = CalcHour();
		 * 
		 * SearchMinMaxElevationResult resMinMaxElev = new
		 * SearchMinMaxElevationResult(); resMinMaxElev = SearchMinMaxElevation(0,
		 * (data.size() - 1), resMinMaxElev); MinElev = resMinMaxElev.min; MaxElev =
		 * resMinMaxElev.max;
		 */

		SetNightBit();

		CheckTimeLimit();
		isCalculated = true;

		if (!backup) {
			Name = new File(name).getName();
			//String Dir = new File(name).getAbsolutePath();
			switch (mode) {
			case 1:
				CgLog.info("TrackData.OpenCGX : '" + name + "' imported at the end of the data");
				break;
			case 2:
				CgLog.info("TrackData.OpenCGX : '" + name + "' imported at the start of the data");
				break;
			default:
				CgLog.info("TrackData.OpenCGX : '" + name + "' loaded");
			}
		}
		FullName = name;

	}// LoadCGX

	/**
	 * Save data in CGX format (complete and partial)
	 * 
	 * @param name  name of the file
	 * @param start first line to save
	 * @param end   last line to save
	 */
	public void SaveCGX(String name, int start, int end, boolean backup) {
		if (data.isEmpty()) {
			return;
		}

		long ts = System.currentTimeMillis();

		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		try {
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(name));
			XMLStreamWriter writer = new IndentingXMLStreamWriter(
					factory.createXMLStreamWriter(bufferedOutputStream, "UTF-8"));

			writer.writeStartDocument("UTF-8", "1.0");
			writer.writeStartElement("COURSEGENERATOR");
			Utils.WriteStringToXML(writer, "VERSION", "5");

			double d = data.get(end).getTotal(CgConst.UNIT_METER) - data.get(start).getTotal(CgConst.UNIT_METER);
			Utils.WriteStringToXML(writer, "TOTALDISTANCE", String.format(Locale.ROOT, "%f", d));

			int j = data.get(end).getTime() - data.get(start).getTime();
			Utils.WriteStringToXML(writer, "TOTALSECOND", String.valueOf(j));

			Utils.WriteStringToXML(writer, "COURSENAME", CourseName);
			Utils.WriteStringToXML(writer, "DESCRIPTION", Description);
			Utils.WriteStringToXML(writer, "STARTTIME", StartTime.toString());
			Utils.WriteStringToXML(writer, "USEELEVEFFECT", (bElevEffect ? "1" : "0"));
			Utils.WriteStringToXML(writer, "USENIGHTCOEFF", (bNightCoeff ? "1" : "0"));
			Utils.WriteStringToXML(writer, "NIGHTSTARTTIME", StartNightTime.toString("HH:mm"));
			Utils.WriteStringToXML(writer, "NIGHTENDTIME", EndNightTime.toString("HH:mm"));
			Utils.WriteStringToXML(writer, "NIGHTCOEFF", String.format(Locale.ROOT, "%f", NightCoeffAsc));
			Utils.WriteStringToXML(writer, "NIGHTCOEFFDESC", String.format(Locale.ROOT, "%f", NightCoeffDesc));
			Utils.WriteStringToXML(writer, "STARTGLOBALCOEFF", String.format(Locale.ROOT, "%f", StartGlobalCoeff));
			Utils.WriteStringToXML(writer, "ENDGLOBALCOEFF", String.format(Locale.ROOT, "%f", EndGlobalCoeff));
			Utils.WriteIntToXML(writer, "TIMEZONE", timeZoneOffsetHours);
			Utils.WriteStringToXML(writer, "TIMEZONEID", timeZoneId);
			Utils.WriteStringToXML(writer, "USESUMMERTIME", (TrackUseDaylightSaving ? "1" : "0"));
			Utils.WriteStringToXML(writer, "CURVE", Paramfile);
			Utils.WriteIntToXML(writer, "MRBSIZEW", MrbSizeW);
			Utils.WriteIntToXML(writer, "MRBSIZEH", MrbSizeH);
			Utils.WriteStringToXML(writer, "MRBSHOWDAYNIGHT", (bShowNightDay ? "1" : "0"));

			Utils.WriteIntToXML(writer, "CLPROFILSIMPLEFILL", clProfil_Simple_Fill.getRGB());
			Utils.WriteIntToXML(writer, "CLPROFILSIMPLEBORDER", clProfil_Simple_Border.getRGB());

			Utils.WriteIntToXML(writer, "CLPROFILRSROAD", clProfil_RS_Road.getRGB());
			Utils.WriteIntToXML(writer, "CLPROFILRSPATH", clProfil_RS_Path.getRGB());
			Utils.WriteIntToXML(writer, "CLPROFILRSBORDER", clProfil_RS_Border.getRGB());

			Utils.WriteIntToXML(writer, "CLPROFILSLOPEINF5", clProfil_SlopeInf5.getRGB());
			Utils.WriteIntToXML(writer, "CLPROFILSLOPEINF10", clProfil_SlopeInf10.getRGB());
			Utils.WriteIntToXML(writer, "CLPROFILSLOPEINF15", clProfil_SlopeInf15.getRGB());
			Utils.WriteIntToXML(writer, "CLPROFILSLOPESUP15", clProfil_SlopeSup15.getRGB());
			Utils.WriteIntToXML(writer, "CLPROFILSLOPEBORDER", clProfil_SlopeBorder.getRGB());

			Utils.WriteIntToXML(writer, "MRBCURVEFILTER", CurveFilter);
			Utils.WriteIntToXML(writer, "WORDWRAPLENGTH", WordWrapLength);
			Utils.WriteStringToXML(writer, "LABELTOBOTTOM", (LabelToBottom ? "1" : "0"));
			Utils.WriteIntToXML(writer, "MRBTYPE", MRBType);
			Utils.WriteIntToXML(writer, "TOPMARGIN", TopMargin);

			Utils.WriteIntToXML(writer, "SMOOTHFILTER", SmoothFilter);

			NumberFormat nf = NumberFormat.getNumberInstance(Locale.ROOT);
			nf.setGroupingUsed(false);
			nf.setMaximumFractionDigits(7);

			writer.writeStartElement("TRACKPOINTS");
			int cmpt = 0;
			for (int i = start; i <= end; i++) {
				CgData r = data.get(i);
				writer.writeStartElement("TRACKPOINT");
				Utils.WriteStringToXML(writer, "LATITUDEDEGREES", nf.format(r.getLatitude()));
				Utils.WriteStringToXML(writer, "LONGITUDEDEGREES", nf.format(r.getLongitude()));
				Utils.WriteStringToXML(writer, "ALTITUDEMETERS", nf.format(r.getElevation(CgConst.UNIT_METER)));
				Utils.WriteStringToXML(writer, "ALTITUDEMETERSNOTSMOOTHED",
						nf.format(r.getElevationNotSmoothed(CgConst.UNIT_METER)));
				Utils.WriteStringToXML(writer, "ALTITUDEMETERSSMOOTHED",
						nf.format(r.getElevationSmoothed(CgConst.UNIT_METER)));
				Utils.WriteStringToXML(writer, "DISTANCEMETERS", nf.format(r.getDist(CgConst.UNIT_METER)));
				Utils.WriteStringToXML(writer, "DISTANCEMETERSCUMUL", nf.format(r.getTotal(CgConst.UNIT_METER)));
				Utils.WriteStringToXML(writer, "DIFF", nf.format(r.getDiff()));
				Utils.WriteStringToXML(writer, "COEFF", nf.format(r.getCoeff()));
				Utils.WriteStringToXML(writer, "RECUP", nf.format(r.getRecovery()));
				Utils.WriteIntToXML(writer, "TIMESECONDE", r.getTime());
				Utils.WriteIntToXML(writer, "EATTIME", r.getStation());
				Utils.WriteIntToXML(writer, "TIMELIMIT", r.getTimeLimit());
				Utils.WriteStringToXML(writer, "COMMENT", r.getComment());
				Utils.WriteStringToXML(writer, "NAME", r.getName());
				Utils.WriteIntToXML(writer, "TAG", r.getTag());
				Utils.WriteStringToXML(writer, "FMTLBMINIROADBOOK", r.FmtLbMiniRoadbook);
				Utils.WriteIntToXML(writer, "OPTMINIROADBOOK", r.OptionMiniRoadbook);
				Utils.WriteIntToXML(writer, "VPOSMINIROADBOOK", r.VPosMiniRoadbook);
				Utils.WriteStringToXML(writer, "COMMENTMINIROADBOOK", r.CommentMiniRoadbook);
				Utils.WriteIntToXML(writer, "FONTSIZEMINIROADBOOK", r.FontSizeMiniRoadbook);

				writer.writeEndElement();
				cmpt++;
			} // for
				// TRACKPOINT
			writer.writeEndElement();

			writer.writeEndElement();

			writer.writeEndDocument();
			writer.flush();
			writer.close();
			bufferedOutputStream.close();

			if (!backup)

			{
				isModified = false;
				Name = new File(name).getName();
				CgLog.info("TrackData.SaveCGX : '" + name + "' saved");
				CgLog.info(cmpt + " positions saved.");
			}
			FullName = name;
		} catch (XMLStreamException |

				IOException e) {
			e.printStackTrace();
		}

		CgLog.info("Save time : " + (System.currentTimeMillis() - ts) + "ms");
	}

	/**
	 * Save CSV file
	 * 
	 * @param name      name of the CSV file
	 * @param start     first line of the data to save
	 * @param end       last line of the data to save
	 * @param separator 0=dot 1=comma
	 */
	public void SaveCSV(String name, int start, int end, int unit, int separator) {
		if (data.size() <= 0)
			return;

		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		StringBuilder s = new StringBuilder();

		long ts = System.currentTimeMillis();

		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ROOT);
		if (separator == 0)
			symbols.setDecimalSeparator('.');
		else
			symbols.setDecimalSeparator(',');

		DecimalFormat decimalFormat7 = new DecimalFormat("##0.#######", symbols);
		DecimalFormat decimalFormat1 = new DecimalFormat("##0.#", symbols);

		// TODO May be export alo elevationNotSmoothed and elevationSmoothed?
		try {
			PrintWriter writer = new PrintWriter(name, "UTF-8");

			s.append(bundle.getString("frmMain.HeaderNum.text") + ";");
			s.append(bundle.getString("frmMain.HeaderLat.text") + ";");
			s.append(bundle.getString("frmMain.HeaderLon.text") + ";");
			s.append(bundle.getString("frmMain.HeaderElev.text") + ";");
			s.append(bundle.getString("frmMain.HeaderElevNotSmoothed.text") + ";"); // "ElevationNotSmoothed" + ";");
																					// //TODO translate
			s.append(bundle.getString("frmMain.HeaderElevSmoothed.text") + ";"); // "ElevationSmoothed" + ";"); //TODO
																					// translate
			s.append(bundle.getString("frmMain.HeaderTag.text") + ";");
			s.append(bundle.getString("frmMain.HeaderDist.text") + ";");
			s.append(bundle.getString("frmMain.HeaderTotal.text") + ";");
			s.append(bundle.getString("frmMain.HeaderDiff.text") + ";");
			s.append(bundle.getString("frmMain.HeaderCoeff.text") + ";");
			s.append(bundle.getString("frmMain.HeaderRecovery.text") + ";");
			s.append(bundle.getString("frmMain.HeaderTime.text") + ";");
			s.append(bundle.getString("frmMain.HeaderTimeLimit.text") + ";");
			s.append(bundle.getString("frmMain.HeaderHour.text") + ";");
			s.append(bundle.getString("frmMain.HeaderStation.text") + ";");
			s.append(bundle.getString("frmMain.HeaderName.text") + ";");
			s.append(bundle.getString("frmMain.HeaderComment.text") + ";");
			s.append(bundle.getString("frmMain.HeaderSpeed.text") + ";"); // "Speed;"); //TODO translate
			s.append(bundle.getString("frmMain.HeaderSlope.text") + ";"); // "Slope;"); //TODO translate
			writer.println(s);
			s.setLength(0);

			for (int i = start; i <= end; i++) {
				CgData d = data.get(i);
				s.append(d.getNumString() + ";");
				s.append(decimalFormat7.format(d.getLatitude()) + ";");
				s.append(decimalFormat7.format(d.getLongitude()) + ";");
				s.append(d.getElevationString(unit, false) + ";"); // No decimal
				s.append(d.getElevationNotSmoothedString(unit, false) + ";"); // No decimal
				s.append(d.getElevationSmoothedString(unit, false) + ";"); // No decimal
				s.append(d.getTag() + ";");
				s.append(d.getDistString(unit, false) + ";"); // No decimal
				s.append(decimalFormat1.format(d.getTotal(unit)) + ";");
				s.append(decimalFormat1.format(d.getDiff()) + ";");
				s.append(decimalFormat1.format(d.getCoeff()) + ";");
				s.append(decimalFormat1.format(d.getRecovery()) + ";");
				s.append(d.getTimeString() + ";");
				s.append(d.getTimeLimitString(false) + ";");
				s.append(d.getHourString() + ";");
				s.append(d.getStationString(false) + ";");

				String str = d.getName();
				str = str.replace("\n", "").replace("\r", "").trim();
				s.append(str + ";");

				str = d.getComment();
				str = str.replace("\n", "").replace("\r", "").trim();
				s.append(str + ";");

				s.append(decimalFormat1.format(d.getSpeed(unit)) + ";");
				s.append(decimalFormat1.format(d.getSlope()) + ";");

				writer.println(s);
				s.setLength(0);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		CgLog.info("Save time : " + (System.currentTimeMillis() - ts) + "ms");
	}

	/**
	 * Calculate the night and day time, speed and distance
	 */
	public void CalcStatNight() {
		if (data.size() <= 0) {
			return;
		}

		tInNight.Init();
		tInDay.Init();

		boolean first = true;
		for (CgData r : data) {
			if (!first) {
				if (bNightCoeff && ((r.getHour().getSecondOfDay() >= StartNightTime.getSecondOfDay())
						|| (r.getHour().getSecondOfDay() <= EndNightTime.getSecondOfDay()))) {
					tInNight.Time = tInNight.Time + r.getdTime_f();
					tInNight.setSpeed(tInNight.getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
					tInNight.setDist(tInNight.getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
					tInNight.Cmpt++;
				} else {
					tInDay.Time = tInDay.Time + r.getdTime_f();
					tInDay.setSpeed(tInDay.getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
					tInDay.setDist(tInDay.getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
					tInDay.Cmpt++;
				}
			} else {
				first = false;
			}
		}
	}

	/**
	 * Calculate the slope statistic
	 */
	public void CalcStatSlope() {
		if (data.size() <= 0) {
			return;
		}

		for (StatData st : StatSlope)
			st.Init();

		int j = 0;
		boolean first = true;
		for (CgData r : data) {
			if (!first) {
				if (r.getSlope() < -40) {
					j = 0;
					StatSlope[j].setSpeed(StatSlope[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
					StatSlope[j].setDist(StatSlope[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
					StatSlope[j].Time = StatSlope[j].Time + r.getdTime_f();
					StatSlope[j].Cmpt++;
				} else if ((r.getSlope() >= -40) && (r.getSlope() < -30)) {
					j = 1;
					StatSlope[j].setSpeed(StatSlope[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
					StatSlope[j].setDist(StatSlope[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
					StatSlope[j].Time = StatSlope[j].Time + r.getdTime_f();
					StatSlope[j].Cmpt++;
				} else if ((r.getSlope() >= -30) && (r.getSlope() < -20)) {
					j = 2;
					StatSlope[j].setSpeed(StatSlope[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
					StatSlope[j].setDist(StatSlope[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
					StatSlope[j].Time = StatSlope[j].Time + r.getdTime_f();
					StatSlope[j].Cmpt++;
				} else if ((r.getSlope() >= -20) && (r.getSlope() < -10)) {
					j = 3;
					StatSlope[j].setSpeed(StatSlope[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
					StatSlope[j].setDist(StatSlope[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
					StatSlope[j].Time = StatSlope[j].Time + r.getdTime_f();
					StatSlope[j].Cmpt++;
				} else if ((r.getSlope() >= -10) && (r.getSlope() < -5)) {
					j = 4;
					StatSlope[j].setSpeed(StatSlope[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
					StatSlope[j].setDist(StatSlope[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
					StatSlope[j].Time = StatSlope[j].Time + r.getdTime_f();
					StatSlope[j].Cmpt++;
				} else if ((r.getSlope() >= -5) && (r.getSlope() < -2)) {
					j = 5;
					StatSlope[j].setSpeed(StatSlope[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
					StatSlope[j].setDist(StatSlope[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
					StatSlope[j].Time = StatSlope[j].Time + r.getdTime_f();
					StatSlope[j].Cmpt++;
				} else if ((r.getSlope() >= -2) && (r.getSlope() <= 2)) {
					j = 6;
					StatSlope[j].setSpeed(StatSlope[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
					StatSlope[j].setDist(StatSlope[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
					StatSlope[j].Time = StatSlope[j].Time + r.getdTime_f();
					StatSlope[j].Cmpt++;
				} else if ((r.getSlope() > 2) && (r.getSlope() <= 5)) {
					j = 7;
					StatSlope[j].setSpeed(StatSlope[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
					StatSlope[j].setDist(StatSlope[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
					StatSlope[j].Time = StatSlope[j].Time + r.getdTime_f();
					StatSlope[j].Cmpt++;
				} else if ((r.getSlope() > 5) && (r.getSlope() <= 10)) {
					j = 8;
					StatSlope[j].setSpeed(StatSlope[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
					StatSlope[j].setDist(StatSlope[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
					StatSlope[j].Time = StatSlope[j].Time + r.getdTime_f();
					StatSlope[j].Cmpt++;
				} else if ((r.getSlope() > 10) && (r.getSlope() <= 20)) {
					j = 9;
					StatSlope[j].setSpeed(StatSlope[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
					StatSlope[j].setDist(StatSlope[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
					StatSlope[j].Time = StatSlope[j].Time + r.getdTime_f();
					StatSlope[j].Cmpt++;
				} else if ((r.getSlope() > 20) && (r.getSlope() <= 30)) {
					j = 10;
					StatSlope[j].setSpeed(StatSlope[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
					StatSlope[j].setDist(StatSlope[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
					StatSlope[j].Time = StatSlope[j].Time + r.getdTime_f();
					StatSlope[j].Cmpt++;
				} else if ((r.getSlope() > 30) && (r.getSlope() <= 40)) {
					j = 11;
					StatSlope[j].setSpeed(StatSlope[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
					StatSlope[j].setDist(StatSlope[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
					StatSlope[j].Time = StatSlope[j].Time + r.getdTime_f();
					StatSlope[j].Cmpt++;
				} else if (r.getSlope() > 40) {
					j = 12;
					StatSlope[j].setSpeed(StatSlope[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
					StatSlope[j].setDist(StatSlope[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
					StatSlope[j].Time = StatSlope[j].Time + r.getdTime_f();
					StatSlope[j].Cmpt++;
				}
			} else {
				first = false;
			}
		} // foreach
	}

	/**
	 * Calculate the elevation statistic
	 */
	public void CalcStatElev() {
		if (data.size() <= 0) {
			return;
		}

		for (StatData st : StatElev)
			st.Init();

		for (StatData st : StatElevNight)
			st.Init();

		for (StatData st : StatElevDay)
			st.Init();

		int j = 0;
		boolean first = true;
		for (CgData r : data) {
			if (!first) {
				if (r.getElevation(CgConst.UNIT_METER) < 1000) {
					j = 0;
					StatElev[j].setSpeed(StatElev[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
					StatElev[j].setDist(StatElev[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
					StatElev[j].Time = StatElev[j].Time + r.getdTime_f();
					StatElev[j].Cmpt++;

					// Night
					if (bNightCoeff && ((r.getHour().getSecondOfDay() >= StartNightTime.getSecondOfDay())
							|| (r.getHour().getSecondOfDay() <= EndNightTime.getSecondOfDay()))) {
						StatElevNight[j].Time = StatElevNight[j].Time + r.getdTime_f();
						StatElevNight[j].setSpeed(
								StatElevNight[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
						StatElevNight[j]
								.setDist(StatElevNight[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
						StatElevNight[j].Cmpt++;
					} else // Day
					{
						StatElevDay[j].Time = StatElevDay[j].Time + r.getdTime_f();
						StatElevDay[j]
								.setSpeed(StatElevDay[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
						StatElevDay[j]
								.setDist(StatElevDay[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
						StatElevDay[j].Cmpt++;
					}
				} else if ((r.getElevation(CgConst.UNIT_METER) >= 1000)
						&& (r.getElevation(CgConst.UNIT_METER) < 1500)) {
					j = 1;
					StatElev[j].setSpeed(StatElev[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
					StatElev[j].setDist(StatElev[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
					StatElev[j].Time = StatElev[j].Time + r.getdTime_f();
					StatElev[j].Cmpt++;
					// Night
					if (bNightCoeff && ((r.getHour().getSecondOfDay() >= StartNightTime.getSecondOfDay())
							|| (r.getHour().getSecondOfDay() <= EndNightTime.getSecondOfDay()))) {
						StatElevNight[j].Time = StatElevNight[j].Time + r.getdTime_f();
						StatElevNight[j].setSpeed(
								StatElevNight[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
						StatElevNight[j]
								.setDist(StatElevNight[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
						StatElevNight[j].Cmpt++;
					} else // Day
					{
						StatElevDay[j].Time = StatElevDay[j].Time + r.getdTime_f();
						StatElevDay[j]
								.setSpeed(StatElevDay[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
						StatElevDay[j]
								.setDist(StatElevDay[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
						StatElevDay[j].Cmpt++;
					}
				} else if ((r.getElevation(CgConst.UNIT_METER) >= 1500)
						&& (r.getElevation(CgConst.UNIT_METER) < 2000)) {
					j = 2;
					StatElev[j].setSpeed(StatElev[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
					StatElev[j].setDist(StatElev[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
					StatElev[j].Time = StatElev[j].Time + r.getdTime_f();
					StatElev[j].Cmpt++;
					// Night
					if (bNightCoeff && ((r.getHour().getSecondOfDay() >= StartNightTime.getSecondOfDay())
							|| (r.getHour().getSecondOfDay() <= EndNightTime.getSecondOfDay()))) {
						StatElevNight[j].Time = StatElevNight[j].Time + r.getdTime_f();
						StatElevNight[j].setSpeed(
								StatElevNight[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
						StatElevNight[j]
								.setDist(StatElevNight[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
						StatElevNight[j].Cmpt++;
					} else // Day
					{
						StatElevDay[j].Time = StatElevDay[j].Time + r.getdTime_f();
						StatElevDay[j]
								.setSpeed(StatElevDay[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
						StatElevDay[j]
								.setDist(StatElevDay[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
						StatElevDay[j].Cmpt++;
					}
				} else if ((r.getElevation(CgConst.UNIT_METER) >= 2000)
						&& (r.getElevation(CgConst.UNIT_METER) < 2500)) {
					j = 3;
					StatElev[j].setSpeed(StatElev[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
					StatElev[j].setDist(StatElev[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
					StatElev[j].Time = StatElev[j].Time + r.getdTime_f();
					StatElev[j].Cmpt++;
					// Night
					if (bNightCoeff && ((r.getHour().getSecondOfDay() >= StartNightTime.getSecondOfDay())
							|| (r.getHour().getSecondOfDay() <= EndNightTime.getSecondOfDay()))) {
						StatElevNight[j].Time = StatElevNight[j].Time + r.getdTime_f();
						StatElevNight[j].setSpeed(
								StatElevNight[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
						StatElevNight[j]
								.setDist(StatElevNight[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
						StatElevNight[j].Cmpt++;
					} else // Day
					{
						StatElevDay[j].Time = StatElevDay[j].Time + r.getdTime_f();
						StatElevDay[j]
								.setSpeed(StatElevDay[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
						StatElevDay[j]
								.setDist(StatElevDay[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
						StatElevDay[j].Cmpt++;
					}
				} else if ((r.getElevation(CgConst.UNIT_METER) >= 2500)
						&& (r.getElevation(CgConst.UNIT_METER) < 3000)) {
					j = 4;
					StatElev[j].setSpeed(StatElev[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
					StatElev[j].setDist(StatElev[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
					StatElev[j].Time = StatElev[j].Time + r.getdTime_f();
					StatElev[j].Cmpt++;
					// Night
					if (bNightCoeff && ((r.getHour().getSecondOfDay() >= StartNightTime.getSecondOfDay())
							|| (r.getHour().getSecondOfDay() <= EndNightTime.getSecondOfDay()))) {
						StatElevNight[j].Time = StatElevNight[j].Time + r.getdTime_f();
						StatElevNight[j].setSpeed(
								StatElevNight[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
						StatElevNight[j]
								.setDist(StatElevNight[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
						StatElevNight[j].Cmpt++;
					} else // Day
					{
						StatElevDay[j].Time = StatElevDay[j].Time + r.getdTime_f();
						StatElevDay[j]
								.setSpeed(StatElevDay[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
						StatElevDay[j]
								.setDist(StatElevDay[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
						StatElevDay[j].Cmpt++;
					}
				} else if ((r.getElevation(CgConst.UNIT_METER) >= 3000)) {
					j = 5;
					StatElev[j].setSpeed(StatElev[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
					StatElev[j].setDist(StatElev[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
					StatElev[j].Time = StatElev[j].Time + r.getdTime_f();
					StatElev[j].Cmpt++;
					// Night
					if (bNightCoeff && ((r.getHour().getSecondOfDay() >= StartNightTime.getSecondOfDay())
							|| (r.getHour().getSecondOfDay() <= EndNightTime.getSecondOfDay()))) {
						StatElevNight[j].Time = StatElevNight[j].Time + r.getdTime_f();
						StatElevNight[j].setSpeed(
								StatElevNight[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
						StatElevNight[j]
								.setDist(StatElevNight[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
						StatElevNight[j].Cmpt++;
					} else // Day
					{
						StatElevDay[j].Time = StatElevDay[j].Time + r.getdTime_f();
						StatElevDay[j]
								.setSpeed(StatElevDay[j].getSpeed(CgConst.UNIT_METER) + r.getSpeed(CgConst.UNIT_METER));
						StatElevDay[j]
								.setDist(StatElevDay[j].getDist(CgConst.UNIT_METER) + r.getDist(CgConst.UNIT_METER));
						StatElevDay[j].Cmpt++;
					}
				}
			} else {
				first = false;
			}
		}
	}

	/**
	 * Find the nearest point in the point list
	 * 
	 * @param lat latitude of the point
	 * @param lon longitude of the point
	 * @return index of the nearest point in the point list
	 */
	public int FindNearestPoint(double lat, double lon) {
		double a, c, dDistance, dLat1InRad, dLong1InRad, dLat2InRad, dLong2InRad, dLongitude, dLatitude;
		double kEarthRadiusKms;
		double min;
		int index = -1;
		int i = 0;

		min = 99999999;

		double k = Math.PI / 180.0;

		dLat1InRad = lat * k;
		dLong1InRad = lon * k;

		for (CgData r : data) {
			kEarthRadiusKms = 6378.14; // 6376.5

			dDistance = 0; // Double.MinValue
			dLat2InRad = r.getLatitude() * k;
			dLong2InRad = r.getLongitude() * k;

			dLongitude = dLong2InRad - dLong1InRad;
			dLatitude = dLat2InRad - dLat1InRad;

			// Intermediate result a.
			a = Math.pow(Math.sin(dLatitude / 2.0), 2.0)
					+ Math.cos(dLat1InRad) * Math.cos(dLat2InRad) * Math.pow(Math.sin(dLongitude / 2.0), 2.0);

			// Intermediate result c (great circle distance in Radians)
			c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));

			// Distance.
			dDistance = kEarthRadiusKms * c * 1000.0;
			if (dDistance < min) {
				min = dDistance;
				index = i;
			}

			i++;
		}

		// Résultat en mètres
		return index;
	}

	/**
	 * Return the minimum elevation of the track
	 * 
	 * @param unit Unit wanted (To get from the settings)
	 * @return Minimum elevation of the track
	 */
	public double getMinElev(int unit) {
		switch (unit) {
		case CgConst.UNIT_METER:
			return MinElev;
		case CgConst.UNIT_MILES_FEET:
			// meter to miles
			return Utils.Meter2Feet(MinElev);
		default:
			return MinElev;
		}
	}

	/**
	 * Return the maximum elevation of the track
	 * 
	 * @param unit Unit wanted (To get from the settings)
	 * @return Maximum elevation of the track
	 */
	public double getMaxElev(int unit) {
		switch (unit) {
		case CgConst.UNIT_METER:
			return MaxElev;
		case CgConst.UNIT_MILES_FEET:
			// meter to miles
			return Utils.Meter2Feet(MaxElev);
		default:
			return MaxElev;
		}
	}

	public double getClimbP(int unit) {
		switch (unit) {
		case CgConst.UNIT_METER:
			return ClimbP;
		case CgConst.UNIT_MILES_FEET:
			// meter to miles
			return Utils.Meter2Feet(ClimbP);
		default:
			return ClimbP;
		}
	}

	/**
	 * Set climbP variable
	 * 
	 * @param climbP Value in meter
	 */
	public void setClimbP(double climbP) {
		ClimbP = climbP;
	}

	public double getClimbM(int unit) {
		switch (unit) {
		case CgConst.UNIT_METER:
			return ClimbM;
		case CgConst.UNIT_MILES_FEET:
			// meter to miles
			return Utils.Meter2Feet(ClimbM);
		default:
			return ClimbM;
		}
	}

	/**
	 * Set climbM variable
	 * 
	 * @param climbM Value in meter
	 */
	public void setClimbM(double climbM) {
		ClimbM = climbM;
	}

	/**
	 * Return the road distance on the track
	 * 
	 * @param unit Unit for the returned value
	 * @return Road distance in meter
	 */
	public double getDistRoad(int unit) {
		switch (unit) {
		case CgConst.UNIT_METER:
			return DistRoad;
		case CgConst.UNIT_MILES_FEET:
			// meter to miles
			return Utils.Meter2uMiles(DistRoad);
		default:
			return DistRoad;
		}
	}

	/**
	 * Set distRoad variable
	 * 
	 * @param distRoad Value in meter
	 */
	public void setDistRoad(double distRoad) {
		DistRoad = distRoad;
	}

	/**
	 * Copy the current track to another
	 * 
	 * @param d track object where to copy the current track
	 * @return track object where the current is copied
	 */
	public TrackData CopyTo(TrackData d) {
		int i = 0;

		d.param = param;
		d.Paramfile = Paramfile;

		d.data.clear();
		for (CgData r : data) {
			CgData n = new CgData();
			n = r.CopyTo(n);

			d.data.add(n);
		}

		d.tInNight = tInNight.CopyTo(d.tInNight);
		d.tInDay = tInDay.CopyTo(d.tInDay);

		int n = StatSlope.length;
		d.StatSlope = new StatData[StatSlope.length];
		if (n >= 0) {
			for (i = 0; i < n; i++) {
				d.StatSlope[i] = new StatData();
				d.StatSlope[i] = StatSlope[i].CopyTo(d.StatSlope[i]);
			}
		}

		n = StatElev.length;
		d.StatElev = new StatData[StatElev.length];
		if (n >= 0) {
			for (i = 0; i < n; i++) {
				d.StatElev[i] = new StatData();
				d.StatElev[i] = StatElev[i].CopyTo(d.StatElev[i]);
			}
		}

		n = StatElevNight.length;
		d.StatElevNight = new StatData[StatElevNight.length];
		if (n >= 0) {
			for (i = 0; i < n; i++) {
				d.StatElevNight[i] = new StatData();
				d.StatElevNight[i] = StatElevNight[i].CopyTo(d.StatElevNight[i]);
			}
		}

		n = StatElevDay.length;
		d.StatElevDay = new StatData[StatElevDay.length];
		if (n >= 0) {
			for (i = 0; i < n; i++) {
				d.StatElevDay[i] = new StatData();
				d.StatElevDay[i] = StatElevDay[i].CopyTo(d.StatElevDay[i]);
			}
		}

		d.Name = d.Name;
		d.CourseName = CourseName;
		d.TotalDistance = TotalDistance;
		d.TotalTime = TotalTime;
		d.isTimeLoaded = isTimeLoaded;
		d.isCalculated = isCalculated;
		d.isModified = isModified;
		d.ClimbP = ClimbP;
		d.ClimbM = ClimbM;
		d.AscTime = AscTime;
		d.DescTime = DescTime;
		d.Description = Description;
		d.isAutoCalc = isAutoCalc;
		d.StartTime = StartTime;
		d.MinElev = MinElev;
		d.MaxElev = MaxElev;
		d.StartGlobalCoeff = StartGlobalCoeff;
		d.EndGlobalCoeff = EndGlobalCoeff;
		d.isTimeLimit = isTimeLimit;
		d.TimeLimit_Line = TimeLimit_Line;
		d.timeZoneOffsetHours = timeZoneOffsetHours;
		d.timeZoneId = timeZoneId;
		d.TrackUseDaylightSaving = TrackUseDaylightSaving;
		d.StartSpeed = StartSpeed;
		d.EndSpeed = EndSpeed;
		d.DistRoad = DistRoad;
		d.ReadError = ReadError;
		d.ReadLineError = ReadLineError;
		d.StartNightTime = StartNightTime;
		d.EndNightTime = EndNightTime;
		d.bNightCoeff = bNightCoeff;
		d.NightCoeffAsc = NightCoeffAsc;
		d.NightCoeffDesc = NightCoeffDesc;
		d.bElevEffect = bElevEffect;
		d.MrbSizeW = MrbSizeW;
		d.MrbSizeH = MrbSizeH;
		d.CurveFilter = CurveFilter;
		d.WordWrapLength = WordWrapLength;
		d.LabelToBottom = LabelToBottom;
		d.MRBType = MRBType;
		d.TopMargin = TopMargin;
		d.bShowNightDay = bShowNightDay;
		d.ReadOnly = ReadOnly;
		return d;
	}

	public void determineTrackTimeZone() {
		if (this.data == null || this.data.isEmpty())
			return;

		timeZoneId = Utils.getTimeZoneFromLatLon(this.data.get(0).getLatitude(), this.data.get(0).getLongitude())
				.getID();
		timeZoneOffsetHours = Utils.hoursUTCOffsetFromLatLon(this.data.get(0).getLatitude(),
				this.data.get(0).getLongitude());
	}

	public void determineSunriseSunsetTimes() {
		if (this.data == null || this.data.isEmpty())
			return;

		if (timeZoneId.equals(""))
			determineTrackTimeZone();

		EndNightTime = Utils.determineSunRiseTimes(StartTime, this.data.get(0).getLatitude(),
				this.data.get(0).getLongitude(), timeZoneId);
		StartNightTime = Utils.determineSunsetTimes(StartTime, this.data.get(0).getLatitude(),
				this.data.get(0).getLongitude(), timeZoneId);

	}

	/**
	 * Copy the smoothed elevation in the elevation field
	 */
	public void SelectSmoothedElevation() {
		if (this.data == null || this.data.isEmpty())
			return;

		for (CgData r : data) {
			r.setElevation(r.getElevationSmoothed(CgConst.UNIT_METER));
		}
	}

	/**
	 * Copy the not smoothed elevation in the elevation field
	 */
	public void SelectNotSmoothedElevation() {
		if (this.data == null || this.data.isEmpty())
			return;

		for (CgData r : data) {
			r.setElevation(r.getElevationNotSmoothed(CgConst.UNIT_METER));
		}
	}

	/**
	 * Copy the Not smoothed elevation in the smoothed elevation field
	 */
	public void CopyNotSmoothedInSmoothedElevation() {
		if (this.data == null || this.data.isEmpty())
			return;

		for (CgData r : data) {
			r.setElevationSmoothed(r.getElevationNotSmoothed(CgConst.UNIT_METER));
		}
	}

	/*
	 * Keep these 2 methods because they can be used to remove enormous slope. Maybe
	 * a bit of debug is necessary because input data are not the right ones (due
	 * the method of calculation of dist and slope)
	 * 
	 * public double CalcNewSlope(double elevPrev, double elevNext, double distPrev,
	 * double distNext) {
	 * 
	 * double de = elevNext-elevPrev; double dd = distPrev-distNext; double
	 * value=elevPrev+(de*distPrev/dd); return value; }
	 * 
	 * public void FixIncorrectSlope() { if (this.data == null ||
	 * this.data.isEmpty()) return;
	 * 
	 * for (int i=0; i<data.size()-1; i++) { CgData act = data.get(i); if
	 * (Math.abs(act.getSlope())>50) { CgData prev = data.get(i-1); CgData next =
	 * data.get(i+1); act.setElevation(CalcNewSlope(
	 * prev.getdElevation(CgConst.UNIT_METER),
	 * next.getdElevation(CgConst.UNIT_METER), prev.getDist(CgConst.UNIT_METER),
	 * next.getDist(CgConst.UNIT_METER) ) ); }
	 * //r.setElevationSmoothed(r.getElevationNotSmoothed(CgConst.UNIT_METER)); } }
	 */

} // TrackData
