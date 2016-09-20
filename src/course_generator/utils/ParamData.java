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

import java.util.ArrayList;

/**
 *
 * @author pierre.delore
 */
public class ParamData {
    public String name = "";
    public String comment = "";
    public ArrayList <CgParam> data;
    //CultureInfo culture = CultureInfo.CreateSpecificCulture("en-GB");

    public ParamData()
    {
      data = new ArrayList<CgParam>();
    }



    //-- Charge les données depuis le disque --
    public void Load(String name)
    {
    /*        
      XmlDocument XmlDoc;
      double Slope = 0;
      double Speed = 0;


      this.name=System.IO.Path.GetFileNameWithoutExtension(name);


      XmlDoc = new XmlDocument();
      

      //-- Clear the DataList --
      data.Clear();

      //-- Open the XML file --
      XmlDoc.Load(name);

      //-- Search the <trk> node --
      foreach (XmlNode n in XmlDoc.ChildNodes)
      {
        if (n.Name.ToUpper() == "PROJECT")
        {
          foreach (XmlNode n1 in n.ChildNodes)
          {
            //-- <NAME>
            if (n1.Name.ToUpper() == "NAME")
            {
              if (n1.ChildNodes.Count > 0)
              {
                if (n1.FirstChild.Value != null)
                  name = n1.FirstChild.Value;
              }
            }
            //-- <COMMENT>
            else if (n1.Name.ToUpper() == "COMMENT")
            {
              if (n1.ChildNodes.Count > 0)
              {
                if (n1.FirstChild.Value != null)
                  comment = n1.FirstChild.Value;
              }
            }
            else if (n1.Name.ToUpper() == "PARAM")
            {
              foreach (XmlNode n2 in n1.ChildNodes)
              {
                //-- <ITEM>
                if (n2.Name.ToUpper() == "ITEM")
                {
                  foreach (XmlNode n3 in n2.ChildNodes)
                  {
                    //-- <SLOPE>
                    if (n3.Name.ToUpper() == "SLOPE")
                    {
                      if (n3.InnerText != null)
                        Slope = double.Parse(n3.InnerText, culture);
                      else
                        Slope = 0;
                    }
                    //-- <SPEED>
                    if (n3.Name.ToUpper() == "SPEED")
                    {
                      if (n3.InnerText != null)
                        Speed = double.Parse(n3.InnerText, culture);
                      else
                        Speed = 0;
                    }
                  }
                  data.Add(new cgParam{Slope=Slope, Speed=Speed});
                } //ITEM
              }
            } //PARAM
          }
        }//PROJECT
      }//Main FOR
      */
    } //Load


    //-- Sauve les données sur le disque --
    public void Save(String fname)
    {
      /*
      XmlTextWriter wr = new XmlTextWriter(fname, Encoding.UTF8);
      wr.Formatting = Formatting.Indented;
      wr.WriteStartDocument(true);

      wr.WriteStartElement("Project");
        wr.WriteElementString("Name", name);
        wr.WriteElementString("Comment", comment);

        wr.WriteStartElement("Param");
          foreach (cgParam r in data)
          {
            wr.WriteStartElement("Item");
              wr.WriteElementString("Slope", r.Slope.ToString(culture)); 
              wr.WriteElementString("Speed", r.Speed.ToString(culture));
            wr.WriteEndElement(); // Item
          } //foreach
        wr.WriteEndElement(); //Param

      wr.WriteEndElement(); //Project
      
      wr.WriteEndDocument();
      wr.Flush();
      wr.Close();
      */
    }


    //-- Recupère le commentaire d'un fichier --
    public String GetComment(String name)
    {
      /*  
      XmlDocument XmlDoc;
      string s = "";


      this.name = System.IO.Path.GetFileNameWithoutExtension(name);


      XmlDoc = new XmlDocument();

      //-- Clear the DataList --
      data.Clear();

      //-- Open the XML file --
      XmlDoc.Load(name);

      //-- Search the <trk> node --
      foreach (XmlNode n in XmlDoc.ChildNodes)
      {
        if (n.Name.ToUpper() == "PROJECT")
        {
          foreach (XmlNode n1 in n.ChildNodes)
          {
            //-- <COMMENT>
            if (n1.Name.ToUpper() == "COMMENT")
            {
              if (n1.ChildNodes.Count > 0)
              {
                if (n1.FirstChild.Value != null)
                  s = n1.FirstChild.Value;
              }
            }
          }
        }//PROJECT
      }//Main FOR
      return s;
      */
      return ""; //TODO a supprimer
    } //GetComment


    public double FindMaxSpeed()
    {
      if (data.size() > 0) //Count
      {
        double max = -9999.0;
        for (int j = 0; j <= data.size() - 1; j++)
        {
          if (data.get(j).Speed > max) max = data.get(j).Speed;
        }
        return max;
      }
      else return 0;
    }


} //Class
