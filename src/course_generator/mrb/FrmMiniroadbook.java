/*
 * Course Generator - Miniroadbook form
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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

import javax.sound.midi.Track;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import course_generator.CgData;
import course_generator.TrackData;
import course_generator.settings.CgSettings;
import course_generator.utils.CgConst;
import course_generator.utils.CgSpinner;
import course_generator.utils.JTextFieldLimit;
import course_generator.utils.Utils;

public class FrmMiniroadbook extends javax.swing.JDialog implements FocusListener {

	private ResourceBundle bundle;
	private boolean ok;
	private CgSettings settings;
	private TrackData track;
	private MrbTableDataModel modelTableData;
	private int DupLine = -1; //Base line for duplication
	private int ConfigDuplication = CgConst.MRB_DUP_POS | CgConst.MRB_DUP_ALIGN | CgConst.MRB_DUP_FORMAT | CgConst.MRB_DUP_SIZE | CgConst.MRB_DUP_TAGS;
	private JButton btSaveAsImage;
	private JButton btConfig;
	private JButton btCopyFormat;
	private JButton btPasteFormat;
	private JButton btPasteFormatConfig;
	private JButton btLabelConnectedToBottom;
	private JButton btLabelConnectedToProfil;
	private JLabel lbProfilType;
	private JButton btMemory1;
	private JButton btMemory2;
	private JButton btMemory3;
	private JButton btMemory4;
	private JButton btMemory5;
	private JComboBox<String> cbProfilType;
	private JPanel pnlProperties;
	private JLabel lbSelect;
	private JCheckBox chkSelect;
	private JLabel lbPosition;
	private CgSpinner spinPosition;
	private JPanel pnlData;
	private JSplitPane SplitPane;
	private JPanel pnlTop;
	private JPanel pnlBottom;
	private JLabel lbAlignement;
	private JButton btAlignLeft;
	private JButton btAlignCenter;
	private JButton btAlignRight;
	private JLabel lbFormat;
	private JTextFieldLimit tfFormat;
	private JButton btFormat;
	private JLabel lbSize;
	private CgSpinner spinSize;
	private JLabel lbTags;
	private JCheckBox chkTags;
	private JLabel lbComment;
	private JTextFieldLimit tfComment;
	private JTable TableData;
	private JScrollPane jScrollPaneData;
	private PanelProfilMRB pnlProfil;
	private CgSpinner spinWidth;
	private CgSpinner spinHeight;
	private JLabel lbWidth;
	private JLabel lbHeight;
	private JScrollPane jScrollPaneProfil;
	private MrbDataList datalist;
	private JToolBar ToolBarMRB;
	private String[] memoFormat;


	/**
	 * Creates new form frmSettings
	 */
	public FrmMiniroadbook(CgSettings settings) {
		this.settings = settings;
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		datalist = new MrbDataList();
		modelTableData = new MrbTableDataModel(datalist, settings);
		memoFormat = new String[5];
		initComponents();
		setModal(true);
	}


	/**
	 * Show the dialog
	 * 
	 * @param settings
	 *            Object containing the settings
	 * @param track
	 *            Object containing the track
	 * @param start_line
	 *            Line number where to start
	 * @param end_line
	 *            Line number where to end
	 * @return Object containing the result
	 */
	public boolean showDialog(TrackData track) {
		this.track = track;

		DupLine=-1;
		
		// -- Set the content of the model
		datalist.data.clear();
		for (CgData r : track.data) {
			if ((r.getTag() & CgConst.TAG_ROADBOOK) != 0) {
				MrbData d = new MrbData(r.getNum(), r.getLatitude(), r.getLongitude(), r.getElevation(),
						r.getElevationMemo(), r.getTag(), r.getDist(), r.getTotal(), r.getDiff(), r.getCoeff(),
						r.getRecovery(), r.getSlope(), r.getSpeed(), r.getdElevation(), r.getTime(), r.getdTime_f(),
						r.getTimeLimit(), r.getHour(), r.getStation(), r.getName(), r.getComment(), 0, 0,
						r.FmtLbMiniRoadbook, r.OptionMiniRoadbook, r.VPosMiniRoadbook, r.CommentMiniRoadbook,
						r.FontSizeMiniRoadbook, 0, 0);
				datalist.data.add(d);
			}
		}

		// -- Calculate the fields
		if (datalist.data.size() >= 1) {
			for (int i = 1; i < datalist.data.size(); i++) {
				MrbData d = datalist.data.get(i);
				MrbData p = datalist.data.get(i - 1);
				d.setDeltaDist(d.getTotal() - p.getTotal());
				d.setDeltaTime(d.getTime() - p.getTime());
			}
		}

		// -- Set profil
		pnlProfil.setData(datalist);
		pnlProfil.setTrack(track);
		pnlProfil.setSettings(settings);
		pnlProfil.setWidth(track.MrbSizeW);
		pnlProfil.setHeight(track.MrbSizeH);

		// -- Set the position of the split bar
		SplitPane.setDividerLocation(settings.MRB_SplitPosition);

		// -- Set profil size
		spinWidth.setValue(track.MrbSizeW);
		spinHeight.setValue(track.MrbSizeH);

		//-- Set profile type
		cbProfilType.setSelectedIndex(track.MRBType);
		
		memoFormat[0] = settings.MemoFormat[0];
		memoFormat[1] = settings.MemoFormat[1];
		memoFormat[2] = settings.MemoFormat[2];
		memoFormat[3] = settings.MemoFormat[3];
		memoFormat[4] = settings.MemoFormat[4];

		RefreshBtLabel();
		RefreshTooltips();

		// End set field
		ok = false;

		// -- Update the display
		// Refresh();

		// -- Show the dialog
		setVisible(true);

		// Memorize the last position of the splitbar
		settings.MRB_SplitPosition = SplitPane.getDividerLocation();

		ok=true;
		
		if (ok) {
			settings.MemoFormat[0]=memoFormat[0];
			settings.MemoFormat[1]=memoFormat[1];
			settings.MemoFormat[2]=memoFormat[2];
			settings.MemoFormat[3]=memoFormat[3];
			settings.MemoFormat[4]=memoFormat[4];
			
			track.MRBType=cbProfilType.getSelectedIndex();
			
			track.MrbSizeW=spinWidth.getValueAsInt();
			track.MrbSizeH=spinHeight.getValueAsInt();
		}
		return ok;
	}


	/**
	 * Manage low level key strokes ESCAPE : Close the window
	 *
	 * @return
	 */
//	protected JRootPane createRootPane() {
//		JRootPane rootPane = new JRootPane();
//		KeyStroke strokeEscape = KeyStroke.getKeyStroke("ESCAPE");
//		KeyStroke strokeEnter = KeyStroke.getKeyStroke("ENTER");
//
//		Action actionListener = new AbstractAction() {
//			public void actionPerformed(ActionEvent actionEvent) {
//				setVisible(false);
//			}
//		};
//
//		Action actionListenerEnter = new AbstractAction() {
//			public void actionPerformed(ActionEvent actionEvent) {
//				RequestToClose();
//			}
//		};
//
//		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//		inputMap.put(strokeEscape, "ESCAPE");
//		rootPane.getActionMap().put("ESCAPE", actionListener);
//
//		inputMap.put(strokeEnter, "ENTER");
//		rootPane.getActionMap().put("ENTER", actionListenerEnter);
//
//		return rootPane;
//	}


	/**
	 * To call before a normal closing of he dialog
	 */
	private void RequestToClose() {
		boolean param_valid = true;
		// check that the parameters are ok

		// -- Ok?
		if (param_valid) {
			ok = true;
			setVisible(false);
		}
	}


	/**
	 * Create the toolbar
	 */
	private void Create_Toolbar() {
		int btw = 38;
		int bth = 24;

		ToolBarMRB = new javax.swing.JToolBar();
		ToolBarMRB.setFloatable(false);
		ToolBarMRB.setRollover(true);

		// -- Save as image
		btSaveAsImage = new javax.swing.JButton();
		btSaveAsImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/save.png")));
		btSaveAsImage.setToolTipText(bundle.getString("FrmMiniroadbook.btSaveAsImage.toolTipText"));
		btSaveAsImage.setPreferredSize(new Dimension(btw, bth));
		btSaveAsImage.setFocusable(false);
		btSaveAsImage.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SaveProfile();
			}
		});
		ToolBarMRB.add(btSaveAsImage);

		// -- Separator
		ToolBarMRB.add(new javax.swing.JToolBar.Separator());

		// -- Configuration
		btConfig = new javax.swing.JButton();
		btConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/settings.png")));
		btConfig.setToolTipText(bundle.getString("FrmMiniroadbook.btConfig.toolTipText"));
		btConfig.setPreferredSize(new Dimension(btw, bth));
		btConfig.setFocusable(false);
		btConfig.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				FrmConfigMrb frm = new FrmConfigMrb();
				frm.showDialog(track);
				
				track.isModified = true;
		        pnlProfil.setTrack(track);
				pnlProfil.Refresh();
			}
		});
		ToolBarMRB.add(btConfig);

		// -- Separator
		ToolBarMRB.add(new javax.swing.JToolBar.Separator());

		// -- Copy format
		btCopyFormat = new javax.swing.JButton();
		btCopyFormat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/pipette.png")));
		btCopyFormat.setToolTipText(bundle.getString("FrmMiniroadbook.btCopyFormat.toolTipText"));
		btCopyFormat.setPreferredSize(new Dimension(btw, bth));
		btCopyFormat.setFocusable(false);
		btCopyFormat.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				CopyFormat();
			}
		});
		ToolBarMRB.add(btCopyFormat);

		// -- Paste format
		btPasteFormat = new javax.swing.JButton();
		btPasteFormat
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/replicate.png")));
		btPasteFormat.setToolTipText(bundle.getString("FrmMiniroadbook.btPasteFormat.toolTipText"));
		btPasteFormat.setPreferredSize(new Dimension(btw, bth));
		btPasteFormat.setFocusable(false);
		btPasteFormat.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				PasteFormat();
			}
		});
		ToolBarMRB.add(btPasteFormat);

		// -- Config format duplication
		btPasteFormatConfig = new javax.swing.JButton();
		btPasteFormatConfig
				.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/replicate_config.png")));
		btPasteFormatConfig.setToolTipText(bundle.getString("FrmMiniroadbook.btPasteFormatConfig.toolTipText"));
		btPasteFormatConfig.setPreferredSize(new Dimension(btw, bth));
		btPasteFormatConfig.setFocusable(false);
		btPasteFormatConfig.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				FrmConfigMrbDuplicate frm = new FrmConfigMrbDuplicate();
				ConfigDuplication = frm.showDialog(ConfigDuplication);
			}
		});
		ToolBarMRB.add(btPasteFormatConfig);

		// -- Separator
		ToolBarMRB.add(new javax.swing.JToolBar.Separator());

		// -- Label connected to the bottom of the profil
		btLabelConnectedToBottom = new javax.swing.JButton();
		btLabelConnectedToBottom.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/label_to_bottom.png")));
		btLabelConnectedToBottom
				.setToolTipText(bundle.getString("FrmMiniroadbook.btLabelConnectedToBottom.toolTipText"));
		btLabelConnectedToBottom.setPreferredSize(new Dimension(btw, bth));
		btLabelConnectedToBottom.setFocusable(false);
		btLabelConnectedToBottom.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
			     track.LabelToBottom = true;
			     track.isModified = true;
			     RefreshBtLabel();
			     repaint();
			}
		});
		ToolBarMRB.add(btLabelConnectedToBottom);

		// -- Label connected to the top of the profil
		btLabelConnectedToProfil = new javax.swing.JButton();
		btLabelConnectedToProfil.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/label_to_profil.png")));
		btLabelConnectedToProfil
				.setToolTipText(bundle.getString("FrmMiniroadbook.btLabelConnectedToProfil.toolTipText"));
		btLabelConnectedToProfil.setPreferredSize(new Dimension(btw, bth));
		btLabelConnectedToProfil.setFocusable(false);
		btLabelConnectedToProfil.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
			     track.LabelToBottom = false;
			     track.isModified = true;
			     RefreshBtLabel();
			     repaint();
			}
		});
		ToolBarMRB.add(btLabelConnectedToProfil);

		// -- Separator
		ToolBarMRB.add(new javax.swing.JToolBar.Separator());

		// -- Label "Profil type"
		lbProfilType = new javax.swing.JLabel(" "+bundle.getString("FrmMiniroadbook.lbProfilType.Text")+" ");
		ToolBarMRB.add(lbProfilType);

		// -- "Profil type" selection
		cbProfilType = new javax.swing.JComboBox<String>();
		String profiltype[] = { bundle.getString("FrmMiniroadbook.ProfilSimple"),
				bundle.getString("FrmMiniroadbook.ProfilRoadTrack"), bundle.getString("FrmMiniroadbook.ProfilSlope") };
		cbProfilType.setModel(new javax.swing.DefaultComboBoxModel<String>(profiltype));
		cbProfilType.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				pnlProfil.setProfileType(cbProfilType.getSelectedIndex());
				track.isModified=true;
			}
		});
		ToolBarMRB.add(cbProfilType);

		// -- Memory 1
		btMemory1 = new javax.swing.JButton();
		btMemory1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/favoris1.png")));
		btMemory1.setPreferredSize(new Dimension(btw, bth));
		btMemory1.setFocusable(false);
		btMemory1.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				memoFormat[0]=ManageMemories(e, memoFormat[0]);
				RefreshTooltips();
			}
		});
		ToolBarMRB.add(btMemory1);

		// -- Memory 2
		btMemory2 = new javax.swing.JButton();
		btMemory2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/favoris2.png")));
		btMemory2.setPreferredSize(new Dimension(btw, bth));
		btMemory2.setFocusable(false);
		btMemory2.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				memoFormat[1]=ManageMemories(e, memoFormat[1]);
				RefreshTooltips();
			}
		});
		ToolBarMRB.add(btMemory2);

		// -- Memory 3
		btMemory3 = new javax.swing.JButton();
		btMemory3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/favoris3.png")));
		btMemory3.setPreferredSize(new Dimension(btw, bth));
		btMemory3.setFocusable(false);
		btMemory3.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				memoFormat[2]=ManageMemories(e, memoFormat[2]);
				RefreshTooltips();
			}
		});
		ToolBarMRB.add(btMemory3);

		// -- Memory 4
		btMemory4 = new javax.swing.JButton();
		btMemory4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/favoris4.png")));
		btMemory4.setPreferredSize(new Dimension(btw, bth));
		btMemory4.setFocusable(false);
		btMemory4.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				memoFormat[3]=ManageMemories(e, memoFormat[3]);
				RefreshTooltips();
			}
		});
		ToolBarMRB.add(btMemory4);

		// -- Memory 5
		btMemory5 = new javax.swing.JButton();
		btMemory5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/favoris5.png")));
		btMemory5.setPreferredSize(new Dimension(btw, bth));
		btMemory5.setFocusable(false);
		btMemory5.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				memoFormat[4]=ManageMemories(e, memoFormat[4]);
				RefreshTooltips();
			}
		});
		ToolBarMRB.add(btMemory5);

		// -- Separator
		ToolBarMRB.add(new javax.swing.JToolBar.Separator());

		// -- Width of the mini roadbook
		lbWidth = new javax.swing.JLabel(" "+bundle.getString("FrmMiniroadbook.lbWidth.Text")+" ");
		ToolBarMRB.add(lbWidth);

		spinWidth = new CgSpinner(1000, 0, 4000, 1);
		spinWidth.setToolTipText(bundle.getString("FrmMiniroadbook.spinWidth.TooltipText"));
		spinWidth.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				track.MrbSizeW = spinWidth.getValueAsInt();
				pnlProfil.setWidth(track.MrbSizeW);
				track.isModified=true;
			}
		});
		ToolBarMRB.add(spinWidth);

		// -- Height of the mini roadbook
		lbHeight = new javax.swing.JLabel(" "+bundle.getString("FrmMiniroadbook.lbHeight.Text")+" ");
		ToolBarMRB.add(lbHeight);

		spinHeight = new CgSpinner(500, 0, 2000, 1);
		spinHeight.setToolTipText(bundle.getString("FrmMiniroadbook.spinHeight.TooltipText"));
		spinHeight.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				track.MrbSizeH = spinHeight.getValueAsInt();
				pnlProfil.setHeight(track.MrbSizeH);
				track.isModified=true;
			}
		});
		ToolBarMRB.add(spinHeight);
		
		RefreshTooltips();
	}




	protected void CopyFormat() {
		if (datalist.data.isEmpty()) return;
		int row = TableData.getSelectedRow();
		if (row<0) return;
	    DupLine=(int) datalist.data.get(row).getNum()-1;
	}

	
	protected void PasteFormat() {
		if (datalist.data.isEmpty()) return;
		int row = TableData.getSelectedRow();
		if (row<0) return;
	    int line = (int) datalist.data.get(row).getNum()-1;
	    if (line>track.data.size()) return;

	    track.isModified=true;
	    if ((ConfigDuplication & CgConst.MRB_DUP_POS) != 0) {
	    	track.data.get(line).VPosMiniRoadbook = track.data.get(DupLine).VPosMiniRoadbook;
	    	datalist.data.get(row).VPosMiniRoadbook = track.data.get(DupLine).VPosMiniRoadbook;	    	
	    }
	    
	    if ((ConfigDuplication & CgConst.MRB_DUP_FORMAT) != 0) {
	    	track.data.get(line).FmtLbMiniRoadbook = track.data.get(DupLine).FmtLbMiniRoadbook;
	    	datalist.data.get(row).FmtLbMiniRoadbook = track.data.get(DupLine).FmtLbMiniRoadbook;	    	
	    }
	    
	    if ((ConfigDuplication & CgConst.MRB_DUP_ALIGN) != 0) {
	    	int src=track.data.get(DupLine).OptionMiniRoadbook;
	    	int dst=track.data.get(line).OptionMiniRoadbook;
	    	
	    	if ((src & CgConst.MRBOPT_LEFT) !=0)
	    		dst=Utils.Set(dst, CgConst.MRBOPT_LEFT);
	    	else
	    		dst=Utils.Reset(dst, CgConst.MRBOPT_LEFT);
	    	
	    	if ((src & CgConst.MRBOPT_CENTER) !=0)
	    		dst=Utils.Set(dst, CgConst.MRBOPT_CENTER);
	    	else
	    		dst=Utils.Reset(dst, CgConst.MRBOPT_CENTER);
	    	
	    	if ((src & CgConst.MRBOPT_RIGHT) !=0)
	    		dst=Utils.Set(dst, CgConst.MRBOPT_RIGHT);
	    	else
	    		dst=Utils.Reset(dst, CgConst.MRBOPT_RIGHT);
	    	
	    	track.data.get(line).OptionMiniRoadbook = dst;
	    	datalist.data.get(row).OptionMiniRoadbook = dst;	    	
	    }
	    
	    if ((ConfigDuplication & CgConst.MRB_DUP_SIZE) != 0) {
	    	track.data.get(line).FontSizeMiniRoadbook = track.data.get(DupLine).FontSizeMiniRoadbook;
	    	datalist.data.get(row).FontSizeMiniRoadbook = track.data.get(DupLine).FontSizeMiniRoadbook;	    	
	    }
	    
	    if ((ConfigDuplication & CgConst.MRB_DUP_TAGS) != 0) {
	    	int src=track.data.get(DupLine).OptionMiniRoadbook;
	    	int dst=track.data.get(line).OptionMiniRoadbook;
	    	
	    	if ((src & CgConst.MRBOPT_SHOWTAGS) !=0)
	    		dst=Utils.Set(dst, CgConst.MRBOPT_SHOWTAGS);
	    	else
	    		dst=Utils.Reset(dst, CgConst.MRBOPT_SHOWTAGS);
	    	
	    	track.data.get(line).OptionMiniRoadbook = dst;
	    	datalist.data.get(row).OptionMiniRoadbook = dst;	    	
	    }

	    RefreshProperties();
	    RefreshTableData();
	    pnlProfil.Refresh();
	}

	protected String ManageMemories(MouseEvent e, String memo) {
		if (datalist.data.isEmpty()) return memo;
		int row = TableData.getSelectedRow();
		if (row<0) return memo;
		
		//-- Left click
	    if (e.getButton() == MouseEvent.BUTTON1) {
	    	int line=(int)datalist.data.get(row).getNum()-1;
	        if (line>track.data.size()) return memo;
	        
	        String txt=memo;
	        tfFormat.setText(txt);
	        track.data.get(line).FmtLbMiniRoadbook = txt;
	        datalist.data.get(row).FmtLbMiniRoadbook = txt;

	        track.isModified = true;
	        RefreshTableData();
	        pnlProfil.Refresh();
	    }
	    //-- Right click
	    else if (e.getButton() == MouseEvent.BUTTON3) {
	    	memo=tfFormat.getText();
	    	track.isModified = true;
	    	//RefreshTooltips();
	    }
	    return memo;
	}


	protected void RefreshTooltips() {
		btMemory1.setToolTipText(String.format(bundle.getString("FrmMiniroadbook.btMemory1.toolTipText"), memoFormat[0]));
		btMemory2.setToolTipText(String.format(bundle.getString("FrmMiniroadbook.btMemory2.toolTipText"), memoFormat[1]));
		btMemory3.setToolTipText(String.format(bundle.getString("FrmMiniroadbook.btMemory3.toolTipText"), memoFormat[2]));
		btMemory4.setToolTipText(String.format(bundle.getString("FrmMiniroadbook.btMemory4.toolTipText"), memoFormat[3]));
		btMemory5.setToolTipText(String.format(bundle.getString("FrmMiniroadbook.btMemory5.toolTipText"), memoFormat[4]));
	}


	protected void RefreshBtLabel() {
		btLabelConnectedToBottom.setSelected(track.LabelToBottom);
		btLabelConnectedToProfil.setSelected(!track.LabelToBottom);
	}


	protected void SaveProfile() {
		String s, ext;

		if (track.data.isEmpty())
			return;

		s = Utils.SaveDialog(this, settings.LastDir, "", ".png", bundle.getString("FrmMiniroadbook.PNGFile"), true,
				bundle.getString("FrmMiniroadbook.FileExist"));

		if (!s.isEmpty()) {
			pnlProfil.save(s);
		}
	}


	private void initComponents() {
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(bundle.getString("FrmMiniroadbook.title"));
		setAlwaysOnTop(true);
		setPreferredSize(new Dimension(1000, 700));
		setType(java.awt.Window.Type.NORMAL);

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new BorderLayout());

		// -- Toolbar
		Create_Toolbar();
		paneGlobal.add(ToolBarMRB, BorderLayout.NORTH);

		// -- Split bar (vertical)
		// -----------------------------------------
		SplitPane = new javax.swing.JSplitPane();
		SplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
		paneGlobal.add(SplitPane, BorderLayout.CENTER);

		// -- Top panel
		pnlTop = new JPanel();
		pnlTop.setLayout(new GridBagLayout());
		SplitPane.setTopComponent(pnlTop);

		// -- Bottom panel
		pnlBottom = new JPanel();
		pnlBottom.setLayout(new GridBagLayout());
		SplitPane.setBottomComponent(pnlBottom);

		
		
		// == Panel properties
		pnlProperties = new JPanel();
		pnlProperties.setLayout(new GridBagLayout());
		Utils.addComponent(pnlTop, pnlProperties, 
				0, 0, 
				1, 1, 
				0, 1, 
				5, 10, 0, 5, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.BOTH);

		// -- Selection
		lbSelect = new javax.swing.JLabel(bundle.getString("FrmMiniroadbook.lbSelect.Text"));
		lbSelect.setMinimumSize(new Dimension(100,25));
		Utils.addComponent(pnlProperties, lbSelect, 
				0, 0, 
				1, 1, 
				0, 0, 
				10, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.BOTH);

		chkSelect = new javax.swing.JCheckBox();
		chkSelect.setMinimumSize(new Dimension(100,25));
		chkSelect.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (datalist.data.isEmpty()) return;
		    		int row = TableData.getSelectedRow();
		    		if (row<0) return;
		    		
			        int line=(int)datalist.data.get(row).getNum()-1;
			        if (line>track.data.size()) return;

			        if (chkSelect.isSelected()) {
			        	track.data.get(line).OptionMiniRoadbook = Utils.Set(track.data.get(line).OptionMiniRoadbook, CgConst.MRBOPT_SEL);
			        	datalist.data.get(row).OptionMiniRoadbook = Utils.Set(datalist.data.get(row).OptionMiniRoadbook, CgConst.MRBOPT_SEL);
			        }
			        else {
			        	track.data.get(line).OptionMiniRoadbook = Utils.Reset(track.data.get(line).OptionMiniRoadbook, CgConst.MRBOPT_SEL);
			        	datalist.data.get(row).OptionMiniRoadbook = Utils.Reset(datalist.data.get(row).OptionMiniRoadbook, CgConst.MRBOPT_SEL);
			        }
			        track.isModified = true;
			        pnlProfil.Refresh();
			}
		});
		Utils.addComponent(pnlProperties, chkSelect, 
				1, 0, 
				GridBagConstraints.REMAINDER, 1, 
				1, 0, 
				10, 5, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		// -- Position
		lbPosition = new javax.swing.JLabel(bundle.getString("FrmMiniroadbook.lbPosition.Text"));
		lbPosition.setPreferredSize(new Dimension(100,25));
		Utils.addComponent(pnlProperties, lbPosition, 
				0, 1, 
				1, 1, 
				0, 0, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.BOTH);

		spinPosition = new CgSpinner(0, 0, 1000, 1);
		spinPosition.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (datalist.data.isEmpty()) return;
	    		int row = TableData.getSelectedRow();
	    		if (row<0) return;
	    		
		        int line=(int)datalist.data.get(row).getNum()-1;
		        if (line>track.data.size()) return;

		        track.data.get(line).VPosMiniRoadbook = spinPosition.getValueAsInt();
		        datalist.data.get(row).VPosMiniRoadbook = spinPosition.getValueAsInt();
				track.isModified=true;
				
				pnlProfil.Refresh();
			}
		});
		Utils.addComponent(pnlProperties, spinPosition, 
				1, 1, 
				GridBagConstraints.REMAINDER, 1, 
				0, 0, 
				0, 5, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		// -- Alignement
		lbAlignement = new javax.swing.JLabel(bundle.getString("FrmMiniroadbook.lbAlignement.Text"));
		lbAlignement.setPreferredSize(new Dimension(100,25));
		Utils.addComponent(pnlProperties, lbAlignement, 
				0, 2, 
				1, 1, 
				0, 0, 
				0, 0, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		btAlignLeft = new javax.swing.JButton();
		btAlignLeft = new javax.swing.JButton("|<");
		btAlignLeft.setToolTipText(bundle.getString("FrmMiniroadbook.btAlignLeft.toolTipText"));
		btAlignLeft.setFocusable(false);
		btAlignLeft.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (datalist.data.isEmpty()) return;
		    		int row = TableData.getSelectedRow();
		    		if (row<0) return;
			        int line=(int)datalist.data.get(row).getNum()-1;
			        
			        track.data.get(line).OptionMiniRoadbook = Utils.Set(track.data.get(line).OptionMiniRoadbook, CgConst.MRBOPT_LEFT);
			        track.data.get(line).OptionMiniRoadbook = Utils.Reset(track.data.get(line).OptionMiniRoadbook, CgConst.MRBOPT_CENTER);
			        track.data.get(line).OptionMiniRoadbook = Utils.Reset(track.data.get(line).OptionMiniRoadbook, CgConst.MRBOPT_RIGHT);

			        datalist.data.get(row).OptionMiniRoadbook = Utils.Set(datalist.data.get(row).OptionMiniRoadbook, CgConst.MRBOPT_LEFT);
			        datalist.data.get(row).OptionMiniRoadbook = Utils.Reset(datalist.data.get(row).OptionMiniRoadbook, CgConst.MRBOPT_CENTER);
			        datalist.data.get(row).OptionMiniRoadbook = Utils.Reset(datalist.data.get(row).OptionMiniRoadbook, CgConst.MRBOPT_RIGHT);
			        
			        RefreshAligmentBt(row);
			        track.isModified = true;
			        pnlProfil.Refresh();
			}
		});
		Utils.addComponent(pnlProperties, btAlignLeft, 
				1, 2, 
				1, 1, 
				0.33, 0, 
				0, 5, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		btAlignCenter = new javax.swing.JButton();
		btAlignCenter = new javax.swing.JButton("|");
		btAlignCenter.setToolTipText(bundle.getString("FrmMiniroadbook.btAlignCenter.toolTipText"));
		btAlignCenter.setFocusable(false);
		btAlignCenter.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (datalist.data.isEmpty()) return;
	    		int row = TableData.getSelectedRow();
	    		if (row<0) return;
		        int line=(int)datalist.data.get(row).getNum()-1;
		        
		        track.data.get(line).OptionMiniRoadbook = Utils.Reset(track.data.get(line).OptionMiniRoadbook, CgConst.MRBOPT_LEFT);
		        track.data.get(line).OptionMiniRoadbook = Utils.Set(track.data.get(line).OptionMiniRoadbook, CgConst.MRBOPT_CENTER);
		        track.data.get(line).OptionMiniRoadbook = Utils.Reset(track.data.get(line).OptionMiniRoadbook, CgConst.MRBOPT_RIGHT);

		        datalist.data.get(row).OptionMiniRoadbook = Utils.Reset(datalist.data.get(row).OptionMiniRoadbook, CgConst.MRBOPT_LEFT);
		        datalist.data.get(row).OptionMiniRoadbook = Utils.Set(datalist.data.get(row).OptionMiniRoadbook, CgConst.MRBOPT_CENTER);
		        datalist.data.get(row).OptionMiniRoadbook = Utils.Reset(datalist.data.get(row).OptionMiniRoadbook, CgConst.MRBOPT_RIGHT);
		        
		        RefreshAligmentBt(row);
		        track.isModified = true;
		        pnlProfil.Refresh();
				}
		});
		Utils.addComponent(pnlProperties, btAlignCenter, 
				2, 2, 
				1, 1, 
				0.33, 0, 
				0, 0, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		btAlignRight = new javax.swing.JButton();
		btAlignRight = new javax.swing.JButton(">|");
		btAlignRight.setToolTipText(bundle.getString("FrmMiniroadbook.btAlignRight.toolTipText"));
		btAlignRight.setFocusable(false);
		btAlignRight.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (datalist.data.isEmpty()) return;
	    		int row = TableData.getSelectedRow();
	    		if (row<0) return;
		        int line=(int)datalist.data.get(row).getNum()-1;
		        
		        track.data.get(line).OptionMiniRoadbook = Utils.Reset(track.data.get(line).OptionMiniRoadbook, CgConst.MRBOPT_LEFT);
		        track.data.get(line).OptionMiniRoadbook = Utils.Reset(track.data.get(line).OptionMiniRoadbook, CgConst.MRBOPT_CENTER);
		        track.data.get(line).OptionMiniRoadbook = Utils.Set(track.data.get(line).OptionMiniRoadbook, CgConst.MRBOPT_RIGHT);

		        datalist.data.get(row).OptionMiniRoadbook = Utils.Reset(datalist.data.get(row).OptionMiniRoadbook, CgConst.MRBOPT_LEFT);
		        datalist.data.get(row).OptionMiniRoadbook = Utils.Reset(datalist.data.get(row).OptionMiniRoadbook, CgConst.MRBOPT_CENTER);
		        datalist.data.get(row).OptionMiniRoadbook = Utils.Set(datalist.data.get(row).OptionMiniRoadbook, CgConst.MRBOPT_RIGHT);
		        
		        RefreshAligmentBt(row);
		        track.isModified = true;
		        pnlProfil.Refresh();
			}
		});
		Utils.addComponent(pnlProperties, btAlignRight, 
				3, 2, 
				1, 1, 
				0.33, 0, 
				0, 0, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		
		// -- Format
		lbFormat = new javax.swing.JLabel(bundle.getString("FrmMiniroadbook.lbFormat.Text"));
		lbFormat.setPreferredSize(new Dimension(100,25));
		Utils.addComponent(pnlProperties, lbFormat, 
				0, 3, 
				1, 1, 
				0, 0, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.BOTH);

		tfFormat = new JTextFieldLimit(200);
		tfFormat.addFocusListener(this);
		tfFormat.setToolTipText(bundle.getString("FrmMiniroadbook.tfFormat.toolTipText"));
		tfFormat.setPreferredSize(new Dimension(100,25));
		Utils.addComponent(pnlProperties, tfFormat, 
				1, 3, 
				2, 1, 
				1, 0, 
				0, 5, 0, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.BOTH);

		btFormat = new javax.swing.JButton("...");
		btFormat.setPreferredSize(new Dimension(100,25));
		btFormat.setToolTipText(bundle.getString("FrmMiniroadbook.btFormat.toolTipText"));
		btFormat.setFocusable(false);
		btFormat.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
								
				if (datalist.data.isEmpty()) return;
	    		int row = TableData.getSelectedRow();
	    		if (row<0) return;
		        int line=(int)datalist.data.get(row).getNum()-1;
		        
		        FrmEditMrbFormat frm=new FrmEditMrbFormat(settings);
		        tfFormat.setText(frm.showDialog(track.data.get(line), track, tfFormat.getText()));

		        track.data.get(line).FmtLbMiniRoadbook = tfFormat.getText();
		        datalist.data.get(row).FmtLbMiniRoadbook = tfFormat.getText();

		        track.isModified = true;
		        pnlProfil.Refresh();
			}
		});
		Utils.addComponent(pnlProperties, btFormat, 
				3, 3, 
				1, 1, 
				0, 0, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.BOTH);

		// -- Size
		lbSize = new javax.swing.JLabel(bundle.getString("FrmMiniroadbook.lbSize.Text"));
		lbSize.setPreferredSize(new Dimension(100,25));
		Utils.addComponent(pnlProperties, lbSize, 
				0, 4, 
				1, 1, 
				0, 0, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.BOTH);

		spinSize = new CgSpinner(10, 7, 72, 1);
		spinSize.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (datalist.data.isEmpty()) return;
	    		int row = TableData.getSelectedRow();
	    		if (row<0) return;
		        int line=(int)datalist.data.get(row).getNum()-1;
		        
		        track.data.get(line).FontSizeMiniRoadbook = spinSize.getValueAsInt();
		        datalist.data.get(row).FontSizeMiniRoadbook = spinSize.getValueAsInt();

		        track.isModified = true;
		        pnlProfil.Refresh();
			}
		});
		Utils.addComponent(pnlProperties, spinSize, 
				1, 4, 
				GridBagConstraints.REMAINDER, 1, 
				1, 0, 
				0, 5, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		// -- Tags
		lbTags = new javax.swing.JLabel(bundle.getString("FrmMiniroadbook.Tags.Text"));
		lbTags.setMinimumSize(new Dimension(100,25));
		Utils.addComponent(pnlProperties, lbTags, 
				0, 5, 
				1, 1, 
				0, 0, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.BOTH);

		chkTags = new javax.swing.JCheckBox();
		chkTags.setMinimumSize(new Dimension(100,25));
		chkTags.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (datalist.data.isEmpty()) return;
		    		int row = TableData.getSelectedRow();
		    		if (row<0) return;
		    		
			        int line=(int)datalist.data.get(row).getNum()-1;
			        if (line>track.data.size()) return;

			        if (chkTags.isSelected()) {
			        	track.data.get(line).OptionMiniRoadbook = Utils.Set(track.data.get(line).OptionMiniRoadbook, CgConst.MRBOPT_SHOWTAGS);
			        	datalist.data.get(row).OptionMiniRoadbook = Utils.Set(datalist.data.get(row).OptionMiniRoadbook, CgConst.MRBOPT_SHOWTAGS);
			        }
			        else {
			        	track.data.get(line).OptionMiniRoadbook = Utils.Reset(track.data.get(line).OptionMiniRoadbook, CgConst.MRBOPT_SHOWTAGS);
			        	datalist.data.get(row).OptionMiniRoadbook = Utils.Reset(datalist.data.get(row).OptionMiniRoadbook, CgConst.MRBOPT_SHOWTAGS);
			        }
			        track.isModified = true;
			        pnlProfil.Refresh();
			}
		});

		Utils.addComponent(pnlProperties, chkTags, 
				1, 5, 
				GridBagConstraints.REMAINDER, 1, 
				1, 0, 
				0, 5, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		// -- Comment
		lbComment = new javax.swing.JLabel(bundle.getString("FrmMiniroadbook.lbComment.Text"));
		lbComment.setMinimumSize(new Dimension(100,25));
		Utils.addComponent(pnlProperties, lbComment, 
				0, 6, 
				1, 1, 
				0, 1, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		tfComment = new JTextFieldLimit(200);
		tfComment.addFocusListener(this);
		Utils.addComponent(pnlProperties, tfComment, 
				1, 6, 
				GridBagConstraints.REMAINDER, 1, 
				1, 1, 
				0, 5, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Panel data
		TableData = new javax.swing.JTable();
		TableData.setModel(modelTableData);// new ParamPointsModel(param));
		TableData.getTableHeader().setReorderingAllowed(false);
		TableData.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				RefreshProperties();
				pnlProfil.setSelLine(TableData.getSelectedRow());
			}
		});
		TableData.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				RefreshProperties();
				pnlProfil.setSelLine(TableData.getSelectedRow());
			}
		});

		jScrollPaneData = new javax.swing.JScrollPane();
		jScrollPaneData.setViewportView(TableData);
		Utils.addComponent(pnlTop, jScrollPaneData, 1, 0, 1, 1, 1, 1, 10, 0, 0, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.BOTH);

		pnlProfil = new PanelProfilMRB(640,480);

		jScrollPaneProfil = new javax.swing.JScrollPane();
		jScrollPaneProfil.setViewportView(pnlProfil);

		Utils.addComponent(pnlBottom, jScrollPaneProfil, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		pack();

		setLocationRelativeTo(null);
	}


	protected void RefreshAligmentBt(int line) {
		int v=datalist.data.get(line).OptionMiniRoadbook;
		btAlignLeft.setSelected(((v & CgConst.MRBOPT_LEFT)!=0));
		btAlignCenter.setSelected(((v & CgConst.MRBOPT_CENTER)!=0));
        btAlignRight.setSelected(((v & CgConst.MRBOPT_RIGHT)!=0));
	}


	protected void RefreshProperties() {
		if (datalist.data.isEmpty()) return;

		int row = TableData.getSelectedRow();
		if (row>=0) {
			chkSelect.setSelected((datalist.data.get(row).OptionMiniRoadbook & CgConst.MRBOPT_SEL) != 0);
			chkSelect.setEnabled(true);

			spinPosition.setValue(datalist.data.get(row).VPosMiniRoadbook);
			spinPosition.setEnabled(true);

			btAlignLeft.setSelected((datalist.data.get(row).OptionMiniRoadbook & CgConst.MRBOPT_LEFT) != 0);
			btAlignLeft.setEnabled(true);

			btAlignCenter.setSelected((datalist.data.get(row).OptionMiniRoadbook & CgConst.MRBOPT_CENTER) != 0);
			btAlignCenter.setEnabled(true);

			btAlignRight.setSelected((datalist.data.get(row).OptionMiniRoadbook & CgConst.MRBOPT_RIGHT) != 0);
			btAlignRight.setEnabled(true);

			tfFormat.setText(datalist.data.get(row).FmtLbMiniRoadbook);
			tfFormat.setEnabled(true);

			spinSize.setValue(datalist.data.get(row).FontSizeMiniRoadbook);
			spinSize.setEnabled(true);

			chkTags.setSelected((datalist.data.get(row).OptionMiniRoadbook & CgConst.MRBOPT_SHOWTAGS) != 0);
			chkTags.setEnabled(true);

			tfComment.setText(datalist.data.get(row).CommentMiniRoadbook);
			tfComment.setEnabled(true);
		}
		else {
			chkSelect.setEnabled(false);
			spinPosition.setEnabled(false);
			btAlignLeft.setEnabled(false);
			btAlignCenter.setEnabled(false);
			btAlignRight.setEnabled(false); 
			tfFormat.setEnabled(false);
			spinSize.setEnabled(false);
			chkTags.setEnabled(false);
			tfComment.setEnabled(false);			
		}
		
		
	}


	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void focusLost(FocusEvent fe) {

		//-- We exit from the format field i
		if (fe.getSource()==tfFormat) {
			if (datalist.data.isEmpty()) return;

			int row = TableData.getSelectedRow();
			if (row>=0) {
		        int line=(int)datalist.data.get(row).getNum()-1;
		        if (line>track.data.size()) return;
		        
		        String txt=tfFormat.getText();
		        track.data.get(line).FmtLbMiniRoadbook = txt;
		        datalist.data.get(row).FmtLbMiniRoadbook = txt;

		        track.isModified = true;
		        RefreshTableData();
		        pnlProfil.Refresh();
			}
		}
		
		//-- We exit from the comment field 
		if (fe.getSource()==tfComment) {
			if (datalist.data.isEmpty()) return;

			int row = TableData.getSelectedRow();
			if (row>=0) {
		        int line=(int)datalist.data.get(row).getNum()-1;
		        if (line>track.data.size()) return;
		        
		        track.data.get(line).CommentMiniRoadbook = tfComment.getText();
		        datalist.data.get(row).CommentMiniRoadbook = tfComment.getText();

		        track.isModified = true;
		        RefreshTableData();
		        pnlProfil.Refresh();
			}
		}		
	}

	private void RefreshTableData() {
		int r=TableData.getSelectedRow();
		modelTableData.fireTableDataChanged();
		if (r>=0)
			TableData.setRowSelectionInterval(r, r);
	}

}
