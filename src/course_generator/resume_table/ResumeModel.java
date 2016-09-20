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

package course_generator.resume_table;

import javax.swing.table.AbstractTableModel;

import course_generator.ResumeData;
import course_generator.settings.CgSettings;

public class ResumeModel  extends AbstractTableModel{
    private final String header[];
    private ResumeData resume;
    private CgSettings settings;

    public ResumeModel(ResumeData data, CgSettings _settings) {
    	super();
    	java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
    	
    	header = new String[21];
		header[0]=bundle.getString("frmMain.ResumeHeaderNum.text");
		header[1]=bundle.getString("frmMain.ResumeHeaderName.text");
		header[2]=bundle.getString("frmMain.ResumeHeaderLine.text");
		header[3]=bundle.getString("frmMain.ResumeHeaderElevation.text");
		header[4]=bundle.getString("frmMain.ResumeHeaderClimbP.text");
		header[5]=bundle.getString("frmMain.ResumeHeaderClimbM.text");
		header[6]=bundle.getString("frmMain.ResumeHeaderDistance.text");
		header[7]=bundle.getString("frmMain.ResumeHeaderTime.text");
		header[8]=bundle.getString("frmMain.ResumeHeaderHour.text");
		header[9]=bundle.getString("frmMain.ResumeHeaderDTTime.text");
		header[10]=bundle.getString("frmMain.ResumeHeaderTimeLimit.text");
		header[11]=bundle.getString("frmMain.ResumeHeaderStationTime.text");
		header[12]=bundle.getString("frmMain.ResumeHeaderDTDistance.text");
		header[13]=bundle.getString("frmMain.ResumeHeaderDTClimbP.text");
		header[14]=bundle.getString("frmMain.ResumeHeaderDTClimbM.text");
		header[15]=bundle.getString("frmMain.ResumeHeaderSpeedP.text");
		header[16]=bundle.getString("frmMain.ResumeHeaderSpeedM.text");
		header[17]=bundle.getString("frmMain.ResumeHeaderAvgSlopeP.text");
		header[18]=bundle.getString("frmMain.ResumeHeaderAvgSlopeM.text");
		header[19]=bundle.getString("frmMain.ResumeHeaderAvgSpeed.text");
		header[20]=bundle.getString("frmMain.ResumeHeaderComment.text");
		
    	settings = _settings;
        resume = data; 
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
        return resume.data.size();
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: 
                //N°
                return resume.data.get(rowIndex);//.getNum();
            case 1: 
                //Name
                return resume.data.get(rowIndex);//.getName();
            case 2: 
                //Line
                return resume.data.get(rowIndex);//.getLine();
            case 3: 
                //Elevation
                return resume.data.get(rowIndex);//.getElevation();
            case 4: 
                //Positive climb
                return resume.data.get(rowIndex);//.getClimbP();
            case 5: 
                //Negative climb
                return resume.data.get(rowIndex);//.getClimbM();
            case 6: 
                //Distance
                return resume.data.get(rowIndex);//.getDist();
            case 7: 
                //Time
                return resume.data.get(rowIndex);//.getTime();
            case 8: 
                //Hour
                return resume.data.get(rowIndex);//.getHour();
            case 9: 
                //dt Time
                return resume.data.get(rowIndex);//.getdTime_f();
            case 10: 
                //Time limit
                return resume.data.get(rowIndex);//.getTimeLimit();
            case 11: 
                //Station Time
                return resume.data.get(rowIndex);//.getStationTime();
            case 12: 
                //dt Distance
                return resume.data.get(rowIndex);//.getdDist();
            case 13: 
                //dt positive climb
                return resume.data.get(rowIndex);//.getdClimbP();
            case 14: 
                //dt negative climb
                return resume.data.get(rowIndex);//.getdClimbM();
            case 15: 
                //Ascend speed
                return resume.data.get(rowIndex);//.getSpeedP();
            case 16: 
                //Descend speed
                return resume.data.get(rowIndex);//.getSpeedM();
            case 17: 
            	//Average ascend slope
            	return resume.data.get(rowIndex);//.getAvgSlopeP();
            case 18: 
            	//Average descend slope
            	return resume.data.get(rowIndex);//.getAvgSlopeM();
            case 19: 
            	//Average speed
            	return resume.data.get(rowIndex);//.getAvgSpeed();
            case 20: 
            	//Comment
            	return resume.data.get(rowIndex);//.getComment();
            default:
                throw new IllegalArgumentException();
        }
    }
    
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
	        case 0: 
	            //N°
	            return ResumeNumClass.class;
	        case 1: 
	            //Name
	            return ResumeNameClass.class;
	        case 2: 
	            //Line
	            return ResumeLineClass.class;
	        case 3: 
	            //Elevation
	            return ResumeElevationClass.class;
	        case 4: 
	            //Positive climb
	            return ResumeClimbPClass.class;
	        case 5: 
	            //Negative climb
	            return ResumeClimbNClass.class;
	        case 6: 
	            //Distance
	            return ResumeDistanceClass.class;
	        case 7: 
	            //Time
	            return ResumeTimeClass.class;
	        case 8: 
	            //Hour
	            return ResumeHourClass.class;
	        case 9: 
	            //dt Time
	            return ResumedtTimeClass.class;
	        case 10: 
	            //Time limit
	            return ResumeTimeLimitClass.class;
	        case 11: 
	            //Station Time
	            return ResumeStationTimeClass.class;
	        case 12: 
	            //dt Distance
	            return ResumedtDistanceClass.class;
	        case 13: 
	            //dt positive climb
	            return ResumedtClimbPClass.class;
	        case 14: 
	            //dt negative climb
	            return ResumedtClimbNClass.class;
	        case 15: 
	            //Ascend speed
	            return ResumeSpeedPClass.class;
	        case 16: 
	            //Descend speed
	            return ResumeSpeedNClass.class;
	        case 17: 
	        	//Average ascend slope
	        	return ResumeAvgSlopePClass.class;
	        case 18: 
	        	//Average descend slope
	        	return ResumeAvgSlopeNClass.class;
	        case 19: 
	        	//Average speed
	        	return ResumeAvgSpeedClass.class;
	        case 20: 
	        	//Comment
	        	return ResumeCommentClass.class;
    	
            default:
                return Object.class;

        }
    }

}
