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

package course_generator.utils;

/**
 *
 * @author pierre.delore
 */
public class StatData {
	/** Speed in meter per hour **/
	private double Speed;
	/** Disatnce in meter **/
	private double Dist;
	/** Time **/
	public double Time;
	/** Counter **/
	public int Cmpt;


	public StatData() {
		Init();
	}


	public void Init() {
		Speed = 0.0;
		Dist = 0.0;
		Time = 0.0;
		Cmpt = 0;
	}


	/**
	 * Set the speed
	 * 
	 * @param speed
	 *            Speed in meter per hour
	 */
	public void setSpeed(double speed) {
		this.Speed = speed;
	}


	/**
	 * Set the speed
	 * 
	 * @param speed
	 *            Speed in meter per hour
	 */
	public void setSpeed(String speed) {
		this.Speed = Double.valueOf(speed);
	}


	/**
	 * Get the speed depending the unit
	 * 
	 * @param unit
	 *            Unit for the returned value
	 * @return Speed in the corresponding unit
	 */
	public double getSpeed(int unit) {
		switch (unit) {
		case CgConst.UNIT_METER:
			return Speed;
		case CgConst.UNIT_MILES_FEET:
			// meter to miles
			return Utils.Meter2uMiles(Speed);
		default:
			return Speed;
		}
	}


	/**
	 * Get the speed as a string
	 * 
	 * @param unit
	 *            Unit for the returned value
	 * @return String containing speed
	 */
	public String getSpeedString(int unit) {

		Double d = getSpeed(unit);

		String s = "";

		// -- Set the value
		switch (unit) {
		case CgConst.UNIT_METER:
			s = String.format("%1.0f ", d);
			break;
		case CgConst.UNIT_MILES_FEET:
			s = String.format("%1.0f ", d);
			break;
		default:
			s = String.format("%1.0f ", d);
			break;
		}
		return s;
	}


	// -------------------------------------------

	/**
	 * Set the distance
	 * 
	 * @param dist
	 *            Speed in meter per hour
	 */
	public void setDist(double dist) {
		this.Dist = dist;
	}


	/**
	 * Get the dist depending the unit
	 * 
	 * @param unit
	 *            Unit for the returned value
	 * @return Dist in the corresponding unit
	 */
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


	/**
	 * Get the dist as a string
	 * 
	 * @param unit
	 *            Unit for the returned value
	 * @return String containing dist
	 */
	public String getDistString(int unit) {

		Double d = getDist(unit);

		String s = "";

		// -- Set the value
		switch (unit) {
		case CgConst.UNIT_METER:
			s = String.format("%1.0f ", d);
			break;
		case CgConst.UNIT_MILES_FEET:
			s = String.format("%1.0f ", d);
			break;
		default:
			s = String.format("%1.0f ", d);
			break;
		}
		return s;
	}


	/**
	 * Copy the content of the object to another object
	 * 
	 * @param d
	 *            Destination object
	 * @return Copied object
	 */
	public StatData CopyTo(StatData d) {
		d.Speed = Speed;
		d.Dist = Dist;
		d.Time = Time;
		d.Cmpt = Cmpt;
		return d;
	}
}
