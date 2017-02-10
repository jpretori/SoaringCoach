package soaringcoach;

import soaringcoach.analysis.GNSSPoint;

public class StraightPhase implements Comparable<StraightPhase>{
	public GNSSPoint start_point;
	public GNSSPoint end_point;
	public double distance;
	
	public StraightPhase(GNSSPoint start, GNSSPoint end) {
		distance = end.distance(start);
		this.end_point = end;
		this.start_point = start;
	}

	@Override
	public int compareTo(StraightPhase o) {
		if (distance < o.distance) {
			return -1;
		}
		
		if (distance > o.distance) {
			return 1;
		}
		
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof StraightPhase)) {return super.equals(obj);}
		
		return new Double(distance).equals(new Double(((StraightPhase) obj).distance));
	}
	
}
