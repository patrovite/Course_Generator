package course_generator.utils;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class CgSpinner extends JSpinner {
	private SpinnerNumberModel model;
	private int min;
	private int max;
	private int step;
	
	public CgSpinner(int start, int min, int max, int step) {
		super();
		this.min=min;
		this.max=max;
		this.step=step;
		
		model=new SpinnerNumberModel(start, //initial value
                min, //min
                max, //max
                step); //step
		setModel(model);

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
    	int value = (int)getValue();
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

	public void setMaximum(int value) {
		max=value;
		model.setMaximum(value);
	}

	public void setMinimum(int value) {
		min=value;
		model.setMinimum(value);
	}

	public void setStep(int value) {
		step=value;
		model.setStepSize(value);
	}

	public int getValueAsInt() {
		return (int)this.getValue();
	}
	
	public double getValueAsDouble() {
		int v=(int)this.getValue();
		return (double)v;
	}
	
}
