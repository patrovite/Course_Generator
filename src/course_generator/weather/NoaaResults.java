package course_generator.weather;

/**
 * A Java representation of a NOAA query "results" element.
 * 
 * @author Frederic Bard
 * 
 */
public class NoaaResults {
	private String station;

	private String value;

	private String attributes;

	private String datatype;

	private String date;


	public String getStation() {
		return station;
	}


	public void setStation(String station) {
		this.station = station;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public String getAttributes() {
		return attributes;
	}


	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}


	public String getDatatype() {
		return datatype;
	}


	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}

}