package com.polymorph.soaringcoach.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.junit.Test;

import com.polymorph.soaringcoach.analysis.parsing.GNSSPointData;

public class TestFileParsing {

	/**
	 * Tests that we can identify B records, and throw
	 * away everything else (H, L, etc... type records)
	 */
	@Test
	public void testIdentifyingRecords() {
		StreamFactory factory = StreamFactory.newInstance();
		
		factory.load("src/main/java/com/polymorph/soaringcoach/analysis/igc_mapping.xml");

		BeanReader br = factory.createReader("igc_file", new File("src/test/resources/5c6c3ke1.igc"));;

		try {
			GNSSPointData pt_data = null;
			while ((pt_data = (GNSSPointData) br.read()) != null) {
				GNSSPoint pt = GNSSPoint.createGNSSPoint(pt_data);
				
				assertNotNull(pt);
			}
			
			
		} finally {
			br.close();
		}
	}

	/**
	 * Tests that we can parse a B record accurately
	 * @throws ParseException 
	 */
	@Test
	public void testParsing() throws ParseException {
		StreamFactory factory = StreamFactory.newInstance();
		
		factory.load("src/main/java/com/polymorph/soaringcoach/analysis/igc_mapping.xml");

		BeanReader br = factory.createReader("igc_file", new File("src/test/resources/small_valid.igc"));;

		// load the mapping file
		ArrayList<GNSSPoint> points = new ArrayList<>(); 
		try {
			GNSSPointData pt_data = null;
			while ((pt_data = (GNSSPointData) br.read()) != null) {
				GNSSPoint pt = GNSSPoint.createGNSSPoint(pt_data);
				points.add(pt);
			}
		} finally {
			br.close();
		}
		
		assertEquals(1, points.size());
		
		GNSSPoint pt = points.get(0);
		
		SimpleDateFormat df = new SimpleDateFormat("HHmmss");
		Date d = df.parse("103956");
		assertEquals(d.getTime(), pt.data.getTimestamp().getTime());
		
		assertEquals(50.763617, pt.getLatitude(), 0.00001);
		
		assertEquals(3.8683, pt.getLongitude(), 0.00001);
		
		assertEquals("A", pt.getAltitudeOK());
		
		assertEquals(336, pt.getPressureAltitude());
		
		assertEquals(360, pt.getGnssAltitude());
	}

}
