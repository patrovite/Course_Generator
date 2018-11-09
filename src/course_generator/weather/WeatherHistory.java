package course_generator.weather;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.Instant;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import course_generator.CgData;
import course_generator.TrackData;
import course_generator.settings.CgSettings;
import course_generator.utils.CgLog;
import course_generator.utils.Utils;
import tk.plogitech.darksky.forecast.APIKey;
import tk.plogitech.darksky.forecast.DarkSkyClient;
import tk.plogitech.darksky.forecast.ForecastRequest;
import tk.plogitech.darksky.forecast.ForecastRequestBuilder;
import tk.plogitech.darksky.forecast.GeoCoordinates;
import tk.plogitech.darksky.forecast.model.Latitude;
import tk.plogitech.darksky.forecast.model.Longitude;

public class WeatherHistory {

	private String TimeZone;
	private ArrayList<WeatherData> Hourly;
	private WeatherData Daily;
	private CgSettings Settings;
	private TrackData Track;
	private double Latitude;
	private double Longitude;
	private int PreviousYearNumber;


	public WeatherHistory(CgSettings settings, TrackData track, Double latitude, Double longitude,
			int previousYearNumber) {
		Settings = settings;
		Track = track;
		Latitude = latitude;
		Longitude = longitude;
		Daily = new WeatherData();
		Hourly = new ArrayList<WeatherData>();
		PreviousYearNumber = previousYearNumber;
	}


	public WeatherHistory(String timeZone, WeatherData dailyData) {
		TimeZone = timeZone;
		Daily = dailyData;
	}


	public void RetrieveWeatherData() {
		CgData firstTrackPoint = Track.data.get(0);

		DateTime startTime = new DateTime(Utils.DateTimetoSpinnerDate(firstTrackPoint.getHour()));
		Instant time = Instant.ofEpochMilli(startTime.minusYears(PreviousYearNumber).getMillis());

		ForecastRequest request = new ForecastRequestBuilder().key(new APIKey(Settings.getDarkSkyApiKey())).time(time)
				.language(ForecastRequestBuilder.Language.en).units(ForecastRequestBuilder.Units.si)
				.exclude(ForecastRequestBuilder.Block.minutely).extendHourly()
				.location(new GeoCoordinates(new Longitude(Longitude), new Latitude(Latitude))).build();

		DarkSkyClient client = new DarkSkyClient();
		try {
			String forecast = client.forecastJsonString(request);

			BufferedWriter bufferedWriter = null;
			try {
				File myFile = new File(
						"C:/Users/frederic/git/mytourbook/forecastupdated" + PreviousYearNumber + ".json");
				// check if file exist, otherwise create the file before writing
				if (!myFile.exists()) {
					myFile.createNewFile();
				}
				Writer writer = new FileWriter(myFile);
				bufferedWriter = new BufferedWriter(writer);
				bufferedWriter.write(forecast);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (bufferedWriter != null)
						bufferedWriter.close();
				} catch (Exception ex) {

				}
			}

			PopulateFields(forecast);

			UpdateTrackWeatherData();

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
				hourlyWeatherData.setTime(retrieveLongElement(hourlyData.getJSONObject(index), "time"));
				// hourlyWeatherData.setTemperature(retrieveDoubleElement(hourlyData.getJSONObject(index),
				// "temperature"));

				Hourly.add(hourlyWeatherData);
			}

			TimeZone = root.getString("timezone");

			JSONObject dailyData = root.getJSONObject("daily").getJSONArray("data").getJSONObject(0);

			Daily.setTime(retrieveLongElement(dailyData, "time"));
			Daily.setSummary(retrieveStringElement(dailyData, "summary"));
			Daily.setIcon(retrieveStringElement(dailyData, "icon"));
			Daily.setMoonPhase(retrieveDoubleElement(dailyData, "moonPhase"));
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


	private void UpdateTrackWeatherData() {
		Track.setDailyWeatherData(this, PreviousYearNumber - 1);
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


	private double retrieveDoubleElement(JSONObject forecastData, String element) {
		double result;
		try {
			result = forecastData.getDouble(element);

		} catch (Exception e) {
			result = 0.0;
		}
		return result;
	}


	private long retrieveLongElement(JSONObject forecastData, String element) {
		long result;
		try {
			result = forecastData.getLong(element);

		} catch (Exception e) {
			result = 0;
		}
		return result;
	}


	/*
	 * private long retrieveTimeElement(JSONObject forecastData, String element) {
	 * long result; try { //long unixtime = forecastData.getLong(element); // Date
	 * millis = new java.util.Date(unixtime * 1000); //result = new
	 * DateTime(millis); } catch (Exception e) { result = null; } return result; }
	 */

	public ArrayList<WeatherData> getHourlyWeather() {
		return Hourly;
	}


	public WeatherData getDailyWeatherData() {
		return Daily;
	}


	public String getTimeZone() {
		return TimeZone;
	}

}
