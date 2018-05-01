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

import course_generator.utils.CgConst;
import course_generator.utils.Utils;

public class CgImportPts {
	/** Selection **/
	private boolean Sel;
	/** Line from the data grid **/
	private double Line;
	/** Distance between point in meter **/
	private double Dist;
	/** Latitude of the position **/
	private double Latitude;
	/** Longitude of the position **/
	private double Longitude;
	/** Elevation in meter **/
	private double Elevation;
	/** Tag of the position **/
	private int Tag;
	/** Name of the position **/
	private String Name;
	/** Comment **/
	private String Comment;


	// -- Constructor
	public CgImportPts() {
		Sel = false;
		Line = 0;
		Dist = 0.0;
		Latitude = 0.0;
		Longitude = 0.0;
		Elevation = 0.0;
		Tag = 0;
		Name = "";
		Comment = "";
	}


	public CgImportPts(double lat, double lon, double ele, int tag, String name, String comment) {
		Sel = false;
		Line = 0;
		Dist = 0.0;
		Latitude = lat;
		Longitude = lon;
		Elevation = ele;
		Tag = tag;
		Name = name;
		Comment = comment;
	}


	// -- Selection
	public boolean getSel() {
		return Sel;
	}


	public String getSelString() {
		if (Sel)
			return "1";
		else
			return "0";
	}


	public void setSel(boolean sel) {
		this.Sel = sel;
	}


	public void invSel() {
		Sel = !Sel;
	}


	// -- Line number
	public double getLine() {
		return Line;
	}


	/**
	 * Return the line as string
	 * 
	 * @return Line number + 1 as string
	 */
	public String getLineString() {
		return String.format("%1.0f", Line + 1);
	}


	public void setLine(double line) {
		Line = line;
	}


	// -- Distance
	public double getDist() {
		return Dist;
	}


	public double getDist(int unit) {
		switch (unit) {
		case CgConst.UNIT_METER:
			return Dist;
		case CgConst.UNIT_MILES_FEET:
			// meter to miles
			return Utils.Meter2uMiles(Dist);
		default:
			return Dist;
		}
	}


	public String getDistString(int unit, boolean withunit) {

		Double d = getDist(unit);

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


	public void setDist(double dist) {
		Dist = dist;
	}


	// -- Latitude
	public double getLatitude() {
		return Latitude;
	}


	public String getLatitudeString() {
		return String.format("%1.7f", Latitude);
	}


	public void setLatitude(double latitude) {
		Latitude = latitude;
	}


	// -- Longitude
	public double getLongitude() {
		return Longitude;
	}


	public String getLongitudeString() {
		return String.format("%1.7f", Longitude);
	}


	public void setLongitude(double longitude) {
		Longitude = longitude;
	}


	// -- Elevation
	public double getElevation() {
		return Elevation;
	}


	public double getElevation(int unit) {
		switch (unit) {
		case CgConst.UNIT_METER:
			return Elevation;
		case CgConst.UNIT_MILES_FEET:
			// meter to miles
			return Utils.Meter2Feet(Elevation);
		default:
			return Elevation;
		}
	}


	public String getElevationString(int unit, boolean withunit) {

		double e = getElevation(unit);

		String s = "";
		switch (unit) {
		case CgConst.UNIT_METER:
			s = String.format("%1.0f ", e);
			if (withunit)
				s = s + "m";
			break;
		case CgConst.UNIT_MILES_FEET:
			s = String.format("%1.0f ", e);
			if (withunit)
				s = s + "feet";
			break;
		default:
			s = String.format("%1.0f ", e);
			if (withunit)
				s = s + "m";
			break;
		}
		return s;
	}


	public void setElevation(double elevation) {
		Elevation = elevation;
	}


	// -- Tag
	public int getTag() {
		return Tag;
	}


	public void setTag(int tag) {
		Tag = tag;
	}


	// -- Name
	public String getName() {
		return Name;
	}


	public void setName(String name) {
		Name = name;
	}


	// -- Comment
	public String getComment() {
		return Comment;
	}


	public void setComment(String comment) {
		Comment = comment;
	}

}
