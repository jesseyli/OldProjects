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
 *  @author Jesse Li
 */
public final class Main {

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

    /** Print a trip for the request on the standard input to the standard
     *  output, using the map data in MAPFILENAME.
     */
    private static void trip(String mapFileName) {
        String request;
        String[] requestList;
        _locations = new HashMap<String,
            Graph<Location, Distance>.Vertex>();
        mapReader(mapFileName);
        Scanner inp = new Scanner(System.in);
        List<Graph<Location, Distance>.Edge> path;
        while (inp.hasNextLine()) {
            request = inp.nextLine();
            requestList = request.trim().split(",\\s+");
            for (int i = 0; i < requestList.length - 1; i += 1) {
                path = Graphs.shortestPath(_graph,
                        _locations.get(requestList[i]),
                        _locations.get(requestList[i + 1]),
                    DISTANCER);
                if (path == null) {
                    System.out.println("Not a valid request");
                    System.exit(1);
                }
                if (i == 0) {
                    System.out.printf("From %s:\n\n", requestList[i]);
                }
                Distance next;
                for (int j = 0; j < path.size(); j += 1) {
                    Distance curr = path.get(j).getLabel();
                    float combined = curr.getLength();
                    int k = j;
                    for (; k < path.size(); k += 1) {
                        if (k + 1 < path.size()) {
                            next = path.get(k + 1).getLabel();
                            if (next.getRoad().equals(curr.getRoad())
                                    && next.getDir().equals(curr.getDir())) {
                                combined = next.getLength() + combined;
                                j += 1;
                            } else {
                                break;
                            }
                        }
                    }
                    if (j == path.size() - 1) {
                        System.out.printf("%d. Take %s %s for %.1f miles to "
                            + "%s.\n", _step, curr.getRoad(),
                            curr.getDir(), combined, requestList[i + 1]);
                        _step += 1;
                        continue;
                    } else {
                        System.out.printf("%d. Take %s %s for %.1f miles.\n",
                            _step, curr.getRoad(),
                            curr.getDir(), combined);
                    }
                    _step += 1;
                }
            }
        }
        _step = 0;
        inp.close();
    }

    /** A distancer that uses the distance formula to find the distance
     *  between two locations. */
    public static final Distancer<Location> DISTANCER =
            new Distancer<Location>() {
                @Override
                public double dist(Location loc1, Location loc2) {
                    return Math.sqrt(Math.pow(loc2.getX()
                            - loc1.getX(), 2) + Math.pow(loc2.getY()
                                    - loc1.getY(), 2));
                }
            };

    /** Helper for mapReader that returns an input line from the reader
     *  INPUT. */
    private static String readerHelper(BufferedReader input) {
        String locationDirection = "";
        String result = "";
        boolean start = true;
        String temp = "";
        try {
            for (int currInt = input.read(); currInt != -1;) {
                temp = Character.toString((char) currInt);
                if (locationDirection.equals("L")) {
                    if (_currInp.size() == 4 && result.matches("L\\s+[\\p{Alnu"
                            + "m}_-]+\\s+-?[\\d.]+\\s+-?[\\d.]+")) {
                        return result.replace("\\s+", " ");
                    }
                    if (_currInp.size() > 4) {
                        throw new IOException();
                    }
                } else if (locationDirection.equals("R")) {
                    if (_currInp.size() == 6 && result.matches(
                            "R\\s+[\\p{Alnum}_-]+\\s+[\\p{Alnum}_-]+\\s+[\\d.]"
                            + "+\\s+[A-Z]+\\s+[\\p{Alnum}_-]+")) {
                        return result.replace("\\s+", " ");
                    }
                    if (_currInp.size() > 6) {
                        throw new IOException();
                    }
                } else if (!start) {
                    throw new IOException();
                }
                if (temp.matches("\\s")) {
                    if (result.equals("")) {
                        currInt = input.read();
                        continue;
                    } else {
                        if (add) {
                            _currInp.add(result);
                            add = false;
                        }
                        if ((locationDirection.equals("L") && _currInp.size()
                                == 4) || (locationDirection.equals("R")
                                        && _currInp.size() == 6)) {
                            continue;
                        } else {
                            result += " ";
                        }
                    }
                } else {
                    add = true;
                    if (start) {
                        locationDirection = temp;
                        start = false;
                    }
                    result += temp;
                }
                currInt = input.read();
            }
        } catch (IOException err) {
            System.exit(1);
        }
        return null;
    }

    /** Reads the file named FILE and saves the locations and distances found
     *  in that file into two ArrayLists. */
    private static void mapReader(String file) {
        String[] tempLine;
        String backward = "";
        String forward = "";
        try {
            FileReader reader = new FileReader(file);
            BufferedReader bReader = new BufferedReader(reader);
            while (true) {
                _currInp.clear();
                _input = readerHelper(bReader);
                if (_input != null) {
                    tempLine = _input.split(" ");
                } else {
                    return;
                }
                if (tempLine[0].equals("R")) {
                    if (_locations.containsKey(tempLine[1])
                            && _locations.containsKey(tempLine[5])) {
                        if (tempLine[4].equals("NS")) {
                            backward = "north";
                            forward = "south";
                        } else if (tempLine[4].equals("EW")) {
                            forward = "west";
                            backward = "east";
                        } else if (tempLine[4].equals("WE")) {
                            forward = "east";
                            backward = "west";
                        } else if (tempLine[4].equals("SN")) {
                            backward = "south";
                            forward = "north";
                        }
                        _graph.add(_locations.get(tempLine[5]),
                                _locations.get(tempLine[1]),
                            new Distance(tempLine[5], tempLine[2],
                                Float.parseFloat(tempLine[3]),
                                backward, tempLine[1]));
                        _graph.add(_locations.get(tempLine[1]),
                                _locations.get(tempLine[5]),
                            new Distance(tempLine[1], tempLine[2],
                                Float.parseFloat(tempLine[3]),
                                forward, tempLine[5]));
                    } else {
                        System.out.println("Invalid input");
                        System.exit(1);
                    }
                } else if (tempLine[0].equals("L")) {
                    if (!_locations.containsKey(tempLine[1])) {
                        _locations.put(tempLine[1],
                            _graph.add(new Location(tempLine[1],
                                Float.parseFloat(tempLine[2]),
                                Float.parseFloat(tempLine[3]))));
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
        System.out.println("CS61B trip program:  ARGS "
            + "should be in the format: [ -m MAP ] [ -o OUT ] [ REQUEST ] "
            + "where MAP contains map data, OUT receives the output, and "
            + "REQUEST contains the locations to be reached during the trip.");
        System.exit(1);
    }

    /** A class that holds a location read from a mapFile. */
    public static class Location implements Weightable {

        /** Creates a Location named NAME with x-coordinate X and its
         *  y-coordinate Y. */
        public Location(String name, float x, float y) {
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

        /** Returns the my y-location. */
        private float getY() {
            return _y;
        }

        /** Returns the my x-location. */
        private float getX() {
            return _x;
        }


        /** A location's weight. */
        private double _weight;

        /** My x-location. */
        private float _x;

        /** My y-location. */
        private float _y;

    }

    /** A Distance class that represents a road from a map file. */
    public static class Distance implements Weighted {

        /** Creates a Distance and initializes city _start to START,
         *  the road _road to ROAD, the length of the route _length to LENGTH,
         *  the direction of the route _direction to DIRECTION and the end
         *  city _end to END. */
        public Distance(String start, String road, float length,
                String direction, String end) {
            _start = start;
            _direction = direction;
            _road = road;
            _length = length;
            _end = end;
        }

        @Override
        public double weight() {
            return _length;
        }

        /** Returns my starting city. */
        public String getStart() {
            return _start;
        }

        /** Returns the road name for my route. */
        public String getRoad() {
            return _road;
        }

        /** Returns the length of my route. */
        public float getLength() {
            return _length;
        }

        /** Returns the direction of my route. */
        public String getDir() {
            return _direction;
        }

        /** Returns my ending city. */
        public String getEnd() {
            return _end;
        }

        /** My starting city. */
        private String _start;

        /** The name of the road between my cities. */
        private String _road;

        /** The distance between my starting city and my ending city. */
        private float _length;

        /** The direction of my route. */
        private String _direction;

        /** My ending city. */
        private String _end;

    }

    /** A directed graph that contains locations as vertices and distances as
     *  edges. */
    private static Graph<Location, Distance> _graph =
        new DirectedGraph<Location, Distance>();

    /** An ArrayList used to store the size of the current input line being
     *  read. */
    private static ArrayList<String> _currInp = new ArrayList<String>();

    /** A HashMap mapping the name of a location to the Vertex that represents
     *  it. */
    private static HashMap<String,
        Graph<Location, Distance>.Vertex> _locations;

    /** A boolean that is true iff the current input line needs to be
     *  increased. Initially false. */
    private static boolean add = false;

    /** A String storing the current input line from the file. */
    private static String _input;

    /** An integer that stores the current step number. */
    private static int _step = 1;

}
