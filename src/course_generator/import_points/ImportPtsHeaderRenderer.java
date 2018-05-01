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

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ImportPtsHeaderRenderer implements TableCellRenderer {
	private final JLabel lbl;
	private final TableCellRenderer wrappedRenderer;


	public ImportPtsHeaderRenderer(TableCellRenderer wrappedRenderer) {
		if (!(wrappedRenderer instanceof JLabel)) {
			throw new IllegalArgumentException("The supplied renderer must inherit from JLabel");
		}

		this.wrappedRenderer = wrappedRenderer;
		this.lbl = (JLabel) wrappedRenderer;
	}


	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int col) {
		if (!(wrappedRenderer instanceof JLabel)) {
			throw new IllegalArgumentException("The supplied renderer must inherit from JLabel");
		}
		wrappedRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
		lbl.setHorizontalAlignment(JLabel.CENTER);

		return lbl;
	}

}