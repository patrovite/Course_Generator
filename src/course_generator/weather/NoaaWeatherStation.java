package course_generator.weather;

public class NoaaWeatherStation {
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
	public String toString() {
		return "ClassPojo [id = " + id + ", name = " + name + ", elevation = " + elevation + ", elevationUnit = "
				+ elevationUnit + ", longitude = " + longitude + ", datacoverage = " + datacoverage + ", latitude = "
				+ latitude + ", mindate = " + mindate + ", maxdate = " + maxdate + "]";
	}
}
