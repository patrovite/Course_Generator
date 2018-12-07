
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
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import course_generator.TrackData;
import course_generator.settings.CgSettings;
import course_generator.utils.CgConst;
import course_generator.utils.CgLog;
import course_generator.utils.Utils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

/**
 * A class that generates a GUI panel for the weather data.
 * 
 * @author Frederic Bard
 */

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

	public static final String EVENT_TYPE_CLICK = "click"; //$NON-NLS-1$

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

			webView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
				public void changed(ObservableValue<? extends State> ov, State oldState, State newState) {
					if (newState == Worker.State.SUCCEEDED) {
						EventListener listener = new EventListener() {
							@Override
							public void handleEvent(Event ev) {
								String domEventType = ev.getType();
								if (domEventType.equals(EVENT_TYPE_CLICK)) {
									String href = ((Element) ev.getTarget()).getAttribute("href"); //$NON-NLS-1$
									if (Desktop.isDesktopSupported()) {
										new Thread(() -> {
											try {
												Desktop.getDesktop().browse(new URI(href));
											} catch (IOException | URISyntaxException e1) {
												e1.printStackTrace();
											}
										}).start();
									}
									ev.preventDefault();
								}
							}
						};

						org.w3c.dom.Document doc = webView.getEngine().getDocument();
						NodeList nodeList = doc.getElementsByTagName("a"); //$NON-NLS-1$
						for (int i = 0; i < nodeList.getLength(); i++) {
							((EventTarget) nodeList.item(i)).addEventListener(EVENT_TYPE_CLICK, listener, false);

						}
					}
				}
			});

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
	 * @param track              The current track.
	 * @param retrieveOnlineData True if we need to retrieve data from the weather
	 *                           provider, otherwise, we retrieve it from the track.
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
	 * @param dataSheetContent The new weather data content.
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
	 * @param previousWeatherData The retrieved NOAA weather data.
	 * @return The filled HTML content of the weather data sheet.
	 */
	private String PopulateWeatherDataSheet(HistoricalWeather previousWeatherData) {

		StringBuilder weatherDataSheetBuilder = new StringBuilder();
		InputStream is = getClass().getResourceAsStream("weatherdatasheet.html"); //$NON-NLS-1$

		try {
			InputStreamReader isr = new InputStreamReader(is, "UTF-8"); //$NON-NLS-1$
			BufferedReader br = new BufferedReader(isr);

			String line;
			while ((line = br.readLine()) != null) {
				weatherDataSheetBuilder.append(line);
			}
			br.close();
			isr.close();
			is.close();
		} catch (IOException e) {
			CgLog.error("RefreshStat : Impossible to read the template file from resource"); //$NON-NLS-1$
			e.printStackTrace();
		}

		// EVENT SUMMARY titles
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@100", //$NON-NLS-1$
				bundle.getString("JPanelWeather.EventSummary.Text")); //$NON-NLS-1$
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@101", //$NON-NLS-1$
				bundle.getString("JPanelWeather.Date.Text")); //$NON-NLS-1$
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@102", //$NON-NLS-1$
				bundle.getString("JPanelWeather.SunriseSunset.Text")); //$NON-NLS-1$
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@103", //$NON-NLS-1$
				bundle.getString("JPanelWeather.DaylightHours.Text")); //$NON-NLS-1$
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@104", //$NON-NLS-1$
				bundle.getString("JPanelWeather.MoonPhase.Text")); //$NON-NLS-1$

		String datePattern = settings.Unit == CgConst.UNIT_MILES_FEET ? "EE MM/dd/yyyy" : "EE dd/MM/yyyy"; //$NON-NLS-1$ //$NON-NLS-2$

		// HISTORICAL WEATHER DATA titles
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@200", //$NON-NLS-1$
				bundle.getString("JPanelWeather.HistoricalWeatherData.Text")); //$NON-NLS-1$
		if (previousWeatherData.pastDailySummaries != null && !previousWeatherData.pastDailySummaries.isEmpty()) {
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@201", //$NON-NLS-1$
					previousWeatherData.pastDailySummaries.get(0).getDate().toString(datePattern));
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@202", //$NON-NLS-1$
					previousWeatherData.pastDailySummaries.get(1).getDate().toString(datePattern));
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@203", //$NON-NLS-1$
					previousWeatherData.pastDailySummaries.get(2).getDate().toString(datePattern));
		} else {
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@201", "No weather station found"); //$NON-NLS-1$ //$NON-NLS-2$
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@202", ""); //$NON-NLS-1$ //$NON-NLS-2$
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@203", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}

		if (previousWeatherData.normalsDaily != null) {
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@204", //$NON-NLS-1$
					bundle.getString("JPanelWeather.NormalsDaily.Text")); //$NON-NLS-1$
		} else {
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@204", ""); //$NON-NLS-1$ //$NON-NLS-2$

		}
		if (previousWeatherData.normalsMonthly != null) {
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@205", //$NON-NLS-1$
					bundle.getString("JPanelWeather.NormalsMonthly.Text")); //$NON-NLS-1$
		} else {
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@205", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}

		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@206", //$NON-NLS-1$
				bundle.getString("JPanelWeather.MaxTemperature.Text")); //$NON-NLS-1$
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@207", //$NON-NLS-1$
				bundle.getString("JPanelWeather.AvgTemperature.Text")); //$NON-NLS-1$
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@208", //$NON-NLS-1$
				bundle.getString("JPanelWeather.MinTemperature.Text")); //$NON-NLS-1$
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@209", //$NON-NLS-1$
				bundle.getString("JPanelWeather.Precipitation.Text")); //$NON-NLS-1$
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@211", //$NON-NLS-1$
				bundle.getString("JPanelWeather.StationName.Text")); //$NON-NLS-1$
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@212", //$NON-NLS-1$
				bundle.getString("JPanelWeather.DistanceFromStart.Text")); //$NON-NLS-1$

		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@110", //$NON-NLS-1$
				track.StartTime.toString(datePattern));
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@111", //$NON-NLS-1$
				track.EndNightTime.toString("HH:mm")); //$NON-NLS-1$
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@112", //$NON-NLS-1$
				track.StartNightTime.toString("HH:mm")); //$NON-NLS-1$

		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@113", previousWeatherData.daylightHours); //$NON-NLS-1$
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@114", //$NON-NLS-1$
				previousWeatherData.getMoonPhaseDescription());

		if (previousWeatherData.pastDailySummaries != null && !previousWeatherData.pastDailySummaries.isEmpty()
				&& previousWeatherData.pastDailySummaries.get(0) != null) {
			// Year -1
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@220", //$NON-NLS-1$
					displayTemperature(previousWeatherData.pastDailySummaries.get(0).getTemperatureMax()));
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@225", //$NON-NLS-1$
					displayTemperature(previousWeatherData.pastDailySummaries.get(0).getTemperatureAverage()));
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@230", //$NON-NLS-1$
					displayTemperature(previousWeatherData.pastDailySummaries.get(0).getTemperatureMin()));
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@235", //$NON-NLS-1$
					previousWeatherData.pastDailySummaries.get(0).getPrecipitation());
		} else {
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@220", ""); //$NON-NLS-1$ //$NON-NLS-2$
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@225", ""); //$NON-NLS-1$ //$NON-NLS-2$
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@230", ""); //$NON-NLS-1$ //$NON-NLS-2$
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@235", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (previousWeatherData.pastDailySummaries != null && !previousWeatherData.pastDailySummaries.isEmpty()
				&& previousWeatherData.pastDailySummaries.get(1) != null) {
			// Year -2
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@221", //$NON-NLS-1$
					displayTemperature(previousWeatherData.pastDailySummaries.get(1).getTemperatureMax()));
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@226", //$NON-NLS-1$
					displayTemperature(previousWeatherData.pastDailySummaries.get(1).getTemperatureAverage()));
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@231", //$NON-NLS-1$
					displayTemperature(previousWeatherData.pastDailySummaries.get(1).getTemperatureMin()));
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@236", //$NON-NLS-1$
					previousWeatherData.pastDailySummaries.get(1).getPrecipitation());
		} else {
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@221", ""); //$NON-NLS-1$ //$NON-NLS-2$
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@226", ""); //$NON-NLS-1$ //$NON-NLS-2$
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@231", ""); //$NON-NLS-1$ //$NON-NLS-2$
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@236", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (previousWeatherData.pastDailySummaries != null && !previousWeatherData.pastDailySummaries.isEmpty()
				&& previousWeatherData.pastDailySummaries.get(2) != null) {
			// Year -3
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@222", //$NON-NLS-1$
					displayTemperature(previousWeatherData.pastDailySummaries.get(2).getTemperatureMax()));
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@227", //$NON-NLS-1$
					displayTemperature(previousWeatherData.pastDailySummaries.get(2).getTemperatureAverage()));
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@232", //$NON-NLS-1$
					displayTemperature(previousWeatherData.pastDailySummaries.get(2).getTemperatureMin()));
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@237", //$NON-NLS-1$
					previousWeatherData.pastDailySummaries.get(2).getPrecipitation());
		} else {
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@222", ""); //$NON-NLS-1$ //$NON-NLS-2$
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@227", ""); //$NON-NLS-1$ //$NON-NLS-2$
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@232", ""); //$NON-NLS-1$ //$NON-NLS-2$
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@237", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// Daily normals
		if (previousWeatherData.normalsDaily != null) {
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@223", //$NON-NLS-1$
					displayTemperature(previousWeatherData.normalsDaily.getTemperatureMax()));
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@228", //$NON-NLS-1$
					displayTemperature(previousWeatherData.normalsDaily.getTemperatureAverage()));
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@233", //$NON-NLS-1$
					displayTemperature(previousWeatherData.normalsDaily.getTemperatureMin()));
		} else {
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@223", ""); //$NON-NLS-1$ //$NON-NLS-2$
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@228", ""); //$NON-NLS-1$ //$NON-NLS-2$
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@233", ""); //$NON-NLS-1$ //$NON-NLS-2$
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@238", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// Monthly normals
		if (previousWeatherData.normalsMonthly != null) {
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@224", //$NON-NLS-1$
					displayTemperature(previousWeatherData.normalsMonthly.getTemperatureMax()));
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@229", //$NON-NLS-1$
					displayTemperature(previousWeatherData.normalsMonthly.getTemperatureAverage()));
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@234", //$NON-NLS-1$
					displayTemperature(previousWeatherData.normalsMonthly.getTemperatureMin()));
		} else {
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@224", ""); //$NON-NLS-1$ //$NON-NLS-2$
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@229", ""); //$NON-NLS-1$ //$NON-NLS-2$
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@234", ""); //$NON-NLS-1$ //$NON-NLS-2$
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@239", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}

		if (previousWeatherData.noaaSummariesWeatherStation != null) {
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@244", //$NON-NLS-1$
					previousWeatherData.noaaSummariesWeatherStation.getName());

			double distanceFromStart = previousWeatherData.noaaSummariesWeatherStation.getDistanceFromStart();
			String distance = ""; //$NON-NLS-1$
			if (settings.Unit == CgConst.UNIT_MILES_FEET)
				distanceFromStart = Utils.Km2Miles(distanceFromStart);

			distance = String.format("%.0f", distanceFromStart); //$NON-NLS-1$
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@246", //$NON-NLS-1$
					distance + " " + Utils.uLDist2String(settings.Unit)); //$NON-NLS-1$
		} else {
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@244", ""); //$NON-NLS-1$ //$NON-NLS-2$
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@246", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (previousWeatherData.noaaNormalsWeatherStation != null && !previousWeatherData.noaaNormalsWeatherStation
				.getId().equals(previousWeatherData.noaaSummariesWeatherStation.getId())) {
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@245", //$NON-NLS-1$
					previousWeatherData.noaaNormalsWeatherStation.getName());

			double distanceFromStart = previousWeatherData.noaaNormalsWeatherStation.getDistanceFromStart();
			String distance = ""; //$NON-NLS-1$
			if (settings.Unit == CgConst.UNIT_MILES_FEET)
				distanceFromStart = Utils.Meter2uMiles(distanceFromStart);

			distance = String.format("%.0f", distanceFromStart); //$NON-NLS-1$
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@247", //$NON-NLS-1$
					distance + " " + Utils.uLDist2String(settings.Unit)); //$NON-NLS-1$
		} else {
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@247", ""); //$NON-NLS-1$ //$NON-NLS-2$
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@245", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}

		String weatherDataSheet = ReplaceImages(weatherDataSheetBuilder.toString(), previousWeatherData.moonFraction);
		weatherDataSheet = AddWeatherStationsHyperLinks(weatherDataSheet,
				previousWeatherData.noaaSummariesWeatherStation, previousWeatherData.noaaNormalsWeatherStation);

		return weatherDataSheet;
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
				CgLog.error("SaveStat : impossible to save the weather data file"); //$NON-NLS-1$
				f.printStackTrace();
			}
			// -- Store the directory
			settings.LastDir = Utils.GetDirFromFilename(s);
		}
	}

	/**
	 * Creates a String containing a temperature value.
	 * 
	 * @param temperatureValue The temperature value.
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
	 * @param originalText The original HTML page
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

	/**
	 * Adds the HTTP link to each weather station.
	 * 
	 * @param originalSheet The original HTML page
	 * @return The HTML page containing the HTTP link for each weather station.
	 */
	private String AddWeatherStationsHyperLinks(String originalSheet, NoaaWeatherStation noaaSummariesWeatherStation,
			NoaaWeatherStation noaaNormalsWeatherStation) {
		Document document = Jsoup.parse(originalSheet);

		document.select("a[href]").forEach(e -> { //$NON-NLS-1$

			String href = e.attr("href"); //$NON-NLS-1$
			switch (href) {
			case "dailySummariesWeatherStation": //$NON-NLS-1$
				if (noaaSummariesWeatherStation != null) {
					e.attr("href", //$NON-NLS-1$
							"https://www.ncdc.noaa.gov/cdo-web/datasets/GHCND/stations/" //$NON-NLS-1$
									+ noaaSummariesWeatherStation.getId() + "/detail"); //$NON-NLS-1$
				}
				break;
			case "normalsWeatherStation": //$NON-NLS-1$
				if (noaaNormalsWeatherStation != null) {
					e.attr("href", //$NON-NLS-1$
							"https://www.ncdc.noaa.gov/cdo-web/datasets/GHCND/stations/" //$NON-NLS-1$
									+ noaaNormalsWeatherStation.getId() + "/detail"); //$NON-NLS-1$
				}
				break;
			}

		});

		return document.toString();
	}
}
