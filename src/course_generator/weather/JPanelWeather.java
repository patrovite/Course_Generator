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
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import course_generator.TrackData;
import course_generator.settings.CgSettings;
import course_generator.utils.CgConst;
import course_generator.utils.CgLog;
import course_generator.utils.CustomToolKit;
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
		CustomToolKit toolKit = new CustomToolKit();
		editorStat.setEditorKit(toolKit);
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
	 * Refreshes the weather tab
	 * 
	 * @param track              The current track
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
				lbInformation.setText(bundle.getString("JPanelWeather.lbInformationMissingInternetConnection.Text"));
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
			updateDataSheet("");
			btWeatherDataSave.setEnabled(false);
			return;
		}

		btWeatherDataSave.setEnabled(true);

		String dataSheetInfo = PopulateWeatherDataSheet(previousWeatherData);

		updateDataSheet(dataSheetInfo);
	}

	/**
	 * Refresh the view and set the cursor position
	 * 
	 * @param dataSheetInfo
	 */
	private void updateDataSheet(String dataSheetInfo) {
		editorStat.setText(dataSheetInfo);
		editorStat.setCaretPosition(0);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (!evt.getPropertyName().equals("NoaaTokenChanged"))
			return;
		UpdatePanel();
	}

	private String PopulateWeatherDataSheet(HistoricalWeather previousWeatherData) {

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
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@100", bundle.getString("JPanelWeather.EventSummary.Text"));
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@101", bundle.getString("JPanelWeather.Date.Text"));
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@102", bundle.getString("JPanelWeather.SunriseSunset.Text"));
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@103", bundle.getString("JPanelWeather.DaylightHours.Text"));
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@104", bundle.getString("JPanelWeather.MoonPhase.Text"));

		// HISTORICAL WEATHER DATA titles
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@200",
				bundle.getString("JPanelWeather.HistoricalWeatherData.Text"));
		if (previousWeatherData.pastDailySummaries != null && !previousWeatherData.pastDailySummaries.isEmpty()) {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@201",
					previousWeatherData.pastDailySummaries.get(0).getDate().toString("EE yyyy-MM-dd"));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@202",
					previousWeatherData.pastDailySummaries.get(1).getDate().toString("EE yyyy-MM-dd"));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@203",
					previousWeatherData.pastDailySummaries.get(2).getDate().toString("EE yyyy-MM-dd"));
		}
		if (previousWeatherData.normalsDaily != null) {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@204", bundle.getString("JPanelWeather.NormalsDaily.Text"));
		} else {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@204", "");

		}
		if (previousWeatherData.normalsMonthly != null) {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@205",
					bundle.getString("JPanelWeather.NormalsMonthly.Text"));
		} else {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@205", "");
		}

		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@206", bundle.getString("JPanelWeather.MaxTemperature.Text"));
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@207", bundle.getString("JPanelWeather.AvgTemperature.Text"));
		// TODO SWAP ????
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@208", bundle.getString("JPanelWeather.MinTemperature.Text"));
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@209", bundle.getString("JPanelWeather.Precipitation.Text"));
		// TODO OTHER DATA ?????
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@211", bundle.getString("JPanelWeather.StationName.Text"));
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@212",
				bundle.getString("JPanelWeather.DistanceFromStart.Text"));

		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@110",
				track.data.get(0).getHour().toString("yyyy-MM-dd HH:mm"));
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@111", track.EndNightTime.toString("HH:mm"));
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@112", track.StartNightTime.toString("HH:mm"));

		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@113", previousWeatherData.daylightHours);
		sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@114", previousWeatherData.getMoonPhaseDescription());

		if (previousWeatherData.pastDailySummaries != null && !previousWeatherData.pastDailySummaries.isEmpty()) {
			// Year -1
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@220",
					displayTemperature(previousWeatherData.pastDailySummaries.get(0).getTemperatureMax()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@225",
					displayTemperature(previousWeatherData.pastDailySummaries.get(0).getTemperatureAverage()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@230",
					displayTemperature(previousWeatherData.pastDailySummaries.get(0).getTemperatureMin()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@235",
					previousWeatherData.pastDailySummaries.get(0).getPrecipitation());

			// Year -2
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@221",
					displayTemperature(previousWeatherData.pastDailySummaries.get(1).getTemperatureMax()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@226",
					displayTemperature(previousWeatherData.pastDailySummaries.get(1).getTemperatureAverage()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@231",
					displayTemperature(previousWeatherData.pastDailySummaries.get(1).getTemperatureMin()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@236",
					previousWeatherData.pastDailySummaries.get(1).getPrecipitation());

			// Year -3
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@222",
					displayTemperature(previousWeatherData.pastDailySummaries.get(2).getTemperatureMax()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@227",
					displayTemperature(previousWeatherData.pastDailySummaries.get(2).getTemperatureAverage()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@232",
					displayTemperature(previousWeatherData.pastDailySummaries.get(2).getTemperatureMin()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@237",
					previousWeatherData.pastDailySummaries.get(2).getPrecipitation());
		}

		// Daily normals
		if (previousWeatherData.normalsDaily != null) {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@223",
					displayTemperature(previousWeatherData.normalsDaily.getTemperatureMax()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@228",
					displayTemperature(previousWeatherData.normalsDaily.getTemperatureAverage()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@233",
					displayTemperature(previousWeatherData.normalsDaily.getTemperatureMin()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@238", previousWeatherData.normalsDaily.getPrecipitation());
		} else {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@223", "");
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@228", "");
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@233", "");
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@238", "");
		}

		// Monthly normals
		if (previousWeatherData.normalsMonthly != null) {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@224",
					displayTemperature(previousWeatherData.normalsMonthly.getTemperatureMax()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@229",
					displayTemperature(previousWeatherData.normalsMonthly.getTemperatureAverage()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@234",
					displayTemperature(previousWeatherData.normalsMonthly.getTemperatureMin()));
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@239",
					displayTemperature(previousWeatherData.normalsMonthly.getPrecipitation()));
		} else {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@224", "");
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@229", "");
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@234", "");
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@239", "");
		}

		if (previousWeatherData.noaaSummariesWeatherStation != null) {
			sheetSkeleton = Utils.sbReplace(sheetSkeleton, "@244",
					previousWeatherData.noaaSummariesWeatherStation.getName());

			double distanceFromStart = previousWeatherData.noaaSummariesWeatherStation.getDistanceFromStart();
			String distance = "";
			if (settings.Unit == CgConst.UNIT_MILES_FEET)
				distanceFromStart = Utils.Km2Miles(distanceFromStart);

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

		return ReplaceImages(sheetSkeleton.toString());
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

				out.write(editorStat.getText());
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
	 * @param temperatureValue The temperature value
	 * @return A String containing a temperature information
	 */
	private String displayTemperature(String temperatureValue) {
		if (temperatureValue == null || temperatureValue == "")
			return "";

		return Utils.FormatTemperature(Double.valueOf(temperatureValue), settings.Unit)
				+ Utils.uTemperatureToString(settings.Unit);
	}

	/**
	 * Because the image paths in the original HTML reference images in the Course
	 * Generator jar (i.e: not accessible by any browser), we convert all the images
	 * references to their actual Base64 value. Why we can't use the Base64
	 * representation in Course Generator : Because Swing's (default) HTML support
	 * does not extend to Base 64 encoded images.
	 * 
	 * @param originalText The original HTML page
	 * @return The HTML page containing base64 representations of each image.
	 */
	private String ReplaceImages(String originalText) {
		Document document = Jsoup.parse(originalText);

		document.select("img[src]").forEach(e -> {

			String image = e.attr("src");
			String base64 = "";
			switch (image) {
			case "sunrise":
				base64 = Utils.imageToBase64(Utils.getIcon(this, "sunrise.png", settings.ToolbarIconSize));
				break;
			case "sunset":
				base64 = Utils.imageToBase64(Utils.getIcon(this, "sunset.png", settings.ToolbarIconSize));
				break;
			case "thermometer":
				base64 = Utils.imageToBase64(Utils.getIcon(this, "thermometer.png", settings.ToolbarIconSize));
				break;
			case "moonphase":
				base64 = Utils.imageToBase64(Utils.getIcon(this, "thermometer.png", settings.ToolbarIconSize));
				break;
			}

			e.attr("src",
					"data:image/png;base64,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");
			/// "data:image/png;base64," + base64);// + "\" width=\"50%\"
			/// height=\"50%\"/>");
		});

		return document.toString();
	}
}
