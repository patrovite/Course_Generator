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

package course_generator.trackdata_table;

import course_generator.CgData;
import course_generator.CgConstants;
import course_generator.settings.CgSettings;

import java.awt.Color;
import static java.awt.Color.GREEN;
import static java.awt.Color.RED;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author pierre.delore
 */
public class ElevationRenderer  extends DefaultTableCellRenderer {
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,	row, column);

        CgData data = (CgData) value;

        CgSettings settings = ((TrackDataModel) table.getModel()).getSettings();
        
        Double ele = data.getElevation(settings.Unit);
        Double slope = data.getSlope();
        
        //-- Set the elevation value
        switch (settings.Unit) {
        case CgConstants.UNIT_METER: 
        	setText(String.format("%1.0f ",ele));
        	break;
        case CgConstants.UNIT_MILES_FEET: 
        	setText(String.format("%1.0f ",ele));
        	break;
        default:
        	setText(String.format("%1.0f ",ele));
        	break;        	
        }
        
        //-- Set the icon next to the elevation value
        if (slope>1.0) {
            setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/up_elev.png")));
        }
        else if (slope<-1.0) {
            setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/down_elev.png")));
        }
        else 
            setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/same_level_elev.png")));

        //-- Set the color of the background. Color depend of the slope value
        float tmpslope = slope.floatValue();
        Color c = Color.WHITE;

        if (tmpslope > 2.0f) {
            if (tmpslope > 50.0f) 
                tmpslope = 50.0f;
            float vsl = 18.0f * tmpslope / 50.0f;
            c = Color.getHSBColor((18.0f - vsl) / 100.0f, 1f, 1f);
        }
        else if (tmpslope < -2.0f) {
            tmpslope = -tmpslope;
            if (tmpslope > 50.0f) 
                tmpslope = 50.0f;
            /*
            float vsl = 18f * tmpslope / 50.0f;
            c = Color.getHSBColor((48f + vsl) / 100.0f, 1f, 1f);
            */
            float vsl = 40f * tmpslope / 50.0f;
            c = Color.getHSBColor(0.35f, 1f, (100f-vsl)/100f);                    
        }
        else c = Color.WHITE;

        if (isSelected)
            setBackground(new Color(51,153,255));
        else
            setBackground(c);

        return this;
    }
        
        
}
