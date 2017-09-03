/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *   SoaringCoach is a tool for analysing IGC files produced by modern FAI
 *   flight recorder devices, and providing the pilot with useful feedback
 *   on how effectively they are flying.    
 *   Copyright (C) 2017 Johan Pretorius
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *   The author can be contacted via email at pretoriusjf@gmail.com, or 
 *   by paper mail by addressing as follows: 
 *      Johan Pretorius 
 *      PO Box 990 
 *      Durbanville 
 *      Cape Town 
 *      7551
 *      South Africa
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package soaringcoach;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import soaringcoach.analysis.AnalysisException;
import soaringcoach.analysis.GNSSPoint;

public class FlightAnalyserTestFacade extends FlightAnalyser {
	
	public static double calculateTrackCourse(GNSSPoint p1, GNSSPoint p2) {
		return FlightAnalyser.calculateTrackCourse(p1, p2);
	}
	
	public static double calculateTrackCourse(Circle c, double lat, double lon) {
		return FlightAnalyser.calculateTrackCourse(c, lat, lon);
	}
	
	public static double calcBearingChange(double crs1, double crs2) {
		return FlightAnalyser.calcBearingChange(crs1, crs2);
	}
	
	public static Flight loadFromFile(String filename) throws AnalysisException, IOException {
		
		Flight f = new Flight();
		
		FileReader file = new FileReader(new File(filename));
		
		f = new FlightAnalyser().readIgcFileBeanIO(file, f);
		
		return f;
	}
}
