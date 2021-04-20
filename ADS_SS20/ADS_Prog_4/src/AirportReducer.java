import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AirportReducer {
    /**
     * Finds a set of connections that belong to a minimal spanning tree in the
     * given graph using the algorithm of Jarnik / Prim.
     *
     *
     * I have used the naive implementation because there was no limitation what so ever.
     *
     *
     * @param airports a list of airports. Airports have incident connections, and it
     *                 is these connections that shall form the MST returned by this
     *                 method. You can assume that there exists a direct or indirect
     *                 connection between any pair of airports, and that the set
     *                 contains at least one airport.
     * @return the connections that form a minimal spanning tree.
     */
    public static Set<Connection> minimalSpanningTree(final List<Airport> airports) {
        //a List to save the already reached airports (U).
        List<Airport> reached = new ArrayList<>();
        //a Set to save all connections needed dor a MST (ET).
        Set<Connection> connectionAll = new HashSet<>();
        //a variable for the amount of airports.
        int n = airports.size();

        //add the first airport to start the loop.
        reached.add(airports.get(0));

        //too connect all airports you need a minimal of n edges .
        for (int i = 1; i < n; i++) {
            //the variable to save the best possible edge for the current reached airports (U).
            Connection bestEdge = getBestEdge(reached);
            //add the "bestEdge" to the neededEdges (ET).
            connectionAll.add(bestEdge);

            //because the order of the order in the connection you have to check which Airport is the missing one in U.
            if (!reached.contains(bestEdge.getAirport2())) {
                reached.add(bestEdge.getAirport2());
            } else {
                reached.add(bestEdge.getAirport1());
            }
        }
        return connectionAll;
    }

    /**
     * The method "getBestEdge" gets the best possible edge for k of U and a not already reached airport.
     *
     * @param reached are the already reached airports.
     * @return the best possible connection.
     */
    public static Connection getBestEdge(List<Airport> reached) {
        //a List to save the best possible connections of the best connections of airports of the reached airports (U).
        List<Connection> connectionsBest = new ArrayList<>();
        //a List of all reachable airports from the k'th airport of U.
        List<Connection> reachable = new ArrayList<>();

        //go through all of the reached airports (U).
        for (int k = 0; k < reached.size(); k++) {
            //a Set of the all possible destinations.
            Set<Connection> destinations = reached.get(k).getConnections();

            for (Connection connection : destinations) {
                //check if the one of the two airports was not already reached because the orders are random.
                if (!reached.contains(connection.getAirport2()) || !reached.contains(connection.getAirport1())) {
                    reachable.add(connection);
                }
            }
            //check if there are connections to get to else you don't have to add any.
           if (reachable.size() > 0) {
                reachable.sort(Connection.COST_COMPARATOR);
                connectionsBest.add(reachable.get(0));
            }
        }

        connectionsBest.sort(Connection.COST_COMPARATOR);
        return connectionsBest.get(0);
    }
}