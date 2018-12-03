package course_generator.weather;

/**
 * Class to store data from the NOAA stations element.
 * 
 * Documentation : https://www.ncdc.noaa.gov/cdo-web/webservices/v2#stations
 * 
 * @author Frederic Bard
 */
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

	public final static String STATIONID = "STATIONID";
	public final static String NAME = "NAME";
	public final static String DISTANCEFROMSTART = "DISTANCEFROMSTART";
	public final static String LATITUDE = "LATITUDE";
	public final static String LONGITUDE = "LONGITUDE";


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getElevation() {
		return elevation;
	}


	public void setElevation(String elevation) {
		this.elevation = elevation;
	}


	public String getElevationUnit() {
		return elevationUnit;
	}


	public void setElevationUnit(String elevationUnit) {
		this.elevationUnit = elevationUnit;
	}


	public String getLongitude() {
		return longitude;
	}


	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}


	public String getDatacoverage() {
		return datacoverage;
	}


	public void setDatacoverage(String datacoverage) {
		this.datacoverage = datacoverage;
	}


	public String getLatitude() {
		return latitude;
	}


	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}


	public String getMindate() {
		return mindate;
	}


	public void setMindate(String mindate) {
		this.mindate = mindate;
	}


	public String getMaxdate() {
		return maxdate;
	}


	public void setMaxdate(String maxdate) {
		this.maxdate = maxdate;
	}


	public double getDistanceFromStart() {
		return distanceFromStart;
	}


	public void setDistanceFromStart(double distanceFromStart) {
		this.distanceFromStart = distanceFromStart;
	}


	@Override
	public int compareTo(NoaaWeatherStation station2) {
		if (this.getDistanceFromStart() < station2.getDistanceFromStart())
			return -1;
		else
			return 1;
	}

}
