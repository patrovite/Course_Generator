package course_generator.weather;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A Java representation of a NOAA query "results" element.
 * 
 * @author Fr�d�ric Bard
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NoaaResults {

	private List<Result> results;

    public List<Result> getResults() { return results; }

	// Weather station query result properties

	@JsonProperty
	private String id;
	@JsonProperty
	private String name;
	@JsonProperty
	private String latitude;
	@JsonProperty
	private String longitude;


	// Daily summaries methods

	public String getDate() {
		return getResults().get(0).getDate();
	}


	public String getMinimumTemperature() {
		return getResult(NoaaHistoricalWeatherRetriever.TMin).getValue();
	}


	private Result getResult(String string) {
		
		for(Result result: results)
		{
			if (result.getDatatype().equals(string)){
			return result;
			}
		}
		return new Result();
	}


	public String getMaximumTemperature() {
		return getResult(NoaaHistoricalWeatherRetriever.TMax).getValue();
	}


	public String getAverageTemperature() {
		return "";//return getResult(NoaaHistoricalWeatherRetriever.TMax).getValue();
	}


	public String getPrecipitation() {
		return getResult(NoaaHistoricalWeatherRetriever.Precipitation).getValue();
	}


	// Daily normals methods

	public String getMinimumTemperatureDailyNormal() {
		return getResult(NoaaHistoricalWeatherRetriever.TMinNormalDaily).getValue();
	}


	public String getMaximumTemperatureDailyNormal() {
		return getResult(NoaaHistoricalWeatherRetriever.TMaxNormalDaily).getValue();
	}


	public String getAverageTemperatureDailyNormal() {
		return getResult(NoaaHistoricalWeatherRetriever.TAvgNormalDaily).getValue();
	}


	// Monthly normals methods

	public String getMinimumTemperatureMonthlyNormal() {
		return getResult(NoaaHistoricalWeatherRetriever.TMinNormalMonthly).getValue();
	}


	public String getMaximumTemperatureMonthlyNormal() {
		return getResult(NoaaHistoricalWeatherRetriever.TMaxNormalMonthly).getValue();
	}


	public String getAverageTemperatureMonthlyNormal() {
		return getResult(NoaaHistoricalWeatherRetriever.TAvgNormalMonthly).getValue();
	}


	// Weather station methods

	public boolean IsStationValid() {
		return this.getStationId() != null && this.getStationName() != null && this.getStationLatitude() != null
				&& this.getStationLongitude() != null;
	}


	public String getStationId() {
		return id;
	}


	public String getStationName() {
		return name;
	}


	public String getStationLatitude() {
		return latitude;
	}


	public String getStationLongitude() {
		return longitude;
	}

}

 class Result {
    private String date;
    private String datatype;
    private String station;
    private String attributes;
    private String value;

    public String getDate() { return date; }
    public void setDate(String value) { this.date = value; }

    public String getDatatype() { return datatype; }
    public void setDatatype(String value) { this.datatype = value; }

    public String getStation() { return station; }
    public void setStation(String value) { this.station = value; }

    public String getAttributes() { return attributes; }
    public void setAttributes(String value) { this.attributes = value; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
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
	private ArrayList<String> coordinates = new ArrayList<>();

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
