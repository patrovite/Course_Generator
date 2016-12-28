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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class JHourSetting extends JPanel{
	private int hour;
	private int minute;
	private int second;
	
	private CgSpinner spinHour;
	private CgSpinner spinMinute;
	private CgSpinner spinSecond;
	private JButton btCancel;

	public JHourSetting() {
		super();
		hour=0;
		minute=0;
		second=0;
		
		setLayout(new GridBagLayout());
		
		spinHour = new CgSpinner(0,0,23,1);
		Utils.addComponent(this, spinHour, 
				0, 0, 
				1, 1, 
				0, 0, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		spinMinute = new CgSpinner(0,0,59,1);
		Utils.addComponent(this, spinMinute, 
				1, 0, 
				1, 1, 
				0, 0, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		spinSecond = new CgSpinner(0,0,59,1);
		Utils.addComponent(this, spinSecond, 
				2, 0, 
				1, 1, 
				0, 0, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.BOTH);
		btCancel = new JButton("0");
		btCancel.setToolTipText("0h00m00s"); 
		btCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				hour=0;
				minute=0;
				second=0;
				Refresh();
			}
		});
		Utils.addComponent(this, btCancel, 
				3, 0, 
				1, 1, 
				1, 0, 
				0, 0, 0, 0, 
				GridBagConstraints.BASELINE_LEADING, GridBagConstraints.NONE);	
	}
	
	protected void Refresh() {
		spinHour.setValue(hour);
		spinMinute.setValue(minute);
		spinSecond.setValue(second);
	}

	public int getHour() {
		hour=(int) spinHour.getValue();
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
		Refresh();
	}

	public int getMinute() {
		minute=(int) spinMinute.getValue();
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
		Refresh();
	}

	public int getSecond() {
		second=(int) spinSecond.getValue();
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
		Refresh();
	}
	
	public void setHMS(int hour, int minute, int second) {
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		Refresh();
	}

	public void setHMSinSecond(int HMSinSecond) {
		this.hour = HMSinSecond / 3600;
		this.minute = (HMSinSecond % 3600) / 60;
		this.second = (HMSinSecond % 3600) % 60;
		Refresh();
	}

	public int getHMSinSecond() {
		getHour();
		getMinute();
		getSecond();
		return hour*3600+minute*60+second;
	}
	
}
