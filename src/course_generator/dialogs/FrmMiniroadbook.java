package course_generator.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import course_generator.TrackData;
import course_generator.settings.CgSettings;
import course_generator.utils.CgSpinner;
import course_generator.utils.JTextFieldLimit;
import course_generator.utils.Utils;

public class FrmMiniroadbook  extends javax.swing.JDialog {

	private ResourceBundle bundle;
	private boolean ok;
	private int start;
	private int end;
	private CgSettings settings;
	private TrackData track;
//	private CgData data;
	private JRadioButton rbFromStart;
	private JRadioButton rbToEnd;
	private JRadioButton rbToLine;
	private ButtonGroup groupEnd;
	private JRadioButton rbVeryEasy;
	private JRadioButton rbEasy;
	private JRadioButton rbAverage;
	private JRadioButton rbVeryHard;
	private JRadioButton rbHard;
	private JRadioButton rbOther;
	private ButtonGroup groupDiff;
	private CgSpinner spinFromLine;
	private CgSpinner spinToLine;
	private CgSpinner spinDiff;
	private JToolBar ToolBar;
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
	private JComboBox cbProfilType;
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
	private JLabel lbProfilSize;
	private JSlider sliderWidth;
	private JSlider sliderHeigth;
	private JPanel pnlProfil;

	
	/**
	 * Creates new form frmSettings
	 */
	public FrmMiniroadbook() {
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
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
	public boolean showDialog(CgSettings settings, TrackData track) {
		this.settings = settings;
		this.track = track;
		

		// Set field
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

		
		if (ok) {
			// Copy fields
//			if (rbFromStart.isSelected())
//				res.Start=0;
//			else 
//				res.Start=spinFromLine.getValueAsInt()-1;
//			
//			if (rbToEnd.isSelected())
//				res.End=track.data.size()-1;
//			else
//				res.End=spinToLine.getValueAsInt()-1;
//			
//			if (rbVeryEasy.isSelected())
//				res.Difficulty=CgConst.DIFF_VERYEASY;
//			else if (rbEasy.isSelected())
//				res.Difficulty=CgConst.DIFF_EASY;
//			else if (rbAverage.isSelected())
//				res.Difficulty=CgConst.DIFF_AVERAGE;
//			else if (rbHard.isSelected())
//				res.Difficulty=CgConst.DIFF_HARD;
//			else if (rbVeryHard.isSelected())
//				res.Difficulty=CgConst.DIFF_VERYHARD;
//			else 
//				res.Difficulty=spinDiff.getValueAsInt();
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
		ToolBar = new javax.swing.JToolBar();
		ToolBar.setFloatable(false);
		ToolBar.setRollover(true);

		//-- Save as image
		btSaveAsImage = new javax.swing.JButton();
		btSaveAsImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/save.png")));
		btSaveAsImage.setToolTipText(bundle.getString("FrmMiniroadbook.btSaveAsImage.toolTipText"));
		btSaveAsImage.setFocusable(false);
		btSaveAsImage.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Save as image
			}
		});
		ToolBar.add(btSaveAsImage);

		//-- Separator
		ToolBar.add(new javax.swing.JToolBar.Separator());

		//-- Configuration
		btConfig = new javax.swing.JButton();
		btConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/settings.png")));
		btConfig.setToolTipText(bundle.getString("FrmMiniroadbook.btConfig.toolTipText"));
		btConfig.setFocusable(false);
		btConfig.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		ToolBar.add(btConfig);

		//-- Copy format
		btCopyFormat = new javax.swing.JButton();
		btCopyFormat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/pipette.png")));
		btCopyFormat.setToolTipText(bundle.getString("FrmMiniroadbook.btCopyFormat.toolTipText"));
		btCopyFormat.setFocusable(false);
		btCopyFormat.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		ToolBar.add(btCopyFormat);

		//-- Paste format
		btPasteFormat = new javax.swing.JButton();
		btPasteFormat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/replicate.png")));
		btPasteFormat.setToolTipText(bundle.getString("FrmMiniroadbook.btPasteFormat.toolTipText"));
		btPasteFormat.setFocusable(false);
		btPasteFormat.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		ToolBar.add(btPasteFormat);
		
		//-- Config format duplication
		btPasteFormatConfig = new javax.swing.JButton();
		btPasteFormatConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/replicate.png")));
		btPasteFormatConfig.setToolTipText(bundle.getString("FrmMiniroadbook.btPasteFormatConfig.toolTipText"));
		btPasteFormatConfig.setFocusable(false);
		btPasteFormatConfig.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		ToolBar.add(btPasteFormatConfig);

		//-- Separator
		ToolBar.add(new javax.swing.JToolBar.Separator());
		
		//-- Label connected to the bottom of the profil
		btLabelConnectedToBottom = new javax.swing.JButton();
		btLabelConnectedToBottom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/label_to_bottom.png")));
		btLabelConnectedToBottom.setToolTipText(bundle.getString("FrmMiniroadbook.btLabelConnectedToBottom.toolTipText"));
		btLabelConnectedToBottom.setFocusable(false);
		btLabelConnectedToBottom.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		ToolBar.add(btLabelConnectedToBottom);

		//-- Label connected to the top of the profil
		btLabelConnectedToProfil = new javax.swing.JButton();
		btLabelConnectedToProfil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/label_to_profil.png")));
		btLabelConnectedToProfil.setToolTipText(bundle.getString("FrmMiniroadbook.btLabelConnectedToProfil.toolTipText"));
		btLabelConnectedToProfil.setFocusable(false);
		btLabelConnectedToProfil.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		ToolBar.add(btLabelConnectedToProfil);

		//-- Separator
		ToolBar.add(new javax.swing.JToolBar.Separator());

		//-- Label "Profil type"
		lbProfilType = new javax.swing.JLabel(bundle.getString("FrmMiniroadbook.lbProfilType.Text")+" ");
		ToolBar.add(lbProfilType);
		
		//-- "Profil type" selection
		cbProfilType = new javax.swing.JComboBox<>();
		String profiltype[] = { 
				bundle.getString("FrmMiniroadbook.ProfilSimple"),
				bundle.getString("FrmMiniroadbook.ProfilRoadTrack"), 
				bundle.getString("FrmMiniroadbook.ProfilSlope") };
		cbProfilType.setModel(new javax.swing.DefaultComboBoxModel<>(profiltype));
		ToolBar.add(cbProfilType);
		
		//-- Separator
		ToolBar.add(new javax.swing.JToolBar.Separator());

		//-- Memory 1
		btMemory1 = new javax.swing.JButton();
		btMemory1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/favoris1.png")));
		btMemory1.setToolTipText(bundle.getString("FrmMiniroadbook.btMemory1.toolTipText"));
		btMemory1.setFocusable(false);
		btMemory1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		ToolBar.add(btMemory1);
		
		//-- Memory 2
		btMemory2 = new javax.swing.JButton();
		btMemory2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/favoris2.png")));
		btMemory2.setToolTipText(bundle.getString("FrmMiniroadbook.btMemory2.toolTipText"));
		btMemory2.setFocusable(false);
		btMemory2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		ToolBar.add(btMemory2);
		
		//-- Memory 3
		btMemory3 = new javax.swing.JButton();
		btMemory3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/favoris3.png")));
		btMemory3.setToolTipText(bundle.getString("FrmMiniroadbook.btMemory3.toolTipText"));
		btMemory3.setFocusable(false);
		btMemory3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		ToolBar.add(btMemory3);
		
		//-- Memory 4
		btMemory4 = new javax.swing.JButton();
		btMemory4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/favoris4.png")));
		btMemory4.setToolTipText(bundle.getString("FrmMiniroadbook.btMemory4.toolTipText"));
		btMemory4.setFocusable(false);
		btMemory4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		ToolBar.add(btMemory4);
		
		//-- Memory 5
		btMemory5 = new javax.swing.JButton();
		btMemory5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/course_generator/images/favoris5.png")));
		btMemory5.setToolTipText(bundle.getString("FrmMiniroadbook.btMemory5.toolTipText"));
		btMemory5.setFocusable(false);
		btMemory5.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO Config
			}
		});
		ToolBar.add(btMemory5);
	}
	
	
	private void initComponents() {
		int line = 0;

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(bundle.getString("FrmMiniroadbook.title"));
		setAlwaysOnTop(true);
		setResizable(false);
		setMinimumSize(new Dimension(1000,700));
		setType(java.awt.Window.Type.UTILITY);

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
//		paneGlobal.setLayout(new GridBagLayout());
		paneGlobal.setLayout(new BorderLayout());

		//-- Toolbar
		Create_Toolbar();
		paneGlobal.add(ToolBar, BorderLayout.NORTH);
//		Utils.addComponent(paneGlobal, ToolBar, 
//				0, 0, 
//				GridBagConstraints.REMAINDER, 1, 
//				1, 0, 
//				0, 0, 0, 0, 
//				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);

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
		
		btAlignLeft = new javax.swing.JButton("|<");
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
		
		btAlignCenter = new javax.swing.JButton("|");
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
		
		btAlignRight = new javax.swing.JButton(">|");
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
		jScrollPaneData = new javax.swing.JScrollPane();
		jScrollPaneData.setViewportView(TableData);
		Utils.addComponent(pnlTop, jScrollPaneData, 
				1, 0, 
				1, 1, 
				1, 1, 
				10, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		// -- Bottom panel
		lbProfilSize=new javax.swing.JLabel("",JLabel.CENTER);
		lbProfilSize.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lbProfilSize.setPreferredSize(new Dimension(40,40));
		lbProfilSize.setText("<HTML>0<BR>0</HTML>");
		lbProfilSize.setOpaque(true);
		lbProfilSize.setBackground(Color.WHITE);
		Utils.addComponent(pnlBottom, lbProfilSize, 
				0, 0, 
				1, 1, 
				0, 0, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		sliderWidth = new javax.swing.JSlider();
		sliderWidth.setMaximum(2000);
		sliderWidth.setMinorTickSpacing(1);
//		sliderWidth.setPaintTicks(true);
//		sliderWidth.setToolTipText(bundle.getString("frmEditNode.sliderPriority.toolTipText")); 
		sliderWidth.setValue(0);
		sliderWidth.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
//				updateSlider(sliderWidth.getValue());
			}
		});
		Utils.addComponent(pnlBottom, sliderWidth, 
				1, 0, 
				1, 1, 
				1, 0, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		sliderHeigth = new javax.swing.JSlider();
		sliderHeigth.setOrientation(JSlider.VERTICAL);
		sliderHeigth.setMaximum(2000);
		sliderHeigth.setMinorTickSpacing(1);
//		sliderHeigth.setPaintTicks(true);
//		sliderHeigth.setToolTipText(bundle.getString("frmEditNode.sliderPriority.toolTipText")); 
		sliderHeigth.setValue(0);
		sliderHeigth.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
//				updateSlider(sliderWidth.getValue());
			}
		});
		Utils.addComponent(pnlBottom, sliderHeigth, 
				0, 1, 
				1, 1, 
				0, 1, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		
		pnlProfil = new JPanel();
		pnlProfil.setOpaque(true);
		pnlProfil.setBackground(Color.WHITE);
		Utils.addComponent(pnlBottom, pnlProfil, 
				1, 1, 
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
