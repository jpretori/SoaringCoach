package soaringcoach.analysis;

import soaringcoach.analysis.parsing.GNSSPointData;

public class GNSSPointFacade extends GNSSPoint {
	public GNSSPointFacade() {super();}
	
	public static boolean isValidGpsFix(GNSSPointData pt) {
		return GNSSPoint.isValidGpsFix(pt);
	}
	
	public static Double decimalizeDegrees(
			String degrees, 
			String decimalized_minutes, 
			String coordinate_ref) {
		return GNSSPoint.decimalizeDegrees(degrees, decimalized_minutes, coordinate_ref);
	}
	
	public static boolean isValidBRecord(String file_input) {
		return GNSSPoint.isValidBRecord(file_input);
	}
}
