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

import javax.swing.table.AbstractTableModel;

import course_generator.ResumeData;
import course_generator.settings.CgSettings;

public class ResumeModel extends AbstractTableModel {
	private static final long serialVersionUID = -7645275879053388708L;
	private final String header[];
	private ResumeData resume;
	private CgSettings settings;


	public ResumeModel(ResumeData data, CgSettings _settings) {
		super();
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");

		header = new String[21];
		header[0] = bundle.getString("frmMain.ResumeHeaderNum.text");
		header[1] = bundle.getString("frmMain.ResumeHeaderName.text");
		header[2] = bundle.getString("frmMain.ResumeHeaderLine.text");
		header[3] = bundle.getString("frmMain.ResumeHeaderElevation.text");
		header[4] = bundle.getString("frmMain.ResumeHeaderClimbP.text");
		header[5] = bundle.getString("frmMain.ResumeHeaderClimbM.text");
		header[6] = bundle.getString("frmMain.ResumeHeaderDistance.text");
		header[7] = bundle.getString("frmMain.ResumeHeaderTime.text");
		header[8] = bundle.getString("frmMain.ResumeHeaderHour.text");
		header[9] = bundle.getString("frmMain.ResumeHeaderDTTime.text");
		header[10] = bundle.getString("frmMain.ResumeHeaderTimeLimit.text");
		header[11] = bundle.getString("frmMain.ResumeHeaderStationTime.text");
		header[12] = bundle.getString("frmMain.ResumeHeaderDTDistance.text");
		header[13] = bundle.getString("frmMain.ResumeHeaderDTClimbP.text");
		header[14] = bundle.getString("frmMain.ResumeHeaderDTClimbM.text");
		header[15] = bundle.getString("frmMain.ResumeHeaderSpeedP.text");
		header[16] = bundle.getString("frmMain.ResumeHeaderSpeedM.text");
		header[17] = bundle.getString("frmMain.ResumeHeaderAvgSlopeP.text");
		header[18] = bundle.getString("frmMain.ResumeHeaderAvgSlopeM.text");
		header[19] = bundle.getString("frmMain.ResumeHeaderAvgSpeed.text");
		header[20] = bundle.getString("frmMain.ResumeHeaderComment.text");

		settings = _settings;
		resume = data;
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
		return resume.data.size();
	}


	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return resume.data.get(rowIndex);// .getNum();
	}


	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return ResumeClass.class;
	}

}
