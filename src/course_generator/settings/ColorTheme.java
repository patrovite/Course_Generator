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

package course_generator.settings;

import java.awt.Color;

import course_generator.utils.CgConst;

public class ColorTheme {
	public Color ColorVeryEasy;
	public Color ColorEasy;
	public Color ColorAverage;
	public Color ColorHard;
	public Color ColorVeryHard;
	public Color ColorNight;
	public int NormalTrackWidth;
	public int NightTrackWidth;
	public int NormalTrackTransparency;
	public int NightTrackTransparency;
	
	public ColorTheme() {
		ColorVeryEasy = CgConst.CL_DIFF_VERYEASY;
		ColorEasy = CgConst.CL_DIFF_EASY;
		ColorAverage = CgConst.CL_DIFF_AVERAGE;
		ColorHard = CgConst.CL_DIFF_HARD;
		ColorVeryHard = CgConst.CL_DIFF_VERYHARD;
		ColorNight = CgConst.CL_MAP_NIGHT_HIGHLIGHT;
		NormalTrackWidth=CgConst.TRACK_NORMAL_TICKNESS;
		NightTrackWidth=CgConst.TRACK_NIGHT_TICKNESS;
		NormalTrackTransparency=CgConst.NORMAL_TRACK_TRANSPARENCY;
		NightTrackTransparency=CgConst.NIGHT_TRACK_TRANSPARENCY;
	}
}
