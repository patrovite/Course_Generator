/*
 * Course Generator
 * Copyright (C) 2017 Pierre Delore
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

package course_generator.maps;

//import java.awt.event.MouseEvent;

//import org.openstreetmap.gui.jmapviewer.DefaultMapController;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MemoryTileCache;

/**
 * This class extend JMapViewer in order to change the way the map is controlled
 * with the mouse. Scroll map with right click => left click
 * 
 * @author pierre.delore
 *
 */
public class JMapViewerCG extends JMapViewer {
	public CGMapController CGMapController;


	public JMapViewerCG() {
		super(new MemoryTileCache(), 8);
		CGMapController = new CGMapController(this);
		// CGMapController.setMovementMouseButton(MouseEvent.BUTTON1);
	}

}
