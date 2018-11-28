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

import course_generator.utils.CgLog;

/**
 *
 * @author Frederic Bard
 */
final public class NoaaWeatherHistoryRetriever {

	private DateTime startDate;
	private String noaaToken;

	private LatLng searchAreaSouthWestCorner;// The south west location of the desired geographical extent for search
	private LatLng searchAreaNorthEastCorner;// The north east location of the desired geographical extent for search

	private LatLng searchAreaCenter;
	private double searchAreaRadius;

	private NoaaWeatherStation noaaNormalsWeatherStation;
	private NoaaWeatherStation noaaSummariesWeatherStation;

	private final String NoaaApiUrl = "https://www.ncdc.noaa.gov/cdo-web/api/v2/";


	private NoaaWeatherHistoryRetriever(LatLng searchAreaCenter, double searchAreaRadius) {
		this.searchAreaCenter = searchAreaCenter;

		// We want a search area of minimum 50km
		this.searchAreaRadius = searchAreaRadius > 50000.0 ? searchAreaRadius : 50000.0;

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

		computeSearchArea();

		// Maximize to 50 miles ?
		// If we find the NORMAL_DLY, liely we find NORMAL_MLY
		noaaNormalsWeatherStation = findClosestStation("NORMAL_DLY");

		noaaSummariesWeatherStation = findClosestStation("GHCND");
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


	private NoaaWeatherStation findClosestStation(String dataSetId) {

		String findWeatherStation = "stations?extent=" + getExtent(searchAreaSouthWestCorner, searchAreaNorthEastCorner)
				+ "&datasetid=" + dataSetId + "&limit=1000";

		int startDate2 = Integer.valueOf(startDate.getYear()) - 3;
		String startDate3 = String.valueOf(startDate2);
		if (dataSetId == "GHCND")
			findWeatherStation = findWeatherStation + "&startdate=" + startDate3 + "-01-01" + "&enddate="
					+ startDate.getYear() + "-12-31";

		String weatherHistory = processNoaaRequest(findWeatherStation);

		if (weatherHistory == "" || !weatherHistory.contains("results"))
			return null;

		JSONObject jsonContent = new JSONObject(weatherHistory.toString());

		String attributesContent = jsonContent.get("results").toString();

		ObjectMapper mapper = new ObjectMapper();
		NoaaWeatherStation closestStation = null;
		double minDistance = Integer.MAX_VALUE;
		try {
			List<NoaaWeatherStation> stations = mapper.readValue(attributesContent,
					new TypeReference<List<NoaaWeatherStation>>() {
					});

			for (NoaaWeatherStation current : stations) {
				LatLng station = new LatLng(Double.valueOf(current.getLatitude()),
						Double.valueOf(current.getLongitude()));

				double distance = LatLngTool.distance(station, searchAreaCenter, LengthUnit.METER);
				if (distance < minDistance) {
					minDistance = distance;
					closestStation = current;
				}
			}
		} catch (IOException e) {
			CgLog.error(
					"NoaaWeatherHistoryRetriever.findClosestStation : Error while searching for the closest weather station for the datasetid "
							+ dataSetId + "\n" + e.getMessage());

		}
		// TODO DOUBLE CHECK THAT A REQUEST WILL ACTUALLY GIVE DATA< OTHERWISE< WE NEED
		// ANOTHER ONE
		closestStation.setDistanceFromStart(minDistance);

		return closestStation;
	}


	private void computeSearchArea() {
		// We find the south west corner of the box
		searchAreaSouthWestCorner = LatLngTool.travel(searchAreaCenter, 225, searchAreaRadius, LengthUnit.METER);
		searchAreaNorthEastCorner = LatLngTool.travel(searchAreaCenter, 45, searchAreaRadius, LengthUnit.METER);
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


	public ArrayList<NoaaDailyNormals> retrieveDailySummaries() {
		if (noaaSummariesWeatherStation == null)
			return null;

		ArrayList<NoaaDailyNormals> pastDailySummaries = new ArrayList<NoaaDailyNormals>();

		for (int pastYearNumber = 1; pastYearNumber < 4; ++pastYearNumber) {
			Instant time = Instant.ofEpochMilli(startDate.minusDays(pastYearNumber * 364).getMillis());

			String datePattern = "yyyy-MM-dd";
			DateTimeFormatter fmt = DateTimeFormat.forPattern(datePattern);
			String pastDate = fmt.print(time.toDateTime());

			String findWeatherStation = "data?datasetid=GHCND&stationid=" + noaaSummariesWeatherStation.getId()
					+ "&startdate=" + pastDate + "&enddate=" + pastDate;

			String dailyNormalsData = processNoaaRequest(findWeatherStation);
			NoaaDailyNormals toto = new NoaaDailyNormals();
			if (!dailyNormalsData.contains("results"))
				pastDailySummaries.add(toto);
			else {
				toto = processDailyNormals(dailyNormalsData);
				toto.setDate(time.toDateTime());
				pastDailySummaries.add(toto);
			}
		}

		return pastDailySummaries;
	}


	public NoaaDailyNormals retrieveDailyNormals() {
		if (noaaNormalsWeatherStation == null)
			return null;

		String datePattern = "MM-dd";

		DateTimeFormatter fmt = DateTimeFormat.forPattern(datePattern);

		String findWeatherStation = "data?datasetid=NORMAL_DLY&datatypeid=DLY-TMIN-NORMAL&datatypeid=DLY-TMAX-NORMAL&datatypeid=DLY-TAVG-NORMAL&"
				+ "" + "stationid=" + noaaNormalsWeatherStation.getId() + "&startdate=2010-" + fmt.print(startDate)
				+ "&enddate=2010-" + fmt.print(startDate);

		String dailyNormalsData = processNoaaRequest(findWeatherStation);
		if (!dailyNormalsData.contains("results"))
			return new NoaaDailyNormals();

		return processDailyNormals(dailyNormalsData);
	}


	public NoaaDailyNormals retrieveMonthlyNormals() {
		if (noaaNormalsWeatherStation == null)
			return null;

		String datePattern = "2010-MM-01";

		DateTimeFormatter fmt = DateTimeFormat.forPattern(datePattern);

		String findWeatherStation = "data?datasetid=NORMAL_MLY&datatypeid=MLY-TMIN-NORMAL&datatypeid=MLY-TMAX-NORMAL&datatypeid=MLY-TAVG-NORMAL&"
				+ "" + "stationid=" + noaaNormalsWeatherStation.getId() + "&startdate=" + fmt.print(startDate)
				+ "&enddate=" + fmt.print(startDate);

		String dailyNormalsData = processNoaaRequest(findWeatherStation);
		if (!dailyNormalsData.contains("results"))
			return new NoaaDailyNormals();

		return processDailyNormals(dailyNormalsData);
	}


	public NoaaWeatherStation getNoaaNormalsWeatherStation() {
		return noaaNormalsWeatherStation;
	}


	public NoaaWeatherStation getNoaaSummariesWeatherStation() {
		return noaaSummariesWeatherStation;
	}

}
