package com.polymorph.SoaringCoach;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.polymorph.soaringcoach.SoaringCoach;
import com.polymorph.soaringcoach.persistence.SoaringCoachDao;

public class TestSoaringCoachDao {

	private SoaringCoachDao dao = null;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSoaringCoachDao() {
		try {
			dao  = new SoaringCoachDao();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception when creating DAO");
		}
	}

	@Test
	public void testSaveIgcBRecord() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetIgcBRecords() {
		//fail("Not yet implemented");
	}

}
