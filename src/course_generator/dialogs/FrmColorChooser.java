/*
 * Course Generator
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

package course_generator.dialogs;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import course_generator.utils.Utils;

public class FrmColorChooser extends javax.swing.JDialog {

    private javax.swing.JButton btCancel;
    private javax.swing.JButton btOk;
    private javax.swing.JColorChooser jColorChooser1;
    private boolean ok;
	private JPanel panelButtons;
	private ResourceBundle bundle;
    
    /**
     * Creates new form DialogColorChooser
     */
    public FrmColorChooser() {
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
        initComponents();
        setModal(true);
    }

    public static Color showDialog(String title, Color c) {
        FrmColorChooser dlg = new FrmColorChooser();
        if (title!=null)
            dlg.setTitle(title);
        dlg.jColorChooser1.setColor(c);
        dlg.ok=false;
        
        dlg.setVisible(true);
        if (dlg.ok) {
            return dlg.jColorChooser1.getColor(); 
        }
        else return c;
    }

    
     /**
     * Manage low level key strokes
     * ESCAPE : Close the window
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
        } ;

        Action actionListenerEnter = new AbstractAction() { 
            public void actionPerformed(ActionEvent actionEvent) { 
                RequestToClose();
            } 
        } ;

        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(strokeEscape, "ESCAPE");
        rootPane.getActionMap().put("ESCAPE", actionListener);

        inputMap.put(strokeEnter, "ENTER");
        rootPane.getActionMap().put("ENTER", actionListenerEnter);

        return rootPane;
    } 
    
    
    private void RequestToClose() {
        boolean param_valid=true;
        //check that the parameters are ok
        
        //
        if (param_valid){
            ok=true;
            setVisible(false);
        }
    }

    private void initComponents() {

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setResizable(false);
		setType(java.awt.Window.Type.UTILITY);
		addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                repaint();
            }
        });

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new GridBagLayout());

		
    	jColorChooser1 = new javax.swing.JColorChooser();
		Utils.addComponent(paneGlobal, jColorChooser1, 
				0, 0, 
				1, 1, 
				1, 1, 
				10, 10, 10, 10, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);


		// == BUTTONS
		// ===========================================================
		panelButtons = new javax.swing.JPanel();
		panelButtons.setLayout(new FlowLayout());
		Utils.addComponent(paneGlobal, panelButtons, 
				0, 1, 
				1, 1, 
				1, 0, 
				0, 0, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

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
		panelButtons.add(btOk);
		panelButtons.add(btCancel);

		// --
		pack();

		setLocationRelativeTo(null);
    }

}
