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

import java.util.ArrayList;

public class ImportPtsData {
	private static ImportPtsData instance;
	public int ReadError = 0;

	public ArrayList<CgImportPts> data;

	// -- Constructeur --
	public ImportPtsData() {
		data = new ArrayList<CgImportPts>();
	}

	public static synchronized ImportPtsData getInstance() {
		if (instance == null) {
			instance = new ImportPtsData();
		}
		return instance;
	}

}
