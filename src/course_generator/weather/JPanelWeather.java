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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import course_generator.settings.CgSettings;
import course_generator.utils.CgLog;
import course_generator.utils.Utils;
import course_generator.utils.WeatherData;

public class JPanelWeather extends JPanel {
	private static final long serialVersionUID = -7168142806619093218L;
	private ResourceBundle bundle;
	private CgSettings settings = null;
	private JEditorPane editorStat;
	private JScrollPane scrollPaneStat;
	private JToolBar toolBar;
	private JButton btStatisticSave;
	private JButton btStatisticRefresh;

	private Double Latitude;
	private Double Longitude;
	private Instant StartTime;


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
		btStatisticSave = new javax.swing.JButton();
		btStatisticSave.setIcon(Utils.getIcon(this, "save_html.png", settings.ToolbarIconSize));
		btStatisticSave.setToolTipText(bundle.getString("JPanelStastistics.btStatisticSave.toolTipText"));
		btStatisticSave.setFocusable(false);
		btStatisticSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// SaveStat();
			}
		});
		toolBar.add(btStatisticSave);

		// -- Separator
		// ---------------------------------------------------------
		toolBar.add(new javax.swing.JToolBar.Separator());

		// -- Refresh
		// --------------------------------------------------------------
		btStatisticRefresh = new javax.swing.JButton();
		btStatisticRefresh.setIcon(Utils.getIcon(this, "refresh.png", settings.ToolbarIconSize));
		btStatisticRefresh.setToolTipText(bundle.getString("JPanelStastistics.btStatisticRefresh.toolTipText"));
		btStatisticRefresh.setFocusable(false);
		btStatisticRefresh.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				refresh();
			}
		});
		toolBar.add(btStatisticRefresh);
	}


	/**
	 * Refresh the statistic tab
	 */
	public void refresh() {
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

		WeatherData previousWeatherData = new WeatherData(settings);
		previousWeatherData.RetrieveWeatherData(Latitude, Longitude, StartTime);
		int index = 600;
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

		sb = Utils.sbReplace(sb, "@" + index++, fmt.print(previousWeatherData.getDate()));
		sb = Utils.sbReplace(sb, "@" + index++, previousWeatherData.getSummary());
		sb = Utils.sbReplace(sb, "@" + index++, previousWeatherData.getMoonPhase());
		sb = Utils.sbReplace(sb, "@" + index++, previousWeatherData.getTemperatureHigh());
		sb = Utils.sbReplace(sb, "@" + index++, previousWeatherData.getTemperatureLow());
		sb = Utils.sbReplace(sb, "@" + index++, previousWeatherData.getWindSpeed());
		sb = Utils.sbReplace(sb, "@" + index++, previousWeatherData.getTemperatureMin());
		sb = Utils.sbReplace(sb, "@" + index++, previousWeatherData.getTemperatureMax());

		// -- Refresh the view and set the cursor position
		editorStat.setText(sb.toString());
		editorStat.setCaretPosition(0);
	}


	public void SetParameters(Double latitude, Double longitude, DateTime time) {
		Latitude = latitude;
		Longitude = longitude;
		StartTime = Instant.ofEpochMilli(time.getMillis());
	}
}
