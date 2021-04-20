import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

public class AirportReducerTest {
	
	@Test
	public void testSingleAirport() {
		AirportTestData testData = new AirportTestData(true);
		testData.withExpectedCost(testData.addSequence(1));
		
		performTest(testData);
	}
	
	@Test
	public void testTwoAirports() {
		AirportTestData testData = new AirportTestData(true);
		testData.withExpectedCost(testData.addSequence(2));
		
		performTest(testData);
	}
	
	@Test
	public void testSequence() {
		AirportTestData testData = new AirportTestData(true);
		testData.withExpectedCost(testData.addSequence(10));
		
		performTest(testData);
	}
	
	@Test
	public void testCircle() {
		AirportTestData testData = new AirportTestData(true);
		testData.withExpectedCost(testData.addSequence(10));
		
		// Find the maximum edge weight
		int maxCost = testData.connections.stream()
				.mapToInt(conn -> conn.getCost())
				.max()
				.getAsInt();
		
		// Add another, costlier edge
		testData.connect(testData.airports.get(0), testData.airports.get(9), maxCost + 1);
		
		performTest(testData);
	}
	
	@Test
	public void testStar() {
		final int airportCount = 10;
		final int cost = 2;
		
		AirportTestData testData = new AirportTestData(true);
		
		Airport center = testData.addAirport();
		for (int i = 0; i < airportCount; i++) {
			testData.connect(center, testData.addAirport(), cost);
		}
		
		performTest(testData.withExpectedCost(airportCount * cost));
	}
	
	@Test
	public void testSmallTree() {
		AirportTestData testData = new AirportTestData(true);
		testData.withExpectedCost(testData.addTree(10));
		
		performTest(testData);
	}
	
	@Test
	public void testLargeTree() {
		AirportTestData testData = new AirportTestData(true);
		testData.withExpectedCost(testData.addTree(30));
		
		performTest(testData);
	}
	
	@Test
	public void testCompleteGraphWithEqualEdgeWeights() {
		AirportTestData testData = new AirportTestData(true);
		
		final int airportCount = 10;
		for (int i = 0; i < airportCount; i++) {
			testData.addAirport();
		}
		
		testData.addRandomEdges(1, 1);
		testData.withExpectedCost(airportCount - 1);
		
		performTest(testData);
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	// Running Tests
	
	/**
	 * Runs the actual test.
	 */
	private void performTest(AirportTestData testData) {
		// Copy stuff so solutions can wreak havoc on the input data
		Set<Connection> connectionsCopy = new HashSet<>(testData.connections);
		List<Airport> airportsCopy = new ArrayList<>(testData.airports);
		
		// Run the student's code
		Set<Connection> studentSolution = AirportReducer.minimalSpanningTree(airportsCopy);
		
		// We don't want null return values
		assertNotNull(studentSolution,
				"Your return value must never be null, but it was for the following instance:\n\n"
						+ testData);
		
		// Ensure that each connection in the solution is contained in our original set of
		// connections
		assertTrue(connectionsCopy.containsAll(studentSolution),
				"Your solution contains connections that were not part of the input.\n\n"
				+ "Your solution: " + studentSolution
				+ "\nThe input: " + testData);
		
		// The result must contain airportCount - 1 connections for it to be a tree
		assertEquals(testData.airports.size() - 1, studentSolution.size(),
				"Your solution does not contain the expected number of edges for it to be a tree.\n\n"
				+ "Input: " + testData);
		
		// If we have at least two airports, make sure the connections touch every one
		if (testData.airports.size() > 1) {
			assertTrue(everyNodeTouched(testData, studentSolution),
					"Your solution does not provide every airport with an incident connection. Input:\n" + testData);
		}
		
		// Sum up the cost of all the connections in the student solution
		int studentCost = studentSolution.stream()
				.mapToInt(conn -> conn.getCost())
				.sum();
		assertEquals(testData.expectedCost, studentCost,
				"Your solution has a cost of " + studentCost + " while we expected " + testData.expectedCost
					+ " for input:\n" + testData);
	}

	/**
	 * Checks if each airport is incident to at least one connection.
	 */
	private boolean everyNodeTouched(AirportTestData testData, Set<Connection> studentSolution) {
		Set<Airport> unconnectedAirports = new HashSet<>(testData.airports);
		
		for (Connection conn : studentSolution) {
			connectAirport(conn.getAirport1(), testData, unconnectedAirports);
			connectAirport(conn.getAirport2(), testData, unconnectedAirports);
		}
		
		return unconnectedAirports.isEmpty();
	}

	/**
	 * Fails if the airport doesn't exist. If it exists, the airport is removed from the
	 * set of unconnected airports.
	 */
	private void connectAirport(Airport airport, AirportTestData testData,
			Set<Airport> unconnectedAirports) {
		
		assertTrue(testData.airports.contains(airport),
				"You managed to produce a connection to airport " + airport
					+ ", which doesn't exist in the following test input:\n" + testData);
		unconnectedAirports.remove(airport);
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
		/** Randomiser we'll use to randomise. */
		private Random rand = new Random();

		/** The list of airports. */
		private final List<Airport> airports = new ArrayList<>();
		/** The set of connections between the airports. */
		private final Set<Connection> connections = new HashSet<>();
		/** The expected cost of the spanning tree. */
		private int expectedCost;
		
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
		
		public AirportTestData withExpectedCost(int expectedCost) {
			this.expectedCost = expectedCost;
			return this;
		}
		
		////////////////////////////////////////////////////////////////////////////
		// 
		
		/**
		 * Adds and returns an airport.
		 */
		public Airport addAirport() {
			Airport airport = new Airport(iataCodes.get(nextAirportIndex++));
			airports.add(airport);
			return airport;
		}
		
		/**
		 * Adds the given number of airports, connects them in a sequence and returns
		 * the cost.
		 */
		public int addSequence(int airportCount) {
			if (airportCount <= 0) {
				airportCount = 2 + rand.nextInt(99);
			}
			
			int cost = 0;
			
			Airport previous = null;
			for (int i = 0; i < airportCount; i++) {
				Airport next = addAirport();
				
				if (previous != null) {
					Connection conn = connect(previous, next);
					cost += conn.getCost();
				}
				
				previous = next;
			}
			
			return cost;
		}
		
		/**
		 * Adds the given number of airports connected to form a tree.
		 */
		public int addTree(int airportCount) {
			if (airportCount <= 0) {
				airportCount = 2 + rand.nextInt(99);
			}
			
			List<Airport> treePorts = new ArrayList<>();
			int cost = 0;
			
			// Add the first airport
			treePorts.add(addAirport());
			
			// Add and connect the remaining airports
			for (int i = 1; i < airportCount; i++) {
				Airport newAirport = addAirport();
				Airport randomAirport = treePorts.get(rand.nextInt(treePorts.size()));
				
				treePorts.add(newAirport);
				Connection conn = connect(newAirport, randomAirport);
				
				cost += conn.getCost();
			}
			
			return cost;
		}
		
		/**
		 * For each pair of airports which are not connected yet, add a connection
		 * with the given probability and a random {@code cost < 0} or fixed cost.
		 */
		public void addRandomEdges(double probability, int cost) {
			// Go through all pairs of airports
			int airportCount = airports.size();
			for (int source = 0; source < airportCount; source++) {
				Airport sourceAirport = airports.get(source);
				
				for (int target = source + 1; target < airportCount; target++) {
					if (rand.nextDouble() < probability) {
						// We have to add a new connection, unless of course there
						// already is one
						Airport targetAirport = airports.get(target);
						
						if (cost < 0) {
							connect(sourceAirport, targetAirport);
						} else {
							connect(sourceAirport, targetAirport, cost);
						}
					}
				}
			}
		}
		
		/**
		 * Connects the given airports with a connection with random cost.
		 */
		public Connection connect(Airport airport1, Airport airport2) {
			return connect(airport1, airport2, 5 + rand.nextInt(95));
		}

		/**
		 * Connects the given airports with a connection with the given cost, unless
		 * they are already connected. Returns either the new connection or {@code null}.
		 */
		public Connection connect(Airport airport1, Airport airport2, int cost) {
			Connection conn = new Connection(airport1, airport2, cost);
			
			if (connections.contains(conn)) {
				return null;
				
			} else {
				connections.add(conn);
				
				airport1.getConnections().add(conn);
				airport2.getConnections().add(conn);
				
				return conn;
			}
		}
		
		////////////////////////////////////////////////////////////////////////////
		// String Output

		@Override
		public String toString() {
			String result = "Airports: " + airports + "\n"
					+ "Connections:\n   ";
			result += connections.stream()
					.map(conn -> conn.toString())
					.collect(Collectors.joining("\n   "));
			
			return result;
		}

	}
	
}