import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

public class AirportReachabilityCalculatorTest {
	
	@Test
	public void testNull() {
		assertThrows(
				IllegalArgumentException.class,
				() -> AirportReachabilityCalculator.findAlwaysReachableAirports(null),
				"Your method doesn't throw the expected exception when we pass null.");
	}
	
	@Test
	public void testExample() {
		AirportTestData testData = new AirportTestData(false);
		
		Airport jav = testData.addAirport("JAV");
		Airport las = testData.addAirport("LAS");
		Airport fnj = testData.addAirport("FNJ");
		Airport ikt = testData.addAirport("IKT");
		Airport txl = testData.addAirport("TXL");
		Airport ber = testData.addAirport("BER");
		
		// Connect the airports
		AirportTestData.connect(jav, fnj);
		AirportTestData.connect(fnj, ikt);
		AirportTestData.connect(ikt, las);
		AirportTestData.connect(las, jav);
		AirportTestData.connect(txl, ikt);
		AirportTestData.connect(ber, txl);
		
		// Assemble the list of always reachable airports
		testData.withReachableAirports(jav, las, fnj, ikt);
		
		performTest(testData);
	}

	@Test
	public void testSingleAirport() {
		AirportTestData testData = new AirportTestData(true);
		
		testData.withReachableAirports(testData.addAirports(1));
		
		performTest(testData);
	}

	@Test
	public void testTwoUnconnectedAirports() {
		AirportTestData testData = new AirportTestData(true);
		
		List<Airport> airports = testData.addString(2);
		testData.withReachableAirports(airports.get(1));
		
		performTest(testData);
	}
	
	@Test
	public void testSmallTree() {
		AirportTestData testData = new AirportTestData(true);
		
		List<Airport> airports = testData.addAirports(3);
		
		AirportTestData.connect(airports.get(0), airports.get(1));
		AirportTestData.connect(airports.get(0), airports.get(2));
		
		performTest(testData);
	}
	
	@Test
	public void testCycle() {
		AirportTestData testData = new AirportTestData(true);
		testData.addCycle(6);
		testData.withReachableAirports(testData.airports);
		
		performTest(testData);
	}
	
	@Test
	public void testCycleWithExtrusion() {
		// The extrusion will lead away from the cycle. Our example picture
		// already shows an extrusion which leads towards the cycle
		AirportTestData testData = new AirportTestData(true);
		
		List<Airport> cycle = testData.addCycle(6);
		List<Airport> extrusion = testData.addString(3);
		AirportTestData.connect(cycle.get(0), extrusion.get(0));
		
		testData.withReachableAirports(extrusion.get(extrusion.size() - 1));
		
		performTest(testData);
	}

	@Test
	public void testTwoHalfConnectedCycles() {
		AirportTestData testData = new AirportTestData(true);
		
		List<Airport> cycle1 = testData.addCycle(6);
		List<Airport> cycle2 = testData.addCycle(6);
		AirportTestData.connect(cycle1.get(0), cycle2.get(0));
		
		testData.withReachableAirports(cycle2);
		
		performTest(testData);
	}

	@Test
	public void testTwoFullConnectedCycles() {
		AirportTestData testData = new AirportTestData(true);
		
		List<Airport> cycle1 = testData.addCycle(6);
		List<Airport> cycle2 = testData.addCycle(6);
		AirportTestData.connect(cycle1.get(0), cycle2.get(0));
		AirportTestData.connect(cycle2.get(0), cycle1.get(0));
		
		testData
			.withReachableAirports(cycle1)
			.withReachableAirports(cycle2);
		
		performTest(testData);
	}

	@Test
	public void testCompleteGraph() {
		AirportTestData testData = new AirportTestData(true);
		
		testData.withReachableAirports(testData.addComplete(10));
		
		performTest(testData);
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	// Running Tests

	/**
	 * Runs the actual tests.
	 */
	private void performTest(AirportTestData testData) {
		// Run algorithm
		Set<Airport> studentSolution = AirportReachabilityCalculator.findAlwaysReachableAirports(testData.airports);
		
		if (studentSolution == null) {
			fail("Your return value must never be null, but it was for the following instance:\n\n" + testData);
		}
		
		// Check if the solution is correct
		if (!testData.reachableAirports.equals(studentSolution)) {
			// Only build the fail message if necessary
			StringBuilder s = new StringBuilder("Your solution is incorrect for the following instance:\n\n" + testData);
			
			Set<Airport> missingAirports = new HashSet<>(testData.reachableAirports);
			missingAirports.removeAll(studentSolution);
			s.append("\nThe following airports were missing in your solution: " + missingAirports);
			
			Set<Airport> superfluousAirports = new HashSet<>(testData.reachableAirports);
			superfluousAirports.removeAll(testData.reachableAirports);
			s.append("\nThe following airports were mistakenly part of your solution: " + superfluousAirports);
			
			fail(s.toString());
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	// Test Data

	/**
	 * Encapsulates test data for a single test.
	 */
	private static class AirportTestData {

		/** IATA airport codes for us to use. */
		private final List<String> iataCodes = Arrays.asList(new String[] { "AAL", "ABQ", "ABV", "ABZ", "ACA",
				"ACC", "ADA", "ADB", "ADD", "ADL", "ADW", "AER", "AFW", "AGP", "AGS", "AKL", "AKT", "ALA", "ALC", "ALG",
				"ALP", "AMA", "AMM", "AMS", "ANC", "ARN", "ATH", "ATL", "ATQ", "AUH", "AUS", "AVL", "AYT", "BAB", "BAD",
				"BAH", "BCN", "BDL", "BEG", "BEL", "BEY", "BFI", "BFS", "BGO", "BGR", "BGW", "BGY", "BHD", "BHM", "BHX",
				"BIL", "BJV", "BKK", "BLA", "BLL", "BLQ", "BLR", "BLV", "BMI", "BNA", "BNE", "BOD", "BOG", "BOH", "BOI",
				"BOJ", "BOM", "BOO", "BOS", "BRE", "BRI", "BRS", "BRU", "BSB", "BSL", "BSR", "BTS", "BUD", "BUF", "BWI",
				"BWN", "BZE", "BZZ", "CAE", "CAG", "CAI", "CAN", "CBM", "CBR", "CCJ", "CCS", "CCU", "CDG", "CEB", "CGH",
				"CGK", "CGN", "CGO", "CGY", "CHA", "CHC", "CHS", "CIA", "CID", "CJJ", "CJU", "CKG", "CLE", "CLT", "CMB",
				"CMH", "CMN", "CMN", "CNF", "CNX", "COK", "CPH", "CPR", "CPT", "CRK", "CRL", "CRP", "CRW", "CSX", "CTA",
				"CTS", "CTU", "CUN", "CUZ", "CVG", "CVS", "CWB", "CWL", "DAB", "DAD", "DAL", "DAM", "DAR", "DAY", "DBQ",
				"DCA", "DEL", "DEN", "DFW", "DHA", "DKR", "DLC", "DLF", "DLH", "DLM", "DME", "DMK", "DMM", "DNA", "DOH",
				"DOV", "DPS", "DQM", "DRS", "DSA", "DSM", "DSS", "DTM", "DTW", "DUB", "DUR", "DUS", "DVO", "DWC", "DXB",
				"DYS", "EDI", "EDW", "EIN", "EMA", "END", "ERI", "ERZ", "ESB", "EVN", "EWR", "EXT", "EZE", "FAI", "FAO",
				"FCO", "FFD", "FFO", "FKB", "FLL", "FLN", "FMO", "FOC", "FRA", "FRU", "FSM", "FSZ", "FTW", "FUK", "FWA",
				"GCM", "GDL", "GDN", "GEG", "GIG", "GLA", "GMP", "GOA", "GOI", "GOT", "GPT", "GRB", "GRJ", "GRU", "GRV",
				"GSB", "GSO", "GSP", "GUM", "GUS", "GVA", "GYD", "GZT", "HAJ", "HAK", "HAM", "HAN", "HAV", "HEL", "HER",
				"HET", "HGH", "HIB", "HKG", "HKT", "HMN", "HMO", "HND", "HNL", "HOU", "HRB", "HRG", "HRI", "HRK", "HSV",
				"HTS", "HYD", "IAD", "IAH", "ICN", "ICT", "IKA", "IND", "ISB", "ISE", "IST", "ITM", "JAN", "JAX", "JED",
				"JFK", "JLN", "JNB", "JNB", "JUB", "KAN", "KBP", "KEF", "KGF", "KHH", "KHV", "KIN", "KIX", "KJA", "KMG",
				"KNH", "KNO", "KOJ", "KRK", "KRT", "KTW", "KUF", "KUL", "KUV", "KWE", "KWI", "KWL", "KZN", "LAS", "LAX",
				"LBA", "LBB", "LCA", "LCK", "LED", "LEJ", "LEX", "LFI", "LFT", "LGA", "LGG", "LGW", "LHR", "LIM", "LIN",
				"LIR", "LIS", "LIT", "LJU", "LKZ", "LLA", "LOS", "LPA", "LPL", "LTK", "LTN", "LTS", "LTX", "LUF", "LUN",
				"LUX", "LXR", "LYS", "MAA", "MAD", "MAN", "MBA", "MBS", "MCF", "MCI", "MCO", "MCT", "MDL", "MDW", "MED",
				"MEL", "MEM", "MEX", "MFM", "MGE", "MGM", "MHD", "MHZ", "MIA", "MKE", "MLA", "MLE", "MLI", "MLU", "MMX",
				"MNH", "MNL", "MOB", "MRS", "MSN", "MSP", "MSQ", "MST", "MSY", "MTY", "MUC", "MUO", "MVD", "MWX", "MXP",
				"NAP", "NAS", "NAT", "NAY", "NBE", "NBO", "NCE", "NCL", "NGB", "NGO", "NKC", "NKG", "NNG", "NRT", "NUE",
				"NWI", "OAK", "ODS", "OKA", "OKC", "OKO", "OMA", "ONT", "OPO", "ORD", "ORF", "ORK", "ORY", "OSL", "OSN",
				"OTP", "OVB", "PAM", "PBI", "PDL", "PDX", "PEK", "PER", "PFO", "PHF", "PHL", "PHX", "PIA", "PIT", "PMI",
				"PMO", "PNH", "POM", "POZ", "PPE", "PRG", "PRN", "PSA", "PTP", "PTY", "PUJ", "PUS", "PVG", "PVR", "PWM",
				"QUO", "RAR", "RDU", "REP", "RFD", "RGN", "RIC", "RIX", "RMS", "RND", "RNO", "ROA", "ROC", "ROV", "RRG",
				"RST", "RSW", "RUH", "SAL", "SAN", "SAT", "SAV", "SAW", "SBN", "SCL", "SCQ", "SDF", "SDQ", "SEA", "SFB",
				"SFO", "SGF", "SGN", "SHA", "SHE", "SHJ", "SIN", "SIP", "SJC", "SJD", "SJJ", "SJU", "SKA", "SKG", "SKT",
				"SLC", "SMF", "SNA", "SNN", "SOF", "SOQ", "SOU", "SPI", "SPS", "SRQ", "SSA", "SSC", "STL", "STN", "STR",
				"SUB", "SUS", "SUU", "SUX", "SVG", "SVO", "SVX", "SXF", "SXM", "SYD", "SYR", "SYX", "SYZ", "SZL", "SZX",
				"TAS", "TBS", "TBZ", "TCM", "TER", "TFN", "TFS", "TGD", "THR", "TIA", "TIJ", "TIK", "TIP", "TLH", "TLL",
				"TLS", "TLV", "TNA", "TOL", "TOS", "TPA", "TPE", "TRD", "TRI", "TRN", "TRV", "TSE", "TSF", "TSN", "TUL",
				"TUN", "TUS", "TXL", "TYN", "TYS", "TZX", "UFA", "UIO", "UPG", "URC", "VAR", "VBG", "VCE", "VDA", "VIE",
				"VKO", "VNO", "VPS", "VRA", "VRN", "WAW", "WLG", "WMI", "WNZ", "WRB", "WRO", "WUH", "XIY", "XMN", "YEG",
				"YHZ", "YMX", "YOW", "YUL", "YVR", "YWG", "YYC", "YYJ", "YYT", "YYZ", "ZAG", "ZIA", "ZNZ", "ZRH" });
		
		/** Index of the next airport designation we'll use. */
		private int nextAirportIndex = 0;

		/** The set of airports we're giving the students. */
		private final Set<Airport> airports = new HashSet<>();
		/** The set of reachable airports. */
		private final Set<Airport> reachableAirports = new HashSet<>();
		
		////////////////////////////////////////////////////////////////////////////
		// Creation
		
		/**
		 * Creates a new instance which will either use the list of airport designations as is
		 * or shuffle it so that subsequent test runs differ slightly.
		 */
		public AirportTestData(boolean shuffled) {
			if (shuffled) {
				Collections.shuffle(iataCodes);
			}
		}
		
		////////////////////////////////////////////////////////////////////////////
		// Configuration
		
		public Airport addAirport(String code) {
			Airport newAirport = new Airport(code);
			airports.add(newAirport);
			return newAirport;
		}
		
		/**
		 * Adds the given always reachable airports and returns this test data object for method chaining.
		 */
		public AirportTestData withReachableAirports(Airport... reachableAirports) {
			withReachableAirports(Arrays.asList(reachableAirports));
			return this;
		}

		/**
		 * Adds the given always reachable airports and returns this test data object for method chaining.
		 */
		public AirportTestData withReachableAirports(Collection<Airport> newCritAirports) {
			reachableAirports.addAll(newCritAirports);
			return this;
		}
		
		////////////////////////////////////////////////////////////////////////////
		// Airport generation
		
		/**
		 * Generates the given number of airports with different IATA designators. Subsequent
		 * invocations will generate airports with yet different IATA designators.
		 */
		private List<Airport> addAirports(int count) {
			List<Airport> newAirports = new ArrayList<>(count);
			for (int i = 0; i < count; i++) {
				newAirports.add(new Airport(iataCodes.get(nextAirportIndex)));
				nextAirportIndex++;
			}
			
			airports.addAll(newAirports);
			
			return newAirports;
		}
		
		/**
		 * Generates, adds and returns a number of airports connected to form a line.
		 */
		private List<Airport> addString(int count) {
			List<Airport> newAirports = addAirports(count);
			for (int i = 1; i < count; i++) {
				connect(newAirports.get(i - 1), newAirports.get(i));
			}
			
			return newAirports;
		}
		
		/**
		 * Generates, adds and returns a number of airports connected to form a cycle.
		 */
		private List<Airport> addCycle(int count) {
			List<Airport> newAirports = addString(count);
			connect(newAirports.get(count - 1), newAirports.get(0));
			return newAirports;
		}
		
		/**
		 * Generates, adds and returns a number of airports that form a complete graph.
		 */
		private List<Airport> addComplete(int count) {
			List<Airport> newAirports = addAirports(count);
			for (int first = 0; first < count; first++) {
				for (int second = first + 1; second < count; second++) {
					connect(newAirports.get(first), newAirports.get(second));
					connect(newAirports.get(second), newAirports.get(first));
				}
			}

			return newAirports;
		}
		
		/**
		 * Convenience method for connecting airport 1 to airport 2.
		 */
		private static void connect(Airport airport1, Airport airport2) {
			airport1.getDestinations().add(airport2);
		}
		
		////////////////////////////////////////////////////////////////////////////
		// String Output
		
		/**
		 * Returns a string that contains a sorted list of airports.
		 */
		public static String toAirportList(Set<Airport> airports) {
			return "[" 
					+ airports.stream()
						.sorted()
						.map(airport -> airport.getIataDesignation())
						.collect(Collectors.joining(", "))
					+ "]";
		}
		
		@Override
		public String toString() {
			String result = "Airports (one per line, with destinations in brackets)\n";
			
			TreeSet<Airport> sortedAirports = new TreeSet<>(airports);
			for (Airport airport : sortedAirports) {
				result += airport.getIataDesignation() + " "
						+ toAirportList(airport.getDestinations()) + "\n";
			}
			
			return result;
		}

	}
	
}