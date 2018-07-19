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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import course_generator.settings.CgSettings;
import course_generator.utils.CgConst;
import course_generator.utils.Utils;

public class ImportPtsRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -7441702492576267791L;


	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		CgImportPts data = null;

		CgSettings settings = ((ImportPtsDataModel) table.getModel()).getSettings();
		setIcon(null);

		data = (CgImportPts) value;

		// Depending of the column number return the rendered label
		switch (column) {
		// case 0:
		// JCheckBox b=new JCheckBox();
		// b.setSelected(data.getSel());
		// return b;
		case 0: // Selection
			// -- Set the value
			if (data.getSel())
				setText("X");
			else
				setText("");

			setHorizontalAlignment(CENTER);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(Color.WHITE);

			return this;
		case 1: // Line
			// -- Set the value
			setText(data.getLineString());
			setHorizontalAlignment(CENTER);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(Color.WHITE);

			return this;

		case 2: // Distance
			setText(data.getDistString(settings.Unit, false));
			setHorizontalAlignment(CENTER);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else {
				double dist = data.getDist();
				if (dist <= settings.DistNear)
					setBackground(Color.GREEN);
				else if (dist <= settings.DistFar)
					setBackground(Color.ORANGE);
				else
					setBackground(Color.RED);
			}

			return this;

		case 3: // Latitude
			// -- Set the value
			setText(data.getLatitudeString());
			setHorizontalAlignment(CENTER);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(new Color(255, 242, 193));

			return this;

		case 4: // Longitude
			// -- Set the value
			setText(data.getLongitudeString());
			setHorizontalAlignment(CENTER);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(new Color(255, 242, 193));

			return this;

		case 5: // Elevation
			setText(data.getElevationString(settings.Unit, false));
			setHorizontalAlignment(CENTER);

			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(Color.WHITE);

			return this;

		case 6: // Tags
			int tag = data.getTag();

			// -- Count the number of active tag
			int cmpt = 0;
			int i = 1;
			while (i < 65536) {
				if ((tag & i) != 0)
					cmpt++;
				i = i * 2;
			}

			int x = 0; // X position in the resulting image
			int xoffset = settings.TagIconSize + 2;

			if (cmpt > 0) {
				// -- Prepare the resulting image
				BufferedImage combined = new BufferedImage(xoffset * cmpt, settings.TagIconSize,
						BufferedImage.TYPE_INT_ARGB);
				Graphics g = combined.getGraphics();

				// Higher point
				if ((tag & CgConst.TAG_HIGH_PT) != 0) {
					ImageIcon image = Utils.getIcon(this, "high_point.png", settings.TagIconSize);
					g.drawImage(image.getImage(), x, 0, null);
					x += xoffset;
				}

				// Lower point
				if ((tag & CgConst.TAG_LOW_PT) != 0) {
					ImageIcon image = Utils.getIcon(this, "low_point.png", settings.TagIconSize);
					g.drawImage(image.getImage(), x, 0, null);
					x += xoffset;
				}

				// Station
				if ((tag & CgConst.TAG_EAT_PT) != 0) {
					ImageIcon image = Utils.getIcon(this, "eat.png", settings.TagIconSize);
					g.drawImage(image.getImage(), x, 0, null);
					x += xoffset;
				}

				// Drink
				if ((tag & CgConst.TAG_WATER_PT) != 0) {
					ImageIcon image = Utils.getIcon(this, "drink.png", settings.TagIconSize);
					g.drawImage(image.getImage(), x, 0, null);
					x += xoffset;
				}

				// Mark
				if ((tag & CgConst.TAG_MARK) != 0) {
					ImageIcon image = Utils.getIcon(this, "flag.png", settings.TagIconSize);
					g.drawImage(image.getImage(), x, 0, null);
					x += xoffset;
				}

				// Roadbook
				if ((tag & CgConst.TAG_ROADBOOK) != 0) {
					ImageIcon image = Utils.getIcon(this, "roadbook.png", settings.TagIconSize);
					g.drawImage(image.getImage(), x, 0, null);
					x += xoffset;
				}

				// Photo
				if ((tag & CgConst.TAG_COOL_PT) != 0) {
					ImageIcon image = Utils.getIcon(this, "photo.png", settings.TagIconSize);
					g.drawImage(image.getImage(), x, 0, null);
					x += xoffset;
				}

				// Note
				if ((tag & CgConst.TAG_NOTE) != 0) {
					ImageIcon image = Utils.getIcon(this, "note.png", settings.TagIconSize);
					g.drawImage(image.getImage(), x, 0, null);
					x += xoffset;
				}

				// Info
				if ((tag & CgConst.TAG_INFO) != 0) {
					ImageIcon image = Utils.getIcon(this, "info.png", settings.TagIconSize);
					g.drawImage(image.getImage(), x, 0, null);
					x += xoffset;
				}

				setIcon(new ImageIcon(combined));
			} else
				setIcon(null); // No image

			// -- Set the value
			setText("");
			setHorizontalAlignment(LEFT);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(new Color(255, 255, 255));

			return this;

		case 7: // Name
			// -- Set the value
			setText(data.getName());
			setHorizontalAlignment(LEFT);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(new Color(255, 255, 255));

			return this;

		case 8: // Comment
			// -- Set the value
			setText(data.getComment());
			setHorizontalAlignment(LEFT);

			// -- Set the background
			if (isSelected)
				setBackground(CgConst.CL_LINE_SELECTION);
			else
				setBackground(new Color(255, 255, 255));

			return this;

		} // switch

		return this;
	}

}
