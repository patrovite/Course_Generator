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

package course_generator.param;

/**
 *
 * @author pierre.delore
 */
public class CgParam implements Comparable<CgParam> {
	public double Slope;
	public double Speed;


	public CgParam(double slope, double speed) {
		Slope = slope;
		Speed = speed;
	}


	@Override
	public int compareTo(CgParam p) {
		if (Slope - p.Slope < 0)
			return -1;
		else if (Slope - p.Slope > 0)
			return 1;
		else
			return 0;
	}

}
