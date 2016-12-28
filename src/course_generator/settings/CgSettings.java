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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.SAXException;

import course_generator.utils.CgConst;
import course_generator.utils.Utils;

/**
 *
 * @author pierre.delore
 */
public class CgSettings {
    public String ParamFile = "";
    public boolean bNoConnectOnStartup = true;
    public int ConnectionTimeout = 10; //Time in second between internet test

    public String MemoFormat[] = new String[5];
    public String mruGPX[] = new String[5];
    public String mruCGX[] = new String[5];
    public int TableMainColWidth[] = new int[16];
    public String Language;
    public int MainWindowWidth;
    public int MainWindowHeight;
    public int VertSplitPosition;
    public int HorizSplitPosition;
    /** Mini roadbook split position **/
    public int MRB_SplitPosition; 
    
    public int Unit = CgConst.UNIT_METER; //Unit for the display 0=meter 1=Miles/feet
    
    public int ReadError = 0;
    public int LineError = 0;
    public String LastDir; //Store the last directory

    public CgSettings() {
        int i=0;
        
        ParamFile = "Default";
        bNoConnectOnStartup = true;
        ConnectionTimeout = 10;
        Language="";
        
        MainWindowWidth=800;
        MainWindowHeight=600;
        VertSplitPosition=200;
        HorizSplitPosition=50;
        MRB_SplitPosition=220;
        
        MemoFormat = new String[5];
        mruGPX = new String[5];
        mruCGX = new String[5];
        TableMainColWidth = new int[16];
    
        Unit = CgConst.UNIT_METER;
        		
        ReadError = 0;
        LineError = 0;
        
        for (i=0;i<5;i++) {
            MemoFormat[i]="";
            mruGPX[i]="";
            mruCGX[i]="";
        }
    
        for (i=0; i<16;i++) {
            TableMainColWidth[i] = 60;
        }
        LastDir="";
        
    }
    
    
    /**
     * Save the settings to the disk
     * @param _Path Path where the setting file is stored
     */
    public void Save(String _Path) {
        //-- Check if the data directory exist. If not! creation
        Path DataFolder = Paths.get(_Path);
        if (Files.notExists(DataFolder))
        {
            boolean result = false;

            try{
                Files.createDirectory(DataFolder);
                result = true;
            } catch(IOException e){
                System.out.println("Impossible to create the data directory"); 
                return;
            }
        }
        
        //-- Save the data in the home directory
        XMLOutputFactory factory      = XMLOutputFactory.newInstance();
        try {
            XMLStreamWriter writer = factory.createXMLStreamWriter(new FileOutputStream(_Path+"/config.xml"), "UTF-8");
            
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeComment("Course Generator (C) Pierre DELORE");
                writer.writeStartElement("CONFIG");
                    Utils.WriteStringToXML(writer, "PARAMFILE", ParamFile);
                    Utils.WriteBooleanToXML(writer, "NOCONNECTIONONSTARTUP", bNoConnectOnStartup);
                    Utils.WriteIntToXML(writer, "CONNECTIONTIMEOUT", ConnectionTimeout);
                    Utils.WriteStringToXML(writer, "LASTDIR", LastDir);
                    Utils.WriteStringToXML(writer, "MEMOFORMAT1", MemoFormat[0]);
                    Utils.WriteStringToXML(writer, "MEMOFORMAT2", MemoFormat[1]);
                    Utils.WriteStringToXML(writer, "MEMOFORMAT3", MemoFormat[2]);
                    Utils.WriteStringToXML(writer, "MEMOFORMAT4", MemoFormat[3]);
                    Utils.WriteStringToXML(writer, "MEMOFORMAT5", MemoFormat[4]);
                    
                    Utils.WriteStringToXML(writer, "MRUGPX1", mruGPX[0]);
                    Utils.WriteStringToXML(writer, "MRUGPX2", mruGPX[1]);
                    Utils.WriteStringToXML(writer, "MRUGPX3", mruGPX[2]);
                    Utils.WriteStringToXML(writer, "MRUGPX4", mruGPX[3]);
                    Utils.WriteStringToXML(writer, "MRUGPX5", mruGPX[4]);

                    Utils.WriteStringToXML(writer, "MRUCGX1", mruCGX[0]);
                    Utils.WriteStringToXML(writer, "MRUCGX2", mruCGX[1]);
                    Utils.WriteStringToXML(writer, "MRUCGX3", mruCGX[2]);
                    Utils.WriteStringToXML(writer, "MRUCGX4", mruCGX[3]);
                    Utils.WriteStringToXML(writer, "MRUCGX5", mruCGX[4]);
                    
                    Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH01", TableMainColWidth[0]);
                    Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH02", TableMainColWidth[1]);
                    Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH03", TableMainColWidth[2]);
                    Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH04", TableMainColWidth[3]);
                    Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH05", TableMainColWidth[4]);
                    Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH06", TableMainColWidth[5]);
                    Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH07", TableMainColWidth[6]);
                    Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH08", TableMainColWidth[7]);
                    Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH09", TableMainColWidth[8]);
                    Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH10", TableMainColWidth[9]);
                    Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH11", TableMainColWidth[10]);
                    Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH12", TableMainColWidth[11]);
                    Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH13", TableMainColWidth[12]);
                    Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH14", TableMainColWidth[13]);
                    Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH15", TableMainColWidth[14]);
                    Utils.WriteIntToXML(writer, "TABLEMAINCOLWIDTH16", TableMainColWidth[15]);
                    
                    Utils.WriteIntToXML(writer, "UNIT", Unit);
                    Utils.WriteStringToXML(writer, "LANGUAGE", Language);
                    Utils.WriteIntToXML(writer, "MAINWINDOWSWIDTH",MainWindowWidth);
                    Utils.WriteIntToXML(writer, "MAINWINDOWSHEIGHT",MainWindowHeight);
                    Utils.WriteIntToXML(writer, "VERTSPLITPOSITION",VertSplitPosition);
                    Utils.WriteIntToXML(writer, "HORIZSPLITPOSITION",HorizSplitPosition);
                    Utils.WriteIntToXML(writer, "MRBSPLITPOSITION",MRB_SplitPosition);
                    
                writer.writeEndElement();
            writer.writeEndDocument();

            writer.flush();
            writer.close();

        } catch (XMLStreamException | IOException e) {
            System.out.println(e.getMessage());	
        }
    }

    /**
     * Load the settings from disk
     * @param _Path Path where the setting file is stored
     */
    public void Load(String _Path) {
        //-- Test if the config file exist
        if (!(new File(_Path+"/config.xml").isFile())) return;
        
        SaxConfigHandler Confighandler = new SaxConfigHandler();
        
        int ret=0;
        try{
            ret=Confighandler.readDataFromConfig(_Path+"/config.xml", this);
        }
        catch (SAXException | IOException | ParserConfigurationException e)
        {
            System.out.println(e.getMessage());
        }
        if (ret!=0)
            System.out.println("Error line ="+Confighandler.getErrLine());
    }
    
    /**
     * Return the distance unit as string
     * @return string with the unit
     */
    public String getDistanceUnitString() {
    	switch (Unit) {
    	case CgConst.UNIT_METER:
    		return "km";
    	case CgConst.UNIT_MILES_FEET:
    		return "miles";
    	default:
    		return "Km";    		
    	}
    }

    /**
     * Return the distance unit as string (abbreviation)
     * @return string with the unit
     */
    public String getShortDistanceUnitString() {
    	switch (Unit) {
    	case CgConst.UNIT_METER:
    		return "km";
    	case CgConst.UNIT_MILES_FEET:
    		return "mi";
    	default:
    		return "km";    		
    	}
    }
    
    
    /**
     * Return the elevation unit as string
     * @return string with the unit
     */
    public String getElevationUnitString() {
    	switch (Unit) {
    	case CgConst.UNIT_METER:
    		return "m";
    	case CgConst.UNIT_MILES_FEET:
    		return "feet";
    	default:
    		return "m";    		
    	}
    }

    /**
     * Return the elevation unit as string (abbreviation)
     * @return string with the unit
     */
    public String getShortElevationUnitString() {
    	switch (Unit) {
    	case CgConst.UNIT_METER:
    		return "m";
    	case CgConst.UNIT_MILES_FEET:
    		return "ft";
    	default:
    		return "m";    		
    	}
    }
    
}
