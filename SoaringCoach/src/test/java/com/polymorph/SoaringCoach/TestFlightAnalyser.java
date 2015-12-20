package com.polymorph.soaringcoach;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.polymorph.soaringcoach.analysis.FlightAnalyser;
import com.polymorph.soaringcoach.analysis.FlightAnalyserTestFacade;
import com.polymorph.soaringcoach.analysis.GNSSPoint;
import com.polymorph.soaringcoach.analysis.TurnData;
import com.polymorph.soaringcoach.analysis.Turns;

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
	
	/**
	 * Test with a set of points that make up an S turn, without ever quite completing a circle 
	 * @throws Exception
	 */
	@Test
	public void testCirclingDetectionDiscard() throws Exception {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109303308755S01911128EA016190171900308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109343308702S01911090EA016310173200309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109383308653S01911048EA016440174100308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109423308633S01910983EA016480174500309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109463308656S01910920EA016440174200309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109503308709S01910895EA016450174400309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109543308763S01910919EA016540175200309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109583308800S01910973EA016570175800309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110023308849S01911007EA016660176900309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110063308901S01910995EA016840178700308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110103308929S01910941EA016900179400309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110143308914S01910879EA016920179500309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110183308865S01910851EA016910179300309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110223308810S01910877EA016860178800308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110263308782S01910943EA016860178700307"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110303308788S01911014EA016880178900309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110343308821S01911072EA016930179500309"));		
		FlightAnalyser fa = new FlightAnalyser(points);
		
		Turns turns = fa.calcTurnRates();
		
		assertEquals("incorrect number of turns detected", 0, turns.getTurns().size());
	}
	
	/**
	 * This set of fixes includes: <br>
	 * - an S-turn (i.e. half a circle left, immediately followed by half a
	 * circle right)<br>
	 * - four separate full circles strung together, all in the same direction
	 * <br>
	 * - part of a circle at the end of the series<br>
	 * Having several circles immediately following each other, also tests the
	 * modulus arithmetic around 360/0 degrees.
	 * @throws Exception 
	 */
	@Test
	public void testCirclingDetectionPositive() throws Exception {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		//Set of points that have a nearly completed thermal turn - it should not be detected as 
		//a turn because the turn was never finished.
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1106503311172S01912222EA014460154100308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1106543311093S01912262EA014460154100308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1106583311014S01912282EA014460154100308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1107023310935S01912292EA014460154100308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1107063310856S01912276EA014480154200308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1107103310776S01912261EA014530154900308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1107143310704S01912256EA014590155800308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1107183310635S01912243EA014620155900308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1107223310570S01912222EA014640156200308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1107263310510S01912194EA014680156400308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1107303310451S01912160EA014690156400308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1107343310388S01912129EA014720156800308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1107383310323S01912103EA014770157500308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1107423310260S01912074EA014870158300308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1107463310198S01912042EA014950159100308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1107503310136S01912009EA015030160000308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1107543310074S01911986EA015110160700309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1107583310006S01911969EA015110160800308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1108023309938S01911954EA015170161200309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1108063309873S01911927EA015210161900309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1108103309808S01911901EA015330163300309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1108143309745S01911883EA015480164600307"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1108183309692S01911848EA015520164900309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1108223309640S01911809EA015500164700309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1108263309592S01911762EA015480164600309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1108303309543S01911716EA015560165300309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1108343309487S01911682EA015680166600309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1108383309423S01911665EA015690166500309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1108423309355S01911661EA015690166500309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1108463309289S01911645EA015810167600309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1108503309232S01911615EA015870168600309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1108543309185S01911574EA015790167500308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1108583309136S01911529EA015780167600308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109023309084S01911486EA015780167600309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109063309033S01911435EA015740166900308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109103308983S01911378EA015790167700308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109143308935S01911325EA015900168900308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109183308891S01911274EA016010169800307"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109223308847S01911223EA016060170300307"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109263308802S01911174EA016130171100308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109303308755S01911128EA016190171900308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109343308702S01911090EA016310173200309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109383308653S01911048EA016440174100308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109423308633S01910983EA016480174500309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109463308656S01910920EA016440174200309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109503308709S01910895EA016450174400309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109543308763S01910919EA016540175200309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109583308800S01910973EA016570175800309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110023308849S01911007EA016660176900309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110063308901S01910995EA016840178700308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110103308929S01910941EA016900179400309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110143308914S01910879EA016920179500309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110183308865S01910851EA016910179300309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110223308810S01910877EA016860178800308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110263308782S01910943EA016860178700307"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110303308788S01911014EA016880178900309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110343308821S01911072EA016930179500309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110383308875S01911066EA017010180400308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110423308873S01911000EA017130181300308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110463308814S01910990EA017080180700308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110503308785S01911062EA017070181200309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110543308807S01911128EA017240183000309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110583308849S01911165EA017340183600309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1111023308895S01911145EA017350183700309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1111063308905S01911080EA017310183100308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1111103308872S01911022EA017370183900308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1111143308812S01911010EA017420184700309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1111183308763S01911049EA017440184600309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1111223308748S01911122EA017430184600309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1111263308767S01911193EA017450184900309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1111303308808S01911246EA017550185800309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1111343308862S01911242EA017630186800308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1111383308870S01911180EA017700187400309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1111423308822S01911139EA017620186700308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1111463308761S01911155EA017620186300308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1111503308727S01911215EA017580185900309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1111543308738S01911287EA017620186500309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1111583308779S01911330EA017630186900309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1112023308832S01911316EA017630186700307"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1112063308854S01911258EA017790188300309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1112103308855S01911197EA017830188600309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1112143308828S01911144EA017780188100309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1112183308776S01911111EA017780188000309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1112223308716S01911111EA017790188200309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1112263308655S01911126EA017780188000307"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1112303308593S01911130EA017720187300308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1112343308531S01911115EA017760187800309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1112383308473S01911090EA017700187500309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1112423308421S01911056EA017700187400309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1112463308371S01911019EA017700187300309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1112503308323S01910978EA017760187800309"));		
		
		FlightAnalyser fa = new FlightAnalyser(points);
		
		Turns turns = fa.calcTurnRates();
		
		//Check # of turns
		ArrayList<TurnData> turns_data_list = turns.getTurns();
		assertEquals("incorrect number of turns detected", 4, turns_data_list.size());
		
		//Check individual turn durations
		TurnData t1 = turns_data_list.get(0);
		assertEquals("first turn duration", 36, t1.duration);
		
		TurnData t2 = turns_data_list.get(1);
		assertEquals("second turn duration", 24, t2.duration);
		
		TurnData t3 = turns_data_list.get(2);
		assertEquals("third turn duration", 32, t3.duration);
		
		TurnData t4 = turns_data_list.get(3);
		assertEquals("fourth turn duration", 28, t4.duration);
		
		//Check average turn rate calc
		assertEquals("average turn rate", 30.0, turns.getAverageTurnRate(), .01);
	}
	
	@Test
	public void testCalculateTrackCourse() {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		GNSSPoint p1 = GNSSPoint.createGNSSPoint("testfile", "B1106503311122S01912340EA0144601541Start");
		GNSSPoint p2 = GNSSPoint.createGNSSPoint("testfile", "B1106523311022S01912340EA0144601541N");
		GNSSPoint p3 = GNSSPoint.createGNSSPoint("testfile", "B1106543310922S01912470EA0144601541NE");
		GNSSPoint p4 = GNSSPoint.createGNSSPoint("testfile", "B1106563310922S01912570EA0144601541E");
		GNSSPoint p5 = GNSSPoint.createGNSSPoint("testfile", "B1106583311038S01912670EA0144601541SE");
		GNSSPoint p6 = GNSSPoint.createGNSSPoint("testfile", "B1107003311138S01912670EA0144601541S");
		GNSSPoint p7 = GNSSPoint.createGNSSPoint("testfile", "B1107023311238S01912550EA0144601541SW");
		GNSSPoint p8 = GNSSPoint.createGNSSPoint("testfile", "B1107043311238S01912450EA0144601541W");
		GNSSPoint p9 = GNSSPoint.createGNSSPoint("testfile", "B1107043311138S01912368EA0144601541NW");
		
		points.add(p1);
		points.add(p2);
		points.add(p3);
		points.add(p4);
		points.add(p5);
		points.add(p6);
		points.add(p7);
		points.add(p8);
		points.add(p9);
		
		FlightAnalyserTestFacade fa = new FlightAnalyserTestFacade(points);
		
		assertEquals(0.0, fa.calculateTrackCourse(p1, p2), 0.1);
		assertEquals(47.4, fa.calculateTrackCourse(p2, p3), 0.1);
		assertEquals(90.0, fa.calculateTrackCourse(p3, p4), 0.1);
		assertEquals(144.2, fa.calculateTrackCourse(p4, p5), 0.1);
		assertEquals(180.0, fa.calculateTrackCourse(p5, p6), 0.1);
		assertEquals(225.1, fa.calculateTrackCourse(p6, p7), 0.1);
		assertEquals(270.0, fa.calculateTrackCourse(p7, p8), 0.1);
		assertEquals(325.5, fa.calculateTrackCourse(p8, p9), 0.1);
	}
}
