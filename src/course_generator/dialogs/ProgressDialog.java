package course_generator.dialogs;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JProgressBar;

import course_generator.utils.Utils;

public class ProgressDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JProgressBar progressBar;
	private JButton buttonCancel;
	private ProgressDialogListener progressDialogListener;
	private ResourceBundle bundle;

	/**
	 * Constructor
	 * 
	 * @param progressBar The progress bar this has to update
	 */
	public ProgressDialog(Window parent, String dialogTitle) {
		super(parent, dialogTitle, ModalityType.APPLICATION_MODAL);

		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setResizable(false);

		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		Container paneGlobal = getContentPane();
		paneGlobal.setLayout(new GridBagLayout());

		// -- Progress bar
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setPreferredSize(new Dimension(500, 30));
		Utils.addComponent(paneGlobal, progressBar, 0, 0, 1, 1, 0, 0, 10, 10, 10, 10,
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);

		// -- Cancel button
		buttonCancel = new JButton(bundle.getString("Global.btCancel.text"));
		buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (progressDialogListener != null) {
					progressDialogListener.progressDialogCancelled();
				}

			}
		});
		Utils.addComponent(paneGlobal, buttonCancel, 0, 1, 1, 1, 0, 0, 0, 10, 10, 10, GridBagConstraints.CENTER,
				GridBagConstraints.NONE);

		pack();
		// -- Center the dialog on the parent
		setLocationRelativeTo(parent);
	}

	public void setValue(int value) {
		progressBar.setValue(value);
	}

	public void setListener(ProgressDialogListener listener) {
		this.progressDialogListener = listener;
	}
}
