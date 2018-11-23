package course_generator.weather;

import org.joda.time.DateTime;

import course_generator.TrackData;

final public class NoaaWeatherDataRetriever {

	private TrackData trackData;
	private DateTime startDate;

	private String searchArea;// The desired geographical extent for search


	private NoaaWeatherDataRetriever(TrackData trackData) {
		this.trackData = trackData;
	}


	private NoaaWeatherDataRetriever(DateTime startTime) {
		this.startDate = startTime;
	}


	/**
	 *
	 *
	 * @param track
	 * 
	 * @return
	 */
	public static NoaaWeatherDataRetriever where(TrackData track) {
		// TODO Pass only the start and furthest point ?
		// If not, the where() is useless as we can get the first point time from the
		// track
		return new NoaaWeatherDataRetriever(track);
	}


	/**
	 *
	 *
	 * @param track
	 * 
	 * @return
	 */
	public static NoaaWeatherDataRetriever when(DateTime dateTime) {
		return new NoaaWeatherDataRetriever(dateTime);
	}


	/**
	 * This method actually builds the WeatherData object
	 *
	 * @return the collected weather data.
	 */
	public WeatherData build() {
		WeatherData weatherData = new WeatherData();

		// TODO
		// searchArea = CreateBox()

		// Loop and enlarge box if needed
		// Update searchArea ?

		// PopulateData() as a new WeatherData();

		return weatherData;
	}
}
