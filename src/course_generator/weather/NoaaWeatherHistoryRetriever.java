package course_generator.weather;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.joda.time.DateTime;

import com.javadocmd.simplelatlng.LatLng;

final public class NoaaWeatherHistoryRetriever {

	private LatLng southWest;// The south west location of the desired geographical extent for search
	private LatLng northEast;// The north east location of the desired geographical extent for search
	private LatLng stationSearchStartingPoint;
	private DateTime startDate;
	private String noaaToken;

	private NoaaWeatherHistoryRetriever(long stationSearchStartingPointlat, long stationSearchStartingPointLon,
			double initialSearchDistance) {
		stationSearchStartingPoint = new LatLng(stationSearchStartingPointlat, stationSearchStartingPointLon);
		// TODO pass the distance to search a station from ?
	}

	private NoaaWeatherHistoryRetriever(DateTime startTime) {
		this.startDate = startTime;
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
	public static NoaaWeatherHistoryRetriever where(long southWestPointLat, long southWestPointLon) {
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
	public static NoaaWeatherHistoryRetriever when(DateTime dateTime) {
		return new NoaaWeatherHistoryRetriever(dateTime);
	}

	/**
	 *
	 *
	 * @param track
	 * 
	 * @return
	 */
	public static NoaaWeatherHistoryRetriever forUser(String noaaToken) {
		return new NoaaWeatherHistoryRetriever(noaaToken);
	}

	/**
	 * This method actually builds the WeatherData object
	 *
	 * @return the collected weather data.
	 */
	public String retrieve() {
		StringBuffer weatherHistory = new StringBuffer();

		// TODO

		// Retrieve daily normals
		// Retrieve daily normals for the year before the event
		// Retrieve daily normals 2 years before the event
		// Retrieve daily normals 3 years before the event
		// Retrieve the monthly average
		try {
			String url = "https://www.ncdc.noaa.gov/cdo-web/api/v2/data?datasetid=GHCND&locationid=ZIP:28801&startdate=2010-05-01&enddate=2010-05-01";
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(url);

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
//TODO log error in the CG log
		}

		// LatLng m = LatLngTool.travel(l, 45, 1, LengthUnit.KILOMETER);

		// Loop and enlarge box if needed
		// Update southWest and northEast?

		return weatherHistory.toString();
	}
}
