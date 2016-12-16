package course_generator.mrb;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;

import javax.swing.JPanel;

import course_generator.CgData;
import course_generator.TrackData;
import course_generator.utils.Utils;
import course_generator.utils.Utils.CalcLineResult;

public class PanelProfilMRB extends JPanel {
	private float transparence;
	private TrackData track;
	private MrbDataList data;
	private int ProfileType;
	private Stroke PenBlackS;
	private int w;
	private int h;
	private int offx = 40; // Position en x de l'axe vertical du profil
	private int offy = 25; // Position en y de l'axe horizontal du profil depuis
							// le bas de l'image
	private int rMargin = 25; // Marge horizontal en pixel par rapport au bord
								// droit
	// int tMargin = 100; //Marge vertical en pixel par rapport au bord haut
	private double PixelPerHUnit = 8; // Number of pixel per horizontal unit
										// (kilomerter or miles)
	private double PixelPerVUnit = 0.1; // Number of pixel per vertical unit
										// (meter)
	// private int ConfigReplication = Constantes.MRB_REP_POS |
	// Constantes.MRB_REP_ALIGN | Constantes.MRB_REP_FORMAT |
	// Constantes.MRB_REP_SIZE | Constantes.MRB_REP_TAGS;
	private int RepLine = 0; // Ligne de base pour la réplication
	// bool LabelToBottom = false;
	private Boolean isGridInit = false;
	private int xSel1 = 0;
	private int xSel2 = 0;
	private int ySel1 = 0;
	private int ySel2 = 0;
	private int yDelta = -1;
	String[] MemoFormat;
	private BasicStroke myPenDot;
	private BasicStroke PenSimpleBorder;
	private int wp = 0; // width of the profil
	private int hp = 0; // height of the profil
	private BasicStroke PenRP_Border;
	private BasicStroke PenSlopeBorder;


	public PanelProfilMRB(int width, int height) {
		super();
		track = null;
		data = null;
		w = width;
		h = height;
		ProfileType = 0;

		PenBlackS = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 10, 0 }, 0);
		myPenDot = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 1, 2 }, 0);

		PenSimpleBorder = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 1, 0 }, 0);

		PenRP_Border = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 1, 0 }, 0);

		PenSlopeBorder = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 1, 0 }, 0);

		

		// grues=new ArrayList<Grue>();
		// ordreAffichage = new ArrayList<ElementTri>();
		// zoom=1.0;
		// transparence=0.70f;
		// fontIndicateur = new Font("ARIAL",Font.PLAIN,24);
		//
		// setPreferredSize(new Dimension(maxX,maxY));
		// //setBackground(new Color(0xFFECBE));
		// setBackground(Color.WHITE);
		// strokeEvolution = new BasicStroke(1, BasicStroke.CAP_BUTT,
		// BasicStroke.JOIN_BEVEL, 0,
		// new float[] { 5, 3 }, 0);
		// strokeFleche = new BasicStroke(20, BasicStroke.CAP_BUTT,
		// BasicStroke.JOIN_BEVEL, 0,
		// new float[] { 10, 0 }, 0);
		// strokeDistribution = new BasicStroke(2, BasicStroke.CAP_BUTT,
		// BasicStroke.JOIN_BEVEL, 0,
		// new float[] { 10, 0 }, 0);
		// stroke0Codeur = new BasicStroke(4, BasicStroke.CAP_BUTT,
		// BasicStroke.JOIN_BEVEL, 0,
		// new float[] { 10, 0 }, 0);
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
		w = width;
		setPreferredSize(new Dimension(w, h));
		repaint();
	}


	/**
	 * Return the width of the panel
	 */
	public int getWidth() {
		return w;
	}


	/**
	 * Set the height of the panel
	 * 
	 * @param height
	 *            Width of the panel
	 */
	public void setHeight(int height) {
		h = height;
		setPreferredSize(new Dimension(w, h));
		repaint();
	}


	/**
	 * Return the height of the panel
	 */
	public int getHeight() {
		return h;
	}


	public int getProfileType() {
		return ProfileType;
	}


	public void setProfileType(int profileType) {
		if ((profileType<0) || (profileType>2)) return;
		
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

		int n = 0;
		double[] stepval = { 1, 2, 2.5, 5 };
		double[] stepval2 = { 0.1, 0.1, 0.2, 0.1 };

		String s;
		// int w = bmProfil.Width;
		// int h = bmProfil.Height;
		double resx = 0.0;
		double resy = 0.0;
		double tf = 0;
		// double ta = 0.0;
		// double tb = 0.0;
		// int SelLine = GridMiniRoadbook.CurrentCellAddress.Y;
		boolean first = false;
		int[] TabY = new int[w];

		for (int i = 0; i < w; i++)
			TabY[i] = 0;

		// Calcul de la largeur du profil
		wp = w - offx - rMargin;
		PixelPerHUnit = wp / ((int) (track.getTotalDistance() / 1000.0));

		// Calcul de la hauteur du profil
		Double dElev = track.MaxElev - track.MinElev;
		if (dElev < 100) {
			dElev = Math.ceil(track.MaxElev / 10.0) * 10.0;
		} else {
			dElev = (Math.ceil(track.MaxElev / 100.0) * 100.0) - (Math.floor(track.MinElev / 100.0) * 100.0);
		}

		hp = h - offy - track.TopMargin;
		PixelPerVUnit = dElev / hp;

		// System.Drawing.Font SelFont= null;
		/*
		 * String s; int w = bmProfil.Width; int h = bmProfil.Height; double
		 * resx = 0.0; double resy = 0.0; double tf = 0; double ta=0.0; double
		 * tb=0.0; double[] stepval= {1, 2, 2.5 ,5}; double[] stepval2 = { 0.1,
		 * 0.1, 0.2, 0.1 }; int SelLine = GridMiniRoadbook.CurrentCellAddress.Y;
		 * boolean first = false; int[] TabY = new int[w];
		 * 
		 * for(int i=0;i<w;i++) TabY[i]=0;
		 * 
		 * using (Graphics g = Graphics.FromImage(bmProfil)) { //-- Tools
		 * creation System.Drawing.Pen PenBlackS = new
		 * System.Drawing.Pen(System.Drawing.Color.Black);
		 * 
		 * System.Drawing.Pen myPenLarge = new
		 * System.Drawing.Pen(System.Drawing.Color.Black); myPenLarge.Width =
		 * 4f;
		 * 
		 * System.Drawing.Pen PenSimpleBorder = new
		 * System.Drawing.Pen(Main.cd.clProfil_Simple_Border);
		 * //Color.FromArgb(0x05, 0x82, 0x05)); PenSimpleBorder.Width = 3f;
		 * 
		 * System.Drawing.Pen PenRP_Border = new
		 * System.Drawing.Pen(Main.cd.clProfil_RS_Border); PenRP_Border.Width =
		 * 3f;
		 * 
		 * System.Drawing.Pen PenSlopeBorder = new
		 * System.Drawing.Pen(Main.cd.clProfil_SlopeBorder);
		 * PenSlopeBorder.Width = 3f;
		 * 
		 * System.Drawing.Pen myPenMoy = new
		 * System.Drawing.Pen(System.Drawing.Color.Black); myPenMoy.Width = 2f;
		 * 
		 * System.Drawing.Pen myPenDot = new
		 * System.Drawing.Pen(System.Drawing.Color.Black); myPenDot.DashPattern
		 * = new float[] { 1, 2 };
		 * 
		 * System.Drawing.Brush myBrushWhite = new SolidBrush(Color.White);
		 * System.Drawing.Brush myBrushSelLabel = new
		 * SolidBrush(Color.FromArgb(0xFF,0xEF, 0xBB));
		 * 
		 * System.Drawing.Brush BrushSlopeInf5 = new
		 * SolidBrush(Main.cd.clProfil_SlopeInf5); System.Drawing.Brush
		 * BrushSlopeInf10 = new SolidBrush(Main.cd.clProfil_SlopeInf10);
		 * System.Drawing.Brush BrushSlopeInf15 = new
		 * SolidBrush(Main.cd.clProfil_SlopeInf15); System.Drawing.Brush
		 * BrushSlopeSup15 = new SolidBrush(Main.cd.clProfil_SlopeSup15);
		 * 
		 * System.Drawing.Brush BrushSimpleFill = new
		 * SolidBrush(Main.cd.clProfil_Simple_Fill);
		 * 
		 * System.Drawing.Brush BrushRP_Road = new
		 * SolidBrush(Main.cd.clProfil_RS_Road); System.Drawing.Brush
		 * BrushRP_Path = new SolidBrush(Main.cd.clProfil_RS_Path);
		 * 
		 * System.Drawing.Brush myBrushBlack = new SolidBrush(Color.Black);
		 * 
		 * System.Drawing.Font FontSmall = new System.Drawing.Font("TAHOMA", 7);
		 * System.Drawing.Font FontAvr = new System.Drawing.Font("TAHOMA", 8);
		 * System.Drawing.Font FontBig = new System.Drawing.Font("TAHOMA", 10);
		 */
		Font FontSmall = new Font("ARIAL", Font.PLAIN, 7);
		Font FontAvr = new Font("ARIAL", Font.PLAIN, 8);
		Font FontBig = new Font("ARIAL", Font.PLAIN, 10);

		CalcLineResult res = new CalcLineResult();

		// -- Main frame display with white background
		g2d.setPaint(Color.WHITE);
		g2d.fillRect(0, 0, w, h);

		g2d.setStroke(PenBlackS);
		g2d.setPaint(Color.BLACK);
		g2d.drawRect(0, 0, w - 1, h - 1);

		if (data == null)
			return;
		if (track == null)
			return;
		if (track.data.isEmpty())
			return;

		// -- Draw the vertical axis
		int YPosArrow = 15; // Position of the arrow compare to the top of the
							// profil
		g2d.setStroke(PenBlackS);
		g2d.drawLine(offx, track.TopMargin - YPosArrow, offx, h - offy + 3);

		// --Draw the end of the vertical axis
		g2d.setPaint(Color.BLACK);
		int[] x = { offx - 5, offx, offx + 5 };
		int[] y = { track.TopMargin - YPosArrow - 1, track.TopMargin - YPosArrow - 10,
				track.TopMargin - YPosArrow - 1 };
		g2d.fillPolygon(x, y, 3);

		// //-- Draw the vertical unit
		// g.DrawString("m", FontAvr, Brushes.Black, offx - 15 - 2,
		// Main.cd.TopMargin - YPosArrow - FontAvr.GetHeight());

		// TODO Set the horizontal text pos corresponding to the text size
		g2d.drawString("m", offx - 15 - 2, track.TopMargin - YPosArrow); // -
																			// FontAvr.GetHeight());

		// -- Draw the horizontal axis
		g2d.drawLine(offx - 3, h - offy, w - 10, h - offy);

		// -- Draw the end of the horizontal axis
		int[] x1 = { w - 15, w - 7, w - 15 };
		int[] y1 = { h - 20, h - 25, h - 30 };
		g2d.fillPolygon(x1, y1, 3);

		// //-- Draw the horizontal unit
		// g.DrawString("km", FontAvr, Brushes.Black, w - 20, h-offy-18);
		// TODO Set the pos of the text corresponding to the text length
		g2d.drawString("km", w - 20, h - offy - 18);

		// if (!track.data.isEmpty())
		// {
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
		// //Double dElev = Main.cd.MaxElev - Main.cd.MinElev;
		//
		// -- Min and max of the X axis
		xmin = 0.0;
		xmax = track.getTotalDistance();

		// -- Calcul graduation axe X
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

		// -- Détermination du mini et maxi de l'axe Y
		if (track.MaxElev - track.MinElev < 100)
			ymin = 0.0;
		else
			ymin = Math.floor(track.MinElev / 100.0) * 100.0;
		ymax = track.MaxElev;

		// -- Calcul graduation axe Y
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
		// g.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.None;

		// -- draw the Vertical grid --
		g2d.setStroke(PenBlackS);
		g2d.setPaint(Color.BLACK);
		for (double i = xmin; i <= xmax; i += stepx2) {
			// g.DrawLine(PenBlackS, offx + (int)(i * resx), h - offy + 2, offx
			// + (int)(i * resx), h - offy);
			g2d.drawLine(offx + (int) (i * resx), h - offy + 2, offx + (int) (i * resx), h - offy);
		}

		for (double i = xmin; i <= xmax; i += stepx) {
			g2d.setStroke(myPenDot);
			g2d.drawLine(offx + (int) (i * resx), h - offy, offx + (int) (i * resx), h - offy - hp);
			g2d.setStroke(PenBlackS);
			g2d.drawLine(offx + (int) (i * resx), h - offy + 4, offx + (int) (i * resx), h - offy);
		}

		// -- Draw the Horizontal grid --
		g2d.setStroke(PenBlackS);
		for (double i = ymin; i <= ymax; i += stepy2) {
			g.drawLine(offx - 2, h - offy - (int) ((i - ymin) * resy), offx, h - offy - (int) ((i - ymin) * resy));
		}

		for (double i = ymin; i <= ymax; i += stepy) {
			g2d.setStroke(myPenDot);
			g.drawLine(offx, h - offy - (int) ((i - ymin) * resy), offx + wp, h - offy - (int) ((i - ymin) * resy));
			g2d.setStroke(PenBlackS);
			g.drawLine(offx - 4, h - offy - (int) ((i - ymin) * resy), offx, h - offy - (int) ((i - ymin) * resy));
		}

		// -- Draw the value of the Y axis
		g2d.setFont(FontAvr);
		g2d.setPaint(Color.BLACK);
		for (int c = 0; c <= NbGradY; c++) {
			s = String.format("%1.0f", (ymin + c * stepy));
			Dimension d = Utils.StringDimension(g2d, s);
			g2d.drawString(s, offx - d.width - 5, (int) ((h - offy) - (c * stepy * resy)) + (d.height / 2));
		}

		// -- Draw the value of the X axis
		for (int c = 0; c <= NbGradX; c++) {
			s = String.format("%1.0f", (xmin + c * stepx) / 1000);
			// stringSize = g.MeasureString(s, FontAvr);
			Dimension d = Utils.StringDimension(g2d, s);
			g2d.drawString(s, (int) (offx + (c * stepx * resx) - (d.width / 2)), h - offy + 4 + d.height);
		}

		// -- Tracé de la courbe - Préparation --
		// PointF pt1 = new PointF(0.0F, 0.0F);
		// PointF pt2 = new PointF(0.0F, 0.0F);
		// PointF pt3 = new PointF(0.0F, 0.0F);
		// PointF pt4 = new PointF(0.0F, 0.0F);
		// PointF[] curvePts = { pt1, pt2, pt3, pt4 };

		int[] xCurvePts = { 0, 0, 0, 0 };
		int[] yCurvePts = { 0, 0, 0, 0 };

		CgData oldr = new CgData();
		oldr.setElevation(track.data.get(0).getElevation());
		oldr.setTotal(track.data.get(0).getTotal());

		// Brush b = Brushes.Black;
		// -- Dessin du profil avec une couleur unique --
		if (ProfileType == 0) {
			first = true;
			Point[] curvePts;
			curvePts = new Point[4];

			g2d.setPaint(track.clProfil_Simple_Fill);
			// -- Dessin du profil avec une couleur unique
			for (CgData r : track.data) {
				// Point en bas à gauche (ancien point)
				xCurvePts[0] = (int) Math.round((offx + 1 + ((oldr.getTotal() - xmin) * resx)));
				yCurvePts[0] = h - offy;

				// point en haut à gauche (ancien point)
				xCurvePts[1] = (int) Math.round((offx + 1 + ((oldr.getTotal() - xmin) * resx)));
				yCurvePts[1] = (int) (h - offy - ((oldr.getElevation() - ymin) * resy));

				// point en haut à droite (nouveau point)
				xCurvePts[2] = (int) Math.round((offx + 1 + ((r.getTotal() - xmin) * resx)));
				yCurvePts[2] = (int) (h - offy - ((r.getElevation() - ymin) * resy));

				// point en bas à droite (nouveau point)
				xCurvePts[3] = (int) Math.round((offx + 1 + ((r.getTotal() - xmin) * resx)));
				yCurvePts[3] = (int) (h - offy);

				// b = BrushSimpleFill;

				// g.SmoothingMode =
				// System.Drawing.Drawing2D.SmoothingMode.None;

				if ((xCurvePts[2] - xCurvePts[0]) > track.CurveFilter) // Filtre
				{
					// g.FillPolygon(b, curvePts, newFillMode);
					g2d.fillPolygon(xCurvePts, yCurvePts, 4);

					res = Utils.CalcLine(xCurvePts[1], yCurvePts[1], xCurvePts[2], yCurvePts[2], res);
					for (tf = xCurvePts[1]; tf < xCurvePts[2]; tf++) {
						TabY[(int) tf] = (int) (res.a * tf + res.b);
						// Premier passage. Permet de tracer le premier trait
						if ((first) && (tf > 0)) {
							TabY[(int) tf - 1] = TabY[(int) tf];
							first = false;
						}
					}
					oldr.setElevation(r.getElevation());
					oldr.setTotal(r.getTotal());
				}
			} // for

			// -- Dessin du trait sur le profil
			oldr.setElevation(track.data.get(0).getElevation());
			oldr.setTotal(track.data.get(0).getTotal());

			g2d.setColor(track.clProfil_Simple_Border);
			g2d.setStroke(PenSimpleBorder);
			for (CgData r : track.data) {
				// Point en bas à gauche (ancien point)
				xCurvePts[0] = (int) Math.round((offx + 1 + ((oldr.getTotal() - xmin) * resx)));
				yCurvePts[0] = h - offy;

				// point en haut à gauche (ancien point)
				xCurvePts[1] = (int) Math.round((offx + 1 + ((oldr.getTotal() - xmin) * resx)));
				yCurvePts[1] = (int) (h - offy - ((oldr.getElevation() - ymin) * resy));

				// point en haut à droite (nouveau point)
				xCurvePts[2] = (int) Math.round((offx + 1 + ((r.getTotal() - xmin) * resx)));
				yCurvePts[2] = (int) (h - offy - ((r.getElevation() - ymin) * resy));

				// point en bas à droite (nouveau point)
				xCurvePts[3] = (int) Math.round((offx + 1 + ((r.getTotal() - xmin) * resx)));
				yCurvePts[3] = h - offy;

				// b = BrushSimpleFill;

				// g.SmoothingMode =
				// System.Drawing.Drawing2D.SmoothingMode.AntiAlias;

				if ((xCurvePts[2] - xCurvePts[0]) > track.CurveFilter) // Filtre
				{
					g2d.drawLine(xCurvePts[1], yCurvePts[1], xCurvePts[2], yCurvePts[2]);
					oldr.setElevation(r.getElevation());
					oldr.setTotal(r.getTotal());
				}
			} // foreach

			// Dernier point
			g2d.fillPolygon(xCurvePts, yCurvePts, 4);
			g2d.drawLine(xCurvePts[1], yCurvePts[1], xCurvePts[2], yCurvePts[2]);

		}

		// -- Dessin du profil avec une couleur pour la route et pour les
		// sentiers --
		else if (ProfileType == 1) {

			int cmpt = 0;
			first = true;
			// -- Dessin du profil avec une couleur pour la route et pour les
			// sentiers
			for (CgData r : track.data) {

				// Point en bas à gauche (ancien point)
				xCurvePts[0] = (int) Math.round((offx + 1 + ((oldr.getTotal() - xmin) * resx)));
				yCurvePts[0] = h - offy;

				// point en haut à gauche (ancien point)
				xCurvePts[1] = (int) Math.round((offx + 1 + ((oldr.getTotal() - xmin) * resx)));
				yCurvePts[1] = (int) (h - offy - ((oldr.getElevation() - ymin) * resy));

				// point en haut à droite (nouveau point)
				xCurvePts[2] = (int) Math.round((offx + 1 + ((r.getTotal() - xmin) * resx)));
				yCurvePts[2] = (int) (h - offy - ((r.getElevation() - ymin) * resy));

				// point en bas à droite (nouveau point)
				xCurvePts[3] = (int) Math.round((offx + 1 + ((r.getTotal() - xmin) * resx)));
				yCurvePts[3] = h - offy;

				g2d.setColor(track.clProfil_RS_Path);

				if (r.getDiff() == 100)
					cmpt++;

				// g.SmoothingMode =
				// System.Drawing.Drawing2D.SmoothingMode.None;

				if ((xCurvePts[2] - xCurvePts[0]) > track.CurveFilter) // Filtre
				{
					if (cmpt > 0)
						g2d.setColor(track.clProfil_RS_Road);
					g2d.fillPolygon(xCurvePts, yCurvePts, 4);
					// g.FillPolygon(b, curvePts, newFillMode);

					res = Utils.CalcLine(xCurvePts[1], yCurvePts[1], xCurvePts[2], yCurvePts[2], res);
					for (tf = xCurvePts[1]; tf < xCurvePts[2]; tf++) {
						TabY[(int) tf] = (int) (res.a * tf + res.b);
						// Premier passage. Permet de tracer le premier trait
						if ((first) && (tf > 0)) {
							TabY[(int) tf - 1] = TabY[(int) tf];
							first = false;
						}
					}

					oldr.setElevation(r.getElevation());
					oldr.setTotal(r.getTotal());
					cmpt = 0;
				}

			} // foreach

			// -- Dessin du trait sur le profil
			oldr.setElevation(track.data.get(0).getElevation());
			oldr.setTotal(track.data.get(0).getTotal());

			for (CgData r : track.data) {
				// Point en bas à gauche (ancien point)
				xCurvePts[0] = (int) Math.round((offx + 1 + ((oldr.getTotal() - xmin) * resx)));
				yCurvePts[0] = h - offy;

				// point en haut à gauche (ancien point)
				xCurvePts[1] = (int) Math.round((offx + 1 + ((oldr.getTotal() - xmin) * resx)));
				yCurvePts[1] = (int) (h - offy - ((oldr.getElevation() - ymin) * resy));

				// point en haut à droite (nouveau point)
				xCurvePts[2] = (int) Math.round((offx + 1 + ((r.getTotal() - xmin) * resx)));
				yCurvePts[2] = (int) (h - offy - ((r.getElevation() - ymin) * resy));

				// point en bas à droite (nouveau point)
				xCurvePts[3] = (int) Math.round((offx + 1 + ((r.getTotal() - xmin) * resx)));
				yCurvePts[3] = h - offy;

				g2d.setColor(track.clProfil_RS_Path);

				// g.SmoothingMode =
				// System.Drawing.Drawing2D.SmoothingMode.AntiAlias;

				g2d.setColor(track.clProfil_RS_Border);
				g2d.setStroke(PenRP_Border);

				if ((xCurvePts[2] - xCurvePts[0]) > track.CurveFilter) // Filtre
				{
					g2d.drawLine(xCurvePts[1], yCurvePts[1], xCurvePts[2], yCurvePts[2]);
					oldr.setElevation(r.getElevation());
					oldr.setTotal(r.getTotal());
				}

			} // foreach

			// Dernier point
			// g2d.fillPolygon(b, curvePts, newFillMode);
			g2d.fillPolygon(xCurvePts, yCurvePts, 4);
			g2d.drawLine(xCurvePts[1], yCurvePts[1], xCurvePts[2], yCurvePts[2]);

		}

		// -- Boucle de dessin de la courbe avec dénivelé --
		else if (ProfileType == 2) {
			double avrSlope = 0.0;
			int nbSlope = 0;
			g2d.setColor(Color.BLACK);
			// b = Brushes.Black;
			first = true;

			// -- Dessin du profil avec la pente
			for (CgData r : track.data) {
				// g.SmoothingMode =
				// System.Drawing.Drawing2D.SmoothingMode.None;
				xCurvePts[0] = (int) Math.round((offx + 1 + ((oldr.getTotal() - xmin) * resx)));
				yCurvePts[0] = h - offy;

				xCurvePts[1] = (int) Math.round((offx + 1 + ((oldr.getTotal() - xmin) * resx)));
				yCurvePts[1] = (int) (h - offy - ((oldr.getElevation() - ymin) * resy));

				xCurvePts[2] = (int) Math.round((offx + 1 + ((r.getTotal() - xmin) * resx)));
				yCurvePts[2] = (int) (h - offy - ((r.getElevation() - ymin) * resy));

				xCurvePts[3] = (int) Math.round((offx + 1 + ((r.getTotal() - xmin) * resx)));
				yCurvePts[3] = h - offy;

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
					// Brushes.OrangeRed;//FF8300
					else if (avr >= 15.0)
						g2d.setColor(track.clProfil_SlopeSup15);

					g2d.fillPolygon(xCurvePts, yCurvePts, 4);

					res = Utils.CalcLine(xCurvePts[1], yCurvePts[1], xCurvePts[2], yCurvePts[2], res);
					for (tf = xCurvePts[1]; tf < xCurvePts[2]; tf++) {
						TabY[(int) tf] = (int) (res.a * tf + res.b);
						// Premier passage. Permet de tracer le premier trait
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

			// -- Dessin du trait sur le profil
			oldr.setElevation(track.data.get(0).getElevation());
			oldr.setTotal(track.data.get(0).getTotal());

			g2d.setColor(track.clProfil_SlopeBorder);
			g2d.setStroke(PenSlopeBorder);
			
			for (CgData r : track.data) {
				// Point en bas à gauche (ancien point)
				xCurvePts[0] = (int) Math.round((offx + 1 + ((oldr.getTotal() - xmin) * resx)));
				yCurvePts[0] = h - offy;

				// point en haut à gauche (ancien point)
				xCurvePts[1] = (int) Math.round((offx + 1 + ((oldr.getTotal() - xmin) * resx)));
				yCurvePts[1] = (int) (h - offy - ((oldr.getElevation() - ymin) * resy));

				// point en haut à droite (nouveau point)
				xCurvePts[2] = (int) Math.round((offx + 1 + ((r.getTotal() - xmin) * resx)));
				yCurvePts[2] = (int) (h - offy - ((r.getElevation() - ymin) * resy));

				// point en bas à droite (nouveau point)
				xCurvePts[3] = (int) Math.round((offx + 1 + ((r.getTotal() - xmin) * resx)));
				yCurvePts[3] = h - offy;


				// g.SmoothingMode =
				// System.Drawing.Drawing2D.SmoothingMode.AntiAlias;
				if ((xCurvePts[2] - xCurvePts[0]) > track.CurveFilter) // Filtre
				{
					g2d.drawLine(xCurvePts[1], yCurvePts[1], (int) xCurvePts[2], (int) yCurvePts[2]);

					oldr.setElevation(r.getElevation());
					oldr.setTotal(r.getTotal());
				}
			} // foreach

			// Dernier point
			// g.FillPolygon(b, curvePts, newFillMode);
			g2d.fillPolygon(xCurvePts, yCurvePts, 4);
			g2d.setStroke(PenSimpleBorder);
			g2d.drawLine(xCurvePts[1], yCurvePts[1], xCurvePts[2], yCurvePts[2]);
		}

		// //-- Dessin des lignes verticales --
		// double km = 0;
		// int posx = 0;
		// int posy = 0;
		// int posy_s = 0;
		// foreach (cgData r in Main.cd.data)
		// {
		// if (
		// (
		// ((r.Tag & Constantes.TAG_HIGH_PT) != 0) ||
		// ((r.Tag & Constantes.TAG_LOW_PT) != 0) ||
		// ((r.Tag & Constantes.TAG_EAT_PT) != 0) ||
		// ((r.Tag & Constantes.TAG_WATER_PT) != 0) ||
		// ((r.Tag & Constantes.TAG_MARK) != 0)
		// ) && ((r.OptionMiniRoadbook & Constantes.MRBOPT_SEL)!=0)
		// )
		// {
		//
		// km = r.getTotal();
		// posx = (int)(km * resx);
		// //posy = hp + tMargin - 5 - r.VPosMiniRoadbook;
		// posy = h - offy - (hp + Main.cd.TopMargin - 7 - r.VPosMiniRoadbook);
		// if (Main.cd.LabelToBottom)
		// posy_s = h - offy;
		// else
		// {
		// //posy_s = (int)(h - offy - ((r.getElevation() - ymin) * resy));
		// posy_s = TabY[offx + posx];
		// }
		//
		// g.DrawLine(myPenMoy, offx + posx, posy_s, offx + posx, posy); //h -
		// offy - posy);
		// }
		// }

		// //-- Dessin des boites de textes --
		// int ii = 0;
		// foreach (cgData r in Main.cd.data)
		// {
		// if (
		// (
		// ((r.Tag & Constantes.TAG_HIGH_PT) != 0) ||
		// ((r.Tag & Constantes.TAG_LOW_PT) != 0) ||
		// ((r.Tag & Constantes.TAG_EAT_PT) != 0) ||
		// ((r.Tag & Constantes.TAG_WATER_PT) != 0) ||
		// ((r.Tag & Constantes.TAG_MARK) != 0)
		// ) && ((r.OptionMiniRoadbook & Constantes.MRBOPT_SEL) != 0)
		// )
		// {
		// //--Caclul le nb de tag
		// n = NbTag(r.Tag);
		//
		// km = r.getTotal();
		// posx = (int)(km * resx);
		// posy = hp + Main.cd.TopMargin - 7 - r.VPosMiniRoadbook;
		// /*
		// if ((r.OptionMiniRoadbook & Constantes.MRBOPT_FONTSMALL) != 0)
		// SelFont = FontSmall;
		// else if ((r.OptionMiniRoadbook & Constantes.MRBOPT_FONTAVR) != 0)
		// SelFont = FontAvr;
		// else if ((r.OptionMiniRoadbook & Constantes.MRBOPT_FONTBIG) != 0)
		// SelFont = FontBig;
		// */
		// if (SelFont != null) SelFont.Dispose();
		// SelFont = new System.Drawing.Font("TAHOMA", r.FontSizeMiniRoadbook);
		//
		// //s = "Les Contamines\n1083m - km:100\n10h23";
		// //s = GenLabel("%N\n%Am - km:%D\n%H", r);
		// //s = GenLabel(r.FmtLbMiniRoadbook, r);
		// s = Utils.GenLabel(r.FmtLbMiniRoadbook, r, Main.cd);
		// stringSize = g.MeasureString(s, SelFont);
		//
		// float l = stringSize.Width + 4 + 2;
		// float l1 = (n * 16) + ((n - 1) * 2) + 4;
		//
		// if (l1 > l) l = l1;
		//
		// //Détermination de l'alignement (gauche/centrer/droite)
		// //Gauche on ne change rien
		// if ((r.OptionMiniRoadbook & Constantes.MRBOPT_CENTER) != 0)
		// {
		// posx = posx - ((int)l + 4) / 2;
		// }
		//
		// if ((r.OptionMiniRoadbook & Constantes.MRBOPT_RIGHT) != 0)
		// {
		// posx = posx - ((int)l + 1);
		// }
		//
		// //-- Size of the tags (with or without tags)
		// int htags = 16;
		// if (((r.OptionMiniRoadbook & Constantes.MRBOPT_SHOWTAGS) == 0) || (n
		// == 0)) htags = 0;
		//
		// //-- Draw rectangle
		// if ((ii == SelLine) && (WithHighlight))
		// g.FillRectangle(myBrushSelLabel, offx + posx, h - offy - posy, l,
		// stringSize.Height + htags + 4);
		// else
		// g.FillRectangle(myBrushWhite, offx + posx, h - offy - posy, l,
		// stringSize.Height + htags + 4);
		// g.DrawRectangle(PenBlackS, offx + posx, h - offy - posy, l,
		// stringSize.Height + htags + 4);
		//
		// if (ii == SelLine)
		// {
		// xSel1 = offx + posx;
		// ySel1 = h - offy - posy;
		// xSel2 = xSel1 + (int)l;
		// ySel2 = ySel2 + (int)stringSize.Height + htags + 4;
		// }
		//
		// // Set format of string.
		// StringFormat drawFormat = new StringFormat();
		// drawFormat.Alignment = StringAlignment.Center;
		// drawFormat.LineAlignment = StringAlignment.Center;
		//
		// g.DrawString(s, SelFont, Brushes.Black, offx + posx + 0 + l / 2, h -
		// offy - posy + 2 + stringSize.Height / 2, drawFormat);
		//
		//
		// if ((r.OptionMiniRoadbook & Constantes.MRBOPT_SHOWTAGS) != 0)
		// {
		// int m = 0;
		// int lt = (int)((l - l1) / 2);
		//
		// if ((r.Tag & Constantes.TAG_HIGH_PT) != 0)
		// {
		// imageListGrid.Draw(g, (offx + posx + 2 + m + lt), h - offy - posy + 1
		// + (int)(stringSize.Height), 0);
		// m += 18;
		// }
		//
		// if ((r.Tag & Constantes.TAG_LOW_PT) != 0)
		// {
		// imageListGrid.Draw(g, (offx + posx + 2 + m + lt), h - offy - posy + 1
		// + (int)(stringSize.Height), 1);
		// m += 18;
		// }
		//
		// if ((r.Tag & Constantes.TAG_EAT_PT) != 0)
		// {
		// imageListGrid.Draw(g, (offx + posx + 2 + m + lt), h - offy - posy + 1
		// + (int)(stringSize.Height), 2);
		// m += 18;
		// }
		//
		// if ((r.Tag & Constantes.TAG_WATER_PT) != 0)
		// {
		// imageListGrid.Draw(g, (offx + posx + 2 + m + lt), h - offy - posy + 1
		// + (int)(stringSize.Height), 3);
		// m += 18;
		// }
		//
		// if ((r.Tag & Constantes.TAG_COOL_PT) != 0)
		// {
		// imageListGrid.Draw(g, (offx + posx + 2 + m + lt), h - offy - posy + 1
		// + (int)(stringSize.Height), 4);
		// m += 18;
		// }
		//
		// if ((r.Tag & Constantes.TAG_NOTE) != 0)
		// {
		// imageListGrid.Draw(g, (offx + posx + 2 + m + lt), h - offy - posy + 1
		// + (int)(stringSize.Height), 6);
		// m += 18;
		// }
		//
		// if ((r.Tag & Constantes.TAG_INFO) != 0)
		// {
		// imageListGrid.Draw(g, (offx + posx + 2 + m + lt), h - offy - posy + 1
		// + (int)(stringSize.Height), 7);
		// m += 18;
		// }
		// }
		// //ii++;
		// }
		// if (
		// (
		// ((r.Tag & Constantes.TAG_HIGH_PT) != 0) ||
		// ((r.Tag & Constantes.TAG_LOW_PT) != 0) ||
		// ((r.Tag & Constantes.TAG_EAT_PT) != 0) ||
		// ((r.Tag & Constantes.TAG_WATER_PT) != 0) ||
		// ((r.Tag & Constantes.TAG_MARK) != 0)
		// ) //&& ((r.OptionMiniRoadbook & Constantes.MRBOPT_SEL) != 0)
		// ) ii++;
		// }
		//
		// //-- Dessin du copyright --
		// Image myImage = Resources.copyright;
		// Bitmap bmCopyright = null;
		// if (bmProfil.Height>290)
		// bmCopyright = new Bitmap(Resources.copyright);
		// else if (bmProfil.Height > 230)
		// bmCopyright = new Bitmap(Resources.copyright_S);
		// else
		// bmCopyright = new Bitmap(Resources.copyright_XS);
		//
		// g.DrawImage(bmCopyright,w-15,(h-offy-bmCopyright.Height)/2);
		// bmCopyright.Dispose();
		//
		// }

		// //-- On supprime les "outils" - Dispose
		// FontSmall.Dispose();
		// FontAvr.Dispose();
		// FontBig.Dispose();
		// myBrushBlack.Dispose();
		// myBrushWhite.Dispose();
		// myBrushSelLabel.Dispose();
		// BrushSlopeInf5.Dispose();
		// BrushSlopeInf10.Dispose();
		// BrushSlopeInf15.Dispose();
		// BrushSlopeSup15.Dispose();
		// BrushSimpleFill.Dispose();
		// BrushRP_Road.Dispose();
		// BrushRP_Path.Dispose();
		// PenBlackS.Dispose();
		// myPenLarge.Dispose();
		// PenSimpleBorder.Dispose();
		// PenRP_Border.Dispose();
		// PenSlopeBorder.Dispose();
		// myPenMoy.Dispose();
		// myPenDot.Dispose();
		// if (SelFont != null) SelFont.Dispose();
		// }

		// //-- On affecte l'image à la picturebox
		// pbProfil.Image = bmProfil;
		// pbProfil.Height = pbProfil.Image.Height;
		// pbProfil.Width = pbProfil.Image.Width;

		g2d.dispose();
	}


	public float getTransparence() {
		return transparence;
	}


	public void setTransparence(float transparence) {
		this.transparence = transparence;
	}

}
