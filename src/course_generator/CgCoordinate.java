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

import java.awt.Color;

import org.openstreetmap.gui.jmapviewer.Coordinate;

/**
 *
 * @author pierre.delore
 */
public class CgCoordinate extends Coordinate {

	private static final long serialVersionUID = -4926982997021073873L;
	private Color _color;

	public CgCoordinate(double lat, double lon, Color color) {
		super(lat, lon);
		_color = color;
	}

	public Color getColor() {
		return _color;
	}
}
