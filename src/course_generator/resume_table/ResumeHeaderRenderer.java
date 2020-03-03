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

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ResumeHeaderRenderer implements TableCellRenderer {
	private final JLabel lbl;
	private final TableCellRenderer wrappedRenderer;
	private final String tooltips[];

	public ResumeHeaderRenderer(TableCellRenderer wrappedRenderer) {
		if (!(wrappedRenderer instanceof JLabel)) {
			throw new IllegalArgumentException("The supplied renderer must inherit from JLabel");
		}

		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		tooltips = new String[21];
		tooltips[0] = bundle.getString("frmMain.ResumeHeaderNum.tooltips");
		tooltips[1] = bundle.getString("frmMain.ResumeHeaderName.tooltips");
		tooltips[2] = bundle.getString("frmMain.ResumeHeaderLine.tooltips");
		tooltips[3] = bundle.getString("frmMain.ResumeHeaderElevation.tooltips");
		tooltips[4] = bundle.getString("frmMain.ResumeHeaderClimbP.tooltips");
		tooltips[5] = bundle.getString("frmMain.ResumeHeaderClimbM.tooltips");
		tooltips[6] = bundle.getString("frmMain.ResumeHeaderDistance.tooltips");
		tooltips[7] = bundle.getString("frmMain.ResumeHeaderTime.tooltips");
		tooltips[8] = bundle.getString("frmMain.ResumeHeaderHour.tooltips");
		tooltips[9] = bundle.getString("frmMain.ResumeHeaderDTTime.tooltips");
		tooltips[10] = bundle.getString("frmMain.ResumeHeaderTimeLimit.tooltips");
		tooltips[11] = bundle.getString("frmMain.ResumeHeaderStationTime.tooltips");
		tooltips[12] = bundle.getString("frmMain.ResumeHeaderDTDistance.tooltips");
		tooltips[13] = bundle.getString("frmMain.ResumeHeaderDTClimbP.tooltips");
		tooltips[14] = bundle.getString("frmMain.ResumeHeaderDTClimbM.tooltips");
		tooltips[15] = bundle.getString("frmMain.ResumeHeaderSpeedP.tooltips");
		tooltips[16] = bundle.getString("frmMain.ResumeHeaderSpeedM.tooltips");
		tooltips[17] = bundle.getString("frmMain.ResumeHeaderAvgSlopeP.tooltips");
		tooltips[18] = bundle.getString("frmMain.ResumeHeaderAvgSlopeM.tooltips");
		tooltips[19] = bundle.getString("frmMain.ResumeHeaderAvgSpeed.tooltips");
		tooltips[20] = bundle.getString("frmMain.ResumeHeaderComment.tooltips");

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
