package soaringcoach;

/**
 * Provides basic information about a flight. Allows for efficiently
 * transporting a list of flights to a UI, such that one can be selected and
 * more detail can be requested.
 * 
 * @author johanpretorius
 *
 */
public class FlightSummary {
	/**
	 * Uniquely identifies the flight
	 */
	public long id = 0;
	
	/**
	 * Total track distance over ground
	 */
	public double total_track_distance = 0;
	
	
	protected FlightSummary(long id, double total_track_distance) {
		this.id = id;
		this.total_track_distance = total_track_distance;
	}
}
