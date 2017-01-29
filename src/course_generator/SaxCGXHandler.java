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

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import course_generator.utils.CgConst;
import course_generator.utils.Utils;

public class SaxCGXHandler extends DefaultHandler{
	private java.util.ResourceBundle bundle = null;
	private Component Parent;
	private double cgx_version=0.0;
    private double totaldistance=0.0;
    private int totalsecond=0;
    private String coursename="";
    private String description="";
    private DateTime start_time=new DateTime(1970,1,1,0,0,0);
    private boolean use_elev_effect=false;
    private boolean use_night_coeff=false;
    private DateTime night_starttime=new DateTime(1970,1,1,0,0,0);
    private DateTime night_endtime=new DateTime(1970,1,1,0,0,0);
    private double night_coeff=0.0;
    private double night_coeff_desc=0.0;
    private double start_globalcoeff=100.0;
    private double end_globalcoeff=100.0;
    private double timezone=0.0;
    private boolean use_summertime=false;
    private String curve="Run_10km_h";
    private int mrb_sizew=800;
    private int mrb_sizeh=300;
     
    private Color clProfil_Simple_Fill;
    private Color clProfil_Simple_Border;
    private Color clProfil_RS_Road;
    private Color clProfil_RS_Path;
    private Color clProfil_RS_Border;
    private Color clProfil_SlopeInf5;
    private Color clProfil_SlopeInf10;
    private Color clProfil_SlopeInf15;
    private Color clProfil_SlopeSup15;
    private Color clProfil_SlopeBorder;
    
    private double trkpt_lat=0.0;
    private double trkpt_lon=0.0;
    private double trkpt_ele=0.0;
    private double trkpt_dist=0.0;
    private double trkpt_distcumul=0.0;
    private double trkpt_diff=0;
    private double trkpt_coeff=0;
    private double trkpt_recup=0;
    private double trkpt_speed=0;
    private int trkpt_timesecond=0;
    private int trkpt_eattime=0;
    private int trkpt_timelimit=0;
    private double trkpt_dtime=0.0;
    private int trkpt_optmrb=0;
    private int trkpt_vposmrb=0;
    private String trkpt_commentmrb="";
    private String trkpt_comment="";
    private String trkpt_name="";
    private int trkpt_tag=0;
    private String trkpt_fmtmrb="";
    private int trkpt_FontSizemrb=0;        
    
    private int trk_nb=0;
    private int trkseg_nb=0;
    private String characters="";
    private int old_time=0;
    
    private int level=0;
    private final int LEVEL_COURSEGENERATOR=1;
    private final int LEVEL_TRACKPOINT=2;
    
    private TrackData trkdata;
    private int mode=0;
    private int errcode=0;
    private int errline=0;
    private final int ERR_READ_NO=0;
    private final int ERR_READ_DOUBLE=-1;
    private final int ERR_READ_INT=-2;
    private final int ERR_READ_BOOL=-3;
    private final int ERR_READ_TIME=-4;
    private final int ERR_READ_VERSION=-5;
    private final int ERR_READ_NOTEXIST=-6;
    private final int ERR_READ_COLOR=-7;
    
    //-- Profil mini-roadbook
    public int MrbSizeW = 640;
    public int MrbSizeH = 480;
    public int CurveFilter = 1;
    public int WordWrapLength = 25;
    public boolean LabelToBottom = false;
    public int MRBType = 0;
    public int TopMargin = 100;
    private int Cmpt=0;
    
    private Locator locator;
    DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
    
     /**
     * Read the CGX file from disc
     * @param filename Name of the cgx file to read
     * @return The error code
     *   Erroce explanation:
     *   ERR_READ_NO = No problem during the reading of the file
     *   ERR_READ_DOUBLE = Parsing error during the read of a double element 
     *   ERR_READ_INT = Parsing error during the read of a integer element
     *   ERR_READ_BOOL = Parsing error during the read of a boolean element
     *   ERR_READ_TIME = Parsing error during the read of a time element
     *   ERR_READ_VERSION = Parsing error during the read of a version of the GPX file. Must be 1.1
     *   ERR_READ_NOTEXIST = The file doesn't exist or can't by read
     *   ERR_READ_COLOR = Parsing error during the read of a color element
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException 
     */
    public int readDataFromCGX(Component parent,String filename, TrackData TData, int readmode) throws SAXException, IOException, ParserConfigurationException{
    	// -- Initialize the string resource for internationalization
    	bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
    	Parent=parent;
        mode=readmode;
        trkdata=TData;
        cgx_version=0.0;
        errline=0;
        totaldistance=0.0;
        totalsecond=0;
        coursename="";
        description="";
        start_time=new DateTime(1970,1,1,0,0,0);
        use_elev_effect=false;
        use_night_coeff=false;
        night_starttime=new DateTime(1970,1,1,0,0,0);
        night_endtime=new DateTime(1970,1,1,0,0,0);
        night_coeff=0.0;
        night_coeff_desc=0.0;
        start_globalcoeff=100.0;
        end_globalcoeff=100.0;
        timezone=0.0;
        use_summertime=false;
        curve="Run_10km_h";
        mrb_sizew=640;
        mrb_sizeh=480;
        trk_nb=0;
        trkseg_nb=0;
        characters="";
        trkpt_lat=0.0;
        trkpt_lon=0.0;
        trkpt_ele=0.0;
        trkpt_name="";
        level=0;
        errcode=ERR_READ_NO;
        Cmpt=0;
        old_time=0;
        
        SAXParserFactory factory =SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        
        
        File f=new File(filename);
        if (f.isFile() && f.canRead()) {
            if (mode==0) trkdata.data.clear();
            parser.parse(f, this);
        }
        else
            trkdata.ReadError=ERR_READ_NOTEXIST;
        
        return trkdata.ReadError;
    }
    
    /*
    @Override
    public void startDocument() throws SAXException {
        //System.out.println("Start document");
    }

    @Override
    public void endDocument() throws SAXException {
        //System.out.println("End document");
    }
    */
    
    public int getErrLine() {
        return errline;
    }
    

    @Override
    public void setDocumentLocator(final Locator locator) {
        this.locator = locator; // Save the locator, so that it can be used later for line tracking when traversing nodes.
    }
            
    @Override
    public void startElement(String uri, String localname, String qName, Attributes attributs) throws SAXException {
        if (qName.equalsIgnoreCase("COURSEGENERATOR")) {
            level++;
        }
        else if (qName.equalsIgnoreCase("TRACKPOINT")) {
            //trk_nb++;
            level++;
        }
    }

        
    /**
     * Parse a string element
     * @return Return the parsed value
     */
    private String ManageString() {
        String S=characters;
        characters="";
        return S;
    }
    
    
    /**
     * Parse a double element
     * @param _default Default value
     * @param _errcode Error code if a parse error occur
     * @return Return the parsed value
     */
    private double ManageDouble(double _default, int _errcode) {
        try {
            Double d=Double.parseDouble(characters);
            characters="";
            return d;
        }
        catch (NumberFormatException e) {
            errcode=_errcode;
            errline=locator.getLineNumber();
            characters="";
            return _default;
        } 
    }

    /**
     * Parse a integer element
     * @param _default Default value
     * @param _errcode Error code if a parse error occur
     * @return Return the parsed value
     */
    private int ManageInt(int _default, int _errcode) {
        try {
            int i=Integer.parseInt(characters);
            characters="";
            return i;
        }
        catch (NumberFormatException e) {
            errcode=_errcode;
            errline=locator.getLineNumber();
            characters="";
            return _default;
        } 
    }
    
    /**
     * Parse a boolean element
     * @param _default Default value
     * @param _errcode Error code if a parse error occur
     * @return Return the parsed value
     */
    private boolean ManageBoolean(boolean _default, int _errcode) {
        try {
            int v=Integer.parseInt(characters);
            characters="";
            return (v==1?true:false);
        }
        catch (NumberFormatException e) {
            errcode=_errcode;
            errline=locator.getLineNumber();
            characters="";
            return _default;
        } 
    }
    
    /**
     * Parse a color element
     * @param _default Default value
     * @param _errcode Error code if a parse error occur
     * @return Return the parsed color
     */
    private Color ManageColor(Color _default, int _errcode) {
        try {
            int v=Integer.parseInt(characters);
            characters="";
            return new Color(v);
        }
        catch (NumberFormatException e) {
            errcode=_errcode;
            errline=locator.getLineNumber();
            characters="";
            return _default;
        } 
    }
    

    
    @Override
    public void endElement(String uri, String localname, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("COURSEGENERATOR")) {
            level--;
        }

        if (level==LEVEL_COURSEGENERATOR) {
            if (qName.equalsIgnoreCase("VERSION")) {
                cgx_version = ManageDouble(0.0, ERR_READ_VERSION);

                if (cgx_version>CgConst.MAX_CGX_VERSION_TO_READ) {
                    errcode=ERR_READ_VERSION;
                    errline=locator.getLineNumber();
                    //TODO Afficher message de défaut car version non lisible
                }
            }
            else if (qName.equalsIgnoreCase("TOTALDISTANCE")) {
                trkdata.setTotalDistance(ManageDouble(0.0,ERR_READ_DOUBLE));
            }
            else if (qName.equalsIgnoreCase("TOTALSECOND")) {
                trkdata.TotalTime=ManageInt(0, ERR_READ_INT);
            }
            else if (qName.equalsIgnoreCase("COURSENAME")) {
                trkdata.CourseName=ManageString();
            }
            else if (qName.equalsIgnoreCase("DESCRIPTION")) {
                trkdata.Description=ManageString();
            }
            else if (qName.equalsIgnoreCase("STARTTIME")) {
                try {
                    trkdata.StartTime=DateTime.parse(characters);
                    characters="";
                    if ( (trkdata.StartTime.getYear()==0) ||
                         (trkdata.StartTime.getMonthOfYear()==0) ||
                            (trkdata.StartTime.getDayOfMonth()==0)
                        )
                        trkdata.StartTime=new DateTime();
                }
                catch (IllegalArgumentException e)
                {
                    trkdata.StartTime=new DateTime();
                    errcode=ERR_READ_TIME;
                    errline=locator.getLineNumber();
                    characters="";
                }
            }        
            else if (qName.equalsIgnoreCase("USEELEVEFFECT")) {
                trkdata.bElevEffect=ManageBoolean(false, ERR_READ_BOOL);
            }        
            else if (qName.equalsIgnoreCase("USENIGHTCOEFF")) {
                trkdata.bNightCoeff=ManageBoolean(false, ERR_READ_BOOL);
            }        
            else if (qName.equalsIgnoreCase("NIGHTSTARTTIME")) {
                try {
                    trkdata.StartNightTime=DateTime.parse(characters,fmt);
                    characters="";
                }
                catch (IllegalArgumentException e)
                {
                    trkdata.StartNightTime=new DateTime();
                    errcode=ERR_READ_TIME;
                    errline=locator.getLineNumber();
                    characters="";
                }
            }        
            else if (qName.equalsIgnoreCase("NIGHTENDTIME")) {
                try {
                    trkdata.EndNightTime=DateTime.parse(characters, fmt);
                    characters="";
                }
                catch (IllegalArgumentException e)
                {
                    trkdata.EndNightTime=new DateTime();
                    errcode=ERR_READ_TIME;
                    errline=locator.getLineNumber();
                    characters="";
                }
            }        
            else if (qName.equalsIgnoreCase("NIGHTCOEFF")) {
                trkdata.NightCoeffAsc=ManageDouble(0.0,ERR_READ_DOUBLE);
            }
            else if (qName.equalsIgnoreCase("NIGHTCOEFFDESC")) {
                trkdata.NightCoeffDesc=ManageDouble(0.0,ERR_READ_DOUBLE);
            }
            else if (qName.equalsIgnoreCase("STARTGLOBALCOEFF")) {
                trkdata.StartGlobalCoeff=ManageDouble(100.0,ERR_READ_DOUBLE);
            }
            else if (qName.equalsIgnoreCase("ENDGLOBALCOEFF")) {
                trkdata.EndGlobalCoeff=ManageDouble(100.0,ERR_READ_DOUBLE);
            }
            else if (qName.equalsIgnoreCase("TIMEZONE")) {
                trkdata.TrackTimeZone=ManageDouble(0.0,ERR_READ_DOUBLE);
            }
            else if (qName.equalsIgnoreCase("USESUMMERTIME")) {
                trkdata.TrackUseSumerTime=ManageBoolean(false,ERR_READ_BOOL);
            }
            else if (qName.equalsIgnoreCase("CURVE")) {
                curve=ManageString();
                if (!Utils.FileExist(Utils.GetHomeDir() + "/"+CgConst.CG_DIR+"/"+ curve + ".par")) {
                	 JOptionPane.showMessageDialog(Parent, bundle.getString("loadCGX.CurveFileError"));
                	trkdata.Paramfile="Default";
                }
                else
                	trkdata.Paramfile=curve;
            }
            else if (qName.equalsIgnoreCase("MRBSIZEW")) {
                trkdata.MrbSizeW=ManageInt(640,ERR_READ_INT);
            }
            else if (qName.equalsIgnoreCase("MRBSIZEH")) {
                trkdata.MrbSizeH=ManageInt(480,ERR_READ_INT); 
            }
            else if (qName.equalsIgnoreCase("CLPROFILSIMPLEFILL")) {
            	trkdata.clProfil_Simple_Fill=ManageColor(Color.BLACK, ERR_READ_COLOR);
            }
            else if (qName.equalsIgnoreCase("CLPROFILSIMPLEBORDER")) {
            	trkdata.clProfil_Simple_Border=ManageColor(Color.BLACK, ERR_READ_COLOR);
            }
            else if (qName.equalsIgnoreCase("CLPROFILRSROAD")) {
            	trkdata.clProfil_RS_Road=ManageColor(Color.BLACK, ERR_READ_COLOR);
            }
            else if (qName.equalsIgnoreCase("CLPROFILRSPATH")) {
            	trkdata.clProfil_RS_Path=ManageColor(Color.BLACK, ERR_READ_COLOR);
            }
            else if (qName.equalsIgnoreCase("CLPROFILRSBORDER")) {
            	trkdata.clProfil_RS_Border=ManageColor(Color.BLACK, ERR_READ_COLOR);
            }
            else if (qName.equalsIgnoreCase("CLPROFILSLOPEINF5")) {
            	trkdata.clProfil_SlopeInf5=ManageColor(Color.BLACK, ERR_READ_COLOR);
            }
            else if (qName.equalsIgnoreCase("CLPROFILSLOPEINF10")) {
            	trkdata.clProfil_SlopeInf10=ManageColor(Color.BLACK, ERR_READ_COLOR);
            }
            else if (qName.equalsIgnoreCase("CLPROFILSLOPEINF15")) {
            	trkdata.clProfil_SlopeInf15=ManageColor(Color.BLACK, ERR_READ_COLOR);
            }
            else if (qName.equalsIgnoreCase("CLPROFILSLOPESUP15")) {
            	trkdata.clProfil_SlopeSup15=ManageColor(Color.BLACK, ERR_READ_COLOR);
            }
            else if (qName.equalsIgnoreCase("CLPROFILSLOPEBORDER")) {
            	trkdata.clProfil_SlopeBorder=ManageColor(Color.BLACK, ERR_READ_COLOR);
            }
            else if (qName.equalsIgnoreCase("MRBCURVEFILTER")) {
                trkdata.CurveFilter=ManageInt(4, ERR_READ_INT);
            }
            else if (qName.equalsIgnoreCase("WORDWRAPLENGTH")) {
                trkdata.WordWrapLength=ManageInt(25,ERR_READ_INT);
            }
            else if (qName.equalsIgnoreCase("LABELTOBOTTOM")) {
                trkdata.LabelToBottom=ManageBoolean(false,ERR_READ_BOOL);
            }
            else if (qName.equalsIgnoreCase("MRBTYPE")) {
                trkdata.MRBType=ManageInt(0, ERR_READ_INT);
            }
            else if (qName.equalsIgnoreCase("TOPMARGIN")) {
                trkdata.TopMargin=ManageInt(0, ERR_READ_INT);
            }
        } //End LEVEL_COURSEGENERATOR

        
        if (level==LEVEL_TRACKPOINT) {
            if (qName.equalsIgnoreCase("LATITUDEDEGREES")) {
                trkpt_lat=ManageDouble(0.0, ERR_READ_DOUBLE);
            }        
            else if (qName.equalsIgnoreCase("LONGITUDEDEGREES")) {
                trkpt_lon=ManageDouble(0.0, ERR_READ_DOUBLE);
            }        
            else if (qName.equalsIgnoreCase("ALTITUDEMETERS")) {
                trkpt_ele=ManageDouble(0.0, ERR_READ_DOUBLE);
            }        
            else if (qName.equalsIgnoreCase("DISTANCEMETERS")) {
                trkpt_dist=ManageDouble(0.0, ERR_READ_DOUBLE);
            }        
            else if (qName.equalsIgnoreCase("DISTANCEMETERSCUMUL")) {
                trkpt_distcumul=ManageDouble(0.0, ERR_READ_DOUBLE);
            }        
            else if (qName.equalsIgnoreCase("SPEED")) {
                trkpt_speed=ManageDouble(0.0, ERR_READ_DOUBLE);
            }        
            else if (qName.equalsIgnoreCase("DIFF")) {
                trkpt_diff=ManageDouble(0.0, ERR_READ_DOUBLE);
                if (cgx_version<2.0) {
                    trkpt_diff = 100.0 - (trkpt_diff - 1.0) * 100.0;
                    if ((trkpt_diff>100.0) || (trkpt_diff<=0.0)) trkpt_diff=100.0;
                }
            }        
            else if (qName.equalsIgnoreCase("COEFF")) {
                trkpt_coeff=ManageDouble(0.0, ERR_READ_DOUBLE);
                if (cgx_version<2.0) {
                    trkpt_coeff = 100.0 - (trkpt_coeff - 1.0) * 100.0;
                    if ((trkpt_coeff>100.0) || (trkpt_coeff<=0.0)) trkpt_coeff=100.0;
                }
            }        
            else if (qName.equalsIgnoreCase("RECUP")) {
                trkpt_recup=ManageDouble(0.0, ERR_READ_DOUBLE);
            }        
            else if (qName.equalsIgnoreCase("TIMESECONDE")) {
                trkpt_timesecond=ManageInt(0, ERR_READ_INT);
                trkdata.isTimeLoaded=true;
            }
            else if (qName.equalsIgnoreCase("DELTATIMESECONDE")) {
                trkpt_dtime=ManageDouble(0.0, ERR_READ_DOUBLE);
                //TODO voir si utilisé
            }
            else if (qName.equalsIgnoreCase("TIMELIMIT")) {
                trkpt_timelimit=ManageInt(0, ERR_READ_INT);
            }
            else if (qName.equalsIgnoreCase("EATTIME")) {
                trkpt_eattime=ManageInt(0, ERR_READ_INT);
            }
            else if (qName.equalsIgnoreCase("COMMENT")) {
                trkpt_name=ManageString();
            }
            else if (qName.equalsIgnoreCase("NAME")) {
                trkpt_name=ManageString();
            }
            else if (qName.equalsIgnoreCase("TAG")) {
                trkpt_tag=ManageInt(0, ERR_READ_INT);
            }
            else if (qName.equalsIgnoreCase("FMTLBMINIROADBOOK")) {
                trkpt_fmtmrb=ManageString();
            }
            else if (qName.equalsIgnoreCase("OPTMINIROADBOOK")) {
                trkpt_optmrb=ManageInt(0, ERR_READ_INT);
            }
            else if (qName.equalsIgnoreCase("VPOSMINIROADBOOK")) {
                trkpt_vposmrb=ManageInt(0, ERR_READ_INT);
            }
            else if (qName.equalsIgnoreCase("COMMENTMINIROADBOOK")) {
                trkpt_commentmrb=ManageString();
            }
            else if (qName.equalsIgnoreCase("FONTSIZEMINIROADBOOK")) {
                trkpt_FontSizemrb=ManageInt(0, ERR_READ_INT);
            }
            else if (qName.equalsIgnoreCase("TRACKPOINT")) {
                level--;
                if ((mode == 0) || (mode == 2)) {
                    // Add data at the of the array
                    Cmpt++;
                    trkdata.data.add(new CgData(
                            Cmpt, //double Num
                            trkpt_lat, //double Latitude
                            trkpt_lon, //double Longitude
                            trkpt_ele, //double Elevation
                            trkpt_ele, //double ElevationMemo
                            trkpt_tag, //int Tag
                            trkpt_dist, //double Dist
                            trkpt_distcumul, //double Total
                            trkpt_diff, //double Diff
                            trkpt_coeff, //double Coeff
                            trkpt_recup, //double Recup
                            0.0, //double Slope
                            trkpt_speed, //double Speed
                            0.0, //double dElevation
                            trkpt_timesecond, //int Time
                            trkpt_timesecond-old_time, //double dTime_f
                            trkpt_timelimit, //int TimeLimit
                            new DateTime(), //DateTime Hour
                            trkpt_eattime, //int Station
                            trkpt_name, //String Name
                            trkpt_comment, //String Comment
                            0.0, //double tmp1
                            0.0, //double tmp2
                            trkpt_fmtmrb, //String FmtLbMiniRoadbook
                            trkpt_optmrb, //int OptionMiniRoadbook
                            trkpt_vposmrb, //int VPosMiniRoadbook
                            trkpt_commentmrb, //String CommentMiniRoadbook
                            trkpt_FontSizemrb //int FontSizeMiniRoadbook
                        ) 
                    );
                }
                else {
                    trkdata.data.add(Cmpt,new CgData(
                            Cmpt, //double Num
                            trkpt_lat, //double Latitude
                            trkpt_lon, //double Longitude
                            trkpt_ele, //double Elevation
                            trkpt_ele, //double ElevationMemo
                            trkpt_tag, //int Tag
                            trkpt_dist, //double Dist
                            trkpt_distcumul, //double Total
                            trkpt_diff, //double Diff
                            trkpt_coeff, //double Coeff
                            trkpt_recup, //double Recup
                            0.0, //double Slope
                            trkpt_speed, //double Speed
                            0.0, //double dElevation
                            trkpt_timesecond, //int Time
                            trkpt_timesecond-old_time, //double dTime_f
                            trkpt_timelimit, //int TimeLimit
                            new DateTime(), //DateTime Hour
                            trkpt_eattime, //int Station
                            trkpt_name, //String Name
                            trkpt_comment, //String Comment
                            0.0, //double tmp1
                            0.0, //double tmp2
                            trkpt_fmtmrb, //String FmtLbMiniRoadbook
                            trkpt_optmrb, //int OptionMiniRoadbook
                            trkpt_vposmrb, //int VPosMiniRoadbook
                            trkpt_commentmrb, //String CommentMiniRoadbook
                            trkpt_FontSizemrb //int FontSizeMiniRoadbook
                        ) 
                    );
                } //else
                Cmpt++;
                old_time=trkpt_timesecond;
            } 
        } //End LEVEL_TRACKPOINT
                
    }

    
    @Override
    public void characters(char[] chars, int start, int end) throws SAXException {
        characters=new String(chars, start, end);
    }
        
}
