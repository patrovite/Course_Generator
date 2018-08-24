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

package course_generator.resume_table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import course_generator.CgResume;
import course_generator.settings.CgSettings;
import course_generator.utils.CgConst;

public class ResumeRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 7192299228959241742L;


	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		CgResume data = (CgResume) value;
		Double val = 0.0;
		String str;

		CgSettings settings = ((ResumeModel) table.getModel()).getSettings();

		setIcon(null);

		if (data == null)
			return this;

		// Depending of the column number return the rendered label
		switch (column) {
		case 0: // N°
			val = data.getNum();

			// -- Display the value
			setText(String.format("%1.0f ", val));
			setHorizontalAlignment(CENTER);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(row % 2 == 0 ? CgConst.CL_RESUME_ALTERNATE_LINE : Color.WHITE);
			
			return this;

		case 1: // Name
			str = data.getName();

			// -- Display the value
			setText(str);
			setHorizontalAlignment(LEFT);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(row % 2 == 0 ? CgConst.CL_RESUME_ALTERNATE_LINE : Color.WHITE);

			return this;

		case 2: // Line
			val = data.getLine();

			// -- Display the value
			setText(String.format("%1.0f ", val));
			setHorizontalAlignment(CENTER);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(row % 2 == 0 ? CgConst.CL_RESUME_ALTERNATE_LINE : Color.WHITE);

			return this;

		case 3: // Elevation
			setText(data.getElevationString(settings.Unit, false));
			setHorizontalAlignment(CENTER);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(row % 2 == 0 ? CgConst.CL_RESUME_ALTERNATE_LINE : Color.WHITE);

			return this;

		case 4: // Positive climb
			setText(data.getClimbPString(settings.Unit, false));
			setHorizontalAlignment(CENTER);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(row % 2 == 0 ? CgConst.CL_RESUME_ALTERNATE_LINE : Color.WHITE);

			return this;

		case 5: // Negative climb
			setText(data.getClimbMString(settings.Unit, false));
			setHorizontalAlignment(CENTER);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(row % 2 == 0 ? CgConst.CL_RESUME_ALTERNATE_LINE : Color.WHITE);

			return this;

		case 6: // Distance
			setText(data.getDistString(settings.Unit, false));
			setHorizontalAlignment(CENTER);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(row % 2 == 0 ? CgConst.CL_RESUME_ALTERNATE_LINE : Color.WHITE);

			return this;

		case 7: // Time
			setText(data.getTimeString());
			setHorizontalAlignment(CENTER);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(row % 2 == 0 ? CgConst.CL_RESUME_ALTERNATE_LINE : Color.WHITE);

			return this;

		case 8: // Hour
			setText(data.getHourString());
			setHorizontalAlignment(CENTER);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(row % 2 == 0 ? CgConst.CL_RESUME_ALTERNATE_LINE : Color.WHITE);

			return this;

		case 9: // dt Time
			setText(data.getdTime_f_String());
			setHorizontalAlignment(CENTER);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(row % 2 == 0 ? CgConst.CL_RESUME_ALTERNATE_LINE : Color.WHITE);

			return this;

		case 10: // Time limit
			setText(data.getTimeLimitString());
			setHorizontalAlignment(CENTER);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(row % 2 == 0 ? CgConst.CL_RESUME_ALTERNATE_LINE : Color.WHITE);

			return this;

		case 11: // Station Time
			setText(data.getStationTimeString());
			setHorizontalAlignment(CENTER);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(row % 2 == 0 ? CgConst.CL_RESUME_ALTERNATE_LINE : Color.WHITE);

			return this;

		case 12: // dt Distance
			setText(data.getdDistString(settings.Unit, false));
			setHorizontalAlignment(CENTER);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(row % 2 == 0 ? CgConst.CL_RESUME_ALTERNATE_LINE : Color.WHITE);

			return this;

		case 13: // dt positive climb
			setText(data.getdClimbPString(settings.Unit, false));
			setHorizontalAlignment(CENTER);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(row % 2 == 0 ? CgConst.CL_RESUME_ALTERNATE_LINE : Color.WHITE);

			return this;

		case 14: // dt negative climb
			setText(data.getdClimbMString(settings.Unit, false));
			setHorizontalAlignment(CENTER);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(row % 2 == 0 ? CgConst.CL_RESUME_ALTERNATE_LINE : Color.WHITE);

			return this;

		case 15: // Ascend speed
			setText(data.getSpeedPString(settings.Unit, false));
			setHorizontalAlignment(CENTER);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(row % 2 == 0 ? CgConst.CL_RESUME_ALTERNATE_LINE : Color.WHITE);

			return this;

		case 16: // Descend speed
			setText(data.getSpeedMString(settings.Unit, false));
			setHorizontalAlignment(CENTER);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(row % 2 == 0 ? CgConst.CL_RESUME_ALTERNATE_LINE : Color.WHITE);

			return this;

		case 17: // Average ascend slope
			setText(data.getAvgSlopePString());
			setHorizontalAlignment(CENTER);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(row % 2 == 0 ? CgConst.CL_RESUME_ALTERNATE_LINE : Color.WHITE);

			return this;

		case 18: // Average descend slope
			setText(data.getAvgSlopeMString());
			setHorizontalAlignment(CENTER);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(row % 2 == 0 ? CgConst.CL_RESUME_ALTERNATE_LINE : Color.WHITE);

			return this;

		case 19: // Average speed
			setText(data.getAvgSpeedString(settings.Unit, false, settings.isPace));
			setHorizontalAlignment(CENTER);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(row % 2 == 0 ? CgConst.CL_RESUME_ALTERNATE_LINE : Color.WHITE);

			return this;

		case 20: // Comment
			str = data.getComment();

			// -- Display the value
			setText(str); // TODO Delete /n
			setHorizontalAlignment(LEFT);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(row % 2 == 0 ? CgConst.CL_RESUME_ALTERNATE_LINE : Color.WHITE);

			return this;
		} // switch

		return this;
	}
}