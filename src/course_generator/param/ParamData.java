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

package course_generator.param;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import course_generator.utils.Utils;

/**
 *
 * @author pierre.delore
 */
public class ParamData {
	public String name = "";
	public String comment = "";
	public ArrayList<CgParam> data;


	public ParamData() {
		data = new ArrayList<CgParam>();
	}


	/**
	 * Load parameters from disk
	 * 
	 * @param name
	 *            File name to load
	 * @throws Exception
	 */
	public void Load(String name) throws Exception {
		SaxParamHandler paramHandler = new SaxParamHandler();

		int ret = paramHandler.readDataFromParam(name, this);
		if (ret != 0)
			System.out.println("Load parameters '" + name + "'from disk. Error line =" + paramHandler.getErrLine());

	} // -- Load


	/**
	 * Save parameters on disk
	 * 
	 * @param fname
	 *            Name of the file
	 */
	public void SaveCurve(String fname, int unit) {
		if (data.size() <= 0) {
			return;
		}

		// -- Save the data in the home directory
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		try {
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(fname));
			XMLStreamWriter writer = factory.createXMLStreamWriter(bufferedOutputStream, "UTF-8");

			writer.writeStartDocument("UTF-8", "1.0");
			writer.writeStartElement("Project");
			Utils.WriteStringToXML(writer, "Name", name);
			Utils.WriteStringToXML(writer, "Comment", comment);
			writer.writeStartElement("Param");
			for (CgParam curvePoint : data) {
				writer.writeStartElement("Item");
				Utils.WriteStringToXML(writer, "Slope", String.format(Locale.ROOT, "%f", curvePoint.getSlope()));
				// Saving the curve speeds using the metric system.
				Utils.WriteStringToXML(writer, "Speed", String.format(Locale.ROOT, "%f", curvePoint.getSpeedNumber()));
				writer.writeEndElement(); // Item
			}
			writer.writeEndElement(); // Param
			writer.writeEndElement(); // Project
			writer.writeEndDocument();
			writer.flush();
			writer.close();
		} catch (XMLStreamException | IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Find the maximum speed of the list
	 * 
	 * @return Maximum speed en km/h
	 */
	public double FindMaxSpeed() {
		if (data.size() > 0) // Count
		{
			double max = -9999.0;
			for (int j = 0; j <= data.size() - 1; j++) {
				if (data.get(j).getSpeedNumber() > max)
					max = data.get(j).getSpeedNumber();
			}
			return max;
		} else
			return 0;
	}

} // Class
