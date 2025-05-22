import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Build {

  /**
   * Prints words that are reachable from the given vertex and are strictly shorter than k characters.
   * If the vertex is null or no reachable words meet the criteria, prints nothing.
   *
   * @param vertex the starting vertex
   * @param k the maximum word length (exclusive)
   */
  public static void printShortWords(Vertex<String> vertex, int k) {
    if (vertex == null) return;
    
    Set<Vertex<String>> visited = new HashSet<>();
    printShortWords(vertex, k, visited);
  }

    public static void printShortWords(Vertex<String> vertex, int k, Set<Vertex<String>> visited) {
    if (vertex == null || visited.contains(vertex)) return;
    
    visited.add(vertex);

    String word = vertex.data;

    if (word != null && word.length() < k) {
        System.out.println(word);
    }
    
    if (vertex.neighbors != null && !vertex.neighbors.isEmpty()) {
      for (Vertex<String> neighbor : vertex.neighbors) {
        printShortWords(neighbor, k, visited);
      }
    }
  }

  /**
   * Returns the longest word reachable from the given vertex, including its own value.
   *
   * @param vertex the starting vertex
   * @return the longest reachable word, or an empty string if the vertex is null
   */
  public static String longestWord(Vertex<String> vertex) {
    String longest = "";
    
    Set<Vertex<String>> visited = new HashSet<>();

    return longestWord(vertex, visited, longest);
  }

  public static String longestWord(Vertex<String> vertex, Set<Vertex<String>> visited, String longest) {
    if (vertex == null || visited.contains(vertex)) return longest;

    visited.add(vertex);

    String word = vertex.data;

    if (word != null && word.length() > longest.length()) {
        longest = word;
    }
    
    if (vertex.neighbors != null && !vertex.neighbors.isEmpty()) {
      for (Vertex<String> neighbor : vertex.neighbors) {
        String nextWord = longestWord(neighbor, visited, longest);
        if (nextWord.length() > longest.length()) {
          longest = nextWord;
        }     
      }
    }

    return longest;
  }

  /**
   * Prints the values of all vertices that are reachable from the given vertex and 
   * have themself as a neighbor.
   *
   * @param vertex the starting vertex
   * @param <T> the type of values stored in the vertices
   */
  public static <T> void printSelfLoopers(Vertex<T> vertex) {
    if (vertex == null) return;

    Set<Vertex<T>> visited = new HashSet<>();

    printSelfLoopers(vertex, visited);
  }

  public static <T> void printSelfLoopers(Vertex<T> vertex, Set<Vertex<T>> visited) {
    if (vertex == null || visited.contains(vertex)) return;
    
    visited.add(vertex);
    
    if (vertex.neighbors != null && !vertex.neighbors.isEmpty()) {
      if (vertex.neighbors.contains(vertex)) {
        System.out.println(vertex.data);
      }

      for (Vertex<T> neighbor : vertex.neighbors) {
        printSelfLoopers(neighbor, visited);
      }
    }
  }

  /**
   * Determines whether it is possible to reach the destination airport through a series of flights
   * starting from the given airport. If the start and destination airports are the same, returns true.
   *
   * @param start the starting airport
   * @param destination the destination airport
   * @return true if the destination is reachable from the start, false otherwise
   */
  public static boolean canReach(Airport start, Airport destination) {
    if (start == null || destination == null) return false;
    if (start == destination) return true;

    Set<Airport> visited = new HashSet<>();
    return canReach(start, destination, visited);
  }

  public static boolean canReach(Airport start, Airport destination, Set<Airport> visited) {
    if (start == null || visited.contains(start)) return false;
    if (start == destination) return true;

    visited.add(start);
    List<Airport> flights = start.getOutboundFlights();

    if (flights != null && !flights.isEmpty()) {
      for (Airport airport : flights) {
        if (canReach(airport, destination, visited)) return true;
      }
    }
    return false;
  }


  /**
   * Returns the set of all values in the graph that cannot be reached from the given starting value.
   * The graph is represented as a map where each vertex is associated with a list of its neighboring values.
   *
   * @param graph the graph represented as a map of vertices to neighbors
   * @param starting the starting value
   * @param <T> the type of values stored in the graph
   * @return a set of values that cannot be reached from the starting value
   */
  public static <T> Set<T> unreachable(Map<T, List<T>> graph, T starting) {
    Set<T> visited = new HashSet<>();
    
    // this will prevent duplicates because it is a set
    Set<T> unreachable = new HashSet<>();

    unreachable(graph, starting, visited);

    // add all the keys (Set of keys) to unreachable (addAll adds collections (set/list))
    // because there cannot be duplicates we do not need to worry about duplicate key/value pairs or items that loop back
    // https://docs.oracle.com/javase/8/docs/api/java/util/AbstractCollection.html#addAll-java.util.Collection-
    unreachable.addAll(graph.keySet());
    
    // add all the values by going through the lists (graph.values() returns a list of lists) adding each list to the the unreachable set
    // Default list also supports addAll
    // https://docs.oracle.com/javase/8/docs/api/java/util/List.html#addAll-java.util.Collection-
    for (List<T> neighbors : graph.values()) {
      unreachable.addAll(neighbors);
    } 

    // remove all the items in reachable that are on the visited list
    // this should leave behind any items which cannot be reached
    // https://docs.oracle.com/javase/8/docs/api/java/util/AbstractSet.html#removeAll-java.util.Collection-
    unreachable.removeAll(visited);

    return unreachable;
  }

    public static <T> void unreachable(Map<T, List<T>> graph, T starting, Set<T> visited) {
    if (visited.contains(starting) || !graph.containsKey(starting)) return;
      
    visited.add(starting);
    
    if (graph.get(starting) != null || !graph.get(starting).isEmpty()) {
      for (T next : graph.get(starting)) {
      unreachable(graph, next, visited);
      }
    }
  }
}
