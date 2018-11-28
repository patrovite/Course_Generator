package course_generator.weather;

import org.joda.time.DateTime;

/**
 * Class to store data from the NOAA GHCND, NORMAL_DLY and NORMAL_MLY datasets.
 * 
 * Documentation : https://www.ncdc.noaa.gov/cdo-web/datasets
 * https://www1.ncdc.noaa.gov/pub/data/cdo/documentation/GHCND_documentation.pdf
 * https://www1.ncdc.noaa.gov/pub/data/cdo/documentation/NORMAL_DLY_documentation.pdf
 * https://www1.ncdc.noaa.gov/pub/data/cdo/documentation/NORMAL_MLY_documentation.pdf
 * 
 * @author Frederic Bard
 * 
 */
public class NoaaWeatherData {

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
