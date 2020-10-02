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

package course_generator.param;

import javax.swing.table.AbstractTableModel;

import course_generator.settings.CgSettings;
import course_generator.utils.CgConst;
import course_generator.utils.Utils;

public class ParamPointsModel extends AbstractTableModel {
	private static final long serialVersionUID = -972678605120520023L;
	private java.util.ResourceBundle bundle;
	private ParamData param;
	private CgSettings settings;

	private final String[] header; // = { "Slope", "Speed" };

	/**
	 * Constructor
	 */
	public ParamPointsModel(ParamData p) {
		param = p;
		this.settings = null;
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		header = new String[2];
		header[0] = bundle.getString("ParamPointsModel.slope");
		header[1] = bundle.getString("ParamPointsModel.speed");
	}

	public void setParam(ParamData p) {
		param = p;
	}

	public void setSettings(CgSettings settings) {
		this.settings = settings;
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
		return param.data.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {

		case 0:
			// Slope
			return param.data.get(rowIndex).getSlope();

		case 1:
			// Speed
			if (settings.Unit == CgConst.UNIT_MILES_FEET)
				return Utils.Km2Miles(param.data.get(rowIndex).getSpeedNumber());
			else
				return param.data.get(rowIndex).getSpeedNumber();

		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
		case 1:
			return Double.class;
		default:
			return Object.class;
		}
	}
}
