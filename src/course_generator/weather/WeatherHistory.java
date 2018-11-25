package course_generator.weather;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.json.JSONObject;

import course_generator.CgData;
import course_generator.TrackData;
import course_generator.settings.CgSettings;
import course_generator.utils.CgLog;
import course_generator.utils.Utils;

public class WeatherHistory {

	private NoaaDailyNormals dailyNormals;
	private ArrayList<NoaaDailySummary> previousDailySummaries;
	private WeatherData MonthlyNormals;
	private CgSettings Settings;
	private TrackData Track;

	public WeatherHistory(CgSettings settings, TrackData track) {
		Settings = settings;
		Track = track;
	}

	public WeatherHistory() {
		// TODO Auto-generated constructor stub
	}

	public void RetrieveWeatherData() {
		// TODO
		// searchArea = CreateBox()

		// Get the start point and furthest point
		// Create a box that includes both of the points
		// Find the center and the distance between the center and the end of the box

		CgData firstTrackPoint = Track.data.get(0);

		DateTime startTime = new DateTime(Utils.DateTimetoSpinnerDate(firstTrackPoint.getHour()));
		// Instant time = Instant.ofEpochMilli(startTime.minusDays(PreviousYearNumber *
		// 364).getMillis());

		try {
			String weatherHistoryContent = NoaaWeatherHistoryRetriever.where(45, 115).when(firstTrackPoint.getHour())
					.forUser(Settings.getNoaaToken()).retrieve();

			PopulateFields(weatherHistoryContent);

			UpdateTrackWeatherData();

		} catch (Exception e) {
			CgLog.error("WeatherData.RetrieveWeatherData : Error while retrieving the weather data '" + e.getMessage()
					+ "'");
		}
	}

	private void PopulateFields(String forecastData) {

		// TODO Deserialize each single element Dailynormals....

		/*
		 * try { JSONObject root = new JSONObject(forecastData); JSONArray hourlyData =
		 * root.getJSONObject("hourly").getJSONArray("data"); for (int index = 0; index
		 * < hourlyData.length(); ++index) { WeatherData hourlyWeatherData = new
		 * WeatherData();
		 * hourlyWeatherData.setTime(retrieveLongElement(hourlyData.getJSONObject(index)
		 * , "time"));
		 * 
		 * Hourly.add(hourlyWeatherData); }
		 * 
		 * TimeZone = root.getString("timezone");
		 * 
		 * JSONObject dailyData =
		 * root.getJSONObject("daily").getJSONArray("data").getJSONObject(0);
		 * 
		 * Daily.setTime(retrieveLongElement(dailyData, "time"));
		 * Daily.setSummary(retrieveStringElement(dailyData, "summary"));
		 * Daily.setIcon(retrieveStringElement(dailyData, "icon"));
		 * Daily.setMoonPhase(retrieveDoubleElement(dailyData, "moonPhase"));
		 * Daily.setPrecipType(retrieveStringElement(dailyData, "precipType"));
		 * Daily.setTemperatureHigh(retrieveDoubleElement(dailyData,
		 * "temperatureHigh"));
		 * Daily.setTemperatureHighTime(retrieveLongElement(dailyData,
		 * "temperatureHighTime"));
		 * Daily.setTemperatureLow(retrieveDoubleElement(dailyData, "temperatureLow"));
		 * Daily.setTemperatureLowTime(retrieveLongElement(dailyData,
		 * "temperatureLowTime"));
		 * Daily.setApparentTemperatureHigh(retrieveDoubleElement(dailyData,
		 * "apparentTemperatureHigh"));
		 * Daily.setApparentTemperatureHighTime(retrieveLongElement(dailyData,
		 * "apparentTemperatureHighTime"));
		 * Daily.setApparentTemperatureLow(retrieveDoubleElement(dailyData,
		 * "apparentTemperatureLow"));
		 * Daily.setApparentTemperatureLowTime(retrieveLongElement(dailyData,
		 * "apparentTemperatureLowTime"));
		 * Daily.setWindSpeed(retrieveDoubleElement(dailyData, "windSpeed"));
		 * 
		 * } catch (Exception e) { CgLog.
		 * error("WeatherData.PopulateFields : Error while reading the weather data '" +
		 * e.getMessage() + "'"); }
		 */
	}

	private void UpdateTrackWeatherData() {
		// Track.setDailyWeatherData(this, PreviousYearNumber - 1);
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
	/*
	 * public ArrayList<WeatherData> getHourlyWeather() { return Hourly; }
	 * 
	 * 
	 * public WeatherData getDailyWeatherData() { return Daily; }
	 * 
	 * 
	 * public String getTimeZone() { return TimeZone; }
	 */
}
