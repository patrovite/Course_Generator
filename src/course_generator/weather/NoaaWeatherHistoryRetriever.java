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

	private DateTime startDate;
	private String noaaToken;

	private LatLng searchAreaSouthWestCorner;// The south west location of the desired geographical extent for search
	private LatLng searchAreaNorthEastCorner;// The north east location of the desired geographical extent for search

	private LatLng searchAreaCenter;
	private double searchAreaRadius;

	private NoaaDailyNormals dailyNormalsData;

	private final String NoaaApiUrl = "https://www.ncdc.noaa.gov/cdo-web/api/v2/";


	private NoaaWeatherHistoryRetriever(LatLng searchAreaCenter, double searchAreaRadius) {
		this.searchAreaCenter = searchAreaCenter;
		this.searchAreaRadius = searchAreaRadius;

		computeSearchArea(1);
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
	public static NoaaWeatherHistoryRetriever where(LatLng searchAreaCenter, double searchAreaRadius) {
		return new NoaaWeatherHistoryRetriever(searchAreaCenter, searchAreaRadius);
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
	public String retrieve() {

		// Get the closest one.

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

		return dailyNormalsData.toString();
	}


	private String getExtent(LatLng swPoint, LatLng nePoint) {

		return String.format("%.3f", swPoint.getLatitude()) + "," + String.format("%.3f", swPoint.getLongitude()) + ","
				+ String.format("%.3f", nePoint.getLatitude()) + "," + String.format("%.3f", nePoint.getLongitude());

	}


	private String findClosestStation(String dataSetId) {

		String weatherHistory = "";
		int enlargingFactor = 1;
		while (!weatherHistory.contains("results")) {
			String findWeatherStation = "stations?extent=";

			findWeatherStation = findWeatherStation + getExtent(searchAreaSouthWestCorner, searchAreaNorthEastCorner)
					+ "&datasetid=" + dataSetId;

			weatherHistory = processNoaaRequest(findWeatherStation);

			enlargingFactor *= 2;
			computeSearchArea(enlargingFactor);
		}

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

				double distance = LatLngTool.distance(station, searchAreaCenter, LengthUnit.METER);
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


	private void computeSearchArea(int enlargingFactor) {
		// We find the south west corner of the box
		searchAreaSouthWestCorner = LatLngTool.travel(searchAreaCenter, 225, searchAreaRadius * enlargingFactor,
				LengthUnit.METER);
		searchAreaNorthEastCorner = LatLngTool.travel(searchAreaCenter, 45, searchAreaRadius * enlargingFactor,
				LengthUnit.METER);
	}


	private String processNoaaRequest(String parameters) {
		StringBuffer weatherHistory = new StringBuffer();
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(NoaaApiUrl + parameters + "&units=metric");

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
