package course_generator.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.joda.time.DateTime;
import org.joda.time.Instant;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
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

	private DateTime requestStartDate;
	private LatLng startPoint;

	private LatLng searchAreaSouthWestCorner;// The south west location of the desired geographical extent for search
	private LatLng searchAreaNorthEastCorner;// The north east location of the desired geographical extent for search

	private LatLng searchAreaCenter;
	private double searchAreaRadius;
	private final double maxSearchAreaRadius = 100000.0; // 100km

	private NoaaWeatherStation noaaSummariesWeatherStation;
	private List<NoaaWeatherData> pastDailySummaries;
	private NoaaWeatherStation noaaNormalsWeatherStation;
	private NoaaWeatherData noaaNormalsDaily;
	private NoaaWeatherData noaaNormalsMonthly;

	public static String NoaaBaseUrl = "https://www.ncei.noaa.gov/access/services/"; //$NON-NLS-1$
	private final String ghcndDatSetId = "&dataset=daily-summaries"; //$NON-NLS-1$
	private final String ghcndDataTypeIds = "&dataTypes=TMAX,TMIN,PRCP"; //$NON-NLS-1$
	private final String normalDlyDataSetId = "&dataset=normals-annualseasonal"; //$NON-NLS-1$
	private final String normalDlyDataTypeIds = "&datatypeid=DLY-TMIN-NORMAL,DLY-TMAX-NORMAL,DLY-TAVG-NORMAL"; //$NON-NLS-1$
	private final String normalMlyDataTypeIds = "&datatypeid=MLY-TMIN-NORMAL,MLY-TMAX-NORMAL,MLY-TAVG-NORMAL"; //$NON-NLS-1$
	private final String startDate = "&startDate=";
	private final String endDate = "&endDate=";
	private final String stations = "&stations=";
	private final String searchWeatherStationQueryBase = "search/v1/data?boundingBox=";
	private final String dataAccess = "data/v1?format=json";


	private NoaaHistoricalWeatherRetriever(LatLng startPoint, LatLng searchAreaCenter, double searchAreaRadius) {
		this.searchAreaCenter = searchAreaCenter;
		this.startPoint = startPoint;

		// We want a search area of minimum 100km
		this.searchAreaRadius = searchAreaRadius > maxSearchAreaRadius ? searchAreaRadius : maxSearchAreaRadius;

	}


	/**
	 * Assigns the weather search area center and radius to a new object.
	 * 
	 * @param searchAreaCenter
	 *            The weather search circle center.
	 * @param searchAreaRadius
	 *            The weather search circle radius.
	 * @return A new object.
	 */
	public static NoaaHistoricalWeatherRetriever where(LatLng startPoint, LatLng searchAreaCenter,
			double searchAreaRadius) {
		return new NoaaHistoricalWeatherRetriever(startPoint, searchAreaCenter, searchAreaRadius);
	}


	/**
	 * 
	 * @param dateTime
	 * @return
	 */
	public NoaaHistoricalWeatherRetriever when(DateTime dateTime) {
		this.requestStartDate = dateTime;
		return this;
	}


	/**
	 * This method builds a NoaaHistoricalWeatherRetriever object and retrieves, if
	 * found, the weather data.
	 *
	 * @return the collected weather data.
	 */
	public NoaaHistoricalWeatherRetriever retrieve() {

		computeSearchArea();

		pastDailySummaries = findMostRelevantDailySummaries();

		noaaNormalsDaily = findMostRelevantNormalsDaily();
		noaaNormalsMonthly = retrieveNormalsMonthly();

		return this;
	}


	/**
	 * Creates an extent compliant to the NOAA extent definition bases on the south
	 * west and north east corners.
	 * 
	 * Official definition : Optional. The desired geographical extent for search.
	 * Designed to take a parameter generated by Google Maps API V3
	 * LatLngBounds.toUrlValue. Stations returned must be located within the extent
	 * specified.
	 * 
	 * @param swPoint
	 *            The GPS coordinates of the south west corner.
	 * @param nePoint
	 *            The GPS coordinates of the north east corner.
	 * @return The NOAA compliant extent as a string.
	 */
	private String getExtent(LatLng swPoint, LatLng nePoint) {
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.ROOT);
		nf.setGroupingUsed(false);
		nf.setMaximumFractionDigits(4);
		return nf.format(swPoint.getLatitude()) + "," + nf.format(swPoint.getLongitude()) + "," //$NON-NLS-1$ //$NON-NLS-2$
				+ nf.format(nePoint.getLatitude()) + "," + nf.format(nePoint.getLongitude()); //$NON-NLS-1$ //$NON-NLS-2$
																								// //$NON-NLS-3$
	}


	/**
	 * Computes the south west and north east corners of the box
	 */
	private void computeSearchArea() {
		searchAreaSouthWestCorner = LatLngTool.travel(searchAreaCenter, 225, searchAreaRadius, LengthUnit.METER);
		searchAreaNorthEastCorner = LatLngTool.travel(searchAreaCenter, 45, searchAreaRadius, LengthUnit.METER);
	}


	/**
	 * Processes a query against the NOAA API.
	 * 
	 * @param parameters
	 *            The parameters to specify within the query.
	 * @return The result of the NOAA query.
	 */
	private String processNoaaRequest(String parameters) {
		StringBuffer weatherHistory = new StringBuffer();
		try {
			HttpClientBuilder clientBuilder = HttpClientBuilder.create();
			clientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(3, true));
			HttpClient client = clientBuilder.build();
			HttpGet request = new HttpGet(NoaaBaseUrl + parameters + "&units=metric&limit=1000"); //$NON-NLS-1$

			HttpResponse response = client.execute(request);

			if (response.getStatusLine().getStatusCode() != 200)
				CgLog.error("NoaaWeatherHistoryRetriever.processNoaaRequest : Error code '" //$NON-NLS-1$
						+ response.getStatusLine().getStatusCode()
						+ "' while executing the NOAA request with the parameters " + parameters + ":\n" //$NON-NLS-2$
						+ response.getStatusLine().getReasonPhrase());

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			String line = ""; //$NON-NLS-1$
			while ((line = rd.readLine()) != null) {
				weatherHistory.append(line);
			}
		} catch (Exception ex) {
			CgLog.error(
					"NoaaWeatherHistoryRetriever.processNoaaRequest : Error while executing the NOAA request with the parameters " //$NON-NLS-1$
							+ parameters + "\n" + ex.getMessage()); //$NON-NLS-1$
		}
		return weatherHistory.toString();
	}


	/**
	 * Retrieves all the available weather stations that meet certain criteria.
	 * 
	 * @param queryParameters
	 *            The parameters for the weather station search.
	 * 
	 * @return If any were found, a list containing all the weather stations sorted
	 *         ascending order by the distance from the start.
	 */
	private List<NoaaWeatherStation> findClosestWeatherStations(String queryParameters) {
		String weatherStationsQueryResults = processNoaaRequest(queryParameters);
		if (weatherStationsQueryResults.equals("") || !weatherStationsQueryResults.contains("results")) //$NON-NLS-1$ //$NON-NLS-2$
			return null;

		List<NoaaWeatherStation> stations = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			String weatherStationsResults = mapper.readValue(weatherStationsQueryResults, JsonNode.class).get("results") //$NON-NLS-1$
					.toString();

			stations = mapper.readValue(weatherStationsResults, new TypeReference<List<NoaaWeatherStation>>() {
			});

			for (NoaaWeatherStation current : stations) {
				LatLng station = new LatLng(Double.valueOf(current.getLatitude()),
						Double.valueOf(current.getLongitude()));

				double distanceFromStart = LatLngTool.distance(station, startPoint, LengthUnit.KILOMETER);
				double distanceFromSearchAreaCenter = LatLngTool.distance(station, searchAreaCenter,
						LengthUnit.KILOMETER);

				// Converting the distance and only keeping 1 decimal.
				distanceFromStart = distanceFromStart * 10;
				distanceFromStart = (double) ((int) distanceFromStart);
				distanceFromStart = distanceFromStart / 10;
				current.setDistanceFromStart(distanceFromStart);

				// Converting the distance and only keeping 1 decimal.
				distanceFromSearchAreaCenter = distanceFromSearchAreaCenter * 10;
				distanceFromSearchAreaCenter = (double) ((int) distanceFromSearchAreaCenter);
				distanceFromSearchAreaCenter = distanceFromSearchAreaCenter / 10;
				current.setDistanceFromSearchAreaCenter(distanceFromSearchAreaCenter);

			}

			// We sort the stations from closest to the start to farthest to the
			// start.
			Collections.sort(stations);

		} catch (IOException e) {
			CgLog.error(
					"NoaaWeatherHistoryRetriever.findClosestStation : Error while searching for the closest weather station for the parameters " //$NON-NLS-1$
							+ queryParameters + "\n" + e.getMessage()); //$NON-NLS-1$

		}

		return stations;
	}


	/**
	 * Attempts to retrieve the most complete and closest daily summaries weather
	 * data (GHCND) for the last 3 years. The data consists of : Temperature Max,
	 * Temperature Min, Precipitation
	 * 
	 * @return If any were found, the last 3 years of daily summaries weather.
	 */
	private List<NoaaWeatherData> findMostRelevantDailySummaries() {

		// Looking for the 3 previous years of weather data
		String threeYearsAgo = String.valueOf(Integer.valueOf(requestStartDate.getYear()) - 3);
		String queryParameters = searchWeatherStationQueryBase
				+ getExtent(searchAreaSouthWestCorner, searchAreaNorthEastCorner) + ghcndDatSetId + startDate
				+ threeYearsAgo + "-01-01" + endDate + requestStartDate.getYear() //$NON-NLS-1$ //$NON-NLS-2$
				+ "-12-31"; //$NON-NLS-1$

		List<NoaaWeatherStation> stations = findClosestWeatherStations(queryParameters);
		if (stations == null)
			return null;

		for (NoaaWeatherStation station : stations) {
			List<NoaaWeatherData> data = retrieveDailySummaries(station.getId());
			if (data == null)
				continue;

			// if the current station has no valid data, we go to the next one,
			// otherwise we return the current results.
			if (data.get(0).isDailySummaryValid()) {
				noaaSummariesWeatherStation = station;
				return data;
			}

		}
		return null;
	}


	/**
	 * Retrieves the daily summaries for a given weather station.
	 * 
	 * @param stationId
	 *            The Id of a given weather station.
	 * @return The weather data, if found.
	 */
	private ArrayList<NoaaWeatherData> retrieveDailySummaries(String stationId) {

		ArrayList<NoaaWeatherData> pastDailySummaries = new ArrayList<NoaaWeatherData>();

		for (int pastYearNumber = 1; pastYearNumber < 4; ++pastYearNumber) {
			Instant time = Instant.ofEpochMilli(requestStartDate.minusDays(pastYearNumber * 364).getMillis());
			String pastDate = time.toDateTime().toString("yyyy-MM-dd"); //$NON-NLS-1$

			String queryParameters = dataAccess + stations + stationId + ghcndDatSetId + ghcndDataTypeIds + startDate // $NON-NLS-1$
					+ pastDate + endDate + pastDate;

			String dailyNormalsData = processNoaaRequest(queryParameters);
			if (!dailyNormalsData.contains("DATE")) //$NON-NLS-1$
				return null;
			else {
				NoaaWeatherData dailyNormals = parseWeatherData(dailyNormalsData);
				pastDailySummaries.add(dailyNormals);
			}
		}

		return pastDailySummaries;
	}


	/**
	 * Attempts to retrieve the most complete and closest normals daily weather
	 * data. The data consists of : Temperature Max, Temperature Min, Temperature
	 * Average.
	 * 
	 * @return If any were found, the weather daily normals.
	 */
	private NoaaWeatherData findMostRelevantNormalsDaily() {

		String queryParameters = searchWeatherStationQueryBase
				+ getExtent(searchAreaSouthWestCorner, searchAreaNorthEastCorner) // $NON-NLS-1$
				+ normalDlyDataSetId;

		List<NoaaWeatherStation> stations = findClosestWeatherStations(queryParameters);
		if (stations == null)
			return null;

		for (NoaaWeatherStation station : stations) {
			NoaaWeatherData data = retrieveNormalsDaily(station.getId());

			// if the current station has no valid data, we go to the next one,
			// otherwise we return the current results.
			if (data != null && data.isNormalsDataValid()) {
				noaaNormalsWeatherStation = station;
				return data;
			}
		}

		return null;
	}


	/**
	 * Retrieves the normals daily for a given weather station.
	 * 
	 * @param stationId
	 *            The Id of a given weather station.
	 * @return The weather data, if found.
	 */
	private NoaaWeatherData retrieveNormalsDaily(String stationId) {
		String queryParameters = dataAccess + stations + stationId + startDate + "2010-" //$NON-NLS-1$ //$NON-NLS-2$
				+ requestStartDate.toString("MM-dd") //$NON-NLS-1$
				+ endDate + "2010-" + requestStartDate.toString("MM-dd") + normalDlyDataSetId + normalDlyDataTypeIds; //$NON-NLS-1$ //$NON-NLS-2$

		String normalsDailyData = processNoaaRequest(queryParameters);
		if (!normalsDailyData.contains("results")) //$NON-NLS-1$
			return null;

		return parseWeatherData(normalsDailyData);
	}


	/**
	 * Retrieve the normals monthly for a given weather station.
	 * 
	 * @return The weather data, if found.
	 */
	private NoaaWeatherData retrieveNormalsMonthly() {
		if (noaaNormalsWeatherStation == null)
			return null;

		String findWeatherStation = "data?stationid=" + noaaNormalsWeatherStation.getId() + normalDlyDataSetId //$NON-NLS-1$
				+ normalMlyDataTypeIds + startDate + requestStartDate.toString("2010-MM-01") + endDate //$NON-NLS-1$ //$NON-NLS-2$
																										// //$NON-NLS-3$
				+ requestStartDate.toString("2010-MM-01"); //$NON-NLS-1$

		String normalsMonthlyData = processNoaaRequest(findWeatherStation);
		if (!normalsMonthlyData.contains("results")) //$NON-NLS-1$
			return null;

		return parseWeatherData(normalsMonthlyData);
	}


	/**
	 * Parses a JSON weather data object into a NoaaWeatherData object.
	 * 
	 * @param dailyNormalsData
	 *            A string containing a NOAA weather data JSON object.
	 * @return The parsed weather data.
	 */
	private NoaaWeatherData parseWeatherData(String weatherData) {

		NoaaWeatherData noaaWeatherData = new NoaaWeatherData();
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			List<NoaaResults> noaaObjects = mapper.readValue(weatherData, new TypeReference<List<NoaaResults>>() {
			});

			for (NoaaResults currentObject : noaaObjects) {
				noaaWeatherData.setTemperatureMin(currentObject.getTmin());
				noaaWeatherData.setTemperatureMax(currentObject.getTmax());
				// case "DLY-TAVG-NORMAL": //$NON-NLS-1$
				// case "MLY-TAVG-NORMAL": //$NON-NLS-1$
				// noaaWeatherData.setTemperatureAverage(currentObject.getValue());
				// break;
				// case "PRCP": //$NON-NLS-1$
				// noaaWeatherData.setPrecipitation(currentObject.getValue());
				// break;
				noaaWeatherData.setDate(DateTime.parse(currentObject.getDate()));
			}

		} catch (

		IOException e) {
			CgLog.error(
					"NoaaWeatherHistoryRetriever.parseWeatherData : Error while parsing the NOAA weather JSON object :" //$NON-NLS-1$
							+ weatherData + "\n" + e.getMessage()); //$NON-NLS-1$
		}

		return noaaWeatherData;
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
