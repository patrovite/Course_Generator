package course_generator.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import course_generator.utils.CgLog;

/**
 * A class that retrieves, for a given track, the historical weather data.
 * 
 * @author Frederic Bard
 */
final public class NoaaHistoricalWeatherRetriever {

	private DateTime startDate;
	private String noaaToken;

	private LatLng searchAreaSouthWestCorner;// The south west location of the desired geographical extent for search
	private LatLng searchAreaNorthEastCorner;// The north east location of the desired geographical extent for search

	private LatLng searchAreaCenter;
	private double searchAreaRadius;
	private final double maxSearchAreaRadius = 100000.0; // 100km

	private NoaaWeatherStation noaaNormalsWeatherStation;
	private NoaaWeatherStation noaaSummariesWeatherStation;
	private List<NoaaWeatherData> pastDailySummaries;
	private NoaaWeatherData noaaNormalsDaily;
	private NoaaWeatherData noaaNormalsMonthly;

	private final String NoaaApiUrl = "https://www.ncdc.noaa.gov/cdo-web/api/v2/";

	private NoaaHistoricalWeatherRetriever(LatLng searchAreaCenter, double searchAreaRadius) {
		this.searchAreaCenter = searchAreaCenter;

		// We want a search area of minimum 50km
		this.searchAreaRadius = searchAreaRadius > maxSearchAreaRadius ? searchAreaRadius : maxSearchAreaRadius;

	}

	private NoaaHistoricalWeatherRetriever(String noaaToken) {
		this.noaaToken = noaaToken;
	}

	/**
	 *
	 *
	 * @param track
	 * 
	 * @return
	 */
	public static NoaaHistoricalWeatherRetriever where(LatLng searchAreaCenter, double searchAreaRadius) {
		return new NoaaHistoricalWeatherRetriever(searchAreaCenter, searchAreaRadius);
	}

	/**
	 *
	 *
	 * @param track
	 * 
	 * @return
	 */
	public NoaaHistoricalWeatherRetriever when(DateTime dateTime) {
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
	public NoaaHistoricalWeatherRetriever forUser(String noaaToken) {
		this.noaaToken = noaaToken;
		return this;
	}

	/**
	 * This method actually builds the WeatherData object
	 *
	 * @return the collected weather data.
	 */
	public NoaaHistoricalWeatherRetriever retrieve() {

		computeSearchArea();

		pastDailySummaries = findMostRelevantDailySummaries();

		noaaNormalsDaily = findMostRelevantNormalsDaily();
		noaaNormalsMonthly = retrieveNormalsMonthly();

		// noaaNormalsWeatherStation = retrieveSummariesData("NORMAL_DLY");

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

	private List<NoaaWeatherStation> findClosestWeatherStations(String queryParameters) {
		String weatherHistory = processNoaaRequest(queryParameters);
		if (weatherHistory.equals("") || !weatherHistory.contains("results"))
			return null;

		JSONObject jsonContent = new JSONObject(weatherHistory.toString());

		String attributesContent = jsonContent.get("results").toString();

		// We store the stations order from closest to the start to farthest to the
		// start.
		// We loop thru each station and we look at the results.
		// If the 1st has few data but the 2nd one has more, we take the 2nd one.
		// If only 1 and no data, we don't take anything.
		//

		ObjectMapper mapper = new ObjectMapper();

		List<NoaaWeatherStation> stations = null;
		try {
			stations = mapper.readValue(attributesContent, new TypeReference<List<NoaaWeatherStation>>() {
			});

			for (NoaaWeatherStation current : stations) {
				LatLng station = new LatLng(Double.valueOf(current.getLatitude()),
						Double.valueOf(current.getLongitude()));

				double distance = LatLngTool.distance(station, searchAreaCenter, LengthUnit.METER);
				current.setDistanceFromStart(distance);

			}

			Collections.sort(stations);

		} catch (IOException e) {
			CgLog.error(
					"NoaaWeatherHistoryRetriever.findClosestStation : Error while searching for the closest weather station for the parameters "
							+ queryParameters + "\n" + e.getMessage());

		}
		// TODO DOUBLE CHECK THAT A REQUEST WILL ACTUALLY GIVE DATA< OTHERWISE< WE NEED
		// ANOTHER ONE

		return stations;
	}

	private List<NoaaWeatherData> findMostRelevantDailySummaries() {

		String findWeatherStation = "stations?extent=" + getExtent(searchAreaSouthWestCorner, searchAreaNorthEastCorner)
				+ "&datasetid=GHCND&limit=1000";

		int startDate2 = Integer.valueOf(startDate.getYear()) - 3;
		String startDate3 = String.valueOf(startDate2);

		findWeatherStation = findWeatherStation + "&startdate=" + startDate3 + "-01-01" + "&enddate="
				+ startDate.getYear() + "-12-31";
		List<NoaaWeatherStation> stations = findClosestWeatherStations(findWeatherStation);
		for (NoaaWeatherStation station : stations) {
			List<NoaaWeatherData> data = retrieveDailySummaries(station.getId());
// if station has acutally no data, we go to the next one, otherwise we return the current results.
			if (data.get(0).isDailySummaryValid()) {
				noaaSummariesWeatherStation = station;
				return data;
			}

		}
		return new ArrayList<NoaaWeatherData>();
	}

	private NoaaWeatherData findMostRelevantNormalsDaily() {

		String findWeatherStation = "stations?extent=" + getExtent(searchAreaSouthWestCorner, searchAreaNorthEastCorner)
				+ "&datasetid=GHCND&limit=1000";

		List<NoaaWeatherStation> stations = findClosestWeatherStations(findWeatherStation);
		for (NoaaWeatherStation station : stations) {
			NoaaWeatherData data = retrieveNormalsDaily(station.getId());
// if station has acutally no data, we go to the next one, otherwise we return the current results.
			if (data.isDailySummaryValid()) {
				noaaNormalsWeatherStation = station;
				return data;
			}
		}

		return new NoaaWeatherData();
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

	private NoaaWeatherData processDailyNormals(String dailyNormalsData) {

		JSONObject jsonContent = new JSONObject(dailyNormalsData.toString());
		String attributesContent = jsonContent.get("results").toString();

		NoaaWeatherData noaaDailyNormals = new NoaaWeatherData();
		// Function to set the date ?
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<NoaaResults> car = mapper.readValue(attributesContent, new TypeReference<List<NoaaResults>>() {
			});

			for (NoaaResults current : car) {
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

	public ArrayList<NoaaWeatherData> retrieveDailySummaries(String stationId) {

		ArrayList<NoaaWeatherData> pastDailySummaries = new ArrayList<NoaaWeatherData>();

		for (int pastYearNumber = 1; pastYearNumber < 4; ++pastYearNumber) {
			Instant time = Instant.ofEpochMilli(startDate.minusDays(pastYearNumber * 364).getMillis());

			String datePattern = "yyyy-MM-dd";
			String pastDate = time.toDateTime().toString(datePattern);

			String findWeatherStation = "data?datasetid=GHCND&stationid=" + stationId + "&startdate=" + pastDate
					+ "&enddate=" + pastDate;

			String dailyNormalsData = processNoaaRequest(findWeatherStation);
			NoaaWeatherData toto = new NoaaWeatherData();
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

	private NoaaWeatherData retrieveNormalsDaily(String stationId) {
		String findWeatherStation = "data?datasetid=NORMAL_DLY&datatypeid=DLY-TMIN-NORMAL&datatypeid=DLY-TMAX-NORMAL&datatypeid=DLY-TAVG-NORMAL"
				+ "&stationid=" + stationId + "&startdate=2010-" + startDate.toString("MM-dd") + "&enddate=2010-"
				+ startDate.toString("MM-dd");

		String sssss = processNoaaRequest(findWeatherStation);
		if (!sssss.contains("results"))
			return new NoaaWeatherData();

		return processDailyNormals(sssss);

	}

	private NoaaWeatherData retrieveNormalsMonthly() {
		if (noaaNormalsWeatherStation == null)
			return new NoaaWeatherData();

		String findWeatherStation = "data?datasetid=NORMAL_MLY&datatypeid=MLY-TMIN-NORMAL&datatypeid=MLY-TMAX-NORMAL&datatypeid=MLY-TAVG-NORMAL&"
				+ "" + "stationid=" + noaaNormalsWeatherStation.getId() + "&startdate="
				+ startDate.toString("2010-MM-01") + "&enddate=" + startDate.toString("2010-MM-01");

		String dailyNormalsData = processNoaaRequest(findWeatherStation);
		if (!dailyNormalsData.contains("results"))
			return new NoaaWeatherData();

		return processDailyNormals(dailyNormalsData);
	}

	public NoaaWeatherStation getNoaaNormalsWeatherStation() {
		return noaaNormalsWeatherStation;
	}

	public NoaaWeatherStation getNoaaSummariesWeatherStation() {
		return noaaSummariesWeatherStation;
	}

	public NoaaWeatherData getNormalsDaily() {
		return noaaNormalsDaily;
	}

	public NoaaWeatherData getNormalsMonthly() {
		return noaaNormalsMonthly;
	}

	public List<NoaaWeatherData> getPastDailySummaries() {
		return pastDailySummaries;
	}
}
