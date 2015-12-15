package com.polymorph.soaringcoach;

import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.polymorph.soaringcoach.analysis.GNSSPoint;
import com.polymorph.soaringcoach.persistence.SoaringCoachDao;

public class TestSoaringCoachDao {

	private SoaringCoachDao dao = null;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	@Ignore
	public void testSoaringCoachDao() {
		try {
			dao  = new SoaringCoachDao();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception when creating DAO");
		}
	}

	@Test
	public void testSaveAndGetIgcBRecord() throws Exception {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		points .add(GNSSPoint.createGNSSPoint("testfile", "B1751253339800S01924950EA0450004450"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1751263359917S01917417EA0449504445"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1751273402883S02029017EA0449004430"));

		dao = new SoaringCoachDao();
		dao.saveIgcBRecord(points);		
	}
}
