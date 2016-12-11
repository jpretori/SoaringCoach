package com.polymorph.soaringcoach.analysis;

import com.polymorph.soaringcoach.Flight;

/**
 * IAnalysis defines the interface contract for analysis processes that the
 * Flight gets put through. Each such implementation has as purpose to produce
 * some metric that the pilot or their coach is interested in. These probably
 * need to be run in some definite order (for example, the check twice analysis
 * can not be run if the centering analysis has not been run yet). The general
 * contract for an IAnalysis implementation is that it accepts a Flight object
 * and a File object, populates some information in the Flight object and
 * returns the newly enriched Flight object. Each implementation should make
 * sure that it hasn’t already been run, before running, as well as making sure
 * that it's preconditions are met - i.e. CheckTwiceAnalysis should check that
 * CentringAnalysis has been run, before it starts - some of them will be
 * computationally expensive.  This interface provides a method to facilitate 
 * this. 
 * 
 * @author johanpretorius
 *
 */
public interface IAnalysis {
	public Flight performAnalysis(Flight flight) throws AnalysisException;
	
	public boolean hasBeenRun(Flight flight);
}
