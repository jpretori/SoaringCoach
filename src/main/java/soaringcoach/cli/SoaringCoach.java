package soaringcoach.cli;

import java.io.File;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import soaringcoach.Circle;
import soaringcoach.Flight;
import soaringcoach.FlightAnalyser;
import soaringcoach.StraightPhase;
import soaringcoach.Thermal;
import soaringcoach.FlightAnalyser.FlightMode;
import soaringcoach.analysis.AnalysisException;

public class SoaringCoach {

	public static void main(String[] args) throws AnalysisException {
		 OptionParser parser = new OptionParser( "n:" );
		 parser.accepts("n").withRequiredArg().required();
		 parser.accepts("?").forHelp();
		 
		 OptionSet options = parser.parse(args);
		 
		 File f = new File((String) options.valueOf("n"));
		 
		 FlightAnalyser fa = new FlightAnalyser();
		 Flight flight = fa.addAndAnalyseFlight(f);
		 
		 System.out.println();
		 System.out.println("Analysis complete.  Dumping flight parameters.");
		 System.out.println("Headline info");
		 System.out.println("=============");
		 System.out.println("Flight ID: " + flight.id);
		 System.out.println("Pilot Name: " + flight.pilot_name);
		 System.out.println("Total Track Distance (km): " + Math.round(flight.total_track_distance)/1000.0);
		 System.out.println("Total Flight Duration (hours): " + (Math.round((flight.getDuration()/3600)*10))/10.0);
		 System.out.println("Number of GNSS Fixes: " + flight.igc_points.size());
		 System.out.println("Number of circles: " + flight.circles.size());
		 System.out.println("Number of thermals: " + flight.thermals.size());
		 System.out.println("Number of straight phases: " + flight.straight_phases.size());
		 System.out.println();
		 
		 System.out.println();
		 
		 System.out.println("Straight Phases");
		 System.out.println("===============");
		 System.out.println("Distances are in km");
		 int i = 1;
		 flight.straight_phases.sort(null);
		 for (StraightPhase s : flight.straight_phases) {
			System.out.println("\t" + i++ + ". Start: " + s.start_point.getTimestamp() + ", Distance: " + Math.round(s.distance)/1000.0);
		 }
		 
		 System.out.println();
		 System.out.println("Thermals");
		 System.out.println("========");
		 i = 1;
		 for (Thermal t : flight.thermals) {
			 System.out.println("\tThermal #: " + i++);
			 System.out.println("\tStart time: " + t.circles.get(0).getTimestamp());
			 System.out.println("\tDuration: " + t.getTotalDuration());
			 System.out.println("\tNumber of circles: " + t.circles.size());
			 System.out.println("\tAverage circle duration: " + t.getAverageCircleDuration());
			 System.out.println("\tTurn direction: " + t.circles.get(0).turn_direction);
			 System.out.print("\tCalculated wind (bearing, speed in km/h): ");
			 if (t.could_not_calculate_wind) {
				 System.out.println("Too few circles to calculate");
			 } else {
				 System.out.println(Math.round(t.wind.bearing) + ", " + Math.round(t.wind.size * 3.6));
			 }
			 System.out.println();
			 System.out.println("\tCircles within this thermal (durations are in seconds)");
			 System.out.println("\t---------------------------");
			 
			 FlightMode thermal_turn_direction = t.circles.get(0).turn_direction;
			 int circle_num = 1;
			 for (Circle c : t.circles) {
				 System.out.println("\t\t" + circle_num++ + ". Duration: " + c.duration);
				 if (c.drift_vector.size > 0) {
					 System.out.println("\t\t\tDrift vector (bearing, distance in m): " + Math.round(c.drift_vector.bearing) + ", " + Math.round(c.drift_vector.size));
				 }
				 if (c.correction_vector.size > 0) {
					 System.out.println("\t\t\tCentring vector (bearing, distance in m): " + Math.round(c.correction_vector.bearing) + ", " + Math.round(c.correction_vector.size));
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
