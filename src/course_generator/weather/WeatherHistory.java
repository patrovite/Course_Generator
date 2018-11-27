package course_generator.weather;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.json.JSONObject;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import course_generator.CgData;
import course_generator.TrackData;
import course_generator.settings.CgSettings;

public class WeatherHistory {

	public NoaaDailyNormals dailyNormals;
	public ArrayList<NoaaDailyNormals> previousDailySummaries;
	public NoaaWeatherStation weatherStation;
	private WeatherData MonthlyNormals;
	private CgSettings Settings;
	private TrackData Track;
	private LatLng searchAreaCenter;
	private double searchAreaRadius;


	public WeatherHistory(CgSettings settings) {
		Settings = settings;
	}


	public void RetrieveWeatherData(TrackData track) {
		if (track == null)
			return;

		Track = track;
		DateTime startTime = Track.data.get(0).getHour();

		determineWeatherSearchArea();

		NoaaWeatherHistoryRetriever weatherHistoryRetriever = NoaaWeatherHistoryRetriever
				.where(searchAreaCenter, searchAreaRadius).when(startTime).forUser(Settings.getNoaaToken()).build();

		weatherStation = weatherHistoryRetriever.getWeatherStation();
		dailyNormals = weatherHistoryRetriever.retrieveDailyNormals();
		previousDailySummaries = weatherHistoryRetriever.retrieveDailySummaries();
		// PopulateFields(weatherHistoryContent);

		// MoonIllumination dd =
		// MoonIllumination.compute().on(startTime.toDate()).execute();
		// dd.getPhase();

		UpdateTrackWeatherData();

	}


	/**
	 * is to encompass most of the track to search a weather station as close as
	 * possible to the course
	 */
	private void determineWeatherSearchArea() {
		// Looking for the furthest point of the track
		double maxDistance = Double.MIN_VALUE;
		LatLng furthestPoint = null;
		LatLng startPoint = new LatLng(Track.data.get(0).getLatitude(), Track.data.get(0).getLongitude());
		for (CgData dataPoint : Track.data) {
			LatLng currentPoint = new LatLng(dataPoint.getLatitude(), dataPoint.getLongitude());

			double distanceFromStart = LatLngTool.distance(startPoint, currentPoint, LengthUnit.METER);

			if (distanceFromStart > maxDistance) {
				maxDistance = distanceFromStart;
				furthestPoint = currentPoint;
			}
		}

		// We find the center of the box formed by the starting point and
		// the furthest point

		double distanceFromStart = LatLngTool.distance(startPoint, furthestPoint, LengthUnit.METER);
		double bearingBetweenPoint = LatLngTool.initialBearing(startPoint, furthestPoint);

		searchAreaCenter = LatLngTool.travel(startPoint, bearingBetweenPoint, distanceFromStart / 2, LengthUnit.METER);
		searchAreaRadius = distanceFromStart / 2;

	}


	private void PopulateFields(String weatherHistoryContent) {

		// TODO Deserialize each single element Dailynormals....

		// try {
		// NoaaDailyNormals userFromJSON = mapper.readValue(forecastData,
		// NoaaDailyNormals.class);
		// String toto = userFromJSON.toString();
		// System.out.println(toto);
		// } catch (IOException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }

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
