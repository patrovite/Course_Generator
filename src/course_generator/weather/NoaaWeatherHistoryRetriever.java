package course_generator.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

final public class NoaaWeatherHistoryRetriever {

	private LatLng southWest;// The south west location of the desired geographical extent for search
	private LatLng northEast;// The north east location of the desired geographical extent for search
	private LatLng stationSearchStartingPoint;
	private DateTime startDate;
	private String noaaToken;

	private NoaaDailyNormals dailyNormalsData;

	private final String NoaaApiUrl = "https://www.ncdc.noaa.gov/cdo-web/api/v2/";


	private NoaaWeatherHistoryRetriever(double stationSearchStartingPointlat, double stationSearchStartingPointLon,
			double initialSearchDistance) {
		stationSearchStartingPoint = new LatLng(stationSearchStartingPointlat, stationSearchStartingPointLon);
		// TODO pass the distance to search a station from ?
	}


	private NoaaWeatherHistoryRetriever(String noaaToken) {
		this.noaaToken = noaaToken;
	}


	/**
	 *
	 *
	 * @param track
	 * 
	 * @return
	 */
	public static NoaaWeatherHistoryRetriever where(double southWestPointLat, double southWestPointLon) {
		// TODO Pass only the start and furthest point ?
		// If not, the where() is useless as we can get the first point time from the
		// track
		return new NoaaWeatherHistoryRetriever(southWestPointLat, southWestPointLon, 1.0);
	}


	/**
	 *
	 *
	 * @param track
	 * 
	 * @return
	 */
	public NoaaWeatherHistoryRetriever when(DateTime dateTime) {
		this.startDate = dateTime;
		return this;
	}


	/**
	 *
	 *
	 * @param track
	 * 
	 * @return
	 */
	public NoaaWeatherHistoryRetriever forUser(String noaaToken) {
		this.noaaToken = noaaToken;
		return this;
	}


	/**
	 * This method actually builds the WeatherData object
	 *
	 * @return the collected weather data.
	 */
	public WeatherHistory retrieve() {

		// Get the closest one.

		WeatherHistory historicalWeather = new WeatherHistory();
		// NORMAL_DLY
		// Retrieve daily normals
		String stationId = findClosestStation("NORMAL_DLY");
		dailyNormalsData = retrieveDailyNormals(stationId);// "GHCND:USC00421259");

		// GHCND
		// Retrieve daily summary for the year before the event
		// Retrieve daily summary 2 years before the event
		// Retrieve daily summary 3 years before the event

		// NORMAL_MLY
		// Retrieve the monthly normals

		// Loop and enlarge box if needed
		// Update southWest and northEast?

		WeatherHistory wh = new WeatherHistory();
		wh.dailyNormals = dailyNormalsData;

		return wh;
	}


	private String getExtent(LatLng swPoint, LatLng nePoint) {

		return String.format("%.3f", swPoint.getLatitude()) + "," + String.format("%.3f", swPoint.getLongitude()) + ","
				+ String.format("%.3f", nePoint.getLatitude()) + "," + String.format("%.3f", nePoint.getLongitude());

	}


	private String findClosestStation(String dataSetId) {
		String findWeatherStation = "stations?extent=";
		LatLng m = LatLngTool.travel(stationSearchStartingPoint, 45, 100, LengthUnit.KILOMETER);

		findWeatherStation = findWeatherStation + getExtent(stationSearchStartingPoint, m) + "&datasetid=" + dataSetId;

		String weatherHistory = processNoaaRequest(findWeatherStation);

		JSONObject jsonContent = new JSONObject(weatherHistory.toString());
		String attributesContent = jsonContent.get("results").toString();

		ObjectMapper mapper = new ObjectMapper();
		NoaaWeatherStation closestStation = null;
		try {
			List<NoaaWeatherStation> car = mapper.readValue(attributesContent,
					new TypeReference<List<NoaaWeatherStation>>() {
					});

			double minDistance = Integer.MAX_VALUE;

			for (NoaaWeatherStation current : car) {
				LatLng station = new LatLng(Double.valueOf(current.getLatitude()),
						Double.valueOf(current.getLongitude()));

				double distance = LatLngTool.distance(station, stationSearchStartingPoint, LengthUnit.KILOMETER);
				if (distance < minDistance) {
					minDistance = distance;
					closestStation = current;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return closestStation.getId();
	}


	private String processNoaaRequest(String parameters) {
		StringBuffer weatherHistory = new StringBuffer();
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(NoaaApiUrl + parameters);

			// add request header
			request.addHeader("token", noaaToken);
			HttpResponse response = client.execute(request);

			System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			String line = "";
			while ((line = rd.readLine()) != null) {
				weatherHistory.append(line);
			}
		} catch (Exception ex) {
			// TODO log error in the CG log
		}
		return weatherHistory.toString();
	}


	private NoaaDailyNormals retrieveDailyNormals(String stationId) {
		String datePattern = "MM-dd";
		// Search for a weather station
		LatLng m = LatLngTool.travel(stationSearchStartingPoint, 45, 100, LengthUnit.KILOMETER);

		//
		DateTimeFormatter fmt = DateTimeFormat.forPattern(datePattern);

		String findWeatherStation = "data?datasetid=GHCND&stationid=" + stationId + "&startdate=2010-"
				+ fmt.print(startDate) + "&enddate=2010-" + fmt.print(startDate);

		String dailyNormalsData = processNoaaRequest(findWeatherStation);

		NoaaDailyNormals dailyNormals = processDailyNormals(dailyNormalsData);

		return dailyNormals;
	}


	private NoaaDailyNormals processDailyNormals(String dailyNormalsData) {
		JSONObject jsonContent = new JSONObject(dailyNormalsData.toString());
		String attributesContent = jsonContent.get("results").toString();

		NoaaDailyNormals noaaDailyNormals = new NoaaDailyNormals();

		ObjectMapper mapper = new ObjectMapper();
		NoaaWeatherStation closestStation = null;
		try {
			List<NoaaData> car = mapper.readValue(attributesContent, new TypeReference<List<NoaaData>>() {
			});

			double minDistance = Integer.MAX_VALUE;

			for (NoaaData current : car) {
				switch (current.getDatatype()) {
				case "TMIN":
					noaaDailyNormals.setTemperatureMin(current.getValue());
					break;
				case "TMAX":
					noaaDailyNormals.setTemperatureMax(current.getValue());
					break;
				}
			}

		} catch (

		IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return noaaDailyNormals;
	}
}
