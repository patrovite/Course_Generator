/*
 * Course Generator - Curve parameter list model
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

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.AbstractListModel;

public class ParamListModel extends AbstractListModel {
	private ArrayList<String> names;


	/**
	 * Constructor
	 */
	public ParamListModel() {
		names = new ArrayList<>();
	}


	/**
	 * Return the size of the list
	 */
	public int getSize() {
		return names.size();
	}


	/**
	 * Return the element at a given index
	 */
	public Object getElementAt(int index) {
		return (String) names.get(index);
	}


	/**
	 * Return the complete list
	 * 
	 * @return list of string
	 */
	public ArrayList<String> getNamesList() {
		return names;
	}


	/**
	 * Set the list
	 * 
	 * @param array
	 *            List of string
	 */
	public void setList(ArrayList<String> array) {
		this.names = array;
		fireContentsChanged(this, 0, names.size());
	}


	/**
	 * Add an element to string
	 * 
	 * @param name
	 *            Element to add
	 */
	public void addElement(String name) {
		this.names.add(name);
		fireContentsChanged(this, 0, names.size());
	}


	/**
	 * Remove all the element from the list
	 */
	public void clear() {
		this.names.clear();
		fireContentsChanged(this, 0, names.size());
	}


	/*
	 * Return the a sorted list
	 */
	public void getSortedList(ArrayList<String> array) {
		Collections.sort(array);
		names = array;
	}


	/**
	 * Sort the list
	 */
	public void sort() {
		Collections.sort(names);
		fireContentsChanged(this, 0, names.size());
	}
}
