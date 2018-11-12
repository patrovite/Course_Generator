package course_generator.weather;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WeatherData {
	// See here for the field descriptions :
	// https://darksky.net/dev/docs#response-format

	/** The time for the weather data request **/
	private long Time; // 1520751600
	/** A human-readable text summary of this data point **/
	private String Summary; // : "Overcast until afternoon.",
	private String Icon; // "icon": "cloudy",
	// "sunriseTime": 1473859401,
	// "sunsetTime": 1473904638,
	/** Moon phase **/
	private double MoonPhase;// :0.43,
	// "precipIntensity": 0,
	// "precipIntensityMax": 0,
	// "precipIntensityMaxTime": 255657600,
	// "precipProbability": 0,
	// "precipAccumulation; // ": 7.337, The amount of snowfall accumulation
	// expected to occur
	private String PrecipType; // ": "snow",
	/** The daytime high temperature. **/
	private double TemperatureHigh;
	private long TemperatureHighTime; // ": 1473894000,
	/** The overnight low temperature. **/
	private double TemperatureLow; // ":32.42,
	private long TemperatureLowTime; // ": 1473944400,
	private double ApparentTemperatureHigh; // ": 54.27,
	private long ApparentTemperatureHighTime; // ": 1473894000,
	private double ApparentTemperatureLow; // ": 32.42,
	private long ApparentTemperatureLowTime; // ": 1473944400,
	// "dewPoint": 32.57,
	// "humidity": 0.63,
	// "pressure": 1016.68,
	/** The wind speed in miles per hour. **/
	private double WindSpeed;// ":0.7,
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

	public WeatherData() {
	}


	public WeatherData(long time, String summary, String icon, double moonPhase, String precipType,
			double temperatureHigh, long temperatureHighTime, double temperatureLow, long temperatureLowTime,
			double apparentTemperatureHigh, long apparentTemperatureHighTime, double apparentTemperatureLow,
			long apparentTemperatureLowTime, double windSpeed) {
		Time = time;
		Summary = summary;
		Icon = icon;
		MoonPhase = moonPhase;
		PrecipType = precipType;
		TemperatureHigh = temperatureHigh;
		TemperatureHighTime = temperatureHighTime;
		TemperatureLow = temperatureLow;
		TemperatureLowTime = temperatureLowTime;
		ApparentTemperatureHigh = apparentTemperatureHigh;
		ApparentTemperatureHighTime = apparentTemperatureHighTime;
		ApparentTemperatureLow = apparentTemperatureLow;
		ApparentTemperatureLowTime = apparentTemperatureLowTime;
		WindSpeed = windSpeed;
	}


	public long getTime() {
		return Time;
	}


	public void setTime(long time) {
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


	public String getSummary() {
		return Summary;
	}


	public double getMoonPhase() {
		return MoonPhase;
	}


	public void setMoonPhase(double moonPhase) {
		MoonPhase = moonPhase;
	}


	public double getTemperatureHigh() {
		return TemperatureHigh;
	}


	public void setTemperatureHigh(double temperatureHigh) {
		TemperatureHigh = temperatureHigh;
	}


	public long getTemperatureHighTime() {
		return TemperatureHighTime;
	}


	public void setTemperatureHighTime(long temperatureHighTime) {
		TemperatureHighTime = temperatureHighTime;
	}


	public double getTemperatureLow() {
		return TemperatureLow;
	}


	public void setTemperatureLow(double temperatureLow) {
		TemperatureLow = temperatureLow;
	}


	public long getTemperatureLowTime() {
		return TemperatureLowTime;
	}


	public void setTemperatureLowTime(long temperatureLowTime) {
		TemperatureLowTime = temperatureLowTime;
	}


	public double getApparentTemperatureHigh() {
		return ApparentTemperatureHigh;
	}


	public void setApparentTemperatureHigh(double apparentTemperatureHigh) {
		ApparentTemperatureHigh = apparentTemperatureHigh;
	}


	public long getApparentTemperatureHighTime() {
		return ApparentTemperatureHighTime;
	}


	public void setApparentTemperatureHighTime(long apparentTemperatureHighTime) {
		ApparentTemperatureHighTime = apparentTemperatureHighTime;
	}


	public double getApparentTemperatureLow() {
		return ApparentTemperatureLow;
	}


	public void setApparentTemperatureLow(double apparentTemperatureLow) {
		ApparentTemperatureLow = apparentTemperatureLow;
	}


	public long getApparentTemperatureLowTime() {
		return ApparentTemperatureLowTime;
	}


	public void setApparentTemperatureLowTime(long apparentTemperatureLowTime) {
		ApparentTemperatureLowTime = apparentTemperatureLowTime;
	}


	public double getWindSpeed() {
		return WindSpeed;
	}


	public void setWindSpeed(double windSpeed) {
		WindSpeed = windSpeed;
	}


	public String getPrecipType() {
		return PrecipType;
	}


	public void setPrecipType(String precipType) {
		PrecipType = precipType;
	}


	public String getSummaryIconFilePath() {
		if (Icon == null || Icon.length() == 0)
			return "";

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

		if (temperature < 25) {
			iconFileName = "Thermometer-Zero";
		} else if (temperature >= 25) {
			iconFileName = "Thermometer-25";
		} else if (temperature >= 50) {
			iconFileName = "Thermometer-50";
		} else if (temperature >= 75) {
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
			filePath = Paths.get(getClass().getResource("/course_generator/images/climacons/PNG/" + fileName).toURI());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath.toString();
	}

}
