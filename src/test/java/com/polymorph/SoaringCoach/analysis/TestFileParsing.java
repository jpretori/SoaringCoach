package com.polymorph.soaringcoach.analysis;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.junit.Test;

import com.polymorph.soaringcoach.analysis.parsing.GNSSPointData;
import com.polymorph.soaringcoach.analysis.parsing.PICName;

public class TestFileParsing {

	/**
	 * Tests that we can identify record types we're interested in, without breaking
	 */
	@Test
	public void testIdentifyingRecords() {
		StreamFactory factory = StreamFactory.newInstance();
		
		factory.load("src/main/java/com/polymorph/soaringcoach/analysis/igc_mapping.xml");

		BeanReader br = factory.createReader("igc_file", new File("src/test/resources/5c6c3ke1.igc"));;
		
		boolean found_b_records = false;
		
		try {
			Object bean = null;
			while ((bean = br.read()) != null) {
				if (bean instanceof GNSSPointData) {
					found_b_records = true;
				}
			}
			
		} finally {
			br.close();
		}
		
		assertEquals(true, found_b_records);
	}

	/**
	 * Tests that we can parse a B record accurately
	 * @throws ParseException 
	 */
	@Test
	public void testBRecordParsing() throws ParseException {
		StreamFactory factory = StreamFactory.newInstance();
		
		factory.load("src/main/java/com/polymorph/soaringcoach/analysis/igc_mapping.xml");

		BeanReader br = factory.createReader("igc_file", new File("src/test/resources/minimal_parsable.igc"));;

		ArrayList<GNSSPoint> points = new ArrayList<>(); 
		try {
			Object bean;
			GNSSPointData pt_data = null;
			while ((bean = br.read()) != null) {
				if (bean instanceof GNSSPointData) {
					pt_data = (GNSSPointData) bean;
					GNSSPoint pt = GNSSPoint.createGNSSPoint(pt_data);
					points.add(pt);
				}
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

	/**
	 * Test header record parsing by checking that the pilot name comes through correctly
	 */
	@Test
	public void testHeaderRecordParsing() {
		StreamFactory factory = StreamFactory.newInstance();
		
		factory.load("src/main/java/com/polymorph/soaringcoach/analysis/igc_mapping.xml");

		BeanReader br = factory.createReader("igc_file", new File("src/test/resources/minimal_parsable.igc"));;

		try {
			Object bean = br.read();
			PICName pilot = (PICName) bean;
			assertEquals("Kevin Mitchell", pilot.picName);
		} finally {
			br.close();
		}
		
		
	}
}
