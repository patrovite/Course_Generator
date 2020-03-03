/*
 * Course Generator
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

package course_generator.utils;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.ParseException;

import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class CgSpinner extends JSpinner {
	private static final long serialVersionUID = 2583265016542926568L;
	private SpinnerNumberModel model;
	private int min;
	private int max;
	private int step;

	public CgSpinner(int start, int min, int max, int step) {
		super();
		this.min = min;
		this.max = max;
		this.step = step;

		model = new SpinnerNumberModel(start, // initial value
				min, // min
				max, // max
				step); // step
		setModel(model);

		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent mwe) {
				MouseWheelAction(mwe.getWheelRotation());
			}
		});

		// Center
		JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) this.getEditor();
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
	}

	private void MouseWheelAction(int wheelRotation) {
		int value = (int) getSpinnerValue();
		int n = Math.abs(wheelRotation);
		if (wheelRotation > 0) {
			value = value + step * n;
			if (value > max)
				value = max;
		} else {
			value = value - step * n;
			if (value < min)
				value = min;
		}
		setValue(value);
	}

	public void setMaximum(int value) {
		max = value;
		model.setMaximum(value);
	}

	public void setMinimum(int value) {
		min = value;
		model.setMinimum(value);
	}

	public void setStep(int value) {
		step = value;
		model.setStepSize(value);
	}

	public int getValueAsInt() {
		return (int) this.getSpinnerValue();
	}

	// To make sure the model has the same value as the editor use the
	// commitEdit method, eg:
	// spinner.commitEdit();
	// Source : https://docs.oracle.com/javase/7/docs/api/javax/swing/JSpinner.html
	public double getValueAsDouble() {
		int v = (int) this.getSpinnerValue();
		return (double) v;
	}

	private Object getSpinnerValue() {
		try {
			this.commitEdit();
		} catch (ParseException ex) {
			ex.printStackTrace();
		}

		return this.getValue();
	}

	public double getValueAsDouble(int unit) {
		switch (unit) {
		case CgConst.UNIT_METER:
			return (double) getValueAsDouble();
		case CgConst.UNIT_MILES_FEET:
			return (double) Utils.Feet2Meter(getValueAsDouble());
		default:
			throw new IllegalArgumentException("The unit " + unit + " was not recognized.");
		}
	}
}
