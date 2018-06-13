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

package course_generator.dialogs;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

import course_generator.utils.Utils;

public class frmAbout extends javax.swing.JDialog {
	private javax.swing.JLabel lbLogo;
	private javax.swing.JLabel lbVersion;
	private javax.swing.JLabel lbWebSite;
	private JLabel lbMessage1;
	private JLabel lbMessage2;

	private ResourceBundle bundle;
	private Timer timer;
	private JTextArea taCopyright;
	private JScrollPane jScrollPaneCopyright;

	class MyTimerActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			setVisible(false);
		}
	}


	/**
	 * Creates new form DialogAbout
	 */
	public frmAbout(java.awt.Frame parent, boolean modal, boolean autoclose, String version) {
		super(parent, modal);

		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		initComponents();

		lbVersion.setText("V" + version);

		if (autoclose) {
			Timer timer = new Timer(2000, new MyTimerActionListener());
			timer.start();
		}
	}


	/**
	 * Show the dialog
	 * 
	 * @param parent
	 *            Parent
	 * @param version
	 *            String containing the version of the software l
	 */
	public static void showDialogAbout(java.awt.Frame parent, boolean autoclose, boolean withcopyrights,
			String version) {
		frmAbout dlg = new frmAbout(parent, false, autoclose, version);
		dlg.Refresh(withcopyrights);
		dlg.setVisible(true);
	}


	private void Refresh(boolean withcopyrights) {
		if (withcopyrights) {
			StringBuilder sb = new StringBuilder();
			sb.append(" Thanks to the contributors :\n");
			sb.append(" - Frédéric Bard (frederic@freemovin.com)\n\n");
			sb.append(" .oO Copyrights Oo.\n");
			sb.append(" Used libraries:\n");
			sb.append(" - Joda-time - http://www.joda.org/joda-time\n");
			sb.append(" - SwingX - LGPL 2.1 - https://swingx.java.net\n");
			sb.append(" - JMapViewer - GPL - http://wiki.openstreetmap.org/wiki/JMapViewer\n");
			sb.append(" - jcommon - LGPL - http://www.jfree.org/jcommon\n");
			sb.append(" - jfreechart - LGPL - http://www.jfree.org/index.html\n");
			sb.append(" - TinyLaF - LGPL - Hans Bickel - http://www.muntjak.de/hans/java/tinylaf\n");
			sb.append(" - SunCalculator - Patrick Kalkman - pkalkie@gmail.com\n");
			sb.append("\n");
			sb.append(" Maps :\n");
			sb.append(" - Openstreetmap : http://www.openstreetmap.org\n");
			sb.append(" - OpenTopoMap : https://opentopomap.org\n");
			sb.append(" - Bing map : (C)Microsoft\n");
			taCopyright.setText(sb.toString());
			jScrollPaneCopyright.setPreferredSize(new Dimension(400, 100));
			jScrollPaneCopyright.setVisible(true);
		} else {
			jScrollPaneCopyright.setPreferredSize(new Dimension(0, 0));
			jScrollPaneCopyright.setVisible(false);
		}
		pack();
		setLocationRelativeTo(null);

	}


	/**
	 * This method initialize the form.
	 */
	private void initComponents() {

		// -- Define dialog properties ------------------------------------------
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setBackground(new java.awt.Color(153, 153, 153));
		setUndecorated(true);
		setResizable(false);
		setType(java.awt.Window.Type.POPUP);
		addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				setVisible(false);
			}
		});

		// -- Layout ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new GridBagLayout());

		taCopyright = new JTextArea();
		taCopyright.setEditable(false);
		jScrollPaneCopyright = new javax.swing.JScrollPane();
		jScrollPaneCopyright.setViewportView(taCopyright);
		jScrollPaneCopyright.setPreferredSize(new Dimension(0, 0));
		jScrollPaneCopyright.setVisible(false);
		Utils.addComponent(paneGlobal, jScrollPaneCopyright, 1, 0, 1, GridBagConstraints.REMAINDER, 1, 1, 0, 0, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH);

		// ----------------------------------------------------------------------
		lbVersion = new javax.swing.JLabel();
		lbVersion.setFont(new java.awt.Font("ARIAL", Font.BOLD + Font.ITALIC, 12));
		lbVersion.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		lbVersion.setForeground(Color.WHITE);
		lbVersion.setText("V0.00");

		Utils.addComponent(paneGlobal, lbVersion, 0, 0, 1, 1, 0, 0, 68, 0, 0, 100, GridBagConstraints.NORTHEAST,
				GridBagConstraints.NONE);

		// ----------------------------------------------------------------------
		lbWebSite = new javax.swing.JLabel();
		lbWebSite.setFont(new java.awt.Font("ARIAL", Font.BOLD + Font.ITALIC, 12));
		lbWebSite.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lbWebSite.setForeground(new Color(0xddff55));
		lbWebSite.setText("<html>www.TechAndRun.com<br>pierre@TechAndRun.com</html>");
		Utils.addComponent(paneGlobal, lbWebSite, 0, 1, 1, 1, 0, 0, 310, 450, 0, 0, GridBagConstraints.SOUTHWEST,
				GridBagConstraints.NONE);

		// ----------------------------------------------------------------------
		lbMessage1 = new javax.swing.JLabel();
		lbMessage1.setFont(new java.awt.Font("ARIAL", Font.BOLD + Font.ITALIC, 12));
		lbMessage1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lbMessage1.setForeground(Color.WHITE);
		lbMessage1.setText(bundle.getString("frmAbout.lbMessage1.Text"));
		Utils.addComponent(paneGlobal, lbMessage1, 0, 2, 1, 1, 0, 0, 10, 450, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.NONE);

		// ----------------------------------------------------------------------
		lbMessage2 = new javax.swing.JLabel();
		lbMessage2.setFont(new java.awt.Font("ARIAL", Font.BOLD + Font.ITALIC, 12));
		lbMessage2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lbMessage2.setForeground(Color.WHITE);
		lbMessage2.setText(bundle.getString("frmAbout.lbMessage2.Text"));
		Utils.addComponent(paneGlobal, lbMessage2, 0, 3, 1, 1, 0, 1, 0, 0, 0, 20, GridBagConstraints.NORTHEAST,
				GridBagConstraints.NONE);

		// ----------------------------------------------------------------------
		lbLogo = new javax.swing.JLabel();
		lbLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/png/image_logo.png")));

		// ----------------------------------------------------------------------
		Utils.addComponent(paneGlobal, lbLogo, 0, 0, 1, 4, 0, 1, 0, 0, 0, 0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE);

		// ----------------------------------------------------------------------
		pack();
		setLocationRelativeTo(null);
	}

}