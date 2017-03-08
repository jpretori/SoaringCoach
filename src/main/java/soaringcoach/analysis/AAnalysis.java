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

package soaringcoach.analysis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import soaringcoach.Flight;

/**
 * AAnalysis defines the interface contract for analysis processes that the
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
public abstract class AAnalysis {
	protected abstract Flight performAnalysis(Flight flight) throws AnalysisException;
	
	public abstract boolean hasBeenRun(Flight flight);
	
	//TODO bin sysouts
	public final Flight analyse(Flight flight) throws AnalysisException {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		checkPreconditions(flight);
		
		if (!hasBeenRun(flight)) {
			System.out.println(
					df.format(LocalDateTime.now()) +
					" Starting analysis [" + 
					this.getClass().getSimpleName() + "]");
			
			this.performAnalysis(flight);
			
			System.out.println(
					df.format(LocalDateTime.now()) +
					" Completed analysis [" + 
					this.getClass().getSimpleName() + "]");
		} else {
			System.out.println(
					df.format(LocalDateTime.now()) +
					" Skipping analysis because it was already performed [" + 
					this.getClass().getSimpleName() + "]");
		}
		
		return flight;
	}

	/**
	 * Allows analysis subclasses to specify their own logic for checking
	 * pre-conditions, which could include checking the Flight object for
	 * existence of raw or even processed information.
	 * 
	 * <p>
	 * If any necessary pre-conditions fail, throw a new
	 * <code>PreconditionsFailedException</code> with an appropriate message.
	 * 
	 * <p>
	 * Default implementation checks that flight object is non-null.
	 * 
	 */
	protected void checkPreconditions(Flight flight) throws PreconditionsFailedException {
		if (flight == null) {
			throw new PreconditionsFailedException("Cannot perform any analysis - flight object is null");
		}
	}
}
