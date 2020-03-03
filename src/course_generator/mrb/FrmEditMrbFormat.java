package course_generator.mrb;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import course_generator.CgData;
import course_generator.TrackData;
import course_generator.settings.CgSettings;
import course_generator.utils.Utils;

public class FrmEditMrbFormat extends javax.swing.JDialog {
	private static final long serialVersionUID = -8114019639948346916L;
	private ResourceBundle bundle;
	private boolean ok;
	private CgSettings settings;
	private TrackData track;
	private CgData data;
	private JLabel lbLabelFormat;
	private JTextField tfLabelFormat;
	private JButton btName;
	private JButton btTotalDist;
	private JButton btElevation;
	private JButton btStation;
	private JButton btStationLong;
	private JButton btStationShort;
	private JButton btHour;
	private JButton btHourLong;
	private JButton btHourShort;
	private JButton btTime;
	private JButton btTimeLong;
	private JButton btTimeShort;
	private JButton btTotalAscend;
	private JButton btTimeLimitTime;
	private JButton btTimeLimitHour;
	private JButton btTotalDescend;
	private JButton btComment;
	private JButton btCommentSpecific;
	private JButton btNewLine;
	private JLabel lbHelp;
	private JLabel lbResult;
	private PanelPreviewLabel pnResult;
	private JButton btOk;
	private JButton btCancel;
	private JPanel jPanelButtons;
	private JButton btRefresh;
	private JButton btPreviousAscend;
	private JButton btPreviousDescend;
	private JButton btTimeDelta;

	/**
	 * Creates new form frmSettings
	 */
	public FrmEditMrbFormat(Window parent, CgSettings settings) {
		super(parent);
		this.settings = settings;
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		initComponents();
		setModal(true);
	}

	/**
	 * Show the dialog
	 * 
	 * @param settings   Object containing the settings
	 * @param track      Object containing the track
	 * @param start_line Line number where to start
	 * @param end_line   Line number where to end
	 * @return Object containing the result
	 */
	public String showDialog(CgData r, TrackData track, String format) {
		this.track = track;
		this.data = r;

		// Set fields
		tfLabelFormat.setText(format);

		// End set field
		ok = false;

		// -- Update the display
		Refresh();

		// -- Show the dialog
		setVisible(true);

		if (ok) {
			return tfLabelFormat.getText();
		} else
			return format;
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

		@SuppressWarnings("serial")
		Action actionListener = new AbstractAction() {
			public void actionPerformed(ActionEvent actionEvent) {
				setVisible(false);
			}
		};

		@SuppressWarnings("serial")
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

	private void initComponents() {
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(bundle.getString("FrmEditMrbFormat.title"));
		setAlwaysOnTop(true);
		setType(java.awt.Window.Type.NORMAL);
		addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentShown(java.awt.event.ComponentEvent evt) {
				repaint();
			}
		});

		// -- Layout
		// ------------------------------------------------------------
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new GridBagLayout());
		int line = 0;

		// -- Line 0

		lbLabelFormat = new JLabel(bundle.getString("FrmEditMrbFormat.lbLabelFormat.text"));
		Utils.addComponent(paneGlobal, lbLabelFormat, 0, line, 3, 1, 0, 0, 10, 10, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		lbResult = new JLabel(bundle.getString("FrmEditMrbFormat.lbResult.text"), JLabel.CENTER);
		Utils.addComponent(paneGlobal, lbResult, 3, line++, 1, 1, 1, 0, 10, 10, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Line 1

		tfLabelFormat = new JTextField();
		Utils.addComponent(paneGlobal, tfLabelFormat, 0, line, 2, 1, 0, 0, 0, 10, 10, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH);

		// btRefresh = new JButton(bundle.getString("FrmEditMrbFormat.btRefresh.text"));
		btRefresh = new JButton();
		btRefresh.setIcon(Utils.getIcon(this, "refresh.png", settings.DialogIconSize));
		btRefresh.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Refresh();
			}
		});
		Utils.addComponent(paneGlobal, btRefresh, 2, line, 1, 1, 0, 0, 0, 2, 10, 0, GridBagConstraints.WEST,
				GridBagConstraints.VERTICAL);

		// pnResult = new JPanel();
		pnResult = new PanelPreviewLabel();
		Utils.addComponent(paneGlobal, pnResult, 3, line++, 1, 9, 1, 1, 0, 10, 0, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		// -- Line 2

		btName = new JButton(bundle.getString("FrmEditMrbFormat.btName.text"));
		btName.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tfLabelFormat.setText(tfLabelFormat.getText() + "%N");
				Refresh();
			}
		});
		Utils.addComponent(paneGlobal, btName, 0, line, 1, 1, 0, 0, 0, 10, 0, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		btTotalDist = new JButton(bundle.getString("FrmEditMrbFormat.btTotalDist.text"));
		btTotalDist.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tfLabelFormat.setText(tfLabelFormat.getText() + "%D");
				Refresh();
			}
		});
		Utils.addComponent(paneGlobal, btTotalDist, 1, line, 1, 1, 0, 0, 0, 0, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		btElevation = new JButton(bundle.getString("FrmEditMrbFormat.btElevation.text"));
		btElevation.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tfLabelFormat.setText(tfLabelFormat.getText() + "%A");
				Refresh();
			}
		});
		Utils.addComponent(paneGlobal, btElevation, 2, line++, 1, 1, 0, 0, 0, 0, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Line 3

		btStation = new JButton(bundle.getString("FrmEditMrbFormat.btStation.text"));
		btStation.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tfLabelFormat.setText(tfLabelFormat.getText() + "%R");
				Refresh();
			}
		});
		Utils.addComponent(paneGlobal, btStation, 0, line, 1, 1, 0, 0, 0, 10, 0, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		btStationLong = new JButton(bundle.getString("FrmEditMrbFormat.btStationLong.text"));
		btStationLong.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tfLabelFormat.setText(tfLabelFormat.getText() + "%Rl");
				Refresh();
			}
		});
		Utils.addComponent(paneGlobal, btStationLong, 1, line, 1, 1, 0, 0, 0, 0, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		btStationShort = new JButton(bundle.getString("FrmEditMrbFormat.btStationShort.text"));
		btStationShort.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tfLabelFormat.setText(tfLabelFormat.getText() + "%Rs");
				Refresh();
			}
		});
		Utils.addComponent(paneGlobal, btStationShort, 2, line++, 1, 1, 0, 0, 0, 0, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Line 4

		btHour = new JButton(bundle.getString("FrmEditMrbFormat.btHour.text"));
		btHour.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tfLabelFormat.setText(tfLabelFormat.getText() + "%H");
				Refresh();
			}
		});
		Utils.addComponent(paneGlobal, btHour, 0, line, 1, 1, 0, 0, 0, 10, 0, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		btHourLong = new JButton(bundle.getString("FrmEditMrbFormat.btHourLong.text"));
		btHourLong.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tfLabelFormat.setText(tfLabelFormat.getText() + "%hl");
				Refresh();
			}
		});
		Utils.addComponent(paneGlobal, btHourLong, 1, line, 1, 1, 0, 0, 0, 0, 0, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		btHourShort = new JButton(bundle.getString("FrmEditMrbFormat.btHourShort.text"));
		btHourShort.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tfLabelFormat.setText(tfLabelFormat.getText() + "%hs");
				Refresh();
			}
		});
		Utils.addComponent(paneGlobal, btHourShort, 2, line++, 1, 1, 0, 0, 0, 0, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Line 5

		btTime = new JButton(bundle.getString("FrmEditMrbFormat.btTime.text"));
		btTime.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tfLabelFormat.setText(tfLabelFormat.getText() + "%T");
				Refresh();
			}
		});
		Utils.addComponent(paneGlobal, btTime, 0, line, 1, 1, 0, 0, 0, 10, 0, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		btTimeLong = new JButton(bundle.getString("FrmEditMrbFormat.btTimeLong.text"));
		btTimeLong.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tfLabelFormat.setText(tfLabelFormat.getText() + "%Tl");
				Refresh();
			}
		});
		Utils.addComponent(paneGlobal, btTimeLong, 1, line, 1, 1, 0, 0, 0, 0, 0, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		btComment = new JButton(bundle.getString("FrmEditMrbFormat.btComment.text"));
		btComment.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tfLabelFormat.setText(tfLabelFormat.getText() + "%c");
				Refresh();
			}
		});
		Utils.addComponent(paneGlobal, btComment, 2, line++, 1, 1, 0, 0, 0, 0, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Line 6

		btTimeShort = new JButton(bundle.getString("FrmEditMrbFormat.btTimeShort.text"));
		btTimeShort.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tfLabelFormat.setText(tfLabelFormat.getText() + "%Ts");
				Refresh();
			}
		});
		Utils.addComponent(paneGlobal, btTimeShort, 0, line, 1, 1, 0, 0, 0, 10, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		btTimeDelta = new JButton(bundle.getString("FrmEditMrbFormat.btTimeDelta.text"));
		btTimeDelta.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tfLabelFormat.setText(tfLabelFormat.getText() + "%Td");
				Refresh();
			}
		});
		Utils.addComponent(paneGlobal, btTimeDelta, 1, line, 1, 1, 0, 0, 0, 0, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		btCommentSpecific = new JButton(bundle.getString("FrmEditMrbFormat.btCommentSpecific.text"));
		btCommentSpecific.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tfLabelFormat.setText(tfLabelFormat.getText() + "%C");
				Refresh();
			}
		});
		Utils.addComponent(paneGlobal, btCommentSpecific, 2, line++, 1, 1, 0, 0, 0, 0, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Line 7

		btTotalAscend = new JButton(bundle.getString("FrmEditMrbFormat.btTotalAscend.text"));
		btTotalAscend.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tfLabelFormat.setText(tfLabelFormat.getText() + "%+");
				Refresh();
			}
		});
		Utils.addComponent(paneGlobal, btTotalAscend, 0, line, 1, 1, 0, 0, 0, 10, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		btPreviousAscend = new JButton(bundle.getString("FrmEditMrbFormat.btPreviousAscend.text"));
		btPreviousAscend.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tfLabelFormat.setText(tfLabelFormat.getText() + "%+d");
				Refresh();
			}
		});
		Utils.addComponent(paneGlobal, btPreviousAscend, 1, line, 1, 1, 0, 0, 0, 0, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		btTimeLimitTime = new JButton(bundle.getString("FrmEditMrbFormat.btTimeLimitTime.text"));
		btTimeLimitTime.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tfLabelFormat.setText(tfLabelFormat.getText() + "%B");
				Refresh();
			}
		});
		Utils.addComponent(paneGlobal, btTimeLimitTime, 2, line++, 1, 1, 0, 0, 0, 0, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Line 8

		btTotalDescend = new JButton(bundle.getString("FrmEditMrbFormat.btTotalDescend.text"));
		btTotalDescend.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tfLabelFormat.setText(tfLabelFormat.getText() + "%-");
				Refresh();
			}
		});
		Utils.addComponent(paneGlobal, btTotalDescend, 0, line, 1, 1, 0, 0, 0, 10, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		btPreviousDescend = new JButton(bundle.getString("FrmEditMrbFormat.btPreviousDescend.text"));
		btPreviousDescend.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tfLabelFormat.setText(tfLabelFormat.getText() + "%-d");
				Refresh();
			}
		});
		Utils.addComponent(paneGlobal, btPreviousDescend, 1, line, 1, 1, 0, 0, 0, 0, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		btTimeLimitHour = new JButton(bundle.getString("FrmEditMrbFormat.btTimeLimitHour.text"));
		btTimeLimitHour.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tfLabelFormat.setText(tfLabelFormat.getText() + "%b");
				Refresh();
			}
		});
		Utils.addComponent(paneGlobal, btTimeLimitHour, 2, line++, 1, 1, 0, 0, 0, 0, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// -- Line 9

		btNewLine = new JButton(bundle.getString("FrmEditMrbFormat.btNewLine.text"));
		btNewLine.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tfLabelFormat.setText(tfLabelFormat.getText() + "%L");
				Refresh();
			}
		});
		Utils.addComponent(paneGlobal, btNewLine, 0, line++, 1, 1, 0, 0, 0, 10, 0, 0,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL);

		// ...

		lbHelp = new JLabel(bundle.getString("FrmEditMrbFormat.lbHelp.text"));
		lbHelp.setOpaque(true);
		lbHelp.setBackground(Color.WHITE);
		lbHelp.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		Utils.addComponent(paneGlobal, lbHelp, 0, line++, 3, 1, 0, 1, 10, 10, 0, 0, GridBagConstraints.BASELINE_LEADING,
				GridBagConstraints.HORIZONTAL);

		// == BUTTONS
		// ===========================================================
		jPanelButtons = new javax.swing.JPanel();
		jPanelButtons.setLayout(new FlowLayout());
		Utils.addComponent(paneGlobal, jPanelButtons, 0, line, GridBagConstraints.REMAINDER, 1, 0, 0, 10, 0, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH);

		btCancel = new javax.swing.JButton();
		btCancel.setIcon(Utils.getIcon(this, "cancel.png", settings.DialogIconSize));
		btCancel.setText(bundle.getString("Global.btCancel.text"));
		btCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setVisible(false);
			}
		});

		btOk = new javax.swing.JButton();
		btOk.setIcon(Utils.getIcon(this, "valid.png", settings.DialogIconSize));
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

		// ------------------------------------------------------------
		pack();
		setLocationRelativeTo(null);
	}

	/**
	 * Refresh the preview panel
	 */
	private void Refresh() {
		data.FmtLbMiniRoadbook = tfLabelFormat.getText();
		pnResult.setData(data);
		pnResult.setTrack(track);
		pnResult.setSettings(settings);
		pnResult.Refresh();
	}

}
