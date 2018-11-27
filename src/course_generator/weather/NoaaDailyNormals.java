package course_generator.weather;

import org.joda.time.DateTime;

/**
 * A representation of the NOAA NORMAL_DLY dataset
 * 
 * https://www.ncdc.noaa.gov/cdo-web/datasets
 */
public class NoaaDailyNormals {

	private String TemperatureMin;
	private String TemperatureMax;
	private String TemperatureAverage;
	private String Precipitation;
	private DateTime Date;


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


	public String getPrecipitation() {
		return Precipitation;
	}


	public void setPrecipitation(String precipitation) {
		this.Precipitation = precipitation;
	}


	public String getTemperatureAverage() {
		return TemperatureAverage;
	}


	public void setTemperatureAverage(String temperatureAverage) {
		TemperatureAverage = temperatureAverage;
	}


	public DateTime getDate() {
		return Date;
	}


	public void setDate(DateTime date) {
		Date = date;
	}

}
