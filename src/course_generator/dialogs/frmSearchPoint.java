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
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import course_generator.TrackData;
import course_generator.TrackData.SearchPointResult;
import course_generator.settings.CgSettings;
import course_generator.utils.CgConst;
import course_generator.utils.CustomFocusTraversalPolicy;
import course_generator.utils.JTextFieldLimit;
import course_generator.utils.Utils;

public class frmSearchPoint extends javax.swing.JDialog {
	private static final long serialVersionUID = 3806825690379556313L;
	private boolean ok;
	private CgSettings settings;
	private TrackData track;
	private java.util.ResourceBundle bundle;
	private JLabel lbLatitude;
	private JTextFieldLimit tfLatitude;
	private JLabel lbLongitude;
	private JTextFieldLimit tfLongitude;
	private JButton btSearch;
	private JPanel pnSearch;
	private JPanel pnResult;
	private JLabel lbPointFound;
	private JLabel lbPointFoundVal;
	private JLabel lbDistanceFromPoint;
	private JLabel lbDistanceFromPointVal;
	private JButton btClose;
	CustomFocusTraversalPolicy newPolicy;
	private List<frmSearchPointListener> listeners = new ArrayList<frmSearchPointListener>();
	private SearchPointResult result = null;

	/**
	 * Creates new form frmSettings
	 */
	public frmSearchPoint(CgSettings settings) {
		super();
		this.settings = settings;
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		initComponents();
	}

	public void addListener(frmSearchPointListener toAdd) {
		listeners.add(toAdd);
	}

	public void notifyUpdateUI() {
		for (frmSearchPointListener hl : listeners)
			hl.UpdateUIEvent();
	}

	public boolean showDialog(CgSettings settings, TrackData track) {
		if ((track == null) || (settings == null))
			return false;

		this.settings = settings;
		this.track = track;
		this.result = null;
		setVisible(true);
		return ok;
	}

	/**
	 * Manage low level key strokes ESCAPE : Close the window
	 *
	 * @return
	 */
	protected JRootPane createRootPane() {
		JRootPane rootPane = new JRootPane();
		KeyStroke strokeEscape = KeyStroke.getKeyStroke("ESCAPE");

		@SuppressWarnings("serial")
		Action actionListener = new AbstractAction() {
			public void actionPerformed(ActionEvent actionEvent) {
				setVisible(false);
			}
		};

		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(strokeEscape, "ESCAPE");
		rootPane.getActionMap().put("ESCAPE", actionListener);

		return rootPane;
	}

	/**
	 * Search the point from the latitude and longitude Display the result
	 */
	private void SearchPoint() {
		double lat = 0.0;
		double lon = 0.0;
		double dist = 0.0;
		boolean ok = true;

		lat = Utils.ParseDoubleEx(tfLatitude.getText(), -1000.0);
		if (lat != -1000.0)
			tfLatitude.setBackground(Color.WHITE);
		else {
			tfLatitude.setBackground(Color.MAGENTA);
			ok = false;
		}

		lon = Utils.ParseDoubleEx(tfLongitude.getText(), -1000.0);
		if (lon != -1000.0)
			tfLongitude.setBackground(Color.WHITE);
		else {
			tfLongitude.setBackground(Color.MAGENTA);
			ok = false;
		}

		// -- Double conversion fails? exit
		if (!ok)
			return;

		// -- Display the result
		lbPointFoundVal.setText(bundle.getString("frmSearchPoint.Searching"));
		result = track.SearchPoint(lat, lon);
		if (result.Point > -1) {
			lbPointFoundVal.setText(String.valueOf(result.Point + 1));

			if (settings.Unit == CgConst.UNIT_MILES_FEET) {
				// meter to miles
				dist = Utils.Meter2uMiles(result.Distance);
			} else
				dist = result.Distance;

			lbDistanceFromPointVal.setText(String.format("%1.0f", dist));

			notifyUpdateUI();
		}
	}

	/**
	 * This method is called to initialize the form.
	 */
	private void initComponents() {
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(bundle.getString("frmSearchPoint.title"));
		setAlwaysOnTop(true);
		setType(java.awt.Window.Type.NORMAL);
		setResizable(false);

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new GridBagLayout());

		// -- Search area
		pnSearch = new javax.swing.JPanel();
		pnSearch.setBorder(
				javax.swing.BorderFactory.createTitledBorder(bundle.getString("frmSearchPoint.pnSearch.border.title")));
		pnSearch.setLayout(new GridBagLayout());

		// -- Latitude
		lbLatitude = new javax.swing.JLabel();
		lbLatitude.setFocusable(false);
		lbLatitude.setText(bundle.getString("frmSearchPoint.lbLatitude.text"));
		Utils.addComponent(pnSearch, lbLatitude, 0, 0, 1, 1, 0, 0, 0, 5, 2, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		tfLatitude = new JTextFieldLimit(10);
		tfLatitude.setFocusable(true);
		// tfLatitude.setDocument(new JTextFieldLimit(10));
		tfLatitude.setMinimumSize(new Dimension(100, 20));
		tfLatitude.setHorizontalAlignment(javax.swing.JTextField.CENTER);
		Utils.addComponent(pnSearch, tfLatitude, 1, 0, 1, 1, 1, 0, 0, 5, 2, 5, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		// -- Longitude
		lbLongitude = new javax.swing.JLabel();
		lbLongitude.setFocusable(false);
		lbLongitude.setText(bundle.getString("frmSearchPoint.lbLongitude.text"));
		Utils.addComponent(pnSearch, lbLongitude, 0, 1, 1, 1, 0, 0, 0, 5, 0, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		tfLongitude = new JTextFieldLimit(10);
		tfLongitude.setFocusable(true);
		// tfLongitude.setDocument(new JTextFieldLimit(10));
		tfLongitude.setHorizontalAlignment(javax.swing.JTextField.CENTER);
		Utils.addComponent(pnSearch, tfLongitude, 1, 1, 1, 1, 1, 0, 0, 5, 0, 5, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		btSearch = new javax.swing.JButton();
		btSearch.setFocusable(true);
		btSearch.setToolTipText(bundle.getString("frmSearchPoint.btSearch.tooltips"));
		btSearch.setIcon(Utils.getIcon(this, "search.png", settings.DialogIconSize));
		btSearch.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SearchPoint();
			}
		});
		Utils.addComponent(pnSearch, btSearch, 2, 0, 1, 2, 0, 0, 0, 5, 0, 5, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.BOTH);

		Utils.addComponent(paneGlobal, pnSearch, 0, 0, 1, 1, 0, 0, 0, 5, 0, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		// -- Result area
		pnResult = new javax.swing.JPanel();
		pnResult.setBorder(
				javax.swing.BorderFactory.createTitledBorder(bundle.getString("frmSearchPoint.pnResult.border.title")));
		pnResult.setLayout(new GridBagLayout());

		// -- Found point
		lbPointFound = new javax.swing.JLabel();
		lbPointFound.setFocusable(false);
		lbPointFound.setText(bundle.getString("frmSearchPoint.lbPointFound.text"));
		Utils.addComponent(pnResult, lbPointFound, 0, 0, 1, 1, 0, 0, 0, 5, 2, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		lbPointFoundVal = new javax.swing.JLabel();
		lbPointFoundVal.setFocusable(false);
		lbPointFoundVal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
		lbPointFoundVal.setMinimumSize(new Dimension(100, 20));
		lbPointFoundVal.setPreferredSize(new Dimension(100, 20));
		lbPointFoundVal.setBackground(new java.awt.Color(255, 255, 255));
		lbPointFoundVal.setOpaque(true);
		lbPointFoundVal.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		Utils.addComponent(pnResult, lbPointFoundVal, 1, 0, 1, 1, 1, 0, 0, 5, 2, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		// -- Distance from point
		lbDistanceFromPoint = new javax.swing.JLabel();
		lbDistanceFromPoint.setFocusable(false);
		lbDistanceFromPoint.setText(bundle.getString("frmSearchPoint.lbDistanceFromPoint.text"));
		Utils.addComponent(pnResult, lbDistanceFromPoint, 0, 1, 1, 1, 0, 0, 0, 5, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		lbDistanceFromPointVal = new javax.swing.JLabel();
		lbDistanceFromPointVal.setFocusable(false);
		lbDistanceFromPointVal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
		lbDistanceFromPointVal.setMinimumSize(new Dimension(100, 20));
		lbDistanceFromPointVal.setPreferredSize(new Dimension(100, 20));
		lbDistanceFromPointVal.setBackground(new java.awt.Color(255, 255, 255));
		lbDistanceFromPointVal.setOpaque(true);
		lbDistanceFromPointVal.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		Utils.addComponent(pnResult, lbDistanceFromPointVal, 1, 1, 1, 1, 1, 0, 0, 5, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		Utils.addComponent(paneGlobal, pnResult, 0, 1, 1, 1, 0, 1, 0, 5, 0, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		// == Close button
		// ===========================================================
		btClose = new javax.swing.JButton();
		btClose.setFocusable(true);
		btClose.setText(bundle.getString("frmSearchPoint.btClose.text"));
		btClose.setIcon(Utils.getIcon(this, "cancel.png", settings.DialogIconSize));
		btClose.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setVisible(false);
			}
		});
		Utils.addComponent(paneGlobal, btClose, 0, 2, GridBagConstraints.REMAINDER, 1, 0, 0, 0, 0, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);

		// -- Configure the travel policy (TAB mvt)
		Vector<Component> order = new Vector<Component>(4);
		order.add(tfLatitude);
		order.add(tfLongitude);
		order.add(btSearch);
		order.add(btClose);
		newPolicy = new CustomFocusTraversalPolicy(order);
		// -- Set the travel policy
		setFocusTraversalPolicy(newPolicy);

		// --
		pack();

		// -- Center the windows
		setLocationRelativeTo(null);
	}

	public SearchPointResult getResult() {
		return result;
	}

}
