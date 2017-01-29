/*
 * Course Generator - Miniroadbook label preview
 * Copyright (C) 2017 Pierre Delore
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


package course_generator.mrb;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import course_generator.CgData;
import course_generator.TrackData;
import course_generator.settings.CgSettings;
import course_generator.utils.DrawStringMultiLine;
import course_generator.utils.Utils;

public class PanelPreviewLabel extends JPanel {
	private CgData data;
	private TrackData track;
	private CgSettings settings;
	
	/**
	 * Constructor
	 */
	public PanelPreviewLabel() {
		super();
		track = null;
		data = null;
		settings = null;		
	}
	
	public CgSettings getSettings() {
		return settings;
	}

	public void setSettings(CgSettings settings) {
		this.settings = settings;
	}

	public TrackData getTrack() {
		return track;
	}

	public void setTrack(TrackData track) {
		this.track = track;
	}

	public CgData getData() {
		return data;
	}

	public void setData(CgData data) {
		this.data = data;
	}
	
	/**
	 * Refresh the display
	 */
	public void Refresh() {
		repaint();
	}

	/**
	 * Refresh the panel
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int width=this.getWidth();
		int height=this.getHeight();

		String s;

		//-- Some tests in order to be sure that we have data
		if (data == null)
			return;
		if (settings == null)
			return;
		if (track == null)
			return;
		if (track.data.isEmpty())
			return;


		// -- Text box drawing --
		DrawStringMultiLine drawStringMulti = new DrawStringMultiLine();
			
		g2d.setFont(new Font("ARIAL", Font.PLAIN, data.FontSizeMiniRoadbook));

		int posx = 0;
		int posy = 5;
				
		//-- Generate the text to display
		s = Utils.GenLabel(data.FmtLbMiniRoadbook, data, track, settings);
		drawStringMulti.setText(g2d, s);

		// -- Width calculation
		int w = drawStringMulti.getWidth() + 4 + 2;

		// -- Text height

		g2d.setColor(Color.WHITE);
		g2d.fillRect(posx, posy, w, drawStringMulti.getHeight() + 4);

		g2d.setColor(Color.BLACK);
		g2d.drawRect(posx, posy, w, drawStringMulti.getHeight() + 4);
		g2d.setClip(posx, posy, w, drawStringMulti.getHeight() + 4);

		// -- Draw the text
		g2d.setColor(Color.BLACK);
		drawStringMulti.draw(g2d, s, posx + 2, posy);
				
		// -- Set the clipping to the whole panel
		g2d.setClip(0, 0, width, height);

		g2d.dispose();
	}

}
