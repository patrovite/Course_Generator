package course_generator.weather;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import course_generator.settings.CgSettings;
import course_generator.utils.CgLog;
import tk.plogitech.darksky.forecast.APIKey;
import tk.plogitech.darksky.forecast.DarkSkyClient;
import tk.plogitech.darksky.forecast.ForecastRequest;
import tk.plogitech.darksky.forecast.ForecastRequestBuilder;
import tk.plogitech.darksky.forecast.GeoCoordinates;
import tk.plogitech.darksky.forecast.model.Latitude;
import tk.plogitech.darksky.forecast.model.Longitude;

public class WeatherHistory {

	private ArrayList<WeatherData> Hourly;
	private WeatherData Daily;
	private CgSettings Settings;
	private double Latitude;
	private double Longitude;


	public WeatherHistory(CgSettings settings, Double latitude, Double longitude) {
		Settings = settings;
		Latitude = latitude;
		Longitude = longitude;
		Daily = new WeatherData();
		Hourly = new ArrayList<WeatherData>();
	}


	public void RetrieveWeatherData(Instant time) {
		ForecastRequest request = new ForecastRequestBuilder().key(new APIKey(Settings.getDarkSkyApiKey())).time(time)
				.language(ForecastRequestBuilder.Language.en).units(ForecastRequestBuilder.Units.si)
				.exclude(ForecastRequestBuilder.Block.minutely).extendHourly()
				.location(new GeoCoordinates(new Longitude(Longitude), new Latitude(Latitude))).build();

		DarkSkyClient client = new DarkSkyClient();
		try {
			String forecast = client.forecastJsonString(request);

			PopulateFields(forecast);

			// UpdateTrackWeatherData()

		} catch (Exception e) {
			CgLog.error("WeatherData.RetrieveWeatherData : Error while retrieving the weather data '" + e.getMessage()
					+ "'");
		}
	}


	private void PopulateFields(String forecastData) {

		try {
			JSONObject root = new JSONObject(forecastData);
			JSONArray hourlyData = root.getJSONObject("hourly").getJSONArray("data");
			for (int index = 0; index < hourlyData.length(); ++index) {
				WeatherData hourlyWeatherData = new WeatherData();
				hourlyWeatherData.setTime(retrieveDateTimeElement(hourlyData.getJSONObject(index), "time"));
				hourlyWeatherData.setTemperature(retrieveDoubleElement(hourlyData.getJSONObject(index), "temperature"));

				Hourly.add(hourlyWeatherData);
			}

			JSONObject dailyData = root.getJSONObject("daily").getJSONArray("data").getJSONObject(0);

			Daily.setTime(retrieveDateTimeElement(dailyData, "time"));
			Daily.setSummary(retrieveStringElement(dailyData, "summary"));
			Daily.setIcon(retrieveStringElement(dailyData, "icon"));
			Daily.setMoonPhase(retrieveDoubleElement(dailyData, "moonPhase"));
			Daily.setPrecipAccumulation(retrieveDoubleElement(dailyData, "precipAccumulation"));
			Daily.setPrecipType(retrieveStringElement(dailyData, "precipType"));
			Daily.setTemperatureHigh(retrieveDoubleElement(dailyData, "temperatureHigh"));
			Daily.setTemperatureHighTime(retrieveLongElement(dailyData, "temperatureHighTime"));
			Daily.setTemperatureLow(retrieveDoubleElement(dailyData, "temperatureLow"));
			Daily.setTemperatureLowTime(retrieveLongElement(dailyData, "temperatureLowTime"));
			Daily.setApparentTemperatureHigh(retrieveDoubleElement(dailyData, "apparentTemperatureHigh"));
			Daily.setApparentTemperatureHighTime(retrieveLongElement(dailyData, "apparentTemperatureHighTime"));
			Daily.setApparentTemperatureLow(retrieveDoubleElement(dailyData, "apparentTemperatureLow"));
			Daily.setApparentTemperatureLowTime(retrieveLongElement(dailyData, "apparentTemperatureLowTime"));
			Daily.setWindSpeed(retrieveDoubleElement(dailyData, "windSpeed"));

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


	private String retrieveLongElement(JSONObject forecastData, String element) {
		String result;
		try {
			result = String.valueOf(forecastData.getLong(element));

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


	public ArrayList<WeatherData> getHourlyWeather() {
		return Hourly;
	}


	public WeatherData getDailyWeatherData() {
		return Daily;
	}


	public String getSummaryIconFilePath() {
		String iconFileName = "";
		switch (Daily.getIcon().toLowerCase()) {
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
		switch (Daily.getPrecipType().toLowerCase()) {
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
