package soaringcoach.analysis;

/**
 * Represents a vector in polar co-ordinates (direction in degrees as per a
 * compass rose, and size). The size can be in any unit - meters, meters per
 * second, or whatever is required. Care should be taken when comparing
 * instances of this class to ensure that the same units are in use.
 * 
 * @author johanpretorius
 *
 */
public class PolarVector {
	public double bearing = 0;
	public double size = 0;
	
	public PolarVector(double bearing, double size) {
		this.bearing = bearing;
		this.size = size;
	}
}
