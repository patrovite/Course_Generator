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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;

import course_generator.settings.CgSettings;
import course_generator.utils.Utils;


public class frmImportPoints extends javax.swing.JDialog {
	private ResourceBundle bundle;
	private CgSettings settings;
	private String name;
	private boolean ok;
	private JTable TableImport;
	private ImportPtsDataModel Model;
	private JScrollPane jScrollPaneImport;
	private JPanel jPanelButtons;
	private JButton btCancel;
	private JButton btOk;
	private JPanel panelHelp;
	
	/**
	 * Creates new form frmImportPoints
	 */
	public frmImportPoints(CgSettings settings) {
		this.settings=settings;
		this.name="";
		Model = new ImportPtsDataModel(settings);
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		initComponents();
		setModal(true);
	}

	public int showDialog(String name) {
		this.name=name;
		
		// End set field
		ok = false;

		//-- Show the dialog
		setVisible(true);

		if (ok) {
			return 0;
		}
		else return -1;
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
		setAlwaysOnTop(true);
		setResizable(false);
		setType(java.awt.Window.Type.UTILITY);
		
		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new GridBagLayout());
	
		TableImport = new javax.swing.JTable();
		TableImport.setModel(Model);
//		TableImport.getTableHeader()
//				.setDefaultRenderer(new MainHeaderRenderer(TableImport.getTableHeader().getDefaultRenderer()));
		TableImport.getTableHeader().setReorderingAllowed(false);

		TableImport.setDefaultRenderer(ImportPtsDataClass.class, new ImportPtsRenderer());

		TableImport.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		TableImport.setRowHeight(20);
//		TableImport.addMouseListener(new java.awt.event.MouseAdapter() {
//			public void mouseClicked(java.awt.event.MouseEvent evt) {
//				if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() >= 2 && !evt.isConsumed()) {
//					evt.consume();
//					selectedRow = TableMain.rowAtPoint(evt.getPoint());
//					notifyDoubleClick();
//				} else
//					TableMainMouseClicked(evt);
//			}
//		});
//		TableImport.addKeyListener(new java.awt.event.KeyAdapter() {
//			public void keyReleased(java.awt.event.KeyEvent evt) {
//				TableMainKeyReleased(evt);
//			}
//		});

		// -- Add the grid to a scroll panel
		// ------------------------------------
		jScrollPaneImport = new javax.swing.JScrollPane();
		jScrollPaneImport.setViewportView(TableImport);
		Utils.addComponent(paneGlobal, jScrollPaneImport, 
				0, 0, 
				GridBagConstraints.REMAINDER, 1, 
				1, 1, 
				10, 10, 10, 10,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);
		
		panelHelp = new JPanel();
		Utils.addComponent(paneGlobal, jScrollPaneImport, 
				0, 1, 
				GridBagConstraints.REMAINDER, 1, 
				1, 0, 
				10, 10, 10, 10,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);
		
		
		// == BUTTONS
		// ===========================================================
		jPanelButtons = new javax.swing.JPanel();
		jPanelButtons.setLayout(new FlowLayout());
		Utils.addComponent(paneGlobal, jPanelButtons, 
			0, 2, 
			GridBagConstraints.REMAINDER, 1, 
			1, 1, 
			0, 0, 0, 0,
			GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);

		btCancel = new javax.swing.JButton();
		btCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/cancel.png")));
		btCancel.setText(bundle.getString("Global.btCancel.text"));
		btCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setVisible(false);
			}
		});

		btOk = new javax.swing.JButton();
		btOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/valid.png")));
		btOk.setText(bundle.getString("Global.btOk.text"));
		btOk.setMinimumSize(btCancel.getMinimumSize());
		btOk.setPreferredSize(btCancel.getPreferredSize());
		btOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				RequestToClose();
			}
		});

		// -- Add buttons
		jPanelButtons.add(btOk);
		jPanelButtons.add(btCancel);

		// --
		pack();

		setLocationRelativeTo(null);
	}

	
}
