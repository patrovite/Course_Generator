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
import course_generator.TrackData;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import static javax.swing.SwingConstants.RIGHT;
import javax.swing.table.DefaultTableCellRenderer;
import org.joda.time.DateTime;

/**
 *
 * @author pierre.delore
 */
public class HourRenderer extends DefaultTableCellRenderer {
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,	row, column);

        TrackData track = (TrackData) value;

        DateTime time = track.data.get(row).getHour();

        //-- Set the value
        setText(time.toString("E HH:mm:ss "));
        setHorizontalAlignment(RIGHT);
        
        //-- Set the background color
        Color c = new Color(221, 255, 155); //Color.LightGreen;
        
        int ts_val = time.getSecondOfDay();
        int ts_start = track.StartNightTime.getSecondOfDay();
        int ts_end = track.EndNightTime.getSecondOfDay();
        if (
           (track.bNightCoeff)
            && (
                (ts_val>ts_start)
                || (ts_val<ts_end)
               )
            )
            c = new Color(0,128,255);//95,158,160); //CadetBlue;

        if (isSelected)
            setBackground(new Color(51,153,255));
        else
            setBackground(c);
        
        return this;
    }
    
}
