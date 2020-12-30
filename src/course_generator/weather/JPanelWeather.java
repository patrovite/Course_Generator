
package course_generator.weather;

import java.awt.Desktop;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import course_generator.TrackData;
import course_generator.dialogs.ProgressDialog;
import course_generator.settings.CgSettings;
import course_generator.utils.CgConst;
import course_generator.utils.CgLog;
import course_generator.utils.Utils;

/**
 * A class that generates a GUI panel for the weather data.
 * 
 * @author Fr�d�ric Bard
 */
public class JPanelWeather extends JPanel implements PropertyChangeListener {
	private static final long serialVersionUID = -7168142806619093218L;
	private ResourceBundle bundle;
	private CgSettings settings = null;
	private JEditorPane editorWeather;
	private JScrollPane scrollPaneWeather;
	private JToolBar toolBar;
	private JButton btWeatherDataSave;
	private JButton btWeatherRefresh;
	private JLabel lbInformation;
	private JLabel InformationWarning;
	private TrackData track = null;
	HistoricalWeather previousWeatherData;
	private String weatherDataSheetContent;
	private ProgressDialog progressDialog;
	private JFrame parentFrame;
	private double moonPhase;


	public JPanelWeather(CgSettings settings) {
		super();
		this.settings = settings;
		this.previousWeatherData = null;
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		initComponents();
	}


	private void initComponents() {
		setLayout(new java.awt.BorderLayout());

		// -- Weather tool bar
		// ---------------------------------------------------
		createWeatherToolbar();
		add(toolBar, java.awt.BorderLayout.NORTH);

		editorWeather = new JEditorPane();
		editorWeather.setContentType("text/html");
		editorWeather.setEditable(false);
		editorWeather.addHyperlinkListener(new HyperlinkListener() {
		    public void hyperlinkUpdate(HyperlinkEvent e) {
		        if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
		        	if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
						try {
							Desktop.getDesktop().browse(e.getURL().toURI());
						} catch (IOException | URISyntaxException ex) {
							ex.printStackTrace();
						}
					} else {
						System.err.println("Opening link not supported on current platform ('"+ e.getURL()+"')");
					}
		        }
		    }
		}
		);
		scrollPaneWeather = new JScrollPane(editorWeather);
		add(scrollPaneWeather, java.awt.BorderLayout.CENTER);
	}


	/**
	 * Create the weather toolbar
	 */
	private void createWeatherToolbar() {
		toolBar = new javax.swing.JToolBar();
		toolBar.setOrientation(javax.swing.SwingConstants.HORIZONTAL);
		toolBar.setFloatable(false);
		toolBar.setRollover(true);

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

		refresh(null, false);
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
			return;
		}

		this.track = track;
		btWeatherRefresh.setEnabled(true);

		previousWeatherData = null;
		if (retrieveOnlineData) {
			progressDialog = new ProgressDialog(parentFrame, bundle.getString("JPanelWeather.lbProgressBar.Text")); //$NON-NLS-1$
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					progressDialog.setVisible(true);
				}
			});
			progressDialog.setValue(0);

			if (!Utils.isInternetReachable()) {
				lbInformation.setText(bundle.getString("JPanelWeather.lbInformationMissingInternetConnection.Text")); //$NON-NLS-1$
				lbInformation.setVisible(true);
				InformationWarning.setVisible(true);
				return;
			}

			previousWeatherData = new HistoricalWeather();
			previousWeatherData.addWeatherDataRetrievedChangeListener(this);
			previousWeatherData.RetrieveWeatherData(track, progressDialog);
		} else {
			// If exists, get the historical weather from the CGX course
			previousWeatherData = track.getHistoricalWeather();

			if (previousWeatherData == null) {
				updateDataSheet(""); //$NON-NLS-1$
				btWeatherDataSave.setEnabled(false);
				return;
			}

			String newContent = PopulateWeatherDataSheet(previousWeatherData);

			updateDataSheet(newContent);
		}

		btWeatherDataSave.setEnabled(true);
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
		editorWeather.setText(dataSheetContent);
	}


	/**
	 * Performs the necessary operations whenever the NOAA token value has changed.
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("WeatherDataRetrieved")) //$NON-NLS-1$
		{
			if (evt.getNewValue().equals("cancelled")) //$NON-NLS-1$
				return;

			if (previousWeatherData == null) {
				updateDataSheet(""); //$NON-NLS-1$
				btWeatherDataSave.setEnabled(false);
			}
			track.setHistoricalWeather(previousWeatherData);
			String newContent = PopulateWeatherDataSheet(previousWeatherData);

			updateDataSheet(newContent);
		}
	}


	/**
	 * Parses weather data and populates the weather data sheet.
	 * 
	 * @param previousWeatherData
	 *            The retrieved NOAA weather data.
	 * @return The filled HTML content of the weather data sheet.
	 */
	private String PopulateWeatherDataSheet(HistoricalWeather previousWeatherData) {

		StringBuilder weatherDataSheetBuilder = new StringBuilder();

		try (InputStream is = getClass().getResourceAsStream("weatherdatasheet.html");
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr)){
			
			String line;
			while ((line = br.readLine()) != null) {
				weatherDataSheetBuilder.append(line);
			}
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
		if (previousWeatherData.getPastDailySummaries() != null
				&& !previousWeatherData.getPastDailySummaries().isEmpty()) {
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@201", //$NON-NLS-1$
					previousWeatherData.getPastDailySummaries().get(0).getDate().toString(datePattern));
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@202", //$NON-NLS-1$
					previousWeatherData.getPastDailySummaries().get(1).getDate().toString(datePattern));
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@203", //$NON-NLS-1$
					previousWeatherData.getPastDailySummaries().get(2).getDate().toString(datePattern));
		} else {
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@201", "No weather station found"); //$NON-NLS-1$ //$NON-NLS-2$
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@202", ""); //$NON-NLS-1$ //$NON-NLS-2$
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@203", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}

		NoaaWeatherData normalsDaily = previousWeatherData.getNormalsDaily() != null
				? previousWeatherData.getNormalsDaily()
				: null;
		String normalsDailyText = normalsDaily != null ? bundle.getString("JPanelWeather.NormalsDaily.Text") : "";
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@204", normalsDailyText); //$NON-NLS-1$ //$NON-NLS-2$
		
		NoaaWeatherData normalsMonthly = previousWeatherData.getNormalsMonthly() != null
				? previousWeatherData.getNormalsMonthly()
				: null;
		String normalsMonthlyText = normalsMonthly != null ? bundle.getString("JPanelWeather.NormalsMonthly.Text") : "";
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@205", normalsMonthlyText); //$NON-NLS-1$ //$NON-NLS-2$
		
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

		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@113", //$NON-NLS-1$
				previousWeatherData.getDaylightHours());
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@114", //$NON-NLS-1$
				previousWeatherData.getMoonPhaseDescription());
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@115", //$NON-NLS-1$
				previousWeatherData.getMoonFraction() + "%");


		// Year -1
		NoaaWeatherData pastDailySummaryYearMinus1 = previousWeatherData.getPastDailySummaries() != null
				&& !previousWeatherData.getPastDailySummaries().isEmpty()
				&& previousWeatherData.getPastDailySummaries().get(0) != null
				? previousWeatherData.getPastDailySummaries().get(0)
						: null;
	    String temperatureMax = pastDailySummaryYearMinus1 !=null ? displayTemperature(pastDailySummaryYearMinus1.getTemperatureMax()) : "";
		String temperatureAverage = pastDailySummaryYearMinus1 !=null ? displayTemperature(pastDailySummaryYearMinus1.getTemperatureAverage()) : "";
		String temperatureMin = pastDailySummaryYearMinus1 !=null ? displayTemperature(pastDailySummaryYearMinus1.getTemperatureMin()) : "";
		String precipitation = pastDailySummaryYearMinus1 !=null ? displayPrecipitation(pastDailySummaryYearMinus1.getPrecipitation()) : "";
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@220", temperatureMax); //$NON-NLS-1$
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@225", temperatureAverage); //$NON-NLS-1$
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@230", temperatureMin); //$NON-NLS-1$
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@235", precipitation); //$NON-NLS-1$

		// Year -2
		NoaaWeatherData pastDailySummaryYearMinus2 = previousWeatherData.getPastDailySummaries() != null
				&& !previousWeatherData.getPastDailySummaries().isEmpty()
				&& previousWeatherData.getPastDailySummaries().get(1) != null
				? previousWeatherData.getPastDailySummaries().get(1)
						: null;
		temperatureMax = pastDailySummaryYearMinus2 !=null ? displayTemperature(pastDailySummaryYearMinus2.getTemperatureMax()) : "";
		temperatureAverage = pastDailySummaryYearMinus2 !=null ? displayTemperature(pastDailySummaryYearMinus2.getTemperatureAverage()) : "";
		temperatureMin = pastDailySummaryYearMinus2 !=null ? displayTemperature(pastDailySummaryYearMinus2.getTemperatureMin()) : "";
		precipitation = pastDailySummaryYearMinus2 !=null ? displayPrecipitation(pastDailySummaryYearMinus2.getPrecipitation()) : "";
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@221", temperatureMax); //$NON-NLS-1$
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@226", temperatureAverage); //$NON-NLS-1$
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@231", temperatureMin); //$NON-NLS-1$
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@236", precipitation); //$NON-NLS-1$
		
		// Year -3
		NoaaWeatherData pastDailySummaryYearMinus3 = previousWeatherData.getPastDailySummaries() != null
				&& !previousWeatherData.getPastDailySummaries().isEmpty()
				&& previousWeatherData.getPastDailySummaries().get(2) != null
				? previousWeatherData.getPastDailySummaries().get(2)
						: null;
		temperatureMax = pastDailySummaryYearMinus3 !=null ? displayTemperature(pastDailySummaryYearMinus3.getTemperatureMax()) : "";
		temperatureAverage = pastDailySummaryYearMinus3 !=null ? displayTemperature(pastDailySummaryYearMinus3.getTemperatureAverage()) : "";
		temperatureMin = pastDailySummaryYearMinus3 !=null ? displayTemperature(pastDailySummaryYearMinus3.getTemperatureMin()) : "";
		precipitation = pastDailySummaryYearMinus3 !=null ? displayPrecipitation(pastDailySummaryYearMinus3.getPrecipitation()) : "";
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@222", temperatureMax); //$NON-NLS-1$
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@227", temperatureAverage); //$NON-NLS-1$
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@232", temperatureMin); //$NON-NLS-1$
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@237", precipitation); //$NON-NLS-1$
				
		// Daily normals
		temperatureMax = normalsDaily !=null ? displayTemperature(normalsDaily.getTemperatureMax()) : "";
		temperatureAverage = normalsDaily !=null ? displayTemperature(normalsDaily.getTemperatureAverage()) : "";
		temperatureMin = normalsDaily !=null ? displayTemperature(normalsDaily.getTemperatureMin()) : "";

		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@223", temperatureMax); //$NON-NLS-1$ //$NON-NLS-2$
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@228", temperatureAverage); //$NON-NLS-1$ //$NON-NLS-2$
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@233", temperatureMin); //$NON-NLS-1$ //$NON-NLS-2$

		// Monthly normals
		temperatureMax = normalsMonthly !=null ? displayTemperature(normalsMonthly.getTemperatureMax()) : "";
		temperatureAverage = normalsMonthly !=null ? displayTemperature(normalsMonthly.getTemperatureAverage()) : "";
		temperatureMin = normalsMonthly !=null ? displayTemperature(normalsMonthly.getTemperatureMin()) : "";

		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@224", temperatureMax); //$NON-NLS-1$ //$NON-NLS-2$
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@229", temperatureAverage); //$NON-NLS-1$ //$NON-NLS-2$
		weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@234", temperatureMin); //$NON-NLS-1$ //$NON-NLS-2$

		if (previousWeatherData.getNoaaSummariesWeatherStation() != null) {
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@244", //$NON-NLS-1$
					previousWeatherData.getNoaaSummariesWeatherStation().getName());

			double distanceFromStart = previousWeatherData.getNoaaSummariesWeatherStation().getDistanceFromStart();
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
		if (previousWeatherData.getNoaaNormalsWeatherStation() != null
				&& previousWeatherData.getNoaaSummariesWeatherStation() != null
				&& !previousWeatherData.getNoaaNormalsWeatherStation().getId()
						.equals(previousWeatherData.getNoaaSummariesWeatherStation().getId())) {
			weatherDataSheetBuilder = Utils.sbReplace(weatherDataSheetBuilder, "@245", //$NON-NLS-1$
					previousWeatherData.getNoaaNormalsWeatherStation().getName());

			double distanceFromStart = previousWeatherData.getNoaaNormalsWeatherStation().getDistanceFromStart();
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

		moonPhase = previousWeatherData.getMoonPhase();
		String weatherDataSheet = InsertImages(weatherDataSheetBuilder.toString(),
				moonPhase);
		weatherDataSheet = AddWeatherStationsHyperLinks(weatherDataSheet,
				previousWeatherData.getNoaaSummariesWeatherStation(),
				previousWeatherData.getNoaaNormalsWeatherStation());

		return weatherDataSheet;
	}


	/**
	 * Save the Weather in HTML format
	 */
	private void SaveStat() {
		String s = Utils.SaveDialog(this, settings.getLastDirectory(), "", ".html", bundle.getString("frmMain.HTMLFile"), true, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				bundle.getString("frmMain.FileExist")); //$NON-NLS-1$

		if (s.isEmpty()) {
			return;
		}
		
		try (FileWriter out = new FileWriter(s)){
				
			weatherDataSheetContent = ReplaceWithBase64Images(weatherDataSheetContent, moonPhase);
			out.write(weatherDataSheetContent);
				
		} catch (Exception f) {
			CgLog.error("SaveStat : impossible to save the weather data file"); //$NON-NLS-1$
			f.printStackTrace();
		}
		
		// -- Store the directory
		settings.setLastDirectory(Utils.GetDirFromFilename(s));
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
	
	private String displayPrecipitation(String precipitationValue) {
		if (precipitationValue == null || precipitationValue.equals("")) //$NON-NLS-1$
			return ""; //$NON-NLS-1$

		return Utils.FormatPrecipitation(precipitationValue, settings.Unit, true);
	}

	/**
	 * Because the image paths in the original HTML reference images in the Course
	 * Generator jar (i.e: not accessible by any browser), we convert all the images
	 * references to their actual Base64 value.
	 * 
	 * @param originalText
	 *          The original HTML page
	 * @param moonPhase
	 * 			The value of the moon phase
	 * @return The HTML page containing base64 representations of each image.
	 */
	private String ReplaceWithBase64Images(String originalText, double moonPhase) {
		Document document = Jsoup.parse(originalText);

		document.select("img[src]").forEach(e -> { //$NON-NLS-1$

			String image = e.attr("src"); //$NON-NLS-1$
			String base64 = ""; //$NON-NLS-1$
			
			if(image.endsWith("sunrise.png")){
				base64 = Utils.imageToBase64(this, "sunrise.png", 128); //$NON-NLS-1$
			} else if(image.endsWith("sunset.png")){
				base64 = Utils.imageToBase64(this, "sunset.png", 128); //$NON-NLS-1$
			}else if(image.endsWith("thermometer.png")){
				base64 = Utils.imageToBase64(this, "thermometer.png", 128); //$NON-NLS-1$
			} else if(image.contains("moon-")){
				File filePath = new File(image);
				String filename = filePath.getName();
				base64 = Utils.imageToBase64(this, filename, 128); //$NON-NLS-1$
			}

			e.attr("src", "data:image/png;base64," + base64); //$NON-NLS-1$ //$NON-NLS-2$

		});

		return document.toString();
	}
	
	/**
	 * Insert the images file paths.
	 * 
	 * @param originalText
	 *          The original HTML page
	 * @param moonPhase
	 * 			The value of the moon phase
	 * @return The HTML page containing the path for each image.
	 */
	private String InsertImages(String originalText, double moonPhase) {
		
		Document document = Jsoup.parse(originalText);
		
		String imagesPath = "/course_generator/images/128/";

		document.select("img[src]").forEach(e -> { //$NON-NLS-1$

			String image = e.attr("src"); //$NON-NLS-1$
			String base64 = ""; //$NON-NLS-1$
			switch (image) {
			case "sunrise": //$NON-NLS-1$
				base64 = getClass().getResource(imagesPath + "sunrise.png").toString();
				break;
			case "sunset": //$NON-NLS-1$
				base64 = getClass().getResource(imagesPath + "sunset.png").toString();
				break;
			case "thermometer": //$NON-NLS-1$
				base64 = getClass().getResource(imagesPath + "thermometer.png").toString();
				break;
			case "moonphase": //$NON-NLS-1$
				String moonPhaseIcon = HistoricalWeather.getMoonPhaseIcon(moonPhase);
				base64 = getClass().getResource(imagesPath + moonPhaseIcon).toString();
				break;
			}

			e.attr("src", base64); //$NON-NLS-1$ //$NON-NLS-2$

		});

		return document.toString();
	}


	/**
	 * Adds the HTTP link to each weather station.
	 * 
	 * @param originalSheet
	 *            The original HTML page
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
							NoaaWeatherStation.WebUrlBase + noaaSummariesWeatherStation.getId() + "/detail"); //$NON-NLS-1$
				}
				break;
			case "normalsWeatherStation": //$NON-NLS-1$
				if (noaaNormalsWeatherStation != null) {
					e.attr("href", //$NON-NLS-1$
							NoaaWeatherStation.WebUrlBase + noaaNormalsWeatherStation.getId() + "/detail"); //$NON-NLS-1$
				}
				break;
			}

		});

		return document.toString();
	}
}
