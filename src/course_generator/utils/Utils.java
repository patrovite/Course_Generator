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

import course_generator.CgData;
import course_generator.TrackData;
import course_generator.TrackData.CalcClimbResult;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
//import org.jdom2.Element;
import org.joda.time.DateTime;

/**
 *
 * @author pierre.delore
 */
public class Utils {
    //static private CultureInfo cultureEN = CultureInfo.CreateSpecificCulture("en-GB");
    //static private CultureInfo cultureFR = CultureInfo.CreateSpecificCulture("fr-FR");

	
	/**
     * Display a load dialog
     * @param Parent Parent windows
     * @param Directory Directory to select when the dialog is displayed
     * @param Extension File extention (ie: ".myp")
     * @param FilterText Text filter
     * @return Filename with path. Empty if cancel
     */
    public static String LoadDialog(Component Parent, String Directory, String Extension, String FilterText) {
    
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(Directory));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        FileFilter mypFilter = new FileTypeFilter(Extension, FilterText);
        fileChooser.addChoosableFileFilter(mypFilter);
        fileChooser.setFileFilter(mypFilter);

        int result = fileChooser.showOpenDialog(Parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }
        else
            return "";
    }
    
    
    /**
     * Display a save dialog
     * @param Parent Parent windows
     * @param Directory Directory to select when the dialog is displayed
     * @param DefaultFileName Default filename. Empty string if none
     * @param Extension File extention (ie: ".myp")
     * @param FilterText Text filter (ie: "File GPX (*.gpx)|*.gpx")
     * @param TestFileExist Test the file exist
     * @param FileExistText Text displayed if the selected file exist (Over write confirmation)
     * @return Filename with path. Empty if cancel
     */
    public static String SaveDialog(Component Parent, String Directory, String DefaultFileName, String Extension, String FilterText, Boolean TestFileExist, String FileExistText) {
        JFileChooser fileChooser = new JFileChooser();
        
        if (DefaultFileName.isEmpty())
            fileChooser.setCurrentDirectory(new File(Directory));
        else
            fileChooser.setSelectedFile(new File(DefaultFileName));
        
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);

        FileFilter mypFilter = new FileTypeFilter(Extension, FilterText);
        fileChooser.addChoosableFileFilter(mypFilter);
        fileChooser.setFileFilter(mypFilter);
        
        
        int result = fileChooser.showSaveDialog(Parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            String selectedFile = fileChooser.getSelectedFile().getAbsolutePath();
            if (Utils.GetFileExtension(selectedFile).isEmpty()) {
                selectedFile = selectedFile + Extension;
            }
            boolean ok=true;
            if (Utils.FileExist(selectedFile) && (TestFileExist)) {
                if (JOptionPane.showConfirmDialog(Parent,
                        FileExistText,
                        "",
                        JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) 
                    ok=false;
            }
            if (ok) 
                return selectedFile;
            else
                return "";
        }
        else return "";
    }
    
	 /**
     * Parse a string containing a double. The separator can be "." or ","
     * @param s Input string containing the double 
     * @return Parsed value in double format
     * @throws ParseException 
     */
    public static double ParseDoubleEx(String s, double value_if_fault) { //throws ParseException{
        int pos, step;
        double v;
        String str, s1;
        boolean ok, error;

        char decimalseparator=new DecimalFormat().getDecimalFormatSymbols().getDecimalSeparator();
        
        v = 0.0;
        str = "";

        //-- Input string empty
        s1 = s.trim();
        if (s1.isEmpty()) {
            return value_if_fault; //Return the fault value
        }

        //-- Let go
        ok = true;
        error = false;
        step = 0;
        pos = 0;
        while ((pos < s1.length()) && (ok) && (!error)) {
            switch (step) {
                case 0: { //'+' ou '-'
                     if (s1.charAt(pos)=='+')  {
                        str = str + s1.charAt(pos);
                        pos++;
                     }
                     else if (s1.charAt(pos)=='-')  {
                        str = str + s1.charAt(pos);
                        pos++;
                     }
                    step = 1;
                    break;
                } //Case 0

                case 1: { //Digit?
                    if (Character.isDigit(s1.charAt(pos))) {
                        str = str + s1.charAt(pos);
                        pos++;
                    } else { //Separator?
                        if ((s1.charAt(pos)==',') || (s1.charAt(pos)=='.')) {
                            str = str + decimalseparator;
                            pos++;
                            step = 2;
                        }
                        else {
                        	error=true;
                        	//step = 10;
                        }
                    }
                    break;
                } // Case 1

                case 2: { //Digit?
                    if (Character.isDigit(s1.charAt(pos))) {
                        str = str + s1.charAt(pos);
                        pos++;
                    } else {
                        error = true;
                    }
                    break;
                } // Case 2
//                case 10: { //Error
//                	error=true;
//                	ok=false;
//                	break;
//                }
                    
            } //Case
        } //While

        if (error)
        	v = value_if_fault;
        else {
	        //-- Convert the string to double
	        try {
	            DecimalFormat decimalFormat = new DecimalFormat("#");
	            v = decimalFormat.parse(str).doubleValue();
	        } catch (ParseException e) {
	        	// A problem! set the "fault" value
	            v = value_if_fault;
	        }
        }
            
        return v;
    }
	
    
	/**
     * Define the parameter for the gridbaglayout constraints
     * @param container Container where to place the component
     * @param component Component to place
     * @param xPos X position in the grid
     * @param yPos Y position in the grid
     * @param compWidth Component width
     * @param compHeight Component height
     * @param weightX X weight (1=can be resized in X)
     * @param weightY Y weight (1=can be resized in Y)
     * @param insetTop Inner top space in pixel
     * @param insetLeft Inner left space in pixel
     * @param insetBottom Inner bottom space in pixel
     * @param insetRight Inner right space in pixel
     * @param anchor Position of the component in the "cell"
     * @param stretch How the component will fill the "cell"
     */
    public static void addComponent(Container container, JComponent component, 
    		int xPos, int yPos, 
    		int width, int height, 
    		double weightX , double weightY, 
    		int insetTop, int insetLeft, int insetBottom, int insetRight, 
    		int anchor, int stretch){
		
		GridBagConstraints gridConstraints = new GridBagConstraints();
		
		gridConstraints.gridx = xPos;
		gridConstraints.gridy = yPos;
		gridConstraints.gridwidth = width;
		gridConstraints.gridheight = height;
		gridConstraints.weightx = weightX;
		gridConstraints.weighty = weightY;
		gridConstraints.insets = new Insets(insetTop, insetLeft, insetBottom, insetRight);
		gridConstraints.anchor = anchor;
		gridConstraints.fill = stretch;
		
		container.add(component, gridConstraints);
		
	}	
    
            
    //Converti des mètres en pieds
    public static double Meter2Feet(double m) {
        return m * 3.28083989501;
    }

    //Converti des pieds en mètres
    public static double Feet2Meter(double f) {
        return f * 0.3048;
    }

    //Convert meters to 1/1000 miles
    public static double Meter2uMiles(double m) {
        return m * 0.62137119223;
    }

    //Converti des miles en mètres
    public static double Miles2Meter(double m) {
        return m * 1.609344 * 1000;
    }

    //Converti des kilomètres en miles
    public static double Km2Miles(double m) {
        return m * 0.62137119223;
    }

    //Converti des miles en kilomètres
    public static double Miles2Km(double m) {
        return m * 1.609344;
    }

    //-- Converti des secondes en HMS sous forme de string -- 
    public static String Second2DateString(int v) {
        int nbh = v / 3600;
        int nbm = (v % 3600) / 60;
        int nbs = (v % 3600) % 60;

        return String.format("%02d:%02d:%02d ",nbh,nbm,nbs);
    }

    //-- Converti des secondes en HM sous forme de string (Pas de secondes)-- 
    public static String Second2DateString_HM(int v) {
        int nbh = v / 3600;
        int nbm = (v % 3600) / 60;

        return String.format("%02d:%02d:%02d ",nbh,nbm);
    }

    //-- Calculate the distance between two GPS points (with the elevation) --
    public static double CalcDistance(double lat1, double lon1, double lat2, double lon2) {
        double a, c, dDistance, dLat1InRad, dLong1InRad, dLat2InRad, dLong2InRad, dLongitude, dLatitude;
        double kEarthRadiusKms;

          //CalcDistance = Math.Acos(Math.Sin(ToRadians(lat1)) * Math.Sin(ToRadians(lat2)) + Math.Cos(ToRadians(lat1)) * Math.Cos(ToRadians(lat2)) * Math.Cos(ToRadians(lon1 - lon2))) * 6378D * 1000D
        kEarthRadiusKms = 6378.14; //6376.5

        dDistance = 0; //Double.MinValue
        dLat1InRad = lat1 * (Math.PI / 180.0);
        dLong1InRad = lon1 * (Math.PI / 180.0);
        dLat2InRad = lat2 * (Math.PI / 180.0);
        dLong2InRad = lon2 * (Math.PI / 180.0);

        dLongitude = dLong2InRad - dLong1InRad;
        dLatitude = dLat2InRad - dLat1InRad;

        //Intermediate result a.
        a = Math.pow(Math.sin(dLatitude / 2.0), 2.0) + Math.cos(dLat1InRad) * Math.cos(dLat2InRad) * Math.pow(Math.sin(dLongitude / 2.0), 2.0);

        //Intermediate result c (great circle distance in Radians)
        c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));

        //Distance.
        dDistance = kEarthRadiusKms * c;

        //Résultat en mètres
        return dDistance * 1000.0;
    }

    
    public static class CalcLineResult{ 
        public double a, b;
        /*
        public CalcLineResult()
        {
            a=b=0.0;
        }*/
    }
    
    //-- Calculate 'a' and 'b' from Y=aX+b --
    public static CalcLineResult CalcLine(double x1, double y1, double x2, double y2, CalcLineResult r) {
        r.a = (y1 - y2) / (x1 - x2);
        r.b = ((y1 * x2) - (y2 * x1)) / (x2 - x1);
        return r; 
    }

    //Compare t1 and t2
    // Return 0 if t1=t2
    // Return 1 if t1>t2
    // Return -1 if t1<t2
    public static int CompareHMS(DateTime t1, DateTime t2)
    {
        int h1,h2,m1,m2,s1,s2;
        
        h1=t1.getHourOfDay();
        m1=t1.getMinuteOfHour();
        s1=t1.getSecondOfMinute();

        h2=t2.getHourOfDay();
        m2=t2.getMinuteOfHour();
        s2=t2.getSecondOfMinute();
        
        if ( (h1==h2) && (m1==m2) && (s1==s2) )
            return 0;
        if ( (h1>h2) 
           || ((h1==h2) && (m1>m2))
           || ((h1==h2) && (m1==m2) && (s1>s2))
           ) return 1;
        return -1;
    }
    
    
    
    public static void WriteStringToXML(XMLStreamWriter writer, String Element, String Data) {
        try {
            writer.writeStartElement(Element);
            writer.writeCharacters(Data);
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    public static void WriteIntToXML(XMLStreamWriter writer, String Element, int Data) {
        try {
            writer.writeStartElement(Element);
            writer.writeCharacters(Integer.toString(Data));
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    public static void WriteDoubleToXML(XMLStreamWriter writer, String Element, double Data) {
        try {
            writer.writeStartElement(Element);
            writer.writeCharacters(Double.toString(Data));
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
    
    public static void WriteBooleanToXML(XMLStreamWriter writer, String Element, boolean Data) {
        try {
            writer.writeStartElement(Element);
            writer.writeCharacters((Data?"1":"0"));
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
    
    
    //checks for connection to the internet through dummy request
    public static boolean isInternetReachable()
    {
        try {
            //make a URL to a known source
            URL url = new URL("http://www.google.com");

            //open a connection to that source
            HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();

            //trying to retrieve data from the source. If there
            //is no connection, this line will fail
            Object objData = urlConnect.getContent();

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            return false;
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            return false;
        }
        return true;
    }
    
    /*
      //-- Détermine la couleur entre rouge et blanc --
    //-- V = entre 0 et 100 --
    public static Color GradientRed(int v) {
        //Blanc R=255 G=255 B=255
        //Jaune R=255 G=255 B=0
        //Rouge R=255 G=0 B=0

        int r, g, b = 0;

        if (v > 100) {
            v = 100;
        }
        if (v < 0) {
            v = 0;
        }
        int step = (0 + 256 + 256) / 100;
        int Val = v * step;

        r = 255;

        if (Val <= 255) {
            g = 255;
            b = 255 - Val;
        } else {
            g = 255 - (Val - 256);
            b = 0;
        }
        return new Color(r, g, b);
    }

    //-- Détermine la couleur entre Bleu et blanc --
    public static Color GradientBlue(int v) {
          //Blanc R=255 G=255 B=255
        //Vert R=0 G=255 B=0

          //R=0   G=170 B=0
        //R=255 G=255 B=255
        int r = 64;
        int g = 64;
        int b = 255;

        if (v > 100) {
            v = 100;
        }
        if (v < 0) {
            v = 0;
        }

        double step_rb = 256.0 / 100.0;
        //double step_g = 196.0 / 100.0;
        int Val_rb = (int) (v * step_rb);
        //int Val_g = (int)(v * step_g);

        g = 255 - Val_rb;
        if (g < 0) {
            g = 0;
        }
          //b = 255 - Val_rb;
        //if (b < 0) b = 0;
        r = 255 - Val_rb;
        if (r < 0) {
            r = 0;
        }
        return new Color(255, r, g, b);

    }

    public static Color ColorFromHSV(double hue, double saturation, double value) {
        int hi = Convert.ToInt32(Math.Floor(hue / 60)) % 6;
        double f = hue / 60 - Math.Floor(hue / 60);

        value = value * 255;
        int v = Convert.ToInt32(value);
        int p = Convert.ToInt32(value * (1 - saturation));
        int q = Convert.ToInt32(value * (1 - f * saturation));
        int t = Convert.ToInt32(value * (1 - (1 - f) * saturation));

        if (hi == 0) {
            return Color.FromArgb(255, v, t, p);
        } else if (hi == 1) {
            return Color.FromArgb(255, q, v, p);
        } else if (hi == 2) {
            return Color.FromArgb(255, p, v, t);
        } else if (hi == 3) {
            return Color.FromArgb(255, p, q, v);
        } else if (hi == 4) {
            return Color.FromArgb(255, t, p, v);
        } else {
            return Color.FromArgb(255, v, p, q);
        }
    }

      // Given H,S,L in range of 0-1
    // Returns a Color (RGB struct) in range of 0-255
    public static Color HSL2RGB(double h, double sl, double l) {
        double v;
        double r, g, b;

        r = l;   // default to gray
        g = l;
        b = l;
        v = (l <= 0.5) ? (l * (1.0 + sl)) : (l + sl - l * sl);

        if (v > 0) {
            double m;
            double sv;
            int sextant;
            double fract, vsf, mid1, mid2;

            m = l + l - v;
            sv = (v - m) / v;
            h *= 6.0;
            sextant = (int) h;
            fract = h - sextant;
            vsf = v * sv * fract;
            mid1 = m + vsf;
            mid2 = v - vsf;
            switch (sextant) {
                case 0:
                    r = v;
                    g = mid1;
                    b = m;
                    break;
                case 1:
                    r = mid2;
                    g = v;
                    b = m;
                    break;
                case 2:
                    r = m;
                    g = v;
                    b = mid1;
                    break;
                case 3:
                    r = m;
                    g = mid2;
                    b = v;
                    break;
                case 4:
                    r = mid1;
                    g = m;
                    b = v;
                    break;
                case 5:
                    r = v;
                    g = m;
                    b = mid2;
                    break;
            }
        }
        return Color.FromArgb(Convert.ToByte(r * 255.0f), Convert.ToByte(g * 255.0f), Convert.ToByte(b * 255.0f));
    }
    */
    
    //TODO Inputbox à traiter
    /*
    public static DialogResult InputBox(string title, string promptText, ref string
    value,int maxlen

    
        )
      {
        Form form = new Form();
        Label label = new Label();
        TextBox textBox = new TextBox();
        Button buttonOk = new Button();
        Button buttonCancel = new Button();

        form.Text = title;
        form.StartPosition = FormStartPosition.CenterParent;
        form.TopMost = true;

        label.Text = promptText;
        textBox.Text = value;
        if (maxlen > 0) {
            textBox.MaxLength = maxlen;
        }

        buttonOk.Text = "OK";
        buttonCancel.Text = "Cancel";
        buttonOk.DialogResult = DialogResult.OK;
        buttonCancel.DialogResult = DialogResult.Cancel;

        label.SetBounds(9, 20, 372, 13);
        textBox.SetBounds(12, 36, 372, 20);
        buttonOk.SetBounds(228, 72, 75, 23);
        buttonCancel.SetBounds(309, 72, 75, 23);

        label.AutoSize = true;
        textBox.Anchor = textBox.Anchor | AnchorStyles.Right;
        buttonOk.Anchor = AnchorStyles.Bottom | AnchorStyles.Right;
        buttonCancel.Anchor = AnchorStyles.Bottom | AnchorStyles.Right;

        form.ClientSize = new Size(396, 107);
        form.Controls.AddRange(new Control[]{label, textBox, buttonOk, buttonCancel});
        form.ClientSize = new Size(Math.Max(300, label.Right + 10), form.ClientSize.Height);
        form.FormBorderStyle = FormBorderStyle.FixedDialog;
        form.StartPosition = FormStartPosition.CenterScreen;
        form.MinimizeBox = false;
        form.MaximizeBox = false;
        form.AcceptButton = buttonOk;
        form.CancelButton = buttonCancel;

        DialogResult dialogResult = form.ShowDialog();
        value = textBox.Text;
        return dialogResult;
    }
    */
    
    public static String GetHomeDir() {
        //return Environment.GetEnvironmentVariable("USERPROFILE");
        return System.getProperty("user.home");
    }

    /*
    public static String GetMyDocumentsDir() {
        return Environment.GetFolderPath(Environment.SpecialFolder.Personal);
    }
    */

    //TODO Valider que c'est multiplateforme!
    public static String GetTempDir() {
        return System.getProperty("java.io.tmpdir");
    }

    /* Supprimé car pas multiplateforme
    public static String GetAppDataDir() {
        return Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData);
    }
    
    
    public static String GetCommonAppDataDir() {
        return Environment.GetFolderPath(Environment.SpecialFolder.CommonApplicationData);
    }
    */
    
      /// Vérifie si un bit est à 1.
    /// </summary>
    /// <param name="Value">Valeur à contrôler.</param>
    /// <param name="Bit">Position du bit.</param>
    /// <returns>True si le bit est 1, false s'il est à 0.</returns>
    public static boolean IsBitOn(int Value, byte Bit) {
        return (Value >> Bit & 1) == 1;
    }

      /// <summary>
    /// Met à 1 le bit d'un mot.
    /// </summary>
    /// <param name="Value">Valeur du mot à modifier.</param>
    /// <param name="Bit">Position du bit.</param>
    /// <returns>Nouvelle valeur du mot.</returns>
    //public static int SetBit(int Value, byte Bit)
    //{ return BitSetEx(Value, Bit, true); }
      /// <summary>
    /// Modifie la valeur d'un mot.
    /// </summary>
    /// <param name="Value">Valeur du mot à modifier.</param>
    /// <param name="Bit">Position du bit.</param>
    /// <param name="On">Si True met le bit à 1, si False met le bit à 0.</param>
    /// <returns>Nouvelle valeur du mot.</returns>
    //public static int BitSetEx(int Value, byte Bit, bool On)
    //{ return On ? Value | (1 << Bit) : ResetBit(Value, Bit); }
      /// <summary>
    /// Modifie la valeur d'un mot.
    /// </summary>
    /// <param name="Value">Référence sur le mot à modifier.</param>
    /// <param name="Bit">Position du bit.</param>
    /// <param name="On">Si True met le bit à 1, si False met le bit à 0.</param>
    /// <returns>Nouvelle valeur du mot.</returns>
      /*
     public static void BitSet(ref int Value, byte Bit, bool On)
     {
     if (On)
     Value=Value | (1 << Bit);
     else
     Value=ResetBit(Value, Bit); 
     }
     */
      /// <summary>
    /// Met à 0 le bit d'un mot.
    /// </summary>
    /// <param name="Value">Valeur du mot à modifier.</param>
    /// <param name="Bit">Position du bit.</param>
    /// <returns>Nouvelle valeur du mot.</returns>
    //public static int ResetBit(int Value, byte Bit)
    //{ return Value & ~(1 << Bit); }
      /// <summary>
    /// Met à 0 le bit d'un mot.
    /// </summary>
    /// <param name="Value">Valeur du mot à modifier.</param>
    /// <param name="Mask">Masque.</param>
    /// <returns>Nouvelle valeur du mot.</returns>
    public static int Reset(int Value, int Mask) {
        return Value & ~Mask;
    }

      /// <summary>
    /// Met à 1 le bit d'un mot.
    /// </summary>
    /// <param name="Value">Valeur du mot à modifier.</param>
    /// <param name="Mask">Masque.</param>
    /// <returns>Nouvelle valeur du mot.</returns>
    public static int Set(int Value, int Mask) {
        return Value | Mask;
    }

    /// <summary>
    /// Word wraps the given text to fit within the specified width.
    /// </summary>
    /// <param name="text">Text to be word wrapped</param>
    /// <param name="width">Width, in characters, to which the text
    /// should be word wrapped</param>
    /// <param name="trim">true=The is trimmed false=no modification</param>
    /// <returns>The modified text</returns>
    public static String WordWrap(String s, int l, boolean trim) {
        String sr = "";
        String st = "";

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != '\n') {
                st = st + s.charAt(i);
            } else {
                sr = sr + WordWrapOneLine(st, l, true) + '\n';
                st = "";
            }
        }
        if (st.length() > 0) {
            sr = sr + WordWrapOneLine(st, l, true);
        }
        return sr;
    }

    public static String WordWrapOneLine(String text, int width, boolean trim) {
        int ps = 0;
        int pe = 0;
        int pt = 0;
        String r = "";
        int step = 0;

        //Supression des essais de début et de fin
        if (trim) {
            text = text.trim();
        }

        while (step != 100) {
            switch (step) {
                //Ajout de la taille
                case 0: {
                    if (ps + width < text.length()) {
                        pe = ps + width;
                        step = 1;
                    } else {
                        pe = text.length() - 1;
                        step = 3;
                    }
                    break;
                }

                //Recherche arrière du premier espace
                case 1: {
                    pt = pe;
                    while ((text.charAt(pe) != ' ') && (pe > 0) && (pe > ps)) {
                        pe--;
                    }
                    if ((pe == ps) || (pe == 0)) {
                        pe = pt;
                        step = 5; //Recherche avant du premier espace car dépassement taille
                    } else {
                        step = 2;
                    }
                    break;
                }

                //Recherche arrière du premier non-espace
                case 2: {
                    pt = pe;
                    while ((text.charAt(pe) == ' ') && (pe > 0) && (pe > ps)) {
                        pe--;
                    }
                    if ((pe == ps) || (pe == 0)) {
                        pe = pt;
                        step = 99; //Recherche avant du premier espace
                    } else {
                        step = 3;
                    }
                    break;
                }

                //Copie de ps..pe vers la chaine résultat
                case 3: {
                    for (int i = ps; i <= pe; i++) {
                        r = r + text.charAt(i);
                    }
                    pe++;
                    if (pe >= text.length()) {
                        step = 99;
                    } else {
                        r = r + '\n';
                        ps = pe;
                        step = 4;
                    }
                    break;
                }

                //Recherche avant du premier non-espace
                case 4: {
                    while ((text.charAt(ps) == ' ') && (ps < text.length())) {
                        ps++;
                    }
                    if (ps > text.length()) {
                        step = 99;
                    } else {
                        pe = ps;
                        step = 0;
                    }
                    break;
                }

                //Recherche avant du premier espace car dépassement taille
                case 5: {
                    while ((text.charAt(pe) != ' ') && (pe < text.length() - 1)) {
                        pe++;
                    }
                    if (pe >= text.length()) {
                        step = 99;
                    } else {
                        //pe = ps;
                        step = 3;
                    }
                    break;
                }

                case 99: //Fin du traitement
                {
                    step = 100;
                    break;
                }

            }

        }
        return r;
    }

    public static String GenLabel(String s, CgData r, TrackData cd) {
        /*
         * %N:Nom
         * %A:Altitude
         * %D:Distance depuis le départ
         * %T:Temps (hh:mm) 
         * %Ts:Temps (hh:mm) 
         * %Tl:Temps (hh:mm:ss) 
         * %H:Heure (ddd hh:mm)
         * %h:Heure (hh:mm)
         * %hs:Heure (hh:mm)
         * %hl:Heure (hh:mm:s)
         * %B:Barrière horaire (hh:mm) -> Temps depuis le départ
         * %b:Barrière horaire (hh:mm) -> Heure limite
         * %C:Commentaire
         * %c:Commentaire venant du tableau principal
         * %L:Retour à la ligne
         * %R:Temps de ravitaillement (hh:mm)
         * %Rs:Temps de ravitaillement (hh:mm)
         * %Rl:Temps de ravitaillement (hh:mm:ss)
         * %+:Cumul du D+
         * %-:Cumul du D-
         */
        int i = 0;
        int step = 0;
        double cp = 0.0;
        double cm = 0.0;
        int tm = 0;
        int tp = 0;
        String sr = "";
        DateTime dt;

        if (cd != null) {
            CalcClimbResult res=new CalcClimbResult();
            res=cd.CalcClimb(0, (int) (r.getNum() - 1), res);
            /*
            cd.CalcClimb(0, (int) (r.Num - 1), ref 
            }cp
            , ref cm, ref tm
            , ref tp
            );
                    */

            for (i = 0; i < s.length(); i++) {
                switch (step) {
                    case 0: {
                        if (s.charAt(i) == '%') {
                            step = 1;
                        } else {
                            sr = sr + s.charAt(i);
                        }
                        break;
                    }
                    case 1: {
                        switch (s.charAt(i)) {
                            //%N:Nom
                            case 'N':
                                sr = sr + r.getName();
                                step = 0;
                                break;

                            //A:Altitude
                            case 'A':
                                sr = sr + String.format("%.0f", r.getElevation()); //string.Format(cultureEN, "{0:0}", r.Elevation);
                                step = 0;
                                break;

                            //%D:Distance depuis le départ
                            case 'D':
                                sr = sr + String.format("%.1f", r.getTotal() / 1000.0);  //string.Format(cultureEN, "{0:0.0}", r.Total / 1000.0);
                                step = 0;
                                break;

                            //%T:Temps (hh:mm)
                            case 'T':
                                //sr = sr + Utils.Second2DateString_HM(r.Time);
                                step = 3;
                                break;

                            //%H:Heure (ddd hh:mm)
                            case 'H':
                                sr = sr + r.getHour().toString("E HH:mm"); //ToString("ddd HH:mm");
                                step = 0;
                                break;

                            //%h:Heure (hh:mm)
                            case 'h':
                                //sr = sr + r.Hour.ToString("HH:mm");
                                step = 2;
                                break;

                            //%R:Ravito (hh:mm:ss)
                            case 'R':
                                //sr = sr + Utils.Second2DateString(r.Station);
                                step = 4;
                                break;

                            //%B:Barrière horaire (hh:mm) -> Temps depuis le départ 
                            case 'B':
                                sr = sr + Utils.Second2DateString_HM(r.getTimeLimit());
                                step = 0;
                                break;

                            //%b:Barrière horaire (hh:mm) -> Heure limite
                            case 'b':
                                if (cd != null) {
                                    dt = cd.StartTime.plusSeconds(r.getTimeLimit());
                                    sr = sr + dt.toString("HH:mm");
                                } else {
                                    sr = sr + "00:00";
                                }
                                //sr = sr + Utils.Second2DateString_HM(r.TimeLimit);
                                step = 0;
                                break;

                            //%C:Commentaire
                            case 'C':
                                sr = sr + r.CommentMiniRoadbook;
                                step = 0;
                                break;

                            //%c:Commentaire venant du tableau
                            case 'c':
                                sr = sr + r.getComment();
                                step = 0;
                                break;

                            //%+:Cumul du D+
                            case '+':
                                sr = sr + String.format("%.0f",res.cp); //string.Format(cultureEN, "{0:0}", cp);
                                step = 0;
                                break;

                            //%-:Cumul du D-
                            case '-':
                                sr = sr + String.format("%.0f",res.cm); //string.Format(cultureEN, "{0:0}", cm);
                                step = 0;
                                break;

                            //%L:Retour à la ligne
                            case 'L':
                                sr = sr + "\n";
                                step = 0;
                                break;

                            default:
                                sr = sr + s.charAt(i);
                                break;
                        }
                        break;
                    }

                    case 2: //%h
                    {
                        switch (s.charAt(i)) {
                            case 's':
                                sr = sr + r.getHour().toString("HH:mm");
                                step = 0;
                                break;
                            case 'l':
                                sr = sr + r.getHour().toString("HH:mm:ss");
                                step = 0;
                                break;
                            default:
                                sr = sr + r.getHour().toString("HH:mm");
                                sr = sr + s.charAt(i);
                                step = 0;
                                break;
                        }
                        break;
                    }

                    case 3: //%T 
                    {
                        switch (s.charAt(i)) {
                            case 's':
                                sr = sr + Utils.Second2DateString_HM(r.getTime());
                                step = 0;
                                break;
                            case 'l':
                                sr = sr + Utils.Second2DateString(r.getTime());
                                step = 0;
                                break;
                            default:
                                sr = sr + Utils.Second2DateString_HM(r.getTime());
                                sr = sr + s.charAt(i);
                                step = 0;
                                break;
                        }
                        break;
                    }

                    case 4: //%R
                    {
                        switch (s.charAt(i)) {
                            case 's':
                                sr = sr + Utils.Second2DateString_HM(r.getStation());
                                step = 0;
                                break;
                            case 'l':
                                sr = sr + Utils.Second2DateString(r.getStation());
                                step = 0;
                                break;
                            default:
                                sr = sr + Utils.Second2DateString(r.getStation());
                                sr = sr + s.charAt(i);
                                step = 0;
                                break;
                        }
                        break;
                    }

                }
            }

            //Traitement d'une commande si dernier caractère (au cas ou on est une commande 1 ou 2 caractères => valeur par défaut
            if (step == 2) {
                sr = sr + r.getHour().toString("HH:mm");
            } else if (step == 3) {
                sr = sr + Utils.Second2DateString_HM(r.getTime());
            } else if (step == 4) {
                sr = sr + Utils.Second2DateString(r.getStation());
            }

            if (cd != null) {
                return Utils.WordWrap(sr, cd.WordWrapLength, true);
            } else {
                return sr;
            }
        }
        return sr; //TODO Ajout: a valider
    }
 
    
    public static String GetFileExtension(String fname) {
        //String name = file.getName();
        /*
         try {
         return fname.substring(fname.lastIndexOf(".") + 1);
         } catch (Exception e) {
         return "";
         }
         */
        for (int i = fname.length() - 1; i >= 0; i--) {
            if ((fname.charAt(i) == '.') && (i != fname.length() - 1)) {
                return fname.substring(i + 1);
            }
            if (fname.charAt(i) == File.separatorChar) {
                return "";
            }
        }
        return "";
    }
    
    
    /**
     * Remove extention form the filename
     * @param filename Filename without path
     * @return filename without extention
     */
    public static String getFileNameWithoutExtension (String filename) {
        // Handle null case specially.

        if (filename == null) return null;

        // Get position of last '.'.

        int pos = filename.lastIndexOf(".");

        // If there wasn't any '.' just return the string as is.

        if (pos == -1) return filename;

        // Otherwise return the string, up to the dot.

        return filename.substring(0, pos);
    }
    
    /**
     * Return the path of a filename Cross platform??
     *
     * @param fname
     * 		File name where to extract the path
     * @return
     * 		String containing the path
     */
    public static String GetDirFromFilename(String fname) {
        File f = new File(fname);
        return f.getParentFile().toString();
    }
    
    public static boolean FileExist(String fname) {
        return new File(fname).isFile();
    }
    
    
    /*
    public static String ReadXMLString(Element node, String key, String def_val) {
        Element childnode=node.getChild(key);
        
        if (childnode!=null) 
            return childnode.getText();
        else
            return def_val;
    }

    public static int ReadXMLInt(Element node, String key, int def_val) {
        Element childnode=node.getChild(key);
        
        if (childnode!=null)  {
            try {
                return Integer.parseInt(childnode.getText());
            } catch (NumberFormatException e) {
            return def_val;
            } 
        }
        else
            return def_val;
    }

    public static double ReadXMLDouble(Element node, String key, double def_val) {
        Element childnode=node.getChild(key);
        
        if (childnode!=null)  {
            try {
                return Double.parseDouble(childnode.getText());
            } catch (NumberFormatException e) {
            return def_val;
            } 
        }
        else
            return def_val;
    }
   */
    
    
} //Class
