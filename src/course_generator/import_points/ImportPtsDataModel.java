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

package course_generator.import_points;

import javax.swing.table.AbstractTableModel;

import course_generator.settings.CgSettings;

public class ImportPtsDataModel extends AbstractTableModel {

	private static final long serialVersionUID = 298180821343433680L;
	private final String header[];
	private ImportPtsData importPts;
	private CgSettings settings;

	public ImportPtsDataModel(CgSettings settings, ImportPtsData datalist) {
		super();
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		importPts = datalist;

		header = new String[9];
		header[0] = bundle.getString("ImportsPts.HeaderSelection.text");
		header[1] = bundle.getString("ImportsPts.HeaderLine.text");
		header[2] = bundle.getString("ImportsPts.HeaderDist.text");
		header[3] = bundle.getString("ImportsPts.HeaderLat.text");
		header[4] = bundle.getString("ImportsPts.HeaderLon.text");
		header[5] = bundle.getString("ImportsPts.HeaderElev.text");
		header[6] = bundle.getString("ImportsPts.HeaderTag.text");
		header[7] = bundle.getString("ImportsPts.HeaderName.text");
		header[8] = bundle.getString("ImportsPts.HeaderComment.text");
		this.settings = settings;
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
		return importPts.data.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0: // Selection
		case 1: // Line
		case 2: // Dist
		case 3: // Lat
		case 4: // Lon
		case 5: // Elev
		case 6: // Tag
		case 7: // Name
		case 8: // Comment
			return importPts.data.get(rowIndex);
		default:
			throw new IllegalArgumentException();
		}
	}

	// @Override
	// public void setValueAt(Object aValue, int row, int column) {
	// if (aValue instanceof Boolean && column == 0) {
	// importPts.data.get(row).setSel(!(boolean)aValue);
	// fireTableCellUpdated(row, column);
	// }
	// }

	// @Override
	// public boolean isCellEditable(int row, int column) {
	// return column == 0;
	// }

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		// if (columnIndex==0) {
		// return boolean.class;
		// }
		// else
		return ImportPtsDataClass.class;
	}
}
