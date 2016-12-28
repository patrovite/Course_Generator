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

package course_generator.mrb;

import org.joda.time.DateTime;

import course_generator.CgData;
import course_generator.utils.CgConst;
import course_generator.utils.Utils;

public class MrbData extends CgData {
	/** Distance from previous point **/
	private double deltadistance;
	/** Time from previous point **/
	private int deltatime;


	public MrbData(double Num, double Latitude, double Longitude, double Elevation, double ElevationMemo, int Tag,
			double Dist, double Total, double Diff, double Coeff, double Recup, double Slope, double Speed,
			double dElevation, int Time, // Time in second
			double dTime_f, // Time this part of the track in second (with decimal)
			int TimeLimit, // Time limit
			DateTime Hour, // Date and time at this point
			int Station, String Name, String Comment, double tmp1, double tmp2, String FmtLbMiniRoadbook,
			int OptionMiniRoadbook, int VPosMiniRoadbook, String CommentMiniRoadbook, int FontSizeMiniRoadbook,
			double deltadistance, int deltatime) {
		
		super(Num, Latitude, Longitude, Elevation, ElevationMemo, Tag, Dist, Total, Diff, Coeff, Recup, Slope, Speed,
				dElevation, Time, // Time in second
				dTime_f, //  Time this part of the track in second (with decimal)
				TimeLimit, // Time limit
				Hour, // Date and time at this point
				Station, Name, Comment, tmp1, tmp2, FmtLbMiniRoadbook, OptionMiniRoadbook, VPosMiniRoadbook,
				CommentMiniRoadbook, FontSizeMiniRoadbook);

		this.deltadistance = deltadistance;
		this.deltatime = deltatime;
	}


	public double getDeltaDist() {
		return deltadistance;
	}


	public double getDeltaDist(int unit) {
		switch (unit) {
			case CgConst.UNIT_METER:
				return deltadistance;
			case CgConst.UNIT_MILES_FEET:
				// meter to miles
				return Utils.Meter2uMiles(deltadistance);
			default:
				return deltadistance;
		}
	}


	public String getDeltaDistString(int unit, boolean withunit) {

		Double d = getDeltaDist(unit)/1000.0;

		String s = "";

		// -- Set the value
		switch (unit) {
			case CgConst.UNIT_METER:
				s = String.format("%1.3f ", d);
				if (withunit)
					s = s + "m";
				break;
			case CgConst.UNIT_MILES_FEET:
				s = String.format("%1.3f ", d);
				if (withunit)
					s = s + "miles";
				break;
			default:
				s = String.format("%1.3f ", d);
				if (withunit)
					s = s + "m";
				break;
		}
		return s;
	}


	public void setDeltaDist(double dist) {
		deltadistance = dist;
	}

	
	public int getDeltaTime() {
		return deltatime;
	}

	public String getDeltaTimeString() {
	 int time = getDeltaTime();

     //-- Set the value
     int nbh = time / 3600;
     int nbm = (time % 3600) / 60;
     int nbs = (time % 3600) % 60;
     return String.format("%02d:%02d:%02d ",nbh,nbm,nbs);
	}

	public void setDeltaTime(int deltatime) {
		this.deltatime=deltatime;
	}

}
