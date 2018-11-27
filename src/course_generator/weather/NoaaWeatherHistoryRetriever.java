package course_generator.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.joda.time.DateTime;
import org.joda.time.Instant;
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

	private NoaaWeatherStation noaaWeatherStation;

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
	public NoaaWeatherHistoryRetriever build() {

		findClosestStation("NORMAL_DLY");

		// GHCND
		// Retrieve daily summary for the year before the event
		// Retrieve daily summary 2 years before the event
		// Retrieve daily summary 3 years before the event

		// NORMAL_MLY
		// Retrieve the monthly normals

		// Loop and enlarge box if needed
		// Update southWest and northEast?

		return this;
	}


	private String getExtent(LatLng swPoint, LatLng nePoint) {

		return String.format("%.3f", swPoint.getLatitude()) + "," + String.format("%.3f", swPoint.getLongitude()) + ","
				+ String.format("%.3f", nePoint.getLatitude()) + "," + String.format("%.3f", nePoint.getLongitude());

	}


	private void findClosestStation(String dataSetId) {

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
		double minDistance = Integer.MAX_VALUE;
		try {
			List<NoaaWeatherStation> car = mapper.readValue(attributesContent,
					new TypeReference<List<NoaaWeatherStation>>() {
					});

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

		noaaWeatherStation = closestStation;
		noaaWeatherStation.setDistanceFromStart(minDistance);

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


	private NoaaDailyNormals processDailyNormals(String dailyNormalsData) {
		JSONObject jsonContent = new JSONObject(dailyNormalsData.toString());
		String attributesContent = jsonContent.get("results").toString();

		NoaaDailyNormals noaaDailyNormals = new NoaaDailyNormals();
		// Function to set the date ?
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<NoaaData> car = mapper.readValue(attributesContent, new TypeReference<List<NoaaData>>() {
			});

			for (NoaaData current : car) {
				switch (current.getDatatype()) {
				case "TMIN":
				case "DLY-TMIN-NORMAL":
				case "MLY-TMIN-NORMAL":
					noaaDailyNormals.setTemperatureMin(current.getValue());
					break;
				case "TMAX":
				case "DLY-TMAX-NORMAL":
				case "MLY-TMAX-NORMAL":
					noaaDailyNormals.setTemperatureMax(current.getValue());
					break;
				case "DLY-TAVG-NORMAL":
				case "MLY-TAVG-NORMAL":
					noaaDailyNormals.setTemperatureAverage(current.getValue());
					break;
				case "PRCP":
					noaaDailyNormals.setPrecipitation(current.getValue());
					break;
				}
				noaaDailyNormals.setDate(DateTime.parse(current.getDate()));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return noaaDailyNormals;
	}


	public NoaaDailyNormals retrieveDailyNormals() {
		String datePattern = "MM-dd";

		DateTimeFormatter fmt = DateTimeFormat.forPattern(datePattern);

		String findWeatherStation = "data?datasetid=NORMAL_DLY&datatypeid=DLY-TMIN-NORMAL&datatypeid=DLY-TMAX-NORMAL&datatypeid=DLY-TAVG-NORMAL&"
				+ "" + "stationid=" + noaaWeatherStation.getId() + "&startdate=2010-" + fmt.print(startDate)
				+ "&enddate=2010-" + fmt.print(startDate);

		String dailyNormalsData = processNoaaRequest(findWeatherStation);

		return processDailyNormals(dailyNormalsData);
	}


	public ArrayList<NoaaDailyNormals> retrieveDailySummaries() {
		ArrayList<NoaaDailyNormals> pastDailySummaries = new ArrayList<NoaaDailyNormals>();

		for (int pastYearNumber = 1; pastYearNumber < 4; ++pastYearNumber) {
			Instant time = Instant.ofEpochMilli(startDate.minusDays(pastYearNumber * 364).getMillis());

			String datePattern = "yyyy-MM-dd";
			DateTimeFormatter fmt = DateTimeFormat.forPattern(datePattern);
			String pastDate = fmt.print(time.toDateTime());

			String findWeatherStation = "data?datasetid=GHCND&stationid=" + noaaWeatherStation.getId() + "&startdate="
					+ pastDate + "&enddate=" + pastDate;

			String dailyNormalsData = processNoaaRequest(findWeatherStation);
			pastDailySummaries.add(processDailyNormals(dailyNormalsData));
		}

		return pastDailySummaries;
	}


	public NoaaDailyNormals retrieveMonthlyNormals() {
		String datePattern = "2010-MM-01";

		DateTimeFormatter fmt = DateTimeFormat.forPattern(datePattern);

		String findWeatherStation = "data?datasetid=NORMAL_MLY&datatypeid=MLY-TMIN-NORMAL&datatypeid=MLY-TMAX-NORMAL&datatypeid=MLY-TAVG-NORMAL&"
				+ "" + "stationid=" + noaaWeatherStation.getId() + "&startdate=" + fmt.print(startDate) + "&enddate="
				+ fmt.print(startDate);

		String dailyNormalsData = processNoaaRequest(findWeatherStation);

		return processDailyNormals(dailyNormalsData);
	}


	public NoaaWeatherStation getWeatherStation() {
		return noaaWeatherStation;
	}

}
