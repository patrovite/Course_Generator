package course_generator.weather;

public class WeatherData {
	// See here for the field descriptions :
	// https://darksky.net/dev/docs#response-format

	// DataSet NORMAL_DLY

	public WeatherData() {
	}


	public void setNormalDailyData() {

	}

	/*
	 * public long getTime() { return Time; }
	 * 
	 * 
	 * public void setTime(long time) { Time = time; }
	 * 
	 * 
	 * public void setSummary(String summary) { Summary = summary; }
	 * 
	 * 
	 * public void setIcon(String icon) { Icon = icon; }
	 * 
	 * 
	 * public String getIcon() { return Icon; }
	 * 
	 * 
	 * public String getSummary() { return Summary; }
	 * 
	 * 
	 * public double getMoonPhase() { return MoonPhase; }
	 * 
	 * 
	 * public void setMoonPhase(double moonPhase) { MoonPhase = moonPhase; }
	 * 
	 * 
	 * public double getTemperatureHigh() { return TemperatureHigh; }
	 * 
	 * 
	 * public void setTemperatureHigh(double temperatureHigh) { TemperatureHigh =
	 * temperatureHigh; }
	 * 
	 * 
	 * public long getTemperatureHighTime() { return TemperatureHighTime; }
	 * 
	 * 
	 * public void setTemperatureHighTime(long temperatureHighTime) {
	 * TemperatureHighTime = temperatureHighTime; }
	 * 
	 * 
	 * public double getTemperatureLow() { return TemperatureLow; }
	 * 
	 * 
	 * public void setTemperatureLow(double temperatureLow) { TemperatureLow =
	 * temperatureLow; }
	 * 
	 * 
	 * public long getTemperatureLowTime() { return TemperatureLowTime; }
	 * 
	 * 
	 * public void setTemperatureLowTime(long temperatureLowTime) {
	 * TemperatureLowTime = temperatureLowTime; }
	 * 
	 * 
	 * public double getApparentTemperatureHigh() { return ApparentTemperatureHigh;
	 * }
	 * 
	 * 
	 * public void setApparentTemperatureHigh(double apparentTemperatureHigh) {
	 * ApparentTemperatureHigh = apparentTemperatureHigh; }
	 * 
	 * 
	 * public long getApparentTemperatureHighTime() { return
	 * ApparentTemperatureHighTime; }
	 * 
	 * 
	 * public void setApparentTemperatureHighTime(long apparentTemperatureHighTime)
	 * { ApparentTemperatureHighTime = apparentTemperatureHighTime; }
	 * 
	 * 
	 * public double getApparentTemperatureLow() { return ApparentTemperatureLow; }
	 * 
	 * 
	 * public void setApparentTemperatureLow(double apparentTemperatureLow) {
	 * ApparentTemperatureLow = apparentTemperatureLow; }
	 * 
	 * 
	 * public long getApparentTemperatureLowTime() { return
	 * ApparentTemperatureLowTime; }
	 * 
	 * 
	 * public void setApparentTemperatureLowTime(long apparentTemperatureLowTime) {
	 * ApparentTemperatureLowTime = apparentTemperatureLowTime; }
	 * 
	 * 
	 * public double getWindSpeed() { return WindSpeed; }
	 * 
	 * 
	 * public void setWindSpeed(double windSpeed) { WindSpeed = windSpeed; }
	 * 
	 * 
	 * public String getPrecipType() { return PrecipType; }
	 * 
	 * 
	 * public void setPrecipType(String precipType) { PrecipType = precipType; }
	 * 
	 * 
	 * public String getSummaryIconFilePath() { if (Icon == null || Icon.length() ==
	 * 0) return "";
	 * 
	 * String iconFileName = ""; switch (Icon.toLowerCase()) { case "clear-day":
	 * iconFileName = "Sun"; break; case "clear-night": iconFileName = "Moon";
	 * break; case "rain": iconFileName = "Cloud-Rain"; break; case "snow": case
	 * "sleet": iconFileName = "Cloud-Snow"; break; case "wind": iconFileName =
	 * "Wind"; break; case "fog": iconFileName = "Cloud-Fog"; break; case "cloudy":
	 * iconFileName = "Cloud"; break; case "partly-cloudy-day": iconFileName =
	 * "Cloud-Sun"; break; case "partly-cloudy-night": iconFileName = "Cloud-Moon";
	 * break; default: return ""; }
	 * 
	 * return getFilePathFromFileName(iconFileName + ".png"); }
	 * 
	 */

	/**
	 * Retrieves the appropriate icon name given a temperature value.
	 * 
	 * @param temperatureValue
	 *            A temperature value in Fahrenheit.
	 * 
	 * @return The icon file name.
	 */
	/*
	 * public String getThermometerIconFilePath(String temperatureValue) { String
	 * iconFileName = ""; int temperature = Integer.valueOf(temperatureValue);
	 * 
	 * if (temperature < 25) { iconFileName = "Thermometer-Zero"; } else if
	 * (temperature >= 25) { iconFileName = "Thermometer-25"; } else if (temperature
	 * >= 50) { iconFileName = "Thermometer-50"; } else if (temperature >= 75) {
	 * iconFileName = "Thermometer-75"; } else if (temperature >= 100) {
	 * iconFileName = "Thermometer-100"; }
	 * 
	 * return getFilePathFromFileName(iconFileName + ".png"); }
	 * 
	 * 
	 * public String getPrecipitationTypeIconFilePath() { String iconFileName = "";
	 * switch (PrecipType.toLowerCase()) { case "rain": iconFileName = "Cloud-Rain";
	 * break; case "snow": case "sleet": iconFileName = "Cloud-Snow"; break;
	 * default: return ""; }
	 * 
	 * return getFilePathFromFileName(iconFileName + ".png"); }
	 * 
	 * 
	 * private String getFilePathFromFileName(String fileName) { Path filePath =
	 * null; try { filePath =
	 * Paths.get(getClass().getResource("/course_generator/images/climacons/PNG/" +
	 * fileName).toURI()); } catch (URISyntaxException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } return filePath.toString(); }
	 */
}
