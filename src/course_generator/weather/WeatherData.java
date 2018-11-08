package course_generator.weather;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.joda.time.DateTime;

public class WeatherData {
	// See here for the field descriptions :
	// https://darksky.net/dev/docs#response-format

	/** The time for the weather data request **/
	private DateTime Time;
	/** A human-readable text summary of this data point **/
	private String Summary; // : "Overcast until afternoon.",
	private String Icon; // "icon": "cloudy",
	// "sunriseTime": 1473859401,
	// "sunsetTime": 1473904638,
	/** Moon phase **/
	private String MoonPhase;// :0.43,
	// "precipIntensity": 0,
	// "precipIntensityMax": 0,
	// "precipIntensityMaxTime": 255657600,
	// "precipProbability": 0,
	private String PrecipAccumulation; // ": 7.337,
	private String PrecipType; // ": "snow",
	/** The daytime high temperature. **/
	private String TemperatureHigh;
	private String TemperatureHighTime;// ": 1473894000,
	/** The overnight low temperature. **/
	private String TemperatureLow; // ":32.42,
	private String TemperatureLowTime;// ": 1473944400,
	private String ApparentTemperatureHigh; // ": 54.27,
	private String ApparentTemperatureHighTime; // ": 1473894000,
	private String ApparentTemperatureLow; // ": 32.42,
	private String ApparentTemperatureLowTime; // ": 1473944400,
	// "dewPoint": 32.57,
	// "humidity": 0.63,
	// "pressure": 1016.68,
	/** The wind speed in miles per hour. **/
	private String WindSpeed;// ":0.7,
	// "windGust": 7.19,
	// "windGustTime": 1473883200,
	// "windBearing": 336,
	// "cloudCover": 0.49,
	// "uvIndex": 4,
	// "uvIndexTime": 1473879600,
	// "visibility": 3.97,
	/** The minimum temperature during a given date **/


	// DEPRECATED "TemperatureMin:38.41,
	// DEPRECATED "temperatureMinTime": 1473854400,
	/** The maximum temperature during a given date **/
	// DEPRECATED "emperatureMax":54.27,
	// DEPRECATED "temperatureMaxTime": 1473894000,
	// DEPRECATED "apparentTemperatureMin": 38.41,
	// DEPRECATED "apparentTemperatureMinTime": 1473854400,
	// DEPRECATED "apparentTemperatureMax": 54.27,
	// DEPRECATED "apparentTemperatureMaxTime": 1473894000

	// Hourly data points
	// private String Temperature;

	public DateTime getTime() {
		return Time;
	}


	public void setTime(DateTime time) {
		Time = time;
	}


	public void setSummary(String summary) {
		Summary = summary;
	}


	public void setIcon(String icon) {
		Icon = icon;
	}


	public String getIcon() {
		return Icon;
	}


	public DateTime getDate() {
		return Time;
	}


	public void setDate(DateTime date) {
		Time = date;
	}


	public String getSummary() {
		return Summary;
	}


	public String getMoonPhase() {
		return MoonPhase;
	}


	public void setMoonPhase(String moonPhase) {
		MoonPhase = moonPhase;
	}


	/*
	 * public String getTemperature() { return Temperature; }
	 * 
	 * public void setTemperature(String temperature) { Temperature = temperature; }
	 */

	public String getTemperatureHigh() {
		return TemperatureHigh;
	}


	public void setTemperatureHigh(String temperatureHigh) {
		TemperatureHigh = temperatureHigh;
	}


	public String getTemperatureHighTime() {
		return TemperatureHighTime;
	}


	public void setTemperatureHighTime(String temperatureHighTime) {
		TemperatureHighTime = temperatureHighTime;
	}


	public String getTemperatureLow() {
		return TemperatureLow;
	}


	public void setTemperatureLow(String temperatureLow) {
		TemperatureLow = temperatureLow;
	}


	public String getTemperatureLowTime() {
		return TemperatureLowTime;
	}


	public void setTemperatureLowTime(String temperatureLowTime) {
		TemperatureLowTime = temperatureLowTime;
	}


	public String getApparentTemperatureHigh() {
		return ApparentTemperatureHigh;
	}


	public void setApparentTemperatureHigh(String apparentTemperatureHigh) {
		ApparentTemperatureHigh = apparentTemperatureHigh;
	}


	public String getApparentTemperatureHighTime() {
		return ApparentTemperatureHighTime;
	}


	public void setApparentTemperatureHighTime(String apparentTemperatureHighTime) {
		ApparentTemperatureHighTime = apparentTemperatureHighTime;
	}


	public String getApparentTemperatureLow() {
		return ApparentTemperatureLow;
	}


	public void setApparentTemperatureLow(String apparentTemperatureLow) {
		ApparentTemperatureLow = apparentTemperatureLow;
	}


	public String getApparentTemperatureLowTime() {
		return ApparentTemperatureLowTime;
	}


	public void setApparentTemperatureLowTime(String apparentTemperatureLowTime) {
		ApparentTemperatureLowTime = apparentTemperatureLowTime;
	}


	public String getWindSpeed() {
		return WindSpeed;
	}


	public void setWindSpeed(String windSpeed) {
		WindSpeed = windSpeed;
	}


	public String getPrecipAccumulation() {
		return PrecipAccumulation;
	}


	public void setPrecipAccumulation(String precipAccumulation) {
		PrecipAccumulation = precipAccumulation;
	}


	public String getPrecipType() {
		return PrecipType;
	}


	public void setPrecipType(String precipType) {
		PrecipType = precipType;
	}


	public String getSummaryIconFilePath() {
		String iconFileName = "";
		switch (Icon.toLowerCase()) {
		case "clear-day":
			iconFileName = "Sun";
			break;
		case "clear-night":
			iconFileName = "Moon";
			break;
		case "rain":
			iconFileName = "Cloud-Rain";
			break;
		case "snow":
		case "sleet":
			iconFileName = "Cloud-Snow";
			break;
		case "wind":
			iconFileName = "Wind";
			break;
		case "fog":
			iconFileName = "Cloud-Fog";
			break;
		case "cloudy":
			iconFileName = "Cloud";
			break;
		case "partly-cloudy-day":
			iconFileName = "Cloud-Sun";
			break;
		case "partly-cloudy-night":
			iconFileName = "Cloud-Moon";
			break;
		default:
			return "";
		}

		return getFilePathFromFileName(iconFileName + ".png");
	}


	/**
	 * Retrieves the appropriate icon name given a temperature value.
	 * 
	 * @param temperatureValue
	 *            A temperature value in Fahrenheit.
	 * @return The icon file name.
	 */
	public String getThermometerIconFilePath(String temperatureValue) {
		String iconFileName = "";
		int temperature = Integer.valueOf(temperatureValue);

		if (temperature <= 0) {
			iconFileName = "Thermometer-Zero";
		} else if (temperature <= 25) {
			iconFileName = "Thermometer-25";
		} else if (temperature <= 50) {
			iconFileName = "Thermometer-50";
		} else if (temperature <= 75) {
			iconFileName = "Thermometer-75";
		} else if (temperature >= 100) {
			iconFileName = "Thermometer-100";
		}

		return getFilePathFromFileName(iconFileName + ".png");
	}


	public String getPrecipitationTypeIconFilePath() {
		String iconFileName = "";
		switch (PrecipType.toLowerCase()) {
		case "rain":
			iconFileName = "Cloud-Rain";
			break;
		case "snow":
		case "sleet":
			iconFileName = "Cloud-Snow";
			break;
		default:
			return "";
		}

		return getFilePathFromFileName(iconFileName + ".png");
	}


	private String getFilePathFromFileName(String fileName) {
		Path filePath = null;
		try {
			filePath = Paths.get(
					getClass().getResource("/course_generator/images/climacons/SVG/Converted/" + fileName).toURI());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath.toString();
	}

}
