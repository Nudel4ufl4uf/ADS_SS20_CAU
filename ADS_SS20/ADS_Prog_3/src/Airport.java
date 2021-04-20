import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Models an airport. For them to be identifiable, all airports have an
 * <a href="https://en.wikipedia.org/wiki/IATA_airport_code">IATA code</a> and
 * two airports are considered equal if their IATA codes match. Since this class
 * implements {@link #equals(Object)} and {@link #hashCode()}, it can be used in
 * hash-based data structures, such as {@link HashSet}.
 * 
 * <p>
 * Each airport has a (possibly empty) set of designations that can be reached
 * directly (that is, there are direct flights from this airport to all
 * designation airports).
 * </p>
 */
public final class Airport implements Comparable<Airport> {

	/** The airport's IATA designation. */
	private final String iataDesignation;
	/** Set of airports that can be reached from this airport. */
	private final Set<Airport> destinations = new HashSet<>();

	/**
	 * Creates a new airport with no connected airports.
	 * 
	 * @param iataCode the airport's non-{@code null} IATA designation.
	 * @throw IllegalArgumentException if the code is invalid.
	 */
	public Airport(String iataCode) {
		ensureValidIataDesignation(iataCode);
		this.iataDesignation = iataCode;
	}

	/**
	 * Throws an {@link IllegalArgumentException} if the given String is not a valid
	 * IATA designation.
	 */
	public static void ensureValidIataDesignation(String iata) {
		if (iata == null) {
			throw new IllegalArgumentException("IATA code cannot be null.");
		}

		if (!Pattern.matches("[a-zA-Z]{3}", iata)) {
			throw new IllegalArgumentException("IATA codes must consist of three characters.");
		}
	}

	/**
	 * Returns this airport's IATA designation.
	 */
	public String getIataDesignation() {
		return iataDesignation;
	}

	/**
	 * Returns the airport's set of destinations that can be reached directly, to be
	 * modified at will.
	 */
	public Set<Airport> getDestinations() {
		return destinations;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Airport) {
			return ((Airport) obj).getIataDesignation().equals(this.getIataDesignation());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return getIataDesignation().hashCode();
	}
	
	@Override
	public String toString() {
		return getIataDesignation();
	}

	@Override
	public int compareTo(Airport o) {
		return iataDesignation.compareTo(o.iataDesignation);
	}

}
