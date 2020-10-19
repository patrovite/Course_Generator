package course_generator.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A class to store data from the NOAA stations element.
 * 
 * Documentation : https://www.ncdc.noaa.gov/cdo-web/webservices/v2#stations
 * 
 * @author Frédéric Bard
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NoaaWeatherStation implements Comparable<NoaaWeatherStation> {

	private String id;

	private String name;

	private String longitude;

	private String latitude;

	private double distanceFromStart;
	private double distanceFromSearchAreaCenter;

	public final static String STATIONID = "STATIONID"; //$NON-NLS-1$
	public final static String NAME = "NAME"; //$NON-NLS-1$
	public final static String DISTANCEFROMSTART = "DISTANCEFROMSTART"; //$NON-NLS-1$
	public final static String LATITUDE = "LATITUDE"; //$NON-NLS-1$
	public final static String LONGITUDE = "LONGITUDE"; //$NON-NLS-1$
	public final static String WebUrlBase = "https://www.ncdc.noaa.gov/cdo-web/datasets/GHCND/stations/GHCND:";


	public NoaaWeatherStation(String id, String name, String latitude, String longitude, double distanceFromStart) {
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.distanceFromStart = distanceFromStart;
	}


	// Getter Methods

	public String getId() {
		return id;
	}


	public String getName() {
		return name;
	}


	public String getLongitude() {
		return longitude;
	}


	public String getLatitude() {
		return latitude;
	}


	public double getDistanceFromStart() {
		return distanceFromStart;
	}


	public double getDistanceFromSearchAreaCenter() {
		return distanceFromSearchAreaCenter;
	}


	public void setDistanceFromSearchAreaCenter(double distanceFromSearchAreaCenter) {
		this.distanceFromSearchAreaCenter = distanceFromSearchAreaCenter;
	}


	@Override
	public int compareTo(NoaaWeatherStation station2) {
		if (this.getDistanceFromSearchAreaCenter() < station2.getDistanceFromSearchAreaCenter())
			return -1;
		else
			return 1;
	}
}
