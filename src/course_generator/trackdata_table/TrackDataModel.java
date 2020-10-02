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

package course_generator.trackdata_table;

import javax.swing.table.AbstractTableModel;

import course_generator.TrackData;
import course_generator.settings.CgSettings;

/**
 *
 * @author pierre.delore
 */
public class TrackDataModel extends AbstractTableModel {

	private static final long serialVersionUID = -1530666127472331439L;
	private final String header[];
	private TrackData track;
	private CgSettings settings;

	public TrackDataModel(TrackData data, CgSettings _settings) {
		super();

		header = new String[16];
		SetTexts();
		settings = _settings;
		track = data; // TrackData.getInstance();
	}

	public void SetTexts() {
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");

		header[0] = bundle.getString("frmMain.HeaderNum.text");
		header[1] = bundle.getString("frmMain.HeaderLat.text");
		header[2] = bundle.getString("frmMain.HeaderLon.text");
		header[3] = bundle.getString("frmMain.HeaderElev.text");
		header[4] = bundle.getString("frmMain.HeaderTag.text");
		header[5] = bundle.getString("frmMain.HeaderDist.text");
		header[6] = bundle.getString("frmMain.HeaderTotal.text");
		header[7] = bundle.getString("frmMain.HeaderDiff.text");
		header[8] = bundle.getString("frmMain.HeaderCoeff.text");
		header[9] = bundle.getString("frmMain.HeaderRecovery.text");
		header[10] = bundle.getString("frmMain.HeaderTime.text");
		header[11] = bundle.getString("frmMain.HeaderTimeLimit.text");
		header[12] = bundle.getString("frmMain.HeaderHour.text");
		header[13] = bundle.getString("frmMain.HeaderStation.text");
		header[14] = bundle.getString("frmMain.HeaderName.text");
		header[15] = bundle.getString("frmMain.HeaderComment.text");
	}

	public CgSettings getSettings() {
		return settings;
	}

	@Override
	public int getColumnCount() {
		return header.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return header[columnIndex];
	}

	@Override
	public int getRowCount() {
		return track.data.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0: // N°
		case 1: // Lat
		case 2: // Lon
		case 3: // Elev
		case 4: // Tag
		case 5: // Dist (m)
		case 6: // Total (km)
		case 7: // Diff
		case 8: // Coeff
		case 9: // Recovery
		case 10: // Time
		case 11: // Timelimit
		case 13: // Station
		case 14: // Name
		case 15: // Comment
			return track.data.get(rowIndex);
		case 12: // Hour
			// This is normal to return the complete track object
			// because we need several info for the rendering
			return track;
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return TrackDataClass.class;
	}

} // Class
