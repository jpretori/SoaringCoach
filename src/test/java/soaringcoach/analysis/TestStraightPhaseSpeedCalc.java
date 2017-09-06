package soaringcoach.analysis;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import soaringcoach.StraightPhase;

public class TestStraightPhaseSpeedCalc {

	@Test
	public void testCalculateSpeeds() {
		//Setup
		GNSSPoint start = new GNSSPointFacade();
		GNSSPoint end = new GNSSPointFacade();
		
		start.data.timestamp = new Date();
		end.data.timestamp = new Date(start.data.timestamp.getTime() + 60*1000); // add 60 seconds
		
		StraightPhase s = new StraightPhase(start, end);
		s.distance = 1432;
		
		//Calculate
		ArrayList<StraightPhase> phases = new ArrayList<>();
		phases.add(s);
		phases = new StraightPhaseAnalysisFacade().calculateSpeeds(phases);
		s = phases.get(0);
		
		//Test
		assertEquals(23.87, s.groundSpeed, 0.1);
	}

}
