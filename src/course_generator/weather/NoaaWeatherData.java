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

	private String maxTemperature;
	private String minTemperature;
	private String AverageTemperature;
	private String Precipitation;
	private DateTime Date;

	public static final String MAXIMUMTEMPERATURE = "MAXIMUMTEMPERATURE"; //$NON-NLS-1$
	public static final String MINIMUMTEMPERATURE = "MINIMUMTEMPERATURE"; //$NON-NLS-1$
	public static final String AVERAGETEMPERATURE = "AVERAGETEMPERATURE"; //$NON-NLS-1$
	public static final String PRECIPITATION = "PRECIPITATION"; //$NON-NLS-1$
	public static final String DATE = "DATE"; //$NON-NLS-1$


	public NoaaWeatherData() {
		maxTemperature = ""; //$NON-NLS-1$
		minTemperature = ""; //$NON-NLS-1$
		AverageTemperature = ""; //$NON-NLS-1$
		Precipitation = ""; //$NON-NLS-1$
	}


	public NoaaWeatherData(String maximumTemperature, String minimumTemperature, String averageTemperature,
			String precipitation, DateTime date) {
		this.maxTemperature = maximumTemperature;
		this.minTemperature = minimumTemperature;
		this.AverageTemperature = averageTemperature;
		this.Precipitation = precipitation;
		this.Date = date;
	}


	public String getTemperatureMin() {
		return minTemperature;
	}


	public void setTemperatureMin(String temperatureMin) {
		this.minTemperature = temperatureMin;
	}


	public String getTemperatureMax() {
		return maxTemperature;
	}


	public void setTemperatureMax(String temperatureMax) {
		this.maxTemperature = temperatureMax;
	}


	public String getPrecipitation() {
		return Precipitation;
	}


	public void setPrecipitation(String precipitation) {
		this.Precipitation = precipitation;
	}


	public String getTemperatureAverage() {
		return AverageTemperature;
	}


	public void setTemperatureAverage(String temperatureAverage) {
		AverageTemperature = temperatureAverage;
	}


	public DateTime getDate() {
		return Date;
	}


	public void setDate(DateTime date) {
		Date = date;
	}


	public boolean isDailySummaryValid() {
		if (maxTemperature != null && !maxTemperature.equals("") && minTemperature != null //$NON-NLS-1$
				&& !minTemperature.equals("")) //$NON-NLS-1$
			return true;

		return false;
	}


	public boolean isNormalsDataValid() {
		if (isDailySummaryValid() && AverageTemperature != null && !AverageTemperature.equals("")) //$NON-NLS-1$
			return true;

		return false;
	}

}
