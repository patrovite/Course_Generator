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

package course_generator;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import course_generator.utils.CgConst;

/**
 *
 * @author Pierre
 */
public class SaxGPXHandler extends DefaultHandler{

    private int trk_nb=0;
    private int trkseg_nb=0;
    private String characters="";
    private String trk_name="";
    private double trkpt_lat=0.0;
    private double trkpt_lon=0.0;
    private double trkpt_ele=0.0;
    private String trkpt_name="";
    private DateTime trkpt_time;
    private int level=0;
    private final int LEVEL_GPX=1;
    private final int LEVEL_TRK=2;
    private final int LEVEL_TRKSEG=3;    
    private final int LEVEL_TRKPT=4;
    private double gpx_version=0;
    
    
    private double mLat;
    private double mLon;
    private double mEle;
    private DateTime old_time;

    private int errcode=0;
    private Locator locator;
    private int errline=0;

    private final int ERR_READ_NO=0;
    private final int ERR_READ_LAT=-1;
    private final int ERR_READ_LON=-2;
    private final int ERR_READ_ELE=-3;
    private final int ERR_READ_TIME=-4;
    private final int ERR_READ_VERSION=-5;
    private final int ERR_READ_NOTEXIST=-6;
    private TrackData trkdata;
    private int mode=0;
    private boolean first=true;
    private DateTime StartTime;
    private int Time_s;
    private double dTime_f;
    private int Cmpt=0;
    
    /**
     * Read the GPX file from disc
     * @param filename Name of the gpx file to read
     * @param TData TrackData object where to store the read data
     * @param readmode Reading mode
     *  0=Load the complete file
     *  1=Insert the read data at the beginning of the current track
     *  2=Insert the read data at the end of the current track
     * @return The error code
     *   Erroce explanation:
     *   ERR_READ_NO = No problem during the reading of the file
     *   ERR_READ_LAT = Parsing error during the read of a latitude (lat) element 
     *   ERR_READ_LON = Parsing error during the read of a longitude (lon) element
     *   ERR_READ_ELE = Parsing error during the read of a elevation (ele) element
     *   ERR_READ_TIME = Parsing error during the read of a time (time) element
     *   ERR_READ_VERSION = Parsing error during the read of a version of the GPX file. Must be 1.1
     *   ERR_READ_NOTEXIST = The file doesn't exist or can't by read
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException 
     */
    public int readDataFromGPX(String filename, TrackData TData, int readmode) throws SAXException, IOException, ParserConfigurationException{
        
        mode=readmode;
        trkdata=TData;
        trk_nb=0;
        trkseg_nb=0;
        characters="";
        trk_name="";
        trkpt_lat=0.0;
        trkpt_lon=0.0;
        trkpt_ele=0.0;
        mLat = 0.0;
        mLon = 0.0;
        mEle = 0.0;        
        Time_s=0;
        Cmpt = 0;
        dTime_f=0.0;
        
        trkpt_name="";
        trkpt_time=new DateTime(1970,1,1,0,0,0);
        level=0;
        errcode=ERR_READ_NO;
        gpx_version=0.0;
        first=true;
        StartTime=new DateTime(1970,1,1,0,0,0);
        
        trkdata.ReadError=0;
        
        SAXParserFactory factory =SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        
        File f=new File(filename);
        if (f.isFile() && f.canRead()) {
            //-- ok let's go
            
            //-- Clear the DataList? --
            if (mode==0) trkdata.data.clear();
            
            //-- Parse the file
            parser.parse(f, this);
        }
        else
            trkdata.ReadError=ERR_READ_NOTEXIST;
        
        return trkdata.ReadError;
    }
    
    public int getErrLine() {
        return errline;
    }
    
    @Override
    public void setDocumentLocator(final Locator locator) {
        this.locator = locator; // Save the locator, so that it can be used later for line tracking when traversing nodes.
    }
    
    @Override
    public void startElement(String uri, String localname, String qName, Attributes attributs) throws SAXException {
        if (qName.equalsIgnoreCase("GPX")) {
            level++;
            // Check all the attributes
            for (int index = 0; index < attributs.getLength(); index++) { 
                if (attributs.getLocalName(index).equalsIgnoreCase("VERSION")) {
                    try {
                        gpx_version=Double.parseDouble(attributs.getValue(index));
                    }
                    catch (NumberFormatException e) {
                        gpx_version=0.0;
                    } 
                    if (gpx_version!=1.1)
                        errcode=ERR_READ_VERSION;
                }
                else if (attributs.getLocalName(index).equalsIgnoreCase("LON"))
                {
                    try {
                        trkpt_lon=Double.parseDouble(attributs.getValue(index));
                    }
                    catch (NumberFormatException e) {
                        trkpt_lon=0.0;
                        errcode=ERR_READ_LON;
                        errline=locator.getLineNumber();
                    }                    
                }
            }
        }
        else if (qName.equalsIgnoreCase("TRK")) {
            trk_nb++;
            level++;
        }
        else if (qName.equalsIgnoreCase("TRKSEG")) {
            trkseg_nb++;
            level++;
        }
        else if (qName.equalsIgnoreCase("TRKPT")) {
            level++;
            for (int index = 0; index < attributs.getLength(); index++) { // on parcourt la liste des attributs
                if (attributs.getLocalName(index).equalsIgnoreCase("LAT")) {
                    try {
                        trkpt_lat=Double.parseDouble(attributs.getValue(index));
                    }
                    catch (NumberFormatException e) {
                        trkpt_lat=0.0;
                        errcode=ERR_READ_LAT;
                        errline=locator.getLineNumber();
                    } 
                }
                else if (attributs.getLocalName(index).equalsIgnoreCase("LON"))
                {
                    try {
                        trkpt_lon=Double.parseDouble(attributs.getValue(index));
                    }
                    catch (NumberFormatException e) {
                        trkpt_lon=0.0;
                        errcode=ERR_READ_LON;
                        errline=locator.getLineNumber();
                    }                    
                }
            }
        }
    }

    
    @Override
    public void endElement(String uri, String localname, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("GPX")) {
            level--;
        }
        else if (qName.equalsIgnoreCase("TRK")) {
            level--;
        }
        else if (qName.equalsIgnoreCase("TRKSEG")) {
            level--;
        }
        else if ((level==LEVEL_TRK) && qName.equalsIgnoreCase("NAME")) {
            trk_name=characters;
            characters="";
        }
        else if ((level==LEVEL_TRKPT) && qName.equalsIgnoreCase("NAME")) {
            trkpt_name=characters;
            characters="";
        }        
        else if ((level==LEVEL_TRKPT) && qName.equalsIgnoreCase("ELE")) {
            try {
                trkpt_ele=Double.parseDouble(characters);
                characters="";
            }
            catch (NumberFormatException e) {
                trkpt_ele=0.0;
                errcode=ERR_READ_ELE;
                errline=locator.getLineNumber();
                characters="";
            } 
        }        
        else if ((level==LEVEL_TRKPT) && qName.equalsIgnoreCase("TIME")) {
            try {
                if (first) {
                    first = false;
                    trkpt_time=DateTime.parse(characters);
                    StartTime = trkpt_time;
                    old_time=StartTime;
                    Time_s = 0;
                    dTime_f = 0.0;
                    characters="";
                }
                else {
                    trkpt_time=DateTime.parse(characters);
                    Time_s=(int) ((new Interval(StartTime, trkpt_time)).toDurationMillis()/1000);
                    dTime_f=((new Interval(old_time, trkpt_time)).toDurationMillis()/1000.0);
                    characters="";
                }
                trkdata.isTimeLoaded = true;            
            }
            catch (IllegalArgumentException e)
            {
                trkpt_time=new DateTime(1970,1,1,0,0,0);
                errcode=ERR_READ_TIME;
                errline=locator.getLineNumber();
                characters="";
            }
        }        
        else if (((level==LEVEL_TRKPT)) && qName.equalsIgnoreCase("TRKPT")) {
            level--;
            
            if ( (mLat!=trkpt_lat) || (mLon!=trkpt_lon) || (!trkdata.isTimeLoaded)) {
                if ((mode == 0) || (mode == 2)) {
                    // Add data at the of the array
                    Cmpt++;
                    trkdata.data.add(new CgData(
                            Cmpt, //double Num
                            trkpt_lat, //double Latitude
                            trkpt_lon, //double Longitude
                            trkpt_ele, //double Elevation
                            trkpt_ele, //double ElevationMemo
                            0, //int Tag
                            0.0, //double Dist
                            0.0, //double Total
                            100.0, //double Diff
                            100.0, //double Coeff
                            0.0, //double Recup
                            0.0, //double Slope
                            0.0, //double Speed
                            0.0, //double dElevation
                            Time_s, //int Time //Temps total en seconde
                            dTime_f, //double dTime_f  //temps de parcours du tronçon en seconde (avec virgule)
                            0, //int TimeLimit //Barrière horaire
                            trkpt_time, //DateTime Hour //Contient la date et l'heure de passage
                            0, //int Station
                            "", //String Name
                            "", //String Comment
                            0.0, //double tmp1
                            0.0, //double tmp2
                            CgConst.DEFAULTMRBFORMAT, //String FmtLbMiniRoadbook
                            CgConst.MRBOPT_SEL | CgConst.MRBOPT_LEFT | CgConst.MRBOPT_SHOWTAGS, //int OptionMiniRoadbook
                            0, //int VPosMiniRoadbook
                            "", //String CommentMiniRoadbook
                            CgConst.DEFAULTMRBFONTSIZE //int FontSizeMiniRoadbook
                        ) 
                    );
                }
                else {
                    trkdata.data.add(Cmpt, new CgData(
                            Cmpt, //double Num
                            trkpt_lat, //double Latitude
                            trkpt_lon, //double Longitude
                            trkpt_ele, //double Elevation
                            trkpt_ele, //double ElevationMemo
                            0, //int Tag
                            0.0, //double Dist
                            0.0, //double Total
                            100.0, //double Diff
                            100.0, //double Coeff
                            0.0, //double Recup
                            0.0, //double Slope
                            0.0, //double Speed
                            0.0, //double dElevation
                            Time_s, //int Time
                            dTime_f, //double dTime_f
                            0, //int TimeLimit
                            trkpt_time, //DateTime Hour
                            0, //int Station
                            "", //String Name
                            "", //String Comment
                            0.0, //double tmp1
                            0.0, //double tmp2
                            CgConst.DEFAULTMRBFORMAT, //String FmtLbMiniRoadbook
                            CgConst.MRBOPT_SEL | CgConst.MRBOPT_LEFT | CgConst.MRBOPT_SHOWTAGS, //int OptionMiniRoadbook
                            0, //int VPosMiniRoadbook
                            "", //String CommentMiniRoadbook
                            CgConst.DEFAULTMRBFONTSIZE //int FontSizeMiniRoadbook
                        ) 
                    );
                } //else
                Cmpt++;
            }
            mLat = trkpt_lat;
            mLon = trkpt_lon;
            mEle = trkpt_ele;
            old_time = trkpt_time;
        } 
    }

    
    @Override
    public void characters(char[] chars, int start, int end) throws SAXException {
        characters=new String(chars, start, end);
    }
    

    
}
