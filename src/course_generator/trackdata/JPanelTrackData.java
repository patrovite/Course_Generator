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

package course_generator.trackdata;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Crosshair;
import org.jfree.data.xy.XYSeriesCollection;

import course_generator.TrackData;
import course_generator.dialogs.frmEditPosition;
import course_generator.settings.CgSettings;
import course_generator.trackdata_table.MainHeaderRenderer;
import course_generator.trackdata_table.TrackDataClass;
import course_generator.trackdata_table.TrackDataModel;
import course_generator.trackdata_table.TrackDataRenderer;

public class JPanelTrackData extends JPanel {
	private TrackData track = null;
	private CgSettings settings = null;
	private List<JPanelTrackDataListener> listeners = new ArrayList<JPanelTrackDataListener>();
	private JTable TableMain;
	private final TrackDataModel ModelTableMain;
	private JScrollPane jScrollPaneData;
	private int selectedRow = -1;
	private int old_row = -2;


	public JPanelTrackData(TrackDataModel model, CgSettings settings) {
		super();
		this.settings = settings;
		this.ModelTableMain = model;
		initComponents();
	}


	public void addListener(JPanelTrackDataListener toAdd) {
		listeners.add(toAdd);
	}


	public void notifyDoubleClick() {
		for (JPanelTrackDataListener hl : listeners)
			hl.doubleClickEvent();
	}


	public void notifySimpleClick() {
		for (JPanelTrackDataListener hl : listeners)
			hl.simpleClickEvent();
	}


	public void notifyReleaseKey() {
		for (JPanelTrackDataListener hl : listeners)
			hl.keyRealeasedEvent();
	}


	private void initComponents() {
		setPreferredSize(new java.awt.Dimension(677, 150));
		setLayout(new java.awt.BorderLayout());

		TableMain = new javax.swing.JTable();
		TableMain.setModel(ModelTableMain);
		TableMain.getTableHeader()
				.setDefaultRenderer(new MainHeaderRenderer(TableMain.getTableHeader().getDefaultRenderer()));
		TableMain.getTableHeader().setReorderingAllowed(false);

		TableMain.setDefaultRenderer(TrackDataClass.class, new TrackDataRenderer());

		TableMain.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		TableMain.setRowHeight(20);
		TableMain.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() >= 2 && !evt.isConsumed()) {
					evt.consume();
					selectedRow = TableMain.rowAtPoint(evt.getPoint());
					notifyDoubleClick();
				} else
					TableMainMouseClicked(evt);
			}
		});
		TableMain.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				TableMainKeyReleased(evt);
			}
		});

		// -- Add the grid to a scroll panel
		// ------------------------------------
		jScrollPaneData = new javax.swing.JScrollPane();
		jScrollPaneData.setViewportView(TableMain);

		add(jScrollPaneData, java.awt.BorderLayout.CENTER);
	}


	private void TableMainMouseClicked(java.awt.event.MouseEvent evt) {
		selectedRow = TableMain.rowAtPoint(evt.getPoint());
		if ((selectedRow < 0) || (selectedRow == old_row))
			return;

		old_row = selectedRow;
		notifySimpleClick();
	}


	private void TableMainKeyReleased(java.awt.event.KeyEvent evt) {
		selectedRow = TableMain.getSelectedRow();
		if ((selectedRow < 0) || (selectedRow == old_row))
			return;

		old_row = selectedRow;
		notifyReleaseKey();
	}


	public void setTrack(TrackData track) {
		this.track = track;
		old_row = -1;
		refresh();
	}


	/**
	 * Refresh the content of the data table
	 */
	public void refresh() {
		int r = TableMain.getSelectedRow();
		ModelTableMain.fireTableDataChanged();
		if (r >= 0)
			TableMain.setRowSelectionInterval(r, r);
	}


	public int getSelectedRow() {
//		return selectedRow;
		return TableMain.getSelectedRow();
	}


	public int getSelectedRowCount() {
		return TableMain.getSelectedRowCount();
	}


	public void setColumnWidth(){
		// -- Set the preferred column width
		for (int i = 0; i < 15; i++) {
			TableMain.getColumnModel().getColumn(i).setPreferredWidth(settings.TableMainColWidth[i]);
		}
		refresh();
	}
	
	public void updateColumnWidth() {
		// -- Update the column width in the settings
		for (int i = 0; i < 16; i++)
			settings.TableMainColWidth[i] = TableMain.getColumnModel().getColumn(i).getWidth();
	}
	
	public void setSelectedRow(int row) {
		TableMain.setRowSelectionInterval(row, row);
		Rectangle rect = TableMain.getCellRect(row, 0, true);
		TableMain.scrollRectToVisible(rect);
	}

}
