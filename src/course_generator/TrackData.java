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

//import static course_generator.utils.Utils.ReadXMLDouble;
//import static course_generator.utils.Utils.ReadXMLInt;
//import static course_generator.utils.Utils.ReadXMLString;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.joda.time.DateTime;

import course_generator.param.ParamData;
/*
import course_generator.table.CoeffClass;
import course_generator.table.DiffClass;
import course_generator.table.DistClass;
import course_generator.table.ElevationClass;
import course_generator.table.HourClass;
import course_generator.table.LatClass;
import course_generator.table.LonClass;
import course_generator.table.RecupClass;
import course_generator.table.StationClass;
import course_generator.table.TimeClass;
import course_generator.table.TimelimitClass;
import course_generator.table.TotalClass;
*/
import course_generator.utils.CgConst;
import course_generator.utils.StatData;
import course_generator.utils.Utils;
import course_generator.utils.Utils.CalcLineResult;

/**
 *
 * @author pierre.delore
 */
public class TrackData {

	private static TrackData	instance;

	/** Slope/Speed parameters **/
	public ParamData			param					= null;
	/** Parameters file name **/
	public String				Paramfile				= "";

	/** Arraylist containing the main data **/
	public ArrayList<CgData>	data;

	/** Statistics data for 'in night' **/
	public StatData				tInNight;
	/** Statistics data for 'in day' **/
	public StatData				tInDay;

	/** Statistics data for 'slope' **/
	public StatData[]			StatSlope;
	/** Statistics data for 'elevation' **/
	public StatData[]			StatElev;
	/** Statistics data for 'elevation during night' **/
	public StatData[]			StatElevNight;
	/** Statistics data for 'elevation during day' **/
	public StatData[]			StatElevDay;

	/** Track name **/
	public String				Name;										// Track
																			// name
	/** Name of the track that appear in the track setting **/
	public String				CourseName				= "";
	/** Total distance in meters **/
	private double				TotalDistance			= 0.0;
	/** Total time in seconde **/
	public int					TotalTime				= 0;
	/** Indicate if during the last loading the time field as been loaded **/
	public boolean				isTimeLoaded			= false;
	/**
	 * Indicate if the data has been calculated. false=They must be calculate
	 **/
	public boolean				isCalculated			= false;
	/** If 'true' this indicate that the data has been modified **/
	public boolean				isModified				= false;
	/** Contain the ascent climb of the whole track (in m) **/
	public double				ClimbP					= 0.0;
	/** Contain the descent climb of the whole track (in m) **/
	public double				ClimbM					= 0.0;
	/** Contain the ascent time of the whole track (in s) **/
	public int					AscTime					= 0;
	/** Contain the descent time of the whole track (in s) **/
	public int					DescTime				= 0;
	/** Description of the track **/
	public String				Description				= "";
	/** Indicate if the autocalc function is active **/
	public boolean				isAutoCalc				= false;
	/** Start time of the track **/
	public DateTime				StartTime				= new DateTime();
	/** Minimum elevation of the track (in m) **/
	private double				MinElev					= -1;
	/** Maximum elevation of the track (in m) **/
	private double				MaxElev					= -1;
	/** Global start health coefficient **/
	public double				StartGlobalCoeff		= 100;
	/** Global end health coefficient **/
	public double				EndGlobalCoeff			= 100;
	/** If 'true' this indicate that the time limit has been reached **/
	public boolean				isTimeLimit				= false;
	/** Indicate where the timelimit has been reached (-1 if none) **/
	public int					TimeLimit_Line			= -1;
	/** Timezone for the sunrise/sunset calculation **/
	public Double				TrackTimeZone			= 0.0;
	/** Are we using the summer time for the sunrise/sunset calculation **/
	public boolean				TrackUseSumerTime		= false;
	public double				StartSpeed				= 0.0;
	public double				EndSpeed				= 0.0;
	/** Number of meter of road in the track **/
	public double				DistRoad				= 0.0;
	/** Indicate the type of error during the file reading **/
	public int					ReadError				= 0;
	/** Indicate the at which line the error appear **/
	public int					ReadLineError			= 0;

	// -- Night Coeff --
	/** Start night time **/
	public DateTime				StartNightTime;
	/** End night time **/
	public DateTime				EndNightTime;
	/** If 'true' this indicate that the night coefficients are used ***/
	public boolean				bNightCoeff				= false;
	/** Ascent nigth coefficient (100%=normal) **/
	public double				NightCoeffAsc			= 100.0;
	/** Descent nigth coefficient (100%=normal) **/
	public double				NightCoeffDesc			= 100.0;

	// -- Elevation coeff
	/** Indicate that the elevation effect is used during the calculation **/
	public boolean				bElevEffect				= false;

	// -- Profil mini-roadbook
	/** Width of the mini roadbook (in pixels) **/
	public int					MrbSizeW				= 640;
	/** Height of the mini roadbook (in pixels) **/
	public int					MrbSizeH				= 480;
	/** Curve filter in the mini roadbook **/
	public int					CurveFilter				= 1;
	/** Number of characters before the line is word wrapped **/
	public int					WordWrapLength			= 25;
	/** Position of the label. 'true'=bottom **/
	public boolean				LabelToBottom			= false;
	/** Type of profil in the mini roadbook **/
	public int					MRBType					= 0;
	/** Top margin size in pixels in the mini roadbook **/
	public int					TopMargin				= 100;

	// -- Profil colors
	public Color				clProfil_Simple_Fill	= Color.BLACK;
	public Color				clProfil_Simple_Border	= Color.BLACK;
	public Color				clProfil_RS_Road		= Color.BLACK;
	public Color				clProfil_RS_Path		= Color.BLACK;
	public Color				clProfil_RS_Border		= Color.BLACK;

	public Color				clProfil_SlopeInf5		= Color.BLACK;
	public Color				clProfil_SlopeInf10		= Color.BLACK;
	public Color				clProfil_SlopeInf15		= Color.BLACK;
	public Color				clProfil_SlopeSup15		= Color.BLACK;
	public Color				clProfil_SlopeBorder	= Color.BLACK;


	// -- Constructor --
	public TrackData() {

		Name = "";
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

		StartNightTime = new DateTime(2010, 1, 1, 0, 0, 0);
		EndNightTime = new DateTime(2010, 1, 1, 0, 0, 0);

		MrbSizeW = 640;
		MrbSizeH = 480;
		CurveFilter = 1;
		WordWrapLength = 25;

		DefaultMRBProfilSimpleColor();
		DefaultMRBProfilRSColor();
		DefaultMRBProfilSlopeColor();
	}


	public static synchronized TrackData getInstance() {
		if (instance == null) {
			instance = new TrackData();
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
	 * @param unit
	 *            Unit
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


	/**
	 * Read a GPX fileand store the data in the array
	 * 
	 * @param name
	 *            Full name of the file
	 * @param mode
	 *            0 = Replace the existing data by the new data 1 = Insert the
	 *            new data at the beginning of the existing data 2 = Add the new
	 *            data at the end of the existing data
	 * @return Return true if time data have been loaded
	 * @throws Exception
	 */
	public boolean OpenGPX(String name, int mode) throws Exception {
		SaxGPXHandler GPXhandler = new SaxGPXHandler();

		int ret = GPXhandler.readDataFromGPX(name, this, mode);
		if (ret != 0)
			System.out.println("Error line =" + GPXhandler.getErrLine());

		int cmpt = 1;

		for (CgData r : data) {
			r.setNum(cmpt);
			cmpt++;
		}

		isCalculated = false;
		isModified = false;
		CalcDist();
		CalcSpeed();
		CalcSlope();

		CalcClimbResult resClimb = new CalcClimbResult();
		CalcClimb(0, data.size() - 1, resClimb); // ref ClimbP, ref ClimbM, ref
													// AscTime, ref DescTime);
		ClimbP = resClimb.cp;
		ClimbM = resClimb.cm;
		AscTime = resClimb.tp;
		DescTime = resClimb.tm;

		TotalTime = CalcHour();

		SearchMinMaxElevationResult resMinMaxElev = new SearchMinMaxElevationResult();
		resMinMaxElev = SearchMinMaxElevation(0, (data.size() - 1), resMinMaxElev); // ref
																					// MinElev,
																					// ref
																					// MaxElev);
		MinElev = resMinMaxElev.min;
		MaxElev = resMinMaxElev.max;

		isCalculated = true;
		Name = new File(name).getName();

		return isTimeLoaded;
	} // -- OpenGPX


	// -- Save GPX file (complet or partial) --
	/**
	 * Save the track in GPX format
	 * 
	 * @param name
	 *            Name of the file
	 * @param start
	 *            Index of the first point to save
	 * @param end
	 *            Index of the last point to save
	 */
	public void SaveGPX(String name, int start, int end) {
		/*
		 * <?xml version="1.0"?> <gpx creator=
		 * "GPS Visualizer http://www.gpsvisualizer.com/" version="1.0"
		 * xmlns="http://www.topografix.com/GPX/1/0"
		 * xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		 * xsi:schemaLocation=
		 * "http://www.topografix.com/GPX/1/0 http://www.topografix.com/GPX/1/0/gpx.xsd"
		 * > <trk> <name>Lamontagnhard</name>
		 * <desc>http://www.lamontagnhard.fr</desc> <trkseg> <trkpt
		 * lat="45.8547528" lon="6.7226378"> <ele>1180.4</ele>
		 * <time>2010-06-11T11:41:10.000Z</time> </trkpt> </trkseg> </trk>
		 * </gpx>
		 */

		if (data.size() <= 0) {
			return;
		}

		// -- Save the data in the home directory
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		try {
			XMLStreamWriter writer = factory.createXMLStreamWriter(new FileOutputStream(name), "UTF-8");

			// writer.writeStartDocument("UTF-8", "1.0");
			// writer.writeComment("Course Generator (C) Pierre DELORE");
			// writer.writeStartElement("CONFIG");
			// Utils.WriteStringToXML(writer, "PARAMFILE", ParamFile);

			// DateTime dt = new DateTime();

			writer.writeStartDocument("UTF-8", "1.0");
			writer.writeStartElement("gpx");
			Utils.WriteStringToXML(writer, "creator", "Course Generator http://www.techandrun.com");
			Utils.WriteStringToXML(writer, "version", "1.1");

			Utils.WriteStringToXML(writer, "xsi:schemaLocation",
					"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd http://www.garmin.com/xmlschemas/WaypointExtension/v1 http://www8.garmin.com/xmlschemas/WaypointExtensionv1.xsd http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www8.garmin.com/xmlschemas/GpxExtensionsv3.xsd http://www.garmin.com/xmlschemas/ActivityExtension/v1 http://www8.garmin.com/xmlschemas/ActivityExtensionv1.xsd http://www.garmin.com/xmlschemas/AdventuresExtensions/v1 http://www8.garmin.com/xmlschemas/AdventuresExtensionv1.xsd");
			Utils.WriteStringToXML(writer, "xmlns", "http://www.topografix.com/GPX/1/1");
			Utils.WriteStringToXML(writer, "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			Utils.WriteStringToXML(writer, "xmlns:wptx1", "http://www.garmin.com/xmlschemas/WaypointExtension/v1");
			Utils.WriteStringToXML(writer, "xmlns:gpxtrx", "http://www.garmin.com/xmlschemas/GpxExtensions/v3");
			Utils.WriteStringToXML(writer, "xmlns:gpxtpx", "http://www.garmin.com/xmlschemas/TrackPointExtension/v1");
			Utils.WriteStringToXML(writer, "xmlns:gpxx", "http://www.garmin.com/xmlschemas/GpxExtensions/v3");
			Utils.WriteStringToXML(writer, "xmlns:trp", "http://www.garmin.com/xmlschemas/TripExtensions/v1");
			Utils.WriteStringToXML(writer, "xmlns:adv", "http://www.garmin.com/xmlschemas/AdventuresExtensions/v1");

			// <trk> node
			writer.writeStartElement("trk");

			// <name> node
			Utils.WriteStringToXML(writer, "name", CourseName);

			// <trkseg> node
			writer.writeStartElement("trkseg");

			for (int i = start; i <= end; i++) {
				CgData r = data.get(i);

				// <trkpt>
				// <trkpt lat="45.8547528" lon="6.7226378">
				writer.writeStartElement("trkpt");
				writer.writeAttribute("lat", String.format(Locale.ROOT, "%1.7f", r.getLatitude()));
				writer.writeAttribute("lon", String.format(Locale.ROOT, "%1.7f", r.getLongitude()));

				// Utils.WriteStringToXML(writer, "lat", String.Format(culture,
				// "{0:0.0000000}", r.Latitude));
				// Utils.WriteStringToXML(writer, "lon", String.Format(culture,
				// "{0:0.0000000}", r.Longitude));

				// <ele>1180.4</ele>
				Utils.WriteStringToXML(writer, "ele", String.format(Locale.ROOT, "%1.7f", r.getElevation()));

				// <time>2010-08-18T07:57:07.000Z</time>
				// dt = r.getHour().ToUniversalTime();
				Utils.WriteStringToXML(writer, "time", r.getHour().toString());

				// <name>toto</name>
				Utils.WriteStringToXML(writer, "name", String.valueOf(i));

				writer.writeEndElement();// Trkpt
			} // for
			writer.writeEndElement();// Trkseg
			writer.writeEndElement();// trk
			writer.writeEndElement();// gpx
			writer.writeEndDocument();
			writer.flush();
			writer.close();
			isModified = false;
			Name = new File(name).getName();
		} catch (XMLStreamException | IOException e) {
			System.out.println(e.getMessage());
		}
	}


	// -- Save tags as waypoint in a GPX file --
	public void SaveWaypoint(String name, int mask) {
		if (data.size() <= 0) {
			return;
		}

		/*
		 * DateTime dt = new DateTime();
		 * 
		 * XmlTextWriter wr = new XmlTextWriter(name, Encoding.UTF8);
		 * wr.Formatting = Formatting.Indented; wr.WriteStartDocument(true);
		 * 
		 * 
		 * wr.WriteStartElement("gpx"); wr.WriteStringToXML("xmlns",
		 * "http://www.topografix.com/GPX/1/1"); wr.WriteStringToXML("creator",
		 * "Course Generator http://dpsite.free.fr");
		 * wr.WriteStringToXML("version", "1.1");
		 * wr.WriteStringToXML("xmlns:xsi",
		 * "http://www.w3.org/2001/XMLSchema-instance");
		 * wr.WriteStringToXML("xsi:schemaLocation",
		 * "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd"
		 * );
		 * 
		 * //foreach (cgData r in data) int i = 1; String s; foreach (cgData r
		 * in data) { if ((r.Tag != 0) && ((r.Tag & mask)!=0)) { // <wpt>
		 * wr.WriteStartElement("wpt"); wr.WriteStringToXML("lat",
		 * String.Format(culture, "{0:0.0000000}", r.Latitude));
		 * wr.WriteStringToXML("lon", String.Format(culture, "{0:0.0000000}",
		 * r.Longitude));
		 * 
		 * // <time>2010-08-18T07:57:07.000Z</time> dt =
		 * r.Hour.ToUniversalTime(); wr.WriteElementString("time",
		 * dt.ToString("s") + "Z");
		 * 
		 * //<name>toto</name> if (r.Name == "") { wr.WriteElementString("name",
		 * "NoName" + i.ToString()); i++; } else wr.WriteElementString("name",
		 * r.Name);
		 * 
		 * 
		 * //<sym>Flag, Green</sym> s = "Flag, Green"; //Par defaut if ((r.Tag &
		 * CgConst.TAG_HIGH_PT) != 0) s = "Summit"; if ((r.Tag &
		 * CgConst.TAG_WATER_PT) != 0) s = "Bar"; if ((r.Tag &
		 * CgConst.TAG_EAT_PT) != 0) s = "Restaurant";
		 * 
		 * wr.WriteElementString("sym", s);
		 */
		/*
		 * <extensions> <gpxx:WaypointExtension
		 * xmlns:gpxx="http://www.garmin.com/xmlschemas/GpxExtensions/v3">
		 * <gpxx:DisplayMode>SymbolAndName</gpxx:DisplayMode>
		 * </gpxx:WaypointExtension> </extensions>
		 */
		/*
		 * wr.WriteStartElement("extensions");
		 * wr.WriteStartElement("gpxx:WaypointExtension");
		 * wr.WriteStringToXML("xmlns:gpxx",
		 * "http://www.garmin.com/xmlschemas/GpxExtensions/v3");
		 * wr.WriteElementString("gpxx:DisplayMode", "SymbolAndName");
		 * wr.WriteEndElement();//gpxx:WaypointExtension
		 * wr.WriteEndElement();//extensions
		 * 
		 * wr.WriteEndElement();//wpt } }//foreach wr.WriteEndElement();//gpx
		 * wr.WriteEndDocument(); wr.Flush(); wr.Close();
		 */
	}

	/**
	 * Class used to store the result of a point search
	 * 
	 * @author pierre
	 *
	 */
	public static class SearchPointResult {

		public double	Distance;	// Distance in meter
		public int		Point;		// -1= no point


		public SearchPointResult(int _point, double _distance) {
			Distance = _distance;
			Point = _point;
		}
	}


	/**
	 * Search the best point from the latitude and longitude
	 * 
	 * @param lat
	 *            Latitude of the point to search
	 * @param lon
	 *            Longitude of the point to search
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


	// -- Calculate Distance ---
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
			Ele = r.getElevation();
			if (b) {
				dist = CalcDistance(mLat, mLon, Lat, Lon);
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
	public void CalcSpeed() {
		boolean b = false;
		for (CgData r : data) {
			if (b) {
				if (r.getdTime_f() != 0.0) {
					r.setSpeed(r.getDist() / r.getdTime_f() * 3.6);
				} else {
					r.setSpeed(0.0);
				}
			} else {
				r.setSpeed(0.0);
				b = true;
			} // if
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
			Ele = (double) r.getElevation();

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

		public double AvrSlopeP, AvrSlopeM, TotClimbP, TotFlat, TotClimbM;
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
					r.TotClimbM += data.get(i).getDist();
				} else if ((slope > -2) && (slope < 2)) {
					r.TotFlat += data.get(i).getDist();
				} else if (slope >= 2) {
					r.TotClimbP += data.get(i).getDist();
				}

			} // for i
		} // if
		r.AvrSlopeP = r.AvrSlopeP / ip;
		r.AvrSlopeM = r.AvrSlopeM / im;
		return r;
	} // CalcAvrSlope

	public static class CalcClimbResult {
		public double	cp, cm;
		public int		tp, tm;
	}


	// -- Calculate climb - and + ---
	// cp: cumul D+ (m)
	// cm: cumul D- (m)
	// tp: cumul temps en montée (s)
	// tm: cumul temps en descente (s)
	public CalcClimbResult CalcClimb(int StartLine, int EndLine, CalcClimbResult r) {
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
			oldElev = data.get(StartLine).getElevation();
			oldTime = data.get(StartLine).getTime();

			for (i = StartLine; i <= EndLine; i++) {
				elev = data.get(i).getElevation();
				time = data.get(i).getTime();

				de = (elev - oldElev);
				dt = (time - oldTime);
				if (Math.abs(de) > CgConst.MIN_ELEV) {
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

		public double avrspeed;
	}


	// -- Calculate the average speed between 2 points
	public CalcAvrSpeedResult CalcAvrSpeed(int StartLine, int EndLine, CalcAvrSpeedResult r) {
		int t = 0;

		if (data.size() > 0) {
			t = (data.get(EndLine).getTime() - data.get(StartLine).getTime());
			if (t != 0) {
				r.avrspeed = (data.get(EndLine).getTotal() - data.get(StartLine).getTotal()) * 3.600 / t;
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


	// -- Calculate road distance --
	public void CalcRoad() {
		DistRoad = 0.0;
		for (CgData r : data) {
			if (r.getDiff() == 100) {
				DistRoad = DistRoad + r.getDist();
			}
		}
	}

	class SearchMinMaxElevationResult {

		public double min, max;
	}


	public SearchMinMaxElevationResult SearchMinMaxElevation(int start, int end, SearchMinMaxElevationResult r) {
		r.min = 9999.0;
		r.max = -1.0;
		int i = 0;
		for (i = start; i < end; i++) {
			double e = data.get(i).getElevation();
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
	 * r.Elevation; if (e < MinElev) MinElev = e; if (e > MaxElev) MaxElev = e;
	 * } }
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

		String sParamfile = Utils.GetHomeDir() + "/"+CgConst.CG_DIR+"/" + Paramfile + ".par";

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
				tmp = param.data.get(j).Slope;
				if (tmp >= x) {
					k = j - 1;
					if (j == 0) {
						k = 0;
					}
					x1 = param.data.get(k).Slope;
					y1 = param.data.get(k).Speed;
					x2 = param.data.get(j).Slope;
					y2 = param.data.get(j).Speed;
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
				y = (double) param.data.get(param.data.size() - 2).Speed;
			}

			// --Night coeff --
			DateTime t = r.getHour();
			if (bNightCoeff
					&& ((Utils.CompareHMS(t, StartNightTime) >= 0) || (Utils.CompareHMS(t, EndNightTime) <= 0))) {
				if (r.getSlope() < -2.0) {
					night = 100.0 / NightCoeffDesc;
				} else {
					night = 100.0 / NightCoeffAsc;
				}
			} else {
				night = 1;
			}
			// --Elev effect --
			if (bElevEffect && ((double) r.getElevation() > 1500.0)) {
				ef = 1.0 + (Math.round(((double) r.getElevation() - 1500.0) / 100.0) / 100.0);
			} else {
				ef = 1.0;
			}
			// --
			double dist = r.getDist();
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

		TotalTime = (int) Math.round(dt);
		isCalculated = true;
		isModified = true;
	} // Calculate


	/**
	 * Calc the min / max of the datas
	 */
	public void CalcMinMax() {
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

		// Remise à zéro des bits 0 et 1 du champ TAG
		for (CgData r : data)
			r.setTag((int) r.getTag() & 0xFC);

		// Boucle principale
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
					valmax = r2.getElevation();
				}
				if ((r2.getTag() & (CgConst.TAG_LOW_PT | CgConst.TAG_HIGH_PT)) == 2) {
					findmin = true;
					valmin = r2.getElevation();
				}

				if (r2.getElevation() > r1.getElevation()) {
					maxm = false;
				}

				if (r2.getElevation() < r1.getElevation()) {
					minm = false;
				}

				dist = dist + r2.getDist();
				if (dist > CgConst.DIST_MAX_MINMAX) {
					break;// return;//Break
				}
			} // For j

			if ((r1 == null) || (r2 == null)) {
				return;
			}
			if (maxm && (findmax || (Math.abs(r2.getElevation() - valmax) < CgConst.MIN_ELEV_MINMAX))) {
				maxm = false;
			}
			if (minm && (findmin || (Math.abs(r2.getElevation() - valmin) < CgConst.MIN_ELEV_MINMAX))) {
				minm = false;
			}

			dist = 0;
			minp = true;
			maxp = true;

			for (k = i; k < data.size(); k++) {
				r3 = data.get(k);
				if (r3.getElevation() > r1.getElevation()) {
					maxp = false;
				}

				if (r3.getElevation() < r1.getElevation()) {
					minp = false;
				}

				if (k < data.size() - 1) {
					dist = dist + (double) data.get(k + 1).getDist();
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

		} // Boucle principale
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
			CalcClimb(0, data.size() - 1, resClimb);
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
	 * @param start
	 *            Index of the starting point
	 */
	public void NewStartingPoint(int start) {

		if (data.size() >= 0) {

			ArrayList<CgData> datatmp;
			// List<cgData> datatmp;

			datatmp = new ArrayList<CgData>();

			int n = start;
			int nb = 0;
			while (nb < data.size()) {
				datatmp.add(new CgData((double) (nb + 1), // Num - double
						data.get(n).getLatitude(), // Latitude - double
						data.get(n).getLongitude(), // Longitude - double
						data.get(n).getElevation(), // Elevation - double
						data.get(n).getElevationMemo(), // ElevationMemo -
														// double
						data.get(n).getTag(), // Tag - int
						data.get(n).getDist(), // Dist - double
						data.get(n).getTotal(), // Total - double
						data.get(n).getDiff(), // Diff - double
						data.get(n).getCoeff(), // Coeff - double
						0.0, // Recup - double
						data.get(n).getSlope(), // Slope - double
						data.get(n).getSpeed(), // Speed - double
						data.get(n).getdElevation(), // dElevation - double
						data.get(n).getTime(), // Time - int
						data.get(n).getdTime_f(), // dTime_f - double
						data.get(n).getTimeLimit(), // TimeLimit - int
						data.get(n).getHour(), // Hour - DateTime
						data.get(n).getStation(), // Station - int
						data.get(n).getName(), // Name - String
						data.get(n).getComment(), // Comment - String
						0.0, // tmp1 - double
						0.0, // tmp2 - double
						data.get(n).FmtLbMiniRoadbook, // String
						data.get(n).OptionMiniRoadbook, // int
						data.get(n).VPosMiniRoadbook, // int
						data.get(n).CommentMiniRoadbook, // String
						data.get(n).FontSizeMiniRoadbook // FontSizeMiniRoadbook //int
				));
				nb++;
				n++;
				if (n >= data.size()) {
					n = 0;
				}
			}

			n = 0;
			for (CgData r : data) {
				r.setNum(datatmp.get(n).getNum()); // Num
				r.setLatitude(datatmp.get(n).getLatitude()); // Latitude
				r.setLongitude(datatmp.get(n).getLongitude()); // Longitude
				r.setElevation(datatmp.get(n).getElevation()); // Elevation
				r.setElevationMemo(datatmp.get(n).getElevationMemo()); // ElevationMemo
				r.setTag(datatmp.get(n).getTag()); // Tag
				r.setDist(datatmp.get(n).getDist()); // Dist
				r.setTotal(datatmp.get(n).getTotal()); // Total
				r.setDiff(datatmp.get(n).getDiff()); // Diff
				r.setCoeff(datatmp.get(n).getCoeff()); // Coeff
				r.setSlope(datatmp.get(n).getSlope()); // Slope
				r.setSpeed(datatmp.get(n).getSpeed()); // Speed
				r.setdElevation(datatmp.get(n).getdElevation()); // dElevation
				r.setTime(datatmp.get(n).getTime()); // Time
				// r.dTime(datatmp[n].dTime; //dTime
				r.setdTime_f(datatmp.get(n).getdTime_f()); // dTime_f
				r.setTimeLimit(datatmp.get(n).getTimeLimit());
				r.setHour(datatmp.get(n).getHour()); // Hour
				r.setStation(datatmp.get(n).getStation()); // Station
				r.setName(datatmp.get(n).getName()); // Name
				r.setComment(datatmp.get(n).getComment()); // Comment
				r.FmtLbMiniRoadbook = data.get(n).FmtLbMiniRoadbook;
				r.OptionMiniRoadbook = data.get(n).OptionMiniRoadbook;
				r.VPosMiniRoadbook = data.get(n).VPosMiniRoadbook;
				r.CommentMiniRoadbook = data.get(n).CommentMiniRoadbook;
				n++;
			}

			CalcDist();
			CalcSpeed();
			CalcSlope();

			CalcClimbResult resClimb = new CalcClimbResult();
			resClimb = CalcClimb(0, data.size() - 1, resClimb);
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
	 * @param name
	 *            name of the file
	 * @param mode
	 *            reading mode (0=complet 1=partial)
	 */
	public void OpenCGX(Component parent, String name, int mode) {
		SaxCGXHandler CGXhandler = new SaxCGXHandler();

		int ret = 0;
		try {
			ret = CGXhandler.readDataFromCGX(parent, name, this, mode);
		} catch (Exception e) {
		}

		if (ret != 0)
			System.out.println("Erreur ligne =" + CGXhandler.getErrLine());

		int cmpt = 1;

		for (CgData r : data) {
			r.setNum(cmpt);
			cmpt++;
		}

		isCalculated = false;
		isModified = false;
		Name = new File(name).getName();

		CalcDist();
		CalcSpeed();
		CalcSlope();

		CalcClimbResult resClimb = new CalcClimbResult();
		CalcClimb(0, data.size() - 1, resClimb);
		ClimbP = resClimb.cp;
		ClimbM = resClimb.cm;
		AscTime = resClimb.tp;
		DescTime = resClimb.tm;

		TotalTime = CalcHour();

		SearchMinMaxElevationResult resMinMaxElev = new SearchMinMaxElevationResult();
		resMinMaxElev = SearchMinMaxElevation(0, (data.size() - 1), resMinMaxElev);
		MinElev = resMinMaxElev.min;
		MaxElev = resMinMaxElev.max;

		CheckTimeLimit();
		isCalculated = true;
		// return isTimeLoaded;
	}// LoadCGX


	/**
	 * Save data in CGX format (complet and partial)
	 * 
	 * @param name
	 *            name of the file
	 * @param start
	 *            first line to save
	 * @param end
	 *            last line to save
	 */
	public void SaveCGX(String name, int start, int end) {
		if (data.isEmpty()) {
			return;
		}

		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		try {
			XMLStreamWriter writer = factory.createXMLStreamWriter(new FileOutputStream(name), "UTF-8");

			writer.writeStartDocument("UTF-8", "1.0");
			writer.writeStartElement("COURSEGENERATOR");
			Utils.WriteStringToXML(writer, "VERSION", "5");

			double d = data.get(end).getTotal() - data.get(start).getTotal();
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
			Utils.WriteStringToXML(writer, "TIMEZONE", String.format(Locale.ROOT, "%f", TrackTimeZone));
			Utils.WriteStringToXML(writer, "USESUMMERTIME", (TrackUseSumerTime ? "1" : "0"));
			Utils.WriteStringToXML(writer, "CURVE", Paramfile);
			Utils.WriteIntToXML(writer, "MRBSIZEW", MrbSizeW);
			Utils.WriteIntToXML(writer, "MRBSIZEH", MrbSizeH);

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

			for (int i = start; i <= end; i++) {
				CgData r = data.get(i);

				writer.writeStartElement("TRACKPOINT");
				Utils.WriteStringToXML(writer, "LATITUDEDEGREES", String.format(Locale.ROOT, "%f", r.getLatitude()));
				Utils.WriteStringToXML(writer, "LONGITUDEDEGREES", String.format(Locale.ROOT, "%f", r.getLongitude()));
				Utils.WriteStringToXML(writer, "ALTITUDEMETERS", String.format(Locale.ROOT, "%f", r.getElevation()));
				Utils.WriteStringToXML(writer, "DISTANCEMETERS", String.format(Locale.ROOT, "%f", r.getDist()));
				Utils.WriteStringToXML(writer, "DISTANCEMETERSCUMUL", String.format(Locale.ROOT, "%f", r.getTotal()));
				Utils.WriteStringToXML(writer, "DIFF", String.format(Locale.ROOT, "%f", r.getDiff()));
				Utils.WriteStringToXML(writer, "COEFF", String.format(Locale.ROOT, "%f", r.getCoeff()));
				Utils.WriteStringToXML(writer, "RECUP", String.format(Locale.ROOT, "%f", r.getRecovery()));
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
			} // for
			writer.writeEndElement();

			writer.writeEndDocument();
			writer.flush();
			writer.close();
			isModified = false;
			Name = new File(name).getName();
		} catch (XMLStreamException | IOException e) {
			System.out.println(e.getMessage());
		}
	}


	/**
	 * Save CSV file
	 * 
	 * @param name
	 *            name of the CSV file
	 * @param start
	 *            first line of the data to save
	 * @param end
	 *            last line of the data to save
	 */
	public void SaveCSV(String name, int start, int end) {
		/*
		 * if (data.Count <= 0) { return; }
		 * 
		 * StreamWriter sw = new StreamWriter(name, false,
		 * System.Text.Encoding.UTF8); sw.WriteLine(
		 * "N°;LAT;LON;ELEV;DIST;DIST CUMUL;DIFF;COEFF;RECUP;TIME(s);EATTIME;COMMENT;CLIMB;SPEED;NAME;HOUR;TAG;TIMELIMIT(s)"
		 * );
		 * 
		 * for (int i = start; i <= end; i++) { cgData r = data[i];
		 * sw.WriteLine( String.Format("{0:0};", r.Num) +
		 * String.Format("{0:0.0000000000};", r.Latitude) +
		 * String.Format("{0:0.0000000000};", r.Longitude) +
		 * String.Format("{0:0.000};", r.Elevation) +
		 * String.Format("{0:0.000};", r.Dist) + String.Format("{0:0.000};",
		 * r.Total) + String.Format("{0:0};", r.Diff) + String.Format("{0:0};",
		 * r.Coeff) + String.Format("{0:0};", r.Recup) + String.Format("{0:0};",
		 * r.Time) + String.Format("{0:0.0};", r.Station) + r.Comment + ";" +
		 * String.Format("{0:0.0};", r.Slope) + String.Format("{0:0.000};",
		 * r.Speed) + r.Name + ";" + r.Hour.ToString("g") + ";" +
		 * String.Format("{0:0};", r.Tag) + String.Format("{0:0}", r.TimeLimit)
		 * );
		 * 
		 * }//foreach sw.Close(); isModified = false;
		 */
	}


	/**
	 * Calculate the night and day time
	 */
	public void CalcStatNight() {
		if (data.size() <= 0) {
			return;
		}

		tInNight.Init();
		tInDay.Init();

		boolean first = true;
		for (CgData r : data) {
			// --Night coeff --
			/*
			 * if ( (r.getHour().CompareTo(StartNightTime.Hour)>=0) ||
			 * (r.getHour().CompareTo(EndNightTime.Hour)<=0) )
			 */
			if (!first) {
				if (bNightCoeff && ((r.getHour().getSecondOfDay() >= StartNightTime.getSecondOfDay())
						|| (r.getHour().getSecondOfDay() <= EndNightTime.getSecondOfDay()))) {
					tInNight.Time = tInNight.Time + r.getdTime_f();
					tInNight.Speed = tInNight.Speed + r.getSpeed();
					tInNight.Dist = tInNight.Dist + r.getDist();
					tInNight.Cmpt++;
				} else {
					tInDay.Time = tInDay.Time + r.getdTime_f();
					tInDay.Speed = tInDay.Speed + r.getSpeed();
					tInDay.Dist = tInDay.Dist + r.getDist();
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
					StatSlope[j].Speed = StatSlope[j].Speed + r.getSpeed();
					StatSlope[j].Dist = StatSlope[j].Dist + r.getDist();
					StatSlope[j].Time = StatSlope[j].Time + r.getdTime_f();
					StatSlope[j].Cmpt++;
				} else if ((r.getSlope() >= -40) && (r.getSlope() < -30)) {
					j = 1;
					StatSlope[j].Speed = StatSlope[j].Speed + r.getSpeed();
					StatSlope[j].Dist = StatSlope[j].Dist + r.getDist();
					StatSlope[j].Time = StatSlope[j].Time + r.getdTime_f();
					StatSlope[j].Cmpt++;
				} else if ((r.getSlope() >= -30) && (r.getSlope() < -20)) {
					j = 2;
					StatSlope[j].Speed = StatSlope[j].Speed + r.getSpeed();
					StatSlope[j].Dist = StatSlope[j].Dist + r.getDist();
					StatSlope[j].Time = StatSlope[j].Time + r.getdTime_f();
					StatSlope[j].Cmpt++;
				} else if ((r.getSlope() >= -20) && (r.getSlope() < -10)) {
					j = 3;
					StatSlope[j].Speed = StatSlope[j].Speed + r.getSpeed();
					StatSlope[j].Dist = StatSlope[j].Dist + r.getDist();
					StatSlope[j].Time = StatSlope[j].Time + r.getdTime_f();
					StatSlope[j].Cmpt++;
				} else if ((r.getSlope() >= -10) && (r.getSlope() < -5)) {
					j = 4;
					StatSlope[j].Speed = StatSlope[j].Speed + r.getSpeed();
					StatSlope[j].Dist = StatSlope[j].Dist + r.getDist();
					StatSlope[j].Time = StatSlope[j].Time + r.getdTime_f();
					StatSlope[j].Cmpt++;
				} else if ((r.getSlope() >= -5) && (r.getSlope() < -2)) {
					j = 5;
					StatSlope[j].Speed = StatSlope[j].Speed + r.getSpeed();
					StatSlope[j].Dist = StatSlope[j].Dist + r.getDist();
					StatSlope[j].Time = StatSlope[j].Time + r.getdTime_f();
					StatSlope[j].Cmpt++;
				} else if ((r.getSlope() >= -2) && (r.getSlope() <= 2)) {
					j = 6;
					StatSlope[j].Speed = StatSlope[j].Speed + r.getSpeed();
					StatSlope[j].Dist = StatSlope[j].Dist + r.getDist();
					StatSlope[j].Time = StatSlope[j].Time + r.getdTime_f();
					StatSlope[j].Cmpt++;
				} else if ((r.getSlope() > 2) && (r.getSlope() <= 5)) {
					j = 7;
					StatSlope[j].Speed = StatSlope[j].Speed + r.getSpeed();
					StatSlope[j].Dist = StatSlope[j].Dist + r.getDist();
					StatSlope[j].Time = StatSlope[j].Time + r.getdTime_f();
					StatSlope[j].Cmpt++;
				} else if ((r.getSlope() > 5) && (r.getSlope() <= 10)) {
					j = 8;
					StatSlope[j].Speed = StatSlope[j].Speed + r.getSpeed();
					StatSlope[j].Dist = StatSlope[j].Dist + r.getDist();
					StatSlope[j].Time = StatSlope[j].Time + r.getdTime_f();
					StatSlope[j].Cmpt++;
				} else if ((r.getSlope() > 10) && (r.getSlope() <= 20)) {
					j = 9;
					StatSlope[j].Speed = StatSlope[j].Speed + r.getSpeed();
					StatSlope[j].Dist = StatSlope[j].Dist + r.getDist();
					StatSlope[j].Time = StatSlope[j].Time + r.getdTime_f();
					StatSlope[j].Cmpt++;
				} else if ((r.getSlope() > 20) && (r.getSlope() <= 30)) {
					j = 10;
					StatSlope[j].Speed = StatSlope[j].Speed + r.getSpeed();
					StatSlope[j].Dist = StatSlope[j].Dist + r.getDist();
					StatSlope[j].Time = StatSlope[j].Time + r.getdTime_f();
					StatSlope[j].Cmpt++;
				} else if ((r.getSlope() > 30) && (r.getSlope() <= 40)) {
					j = 11;
					StatSlope[j].Speed = StatSlope[j].Speed + r.getSpeed();
					StatSlope[j].Dist = StatSlope[j].Dist + r.getDist();
					StatSlope[j].Time = StatSlope[j].Time + r.getdTime_f();
					StatSlope[j].Cmpt++;
				} else if (r.getSlope() > 40) {
					j = 12;
					StatSlope[j].Speed = StatSlope[j].Speed + r.getSpeed();
					StatSlope[j].Dist = StatSlope[j].Dist + r.getDist();
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
				if (r.getElevation() < 1000) {
					j = 0;
					StatElev[j].Speed = StatElev[j].Speed + r.getSpeed();
					StatElev[j].Dist = StatElev[j].Dist + r.getDist();
					StatElev[j].Time = StatElev[j].Time + r.getdTime_f();
					StatElev[j].Cmpt++;

					// Night
					if (bNightCoeff && ((r.getHour().getSecondOfDay() >= StartNightTime.getSecondOfDay())
							|| (r.getHour().getSecondOfDay() <= EndNightTime.getSecondOfDay()))) {
						StatElevNight[j].Time = StatElevNight[j].Time + r.getdTime_f();
						StatElevNight[j].Speed = StatElevNight[j].Speed + r.getSpeed();
						StatElevNight[j].Dist = StatElevNight[j].Dist + r.getDist();
						StatElevNight[j].Cmpt++;
					} else // Day
					{
						StatElevDay[j].Time = StatElevDay[j].Time + r.getdTime_f();
						StatElevDay[j].Speed = StatElevDay[j].Speed + r.getSpeed();
						StatElevDay[j].Dist = StatElevDay[j].Dist + r.getDist();
						StatElevDay[j].Cmpt++;
					}
				} else if ((r.getElevation() >= 1000) && (r.getElevation() < 1500)) {
					j = 1;
					StatElev[j].Speed = StatElev[j].Speed + r.getSpeed();
					StatElev[j].Dist = StatElev[j].Dist + r.getDist();
					StatElev[j].Time = StatElev[j].Time + r.getdTime_f();
					StatElev[j].Cmpt++;
					// Night
					if (bNightCoeff && ((r.getHour().getSecondOfDay() >= StartNightTime.getSecondOfDay())
							|| (r.getHour().getSecondOfDay() <= EndNightTime.getSecondOfDay()))) {
						StatElevNight[j].Time = StatElevNight[j].Time + r.getdTime_f();
						StatElevNight[j].Speed = StatElevNight[j].Speed + r.getSpeed();
						StatElevNight[j].Dist = StatElevNight[j].Dist + r.getDist();
						StatElevNight[j].Cmpt++;
					} else // Day
					{
						StatElevDay[j].Time = StatElevDay[j].Time + r.getdTime_f();
						StatElevDay[j].Speed = StatElevDay[j].Speed + r.getSpeed();
						StatElevDay[j].Dist = StatElevDay[j].Dist + r.getDist();
						StatElevDay[j].Cmpt++;
					}
				} else if ((r.getElevation() >= 1500) && (r.getElevation() < 2000)) {
					j = 2;
					StatElev[j].Speed = StatElev[j].Speed + r.getSpeed();
					StatElev[j].Dist = StatElev[j].Dist + r.getDist();
					StatElev[j].Time = StatElev[j].Time + r.getdTime_f();
					StatElev[j].Cmpt++;
					// Night
					if (bNightCoeff && ((r.getHour().getSecondOfDay() >= StartNightTime.getSecondOfDay())
							|| (r.getHour().getSecondOfDay() <= EndNightTime.getSecondOfDay()))) {
						StatElevNight[j].Time = StatElevNight[j].Time + r.getdTime_f();
						StatElevNight[j].Speed = StatElevNight[j].Speed + r.getSpeed();
						StatElevNight[j].Dist = StatElevNight[j].Dist + r.getDist();
						StatElevNight[j].Cmpt++;
					} else // Day
					{
						StatElevDay[j].Time = StatElevDay[j].Time + r.getdTime_f();
						StatElevDay[j].Speed = StatElevDay[j].Speed + r.getSpeed();
						StatElevDay[j].Dist = StatElevDay[j].Dist + r.getDist();
						StatElevDay[j].Cmpt++;
					}
				} else if ((r.getElevation() >= 2000) && (r.getElevation() < 2500)) {
					j = 3;
					StatElev[j].Speed = StatElev[j].Speed + r.getSpeed();
					StatElev[j].Dist = StatElev[j].Dist + r.getDist();
					StatElev[j].Time = StatElev[j].Time + r.getdTime_f();
					StatElev[j].Cmpt++;
					// Night
					if (bNightCoeff && ((r.getHour().getSecondOfDay() >= StartNightTime.getSecondOfDay())
							|| (r.getHour().getSecondOfDay() <= EndNightTime.getSecondOfDay()))) {
						StatElevNight[j].Time = StatElevNight[j].Time + r.getdTime_f();
						StatElevNight[j].Speed = StatElevNight[j].Speed + r.getSpeed();
						StatElevNight[j].Dist = StatElevNight[j].Dist + r.getDist();
						StatElevNight[j].Cmpt++;
					} else // Day
					{
						StatElevDay[j].Time = StatElevDay[j].Time + r.getdTime_f();
						StatElevDay[j].Speed = StatElevDay[j].Speed + r.getSpeed();
						StatElevDay[j].Dist = StatElevDay[j].Dist + r.getDist();
						StatElevDay[j].Cmpt++;
					}
				} else if ((r.getElevation() >= 2500) && (r.getElevation() < 3000)) {
					j = 4;
					StatElev[j].Speed = StatElev[j].Speed + r.getSpeed();
					StatElev[j].Dist = StatElev[j].Dist + r.getDist();
					StatElev[j].Time = StatElev[j].Time + r.getdTime_f();
					StatElev[j].Cmpt++;
					// Night
					if (bNightCoeff && ((r.getHour().getSecondOfDay() >= StartNightTime.getSecondOfDay())
							|| (r.getHour().getSecondOfDay() <= EndNightTime.getSecondOfDay()))) {
						StatElevNight[j].Time = StatElevNight[j].Time + r.getdTime_f();
						StatElevNight[j].Speed = StatElevNight[j].Speed + r.getSpeed();
						StatElevNight[j].Dist = StatElevNight[j].Dist + r.getDist();
						StatElevNight[j].Cmpt++;
					} else // Day
					{
						StatElevDay[j].Time = StatElevDay[j].Time + r.getdTime_f();
						StatElevDay[j].Speed = StatElevDay[j].Speed + r.getSpeed();
						StatElevDay[j].Dist = StatElevDay[j].Dist + r.getDist();
						StatElevDay[j].Cmpt++;
					}
				} else if ((r.getElevation() >= 3000)) {
					j = 5;
					StatElev[j].Speed = StatElev[j].Speed + r.getSpeed();
					StatElev[j].Dist = StatElev[j].Dist + r.getDist();
					StatElev[j].Time = StatElev[j].Time + r.getdTime_f();
					StatElev[j].Cmpt++;
					// Night
					if (bNightCoeff && ((r.getHour().getSecondOfDay() >= StartNightTime.getSecondOfDay())
							|| (r.getHour().getSecondOfDay() <= EndNightTime.getSecondOfDay()))) {
						StatElevNight[j].Time = StatElevNight[j].Time + r.getdTime_f();
						StatElevNight[j].Speed = StatElevNight[j].Speed + r.getSpeed();
						StatElevNight[j].Dist = StatElevNight[j].Dist + r.getDist();
						StatElevNight[j].Cmpt++;
					} else // Day
					{
						StatElevDay[j].Time = StatElevDay[j].Time + r.getdTime_f();
						StatElevDay[j].Speed = StatElevDay[j].Speed + r.getSpeed();
						StatElevDay[j].Dist = StatElevDay[j].Dist + r.getDist();
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
	 * @param lat
	 *            latitude of the point
	 * @param lon
	 *            longitude of the point
	 * @return index of the nearest point in the point list
	 */
	public int FindNearestPoint(double lat, double lon) {
		double a, c, dDistance, dLat1InRad, dLong1InRad, dLat2InRad, dLong2InRad, dLongitude, dLatitude;
		double kEarthRadiusKms;
		double lat1, lon1, min;
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
	 * @param unit
	 *            Unit wanted (To get from the settings)
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
	 * @param unit
	 *            Unit wanted (To get from the settings)
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

} // TrackData
