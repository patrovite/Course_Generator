package course_generator.weather;

import org.joda.time.DateTime;

import com.javadocmd.simplelatlng.LatLng;

final public class NoaaWeatherHistoryRetriever {

	private LatLng southWest;// The south west location of the desired geographical extent for search
	private LatLng northEast;// The north east location of the desired geographical extent for search
	private LatLng stationSearchStartingPoint;
	private DateTime startDate;


	private NoaaWeatherHistoryRetriever(long stationSearchStartingPointlat, long stationSearchStartingPointLon,
			double initialSearchDistance) {
		stationSearchStartingPoint = new LatLng(stationSearchStartingPointlat, stationSearchStartingPointLon);
		// TODO pass the distance to search a station from ?
	}


	private NoaaWeatherHistoryRetriever(DateTime startTime) {
		this.startDate = startTime;
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
	 * This method actually builds the WeatherData object
	 *
	 * @return the collected weather data.
	 */
	public static String retrieve() {
		String weatherHistory = new String();

		// TODO

		// LatLng m = LatLngTool.travel(l, 45, 1, LengthUnit.KILOMETER);

		// Loop and enlarge box if needed
		// Update southWest and northEast?

		return weatherHistory;
	}
}
