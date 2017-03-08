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

package soaringcoach;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.Before;

import soaringcoach.analysis.GNSSPoint;
import soaringcoach.persistence.DaoFactory;
import soaringcoach.persistence.SoaringCoachDao;

public class TestSoaringCoachDao {

	private SoaringCoachDao dao = null;

	@Before
	public void setUp() throws Exception {
	}

	//@Test
	public void testSoaringCoachDao() {
		try {
			Connection connection = DaoFactory.getConnection();
			dao  = new SoaringCoachDao(connection);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception when creating DAO");
		}
	}

	//@Test
	public void testSaveAndGetIgcBRecord() throws Exception {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		UUID filename_uuid = UUID.randomUUID();
		String filename = filename_uuid.toString();
		
		points.add(GNSSPoint.createGNSSPoint(filename, "B1751253339800S01924950EA0450004450TEST ENTRY FROM JUNIT"));
		points.add(GNSSPoint.createGNSSPoint(filename, "B1751263359917S01917417EA0449504445TEST ENTRY FROM JUNIT"));
		points.add(GNSSPoint.createGNSSPoint(filename, "B1751273402883S02029017EA0449004430TEST ENTRY FROM JUNIT"));
		
		dao = new SoaringCoachDao(DaoFactory.getConnection());
		dao.saveIgcBRecord(points);
		
		ArrayList<GNSSPoint> loaded_points = dao.getIgcBRecords(filename);
		
		//Did we get enough (or more) records
		assertEquals(
				"Not enough records loaded from DB, expected [3] points", 3, loaded_points.size());
				
		//Do the last fields of the individual records loaded, match with what we saved 
		for (int i = 0; i < loaded_points.size(); i++) {
			GNSSPoint db_point = loaded_points.get(i);
			GNSSPoint test_point = points.get(i);
			
			assertEquals(
					"GnssPoint at index ["+i+"] of file ["+filename+"] - Filename", 
					test_point.getFilename(), 
					db_point.getFilename());
			assertEquals(
					"GnssPoint at index ["+i+"] of file ["+filename+"] - AltitudeOK Flag", 
					test_point.getAltitudeOK(), 
					db_point.getAltitudeOK());
			assertEquals(
					"GnssPoint at index ["+i+"] of file ["+filename+"] - GNSS Altitude", 
					test_point.getGnssAltitude(), 
					db_point.getGnssAltitude());
			assertEquals(
					"GnssPoint at index ["+i+"] of file ["+filename+"] - Latitude", 
					test_point.getLatitude(), 
					db_point.getLatitude(), 0.0001);
			assertEquals(
					"GnssPoint at index ["+i+"] of file ["+filename+"] - Longitude", 
					test_point.getLongitude(), 
					db_point.getLongitude(), 0.0001);
			assertEquals(
					"GnssPoint at index ["+i+"] of file ["+filename+"] - Other", 
					test_point.getOther(), 
					db_point.getOther());
			assertEquals(
					"GnssPoint at index ["+i+"] of file ["+filename+"] - Pressure Altitude", 
					test_point.getPressureAltitude(), 
					db_point.getPressureAltitude());
			assertEquals(
					"GnssPoint at index ["+i+"] of file ["+filename+"] - Timestamp", 
					test_point.getTimestamp(), 
					db_point.getTimestamp());
		}
	}
}
