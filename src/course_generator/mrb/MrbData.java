package course_generator.mrb;

import org.joda.time.DateTime;

import course_generator.CgData;
import course_generator.utils.CgConst;
import course_generator.utils.Utils;

public class MrbData extends CgData {
	/** Distance from previous point **/
	private double deltadistance;
	/** Time from previous point **/
	private int deltatime;


	public MrbData(double Num, double Latitude, double Longitude, double Elevation, double ElevationMemo, int Tag,
			double Dist, double Total, double Diff, double Coeff, double Recup, double Slope, double Speed,
			double dElevation, int Time, // Time in second
			double dTime_f, // Temps de parcours du tronÃ§on en seconde (avec
							// virgule)
			int TimeLimit, // Barrière horaire
			DateTime Hour, // Contient la date et l'heure de passage
			int Station, String Name, String Comment, double tmp1, double tmp2, String FmtLbMiniRoadbook,
			int OptionMiniRoadbook, int VPosMiniRoadbook, String CommentMiniRoadbook, int FontSizeMiniRoadbook,
			double deltadistance, int deltatime) {
		super(Num, Latitude, Longitude, Elevation, ElevationMemo, Tag, Dist, Total, Diff, Coeff, Recup, Slope, Speed,
				dElevation, Time, // Time in second
				dTime_f, // Temps de parcours du tronÃ§on en seconde (avec
							// virgule)
				TimeLimit, // Barrière horaire
				Hour, // Contient la date et l'heure de passage
				Station, Name, Comment, tmp1, tmp2, FmtLbMiniRoadbook, OptionMiniRoadbook, VPosMiniRoadbook,
				CommentMiniRoadbook, FontSizeMiniRoadbook);

		this.deltadistance = deltadistance;
		this.deltatime = deltatime;
	}


	public double getDeltaDist() {
		return deltadistance;
	}


	public double getDeltaDist(int unit) {
		switch (unit) {
			case CgConst.UNIT_METER:
				return deltadistance;
			case CgConst.UNIT_MILES_FEET:
				// meter to miles
				return Utils.Meter2uMiles(deltadistance);
			default:
				return deltadistance;
		}
	}


	public String getDeltaDistString(int unit, boolean withunit) {

		Double d = getDeltaDist(unit)/1000.0;

		String s = "";

		// -- Set the value
		switch (unit) {
			case CgConst.UNIT_METER:
				s = String.format("%1.3f ", d);
				if (withunit)
					s = s + "m";
				break;
			case CgConst.UNIT_MILES_FEET:
				s = String.format("%1.3f ", d);
				if (withunit)
					s = s + "miles";
				break;
			default:
				s = String.format("%1.3f ", d);
				if (withunit)
					s = s + "m";
				break;
		}
		return s;
	}


	public void setDeltaDist(double dist) {
		deltadistance = dist;
	}

	
	public int getDeltaTime() {
		return deltatime;
	}

	public String getDeltaTimeString() {
	 int time = getDeltaTime();

     //-- Set the value
     int nbh = time / 3600;
     int nbm = (time % 3600) / 60;
     int nbs = (time % 3600) % 60;
     return String.format("%02d:%02d:%02d ",nbh,nbm,nbs);
	}

	public void setDeltaTime(int deltatime) {
		this.deltatime=deltatime;
	}

}
