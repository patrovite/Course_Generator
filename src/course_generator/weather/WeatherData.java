package course_generator.weather;

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
	// "precipProbability": 0,
	/** The daytime high temperature. **/
	private String TemperatureHigh;
	// "temperatureHighTime": 1473894000,
	/** The overnight low temperature. **/
	private String TemperatureLow; // ":32.42,
	// "temperatureLowTime": 1473944400,
	// "apparentTemperatureHigh": 54.27,
	// "apparentTemperatureHighTime": 1473894000,
	// "apparentTemperatureLow": 32.42,
	// "apparentTemperatureLowTime": 1473944400,
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
	private String TemperatureMin;// ":38.41,
	// "temperatureMinTime": 1473854400,
	/** The maximum temperature during a given date **/
	private String TemperatureMax;// ":54.27,
	// "temperatureMaxTime": 1473894000,
	// "apparentTemperatureMin": 38.41,
	// "apparentTemperatureMinTime": 1473854400,
	// "apparentTemperatureMax": 54.27,
	// "apparentTemperatureMaxTime": 1473894000

	// Hourly data points
	private String Temperature;


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


	public String getSummary() {
		return Summary;
	}


	public String getMoonPhase() {
		return MoonPhase;
	}


	public void setMoonPhase(String moonPhase) {
		MoonPhase = moonPhase;
	}


	public String getTemperature() {
		return Temperature;
	}


	public void setTemperature(String temperature) {
		Temperature = temperature;
	}


	public String getTemperatureHigh() {
		return TemperatureHigh;
	}


	public void setTemperatureHigh(String temperatureHigh) {
		TemperatureHigh = temperatureHigh;
	}


	public String getTemperatureLow() {
		return TemperatureLow;
	}


	public void setTemperatureLow(String temperatureLow) {
		TemperatureLow = temperatureLow;
	}


	public String getWindSpeed() {
		return WindSpeed;
	}


	public void setWindSpeed(String windSpeed) {
		WindSpeed = windSpeed;
	}


	public String getTemperatureMin() {
		return TemperatureMin;
	}


	public void setTemperatureMin(String temperatureMin) {
		TemperatureMin = temperatureMin;
	}


	public String getTemperatureMax() {
		return TemperatureMax;
	}


	public void setTemperatureMax(String temperatureMax) {
		TemperatureMax = temperatureMax;
	}

}
