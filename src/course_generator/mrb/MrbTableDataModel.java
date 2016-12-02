package course_generator.mrb;

import javax.swing.table.AbstractTableModel;

import course_generator.settings.CgSettings;

public class MrbTableDataModel extends AbstractTableModel {
	private java.util.ResourceBundle bundle;
	private MrbDataList list;
	private CgSettings settings;
	
	private final String[] header;	

	/**
	 * Constructor
	 */
	public MrbTableDataModel(MrbDataList p, CgSettings s) {
		list=p;
		settings=s;
		bundle = java.util.ResourceBundle.getBundle("course_generator/Bundle");
		header = new String[11];
		header[0]=bundle.getString("MrbDataModel.name");
		header[1]=bundle.getString("MrbDataModel.dist");
		header[2]=bundle.getString("MrbDataModel.deltadist");
		header[3]=bundle.getString("MrbDataModel.elevation");
		header[4]=bundle.getString("MrbDataModel.time");
		header[5]=bundle.getString("MrbDataModel.deltatime");
		header[6]=bundle.getString("MrbDataModel.hour");
		header[7]=bundle.getString("MrbDataModel.timelimit");
		header[8]=bundle.getString("MrbDataModel.stationtime");
		header[9]=bundle.getString("MrbDataModel.comment");
		header[10]=bundle.getString("MrbDataModel.line");
	}

	public void setData(MrbDataList p) {
		list=p;
	}

	
	
	@Override
	public int getColumnCount() {
		return header.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return header[columnIndex];
	}
	
	
	@Override
	public int getRowCount() {
		return list.data.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {

		case 0:
			//name
			return list.data.get(rowIndex).getName();
		case 1:
			//dist
			return list.data.get(rowIndex).getTotalString(settings.Unit, false);
		case 2:
			//deltadist
			return list.data.get(rowIndex).getDeltaDistString(settings.Unit, false);
		case 3:
			//elevation
			return list.data.get(rowIndex).getElevationString(settings.Unit, false);
		case 4:
			//time
			return list.data.get(rowIndex).getTimeString();
		case 5:
			//deltatime
			return list.data.get(rowIndex).getDeltaTimeString();
		case 6:
			//hour
			return list.data.get(rowIndex).getHourString();
		case 7:
			//timelimit
			return list.data.get(rowIndex).getTimeLimitString(true);
		case 8:
			//stationtime
			return list.data.get(rowIndex).getStationString(true);
		case 9:
			//comment
			return list.data.get(rowIndex).getComment();
		case 10:
			//line
			return list.data.get(rowIndex).getNumString();
						
		default:
			throw new IllegalArgumentException();
		}
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
				return String.class;
			default:
				return Object.class;
		}
	}
}
