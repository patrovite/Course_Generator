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

package course_generator;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.joda.time.DateTime;

public class ResumeData {
	private static ResumeData instance;

	public ArrayList<CgResume> data;

	// -- Constructeur --
	public ResumeData() {
		data = new ArrayList<CgResume>();
	}

	public static synchronized ResumeData getInstance() {
		if (instance == null) {
			instance = new ResumeData();
		}
		return instance;
	}

	/**
	 * Save the resume data as a CSV file
	 * 
	 * @param filename
	 *            Full path and name to the file to create
	 * @param unit
	 *            unit constant
	 */
	public void SaveAsCSV(String filename, int unit) {
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		StringBuilder s = new StringBuilder();
		
		try {
			PrintWriter writer = new PrintWriter(filename, "UTF-8");

			s.append(bundle.getString("frmMain.ResumeHeaderNum.text") + ";");
			s.append(bundle.getString("frmMain.ResumeHeaderName.text") + ";");
			s.append(bundle.getString("frmMain.ResumeHeaderLine.text") + ";");
			s.append(bundle.getString("frmMain.ResumeHeaderElevation.text") + ";");
			s.append(bundle.getString("frmMain.ResumeHeaderClimbP.text") + ";");
			s.append(bundle.getString("frmMain.ResumeHeaderClimbM.text") + ";");
			s.append(bundle.getString("frmMain.ResumeHeaderDistance.text") + ";");
			s.append(bundle.getString("frmMain.ResumeHeaderTime.text") + ";");
			s.append(bundle.getString("frmMain.ResumeHeaderHour.text") + ";");
			s.append(bundle.getString("frmMain.ResumeHeaderDTTime.text") + ";");
			s.append(bundle.getString("frmMain.ResumeHeaderTimeLimit.text") + ";");
			s.append(bundle.getString("frmMain.ResumeHeaderStationTime.text") + ";");
			s.append(bundle.getString("frmMain.ResumeHeaderDTDistance.text") + ";");
			s.append(bundle.getString("frmMain.ResumeHeaderDTClimbP.text") + ";");
			s.append(bundle.getString("frmMain.ResumeHeaderDTClimbM.text") + ";");
			s.append(bundle.getString("frmMain.ResumeHeaderSpeedP.text") + ";");
			s.append(bundle.getString("frmMain.ResumeHeaderSpeedM.text") + ";");
			s.append(bundle.getString("frmMain.ResumeHeaderAvgSlopeP.text") + ";");
			s.append(bundle.getString("frmMain.ResumeHeaderAvgSlopeM.text") + ";");
			s.append(bundle.getString("frmMain.ResumeHeaderAvgSpeed.text") + ";");
			s.append(bundle.getString("frmMain.ResumeHeaderComment.text") + ";");
			writer.println(s);
			s.setLength(0);

			for (CgResume d : data) {
				s.append(d.getNumString() + ";");
				s.append(d.getName() + ";");
				s.append(d.getLineString() + ";");
				s.append(d.getElevationString(unit, false) + ";");
				s.append(d.getClimbPString(unit, false) + ";");
				s.append(d.getClimbMString(unit, false) + ";");
				s.append(d.getDistString(unit, false) + ";");
				s.append(d.getTimeString() + ";");
				s.append(d.getHourString() + ";");
				s.append(d.getdTime_f_String() + ";");
				s.append(d.getTimeLimitString() + ";");
				s.append(d.getStationTimeString() + ";");
				s.append(d.getdDistString(unit, false) + ";");
				s.append(d.getdClimbPString(unit, false) + ";");
				s.append(d.getdClimbMString(unit, false) + ";");
				s.append(d.getSpeedPString(unit, false) + ";");
				s.append(d.getSpeedMString(unit, false) + ";");
				s.append(d.getAvgSlopePString() + ";");
				s.append(d.getAvgSlopeMString() + ";");
				s.append(d.getAvgSpeedString(unit, false) + ";");
				s.append(d.getComment());
				writer.println(s);
				s.setLength(0);
			}

			writer.close();
		} catch (IOException e) {
			 e.printStackTrace();
		}

	}

}
