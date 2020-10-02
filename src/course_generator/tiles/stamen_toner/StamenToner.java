/*
 * Course Generator
 * Copyright (C) 2019 Pierre Delore
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

package course_generator.tiles.stamen_toner;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.AbstractOsmTileSource;

/**
 *
 * @author pierre.delore
 */
public class StamenToner extends AbstractOsmTileSource {
	// http://tile.stamen.com/toner/{z}/{x}/{y}.png
	private static final String PATTERN = "http://tile.stamen.com/toner";
	private static final String[] SERVER = { "a", "b", "c" };

	private int SERVER_NUM = 0;

	/**
	 * Constructs a new {@code "Mapnik"} tile source.
	 */
	public StamenToner() {
		super("map", PATTERN);
	}

	@Override
	public String getBaseUrl() {
		String url = String.format(this.baseUrl, new Object[] { SERVER[SERVER_NUM] });
		SERVER_NUM = (SERVER_NUM + 1) % SERVER.length;
		return url;
	}

	@Override
	public int getMaxZoom() {
		return 18;
	}

	public TileSource.TileUpdate getTileUpdate() {
		return TileSource.TileUpdate.IfNoneMatch;
	}

	@Override
	public String getAttributionText(int zoom, Coordinate topLeft, Coordinate botRight) {
		return "Map tiles by Stamen Design, under CC BY 3.0. Data by OpenStreetMap, under ODbL";
	}
}
