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

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author pierre.delore
 */
public class MainHeaderRenderer implements TableCellRenderer {
	private final JLabel lbl;
	private final TableCellRenderer wrappedRenderer;
	private final String tooltips[];


	public MainHeaderRenderer(TableCellRenderer wrappedRenderer) {
		if (!(wrappedRenderer instanceof JLabel)) {
			throw new IllegalArgumentException("The supplied renderer must inherit from JLabel");
		}

		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		tooltips = new String[16];
		tooltips[0] = bundle.getString("frmMain.HeaderNum.tooltips");
		tooltips[1] = bundle.getString("frmMain.HeaderLat.tooltips");
		tooltips[2] = bundle.getString("frmMain.HeaderLon.tooltips");
		tooltips[3] = bundle.getString("frmMain.HeaderElev.tooltips");
		tooltips[4] = bundle.getString("frmMain.HeaderTag.tooltips");
		tooltips[5] = bundle.getString("frmMain.HeaderDist.tooltips");
		tooltips[6] = bundle.getString("frmMain.HeaderTotal.tooltips");
		tooltips[7] = bundle.getString("frmMain.HeaderDiff.tooltips");
		tooltips[8] = bundle.getString("frmMain.HeaderCoeff.tooltips");
		tooltips[9] = bundle.getString("frmMain.HeaderRecovery.tooltips");
		tooltips[10] = bundle.getString("frmMain.HeaderTime.tooltips");
		tooltips[11] = bundle.getString("frmMain.HeaderTimeLimit.tooltips");
		tooltips[12] = bundle.getString("frmMain.HeaderHour.tooltips");
		tooltips[13] = bundle.getString("frmMain.HeaderStation.tooltips");
		tooltips[14] = bundle.getString("frmMain.HeaderName.tooltips");
		tooltips[15] = bundle.getString("frmMain.HeaderComment.tooltips");

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

		lbl.setToolTipText(tooltips[col]);

		return lbl;
	}

}
