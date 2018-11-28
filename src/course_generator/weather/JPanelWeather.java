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
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import course_generator.TrackData;
import course_generator.settings.CgSettings;
import course_generator.utils.CgConst;
import course_generator.utils.CgLog;
import course_generator.utils.Utils;

public class JPanelWeather extends JPanel implements PropertyChangeListener {
	private static final long serialVersionUID = -7168142806619093218L;
	private ResourceBundle bundle;
	private CgSettings settings = null;
	private JEditorPane editorStat;
	private JScrollPane scrollPaneStat;
	private JToolBar toolBar;
	private JButton btWeatherDataSave;
	private JButton btWeatherRefresh;
	private JLabel lbInformation;
	private JLabel InformationWarning;
	private JLabel getNoaaTokenLink;
	private TrackData track = null;


	public JPanelWeather(CgSettings settings) {
		super();
		this.settings = settings;
		this.settings.addNoaaTokenChangeListener(this);
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		initComponents();
	}


	private void initComponents() {
		setLayout(new java.awt.BorderLayout());

		// -- Statistic tool bar
		// ---------------------------------------------------
		createWeatherToolbar();
		UpdatePanel();
		add(toolBar, java.awt.BorderLayout.NORTH);

		editorStat = new JEditorPane();
		editorStat.setContentType("text/html");
		editorStat.setEditable(false);
		scrollPaneStat = new JScrollPane(editorStat);
		add(scrollPaneStat, java.awt.BorderLayout.CENTER);
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
		btWeatherDataSave.setIcon(Utils.getIcon(this, "save_html.png", settings.ToolbarIconSize));
		btWeatherDataSave.setToolTipText(bundle.getString("JPanelWeather.btWeatherDataSave.toolTipText"));
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
		btWeatherRefresh.setIcon(Utils.getIcon(this, "refresh.png", settings.ToolbarIconSize));
		btWeatherRefresh.setToolTipText(bundle.getString("JPanelWeather.btWeatherRefresh.toolTipText"));
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
		InformationWarning = new JLabel(Utils.getIcon(this, "cancel.png", settings.ToolbarIconSize));
		InformationWarning.setVisible(false);
		InformationWarning.setFocusable(false);
		toolBar.add(InformationWarning);

		lbInformation = new JLabel(" " + bundle.getString("JPanelWeather.lbInformationMissingNoaaToken.Text"));
		Font boldFont = new Font(lbInformation.getFont().getName(), Font.BOLD, lbInformation.getFont().getSize());
		lbInformation.setFont(boldFont);
		InformationWarning.setFocusable(false);
		lbInformation.setVisible(false);
		toolBar.add(lbInformation);

		getNoaaTokenLink = new JLabel(". " + bundle.getString("JPanelWeather.lbNOAARequestTokenLink.Text"));
		getNoaaTokenLink.setForeground(Color.BLUE.darker());
		boldFont = new Font(getNoaaTokenLink.getFont().getName(), Font.BOLD, getNoaaTokenLink.getFont().getSize());
		getNoaaTokenLink.setFont(boldFont);
		getNoaaTokenLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		getNoaaTokenLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {

					Desktop.getDesktop().browse(new URI("https://www.ncdc.noaa.gov/cdo-web/token"));

				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		getNoaaTokenLink.setVisible(false);
		toolBar.add(getNoaaTokenLink);

		refresh(null, false);
	}


	private void UpdatePanel() {

		boolean isNoaaTokenValid = settings.isNoaaTokenValid();

		InformationWarning.setVisible(!isNoaaTokenValid);
		lbInformation.setVisible(!isNoaaTokenValid);
		getNoaaTokenLink.setVisible(!isNoaaTokenValid);

		btWeatherRefresh.setEnabled(isNoaaTokenValid && track != null);
	}


	/**
	 * Refresh the statistic tab
	 */
	public void refresh(TrackData track, boolean retrieveOnlineData) {
		if (track == null || track.data.isEmpty()) {
			UpdatePanel();
			return;
		}

		this.track = track;

		UpdatePanel();

		ArrayList<WeatherHistory> previousWeatherHistory = new ArrayList<WeatherHistory>();
		WeatherHistory previousWeatherData = null;
		if (retrieveOnlineData) {

			if (!Utils.isInternetReachable()) {
				lbInformation.setText(bundle.getString("JPanelWeather.lbInformationMissingInternetConnection.Text"));
				lbInformation.setVisible(true);
				InformationWarning.setVisible(true);
				return;
			}

			previousWeatherData = new WeatherHistory(settings);
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			previousWeatherData.RetrieveWeatherData(track);
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			for (int forecastIndex = 0; forecastIndex < 3; ++forecastIndex) {
				/*
				 * WeatherHistory previousWeatherData = new WeatherHistory(settings, track,
				 * Latitude, Longitude, forecastIndex + 1);
				 * 
				 * previousWeatherData.RetrieveWeatherData();
				 * previousWeatherHistory.add(previousWeatherData);
				 */
			}
		} else {
			// If exists, get the historical weather from the CGX course
			// previousWeatherHistory = track.getHistoricalWeather();
		}

		// if (previousWeatherHistory.isEmpty()) {
		// -- Refresh the view and set the cursor position
		// editorStat.setText("");
		// editorStat.setCaretPosition(0);
		// return;
		// }
		if (previousWeatherData == null) {
			return;
		}

		btWeatherDataSave.setEnabled(true);

		String dataSheetInfo = PopulateWeatherDataSheet(previousWeatherData);

		// -- Refresh the view and set the cursor position
		if (dataSheetInfo != "") {
			editorStat.setText(dataSheetInfo);
			editorStat.setCaretPosition(0);
		}
	}


	public void propertyChange(PropertyChangeEvent evt) {
		if (!evt.getPropertyName().equals("NoaaTokenChanged"))
			return;
		UpdatePanel();
	}


	private String PopulateWeatherDataSheet(WeatherHistory previousWeatherData) {

		StringBuilder sheetSkeleton = new StringBuilder();
		InputStream is = getClass().getResourceAsStream("weatherdatasheet.html");

		try {
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);

			String line;
			while ((line = br.readLine()) != null) {
				sheetSkeleton.append(line);
			}
			br.close();
			isr.close();
			is.close();
		} catch (IOException e) {
			CgLog.error("RefreshStat : Impossible to read the template file from resource");
			e.printStackTrace();
		}

		// EVENT SUMMARY titles
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@100", "EVENT SUMMARY");
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@101", "Date");
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@102", "");
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@103", "Daylight hours");
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@104", "moonphase");
		// TODO Store that i the CGX as well ?

		// HISTORICAL WEATHER DATA titles
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@200", "HISTORICAL WEATHER DATA");
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@201",
				displayDateTime(previousWeatherData.previousDailySummaries.get(0).getDate(), "EE yyyy-MM-dd"));
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@202",
				displayDateTime(previousWeatherData.previousDailySummaries.get(1).getDate(), "EE yyyy-MM-dd"));
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@203",
				displayDateTime(previousWeatherData.previousDailySummaries.get(2).getDate(), "EE yyyy-MM-dd"));
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@204", "Daily normals");
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@205", "Monthly average");

		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@206", "TMax");
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@207", "TAvg");
		// TODO SWAP ????
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@208", "TMin");
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@209", "Precip");
		// TODO OTHER DATA ?????
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@211", "Weather station name");
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@212", "Distance from start");

		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@110",
				displayDateTime(track.data.get(0).getHour(), "yyyy-MM-dd HH:mm"));
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@111", displayDateTime(track.EndNightTime, "HH:mm"));
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@112", displayDateTime(track.StartNightTime, "HH:mm"));
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@113", "12h");
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@114", "78%");

		if (!previousWeatherData.previousDailySummaries.isEmpty()) {
			// Year -1
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@220",
					displayTemperature(previousWeatherData.previousDailySummaries.get(0).getTemperatureMax()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@225",
					displayTemperature(previousWeatherData.previousDailySummaries.get(0).getTemperatureAverage()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@230",
					displayTemperature(previousWeatherData.previousDailySummaries.get(0).getTemperatureMin()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@235",
					displayDouble(previousWeatherData.previousDailySummaries.get(0).getPrecipitation()));

			// Year -2
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@221",
					displayTemperature(previousWeatherData.previousDailySummaries.get(1).getTemperatureMax()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@226",
					displayTemperature(previousWeatherData.previousDailySummaries.get(1).getTemperatureAverage()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@231",
					displayTemperature(previousWeatherData.previousDailySummaries.get(1).getTemperatureMin()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@236",
					displayDouble(previousWeatherData.previousDailySummaries.get(1).getPrecipitation()));

			// Year -3
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@222",
					displayTemperature(previousWeatherData.previousDailySummaries.get(2).getTemperatureMax()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@227",
					displayTemperature(previousWeatherData.previousDailySummaries.get(2).getTemperatureAverage()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@232",
					displayTemperature(previousWeatherData.previousDailySummaries.get(2).getTemperatureMin()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@237",
					displayDouble(previousWeatherData.previousDailySummaries.get(2).getPrecipitation()));
		}

		// Daily normals
		if (previousWeatherData.dailyNormals != null) {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@223",
					displayTemperature(previousWeatherData.dailyNormals.getTemperatureMax()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@228",
					displayTemperature(previousWeatherData.dailyNormals.getTemperatureAverage()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@233",
					displayTemperature(previousWeatherData.dailyNormals.getTemperatureMin()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@238",
					displayDouble(previousWeatherData.dailyNormals.getPrecipitation()));
		}

		// Monthly normals
		if (previousWeatherData.monthlyNormals != null) {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@224",
					displayTemperature(previousWeatherData.monthlyNormals.getTemperatureMax()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@229",
					displayTemperature(previousWeatherData.monthlyNormals.getTemperatureAverage()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@234",
					displayTemperature(previousWeatherData.monthlyNormals.getTemperatureMin()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@239",
					displayTemperature(previousWeatherData.monthlyNormals.getPrecipitation()));
		}

		if (previousWeatherData.noaaSummariesWeatherStation != null) {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@244",
					previousWeatherData.noaaSummariesWeatherStation.getName());

			double distanceFromStart = previousWeatherData.noaaSummariesWeatherStation.getDistanceFromStart();
			String distance = "";
			if (settings.Unit == CgConst.UNIT_MILES_FEET)
				distanceFromStart = Utils.Meter2uMiles(distanceFromStart) / 1000.0;

			distance = String.format("%.0f", distanceFromStart);
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@246", distance + " " + Utils.uLDist2String(settings.Unit));
		} else {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@244", "");
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@246", "");
		}

		if (previousWeatherData.noaaNormalsWeatherStation != null) {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@245",
					previousWeatherData.noaaNormalsWeatherStation.getName());

			double distanceFromStart = previousWeatherData.noaaNormalsWeatherStation.getDistanceFromStart();
			String distance = "";
			if (settings.Unit == CgConst.UNIT_MILES_FEET)
				distanceFromStart = Utils.Meter2uMiles(distanceFromStart) / 1000.0;

			distance = String.format("%.0f", distanceFromStart);
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@247", distance + " " + Utils.uLDist2String(settings.Unit));
		} else {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@245", "");
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@247", "");
		}

		return sheetSkeleton.toString();
	}


	/**
	 * Save the statistics in TXT format
	 */
	private void SaveStat() {
		String s;
		s = Utils.SaveDialog(this, settings.LastDir, "", ".html", bundle.getString("frmMain.HTMLFile"), true,
				bundle.getString("frmMain.FileExist"));

		if (!s.isEmpty()) {
			try {
				FileWriter out = new FileWriter(s);

				String text = ReplaceImages(editorStat.getText());

				out.write(text);
				out.close();
			} catch (Exception f) {
				CgLog.error("SaveStat : impossible to save the statistic file");
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
	 *            The temperature value
	 * @return A String containing a temperature information
	 */
	private String displayDouble(String value) {
		if (value == null || value == "")
			return "";

		return String.valueOf(value);
	}


	/**
	 * Creates a String containing a temperature value.
	 * 
	 * @param temperatureValue
	 *            The temperature value
	 * @return A String containing a temperature information
	 */
	private String displayTemperature(String temperatureValue) {
		if (temperatureValue == null || temperatureValue == "")
			return "";

		return Utils.FormatTemperature(Double.valueOf(temperatureValue), settings.Unit)
				+ Utils.uTemperatureToString(settings.Unit);
	}


	/**
	 * Creates a String containing a measured time.
	 * 
	 * @param time
	 *            A Unix time.
	 * @return A String containing a time information.
	 */
	private String displayTime(long time, String timeZone) {
		DateTime dateTime = Utils.unixTimeToDateTime(time, timeZone);

		DateTimeFormatter dtfOut = DateTimeFormat.forPattern("HH:mm");

		return dtfOut.print(dateTime);
	}


	/**
	 * Creates a String containing a measured time.
	 * 
	 * @param time
	 *            A Unix time.
	 * @return A String containing a time information.
	 */
	private String displayDateTime(DateTime date, String pattern) {
		DateTimeFormatter dtfOut = DateTimeFormat.forPattern(pattern);

		return dtfOut.print(date);
	}


	private String addImage(String iconFilePath) {
		if (iconFilePath == "")
			return "";
		return "<img src=\"file:/" + iconFilePath + "\" width=\"50%\" height=\"50%\"/>";
	}


	/**
	 * Because the image paths in the original HTML reference images in the Course
	 * Generator jar (i.e: not accessible by any browser), we convert all the images
	 * references to their actual Base64 value. Why we can't use the Base64
	 * representation in Course Generator : Because Swing's (default) HTML support
	 * does not extend to Base 64 encoded images.
	 * 
	 * @param originalText
	 *            The original HTML page
	 * @return The HTML page containing base64 representations of each image.
	 */
	private String ReplaceImages(String originalText) {
		Document document = Jsoup.parse(originalText);

		document.select("img[src]").forEach(e -> {
			System.out.println(e.text());

			String absoluteFilePath = e.attr("src");

			// We remove the string "file:/"
			absoluteFilePath = absoluteFilePath.substring(6);
			String base64 = Utils.getFileBase64(absoluteFilePath);

			e.attr("src", "data:image/png;base64," + base64);
		});

		return document.toString();
	}
}
