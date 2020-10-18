package course_generator.weather;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.SwingWorker;

import org.shredzone.commons.suncalc.MoonIllumination;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import course_generator.CgData;
import course_generator.TrackData;
import course_generator.dialogs.ProgressDialog;
import course_generator.dialogs.ProgressDialogListener;
import course_generator.utils.CgLog;
import course_generator.utils.Utils;

/**
 * A class that generates and contains, for a given track, the historical
 * weather data.
 * 
 * @author Frederic Bard
 */
public class HistoricalWeather implements ProgressDialogListener {

	// Daily summaries
	private NoaaWeatherStation noaaSummariesWeatherStation;
	private List<NoaaWeatherData> pastDailySummaries;

	// Normals
	private NoaaWeatherStation noaaNormalsWeatherStation;
	private NoaaWeatherData normalsDaily;
	private NoaaWeatherData normalsMonthly;

	// Event data
	private String daylightHours;
	private int moonFraction;
	private double moonPhase;

	private TrackData Track;
	private LatLng searchAreaCenter;
	private double searchAreaRadius;

	SwingWorker<Integer, Integer> weatherRetrieverWorker;
	private PropertyChangeSupport WeatherDataRetrieved = new PropertyChangeSupport(this);

	public static final String MOONFRACTION = "MOONFRACTION"; //$NON-NLS-1$
	public static final String MOONPHASE = "MOONPHASE"; //$NON-NLS-1$
	public static final String DAYLIGHTHOURS = "DAYLIGHTHOURS"; //$NON-NLS-1$


	public HistoricalWeather() {
	}


	public HistoricalWeather(ArrayList<NoaaWeatherData> pastDailySummaries, NoaaWeatherData normalsDaily,
			NoaaWeatherData normalsMonthly, NoaaWeatherStation noaaSummariesWeatherStation,
			NoaaWeatherStation noaaNormalsWeatherStation, String daylightHours, int moonFraction,
			double moonPhase) {
		this.pastDailySummaries = pastDailySummaries;
		this.normalsDaily = normalsDaily;
		this.normalsMonthly = normalsMonthly;
		this.noaaSummariesWeatherStation = noaaSummariesWeatherStation;
		this.noaaNormalsWeatherStation = noaaNormalsWeatherStation;
		this.daylightHours = daylightHours;
		this.moonFraction = moonFraction;
		this.moonPhase = moonPhase;
	}


	/**
	 * Retrieves the ephemeris and the historical weather data for a given track.
	 * 
	 * @param track
	 *            The current track
	 */
	public void RetrieveWeatherData(TrackData track, ProgressDialog progressDialog) {
		if (track == null || track.data == null || track.data.isEmpty())
			return;

		progressDialog.setListener(this);
		weatherRetrieverWorker = new SwingWorker<Integer, Integer>() {

			@Override
			protected Integer doInBackground() throws Exception {
				Track = track;
				LatLng startPoint = new LatLng(track.data.get(0).getLatitude(), track.data.get(0).getLongitude());

				determineWeatherSearchArea();

				publish(25);

				long retrievalstartTime = System.currentTimeMillis();
				NoaaHistoricalWeatherRetriever weatherHistoryRetriever = NoaaHistoricalWeatherRetriever
						.where(startPoint, searchAreaCenter, searchAreaRadius).when(Track.StartTime).retrieve();
				CgLog.info("Weather retrieval time : " + (System.currentTimeMillis() - retrievalstartTime) + "ms"); //$NON-NLS-1$ //$NON-NLS-2$

				publish(75);

				noaaNormalsWeatherStation = weatherHistoryRetriever.getNoaaNormalsWeatherStation();
				noaaSummariesWeatherStation = weatherHistoryRetriever.getNoaaSummariesWeatherStation();
				normalsDaily = weatherHistoryRetriever.getNormalsDaily();
				pastDailySummaries = weatherHistoryRetriever.getPastDailySummaries();
				normalsMonthly = weatherHistoryRetriever.getNormalsMonthly();

				// The start time might have been changed, we recompute the sunrise and sunset
				// times.
				Track.determineSunriseSunsetTimes();

				daylightHours = Utils.computeDaylightHours(Track.EndNightTime, Track.StartNightTime);

				if (Track.timeZoneId.equals("")) { //$NON-NLS-1$
					// The time zone id, sunrise and sunset hours haven't been computed yet.
					// Let's do it.
					Track.determineTrackTimeZone();
				}

				MoonIllumination moonIllumination = MoonIllumination.compute().on(Track.StartTime.toDate())
						.timezone(Track.timeZoneId).execute();
				moonFraction = (int) (Math.round(moonIllumination.getFraction() * 100));
				moonPhase = moonIllumination.getPhase();
				publish(100);

				Thread.sleep(200);

				return null;
			}


			@Override
			protected void done() {
				progressDialog.setVisible(false);

				String weatherDataFinalStatus;
				if (isCancelled())
					weatherDataFinalStatus = "cancelled"; //$NON-NLS-1$
				else
					weatherDataFinalStatus = "done"; //$NON-NLS-1$

				WeatherDataRetrieved.firePropertyChange("WeatherDataRetrieved", "in progress", weatherDataFinalStatus); //$NON-NLS-1$ //$NON-NLS-2$
			}


			@Override
			protected void process(List<Integer> chunks) {
				progressDialog.setValue(chunks.get(chunks.size() - 1));
			}

		};
		weatherRetrieverWorker.execute();
	}


	/**
	 * Determines the geographic area covered by a GPS track. The goal is to
	 * encompass most of the track to search a weather station as close as possible
	 * to the overall course and not just to a specific point.
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

		double distanceFromStart = LatLngTool.distance(startPoint, furthestPoint, LengthUnit.METER);
		double bearingBetweenPoint = LatLngTool.initialBearing(startPoint, furthestPoint);

		// We find the center and the radius of the circle formed by the starting point
		// and
		// the farthest point
		searchAreaCenter = LatLngTool.travel(startPoint, bearingBetweenPoint, distanceFromStart / 2, LengthUnit.METER);
		searchAreaRadius = distanceFromStart / 2;

	}


	public NoaaWeatherStation getNoaaSummariesWeatherStation() {
		return noaaSummariesWeatherStation;
	}


	public List<NoaaWeatherData> getPastDailySummaries() {
		return pastDailySummaries;
	}


	public NoaaWeatherStation getNoaaNormalsWeatherStation() {
		return noaaNormalsWeatherStation;
	}


	public NoaaWeatherData getNormalsDaily() {
		return normalsDaily;
	}


	public NoaaWeatherData getNormalsMonthly() {
		return normalsMonthly;
	}


	public String getDaylightHours() {
		return daylightHours;
	}


	public int getMoonFraction() {
		return moonFraction;
	}
	
	public double getMoonPhase() {
		return moonPhase;
	}


	/**
	 * Gives the moon phase description for a given moon phase value.
	 * Interpretation table :
	 * https://www.ducksters.com/science/phases_of_the_moon.php
	 * 
	 * @return A String containing the moon phase description
	 */
	public String getMoonPhaseDescription() {
		
		String moonPhaseDescription = "";

		ResourceBundle bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle"); //$NON-NLS-1$

		if (moonPhase == -180 || moonPhase == 180) {
			moonPhaseDescription = bundle.getString("JPanelWeather.lbMoonPhaseNewMoon.Text"); //$NON-NLS-1$
		} else if (moonPhase > -180 && moonPhase < -90) {
			moonPhaseDescription = bundle.getString("JPanelWeather.lbMoonPhaseWaxingCrescent.Text"); //$NON-NLS-1$
		} else if (moonPhase == -90) {
			moonPhaseDescription = bundle.getString("JPanelWeather.lbMoonPhaseFirstQuarter.Text"); //$NON-NLS-1$
		} else if (moonPhase > -90 && moonPhase < 0) {
			moonPhaseDescription = bundle.getString("JPanelWeather.lbMoonPhaseWaxingGibbous.Text"); //$NON-NLS-1$
		} else if (moonPhase == 0) {
			moonPhaseDescription = bundle.getString("JPanelWeather.lbMoonPhaseFullMoon.Text"); //$NON-NLS-1$
		} else if (moonPhase > 0 && moonPhase < 90) {
			moonPhaseDescription = bundle.getString("JPanelWeather.lbMoonPhaseWaningGibbous.Text"); //$NON-NLS-1$
		} else if (moonPhase == 90) {
			moonPhaseDescription = bundle.getString("JPanelWeather.lbMoonPhaseLastQuarter.Text"); //$NON-NLS-1$
		} else if (moonPhase > 90 && moonPhase < 180) {
			moonPhaseDescription = bundle.getString("JPanelWeather.lbMoonPhaseWaningCrescent.Text"); //$NON-NLS-1$
		}
		return moonPhaseDescription;
	}


	/**
	 * Gives the moon phase icon for a given moon fraction value. Interpretation
	 * table : https://www.ducksters.com/science/phases_of_the_moon.php
	 * 
	 * @return A String containing the name of the appropriate moon phase icon
	 */
	public static String getMoonPhaseIcon(double moonPhase) {
		String moonPhaseIcon = "";

		if (moonPhase == -180 || moonPhase == 180) {
			moonPhaseIcon = "moon-new"; //$NON-NLS-1$
		} else if (moonPhase > -180 && moonPhase < -90) {
			moonPhaseIcon = "moon-waxing-crescent"; //$NON-NLS-1$
		} else if (moonPhase == -90) {
			moonPhaseIcon = "moon-first-quarter"; //$NON-NLS-1$
		} else if (moonPhase > -90 && moonPhase < 0) {
			moonPhaseIcon = "moon-waxing-gibbous"; //$NON-NLS-1$
		} else if (moonPhase == 0) {
			moonPhaseIcon = "moon-full"; //$NON-NLS-1$
		} else if (moonPhase > 0 && moonPhase < 90) {
			moonPhaseIcon = "moon-waning-gibbous"; //$NON-NLS-1$
		} else if (moonPhase == 90) {
			moonPhaseIcon = "moon-last-quarter"; //$NON-NLS-1$
		} else if (moonPhase > 90 && moonPhase < 180) {
			moonPhaseIcon = "moon-waning-crescent"; //$NON-NLS-1$
		}
		
		return moonPhaseIcon + ".png"; //$NON-NLS-1$
	}


	public void addWeatherDataRetrievedChangeListener(PropertyChangeListener listener) {
		WeatherDataRetrieved.addPropertyChangeListener(listener);
	}


	@Override
	public void progressDialogCancelled() {
		weatherRetrieverWorker.cancel(true);
	}
}
