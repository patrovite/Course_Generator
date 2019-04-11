package course_generator.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A Java representation of a NOAA query "results" element.
 * 
 * @author Frederic Bard
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NoaaResults {
	private String station;

	private String date;

	private String tmax;

	private String tmin;

	@JsonProperty("DLY-TMIN-NORMAL")
	private String MinimumTemperatureDailyNormal;

	@JsonProperty("DLY-TMAX-NORMAL")
	private String MaximumTemperatureDailyNormal;

	@JsonProperty("DLY-TAVG-NORMAL")
	private String AverageTemperatureDailyNormal;

	@JsonProperty("MLY-TMIN-NORMAL")
	private String MinimumTemperatureMonthlyNormal;

	@JsonProperty("MLY-TMAX-NORMAL")
	private String MaximumTemperatureMonthlyNormal;

	@JsonProperty("MLY-TAVG-NORMAL")
	private String AverageTemperatureMonthlyNormal;


	public String getMinimumTemperatureMonthlyNormal() {
		return MinimumTemperatureMonthlyNormal;
	}


	public String getMaximumTemperatureMonthlyNormal() {
		return MaximumTemperatureMonthlyNormal;
	}


	public String getAverageTemperatureMonthlyNormal() {
		return AverageTemperatureMonthlyNormal;
	}


	public String getMaximumTemperatureDailyNormal() {
		return MaximumTemperatureDailyNormal;
	}


	public String getAverageTemperatureDailyNormal() {
		return AverageTemperatureDailyNormal;
	}


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


	public String getMinimumTemperatureDailyNormal() {
		return MinimumTemperatureDailyNormal;
	}


	public void setMinimumTemperatureDailyNormal(String minimumTemperatureDailyNormal) {
		MinimumTemperatureDailyNormal = minimumTemperatureDailyNormal;
	}
}