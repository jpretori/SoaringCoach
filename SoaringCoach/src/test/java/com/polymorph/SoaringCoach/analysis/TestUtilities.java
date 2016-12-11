package com.polymorph.soaringcoach.analysis;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestUtilities {
	
	public static void testHasBeenRun(IAnalysis a, Flight f) throws AnalysisException {
		if (f == null) {
			f = new Flight();
		}
		
		assertFalse(a.hasBeenRun(f));
		
		a.performAnalysis(f);
		
		assertTrue(a.hasBeenRun(f));
	}
}
