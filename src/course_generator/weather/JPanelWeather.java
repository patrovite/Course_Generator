/*
 * Course Generator
 * Copyright (C) 2016 Pierre Delore
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package course_generator.weather;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import course_generator.TrackData;
import course_generator.settings.CgSettings;
import course_generator.utils.CgConst;
import course_generator.utils.CgLog;
import course_generator.utils.Utils;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

public class JPanelWeather extends JFXPanel implements PropertyChangeListener {
	private static final long serialVersionUID = -7168142806619093218L;
	private ResourceBundle bundle;
	private CgSettings settings = null;
	private JToolBar toolBar;
	private JButton btWeatherDataSave;
	private JButton btWeatherRefresh;
	private JLabel lbInformation;
	private JLabel InformationWarning;
	private JLabel getNoaaTokenLink;
	private TrackData track = null;
	private WebView webView;
	private String weatherDataSheetContent;


	public JPanelWeather(CgSettings settings) {
		super();
		this.settings = settings;
		this.settings.addNoaaTokenChangeListener(this);
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle"); //$NON-NLS-1$
		initComponents();
	}


	private void initComponents() {
		setLayout(new java.awt.BorderLayout());

		// -- Statistic tool bar
		// ---------------------------------------------------
		createWeatherToolbar();
		UpdatePanel();
		add(toolBar, java.awt.BorderLayout.NORTH);

		final JFXPanel fxPanel = new JFXPanel();

		// Creation of scene and future interactions with JFXPanel
		// should take place on the JavaFX Application Thread
		Platform.runLater(() -> {
			webView = new WebView();
			fxPanel.setScene(new Scene(webView));
		});
		add(fxPanel, java.awt.BorderLayout.CENTER);
	}


	/**
	 * Create the weather toolbar
	 */
	private void createWeatherToolbar() {
		toolBar = new javax.swing.JToolBar();
		toolBar.setOrientation(javax.swing.SwingConstants.HORIZONTAL);
		toolBar.setFloatable(false);
		toolBar.setRollover(true);
		toolBar.setAlignmentX(Component.LEFT_ALIGNMENT);

		// -- Save
		// --------------------------------------------------------------
		btWeatherDataSave = new javax.swing.JButton();
		btWeatherDataSave.setIcon(Utils.getIcon(this, "save_html.png", settings.ToolbarIconSize)); //$NON-NLS-1$
		btWeatherDataSave.setToolTipText(bundle.getString("JPanelWeather.btWeatherDataSave.toolTipText")); //$NON-NLS-1$
		btWeatherDataSave.setEnabled(false);
		btWeatherDataSave.setFocusable(false);
		btWeatherDataSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SaveStat();
			}
		});
		toolBar.add(btWeatherDataSave);

		// -- Separator
		// ---------------------------------------------------------
		toolBar.add(new javax.swing.JToolBar.Separator());

		// -- Refresh
		// --------------------------------------------------------------
		btWeatherRefresh = new javax.swing.JButton();
		btWeatherRefresh.setIcon(Utils.getIcon(this, "download_weather.png", settings.ToolbarIconSize)); //$NON-NLS-1$
		btWeatherRefresh.setToolTipText(bundle.getString("JPanelWeather.btWeatherRefresh.toolTipText")); //$NON-NLS-1$
		btWeatherRefresh.setEnabled(false);
		btWeatherRefresh.setFocusable(false);
		btWeatherRefresh.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				refresh(track, true);
			}
		});
		toolBar.add(btWeatherRefresh);

		// -- Tab information
		// --------------------------------------------------------------
		InformationWarning = new JLabel(Utils.getIcon(this, "cancel.png", settings.ToolbarIconSize)); //$NON-NLS-1$
		InformationWarning.setVisible(false);
		InformationWarning.setFocusable(false);
		toolBar.add(InformationWarning);

		lbInformation = new JLabel(" " + bundle.getString("JPanelWeather.lbInformationMissingNoaaToken.Text")); //$NON-NLS-1$ //$NON-NLS-2$
		Font boldFont = new Font(lbInformation.getFont().getName(), Font.BOLD, lbInformation.getFont().getSize());
		lbInformation.setFont(boldFont);
		InformationWarning.setFocusable(false);
		lbInformation.setVisible(false);
		toolBar.add(lbInformation);

		getNoaaTokenLink = new JLabel(". " + bundle.getString("JPanelWeather.lbNOAARequestTokenLink.Text")); //$NON-NLS-1$ //$NON-NLS-2$
		getNoaaTokenLink.setForeground(Color.BLUE.darker());
		boldFont = new Font(getNoaaTokenLink.getFont().getName(), Font.BOLD, getNoaaTokenLink.getFont().getSize());
		getNoaaTokenLink.setFont(boldFont);
		getNoaaTokenLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		getNoaaTokenLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {

					Desktop.getDesktop().browse(new URI("https://www.ncdc.noaa.gov/cdo-web/token")); //$NON-NLS-1$

				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		getNoaaTokenLink.setVisible(false);
		toolBar.add(getNoaaTokenLink);

		refresh(null, false);
	}


	/**
	 * Updates the panel buttons states depending on the NOAA token validity
	 */
	private void UpdatePanel() {

		boolean isNoaaTokenValid = settings.isNoaaTokenValid();

		InformationWarning.setVisible(!isNoaaTokenValid);
		lbInformation.setVisible(!isNoaaTokenValid);
		getNoaaTokenLink.setVisible(!isNoaaTokenValid);

		btWeatherRefresh.setEnabled(isNoaaTokenValid && track != null);
	}


	/**
	 * Refreshes the weather data sheet.
	 * 
	 * @param track
	 *            The current track.
	 * @param retrieveOnlineData
	 *            True if we need to retrieve data from the weather provider,
	 *            otherwise, we retrieve it from the track.
	 */
	public void refresh(TrackData track, boolean retrieveOnlineData) {
		if (track == null || track.data.isEmpty()) {
			UpdatePanel();
			return;
		}

		this.track = track;

		UpdatePanel();

		HistoricalWeather previousWeatherData = null;
		if (retrieveOnlineData) {
			if (!Utils.isInternetReachable()) {
				lbInformation.setText(bundle.getString("JPanelWeather.lbInformationMissingInternetConnection.Text")); //$NON-NLS-1$
				lbInformation.setVisible(true);
				InformationWarning.setVisible(true);
				return;
			}

			previousWeatherData = new HistoricalWeather(settings);

			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			previousWeatherData.RetrieveWeatherData(track);
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		} else {
			// If exists, get the historical weather from the CGX course
			previousWeatherData = track.getHistoricalWeather();
		}

		if (previousWeatherData == null) {
			updateDataSheet(""); //$NON-NLS-1$
			btWeatherDataSave.setEnabled(false);
			return;
		}

		btWeatherDataSave.setEnabled(true);

		String newContent = PopulateWeatherDataSheet(previousWeatherData);

		updateDataSheet(newContent);

	}


	/**
	 * Refreshes the weather data sheet with the new content.
	 * 
	 * @param dataSheetContent
	 *            The new weather data content.
	 * 
	 */
	private void updateDataSheet(String dataSheetContent) {
		weatherDataSheetContent = dataSheetContent;
		Platform.runLater(() -> {
			webView.getEngine().loadContent(weatherDataSheetContent);
		});
	}


	/**
	 * Performs the necessary operations whenever the NOAA token value has changed.
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (!evt.getPropertyName().equals("NoaaTokenChanged")) //$NON-NLS-1$
			return;
		UpdatePanel();
	}


	/**
	 * Parses weather data and populates the weather data sheet.
	 * 
	 * @param previousWeatherData
	 *            The retrieved NOAA weather data.
	 * @return The filled HTML content of the weather data sheet.
	 */
	private String PopulateWeatherDataSheet(HistoricalWeather previousWeatherData) {

		StringBuilder sheetSkeleton = new StringBuilder();
		InputStream is = getClass().getResourceAsStream("weatherdatasheet.html"); //$NON-NLS-1$

		try {
			InputStreamReader isr = new InputStreamReader(is, "UTF-8"); //$NON-NLS-1$
			BufferedReader br = new BufferedReader(isr);

			String line;
			while ((line = br.readLine()) != null) {
				sheetSkeleton.append(line);
			}
			br.close();
			isr.close();
			is.close();
		} catch (IOException e) {
			CgLog.error("RefreshStat : Impossible to read the template file from resource"); //$NON-NLS-1$
			e.printStackTrace();
		}

		// EVENT SUMMARY titles
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@100", bundle.getString("JPanelWeather.EventSummary.Text")); //$NON-NLS-1$ //$NON-NLS-2$
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@101", bundle.getString("JPanelWeather.Date.Text")); //$NON-NLS-1$ //$NON-NLS-2$
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@102", bundle.getString("JPanelWeather.SunriseSunset.Text")); //$NON-NLS-1$ //$NON-NLS-2$
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@103", bundle.getString("JPanelWeather.DaylightHours.Text")); //$NON-NLS-1$ //$NON-NLS-2$
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@104", bundle.getString("JPanelWeather.MoonPhase.Text")); //$NON-NLS-1$ //$NON-NLS-2$

		String datePattern = settings.Unit == CgConst.UNIT_MILES_FEET ? "EE MM/dd/yyyy" : "EE dd/MM/yyyy"; //$NON-NLS-1$ //$NON-NLS-2$

		// HISTORICAL WEATHER DATA titles
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@200", //$NON-NLS-1$
				bundle.getString("JPanelWeather.HistoricalWeatherData.Text")); //$NON-NLS-1$
		if (previousWeatherData.pastDailySummaries != null && !previousWeatherData.pastDailySummaries.isEmpty()) {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@201", //$NON-NLS-1$
					previousWeatherData.pastDailySummaries.get(0).getDate().toString(datePattern));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@202", //$NON-NLS-1$
					previousWeatherData.pastDailySummaries.get(1).getDate().toString(datePattern));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@203", //$NON-NLS-1$
					previousWeatherData.pastDailySummaries.get(2).getDate().toString(datePattern));
		} else {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@201", "No weather station found"); //$NON-NLS-1$ //$NON-NLS-2$
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@202", ""); //$NON-NLS-1$ //$NON-NLS-2$
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@203", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}

		if (previousWeatherData.normalsDaily != null) {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@204", bundle.getString("JPanelWeather.NormalsDaily.Text")); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@204", ""); //$NON-NLS-1$ //$NON-NLS-2$

		}
		if (previousWeatherData.normalsMonthly != null) {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@205", //$NON-NLS-1$
					bundle.getString("JPanelWeather.NormalsMonthly.Text")); //$NON-NLS-1$
		} else {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@205", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}

		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@206", bundle.getString("JPanelWeather.MaxTemperature.Text")); //$NON-NLS-1$ //$NON-NLS-2$
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@207", bundle.getString("JPanelWeather.AvgTemperature.Text")); //$NON-NLS-1$ //$NON-NLS-2$
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@208", bundle.getString("JPanelWeather.MinTemperature.Text")); //$NON-NLS-1$ //$NON-NLS-2$
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@209", bundle.getString("JPanelWeather.Precipitation.Text")); //$NON-NLS-1$ //$NON-NLS-2$
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@211", bundle.getString("JPanelWeather.StationName.Text")); //$NON-NLS-1$ //$NON-NLS-2$
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@212", //$NON-NLS-1$
				bundle.getString("JPanelWeather.DistanceFromStart.Text")); //$NON-NLS-1$

		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@110", //$NON-NLS-1$
				track.data.get(0).getHour().toString(datePattern + " HH:mm")); //$NON-NLS-1$
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@111", track.EndNightTime.toString("HH:mm")); //$NON-NLS-1$ //$NON-NLS-2$
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@112", track.StartNightTime.toString("HH:mm")); //$NON-NLS-1$ //$NON-NLS-2$

		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@113", previousWeatherData.daylightHours); //$NON-NLS-1$
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@114", previousWeatherData.getMoonPhaseDescription()); //$NON-NLS-1$

		if (previousWeatherData.pastDailySummaries != null && previousWeatherData.pastDailySummaries.get(0) != null) {
			// Year -1
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@220", //$NON-NLS-1$
					displayTemperature(previousWeatherData.pastDailySummaries.get(0).getTemperatureMax()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@225", //$NON-NLS-1$
					displayTemperature(previousWeatherData.pastDailySummaries.get(0).getTemperatureAverage()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@230", //$NON-NLS-1$
					displayTemperature(previousWeatherData.pastDailySummaries.get(0).getTemperatureMin()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@235", //$NON-NLS-1$
					previousWeatherData.pastDailySummaries.get(0).getPrecipitation());
		} else {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@220", ""); //$NON-NLS-1$ //$NON-NLS-2$
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@225", ""); //$NON-NLS-1$ //$NON-NLS-2$
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@230", ""); //$NON-NLS-1$ //$NON-NLS-2$
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@235", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (previousWeatherData.pastDailySummaries != null && previousWeatherData.pastDailySummaries.get(1) != null) {
			// Year -2
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@221", //$NON-NLS-1$
					displayTemperature(previousWeatherData.pastDailySummaries.get(1).getTemperatureMax()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@226", //$NON-NLS-1$
					displayTemperature(previousWeatherData.pastDailySummaries.get(1).getTemperatureAverage()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@231", //$NON-NLS-1$
					displayTemperature(previousWeatherData.pastDailySummaries.get(1).getTemperatureMin()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@236", //$NON-NLS-1$
					previousWeatherData.pastDailySummaries.get(1).getPrecipitation());
		} else {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@221", ""); //$NON-NLS-1$ //$NON-NLS-2$
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@226", ""); //$NON-NLS-1$ //$NON-NLS-2$
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@231", ""); //$NON-NLS-1$ //$NON-NLS-2$
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@236", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (previousWeatherData.pastDailySummaries != null && previousWeatherData.pastDailySummaries.get(2) != null) {
			// Year -3
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@222", //$NON-NLS-1$
					displayTemperature(previousWeatherData.pastDailySummaries.get(2).getTemperatureMax()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@227", //$NON-NLS-1$
					displayTemperature(previousWeatherData.pastDailySummaries.get(2).getTemperatureAverage()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@232", //$NON-NLS-1$
					displayTemperature(previousWeatherData.pastDailySummaries.get(2).getTemperatureMin()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@237", //$NON-NLS-1$
					previousWeatherData.pastDailySummaries.get(2).getPrecipitation());
		} else {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@222", ""); //$NON-NLS-1$ //$NON-NLS-2$
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@227", ""); //$NON-NLS-1$ //$NON-NLS-2$
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@232", ""); //$NON-NLS-1$ //$NON-NLS-2$
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@237", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// Daily normals
		if (previousWeatherData.normalsDaily != null) {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@223", //$NON-NLS-1$
					displayTemperature(previousWeatherData.normalsDaily.getTemperatureMax()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@228", //$NON-NLS-1$
					displayTemperature(previousWeatherData.normalsDaily.getTemperatureAverage()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@233", //$NON-NLS-1$
					displayTemperature(previousWeatherData.normalsDaily.getTemperatureMin()));
		} else {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@223", ""); //$NON-NLS-1$ //$NON-NLS-2$
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@228", ""); //$NON-NLS-1$ //$NON-NLS-2$
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@233", ""); //$NON-NLS-1$ //$NON-NLS-2$
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@238", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// Monthly normals
		if (previousWeatherData.normalsMonthly != null) {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@224", //$NON-NLS-1$
					displayTemperature(previousWeatherData.normalsMonthly.getTemperatureMax()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@229", //$NON-NLS-1$
					displayTemperature(previousWeatherData.normalsMonthly.getTemperatureAverage()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@234", //$NON-NLS-1$
					displayTemperature(previousWeatherData.normalsMonthly.getTemperatureMin()));
		} else {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@224", ""); //$NON-NLS-1$ //$NON-NLS-2$
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@229", ""); //$NON-NLS-1$ //$NON-NLS-2$
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@234", ""); //$NON-NLS-1$ //$NON-NLS-2$
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@239", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}

		if (previousWeatherData.noaaSummariesWeatherStation != null) {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@244", //$NON-NLS-1$
					previousWeatherData.noaaSummariesWeatherStation.getName());

			double distanceFromStart = previousWeatherData.noaaSummariesWeatherStation.getDistanceFromStart();
			String distance = ""; //$NON-NLS-1$
			if (settings.Unit == CgConst.UNIT_MILES_FEET)
				distanceFromStart = Utils.Km2Miles(distanceFromStart);

			distance = String.format("%.0f", distanceFromStart); //$NON-NLS-1$
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@246", distance + " " + Utils.uLDist2String(settings.Unit)); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@244", ""); //$NON-NLS-1$ //$NON-NLS-2$
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@246", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (previousWeatherData.noaaNormalsWeatherStation != null) {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@245", //$NON-NLS-1$
					previousWeatherData.noaaNormalsWeatherStation.getName());

			double distanceFromStart = previousWeatherData.noaaNormalsWeatherStation.getDistanceFromStart();
			String distance = ""; //$NON-NLS-1$
			if (settings.Unit == CgConst.UNIT_MILES_FEET)
				distanceFromStart = Utils.Meter2uMiles(distanceFromStart);

			distance = String.format("%.0f", distanceFromStart); //$NON-NLS-1$
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@247", distance + " " + Utils.uLDist2String(settings.Unit)); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@247", ""); //$NON-NLS-1$ //$NON-NLS-2$
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@245", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}

		return ReplaceImages(sheetSkeleton.toString(), previousWeatherData.moonFraction);
	}


	/**
	 * Save the statistics in HTML format
	 */
	private void SaveStat() {
		String s;
		s = Utils.SaveDialog(this, settings.LastDir, "", ".html", bundle.getString("frmMain.HTMLFile"), true, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				bundle.getString("frmMain.FileExist")); //$NON-NLS-1$

		if (!s.isEmpty()) {
			try {
				FileWriter out = new FileWriter(s);

				out.write(weatherDataSheetContent);
				out.close();
			} catch (Exception f) {
				CgLog.error("SaveStat : impossible to save the statistic file"); //$NON-NLS-1$
				f.printStackTrace();
			}
			// -- Store the directory
			settings.LastDir = Utils.GetDirFromFilename(s);
		}
	}


	/**
	 * Creates a String containing a temperature value.
	 * 
	 * @param temperatureValue
	 *            The temperature value.
	 * @return A String containing a temperature information.
	 */
	private String displayTemperature(String temperatureValue) {
		if (temperatureValue == null || temperatureValue.equals("")) //$NON-NLS-1$
			return ""; //$NON-NLS-1$

		return Utils.FormatTemperature(Double.valueOf(temperatureValue), settings.Unit)
				+ Utils.uTemperatureToString(settings.Unit);
	}


	/**
	 * Because the image paths in the original HTML reference images in the Course
	 * Generator jar (i.e: not accessible by any browser), we convert all the images
	 * references to their actual Base64 value.
	 * 
	 * @param originalText
	 *            The original HTML page
	 * @return The HTML page containing base64 representations of each image.
	 */
	private String ReplaceImages(String originalText, double moonFraction) {
		Document document = Jsoup.parse(originalText);

		document.select("img[src]").forEach(e -> { //$NON-NLS-1$

			String image = e.attr("src"); //$NON-NLS-1$
			String base64 = ""; //$NON-NLS-1$
			switch (image) {
			case "sunrise": //$NON-NLS-1$
				base64 = Utils.imageToBase64(this, "sunrise.png", 128); //$NON-NLS-1$
				break;
			case "sunset": //$NON-NLS-1$
				base64 = Utils.imageToBase64(this, "sunset.png", 128); //$NON-NLS-1$
				break;
			case "thermometer": //$NON-NLS-1$
				base64 = Utils.imageToBase64(this, "thermometer.png", 128); //$NON-NLS-1$
				break;
			case "moonphase": //$NON-NLS-1$
				String moonPhaseIcon = HistoricalWeather.getMoonPhaseIcon(moonFraction);
				base64 = Utils.imageToBase64(this, moonPhaseIcon, 128);
				break;
			}

			e.attr("src", "data:image/png;base64," + base64); //$NON-NLS-1$ //$NON-NLS-2$

		});

		return document.toString();
	}
}
