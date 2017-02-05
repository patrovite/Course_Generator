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

import java.awt.Color;

public class CgConst {
	/**Unit is meter**/
	public static final int UNIT_METER=0;
	/**Unit is feet and miles**/
    public static final int UNIT_MILES_FEET=1;
    
    /**Course Generator directory**/
    public static final String CG_DIR="Course Generator";
    /**Maximum version of the CGX file to read**/
    public static final int MAX_CGX_VERSION_TO_READ = 5;
    /**Maximum value of health coefficient**/
    public static final double MAX_COEFF=200.0;
    /**Maximum value of track difficulty coefficient**/
    public static final double MAX_DIFF=100.0;
    
    public static final int TAG_HIGH_PT    = 1;
    public static final int TAG_LOW_PT     = 2;
    public static final int TAG_EAT_PT     = 4;
    public static final int TAG_WATER_PT   = 8;
    public static final int TAG_COOL_PT    = 16;
    public static final int TAG_MARK       = 32;
    public static final int TAG_NOTE       = 64;
    public static final int TAG_INFO       = 128;
    public static final int TAG_ROADBOOK   = 256;


    /**Minimum value of the slope for the curve**/
    public static final double MIN_CLIMB = -50.0;
    /**Maximum value of the slope for the curve**/
    public static final double MAX_CLIMB = 50.0;

    /**Delta value for the total elevation calculation**/
    public static final double MIN_ELEV = 10.0;

    public static final double DIST_MAX_MINMAX = 1000.0;
    public static final double MIN_ELEV_MINMAX = 50.0;

    public static final double DIFF_VERYEASY = 100;
    public static final double DIFF_EASY = 98;
    public static final double DIFF_AVERAGE = 95;
    public static final double DIFF_HARD = 88;
    public static final double DIFF_VERYHARD = 80;

    public static final int SRC_MAIN = 0;
    public static final int SRC_RESUME = 1;

    public static final int IMPORT_MODE_LOAD = 0;
    public static final int IMPORT_MODE_INS_START = 1;
    public static final int IMPORT_MODE_ADD_END = 2;

    public static final int MRBOPT_SEL       = 0x0001;
    public static final int MRBOPT_LEFT      = 0x0002;
    public static final int MRBOPT_CENTER    = 0x0004; 
    public static final int MRBOPT_RIGHT     = 0x0008;
    public static final int MRBOPT_SHOWTAGS  = 0x0080;

    public static final String DEFAULTMRBFORMAT = "%N%L%Am%L%Dkm%L%H";
    public static final int DEFAULTMRBFONTSIZE = 10;
    
    public static final int MRB_DUP_POS = 0x0001;
    public static final int MRB_DUP_ALIGN = 0x0002;
    public static final int MRB_DUP_FORMAT = 0x0004;
    public static final int MRB_DUP_SIZE = 0x0008;
    public static final int MRB_DUP_TAGS = 0x0010;

    public static final int MRB_DUP_NONE = 0;
    public static final int MRB_DUP_SIMPLE = 1;
    public static final int MRB_DUP_MULTI = 2;

    public static final Color CL_PROFIL_SIMPLE_FILL = new Color(0xAB, 0xF9, 0x2F);
    public static final Color CL_PROFIL_SIMPLE_BORDER = new Color(0x05, 0x82, 0x05);

    public static final Color CL_PROFIL_RS_ROAD = new Color(0x74, 0xAA, 0x1F);
    public static final Color CL_PROFIL_RS_PATH = new Color(0xAB, 0xF9, 0x2F);
    public static final Color CL_PROFIL_RS_BORDER = new Color(0x05, 0x82, 0x05);

    public static final Color CL_PROFIL_SLOPE_INF5 = new Color(0xFF, 0xBB, 0x73);
    public static final Color CL_PROFIL_SLOPE_INF10 = new Color(0xFF, 0xA2, 0x40);
    public static final Color CL_PROFIL_SLOPE_INF15 = new Color(0xFF, 0x83, 0x00);
    public static final Color CL_PROFIL_SLOPE_SUP15 = new Color(0xA6, 0x55, 0x00);
    public static final Color CL_PROFIL_SLOPE_BORDER = new Color(0xA6, 0x55, 0x00);
    
    public static final Color CL_DIFF_VERYEASY=new Color(255, 255, 255); //-- White
    public static final Color CL_DIFF_EASY=new Color(170, 212, 0); //-- Green
    public static final Color CL_DIFF_AVERAGE=new Color(85, 153, 255); //-- Blue
    public static final Color CL_DIFF_HARD=new Color(255, 0, 0); //-- Red
    public static final Color CL_DIFF_VERYHARD=new Color(77, 77, 77); //-- Gray
    
}
