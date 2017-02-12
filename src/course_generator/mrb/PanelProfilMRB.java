/*
 * Course Generator - Miniroadbook profile panel
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

package course_generator.mrb;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import course_generator.CgData;
import course_generator.TrackData;
import course_generator.settings.CgSettings;
import course_generator.utils.CgConst;
import course_generator.utils.DrawStringMultiLine;
import course_generator.utils.Utils;
import course_generator.utils.Utils.CalcLineResult;

public class PanelProfilMRB extends JPanel {
	private boolean WithHighlight;
	private float transparence;
	private TrackData track;
	private CgSettings settings;
	private MrbDataList data;
	private int ProfileType;
	private Stroke PenBlackS;
	/** Selected line **/
	private int SelLine;
	/** width of the panel **/
	private int width;
	/** heigth of the panel **/
	private int height;
	/** X position of the vertical axis of the profile **/
	private int offx = 40;
	/**
	 * Y position of the horizontal axis of the profile from the bottom of the
	 * panel
	 **/
	private int offy = 25;

	/** Horizontal margin in pixel from the right border **/
	private int rMargin = 25;
	/** Number of pixel per horizontal unit (kilometer or miles) **/
	private double PixelPerHUnit = 8;
	/** Number of pixel per vertical unit (meter) **/
	private double PixelPerVUnit = 0.1;
	// private int ConfigReplication = CgConst.MRB_REP_POS |
	// CgConst.MRB_REP_ALIGN | CgConst.MRB_REP_FORMAT |
	// CgConst.MRB_REP_SIZE | CgConst.MRB_REP_TAGS;

	/** Base line for the duplicate feature **/
	private int RepLine = 0;
	private Boolean isGridInit = false;
	private int xSel1 = 0;
	private int xSel2 = 0;
	private int ySel1 = 0;
	private int ySel2 = 0;
	private int yDelta = -1;
	String[] MemoFormat;
	private BasicStroke myPenDot;
	private BasicStroke PenSimpleBorder;
	/** width of the profil **/
	private int wp = 0;
	/** height of the profil **/
	private int hp = 0;
	private BasicStroke PenRP_Border;
	private BasicStroke PenSlopeBorder;
	private BasicStroke myPenMoy;

	public PanelProfilMRB(int width, int height) {
		super();
		track = null;
		data = null;
		settings = null;
		this.width = width;
		this.height = height;
		ProfileType = 0;
		WithHighlight = true;

		myPenMoy = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 10, 0 }, 0);
		PenBlackS = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 10, 0 }, 0);
		myPenDot = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 1, 2 }, 0);

		PenSimpleBorder = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 1, 0 }, 0);
		PenRP_Border = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 1, 0 }, 0);
		PenSlopeBorder = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 1, 0 }, 0);
	}

	/**
	 * Set the CG settings on the object
	 * 
	 * @param settings
	 *            CG settings
	 */
	public void setSettings(CgSettings settings) {
		this.settings = settings;
	}

	/**
	 * Refresh the profil
	 */
	public void Refresh() {
		repaint();
	}

	/**
	 * Get the track object
	 * 
	 * @return track object
	 */
	public TrackData getTrack() {
		return track;
	}

	/**
	 * Set the track object
	 * 
	 * @param track
	 *            Track object
	 */
	public void setTrack(TrackData track) {
		this.track = track;
	}

	/**
	 * Get the data list object
	 * 
	 * @return Data list object
	 */
	public MrbDataList getData() {
		return data;
	}

	/**
	 * Set the data list object
	 * 
	 * @param data
	 *            Get the data list object
	 */
	public void setData(MrbDataList data) {
		this.data = data;
	}

	/**
	 * Set the width of the panel
	 * 
	 * @param width
	 *            Width of the panel
	 */
	public void setWidth(int width) {
		this.width = width;
		setPreferredSize(new Dimension(this.width, this.height));
		repaint();
	}

	/**
	 * Return the width of the panel
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Set the height of the panel
	 * 
	 * @param height
	 *            Width of the panel
	 */
	public void setHeight(int height) {
		this.height = height;
		setPreferredSize(new Dimension(this.width, this.height));
		repaint();
	}

	/**
	 * Return the height of the panel
	 */
	public int getHeight() {
		return height;
	}

	public int getProfileType() {
		return ProfileType;
	}

	public void setProfileType(int profileType) {
		if ((profileType < 0) || (profileType > 2))
			return;

		ProfileType = profileType;
		repaint();
	}

	/**
	 * Refresh the panel
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// private void RefreshProfil(bool WithHighlight)

		int nbtag = 0;
		double[] stepval = { 1, 2, 2.5, 5 };
		double[] stepval2 = { 0.1, 0.1, 0.2, 0.1 };

		String s;
		double resx = 0.0;
		double resy = 0.0;
//		double tf = 0;
//		boolean first = false;
		int[] TabY = new int[width];

		for (int i = 0; i < width; i++)
			TabY[i] = 0;

		// Calculate the profile width
		wp = width - offx - rMargin;
		PixelPerHUnit = wp / ((int) (track.getTotalDistance(settings.Unit) / 1000.0));

		// Calculate the profile height
		
		Double dElev = track.getMaxElev(settings.Unit) - track.getMinElev(settings.Unit);
		if (dElev < 100) {
			dElev = Math.ceil(track.getMaxElev(settings.Unit) / 10.0) * 10.0;
		} else {
			dElev = (Math.ceil(track.getMaxElev(settings.Unit) / 100.0) * 100.0) - (Math.floor(track.getMinElev((settings.Unit)) / 100.0) * 100.0);
		}

		hp = height - offy - track.TopMargin;
		PixelPerVUnit = dElev / hp;

//		Font FontSmall = new Font("ARIAL", Font.PLAIN, 7);
		Font FontGraduation = new Font("ARIAL", Font.PLAIN, 10);
//		Font FontLabel = new Font("ARIAL", Font.PLAIN, 11);

		// CalcLineResult res = new CalcLineResult();

		// -- Main frame display with white background
		g2d.setPaint(Color.WHITE);
		g2d.fillRect(0, 0, width, height);

		g2d.setStroke(PenBlackS);
		g2d.setPaint(Color.BLACK);
		g2d.drawRect(0, 0, width - 1, height - 1);

		//-- Some tests in order to be sure that we have data
		if (data == null)
			return;
		if (settings == null)
			return;
		if (track == null)
			return;
		if (track.data.isEmpty())
			return;

		// -- Vertical axis drawing
		// Position of the arrow compare to the top of the profile
		int YPosArrow = 15;

		g2d.setStroke(PenBlackS);
		g2d.drawLine(offx, track.TopMargin - YPosArrow, offx, height - offy + 3);

		// -- End of the vertical axis drawing
		g2d.setPaint(Color.BLACK);
		int[] x = { offx - 5, offx, offx + 5 };
		int[] y = { track.TopMargin - YPosArrow - 1, track.TopMargin - YPosArrow - 10,
				track.TopMargin - YPosArrow - 1 };
		g2d.fillPolygon(x, y, 3);

		// -- Vertical unit drawing
		s = settings.getShortElevationUnitString();
		Dimension d = Utils.StringDimension(g2d, s);
		g2d.drawString(s, offx - d.width - 8, track.TopMargin - YPosArrow);

		
		
		
		// -- Horizontal axis drawing
		g2d.drawLine(offx - 3, height - offy, width - 10, height - offy);

		// -- End of the horizontal axis drawing
		int[] x1 = { width - 15, width - 7, width - 15 };
		int[] y1 = { height - 20, height - 25, height - 30 };
		g2d.fillPolygon(x1, y1, 3);

		// -- Horizontal unit drawing
		s = settings.getShortDistanceUnitString();
		d = Utils.StringDimension(g2d, s);
		g2d.drawString(s, width - d.width-2, height - offy - 10);

		
		Double ymin = 0.0;
		Double ymax = 0.0;
		Double xmin = 0.0;
		Double xmax = 0.0;
		Double stepx = 0.0;
		Double stepy = 0.0;
		Double stepx2 = 0.0;
		Double stepy2 = 0.0;
		int NbGradX = 0;
		int NbGradY = 0;

		// -- Min and max of the X axis
		xmin = 0.0;
		xmax = track.getTotalDistance(settings.Unit);

		// -- X axis ticks calculation
		int ctab = 0;
		int cmul = 0;
		double p = 1.0;
		boolean ok = false;

		while (!ok) {
			ctab = 0;
			while (ctab < 4 && !ok) {
				cmul = 5;
				while (cmul <= 10 && !ok) {
					if (xmin + p * stepval[ctab] * cmul > xmax)
						ok = true;
					else
						cmul++;
				}
				if (!ok)
					ctab++;
			}
			if (!ok)
				p = p * 10;
		}
		stepx = stepval[ctab] * p;
		stepx2 = stepval2[ctab] * stepx;
		NbGradX = cmul;
		xmax = xmin + stepx * NbGradX;

		// -- Min and max of the y axis calculation
		if (track.getMaxElev(settings.Unit) - track.getMinElev(settings.Unit) < 100)
			ymin = 0.0;
		else
			ymin = Math.floor(track.getMinElev(settings.Unit) / 100.0) * 100.0;
		ymax = track.getMaxElev(settings.Unit);

		// -- Y axis ticks calculation
		ctab = 0;
		cmul = 0;
		p = 1.0;
		ok = false;

		while (!ok) {
			ctab = 0;
			while (ctab < 4 && !ok) {
				cmul = 5;
				while (cmul <= 10 && !ok) {
					if (ymin + p * stepval[ctab] * cmul > ymax)
						ok = true;
					else
						cmul++;
				}
				if (!ok)
					ctab++;
			}
			if (!ok)
				p = p * 10;
		}
		stepy = stepval[ctab] * p;
		stepy2 = stepval2[ctab] * stepy;
		NbGradY = cmul;
		ymax = ymin + stepy * NbGradY;

		resx = wp / (xmax - xmin);
		resy = hp / (ymax - ymin);

		// -- Horizontal line --

		// -- Vertical grid drawing --
		g2d.setStroke(PenBlackS);
		g2d.setPaint(Color.BLACK);
		for (double i = xmin; i <= xmax; i += stepx2) {
			g2d.drawLine(offx + (int) (i * resx), height - offy + 2, offx + (int) (i * resx), height - offy);
		}

		for (double i = xmin; i <= xmax; i += stepx) {
			g2d.setStroke(myPenDot);
			g2d.drawLine(offx + (int) (i * resx), height - offy, offx + (int) (i * resx), height - offy - hp);
			g2d.setStroke(PenBlackS);
			g2d.drawLine(offx + (int) (i * resx), height - offy + 4, offx + (int) (i * resx), height - offy);
		}

		// -- Horizontal grid drawing --
		g2d.setStroke(PenBlackS);
		for (double i = ymin; i <= ymax; i += stepy2) {
			g.drawLine(offx - 2, height - offy - (int) ((i - ymin) * resy), offx,
					height - offy - (int) ((i - ymin) * resy));
		}

		for (double i = ymin; i <= ymax; i += stepy) {
			g2d.setStroke(myPenDot);
			g.drawLine(offx, height - offy - (int) ((i - ymin) * resy), offx + wp,
					height - offy - (int) ((i - ymin) * resy));
			g2d.setStroke(PenBlackS);
			g.drawLine(offx - 4, height - offy - (int) ((i - ymin) * resy), offx,
					height - offy - (int) ((i - ymin) * resy));
		}

		// -- Y axis value drawing
		g2d.setFont(FontGraduation);
		g2d.setPaint(Color.BLACK);
		for (int c = 0; c <= NbGradY; c++) {
			s = String.format("%1.0f", (ymin + c * stepy));
			d = Utils.StringDimension(g2d, s);
			g2d.drawString(s, offx - d.width - 5, (int) ((height - offy) - (c * stepy * resy)) + (d.height / 2));
		}

		// -- Draw the value of the X axis
		for (int c = 0; c <= NbGradX; c++) {
			s = String.format("%1.0f", (xmin + c * stepx) / 1000);
			// stringSize = g.MeasureString(s, FontAvr);
			d = Utils.StringDimension(g2d, s);
			g2d.drawString(s, (int) (offx + (c * stepx * resx) - (d.width / 2)), height - offy + 4 + d.height);
		}

		// -- Simple profile drawing --
		if (ProfileType == 0)
			DrawSimpleProfile(g2d, xmin, ymin, resx, resy, TabY);
		// -- Road/path profile drawing --
		else if (ProfileType == 1)
			DrawRoadPathProfile(g2d, xmin, ymin, resx, resy, TabY);
		// -- Slope profile drawing--
		else if (ProfileType == 2)
			DrawSlopeProfile(g2d, xmin, ymin, resx, resy, TabY);

		// -- Vertical line drawing --
		double dist = 0;
		int posx = 0;
		int posy = 0;
		int posy_s = 0;

		g2d.setStroke(myPenMoy);
		g2d.setColor(Color.BLACK);

		for (CgData r : data.data) {
			if ((((r.getTag() & CgConst.TAG_HIGH_PT) != 0) || ((r.getTag() & CgConst.TAG_LOW_PT) != 0)
					|| ((r.getTag() & CgConst.TAG_EAT_PT) != 0) || ((r.getTag() & CgConst.TAG_WATER_PT) != 0)
					|| ((r.getTag() & CgConst.TAG_MARK) != 0)) && ((r.OptionMiniRoadbook & CgConst.MRBOPT_SEL) != 0)) {
				dist = r.getTotal(settings.Unit);
				posx = (int) (dist * resx);
				posy = height - offy - (hp + track.TopMargin - 7 - r.VPosMiniRoadbook);
				if (track.LabelToBottom)
					posy_s = height - offy;
				else
					posy_s = TabY[offx + posx];

				g2d.drawLine(offx + posx, posy_s, offx + posx, posy);
			}
		}

		// -- Text box drawing --
		int ii = 0;
		DrawStringMultiLine drawStringMulti = new DrawStringMultiLine();
		for (CgData r : data.data) {
			if ((((r.getTag() & CgConst.TAG_HIGH_PT) != 0) || ((r.getTag() & CgConst.TAG_LOW_PT) != 0)
					|| ((r.getTag() & CgConst.TAG_EAT_PT) != 0) || ((r.getTag() & CgConst.TAG_WATER_PT) != 0)
					|| ((r.getTag() & CgConst.TAG_MARK) != 0)) && ((r.OptionMiniRoadbook & CgConst.MRBOPT_SEL) != 0)) {
				// -- Calc the number of tag
				nbtag = NbTag(r.getTag());

				g2d.setFont(new Font("ARIAL", Font.PLAIN, r.FontSizeMiniRoadbook));

				dist = r.getTotal(settings.Unit);
				posx = (int) (dist * resx);
				posy = hp + track.TopMargin - 0 - r.VPosMiniRoadbook;

				//-- Generate the text to display
				// s = "Les Contamines\n1083m - km:100\n10h23";
				// s = GenLabel("%N\n%Am - km:%D\n%H", r);
				// s = GenLabel(r.FmtLbMiniRoadbook, r);
				// s = Utils.GenLabel(r.FmtLbMiniRoadbook, r, track);
//				s = Utils.GenLabel("%N%L%Am - km:%D\n%H", r, track, settings);
				s = Utils.GenLabel(r.FmtLbMiniRoadbook, r, track, settings);
				drawStringMulti.setText(g2d, s);

				// -- Width calculation
				int w = drawStringMulti.getWidth() + 4 + 2;
				int w1 = (nbtag * 16) + ((nbtag - 1) * 2) + 4;
				if (w1 > w)
					w = w1;

				// -- Text height
				int htext = drawStringMulti.getHeight();

				// -- Alignment position (left/center/right)
				// Left nothing change
				if ((r.OptionMiniRoadbook & CgConst.MRBOPT_CENTER) != 0) {
					posx = posx - (w + 4) / 2;
				}

				if ((r.OptionMiniRoadbook & CgConst.MRBOPT_RIGHT) != 0) {
					posx = posx - (w + 1);
				}

				// -- Size of the tags (with or without tags)
				int htags = 16;
				if (((r.OptionMiniRoadbook & CgConst.MRBOPT_SHOWTAGS) == 0) || (nbtag == 0))
					htags = 0;

				// -- Draw rectangle With or without selection
				if ((ii == SelLine) && (WithHighlight)) {
					g2d.setColor(new Color(0xFF, 0xEF, 0xBB));
					g2d.fillRect(offx + posx, height - offy - posy, w, drawStringMulti.getHeight() + htags + 4);
				} else {
					g2d.setColor(Color.WHITE); // myBrushWhite
					g2d.fillRect(offx + posx, height - offy - posy, w, drawStringMulti.getHeight() + htags + 4);
				}

				g2d.setColor(Color.BLACK); // PenBlackS
				g2d.drawRect(offx + posx, height - offy - posy, w, drawStringMulti.getHeight() + htags + 4);
				g2d.setClip(offx + posx, height - offy - posy, w, drawStringMulti.getHeight() + htags + 4);

				if (ii == SelLine) {
					xSel1 = offx + posx;
					ySel1 = height - offy - posy;
					xSel2 = xSel1 + w;
					ySel2 = ySel2 + htext + htags + 4;
				}

				// -- Draw the text
				g2d.setColor(Color.BLACK);
				drawStringMulti.draw(g2d, s, offx + posx + 2, height - offy - posy);// +
																					// htext);

				// -- Set the clipping to the whole panel
				g2d.setClip(0, 0, width, height);

				if ((r.OptionMiniRoadbook & CgConst.MRBOPT_SHOWTAGS) != 0) {
					int m = 0;
					int lt = (int) ((w - w1) / 2);

					if ((r.getTag() & CgConst.TAG_HIGH_PT) != 0) {
						Image img = Toolkit.getDefaultToolkit()
								.getImage(getClass().getResource("/course_generator/images/high_point.png"));
						g2d.drawImage(img, (offx + posx + 2 + m + lt), height - offy - posy + 1 + htext, this);
						m += 18;
					}

					if ((r.getTag() & CgConst.TAG_LOW_PT) != 0) {
						Image img = Toolkit.getDefaultToolkit()
								.getImage(getClass().getResource("/course_generator/images/low_point.png"));
						g2d.drawImage(img, (offx + posx + 2 + m + lt), height - offy - posy + 1 + htext, this);
						m += 18;
					}

					if ((r.getTag() & CgConst.TAG_EAT_PT) != 0) {
						Image img = Toolkit.getDefaultToolkit()
								.getImage(getClass().getResource("/course_generator/images/eat.png"));
						g2d.drawImage(img, (offx + posx + 2 + m + lt), height - offy - posy + 1 + htext, this);
						m += 18;
					}

					if ((r.getTag() & CgConst.TAG_WATER_PT) != 0) {
						Image img = Toolkit.getDefaultToolkit()
								.getImage(getClass().getResource("/course_generator/images/drink.png"));
						g2d.drawImage(img, (offx + posx + 2 + m + lt), height - offy - posy + 1 + htext, this);
						m += 18;
					}

					if ((r.getTag() & CgConst.TAG_COOL_PT) != 0) {
						Image img = Toolkit.getDefaultToolkit()
								.getImage(getClass().getResource("/course_generator/images/photo.png"));
						g2d.drawImage(img, (offx + posx + 2 + m + lt), height - offy - posy + 1 + htext, this);
						m += 18;
					}

					if ((r.getTag() & CgConst.TAG_NOTE) != 0) {
						Image img = Toolkit.getDefaultToolkit()
								.getImage(getClass().getResource("/course_generator/images/note.png"));
						g2d.drawImage(img, (offx + posx + 2 + m + lt), height - offy - posy + 1 + htext, this);
						m += 18;
					}

					if ((r.getTag() & CgConst.TAG_INFO) != 0) {
						Image img = Toolkit.getDefaultToolkit()
								.getImage(getClass().getResource("/course_generator/images/info.png"));
						g2d.drawImage(img, (offx + posx + 2 + m + lt), height - offy - posy + 1 + htext, this);
						m += 18;
					}
				}
				// ii++;
			}
			if ((((r.getTag() & CgConst.TAG_HIGH_PT) != 0) || ((r.getTag() & CgConst.TAG_LOW_PT) != 0)
					|| ((r.getTag() & CgConst.TAG_EAT_PT) != 0) || ((r.getTag() & CgConst.TAG_WATER_PT) != 0)
					|| ((r.getTag() & CgConst.TAG_MARK) != 0)))
				ii++;
		}

		// -- Copyright --
		Image img = Toolkit.getDefaultToolkit()
				.getImage(getClass().getResource("/course_generator/images/copyright.png"));
		g2d.drawImage(img, width - 15, (height - offy - img.getHeight(null)) / 2, this);

		g2d.dispose();
	}

	/**
	 * Draw the simple type profile
	 * 
	 * @param g2d
	 *            Graphic context
	 * @param xmin
	 *            Minimum x axis value
	 * @param ymin
	 *            Minimum y axis value
	 * @param resx
	 *            x axis resolution
	 * @param resy
	 *            y axis resolution
	 * @param TabY
	 *            Array of y value for each x pixel
	 */
	private void DrawSimpleProfile(Graphics2D g2d, double xmin, double ymin, double resx, double resy, int[] TabY) {
		boolean first = true;
		int[] xCurvePts = { 0, 0, 0, 0 };
		int[] yCurvePts = { 0, 0, 0, 0 };
		CalcLineResult res = new CalcLineResult();
	
		CgData oldr = new CgData();
	
		oldr.setElevation(track.data.get(0).getElevation());
		oldr.setTotal(track.data.get(0).getTotal());
	
		g2d.setPaint(track.clProfil_Simple_Fill);
	
		// -- Draw the profile with a unique color
		for (CgData r : track.data) {
			// Point on bottom left (old point)
			xCurvePts[0] = (int) Math.round((offx + 1 + ((oldr.getTotal(settings.Unit) - xmin) * resx)));
			yCurvePts[0] = height - offy;
	
			// Point on top left (old point)
			xCurvePts[1] = (int) Math.round((offx + 1 + ((oldr.getTotal(settings.Unit) - xmin) * resx)));
			yCurvePts[1] = height - offy - (int)((oldr.getElevation(settings.Unit) - ymin) * resy);
	
			// Point on top right (new point)
			xCurvePts[2] = (int) Math.round((offx + 1 + ((r.getTotal(settings.Unit) - xmin) * resx)));
			yCurvePts[2] = height - offy - (int)((r.getElevation(settings.Unit) - ymin) * resy);
	
			// Point on bottom right (new point)
			xCurvePts[3] = (int) Math.round((offx + 1 + ((r.getTotal(settings.Unit) - xmin) * resx)));
			yCurvePts[3] = height - offy;
	
			// Filter
			if ((xCurvePts[2] - xCurvePts[0]) > track.CurveFilter) {
				//-- Draw the polygon
				g2d.fillPolygon(xCurvePts, yCurvePts, 4);
	
				res = Utils.CalcLine(xCurvePts[1], yCurvePts[1], xCurvePts[2], yCurvePts[2], res);
				for (int tf = xCurvePts[1]; tf < xCurvePts[2]; tf++) {
					TabY[(int) tf] = (int) (res.a * tf + res.b);
					// First pass. Allow to draw the first line
					if ((first) && (tf > 0)) {
						TabY[(int) tf - 1] = TabY[(int) tf];
						first = false;
					}
				}
				oldr.setElevation(r.getElevation());
				oldr.setTotal(r.getTotal());
			}
		} // for
	
		// -- Draw the line on the profile
		oldr.setElevation(track.data.get(0).getElevation());
		oldr.setTotal(track.data.get(0).getTotal());
	
		g2d.setColor(track.clProfil_Simple_Border);
		g2d.setStroke(PenSimpleBorder);
		for (CgData r : track.data) {
			// Point on bottom left (old point)
			xCurvePts[0] = (int) Math.round((offx + 1 + ((oldr.getTotal(settings.Unit) - xmin) * resx)));
			yCurvePts[0] = height - offy;
	
			// Point on top left (old point)
			xCurvePts[1] = (int) Math.round((offx + 1 + ((oldr.getTotal(settings.Unit) - xmin) * resx)));
			yCurvePts[1] = (int) (height - offy - ((oldr.getElevation(settings.Unit) - ymin) * resy));
	
			// Point on top right (new point)
			xCurvePts[2] = (int) Math.round((offx + 1 + ((r.getTotal(settings.Unit) - xmin) * resx)));
			yCurvePts[2] = (int) (height - offy - ((r.getElevation(settings.Unit) - ymin) * resy));
	
			// Point on bottom right (new point)
			xCurvePts[3] = (int) Math.round((offx + 1 + ((r.getTotal(settings.Unit) - xmin) * resx)));
			yCurvePts[3] = height - offy;
	
			if ((xCurvePts[2] - xCurvePts[0]) > track.CurveFilter) // Filter
			{
				g2d.drawLine(xCurvePts[1], yCurvePts[1], xCurvePts[2], yCurvePts[2]);
				oldr.setElevation(r.getElevation());
				oldr.setTotal(r.getTotal());
			}
		} // for
	
		// Last point
		g2d.fillPolygon(xCurvePts, yCurvePts, 4);
		g2d.drawLine(xCurvePts[1], yCurvePts[1], xCurvePts[2], yCurvePts[2]);
	}

	/**
	 * Draw the road/path type profile
	 * 
	 * @param g2d
	 *            Graphic context
	 * @param xmin
	 *            Minimum x axis value
	 * @param ymin
	 *            Minimum y axis value
	 * @param resx
	 *            x axis resolution
	 * @param resy
	 *            y axis resolution
	 * @param TabY
	 *            Array of y value for each x pixel
	 */
	private void DrawRoadPathProfile(Graphics2D g2d, Double xmin, Double ymin, double resx, double resy, int[] TabY) {
		int cmpt = 0;
		boolean first = true;
		int[] xCurvePts = { 0, 0, 0, 0 };
		int[] yCurvePts = { 0, 0, 0, 0 };
		CalcLineResult res = new CalcLineResult();
		CgData oldr = new CgData();
	
		oldr.setElevation(track.data.get(0).getElevation());
		oldr.setTotal(track.data.get(0).getTotal());
	
		// -- Draw the profile with a color for the road and a color for the
		// path
		for (CgData r : track.data) {
	
			// Point on botton left (old point)
			xCurvePts[0] = (int) Math.round((offx + 1 + ((oldr.getTotal(settings.Unit) - xmin) * resx)));
			yCurvePts[0] = height - offy;
	
			// Point on top left (old point)
			xCurvePts[1] = (int) Math.round((offx + 1 + ((oldr.getTotal(settings.Unit) - xmin) * resx)));
			yCurvePts[1] = (int) (height - offy - ((oldr.getElevation(settings.Unit) - ymin) * resy));
	
			// Point on top right (new point)
			xCurvePts[2] = (int) Math.round((offx + 1 + ((r.getTotal(settings.Unit) - xmin) * resx)));
			yCurvePts[2] = (int) (height - offy - ((r.getElevation(settings.Unit) - ymin) * resy));
	
			// Point on bottom right (new point)
			xCurvePts[3] = (int) Math.round((offx + 1 + ((r.getTotal(settings.Unit) - xmin) * resx)));
			yCurvePts[3] = height - offy;
	
			g2d.setColor(track.clProfil_RS_Path);
	
			if (r.getDiff() == 100)
				cmpt++;
	
			if ((xCurvePts[2] - xCurvePts[0]) > track.CurveFilter) // Filtre
			{
				if (cmpt > 0)
					g2d.setColor(track.clProfil_RS_Road);
				g2d.fillPolygon(xCurvePts, yCurvePts, 4);
	
				res = Utils.CalcLine(xCurvePts[1], yCurvePts[1], xCurvePts[2], yCurvePts[2], res);
				for (int tf = xCurvePts[1]; tf < xCurvePts[2]; tf++) {
					TabY[(int) tf] = (int) (res.a * tf + res.b);
					// First pass. Allow to draw the first line
					if ((first) && (tf > 0)) {
						TabY[(int) tf - 1] = TabY[(int) tf];
						first = false;
					}
				}
	
				oldr.setElevation(r.getElevation());
				oldr.setTotal(r.getTotal());
				cmpt = 0;
			}
	
		} // for
	
		// -- Draw the line on the profile
		oldr.setElevation(track.data.get(0).getElevation());
		oldr.setTotal(track.data.get(0).getTotal());
	
		for (CgData r : track.data) {
			// Point on bottom left (old point)
			xCurvePts[0] = (int) Math.round((offx + 1 + ((oldr.getTotal(settings.Unit) - xmin) * resx)));
			yCurvePts[0] = height - offy;
	
			// Point on top left (old point)
			xCurvePts[1] = (int) Math.round((offx + 1 + ((oldr.getTotal(settings.Unit) - xmin) * resx)));
			yCurvePts[1] = (int) (height - offy - ((oldr.getElevation(settings.Unit) - ymin) * resy));
	
			// Point on top right (new point)
			xCurvePts[2] = (int) Math.round((offx + 1 + ((r.getTotal(settings.Unit) - xmin) * resx)));
			yCurvePts[2] = (int) (height - offy - ((r.getElevation(settings.Unit) - ymin) * resy));
	
			// Point on bottom right (new point)
			xCurvePts[3] = (int) Math.round((offx + 1 + ((r.getTotal(settings.Unit) - xmin) * resx)));
			yCurvePts[3] = height - offy;
	
			g2d.setColor(track.clProfil_RS_Path);
	
			g2d.setColor(track.clProfil_RS_Border);
			g2d.setStroke(PenRP_Border);
	
			if ((xCurvePts[2] - xCurvePts[0]) > track.CurveFilter) // Filtre
			{
				g2d.drawLine(xCurvePts[1], yCurvePts[1], xCurvePts[2], yCurvePts[2]);
				oldr.setElevation(r.getElevation());
				oldr.setTotal(r.getTotal());
			}
	
		} // for
	
		// Last point
		g2d.fillPolygon(xCurvePts, yCurvePts, 4);
		g2d.drawLine(xCurvePts[1], yCurvePts[1], xCurvePts[2], yCurvePts[2]);
	}

	/**
	 * Draw the slope type profile
	 * 
	 * @param g2d
	 *            Graphic context
	 * @param xmin
	 *            Minimum x axis value
	 * @param ymin
	 *            Minimum y axis value
	 * @param resx
	 *            x axis resolution
	 * @param resy
	 *            y axis resolution
	 * @param TabY
	 *            Array of y value for each x pixel
	 */
	private void DrawSlopeProfile(Graphics2D g2d, Double xmin, Double ymin, double resx, double resy, int[] TabY) {
		double avrSlope = 0.0;
		int nbSlope = 0;
		g2d.setColor(Color.BLACK);
		// b = Brushes.Black;
		boolean first = true;
		int[] xCurvePts = { 0, 0, 0, 0 };
		int[] yCurvePts = { 0, 0, 0, 0 };
		CalcLineResult res = new CalcLineResult();
		CgData oldr = new CgData();

		oldr.setElevation(track.data.get(0).getElevation());
		oldr.setTotal(track.data.get(0).getTotal());

		// -- Draw the slope profile
		for (CgData r : track.data) {
			xCurvePts[0] = (int) Math.round((offx + 1 + ((oldr.getTotal(settings.Unit) - xmin) * resx)));
			yCurvePts[0] = height - offy;

			xCurvePts[1] = (int) Math.round((offx + 1 + ((oldr.getTotal(settings.Unit) - xmin) * resx)));
			yCurvePts[1] = (int) (height - offy - ((oldr.getElevation(settings.Unit) - ymin) * resy));

			xCurvePts[2] = (int) Math.round((offx + 1 + ((r.getTotal(settings.Unit) - xmin) * resx)));
			yCurvePts[2] = (int) (height - offy - ((r.getElevation(settings.Unit) - ymin) * resy));

			xCurvePts[3] = (int) Math.round((offx + 1 + ((r.getTotal(settings.Unit) - xmin) * resx)));
			yCurvePts[3] = height - offy;

			avrSlope += Math.abs(r.getSlope());
			nbSlope++;

			if ((xCurvePts[2] - xCurvePts[0]) > track.CurveFilter) // Filtre
			{
				double avr = avrSlope / nbSlope;
				if (avr < 5.0)
					g2d.setColor(track.clProfil_SlopeInf5);
				else if (avr < 10.0)
					g2d.setColor(track.clProfil_SlopeInf10);
				else if (avr < 15.0)
					g2d.setColor(track.clProfil_SlopeInf15);
				else if (avr >= 15.0)
					g2d.setColor(track.clProfil_SlopeSup15);

				g2d.fillPolygon(xCurvePts, yCurvePts, 4);

				res = Utils.CalcLine(xCurvePts[1], yCurvePts[1], xCurvePts[2], yCurvePts[2], res);
				for (int tf = xCurvePts[1]; tf < xCurvePts[2]; tf++) {
					TabY[(int) tf] = (int) (res.a * tf + res.b);
					// First pass. Allow to draw the first line
					if ((first) && (tf > 0)) {
						TabY[(int) tf - 1] = TabY[(int) tf];
						first = false;
					}
				}

				oldr.setElevation(r.getElevation());
				oldr.setTotal(r.getTotal());
				avrSlope = 0.0;
				nbSlope = 0;
			}
		}

		// -- Draw the line on the profile
		oldr.setElevation(track.data.get(0).getElevation());
		oldr.setTotal(track.data.get(0).getTotal());

		g2d.setColor(track.clProfil_SlopeBorder);
		g2d.setStroke(PenSlopeBorder);

		for (CgData r : track.data) {
			// Point on the bottom left (old point)
			xCurvePts[0] = (int) Math.round((offx + 1 + ((oldr.getTotal(settings.Unit) - xmin) * resx)));
			yCurvePts[0] = height - offy;

			// Point on the top left (old point)
			xCurvePts[1] = (int) Math.round((offx + 1 + ((oldr.getTotal(settings.Unit) - xmin) * resx)));
			yCurvePts[1] = (int) (height - offy - ((oldr.getElevation(settings.Unit) - ymin) * resy));

			// Point on top right (new point)
			xCurvePts[2] = (int) Math.round((offx + 1 + ((r.getTotal(settings.Unit) - xmin) * resx)));
			yCurvePts[2] = (int) (height - offy - ((r.getElevation(settings.Unit) - ymin) * resy));

			// Point on bottom right (new point)
			xCurvePts[3] = (int) Math.round((offx + 1 + ((r.getTotal(settings.Unit) - xmin) * resx)));
			yCurvePts[3] = height - offy;

			// Filter
			if ((xCurvePts[2] - xCurvePts[0]) > track.CurveFilter) {
				g2d.drawLine(xCurvePts[1], yCurvePts[1], (int) xCurvePts[2], (int) yCurvePts[2]);

				oldr.setElevation(r.getElevation());
				oldr.setTotal(r.getTotal());
			}
		} // for

		// Last point
		g2d.fillPolygon(xCurvePts, yCurvePts, 4);
		g2d.setStroke(PenSimpleBorder);
		g2d.drawLine(xCurvePts[1], yCurvePts[1], xCurvePts[2], yCurvePts[2]);
	}

	public float getTransparence() {
		return transparence;
	}

	public void setTransparence(float transparence) {
		this.transparence = transparence;
	}

	public void save(String filename) {
		// -- Disable Highlight of the current box
		WithHighlight = false;
		// -- Save the image
		BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		paint(g);
		try {
			ImageIO.write(image, "png", new File(filename));
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		// -- Enable Highlight of the current box
		WithHighlight = true;
	}

	private int NbTag(int Value) {
		int n = 0;
		if ((Value & CgConst.TAG_HIGH_PT) != 0)
			n++;
		if ((Value & CgConst.TAG_LOW_PT) != 0)
			n++;
		if ((Value & CgConst.TAG_EAT_PT) != 0)
			n++;
		if ((Value & CgConst.TAG_WATER_PT) != 0)
			n++;
		if ((Value & CgConst.TAG_COOL_PT) != 0)
			n++;
		// if ((Value | CgConst.TAG_MARK)!=0) n++;
		if ((Value & CgConst.TAG_NOTE) != 0)
			n++;
		if ((Value & CgConst.TAG_INFO) != 0)
			n++;
		// if ((Value | CgConst.TAG_ROADBOOK) != 0) n++;
		return n;
	}

	public int getSelLine() {
		return SelLine;
	}

	public void setSelLine(int selLine) {
		SelLine = selLine;
		repaint();
	}

}
