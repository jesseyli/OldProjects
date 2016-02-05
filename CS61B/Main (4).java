package trip;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;
import graph.Graph;
import graph.Graphs;
import graph.DirectedGraph;
import graph.Weightable;
import graph.Weighted;
import graph.Distancer;


/** Initial class for the 'trip' program.
 *  @author Roy Kim
 */
public final class Main {

    /** A class that holds a location read from a mapFile. */
    public static class Location implements Weightable {

        /** Constructor that assigns the location's NAME to _name, its
         *  X-coordinate to _x and its Y-coordinate to _y. */
        public Location(String name, float x, float y) {
            _name = name;
            _x = x;
            _y = y;
        }

        @Override
        public void setWeight(double w) {
            _weight = w;
        }

        @Override
        public double weight() {
            return _weight;
        }

        /** A method that returns the Location's x-coordinate. */
        private float getX() {
            return _x;
        }

        /** A method that returns the Location's y-coordinate. */
        private float getY() {
            return _y;
        }

        /** A location's name. */
        private String _name;

        /** A location's x-coordinate. */
        private float _x;

        /** A location's y-coordinate. */
        private float _y;

        /** A location's weight. */
        private double _weight;

    }

    /** A class that holds a distance read from a mapFile. */
    public static class Distance implements Weighted {

        /** Constructor that assigns the direction's START city to _start,
         *  the ROAD's name to _road, the LENGTH of the route to _length, the
         *  DIRECTION of the route to _direction and the END city to _end.
         */
        public Distance(String start, String road, float length,
                String direction, String end) {
            _start = start;
            _road = road;
            _length = length;
            _direction = direction;
            _end = end;
        }

        @Override
        public double weight() {
            return _length;
        }

        /** Method that returns the starting city. */
        public String getStart() {
            return _start;
        }

        /** Method that returns the road for the route. */
        public String getRoad() {
            return _road;
        }

        /** Method that returns the length of the route. */
        public float getLength() {
            return _length;
        }

        /** Method that returns the direction of the route given the
         *  STARTCITY. */
        public String getDir() {
            return _direction;
        }

        /** Method that returns the ending city. */
        public String getEnd() {
            return _end;
        }

        /** A direction's starting city. */
        private String _start;

        /** The name of the road for the route of the direction. */
        private String _road;

        /** The length of the distance from the startin city to the ending
         *  city. */
        private float _length;

        /** The direction of the route. */
        private String _direction;

        /** The direction's ending city. */
        private String _end;

    }

    /** Entry point for the CS61B trip program.  ARGS may contain options
     *  and targets:
     *      [ -m MAP ] [ -o OUT ] [ REQUEST ]
     *  where MAP (default Map) contains the map data, OUT (default standard
     *  output) takes the result, and REQUEST (default standard input) contains
     *  the locations along the requested trip.
     */
    public static void main(String... args) {
        String mapFileName;
        String outFileName;
        String requestFileName;

        mapFileName = "Map";
        outFileName = requestFileName = null;

        int a;
        for (a = 0; a < args.length; a += 1) {
            if (args[a].equals("-m")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    mapFileName = args[a];
                }
            } else if (args[a].equals("-o")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    outFileName = args[a];
                }
            } else if (args[a].startsWith("-")) {
                usage();
            } else {
                break;
            }
        }

        if (a == args.length - 1) {
            requestFileName = args[a];
        } else if (a > args.length) {
            usage();
        }

        if (requestFileName != null) {
            try {
                System.setIn(new FileInputStream(requestFileName));
            } catch  (FileNotFoundException e) {
                System.err.printf("Could not open %s.%n", requestFileName);
                System.exit(1);
            }
        }

        if (outFileName != null) {
            try {
                System.setOut(new PrintStream(new FileOutputStream(outFileName),
                                              true));
            } catch  (FileNotFoundException e) {
                System.err.printf("Could not open %s for writing.%n",
                                  outFileName);
                System.exit(1);
            }
        }

        trip(mapFileName);
    }

    /** A distancer used for the trip method that uses the distance formula. */
    public static final Distancer<Location> LOC_DISTANCER =
            new Distancer<Location>() {
                @Override
                public double dist(Location loc1, Location loc2) {
                    return Math.sqrt(Math.pow(loc2.getY()
                        - loc1.getY(), 2) + Math.pow(loc2.getX()
                            - loc1.getX(), 2));
                }
            };

    /** Print a trip for the request on the standard input to the standard
     *  output, using the map data in MAPFILENAME.
     */
    private static void trip(String mapFileName) {
        locationNames = new HashMap<String,
            Graph<Location, Distance>.Vertex>();
        mapReader(mapFileName);
        Scanner scan = new Scanner(System.in);
        String currRequest;
        String[] currReqArray;
        String direction;
        List<Graph<Location, Distance>.Edge> path;
        while (scan.hasNextLine()) {
            currRequest = scan.nextLine();
            currReqArray = currRequest.trim().split(",\\s+");
            for (int i = 0; i < currReqArray.length - 1; i += 1) {
                path = Graphs.shortestPath(tripGraph,
                    locationNames.get(currReqArray[i]),
                    locationNames.get(currReqArray[i + 1]),
                    LOC_DISTANCER);
                if (path == null) {
                    System.out.println("Invalid request");
                    System.exit(1);
                }
                if (i == 0) {
                    System.out.printf("From %s:\n\n", currReqArray[i]);
                }
                Distance next;
                for (int j = 0; j < path.size(); j += 1) {
                    Distance curr = (Distance) path.get(j).getLabel();
                    float combLength = curr.getLength();
                    int dups = j;
                    for (; dups < path.size(); dups += 1) {
                        if (dups + 1 < path.size()) {
                            next = (Distance) path.get(dups + 1).getLabel();
                            if (next.getRoad().equals(curr.getRoad())
                                    && next.getDir().equals(curr.getDir())) {
                                combLength = combLength + next.getLength();
                                j += 1;
                            } else {
                                break;
                            }
                        }
                    }
                    if (j == path.size() - 1) {
                        System.out.printf("%d. Take %s %s for %.1f miles to "
                            + "%s.\n", currStep, curr.getRoad(),
                            curr.getDir(), combLength, currReqArray[i + 1]);
                        currStep += 1;
                        continue;
                    } else {
                        System.out.printf("%d. Take %s %s for %.1f miles.\n",
                            currStep, curr.getRoad(),
                            curr.getDir(), combLength);
                    }
                    currStep += 1;
                }
            }
        }
        currStep = 0;
    }

    /** Helper method of mapReader that will return a valid input line (if it
     *  exists) by going through the reader INPUT. */
    private static String mapReaderHelp(BufferedReader input) {
        String locOrDir = "";
        String result = "";
        boolean first = true;
        String current = "";
        try {
            for (int currInt = input.read(); currInt != -1;) {
                current = Character.toString((char) currInt);
                if (locOrDir.equals("R")) {
                    if (currInp.size() == 6
                            && result.matches("R\\s+[\\p{Alnum}_-]+\\s+"
                                + "[\\p{Alnum}_-]+\\s+[\\d.]+\\s+[A-Z]+\\s+"
                                + "[\\p{Alnum}_-]+")) {
                        return result.replace("\\s+", " ");
                    }
                    if (currInp.size() > 6) {
                        throw new IOException();
                    }
                } else if (locOrDir.equals("L")) {
                    if (currInp.size() == 4
                            && result.matches("L\\s+[\\p{Alnum}_-]+\\s+-?"
                                + "[\\d.]+\\s+-?[\\d.]+")) {
                        return result.replace("\\s+", " ");
                    }
                    if (currInp.size() > 4) {
                        throw new IOException();
                    }
                } else if (!first) {
                    throw new IOException();
                }
                if (current.matches("\\s") && result.equals("")) {
                    currInt = input.read();
                    continue;
                } else if (current.matches("\\s") && !result.equals("")) {
                    if (add) {
                        currInp.add(result);
                        add = false;
                    }
                    if ((locOrDir.equals("L") && currInp.size() == 4)
                            || (locOrDir.equals("R") && currInp.size() == 6)) {
                        continue;
                    } else {
                        result += " ";
                    }
                } else {
                    add = true;
                    if (first) {
                        locOrDir = current;
                        first = false;
                    }
                    result = result + current;
                }
                currInt = input.read();
            }
        } catch (IOException err) {
            System.out.println("Error occurred");
            System.exit(1);
        }
        return null;
    }

    /** Read the map FILE and save the distances and locations in two
     *  different ArrayLists. */
    private static void mapReader(String file) {
        String reverse = "";
        String[] currInpLine;
        String direc = "";
        Location addLoc;
        try {
            FileReader mReader = new FileReader(file);
            BufferedReader inp = new BufferedReader(mReader);
            while (true) {
                currInp.clear();
                currInput = mapReaderHelp(inp);
                if (currInput == null) {
                    return;
                } else {
                    currInpLine = currInput.split(" ");
                }
                if (currInpLine[0].equals("L")) {
                    if (!locationNames.containsKey(currInpLine[1])) {
                        locationNames.put(currInpLine[1],
                            tripGraph.add(new Location(currInpLine[1],
                                Float.parseFloat(currInpLine[2]),
                                Float.parseFloat(currInpLine[3]))));
                    }
                } else if (currInpLine[0].equals("R")) {
                    if (locationNames.containsKey(currInpLine[1])
                            && locationNames.containsKey(currInpLine[5])) {
                        if (currInpLine[4].equals("NS")) {
                            direc = "south";
                            reverse = "north";
                        } else if (currInpLine[4].equals("SN")) {
                            direc = "north";
                            reverse = "south";
                        } else if (currInpLine[4].equals("EW")) {
                            direc = "west";
                            reverse = "east";
                        } else if (currInpLine[4].equals("WE")) {
                            direc = "east";
                            reverse = "west";
                        }
                        tripGraph.add(locationNames.get(currInpLine[1]),
                            locationNames.get(currInpLine[5]),
                            new Distance(currInpLine[1], currInpLine[2],
                                Float.parseFloat(currInpLine[3]),
                                direc, currInpLine[5]));
                        tripGraph.add(locationNames.get(currInpLine[5]),
                            locationNames.get(currInpLine[1]),
                            new Distance(currInpLine[5], currInpLine[2],
                                Float.parseFloat(currInpLine[3]),
                                reverse, currInpLine[1]));
                    } else {
                        System.out.println("Invalid inputs");
                        System.exit(1);
                    }
                }
            }
        } catch (FileNotFoundException err) {
            System.out.println("File cannot be found");
            System.exit(1);
        }
    }

    /** Print a brief usage message and exit program abnormally. */
    private static void usage() {
        System.out.println("Entry point for the CS61B trip program."
            + " ARGS may contain options and targets:"
            + "     [ -m MAP ] [ -o OUT ] [ REQUEST ] "
            + "where MAP (default Map) contains the map data, OUT (default "
            + "standard output) takes the result, and REQUEST (default "
            + "standard input) contains the locations along the requested"
            + " trip.");
        System.exit(1);
    }

    /** ArrayList that holds all the distances found within the mapFile. */
    private static ArrayList<Distance> distances;

    /** ArrayList that holds all the locations found within the mapFile. */
    private static ArrayList<Location> locations;

    /** ArrayList that holds all the Vertices found within the graph. */
    private static ArrayList<Graph.Vertex> vertices;

    /** A HashMap that maps the string form of a location to its actual
     *  Location class instance variable. */
    private static HashMap<String,
        Graph<Location, Distance>.Vertex> locationNames;

    /** An undirected graph that contains locations and distances. */
    private static Graph<Location, Distance> tripGraph =
        new DirectedGraph<Location, Distance>();

    /** An ArrayList of strings that keeps track of the size of the current
     *  input line being read. */
    private static ArrayList<String> currInp = new ArrayList<String>();

    /** An int that keeps track of the current step number. */
    private static int currStep = 1;

    /** A String that holds the current input line from the map file. */
    private static String currInput;

    /** A boolean that is used in mapReaderHelper that is true iff it is time
     *  to increment the arrayList currInp by one. */
    private static boolean add = false;

}
