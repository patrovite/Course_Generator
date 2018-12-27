package course_generator.dialogs;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JProgressBar;

public class ProgressDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JProgressBar progressBar;
	private JButton buttonCancel;
	private ProgressDialogListener progressDialogListener;
	private ResourceBundle bundle;


	/**
	 * Constructor
	 * 
	 * @param progressBar
	 *            The progress bar this has to update
	 */
	public ProgressDialog(Window parent, String dialogTitle) {
		super(parent, dialogTitle, ModalityType.APPLICATION_MODAL);

		setLayout(new FlowLayout());
		setSize(450, 150);

		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setPreferredSize(new Dimension(300, 100));
		add(progressBar);

		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle"); //$NON-NLS-1$

		buttonCancel = new JButton(bundle.getString("Global.btCancel.text")); //$NON-NLS-1$
		buttonCancel.setPreferredSize(new Dimension(100, 75));
		buttonCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (progressDialogListener != null) {
					progressDialogListener.progressDialogCancelled();
				}

			}
		});
		add(buttonCancel);

		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	}


	public void setValue(int value) {
		progressBar.setValue(value);
	}


	public void setListener(ProgressDialogListener listener) {
		this.progressDialogListener = listener;
	}
}
