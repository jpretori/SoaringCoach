package com.polymorph.soaringcoach.cli;

import java.io.File;

import com.polymorph.soaringcoach.Circle;
import com.polymorph.soaringcoach.Flight;
import com.polymorph.soaringcoach.FlightAnalyser;
import com.polymorph.soaringcoach.FlightAnalyser.FlightMode;
import com.polymorph.soaringcoach.Thermal;
import com.polymorph.soaringcoach.analysis.AnalysisException;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class SoaringCoach {

	public static void main(String[] args) throws AnalysisException {
		 OptionParser parser = new OptionParser( "n:" );
		 parser.accepts("n").withRequiredArg().required();
		 parser.accepts("?").forHelp();
		 
		 OptionSet options = parser.parse(args);
		 
		 File f = new File((String) options.valueOf("n"));
		 
		 FlightAnalyser fa = new FlightAnalyser();
		 Flight flight = fa.addAndAnalyseFlight(f);
		 
		 System.out.println("Analysis complete.  Dumping flight parameters.");
		 System.out.println("Headline info");
		 System.out.println("=============");
		 System.out.println("Flight ID: " + flight.id);
		 System.out.println("Total Track Distance (km): " + Math.round(flight.total_track_distance)/1000.0);
		 System.out.println("Number of GNSS Fixes: " + flight.igc_points.size());
		 System.out.println("Number of circles: " + flight.circles.size());
		 System.out.println("Number of thermals: " + flight.thermals.size());
		 System.out.println();
		 System.out.println("Thermals");
		 System.out.println("========");
		 int i = 1;
		 for (Thermal t : flight.thermals) {
			 System.out.println("\tThermal #: " + i++);
			 System.out.println("\tThermal start time: " + t.circles.get(0).getTimestamp());
			 System.out.println("\tThermal duration: " + t.getTotalDuration());
			 System.out.println("\tNumber of circles: " + t.circles.size());
			 System.out.println("\tAverage circle duration: " + t.getAverageCircleDuration());
			 if (t.is_flying_erratically) {
				 System.out.println("\tPilot seems to be flying erratically!");
			 }
			 if (t.wind.size > 0) {
				 System.out.println("\tCalculated wind bearing and speed in m/s: " + Math.round(t.wind.bearing) + ", " + Math.round(t.wind.size));
			 }
			 System.out.println();
			 System.out.println("\tCircles within this thermal (durations are in seconds)");
			 System.out.println("\t---------------------------");
			 
			 FlightMode thermal_turn_direction = t.circles.get(0).turn_direction;
			 int circle_num = 1;
			 for (Circle c : t.circles) {
				 System.out.println("\t\t" + circle_num++ + ". Duration: " + c.duration);
				 if (c.correction_vector.size > 0) {
					 System.out.println("\t\t\tCentring manoevre bearing and distance in meters: " + Math.round(c.correction_vector.bearing) + ", " + Math.round(c.correction_vector.size));
				 }
				 if (c.turn_direction != thermal_turn_direction) {
					 System.out.println("\t\t\tSwitched turn direction!");
					 thermal_turn_direction = c.turn_direction;
				 }
			}
			System.out.println();
		}
	}

}
