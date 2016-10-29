package course_generator.utils;

import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class CgSpinner extends JSpinner {

	public CgSpinner(int start, int min, int max, int step) {
		super();
		setModel(new SpinnerNumberModel(start, //initial value
                min, //min
                max, //max
                step) //step
				);
		//Center
		JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor)this.getEditor();
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
	}
}
