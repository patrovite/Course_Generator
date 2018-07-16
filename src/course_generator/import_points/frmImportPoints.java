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

package course_generator.import_points;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;

import course_generator.TrackData;
import course_generator.TrackData.SearchPointResult;
import course_generator.settings.CgSettings;
import course_generator.utils.CgLog;
import course_generator.utils.Utils;

public class frmImportPoints extends javax.swing.JDialog {
	private ResourceBundle bundle;
	private CgSettings settings;
	private TrackData track;
	private String name;
	private boolean ok;
	private JTable TableImport;
	private ImportPtsDataModel Model;
	private ImportPtsData list;
	private JScrollPane jScrollPaneImport;
	private JPanel jPanelButtons;
	private JButton btCancel;
	private JButton btImport;
	private JPanel panelSmallButtons;
	private JButton btSelAll;
	private JButton btUnselAll;
	private JButton btHelp;


	/**
	 * Creates new form frmImportPoints
	 */
	public frmImportPoints(CgSettings settings) {
		this.settings = settings;
		this.name = "";
		list = new ImportPtsData();
		Model = new ImportPtsDataModel(settings, list);
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		initComponents();
		setModal(true);
	}


	public int showDialog(String name, TrackData track) {
		this.name = name;
		this.track = track;

		// -- read the CGP file
		SaxCGPHandler CGPhandler = new SaxCGPHandler();

		int ret = 0;
		try {
			ret = CGPhandler.readDataFromCGP(name, list);
		} catch (Exception e) {
		}

		if (ret != 0)
			CgLog.error("frmImportPoints.showDialog : Error while reading '" + name + "'. Line ="
					+ CGPhandler.getErrLine());

		// -- Calculate distance
		SearchPointResult result = null;
		for (CgImportPts d : list.data) {
			result = track.SearchPoint(d.getLatitude(), d.getLongitude());
			if (result.Point > -1) {
				d.setDist(result.Distance);
				d.setLine(result.Point);
			} else {
				d.setDist(result.Distance);
				d.setLine(0);
			}
		}

		// -- Update the table
		// RefreshTable();

		// End set field
		ok = false;

		// -- Show the dialog
		setVisible(true);

		if (ok) {
			return 0;
		} else
			return -1;
	}


	/**
	 * Manage low level key strokes ESCAPE : Close the window
	 *
	 * @return
	 */
	protected JRootPane createRootPane() {
		JRootPane rootPane = new JRootPane();
		KeyStroke strokeEscape = KeyStroke.getKeyStroke("ESCAPE");
		KeyStroke strokeEnter = KeyStroke.getKeyStroke("ENTER");

		Action actionListener = new AbstractAction() {
			public void actionPerformed(ActionEvent actionEvent) {
				setVisible(false);
			}
		};

		Action actionListenerEnter = new AbstractAction() {
			public void actionPerformed(ActionEvent actionEvent) {
				RequestToClose();
			}
		};

		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(strokeEscape, "ESCAPE");
		rootPane.getActionMap().put("ESCAPE", actionListener);

		inputMap.put(strokeEnter, "ENTER");
		rootPane.getActionMap().put("ENTER", actionListenerEnter);

		return rootPane;
	}


	private void RequestToClose() {
		boolean param_valid = true;
		// check that the parameters are ok

		// -- Ok?
		if (param_valid) {
			ok = true;
			setVisible(false);
		}
	}


	private void initComponents() {
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(bundle.getString("frmImportPoints.title"));
		setMinimumSize(new Dimension(800, 400));
		setAlwaysOnTop(true);
		setType(java.awt.Window.Type.UTILITY);

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new GridBagLayout());

		TableImport = new javax.swing.JTable();
		TableImport.setModel(Model);
		TableImport.getTableHeader()
				.setDefaultRenderer(new ImportPtsHeaderRenderer(TableImport.getTableHeader().getDefaultRenderer()));
		TableImport.getTableHeader().setReorderingAllowed(false);
		TableImport.setDefaultRenderer(ImportPtsDataClass.class, new ImportPtsRenderer());

		TableImport.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		TableImport.setRowHeight(settings.TagIconSize+4);
		TableImport.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				TableMouseClicked(evt);
			}
		});
		TableImport.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				TableKeyReleased(evt);
			}
		});

		// -- Add the grid to a scroll panel
		// ------------------------------------
		jScrollPaneImport = new javax.swing.JScrollPane();
		jScrollPaneImport.setViewportView(TableImport);
		Utils.addComponent(paneGlobal, jScrollPaneImport, 0, 0, GridBagConstraints.REMAINDER, 1, 1, 1, 10, 10, 10, 10,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH);

		panelSmallButtons = new JPanel();
		panelSmallButtons.setLayout(new GridBagLayout());
		Utils.addComponent(paneGlobal, panelSmallButtons, 0, 1, GridBagConstraints.REMAINDER, 1, 1, 0, 0, 10, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH);

		btSelAll = new javax.swing.JButton();
		btSelAll.setIcon(Utils.getIcon(this,"select_all.png",settings.DialogIconSize));
		btSelAll.setToolTipText(bundle.getString("frmImportPoints.btSelAll.toolTipText"));
		btSelAll.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SelectAll();
			}
		});
		Utils.addComponent(panelSmallButtons, btSelAll, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.VERTICAL);

		btUnselAll = new javax.swing.JButton();
		btUnselAll.setIcon(Utils.getIcon(this,"unselect_all.png",settings.DialogIconSize));
		btUnselAll.setToolTipText(bundle.getString("frmImportPoints.btUnselAll.toolTipText"));
		btUnselAll.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				UnselectAll();
			}
		});
		Utils.addComponent(panelSmallButtons, btUnselAll, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.VERTICAL);

		btHelp = new javax.swing.JButton();
		btHelp.setIcon(Utils.getIcon(this,"help_dialog.png",settings.DialogIconSize));
		btHelp.setToolTipText(bundle.getString("frmImportPoints.btHelp.toolTipText"));
		Utils.addComponent(panelSmallButtons, btHelp, 2, 0, 1, 1, 1, 0, 0, 0, 0, 0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.VERTICAL);

		// == BUTTONS
		// ===========================================================
		jPanelButtons = new javax.swing.JPanel();
		jPanelButtons.setLayout(new FlowLayout());
		Utils.addComponent(paneGlobal, jPanelButtons, 0, 2, GridBagConstraints.REMAINDER, 1, 0, 0, 0, 0, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);

		btCancel = new javax.swing.JButton();
		btCancel.setIcon(Utils.getIcon(this,"cancel.png",settings.DialogIconSize));
		btCancel.setText(bundle.getString("Global.btCancel.text"));
		btCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setVisible(false);
			}
		});

		btImport = new javax.swing.JButton();
		btImport.setIcon(Utils.getIcon(this,"import.png",settings.DialogIconSize));
		btImport.setText(bundle.getString("frmImportPoints.btImport.text"));
		// btImport.setMinimumSize(btCancel.getMinimumSize());
		// btImport.setPreferredSize(btCancel.getPreferredSize());
		btImport.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ImportPoints();
				RequestToClose();
			}
		});

		// -- Add buttons
		jPanelButtons.add(btImport);
		jPanelButtons.add(btCancel);

		// --
		pack();

		setLocationRelativeTo(null);
	}


	/**
	 * Import the points in the track
	 */
	private void ImportPoints() {
		if (track.data.size() < 0)
			return;
		if (list.data.size() < 0)
			return;

		for (CgImportPts r : list.data) {
			if (r.getSel()) {
				track.data.get((int) r.getLine()).setTag(r.getTag());
				track.data.get((int) r.getLine()).setName(r.getName());
				track.data.get((int) r.getLine()).setComment(r.getComment());

				track.isModified = true;
			}
		}
	}


	/**
	 * Unselect every line
	 */
	private void UnselectAll() {
		if (list.data.size() <= 0)
			return;

		for (CgImportPts p : list.data) {
			p.setSel(false);
		}
		RefreshTable();
	}


	/**
	 * Select every line
	 */
	private void SelectAll() {
		if (list.data.size() <= 0)
			return;

		for (CgImportPts p : list.data) {
			p.setSel(true);
		}
		RefreshTable();
	}


	/**
	 * Table key release managment SPACE key invert the selection
	 * 
	 * @param evt
	 *            event
	 */
	protected void TableKeyReleased(KeyEvent evt) {
		if (list.data.size() <= 0)
			return;

		int row = TableImport.getSelectedRow();
		int col = TableImport.getSelectedColumn();

		if ((col == 0) && (evt.getKeyCode() == KeyEvent.VK_SPACE)) {
			list.data.get(row).invSel();
			RefreshTable();
		}
	}


	/**
	 * Table mouse click managment A click in the first column invert the selection
	 * 
	 * @param evt
	 *            event
	 */
	protected void TableMouseClicked(MouseEvent evt) {
		if (list.data.size() <= 0)
			return;

		int row = TableImport.rowAtPoint(evt.getPoint());
		int col = TableImport.columnAtPoint(evt.getPoint());

		if (col == 0) {
			list.data.get(row).invSel();
			RefreshTable();
		}
	}

	
	/**
	 * Refresh the table
	 */
	private void RefreshTable() {
		int r = TableImport.getSelectedRow();
		Model.fireTableDataChanged();
		if (r >= 0)
			TableImport.setRowSelectionInterval(r, r);
	}

}
