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
import course_generator.utils.CgConst;
import java.awt.Component;
import javax.swing.JTable;
import static javax.swing.SwingConstants.RIGHT;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;

/**
 *
 * @author pierre.delore
 */
public class DiffRenderer   extends DefaultTableCellRenderer {
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,	row, column);

        CgData data = (CgData) value;

        Double diff = data.getDiff();

        //-- Set the value
        setText(String.format("%1.0f ",diff));
        setHorizontalAlignment(RIGHT);
        
        //-- Set the background color
        Color c = Color.WHITE;
        if ((diff <= CgConst.DIFF_VERYEASY) && (diff > CgConst.DIFF_EASY)) {
            c = CgConst.CL_DIFF_VERYEASY; //Color.WHITE; //-- paint in white
        } 
        else if ((diff <= CgConst.DIFF_EASY) && (diff > CgConst.DIFF_AVERAGE)) {
            c = CgConst.CL_DIFF_EASY; //  new Color(170, 212, 0); //-- Paint in green
        } 
        else if ((diff <= CgConst.DIFF_AVERAGE) && (diff > CgConst.DIFF_HARD)) {
            c = CgConst.CL_DIFF_AVERAGE; //new Color(85, 153, 255); //-- Paint in blue
        } 
        else if ((diff <= CgConst.DIFF_HARD) && (diff > CgConst.DIFF_VERYHARD)) {
            c = CgConst.CL_DIFF_HARD; //new Color(255, 0, 0); //-- Paint in red
        }     
        else if (diff <= CgConst.DIFF_VERYHARD) {
            c = CgConst.CL_DIFF_VERYHARD; //new Color(77, 77, 77); //-- Paint in gray
        }

        if (isSelected)
            setBackground(new Color(51,153,255));
        else
            setBackground(c);
        
        return this;
    }
}