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

package course_generator.statistics;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import course_generator.TrackData;
import course_generator.TrackData.CalcAvrSlopeResult;
import course_generator.TrackData.CalcAvrSpeedResult;
import course_generator.settings.CgSettings;
import course_generator.utils.CgConst;
import course_generator.utils.CgLog;
import course_generator.utils.Utils;

public class JPanelStatistics extends JPanel {
	private ResourceBundle bundle;
	private TrackData track = null;
	private CgSettings settings = null;
	private JEditorPane editorStat;
	private JScrollPane scrollPaneStat;
	private JToolBar toolBar;
	private JButton btStatisticSave;
	private JButton btStatisticRefresh;


	public JPanelStatistics(CgSettings settings) {
		super();
		track = null;
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
				SaveStat();
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
		if (track == null)
			return;

		if (track.data.isEmpty())
			return;

		StringBuilder sb = new StringBuilder();
		int unit = settings.Unit;
		boolean isPace = settings.isPace;

		// -- Get current language
		String lang = Locale.getDefault().toString();

		InputStream is = getClass().getResourceAsStream("stattemplate_" + lang + ".html");
		// -- File exist?
		if (is == null) {
			/// -- Use default file
			is = getClass().getResourceAsStream("stattemplate_en_US.html");
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

		track.CalcStatElev();
		track.CalcStatSlope();
		track.CalcStatNight();

		CalcAvrSlopeResult casr = new CalcAvrSlopeResult();
		casr = track.CalcAvrSlope(0, track.data.size() - 1, casr);

		CalcAvrSpeedResult speedResult = new CalcAvrSpeedResult();
		speedResult = track.CalcAvrSpeed(0, track.data.size() - 1, speedResult);

		track.CalcRoad();

		sb = Utils.sbReplace(sb, "@500",
				String.format("%1.3f " + Utils.uLDist2String(unit), track.getTotalDistance(unit) / 1000));
		sb = Utils.sbReplace(sb, "@501", String.format("%1.0f " + Utils.uElev2String(unit), track.getClimbP(unit)));
		sb = Utils.sbReplace(sb, "@502", String.format("%1.0f " + Utils.uElev2String(unit), track.getClimbM(unit)));
		sb = Utils.sbReplace(sb, "@503", String.format("%1.0f " + Utils.uElev2String(unit), track.getMinElev(unit)));
		sb = Utils.sbReplace(sb, "@504", String.format("%1.0f " + Utils.uElev2String(unit), track.getMaxElev(unit)));
		double temp = ((track.getMaxElev(CgConst.UNIT_METER) - track.getMinElev(CgConst.UNIT_METER)) / 100) * 0.6;
		sb = Utils.sbReplace(sb, "@505", String.format("~%1.1f°C / ~%1.1f°F", temp, Utils.C2F(temp) - 32.0));
		sb = Utils.sbReplace(sb, "@506", String.format("%1.1f%%", casr.AvrSlopeP));
		sb = Utils.sbReplace(sb, "@507", String.format("%1.1f%%", casr.AvrSlopeM));
		sb = Utils.sbReplace(sb, "@508",
				String.format("%1.3f " + Utils.uLDist2String(unit), casr.getTotClimbP(unit) / 1000));
		sb = Utils.sbReplace(sb, "@509",
				String.format("%1.3f " + Utils.uLDist2String(unit), casr.getTotFlat(unit) / 1000));
		sb = Utils.sbReplace(sb, "@510",
				String.format("%1.3f " + Utils.uLDist2String(unit), casr.getTotClimbM(unit) / 1000));
		sb = Utils.sbReplace(sb, "@511",
				speedResult.getAvrspeed(unit, isPace) + " " + Utils.uSpeed2String(unit, isPace));

		double tmpdbl = (track.getDistRoad(unit) * 100 / track.getTotalDistance(unit));
		sb = Utils.sbReplace(sb, "@512",
				String.format("%1.0f%% / %1.3f " + Utils.uLDist2String(unit), tmpdbl, track.getDistRoad(unit) / 1000));
		sb = Utils.sbReplace(sb, "@513", String.format("%1.0f%% / %1.3f " + Utils.uLDist2String(unit), 100.0 - tmpdbl,
				(track.getTotalDistance(unit) - track.getDistRoad(unit)) / 1000));

		sb = Utils.sbReplace(sb, "@514", track.CourseName);
		sb = Utils.sbReplace(sb, "@515", track.Description);

		// -- Speed, distance and time vs slope
		for (int i = 1; i <= 13; i++)
			sb = Utils.sbReplace(sb, String.format("@%03d", i),
					CalcVMoy(track.StatSlope[i - 1].getDist(unit), track.StatSlope[i - 1].Time, unit));

		for (int i = 21; i <= 33; i++)
			sb = Utils.sbReplace(sb, String.format("@%03d", i),
					String.format("%1.3f " + Utils.uLDist2String(unit), track.StatSlope[i - 21].getDist(unit) / 1000)
							+ ' ' + String.format("(%1.1f%%)",
									track.StatSlope[i - 21].getDist(unit) / track.getTotalDistance(unit) * 100));

		for (int i = 41; i <= 53; i++) {
			int k = (int) track.StatSlope[i - 41].Time;
			sb = Utils.sbReplace(sb, String.format("@%03d", i), Utils.Second2DateString(k));
		}

		// -- Speed, distance and time vs elevation
		for (int i = 100; i <= 105; i++)
			sb = Utils.sbReplace(sb, String.format("@%03d", i),
					CalcVMoy(track.StatElev[i - 100].getDist(unit), track.StatElev[i - 100].Time, unit));

		for (int i = 110; i <= 115; i++)
			sb = Utils.sbReplace(sb, String.format("@%03d", i),
					String.format("%1.3f " + Utils.uLDist2String(unit), track.StatElev[i - 110].getDist(unit) / 1000)
							+ ' ' + String.format("(%1.1f%%)",
									track.StatElev[i - 110].getDist(unit) / track.getTotalDistance(unit) * 100));

		for (int i = 120; i <= 125; i++) {
			int k = (int) track.StatElev[i - 120].Time;
			sb = Utils.sbReplace(sb, String.format("@%03d", i), Utils.Second2DateString(k));
		}

		// -- Speed, distance and track time vs the elevation (day)
		for (int i = 200; i <= 205; i++)
			sb = Utils.sbReplace(sb, String.format("@%03d", i),
					CalcVMoy(track.StatElevDay[i - 200].getDist(unit), track.StatElevDay[i - 200].Time, unit));

		for (int i = 210; i <= 215; i++)
			sb = Utils.sbReplace(sb, String.format("@%03d", i),
					String.format("%1.3f " + Utils.uLDist2String(unit), track.StatElevDay[i - 210].getDist(unit) / 1000)
							+ ' ' + String.format("(%1.1f%%)",
									track.StatElevDay[i - 210].getDist(unit) / track.getTotalDistance(unit) * 100));

		for (int i = 220; i <= 225; i++) {
			int k = (int) track.StatElevDay[i - 220].Time;
			sb = Utils.sbReplace(sb, String.format("@%03d", i), Utils.Second2DateString(k));
		}

		// -- Speed, distance and track time vs the elevation (night)
		for (int i = 300; i <= 305; i++)
			sb = Utils.sbReplace(sb, String.format("@%03d", i),
					CalcVMoy(track.StatElevNight[i - 300].getDist(unit), track.StatElevNight[i - 300].Time, unit));

		for (int i = 310; i <= 315; i++)
			sb = Utils.sbReplace(sb, String.format("@%03d", i),
					String.format("%1.3f " + Utils.uLDist2String(unit),
							track.StatElevNight[i - 310].getDist(unit) / 1000) + ' '
							+ String.format("(%1.1f%%)",
									track.StatElevNight[i - 310].getDist(unit) / track.getTotalDistance(unit) * 100));

		for (int i = 320; i <= 325; i++) {
			int k = (int) track.StatElevNight[i - 320].Time;
			sb = Utils.sbReplace(sb, String.format("@%03d", i), Utils.Second2DateString(k));
		}

		// -- Speed, distance and track time during day and night
		sb = Utils.sbReplace(sb, "@400", CalcVMoy(track.tInDay.getDist(unit), track.tInDay.Time, unit));
		sb = Utils.sbReplace(sb, "@410",
				String.format("%1.3f " + Utils.uLDist2String(unit), track.tInDay.getDist(unit) / 1000) + ' '
						+ String.format("(%1.1f%%)", track.tInDay.getDist(unit) / track.getTotalDistance(unit) * 100));

		int k1 = (int) track.tInDay.Time;
		sb = Utils.sbReplace(sb, "@420", Utils.Second2DateString(k1));

		sb = Utils.sbReplace(sb, "@401", CalcVMoy(track.tInNight.getDist(unit), track.tInNight.Time, unit));
		sb = Utils.sbReplace(sb, "@411",
				String.format("%1.3f " + Utils.uLDist2String(unit), track.tInNight.getDist(unit) / 1000) + ' ' + String
						.format("(%1.1f%%)", track.tInNight.getDist(unit) / track.getTotalDistance(unit) * 100));

		k1 = (int) track.tInNight.Time;
		sb = Utils.sbReplace(sb, "@421", Utils.Second2DateString(k1));

		/*
		 * @900=1000m=3280feet
		 * 
		 * @901=1500m=4921feet
		 * 
		 * @902=2000m=6561feet
		 * 
		 * @903=2500m=8202feet
		 * 
		 * @904=3000m=9842feet
		 */
		if (unit == CgConst.UNIT_METER) {
			sb = Utils.sbReplace(sb, "@900", "1000m");
			sb = Utils.sbReplace(sb, "@901", "1500m");
			sb = Utils.sbReplace(sb, "@902", "2000m");
			sb = Utils.sbReplace(sb, "@903", "2500m");
			sb = Utils.sbReplace(sb, "@904", "3000m");
		} else {
			sb = Utils.sbReplace(sb, "@900", "3280 feet");
			sb = Utils.sbReplace(sb, "@901", "4921 feet");
			sb = Utils.sbReplace(sb, "@902", "6561 feet");
			sb = Utils.sbReplace(sb, "@903", "8202 feet");
			sb = Utils.sbReplace(sb, "@904", "9842 feet");
		}

		// -- Refresh the view and set the cursor position
		editorStat.setText(sb.toString());
		editorStat.setCaretPosition(0);
	}


	private String CalcVMoy(double d, double t, int unit) {
		if (t != 0) {
			return String.format("%1.1f " + Utils.uSpeed2String(unit, false), d / t * 3.6);
		} else
			return "0.0 " + Utils.uSpeed2String(unit, false);
	}


	/**
	 * Save the statistics in TXT format
	 */
	private void SaveStat() {
		String s;

		if (track == null)
			return;

		if (track.data.isEmpty())
			return;

		s = Utils.SaveDialog(this, settings.LastDir, "", ".html", bundle.getString("frmMain.HTMLFile"), true,
				bundle.getString("frmMain.FileExist"));

		if (!s.isEmpty()) {
			// -- Save the statistics
			// track.SaveCGX(s, 0, track.data.size() - 1);
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


	public void setTrack(TrackData track) {
		this.track = track;
		refresh();
	}


	public void setSettings(CgSettings settings) {
		this.settings = settings;
	}

}
