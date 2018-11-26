package course_generator.weather;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

final public class NoaaWeatherHistoryRetriever {

	private LatLng southWest;// The south west location of the desired geographical extent for search
	private LatLng northEast;// The north east location of the desired geographical extent for search
	private LatLng stationSearchStartingPoint;
	private DateTime startDate;
	private String noaaToken;


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
	public String retrieve() {
		StringBuffer weatherHistory = new StringBuffer();
		String datePattern = "yyyy-MM-dd";

		DateTimeFormatter fmt = DateTimeFormat.forPattern(datePattern);
		// TODO

		// Search for a weather station
		LatLng m = LatLngTool.travel(stationSearchStartingPoint, 45, 100, LengthUnit.KILOMETER);

		// TODO static string???
		String findWeatherStation = "https://www.ncdc.noaa.gov/cdo-web/api/v2/stations?extent=";

		findWeatherStation = findWeatherStation + String.format("%.3f", stationSearchStartingPoint.getLatitude()) + ","
				+ String.format("%.3f", stationSearchStartingPoint.getLongitude()) + ","
				+ String.format("%.3f", m.getLatitude()) + "," + String.format("%.3f", m.getLongitude());

		findWeatherStation = findWeatherStation + "&datasetid=GHCND";
		findWeatherStation = findWeatherStation + "&startdate=" + fmt.print(startDate);
		findWeatherStation = findWeatherStation + "&enddate=" + fmt.print(startDate);

		// Retrieve daily normals
		// Retrieve daily normals for the year before the event
		// Retrieve daily normals 2 years before the event
		// Retrieve daily normals 3 years before the event
		// Retrieve the monthly average
		try {
			// String url =
			// "https://www.ncdc.noaa.gov/cdo-web/api/v2/data?datasetid=GHCND&locationid=ZIP:28801&startdate=2010-05-01&enddate=2010-05-01";
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(findWeatherStation);

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

		// Loop and enlarge box if needed
		// Update southWest and northEast?

		return weatherHistory.toString();
	}
}
