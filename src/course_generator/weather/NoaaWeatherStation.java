package course_generator.weather;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A class to store data from the NOAA stations element.
 * 
 * Documentation : https://www.ncdc.noaa.gov/cdo-web/webservices/v2#stations
 * 
 * @author Frederic Bard
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NoaaWeatherStation implements Comparable<NoaaWeatherStation> {

	private String id;

	private String name;

	private String elevation;

	private String elevationUnit;

	private String longitude;

	private String datacoverage;

	private String latitude;

	private String mindate;

	private String maxdate;

	private double distanceFromStart;
	private double distanceFromSearchAreaCenter;

	public final static String STATIONID = "STATIONID"; //$NON-NLS-1$
	public final static String NAME = "NAME"; //$NON-NLS-1$
	public final static String DISTANCEFROMSTART = "DISTANCEFROMSTART"; //$NON-NLS-1$
	public final static String LATITUDE = "LATITUDE"; //$NON-NLS-1$
	public final static String LONGITUDE = "LONGITUDE"; //$NON-NLS-1$


	public NoaaWeatherStation() {
	}


	public NoaaWeatherStation(String id, String name, String latitude, String longitude, double distanceFromStart) {
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.distanceFromStart = distanceFromStart;
	}

	public ArrayList<Object> dataTypes = new ArrayList<Object>();
	public DateRange DateRangeObject;
	private float fileSize;
	public ArrayList<Object> boundingPoints = new ArrayList<Object>();
	private String filePath;
	public Location location;
	public ArrayList<Station> stations = new ArrayList<Station>();
	private float dataTypesCount;


	// Getter Methods

	public String getId() {
		if (this.stations != null && this.stations.size() > 0)
			return this.stations.get(0).getId();

		return "";
	}


	public String getLongitude() {
		if (this.location != null)
			return this.location.getLongitude();

		return "";
	}


	public String getLatitude() {
		if (this.location != null)
			return this.location.getLatitude();

		return "";
	}


	public double getDistanceFromStart() {
		return distanceFromStart;
	}


	public void setDistanceFromStart(double distanceFromStart) {
		this.distanceFromStart = distanceFromStart;
	}


	public void setDistanceFromSearchAreaCenter(double distanceFromSearchAreaCenter) {
		this.distanceFromSearchAreaCenter = distanceFromSearchAreaCenter;
	}


	public double getDistanceFromSearchAreaCenter() {
		return distanceFromSearchAreaCenter;
	}


	public DateRange getDateRange() {
		return DateRangeObject;
	}


	public float getFileSize() {
		return fileSize;
	}


	public String getFilePath() {
		return filePath;
	}


	public String getName() {
		return name;
	}


	public float getDataTypesCount() {
		return dataTypesCount;
	}


	// Setter Methods

	public void setDateRange(DateRange dateRangeObject) {
		this.DateRangeObject = dateRangeObject;
	}


	public void setFileSize(float fileSize) {
		this.fileSize = fileSize;
	}


	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setDataTypesCount(float dataTypesCount) {
		this.dataTypesCount = dataTypesCount;
	}


	@Override
	public int compareTo(NoaaWeatherStation station2) {
		if (this.getDistanceFromSearchAreaCenter() < station2.getDistanceFromSearchAreaCenter())
			return -1;
		else
			return 1;
	}
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Location {
	public ArrayList<String> coordinates = new ArrayList<String>();

	// Getter Methods


	public String getLatitude() {
		if (this.coordinates != null && this.coordinates.size() == 2) {
			return this.coordinates.get(1);
		}
		return "";
	}


	public String getLongitude() {
		if (this.coordinates != null && this.coordinates.size() == 2) {
			return this.coordinates.get(0);
		}
		return "";
	}

	// Setter Methods

}

@JsonIgnoreProperties(ignoreUnknown = true)
class Station {
	public String name;
	public String id;

	// Getter Methods


	public String getId() {
		return id;
	}


	public String getName() {
		return name;
	}

	// Setter Methods

}

class DateRange {
	private String start;
	private String end;


	// Getter Methods

	public String getStart() {
		return start;
	}


	public String getEnd() {
		return end;
	}


	// Setter Methods

	public void setStart(String start) {
		this.start = start;
	}


	public void setEnd(String end) {
		this.end = end;
	}

}
