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

package soaringcoach.analysis.parsing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FlightDate {
	public String recordType;
	public String ddmmyy;
	
	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	public String getddmmyy() {
		return ddmmyy;
	}
	public void setddmmyy(String text) {
		if (text != null) {
			try {
				this.ddmmyy = text.substring(text.indexOf(":") + 1);
			} catch (IndexOutOfBoundsException e) {
				this.ddmmyy = "";
			}
		} else {
			this.ddmmyy = text;
		}
	}
	
	public String getFlightDateString() throws ParseException {
		SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat parse = new SimpleDateFormat("ddMMyy");
		Date date = parse.parse(ddmmyy);
		return output.format(date);
	}
}
