import java.util.*;

public class AirportReachabilityCalculator {
    /**
     * Finds airports that are directly or indirectly reachable from all other
     * airports. Phrase differently: for each airport {@code a} in the result it
     * must hold that there is a directed path from each other airport {@code b} to
     * {@code a}.
     *
     * @param airports the set of airports.
     * @return the set of always reachable airports.
     * @throws IllegalArgumentException if {@code airports} is {@code null}.
     */

    public static Set<Airport> findAlwaysReachableAirports(Set<Airport> airports) {
        //check if the airports is null
        if (airports == null) {
            throw new IllegalArgumentException();
        }
        //check if there is only one airport
        if (airports.size() == 1) {
            return airports;
        }

        //List for all airport names to compare and delete of.
        Set<String> allAirports = new HashSet<>();

        //read out all possible airports by name.
        for (Airport airport : airports) {
            allAirports.add(airport.getIataDesignation());
        }

        //go through all the airports and check on the reachable airports.
        for (Airport airport : airports) {

            //create a list were all possible destinations of the given airport can be saved.
            Set<String> possibleDestinations = new HashSet<>();
            //create a list of to check elements.
            ArrayList<Airport> toCheck = new ArrayList<>();

            //save the current airport.
            possibleDestinations.add(airport.getIataDesignation());
            //add the destinations of the current airport.
            toCheck.addAll(airport.getDestinations());

            //go through the list of to check destinations and add more destinations if needed until the whole list
            // is checked.
            for (int i = 0; ; i++) {
                //end the loop if the whole list is checked
                if (i == toCheck.size()) {
                    break;
                }

                //get the current item of the list
                Airport current = toCheck.get(i);

                //check through the destinations till there is a already reached destination
                while (!possibleDestinations.contains(current.getIataDesignation())) {
                    possibleDestinations.add(current.getIataDesignation());
                    toCheck.addAll(current.getDestinations());
                }
            }
            allAirports.retainAll(possibleDestinations);
        }

        //the set of airports which are reachable from every airport
        Set<Airport> finalSet = new HashSet<>();

        //convert the airport strings back to a set of airport sets
        for (String airportSting : allAirports) {
            for (Airport airport : airports) {
                if (airport.getIataDesignation() == airportSting) {
                    finalSet.add(airport);
                }
            }
        }

        //delete all not reachable airports of the list of all airports
        airports.retainAll(finalSet);
        return airports;
    }

}
