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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import course_generator.TrackData;
import course_generator.settings.CgSettings;
import course_generator.utils.CgLog;
import course_generator.utils.Utils;

public class JPanelWeather extends JPanel {
	private static final long serialVersionUID = -7168142806619093218L;
	private ResourceBundle bundle;
	private CgSettings settings = null;
	private JEditorPane editorStat;
	private JScrollPane scrollPaneStat;
	private JToolBar toolBar;
	private JButton btWeatherDataSave;
	private JButton btWeatherRefresh;
	private JLabel lbInformation;
	private JMenuItem InformationWarning;
	private TrackData track = null;

	private Double Latitude;
	private Double Longitude;
	private DateTime StartTime;


	public JPanelWeather(CgSettings settings) {
		super();
		this.settings = settings;
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		initComponents();
	}


	private void initComponents() {
		setLayout(new java.awt.BorderLayout());

		// -- Statistic tool bar
		// ---------------------------------------------------
		Create_Statistic_Toolbar();
		add(toolBar, java.awt.BorderLayout.NORTH);

		editorStat = new JEditorPane();
		editorStat.setContentType("text/html");
		editorStat.setEditable(false);
		scrollPaneStat = new JScrollPane(editorStat);
		add(scrollPaneStat, java.awt.BorderLayout.CENTER);
	}


	/**
	 * Create the status toolbar
	 */
	private void Create_Statistic_Toolbar() {
		toolBar = new javax.swing.JToolBar();
		toolBar.setOrientation(javax.swing.SwingConstants.HORIZONTAL);
		toolBar.setFloatable(false);
		toolBar.setRollover(true);

		// -- Save
		// --------------------------------------------------------------
		btWeatherDataSave = new javax.swing.JButton();
		btWeatherDataSave.setIcon(Utils.getIcon(this, "save_html.png", settings.ToolbarIconSize));
		btWeatherDataSave.setToolTipText(bundle.getString("JPanelWeather.btWeatherDataSave.toolTipText"));
		btWeatherDataSave.setFocusable(false);
		btWeatherDataSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// SaveStat();
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
		btWeatherRefresh.setFocusable(false);
		btWeatherRefresh.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				refresh(track);
			}
		});
		toolBar.add(btWeatherRefresh);

		// -- Tab information
		// --------------------------------------------------------------
		InformationWarning = new JMenuItem(Utils.getIcon(this, "cancel.png", settings.TagIconSize));
		InformationWarning.setVisible(false);
		toolBar.add(InformationWarning);

		lbInformation = new JLabel();
		lbInformation.setVisible(false);
		toolBar.add(lbInformation);
	}


	/**
	 * Refresh the statistic tab
	 */
	public void refresh(TrackData track) {
		if (track == null)
			return;

		if (track.data.isEmpty())
			return;

		this.track = track;

		StringBuilder sb = new StringBuilder();

		// -- Get current language
		String lang = Locale.getDefault().toString();

		InputStream is = getClass().getResourceAsStream("statweather_" + lang + ".html");
		// -- File exist?
		if (is == null) {
			/// -- Use default file
			is = getClass().getResourceAsStream("statweather_en_US.html");
			CgLog.info("RefreshStat: Statistic file not present! Loading the english statistic file");
		}

		try {
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);

			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			isr.close();
			is.close();
		} catch (IOException e) {
			CgLog.error("RefreshStat : Impossible to read the template file from resource");
			e.printStackTrace();
		}

		lbInformation.setText("");
		if (!Utils.isInternetReachable()) {
			lbInformation.setText(bundle.getString("JPanelWeather.lbInformationMissingInternetConnection.Text"));
			lbInformation.setVisible(true);
			InformationWarning.setVisible(true);
			return;
		}
		if (!settings.isDarkSkyApiKeyValid()) {
			if (lbInformation.getText() != "") {
				lbInformation.setText(", ");
			}
			lbInformation.setText(
					lbInformation.getText().concat(bundle.getString("JPanelWeather.lbInformationMissingApiKey.Text")));
			lbInformation.setVisible(true);
			InformationWarning.setVisible(true);
			return;
		}
		lbInformation.setVisible(false);
		InformationWarning.setVisible(false);
		// if(not online or api key missing, update the label and display the waring)

		WeatherHistory previousWeatherData = new WeatherHistory(settings, Latitude, Longitude);
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

		sb = Utils.sbReplace(sb, "@0", Utils.uTemperatureToString(settings.Unit));
		sb = Utils.sbReplace(sb, "@1", Utils.uTemperatureToString(settings.Unit));
		sb = Utils.sbReplace(sb, "@2", Utils.uSpeed2String(settings.Unit, false));
		sb = Utils.sbReplace(sb, "@3", Utils.uTemperatureToString(settings.Unit));
		sb = Utils.sbReplace(sb, "@4", Utils.uTemperatureToString(settings.Unit));

		for (int totalForecasts = 0; totalForecasts < 3; ++totalForecasts) {
			previousWeatherData
					.RetrieveWeatherData(Instant.ofEpochMilli(StartTime.minusYears(totalForecasts + 1).getMillis()));

			int index = 600 + totalForecasts * 100;

			sb = Utils.sbReplace(sb, "@" + index++, fmt.print(previousWeatherData.getDailyWeatherData().getDate()));
			sb = Utils.sbReplace(sb, "@" + index++, previousWeatherData.getDailyWeatherData().getSummary());
			sb = Utils.sbReplace(sb, "@" + index++, previousWeatherData.getDailyWeatherData().getMoonPhase());
			sb = Utils.sbReplace(sb, "@" + index++, Utils.FormatTemperature(
					Double.valueOf(previousWeatherData.getDailyWeatherData().getTemperatureHigh()), settings.Unit));
			sb = Utils.sbReplace(sb, "@" + index++, Utils.FormatTemperature(
					Double.valueOf(previousWeatherData.getDailyWeatherData().getTemperatureLow()), settings.Unit));
			sb = Utils.sbReplace(sb, "@" + index++,
					Utils.FormatSpeed(Double.valueOf(previousWeatherData.getDailyWeatherData().getWindSpeed()),
							settings.Unit, false, false));
			sb = Utils.sbReplace(sb, "@" + index++, Utils.FormatTemperature(
					Double.valueOf(previousWeatherData.getDailyWeatherData().getTemperatureMin()), settings.Unit));
			sb = Utils.sbReplace(sb, "@" + index++, Utils.FormatTemperature(
					Double.valueOf(previousWeatherData.getDailyWeatherData().getTemperatureMax()), settings.Unit));
		}

		// -- Refresh the view and set the cursor position
		editorStat.setText(sb.toString());
		editorStat.setCaretPosition(0);
	}


	public void SetParameters(Double latitude, Double longitude, DateTime startTime) {
		Latitude = latitude;
		Longitude = longitude;
		StartTime = startTime;
	}
}
