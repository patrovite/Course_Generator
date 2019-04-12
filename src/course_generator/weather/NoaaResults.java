package course_generator.weather;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A Java representation of a NOAA query "results" element.
 * 
 * @author Frédéric Bard
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NoaaResults {

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

	// Weather station query result properties

	@JsonProperty
	private ArrayList<Station> stations = new ArrayList<Station>();
	@JsonProperty
	private Location location;


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


	public String getStationId() {
		if (this.stations != null && this.stations.size() > 0)
			return stations.get(0).getId();

		return null;
	}


	public String getStationName() {
		if (this.stations != null && this.stations.size() > 0)
			return stations.get(0).getName();

		return null;
	}


	public String getStationLatitude() {
		if (this.location != null)
			return this.location.getLatitude();

		return null;
	}


	public String getStationLongitude() {
		if (this.location != null)
			return this.location.getLongitude();

		return null;
	}


	public String getDate() {
		return date;
	}


	public String getTmin() {
		return tmin;
	}


	public String getTmax() {
		return tmax;
	}


	public String getMinimumTemperatureDailyNormal() {
		return MinimumTemperatureDailyNormal;
	}

}

@JsonIgnoreProperties(ignoreUnknown = true)
class Station {
	@JsonProperty
	private String name;
	@JsonProperty
	private String id;


	// Getter Methods
	public String getId() {
		return id;
	}


	public String getName() {
		return name;
	}
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Location {
	@JsonProperty
	private ArrayList<String> coordinates = new ArrayList<String>();

	// Getter Methods


	public String getLatitude() {
		if (this.coordinates != null && this.coordinates.size() == 2) {
			return this.coordinates.get(1);
		}
		return null;
	}


	public String getLongitude() {
		if (this.coordinates != null && this.coordinates.size() == 2) {
			return this.coordinates.get(0);
		}
		return null;
	}

}
