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

import course_generator.TrackData;
import course_generator.settings.CgSettings;

import javax.swing.table.AbstractTableModel;
import org.joda.time.DateTime;

/**
 *
 * @author pierre.delore
 */
public class TrackDataModel extends AbstractTableModel {
    
//	private final String [] header = {"N°", "Lat", "Lon", "Elev", "Tag", "Dist (m)", "Total (km)", "Diff", "Coeff", "Récup", "Temps", "BH", "Heure", "Ravito", "Nom", "Commentaire"};
    private final String header[];
    private TrackData track;
    private CgSettings settings;

    public TrackDataModel(TrackData data, CgSettings _settings) {
    	super();
    	java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
    	
    	header = new String[16];
		header[0]=bundle.getString("frmMain.HeaderNum.text");
		header[1]=bundle.getString("frmMain.HeaderLat.text");
		header[2]=bundle.getString("frmMain.HeaderLon.text");
		header[3]=bundle.getString("frmMain.HeaderElev.text");
		header[4]=bundle.getString("frmMain.HeaderTag.text");
		header[5]=bundle.getString("frmMain.HeaderDist.text");
		header[6]=bundle.getString("frmMain.HeaderTotal.text");
		header[7]=bundle.getString("frmMain.HeaderDiff.text");
		header[8]=bundle.getString("frmMain.HeaderCoeff.text");
		header[9]=bundle.getString("frmMain.HeaderRecovery.text");
		header[10]=bundle.getString("frmMain.HeaderTime.text");
		header[11]=bundle.getString("frmMain.HeaderTimeLimit.text");
		header[12]=bundle.getString("frmMain.HeaderHour.text");
		header[13]=bundle.getString("frmMain.HeaderStation.text");
		header[14]=bundle.getString("frmMain.HeaderName.text");
		header[15]=bundle.getString("frmMain.HeaderComment.text");
    	
    	settings = _settings;
        track = data; //TrackData.getInstance();
    }
   
    public CgSettings getSettings() {
        return settings;
    }
    
    @Override
    public int getColumnCount() {
        return header.length;
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        return header[columnIndex];
    }

    @Override
    public int getRowCount() {
        return track.data.size();
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: 
                //N°
                return track.data.get(rowIndex).getNum();
            case 1: 
                //Lat
                return track.data.get(rowIndex);
            case 2: 
                //Lon
                return track.data.get(rowIndex);
            case 3: 
                //Elev
                return track.data.get(rowIndex);
            case 4: 
                //Tag
                return track.data.get(rowIndex);
            case 5: 
                //Dist (m)
                return track.data.get(rowIndex);
            case 6: 
                //Total (km)
                return track.data.get(rowIndex);
            case 7: 
                //Diff
                return track.data.get(rowIndex);
            case 8: 
                //Coeff
                return track.data.get(rowIndex);
            case 9: 
                //Récup
                return track.data.get(rowIndex);
            case 10: 
                //Temps
                return track.data.get(rowIndex);
            case 11: 
                //Timelimit
                return track.data.get(rowIndex);
            case 12: 
                //Hour
                return track; //track.ClassHour.track;
            case 13: 
                //Station
                return track.data.get(rowIndex); //track.ClassStation.track.data.get(rowIndex);
            case 14: 
                //Nom
                return track.data.get(rowIndex).getName();
            case 15: 
                //Commentaire
                return track.data.get(rowIndex).getComment();
            default:
                throw new IllegalArgumentException();
        }
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: 
                //N°
                return Double.class;
            case 1: 
                //Lat
                return LatClass.class;
            case 2: 
                //Lon
                return LonClass.class;
            case 3: 
                //Elev
                return ElevationClass.class;
            case 4: 
                //Tag
                return TagClass.class;
            case 5: 
                //Dist (m)
                return DistClass.class;
            case 6: 
                //Total (km)
                return TotalClass.class;
            case 7: 
                //Diff
                return DiffClass.class;
            case 8: 
                //Coeff
                return CoeffClass.class;
            case 9: 
                //Récup
                return RecupClass.class;
            case 10: 
                //Temps
                return TimeClass.class;
            case 11: 
                //Time limit
                return TimelimitClass.class;
            case 12: 
                //Hour
                return HourClass.class;
            case 13: 
                //Station
                return StationClass.class;
            case 14: 
                //Nom
                return String.class;
            case 15: 
                //Commentaire
                return String.class;
            default:
                return Object.class;

        }
    }
    

} //Class
