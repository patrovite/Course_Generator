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
    public double Speed;
    public double Dist;
    public double Time;
    public int Cmpt;

    //CultureInfo culture = CultureInfo.CreateSpecificCulture("en-GB");

    public StatData() {
	Init();
    }

    public void Init()  {
            Speed=0.0;
            Dist=0.0;
            Time=0.0;
            Cmpt=0;
    }    
}
