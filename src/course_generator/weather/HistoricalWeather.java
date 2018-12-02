package course_generator.weather;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.shredzone.commons.suncalc.MoonIllumination;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import course_generator.CgData;
import course_generator.TrackData;
import course_generator.settings.CgSettings;
import course_generator.utils.Utils;

/**
 * A class that generates and contains, for a given track, the historical
 * weather data.
 * 
 * @author Frederic Bard
 */
public class HistoricalWeather {

	public List<NoaaWeatherData> pastDailySummaries;
	public NoaaWeatherData normalsDaily;
	public NoaaWeatherData normalsMonthly;
	public NoaaWeatherStation noaaSummariesWeatherStation;
	public NoaaWeatherStation noaaNormalsWeatherStation;
	public String daylightHours;
	public double moonFraction;
	private CgSettings Settings;
	private TrackData Track;
	private LatLng searchAreaCenter;
	private double searchAreaRadius;

	public static final String MOONFRACTION = "MOONFRACTION";
	public static final String DAYLIGHTHOURS = "DAYLIGHTHOURS";

	public HistoricalWeather(CgSettings settings) {
		Settings = settings;
	}

	public HistoricalWeather(ArrayList<NoaaWeatherData> pastDailySummaries, NoaaWeatherData normalsDaily,
			NoaaWeatherData normalsMonthly, NoaaWeatherStation noaaSummariesWeatherStation,
			NoaaWeatherStation noaaNormalsWeatherStation, String daylightHours, double moonFraction) {
		this.pastDailySummaries = pastDailySummaries;
		this.normalsDaily = normalsDaily;
		this.normalsMonthly = normalsMonthly;
		this.noaaSummariesWeatherStation = noaaSummariesWeatherStation;
		this.noaaNormalsWeatherStation = noaaNormalsWeatherStation;
		this.daylightHours = daylightHours;
		this.moonFraction = moonFraction;
	}

	/**
	 * Retrieves the ephemeris and the historical weather data for a given track.
	 * 
	 * @param track A track
	 */
	public void RetrieveWeatherData(TrackData track) {
		if (track == null)
			return;

		Track = track;
		DateTime startTime = Track.data.get(0).getHour();

		determineWeatherSearchArea();

		NoaaHistoricalWeatherRetriever weatherHistoryRetriever = NoaaHistoricalWeatherRetriever
				.where(searchAreaCenter, searchAreaRadius).when(startTime).forUser(Settings.getNoaaToken()).retrieve();

		noaaNormalsWeatherStation = weatherHistoryRetriever.getNoaaNormalsWeatherStation();
		noaaSummariesWeatherStation = weatherHistoryRetriever.getNoaaSummariesWeatherStation();
		normalsDaily = weatherHistoryRetriever.getNormalsDaily();
		pastDailySummaries = weatherHistoryRetriever.getPastDailySummaries();
		// normalsMonthly = weatherHistoryRetriever.retrieveMonthlyNormals();

		if (track.StartNightTime == TrackData.defaultSunriseSunsetTime) {
			// The sunrise and sunset values haven't been computed yet.
			// Let's do it.
			Track.determineSunriseSunsetTimes();
		}

		daylightHours = Utils.computeDaylightHours(Track.EndNightTime, Track.StartNightTime);

		if (Track.timeZoneId.equals("")) {
			// The time zone id, sunrise and sunset hours haven't been computed yet.
			// Let's do it.
			Track.determineTrackTimeZone();
		}

		MoonIllumination moonIllumination = MoonIllumination.compute().on(startTime.toDate()).timezone(Track.timeZoneId)
				.execute();
		moonFraction = moonIllumination.getFraction() * 100;
		moonFraction = (double) ((int) moonFraction);
		moonFraction = moonFraction / 100;

		Track.setHistoricalWeather(this);
	}

	/**
	 * is to encompass most of the track to search a weather station as close as
	 * possible to the course
	 */
	private void determineWeatherSearchArea() {
		// Looking for the farthest point of the track
		double maxDistance = Double.MIN_VALUE;
		LatLng furthestPoint = null;
		LatLng startPoint = new LatLng(Track.data.get(0).getLatitude(), Track.data.get(0).getLongitude());
		for (CgData dataPoint : Track.data) {
			LatLng currentPoint = new LatLng(dataPoint.getLatitude(), dataPoint.getLongitude());

			double distanceFromStart = LatLngTool.distance(startPoint, currentPoint, LengthUnit.METER);

			if (distanceFromStart > maxDistance) {
				maxDistance = distanceFromStart;
				furthestPoint = currentPoint;
			}
		}

		// We find the center of the box formed by the starting point and
		// the farthest point

		double distanceFromStart = LatLngTool.distance(startPoint, furthestPoint, LengthUnit.METER);
		double bearingBetweenPoint = LatLngTool.initialBearing(startPoint, furthestPoint);

		searchAreaCenter = LatLngTool.travel(startPoint, bearingBetweenPoint, distanceFromStart / 2, LengthUnit.METER);
		searchAreaRadius = distanceFromStart / 2;

	}

	/**
	 * Gives the moon phase description for a given moon fraction value.
	 * Interpretation table :
	 * https://github.com/mourner/suncalc/blob/master/README.md
	 * 
	 * @return A String containing the moon phase description
	 */
	public String getMoonPhaseDescription() {
		String moonPhaseDescription;

		if (moonFraction > Double.MIN_VALUE && moonFraction < 0.1) {
			moonPhaseDescription = "New Moon";
		} else if (moonFraction >= 0.1 && moonFraction < 0.25) {
			moonPhaseDescription = "Waxing Crescent";
		} else if (moonFraction == 0.25) {
			moonPhaseDescription = "First Quarter";
		} else if (moonFraction > 0.25 && moonFraction < 0.5) {
			moonPhaseDescription = "Waxing Gibbous";
		} else if (moonFraction == 0.5) {
			moonPhaseDescription = "Full Moon";
		} else if (moonFraction > 0.5 && moonFraction < 0.75) {
			moonPhaseDescription = "Waning Gibbous";
		} else if (moonFraction == 0.75) {
			moonPhaseDescription = "Last Quarter";
		} else {
			moonPhaseDescription = "Waning Crescent";
		}
		return moonPhaseDescription;
	}

	/**
	 * Gives the moon phase icon for a given moon fraction value. Interpretation
	 * table : https://github.com/mourner/suncalc/blob/master/README.md
	 * 
	 * @return A String containing the name of the appropriate moon phase icon
	 */
	public static String getMoonPhaseIcon(double moonFraction) {
		String moonPhaseIcon;

		if (moonFraction > Double.MIN_VALUE && moonFraction < 0.1) {
			moonPhaseIcon = "moon-new";
		} else if (moonFraction >= 0.1 && moonFraction < 0.25) {
			moonPhaseIcon = "moon-waxing-crescent";
		} else if (moonFraction == 0.25) {
			moonPhaseIcon = "moon-first-quarter";
		} else if (moonFraction > 0.25 && moonFraction < 0.5) {
			moonPhaseIcon = "moon-waxing-gibbous";
		} else if (moonFraction == 0.5) {
			moonPhaseIcon = "moon-full";
		} else if (moonFraction > 0.5 && moonFraction < 0.75) {
			moonPhaseIcon = "moon-waning-gibbous";
		} else if (moonFraction == 0.75) {
			moonPhaseIcon = "moon-last-quarter";
		} else {
			moonPhaseIcon = "moon-waning-crescent";
		}
		return moonPhaseIcon + ".png";
	}

}
