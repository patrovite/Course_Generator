package course_generator.utils;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class CgSpinnerDouble  extends JSpinner {

	private SpinnerNumberModel model;
	private double step;
	private double min;
	private double max;
	
	public CgSpinnerDouble(double start, double min, double max, double step) {
		super();
		this.min=min;
		this.max=max;
		this.step=step;
		
		model = new SpinnerNumberModel(start, //initial value
				min, //min
				max, //max
				step);
		
		this.setModel(model);

		addMouseWheelListener(new MouseWheelListener() {
		    public void mouseWheelMoved(MouseWheelEvent mwe) {
		    	MouseWheelAction(mwe.getWheelRotation());
		    }
		});
		
		//Center
		JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor)this.getEditor();
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
	}

	private void MouseWheelAction(int wheelRotation) {
    	double value = (Double)getValue();
    	int n=Math.abs(wheelRotation);
    	if (wheelRotation>0) {
    		value=value+step*n;
    		if (value>max) value=max;
    	}
    	else {
    		value=value-step*n;
    		if (value<min) value=min;
    	}
        setValue(value);
	}

	public void setMaximum(double value) {
		max=value;
		model.setMaximum(value);
	}

	public void setMinimum(double value) {
		min=value;
		model.setMinimum(value);
	}

	public void setStep(double value) {
		step=value;
		model.setStepSize(value);
	}
	
    /**
     * Returns the current value as a Double
     */
    public Double getValueAsDouble() {
        return (Double)getValue();
    }
		
}
