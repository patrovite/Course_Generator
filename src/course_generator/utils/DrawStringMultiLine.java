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

import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class DrawStringMultiLine {
	private String Text;
	private String[] TabText;
	private int Width;
	private int Height;
	private int Align;
	public final int ALIGN_CENTER=0;
	public final int ALIGN_LEFT=1;
	public final int ALIGN_RIGHT=2;
	
	public DrawStringMultiLine() {
		Text="";
		Width=0;
		Height=0;
		Align=0;
	}
	

	public void draw(Graphics2D g2d, String text, int x, int y) {
		setText(g2d,text);
		
		int w=0;
		FontMetrics m = g2d.getFontMetrics();
		int hh= m.getHeight();
		int h=hh;
		
		for (String line : TabText) {
			if (Align==ALIGN_CENTER)
				w=(Width-m.stringWidth(line))/2;
			else if (Align==ALIGN_RIGHT)
				w=(Width-m.stringWidth(line));
			else 
				w=0;
			g2d.drawString(line, x+w, y+h);
			h=h+hh;
		}
	}

	public String getText() {
		return Text;
	}

	public void setText(Graphics2D g2d, String text) {
		Text = text;
		TabText=Text.split("\n");
		CalcDimension(g2d);
	}

	public int getWidth() {
		return Width;
	}

	private void CalcDimension(Graphics2D g2d) {
		int xmax=0;
		int w=0;
		int h=0;
		FontMetrics m = g2d.getFontMetrics();
		
		for (String line : TabText) {
			w=m.stringWidth(line);
			if (w>xmax) xmax=w;
			h=h+m.getHeight();
		}
		Width=xmax;
		Height=h;
	}

	public int getHeight() {
		return Height;
	}


	public int getAlign() {
		return Align;
	}


	public void setAlign(int align) {
		Align = align;
	}

	
	
	
}
