package course_generator.weather;

/**
 * A representation of the NOAA NORMAL_DLY dataset
 * 
 * https://www.ncdc.noaa.gov/cdo-web/datasets
 */
public class NoaaDailyNormals {
	private String station;

	private String TemperatureMin;
	private String TemperatureMax;


	public String getStation() {
		return station;
	}


	public void setStation(String station) {
		this.station = station;
	}


	public String getTemperatureMin() {
		return TemperatureMin;
	}


	public void setTemperatureMin(String temperatureMin) {
		this.TemperatureMin = temperatureMin;
	}


	public String getTemperatureMax() {
		return TemperatureMax;
	}


	public void setTemperatureMax(String temperatureMax) {
		this.TemperatureMax = temperatureMax;
	}

}
