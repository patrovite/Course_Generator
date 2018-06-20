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

package course_generator.resume;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import course_generator.CgData;
import course_generator.CgResume;
import course_generator.ResumeData;
import course_generator.TrackData;
import course_generator.TrackData.CalcAvrSlopeResult;
import course_generator.TrackData.CalcAvrSpeedResult;
import course_generator.TrackData.CalcClimbResult;
import course_generator.resume_table.ResumeClass;
import course_generator.resume_table.ResumeHeaderRenderer;
import course_generator.resume_table.ResumeModel;
import course_generator.resume_table.ResumeRenderer;
import course_generator.settings.CgSettings;
import course_generator.trackdata.JPanelTrackDataListener;
import course_generator.utils.CgConst;
import course_generator.utils.Utils;

public class JPanelResume extends JPanel {
	private int old_row_resume = -1;
	private int selectedRow = -1;
	private TrackData Track = null;
	private final ResumeModel ModelTableResume;
	private JTable TableResume;
	private JScrollPane jScrollPaneResume;
	private ResourceBundle bundle;
	private ResumeData Resume;
	private CgSettings Settings;
	private JToolBar ToolBarResume;
	private JButton btResumeSave;
	private JButton btRefreshRefresh;
	private List<JPanelResumeListener> listeners = new ArrayList<JPanelResumeListener>();


	public JPanelResume(ResumeData resume, CgSettings settings) {
		super();
		Resume = resume;
		Settings = settings;
		ModelTableResume = new ResumeModel(Resume, Settings);
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		initComponents();
	}


	public void addListener(JPanelResumeListener toAdd) {
		listeners.add(toAdd);
	}


	public void notifyLineChange() {
		for (JPanelResumeListener hl : listeners)
			hl.lineChangeEvent();
	}

	public void notifyDoubleClick() {
		for (JPanelResumeListener hl : listeners)
			hl.doubleClickEvent();
	}

	private void initComponents() {
		setLayout(new java.awt.BorderLayout());

		// -- Resume tool bar
		// ---------------------------------------------------
		Create_Resume_Toolbar();
		add(ToolBarResume, java.awt.BorderLayout.NORTH);

		TableResume = new javax.swing.JTable();
		TableResume.setModel(ModelTableResume);
		TableResume.setRowHeight(20);
		TableResume.getTableHeader().setReorderingAllowed(false);

		TableResume.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);

		TableResume.getTableHeader()
				.setDefaultRenderer(new ResumeHeaderRenderer(TableResume.getTableHeader().getDefaultRenderer()));

		TableResume.setDefaultRenderer(ResumeClass.class, new ResumeRenderer());

		TableResume.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {

				if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() >= 2 && !evt.isConsumed()) {
					evt.consume();
					selectedRow = TableResume.rowAtPoint(evt.getPoint());
					notifyDoubleClick();
				} else
					TableResumeMouseClicked(evt);
			}
		});
		TableResume.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				TableResumeKeyReleased(evt);
			}
		});

		// -- Add the grid to a scroll panel
		// ------------------------------------
		jScrollPaneResume = new javax.swing.JScrollPane();
		jScrollPaneResume.setViewportView(TableResume);
		add(jScrollPaneResume, java.awt.BorderLayout.CENTER);
	}


	/**
	 * Create resume toolbar
	 */
	private void Create_Resume_Toolbar() {
		ToolBarResume = new javax.swing.JToolBar();
		ToolBarResume.setOrientation(javax.swing.SwingConstants.HORIZONTAL);
		ToolBarResume.setFloatable(false);
		ToolBarResume.setRollover(true);

		// -- Save
		// --------------------------------------------------------------
		btResumeSave = new javax.swing.JButton();
		btResumeSave.setIcon(Utils.getIcon(this, "save_csv.png", Settings.ToolbarIconSize));
		btResumeSave.setToolTipText(bundle.getString("JPanelResume.btResumeSave.toolTipText"));
		btResumeSave.setFocusable(false);
		btResumeSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SaveResumeAsCSV();
			}
		});
		ToolBarResume.add(btResumeSave);

		// -- Separator
		// ---------------------------------------------------------
		ToolBarResume.add(new javax.swing.JToolBar.Separator());

		// -- Refresh
		// --------------------------------------------------------------
		btRefreshRefresh = new javax.swing.JButton();
		btRefreshRefresh.setIcon(Utils.getIcon(this, "refresh.png", Settings.ToolbarIconSize));
		btRefreshRefresh.setToolTipText(bundle.getString("JPanelResume.btRefreshRefresh.toolTipText"));
		btRefreshRefresh.setFocusable(false);
		btRefreshRefresh.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				refresh();
			}
		});
		ToolBarResume.add(btRefreshRefresh);

	}


	private void TableResumeKeyReleased(KeyEvent evt) {
		int row = TableResume.getSelectedRow();
		int col = TableResume.getSelectedColumn();
		if ((row < 0) || (col < 0) || (row == old_row_resume))
			return;
		old_row_resume = row;
		notifyLineChange();
		// SelectPositionFromResume(row);
	}


	private void TableResumeMouseClicked(MouseEvent evt) {
		int row = TableResume.rowAtPoint(evt.getPoint());
		int col = TableResume.columnAtPoint(evt.getPoint());
		if ((row < 0) || (col < 0) || (row == old_row_resume))
			return;

		old_row_resume = row;
		notifyLineChange();
		// SelectPositionFromResume(row);
	}
		

	public int getSelectedLine() {
		return TableResume.getSelectedRow();
	}

	public int getDataTrackLine() {
		int p=TableResume.getSelectedRow();
		return (int) Resume.data.get(p).getLine()-1;
	}

	// private void SelectPositionFromResume(int row) {
	// if (Resume.data.size() > 0) {
	// int r=(int)(Resume.data.get(row).getLine())-1;
	// //-- Set table main selection
	// TableMain.setRowSelectionInterval(r, r);
	// TableMain.scrollRectToVisible(new Rectangle(TableMain.getCellRect(r, 0,
	// true)));
	// // -- Refresh marker position on the map
	// RefreshCurrentPosMarker(Track.data.get(r).getLatitude(),
	// Track.data.get(r).getLongitude());
	// // -- Refresh profil crooshair position and profil info
	// RefreshProfilInfo(r);
	// xCrosshair.setValue(Track.data.get(r).getTotal(Settings.Unit) / 1000.0);
	// yCrosshair.setValue(Track.data.get(r).getElevation(Settings.Unit));
	// }
	// }

	/**
	 * Refresh the content of the resume table
	 */
	public void RefreshTableResume() {
		int r = TableResume.getSelectedRow();
		ModelTableResume.fireTableDataChanged();
		if (r >= 0)
			TableResume.setRowSelectionInterval(r, r);
	}


	private void SaveResumeAsCSV() {
		if (Resume.data.size() > 0) {
			String s;
			s = Utils.SaveDialog(this, Settings.LastDir, "", ".csv", bundle.getString("frmMain.CSVFile"), true,
					bundle.getString("frmMain.FileExist"));

			if (!s.isEmpty()) {
				Resume.SaveAsCSV(s, Settings.Unit, Settings.isPace);

				// -- Store the directory
				Settings.LastDir = Utils.GetDirFromFilename(s);
			}
		}
	}


	/**
	 * Refresh the resume grid
	 * 
	 * @param force
	 * 
	 */
	public void refresh() {
		// Exit if the tab is not displayed
		// if (TabbedPaneMain.getSelectedIndex() != 4) // Resume
		// return;

		// StatusBar.Items["Message"].Visible = true;
		// StatusBar.Items["Message"].Text = "Mise à jour resumé en cours...";
		// StatusBar.Refresh();

		int i = 0;
		int k = 0;
		int old = 0;
		CgData OldData;

		if (Track == null)
			return;
		if (Track.data.isEmpty())
			return;

		CalcClimbResult ccr = new CalcClimbResult();
		CalcAvrSlopeResult casr = new CalcAvrSlopeResult();
		CalcAvrSpeedResult speedResult = new CalcAvrSpeedResult();

		Resume.data.clear();

		OldData = Track.data.get(0);

		for (CgData src : Track.data) {
			if ((src.getTag() & 32) != 0) {
				k++;
				CgResume dst = new CgResume();
				// ResGrid.Rows.Add();

				dst.setNum(k);
				dst.setName(src.getName());
				dst.setLine(src.getNum());
				dst.setElevation(src.getElevation(CgConst.UNIT_METER));

				ccr = Track.CalcClimb(0, i, ccr);
				dst.setClimbP(ccr.cp);
				dst.setClimbM(ccr.cm);

				dst.setDist(src.getTotal(CgConst.UNIT_METER) / 1000.0);
				dst.setTime(src.getTime());
				dst.setHour(src.getHour());

				dst.setTimeLimit(src.getTimeLimit());
				dst.setStationTime(src.getStation());

				dst.setdTime_f(src.getTime() - OldData.getTime());
				dst.setdDist((src.getTotal(CgConst.UNIT_METER) - OldData.getTotal(CgConst.UNIT_METER)) / 1000.0);

				ccr = Track.CalcClimb(old, i, ccr);
				casr = Track.CalcAvrSlope(old, i, casr);
				speedResult = Track.CalcAvrSpeed(old, i, speedResult);

				dst.setdClimbP(ccr.cp);
				dst.setdClimbM(ccr.cm);

				dst.setSpeedP(ccr.cp * 3600 / Math.abs(ccr.tp));
				dst.setSpeedM(ccr.cm * 3600 / Math.abs(ccr.tm));

				// if ((AvrSlopeP == 0) || (Double.IsNaN(AvrSlopeP) ))
				dst.setAvgSlopeP(casr.AvrSlopeP);
				dst.setAvgSlopeM(casr.AvrSlopeM);

				dst.setAvgSpeed(Double.valueOf(speedResult.getAvrspeed(CgConst.UNIT_METER, false)));

				dst.setComment(src.getComment());

				Resume.data.add(dst);

				OldData = src;
				old = i;
			}
			i++;
		}
		// -- Refresh the grid
		TableResume.invalidate();
	}


	public void setTrack(TrackData track) {
		Track = track;
		refresh();
	}

}
