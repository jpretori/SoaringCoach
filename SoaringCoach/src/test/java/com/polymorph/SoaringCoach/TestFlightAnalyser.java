package com.polymorph.soaringcoach;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.polymorph.soaringcoach.analysis.FlightAnalyser;
import com.polymorph.soaringcoach.analysis.GNSSPoint;

public class TestFlightAnalyser {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testFlightAnalyser() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCalcTotalDistanceFewPointsFarApart() {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0945003339755S01924837EA0450004550FAWC"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1005003359832S01917153EA0450004550Villiersdorp"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1045003402828S02028435EA0450004550FASX"));
		
		FlightAnalyser fa = new FlightAnalyser(points);
		assertEquals(148673, fa.calcTotalDistance(), 1);
	}

	@Test
	public void testCalcTotalDistanceFewPointsMediumDistance() {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905003339778S01924835EA0450003450FAWC"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0910003335963S01928400EA0450003450Quarry"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0915003341230S01925350EA0450003450Nekkies"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0920003343993S01921747EA0450003450Vic Peak"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0925003339778S01924835EA0450003450FAWC"));
		
		FlightAnalyser fa = new FlightAnalyser(points);
		assertEquals(36497, fa.calcTotalDistance(), 1);
	}


	@Test
	public void testCalcTotalDistanceManyPointsVeryClose() {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905003339778S01924835EA0450003450FAWC"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905013339779S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905023339779S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905033339780S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905043339780S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905053339781S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905063339782S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905073339782S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905083339783S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905093339784S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905103339784S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905113339785S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905123339786S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905133339787S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905143339788S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905153339789S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905163339790S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905173339790S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905183339791S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905193339792S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905203339793S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905213339793S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905223339794S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905233339794S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905243339794S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905253339794S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905263339795S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905273339796S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905283339797S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905293339798S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905303339799S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905313339800S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905323339801S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905333339802S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905343339803S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905353339804S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905363339805S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905373339806S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905383339807S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905393339808S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905403339809S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905413339810S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905423339811S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905433339812S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905443339813S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905453339814S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905463339815S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905473339816S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905483339817S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905493339818S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905503339819S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905513339820S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905523339821S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905533339822S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905543339823S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905553339824S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905563339825S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905573339826S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905583339827S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905593339828S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906003339829S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906013339830S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906023339831S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906033339832S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906043339833S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906053339834S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906063339835S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906073339836S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906083339837S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906093339838S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906103339839S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906113339840S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906123339841S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906133339842S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906143339843S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906153339844S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906163339845S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906173339846S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906183339847S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906193339848S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906203339849S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906213339850S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906223339851S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906233339852S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906243339853S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906253339854S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906263339855S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906273339856S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906283339857S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906293339858S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906303339859S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906313339860S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906323339861S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906333339862S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906343339863S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906353339864S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906363339865S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906373339866S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906383339867S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906393339868S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906403339869S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906413339870S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906423339871S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906433339872S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906443339873S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906453339874S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906463339875S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906473339876S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906483339877S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906493339878S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906503339879S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906513339880S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906523339881S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906533339882S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906543339883S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906553339884S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906563339885S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906573339886S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906583339887S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0906593339888S01924835EA0450003450ALittleNorth"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0907003339889S01924835EA0450003450ALittleNorth"));
		
		FlightAnalyser fa = new FlightAnalyser(points);
		assertEquals(206, fa.calcTotalDistance(), 1);
	}
}
