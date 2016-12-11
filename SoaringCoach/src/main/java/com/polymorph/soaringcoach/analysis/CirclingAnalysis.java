package com.polymorph.soaringcoach.analysis;

import java.util.ArrayList;

import com.polymorph.soaringcoach.analysis.FlightAnalyser.FlightMode;

/**
 * makes an array of Circles in the flight, marks each as LH or RH, calculates each’s duration, etc
 * 
 * @author johanpretorius
 *
 */
public class CirclingAnalysis implements IAnalysis {
	// TURN_RATE_THRESHOLD is in degrees per second.  Turning faster than this constitutes making a thermal turn.
	private final int TURN_RATE_THRESHOLD = 4; 

	@Override
	public Flight performAnalysis(Flight flight) throws AnalysisException {
		
		if (flight.igc_points == null) {
			//null points array probably means the Flight has not been initialised properly for some reason.
			throw new AnalysisException("no GPS fixes - Flight object is not initialized properly");
		}
		
		ArrayList<Circle> circles = new ArrayList<Circle>();
		
		GNSSPoint p1 = null;

		FlightMode mode = FlightMode.CRUISING;
		Circle circle = null;
		
		for (GNSSPoint p2 : flight.igc_points) {
			
			if (p1 != null && p2 != null) {
				p2.track_course_deg = FlightAnalyser.calculateTrackCourse(p1, p2);
				
				p2.resolve(p1);
				
				switch (mode) {
					case CRUISING:
						//Detect mode change
						if (p2.turn_rate > TURN_RATE_THRESHOLD) {
							mode = FlightMode.TURNING_RIGHT;
							circle = new Circle(p1, p2, mode);
						} else if (p2.turn_rate < -TURN_RATE_THRESHOLD) {
							mode = FlightMode.TURNING_LEFT;
							circle = new Circle(p1, p2, mode);
						}
						break;
					case TURNING_LEFT: 
						//Detect mode change
						if (Math.abs(p2.turn_rate) < TURN_RATE_THRESHOLD) {
							//Turning too slowly to still call this a thermal turn
							mode = FlightMode.CRUISING;
							circle = null;
						} else if (p2.turn_rate > TURN_RATE_THRESHOLD) {
							//Switched turn direction
							mode = FlightMode.TURNING_RIGHT;
							circle = new Circle(p1, p2, mode);
						} else {
							//Detect going past turn start course.  Only relevant if we're still turning.
							circle.detectCircleCompleted(p2);
							
							if (circle.circle_completed) {
								// This means we've just passed the course on which the turn started.
								// So a full circle is accomplished!
								
								circle.setDuration(p2);
								circles.add(circle);
								circle = new Circle(p1, p2, circle); 
							}
						}
						break;
					case TURNING_RIGHT:
						//Detect mode change
						if (Math.abs(p2.turn_rate) < TURN_RATE_THRESHOLD) {
							//Turning too slowly to still call this a thermal turn
							mode = FlightMode.CRUISING;
							circle = null;
						} else if (p2.turn_rate < -TURN_RATE_THRESHOLD) {
							//Switched turn direction
							mode = FlightMode.TURNING_LEFT;
							circle = new Circle(p1, p2, mode);
						} else {
							//Detect going past turn start course.  Only relevant if we're still turning.
							circle.detectCircleCompleted(p2);
							
							if (circle.circle_completed) {
								// This means we've just passed the course on which the turn started.
								// So a full circle is accomplished!
	
								circle.setDuration(p2);
								circles.add(circle);
								circle = new Circle(p1, p2, circle); 
							}
						}
						break;
					default: throw new AnalysisException("Unexpected flight mode indicator [" + mode + "]");
				}
			}
			
			//transfer the pointer so we scan through the list looking at 2 adjacent points all the time
			p1 = p2; 
		}
		
		flight.circles = circles;
		
		flight.is_circling_analysis_complete = true;
		return flight;
	}

	@Override
	public boolean hasBeenRun(Flight flight) {
		return flight.is_circling_analysis_complete;
	}

}
