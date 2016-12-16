package course_generator.mrb;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import course_generator.CgData;
import course_generator.TrackData;
import course_generator.settings.CgSettings;
import course_generator.utils.CgConst;
import course_generator.utils.CgSpinner;
import course_generator.utils.JTextFieldLimit;
import course_generator.utils.Utils;

public class FrmMiniroadbook  extends javax.swing.JDialog {

	private ResourceBundle bundle;
	private boolean ok;
	private CgSettings settings;
	private TrackData track;
	private MrbTableDataModel model;
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
	private JPanel pnlToolbar;
	private JScrollPane jScrollPaneProfil;
	private MrbDataList datalist;

	
	/**
	 * Creates new form frmSettings
	 */
	public FrmMiniroadbook(CgSettings settings) {
		this.settings = settings;
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		datalist = new MrbDataList();
		model = new MrbTableDataModel(datalist, settings);
		initComponents();
		setModal(true);
	}

	/**
	 * Show the dialog
	 * @param settings
	 * 	Object containing the settings
	 * @param track
	 * 	Object containing the track
	 * @param start_line
	 * 	Line number where to start   
	 * @param end_line
	 *  Line number where to end
	 * @return
	 * 	Object containing the result 
	 */
	public boolean showDialog(TrackData track) {
		this.track = track;
		
		//-- Set the content of the model
		datalist.data.clear();
		for (CgData r : track.data) {
			if ((r.getTag() & CgConst.TAG_ROADBOOK)!=0) {
				MrbData d=new MrbData(
						r.getNum(),
						r.getLatitude(), r.getLongitude(),
						r.getElevation(), r.getElevationMemo(),
						r.getTag(),
						r.getDist(), r.getTotal(),
						r.getDiff(), r.getCoeff(), 
						r.getRecovery(), 
						r.getSlope(), r.getSpeed(),
						r.getdElevation(), r.getTime(),
						r.getdTime_f(),
						r.getTimeLimit(),
						r.getHour(),
						r.getStation(),
						r.getName(), r.getComment(),
						0,0,
						r.FmtLbMiniRoadbook,
						r.OptionMiniRoadbook, r.VPosMiniRoadbook, 
						r.CommentMiniRoadbook, r.FontSizeMiniRoadbook,
						0, 0	
						);
				datalist.data.add(d);
			}
		}
		
		//-- Calculate the fields
		if (datalist.data.size()>=1) {
			for (int i=1; i<datalist.data.size(); i++) {
				MrbData d = datalist.data.get(i);
				MrbData p = datalist.data.get(i-1);
				d.setDeltaDist(d.getTotal()-p.getTotal());
				d.setDeltaTime(d.getTime()-p.getTime());
			}
		}
		
		//-- Set profil
		pnlProfil.setData(datalist);
		pnlProfil.setTrack(track);
		
		//-- Set the position of the split bar
		SplitPane.setDividerLocation(settings.MRB_SplitPosition);
		
		//-- Set profil size
		spinWidth.setValue(track.MrbSizeW);
		spinHeight.setValue(track.MrbSizeH);
		//TODO set the size of the profil
		
		
//		spinFromLine.setValue(start);
//		spinFromLine.setMaximum(track.data.size());
//		spinToLine.setValue(end);
//		spinToLine.setMaximum(track.data.size());
		
		// End set field
		ok = false;

		//-- Update the display
		//Refresh();
		
		//-- Show the dialog
		setVisible(true);

		//Memorize the last position of the splitbar
		settings.MRB_SplitPosition=SplitPane.getDividerLocation();
		
		if (ok) {
			
		}
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
		int col=0;
		int btw=38;
		int bth=24;
		int ds=2; //Space under the compoents
		
		pnlToolbar = new JPanel();
		pnlToolbar.setLayout(new GridBagLayout());
		
		//-- Save as image
		btSaveAsImage = new javax.swing.JButton();
		btSaveAsImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/save.png")));
		btSaveAsImage.setToolTipText(bundle.getString("FrmMiniroadbook.btSaveAsImage.toolTipText"));
		btSaveAsImage.setPreferredSize(new Dimension(btw, bth));
		btSaveAsImage.setFocusable(false);
		btSaveAsImage.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Save as image
			}
		});
		Utils.addComponent(pnlToolbar, btSaveAsImage, 
		col++, 0, 
		1, 1, 
		0, 0, 
		0, 0, ds, 0, 
		GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		//-- Configuration
		btConfig = new javax.swing.JButton();
		btConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/settings.png")));
		btConfig.setToolTipText(bundle.getString("FrmMiniroadbook.btConfig.toolTipText"));
		btConfig.setPreferredSize(new Dimension(btw, bth));
		btConfig.setFocusable(false);
		btConfig.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		Utils.addComponent(pnlToolbar, btConfig, 
		col++, 0, 
		1, 1, 
		0, 0, 
		0, 5, ds, 0, 
		GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		//-- Copy format
		btCopyFormat = new javax.swing.JButton();
		btCopyFormat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/pipette.png")));
		btCopyFormat.setToolTipText(bundle.getString("FrmMiniroadbook.btCopyFormat.toolTipText"));
		btCopyFormat.setPreferredSize(new Dimension(btw, bth));
		btCopyFormat.setFocusable(false);
		btCopyFormat.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		Utils.addComponent(pnlToolbar, btCopyFormat, 
		col++, 0, 
		1, 1, 
		0, 0, 
		0, 5, ds, 0, 
		GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		//-- Paste format
		btPasteFormat = new javax.swing.JButton();
		btPasteFormat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/replicate.png")));
		btPasteFormat.setToolTipText(bundle.getString("FrmMiniroadbook.btPasteFormat.toolTipText"));
		btPasteFormat.setPreferredSize(new Dimension(btw, bth));
		btPasteFormat.setFocusable(false);
		btPasteFormat.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		Utils.addComponent(pnlToolbar, btPasteFormat, 
		col++, 0, 
		1, 1, 
		0, 0, 
		0, 0, ds, 0, 
		GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		//-- Config format duplication
		btPasteFormatConfig = new javax.swing.JButton();
		btPasteFormatConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/replicate.png")));
		btPasteFormatConfig.setToolTipText(bundle.getString("FrmMiniroadbook.btPasteFormatConfig.toolTipText"));
		btPasteFormatConfig.setPreferredSize(new Dimension(btw, bth));
		btPasteFormatConfig.setFocusable(false);
		btPasteFormatConfig.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		Utils.addComponent(pnlToolbar, btPasteFormatConfig, 
		col++, 0, 
		1, 1, 
		0, 0, 
		0, 0, ds, 0, 
		GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		//-- Label connected to the bottom of the profil
		btLabelConnectedToBottom = new javax.swing.JButton();
		btLabelConnectedToBottom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/label_to_bottom.png")));
		btLabelConnectedToBottom.setToolTipText(bundle.getString("FrmMiniroadbook.btLabelConnectedToBottom.toolTipText"));
		btLabelConnectedToBottom.setPreferredSize(new Dimension(btw, bth));
		btLabelConnectedToBottom.setFocusable(false);
		btLabelConnectedToBottom.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		Utils.addComponent(pnlToolbar, btLabelConnectedToBottom, 
		col++, 0, 
		1, 1, 
		0, 0, 
		0, 5, ds, 0, 
		GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		//-- Label connected to the top of the profil
		btLabelConnectedToProfil = new javax.swing.JButton();
		btLabelConnectedToProfil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/label_to_profil.png")));
		btLabelConnectedToProfil.setToolTipText(bundle.getString("FrmMiniroadbook.btLabelConnectedToProfil.toolTipText"));
		btLabelConnectedToProfil.setPreferredSize(new Dimension(btw, bth));
		btLabelConnectedToProfil.setFocusable(false);
		btLabelConnectedToProfil.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		Utils.addComponent(pnlToolbar, btLabelConnectedToProfil, 
		col++, 0, 
		1, 1, 
		0, 0, 
		0, 0, ds, 0, 
		GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		//-- Label "Profil type"
		lbProfilType = new javax.swing.JLabel(bundle.getString("FrmMiniroadbook.lbProfilType.Text"));
		Utils.addComponent(pnlToolbar, lbProfilType, 
		col++, 0, 
		1, 1, 
		0, 0, 
		0, 5, ds, 0, 
		GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		//-- "Profil type" selection
		cbProfilType = new javax.swing.JComboBox<String>();
		String profiltype[] = { 
				bundle.getString("FrmMiniroadbook.ProfilSimple"),
				bundle.getString("FrmMiniroadbook.ProfilRoadTrack"), 
				bundle.getString("FrmMiniroadbook.ProfilSlope") };
		cbProfilType.setModel(new javax.swing.DefaultComboBoxModel<String>(profiltype));
		cbProfilType.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				pnlProfil.setProfileType(cbProfilType.getSelectedIndex());
			}
		});
		Utils.addComponent(pnlToolbar, cbProfilType, 
		col++, 0, 
		1, 1, 
		0, 0, 
		0, 3, ds, 0, 
		GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		//-- Memory 1
		btMemory1 = new javax.swing.JButton();
		btMemory1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/favoris1.png")));
		btMemory1.setToolTipText(bundle.getString("FrmMiniroadbook.btMemory1.toolTipText"));
		btMemory1.setPreferredSize(new Dimension(btw, bth));
		btMemory1.setFocusable(false);
		btMemory1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		Utils.addComponent(pnlToolbar, btMemory1, 
		col++, 0, 
		1, 1, 
		0, 0, 
		0, 5, ds, 0, 
		GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		//-- Memory 2
		btMemory2 = new javax.swing.JButton();
		btMemory2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/favoris2.png")));
		btMemory2.setToolTipText(bundle.getString("FrmMiniroadbook.btMemory2.toolTipText"));
		btMemory2.setPreferredSize(new Dimension(btw, bth));
		btMemory2.setFocusable(false);
		btMemory2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		Utils.addComponent(pnlToolbar, btMemory2, 
		col++, 0, 
		1, 1, 
		0, 0, 
		0, 0, ds, 0, 
		GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);
		
		//-- Memory 3
		btMemory3 = new javax.swing.JButton();
		btMemory3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/favoris3.png")));
		btMemory3.setToolTipText(bundle.getString("FrmMiniroadbook.btMemory3.toolTipText"));
		btMemory3.setPreferredSize(new Dimension(btw, bth));
		btMemory3.setFocusable(false);
		btMemory3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		Utils.addComponent(pnlToolbar, btMemory3, 
		col++, 0, 
		1, 1, 
		0, 0, 
		0, 0, ds, 0, 
		GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		//-- Memory 4
		btMemory4 = new javax.swing.JButton();
		btMemory4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/favoris4.png")));
		btMemory4.setToolTipText(bundle.getString("FrmMiniroadbook.btMemory4.toolTipText"));
		btMemory4.setPreferredSize(new Dimension(btw, bth));
		btMemory4.setFocusable(false);
		btMemory4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		Utils.addComponent(pnlToolbar, btMemory4, 
		col++, 0, 
		1, 1, 
		0, 0, 
		0, 0, ds, 0, 
		GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		//-- Memory 5
		btMemory5 = new javax.swing.JButton();
		btMemory5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/favoris5.png")));
		btMemory5.setToolTipText(bundle.getString("FrmMiniroadbook.btMemory5.toolTipText"));
		btMemory5.setPreferredSize(new Dimension(btw, bth));
		btMemory5.setFocusable(false);
		btMemory5.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		Utils.addComponent(pnlToolbar, btMemory5, 
		col++, 0, 
		1, 1, 
		0, 0, 
		0, 0, ds, 0, 
		GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);


		//-- Width of the mini roadbook
		lbWidth = new javax.swing.JLabel(bundle.getString("FrmMiniroadbook.lbWidth.Text"));
		Utils.addComponent(pnlToolbar, lbWidth, 
		col++, 0, 
		1, 1, 
		0, 0, 
		0, 5, ds, 0, 
		GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		spinWidth = new CgSpinner(800,0,2000,1);
		spinWidth.setToolTipText(bundle.getString("FrmMiniroadbook.spinWidth.TooltipText"));
		spinWidth.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				track.MrbSizeW=spinWidth.getValueAsInt();
				pnlProfil.setWidth(track.MrbSizeW);
//				jScrollPaneProfil.repaint();
			}
		});
		Utils.addComponent(pnlToolbar, spinWidth, 
		col++, 0, 
		1, 1, 
		0, 0, 
		0, 3, ds, 0, 
		GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		//-- Height of the mini roadbook
		lbHeight = new javax.swing.JLabel(bundle.getString("FrmMiniroadbook.lbHeight.Text"));
		Utils.addComponent(pnlToolbar, lbHeight, 
		col++, 0, 
		1, 1, 
		0, 0, 
		0, 5, ds, 0, 
		GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

		
		spinHeight = new CgSpinner(600,0,2000,1);
		spinHeight.setToolTipText(bundle.getString("FrmMiniroadbook.spinHeight.TooltipText"));
		spinHeight.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				track.MrbSizeH=spinHeight.getValueAsInt();
				pnlProfil.setHeight(track.MrbSizeH);
//				jScrollPaneProfil.repaint();
			}
		});
		Utils.addComponent(pnlToolbar, spinHeight, 
		col++, 0, 
		1, 1, 
		1, 0, 
		0, 3, ds, 0, 
		GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);
	}
	
	
	private void initComponents() {
		int line = 0;

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(bundle.getString("FrmMiniroadbook.title"));
		setAlwaysOnTop(true);
		setPreferredSize(new Dimension(1000,700));
		setType(java.awt.Window.Type.NORMAL);

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new BorderLayout());

		//-- Toolbar
		Create_Toolbar();
		paneGlobal.add(pnlToolbar, BorderLayout.NORTH);

		// -- Split bar (vertical)
		// -----------------------------------------
		SplitPane = new javax.swing.JSplitPane();
		SplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
		paneGlobal.add(SplitPane, BorderLayout.CENTER);
		
		//-- Top panel
		pnlTop = new JPanel();
		pnlTop.setLayout(new GridBagLayout());
		SplitPane.setTopComponent(pnlTop);

		//-- Bottom panel
		pnlBottom = new JPanel();
		pnlBottom.setLayout(new GridBagLayout());
		SplitPane.setBottomComponent(pnlBottom);
		
		//== Panel properties
		pnlProperties = new JPanel();
		pnlProperties.setBorder(BorderFactory.createTitledBorder(bundle.getString("FrmMiniroadbook.pnlProperties.Title")));
		pnlProperties.setLayout(new GridBagLayout());
		Utils.addComponent(pnlTop, pnlProperties, 
				0, 0, 
				1, 1, 
				0, 1, 
				10, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		//-- Selection
		lbSelect=new javax.swing.JLabel(bundle.getString("FrmMiniroadbook.lbSelect.Text"));
		Utils.addComponent(pnlProperties, lbSelect, 
				0, 0, 
				1, 1, 
				0, 0, 
				10, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		chkSelect = new javax.swing.JCheckBox();
		Utils.addComponent(pnlProperties, chkSelect, 
				1, 0, 
				GridBagConstraints.REMAINDER, 1, 
				1, 0, 
				10, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		//-- Position
		lbPosition=new javax.swing.JLabel(bundle.getString("FrmMiniroadbook.lbPosition.Text"));
		Utils.addComponent(pnlProperties, lbPosition, 
				0, 1, 
				1, 1, 
				0, 0, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		spinPosition = new CgSpinner(0,0,59,1);
		Utils.addComponent(pnlProperties, spinPosition, 
				1, 1, 
				GridBagConstraints.REMAINDER, 1, 
				0, 0, 
				0, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		//-- Alignement
		lbAlignement=new javax.swing.JLabel(bundle.getString("FrmMiniroadbook.lbAlignement.Text"));
		Utils.addComponent(pnlProperties, lbAlignement, 
				0, 2, 
				1, 1, 
				0, 0, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
//		btAlignLeft = new javax.swing.JButton("|<");
		btAlignLeft = new javax.swing.JButton();
		btAlignLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/left.png")));
		btAlignLeft.setToolTipText(bundle.getString("FrmMiniroadbook.btAlignLeft.toolTipText"));
		btAlignLeft.setFocusable(false);
		btAlignLeft.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		Utils.addComponent(pnlProperties, btAlignLeft, 
				1, 2, 
				1, 1, 
				0, 0, 
				0, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
//		btAlignCenter = new javax.swing.JButton("|");
		btAlignCenter = new javax.swing.JButton();
		btAlignCenter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/center.png")));
		btAlignCenter.setToolTipText(bundle.getString("FrmMiniroadbook.btAlignCenter.toolTipText"));
		btAlignCenter.setFocusable(false);
		btAlignCenter.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		Utils.addComponent(pnlProperties, btAlignCenter, 
				2, 2, 
				1, 1, 
				0, 0, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
//		btAlignRight = new javax.swing.JButton(">|");
		btAlignRight = new javax.swing.JButton();
		btAlignRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/right.png")));
		btAlignRight.setToolTipText(bundle.getString("FrmMiniroadbook.btAlignRight.toolTipText"));
		btAlignRight.setFocusable(false);
		btAlignRight.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		Utils.addComponent(pnlProperties, btAlignRight, 
				3, 2, 
				1, 1, 
				0, 0, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		//-- Format
		lbFormat=new javax.swing.JLabel(bundle.getString("FrmMiniroadbook.lbFormat.Text"));
		Utils.addComponent(pnlProperties, lbFormat, 
				0, 3, 
				1, 1, 
				0, 0, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		tfFormat = new JTextFieldLimit(200);
		Utils.addComponent(pnlProperties, tfFormat, 
				1, 3, 
				2, 1, 
				1, 0, 
				0, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		btFormat = new javax.swing.JButton("...");
		btFormat.setToolTipText(bundle.getString("FrmMiniroadbook.btFormat.toolTipText"));
		btFormat.setFocusable(false);
		btFormat.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		Utils.addComponent(pnlProperties, btFormat, 
				3, 3, 
				1, 1, 
				1, 0, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		//-- Size
		lbSize=new javax.swing.JLabel(bundle.getString("FrmMiniroadbook.lbSize.Text"));
		Utils.addComponent(pnlProperties, lbSize, 
				0, 4, 
				1, 1, 
				0, 0, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		spinSize = new CgSpinner(0,0,59,1);
		Utils.addComponent(pnlProperties, spinSize, 
				1, 4, 
				GridBagConstraints.REMAINDER, 1, 
				0, 0, 
				0, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		//-- Tags
		lbTags=new javax.swing.JLabel(bundle.getString("FrmMiniroadbook.Tags.Text"));
		Utils.addComponent(pnlProperties, lbTags, 
				0, 5, 
				1, 1, 
				0, 0, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		chkTags = new javax.swing.JCheckBox();
		Utils.addComponent(pnlProperties, chkTags, 
				1, 5, 
				GridBagConstraints.REMAINDER, 1, 
				1, 0, 
				0, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		//-- Comment
		lbComment=new javax.swing.JLabel(bundle.getString("FrmMiniroadbook.lbComment.Text"));
		Utils.addComponent(pnlProperties, lbComment, 
				0, 6, 
				1, 1, 
				0, 1, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		tfComment = new JTextFieldLimit(200);
		Utils.addComponent(pnlProperties, tfComment, 
				1, 6, 
				GridBagConstraints.REMAINDER, 1, 
				1, 1, 
				0, 5, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		//-- Panel data
		TableData = new javax.swing.JTable();
		TableData.setModel(model);//new ParamPointsModel(param));
		TableData.getTableHeader().setReorderingAllowed(false);
		TableData.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
//				TableMainMouseClicked(evt);
			}
		});
		TableData.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
//				TableMainKeyReleased(evt);
			}
		});
		
		jScrollPaneData = new javax.swing.JScrollPane();
		jScrollPaneData.setViewportView(TableData);
		Utils.addComponent(pnlTop, jScrollPaneData, 
				1, 0, 
				1, 1, 
				1, 1, 
				10, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		pnlProfil = new PanelProfilMRB(640,480);
		
		jScrollPaneProfil = new javax.swing.JScrollPane();
		jScrollPaneProfil.setViewportView(pnlProfil);
		
		Utils.addComponent(pnlBottom, jScrollPaneProfil, 
				0, 0, 
				1, 1, 
				1, 1, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
				
		pack();

		setLocationRelativeTo(null);
	}

	protected void Refresh() {
//		spinFromLine.setEnabled(rbFromLine.isSelected());
//		spinToLine.setEnabled(rbToLine.isSelected());
//		spinDiff.setEnabled(rbOther.isSelected());
	}
	
}
