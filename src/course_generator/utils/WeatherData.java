package course_generator.utils;

import java.time.Instant;
import java.util.Date;

import org.joda.time.DateTime;
import org.json.JSONObject;

import course_generator.settings.CgSettings;
import tk.plogitech.darksky.forecast.APIKey;
import tk.plogitech.darksky.forecast.DarkSkyClient;
import tk.plogitech.darksky.forecast.ForecastRequest;
import tk.plogitech.darksky.forecast.ForecastRequestBuilder;
import tk.plogitech.darksky.forecast.GeoCoordinates;
import tk.plogitech.darksky.forecast.model.Latitude;
import tk.plogitech.darksky.forecast.model.Longitude;

public class WeatherData {

	private DateTime Time;
	/** Counter **/
	private String Summary;// : "Overcast until afternoon.",
	// "icon": "cloudy",
	// "sunriseTime": 1473859401,
	// "sunsetTime": 1473904638,
	/** Moon phase **/
	private String MoonPhase;// :0.43,
	// "precipIntensity": 0,
	// "precipIntensityMax": 0,
	// "precipProbability": 0,
	private String TemperatureHigh;// ":54.27,
	// "temperatureHighTime": 1473894000,
	private String TemperatureLow;// ":32.42,
	// "temperatureLowTime": 1473944400,
	// "apparentTemperatureHigh": 54.27,
	// "apparentTemperatureHighTime": 1473894000,
	// "apparentTemperatureLow": 32.42,
	// "apparentTemperatureLowTime": 1473944400,
	// "dewPoint": 32.57,
	// "humidity": 0.63,
	// "pressure": 1016.68,
	private String WindSpeed;// ":0.7,
	// "windGust": 7.19,
	// "windGustTime": 1473883200,
	// "windBearing": 336,
	// "cloudCover": 0.49,
	// "uvIndex": 4,
	// "uvIndexTime": 1473879600,
	// "visibility": 3.97,
	private String TemperatureMin;// ":38.41,
	// "temperatureMinTime": 1473854400,
	private String TemperatureMax;// ":54.27,
	// "temperatureMaxTime": 1473894000,
	// "apparentTemperatureMin": 38.41,
	// "apparentTemperatureMinTime": 1473854400,
	// "apparentTemperatureMax": 54.27,
	// "apparentTemperatureMaxTime": 1473894000

	private CgSettings Settings;


	public WeatherData(CgSettings settings) {
		Settings = settings;
	}


	public void RetrieveWeatherData(Double latitude, Double longitude, Instant time) {
		ForecastRequest request = new ForecastRequestBuilder().key(new APIKey(Settings.getDarkSkyApiKey())).time(time)
				.language(ForecastRequestBuilder.Language.en).units(ForecastRequestBuilder.Units.si)
				.exclude(ForecastRequestBuilder.Block.minutely).extendHourly()
				.location(new GeoCoordinates(new Longitude(longitude), new Latitude(latitude))).build();

		DarkSkyClient client = new DarkSkyClient();
		try {
			String forecast = client.forecastJsonString(request);

			PopulateFields(forecast);

		} catch (Exception e) {
			CgLog.error("WeatherData.RetrieveWeatherData : Error while retrieving the weather data '" + e.getMessage()
					+ "'");
		}
	}


	private void PopulateFields(String forecastData) {

		try {

			JSONObject root = new JSONObject(forecastData);
			JSONObject dailyData = root.getJSONObject("daily").getJSONArray("data").getJSONObject(0);

			Time = retrieveDateTimeElement(dailyData, "time");
			Summary = retrieveStringElement(dailyData, "summary");
			MoonPhase = retrieveDoubleElement(dailyData, "moonPhase");
			TemperatureHigh = retrieveDoubleElement(dailyData, "temperatureHigh");
			TemperatureLow = retrieveDoubleElement(dailyData, "temperatureLow");
			WindSpeed = retrieveDoubleElement(dailyData, "windSpeed");
			TemperatureMin = retrieveDoubleElement(dailyData, "temperatureMin");
			TemperatureMax = retrieveDoubleElement(dailyData, "temperatureMax");
		} catch (Exception e) {
			CgLog.error("WeatherData.PopulateFields : Error while reading the weather data '" + e.getMessage() + "'");
		}

	}


	private String retrieveStringElement(JSONObject forecastData, String element) {
		String result;
		try {
			result = forecastData.getString(element);

		} catch (Exception e) {
			result = "";
		}
		return result;
	}


	private String retrieveDoubleElement(JSONObject forecastData, String element) {
		String result;
		try {
			result = String.valueOf(forecastData.getDouble(element));

		} catch (Exception e) {
			result = "";
		}
		return result;
	}


	private DateTime retrieveDateTimeElement(JSONObject forecastData, String element) {
		DateTime result;
		try {
			long unixtime = forecastData.getLong(element);
			Date millis = new java.util.Date(unixtime * 1000);
			result = new DateTime(millis);

		} catch (Exception e) {
			result = null;
		}
		return result;
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


	public String getTemperatureHigh() {
		return TemperatureHigh;
	}


	public String getTemperatureLow() {
		return TemperatureLow;
	}


	public String getWindSpeed() {
		return WindSpeed;
	}


	public String getTemperatureMin() {
		return TemperatureMin;
	}


	public String getTemperatureMax() {
		return TemperatureMax;
	}

}
