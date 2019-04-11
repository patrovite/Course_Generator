package course_generator.weather;

/**
 * A Java representation of a NOAA query "results" element.
 * 
 * @author Frederic Bard
 * 
 */
public class NoaaResults {
	public String station;

	public String date;

	public String tmax;

	public String tmin;


	public String getStation() {
		return station;
	}


	public void setStation(String station) {
		this.station = station;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getTmin() {
		return tmin;
	}


	public void setTmin(String tmin) {
		this.tmin = tmin;
	}


	public String getTmax() {
		return tmax;
	}


	public void setTmax(String tmax) {
		this.tmax = tmax;
	}

}